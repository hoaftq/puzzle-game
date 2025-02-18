/**
 * Puzzle game using Java AWT
 * Copyright @ 2011 Trac Quang Hoa
 */
package hoaftq.puzzle.game;

import hoaftq.puzzle.info.GameInfoView;
import hoaftq.puzzle.option.GameOption;
import hoaftq.puzzle.tile.ImageTilesView;
import hoaftq.puzzle.tile.NumberTilesView;
import hoaftq.puzzle.tile.TilesView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

/**
 * Panel display game board
 */
public class GamePanel extends JPanel {
    private final static int MARGIN_LEFT = 0;
    private final static int MARGIN_TOP = 0;
    private final static int MARGIN_RIGHT = 0;
    private final static int MARGIN_BOTTOM = 50;

    private boolean isPlaying = false;

    private TilesView tilesView;

    private GameInfoView gameInfoView;

    private GameLogic gameLogic;

    public GamePanel(GameOption gameOption, GameInfoView gameInfoView) {
        this.gameInfoView = gameInfoView;
        gameInfoView.registerTickListener((t) -> {
            repaint();
        });

        addMouseListener(new MouseHandler());
        addKeyListener(new KeyHandler());

        setFocusable(true);

        newGame(gameOption);
    }

    /**
     * Set game board size<br/>
     * Used when game frame resize
     *
     * @param width  width of client frame
     * @param height height of client frame
     */
    public void setGameBoardSize(int width, int height) {
        tilesView.setWidth(width - MARGIN_LEFT - MARGIN_RIGHT);
        tilesView.setHeight(height - MARGIN_TOP - MARGIN_BOTTOM);
        repaint();
    }

    /**
     * Create new game
     *
     * @param gameOption game information
     */
    public void newGame(GameOption gameOption) {
        gameLogic = new GameLogic(gameOption.row(), gameOption.column(), gameOption.emptyPosition());

        // Initialize image pieces/number pieces
        if (gameOption.usedImage()) {
            try {
                tilesView = new ImageTilesView(gameOption.row(), gameOption.column(), this.getWidth(), this.getHeight(), gameOption.puzzleImage());
            } catch (IOException e) {
                tilesView = new NumberTilesView(gameOption.row(), gameOption.column(), this.getWidth(), this.getHeight());
            }
        } else {
            tilesView = new NumberTilesView(gameOption.row(), gameOption.column(), this.getWidth(), this.getHeight());
        }

        setGameBoardSize(getWidth(), getHeight());

        // Reset game information
        gameInfoView.reset();
        gameInfoView.startTimer();

        // Start game
        gameLogic.createGameBoard();
        isPlaying = true;

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (isPlaying) {

            // Draw game board background
            g.setColor(Color.WHITE);
            g.fillRect(MARGIN_LEFT, MARGIN_TOP, tilesView.getWidth(), tilesView.getHeight() + 2);

            // Draw tiles
            for (byte i = 0; i < gameLogic.getRow(); i++) {
                for (byte j = 0; j < gameLogic.getColumn(); j++) {
                    if (gameLogic.emptyTilePosition.x() != i || gameLogic.emptyTilePosition.y() != j) {
                        tilesView.drawOne(g,
                                MARGIN_LEFT,
                                MARGIN_TOP,
                                i,
                                j,
                                gameLogic.tilePositions[i][j].x(),
                                gameLogic.tilePositions[i][j].y());
                    }
                }
            }
        } else {

            // Draw finished game board
            tilesView.drawAll(g, MARGIN_LEFT, MARGIN_TOP);
        }

        // Draw game information
        paintInformation(g);
    }

    /**
     * Draw game information
     *
     * @param g graphics
     */
    private void paintInformation(Graphics g) {
        int y1 = MARGIN_TOP + tilesView.getHeight();
        int y2 = y1 + MARGIN_BOTTOM;
        gameInfoView.paint(g, MARGIN_LEFT + tilesView.getWidth() + MARGIN_RIGHT, y1, y2);
    }

    /**
     * Get piece from mouse position
     *
     * @param x horizontal mouse position
     * @param y vertical mouse position
     * @return piece where mouse position (null if mouse clicked outside game
     * board)
     */
    private TilePosition getPieceFromMousePos(int x, int y) {
        int tempX = (x - MARGIN_LEFT) * gameLogic.getRow() / tilesView.getWidth();
        int tempY = (y - MARGIN_TOP) * gameLogic.getColumn() / tilesView.getHeight();

        if (tempX < 0 || tempX > gameLogic.getRow() - 1 || tempY < 0 || tempY > gameLogic.getColumn() - 1) {
            return null;
        }

        return new TilePosition((byte) tempX, (byte) tempY);
    }

    /**
     * Mouse clicked handler
     */
    private class MouseHandler extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            var mousePosition = getPieceFromMousePos(e.getX(), e.getY());
            if (mousePosition != null && gameLogic.moveTile(mousePosition)) {
                gameInfoView.increaseStep();
                checkFinished();
                repaint();
            }
        }
    }

    /**
     * Keyboard pressed handler
     */
    private class KeyHandler extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            var hasMoved = switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT -> gameLogic.moveEmptyPositionToRight();
                case KeyEvent.VK_RIGHT -> gameLogic.moveEmptyPositionToLeft();
                case KeyEvent.VK_DOWN -> gameLogic.moveEmptyPositionUp();
                case KeyEvent.VK_UP -> gameLogic.moveEmptyPositionDown();
                default -> false;
            };
            if (hasMoved) {
                gameInfoView.increaseStep();
                checkFinished();
                repaint();
            }
        }
    }

    private void checkFinished() {
        if (gameLogic.hasFinished()) {
            gameInfoView.stopTimer();
            isPlaying = false;
        }
    }
}