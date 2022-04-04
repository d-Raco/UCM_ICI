package es.ucm.fdi.ici.c1920.practica4.grupo01;

import ucm.gaia.jcolibri.cbrcore.Attribute;
import es.ucm.fdi.ici.c1920.practica4.grupo01.Constants.CASE_TYPE;
import pacman.game.Constants.MOVE;

public class MsPacManDescription implements ucm.gaia.jcolibri.cbrcore.CaseComponent
{
	String id; 
	
	CASE_TYPE type;
	
	Integer msPacManNode;

	MOVE msPacManLastMove;
	
	MOVE moveToBlinky, moveToPinky, moveToInky, moveToSue;
	
	Integer blinkyDistance, pinkyDistance, inkyDistance, sueDistance;
	Boolean blinkyEdible, pinkyEdible, inkyEdible, sueEdible;
	
	Integer closestPills1, closestPills2, closestPills3, closestPills4;
	
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
	
	
	
	
	
	
	

	public Integer getMsPacManNode() {
		return msPacManNode;
	}

	public void setMsPacManNode(Integer msPacManNode) {
		this.msPacManNode = msPacManNode;
	}

	
	
	
	
	
	public MOVE getMsPacManLastMove() {
		return msPacManLastMove;
	}

	public void setMsPacManLastMove(MOVE msPacManLastMove) {
		this.msPacManLastMove = msPacManLastMove;
	}
	
	
	
	
	
	
	
	public MOVE getMoveToBlinky() {
		return moveToBlinky;
	}

	public void setMoveToBlinky(MOVE moveToBlinky) {
		this.moveToBlinky = moveToBlinky;
	}

	public MOVE getMoveToPinky() {
		return moveToPinky;
	}

	public void setMoveToPinky(MOVE moveToPinky) {
		this.moveToPinky = moveToPinky;
	}

	public MOVE getMoveToInky() {
		return moveToInky;
	}

	public void setMoveToInky(MOVE moveToInky) {
		this.moveToInky = moveToInky;
	}

	public MOVE getMoveToSue() {
		return moveToSue;
	}

	public void setMoveToSue(MOVE moveToSue) {
		this.moveToSue = moveToSue;
	}
	
	
	
	
	
	
	
	
	public Integer getBlinkyDistance() {
		return blinkyDistance;
	}

	public void setBlinkyDistance(Integer blinkyDistance) {
		this.blinkyDistance = blinkyDistance;
	}

	public Integer getPinkyDistance() {
		return pinkyDistance;
	}

	public void setPinkyDistance(Integer pinkyDistance) {
		this.pinkyDistance = pinkyDistance;
	}

	public Integer getInkyDistance() {
		return inkyDistance;
	}

	public void setInkyDistance(Integer inkyDistance) {
		this.inkyDistance = inkyDistance;
	}

	public Integer getSueDistance() {
		return sueDistance;
	}

	public void setSueDistance(Integer sueDistance) {
		this.sueDistance = sueDistance;
	}
	
	
	
	
	

	public Boolean getBlinkyEdible() {
		return blinkyEdible;
	}

	public void setBlinkyEdible(Boolean blinkyEdible) {
		this.blinkyEdible = blinkyEdible;
	}

	public Boolean getPinkyEdible() {
		return pinkyEdible;
	}

	public void setPinkyEdible(Boolean pinkyEdible) {
		this.pinkyEdible = pinkyEdible;
	}

	public Boolean getInkyEdible() {
		return inkyEdible;
	}

	public void setInkyEdible(Boolean inkyEdible) {
		this.inkyEdible = inkyEdible;
	}

	public Boolean getSueEdible() {
		return sueEdible;
	}

	public void setSueEdible(Boolean sueEdible) {
		this.sueEdible = sueEdible;
	}
	
	
	
	
	

	public Integer getClosestPills1() {
		return closestPills1;
	}

	public void setClosestPills1(Integer closestPills1) {
		this.closestPills1 = closestPills1;
	}

	public Integer getClosestPills2() {
		return closestPills2;
	}

	public void setClosestPills2(Integer closestPills2) {
		this.closestPills2 = closestPills2;
	}

	public Integer getClosestPills3() {
		return closestPills3;
	}

	public void setClosestPills3(Integer closestPills3) {
		this.closestPills3 = closestPills3;
	}

	public Integer getClosestPills4() {
		return closestPills4;
	}

	public void setClosestPills4(Integer closestPills4) {
		this.closestPills4 = closestPills4;
	}
	
}
