/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman.controllers;

import java.util.Random;
import java.util.EnumMap;
import pacman.game.Constants;

import pacman.game.Constants.GHOST;
import pacman.game.Constants.MOVE;

import pacman.game.Game;

/**
 *
 * @author lebam
 */
public class GhostsAggresive extends GhostController {
    private EnumMap<GHOST, MOVE> moves = new EnumMap<GHOST, MOVE>(GHOST.class);
    private MOVE[] allMoves = MOVE.values();
    private Random rnd = new Random();
    
    @Override
    
    public EnumMap<GHOST, MOVE> getMove(Game game, long timeDue) {
        moves.clear();
        for (GHOST ghostType : GHOST.values()) {
            if (game.doesGhostRequireAction(ghostType))
            	moves.put(ghostType, game.getApproximateNextMoveTowardsTarget(game.getGhostCurrentNodeIndex(ghostType), game.getPacmanCurrentNodeIndex(), 
                          game.getGhostLastMoveMade(ghostType), Constants.DM.MANHATTAN));
        }
        
        return moves;
    }
}
