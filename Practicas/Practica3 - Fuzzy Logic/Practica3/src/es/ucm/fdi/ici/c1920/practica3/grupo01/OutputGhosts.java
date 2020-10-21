package es.ucm.fdi.ici.c1920.practica3.grupo01;

import java.util.HashMap;

import pacman.game.Constants.GHOST;
import pacman.game.Game;

public class OutputGhosts {	
	public Constants.OUTPUT processOutput(Game game, GHOST ghost, HashMap<String,Double> output, InputGhosts inputClass)
	{
		//goToPacman
		double goToPacman = output.get("goToPacman");
		
		//stayInArea
		double stayInArea = output.get("stayInArea");
		
		if(inputClass.needToSeparate(game, ghost))
			return Constants.OUTPUT.SEPARATE;
		
		else if(goToPacman > Constants.GOTOPACMAN_LIMIT && !inputClass.hasArrivedToPacmanNode()) 
			return Constants.OUTPUT.GOTOPACMAN;
		
		else if(stayInArea > Constants.STAYINAREA_LIMIT) 
			return Constants.OUTPUT.STAYINAREA;
		
		return Constants.OUTPUT.PATROLL;
	}
}
