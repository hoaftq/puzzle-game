/**
 * Puzzle game using Java AWT
 */
package hoaftq.puzzle.entity;

public record GameInfo(
        boolean usedImage,
        PuzzleImage puzzleImage,
        byte row,
        byte column,
        EmptyTilePosition emptyPosition) {
}
