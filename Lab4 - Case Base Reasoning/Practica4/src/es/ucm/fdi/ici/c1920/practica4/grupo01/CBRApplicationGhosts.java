package es.ucm.fdi.ici.c1920.practica4.grupo01;

import ucm.gaia.jcolibri.cbrcore.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

import es.ucm.fdi.ici.c1920.practica4.grupo01.Constants.CASE_TYPE;
import pacman.game.Game;
import pacman.game.Constants.GHOST;
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

public class CBRApplicationGhosts implements StandardCBRApplication
{
	private Vector<CBRCase> _buffer = new Vector<CBRCase>();
	private Collection<CBRCase> _casesToLearn = new ArrayList<CBRCase>();;
	private Random _rnd = new Random();

	private Ghosts _Ghosts;
	public CBRApplicationGhosts(Ghosts g)
	{
		_Ghosts = g;
	}
	
	private GHOST _ghost;
	public void setGhost(GHOST g) { _ghost = g; }

	private Game _game;
	public void setGame(Game g) { _game = g; }

	
	private Connector _connector;
	private CBRCaseBase _caseBase;
	
	public void configure() throws ExecutionException
	{
		try
		{
			_connector = new PlainTextConnector();
			_connector.initFromXMLfile(FileIO.findFile("src/es/ucm/fdi/ici/c1920/practica4/grupo01/data/GhostsTextConnector.xml"));
			_caseBase = new LinealCaseBase();
			
		} 
		catch(Exception e)
		{
			throw new ExecutionException(e);
		}
	}
	
	private Collection<CBRCase> _blinkyChasingCases = new ArrayList<CBRCase>();
	private Collection<CBRCase> _blinkyRunawayCases = new ArrayList<CBRCase>();
	
	private Collection<CBRCase> _pinkyChasingCases = new ArrayList<CBRCase>();
	private Collection<CBRCase> _pinkyRunawayCases = new ArrayList<CBRCase>();
	
	private Collection<CBRCase> _inkyChasingCases = new ArrayList<CBRCase>();
	private Collection<CBRCase> _inkyRunawayCases = new ArrayList<CBRCase>();
	
	private Collection<CBRCase> _sueChasingCases = new ArrayList<CBRCase>();
	private Collection<CBRCase> _sueRunawayCases = new ArrayList<CBRCase>();
	
	public CBRCaseBase preCycle() throws ExecutionException
	{
		_caseBase.init(_connector);
		java.util.Collection<CBRCase> cases = _caseBase.getCases();
		
		for(CBRCase c: cases)
		{
			GhostsDescription desc = (GhostsDescription) c.getDescription();
			if(desc.getName().equals("BLINKY"))
			{
				if(desc.getType() == CASE_TYPE.RUN)
					_blinkyRunawayCases.add(c);
				else
					_blinkyChasingCases.add(c);
			}
			
			if(desc.getName().equals("PINKY"))
			{
				if(desc.getType() == CASE_TYPE.RUN)
					_pinkyRunawayCases.add(c);
				else
					_pinkyChasingCases.add(c);
			}
			
			if(desc.getName().equals("INKY"))
			{
				if(desc.getType() == CASE_TYPE.RUN)
					_inkyRunawayCases.add(c);
				else
					_inkyChasingCases.add(c);
			}
			
			if(desc.getName().equals("SUE"))
			{
				if(desc.getType() == CASE_TYPE.RUN)
					_sueRunawayCases.add(c);
				else
					_sueChasingCases.add(c);
			}
		}
		return _caseBase;
	}
	
	public void cycle(CBRQuery query) throws ExecutionException
	{
		NNConfig simConfig = new NNConfig();
		
		//---------------------------------------------------------------------------------------------------------
		
		simConfig.setDescriptionSimFunction(new GhostsGlobalSimilarityFunction());
		
		simConfig.addMapping(new Attribute("nodeMsPacMan", GhostsDescription.class), new Equal());
		
		simConfig.addMapping(new Attribute("msPacManLastMove", GhostsDescription.class), new Equal());
		
		simConfig.addMapping(new Attribute("nodeGhost1", GhostsDescription.class), new Equal());		
		simConfig.addMapping(new Attribute("nodeGhost2", GhostsDescription.class), new Equal());
		simConfig.addMapping(new Attribute("nodeGhost3", GhostsDescription.class), new Equal());
		simConfig.addMapping(new Attribute("nodeGhost4", GhostsDescription.class), new Equal());
		
		simConfig.addMapping(new Attribute("distanceGhost1", GhostsDescription.class), new Interval(20));
		simConfig.addMapping(new Attribute("distanceGhost2", GhostsDescription.class), new Interval(20));
		simConfig.addMapping(new Attribute("distanceGhost3", GhostsDescription.class), new Interval(20));
		simConfig.addMapping(new Attribute("distanceGhost4", GhostsDescription.class), new Interval(20));
		
		simConfig.addMapping(new Attribute("edibleGhost1", GhostsDescription.class), new Equal());
		simConfig.addMapping(new Attribute("edibleGhost2", GhostsDescription.class), new Equal());
		simConfig.addMapping(new Attribute("edibleGhost3", GhostsDescription.class), new Equal());
		simConfig.addMapping(new Attribute("edibleGhost4", GhostsDescription.class), new Equal());
		
		//---------------------------------------------------------------------------------------------------------
		
		Collection<RetrievalResult> eval = null;
		
		GhostsDescription desc = (GhostsDescription) query.getDescription();
		
		if(desc.getName().equals("BLINKY"))
		{
			if(desc.getType() == CASE_TYPE.RUN)
				eval = NNScoringMethod.evaluateSimilarity(_blinkyRunawayCases, query, simConfig);
			else
				eval = NNScoringMethod.evaluateSimilarity(_blinkyChasingCases, query, simConfig);
		}
		
		if(desc.getName().equals("PINKY"))
		{
			if(desc.getType() == CASE_TYPE.RUN)
				eval = NNScoringMethod.evaluateSimilarity(_pinkyRunawayCases, query, simConfig);
			else
				eval = NNScoringMethod.evaluateSimilarity(_pinkyChasingCases, query, simConfig);
		}
		
		if(desc.getName().equals("INKY"))
		{
			if(desc.getType() == CASE_TYPE.RUN)
				eval = NNScoringMethod.evaluateSimilarity(_inkyRunawayCases, query, simConfig);
			else
				eval = NNScoringMethod.evaluateSimilarity(_inkyChasingCases, query, simConfig);
		}
		
		if(desc.getName().equals("SUE"))
		{
			if(desc.getType() == CASE_TYPE.RUN)
				eval = NNScoringMethod.evaluateSimilarity(_sueRunawayCases, query, simConfig);
			else
				eval = NNScoringMethod.evaluateSimilarity(_sueChasingCases, query, simConfig);
		}
		
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
		newCase.setSolution(new GhostsSolution());
		newCase.setJustificationOfSolution(new GhostsJustification());
		newCase.setResult(new GhostsResult());
		
		MOVE finalMove;
		if(casos.length <= 0)
		{
			//movimiento random
			MOVE[] possibleMoves = _game.getPossibleMoves(_game.getGhostCurrentNodeIndex(_ghost), _game.getGhostLastMoveMade(_ghost));
			finalMove = possibleMoves[_rnd.nextInt(possibleMoves.length)];
		}
		else 
		{
			//movimiento solucion
			finalMove = chooseCaseMove(casos);
		}
		
		_Ghosts.setSolution(finalMove); 
		
		((GhostsResult) newCase.getResult()).setType(((GhostsDescription)query.getDescription()).getType());
		((GhostsResult) newCase.getResult()).setEvaluation(0f);
		
		((GhostsSolution) newCase.getSolution()).setType(((GhostsDescription)query.getDescription()).getType());
		((GhostsSolution) newCase.getSolution()).setMoveMade(finalMove);
		
		((GhostsJustification) newCase.getJustificationOfSolution()).setType(((GhostsDescription)query.getDescription()).getType());
		((GhostsJustification) newCase.getJustificationOfSolution()).setMsPacManDead(false);
		((GhostsJustification) newCase.getJustificationOfSolution()).setMsPacManTotalPoints(0);
		
		
		
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
		for(int i = 0; i < size; i++)
		{
			GhostsSolution s = (GhostsSolution) casos[i].get_case().getSolution();
			GhostsResult r = (GhostsResult) casos[i].get_case().getResult();
			
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
			int newPoints = ((GhostsJustification) c.getJustificationOfSolution()).getMsPacManTotalPoints();
			newPoints += points;
			
			((GhostsJustification) c.getJustificationOfSolution()).setMsPacManTotalPoints(newPoints);
			
			float evaluation = ((GhostsResult) c.getResult()).getEvaluation();
			
			if(((GhostsDescription) c.getDescription()).getType() == CASE_TYPE.RUN)
			{
				if(evaluation - points / 5 >= -100)
					evaluation -= points / 5;
				else
					evaluation = -100;
			}
			else
			{
				if(evaluation - points / 10 >= -100)
					evaluation -= points / 10;
				else
					evaluation = -100;
			}
			
			((GhostsResult) c.getResult()).setEvaluation(evaluation);
		}
		
		if(_buffer.size() > 20)
		{
			CBRCase c = _buffer.elementAt(0);
			
			removeCaseFromBuffer(c);
		}
	}
	
	public void MsPacManDead(int points)
	{
		int size = _buffer.size();
		for(int i = 0; i< size; i++)
		{
			CBRCase c = _buffer.elementAt(i);
			int newPoints = ((GhostsJustification) c.getJustificationOfSolution()).getMsPacManTotalPoints();
			newPoints += points;
			
			((GhostsJustification) c.getJustificationOfSolution()).setMsPacManTotalPoints(newPoints);
			
			float evaluation = ((GhostsResult) c.getResult()).getEvaluation();

			if(evaluation - points / 5 >= -100)
				evaluation -= points / 5;
			else
				evaluation = -100;
			
			if(evaluation + 150 <= 100)
				evaluation += 150;
			else
				evaluation = 100;
			
			((GhostsResult) c.getResult()).setEvaluation(evaluation);
			
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
		if(((GhostsResult) c.getResult()).getEvaluation() > 50)
			_casesToLearn.add(c);
	}
	
	public void postCycle() throws ExecutionException
	{
		MsPacManDead(_game.getScore() - _Ghosts.getLastScore());
		_caseBase.learnCases(_casesToLearn);
		this._caseBase.close();
	}
}
