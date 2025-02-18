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

/**
 * Puzzle image
 */
public record PuzzleImage(String fileName, boolean isInternalResource) implements Serializable {

    /**
     * Create puzzle image
     *
     * @param fileName           image file name
     * @param isInternalResource is on the execute file
     */
    public PuzzleImage(String fileName, boolean isInternalResource) {
        this.fileName = fileName;
        this.isInternalResource = isInternalResource;
    }

    /**
     * @return image file name
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @return is an embedded image or external one
     */
    public boolean isInternalResource() {
        return isInternalResource;
    }

    /**
     * Load image from resource or a file
     *
     * @return image
     * @throws IOException an error occurs during reading
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
        if (isInternalResource) {
            return fileName;
        }

        return new File(fileName).getName();
    }
}
