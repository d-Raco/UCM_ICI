package es.ucm.fdi.ici.c1920.practica4.grupo01;

import es.ucm.fdi.ici.c1920.practica4.grupo01.Constants.CASE_TYPE;
import pacman.game.Constants.MOVE;
import ucm.gaia.jcolibri.cbrcore.Attribute;

public class MsPacManJustification implements ucm.gaia.jcolibri.cbrcore.CaseComponent
{
	String id; 
	
	CASE_TYPE type;
	
	Boolean achivementMade;
	
	Integer totalPoints;
	
	Boolean dead;

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
	
	
	
	

	public Boolean getAchivementMade() {
		return achivementMade;
	}

	public void setAchivementMade(Boolean achivementMade) {
		this.achivementMade = achivementMade;
	}
	
	
	
	

	public Integer getTotalPoints() {
		return totalPoints;
	}

	public void setTotalPoints(Integer totalPoints) {
		this.totalPoints = totalPoints;
	}
	
	
	
	

	public Boolean getDead() {
		return dead;
	}

	public void setDead(Boolean dead) {
		this.dead = dead;
	}
	

}
