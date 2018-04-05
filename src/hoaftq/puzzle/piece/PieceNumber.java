/**
 * Puzzle game using Java AWT
 * Copyright @ 2011 Trac Quang Hoa
 */
package hoaftq.puzzle.piece;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * Piece number
 */
public class PieceNumber extends PieceAbstract {

	/**
	 * Create PieceNumber
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
	public PieceNumber(byte xSplit, byte ySplit, int width, int height) {
		super(xSplit, ySplit, width, height);
	}

	@Override
	public void drawPiece(Graphics g, int x, int y, byte xIndexGameBoard,
			byte yIndexGameBoard, byte xIndex, byte yIndex) {

		// Calculate width, height, horizontal coordinate, vertical coordinate
		// of piece in game board
		int pieceGameBoardWidth = width / xSplit;
		int pieceGameBoardHeight = height / ySplit;
		int xGameBoard = x + xIndexGameBoard * pieceGameBoardWidth;
		int yGameBoard = y + yIndexGameBoard * pieceGameBoardHeight;

		// Fill background of piece
		g.setColor(Color.BLACK);
		g.fillRect(xGameBoard, yGameBoard, pieceGameBoardWidth,
				pieceGameBoardHeight);

		// Draw border of piece
		g.setColor(Color.WHITE);
		g.drawRect(xGameBoard, yGameBoard, pieceGameBoardWidth - 1,
				pieceGameBoardHeight - 1);

		// Calculate draw number
		int number = (yIndex * xSplit) + xIndex + 1;
		String numberString = Integer.toString(number);

		// Draw number piece
		Font font = g.getFont().deriveFont(50f);
		g.setFont(font);

		Rectangle2D bound = font.getStringBounds(numberString,
				((Graphics2D) g).getFontRenderContext());
		g.setClip(xGameBoard, yGameBoard, pieceGameBoardWidth,
				pieceGameBoardHeight);
		g.setColor(Color.RED);
		g.drawString(
				Integer.toString(number),
				xGameBoard
						+ (int) ((pieceGameBoardWidth - bound.getWidth()) / 2),
				yGameBoard
						+ (int) ((pieceGameBoardHeight - bound.getHeight()) / 2 - bound
								.getY()));
		g.setClip(null);
	}

	@Override
	public void drawAll(Graphics g, int x, int y) {

		// Draw all piece in its position
		for (byte i = 0; i < xSplit; i++) {
			for (byte j = 0; j < ySplit; j++) {
				drawPiece(g, x, y, i, j, i, j);
			}
		}
	}
}
