package ucm.gaia.jcolibri.method.maintenance.algorithms;

import ucm.gaia.jcolibri.cbrcore.CBRCase;
import ucm.gaia.jcolibri.extensions.classification.ClassificationSolution;
import ucm.gaia.jcolibri.method.maintenance.AbstractCaseBaseEditMethod;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import ucm.gaia.jcolibri.method.retrieve.RetrievalResult;
import ucm.gaia.jcolibri.method.retrieve.selection.SelectCases;
import ucm.gaia.jcolibri.method.reuse.classification.KNNClassificationConfig;
import ucm.gaia.jcolibri.method.reuse.classification.KNNClassificationMethod;
import ucm.gaia.jcolibri.method.revise.classification.BasicClassificationOracle;
import ucm.gaia.jcolibri.method.revise.classification.ClassificationOracle;
import ucm.gaia.jcolibri.util.ProgressController;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;


/**
 * Provides the ability to run the RENN case base editing algorithm 
 * on a case base to eliminate noise.
 * 
 * @author Lisa Cummins
 * @author Derek Bridge
 * 18/05/07
 */
public class RENNNoiseReduction extends AbstractCaseBaseEditMethod {

	/**
	 * Simulates the RENN case base editing algorithm, returning the cases
	 * that would be deleted by the algorithm.
	 * @param cases The group of cases on which to perform editing.
	 * @param simConfig The similarity configuration for these cases.
	 * @return the list of cases that would be deleted by the 
	 * RENN algorithm.
	 */
	public List<CBRCase> retrieveCasesToDelete(Collection<CBRCase> cases, KNNClassificationConfig simConfig)
	{	/* RENN Algorithm:
		 *
		 * T: Training Set
		 * 
		 * Repeat
		 *		changes = false
		 *		For all x E T do
		 *			If x does not agree with the majority of its NN
		 *				T = T - {x}
		 *				changes = true
		 *			End-If
		 *		End-For
		 * Until not changes
		 *
		 * Return T	
	 	 */
		ProgressController.init(this.getClass(),"RENN Noise Reduction", ProgressController.UNKNOWN_STEPS);
		List<CBRCase> localCases = new LinkedList<CBRCase>();
		
		for(CBRCase c: cases)
		{	localCases.add(c);
		}
		
		List<CBRCase> allCasesToBeRemoved = new LinkedList<CBRCase>();

		boolean changes = true;
		while(changes && localCases.size() > 1)
		{	changes = false;
			ListIterator<CBRCase> iter = localCases.listIterator();	
			while (iter.hasNext())
			{	CBRCase q = iter.next();
				iter.remove();
				Collection<RetrievalResult> knn = NNScoringMethod.evaluateSimilarity(localCases, q, simConfig);
				knn = SelectCases.selectTopKRR(knn, simConfig.getK());
				try
				{	KNNClassificationMethod classifier = ((KNNClassificationConfig)simConfig).getClassificationMethod();
					ClassificationSolution predictedSolution = classifier.getPredictedSolution(knn);
					ClassificationOracle oracle = new BasicClassificationOracle();
					if(!oracle.isCorrectPrediction(predictedSolution, q))
					{	allCasesToBeRemoved.add(q);
						changes = true;
					}
					else
					{	iter.add(q);
					}
				} catch(ClassCastException cce)
				{	org.apache.commons.logging.LogFactory.getLog(RENNNoiseReduction.class).error(cce);
					System.exit(0);
				}
			}
			ProgressController.step(this.getClass());
		}
		ProgressController.finish(this.getClass());
		return allCasesToBeRemoved;
	}
}