package hoaftq.puzzle.entity;

public class GameInfoValidator {

    /**
     * Check if a string is a valid value for row or coumn
     */
    public boolean validateRowOrColumn(String value) {
        try {
            int v = Integer.parseInt(value);
            if (v >= 2 && v <= 10) {
                return true;
            }
        } catch (NumberFormatException ignored) {
        }

        return false;
    }
}
