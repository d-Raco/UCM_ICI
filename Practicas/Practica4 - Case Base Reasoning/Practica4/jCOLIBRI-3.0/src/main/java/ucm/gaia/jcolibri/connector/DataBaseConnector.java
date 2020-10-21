package ucm.gaia.jcolibri.connector;

import org.apache.logging.log4j.LogManager;
import java.net.URL;
import java.util.*;

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.w3c.dom.Document;

import ucm.gaia.jcolibri.cbrcore.CBRCase;
import ucm.gaia.jcolibri.cbrcore.CaseBaseFilter;
import ucm.gaia.jcolibri.cbrcore.CaseComponent;
import ucm.gaia.jcolibri.cbrcore.Connector;
import ucm.gaia.jcolibri.exception.AttributeAccessException;
import ucm.gaia.jcolibri.exception.InitializingException;
import ucm.gaia.jcolibri.util.FileIO;

/**
 * Implements a data base connector using the <a href="www.hibernate.org">Hibernate package</a>.
 * <p>
 * The configuration file follows the schema defined in
 * <a href="DataBaseConnector.xsd">/doc/configfilesSchemas/DataBaseConnector.xsd</a>:
 * <p>
 * <img src="DataBaseConnectorSchema.jpg">
 * <p> 
 * There are several examples that incrementally show how to use this connector: Test1, Test2, Test3, Test4 and Test5.
 * 
 * @author Juan Antonio Recio García
 * @version 3.0
 *
 */
public class DataBaseConnector implements Connector {

	private SessionFactory sessionFactory;
	private String descriptionClassName;
	private String solutionClassName;
	private String justOfSolutionClassName;
	private String resultClassName;
	
	
	/* (non-Javadoc)
	 * @see jcolibri.cbrcore.Connector#close()
	 */
	public void close() {}

	/* (non-Javadoc)
	 * @see jcolibri.cbrcore.Connector#deleteCases(java.util.Collection)
	 */
	public void deleteCases(Collection<CBRCase> cases) {}

	/* (non-Javadoc)
	 * @see jcolibri.cbrcore.Connector#initFromXMLfile(java.io.File)
	 */
	public void initFromXMLfile(URL file) throws InitializingException {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        //factory.setValidating(true);   
	        //factory.setNamespaceAware(true);
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        Document document = builder.parse( file.openStream() );
	        
	        String hcf = document.getElementsByTagName("HibernateConfigFile").item(0).getTextContent();
	        
	        String descriptionMapFile = document.getElementsByTagName("DescriptionMappingFile").item(0).getTextContent();
	        descriptionClassName = document.getElementsByTagName("DescriptionClassName").item(0).getTextContent();
	        
	        Configuration hbconfig = new Configuration();
	        hbconfig.configure(FileIO.findFile(hcf));
	        hbconfig.addURL(FileIO.findFile(descriptionMapFile));
	        
	        try{
		        String solutionMapFile = document.getElementsByTagName("SolutionMappingFile").item(0).getTextContent();
		        solutionClassName = document.getElementsByTagName("SolutionClassName").item(0).getTextContent();	 
		        hbconfig.addResource(solutionMapFile);
	        }catch(Exception e)
	        {
	        	LogManager.getLogger(this.getClass()).info("Case does not have solution");
	        }
	        
	        try{
		        String justOfSolutionMapFile = document.getElementsByTagName("JustificationOfSolutionMappingFile").item(0).getTextContent();
		        justOfSolutionClassName = document.getElementsByTagName("JustificationOfSolutionClassName").item(0).getTextContent();	 
		        hbconfig.addResource(justOfSolutionMapFile);
	        }catch(Exception e)
	        {
	        	LogManager.getLogger(this.getClass()).info("Case does not have justification of the solution");
	        }
	        
	        try{
		        String resultMapFile = document.getElementsByTagName("ResultMappingFile").item(0).getTextContent();
		        resultClassName = document.getElementsByTagName("ResultClassName").item(0).getTextContent();	 
		        hbconfig.addResource(resultMapFile);
	        }catch(Exception e)
	        {
	        	LogManager.getLogger(this.getClass()).info("Case does not have result");
	        }
	             
	        
	        sessionFactory = hbconfig.buildSessionFactory();
				
		} catch (Throwable ex) {
			throw new InitializingException(ex);
		}

	}

	/* (non-Javadoc)
	 * @see jcolibri.cbrcore.Connector#retrieveAllCases()
	 */
	public Collection<CBRCase> retrieveAllCases(){
		
		List<CBRCase> res = new ArrayList<>();

		try {

			Session session;
			Transaction transaction;
					
			List descList;
			HashMap<Object, CaseComponent> solList = null;
			HashMap<Object, CaseComponent> justSolList = null;
			HashMap<Object, CaseComponent> resList = null;
			
			if(solutionClassName != null)
			    solList = retrieveComponent(solutionClassName);

			if(justOfSolutionClassName != null)
			    justSolList = retrieveComponent(justOfSolutionClassName);

			if(resultClassName != null)
			    resList = retrieveComponent(resultClassName);

			session = sessionFactory.openSession();	
			transaction = session.beginTransaction();
			descList = session.createQuery("from "+ descriptionClassName).list();			
			transaction.commit();
			session.close();

			for(Iterator iter = descList.iterator(); iter.hasNext();) {
				CBRCase _case = new CBRCase();
				CaseComponent desc = (CaseComponent)iter.next();
				_case.setDescription(desc);
				
				if(solutionClassName != null) {

					CaseComponent cc = solList.get(desc.getIdAttribute().getValue(desc));
					if(cc != null)
						_case.setSolution(cc);

				}

				if(justOfSolutionClassName != null) {
					CaseComponent cc = justSolList.get(desc.getIdAttribute().getValue(desc));
					if(cc != null)
						_case.setJustificationOfSolution(cc);
				}

				if(resultClassName != null) {
					CaseComponent cc = resList.get(desc.getIdAttribute().getValue(desc));
					if(cc != null)
						_case.setResult(cc);
				}
						
				res.add(_case);
				
			}

		} catch (Exception e) {
			LogManager.getLogger().error(e);
		}

		LogManager.getLogger().info(res.size() + " cases read from the database.");
		return res;
	}

	private HashMap<Object, CaseComponent> retrieveComponent(String className) throws AttributeAccessException {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        HashMap<Object, CaseComponent> result = new HashMap<>();
        List list = session.createQuery("from " + className).list();

        transaction.commit();
        session.close();

        // TODO - Pasar a Java 8, Streams
        for(Object obj: list) {
            CaseComponent cc = (CaseComponent) obj;
            result.put(cc.getIdAttribute().getValue(cc), cc);
        }

        return result;
    }

	/* (non-Javadoc)
	 * @see jcolibri.cbrcore.Connector#retrieveSomeCases(jcolibri.cbrcore.CaseBaseFilter)
	 */
	public List<CBRCase> retrieveSomeCases(CaseBaseFilter filter) {
		return null;
	}

	/* (non-Javadoc)
	 * @see jcolibri.cbrcore.Connector#storeCases(java.util.Collection)
	 */
	public void storeCases(Collection<CBRCase> cases) {

	    for(CBRCase c: cases) {
		    saveOrUpdateCaseComponent(c.getDescription());
		    saveOrUpdateCaseComponent(c.getSolution());
		    saveOrUpdateCaseComponent(c.getJustificationOfSolution());
		    saveOrUpdateCaseComponent(c.getResult());
		}

		LogManager.getLogger().info(cases.size() + " cases stored into the database.");

	}

	private void saveOrUpdateCaseComponent(CaseComponent caseComponent) {

        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        if(caseComponent != null)
            session.saveOrUpdate(caseComponent);

        transaction.commit();
        session.close();

    }
}
