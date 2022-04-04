/**
 * StoreCasesMethod.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 05/01/2007
 */
package ucm.gaia.jcolibri.method.retain;

import ucm.gaia.jcolibri.cbrcore.CBRCase;
import ucm.gaia.jcolibri.cbrcore.CBRCaseBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores cases in the case base.
 * @author Juan A. Recio-Garcia
 *
 */
public class StoreCasesMethod {

	
	/**
	 * Simple method that adds some cases to the case base invoking caseBase->learnCases().
	 */
	public static void storeCases(CBRCaseBase caseBase, List<CBRCase> cases)
	{
		caseBase.learnCases(cases);
	}
	
	/**
	 * Simple method that add a case to the case base invoking caseBase->learnCases().
	 */
	public static void storeCase(CBRCaseBase caseBase, CBRCase _case)
	{
		List<CBRCase> cases = new ArrayList<CBRCase>();
		cases.add(_case);
		caseBase.learnCases(cases);
	}

}
