package es.ucm.fdi.ici.c1920.practica3.grupo01;

import pacman.controllers.PacmanController;
import pacman.game.Constants.DM;
import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;
import pacman.game.Game;

import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;


public final class MsPacMan extends PacmanController 
{
	private InputMsPacMan inputClass = new InputMsPacMan();
	private OutputMsPacMan outputClass = new OutputMsPacMan();
	private ActionMsPacMan actionClass = new ActionMsPacMan();
	
			
	
	FuzzyEngine fe;
	HashMap<String, Double> input;   HashMap<String, Double> output;
	
	public MsPacMan() 
	{
		fe = new FuzzyEngine(FuzzyEngine.FUZZY_CONTROLLER.MSPACMAN);
		input = new HashMap<String,Double>();
		output = new HashMap<String,Double>();
	}
	
    @Override
    public MOVE getMove(Game game, long timeDue) 
    {
        input.clear(); output.clear();
        
		input = inputClass.getInput(game);

        fe.evaluate("FuzzyMsPacMan", input, output);
        
        outputClass.processOutput(output, inputClass);
		
		
		if(!outputClass.runAwayNodeVector.isEmpty())
			return actionClass.runAwayFromClosestGhost(game, inputClass.getMoveToNearestPill(), outputClass.runAwayNodeVector, outputClass.runAwayDistancesVector,
														outputClass.goToEdibleNodeVector, outputClass.goToEdibleDistancesVector, outputClass.ghostsLastMoveMadeVector);
		
		if(!outputClass.goToEdibleNodeVector.isEmpty())
			return actionClass.runToEdibleGhost(game, inputClass.getMoveToNearestPill(), 
												outputClass.goToEdibleNodeVector, outputClass.goToEdibleDistancesVector);
			
		
		return actionClass.runToPills(game, inputClass.getMoveToNearestPill());
    }
}