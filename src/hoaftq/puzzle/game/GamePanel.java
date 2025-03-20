/**
 * Puzzle game using Java AWT
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

    private final GameInfoView gameInfoView;
    private TilesView tilesView;
    private GameLogic gameLogic;

    private boolean isPlaying = false;

    public GamePanel(GameInfoView gameInfoView) {
        this.gameInfoView = gameInfoView;
        this.gameInfoView.registerTickListener(t -> repaint());

        addMouseListener(new MouseHandler());
        addKeyListener(new KeyHandler());

        setFocusable(true);
    }

    /**
     * Set game board size.
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
     */
    public void newGame(GameOption gameOption) {
        gameLogic = new GameLogic(gameOption.row(), gameOption.column(), gameOption.emptyPosition());
        tilesView = createTitleView(gameOption);

        // Reset game information
        gameInfoView.reset();
        gameInfoView.startTimer();

        // Start game
        gameLogic.createGameBoard();
        isPlaying = true;

        setGameBoardSize(getWidth(), getHeight());
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
                    if (gameLogic.getEmptyTilePosition().x() == i && gameLogic.getEmptyTilePosition().y() == j) {
                        continue;
                    }

                    tilesView.drawOne(g,
                            MARGIN_LEFT,
                            MARGIN_TOP,
                            i,
                            j,
                            gameLogic.getAt(i, j).x(),
                            gameLogic.getAt(i, j).y());
                }
            }
        } else {
            // Draw finished game board
            tilesView.drawAll(g, MARGIN_LEFT, MARGIN_TOP);
        }

        // Draw game information
        paintInformation(g);
    }

    private TilesView createTitleView(GameOption gameOption) {
        if (gameOption.usedImage()) {
            try {
                return new ImageTilesView(
                        gameOption.row(),
                        gameOption.column(),
                        this.getWidth(),
                        this.getHeight(),
                        gameOption.puzzleImage());
            } catch (IOException ignored) {
            }
        }

        return new NumberTilesView(gameOption.row(), gameOption.column(), this.getWidth(), this.getHeight());
    }

    private void paintInformation(Graphics g) {
        int y1 = MARGIN_TOP + tilesView.getHeight();
        int y2 = y1 + MARGIN_BOTTOM;
        gameInfoView.paint(g, MARGIN_LEFT + tilesView.getWidth() + MARGIN_RIGHT, y1, y2);
    }

    private void updateAfterMoving() {
        gameInfoView.increaseStep();
        if (gameLogic.hasFinished()) {
            gameInfoView.stopTimer();
            isPlaying = false;
        }

        repaint();
    }

    /**
     * Mouse clicked handler
     */
    private class MouseHandler extends MouseAdapter {

        @Override
        public void mouseClicked(MouseEvent e) {
            var mousePosition = getTileFromMousePos(e.getX(), e.getY());
            if (mousePosition != null && gameLogic.moveEmptyPositionTo(mousePosition)) {
                updateAfterMoving();
            }
        }

        /**
         * Get tile from mouse position
         *
         * @param mouseX horizontal mouse position
         * @param mouseY vertical mouse position
         * @return tile where mouse is (null if mouse is outside game board)
         */
        private TilePosition getTileFromMousePos(int mouseX, int mouseY) {
            int x = (mouseX - MARGIN_LEFT) * gameLogic.getRow() / tilesView.getWidth();
            int y = (mouseY - MARGIN_TOP) * gameLogic.getColumn() / tilesView.getHeight();

            if (x < 0 || x > gameLogic.getRow() - 1 || y < 0 || y > gameLogic.getColumn() - 1) {
                return null;
            }

            return new TilePosition((byte) x, (byte) y);
        }
    }

    /**
     * Keyboard pressed handler
     */
    private class KeyHandler extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            var hasMoved = switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT -> gameLogic.moveEmptyPositionHorizontally(1);
                case KeyEvent.VK_RIGHT -> gameLogic.moveEmptyPositionHorizontally(-1);
                case KeyEvent.VK_DOWN -> gameLogic.moveEmptyPositionVertically(-1);
                case KeyEvent.VK_UP -> gameLogic.moveEmptyPositionVertically(1);
                default -> false;
            };
            if (hasMoved) {
                updateAfterMoving();
            }
        }
    }
}