/**
 * NNretrievalMethod.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 03/01/2007
 */
package ucm.gaia.jcolibri.method.retrieve.NNretrieval;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import ucm.gaia.jcolibri.cbrcore.CBRCase;
import ucm.gaia.jcolibri.cbrcore.CBRQuery;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.similarity.GlobalSimilarityFunction;
import ucm.gaia.jcolibri.method.retrieve.RetrievalResult;
import ucm.gaia.jcolibri.util.ProgressController;

/**
 * Performs a Nearest Neighbor numeric scoring comparing attributes. 
 * It uses global similarity functions to compare compound attributes 
 * (CaseComponents) and 
 * local similarity functions to compare simple attributes.
 * The configuration of this method is stored in the NNConfig object.
 * @author Juan A. Recio-Garc�a
 * @version 2.0
 * @see NNConfig
 */
public class NNScoringMethod {

    /**
    * Performs the NN scoring over a collection of cases comparing them with a query.
    * This zmethod is configured through the NNConfig object.
    */
	public static List<RetrievalResult> evaluateSimilarity(Collection<CBRCase> cases, CBRQuery query, NNConfig simConfig) {

        ProgressController.init(NNScoringMethod.class,"Numeric Similarity Computation", cases.size());

        GlobalSimilarityFunction gsf = simConfig.getDescriptionSimFunction();

        // Parallel stream
	    List<RetrievalResult> res = cases.parallelStream()
                .map(c -> new RetrievalResult(c, gsf.compute(c.getDescription(), query.getDescription(), c, query, simConfig)))
                .collect(Collectors.toList());

	    // Sort the result
        res.sort(RetrievalResult::compareTo);

        ProgressController.finish(NNScoringMethod.class);

		return res;
	}
}
