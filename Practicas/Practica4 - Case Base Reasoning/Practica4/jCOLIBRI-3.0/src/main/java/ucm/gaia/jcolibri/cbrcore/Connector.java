package ucm.gaia.jcolibri.cbrcore;

import ucm.gaia.jcolibri.exception.InitializingException;

import java.util.Collection;

/**
 * Connector interface declares the methods required to access the cases stored in a persistence media.
 * jCOLIBRI splits the managing of cases into persistence media and in-memory organization. This interface defines
 * the access to de persistence and the CBRCaseBase interface defines the in-memory organization. Both interfaces
 * are related as the CBRCaseBase manages the Connector.
 * 
 * Implementations should read/write cases from Data Bases, Plain Text files, Ontologies, XML files, etc.
 * 
 * @author Juan A. Recio-Garc�a
 * @see CBRCaseBase
 */
public interface Connector {

	/**
	 * Initialices the connector with the given XML file
	 * 
	 * @param file XMl file with the settings
	 * @throws InitializingException Raised if the connector can not be initialezed.
	 */
	void initFromXMLfile(java.net.URL file) throws InitializingException;

	/**
	 * Cleanup any resource that the connector might be using, and suspends the
	 * service
	 */
	void close();

	/**
	 * Stores given classes on the storage media
	 * @param cases List of cases
	 */
	void storeCases(Collection<CBRCase> cases);

	/**
	 * Deletes given cases for the storage media
	 * @param cases List of cases
	 */
	void deleteCases(Collection<CBRCase> cases);

	/**
	 * Returns max cases without any special consideration
	 * @return The list of retrieved cases
	 */
	Collection<CBRCase> retrieveAllCases();

	/**
	 * Retrieves some cases depending on the filter. TODO.
	 */
	Collection<CBRCase> retrieveSomeCases(CaseBaseFilter filter);

}
