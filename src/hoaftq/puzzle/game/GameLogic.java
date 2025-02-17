package hoaftq.puzzle.game;

import hoaftq.puzzle.entity.EmptyTilePosition;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

public class GameLogic {
    private byte row;

    /**
     * Subdivision vertical
     */
    private byte column;

    /**
     * Piece game board
     */
    public TilePosition[][] tilePositions;

    /**
     * Empty piece
     */
    public TilePosition emptyTilePosition;


    public byte getRow() {
        return row;
    }

    public byte getColumn() {
        return column;
    }

    public GameLogic(byte row, byte column, EmptyTilePosition position) {
        this.row = row;
        this.column = column;

        // Initialize empty piece
        switch (position) {
            case TOP_LEFT:
                emptyTilePosition = new TilePosition((byte) 0, (byte) 0);
                break;
            case TOP_RIGHT:
                emptyTilePosition = new TilePosition((byte) (row - 1), (byte) 0);
                break;
            case BOTTOM_RIGHT:
                emptyTilePosition = new TilePosition((byte) (row - 1), (byte) (column - 1));
                break;
            case BOTTOM_LEFT:
                emptyTilePosition = new TilePosition((byte) 0, (byte) (column - 1));
                break;
        }

    }

    /**
     * Create new game board
     */
    public void createGameBoard() {

        // Initialize original game board
        tilePositions = new TilePosition[row][column];
        for (byte i = 0; i < row; i++) {
            for (byte j = 0; j < column; j++) {
                tilePositions[i][j] = new TilePosition(i, j);
            }
        }

        // Add game board to a piece list
        java.util.List<TilePosition> tilePositionList = new LinkedList<>();
        for (TilePosition[] pa : tilePositions) {
            tilePositionList.addAll(Arrays.asList(pa));
        }
        tilePositionList.remove(emptyTilePosition);

        // Random move from empty piece to all piece on the pieces list
//        var saveEmptyPiece = emptyTilePosition.clone();
        var saveEmptyPiece = emptyTilePosition;
        var random = new Random();
        while (tilePositionList.size() > 0) {
            int index = random.nextInt(tilePositionList.size());
            move(tilePositionList.get(index));
            tilePositionList.remove(index);
        }

        // Move empty piece to default position
        move(saveEmptyPiece);
    }

    /**
     * Random move from empty piece to new piece<br/>
     * Used to disturbance piece
     *
     * @param tilePosition destination move
     */
    private void move(TilePosition tilePosition) {
        int stepX;
        int stepY;

        // Calculate horizontal move module step
        if (emptyTilePosition.x() < tilePosition.x()) {
            stepX = 1;
        } else if (emptyTilePosition.x() > tilePosition.x()) {
            stepX = -1;
        } else {
            stepX = 0;
        }

        // Calculate vertical move module step
        if (emptyTilePosition.y() < tilePosition.y()) {
            stepY = 1;
        } else if (emptyTilePosition.y() > tilePosition.y()) {
            stepY = -1;
        } else {
            stepY = 0;
        }

        Random random = new Random();
        while (true) {
            if (random.nextBoolean()) {

                // Move horizontally 1 step
                tilePositions[emptyTilePosition.x()][emptyTilePosition.y()] = tilePositions[emptyTilePosition.x()
                                                                                            + stepX][emptyTilePosition.y()];
//                emptyTilePosition.x += stepX;
                emptyTilePosition = emptyTilePosition.moveHorizontally(stepX);

                // If can't move horizontally, move vertically until the move
                // completed
                if (emptyTilePosition.x() == tilePosition.x()) {
                    while (tilePosition.y() != emptyTilePosition.y()) {
                        tilePositions[tilePosition.x()][emptyTilePosition.y()] = tilePositions[tilePosition.x()][emptyTilePosition.y()
                                                                                                                 + stepY];
//                        emptyTilePosition.y += stepY;
                        emptyTilePosition = emptyTilePosition.moveVertically(stepY);
                    }

                    break;
                }
            } else {

                // Move vertically 1 step
                tilePositions[emptyTilePosition.x()][emptyTilePosition.y()] = tilePositions[emptyTilePosition.x()][emptyTilePosition.y()
                                                                                                                   + stepY];
//                emptyTilePosition.y += stepY;
                emptyTilePosition = emptyTilePosition.moveVertically(stepY);

                // If can't move vertically, move horizontally until the move
                // completed
                if (emptyTilePosition.y() == tilePosition.y()) {
                    while (tilePosition.x() != emptyTilePosition.x()) {
                        tilePositions[emptyTilePosition.x()][tilePosition.y()] = tilePositions[emptyTilePosition.x()
                                                                                               + stepX][tilePosition.y()];
//                        emptyTilePosition.x += stepX;
                        emptyTilePosition = emptyTilePosition.moveHorizontally(stepX);
                    }

                    break;
                }
            }
        }
    }

    /**
     * Check game finished
     */
    public boolean checkFinished() {
        boolean isFinished = true;

        // Check pieces on ySplit - 1 rows on the top
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column - 1; j++) {
                if (tilePositions[i][j].x() != i || tilePositions[i][j].y() != j) {
                    isFinished = false;
                }
            }
        }

        // Check pieces on the bottom row
        for (int i = 0; i < row - 1; i++) {
            if (tilePositions[i][column - 1].x() != i
                || tilePositions[i][column - 1].y() != column - 1) {
                isFinished = false;
            }
        }

        return isFinished;
    }
}
