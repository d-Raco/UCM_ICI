package es.ucm.fdi.ici.c1920.practica4.grupo01;

import es.ucm.fdi.ici.c1920.practica4.grupo01.Constants.CASE_TYPE;
import pacman.game.Constants.MOVE;
import ucm.gaia.jcolibri.cbrcore.Attribute;

public class MsPacManSolution implements ucm.gaia.jcolibri.cbrcore.CaseComponent
{
	String id; 
	
	CASE_TYPE type;
	
	MOVE moveMade;

	public Attribute getIdAttribute()
	{
		return new Attribute("id", MsPacManDescription.class); 
	}
	
	
	
	
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
	
	

	public CASE_TYPE getType() {
		return type;
	}

	public void setType(CASE_TYPE type) {
		this.type = type;
	}
	
	
	
	
	

	public MOVE getMoveMade() {
		return moveMade;
	}

	public void setMoveMade(MOVE moveMade) {
		this.moveMade = moveMade;
	}
	
	
}
