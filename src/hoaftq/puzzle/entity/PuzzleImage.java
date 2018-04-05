/**
 * Puzzle game using Java AWT
 * Copyright @ 2011 Trac Quang Hoa
 */
package hoaftq.puzzle.entity;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;

/**
 * Puzzle image
 */
public class PuzzleImage implements Serializable {

	/**
	 * Create puzzle image
	 * 
	 * @param fileName
	 *            image file name
	 * @param isInternalResource
	 *            is on the execute file
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
	 * @return is image file on the execute file
	 */
	public boolean isInternalResource() {
		return isInternalResource;
	}

	/**
	 * Load image from resource or file
	 * 
	 * @param puzzleImage
	 *            puzzle image
	 * @return image
	 * @throws IOException
	 *             an error occurs during reading
	 */
	public Image loadImage() throws IOException {
		Image image;
		if (isInternalResource) {
			image = ImageIO.read(getClass().getResource(
					"/hoaftq/puzzle/resource/" + fileName));
		} else {
			image = ImageIO.read(new File(fileName));
		}

		return image;
	}

	@Override
	public String toString() {
		if (isInternalResource) {
			return fileName;
		} else {
			File file = new File(fileName);
			return file.getName();
		}
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof PuzzleImage) || fileName == null) {
			return false;
		}
		PuzzleImage imageName = (PuzzleImage) o;

		return fileName.equals(imageName.fileName)
				&& isInternalResource == imageName.isInternalResource;
	}

	@Override
	public int hashCode() {
		String internalResourceString = String.valueOf(isInternalResource);
		if (fileName == null) {
			return internalResourceString.hashCode();
		}

		return (fileName + internalResourceString).hashCode();
	}

	/**
	 * Image file name
	 */
	private String fileName;

	/**
	 * Is image file on the execute file
	 */
	private boolean isInternalResource;
	private static final long serialVersionUID = 1L;
}
