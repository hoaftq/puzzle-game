/**
 * Puzzle game using Java AWT
 * Copyright @ 2011 Trac Quang Hoa
 */
package hoaftq.puzzle.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

import hoaftq.puzzle.entity.GameInfo;
import hoaftq.puzzle.info.Numbers;
import hoaftq.puzzle.piece.PieceAbstract;
import hoaftq.puzzle.piece.PieceImage;
import hoaftq.puzzle.piece.PieceNumber;

/**
 * Panel display game board
 */
public class GamePanel extends JPanel {

	/**
	 * Create game panel
	 * 
	 * @param gameInfo
	 *            game information
	 * @throws IOException
	 *             an error occurs during reading
	 */
	public GamePanel(GameInfo gameInfo) throws IOException {
		numbers = new Numbers("numbers.gif");

		// Add mouse and keyboard listener
		addMouseListener(new MouseHandler());
		setFocusable(true);
		addKeyListener(new KeyHandler());

		// Timer count time played
		timer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				time++;
				repaint();
			}
		});

		newGame(gameInfo);
	}

	/**
	 * Set game board size<br/>
	 * Used when game frame resize
	 * 
	 * @param width
	 *            width of client frame
	 * @param height
	 *            height of client frame
	 */
	public void setGameBoardSize(int width, int height) {
		pieceObject.setWidth(width - MARGIN_LEFT - MARGIN_RIGHT);
		pieceObject.setHeight(height - MARGIN_TOP - MARGIN_BOTTOM);
		repaint();
	}

	/**
	 * Create new game
	 * 
	 * @param gameInfo
	 *            game information
	 */
	public void newGame(GameInfo gameInfo) {

		// Set subdivision size
		xSplit = gameInfo.getxSplit();
		ySplit = gameInfo.getySplit();

		// Initialize image pieces/number pieces
		if (gameInfo.isUsedImage()) {
			try {
				pieceObject = new PieceImage(gameInfo);
			} catch (IOException e) {
				pieceObject = new PieceNumber(xSplit, ySplit, this.getWidth(),
						this.getHeight());
			}
		} else {
			pieceObject = new PieceNumber(xSplit, ySplit, this.getWidth(),
					this.getHeight());
		}
		setGameBoardSize(getWidth(), getHeight());

		// Initialize empty piece
		switch (gameInfo.getEmptyPosition()) {
		case TOPLEFT:
			emptyPiece = new Piece((byte) 0, (byte) 0);
			break;
		case TOPRIGHT:
			emptyPiece = new Piece((byte) (xSplit - 1), (byte) 0);
			break;
		case BOTTOMRIGHT:
			emptyPiece = new Piece((byte) (xSplit - 1), (byte) (ySplit - 1));
			break;
		case BOTTOMLEFT:
			emptyPiece = new Piece((byte) 0, (byte) (ySplit - 1));
			break;
		}

		// Reset game information
		time = 0;
		step = 0;

		// Start game
		createGameBoard();
		isPlaying = true;
		timer.start();
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (isPlaying) {

			// Draw game board background
			g.setColor(Color.WHITE);
			g.fillRect(MARGIN_LEFT, MARGIN_TOP,
					(pieceObject.getWidth() / pieceObject.getXSplit())
							* pieceObject.getXSplit(),
					(pieceObject.getHeight() / pieceObject.getYSplit())
							* pieceObject.getYSplit() + 2);

			// Draw pieces
			for (byte i = 0; i < xSplit; i++) {
				for (byte j = 0; j < ySplit; j++) {
					if (emptyPiece.x != i || emptyPiece.y != j) {
						pieceObject.drawPiece(g, MARGIN_LEFT, MARGIN_TOP, i, j,
								pieces[i][j].x, pieces[i][j].y);
					}
				}
			}
		} else {

			// Draw finished game board
			pieceObject.drawAll(g, MARGIN_LEFT, MARGIN_TOP);
		}

		// Draw game information
		paintInformation(g);
	}

	/**
	 * Draw game information
	 * 
	 * @param g
	 *            graphics
	 */
	private void paintInformation(Graphics g) {
		int y1 = MARGIN_TOP + pieceObject.getHeight();
		int y2 = y1 + MARGIN_BOTTOM;

		// Draw time played
		numbers.drawNumber(g, 10, y1, y2, time, 4);

		// Draw played step
		numbers.drawNumberRightAlign(g, MARGIN_LEFT + pieceObject.getWidth()
				+ MARGIN_RIGHT - 12, y1, y2, step);
	}

	/**
	 * Create new game board
	 */
	private void createGameBoard() {

		// Initialize original game board
		pieces = new Piece[xSplit][ySplit];
		for (byte i = 0; i < xSplit; i++) {
			for (byte j = 0; j < ySplit; j++) {
				pieces[i][j] = new Piece(i, j);
			}
		}

		// Add game board to a piece list
		LinkedList<Piece> pieceList = new LinkedList<Piece>();
		for (Piece[] pa : pieces) {
			pieceList.addAll(Arrays.asList(pa));
		}
		pieceList.remove(emptyPiece);

		// Random move from empty piece to all piece on the pieces list
		Piece saveEmptyPiece = emptyPiece.clone();
		Random random = new Random();
		while (pieceList.size() > 0) {
			int index = random.nextInt(pieceList.size());
			move(pieceList.get(index));
			pieceList.remove(index);
		}

		// Move empty piece to default position
		move(saveEmptyPiece);
	}

	/**
	 * Random move from empty piece to new piece<br/>
	 * Used to disturbance piece
	 * 
	 * @param piece
	 *            destination move
	 */
	private void move(Piece piece) {
		int stepX;
		int stepY;

		// Calculate horizontal move module step
		if (emptyPiece.x < piece.x) {
			stepX = 1;
		} else if (emptyPiece.x > piece.x) {
			stepX = -1;
		} else {
			stepX = 0;
		}

		// Calculate vertical move module step
		if (emptyPiece.y < piece.y) {
			stepY = 1;
		} else if (emptyPiece.y > piece.y) {
			stepY = -1;
		} else {
			stepY = 0;
		}

		Random random = new Random();
		while (true) {
			if (random.nextBoolean()) {

				// Move horizontally 1 step
				pieces[emptyPiece.x][emptyPiece.y] = pieces[emptyPiece.x
						+ stepX][emptyPiece.y];
				emptyPiece.x += stepX;

				// If can't move horizontally, move vertically until the move
				// completed
				if (emptyPiece.x == piece.x) {
					while (piece.y != emptyPiece.y) {
						pieces[piece.x][emptyPiece.y] = pieces[piece.x][emptyPiece.y
								+ stepY];
						emptyPiece.y += stepY;
					}

					break;
				}
			} else {

				// Move vertically 1 step
				pieces[emptyPiece.x][emptyPiece.y] = pieces[emptyPiece.x][emptyPiece.y
						+ stepY];
				emptyPiece.y += stepY;

				// If can't move vertically, move horizontally until the move
				// completed
				if (emptyPiece.y == piece.y) {
					while (piece.x != emptyPiece.x) {
						pieces[emptyPiece.x][piece.y] = pieces[emptyPiece.x
								+ stepX][piece.y];
						emptyPiece.x += stepX;
					}

					break;
				}
			}
		}
	}

	/**
	 * Check game finished
	 */
	private void checkFinished() {
		boolean isFinished = true;

		// Check pieces on ySplit - 1 rows on the top
		for (int i = 0; i < xSplit; i++) {
			for (int j = 0; j < ySplit - 1; j++) {
				if (pieces[i][j].x != i || pieces[i][j].y != j) {
					isFinished = false;
				}
			}
		}

		// Check pieces on the bottom row
		for (int i = 0; i < xSplit - 1; i++) {
			if (pieces[i][ySplit - 1].x != i
					|| pieces[i][ySplit - 1].y != ySplit - 1) {
				isFinished = false;
			}
		}

		// If game finished, stop timer and toggle game state
		if (isFinished) {
			timer.stop();
			isPlaying = false;
		}
	}

	/**
	 * Get piece from mouse position
	 * 
	 * @param x
	 *            horizontal mouse position
	 * @param y
	 *            vertical mouse position
	 * @return piece where mouse position (null if mouse clicked outside game
	 *         board)
	 */
	private Piece getPieceFromMousePos(int x, int y) {
		int tempX = (x - MARGIN_LEFT) * xSplit / pieceObject.getWidth();
		int tempY = (y - MARGIN_TOP) * ySplit / pieceObject.getHeight();

		if (tempX < 0 || tempX > xSplit - 1 || tempY < 0 || tempY > ySplit - 1) {
			return null;
		}

		return new Piece((byte) tempX, (byte) tempY);
	}

	/**
	 * Game state
	 */
	private boolean isPlaying = false;

	/**
	 * Subdivision horizontal
	 */
	private byte xSplit;

	/**
	 * Subdivision vertical
	 */
	private byte ySplit;

	/**
	 * Piece game board
	 */
	private Piece[][] pieces;

	/**
	 * Empty piece
	 */
	private Piece emptyPiece;

	/**
	 * Piece object (image piece or number piece)
	 */
	private PieceAbstract pieceObject;

	/**
	 * Time played
	 */
	private int time;

	/**
	 * Step moved
	 */
	private int step;

	/**
	 * Timer count time played
	 */
	private Timer timer;

	/**
	 * Number object used to display time played and step
	 */
	private Numbers numbers;

	/**
	 * Left margin of game board
	 */
	private final static int MARGIN_LEFT = 0;

	/**
	 * Top margin of game board
	 */
	private final static int MARGIN_TOP = 0;

	/**
	 * Right margin of game board
	 */
	private final static int MARGIN_RIGHT = 0;

	/**
	 * Bottom margin of game board
	 */
	private final static int MARGIN_BOTTOM = 50;

	private static final long serialVersionUID = 1L;

	/**
	 * Mouse clicked handler
	 */
	private class MouseHandler extends MouseAdapter {

		@Override
		public void mouseClicked(MouseEvent e) {

			// Get piece where mouse clicked
			Piece mousePiece = getPieceFromMousePos(e.getX(), e.getY());
			if (mousePiece == null)
				return;

			// If clicked piece is neighbor empty piece then move it
			if (Math.abs(mousePiece.x - emptyPiece.x)
					+ Math.abs(mousePiece.y - emptyPiece.y) == 1) {
				pieces[emptyPiece.x][emptyPiece.y] = pieces[mousePiece.x][mousePiece.y];
				emptyPiece = mousePiece;
				step++;
				checkFinished();
				repaint();
			}
		}
	}

	/**
	 * Keyboard pressed handler
	 */
	private class KeyHandler extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_LEFT:

				// If have a piece on the right empty piece, move it to left
				if (emptyPiece.x < xSplit - 1) {
					pieces[emptyPiece.x][emptyPiece.y] = pieces[emptyPiece.x + 1][emptyPiece.y];
					emptyPiece.x++;
					step++;
					checkFinished();
					repaint();
				}
				break;
			case KeyEvent.VK_RIGHT:

				// If have a piece on the left empty piece, move it to right
				if (emptyPiece.x != 0) {
					pieces[emptyPiece.x][emptyPiece.y] = pieces[emptyPiece.x - 1][emptyPiece.y];
					emptyPiece.x--;
					step++;
					checkFinished();
					repaint();
				}
				break;
			case KeyEvent.VK_DOWN:

				// If have a piece on the top empty piece, move it to down
				if (emptyPiece.y != 0) {
					pieces[emptyPiece.x][emptyPiece.y] = pieces[emptyPiece.x][emptyPiece.y - 1];
					emptyPiece.y--;
					step++;
					checkFinished();
					repaint();
				}
				break;
			case KeyEvent.VK_UP:

				// If have a piece on the bottom empty piece, move it to up
				if (emptyPiece.y < xSplit - 1) {
					pieces[emptyPiece.x][emptyPiece.y] = pieces[emptyPiece.x][emptyPiece.y + 1];
					emptyPiece.y++;
					step++;
					checkFinished();
					repaint();
				}
				break;
			}
		}
	}

	/**
	 * Describe a piece in game board
	 */
	private class Piece {
		private byte x;
		private byte y;

		public Piece(byte x, byte y) {
			this.x = x;
			this.y = y;
		}

		@Override
		protected Piece clone() {
			return new Piece(x, y);
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof Piece)) {
				return false;
			}

			Piece p = (Piece) o;
			return x == p.x && y == p.y;
		}

		@Override
		public int hashCode() {
			return x * 10 + y;
		}
	}
}