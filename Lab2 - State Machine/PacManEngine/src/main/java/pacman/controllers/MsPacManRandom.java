/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pacman.controllers;

import java.util.Random;

import pacman.game.Constants.MOVE;

import pacman.game.Game;

/**
 *
 * @author lebami
 */

public final class MsPacManRandom extends PacmanController {
        private Random rnd = new Random();
        
        @Override
        
        public MOVE getMove(Game game, long timeDue) {
            MOVE[] possibleMoves = game.getPossibleMoves(game.getPacmanCurrentNodeIndex(), game.getPacmanLastMoveMade());
            return possibleMoves[rnd.nextInt(possibleMoves.length)];
        }
}
