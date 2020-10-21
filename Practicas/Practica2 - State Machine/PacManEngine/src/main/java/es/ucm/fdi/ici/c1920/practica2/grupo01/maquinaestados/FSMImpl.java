package es.ucm.fdi.ici.c1920.practica2.grupo01.maquinaestados;

import pacman.game.Game;
import pacman.game.Constants.GHOST;

public class FSMImpl
{
	private States firstState = States.NO_STATE;
	private States secondState = States.NO_STATE;
	private States thirdState = States.NO_STATE;
	
	public States getFirstState() { return firstState; };
	public States getSecondState() { return secondState; };
	public States getThirdState() { return thirdState; };
	
	public void changeFirstState(States newState) { firstState = newState; }
	public void changeSecondState(States newState) { secondState = newState; }
	public void changeThirdState(States newState) { secondState = newState; }
	
	public void chooseState(Game game) {}

};