/**
 * Puzzle game using Java AWT
 * Copyright @ 2011 Trac Quang Hoa
 */
package hoaftq.puzzle.piece;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * Represents all the number tiles
 */
public class NumbersView extends TilesView {

    public NumbersView(byte row, byte column, int width, int height) {
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

        // Calculate width, height, horizontal coordinate, vertical coordinate
        // of piece in game board
        int withPerTile = width / row;
        int heightPerTile = height / column;
        int tileLeft = left + xOnGameBoard * withPerTile;
        int tileTop = top + yOnGameBoard * heightPerTile;

        // Fill background of tile
        g.setColor(Color.BLACK);
        g.fillRect(tileLeft, tileTop, withPerTile, heightPerTile);

        // Draw border of piece
        g.setColor(Color.WHITE);
        g.drawRect(tileLeft, tileTop, withPerTile - 1, heightPerTile - 1);

        // Calculate the value that will be displayed on the tile
        int value = (yOnImageNumbers * row) + xOnImageNumbers + 1;
        String valueString = Integer.toString(value);

        // Draw value piece
        var font = g.getFont().deriveFont(50f);
        g.setFont(font);

        var bound = font.getStringBounds(valueString, ((Graphics2D) g).getFontRenderContext());
        g.setClip(tileLeft, tileTop, withPerTile, heightPerTile);
        g.setColor(Color.RED);
        g.drawString(
                valueString,
                tileLeft
                + (int) ((withPerTile - bound.getWidth()) / 2),
                tileTop
                + (int) ((heightPerTile - bound.getHeight()) / 2 - bound.getY()));
        g.setClip(null);
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
}
