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
import java.util.Date;

/**
 * This methods uses all the cases as queries. 
 * It executes so cycles as cases in the case base. 
 * In each cycle one case is used as query.  
 * 
 * @author Juan A. Recio García - GAIA http://gaia.fdi.ucm.es
 * @version 2.0
 */
public class LeaveOneOutEvaluator extends Evaluator {

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
	 * Performs the Leave-One-Out evaluation. 
	 * For each case in the case base,  remove that case from the case base and use it as query for that cycle.
	 */
	public void LeaveOneOut() {
		try {
			ArrayList<CBRCase> aux = new ArrayList<CBRCase>();

			long t = (new Date()).getTime();
			int numberOfCycles = 0;

			// Run the precycle to load the case base
			LogManager.getLogger().info("Running precycle()");
			CBRCaseBase caseBase = app.preCycle();

			if (!(caseBase instanceof CachedLinealCaseBase))
				LogManager.getLogger().warn(
								"Evaluation should be executed using a cached case base");

			
			ArrayList<CBRCase> cases = new ArrayList<CBRCase>(caseBase.getCases());
			
			ProgressController.init(getClass(),"LeaveOneOut Evaluation", cases.size());
			
			//For each case in the case base
			for(CBRCase _case : cases) {
				
				//Delete the case in the case base
				aux.clear();
				aux.add(_case);
				caseBase.forgetCases(aux);

				//Run the cycle
				LogManager.getLogger().info(
						"Running cycle() " + numberOfCycles);
				app.cycle(_case);

				//Recover case base
				caseBase.learnCases(aux);

				numberOfCycles++;
				ProgressController.step(getClass());
			}

			//Run PostCycle
			LogManager.getLogger().info("Running postcycle()");
			app.postCycle();

			ProgressController.finish(getClass());
			
			t = (new Date()).getTime() - t;

			//complete evaluation report
			report.setTotalTime(t);
			report.setNumberOfCycles(numberOfCycles);

		} catch (ExecutionException e) {
			LogManager.getLogger().error(e);
			e.printStackTrace();
		}
	}

}