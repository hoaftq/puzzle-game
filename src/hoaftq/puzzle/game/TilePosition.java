package hoaftq.puzzle.game;

public record TilePosition(byte x, byte y) {

    public TilePosition moveHorizontally(int step) {
        return new TilePosition((byte) (x + step), y);
    }

    public TilePosition moveVertically(int step) {
        return new TilePosition(x, (byte) (y + step));
    }
}
