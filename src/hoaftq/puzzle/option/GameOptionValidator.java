package hoaftq.puzzle.option;

public class GameOptionValidator {

    /**
     * Check if a string is a valid value for row or column
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
