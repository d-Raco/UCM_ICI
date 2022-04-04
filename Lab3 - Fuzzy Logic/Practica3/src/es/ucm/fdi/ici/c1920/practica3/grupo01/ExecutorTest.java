package es.ucm.fdi.ici.c1920.practica3.grupo01;

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
        Executor executor = new Executor.Builder().setTickLimit(4000).setPacmanPO(true).setGhostPO(true).setPOType(POType.LOS).setVisual(true).setScaleFactor(3.0).build();
        
        PacmanController pacMan = new MsPacMan();
        
        GhostController ghosts = new Ghosts();
        
        System.out.println(executor.runGame(pacMan, ghosts, 50));
    }
}
