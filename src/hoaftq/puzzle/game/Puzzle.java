/**
 * Puzzle game using Java AWT
 * Copyright @ 2011 Trac Quang Hoa
 */
package hoaftq.puzzle.game;

import hoaftq.puzzle.entity.GameInfoStorage;
import hoaftq.puzzle.entity.GameInfoValidator;

/**
 * Entry class of puzzle game
 */
public class Puzzle {
    public static void main(String[] args) {
        var gameInfoValidator = new GameInfoValidator();
        var gameInfoStorage = new GameInfoStorage(gameInfoValidator);
        var gameFrame = new GameFrame(gameInfoStorage);
        gameFrame.setVisible(true);
    }
}
