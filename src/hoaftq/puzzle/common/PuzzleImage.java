/**
 * Puzzle game using Java AWT
 */
package hoaftq.puzzle.common;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;

public record PuzzleImage(String fileName, boolean isInternalResource) implements Serializable {

    /**
     * Load image from resource or an external file
     */
    public Image loadImage() throws IOException {
        if (isInternalResource) {
            var resourceURL = getClass().getResource("/hoaftq/puzzle/resource/" + fileName);
            Objects.requireNonNull(resourceURL);

            return ImageIO.read(resourceURL);
        }

        return ImageIO.read(new File(fileName));
    }

    @Override
    public String toString() {
        return isInternalResource ? fileName : new File(fileName).getName();
    }
}
