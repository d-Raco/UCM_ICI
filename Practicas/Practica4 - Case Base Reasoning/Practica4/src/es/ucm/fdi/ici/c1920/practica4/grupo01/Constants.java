package es.ucm.fdi.ici.c1920.practica4.grupo01;

public class Constants {
	//GHOST CONSTANTS
	public static final Double GOTOPACMAN_LIMIT = 4.0;
	public static final Double STAYINAREA_LIMIT = 3.0;
	public static final Double GHOSTS_MAX_DISTANCE = 200.0;
	public static final Double GHOSTS_MAX_TIME_LAST_SAW = 3.0;

	 //PACMAN CONSTANTS
	 public static final Double PACMAN_MAX_DISTANCE = 200.0;
	 public static final Double PACMAN_MAX_TIME_LAST_SAW = 2.0;
	 public static final Double RUNAWAY_LIMIT = 15.0;
	 public static final Double GOTOEDIBLE_LIMIT = 15.0;
	 
	 public enum CASE_TYPE
	 {
		 CHASE,
		 RUN,
		 NEUTRAL
	 }
}
