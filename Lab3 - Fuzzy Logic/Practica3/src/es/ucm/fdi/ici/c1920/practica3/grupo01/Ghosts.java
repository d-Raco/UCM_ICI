package es.ucm.fdi.ici.c1920.practica3.grupo01;

import java.util.HashMap;

import pacman.game.Game;
import pacman.controllers.POGhostController;
import pacman.game.Constants.*;

public class Ghosts extends POGhostController 
{
	private InputGhosts inputClass = new InputGhosts();
	private OutputGhosts outputClass = new OutputGhosts();
	private ActionGhosts actionClass = new ActionGhosts();
	
	private Quadrant[] quadrants;
		
	FuzzyEngine fe;
	HashMap<String, Double> input;   HashMap<String, Double> output;
	
	public Ghosts() 
	{
		fe = new FuzzyEngine(FuzzyEngine.FUZZY_CONTROLLER.GHOSTS);
		input = new HashMap<String,Double>();
		output = new HashMap<String,Double>();
	}
	
	public MOVE getMove(GHOST ghost, Game game, long timeDue) 
	{
		input.clear(); output.clear();
		
		if(game.getCurrentLevelTime() == 0 && ghost.name().equals("BLINKY"))
		{
			inputClass.resetInput(game, ghost);
	        quadrants = Quadrant.setQuadrants(5, game, game.getCurrentLevel());
		}
	       
		if(game.getGhostLairTime(ghost) <= 0)
		{
			input = inputClass.getInput(game, ghost, quadrants);
			
	        //Procesar output
			fe.evaluate("FuzzyGhosts", input, output);
			
			switch(outputClass.processOutput(game, ghost, output, inputClass))
			{
			case GOTOPACMAN:
				return actionClass.followPacman(game, ghost, inputClass.getLastNodePacman(), game.isGhostEdible(ghost));
			
			case STAYINAREA:
				return actionClass.stayInArea(game, ghost, inputClass.getQuadrant(), quadrants[Quadrant.PACMAN], game.isGhostEdible(ghost));
			
			case PATROLL:
				return actionClass.patroll(game, ghost, game.isGhostEdible(ghost));
				
			case SEPARATE:
				return actionClass.separate(game, ghost, inputClass.getClosestGhost(), inputClass.getLastNodePacman());
				
			default:
				return null;
			}
		}
		else
			return null;
	}
}
