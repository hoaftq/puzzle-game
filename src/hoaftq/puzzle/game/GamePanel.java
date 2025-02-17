/**
 * Puzzle game using Java AWT
 * Copyright @ 2011 Trac Quang Hoa
 */
package hoaftq.puzzle.game;

import hoaftq.puzzle.entity.GameOption;
import hoaftq.puzzle.piece.ImageTilesView;
import hoaftq.puzzle.piece.NumberTilesView;
import hoaftq.puzzle.piece.TilesView;

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

    /**
     * Game state
     */
    private boolean isPlaying = false;


    /**
     * Piece object (image piece or number piece)
     */
    private TilesView tilesView;

    private GameInfo gameInfo;

    private GameLogic gameLogic;

    /**
     * Create game panel
     *
     * @param gameOption game information
     * @throws IOException an error occurs during reading
     */
    public GamePanel(GameOption gameOption, GameInfo gameInfo) throws IOException {
        this.gameInfo = gameInfo;

        // Add mouse and keyboard listener
        addMouseListener(new MouseHandler());
        setFocusable(true);
        addKeyListener(new KeyHandler());

        gameInfo.setTickListener((t) -> {
            repaint();
        });

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
        gameInfo.reset();
        gameInfo.startTimer();

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

        gameInfo.paintInformation(g, MARGIN_LEFT + tilesView.getWidth()
                                     + MARGIN_RIGHT, y1, y2);
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

            // Get piece where mouse clicked
            TilePosition mouseTilePosition = getPieceFromMousePos(e.getX(), e.getY());
            if (mouseTilePosition == null)
                return;

            // If clicked piece is neighbor empty piece then move it
            if (Math.abs(mouseTilePosition.x() - gameLogic.emptyTilePosition.x())
                + Math.abs(mouseTilePosition.y() - gameLogic.emptyTilePosition.y()) == 1) {
                gameLogic.tilePositions[gameLogic.emptyTilePosition.x()][gameLogic.emptyTilePosition.y()] = gameLogic.tilePositions[mouseTilePosition.x()][mouseTilePosition.y()];
                gameLogic.emptyTilePosition = mouseTilePosition;
//                step++;
                gameInfo.increaseStep();
                checkFinished();
                // If game finished, stop timer and toggle game state
//            timer.stop();
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
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:

                    // If have a piece on the right empty piece, move it to left
                    if (gameLogic.emptyTilePosition.x() < gameLogic.getRow() - 1) {
                        gameLogic.tilePositions[gameLogic.emptyTilePosition.x()][gameLogic.emptyTilePosition.y()] = gameLogic.tilePositions[gameLogic.emptyTilePosition.x() + 1][gameLogic.emptyTilePosition.y()];
//                        emptyTilePosition.x++;
                        gameLogic.emptyTilePosition = gameLogic.emptyTilePosition.moveHorizontally(1);
//                        step++;
                        gameInfo.increaseStep();
                        checkFinished();
                        repaint();
                    }
                    break;
                case KeyEvent.VK_RIGHT:

                    // If have a piece on the left empty piece, move it to right
                    if (gameLogic.emptyTilePosition.x() != 0) {
                        gameLogic.tilePositions[gameLogic.emptyTilePosition.x()][gameLogic.emptyTilePosition.y()] = gameLogic.tilePositions[gameLogic.emptyTilePosition.x() - 1][gameLogic.emptyTilePosition.y()];
//                        emptyTilePosition.x--;
                        gameLogic.emptyTilePosition = gameLogic.emptyTilePosition.moveHorizontally(-1);
//                        step++;
                        gameInfo.increaseStep();
                        checkFinished();
                        repaint();
                    }
                    break;
                case KeyEvent.VK_DOWN:

                    // If have a piece on the top empty piece, move it to down
                    if (gameLogic.emptyTilePosition.y() != 0) {
                        gameLogic.tilePositions[gameLogic.emptyTilePosition.x()][gameLogic.emptyTilePosition.y()] = gameLogic.tilePositions[gameLogic.emptyTilePosition.x()][gameLogic.emptyTilePosition.y() - 1];
//                        emptyTilePosition.y--;
                        gameLogic.emptyTilePosition = gameLogic.emptyTilePosition.moveVertically(-1);
//                        step++;
                        gameInfo.increaseStep();
                        checkFinished();
                        repaint();
                    }
                    break;
                case KeyEvent.VK_UP:

                    // If have a piece on the bottom empty piece, move it to up
                    if (gameLogic.emptyTilePosition.y() < gameLogic.getRow() - 1) {
                        gameLogic.tilePositions[gameLogic.emptyTilePosition.x()][gameLogic.emptyTilePosition.y()] = gameLogic.tilePositions[gameLogic.emptyTilePosition.x()][gameLogic.emptyTilePosition.y() + 1];
//                        emptyTilePosition.y++;
                        gameLogic.emptyTilePosition = gameLogic.emptyTilePosition.moveVertically(1);
//                        step++;
                        gameInfo.increaseStep();
                        checkFinished();
                        repaint();
                    }
                    break;
            }
        }
    }

    private void checkFinished() {
        if (gameLogic.checkFinished()) {
            gameInfo.stopTimer();
            isPlaying = false;
        }
    }
}