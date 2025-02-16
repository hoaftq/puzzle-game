package hoaftq.puzzle.dialog;

import hoaftq.puzzle.entity.PuzzleImage;
import hoaftq.puzzle.piece.ImageTilesView;
import hoaftq.puzzle.piece.NumberTilesView;
import hoaftq.puzzle.piece.TilesView;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Objects;

/**
 * Panel to display using image or numbers
 */
public class ImageNumbersPanel extends JPanel {
    private static final String IMAGE_ERROR_MESSAGE = "Image could not be displayed. Please choose another one.";

    private final JPanel drawingPanel;
    private final byte row;
    private final byte column;

    /**
     * Piece image or piece number
     */
    private TilesView tilesView;
    private boolean isImageError;

    public ImageNumbersPanel(byte row, byte column, int width, int height) {
        this.row = row;
        this.column = column;

        // Create a sub panel that displays image or numbers
        drawingPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                if (isImageError) {
                    drawImageErrorMessage(g, width, height);
                } else if (tilesView != null) {
                    tilesView.drawAll(g, 0, 0);
                }
            }
        };
        drawingPanel.setPreferredSize(new Dimension(width, height));
        drawingPanel.setBackground(Color.BLACK);
        drawingPanel.setForeground(Color.RED);

        add(drawingPanel);
    }

    public boolean displayImage(PuzzleImage puzzleImage) {
        Objects.requireNonNull(puzzleImage);
        try {
            tilesView = new ImageTilesView(row, column, drawingPanel.getWidth(), drawingPanel.getHeight(), puzzleImage);
            isImageError = false;
        } catch (IOException e) {
            isImageError = true;
        } finally {
            drawingPanel.repaint();
        }

        return !isImageError;
    }

    public void displayNumbers() {
        tilesView = new NumberTilesView(row, column, drawingPanel.getWidth(), drawingPanel.getHeight());
        drawingPanel.repaint();
    }

    private static void drawImageErrorMessage(Graphics g, int width, int height) {
        var fontMetrics = g.getFontMetrics();
        var left = (width - fontMetrics.stringWidth(IMAGE_ERROR_MESSAGE)) / 2;
        var top = height / 2;
        g.drawString(IMAGE_ERROR_MESSAGE, left, top);
    }
}
