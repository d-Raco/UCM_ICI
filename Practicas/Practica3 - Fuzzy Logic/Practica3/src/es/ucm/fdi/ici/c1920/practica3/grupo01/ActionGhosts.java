package es.ucm.fdi.ici.c1920.practica3.grupo01;

import java.util.Random;
import java.util.Vector;

import pacman.game.Game;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

public class ActionGhosts 
{
	private Random rnd = new Random();
	
	public MOVE followPacman(Game game, GHOST ghostType, int lastNodePacman, boolean edible)
	{
		MOVE move;
		if(!edible)
			move = game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghostType), lastNodePacman, game.getGhostLastMoveMade(ghostType), DM.PATH);
		else
			move = game.getApproximateNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghostType), lastNodePacman, game.getGhostLastMoveMade(ghostType), DM.PATH);

		return 	move;
	}
	
	public MOVE stayInArea(Game game, GHOST ghostType, Quadrant q, Quadrant pacmanQuadrant, boolean edible)
	{
		MOVE move;
		if(!edible || (edible && q != pacmanQuadrant)) 
			move = game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghostType), q.getNodeIndex(), game.getGhostLastMoveMade(ghostType), DM.PATH);		
		else 
			move = game.getApproximateNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghostType), q.getNodeIndex(), game.getGhostLastMoveMade(ghostType), DM.PATH);		

		return move;
	}
	
	public MOVE patroll(Game game, GHOST ghostType, boolean edible)
	{
		MOVE[] possibleMoves = game.getPossibleMoves(game.getGhostCurrentNodeIndex(ghostType), game.getGhostLastMoveMade(ghostType));
		
		MOVE move = goToPill(game, ghostType, edible);
		
		if(move == null)
			move = possibleMoves[rnd.nextInt(possibleMoves.length)];

		return move;
	}
	
	public MOVE separate(Game game, GHOST ghostType, GHOST closestGhost, int lastNodePacman)
	{
		MOVE move = null, tmp;
		MOVE[] possibleMoves;
		double distance = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(closestGhost), game.getGhostCurrentNodeIndex(ghostType));
		double distanceThisGhostFromPacman = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghostType), lastNodePacman);
		double distanceChasingGhostFromPacman = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(closestGhost), lastNodePacman);

		if((distance == 0 && ghostType.name().charAt(0) < closestGhost.name().charAt(0)) || 
				(distance <= 10 && distanceThisGhostFromPacman > distanceChasingGhostFromPacman)) 
		{
			possibleMoves = game.getPossibleMoves(game.getGhostCurrentNodeIndex(ghostType), 
					game.getGhostLastMoveMade(ghostType));
			
			move = possibleMoves[0];
			
			for(int i = 1; i < possibleMoves.length; ++i) 
			{
				tmp = possibleMoves[i];
				if(tmp == game.getApproximateNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghostType),
						game.getGhostCurrentNodeIndex(closestGhost), game.getGhostLastMoveMade(ghostType), DM.PATH))
					move = tmp;
			}
		}
		
		return move;
	}
	
	private MOVE goToPill(Game game, GHOST ghostType, boolean edible)
	{
		Vector<MOVE> goToPill = new Vector<MOVE>();
		
		int pillIndex = -1;
		int[] pills = game.getActivePillsIndices();
		
        double min_distance = 20;
        
        int size = pills.length;
        for(int i = 0; i < size; ++i)
        {
        	int distance = game.getShortestPathDistance(game.getGhostCurrentNodeIndex(ghostType), pills[i]);
        	if(distance < min_distance)
        		if(!edible)
        			goToPill.addElement(game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghostType), pillIndex, 
        																		game.getGhostLastMoveMade(ghostType), DM.PATH));
        		else
        			goToPill.addElement(game.getApproximateNextMoveAwayFromTarget(game.getGhostCurrentNodeIndex(ghostType), pillIndex, 
        																		game.getGhostLastMoveMade(ghostType), DM.PATH));
        }
		
        int tam = goToPill.size();
        if(tam != 0)
        	return goToPill.get(rnd.nextInt(tam));
		
		return null;
	}
}
