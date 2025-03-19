package hoaftq.puzzle.game;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;

public class GameLogic {
    private final byte row;
    private final byte column;

    private TilePosition[][] tilePositions;
    private TilePosition emptyTilePosition;

    public GameLogic(byte row, byte column, EmptyTilePosition position) {
        this.row = row;
        this.column = column;

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

    public byte getRow() {
        return row;
    }

    public byte getColumn() {
        return column;
    }

    public TilePosition getEmptyTilePosition() {
        return emptyTilePosition;
    }

    public TilePosition getAt(int x, int y) {
        Objects.requireNonNull(tilePositions, "createGameBoard must be called first.");
        return tilePositions[x][y];
    }

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
            moveRandomly(tilePositionList.get(index));
            tilePositionList.remove(index);
        }

        // Move empty piece to default position
        moveRandomly(saveEmptyPiece);
    }

    /**
     * Random move from empty piece to new piece<br/>
     * Used to disturbance piece
     *
     * @param destinationPos destination move
     */
    private void moveRandomly(TilePosition destinationPos) {
        int stepX;
        int stepY;

        // Calculate horizontal move module step
        if (emptyTilePosition.x() < destinationPos.x()) {
            stepX = 1;
        } else if (emptyTilePosition.x() > destinationPos.x()) {
            stepX = -1;
        } else {
            stepX = 0;
        }

        // Calculate vertical move module step
        if (emptyTilePosition.y() < destinationPos.y()) {
            stepY = 1;
        } else if (emptyTilePosition.y() > destinationPos.y()) {
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
                if (emptyTilePosition.x() == destinationPos.x()) {
                    while (destinationPos.y() != emptyTilePosition.y()) {
                        tilePositions[destinationPos.x()][emptyTilePosition.y()] = tilePositions[destinationPos.x()][emptyTilePosition.y()
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
                if (emptyTilePosition.y() == destinationPos.y()) {
                    while (destinationPos.x() != emptyTilePosition.x()) {
                        tilePositions[emptyTilePosition.x()][destinationPos.y()] = tilePositions[emptyTilePosition.x()
                                                                                                 + stepX][destinationPos.y()];
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
    public boolean hasFinished() {
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

    public boolean moveEmptyPositionToRight() {
        if (emptyTilePosition.x() < row - 1) {
            tilePositions[emptyTilePosition.x()][emptyTilePosition.y()] = tilePositions[emptyTilePosition.x() + 1][emptyTilePosition.y()];
            emptyTilePosition = emptyTilePosition.moveHorizontally(1);
            return true;
        }

        return false;
    }

    public boolean moveEmptyPositionToLeft() {
        if (emptyTilePosition.x() != 0) {
            tilePositions[emptyTilePosition.x()][emptyTilePosition.y()] = tilePositions[emptyTilePosition.x() - 1][emptyTilePosition.y()];
            emptyTilePosition = emptyTilePosition.moveHorizontally(-1);
            return true;
        }

        return false;
    }

    public boolean moveEmptyPositionUp() {
        if (emptyTilePosition.y() != 0) {
            tilePositions[emptyTilePosition.x()][emptyTilePosition.y()] = tilePositions[emptyTilePosition.x()][emptyTilePosition.y() - 1];
            emptyTilePosition = emptyTilePosition.moveVertically(-1);
            return true;
        }

        return false;
    }

    public boolean moveEmptyPositionDown() {
        if (emptyTilePosition.y() < getRow() - 1) {
            tilePositions[emptyTilePosition.x()][emptyTilePosition.y()] = tilePositions[emptyTilePosition.x()][emptyTilePosition.y() + 1];
            emptyTilePosition = emptyTilePosition.moveVertically(1);
            return true;
        }

        return false;
    }

    public boolean moveTile(TilePosition position) {
        if (Math.abs(position.x() - emptyTilePosition.x()) + Math.abs(position.y() - emptyTilePosition.y()) == 1) {
            tilePositions[emptyTilePosition.x()][emptyTilePosition.y()] = tilePositions[position.x()][position.y()];
            emptyTilePosition = position;
            return true;
        }

        return false;
    }
}

