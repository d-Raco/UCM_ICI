package ucm.gaia.jcolibri.method.maintenance.algorithms;


import org.apache.logging.log4j.LogManager;
import ucm.gaia.jcolibri.cbrcore.CBRCase;
import ucm.gaia.jcolibri.exception.InitializingException;
import ucm.gaia.jcolibri.method.maintenance.AbstractCaseBaseEditMethod;
import ucm.gaia.jcolibri.method.maintenance.CompetenceModel;
import ucm.gaia.jcolibri.method.maintenance.solvesFunctions.CBESolvesFunction;
import ucm.gaia.jcolibri.method.reuse.classification.KNNClassificationConfig;
import ucm.gaia.jcolibri.util.ProgressController;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Provides the ability to run the RC case base editing algorithm 
 * on a case base to eliminate redundancy.
 * 
 * @author Lisa Cummins
 * @author Derek Bridge
 * 18/05/07
 */
public class RCRedundancyRemoval extends AbstractCaseBaseEditMethod {
	
	/**
	 * Simulates the RC case base editing algorithm, returning the cases
	 * that would be deleted by the algorithm.
	 * @param cases The group of cases on which to perform editing.
	 * @param simConfig The similarity configuration for these cases.
	 * @return the list of cases that would be deleted by the 
	 * RC algorithm.
	 */
	public List<CBRCase> retrieveCasesToDelete(Collection<CBRCase> cases, KNNClassificationConfig simConfig)
	{	/* 
		 * RC Algorithm:
		 *	
		 * T: Original training cases
		 * CM: Competence Model
		 * RC(c): Sum_c' E CoverageSet(C) (1/|ReachabilitySet(c')|)
		 * 
		 * Edit(T,CM,RC):
		 * 
		 * R-Set = RENN(T) {that is, repeated ENN}
		 * (Not included here, RENN performed separately)
		 * E-Set = {}
		 * While R-Set is not empty
		 * 		c = Next case in R-Set according to RC
		 * 		E-Set = E-Set U {c}
		 * 		R-Set = R-Set � CoverageSet(c)
		 * 		Update(CM)
		 * EndWhile
		 * 
		 * Return (E-Set)
		 */
	    ProgressController.init(this.getClass(),"RC Redundancy Removal", ProgressController.UNKNOWN_STEPS);
		List<CBRCase> localCases = new LinkedList<CBRCase>();
		for(CBRCase c: cases)
		{	localCases.add(c);
		}
			
		CompetenceModel sc = new CompetenceModel();
		
		LinkedList<CBRCase> keepCases = new LinkedList<CBRCase>();
		
		while(localCases.size() > 0)
		{	double topRCScore = 0.0;
			CBRCase topRCCase = null;

			sc.computeCompetenceModel(new CBESolvesFunction(), simConfig, localCases);
			
			try
			{   for(CBRCase c: localCases)
			    {	double rcScore = 0.0;
			    	Collection<CBRCase> cCov = sc.getCoverageSet(c);
			    	for(CBRCase c1: cCov)
			    	{	rcScore += (1/(double)sc.getReachabilitySet(c1).size());
			    	}
			    	if(rcScore > topRCScore)
			    	{	topRCScore = rcScore;
			    		topRCCase = c;
			    	}
			    }
			    
			    keepCases.add(topRCCase);
			    
			    Collection<CBRCase> cSet = sc.getCoverageSet(topRCCase);
			    Collection<CBRCase> toRemove = new LinkedList<CBRCase>();
			    for(CBRCase c: cSet)
			    {	toRemove.add(c);
			    }
			    localCases.removeAll(toRemove);
			} catch (InitializingException e)
			{   LogManager.getLogger().error(e);
			}
			ProgressController.step(this.getClass());
		}
		
		//Add all cases that are not being kept to the list of deleted cases
		List<CBRCase> allCasesToBeRemoved = new LinkedList<CBRCase>();
		for(CBRCase c: cases)
		{	if(!keepCases.contains(c))
				allCasesToBeRemoved.add(c);
		}
		ProgressController.finish(this.getClass());
		return allCasesToBeRemoved;
	}
}