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

        emptyTilePosition = switch (position) {
            case TOP_LEFT -> new TilePosition((byte) 0, (byte) 0);
            case TOP_RIGHT -> new TilePosition((byte) (row - 1), (byte) 0);
            case BOTTOM_RIGHT -> new TilePosition((byte) (row - 1), (byte) (column - 1));
            case BOTTOM_LEFT -> new TilePosition((byte) 0, (byte) (column - 1));
        };
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
        initializeTiles();
        shuffleTiles();
    }

    private void initializeTiles() {
        tilePositions = new TilePosition[row][column];
        for (byte i = 0; i < row; i++) {
            for (byte j = 0; j < column; j++) {
                tilePositions[i][j] = new TilePosition(i, j);
            }
        }
    }

    private void shuffleTiles() {
        var tilePositions = new LinkedList<TilePosition>();
        for (TilePosition[] pa : this.tilePositions) {
            tilePositions.addAll(Arrays.asList(pa));
        }
        tilePositions.remove(emptyTilePosition);

        // Random move from empty piece to all piece on the pieces list
        var savedEmptyTilePosition = emptyTilePosition;
        var random = new Random();
        while (!tilePositions.isEmpty()) {
            int index = random.nextInt(tilePositions.size());
            moveRandomly(tilePositions.get(index));
            tilePositions.remove(index);
        }

        // Move empty piece to default position
        moveRandomly(savedEmptyTilePosition);
    }

    /**
     * Random move from empty tile to destination position
     */
    private void moveRandomly(TilePosition destinationPos) {
        // Calculate horizontal move step
        var stepX = Byte.compare(destinationPos.x(), emptyTilePosition.x());

        // Calculate vertical move step
        var stepY = Byte.compare(destinationPos.y(), emptyTilePosition.y());

        var random = new Random();
        while (true) {
            if (random.nextBoolean()) {

                // Move horizontally 1 step
                moveEmptyPositionHorizontallyWithoutChecking(stepX);

                // If the empty position can't be moved horizontally anymore, move vertically until reaching the destination
                if (emptyTilePosition.x() == destinationPos.x()) {
                    while (emptyTilePosition.y() != destinationPos.y()) {
                        moveEmptyPositionVerticallyWithoutChecking(stepY);
                    }

                    break;
                }
            } else {

                // Move vertically 1 step
                moveEmptyPositionVerticallyWithoutChecking(stepY);

                // If the empty position can't be moved vertically anymore, move horizontally until reaching the destination
                if (emptyTilePosition.y() == destinationPos.y()) {
                    while (destinationPos.x() != emptyTilePosition.x()) {
                        moveEmptyPositionHorizontallyWithoutChecking(stepX);
                    }

                    break;
                }
            }
        }
    }

    /**
     * Check if the game finishes
     */
    public boolean hasFinished() {
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < column; j++) {
                if (tilePositions[i][j].x() != i || tilePositions[i][j].y() != j) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean moveEmptyPositionHorizontally(int step) {
        if (emptyTilePosition.x() + step >= 0 && emptyTilePosition.x() + step < row) {
            moveEmptyPositionHorizontallyWithoutChecking(step);
            return true;
        }

        return false;
    }

    public boolean moveEmptyPositionVertically(int step) {
        if (emptyTilePosition.y() + step >= 0 && emptyTilePosition.y() + step < column) {
            moveEmptyPositionVerticallyWithoutChecking(step);
            return true;
        }

        return false;
    }

    public boolean moveEmptyPositionTo(TilePosition position) {
        if (Math.abs(position.x() - emptyTilePosition.x()) + Math.abs(position.y() - emptyTilePosition.y()) == 1) {
            tilePositions[emptyTilePosition.x()][emptyTilePosition.y()] = tilePositions[position.x()][position.y()];
            emptyTilePosition = position;
            return true;
        }

        return false;
    }

    private void moveEmptyPositionHorizontallyWithoutChecking(int step) {
        tilePositions[emptyTilePosition.x()][emptyTilePosition.y()]
                = tilePositions[emptyTilePosition.x() + step][emptyTilePosition.y()];
        emptyTilePosition = emptyTilePosition.moveHorizontally(step);
    }

    private void moveEmptyPositionVerticallyWithoutChecking(int step) {
        tilePositions[emptyTilePosition.x()][emptyTilePosition.y()]
                = tilePositions[emptyTilePosition.x()][emptyTilePosition.y() + step];
        emptyTilePosition = emptyTilePosition.moveVertically(step);
    }
}

