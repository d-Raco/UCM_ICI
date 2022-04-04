package ucm.gaia.jcolibri.cbrcore;

import ucm.gaia.jcolibri.exception.InitializingException;

import java.util.Collection;

/**
 * This interface defines the methods that at least any Case Base must implement
 * to be transparently used by the framework.
 * <p>
 * A Case Base is the in-memory organization of the cases. 
 * Cases are read from the persistence media an loaded into an implementation of this interface.
 * 
 * Further implementations will provide cache mechanisms, optimized organizations, etc.
 * 
 * @author Juan A. Recio-García
 */
public interface CBRCaseBase {

    /**
     * Initializes the case base. This methods recibes the connector that manages the persistence media.
     *
     */
    void init(Connector connector) throws InitializingException;

    /**
     * DeInitializes the case base.
     *
     */
    void close();

	/**
	 * Returns all the cases available on this case base
	 * 
	 * @return all the cases available on this case base
	 */
	Collection<CBRCase> getCases();
	
	
    /**
     * Returns some cases depending on the filter
     * @param filter a case base filter
     * @return a collection of cases
     */
    Collection<CBRCase> getCases(CaseBaseFilter filter);


	/**
	 * Adds a collection of new CBRCase objects to the current case base
	 * 
	 * @param cases to be added
	 */
	void learnCases(Collection<CBRCase> cases);

	/**
	 * Removes a collection of new CBRCase objects to the current case base
	 * 
	 * @param cases to be removed
	 */
	void forgetCases(Collection<CBRCase> cases);
}
