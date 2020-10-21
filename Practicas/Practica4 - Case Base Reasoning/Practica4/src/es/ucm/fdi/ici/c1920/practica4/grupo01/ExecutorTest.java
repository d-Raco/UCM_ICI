package es.ucm.fdi.ici.c1920.practica4.grupo01;

import pacman.controllers.PacmanController;
import pacman.game.internal.POType;
import pacman.controllers.HumanController;
import pacman.Executor;
import pacman.controllers.GhostController;
import pacman.controllers.KeyBoardInput;

/**
 *
 * @author lebami
 */

public class ExecutorTest 
{
    public static void main(String[] args) 
    {
        PacmanController pacMan = new MsPacMan();
        PacmanController pacManAprender = new MsPacManAprender();
        
        GhostController ghosts = new Ghosts();
        GhostController ghostsAprender = new GhostsAprender();
        
        Executor executor = new Executor.Builder().setTickLimit(4000).setPacmanPO(false).setGhostPO(false).setVisual(true).setScaleFactor(3.0).build();

        System.out.println(executor.runGame(pacMan, ghosts, 50));
        //System.out.println(executor.runGame(pacMan, ghostsAprender, 50));
        //System.out.println(executor.runGame(pacManAprender, ghosts, 50));
        //System.out.println(executor.runExperiment(pacMan, ghostsAprender, 1000, "hola"));
        //System.out.println(executor.runExperiment(pacManAprender, ghosts, 1000, "hola"));
    }
}
