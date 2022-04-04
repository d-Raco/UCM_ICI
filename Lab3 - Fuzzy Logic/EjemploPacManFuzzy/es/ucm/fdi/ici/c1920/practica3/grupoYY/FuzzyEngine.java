package es.ucm.fdi.ici.c1920.practica3.grupoYY;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.rule.Rule;
import net.sourceforge.jFuzzyLogic.rule.Variable;

/**
 * FuzzyEngine.
 * This class wraps and simplifies jFuzzyLogic library  
 * @author Juan A. Recio
 *
 */
public class FuzzyEngine {

	public enum FUZZY_CONTROLLER  {MSPACMAN, GHOSTS};
	
	private FIS fis = null;
	private static final String BIN_CLASSES = "bin.";
	private static final String DATA_FOLDER = ".data.";
	private static final String PACMAN_FCL = "pacman.fcl";
	private static final String GHOSTS_FCL = "ghosts.fcl";
	
	
	public FuzzyEngine(FUZZY_CONTROLLER controller)
	{
		super();
		
		//DANGER!!! Only works outside jar files. 
		//Should work on windows. Not tested...
		String dir = BIN_CLASSES+this.getClass().getPackage().getName()+DATA_FOLDER;
		dir = dir.replace(".", java.io.File.separator);
		String fcl_file = null;
		if(controller.equals(FUZZY_CONTROLLER.MSPACMAN))
				fcl_file = PACMAN_FCL;
		else
			fcl_file = GHOSTS_FCL;
				
		String filename = dir + fcl_file;
		
		fis = FIS.load(filename, false);

		if (fis == null) {
			System.err.println("Can't load file: '" + filename + "'");
		}
	}
	
	/**
	 * Evaluates a fuzzy function given the input variables and fills the output map with the output variables (defuzzified)
	 * @param function is the fuzzy function to execute defined in the FUNCTION_BLOCK line. 
	 * @param input is a map containing the input values. These values will be fuzzified according to the FUZZIFY blocks
	 * @param output is a map containing the output values defined in the DEFUZZIFY block. These variables are defuzzified.
	 */
	public void evaluate(String function, Map<String,Double> input, Map<String,Double> output)
	{
		output.clear();
		FunctionBlock fb = fis.getFunctionBlock(function);
		
		// Set inputs
		for(String varName: input.keySet())
			fb.setVariable(varName, input.get(varName));

		// Evaluate
		fb.evaluate();
		
		// Get Output
		HashMap<String,Variable> variables = fb.getVariables();
		for(String varName: variables.keySet())
		{
			Variable var = variables.get(varName);
			if(var.isOutput())
				output.put(varName, var.defuzzify());
		}
		return; 
	}
	
	/**
	 * Shows the degree of support of each rule. Used only for debugging purposes. 
	 * @param function the function containing the rules.
	 * @param block the rules blocks to debug.
	 */
	public void debugRules(String function, String block)
	{
		for( Rule r : fis.getFunctionBlock(function).getFuzzyRuleBlock(block).getRules() )
		      System.out.println(r);
	}
	
	/**
	 * Main method for testing
	 */
	public static void main(String[] args)
	{
		FuzzyEngine fe = new FuzzyEngine(FUZZY_CONTROLLER.MSPACMAN);
		HashMap<String, Double> input = new HashMap<String,Double>();
		HashMap<String, Double> output = new HashMap<String,Double>();
		input.put("BLINKYdistance", 10.0);
		input.put("PINKYdistance", 80.0);
		input.put("INKYdistance", 50.0);
		input.put("SUEdistance", 17.0);
		fe.evaluate("FuzzyMsPacMan", input, output);
		System.out.println(output.get("runAway"));
		fe.debugRules("FuzzyMsPacMan", "MsPacManRules");
	}
}
