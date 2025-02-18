package hoaftq.puzzle.game;

public record TilePosition(byte x, byte y) {

    public TilePosition getLeft() {
        return new TilePosition((byte) (x - 1), y);
    }

    public TilePosition getRight() {
        return new TilePosition((byte) (x + 1), y);
    }

    public TilePosition getUp() {
        return new TilePosition(x, (byte) (y - 1));
    }

    public TilePosition getDown() {
        return new TilePosition(x, (byte) (y + 1));
    }

    public TilePosition moveHorizontally(int step) {
        return new TilePosition((byte) (x + step), y);
    }

    public TilePosition moveVertically(int step) {
        return new TilePosition(x, (byte) (y + step));
    }
}
