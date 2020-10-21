package ucm.gaia.jcolibri.extensions.maintenance_evaluation.evaluators;

import org.apache.logging.log4j.LogManager;
import ucm.gaia.jcolibri.casebase.CachedLinealCaseBase;
import ucm.gaia.jcolibri.cbrcore.CBRCase;
import ucm.gaia.jcolibri.cbrcore.CBRCaseBase;
import ucm.gaia.jcolibri.extensions.maintenance_evaluation.MaintenanceEvaluator;
import ucm.gaia.jcolibri.util.FileIO;
import ucm.gaia.jcolibri.util.ProgressController;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


/**
 * This evaluation splits the case base in two sets: one used for testing where each
 * case is used as query, and another that acts as normal case base.
 * It uses queries from a file so that the evaluation can be repeated with the 
 * same test/training set split.
 * The generateSplit() method does the initial random split and saves the query set in a file. 
 * Later, the  HoldOutfromFile() method uses that file to load the query set and 
 * perform the evaluation.
 * 
 * @author Juan A. Recio Garc�a & Lisa Cummins
 */
public class MaintenanceSameSplitEvaluator extends MaintenanceEvaluator {

	/**
	 * Perform HoldOut evaluation using the queries contained in the specified file.
	 * @param file the file containing the queries.
	 */
	public void HoldOutfromFile(String file) 
	{	try 
		{	// Obtain the time
			long t = (new Date()).getTime();
			int numberOfCycles = 0;

			// Run the precycle to load the case base
			LogManager.getLogger().info("Running precycle()");
			CBRCaseBase caseBase = app.preCycle();

			if (!(caseBase instanceof CachedLinealCaseBase))
				LogManager.getLogger().warn("Evaluation should be executed using a cached case base");

			List<CBRCase> originalCases = new ArrayList<CBRCase>(caseBase.getCases());
			List<CBRCase> querySet = new ArrayList<CBRCase>();

			prepareCases(originalCases, querySet, file, caseBase);
			
			int totalSteps = querySet.size();
			ProgressController.init(getClass(), "Same Split - Hold Out Evaluation", totalSteps);
			
			// Run cycle for each case in querySet
			for (CBRCase c : querySet) {
				// Run the cycle
				LogManager.getLogger().info("Running cycle() " + numberOfCycles);

			//	report.storeQueryNum();

				app.cycle(c);

				ProgressController.step(getClass());
				numberOfCycles++;
			}

			ProgressController.finish(getClass());

			// Revert case base to original state
			caseBase.forgetCases(originalCases);
			caseBase.learnCases(originalCases);

			// Run the poscycle to finish the application
			LogManager.getLogger().info("Running postcycle()");
			app.postCycle();

			t = (new Date()).getTime() - t;

			// Obtain and complete the evaluation result
			report.setTotalTime(t);
			report.setNumberOfCycles(numberOfCycles);

		} catch (Exception e) 
		{	LogManager.getLogger().error(e);
		}
	}
	
	
	/**
	 * Prepares the cases for evaluation by setting up test and training sets
	 * @param originalCases Complete original set of cases
	 * @param querySet Where queries are to be stored
	 * @param caseBase The case base
	 */
	protected void prepareCases(List<CBRCase> originalCases, List<CBRCase> querySet,
		String file, CBRCaseBase caseBase)
	{	
	   	ArrayList<CBRCase> caseBaseSet = new ArrayList<CBRCase>();

	    // Split the case base
		splitCaseBaseFromFile(originalCases, querySet, caseBaseSet, file);

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
	 * Splits the case base in two sets: queries and case base, with the
	 * queries contained in the given file
	 * 
	 * @param wholeCaseBase
	 *            Complete original case base
	 * @param querySet
	 *            Output param where queries are stored
	 * @param casebaseSet
	 *            Output param where case base is stored
	 * @param filename
	 *            File which contains the queries
	 */
	public static void splitCaseBaseFromFile(List<CBRCase> wholeCaseBase,
		List<CBRCase> querySet, List<CBRCase> casebaseSet, String filename)
	{	querySet.clear();
        	casebaseSet.clear();
        	
        	casebaseSet.addAll(wholeCaseBase);
        	
        	try 
        	{	BufferedReader br = null;
        		br = new BufferedReader(new FileReader(FileIO.findFile(filename).getFile()));
        		if (br == null)
        			throw new Exception("Error opening file: " + filename);
        
        		String line = "";
        		while ((line = br.readLine()) != null) 
        		{	CBRCase c = null;
        			int pos=0;
        			boolean found = false;
        			for(Iterator<CBRCase> iter = casebaseSet.iterator(); iter.hasNext() && (!found); )
        			{	c = iter.next();
        				if(c.getID().toString().equals(line))
        					found = true;
        				else
        					pos++;
        			}
        			if(c==null)
        			{	System.out.println("Case "+line+" not found into case base");
        				continue;
        			}
        			
        			casebaseSet.remove(pos);
        			querySet.add(c);
        		}
        		br.close();
        	} catch (Exception e)
        	{	System.out.println(e);
        	}
	}
}