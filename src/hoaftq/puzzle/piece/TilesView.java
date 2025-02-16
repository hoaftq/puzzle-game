/**
 * Puzzle game using Java AWT
 */
package hoaftq.puzzle.piece;

import java.awt.*;

/**
 * Representing the whole image or all the number displaying on the game board
 */
public abstract class TilesView {
    protected byte row;

    protected byte column;

    /**
     * Width of game board
     */
    protected int width;

    /**
     * Height of game board
     */
    protected int height;

    protected TilesView(byte row, byte column) {
        this.row = row;
        this.column = column;
    }

    protected TilesView(byte row, byte column, int width, int height) {
        this.row = row;
        this.column = column;
        this.width = width;
        this.height = height;
    }

    public byte getRow() {
        return row;
    }

    public byte getColumn() {
        return column;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Draw one tile on the game board
     *
     * @param g               graphics
     * @param left            left coordinate where to start drawing
     * @param top             top coordinate where to start drawing
     * @param xOnGameBoard    horizontal index of the drawn tile on game board
     * @param yOnGameBoard    vertical index of the drawn tile on game board
     * @param xOnImageNumbers horizontal index of tile on the image/numbers
     * @param yOnImageNumbers vertical index of tile on the image/numbers
     */
    public abstract void drawOne(Graphics g,
                                 int left,
                                 int top,
                                 byte xOnGameBoard,
                                 byte yOnGameBoard,
                                 byte xOnImageNumbers,
                                 byte yOnImageNumbers);

    /**
     * Draw complete game board with all the tiles
     *
     * @param g    graphics
     * @param left left coordinate where to start drawing
     * @param top  top coordinate where to start drawing
     */
    public abstract void drawAll(Graphics g, int left, int top);
}
