package com.myFuzzyProject;

import java.util.HashMap;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;

public class TippingClass {
	public static void main(String[] args) throws Exception {
		String filename = "tipper.fcl";
		FIS fis = FIS.load(filename, true);

		if (fis == null) {
			System.err.println("Can't load file: '" 
						+ filename + "'");
			System.exit(1);
		}

		// Get default function block
		FunctionBlock fb = fis.getFunctionBlock("tipper");

		// Set inputs
		fb.setVariable("food", 8);
		fb.setVariable("service", 3);

		// Evaluate
		fb.evaluate();
		
		 Variable tip = fb.getVariable("tip");
	     JFuzzyChart.get().chart(tip, tip.getDefuzzifier(), true);

		// Show all variables
		HashMap<String,Variable> variables = fb.getVariables();
		for(String s: variables.keySet())
			System.out.println(s +" ---> "+ variables.get(s));
	     
		// Show output variable
		fb.getVariable("tip").defuzzify();
		System.out.println("Tip: " + fb.getVariable("tip").getValue());

		// Print ruleSet
		System.out.println("RuleSet");
		System.out.println(fb);
	}

}
