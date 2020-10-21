package ucm.gaia.jcolibri.method.reuse.classification;

import ucm.gaia.jcolibri.cbrcore.CBRCase;
import ucm.gaia.jcolibri.cbrcore.CBRQuery;
import ucm.gaia.jcolibri.extensions.classification.ClassificationSolution;
import ucm.gaia.jcolibri.method.retrieve.RetrievalResult;

import java.util.Collection;
import java.util.List;

/**
 * Interface for providing the ability to classify a 
 * query by predicting its solution from supplied cases.
 * 
 * @author Derek Bridge
 * @author Lisa Cummins
 * 16/05/07
 */
public interface KNNClassificationMethod
{
    /**
     * Gets the predicted solution of the given cases according 
     * to the classification type.
     * @param cases a list of cases along with similarity scores.
     * @return Returns the predicted solution.
     */
    ClassificationSolution getPredictedSolution(Collection<RetrievalResult> cases);
    
    /**
     * Gets the predicted solution of the given cases according 
     * to the classification type and returns a case that has the
     * query description and the predicted solution.
     * @param query the query.
     * @param cases a list of cases along with similarity scores.
     * @return Returns a case with the query description as its 
     * description and the predicted solution as its solution. 
     */
    CBRCase getPredictedCase(CBRQuery query, Collection<RetrievalResult> cases);
}
