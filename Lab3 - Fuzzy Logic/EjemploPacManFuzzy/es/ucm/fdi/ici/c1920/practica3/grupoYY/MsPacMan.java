package es.ucm.fdi.ici.c1920.practica3.grupoYY;

import pacman.controllers.PacmanController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/*
 * The Class RandomPacMan.
 */
public final class MsPacMan extends PacmanController {

	
	private static final Double RUNAWAY_LIMIT = 15.0;
	private static final Double MAX_DISTANCE = 200.0;
	
	FuzzyEngine fe;
	HashMap<String, Double> input;   HashMap<String, Double> output;
	
	public MsPacMan() {
		fe = new FuzzyEngine(FuzzyEngine.FUZZY_CONTROLLER.MSPACMAN);
		input = new HashMap<String,Double>();
		output = new HashMap<String,Double>();
	}
	
	private double[] distances = {MAX_DISTANCE,MAX_DISTANCE,MAX_DISTANCE,MAX_DISTANCE};
	private int current;
	
    @Override
    public MOVE getMove(Game game, long timeDue) {
        current = game.getPacmanCurrentNodeIndex();
        input.clear(); output.clear();

        for (GHOST ghost : GHOST.values()) {
            double distance = game.getShortestPathDistance(current, game.getGhostCurrentNodeIndex(ghost));
            if(distance == -1) distance = 200;
            if(distance != 0) // With PO visibility non observable ghosts distance is 0.
            	distances[ghost.ordinal()] = distance;
            input.put(ghost.name()+"distance", distances[ghost.ordinal()]);
        }
        System.out.println(Arrays.toString(distances));
        fe.evaluate("FuzzyMsPacMan", input, output);
		double runaway = output.get("runAway");
		System.out.println(runaway);
		if(runaway > RUNAWAY_LIMIT)
			return runAwayFromClosestGhost(game);
		else
			return goToPills(game);
    }

    
    private MOVE runAwayFromClosestGhost(Game game) {
    	System.out.println("RunAway");
    	double min_distance = Double.MAX_VALUE;
        GHOST closest_ghost = null;
        for (GHOST ghost : GHOST.values()) {
            double distance = distances[ghost.ordinal()];    		
    		if(distance < min_distance)
    		{
    			min_distance = distance;
    			closest_ghost = ghost;
    		}
        }
    	return game.getNextMoveAwayFromTarget(current, game.getGhostCurrentNodeIndex(closest_ghost), DM.PATH);
    }
    
	private MOVE goToPills(Game game) {
		System.out.println("goToPills");
        int[] pills = game.getActivePillsIndices();
        int[] powerPills = game.getActivePowerPillsIndices();

        ArrayList<Integer> targets = new ArrayList<Integer>();

        for (int i = 0; i < pills.length; i++)                    //check which pills are available
        {
            if ((game.isPillStillAvailable(i)!=null) &&game.isPillStillAvailable(i)!=null) {
                targets.add(pills[i]);
            }
        }

        for (int i = 0; i < powerPills.length; i++)            //check with power pills are available
        {
            if ((game.isPowerPillStillAvailable(i)!=null) && game.isPowerPillStillAvailable(i)) {
                targets.add(powerPills[i]);
            }
        }

        int[] targetsArray = new int[targets.size()];        //convert from ArrayList to array
        for (int i = 0; i < targetsArray.length; i++) {
            targetsArray[i] = targets.get(i);
        }
        //return the next direction once the closest target has been identified
        return game.getNextMoveTowardsTarget(current, game.getClosestNodeIndexFromNodeIndex(current, targetsArray, DM.PATH), DM.PATH);

	}
}