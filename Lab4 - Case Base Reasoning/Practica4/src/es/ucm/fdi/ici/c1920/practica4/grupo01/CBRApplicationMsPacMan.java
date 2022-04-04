package es.ucm.fdi.ici.c1920.practica4.grupo01;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import ucm.gaia.jcolibri.cbrcore.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import java.util.Random;

import es.ucm.fdi.ici.c1920.practica4.grupo01.Constants.CASE_TYPE;
import pacman.game.Game;
import pacman.game.Constants.MOVE;
import ucm.gaia.jcolibri.casebase.LinealCaseBase;
import ucm.gaia.jcolibri.cbraplications.StandardCBRApplication;
import ucm.gaia.jcolibri.connector.DataBaseConnector;
import ucm.gaia.jcolibri.connector.PlainTextConnector;
import ucm.gaia.jcolibri.exception.ExecutionException;
import ucm.gaia.jcolibri.method.retrieve.RetrievalResult;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.NNConfig;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.NNScoringMethod;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.similarity.global.Average;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Equal;
import ucm.gaia.jcolibri.method.retrieve.NNretrieval.similarity.local.Interval;
import ucm.gaia.jcolibri.method.retrieve.selection.SelectCases;
import ucm.gaia.jcolibri.util.FileIO;

public class CBRApplicationMsPacMan implements StandardCBRApplication
{
	private Vector<CBRCase> _buffer = new Vector<CBRCase>();
	private Collection<CBRCase> _casesToLearn = new ArrayList<CBRCase>();;
	private Random _rnd = new Random();
	
	private MsPacMan _MsPacMan;
	public CBRApplicationMsPacMan(MsPacMan pacman)
	{
		_MsPacMan = pacman;
	}

	private Game _game;
	public void setGame(Game g) { _game = g; }
	
	private PlainTextConnector _connector;
	private LinealCaseBase _caseBase;
	
	public void configure() throws ExecutionException
	{
		try
		{
			_connector = new PlainTextConnector();
			_connector.initFromXMLfile(FileIO.findFile("src/es/ucm/fdi/ici/c1920/practica4/grupo01/data/MsPacmanTextConnector.xml"));
			_caseBase = new LinealCaseBase();
			
		} 
		catch(Exception e)
		{
			throw new ExecutionException(e);
		}
	}
	
	private Collection<CBRCase> _chasingCases = new ArrayList<CBRCase>();
	private Collection<CBRCase> _neutralCases = new ArrayList<CBRCase>();
	private Collection<CBRCase> _runawayCases = new ArrayList<CBRCase>();
	
	public CBRCaseBase preCycle() throws ExecutionException
	{
		_caseBase.init(_connector);
		java.util.Collection<CBRCase> cases = _caseBase.getCases();
		
		
		for(CBRCase c : cases)
		{
			//System.out.println(c);
			
			MsPacManDescription desc = (MsPacManDescription) c.getDescription();
			if(desc.getType() == CASE_TYPE.RUN)
				_runawayCases.add(c);
			else if (desc.getType() == CASE_TYPE.CHASE) 
				_chasingCases.add(c);
			else
				_neutralCases.add(c);
				
		}
			
		return _caseBase;
	}
	
	public void cycle(CBRQuery query) throws ExecutionException
	{
		NNConfig simConfig = new NNConfig();
		
		//-------------------------------------------------------------------------------------------------------------------
		
		simConfig.setDescriptionSimFunction(new MsPacManGlobalSimilarityFunction());
		//simConfig.setDescriptionSimFunction(new Average());
		
		simConfig.addMapping(new Attribute("msPacManNode", MsPacManDescription.class), new Equal());

		simConfig.addMapping(new Attribute("msPacManLastMove", MsPacManDescription.class), new Equal());
		
		simConfig.addMapping(new Attribute("moveToBlinky", MsPacManDescription.class), new Equal());
		simConfig.addMapping(new Attribute("moveToPinky", MsPacManDescription.class), new Equal());
		simConfig.addMapping(new Attribute("moveToInky", MsPacManDescription.class), new Equal());
		simConfig.addMapping(new Attribute("moveToSue", MsPacManDescription.class), new Equal());
		
		simConfig.addMapping(new Attribute("blinkyDistance", MsPacManDescription.class), new Equal());
		simConfig.addMapping(new Attribute("pinkyDistance", MsPacManDescription.class), new Equal());
		simConfig.addMapping(new Attribute("inkyDistance", MsPacManDescription.class), new Equal());
		simConfig.addMapping(new Attribute("sueDistance", MsPacManDescription.class), new Equal());
		
		simConfig.addMapping(new Attribute("blinkyEdible", MsPacManDescription.class), new Equal());
		simConfig.addMapping(new Attribute("pinkyEdible", MsPacManDescription.class), new Equal());
		simConfig.addMapping(new Attribute("inkyEdible", MsPacManDescription.class), new Equal());
		simConfig.addMapping(new Attribute("sueEdible", MsPacManDescription.class), new Equal());
		
		Attribute ClosestPills1 = new Attribute("closestPills1", MsPacManDescription.class);
		simConfig.addMapping(ClosestPills1, new Equal());
		simConfig.setWeight(ClosestPills1, 0.1);
		
		Attribute ClosestPills2 = new Attribute("closestPills2", MsPacManDescription.class);
		simConfig.addMapping(ClosestPills2, new Equal());
		simConfig.setWeight(ClosestPills2, 0.1);
		
		Attribute ClosestPills3 = new Attribute("closestPills3", MsPacManDescription.class);
		simConfig.addMapping(ClosestPills3, new Equal());
		simConfig.setWeight(ClosestPills3, 0.1);
		
		Attribute ClosestPills4 = new Attribute("closestPills4", MsPacManDescription.class);
		simConfig.addMapping(ClosestPills4, new Equal());
		simConfig.setWeight(ClosestPills4, 0.1);

		//-------------------------------------------------------------------------------------------------------------------
		
		Collection<RetrievalResult> eval;
		
		if(((MsPacManDescription)query.getDescription()).getType() == CASE_TYPE.RUN)
			eval = NNScoringMethod.evaluateSimilarity(_runawayCases, query, simConfig);
		else if (((MsPacManDescription)query.getDescription()).getType() == CASE_TYPE.CHASE) 
			eval = NNScoringMethod.evaluateSimilarity(_chasingCases, query, simConfig);
		else
			eval = NNScoringMethod.evaluateSimilarity(_neutralCases, query, simConfig);
		
		
		eval = SelectCases.selectTopKRR(eval, 10);
		
		Iterator<RetrievalResult> it = eval.iterator();
		RetrievalResult[] casos = new RetrievalResult[eval.size()];
		int i = 0;
		while(it.hasNext())
		{
			casos[i] = it.next(); 
			i++;
		}
		
		CBRCase newCase = new CBRCase();
		newCase.setDescription(query.getDescription());
		newCase.setSolution(new MsPacManSolution());
		newCase.setJustificationOfSolution(new MsPacManJustification());
		newCase.setResult(new MsPacManResult());
		
		MOVE finalMove;
		if(casos.length <= 0)
		{
			//movimiento random
			MOVE[] possibleMoves = _game.getPossibleMoves(_game.getPacmanCurrentNodeIndex(), _game.getPacmanLastMoveMade());
			finalMove = possibleMoves[_rnd.nextInt(possibleMoves.length)];
		}
		else 
		{
			//movimiento solucion
			finalMove = chooseCaseMove(casos);
		}
		
		_MsPacMan.setSolution(finalMove);
		
		((MsPacManResult) newCase.getResult()).setType(((MsPacManDescription)query.getDescription()).getType());
		((MsPacManResult) newCase.getResult()).setEvaluation(0f);	
		
		((MsPacManSolution) newCase.getSolution()).setType(((MsPacManDescription)query.getDescription()).getType());
		((MsPacManSolution) newCase.getSolution()).setMoveMade(finalMove);
		
		((MsPacManJustification) newCase.getJustificationOfSolution()).setType(((MsPacManDescription)query.getDescription()).getType());		
		((MsPacManJustification) newCase.getJustificationOfSolution()).setDead(false);
		((MsPacManJustification) newCase.getJustificationOfSolution()).setAchivementMade(false);		
		((MsPacManJustification) newCase.getJustificationOfSolution()).setTotalPoints(0);
		
		_buffer.add(newCase);
	}
	
	private MOVE chooseCaseMove(RetrievalResult[] casos)
	{
		int top = 0,
			left = 0,
			bottom = 0,
			right = 0;
		
		MOVE move;
		
		int size = casos.length;
		for(int i = 0; i < size && casos[i] != null; i++)
		{
			MsPacManSolution s = (MsPacManSolution) casos[i].get_case().getSolution();
			MsPacManResult r = (MsPacManResult) casos[i].get_case().getResult();
			switch(s.getMoveMade())
			{
				case RIGHT:
					right += r.getEvaluation();
					break;
				case UP:
					top += r.getEvaluation();
					break;
				case LEFT:
					left += r.getEvaluation();
					break;
				case DOWN:
					bottom += r.getEvaluation();
					break;
			}
		}
		
		if(top != 0 && top >= left && top >= bottom && top >= right)
			move = MOVE.UP;
		else if (left != 0 && left >= top && left >= bottom && left >= right)
			move = MOVE.LEFT;
		else if(bottom != 0 && bottom >= top && bottom >= left && bottom >= right)
			move = MOVE.DOWN;
		else if(right != 0 && right >= top && right >= left && right >= bottom)
			move = MOVE.RIGHT;
		else
		{
			MOVE[] possibleMoves = _game.getPossibleMoves(_game.getPacmanCurrentNodeIndex(), _game.getPacmanLastMoveMade());
			move = possibleMoves[_rnd.nextInt(possibleMoves.length)];
		}
		
		return move;
	}
	
	public void updateBuffer(int points)
	{
		for(CBRCase c : _buffer)
		{
			int newPoints = ((MsPacManJustification) c.getJustificationOfSolution()).getTotalPoints();
			newPoints += points;
			
			((MsPacManJustification) c.getJustificationOfSolution()).setTotalPoints(newPoints);
			
			float evaluation = ((MsPacManResult) c.getResult()).getEvaluation();

			if(evaluation + points / 10 <= 100)
				evaluation += points / 10;
			else
				evaluation = 100;
			
			((MsPacManResult) c.getResult()).setEvaluation(evaluation);
		}
		
		if(_buffer.size() > 20)
		{
			CBRCase c = _buffer.elementAt(0);
			
			if(((MsPacManSolution) c.getSolution()).getType() == CASE_TYPE.RUN || 
				((MsPacManSolution) c.getSolution()).getType() == CASE_TYPE.CHASE && ((MsPacManJustification) c.getJustificationOfSolution()).getTotalPoints() > 20 ||
				((MsPacManSolution) c.getSolution()).getType() == CASE_TYPE.NEUTRAL && ((MsPacManJustification) c.getJustificationOfSolution()).getTotalPoints() > 5)
			{
				((MsPacManJustification) c.getJustificationOfSolution()).setAchivementMade(true);
				
				float evaluation = ((MsPacManResult) c.getResult()).getEvaluation();
				
				if(evaluation + 30 <= 100)
					evaluation += 30;
				else
					evaluation = 100;
				
				((MsPacManResult) c.getResult()).setEvaluation(evaluation);
			}
			else
			{
				float evaluation = ((MsPacManResult) c.getResult()).getEvaluation();
				
				if(evaluation - 30 >= -100)
					evaluation -= 30;
				else
					evaluation = -100;
				
				((MsPacManResult) c.getResult()).setEvaluation(evaluation);
				
				((MsPacManJustification) c.getJustificationOfSolution()).setAchivementMade(false);
			}
				
			removeCaseFromBuffer(c);
		}
	}
	
	public void MsPacManDead(int points)
	{
		for(CBRCase c : _buffer)
		{
			
			int newPoints = ((MsPacManJustification) c.getJustificationOfSolution()).getTotalPoints();
			newPoints += points;
			
			((MsPacManJustification) c.getJustificationOfSolution()).setTotalPoints(newPoints);
			
			((MsPacManJustification) c.getJustificationOfSolution()).setDead(true);
			
			float evaluation = ((MsPacManResult) c.getResult()).getEvaluation();
			
			if(evaluation + points / 10 <= 100)
				evaluation += points / 10;
			else
				evaluation = 100;
			
			if(evaluation - 70 >= -100)
				evaluation -= 70;
			else
				evaluation = -100;
			
			if(( ((MsPacManSolution) c.getSolution()).getType() == CASE_TYPE.CHASE && ((MsPacManJustification) c.getJustificationOfSolution()).getTotalPoints() > 20 ||
				((MsPacManSolution) c.getSolution()).getType() == CASE_TYPE.NEUTRAL && ((MsPacManJustification) c.getJustificationOfSolution()).getTotalPoints() > 5))
				{
					((MsPacManJustification) c.getJustificationOfSolution()).setAchivementMade(true);

					if(evaluation + 30 <= 100)
						evaluation += 30;
					else
						evaluation = 100;
				}
			else if(((MsPacManSolution) c.getSolution()).getType() == CASE_TYPE.RUN)
				((MsPacManJustification) c.getJustificationOfSolution()).setAchivementMade(false);
			
			((MsPacManResult) c.getResult()).setEvaluation(evaluation);
			
			learnCase(c);
			
		}
		
		_buffer.clear();
	}
	
	public void removeCaseFromBuffer(CBRCase c)
	{
		learnCase(c);
		_buffer.remove(c);
	}
	
	public void learnCase(CBRCase c)
	{
		if((((MsPacManResult) c.getResult()).getEvaluation() > 50))
			_casesToLearn.add(c);
	}
	
	public void postCycle() throws ExecutionException
	{
		MsPacManDead(_game.getScore() - _MsPacMan.getLastScore());
		_caseBase.learnCases(_casesToLearn);
		this._caseBase.close();
	}
}
