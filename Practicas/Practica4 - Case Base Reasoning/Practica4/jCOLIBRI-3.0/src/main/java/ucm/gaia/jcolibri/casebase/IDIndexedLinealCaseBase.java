package ucm.gaia.jcolibri.casebase;

import ucm.gaia.jcolibri.cbrcore.CBRCase;
import ucm.gaia.jcolibri.cbrcore.CBRCaseBase;
import ucm.gaia.jcolibri.cbrcore.CaseBaseFilter;
import ucm.gaia.jcolibri.cbrcore.Connector;
import ucm.gaia.jcolibri.exception.AttributeAccessException;

import java.util.Collection;
import java.util.HashMap;

/**
 * This is a modification of LinealCaseBase that also keeps an index of cases using their IDs. 
 * Internally it uses a hash table that relates each ID with its corresponding case.
 * It adds the method: getCase(Object ID)
 * 
 * @author Juan A. Recio-García
 *
 */
public class IDIndexedLinealCaseBase implements CBRCaseBase {

	private Connector connector;
	private Collection<CBRCase> cases;
	private HashMap<Object, CBRCase> index;

	/**
	 * Private method that executes the indexing of cases.
	 * @param cases
	 */
	private void indexCases(Collection<CBRCase> cases)
	{
		index = new HashMap<>();
		for(CBRCase c: cases)
		{
			try {
				Object o = c.getDescription().getIdAttribute().getValue(c.getDescription());
				index.put(o, c);
			} catch (AttributeAccessException e) { }
		}
	}
	
	/* (non-Javadoc)
	 * @see jcolibri.cbrcore.CBRCaseBase#init()
	 */
	public void init(Connector connector) {
		this.connector = connector;
		cases = this.connector.retrieveAllCases();	
		indexCases(cases);
			
	}
	
	/* (non-Javadoc)
	 * @see jcolibri.cbrcore.CBRCaseBase#close()
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
	public Collection<CBRCase> getCases(CaseBaseFilter filter) {return null;
	}


	/* (non-Javadoc)
	 * @see jcolibri.cbrcore.CBRCaseBase#learnCases(java.util.Collection)
	 */
	public void learnCases(Collection<CBRCase> cases) {
		connector.storeCases(cases);
		indexCases(cases);
		this.cases.addAll(cases);

	}

	/**
	 * Returns the case that corresponds with the id parameter.
	 */
	public CBRCase getCase(Object id)
	{
		return index.get(id);
	}

}
