package hoaftq.puzzle.dialog;

import hoaftq.puzzle.entity.GameInfo;
import hoaftq.puzzle.entity.PuzzleImage;
import hoaftq.puzzle.piece.TilesView;
import hoaftq.puzzle.piece.ImageView;
import hoaftq.puzzle.piece.NumbersView;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Panel to display using image or numbers
 */
public class ImageNumbersPanel extends JPanel {
    private static final String IMAGE_ERROR_MESSAGE = "Image could not be displayed. Please choose another one.";

    private final JPanel drawingPanel;
    private final GameInfo gameInfo;

    /**
     * Piece image or piece number
     */
    private TilesView pieceObject;
    private boolean isImageError;

    public ImageNumbersPanel(GameInfo gameInfo, int width, int height) {
        this.gameInfo = gameInfo;

        // Create a sub panel that displays image or numbers
        drawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (isImageError) {
                    drawImageErrorMessage(g, width, height);
                } else if (pieceObject != null) {
                    pieceObject.drawAll(g, 0, 0);
                }
            }
        };
        drawingPanel.setPreferredSize(new Dimension(width, height));
        drawingPanel.setBackground(Color.BLACK);
        drawingPanel.setForeground(Color.RED);

        add(drawingPanel);
    }

    /**
     * Set puzzle image to display on the panel.
     * If {@code puzzleImage} is null then numbers will be used instead.
     *
     * @param puzzleImage puzzle image to display
     */
    public boolean setDisplayingObject(PuzzleImage puzzleImage) {
        isImageError = false;

        if (puzzleImage != null) {
            try {
                pieceObject = new ImageView(
                        gameInfo.row(),
                        gameInfo.column(),
                        drawingPanel.getWidth(),
                        drawingPanel.getHeight(),
                        puzzleImage);
            } catch (IOException e) {
                isImageError = true;
            }
        } else {
            pieceObject = new NumbersView(
                    gameInfo.row(),
                    gameInfo.column(),
                    drawingPanel.getWidth(),
                    drawingPanel.getHeight());
        }

        drawingPanel.repaint();
        return !isImageError;
    }

    private static void drawImageErrorMessage(Graphics g, int width, int height) {
        var fontMetrics = g.getFontMetrics();
        var left = (width - fontMetrics.stringWidth(IMAGE_ERROR_MESSAGE)) / 2;
        var top = height / 2;
        g.drawString(IMAGE_ERROR_MESSAGE, left, top);
    }
}
