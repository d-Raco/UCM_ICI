package ucm.gaia.jcolibri.casebase;

import ucm.gaia.jcolibri.cbrcore.CBRCase;
import ucm.gaia.jcolibri.cbrcore.CBRCaseBase;
import ucm.gaia.jcolibri.cbrcore.CaseBaseFilter;
import ucm.gaia.jcolibri.cbrcore.Connector;

import java.util.Collection;

/**
 * Basic Lineal Case Base that stores cases into a Collection.
 * This class does not includes any kind of caching mechanism. 
 * That way, if you call to learn() or forget() cases are automatically stored/removed to/from the persistence media. 
 * This will be a performance problem if you plan to learn/forget in multiple steps. This case base is unrecommended for evaluation.
 * <p>
 * Depending on your requirements the CachedLinealCaseBase could be more suitable.
 * 
 * @author Juan A. Recio-García
 * @see CachedLinealCaseBase
 *
 */
public class LinealCaseBase implements CBRCaseBase {

	private Connector connector;
	private Collection<CBRCase> cases;

	public void init(Connector connector) {
		this.connector = connector;
		cases = this.connector.retrieveAllCases();	
	}
	
	/* (non-Javadoc)
	 * @see jcolibri.cbrcore.CBRCaseBase#deInit()
	 */
	public void close() {
		this.connector.close();

	}

	/* (non-Javadoc)
	 * @see jcolibri.cbrcore.CBRCaseBase#forgetCases(java.util.Collection)
	 */
	public void forgetCases(Collection<CBRCase> cases) {}

	/* (non-Javadoc)
	 * @see jcolibri.cbrcore.CBRCaseBase#getCases()
	 */
	public Collection<CBRCase> getCases() {
		return cases;
	}

	/* (non-Javadoc)
	 * @see jcolibri.cbrcore.CBRCaseBase#getCases(jcolibri.cbrcore.CaseBaseFilter)
	 */
	public Collection<CBRCase> getCases(CaseBaseFilter filter) {return null;}


	/* (non-Javadoc)
	 * @see jcolibri.cbrcore.CBRCaseBase#learnCases(java.util.Collection)
	 */
	public void learnCases(Collection<CBRCase> cases) {
		connector.storeCases(cases);
		this.cases.addAll(cases);

	}


}
