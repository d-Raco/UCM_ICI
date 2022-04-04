package ucm.gaia.jcolibri.extensions.maintenance_evaluation.evaluators;

import org.apache.logging.log4j.LogManager;
import ucm.gaia.jcolibri.casebase.CachedLinealCaseBase;
import ucm.gaia.jcolibri.cbrcore.CBRCase;
import ucm.gaia.jcolibri.cbrcore.CBRCaseBase;
import ucm.gaia.jcolibri.exception.ExecutionException;
import ucm.gaia.jcolibri.extensions.maintenance_evaluation.MaintenanceEvaluator;
import ucm.gaia.jcolibri.util.ProgressController;

import java.util.ArrayList;
import java.util.Date;


/**
 * This evalutation takes each case in turn to be the query.
 * It maintains the case-base (the remaining cases) and then
 * uses that as a training set to evaluate the query.
 * 
 * @author Lisa Cummins.
 * @author Juan A. Recio Garc�a - GAIA http://gaia.fdi.ucm.es
 */
public class MaintenanceLeaveOneOutEvaluator extends MaintenanceEvaluator
{
	/**
	 * Performs the Leave-One-Out evaluation. 
	 * For each case in the case base,  remove that case from the case base, 
	 * maintain the case-base and the use the case as a query for that cycle.
	 */
	public void LeaveOneOut() 
	{	try 
		{	java.util.ArrayList<CBRCase> aux = new java.util.ArrayList<CBRCase>();

			long t = (new Date()).getTime();
			int numberOfCycles = 0;

			// Run the precycle to load the case base
			LogManager.getLogger().info("Running precycle()");
			CBRCaseBase caseBase = app.preCycle();

			if (!(caseBase instanceof CachedLinealCaseBase))
				LogManager.getLogger().warn("Evaluation should be executed using a cached case base");

			prepareCases(caseBase);

			ArrayList<CBRCase> cases = new ArrayList<CBRCase>(caseBase.getCases());	

			ProgressController.init(getClass(),"LeaveOneOut Evaluation", cases.size());
			
			//For each case in the case base
			for(CBRCase _case : cases) 
			{	//Delete the case in the case base
				aux.clear();
				aux.add(_case);
				caseBase.forgetCases(aux);

				//Run the cycle
				LogManager.getLogger().info("Running cycle() " + numberOfCycles);

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
		}
	}

	/**
	 * Prepares the cases for evaluation
	 * @param caseBase the case base
	 */
	protected void prepareCases(CBRCaseBase caseBase)
	{	if(this.simConfig != null && this.editMethod != null)
		{	// Perform maintenance on this case base
			editCaseBase(caseBase);
		}
	}
}