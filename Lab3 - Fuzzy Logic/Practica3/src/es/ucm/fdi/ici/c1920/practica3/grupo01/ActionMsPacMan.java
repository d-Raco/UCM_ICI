package es.ucm.fdi.ici.c1920.practica3.grupo01;

import java.util.Random;
import java.util.Vector;

import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class ActionMsPacMan 
{
	private int closest_ghost1, closest_ghost2, closest_ghost3, closest_ghost4;
	private Random rnd = new Random();
	
	//--------------------- HUIR ---------------------------
	
	public MOVE runAwayFromClosestGhost(Game game, MOVE moveToNearestPill, Vector<Integer> runAwayNodeVector, Vector<Double> runAwayDistancesVector,
											Vector<Integer> goToEdibleNodeVector, Vector<Double> goToEdibleDistancesVector, Vector<MOVE> ghostsLastMoveMadeVector) 
    {
    	getCloserGhosts(game, runAwayNodeVector, runAwayDistancesVector);
    	
    	
		if(closest_ghost1 != -1)
    	{
	    	MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
			
			//--------ESTABLECER LOS moveTowards SEGÚN EL FANTASMA NULL, SI HAY UNO
			MOVE moveTowards_1 = null, 
				moveTowards_2 = null, 
				moveTowards_3 = null, 
				moveTowards_4 = null;
			
			moveTowards_1 = game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), 
					runAwayNodeVector.get(closest_ghost1), MOVE.NEUTRAL, DM.PATH);
				
			if (closest_ghost2 != -1)
			{
				moveTowards_2 = game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), 
						runAwayNodeVector.get(closest_ghost2), MOVE.NEUTRAL, DM.PATH);
					
				if(closest_ghost3 != -1)
				{
					moveTowards_3 = game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), 
							runAwayNodeVector.get(closest_ghost3), MOVE.NEUTRAL, DM.PATH);
						
					if(closest_ghost4 != -1)
					{
						moveTowards_4 = game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), 
								runAwayNodeVector.get(closest_ghost4), MOVE.NEUTRAL, DM.PATH);
					}
				}
			}
			
			//---------GUARDAR EL moveTowards DEL FANTASMA EDIBLE
			
			MOVE moveTowardsEdible = runToEdibleGhost(game, moveToNearestPill,goToEdibleNodeVector, goToEdibleDistancesVector);
			
			//---------ALEJARSE
			MOVE move = null;
			
			boolean exit = false, 
					wayToPill_1 = false, wayToPill_2 = false, wayToPill_3 = false,
					wayToEdible_1 = false, wayToEdible_2 = false, wayToEdible_3 = false,
					escapeFromTwo = false, escapeFromThree = false, escapeFromFour = false;
			
			int i = 0,
				possibleMovesSize = possibleMoves.length;
			while(i < possibleMovesSize && !exit)
			{
				if(possibleMovesForFour(possibleMoves[i], ghostsLastMoveMadeVector, moveTowards_1, moveTowards_2, moveTowards_3, moveTowards_4))
				{
					escapeFromFour = true;
					
					move = possibleMoves[i];
					if (moveToNearestPill != null && move == moveToNearestPill)
						exit = true;
					
				}
				else if(!escapeFromFour && possibleMovesForThree(possibleMoves[i], ghostsLastMoveMadeVector, moveTowards_1, moveTowards_2, moveTowards_3))
				{
					escapeFromThree = true;
					
					if(!wayToPill_3 && !wayToEdible_3)
					{
						move = possibleMoves[i];
						if (moveToNearestPill != null && move == moveToNearestPill)
							wayToPill_3 = true;
					}
					
					if(moveTowardsEdible != null && moveTowardsEdible == possibleMoves[i])
					{
						move = possibleMoves[i];
						wayToEdible_3 = true;
					}
					
				}
				else if(!escapeFromFour && !escapeFromThree && possibleMovesForTwo(possibleMoves[i], ghostsLastMoveMadeVector, moveTowards_1, moveTowards_2))
				{
					escapeFromTwo = true;
					
					if(!wayToPill_2 && !wayToEdible_2)
					{
						move = possibleMoves[i];
						if (moveToNearestPill != null && move == moveToNearestPill)
							wayToPill_2 = true;
					}
					
					if(moveTowardsEdible != null && moveTowardsEdible == possibleMoves[i])
					{
						move = possibleMoves[i];
						wayToEdible_2 = true;
					}
				}
				else if(!escapeFromFour && !escapeFromThree && !escapeFromTwo && possibleMovesForOne(possibleMoves[i], ghostsLastMoveMadeVector, moveTowards_1))
				{
					if(!wayToPill_1 && !wayToEdible_1)
					{
						move = possibleMoves[i];
						if (moveToNearestPill != null && move == moveToNearestPill)
							wayToPill_1 = true;
					}
					
					if(moveTowardsEdible != null && moveTowardsEdible == possibleMoves[i])
					{
						move = possibleMoves[i];
						wayToEdible_1 = true;
					}
				}
				
				i++;
			}
				
			if(move == null)
				return game.getNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), runAwayNodeVector.get(closest_ghost1),game.getPacmanLastMoveMade(), DM.PATH);
			
			return move;
    	}
    	else
    	{
    		if(moveToNearestPill == null)
    		{
    			MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
    			
    			return possibleMoves[rnd.nextInt(possibleMoves.length)];
    		}
    		else
    			return moveToNearestPill;
    	}
    	
    }

	private boolean possibleMovesForOne(MOVE possibleMoves, Vector<MOVE> ghostsLastMoveMadeVector, MOVE moveTowards_1)
	{
		return possibleMoves != moveTowards_1 || possibleMoves == ghostsLastMoveMadeVector.get(closest_ghost1);
	}
	
	private boolean possibleMovesForTwo(MOVE possibleMoves, Vector<MOVE> ghostsLastMoveMadeVector, MOVE moveTowards_1, MOVE moveTowards_2)
	{
		return closest_ghost2 != -1 && 
				(possibleMoves != moveTowards_1 || possibleMoves == ghostsLastMoveMadeVector.get(closest_ghost1)) && 
				(possibleMoves != moveTowards_2 || possibleMoves == ghostsLastMoveMadeVector.get(closest_ghost2));
	}
	
	private boolean possibleMovesForThree(MOVE possibleMoves, Vector<MOVE> ghostsLastMoveMadeVector, MOVE moveTowards_1, MOVE moveTowards_2, MOVE moveTowards_3)
	{
		return closest_ghost3 != -1 && 
				(possibleMoves != moveTowards_1 || possibleMoves == ghostsLastMoveMadeVector.get(closest_ghost1)) && 
				(possibleMoves != moveTowards_2 || possibleMoves == ghostsLastMoveMadeVector.get(closest_ghost2)) && 
				(possibleMoves != moveTowards_3 || possibleMoves == ghostsLastMoveMadeVector.get(closest_ghost3));
	}
	
	private boolean possibleMovesForFour(MOVE possibleMoves, Vector<MOVE> ghostsLastMoveMadeVector, MOVE moveTowards_1, MOVE moveTowards_2, MOVE moveTowards_3, MOVE moveTowards_4)
	{
		return closest_ghost4 != -1 && 
				(possibleMoves != moveTowards_1 || possibleMoves == ghostsLastMoveMadeVector.get(closest_ghost1)) && 
				(possibleMoves != moveTowards_2 || possibleMoves == ghostsLastMoveMadeVector.get(closest_ghost2)) && 
				(possibleMoves != moveTowards_3 || possibleMoves == ghostsLastMoveMadeVector.get(closest_ghost3)) && 
				(possibleMoves != moveTowards_4 || possibleMoves == ghostsLastMoveMadeVector.get(closest_ghost4));
	}
	
	private void getCloserGhosts(Game game, Vector<Integer> runAwayNodeVector, Vector<Double> runAwayDistancesVector)
    {
    	double min_distance1 = Constants.PACMAN_MAX_DISTANCE,
    			min_distance2 = Constants.PACMAN_MAX_DISTANCE,
    			min_distance3 = Constants.PACMAN_MAX_DISTANCE,
    			min_distance4 = Constants.PACMAN_MAX_DISTANCE;
    	
    	closest_ghost1 = closest_ghost2 = closest_ghost3 = closest_ghost4 = -1;
    	
    	int size = runAwayNodeVector.size();
        for (int i = 0; i < size; i++) 
        {
            double distance = runAwayDistancesVector.get(i);
            
            if(distance < min_distance1)
    		{
    			min_distance4 = min_distance3;
    			min_distance3 = min_distance2;
    			min_distance2 = min_distance1;
    			min_distance1 = distance;
    			
    			closest_ghost4 = closest_ghost3;
    			closest_ghost3 = closest_ghost2;
    			closest_ghost2 = closest_ghost1;
    			closest_ghost1 = i;
    		}
    		else if(distance < min_distance2)
    		{
    			min_distance4 = min_distance3;
    			min_distance3 = min_distance2;
    			min_distance2 = distance;
    			
    			closest_ghost4 = closest_ghost3;
    			closest_ghost3 = closest_ghost2;
    			closest_ghost2 = i;
    		}
    		else if(distance < min_distance3)
    		{
    			min_distance4 = min_distance3;
    			min_distance3 = distance;
    			
    			closest_ghost4 = closest_ghost3;
    			closest_ghost3 = i;
    		}
    		else if(distance < min_distance4)
    		{
    			min_distance4 = distance;
    			
    			closest_ghost4 = i;
    		}
        }
    }
	
	//------------------------- PERSEGUIR --------------------------
	
	public MOVE runToEdibleGhost(Game game, MOVE moveToNearestPill, Vector<Integer> goToEdibleNodeVector, Vector<Double> goToEdibleDistancesVector)
    {
		MOVE move = null;
		
		double min_distancia = Double.MAX_VALUE;
		boolean wayToPill = false;
		
		int size = goToEdibleNodeVector.size();
		for(int i = 0; i < size; i++)
		{
			if(!wayToPill)
			{
				min_distancia = goToEdibleDistancesVector.get(i);
				
				move = game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), goToEdibleNodeVector.get(i), 
						game.getPacmanLastMoveMade(), DM.PATH);
				
				if(moveToNearestPill != null && move == moveToNearestPill)
					wayToPill = true;
			}
			else
			{
				MOVE moveTowards = game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), goToEdibleNodeVector.get(i), 
						game.getPacmanLastMoveMade(), DM.PATH);
				
				if(moveTowards == moveToNearestPill)
				{
					min_distancia = goToEdibleDistancesVector.get(i);
					move = moveTowards;
				}
			}
		}
		
		return move;
    }

	
	//------------------------- IR A POR PILLS --------------------------
	
	public MOVE runToPills(Game game, MOVE moveToNearestPill)
	{
		if(moveToNearestPill == null)
		{
			MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
			
			return possibleMoves[rnd.nextInt(possibleMoves.length)];
		}
		else
			return moveToNearestPill;
	}
}
