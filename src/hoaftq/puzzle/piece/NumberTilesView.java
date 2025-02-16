/**
 * Puzzle game using Java AWT
 * Copyright @ 2011 Trac Quang Hoa
 */
package hoaftq.puzzle.piece;

import java.awt.*;

/**
 * Represents all the number tiles
 */
public class NumberTilesView extends TilesView {

    public NumberTilesView(byte row, byte column, int width, int height) {
        super(row, column, width, height);
    }

    @Override
    public void drawOne(Graphics g,
                        int left,
                        int top,
                        byte xOnGameBoard,
                        byte yOnGameBoard,
                        byte xOnImageNumbers,
                        byte yOnImageNumbers) {
        int withPerTile = width / row;
        int heightPerTile = height / column;
        int tileLeft = left + xOnGameBoard * withPerTile;
        int tileTop = top + yOnGameBoard * heightPerTile;

        fillBackground(g, tileLeft, tileTop, withPerTile, heightPerTile);

        drawBorder(g, tileLeft, tileTop, withPerTile, heightPerTile);

        var tileValue = getTileValue(xOnImageNumbers, yOnImageNumbers);
        drawTileValue(g, tileValue, tileLeft, tileTop, withPerTile, heightPerTile);
    }


    @Override
    public void drawAll(Graphics g, int left, int top) {

        // Draw all the tiles in their original positions
        for (byte i = 0; i < row; i++) {
            for (byte j = 0; j < column; j++) {
                drawOne(g, left, top, i, j, i, j);
            }
        }
    }

    private static void fillBackground(Graphics g, int tileLeft, int tileTop, int withPerTile, int heightPerTile) {
        g.setColor(Color.BLACK);
        g.fillRect(tileLeft, tileTop, withPerTile, heightPerTile);
    }

    private static void drawBorder(Graphics g, int tileLeft, int tileTop, int withPerTile, int heightPerTile) {
        g.setColor(Color.WHITE);
        g.drawRect(tileLeft, tileTop, withPerTile - 1, heightPerTile - 1);
    }

    /**
     * Calculate the value that will be displayed on the tile
     */
    private String getTileValue(byte xOnImageNumbers, byte yOnImageNumbers) {
        int value = (yOnImageNumbers * row) + xOnImageNumbers + 1;
        return Integer.toString(value);
    }

    private static void drawTileValue(Graphics g,
                                      String value,
                                      int tileLeft,
                                      int tileTop,
                                      int withPerTile,
                                      int heightPerTile) {
        var font = g.getFont().deriveFont(50f);
        g.setFont(font);

        var bound = font.getStringBounds(value, ((Graphics2D) g).getFontRenderContext());
        g.setClip(tileLeft, tileTop, withPerTile, heightPerTile);
        g.setColor(Color.RED);
        g.drawString(
                value,
                tileLeft + (int) ((withPerTile - bound.getWidth()) / 2),
                tileTop + (int) ((heightPerTile - bound.getHeight()) / 2 - bound.getY()));
        g.setClip(null);
    }
}
