/**
 * SelectAttributeMethod.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 05/11/2007
 */
package ucm.gaia.jcolibri.extensions.recommendation.navigationByAsking;


import ucm.gaia.jcolibri.cbrcore.Attribute;
import ucm.gaia.jcolibri.cbrcore.CBRCase;
import ucm.gaia.jcolibri.cbrcore.CBRQuery;
import ucm.gaia.jcolibri.exception.ExecutionException;

import java.util.List;

/**
 * Interface for methods that select an attribute to be asked.
 * 
 * @author Juan A. Recio-Garcia
 * @author Developed at University College Cork (Ireland) in collaboration with Derek Bridge.
 * @version 1.0
 *
 */
public interface SelectAttributeMethod
{
    /**
     * Selects the attribute to be asked
     * @param cases list of working cases
     * @param query is the current query
     * @return selected attribute or null if there are not more attributes to ask.
     * @throws ExecutionException
     */
    public Attribute getAttribute(List<CBRCase> cases, CBRQuery query) throws ExecutionException;
}
