package es.ucm.fdi.ici.c1920.practica4.grupo01;

import es.ucm.fdi.ici.c1920.practica4.grupo01.Constants.CASE_TYPE;
import pacman.controllers.PacmanController;
import pacman.game.Constants.MOVE;
import ucm.gaia.jcolibri.cbrcore.CBRQuery;
import pacman.game.Game;


public final class MsPacMan extends PacmanController 
{
	int lastPoints = 0;
	
	MOVE move = MOVE.NEUTRAL;
	
	private InputMsPacMan inputClass = new InputMsPacMan();
	private OutputMsPacMan outputClass = new OutputMsPacMan();
	
	private CBRApplicationMsPacMan CBR_MsPacMan = new CBRApplicationMsPacMan(this);	
	
	CBRQuery query;
	
	public void preCompute(String opponent) 
	{
    	System.out.println("Precompute. Oponent: " + opponent);
    	
		try
		{
			CBR_MsPacMan.configure();
			CBR_MsPacMan.preCycle();

		} catch(Exception e)
		{
			System.out.println("MsPacMan preCompute():" + e);
		}
		
	}
	
	public void postCompute()
	{
    	System.out.println("Postcompute");
    	
		try
		{
			CBR_MsPacMan.postCycle();
			
		} catch(Exception e)
		{
			System.out.println("MsPacMan postCompute():" + e);
		}
	}
	
    @Override
    public MOVE getMove(Game game, long timeDue) 
    {
    	move = MOVE.NEUTRAL;
    	
    	int diffPoints = game.getScore() - lastPoints;
    	lastPoints = game.getScore();
    	
    	if(game.wasPacManEaten())
    		CBR_MsPacMan.MsPacManDead(diffPoints);
    	
    	if(game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade()).length > 1)
    	{
    		CBR_MsPacMan.setGame(game);
    		
    		MsPacManDescription queryDesc = inputClass.getInput(game);
    		
	        outputClass.processOutput(queryDesc);
			
			if(outputClass.isRunning())
				queryDesc.setType(CASE_TYPE.RUN);
			
			else if(!outputClass.isChasing())
				queryDesc.setType(CASE_TYPE.CHASE);
			
			else
				queryDesc.setType(CASE_TYPE.NEUTRAL);

    		query = new CBRQuery();
    		query.setDescription(queryDesc);
    		
    		try
    		{
    			CBR_MsPacMan.cycle(query);
    			CBR_MsPacMan.updateBuffer(diffPoints);
    			
    		} catch(Exception e) 
    		{
    			System.out.println("MsPacMan getMove():" + e);
    		}

    		return move;
    	}

		return move;
    }
    
    public void setSolution(MOVE m) { move = m; }
    public int getLastScore() {return lastPoints;}
}