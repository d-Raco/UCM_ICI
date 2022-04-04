package es.ucm.fdi.ici.c1920.practica2.grupo01.maquinaestados;

import java.util.*;
import pacman.controllers.PacmanController;
import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class MsPacMan extends PacmanController
{
	private MsPacmanStates states = new MsPacmanStates();
	
	private int numGhostChasing = 0;
	
	private int pacman;
	
	private MOVE pacman_lastMove = null;
	
	/*
	 * 
	 * 
	 *         M E T O D O S
	 * 
	 * 
	 */
	
	@Override
	public MOVE getMove(Game game, long timeDue) 
	{
		//float time = System.currentTimeMillis() % 1000;
		
		states.chooseState(game);
		
		pacman = game.getPacmanCurrentNodeIndex();
		pacman_lastMove = game.getPacmanLastMoveMade();
		
		States state1 = states.getFirstState();
		switch(state1)
		{
			case COMER_PILLS:
				//System.out.println("pacman pill: " + (System.currentTimeMillis() % 1000 - time));
				return state1.getSingleMove();
				
			case PERSEGUIR:
				//System.out.println("pacman perseguir: " + (System.currentTimeMillis() % 1000 - time));
				return moveTowardsEdible(game, state1);
				
			case HUIR:
				//System.out.println("pacman huir 1: " + (System.currentTimeMillis()  % 1000 - time));
				numGhostChasing = 1;
				return moveAwayFromGhosts(game, state1.getSingleMove(), null, null, null);
				
			case HUIR_2:
				//System.out.println("pacman huir 2 : " + (System.currentTimeMillis()  % 1000 - time));
				numGhostChasing = 2;
				return moveAwayFromGhosts(game, state1.getMove().get(0), state1.getMove().get(1), null, null);
				
			case HUIR_3:
				//System.out.println("pacman huir 3: " + (System.currentTimeMillis()  % 1000 - time));
				numGhostChasing = 3;
				return moveAwayFromGhosts(game, state1.getMove().get(0), state1.getMove().get(1), state1.getMove().get(2), null);
				
			case HUIR_4:
				//System.out.println("pacman huir 4: " + (System.currentTimeMillis()  % 1000 - time));
				numGhostChasing = 4;
				return moveAwayFromGhosts(game, state1.getMove().get(0), state1.getMove().get(1), state1.getMove().get(2), state1.getMove().get(3));
				
			default:
				return MOVE.NEUTRAL;
		}
	}
	
	private MOVE moveAwayFromGhosts(Game game, MOVE moveTowards_1, MOVE moveTowards_2, MOVE moveTowards_3, MOVE moveTowards_4)
	{
		MOVE[] possibleMoves = game.getPossibleMoves(pacman, pacman_lastMove);
		MOVE pillMove = states.getSecondState().getSingleMove();
		MOVE edibleMove = states.getSecondState().getSingleMove();
		
		MOVE choosenMove = null;
		
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

				 choosenMove = possibleMoves[i];
				 if (choosenMove == pillMove)
					 exit = true;

	         }
			 else if(!escapeFromFour && possibleMovesForThree(possibleMoves[i], moveTowards_1, moveTowards_2, moveTowards_3))
			 {
				 escapeFromThree = true;

				 if(!wayToPill_3 && !wayToEdible_3)
				 {
					 choosenMove = possibleMoves[i]; 
					 if (choosenMove == pillMove)
						 wayToPill_3 = true;
				 }
				 
				 if(edibleMove != null && edibleMove == possibleMoves[i])
				 {
					 choosenMove = possibleMoves[i];
					 wayToEdible_3 = true;
				 }
			}
			else if(!escapeFromFour && !escapeFromThree && possibleMovesForTwo(possibleMoves[i], moveTowards_1, moveTowards_2))
			{
				escapeFromTwo = true;
				 
				if(!wayToPill_2 && !wayToEdible_2)
				 {
					 choosenMove = possibleMoves[i];
					 if (choosenMove == pillMove)
						 wayToPill_2 = true;
				 }

				 if(edibleMove != null && edibleMove == possibleMoves[i])
				 {
					 choosenMove = possibleMoves[i];
					 wayToEdible_2 = true;
				 }
			}
			else if(!escapeFromFour && !escapeFromThree && !escapeFromTwo && possibleMovesForOne(possibleMoves[i], moveTowards_1))
	        {
	             if(!wayToPill_1 && !wayToEdible_1)
	             {
	            	 choosenMove = possibleMoves[i];
	                 if (choosenMove == pillMove)
	                     wayToPill_1 = true;
	             }

	             if(edibleMove != null && edibleMove == possibleMoves[i])
	             {
	            	 choosenMove = possibleMoves[i];
	                 wayToEdible_1 = true;
	             }
	        }

			i++;
		}
			
		if(choosenMove == null)
			choosenMove = states.getFirstState().getMoveAway();
		
		States s = states.getFirstState();
		if(numGhostChasing > 1)
		{
			Vector<MOVE> m = s.getMove();
			m.clear();
			s.setMove(m);
		}
		
		return choosenMove;
	}
	
	
	private MOVE moveTowardsEdible(Game game, States state1)
	{
		Vector<MOVE> movesEdible = state1.getMove();
		MOVE pillMove = states.getSecondState().getSingleMove();
		
		MOVE choosenMove = null;
		
		int size = movesEdible.size();
		int i = 0;
		while (i < size && choosenMove != pillMove)
		{
			choosenMove = movesEdible.get(i);
			i++;
		}
		
		if(choosenMove == null)
			choosenMove = movesEdible.get(0);
		
		movesEdible.clear(); // PREGUNTAR PABLO
		state1.setMove(movesEdible);
		
		return choosenMove;
	}

	
	private boolean possibleMovesForOne(MOVE possibleMoves, MOVE moveTowards_1)
	{
		return (numGhostChasing >= 1 && 
				possibleMoves != moveTowards_1);
	}
	
	private boolean possibleMovesForTwo(MOVE possibleMoves, MOVE moveTowards_1, MOVE moveTowards_2)
	{
		return (numGhostChasing >= 2 && 
				possibleMoves != moveTowards_1 && possibleMoves != moveTowards_2);
	}
	
	private boolean possibleMovesForThree(MOVE possibleMoves, MOVE moveTowards_1, MOVE moveTowards_2, MOVE moveTowards_3)
	{
		return (numGhostChasing >= 3 && 
				possibleMoves != moveTowards_1 && possibleMoves != moveTowards_2 && possibleMoves != moveTowards_3);
	}
	
	private boolean possibleMovesForFour(MOVE possibleMoves, MOVE moveTowards_1, MOVE moveTowards_2, MOVE moveTowards_3, MOVE moveTowards_4)
	{
		return (numGhostChasing == 4 && 
				possibleMoves != moveTowards_1 && possibleMoves != moveTowards_2 && possibleMoves != moveTowards_3 && possibleMoves != moveTowards_4);
	}

}