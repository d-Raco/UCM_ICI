package ucm.gaia.jcolibri.evaluation.evaluators;

import org.apache.logging.log4j.LogManager;
import ucm.gaia.jcolibri.casebase.CachedLinealCaseBase;
import ucm.gaia.jcolibri.cbraplications.StandardCBRApplication;
import ucm.gaia.jcolibri.cbrcore.CBRCase;
import ucm.gaia.jcolibri.cbrcore.CBRCaseBase;
import ucm.gaia.jcolibri.evaluation.EvaluationReport;
import ucm.gaia.jcolibri.evaluation.Evaluator;
import ucm.gaia.jcolibri.exception.ExecutionException;
import ucm.gaia.jcolibri.util.ProgressController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * This method splits the case base in two sets: one used for testing where each
 * case is used as query, and another that acts as normal case base. This
 * process is performed serveral times.
 * 
 * @author Juan A. Recio García - GAIA http://gaia.fdi.ucm.es
 * @version 2.0
 */
public class HoldOutEvaluator extends Evaluator {

	protected StandardCBRApplication app;

	public void init(StandardCBRApplication cbrApp) {
	    	
		report = new EvaluationReport();
		app = cbrApp;
		try {
			app.configure();
		} catch (ExecutionException e) {
			LogManager.getLogger().error(e);
		}
	}

	/**
	 * Performs the Hold-Out evaluation. 
	 * @param testPercent Percent of the case base used as queries. The case base is splited randomly in each repetition.
	 * @param repetitions Number of repetitions. 
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

			List<CBRCase> originalCases = new ArrayList<>(caseBase.getCases());
			
			int totalSteps = ((originalCases.size() * testPercent) / 100);
			totalSteps = totalSteps*repetitions;
			ProgressController.init(getClass(),"Hold Out Evaluation", totalSteps);
			
			// For each repetition
			for (int rep = 0; rep < repetitions; rep++) {

				List<CBRCase> querySet = new ArrayList<>();
				List<CBRCase> caseBaseSet = new ArrayList<>();

				// Split the case base
				splitCaseBase(originalCases, querySet, caseBaseSet, testPercent);

				// Clear the caseBase
				caseBase.forgetCases(originalCases);

				// Set the cases that acts as case base in this repetition
				caseBase.learnCases(caseBaseSet);

				// Run cycle for each case in querySet
				for (CBRCase c : querySet) {
					// Run the cycle
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
			LogManager.getLogger().error(e);
		}

	}

	/**
	 * Splits the case base in two sets: queries and case base
	 * @param holeCaseBase Complete original case base
	 * @param querySet Output param where queries are stored
	 * @param casebaseSet Output param where case base is stored
	 * @param testPercent Percentage of cases used as queries
	 */
	protected void splitCaseBase(Collection<CBRCase> holeCaseBase, List<CBRCase> querySet, List<CBRCase> casebaseSet, int testPercent) {
		querySet.clear();
		casebaseSet.clear();

		int querySetSize = (holeCaseBase.size() * testPercent) / 100;
		casebaseSet.addAll(holeCaseBase);

		for (int i = 0; i < querySetSize; i++) {
			int random = (int) (Math.random() * casebaseSet.size());
			CBRCase _case = casebaseSet.get(random);
			casebaseSet.remove(random);
			querySet.add(_case);
		}
	}

}