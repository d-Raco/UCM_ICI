package ucm.gaia.jcolibri.evaluation;


import ucm.gaia.jcolibri.cbraplications.StandardCBRApplication;

/**
 * This abstract class defines the common behaviour of an evaluator.
 * @author Juanan
 */
public abstract class Evaluator {
	
	/** Initializes the evaluator with the CBR application to evaluate 
	 * @see StandardCBRApplication
	 * */
	public abstract void init(StandardCBRApplication cbrApp);
	
	/** Object that stores the evaluation results */
	protected static EvaluationReport report;
	
	/** Returns the evaluation report */
	public static EvaluationReport getEvaluationReport(){
		return report;
	}
}
