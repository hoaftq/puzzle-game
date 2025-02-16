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
public class ImageView extends TilesView {

	/**
	 * Create piece image, use real size of the image
	 * 
	 * @param gameInfo
	 *            game information
	 * @throws IOException
	 *             an error occurs during reading
	 */
	public ImageView(GameInfo gameInfo) throws IOException {
		super(gameInfo.row(), gameInfo.column());
		if (gameInfo.puzzleImage() == null) {
			throw new IllegalArgumentException("gameInfo");
		}

		image = gameInfo.puzzleImage().loadImage();
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
	public ImageView(GameInfo gameInfo, int width, int height)
			throws IOException {
		super(gameInfo.row(), gameInfo.column(), width, height);
		if (gameInfo.puzzleImage() == null) {
			throw new IllegalArgumentException("gameInfo");
		}

		image = gameInfo.puzzleImage().loadImage();
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
	public ImageView(byte xSplit, byte ySplit, int width, int height,
					 PuzzleImage puzzleImage) throws IOException {
		super(xSplit, ySplit, width, height);

		image = puzzleImage.loadImage();
	}

	@Override
	public void drawOne(Graphics g, int left, int top, byte xOnGameBoard,
						byte yOnGameBoard, byte xOnImageNumbers, byte yOnImageNumbers) {

		// Calculate width, height, horizontal coordinate and vertical
		// coordinate of piece in game board
		int pieceGameBoardWidth = width / row;
		int pieceGameBoardHeight = height / column;
		int xGameBoard = left + xOnGameBoard * pieceGameBoardWidth;
		int yGameBoard = top + yOnGameBoard * pieceGameBoardHeight;

		// Calculate width, height, horizontal coordinate and vertical
		// coordinate of piece in image pieces
		int pieceImageWidth = image.getWidth(null) / row;
		int pieceImageHeight = image.getHeight(null) / column;
		int xImage = xOnImageNumbers * pieceImageWidth;
		int yImage = yOnImageNumbers * pieceImageHeight;

		// Draw piece image
		g.drawImage(image, xGameBoard + 1, yGameBoard + 1, xGameBoard
				+ pieceGameBoardWidth - 1, yGameBoard + pieceGameBoardHeight
				- 1, xImage, yImage, xImage + pieceImageWidth, yImage
				+ pieceImageHeight, null);
	}

	@Override
	public void drawAll(Graphics g, int left, int top) {
		g.drawImage(image, left, top, left + width, left + height, 0, 0,
				image.getWidth(null), image.getHeight(null), null);
	}

	/**
	 * Subdivision image
	 */
	private Image image;
}
