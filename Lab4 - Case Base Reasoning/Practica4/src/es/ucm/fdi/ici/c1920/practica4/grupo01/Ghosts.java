package es.ucm.fdi.ici.c1920.practica4.grupo01;

import java.util.EnumMap;

import es.ucm.fdi.ici.c1920.practica4.grupo01.Constants.CASE_TYPE;
import pacman.controllers.GhostController;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;
import ucm.gaia.jcolibri.cbrcore.CBRQuery;

public class Ghosts extends GhostController 
{
	int lastPoints = 0;
	
	MOVE move = MOVE.NEUTRAL;
	
	private InputGhosts inputClass = new InputGhosts();
	private OutputGhosts outputClass = new OutputGhosts();
	
	private CBRApplicationGhosts CBR_Ghosts = new CBRApplicationGhosts(this);

	CBRQuery query;
	
	public void preCompute(String opponent) 
	{
    	System.out.println("Precompute. Oponent: " + opponent);
    	
		try
		{
			CBR_Ghosts.configure();
			CBR_Ghosts.preCycle();
		}
		catch(Exception e)
		{
			System.out.println("Ghosts preCompute() :" + e);
		}
	}
	
	public void postCompute()
	{
    	System.out.println("Postcompute");
    	
		try
		{
			CBR_Ghosts.postCycle();
			
		} catch(Exception e)
		{
			System.out.println("Ghosts postCompute():" + e);
		}
	}
	
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) 
	{ 
		CBR_Ghosts.setGame(game);
		
		EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
		
		for(GHOST ghost : GHOST.values())
		{
			move = MOVE.NEUTRAL;
			
	    	int diffPoints = game.getScore() - lastPoints;
	    	lastPoints = game.getScore();
	    	
			if(game.wasPacManEaten() && ghost.name() == "BLINKY")
				CBR_Ghosts.MsPacManDead(diffPoints);
				
			if(game.getPossibleMoves(game.getGhostCurrentNodeIndex(ghost), game.getGhostLastMoveMade(ghost)).length > 1)
				checkGhosts(ghost, game, diffPoints);
			
			moves.put(ghost, move);
		}
			
		return moves;
	}
	
	private void checkGhosts(GHOST ghost, Game game, int diffPoints)
	{
		GhostsDescription queryDesc = inputClass.getInput(ghost, game);
	    
		if(game.isGhostEdible(ghost))
			queryDesc.setType(CASE_TYPE.RUN);
		else
			queryDesc.setType(CASE_TYPE.CHASE);

	    query = new CBRQuery();
	    query.setDescription(queryDesc);
	    
    	try
    	{
    		CBR_Ghosts.setGhost(ghost);
    		CBR_Ghosts.cycle(query);
    		CBR_Ghosts.updateBuffer(diffPoints);
    	}
    	catch(Exception e) 
    	{
			System.out.println("Ghosts checkGhosts() with ghost " + ghost.name() + " :" + e);
    	}
	}
	
	public void setSolution(MOVE m) { move = m; }
    public int getLastScore() {return lastPoints;}
}
