package es.ucm.fdi.ici.c1920.practica4.grupo01;

import ucm.gaia.jcolibri.method.retrieve.NNretrieval.similarity.StandardGlobalSimilarityFunction;

public class GhostsGlobalSimilarityFunction extends StandardGlobalSimilarityFunction 
{
    /**
     * Hook method that must be implemented by subclasses returned the global similarity value.
     * @param values of the similarity of the sub-attributes
     * @param weights of the sub-attributes
     * @param numberOfvalues (or sub-attributes) that were obtained (some subattributes may not compute for the similarity).
     * @return a value between [0..1]
     */

	public double computeSimilarity(double[] values, double[] weigths, int ivalue)
	{
		if(values[0] == 0 || values[1] == 0)
			return 0;
		
		double acum = 0;
		double weigthsAcum = 0;
		for(int i = 2; i < ivalue; i++)
		{
			acum += values[i] * weigths[i];
			weigthsAcum += weigths[i];
		}
		
		return acum/weigthsAcum;
	}
}