/**
 * SelectCases.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 24/11/2007
 */
package ucm.gaia.jcolibri.method.retrieve.selection;

import ucm.gaia.jcolibri.cbrcore.CBRCase;
import ucm.gaia.jcolibri.method.retrieve.RetrievalResult;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class that stores the selectAll and selectTopK methods.
 * 
 * @author Juan A. Recio-Garcia
 * @author Developed at University College Cork (Ireland) in collaboration with Derek Bridge.
 * @version 2.0
 */
public class SelectCases {

    /**
     * Selects all cases
     * @param cases to select
     * @return all cases
     */
    public static Collection<CBRCase> selectAll(Collection<RetrievalResult> cases) {

        List<CBRCase> res = cases.stream()
                .map(RetrievalResult::get_case)
                .collect(Collectors.toList());

	    return res;
    }
    
    /**
     * Selects top K cases
     * @param cases to select
     * @param k is the number of csaes to select
     * @return top k cases
     */
    public static Collection<CBRCase> selectTopK(Collection<RetrievalResult> cases, int k) {

        List<CBRCase> res = cases.stream()
                            .limit(k)
                            .map(RetrievalResult::get_case)
                            .collect(Collectors.toList());

        return res;
    }
    
    /**
     * Selects all cases but returns them into RetrievalResult objects
     * @param cases to select
     * @return all cases into RetrievalResult objects
     */
    public static Collection<RetrievalResult> selectAllRR(Collection<RetrievalResult> cases) {
	    return cases;
    }
    
    /**
     * Selects top k cases but returns them into RetrievalResult objects
     * @param cases to select
     * @return top k cases into RetrievalResult objects
     */
    public static Collection<RetrievalResult> selectTopKRR(Collection<RetrievalResult> cases, int k) {

        List<RetrievalResult> res = cases.stream()
                                        .limit(k)
                                        .collect(Collectors.toList());

        return res;
    }
}
