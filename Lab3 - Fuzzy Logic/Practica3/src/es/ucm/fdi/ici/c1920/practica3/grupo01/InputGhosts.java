package es.ucm.fdi.ici.c1920.practica3.grupo01;

import java.util.HashMap;

import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class InputGhosts 
{	
	private double time_lastMove = System.nanoTime() / 1000000000;
	
	private HashMap<String, Double> input;
	private Quadrant q;
	private int lastNodePacman, ghostIndex;
	private double lastTimeSawPacman;
	private boolean[] gotToPacmanNode = new boolean[4];
	private GHOST closestGhost;
	
	private double distanceFromQuadrantX, distanceFromQuadrantY;
	
	public void resetInput(Game game, GHOST ghost)
	{
		lastNodePacman = -1;
		lastTimeSawPacman = Constants.GHOSTS_MAX_TIME_LAST_SAW;
			
		gotToPacmanNode[ghostIndex] = false;
        
	}
	
	public HashMap<String,Double> getInput(Game game, GHOST ghost, Quadrant[] quadrants)
	{
		input = new HashMap<String,Double>();
		
	    int pacmanNode = game.getPacmanCurrentNodeIndex();
		float time = System.nanoTime() / 1000000000;
		
		if(game.wasPacManEaten())
			resetInput(game, ghost);
		
		double distance = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), pacmanNode);
		double distanceToPacman;
		

		if(distance != 0) // With PO visibility non observable ghosts distance is 0.
		{
			distanceToPacman = distance;
			lastNodePacman = pacmanNode;
			lastTimeSawPacman = 0;
			quadrants[Quadrant.PACMAN].setNodeIndex(lastNodePacman);
			quadrants[Quadrant.PACMAN].setX(game.getNodeXCood(lastNodePacman));
			quadrants[Quadrant.PACMAN].setY(game.getNodeYCood(lastNodePacman));
			
			gotToPacmanNode[ghostIndex] = false;
		}
		else
		{
			distanceToPacman = Constants.GHOSTS_MAX_DISTANCE;
			
			if(lastTimeSawPacman < Constants.GHOSTS_MAX_TIME_LAST_SAW && ghost.name().equals("BLINKY"));
				lastTimeSawPacman += time - time_lastMove;
				
			time_lastMove = time;
		}
		q = setQuadrant(game, ghost, quadrants);//***********
		
		if(game.getGhostCurrentNodeIndex(ghost) == pacmanNode)
			gotToPacmanNode[ghostIndex] = true;
		
		// ESTO SIRVE PARA VER COMO DE LEJOS ESTAS DE PACMAN. SI ESTAS MUY CERCA TE AVISA CUANDO PROCESES EL OUTPUT DE ABAJO (goToPacman).
		input.put("PACMAN_distance", distanceToPacman);
		// ESTO SIRVE PARA VER COMO DE LEJOS ESTAS DE TU AREA. SI ESTAS MUY LEJOS TE AVISA CUANDO PROCESES EL OUTPUT DE ABAJO (stayInArea).
        input.put("GHOST_AREA_X_distance", distanceFromQuadrantX);
        input.put("GHOST_AREA_Y_distance", distanceFromQuadrantY);
        input.put("reliable_POSITION", lastTimeSawPacman);
		
        return input;
	}
		
	private Quadrant setQuadrant(Game game, GHOST ghost, Quadrant[] quadrants)
	{
		distanceFromQuadrantX = -1;
		distanceFromQuadrantY = -1;
		Quadrant q = null;

		if(lastTimeSawPacman < Constants.GHOSTS_MAX_TIME_LAST_SAW) 
		{
			q = quadrants[Quadrant.PACMAN];
			distanceFromQuadrantX = Math.abs(game.getNodeXCood(game.getGhostCurrentNodeIndex(ghost)) - q.getX());
			distanceFromQuadrantY = Math.abs(game.getNodeYCood(game.getGhostCurrentNodeIndex(ghost)) - q.getY());
		}
		else
		{
			switch(ghost.name()) 
			{
			case "BLINKY":
				q = quadrants[Quadrant.BLINKY];
				distanceFromQuadrantX = Math.abs(game.getNodeXCood(game.getGhostCurrentNodeIndex(ghost)) - q.getX());
				distanceFromQuadrantY = Math.abs(game.getNodeYCood(game.getGhostCurrentNodeIndex(ghost)) - q.getY());
				ghostIndex = 0;
				break;
			case "PINKY":
				q = quadrants[Quadrant.PINKY];
				distanceFromQuadrantX = Math.abs(game.getNodeXCood(game.getGhostCurrentNodeIndex(ghost)) - q.getX());
				distanceFromQuadrantY = Math.abs(game.getNodeYCood(game.getGhostCurrentNodeIndex(ghost)) - q.getY());
				ghostIndex = 1;
				break;
			case "INKY":
				q = quadrants[Quadrant.INKY];
				distanceFromQuadrantX = Math.abs(game.getNodeXCood(game.getGhostCurrentNodeIndex(ghost)) - q.getX());
				distanceFromQuadrantY = Math.abs(game.getNodeYCood(game.getGhostCurrentNodeIndex(ghost)) - q.getY());
				ghostIndex = 2;
				break;
			case "SUE":
				q = quadrants[Quadrant.SUE];
				distanceFromQuadrantX = Math.abs(game.getNodeXCood(game.getGhostCurrentNodeIndex(ghost)) - q.getX());
				distanceFromQuadrantY = Math.abs(game.getNodeYCood(game.getGhostCurrentNodeIndex(ghost)) - q.getY());
				ghostIndex = 3;
				break;
			}
		}
		
		return q;
	}
	
	private GHOST getClosestGhost(Game game, GHOST ghost)
	{
		closestGhost = null;
		int distance, closeDistance = 15;
		
		for(GHOST ghostType : GHOST.values()) 
		{
			if(ghost != ghostType && game.getGhostLairTime(ghostType) == 0) 
			{
				distance = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghost), 
						game.getGhostCurrentNodeIndex(ghostType));
				if (distance <= closeDistance) {
					closestGhost = ghostType;
					closeDistance = distance;
				}
			}
		}
		
		return closestGhost;
	}

	
	public boolean needToSeparate(Game game, GHOST ghost) {
		boolean separate = false;
		GHOST closestGhost = getClosestGhost(game, ghost);
		if(closestGhost != null && ((game.isGhostEdible(ghost) && game.isGhostEdible(closestGhost)) || (!game.isGhostEdible(ghost) && !game.isGhostEdible(closestGhost))))
			separate = true;
		return separate;
	}
	
	public Quadrant getQuadrant() { return q; }
	
	public boolean hasArrivedToPacmanNode() { return gotToPacmanNode[ghostIndex]; }
	
	public int getLastNodePacman() {return lastNodePacman;}
	
	public GHOST getClosestGhost() {return closestGhost;}
}
