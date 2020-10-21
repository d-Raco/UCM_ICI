package es.ucm.fdi.ici.c1920.practica4.grupo01;

import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

public class InputMsPacMan 
{
	private MsPacManDescription queryDesc;
	
	public MsPacManDescription getInput(Game game)
	{
		queryDesc = new MsPacManDescription();
        
		setMsPacMan(game);
        
        setGhosts(game);
		
        setPills(game);
        
        return queryDesc;
	}
	
	private void setMsPacMan (Game game)
	{
		queryDesc.setMsPacManLastMove(game.getPacmanLastMoveMade());
		queryDesc.setMsPacManNode(game.getPacmanCurrentNodeIndex());
	}
	
	private void setGhosts(Game game)
    {
        for (GHOST ghost : GHOST.values()) 
        { 
            switch(ghost.name())
            {
	            case "BLINKY":
	            	queryDesc.setBlinkyDistance(game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost)));
	            	queryDesc.setBlinkyEdible(game.getGhostEdibleTime(ghost) > 1);
	            	
	            	if(game.getGhostLairTime(ghost) == 0)
	            		queryDesc.setMoveToBlinky(game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade(), DM.PATH));
	            	
	            	else
	            		queryDesc.setMoveToBlinky(game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostInitialNodeIndex(), game.getPacmanLastMoveMade(), DM.PATH));
	            	
	            	break;
	            	
	            case "PINKY":
	            	queryDesc.setPinkyDistance(game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost)));
	            	queryDesc.setPinkyEdible(game.getGhostEdibleTime(ghost) > 1);
	            	
	            	if(game.getGhostLairTime(ghost) == 0)
	            		queryDesc.setMoveToPinky(game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade(), DM.PATH));
	            	
	            	else
	            		queryDesc.setMoveToPinky(game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostInitialNodeIndex(), game.getPacmanLastMoveMade(), DM.PATH));
	            	
	            	break;
	            	
	            case "INKY":
	            	queryDesc.setInkyDistance(game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost)));
	            	queryDesc.setInkyEdible(game.getGhostEdibleTime(ghost) > 1);

	            	if(game.getGhostLairTime(ghost) == 0)
	            		queryDesc.setMoveToInky(game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade(), DM.PATH));
	            	
	            	else
	            		queryDesc.setMoveToInky(game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostInitialNodeIndex(), game.getPacmanLastMoveMade(), DM.PATH));
	            	
	            	break;
	            	
	            case "SUE":
	            	queryDesc.setSueDistance(game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost)));
	            	queryDesc.setSueEdible(game.getGhostEdibleTime(ghost) > 1);
	            	
	            	if(game.getGhostLairTime(ghost) == 0)
	            		queryDesc.setMoveToSue(game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostCurrentNodeIndex(ghost), game.getPacmanLastMoveMade(), DM.PATH));
	            	
	            	else
	            		queryDesc.setMoveToSue(game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), game.getGhostInitialNodeIndex(), game.getPacmanLastMoveMade(), DM.PATH));
	            	
	            	break;
            }
        }
    }
	
    private void setPills(Game game) 
	{
        int[] pills = game.getActivePillsIndices();
        
        int pill1 = -1, pill2 = -1, pill3 = -1, pill4 = -1;
        
        double min_distance1 = Double.MAX_VALUE,
        		min_distance2 = Double.MAX_VALUE,
        		min_distance3 = Double.MAX_VALUE,
        		min_distance4 = Double.MAX_VALUE;
	
		for(int index : pills) 
		{
			double distance = game.getShortestPathDistance(game.getPacmanCurrentNodeIndex(), index);
			if(distance <= min_distance1) 
			{
				pill4 = pill3;
				pill3 = pill2;
				pill2 = pill1;
				pill1 = index;
				
				min_distance1 = distance;
			}
			else if(distance <= min_distance2)
			{
				pill4 = pill3;
				pill3 = pill2;
				pill2 = index;
				
				min_distance2 = distance;
				
			}
			else if(distance <= min_distance3)
			{
				pill4 = pill3;
				pill3 = index;
				
				min_distance3 = distance;
			}
			else if(distance <= min_distance4)
			{
				pill4 = index;
				
				min_distance4 = distance;
			}
		}
		
		queryDesc.setClosestPills1(pill1);
		queryDesc.setClosestPills2(pill2);
		queryDesc.setClosestPills3(pill3);
		queryDesc.setClosestPills4(pill4);
		
	}
    
}
