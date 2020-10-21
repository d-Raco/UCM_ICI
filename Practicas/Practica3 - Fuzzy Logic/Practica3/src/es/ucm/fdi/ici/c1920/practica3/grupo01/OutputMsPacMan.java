package es.ucm.fdi.ici.c1920.practica3.grupo01;

import java.util.HashMap;
import java.util.Vector;
import pacman.game.Constants.MOVE;

public class OutputMsPacMan 
{
	double runawayBLINKY;
	double runawayPINKY;
	double runawayINKY;
	double runawaySUE;

	double gotoedibleBLINKY;
	double gotoediblePINKY;
	double gotoedibleINKY;
	double gotoedibleSUE;
	

	public Vector<MOVE> ghostsLastMoveMadeVector = new Vector<MOVE>();
	
	public Vector<Integer> goToEdibleNodeVector = new Vector<Integer>();
	public Vector<Double> goToEdibleDistancesVector = new Vector<Double>();
	
	public Vector<Integer> runAwayNodeVector = new Vector<Integer>();
	public Vector<Double> runAwayDistancesVector = new Vector<Double>();
	
	
	
	public void processOutput(HashMap<String, Double> output, InputMsPacMan inputClass)
	{
		goToEdibleNodeVector.clear();
		goToEdibleDistancesVector.clear();
		runAwayNodeVector.clear();
		runAwayDistancesVector.clear();
		
		// HUIR
		runawayBLINKY = output.get("runAwayBLINKY");
		runawayPINKY = output.get("runAwayPINKY");
		runawayINKY = output.get("runAwayINKY");
		runawaySUE = output.get("runAwaySUE");
		
		// IR A POR EDIBLE
		gotoedibleBLINKY = output.get("goToEdibleBLINKY");
		gotoediblePINKY = output.get("goToEdiblePINKY");
		gotoedibleINKY = output.get("goToEdibleINKY");
		gotoedibleSUE = output.get("goToEdibleSUE");
		
		updateVectors(inputClass);
	}
	
	public void updateVectors(InputMsPacMan inputClass)
	{
		if(gotoedibleBLINKY > Constants.GOTOEDIBLE_LIMIT)
		{
			goToEdibleNodeVector.addElement(inputClass.getNodesArray()[0]);
			goToEdibleDistancesVector.addElement(inputClass.getDistancesEdiblesArray()[0]);
		}
		
		if(gotoediblePINKY > Constants.GOTOEDIBLE_LIMIT)
		{
			goToEdibleNodeVector.addElement(inputClass.getNodesArray()[1]);
			goToEdibleDistancesVector.addElement(inputClass.getDistancesEdiblesArray()[1]);
		}
		
		if(gotoedibleINKY > Constants.GOTOEDIBLE_LIMIT)
		{
			goToEdibleNodeVector.addElement(inputClass.getNodesArray()[2]);
			goToEdibleDistancesVector.addElement(inputClass.getDistancesEdiblesArray()[2]);
		}
		
		if(gotoedibleSUE > Constants.GOTOEDIBLE_LIMIT)
		{
			goToEdibleNodeVector.addElement(inputClass.getNodesArray()[3]);
			goToEdibleDistancesVector.addElement(inputClass.getDistancesEdiblesArray()[3]);
		}
		
		if(runawayBLINKY > Constants.RUNAWAY_LIMIT)
		{
			runAwayNodeVector.addElement(inputClass.getNodesArray()[0]);
			runAwayDistancesVector.addElement(inputClass.getDistancesNoEdiblesArray()[0]);
			ghostsLastMoveMadeVector.addElement(inputClass.getGhostsLastMoveMade()[0]);
		}
		
		if(runawayPINKY > Constants.RUNAWAY_LIMIT)
		{
			runAwayNodeVector.addElement(inputClass.getNodesArray()[1]);
			runAwayDistancesVector.addElement(inputClass.getDistancesNoEdiblesArray()[1]);
			ghostsLastMoveMadeVector.addElement(inputClass.getGhostsLastMoveMade()[1]);
		}
		
		if(runawayINKY > Constants.RUNAWAY_LIMIT)
		{
			runAwayNodeVector.addElement(inputClass.getNodesArray()[2]);
			runAwayDistancesVector.addElement(inputClass.getDistancesNoEdiblesArray()[2]);
			ghostsLastMoveMadeVector.addElement(inputClass.getGhostsLastMoveMade()[2]);
		}
		
		if(runawaySUE > Constants.RUNAWAY_LIMIT)
		{
			runAwayNodeVector.addElement(inputClass.getNodesArray()[3]);
			runAwayDistancesVector.addElement(inputClass.getDistancesNoEdiblesArray()[3]);
			ghostsLastMoveMadeVector.addElement(inputClass.getGhostsLastMoveMade()[3]);
		}
	}
}
