/**
 * Puzzle game using Java AWT
 */
package hoaftq.puzzle;

import hoaftq.puzzle.option.GameOptionStorage;
import hoaftq.puzzle.option.GameOptionValidator;
import hoaftq.puzzle.game.GameFrame;

public class Puzzle {
    public static void main(String[] args) {
        var gameInfoValidator = new GameOptionValidator();
        var gameInfoStorage = new GameOptionStorage(gameInfoValidator);
        var gameFrame = new GameFrame(gameInfoStorage.get());
        gameFrame.setVisible(true);
    }
}
