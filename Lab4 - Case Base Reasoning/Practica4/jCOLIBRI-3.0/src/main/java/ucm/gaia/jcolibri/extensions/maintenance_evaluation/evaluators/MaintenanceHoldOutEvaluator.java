/**
 * DetailedHoldOutEvaluator.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * @author Lisa Cummins.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 23/07/2007
 */
package ucm.gaia.jcolibri.extensions.maintenance_evaluation.evaluators;

import org.apache.logging.log4j.LogManager;
import ucm.gaia.jcolibri.casebase.CachedLinealCaseBase;
import ucm.gaia.jcolibri.cbrcore.CBRCase;
import ucm.gaia.jcolibri.cbrcore.CBRCaseBase;
import ucm.gaia.jcolibri.extensions.maintenance_evaluation.MaintenanceEvaluator;
import ucm.gaia.jcolibri.util.ProgressController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This evaluation splits the case base in two sets: a training set and a test set. 
 * The training set is used as a case-base. It is maintained and then the cases in the
 * test set are used as queries to evaluate the training set. 
 * This process is performed serveral times.
 * 
 * @author Lisa Cummins.
 * @author Juan A. Recio Garc�a - GAIA http://gaia.fdi.ucm.es
 */
public class MaintenanceHoldOutEvaluator extends MaintenanceEvaluator
{
	/**
	 * Performs the Hold-Out evaluation. 
	 * @param testPercent percentage of the case base used as queries. 
	 * The case base is split randomly in each repetition.
	 * @param repetitions number of repetitions. 
	 */
	public void HoldOut(int testPercent, int repetitions) {
		try {
			// Obtain the time
			long t = (new Date()).getTime();
			int numberOfCycles = 0;

			// Run the precycle to load the case base
			LogManager.getLogger().info("Running precycle()");
			CBRCaseBase caseBase = app.preCycle();

			if (!(caseBase instanceof CachedLinealCaseBase))
				LogManager.getLogger().warn("Evaluation should be executed using a cached case base");

			List<CBRCase> originalCases = new ArrayList<CBRCase>(
					caseBase.getCases());
			
			int totalSteps = ((originalCases.size() * testPercent) / 100);
			totalSteps = totalSteps*repetitions;
			ProgressController.init(getClass(),"Hold Out Evaluation", totalSteps);

			// For each repetition
			for (int rep = 0; rep < repetitions; rep++) 
			{   	List<CBRCase> querySet = new ArrayList<CBRCase>();
				prepareCases(originalCases, querySet, testPercent, caseBase);
				
				// Run cycle for each case in querySet
				for (CBRCase c : querySet) 
				{	// Run the cycle
					LogManager.getLogger().info("Running cycle() " + numberOfCycles);
			
					app.cycle(c);
					
					ProgressController.step(getClass());
					numberOfCycles++;
				}
			}

			ProgressController.finish(getClass());
			
			//Revert case base to original state
			caseBase.forgetCases(originalCases);
			caseBase.learnCases(originalCases);
			
			// Run the poscycle to finish the application
			LogManager.getLogger().info("Running postcycle()");
			app.postCycle();

			t = (new Date()).getTime() - t;

			// Obtain and complete the evaluation result
			report.setTotalTime(t);
			report.setNumberOfCycles(numberOfCycles);

		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	/**
	 * Prepares the cases for evaluation by setting up test and training sets
	 * @param originalCases Complete original set of cases
	 * @param querySet Where queries are to be stored
	 * @param testPercent Percentage of cases used as queries
	 * @param caseBase The case base
	 */
	protected void prepareCases(List<CBRCase> originalCases, List<CBRCase> querySet,
		int testPercent, CBRCaseBase caseBase)
	{	
	    	ArrayList<CBRCase> caseBaseSet = new ArrayList<CBRCase>();

	    	// Split the case base
		splitCaseBase(originalCases, querySet, caseBaseSet, testPercent);

		// Clear the caseBase
		caseBase.forgetCases(originalCases);

		// Set the cases that acts as case base in this repetition
		caseBase.learnCases(caseBaseSet);
		
		if(this.simConfig != null && this.editMethod != null)
		{	// Perform maintenance on this case base
			editCaseBase(caseBase);
		}
	}

	/**
	 * Splits the case base in two sets: queries and case base
	 * 
	 * @param wholeCaseBase
	 *            Complete original case base
	 * @param querySet
	 *            Output param where queries are stored
	 * @param casebaseSet
	 *            Output param where case base is stored
	 * @param testPercent
	 *            Percentage of cases used as queries
	 */
	public static void splitCaseBase(List<CBRCase> wholeCaseBase,
		List<CBRCase> querySet, List<CBRCase> casebaseSet, int testPercent) 
	{	querySet.clear();
		casebaseSet.clear();

		int querySetSize = (wholeCaseBase.size() * testPercent) / 100;
		casebaseSet.addAll(wholeCaseBase);

		for (int i = 0; i < querySetSize; i++) 
		{	int random = (int) (Math.random() * casebaseSet.size());
			CBRCase _case = casebaseSet.get(random);
			casebaseSet.remove(random);
			querySet.add(_case);
		}
	}
}