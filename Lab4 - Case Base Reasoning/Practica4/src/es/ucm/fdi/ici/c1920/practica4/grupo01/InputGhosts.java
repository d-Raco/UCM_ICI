package es.ucm.fdi.ici.c1920.practica4.grupo01;

import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class InputGhosts 
{
	private GhostsDescription queryDesc;
	
	public GhostsDescription getInput(GHOST gh, Game game)
	{
		queryDesc = new GhostsDescription();

		setMsPacMan(game);

        setGhosts(game, gh);
        
		return queryDesc;
	}
	

	private void setMsPacMan (Game game)
	{
		queryDesc.setMsPacManLastMove(game.getPacmanLastMoveMade());
		queryDesc.setNodeMsPacMan(game.getPacmanCurrentNodeIndex());
	}
	
	private void setGhosts(Game game, GHOST ghost)
    {	
		queryDesc.setName(ghost.name());

		for(GHOST ghostType : GHOST.values())
		{			
			switch(ghostType.name())
			{
			case "BLINKY":
				queryDesc.setDistanceGhost1(game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostType)));
		        queryDesc.setEdibleGhost1(game.getGhostEdibleTime(ghostType) > 1);
		        
		        
		        if(game.getGhostLairTime(ghost) == 0)
		        	queryDesc.setNodeGhost1(game.getGhostCurrentNodeIndex(ghostType));
		        else
            		queryDesc.setNodeGhost1(game.getGhostInitialNodeIndex());
	        	
		        break;
	        
			case "PINKY":
				queryDesc.setDistanceGhost2(game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostType)));
		        queryDesc.setEdibleGhost2(game.getGhostEdibleTime(ghostType) > 1);

		        if(game.getGhostLairTime(ghost) == 0)
		        	queryDesc.setNodeGhost2(game.getGhostCurrentNodeIndex(ghostType));
		        else
            		queryDesc.setNodeGhost2(game.getGhostInitialNodeIndex());
		        
		        break;
	        
			case "INKY":
				queryDesc.setDistanceGhost3(game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostType)));
		        queryDesc.setEdibleGhost3(game.getGhostEdibleTime(ghostType) > 1);
		        
		        if(game.getGhostLairTime(ghost) == 0)
		        	queryDesc.setNodeGhost3(game.getGhostCurrentNodeIndex(ghostType));
		        else
            		queryDesc.setNodeGhost3(game.getGhostInitialNodeIndex());
		        
		        break;
	        
		    default:
		    	queryDesc.setDistanceGhost4(game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghostType)));
		        queryDesc.setEdibleGhost4(game.getGhostEdibleTime(ghostType) > 1);
		        
		        if(game.getGhostLairTime(ghost) == 0)
		        	queryDesc.setNodeGhost4(game.getGhostCurrentNodeIndex(ghostType));
		        else
            		queryDesc.setNodeGhost4(game.getGhostInitialNodeIndex());
			}
		}	   
	}

}
