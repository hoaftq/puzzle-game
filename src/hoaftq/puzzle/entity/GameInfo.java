/**
 * Puzzle game using Java AWT
 * Copyright @ 2011 Trac Quang Hoa
 */
package hoaftq.puzzle.entity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Game information
 */
public class GameInfo {

	/**
	 * Create game information
	 * 
	 * @param usedImage
	 *            use image or use number
	 * @param fileName
	 *            image file name
	 * @param isInternalResource
	 *            image file on the execute file
	 * @param xSplit
	 *            subdivision horizontal
	 * @param ySplit
	 *            subdivision vertical
	 * @param emptyPosition
	 *            empty piece position
	 */
	public GameInfo(boolean usedImage, String fileName,
			boolean isInternalResource, byte xSplit, byte ySplit,
			EmptyPiecePosition emptyPosition) {
		this.usedImage = usedImage;
		puzzleImage = new PuzzleImage(fileName, isInternalResource);
		this.xSplit = xSplit;
		this.ySplit = ySplit;
		this.emptyPosition = emptyPosition;
	}

	/**
	 * Internal constructor
	 */
	GameInfo() {
	}

	/**
	 * @return use image piece or use number piece
	 */
	public boolean isUsedImage() {
		return usedImage;
	}

	/**
	 * @param usedImage
	 *            use image or use number
	 */
	public void setUsedImage(boolean usedImage) {
		this.usedImage = usedImage;
	}

	/**
	 * @return puzzle image
	 */
	public PuzzleImage getPuzzleImage() {
		return puzzleImage;
	}

	/**
	 * @param puzzleImage
	 *            puzzle image to set
	 */
	public void setPuzzleImage(PuzzleImage puzzleImage) {
		this.puzzleImage = puzzleImage;
	}

	/**
	 * @return horizontal subdivision
	 */
	public byte getxSplit() {
		return xSplit;
	}

	/**
	 * @param xSplit
	 *            horizontal subdivision to set
	 */
	public void setxSplit(byte xSplit) {
		this.xSplit = xSplit;
	}

	/**
	 * @return the vertical subdivision
	 */
	public byte getySplit() {
		return ySplit;
	}

	/**
	 * @param ySplit
	 *            vertical subdivision to set
	 */
	public void setySplit(byte ySplit) {
		this.ySplit = ySplit;
	}

	/**
	 * @return empty piece position
	 */
	public EmptyPiecePosition getEmptyPosition() {
		return emptyPosition;
	}

	/**
	 * @param emptyPosition
	 *            empty piece position to set
	 */
	public void setEmptyPosition(EmptyPiecePosition emptyPosition) {
		this.emptyPosition = emptyPosition;
	}

	/**
	 * Save game information to properties file
	 */
	public void saveData() {
		Properties properties = new Properties();

		// Calculate image type
		String imageType;
		if (usedImage) {
			if (puzzleImage.isInternalResource()) {
				imageType = IMAGE_TYPE_USE_INTERNAL_IMAGE;
			} else {
				imageType = IMAGE_TYPE_USE_EXTERNAL_IMAGE;
			}
			properties.put(IMAGE_NAME_KEY, puzzleImage.getFileName());
		} else {
			imageType = IMAGE_TYPE_USE_NUMBER;
		}
		properties.put(IMAGE_TYPE_KEY, imageType);
		properties.put(X_KEY, Byte.toString(xSplit));
		properties.put(Y_KEY, Byte.toString(ySplit));

		// Save information to properties file
		try {
			properties.store(new FileOutputStream(
					PUZZLE_PROPERTIES_FILENAME_KEY),
					"ImageName: image file name" + LINE_SEPARATOR
							+ "ImageType: " + IMAGE_TYPE_USE_NUMBER
							+ "-use number, " + IMAGE_TYPE_USE_INTERNAL_IMAGE
							+ "-use default image, "
							+ IMAGE_TYPE_USE_EXTERNAL_IMAGE
							+ "-use customize image" + LINE_SEPARATOR
							+ "x: integer from 2 to 10" + LINE_SEPARATOR
							+ "y: integer from 2 to 10");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Load game information from properties file
	 * 
	 * @return game information
	 */
	public static GameInfo loadData() {
		GameInfo gameInfo = new GameInfo();

		// Load information from properties file
		Properties properties = new Properties();
		try {
			properties
					.load(new FileInputStream(PUZZLE_PROPERTIES_FILENAME_KEY));
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Get image type and image file name
		String imageType = properties.getProperty(IMAGE_TYPE_KEY,
				IMAGE_TYPE_USE_INTERNAL_IMAGE);
		String imageFileName = properties.getProperty(IMAGE_NAME_KEY,
				"default.jpg");
		if (!IMAGE_TYPE_USE_NUMBER.equals(imageType)
				&& !IMAGE_TYPE_USE_INTERNAL_IMAGE.equals(imageType)
				&& !IMAGE_TYPE_USE_EXTERNAL_IMAGE.equals(imageType)) {
			imageType = IMAGE_TYPE_USE_INTERNAL_IMAGE;
			imageFileName = "default.jpg";
		} else {
			File file = new File(imageFileName);
			if (IMAGE_TYPE_USE_EXTERNAL_IMAGE.equals(imageType)
					&& !file.exists()) {
				imageType = IMAGE_TYPE_USE_NUMBER;
			}
		}
		gameInfo.setUsedImage(!IMAGE_TYPE_USE_NUMBER.equals(imageType));
		gameInfo.setPuzzleImage(new PuzzleImage(imageFileName,
				IMAGE_TYPE_USE_INTERNAL_IMAGE.equals(imageType)));

		// Get horizontal subdivision
		String xString = properties.getProperty(X_KEY);
		if (!isPieceNumber(xString)) {
			xString = "4";
		}
		gameInfo.setxSplit(Byte.parseByte(xString));

		// Get vertical subdivision
		String yString = properties.getProperty(Y_KEY);
		if (!isPieceNumber(yString)) {
			yString = "4";
		}
		gameInfo.setySplit(Byte.parseByte(yString));

		gameInfo.setEmptyPosition(EmptyPiecePosition.BOTTOMRIGHT);
		return gameInfo;
	}

	/**
	 * Check a string valid piece number(value form 2 to 10)
	 * 
	 * @param value
	 *            piece number string
	 * @return true if is piece number, false otherwise
	 */
	public static boolean isPieceNumber(String value) {
		if (value == null || !value.matches("0*[1-9]\\d{0,1}")) {
			return false;
		}

		int v = Integer.parseInt(value);
		if (v < 2 || v > 10) {
			return false;
		}

		return true;
	}

	/**
	 * Used image or number piece
	 */
	private boolean usedImage;

	/**
	 * Puzzle image
	 */
	private PuzzleImage puzzleImage;

	/**
	 * Subdivision horizontal
	 */
	private byte xSplit;

	/**
	 * Subdivision vertical
	 */
	private byte ySplit;

	/**
	 * Empty piece position
	 */
	private EmptyPiecePosition emptyPosition;

	/**
	 * Properties file name
	 */
	private static final String PUZZLE_PROPERTIES_FILENAME_KEY = "Puzzle.properties";

	/**
	 * Image type key
	 */
	private static final String IMAGE_TYPE_KEY = "ImageType";

	/**
	 * Image name key
	 */
	private static final String IMAGE_NAME_KEY = "ImageName";

	/**
	 * Horizontal subdivision key
	 */
	private static final String X_KEY = "X";

	/**
	 * Vertical subdivision key
	 */
	private static final String Y_KEY = "Y";

	/**
	 * System line separator
	 */
	private static final String LINE_SEPARATOR = System
			.getProperty("line.separator");

	/**
	 * Image type: use number
	 */
	private static final String IMAGE_TYPE_USE_NUMBER = "0";

	/**
	 * Image type: use internal image
	 */
	private static final String IMAGE_TYPE_USE_INTERNAL_IMAGE = "1";

	/**
	 * Image type: use external image
	 */
	private static final String IMAGE_TYPE_USE_EXTERNAL_IMAGE = "2";
}
