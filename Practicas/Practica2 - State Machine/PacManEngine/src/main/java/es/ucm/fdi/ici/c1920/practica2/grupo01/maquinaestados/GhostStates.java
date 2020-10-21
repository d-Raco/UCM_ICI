package es.ucm.fdi.ici.c1920.practica2.grupo01.maquinaestados;

import java.util.Vector;

import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class GhostStates extends FSMImpl
{
	private int huntingDistanceFromPacman = 50,
			separationGhosts = 15,
			distanceThisGhostFromPacman = 0, 
			distanceClosestGhostFromPacman = 0,
			pacman;
	
	private MOVE moveToPacman;
	
	public void chooseState(Game game, int pacman, GHOST ghostType, GHOST closestEdibleGhostToPacman, GHOST closestChasingGhost, boolean pacmanCloseToPPill) 
	{
		if(!game.doesGhostRequireAction(ghostType)) 
			changeFirstState(States.NO_STATE);
		else
		{
			this.pacman = pacman;
			moveToPacman = game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghostType),
							pacman, game.getGhostLastMoveMade(ghostType), DM.PATH);
			
			if(game.isGhostEdible(ghostType) || pacmanCloseToPPill) // HUIR
				movesRunAway(game, ghostType, closestEdibleGhostToPacman, closestChasingGhost);
			
			else
				moveTowardsPacman(game, ghostType);
		}
	}

	/*
	 * Devuelve la pill más cercana al fantasma ghostType. 
	 * 
	 * Usamos esta función para que los fantasmas, si son edibles, eviten las pills y así no le den tantos puntos a pacman.
	 */
	private int closestPillToGhost(Game game, GHOST ghostType) 
	{
		int[] pillIndices = game.getActivePillsIndices();
		int distance, min = 100000, nearestPill = 0;
		
		for(int index : pillIndices) 
		{
			distance = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghostType), index, game.getPacmanLastMoveMade());
			if(distance <= min) 
			{
				nearestPill = index;
				min = distance;
			}
		}
		
		pillIndices = game.getActivePowerPillsIndices();
		
		for(int index : pillIndices) 
		{
			distance = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghostType), index, game.getPacmanLastMoveMade());
			if(distance <= min) 
			{
				nearestPill = index;
				min = distance;
			}
		}
		return nearestPill;
	}
	
	/*
	 * Dependiendo de la variable edible, esta función devuelve al fantasma edible más cercano al fantasma ghostIndex
	 * o al fantasma que esté persiguiendo a pacman más cercano al fantasma ghostIndex. 
	 * 
	 * Si edible es false, usamos esta función para que el closestEdibleGhostToPacman atraiga a pacman al chasing ghost más cercano.
	 * 
	 * Si edible es true, usamos esta función para que los fantsmas edibles huyan entre sí.
	 */
	private GHOST getClosestGhostToGhost(Game game, GHOST ghostIndex, boolean edible) 
	{
		GHOST closestGhost = null;
		int distance, min = 10000;
		
		for(GHOST ghostType : GHOST.values()) 
		{
			if(ghostType != ghostIndex)
			{
				if(game.isGhostEdible(ghostType) == edible) 
				{
					distance = game.getManhattanDistance(game.getGhostCurrentNodeIndex(ghostIndex), game.getGhostCurrentNodeIndex(ghostType));
					if (distance <= min) {
						closestGhost = ghostType;
						min = distance;
					}
				}
			}
		}
		
		return closestGhost;
	}

	private void movesRunAway(Game game, GHOST ghostType, GHOST closestEdibleGhostToPacman, GHOST closestChasingGhost)
	{
		// PRIMER ESTADO
		States huir = States.HUIR;
		huir.setSingleMove(moveToPacman);
		changeFirstState(huir);
		GHOST closestGhostToGhost = getClosestGhostToGhost(game, ghostType, true);
		
		// SEGUNDO ESTADO
		if (ghostType == closestEdibleGhostToPacman)
		{
			States suicida = States.SUICIDA;
			if(closestChasingGhost != null)
			{
				MOVE moveToChasingGhost = game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghostType),
						game.getGhostCurrentNodeIndex(closestChasingGhost), game.getGhostLastMoveMade(ghostType), DM.PATH);
				
				suicida.setSingleMove(moveToChasingGhost);
			}
			else if(closestGhostToGhost != null)
			{
				MOVE avoidClosestGhost = game.getApproximateNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghostType),
						game.getGhostCurrentNodeIndex(closestGhostToGhost), game.getGhostLastMoveMade(ghostType), DM.PATH);
				
				suicida.setSingleMove(avoidClosestGhost);
			}
			else
			{
				MOVE moveToPill = game.getApproximateNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghostType),
								closestPillToGhost(game, ghostType), game.getGhostLastMoveMade(ghostType), DM.PATH);
				
				suicida.setSingleMove(moveToPill);
			}
			
			changeSecondState(suicida);
		}
		else
		{
			States no_suicida = States.NO_SUICIDA;
			
			Vector<MOVE> m = new Vector<MOVE>(2);

			// Fantasma edible cercano a pacman
			if(closestEdibleGhostToPacman != null)
				m.addElement(game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghostType),
						game.getGhostCurrentNodeIndex(closestEdibleGhostToPacman), game.getGhostLastMoveMade(ghostType), DM.PATH));
			else
				m.addElement(null);
			
			// Fantasma edible cercano a mi
			if(closestGhostToGhost != null)
			{
				distanceThisGhostFromPacman = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghostType), pacman);
				distanceClosestGhostFromPacman = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(closestGhostToGhost), pacman);
				
				if((distanceThisGhostFromPacman == distanceClosestGhostFromPacman && 
						ghostType.name().charAt(0) < closestGhostToGhost.name().charAt(0)) ||
						distanceThisGhostFromPacman < distanceClosestGhostFromPacman)
				{
					m.addElement(game.getApproximateNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghostType),
							game.getGhostCurrentNodeIndex(closestGhostToGhost), game.getGhostLastMoveMade(ghostType), DM.PATH));
				}
				else
					m.addElement(null);
			}
			else
				m.addElement(null);
			
			no_suicida.setMove(m);
			changeSecondState(no_suicida);
			
		}
		
	}
	
	private void moveTowardsPacman(Game game, GHOST ghostType)
	{
		// PRIMER ESTADO
		States perseguir = States.PERSEGUIR_A_PACMAN;
		perseguir.setSingleMove(moveToPacman);
		changeFirstState(perseguir);
		
		// SEGUNDO ESTADO
		GHOST chasing = getClosestGhostToGhost(game, ghostType, false);
		if(chasing != null)
		{
			distanceThisGhostFromPacman = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghostType), pacman);
			distanceClosestGhostFromPacman = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(chasing), pacman);
						
			if((distanceThisGhostFromPacman == distanceClosestGhostFromPacman && 
				ghostType.name().charAt(0) < chasing.name().charAt(0)) ||
				(distanceThisGhostFromPacman <= huntingDistanceFromPacman &&
				distanceThisGhostFromPacman - distanceClosestGhostFromPacman < separationGhosts &&
				distanceThisGhostFromPacman > distanceClosestGhostFromPacman))
			{
				MOVE moveAwayChasing = game.getApproximateNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghostType),
						game.getGhostCurrentNodeIndex(chasing), game.getGhostLastMoveMade(ghostType), DM.PATH);

				States alejarse = States.ALEJARSE;
				alejarse.setSingleMove(moveAwayChasing);
				changeSecondState(alejarse);
			}
			else
				changeSecondState(States.NO_STATE);
		}
		else
			changeSecondState(States.NO_STATE);
	}
}

