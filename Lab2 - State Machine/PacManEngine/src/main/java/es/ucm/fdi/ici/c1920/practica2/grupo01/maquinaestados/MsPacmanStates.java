package es.ucm.fdi.ici.c1920.practica2.grupo01.maquinaestados;

import java.util.Vector;

import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class MsPacmanStates extends FSMImpl
{
	private int rangeEdible = 50,
			rangeEdibleChoice = 15,
			rangeChasing = 50;
	
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
	
	/*
	 * 
	 * 
	 *         M E T O D O S
	 * 
	 * 
	 */
	
	@Override
	public void chooseState(Game game) 
	{
		float time = System.currentTimeMillis() % 1000;
		
		pacman = game.getPacmanCurrentNodeIndex();
		pacman_lastMove = game.getPacmanLastMoveMade();
		
		getNearestChasingAndEdibleGhost(game);
		
		nearestPill = getNearestPill(game);

		if(numGhostInRange != 0)
			moveAwayGhostsStates(game);
		
		else if(edibleGhost != null)
			moveTowardsEdibleStates(game);
		
		else
		{
			States s = States.COMER_PILLS;
			s.setSingleMove(nearestPill);
			changeFirstState(s);
			
			changeSecondState(States.NO_STATE);
			changeThirdState(States.NO_STATE);
		}
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
	
	private MOVE getNearestPill(Game game) 
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

	private void moveAwayGhostsStates(Game game)
	{	
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
		States state;
		Vector<MOVE> stateMoves = new Vector<MOVE>(2);
		MOVE moveAwayFirst = null;
		
		MOVE moveTowards_1 = null, 
			moveTowards_2 = null, 
			moveTowards_3 = null, 
			moveTowards_4 = null;
		
		if(!ghostNull_1)
		{
			moveTowards_1 = game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), 
					game.getGhostCurrentNodeIndex(chasingGhost), MOVE.NEUTRAL, DM.PATH);
			moveAwayFirst = game.getApproximateNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), 
					game.getGhostCurrentNodeIndex(chasingGhost), pacman_lastMove, DM.PATH);
		}
		else
		{
			moveTowards_1 = game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), 
					initialGhost, MOVE.NEUTRAL, DM.PATH);
			moveAwayFirst = game.getApproximateNextMoveAwayFromTarget(game.getPacmanCurrentNodeIndex(), 
					initialGhost, pacman_lastMove, DM.PATH);
		}
		
		state = States.HUIR;
		stateMoves.addElement(moveTowards_1);
		
		if (numGhostInRange >= 2)
		{
			if(!ghostNull_2)
				moveTowards_2 = game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), 
					game.getGhostCurrentNodeIndex(chasingGhost_2), MOVE.NEUTRAL, DM.PATH);
			else
				moveTowards_2 = game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), 
						initialGhost, MOVE.NEUTRAL, DM.PATH);
			
			state = States.HUIR_2;
			stateMoves.addElement(moveTowards_2);
				
			if(numGhostInRange >= 3)
			{
				if(!ghostNull_3)
					moveTowards_3 = game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), 
						game.getGhostCurrentNodeIndex(chasingGhost_3), MOVE.NEUTRAL, DM.PATH);
				else
					moveTowards_3 = game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), 
							initialGhost, MOVE.NEUTRAL, DM.PATH);
				
				state = States.HUIR_3;
				stateMoves.addElement(moveTowards_3);
					
				if(numGhostInRange == 4)
				{
					if(!ghostNull_4)
						moveTowards_4 = game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), 
								game.getGhostCurrentNodeIndex(chasingGhost_4), MOVE.NEUTRAL, DM.PATH);
					else
						moveTowards_4 = game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), 
								initialGhost, MOVE.NEUTRAL, DM.PATH);
					
					state = States.HUIR_4;
					stateMoves.addElement(moveTowards_4);
				}
			}
		}
		
		
		if(state == States.HUIR)
			state.setSingleMove(stateMoves.get(0));
		else
			state.setMove(stateMoves);
		
		state.setMoveAway(moveAwayFirst);
		
		changeFirstState(state);
		
		
		//---------GUARDAR EL moveTowards DEL FANTASMA EDIBLE y de PILL
		state = States.COMER_PILLS;
		state.setSingleMove(nearestPill);
		changeSecondState(state);
		
		MOVE moveTowardsEdible = null;
		if(edibleGhostChased != null)
		{
			moveTowardsEdible = game.getApproximateNextMoveTowardsTarget(game.getPacmanCurrentNodeIndex(), 
					game.getGhostCurrentNodeIndex(edibleGhostChased), MOVE.NEUTRAL, DM.PATH);
			
			state = States.PERSEGUIR;
			state.setSingleMove(moveTowardsEdible);
			changeThirdState(state);
		}
		
		
	}

	private void moveTowardsEdibleStates(Game game)
	{
		MOVE moveTowards = null;
		
		States s = States.PERSEGUIR;
		Vector<MOVE> edibleMoves = new Vector<MOVE>(4);
		
		int i = 0, size = ediblesVector.size();
		while (i < size)
		{
			moveTowards = game.getApproximateNextMoveTowardsTarget(pacman, 
							game.getGhostCurrentNodeIndex(ediblesVector.get(i)), pacman_lastMove, DM.PATH);
			
			edibleMoves.addElement(moveTowards);
			
			i++;
		}
		ediblesVector.clear();
		
		edibleMoves.add(0, game.getApproximateNextMoveTowardsTarget(pacman, 
							game.getGhostCurrentNodeIndex(edibleGhost), pacman_lastMove, DM.PATH));
		
		s.setMove(edibleMoves);
		changeFirstState(s);
		
		s = States.COMER_PILLS;
		s.setSingleMove(nearestPill);
		changeSecondState(s);
		
		changeThirdState(States.NO_STATE);
	}
}
