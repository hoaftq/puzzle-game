/**
 * Puzzle game using Java AWT
 * Copyright @ 2011 Trac Quang Hoa
 */
package hoaftq.puzzle.piece;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import hoaftq.puzzle.entity.GameInfo;
import hoaftq.puzzle.entity.PuzzleImage;

/**
 * Piece image
 */
public class PieceImage extends PieceAbstract {

	/**
	 * Create piece image, use real size of the image
	 * 
	 * @param gameInfo
	 *            game information
	 * @throws IOException
	 *             an error occurs during reading
	 */
	public PieceImage(GameInfo gameInfo) throws IOException {
		super(gameInfo.getxSplit(), gameInfo.getySplit());
		if (gameInfo.getPuzzleImage() == null) {
			throw new IllegalArgumentException("gameInfo");
		}

		image = gameInfo.getPuzzleImage().loadImage();
		width = image.getWidth(null);
		height = image.getHeight(null);
	}

	/**
	 * Create piece image
	 * 
	 * @param gameInfo
	 *            game information
	 * @param width
	 *            width of game board
	 * @param height
	 *            height of game board
	 * @throws IOException
	 *             an error occurs during reading
	 */
	public PieceImage(GameInfo gameInfo, int width, int height)
			throws IOException {
		super(gameInfo.getxSplit(), gameInfo.getySplit(), width, height);
		if (gameInfo.getPuzzleImage() == null) {
			throw new IllegalArgumentException("gameInfo");
		}

		image = gameInfo.getPuzzleImage().loadImage();
	}

	/**
	 * Create piece image
	 * 
	 * @param xSplit
	 *            horizontal subdivision
	 * @param ySplit
	 *            vertical subdivision
	 * @param width
	 *            width of game board
	 * @param height
	 *            height of game board
	 * @param puzzleImage
	 *            puzzle image
	 * @throws IOException
	 *             an error occurs during reading
	 */
	public PieceImage(byte xSplit, byte ySplit, int width, int height,
			PuzzleImage puzzleImage) throws IOException {
		super(xSplit, ySplit, width, height);

		image = puzzleImage.loadImage();
	}

	@Override
	public void drawPiece(Graphics g, int x, int y, byte xIndexGameBoard,
			byte yIndexGameBoard, byte xIndex, byte yIndex) {

		// Calculate width, height, horizontal coordinate and vertical
		// coordinate of piece in game board
		int pieceGameBoardWidth = width / xSplit;
		int pieceGameBoardHeight = height / ySplit;
		int xGameBoard = x + xIndexGameBoard * pieceGameBoardWidth;
		int yGameBoard = y + yIndexGameBoard * pieceGameBoardHeight;

		// Calculate width, height, horizontal coordinate and vertical
		// coordinate of piece in image pieces
		int pieceImageWidth = image.getWidth(null) / xSplit;
		int pieceImageHeight = image.getHeight(null) / ySplit;
		int xImage = xIndex * pieceImageWidth;
		int yImage = yIndex * pieceImageHeight;

		// Draw piece image
		g.drawImage(image, xGameBoard + 1, yGameBoard + 1, xGameBoard
				+ pieceGameBoardWidth - 1, yGameBoard + pieceGameBoardHeight
				- 1, xImage, yImage, xImage + pieceImageWidth, yImage
				+ pieceImageHeight, null);
	}

	@Override
	public void drawAll(Graphics g, int x, int y) {
		g.drawImage(image, x, y, x + width, x + height, 0, 0,
				image.getWidth(null), image.getHeight(null), null);
	}

	/**
	 * Subdivision image
	 */
	private Image image;
}
