package es.ucm.fdi.ici.c1920.practica3.grupo01;

import pacman.game.Constants.MOVE;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;

import java.util.HashMap;
import java.util.Vector;

import pacman.game.Game;

public class InputMsPacMan 
{
	private MOVE[] ghostsLastMoveMade = {MOVE.NEUTRAL, MOVE.NEUTRAL, MOVE.NEUTRAL, MOVE.NEUTRAL};
	
	private double[] distancesNoEdibles = {Constants.PACMAN_MAX_DISTANCE, Constants.PACMAN_MAX_DISTANCE, Constants.PACMAN_MAX_DISTANCE, Constants.PACMAN_MAX_DISTANCE};
	private double[] distancesEdibles = {Constants.PACMAN_MAX_DISTANCE, Constants.PACMAN_MAX_DISTANCE, Constants.PACMAN_MAX_DISTANCE, Constants.PACMAN_MAX_DISTANCE};
	private double[] lastTimeSaw = {Constants.PACMAN_MAX_TIME_LAST_SAW, Constants.PACMAN_MAX_TIME_LAST_SAW, Constants.PACMAN_MAX_TIME_LAST_SAW, Constants.PACMAN_MAX_TIME_LAST_SAW};

	private int[] nodesGhosts = {-1, -1, -1, -1};
	
	private boolean[] edibles = {false, false, false, false};
	

	private double time_lastMove = System.nanoTime() / 1000000000;
	
	private int initialGhostNode;
	private int nearestPill;
	
	private MOVE moveToNearestPill = null;

	Vector<Integer> pillsRegistered = new Vector<>();
	
	HashMap<String, Double> input;
	

	
	public HashMap<String,Double> getInput(Game game)
	{
		input = new HashMap<String,Double>();
		
		initialGhostNode = game.getGhostInitialNodeIndex();
		
		if(game.getCurrentLevelTime() == 0)
			pillsRegistered.clear();

		//Si nos hemos comido una pill, quitamos esta pill del vector de pills avistadas
        removeEatenPill(game);
        
        //Cogemos el movimiento a la siguiente pill avistada más cercana
        moveToNearestPill = goToPills(game);
        
        //Mirar distancias de los fantasmas
        updateInput(game);
        
        return input;
	}
	
    private void removeEatenPill(Game game)
    {
        if(game.wasPillEaten())
        {
        	int i = 0, size = pillsRegistered.size();
        	while(i < size && pillsRegistered.get(i) != nearestPill)
        		i++;
        	
        	if(i < size) //Por si acaso ha avido un error y no nos hemos guardado una que no hemos registrado.
        		pillsRegistered.removeElementAt(i);
        }
    }
	
    private MOVE goToPills(Game game) 
	{
        int[] pills = game.getActivePillsIndices();
        
        double min_distance = Double.MAX_VALUE;
        
        nearestPill = -1;
	
		for(int index : pills) 
		{
			// REGISTRAR LA PILL AVISTADA
			int i = 0, size = pillsRegistered.size();
			boolean salir = false;
			while(i < size && !salir)
			{
				if(pillsRegistered.get(i) == index)
					salir = true;
				i++;
			}
			
			if(!salir)
				pillsRegistered.add(index);
			
			// COMPROBAR DISTANCIAS ENTRE LAS QUE VEMOS
			double distance = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), index);
			if(distance <= min_distance) 
			{
				nearestPill = index;
				min_distance = distance;
			}
		}
		
		if(nearestPill != -1)
			return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), nearestPill, 
															game.getPacmanLastMoveMade(), DM.PATH);
		else if (pillsRegistered.size() == 0)
			return null;
		else
		{
			for(int index : pillsRegistered) 
			{
				// COMPROBAR DISTANCIAS ENTRE LAS QUE TENEMOS REGISTRADAS
				double distance = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), index);
				if(distance <= min_distance) 
				{
					nearestPill = index;
					min_distance = distance;
				}
			}
			
			return game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), nearestPill, 
															game.getPacmanLastMoveMade(), DM.PATH);
		}
	}
    
    private void updateInput(Game game)
    {
    	float time = System.nanoTime() / 1000000000;
    	
    		
        for (GHOST ghost : GHOST.values()) 
        {
    		//Reiniciamos toda la informacion de los fantasmas si MsPacMan muere o si se come una Power Pill
            if(game.wasPacManEaten() || game.getCurrentLevelTime() == 0)
            {
            	ghostsLastMoveMade[ghost.ordinal()] = MOVE.NEUTRAL;
            	
            	nodesGhosts[ghost.ordinal()] = initialGhostNode;
            	lastTimeSaw[ghost.ordinal()] = 0;
            	
            	distancesNoEdibles[ghost.ordinal()] = Constants.PACMAN_MAX_DISTANCE;
            	distancesEdibles[ghost.ordinal()] = Constants.PACMAN_MAX_DISTANCE;
        		edibles[ghost.ordinal()] = false;
            }
            else if(game.wasPowerPillEaten() && lastTimeSaw[ghost.ordinal()] < Constants.PACMAN_MAX_TIME_LAST_SAW && !edibles[ghost.ordinal()])
            {
	            distancesEdibles[ghost.ordinal()] = distancesNoEdibles[ghost.ordinal()];
	            distancesNoEdibles[ghost.ordinal()] = Constants.PACMAN_MAX_DISTANCE;
	            		
	            edibles[ghost.ordinal()] = true;
	            lastTimeSaw[ghost.ordinal()] = 0;
            	
            	ghostsLastMoveMade[ghost.ordinal()] = MOVE.NEUTRAL;
            }
            else
            {
	            if(game.wasGhostEaten(ghost))
	            {
	            	ghostsLastMoveMade[ghost.ordinal()] = MOVE.NEUTRAL;
	            	
	            	nodesGhosts[ghost.ordinal()] = initialGhostNode;
	            	lastTimeSaw[ghost.ordinal()] = 1;
	            	
	            	distancesNoEdibles[ghost.ordinal()] = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), initialGhostNode);
	            	distancesEdibles[ghost.ordinal()] = Constants.PACMAN_MAX_DISTANCE;
	            	
	        		edibles[ghost.ordinal()] = false;
	            }
	            else
	            {
		            double distance = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost));
		            
		            if(distance != 0) // Estamos viendo al fantasma
		            {
		            	nodesGhosts[ghost.ordinal()] = game.getGhostCurrentNodeIndex(ghost);
		            	lastTimeSaw[ghost.ordinal()] = 0;
		            	
		            	if(game.isGhostEdible(ghost))
		            	{
		            		distancesEdibles[ghost.ordinal()] = distance;
		            		edibles[ghost.ordinal()] = true;
		            	}
		            	else
		            	{
		            		distancesNoEdibles[ghost.ordinal()] = distance;
		            		ghostsLastMoveMade[ghost.ordinal()] = game.getGhostLastMoveMade(ghost);
		            	}
		            	
		            }
		            else
		            {
			            if(lastTimeSaw[ghost.ordinal()] >= Constants.PACMAN_MAX_TIME_LAST_SAW)
			            {
			            	ghostsLastMoveMade[ghost.ordinal()] = MOVE.NEUTRAL;
			            	
			            	nodesGhosts[ghost.ordinal()] = -1;
			            	lastTimeSaw[ghost.ordinal()] = Constants.PACMAN_MAX_TIME_LAST_SAW;
			            	
			            	distancesNoEdibles[ghost.ordinal()] = Constants.PACMAN_MAX_DISTANCE;
			            	distancesEdibles[ghost.ordinal()] = Constants.PACMAN_MAX_DISTANCE;
			            	
		            		edibles[ghost.ordinal()] = false;
			            }
			            else
			            	lastTimeSaw[ghost.ordinal()] += time - time_lastMove;
		            }
	            }
            }
            
            
            input.put(ghost.name()+"_RA_distance", distancesNoEdibles[ghost.ordinal()]);
            input.put(ghost.name()+"_GTE_distance", distancesEdibles[ghost.ordinal()]);
            input.put("reliable_"+ghost.name(), lastTimeSaw[ghost.ordinal()]);
        }
        
        time_lastMove = time;
        
        //System.out.println("Distancias no edibles: " + Arrays.toString(distancesNoEdibles));
        //System.out.println("Distancias edibles: " + Arrays.toString(distancesEdibles));
    }
    
  
    public MOVE getMoveToNearestPill() { return moveToNearestPill; }
    public MOVE[] getGhostsLastMoveMade() { return ghostsLastMoveMade; }
    public boolean[] getEdiblesArray() { return edibles; }
    public double[] getDistancesEdiblesArray() { return distancesEdibles; }
    public double[] getDistancesNoEdiblesArray() { return distancesNoEdibles; }
    public int[] getNodesArray() { return nodesGhosts; }
}
