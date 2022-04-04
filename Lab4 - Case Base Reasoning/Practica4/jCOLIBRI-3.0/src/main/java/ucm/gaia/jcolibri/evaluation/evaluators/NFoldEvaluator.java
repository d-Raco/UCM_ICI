package ucm.gaia.jcolibri.evaluation.evaluators;

import org.apache.logging.log4j.LogManager;
import ucm.gaia.jcolibri.casebase.CachedLinealCaseBase;
import ucm.gaia.jcolibri.cbraplications.StandardCBRApplication;
import ucm.gaia.jcolibri.cbrcore.CBRCase;
import ucm.gaia.jcolibri.cbrcore.CBRCaseBase;
import ucm.gaia.jcolibri.evaluation.EvaluationReport;
import ucm.gaia.jcolibri.evaluation.Evaluator;
import ucm.gaia.jcolibri.exception.ExecutionException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * This evaluation method divides the case base into several random folds (indicated by the user). 
 * For each fold, their cases are used as queries and the remaining folds are used together as case base. 
 * This process is performed several times.
 * 
 * @author Juan A. Recio García - GAIA http://gaia.fdi.ucm.es
 * @version 2.0
 */
public class NFoldEvaluator extends Evaluator {

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
	 * Executes the N-Fold evaluation.
	 * @param folds Number of folds (randomly generated).
	 * @param repetitions Number of repetitions
	 */
    public void NFoldEvaluation(int folds, int repetitions)
    {   
        try
        {
            //Get the time
            long t = (new Date()).getTime();
            int numberOfCycles = 0;

        	// Run the precycle to load the case base
            LogManager.getLogger().info("Running precycle()");
			CBRCaseBase caseBase = app.preCycle();

			if (!(caseBase instanceof CachedLinealCaseBase))
                LogManager.getLogger().warn(
								"Evaluation should be executed using a cached case base");
            
            Collection<CBRCase> cases = new ArrayList<>(caseBase.getCases());
            
            //For each repetition
            for(int r=0; r<repetitions; r++)
            {
                //Create the folds
                createFolds(cases, folds);
                
                //For each fold
                for(int f=0; f<folds; f++)
                {
                    ArrayList<CBRCase> querySet = new ArrayList<>();
                    ArrayList<CBRCase> caseBaseSet = new ArrayList<>();

                    //Obtain the query and casebase sets
                    getFolds(f, querySet, caseBaseSet);
                    
                    //Clear the caseBase
                    caseBase.forgetCases(cases);
                    
                    //Set the cases that acts as casebase in this cycle
                    caseBase.learnCases(caseBaseSet);
                    
                    //Run cycle for each case in querySet (current fold)
                    for(CBRCase c: querySet)
                    {
                        LogManager.getLogger().info(
        							"Running cycle() " + numberOfCycles);
        					app.cycle(c);
                        
                        numberOfCycles++;
                    }          
                } 
                
            }

			//Revert case base to original state
			caseBase.forgetCases(cases);
			caseBase.learnCases(cases);
            
            //Run the poscycle to finish the application
            LogManager.getLogger().info("Running postcycle()");
			app.postCycle();

            
            //Complete the evaluation result
            report.setTotalTime(t);
            report.setNumberOfCycles(numberOfCycles);
            

    	} catch (Exception e) {
            LogManager.getLogger().error(e);
		}

    }

    
    protected ArrayList<ArrayList<CBRCase>> _folds;

    protected void createFolds(Collection<CBRCase> cases, int folds) {
        _folds = new  ArrayList<ArrayList<CBRCase>>();
        int foldsize = cases.size() / folds;
        ArrayList<CBRCase> copy = new ArrayList<CBRCase>(cases);

        for(int f=0; f<folds; f++) {

            ArrayList<CBRCase> fold = new ArrayList<CBRCase>();
            for(int i=0; (i<foldsize)&&(copy.size()>0); i++) {

                int random = (int) (Math.random() * copy.size());
                CBRCase _case = copy.get( random );
                copy.remove(random);
                fold.add(_case);
            }
            _folds.add(fold);
        }
    }
    
    protected void getFolds(int f, List<CBRCase> querySet, List<CBRCase> caseBaseSet) {
        querySet.clear();
        caseBaseSet.clear();
        
        querySet.addAll(_folds.get(f));
        
        for(int i=0; i<_folds.size(); i++)
            if(i!=f)
                caseBaseSet.addAll(_folds.get(i));
    }
    
}