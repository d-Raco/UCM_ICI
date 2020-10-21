package es.ucm.fdi.ici.c1920.practica2.grupo01.maquinaestados;

import java.util.EnumMap;

import pacman.game.Game;
import pacman.controllers.GhostController;
import pacman.game.Constants.*;

public class Ghosts extends GhostController 
{
	private MOVE[] possibleMoves;
	private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
	private GhostStates state = new GhostStates();
	private boolean pacmanCloseToPPill;
	private int pacman;
	
	@Override
	public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) 
	{
		moves.clear();
		pacman = game.getPacmanCurrentNodeIndex();
		pacmanCloseToPPill = pacmanCloseToPPill(game);
		GHOST closestEdibleGhostToPacman = getClosestEdibleGhostToPacman(game),
				closestChasingGhost;
		
		for(GHOST ghostType : GHOST.values()) 
		{
			closestChasingGhost = getClosestChasingGhostToChasingGhost(game, ghostType);
			possibleMoves = game.getPossibleMoves(game.getGhostCurrentNodeIndex(ghostType), 
					game.getGhostLastMoveMade(ghostType));
			
			state.chooseState(game, pacman, ghostType, closestEdibleGhostToPacman, closestChasingGhost, pacmanCloseToPPill);
			switch(state.getFirstState())
			{
				case HUIR:
					switch(state.getSecondState())
					{
						case SUICIDA:
							moves.put(ghostType, nextMoveClosestEdibleGhost(game, closestEdibleGhostToPacman));
							break;
						case NO_SUICIDA:
							moves.put(ghostType, nextMoveEdibleGhosts(game, ghostType, closestEdibleGhostToPacman));
							break;
						default:
							moves.put(ghostType, null);
							break;
					}
					break;
				case PERSEGUIR_A_PACMAN:
					switch(state.getSecondState())
					{
						case ALEJARSE:
							moves.put(ghostType, nextMoveAvoidCloseGhosts(game, ghostType, closestChasingGhost));
							break;
						default:
							moves.put(ghostType, nextMoveChasingGhosts(game, ghostType));
							break;
					}
					break;
				default:
					moves.put(ghostType, null);
			}
		}
		return moves;
	}
	
	/*
	 * Devuelve true si pacman está a una distancia de 30 de una PowerPill. Devuelve falso si no.
	 * 
	 * Usamos esta función para que los fantasmas huyan si pacman se acerca demasiado a una PowerPill.
	 */
	private boolean pacmanCloseToPPill(Game game) 
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
			if(!game.isGhostEdible(ghostType) && ghostType != ghostIndex && game.getGhostLairTime(ghostType) == 0) 
			{
				distance = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghostIndex), 
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
	private GHOST getClosestEdibleGhostToPacman(Game game) 
	{
		GHOST closestEdibleGhost = null;
		int distance, min = 10000;
		
		for(GHOST ghostType : GHOST.values()) 
		{
			if(game.isGhostEdible(ghostType) || pacmanCloseToPPill) 
			{
				distance = game.getShortestPathDistance(pacman, game.getGhostCurrentNodeIndex(ghostType));
				if (distance <= min) {
					closestEdibleGhost = ghostType;
					min = distance;
				}
			}
		}
		
		return closestEdibleGhost;
	}
	
	
	/*
	 * Devuelve el mejor movimiento para el closestEdibleGhostToPacman.
	 * 
	 * Su jerarquía de movimientos es: 
	 * 1. Evitar a pacman.
	 * 2. Ir hacia el chasing ghost más cercano.
	 * 3. Si no hay chasing ghosts, evitar otros fantasmas edibles.
	 * 4. Si no hay ni chasing ni edibles, evita pills.
	 */
	private MOVE nextMoveClosestEdibleGhost(Game game, GHOST ghostType) 
	{
		MOVE tmp, move = possibleMoves[0],
				moveToPacman = state.getFirstState().getSingleMove(),
				bestMove = state.getSecondState().getSingleMove();
		
		for(int i = 1; i < possibleMoves.length; ++i) 
		{
			tmp = possibleMoves[i];
			if(move == moveToPacman)
				move = tmp;
			else if(tmp == bestMove && tmp != moveToPacman)
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
	private MOVE nextMoveEdibleGhosts(Game game, GHOST ghostType, GHOST closestEdibleGhostToPacman) 
	{
		MOVE move = possibleMoves[0], tmp, moveToPacman = state.getFirstState().getSingleMove(),
				moveToClosestEdibleGhostToPacman = state.getSecondState().getMove().get(0), 
				moveAwayFromClosestEdibleGhost = state.getSecondState().getMove().get(1);
	
		for(int i = 1; i < possibleMoves.length; ++i) 
		{
			tmp = possibleMoves[i];
			if(move == moveToPacman)
				move = tmp;
			else if(move == moveToClosestEdibleGhostToPacman && tmp != moveToPacman)
				move = tmp;
			else if(tmp != moveToPacman && tmp != moveToClosestEdibleGhostToPacman && move != moveAwayFromClosestEdibleGhost)
				move = tmp;
		}
		
		return move;
	}
	
	private MOVE nextMoveChasingGhosts(Game game, GHOST ghostType) 
	{
		return state.getFirstState().getSingleMove();
	}
	
	private MOVE nextMoveAvoidCloseGhosts(Game game, GHOST ghostType, GHOST closestChasingGhost)
	{
		MOVE  move = possibleMoves[0], tmp = null, moveToPacman = state.getFirstState().getSingleMove(),
				awayFromGhost = state.getSecondState().getSingleMove();
					
		for(int i = 0; i < possibleMoves.length; ++i) 
		{
			tmp = possibleMoves[i];
			if(tmp == awayFromGhost && tmp != moveToPacman)
				move = tmp;
		}
				
		return move;
	}
}

