/**
 * Puzzle game using Java AWT
 */
package hoaftq.puzzle.tile;

import hoaftq.puzzle.common.PuzzleImage;

import java.awt.*;
import java.io.IOException;

/**
 * Image tiles
 */
public class ImageTilesView extends TilesView {
    private final Image image;

    public ImageTilesView(byte row, byte column, int width, int height, PuzzleImage puzzleImage) throws IOException {
        super(row, column, width, height);
        image = puzzleImage.loadImage();
    }

    @Override
    public void drawOne(Graphics g, int left, int top, byte xOnGameBoard,
                        byte yOnGameBoard, byte xOnImageNumbers, byte yOnImageNumbers) {
        int widthPerRow = width / row;
        int heightPerColumn = height / column;
        int tileLeft = left + xOnGameBoard * widthPerRow;
        int tileRight = top + yOnGameBoard * heightPerColumn;


        // Calculate position and size of the tile on the image
        int tileImageWidth = image.getWidth(null) / row;
        int tileImageHeight = image.getHeight(null) / column;
        int tileImageLeft = xOnImageNumbers * tileImageWidth;
        int tileImageTop = yOnImageNumbers * tileImageHeight;

        // Draw the tile image to the tile on the view
        g.drawImage(image,
                tileLeft + 1,
                tileRight + 1,
                tileLeft + widthPerRow - 1,
                tileRight + heightPerColumn - 1,
                tileImageLeft,
                tileImageTop,
                tileImageLeft + tileImageWidth,
                tileImageTop + tileImageHeight,
                null);
    }

    @Override
    public void drawAll(Graphics g, int left, int top) {
        g.drawImage(image,
                left,
                top,
                left + width,
                left + height,
                0,
                0,
                image.getWidth(null),
                image.getHeight(null),
                null);
    }
}
