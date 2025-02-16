package hoaftq.puzzle.info;

import hoaftq.puzzle.entity.PuzzleImage;

import java.awt.*;
import java.io.IOException;
import java.util.Stack;

/**
 * Class used to draw time played and step moved
 */
public class Numbers {

    /**
     * Image with all the digits
     */
    private final Image image;

    /**
     * Width of each digit
     */
    private final int digitWidth;

    /**
     * Height of each digit
     */
    private final int digitHeight;

    /**
     * Create Numbers with an image file
     *
     * @param imageName image file name in the execute file
     * @throws IOException an error occurs during reading
     */
    public Numbers(String imageName) throws IOException {
        image = new PuzzleImage(imageName, true).loadImage();
        digitWidth = image.getWidth(null) / 10;
        digitHeight = image.getHeight(null);
    }

    /**
     * Draw a positive number with left align
     *
     * @param g           - Graphics
     * @param x1          - left coordinate of number container
     * @param y1          - top coordinate of number container
     * @param y2          - bottom coordinate of number container
     * @param n           - number to draw
     * @param totalDigits - total digit to display, fill 0 on the left if not enough digit
     */
    public void drawNumber(Graphics g, int x1, int y1, int y2, int n, int totalDigits) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be a positive number");
        }

        Stack<Byte> stack = new Stack<>();

        // Push digits of number into stack
        do {
            stack.push((byte) (n % 10));
            n = n / 10;
        } while (n > 0);

        // Fill 0 on the left of number
        for (int i = totalDigits - stack.size(); i >= 1; i--) {
            stack.push((byte) 0);
        }

        // Draw each digit of number
        while (!stack.empty()) {
            drawDigit(g, x1, y1, y2, stack.pop());
            x1 += digitWidth;
        }
    }

    /**
     * Draw a number with right align
     *
     * @param g  - graphics
     * @param x2 - right coordinate digit
     * @param y1 - top coordinate digit container
     * @param y2 - bottom coordinate digit container
     * @param n  - digit draw
     */
    public void drawNumberRightAlign(Graphics g, int x2, int y1, int y2, int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be a positive number");
        }

        // Draw number from right to left
        do {
            drawDigit(g, x2 - digitWidth, y1, y2, (byte) (n % 10));
            n = n / 10;
            x2 -= digitWidth;
        } while (n > 0);
    }

    /**
     * Draw a digit
     *
     * @param g  - graphics
     * @param x1 - left coordinate digit
     * @param y1 - top coordinate digit container
     * @param y2 - bottom coordinate digit container
     * @param d  - digit to draw
     */
    private void drawDigit(Graphics g, int x1, int y1, int y2, byte d) {
        int leftDigit = digitWidth * d;
        int y = (y2 + y1 - digitHeight) / 2;
        g.drawImage(image, x1, y, x1 + digitWidth, y + digitHeight, leftDigit,
                0, leftDigit + digitWidth, digitHeight, null);
    }

}
