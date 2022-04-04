package es.ucm.fdi.ici.c1920.practica4.grupo01;

import java.util.*;
import pacman.controllers.PacmanController;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class MsPacManAprender extends PacmanController
{
	
	private int rangeEdible = 50,
			rangeEdibleChoice = 15,
			rangeChasing = 60;
	
	private int numGhostInRange = 0;
	
	private int pacman,
			initialGhost = -1;
	
	private MOVE pacman_lastMove = null;
	
	private MOVE nearestPill = null;
	
	private GHOST chasingGhost = null,
			chasingGhost_2 = null,
			chasingGhost_3 = null,
			chasingGhost_4 = null,
			edibleGhost = null,
			edibleGhostChased = null;
	
	Vector<GHOST> ediblesVector = new Vector<GHOST>(4);
	
	
	@Override
	public MOVE getMove(Game game, long timeDue) 
	{
		float time = System.currentTimeMillis() % 1000;
		
		pacman = game.getPacmanCurrentNodeIndex();
		pacman_lastMove = game.getPacmanLastMoveMade();
		
		getNearestChasingAndEdibleGhost(game);
		
		nearestPill = moveTowardsNearestPill(game);

		if(numGhostInRange != 0)
		{
			//System.out.println("pacman huir: " + (System.currentTimeMillis()  % 1000 - time));
			return moveAwayFromGhosts(game);
		}
		
		if(edibleGhost != null)
		{
			//System.out.println("pacman perseguir: " + (System.currentTimeMillis() % 1000 - time));
			return moveTowardsEdible(game);
		}
		
		//System.out.println("pacman pill: " + (System.currentTimeMillis() % 1000 - time));
		return nearestPill;
	}
	
	private void getNearestChasingAndEdibleGhost(Game game) 
	{
		int shorterPath = rangeChasing,
			shorterPath_2 = rangeChasing,
			shorterPath_3 = rangeChasing,
			shorterPath_4 = rangeChasing;
		
		int shorterEdiblePath = rangeEdible;
		int shorterEdibleChased = rangeEdible;
		
		int initialGhostNode = game.getGhostInitialNodeIndex();
		
		chasingGhost = null; chasingGhost_2 = null; chasingGhost_3 = null; chasingGhost_4 = null;
		edibleGhost = null; edibleGhostChased = null; 
		initialGhost = -1;
		numGhostInRange = 0;
		
		for(GHOST ghostType : GHOST.values())
		{
			int time = game.getGhostLairTime(ghostType);
			int pathDistance;
			
			if(time == 0)	
				pathDistance = game.getShortestPathDistance(pacman, game.getGhostCurrentNodeIndex(ghostType));
			else
				pathDistance = game.getShortestPathDistance(pacman, initialGhostNode);
			
			if(!game.isGhostEdible(ghostType)) //Coger el fantasma normal más cercano
			{
				if (pathDistance < shorterPath) 
				{
					shorterPath_4 = shorterPath_3;
					shorterPath_3 = shorterPath_2;
					shorterPath_2 = shorterPath;
					shorterPath = pathDistance;
					
					chasingGhost_4 = chasingGhost_3;
					chasingGhost_3 = chasingGhost_2;
					chasingGhost_2 = chasingGhost;
					
					if(time != 0)
					{
						initialGhost = initialGhostNode;
						chasingGhost = null;
					}
					
					else
						chasingGhost = ghostType;
					
					numGhostInRange++;
				}
				else if (pathDistance < shorterPath_2)
				{
					shorterPath_4 = shorterPath_3;
					shorterPath_3 = shorterPath_2;
					shorterPath_2 = pathDistance;
					
					chasingGhost_4 = chasingGhost_3;
					chasingGhost_3 = chasingGhost_2;
					
					if(time != 0)
					{
						initialGhost = initialGhostNode;
						chasingGhost_2 = null;
					}
					else
						chasingGhost_2 = ghostType;
					
					numGhostInRange++;
				}
				else if (pathDistance < shorterPath_3)
				{
					shorterPath_4 = shorterPath_3;
					shorterPath_3 = pathDistance;
					
					chasingGhost_4 = chasingGhost_3;
					
					if(time != 0)
					{
						initialGhost = initialGhostNode;
						chasingGhost_3 = null;
					}
					else
						chasingGhost_3 = ghostType;
					
					numGhostInRange++;
				}
				else if (pathDistance < shorterPath_4)
				{
					shorterPath_4 = pathDistance;
					
					if(time != 0)
					{
						initialGhost = initialGhostNode;
						chasingGhost_4 = null;
					}
					else
						chasingGhost_4 = ghostType;
					
					numGhostInRange++;
				}
			}
			else //Coger el fantasma comestible más cercano
			{
				if (pathDistance < rangeEdibleChoice)
					ediblesVector.addElement(ghostType);
				
				if (pathDistance < shorterEdiblePath)
				{
					shorterEdiblePath = pathDistance;
					edibleGhost = ghostType;
						
					if(pathDistance < shorterEdibleChased)
					{
							shorterEdibleChased = pathDistance;
							edibleGhostChased = ghostType;
					}
				}
			}	
		}
	}
	
	private MOVE moveTowardsNearestPill(Game game) 
	{
		int[] pillIndices = game.getActivePillsIndices();
		
		int distance,
			min = 100000, 
			nearestPill = 0;
		
		for(int index : pillIndices) 
		{
			distance = game.getShortestPathDistance(pacman, index);
			if(distance <= min) 
			{
				nearestPill = index;
				min = distance;
			}
		}
		
		if (chasingGhost != null || chasingGhost_2 != null)
		{
			pillIndices = game.getActivePowerPillsIndices();
			min += 40;
			for(int index : pillIndices) 
			{
				distance = game.getShortestPathDistance(pacman, index);
				if(distance <= min) 
				{
					nearestPill = index;
					min = distance;
				}
			}
		}
		
		if(chasingGhost != null)
			return game.getApproximateNextMoveTowardsTarget(pacman, nearestPill, MOVE.NEUTRAL, DM.PATH);
		
		return game.getApproximateNextMoveTowardsTarget(pacman, nearestPill, pacman_lastMove, DM.PATH);
	}

	private MOVE moveAwayFromGhosts(Game game)
	{
		MOVE[] possibleMoves = game.getPossibleMoves(pacman, pacman_lastMove);
		
		//--------VER EL FANTASMA QUE ESTA A NULL SI HAY UNO EN LA BASE
		boolean ghostNull_1 = false,
				ghostNull_2 = false,
				ghostNull_3 = false,
				ghostNull_4 = false;
		
		if(initialGhost != -1)
		{
			if (chasingGhost == null) ghostNull_1 = true;
			
			if (chasingGhost_2 == null && numGhostInRange >= 2) ghostNull_2 = true;
		
			if (chasingGhost_3 == null && numGhostInRange >= 3) ghostNull_3 = true;
			
			if (chasingGhost_4 == null && numGhostInRange >= 4) ghostNull_4 = true;
		}
		
		
		//--------ESTABLECER LOS moveTowards SEGÚN EL FANTASMA NULL, SI HAY UNO
		MOVE moveTowards_1 = null, 
			moveTowards_2 = null, 
			moveTowards_3 = null, 
			moveTowards_4 = null;
		
		if(!ghostNull_1)
			moveTowards_1 = game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), 
					game.getGhostCurrentNodeIndex(chasingGhost), MOVE.NEUTRAL, DM.PATH);
		else
			moveTowards_1 = game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), 
					initialGhost, MOVE.NEUTRAL, DM.PATH);
			
		if (numGhostInRange >= 2)
		{
			if(!ghostNull_2)
				moveTowards_2 = game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), 
					game.getGhostCurrentNodeIndex(chasingGhost_2), MOVE.NEUTRAL, DM.PATH);
			else
				moveTowards_2 = game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), 
						initialGhost, MOVE.NEUTRAL, DM.PATH);
				
			if(numGhostInRange >= 3)
			{
				if(!ghostNull_3)
					moveTowards_3 = game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), 
						game.getGhostCurrentNodeIndex(chasingGhost_3), MOVE.NEUTRAL, DM.PATH);
				else
					moveTowards_3 = game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), 
							initialGhost, MOVE.NEUTRAL, DM.PATH);
					
				if(numGhostInRange == 4)
				{
					if(!ghostNull_4)
						moveTowards_4 = game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), 
								game.getGhostCurrentNodeIndex(chasingGhost_4), MOVE.NEUTRAL, DM.PATH);
					else
						moveTowards_4 = game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), 
								initialGhost, MOVE.NEUTRAL, DM.PATH);
				}
			}
		}
		
		//---------GUARDAR EL moveTowards DEL FANTASMA EDIBLE
		MOVE moveTowardsEdible = null;
		if(edibleGhostChased != null) 
			moveTowardsEdible = game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), 
					game.getGhostCurrentNodeIndex(edibleGhostChased), MOVE.NEUTRAL, DM.PATH);
		
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
			if(possibleMovesForFour(possibleMoves[i], moveTowards_1, moveTowards_2, moveTowards_3, moveTowards_4))
			{
				escapeFromFour = true;
				
				move = possibleMoves[i];
				if (move == nearestPill)
					exit = true;
				
			}
			else if(!escapeFromFour && possibleMovesForThree(possibleMoves[i], moveTowards_1, moveTowards_2, moveTowards_3))
			{
				escapeFromThree = true;
				
				if(!wayToPill_3 && !wayToEdible_3)
				{
					move = possibleMoves[i];
					if (move == nearestPill)
						wayToPill_3 = true;
				}

				if(moveTowardsEdible != null && moveTowardsEdible == possibleMoves[i])
				{
					move = possibleMoves[i];
					wayToEdible_3 = true;
				}
				
			}
			else if(!escapeFromFour && !escapeFromThree && possibleMovesForTwo(possibleMoves[i], moveTowards_1, moveTowards_2))
			{
				escapeFromTwo = true;
				
				if(!wayToPill_2 && !wayToEdible_2)
				{
					move = possibleMoves[i];
					if (move == nearestPill)
						wayToPill_2 = true;
				}
				
				if(moveTowardsEdible != null && moveTowardsEdible == possibleMoves[i])
				{
					move = possibleMoves[i];
					wayToEdible_2 = true;
				}
			}
			else if(!escapeFromFour && !escapeFromThree && !escapeFromTwo && possibleMovesForOne(possibleMoves[i], moveTowards_1))
			{
				if(!wayToPill_1 && !wayToEdible_1)
				{
					move = possibleMoves[i];
					if (move == nearestPill)
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
		{
			if(chasingGhost == null)
				return game.getApproximateNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), 
						initialGhost, game.getPacmanLastMoveMade(), DM.PATH);
			else
				return game.getApproximateNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), 
						game.getGhostCurrentNodeIndex(chasingGhost), game.getPacmanLastMoveMade(), DM.PATH);
		}
		
		return move;
	}
	
	private boolean possibleMovesForOne(MOVE possibleMoves, MOVE moveTowards_1)
	{
		return (numGhostInRange == 1 && 
				possibleMoves != moveTowards_1);
	}
	
	private boolean possibleMovesForTwo(MOVE possibleMoves, MOVE moveTowards_1, MOVE moveTowards_2)
	{
		return (numGhostInRange == 2 && 
				possibleMoves != moveTowards_1 && possibleMoves != moveTowards_2);
	}
	
	private boolean possibleMovesForThree(MOVE possibleMoves, MOVE moveTowards_1, MOVE moveTowards_2, MOVE moveTowards_3)
	{
		return (numGhostInRange == 3 && 
				possibleMoves != moveTowards_1 && possibleMoves != moveTowards_2 && possibleMoves != moveTowards_3);
	}
	
	private boolean possibleMovesForFour(MOVE possibleMoves, MOVE moveTowards_1, MOVE moveTowards_2, MOVE moveTowards_3, MOVE moveTowards_4)
	{
		return (numGhostInRange == 4 && 
				possibleMoves != moveTowards_1 && possibleMoves != moveTowards_2 && possibleMoves != moveTowards_3 && possibleMoves != moveTowards_4);
	}

	private MOVE moveTowardsEdible(Game game)
	{
		MOVE moveTowards = null;
		
		int i = 0, size = ediblesVector.size();
		while (i < size && moveTowards != nearestPill)
		{
			moveTowards = game.getApproximateNextMoveTowardsTarget(pacman, 
							game.getGhostCurrentNodeIndex(ediblesVector.get(i)), pacman_lastMove, DM.PATH);
			
			i++;
		}
		ediblesVector.clear();
		
		if(moveTowards != null)
			return moveTowards;
		else
			return game.getApproximateNextMoveTowardsTarget(pacman, 
					game.getGhostCurrentNodeIndex(edibleGhost), pacman_lastMove, DM.PATH);
	}
}