/**
 * Puzzle game using Java AWT
 */
package hoaftq.puzzle.utility;

import java.awt.*;

public interface WindowUtil {

    /**
     * Center a window to its owner
     *
     * @param window window that is centered to its owner
     */
    static void centerOwner(Window window) {
        var owner = window.getOwner();
        int x, y;

        // If owner is null, set the window to center screen
        if (owner == null) {
            var scrSize = Toolkit.getDefaultToolkit().getScreenSize();
            x = (int) ((scrSize.getWidth() - window.getWidth()) / 2);
            y = (int) ((scrSize.getHeight() - window.getHeight()) / 2);
        } else {

            // Set the window to center owner
            x = owner.getX() + (owner.getWidth() - window.getWidth()) / 2;
            y = owner.getY() + (owner.getHeight() - window.getHeight()) / 2;
        }

        // Ensure dialog in screen
        if (x < 0) x = 0;
        if (y < 0) y = 0;

        window.setLocation(x, y);
    }
}
