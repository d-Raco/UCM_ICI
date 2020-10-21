/**
 * AskingAndProposingPreferenceElicitation.java
 * jCOLIBRI2 framework. 
 * @author Juan A. Recio-Garc�a.
 * GAIA - Group for Artificial Intelligence Applications
 * http://gaia.fdi.ucm.es
 * 05/11/2007
 */
package ucm.gaia.jcolibri.extensions.recommendation.askingAndProposing;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ucm.gaia.jcolibri.cbrcore.Attribute;
import ucm.gaia.jcolibri.cbrcore.CBRCase;
import ucm.gaia.jcolibri.cbrcore.CBRQuery;
import ucm.gaia.jcolibri.exception.ExecutionException;
import ucm.gaia.jcolibri.extensions.recommendation.navigationByAsking.ObtainQueryWithAttributeQuestionMethod;
import ucm.gaia.jcolibri.extensions.recommendation.navigationByAsking.SelectAttributeMethod;
import ucm.gaia.jcolibri.extensions.recommendation.navigationByProposing.CriticalUserChoice;
import ucm.gaia.jcolibri.extensions.recommendation.navigationByProposing.queryElicitation.MoreLikeThis;

/**
 * Method that implements the Preference elicitation task for the Expert Clerk system.<br>
 * See recommender 8 for details.<br>
 * In NbA it elicits the query asking for the value of an attribute.<br>
 * In NbP it uses the MoreLikeThis method.
 * 
 * @author Juan A. Recio-Garcia
 * @author Developed at University College Cork (Ireland) in collaboration with Derek Bridge.
 * @version 1.0
 */
public class AskingAndProposingPreferenceElicitation
{
    
    private static DisplayCasesIfNumberAndChangeNavigation.NavigationMode mode = DisplayCasesIfNumberAndChangeNavigation.NavigationMode.NBA;
    
    /** 
     * Changes the navigation type.
     */
    public static void changeTo(DisplayCasesIfNumberAndChangeNavigation.NavigationMode _mode)
    {
	mode = _mode;
    }
    
    /**
     * Executes the preference elicitation. <br>
     * If NbA mode it obtains a new query using the ObtainQueryWithAttributeQuestionMethod.<br>
     * If NBP mode it revises the query using MoreLikeThis.<br> 
     * @param query to elicit
     * @param cases is the working cases set
     * @param sam is the method used to obtain the next attribute to ask (only in NbA mode).
     * @param cuc is the user critique in NbP mode.
     * @throws ExecutionException if any error.
     */
    public static void doPreferenceElicitation(CBRQuery query, List<CBRCase> cases, SelectAttributeMethod sam, CriticalUserChoice cuc) throws ExecutionException
    {
	if(mode == DisplayCasesIfNumberAndChangeNavigation.NavigationMode.NBA)
	{
	    Attribute att = sam.getAttribute(cases, query);
	    Map<Attribute,String> labels = new HashMap<Attribute,String>();
	    ObtainQueryWithAttributeQuestionMethod.obtainQueryWithAttributeQuestion(query, att, labels,cases);
	} 
	else if(mode == DisplayCasesIfNumberAndChangeNavigation.NavigationMode.NBP)
	{
	    new MoreLikeThis().reviseQuery(query, cuc.getSelectedCase(), cases);
	}

    }
}
