package es.ucm.fdi.ici.c1920.practica2.grupo01.maquinaestados;

import pacman.controllers.PacmanController;
import pacman.controllers.MsPacManRandom;
import pacman.controllers.HumanController;
import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.GhostsRandom;
import pacman.controllers.GhostsAggresive;
import pacman.controllers.KeyBoardInput;

/**
 *
 * @author lebami
 */

public class ExecutorTest 
{
    public static void main(String[] args) 
    {
        Executor executor = new Executor.Builder().setTickLimit(4000).setVisual(true).setScaleFactor(3.0).build();
        
        //PacmanController pacMan = new MsPacManRandom();
        //PacmanController pacMan = new HumanController(new KeyBoardInput());
        PacmanController pacMan = new MsPacMan();
        
        //GhostController ghosts = new GhostsRandom();
        //GhostController ghosts = new GhostsAggresive();
        GhostController ghosts = new Ghosts();
        
        System.out.println(executor.runGame(pacMan, ghosts, 50));
    }
}
