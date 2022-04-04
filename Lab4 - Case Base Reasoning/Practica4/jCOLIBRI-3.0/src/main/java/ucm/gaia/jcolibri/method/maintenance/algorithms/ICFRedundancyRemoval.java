package ucm.gaia.jcolibri.method.maintenance.algorithms;

import ucm.gaia.jcolibri.cbrcore.CBRCase;
import ucm.gaia.jcolibri.method.maintenance.AbstractCaseBaseEditMethod;
import ucm.gaia.jcolibri.method.maintenance.CompetenceModel;
import ucm.gaia.jcolibri.method.maintenance.solvesFunctions.ICFSolvesFunction;
import ucm.gaia.jcolibri.method.reuse.classification.KNNClassificationConfig;
import ucm.gaia.jcolibri.util.ProgressController;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Provides the ability to run the ICF case base editing algorithm 
 * on a case base to eliminate redundancy.
 * 
 * @author Lisa Cummins
 * @author Derek Bridge
 * 18/05/07
 */
public class ICFRedundancyRemoval extends AbstractCaseBaseEditMethod {
	
	/**
	 * Simulates the ICF case base editing algorithm, returning the cases
	 * that would be deleted by the algorithm.
	 * @param cases The group of cases on which to perform editing.
	 * @param simConfig The similarity configuration for these cases.
	 * @return the list of cases that would be deleted by the 
	 * ICF algorithm.
	 */
	public Collection<CBRCase> retrieveCasesToDelete(Collection<CBRCase> cases, KNNClassificationConfig simConfig)
	{	/* ICF Algorithm:
		 * T: Training Set
		 * 
		 * Run RENN on T
		 * (Not included here, RENN performed seperately)
		 *
		 * Repeat
		 * 		For all x E T do
		 * 			compute reachable(x)
		 * 			compute coverage(x)
		 * 		End-For
		 * 		progress = false
		 * 		For all x E T do
		 * 			If |reachable(x)| > |coverage(x)| then
		 * 				flag x for removal
		 * 				process = true
		 * 			End-If
		 * 		End-For
		 * 		For all x E T do	
		 * 			If x flagged for removal then
		 * 				T = T - {x}
		 * 			End-If
		 * 		End-For
		 * Until not progress
		 * 
		 * Return T
		 */
		ProgressController.init(this.getClass(),"ICF Redundancy Removal", ProgressController.UNKNOWN_STEPS);
		Collection<CBRCase> localCases = new LinkedList<>();
		for(CBRCase c: cases)
		{	localCases.add(c);
		}

		CompetenceModel sc = new CompetenceModel();
		Map<CBRCase, Collection<CBRCase>> coverageSets = null, reachabilitySets = null;
		Collection<CBRCase> allCasesToBeRemoved = new LinkedList<CBRCase>();
	
		boolean changes = true;
		while(changes)
		{	changes = false;
			Collection<CBRCase> casesToBeRemoved = new LinkedList<CBRCase>();
			
			sc.computeCompetenceModel(new ICFSolvesFunction(), simConfig, localCases);
			coverageSets = sc.getCoverageSets();
			reachabilitySets = sc.getReachabilitySets();
	
			for(CBRCase c: localCases)
			{	Collection<CBRCase> coverageSet = coverageSets.get(c);
				Collection<CBRCase> reachabilitySet = reachabilitySets.get(c);
				if(reachabilitySet.size() > coverageSet.size())
				{	casesToBeRemoved.add(c);
					changes = true;
				}
			}
	
			allCasesToBeRemoved.addAll(casesToBeRemoved);
			localCases.removeAll(casesToBeRemoved);
			ProgressController.step(this.getClass());
		}
		ProgressController.finish(this.getClass());
		return allCasesToBeRemoved;
	}	
}