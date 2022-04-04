/**
 * Equal.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 28/10/2007
 */
package ucm.gaia.jcolibri.method.retrieve.FilterBasedRetrieval.predicates;

import java.util.Set;

import es.ucm.fdi.gaia.ontobridge.OntoBridge;
import ucm.gaia.jcolibri.datatypes.Instance;
import ucm.gaia.jcolibri.exception.NoApplicableFilterPredicateException;
import ucm.gaia.jcolibri.util.OntoBridgeSingleton;

/**
 * Predicate that compares if two objects (that must be Instance typed) are compatible.
 * Compatible means that the Least-Common-Subsumer of the query and the case instances
 * is the direct parent of the query. Informally, it means that que case is "under" 
 * the query in the ontology tree.
 * Only applicable to Instances. 
 * 
 * @author Juan A. Recio-Garcia
 * @author Developed at University College Cork (Ireland) in collaboration with Derek Bridge.
 * @version 1.0
 * @see FilterBasedRetrievalMethod
 * @see FilterConfig
 */
public class OntologyCompatible implements FilterPredicate
{
    public boolean compute(Object caseObject, Object queryObject) throws NoApplicableFilterPredicateException
    {
	if((caseObject == null)&&(queryObject==null))
	    return true;
	else if(caseObject == null)
	    return false;
	else if(queryObject == null)
	    return true;
	else if (! (caseObject instanceof Instance))
	    throw new NoApplicableFilterPredicateException(this.getClass(), caseObject.getClass());
	else if (! (queryObject instanceof Instance))
	    throw new NoApplicableFilterPredicateException(this.getClass(), queryObject.getClass());
	else 
	{
	    Instance caseInstance = (Instance) caseObject;
	    Instance queryInstance = (Instance) queryObject;
	    OntoBridge ob = OntoBridgeSingleton.getOntoBridge();

	    Set<String> lcs = ob.LCS(caseInstance.toString(), queryInstance.toString());
	    Set<String> directParents = ob.LCS(queryInstance.toString(), queryInstance.toString());
	    
	    lcs.retainAll(directParents);
	    return !lcs.isEmpty();

	}
    }

}
