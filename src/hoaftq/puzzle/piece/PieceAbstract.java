/**
 * Puzzle game using Java AWT
 * Copyright @ 2011 Trac Quang Hoa
 */
package hoaftq.puzzle.piece;

import java.awt.Graphics;

/**
 * Piece image/piece number abstract class
 */
public abstract class PieceAbstract {

	/**
	 * Create piece object without width and height of game board
	 * 
	 * @param xSplit
	 *            subdivision horizontal
	 * @param ySplit
	 *            subdivision vertical
	 */
	public PieceAbstract(byte xSplit, byte ySplit) {
		this.xSplit = xSplit;
		this.ySplit = ySplit;
	}

	/**
	 * Create piece object with width and height of game board
	 * 
	 * @param xSplit
	 *            subdivision horizontal
	 * @param ySplit
	 *            subdivision vertical
	 * @param width
	 *            width of game board
	 * @param height
	 *            height of game board
	 */
	public PieceAbstract(byte xSplit, byte ySplit, int width, int height) {
		this.xSplit = xSplit;
		this.ySplit = ySplit;
		this.width = width;
		this.height = height;
	}

	/**
	 * @return the subdivision horizontal
	 */
	public byte getXSplit() {
		return xSplit;
	}

	/**
	 * @return the subdivision vertical
	 */
	public byte getYSplit() {
		return ySplit;
	}

	/**
	 * @return the width of game board
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * @param width
	 *            the width of game board to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return the height of game board
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            the height of game board to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @param g
	 *            graphics
	 * @param x
	 *            left coordinate of game board
	 * @param y
	 *            top coordinate of game board
	 * @param xIndexGameBoard
	 *            x of piece in game board
	 * @param yIndexGameBoard
	 *            y of piece in game board
	 * @param xIndex
	 *            x of piece in piece image/piece number
	 * @param yIndex
	 *            y of piece in piece image/piece number
	 */
	public abstract void drawPiece(Graphics g, int x, int y,
			byte xIndexGameBoard, byte yIndexGameBoard, byte xIndex, byte yIndex);

	/**
	 * Draw competed game board
	 * 
	 * @param g
	 *            graphics
	 * @param x
	 *            left coordinate of game board
	 * @param y
	 *            top coordinate of game board
	 */
	public abstract void drawAll(Graphics g, int x, int y);

	/**
	 * Subdivision horizontal
	 */
	protected byte xSplit;

	/**
	 * Subdivision vertical
	 */
	protected byte ySplit;

	/**
	 * Width of game board
	 */
	protected int width;

	/**
	 * Height of game board
	 */
	protected int height;
}
