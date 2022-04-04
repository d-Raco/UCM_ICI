/**
 * DirectAttributeCopyMethod.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 05/01/2007
 */
package ucm.gaia.jcolibri.method.reuse;


import org.apache.logging.log4j.LogManager;
import ucm.gaia.jcolibri.cbrcore.Attribute;
import ucm.gaia.jcolibri.cbrcore.CBRCase;
import ucm.gaia.jcolibri.cbrcore.CBRQuery;
import ucm.gaia.jcolibri.cbrcore.CaseComponent;
import ucm.gaia.jcolibri.exception.AttributeAccessException;
import ucm.gaia.jcolibri.util.AttributeUtils;

import java.util.Collection;

/**
 * Copies the value of an attribute in the query to an attribute of a case 
 * @author Juan A. Recio-Garcia
 * @version 2.0
 *
 */
public class DirectAttributeCopyMethod {

	/**
	 * Copies the value of the querySource attribute into the caseDestination attribute of the cases. 
	 */
	public static void copyAttribute(Attribute querySource, Attribute caseDestination, CBRQuery query, Collection<CBRCase> cases)
	{
		Object queryValue = AttributeUtils.findValue(querySource, query);
		try {
			
			for(CBRCase c: cases)
			{
				CaseComponent cc = AttributeUtils.findBelongingComponent(caseDestination, c);
				caseDestination.setValue(cc, queryValue);
			}
		} catch (AttributeAccessException e) {
			LogManager.getLogger().error(e);
		}
	}
	
}
