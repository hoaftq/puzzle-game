/**
 * Puzzle game using Java AWT
 * Copyright @ 2011 Trac Quang Hoa
 */
package hoaftq.puzzle.info;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.util.Stack;

import hoaftq.puzzle.entity.PuzzleImage;

/**
 * Class used to draw time played and step moved
 */
public class Numbers {

	/**
	 * Create Numbers with an image file
	 * 
	 * @param imageName
	 *            image file name in the execute file
	 * @throws IOException
	 *             an error occurs during reading
	 */
	public Numbers(String imageName) throws IOException {
		image = new PuzzleImage(imageName, true).loadImage();
		widthDigit = image.getWidth(null) / 10;
		heightDigit = image.getHeight(null);
	}

	/**
	 * Draw a positive number with left align
	 * 
	 * @param g
	 *            - Graphics
	 * @param x1
	 *            - left coordinate of number container
	 * @param y1
	 *            - top coordinate of number container
	 * @param y2
	 *            - bottom coordinate of number container
	 * @param n
	 *            - number draw
	 * @param cntFixDigit
	 *            - count number digit draw, fill 0 on the left if not enough
	 *            digit
	 */
	public void drawNumber(Graphics g, int x1, int y1, int y2, int n,
			int cntFixDigit) {
		if (n < 0) {
			throw new IllegalArgumentException("n must be a positive number");
		}

		Stack<Byte> stack = new Stack<Byte>();

		// Push digits of number into stack
		do {
			stack.push((byte) (n % 10));
			n = n / 10;
		} while (n > 0);

		// Fill 0 on the left of number
		for (int i = cntFixDigit - stack.size(); i >= 1; i--) {
			stack.push((byte) 0);
		}

		// Draw each digit of number
		while (!stack.empty()) {
			drawDigit(g, x1, y1, y2, stack.pop());
			x1 += widthDigit;
		}
	}

	/**
	 * Draw a number with right align
	 * 
	 * @param g
	 *            - graphics
	 * @param x2
	 *            - right coordinate digit
	 * @param y1
	 *            - top coordinate digit container
	 * @param y2
	 *            - bottom coordinate digit container
	 * @param n
	 *            - digit draw
	 */
	public void drawNumberRightAlign(Graphics g, int x2, int y1, int y2, int n) {
		if (n < 0) {
			throw new IllegalArgumentException("n must be a positive number");
		}

		// Draw number from right to left
		do {
			drawDigit(g, x2 - widthDigit, y1, y2, (byte) (n % 10));
			n = n / 10;
			x2 -= widthDigit;
		} while (n > 0);
	}

	/**
	 * Draw a digit
	 * 
	 * @param g
	 *            - graphics
	 * @param x1
	 *            - left coordinate digit
	 * @param y1
	 *            - top coordinate digit container
	 * @param y2
	 *            - bottom coordinate digit container
	 * @param d
	 *            - digit draw
	 */
	private void drawDigit(Graphics g, int x1, int y1, int y2, byte d) {
		int leftDigit = widthDigit * d;
		int y = (y2 + y1 - heightDigit) / 2;
		g.drawImage(image, x1, y, x1 + widthDigit, y + heightDigit, leftDigit,
				0, leftDigit + widthDigit, heightDigit, null);
	}

	/**
	 * Digits image
	 */
	private Image image;

	/**
	 * Width of a digit
	 */
	private int widthDigit;

	/**
	 * Height of digit
	 */
	private int heightDigit;
}
