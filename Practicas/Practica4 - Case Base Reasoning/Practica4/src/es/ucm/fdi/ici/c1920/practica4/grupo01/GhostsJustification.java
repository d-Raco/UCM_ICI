package es.ucm.fdi.ici.c1920.practica4.grupo01;

import es.ucm.fdi.ici.c1920.practica4.grupo01.Constants.CASE_TYPE;
import ucm.gaia.jcolibri.cbrcore.Attribute;

public class GhostsJustification implements ucm.gaia.jcolibri.cbrcore.CaseComponent
{
	String id;
	
	CASE_TYPE type;

	Integer msPacManTotalPoints;
	
	Boolean msPacManDead;
	
	public Attribute getIdAttribute()
	{
		return new Attribute("id", GhostsDescription.class);
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
	
	
	
	
	
	
	

	public Integer getMsPacManTotalPoints() {
		return msPacManTotalPoints;
	}

	public void setMsPacManTotalPoints(Integer msPacManTotalPoints) {
		this.msPacManTotalPoints = msPacManTotalPoints;
	}
	
	
	
	
	
	
	

	public Boolean getMsPacManDead() {
		return msPacManDead;
	}

	public void setMsPacManDead(Boolean msPacManDead) {
		this.msPacManDead = msPacManDead;
	}
	
}