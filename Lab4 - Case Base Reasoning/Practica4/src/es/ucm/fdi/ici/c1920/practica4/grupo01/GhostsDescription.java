package es.ucm.fdi.ici.c1920.practica4.grupo01;

import es.ucm.fdi.ici.c1920.practica4.grupo01.Constants.CASE_TYPE;
import pacman.game.Constants.MOVE;
import ucm.gaia.jcolibri.cbrcore.Attribute;

public class GhostsDescription implements ucm.gaia.jcolibri.cbrcore.CaseComponent
{
	String id;
	
	CASE_TYPE type;

	String name;
	Integer nodeMsPacMan;
	MOVE msPacManLastMove;
	
	Integer nodeGhost1, nodeGhost2, nodeGhost3, nodeGhost4;
	Integer distanceGhost1, distanceGhost2, distanceGhost3, distanceGhost4;
	Boolean edibleGhost1, edibleGhost2, edibleGhost3, edibleGhost4;
	
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
	
	
	
	
	
	
	
	
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}






	public Integer getNodeMsPacMan() {
		return nodeMsPacMan;
	}

	public void setNodeMsPacMan(Integer nodeMsPacMan) {
		this.nodeMsPacMan = nodeMsPacMan;
	}
	
	public MOVE getMsPacManLastMove() {
		return msPacManLastMove;
	}

	public void setMsPacManLastMove(MOVE msPacManLastMove) {
		this.msPacManLastMove = msPacManLastMove;
	}
	
	
	
	
	
	
	

	public Integer getNodeGhost1() {
		return nodeGhost1;
	}

	public void setNodeGhost1(Integer nodeGhost1) {
		this.nodeGhost1 = nodeGhost1;
	}

	public Integer getNodeGhost2() {
		return nodeGhost2;
	}

	public void setNodeGhost2(Integer nodeGhost2) {
		this.nodeGhost2 = nodeGhost2;
	}

	public Integer getNodeGhost3() {
		return nodeGhost3;
	}

	public void setNodeGhost3(Integer nodeGhost3) {
		this.nodeGhost3 = nodeGhost3;
	}

	public Integer getNodeGhost4() {
		return nodeGhost4;
	}

	public void setNodeGhost4(Integer nodeGhost4) {
		this.nodeGhost4 = nodeGhost4;
	}
	
	
	
	
	
	
	
	

	public Integer getDistanceGhost1() {
		return distanceGhost1;
	}

	public void setDistanceGhost1(Integer distanceGhost1) {
		this.distanceGhost1 = distanceGhost1;
	}

	public Integer getDistanceGhost2() {
		return distanceGhost2;
	}

	public void setDistanceGhost2(Integer distanceGhost2) {
		this.distanceGhost2 = distanceGhost2;
	}

	public Integer getDistanceGhost3() {
		return distanceGhost3;
	}

	public void setDistanceGhost3(Integer distanceGhost3) {
		this.distanceGhost3 = distanceGhost3;
	}

	public Integer getDistanceGhost4() {
		return distanceGhost4;
	}

	public void setDistanceGhost4(Integer distanceGhost4) {
		this.distanceGhost4 = distanceGhost4;
	}
	
	
	
	
	
	
	
	

	public Boolean getEdibleGhost1() {
		return edibleGhost1;
	}

	public void setEdibleGhost1(Boolean edibleGhost1) {
		this.edibleGhost1 = edibleGhost1;
	}
	
	public Boolean getEdibleGhost2() {
		return edibleGhost2;
	}

	public void setEdibleGhost2(Boolean edibleGhost2) {
		this.edibleGhost2 = edibleGhost2;
	}
	
	public Boolean getEdibleGhost3() {
		return edibleGhost3;
	}

	public void setEdibleGhost3(Boolean edibleGhost3) {
		this.edibleGhost3 = edibleGhost3;
	}
	
	public Boolean getEdibleGhost4() {
		return edibleGhost4;
	}

	public void setEdibleGhost4(Boolean edibleGhost4) {
		this.edibleGhost4 = edibleGhost4;
	}
}
