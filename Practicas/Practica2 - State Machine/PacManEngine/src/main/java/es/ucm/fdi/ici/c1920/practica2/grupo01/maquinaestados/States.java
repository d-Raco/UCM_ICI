package es.ucm.fdi.ici.c1920.practica2.grupo01.maquinaestados;
import java.util.Vector;

import pacman.game.Constants.MOVE;

public enum States 
{
	NO_STATE,
	
	/*
	 * PACMAN STATES
	 */
	COMER_PILLS
	{
		private MOVE move = MOVE.NEUTRAL;
		
		
		
		public MOVE getSingleMove() {return move;}
		
		public void setSingleMove(MOVE m) { move = m; }
	},
	
	PERSEGUIR
	{
		private Vector<MOVE> move = new Vector<MOVE>(4);

		
		
		public Vector<MOVE> getMove() { return move; }
		
	    public void setMove(Vector<MOVE> m) { move = m; }
	},
	
	HUIR
	{
		private MOVE move = MOVE.NEUTRAL;
		private MOVE moveAway = MOVE.NEUTRAL;
		
		
		
		public MOVE getSingleMove() {return move;}
		public MOVE getMoveAway() { return moveAway; }
		
		public void setSingleMove(MOVE m) { move = m; }
	    public void setMoveAway(MOVE m) { moveAway = m; }
	},
	
	HUIR_2
	{
		private Vector<MOVE> move = new Vector<MOVE>(2);
		private MOVE moveAwayFirst = MOVE.NEUTRAL;
		
		
		
		public Vector<MOVE> getMove() { return move; }
		public MOVE getMoveAway() { return moveAwayFirst; }
		
	    public void setMove(Vector<MOVE> m) { move = m; }
	    public void setMoveAway(MOVE m) { moveAwayFirst = m; }
	},
	
	HUIR_3
	{
		private Vector<MOVE> move = new Vector<MOVE>(3);
		private MOVE moveAwayFirst = MOVE.NEUTRAL;
		
		
		
		public Vector<MOVE> getMove() { return move; }
		public MOVE getMoveAway() { return moveAwayFirst; }
		
	    public void setMove(Vector<MOVE> m) { move = m; }
	    public void setMoveAway(MOVE m) { moveAwayFirst = m; }
	},
	
	HUIR_4
	{
		private Vector<MOVE> move = new Vector<MOVE>(4);
		private MOVE moveAwayFirst = MOVE.NEUTRAL;
		
		
		
		public Vector<MOVE> getMove() { return move; }
		public MOVE getMoveAway() { return moveAwayFirst; }   
		
	    public void setMove(Vector<MOVE> m) { move = m; }
	    public void setMoveAway(MOVE m) { moveAwayFirst = m; }
	},

	/*
	 * GHOSTS STATES ( SALVO HUIR )
	 */
	
	SUICIDA
	{
		private MOVE move = MOVE.NEUTRAL;
		
		
		
		public MOVE getSingleMove() {return move;}
		
		public void setSingleMove(MOVE m) { move = m; }
	},
	
	NO_SUICIDA
	{
		private Vector<MOVE> move = new Vector<MOVE>(2);
		private MOVE moveAwayFirst = MOVE.NEUTRAL;
		
		
		
		public Vector<MOVE> getMove() { return move; }
		public MOVE getMoveAway() { return moveAwayFirst; }
		
	    public void setMove(Vector<MOVE> m) { move = m; }
	    public void setMoveAway(MOVE m) { moveAwayFirst = m; }
	},
	
	ALEJARSE
	{
		private MOVE move = MOVE.NEUTRAL;
		
		
		
		public MOVE getSingleMove() {return move;}
		
		public void setSingleMove(MOVE m) { move = m; }
	},
	
	PERSEGUIR_A_PACMAN
	{
		private MOVE move = MOVE.NEUTRAL;
		
		
		
		public MOVE getSingleMove() {return move;}
		
		public void setSingleMove(MOVE m) { move = m; }
	};
	
	public MOVE getSingleMove() { return MOVE.NEUTRAL;};
    public Vector<MOVE> getMove(){return  new Vector<MOVE>(1);};
    public MOVE getMoveAway(){ return MOVE.NEUTRAL;};
    
	public void setSingleMove(MOVE m) {};
    public void setMove(Vector<MOVE> m) {};
    public void setMoveAway(MOVE m) {};

}
