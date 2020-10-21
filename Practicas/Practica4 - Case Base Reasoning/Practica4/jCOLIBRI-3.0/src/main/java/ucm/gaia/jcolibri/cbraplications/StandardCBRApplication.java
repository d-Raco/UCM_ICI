package ucm.gaia.jcolibri.cbraplications;

import ucm.gaia.jcolibri.cbrcore.CBRCaseBase;
import ucm.gaia.jcolibri.cbrcore.CBRQuery;
import ucm.gaia.jcolibri.exception.ExecutionException;

/**
 * Defines the method of an standard CBR application.
 * It is composed by:
 * <ul>
 * <li>A configuration method to set up the application.
 * <li>A preCycle that loads cases and prepares the application to run.
 * <li>The cycle method that runs a CBR step using the given query.
 * <li>A postCycle in charge of finishing the application.
 * </ul>
 * 
 * @author Juan A. Recio-García
 *
 */
public interface StandardCBRApplication
{
	/**
	 * Configures the application: case base, connectors, etc.
	 * @throws ExecutionException
	 */
    void configure() throws ExecutionException;

    /**
     * Runs the precyle where typically cases are read and organized into a case base. 
     * @return The created case base with the cases in the storage.
     * @throws ExecutionException
     */
    CBRCaseBase preCycle() throws ExecutionException;

    /**
     * Executes a CBR cycle with the given query.
     * @throws ExecutionException
     */
    void cycle(CBRQuery query) throws ExecutionException;

    /**
     * Runs the code to shutdown the application. Typically it closes the connector.
     * @throws ExecutionException
     */
    void postCycle() throws ExecutionException;
}
