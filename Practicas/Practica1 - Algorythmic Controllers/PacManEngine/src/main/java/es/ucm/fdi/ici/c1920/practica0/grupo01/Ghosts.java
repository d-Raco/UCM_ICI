package es.ucm.fdi.ici.c1920.practica0.grupo01;

import java.util.EnumMap;

import pacman.game.Game;
import pacman.controllers.GhostController;
import pacman.game.Constants.*;

public class Ghosts extends GhostController 
{
	private MOVE[] possibleMoves;
	private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
	
	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) 
	{
		float time = System.currentTimeMillis() % 1000;
		
		moves.clear();
		int pacman = game.getPacmanCurrentNodeIndex();
		GHOST closestEdibleGhostToPacman = getClosestEdibleGhostToPacman(game, pacman);
		
		if(closestEdibleGhostToPacman != null) {
			if(!game.doesGhostRequireAction(closestEdibleGhostToPacman))
				moves.put(closestEdibleGhostToPacman, null);
			else
				moves.put(closestEdibleGhostToPacman, nextMoveClosestEdibleGhost(game, closestEdibleGhostToPacman, pacman));
		}
		
		for(GHOST ghostType : GHOST.values()) 
		{
			if(ghostType != closestEdibleGhostToPacman) 
			{
				if(!game.doesGhostRequireAction(ghostType)) 
					moves.put(ghostType, null);
				else 
				{	
					if(game.isGhostEdible(ghostType) || pacmanCloseToPPill(game, pacman))					
						moves.put(ghostType, nextMoveEdibleGhosts(game, ghostType, closestEdibleGhostToPacman, pacman));		
					else 
						moves.put(ghostType, nextMoveChasingGhosts(game, ghostType, pacman));
				}
			}
		}
		System.out.println("ghost: " + ( System.currentTimeMillis() % 1000 - time));
		return moves;
	}
	
	/*
	 * Devuelve true si pacman está a una distancia de 30 de una PowerPill. Devuelve falso si no.
	 * 
	 * Usamos esta función para que los fantasmas huyan si pacman se acerca demasiado a una PowerPill.
	 */
	private boolean pacmanCloseToPPill(Game game, int pacman) 
	{
		int[] powerPillIndices = game.getActivePowerPillsIndices();
		int distance = 30,
			powerpillsSize = powerPillIndices.length;
		
		boolean close = false;
		int i = 0;
		while (i < powerpillsSize && !close)
		{
			if(game.getShortestPathDistance(pacman, powerPillIndices[i]) <= distance) 
				close = true;
			i++;
		}

		return close;
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
	 * Devuelve el fantasma chasing más cercano al fantasma chasing ghostIndex si están muy cerca.
	 * 
	 * Usamos esta función para que los fantasmas cojan distintos movimientos y con suerte arrinconen a pacman.
	 * 
	 */
	private GHOST getClosestChasingGhostToChasingGhost(Game game, GHOST ghostIndex)
	{
		GHOST ghostCloseToGhost = null;
		int distance, closeDistance = 15;
		
		for(GHOST ghostType : GHOST.values()) 
		{
			if(!game.isGhostEdible(ghostType) && ghostType != ghostIndex) 
			{
				distance = game.getManhattanDistance(game.getGhostCurrentNodeIndex(ghostIndex), 
						game.getGhostCurrentNodeIndex(ghostType));
				if (distance <= closeDistance) {
					ghostCloseToGhost = ghostType;
					closeDistance = distance;
				}
			}
		}
		
		return ghostCloseToGhost;
	}
	
	/*
	 * Devuelve el fantasma edible más cercano a pacman o aquel que está más cerca si pacman se acerca a una PowerPill.
	 * 
	 * Usamos esta función para que el resto de fantasmas huyan de este fantasma, que estará llevando a pacman en otra dirección.
	 * 
	 */
	private GHOST getClosestEdibleGhostToPacman(Game game, int pacman) 
	{
		GHOST closestEdibleGhost = null;
		int distance, min = 10000;
		
		for(GHOST ghostType : GHOST.values()) 
		{
			if(game.isGhostEdible(ghostType) || pacmanCloseToPPill(game, pacman)) 
			{
				distance = game.getManhattanDistance(pacman, game.getGhostCurrentNodeIndex(ghostType));
				if (distance <= min) {
					closestEdibleGhost = ghostType;
					min = distance;
				}
			}
		}
		
		return closestEdibleGhost;
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
			if(game.isGhostEdible(ghostType) == edible) 
			{
				distance = game.getManhattanDistance(game.getGhostCurrentNodeIndex(ghostIndex), game.getGhostCurrentNodeIndex(ghostType));
				if (distance <= min) {
					closestGhost = ghostType;
					min = distance;
				}
			}
		}
		
		return closestGhost;
	}
	
	/*
	 * Devuelve el mejor movimiento para el closestEdibleGhostToPacman.
	 * 
	 * Su jerarquía de movimientos es: 
	 * 1. Evitar a pacman.
	 * 2. Ir hacia el chasing ghost más cercano.
	 * 3. Si no hay chasing ghosts, evitar pills.
	 */
	private MOVE nextMoveClosestEdibleGhost(Game game, GHOST ghostType, int pacman) 
	{
		MOVE move = null, tmp = null, moveToPacman = null, moveToChasingGhost = null, moveToPill = null;
		GHOST closestChasingGhost = getClosestGhostToGhost(game, ghostType, false);
		possibleMoves = game.getPossibleMoves(game.getGhostCurrentNodeIndex(ghostType), 
				game.getGhostLastMoveMade(ghostType));
		
		move = possibleMoves[0];
		moveToPacman = game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghostType),
				pacman, game.getGhostLastMoveMade(ghostType), DM.PATH);
		moveToPill = game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghostType),
				closestPillToGhost(game, ghostType), game.getGhostLastMoveMade(ghostType), DM.PATH);
		
		if(closestChasingGhost != null)
			moveToChasingGhost = game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghostType),
					game.getGhostCurrentNodeIndex(closestChasingGhost), game.getGhostLastMoveMade(ghostType), DM.PATH);
		
		for(int i = 1; i < possibleMoves.length; ++i) 
		{
			tmp = possibleMoves[i];
			if(move == moveToPacman)
				move = tmp;
			else if(tmp == moveToChasingGhost && tmp != moveToPacman)
				move = tmp;
			else if(moveToChasingGhost == null && tmp != moveToPacman && move == moveToPill)
				move = tmp;
		}
		
		return move;
	}
	
	/*
	 * Devuelve el mejor movimiento para los fantasmas distintos a closestEdibleGhostToPacman cuando están edibles 
	 * o pacman está cerca a una PowerPill.
	 * 
	 * Su jerarquía de movimientos es: 
	 * 1. Evitar a pacman.
	 * 2. Evitar al closestEdibleGhostToPacman.
	 * 3. Evitar otros fantasmas.
	 */
	private MOVE nextMoveEdibleGhosts(Game game, GHOST ghostType, GHOST closestEdibleGhostToPacman, int pacman) 
	{
		MOVE move = null, tmp = null, moveToPacman = null, moveToClosestEdibleGhostToPacman = null, moveToClosestEdibleGhost = null;
		GHOST closestEdibleGhost = null;
		
		possibleMoves = game.getPossibleMoves(game.getGhostCurrentNodeIndex(ghostType), 
				game.getGhostLastMoveMade(ghostType));
		
		move = possibleMoves[0];
		moveToPacman = game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghostType),
				pacman, game.getGhostLastMoveMade(ghostType), DM.PATH);
		closestEdibleGhost = getClosestGhostToGhost(game, ghostType, true);
		
		if(closestEdibleGhost != null)
			moveToClosestEdibleGhost = game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghostType),
					game.getGhostCurrentNodeIndex(closestEdibleGhost), game.getGhostLastMoveMade(ghostType), DM.PATH);
		
		if(closestEdibleGhostToPacman != null)
			moveToClosestEdibleGhostToPacman = game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghostType),
					game.getGhostCurrentNodeIndex(closestEdibleGhostToPacman), game.getGhostLastMoveMade(ghostType), DM.PATH);
		
		for(int i = 1; i < possibleMoves.length; ++i) 
		{
			tmp = possibleMoves[i];
			if(move == moveToPacman)
				move = tmp;
			else if(move == moveToClosestEdibleGhostToPacman && tmp != moveToPacman)
				move = tmp;
			else if(tmp != moveToPacman && tmp != moveToClosestEdibleGhostToPacman && move == moveToClosestEdibleGhost)
				move = tmp;
		}
		
		return move;
	}
	
	/*
	 * Devuelve el mejor movimiento para los fantasmas no edibles. Se dirigen hacia pacman, pero en caso de que se amontonen
	 * y estén a huntingDistanceFromPacman de pacman, mandamos a los más lejanos del apelotonamiento en otra dirección para que traten de rodear a pacman.
	 */
	private MOVE nextMoveChasingGhosts(Game game, GHOST ghostType, int pacman) 
	{
		MOVE move = null, tmp = null;
		GHOST closestChasingGhost = null;
		int huntingDistanceFromPacman = 30, distanceThisGhostFromPacman = 0, distanceClosestGhostFromPacman = 0;
		
		move = game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghostType),
				pacman, game.getGhostLastMoveMade(ghostType), DM.PATH);
		closestChasingGhost = getClosestChasingGhostToChasingGhost(game, ghostType);
		/*
		if(closestChasingGhost != null) 
		{
			distanceThisGhostFromPacman = game.getManhattanDistance(game.getGhostCurrentNodeIndex(ghostType), pacman);
			distanceClosestGhostFromPacman = game.getManhattanDistance(game.getGhostCurrentNodeIndex(closestChasingGhost), pacman);
			
			if(distanceThisGhostFromPacman == distanceClosestGhostFromPacman && ghostType.name().charAt(0) < closestChasingGhost.name().charAt(0)) 
			{
				possibleMoves = game.getPossibleMoves(game.getGhostCurrentNodeIndex(ghostType), 
						game.getGhostLastMoveMade(ghostType));
				
				move = possibleMoves[0];
				
				for(int i = 1; i < possibleMoves.length; ++i) 
				{
					tmp = possibleMoves[i];
					if(tmp == game.getApproximateNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghostType),
							game.getGhostCurrentNodeIndex(closestChasingGhost), game.getGhostLastMoveMade(ghostType), DM.PATH))
						move = tmp;
				}
			}
			
			else if(distanceThisGhostFromPacman <= huntingDistanceFromPacman && distanceThisGhostFromPacman > distanceClosestGhostFromPacman) 
			{
				possibleMoves = game.getPossibleMoves(game.getGhostCurrentNodeIndex(ghostType), 
						game.getGhostLastMoveMade(ghostType));
				
				move = possibleMoves[0];
				
				for(int i = 1; i < possibleMoves.length; ++i) 
				{
					tmp = possibleMoves[i];
					if(tmp == game.getApproximateNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghostType),
							game.getGhostCurrentNodeIndex(closestChasingGhost), game.getGhostLastMoveMade(ghostType), DM.PATH))
						move = tmp;
				}
			} 
		}
		*/
		return move;
	}
}
