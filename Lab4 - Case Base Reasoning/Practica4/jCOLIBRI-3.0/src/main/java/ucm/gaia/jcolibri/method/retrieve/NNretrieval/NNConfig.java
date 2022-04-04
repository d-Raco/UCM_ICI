/**
 * NNConfig.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 03/01/2007
 */
package ucm.gaia.jcolibri.method.retrieve.NNretrieval;


import ucm.gaia.jcolibri.cbrcore.Attribute;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.similarity.GlobalSimilarityFunction;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.similarity.LocalSimilarityFunction;

import java.util.HashMap;

/**
 * This class stores the configuration for the NN retrieval method.
 * It stores:
 * <ul>
 * <li>The global similarity function for the description.
 * <li>Global similarity functions for each compound attribute (CaseComponents excepting the description).
 * <li>Local similairity functions for each simple attribute.
 * <li>Weight for each attribute. (1 by default)
 * </ul>
 * @author Juan A. Recio-Garcia
 * @version 1.0
 */
public class NNConfig{
		
	private HashMap<Attribute, LocalSimilarityFunction> maplocal = new HashMap<>();
	private HashMap<Attribute, GlobalSimilarityFunction> mapglobal = new HashMap<>();
	private HashMap<Attribute, Double> mapweight = new HashMap<>();
	
	private GlobalSimilarityFunction descriptionSimFunction;
	

	public NNConfig()
	{
	}

	/**
	 * @return Returns the description similarity function.
	 */
	public GlobalSimilarityFunction getDescriptionSimFunction() {
		return descriptionSimFunction;
	}
	/**
	 * @param descriptionSimFunction The description similarity function. to set.
	 */
	public void setDescriptionSimFunction(GlobalSimilarityFunction descriptionSimFunction) {
		this.descriptionSimFunction = descriptionSimFunction;
	}

	/**
	 * Sets the local similarity function to apply to a simple attribute.
	 */
	public void addMapping(Attribute attribute, LocalSimilarityFunction similFunction)
	{
		maplocal.put(attribute, similFunction);
	}
	
	/**
	 * Gets the local similarity function configured for a given simple attribute.
	 */
	public LocalSimilarityFunction getLocalSimilFunction(Attribute attribute)
	{
		return maplocal.get(attribute);
	}
	
	/**
	 * Sets the global similarity function to apply to a compound attribute.
	 */
	public void addMapping(Attribute attribute, GlobalSimilarityFunction similFunction)
	{
		mapglobal.put(attribute, similFunction);
	}
	
	/**
	 * Gets the global similarity function configured for a given compound attribute.
	 */
	public GlobalSimilarityFunction getGlobalSimilFunction(Attribute attribute)
	{
		return mapglobal.get(attribute);
	}
	
	/**
	 * Sets the weight for an attribute.
	 */
	public void setWeight(Attribute attribute, Double weight)
	{
		mapweight.put(attribute, weight);
	}
	
	/**
	 * Gets the weight for an attribute. If an attribute does not have a configured weight it returns 1 by default.
	 */
	public Double getWeight(Attribute attribute)
	{
		Double d = mapweight.get(attribute);
		if(d!= null)
			return d;
		else
			return new Double(1); 
	}
}
