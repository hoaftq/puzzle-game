/**
 * Puzzle game using Java AWT
 * Copyright @ 2011 Trac Quang Hoa
 */
package hoaftq.puzzle;

import hoaftq.puzzle.option.GameOptionStorage;
import hoaftq.puzzle.option.GameOptionValidator;
import hoaftq.puzzle.game.GameFrame;

/**
 * Entry class of puzzle game
 */
public class Puzzle {
    public static void main(String[] args) {
        var gameInfoValidator = new GameOptionValidator();
        var gameInfoStorage = new GameOptionStorage(gameInfoValidator);
        var gameFrame = new GameFrame(gameInfoStorage);
        gameFrame.setVisible(true);
    }
}
