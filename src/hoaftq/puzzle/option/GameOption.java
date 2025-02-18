/**
 * Puzzle game using Java AWT
 */
package hoaftq.puzzle.option;

import hoaftq.puzzle.game.EmptyTilePosition;
import hoaftq.puzzle.common.PuzzleImage;

public record GameOption(
        boolean usedImage,
        PuzzleImage puzzleImage,
        byte row,
        byte column,
        EmptyTilePosition emptyPosition) {
}
