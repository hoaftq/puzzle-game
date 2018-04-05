/**
 * Puzzle game using Java AWT
 * Copyright @ 2011 Trac Quang Hoa
 */
package hoaftq.puzzle.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileFilter;

import hoaftq.puzzle.entity.GameInfo;
import hoaftq.puzzle.entity.PuzzleImage;
import hoaftq.puzzle.piece.PieceAbstract;
import hoaftq.puzzle.piece.PieceImage;
import hoaftq.puzzle.piece.PieceNumber;
import hoaftq.puzzle.utility.WindowUtil;

/**
 * Option game dialog
 */
public class OptionDialog extends JDialog {

	/**
	 * Create option dialog
	 * 
	 * @param owner
	 *            owner of dialog
	 * @param gameInfo
	 *            game information
	 */
	public OptionDialog(JFrame owner, GameInfo gameInfo) {
		super(owner, "Customize Game", true);
		this.gameInfo = gameInfo;

		add(createImageNumberOptionPanel(), BorderLayout.NORTH);
		add(createImagePanel(), BorderLayout.CENTER);
		add(createSubdivisionAndOperatorPanel(), BorderLayout.SOUTH);

		pack();
		setResizable(false);
		setDefaultCloseOperation(HIDE_ON_CLOSE);

		// Display initial image panel
		if (gameInfo.isUsedImage()) {
			puzzleImageList.setEnabled(true);
			browseButton.setEnabled(true);

			imagePanel.setPuzzleImage(gameInfo.getPuzzleImage());
		} else {
			puzzleImageList.setEnabled(false);
			browseButton.setEnabled(false);

			imagePanel.setNumbers();
		}
	}

	/**
	 * @return game information
	 */
	public GameInfo getGameInfo() {
		return gameInfo;
	}

	/**
	 * Show dialog
	 * 
	 * @return true if user choose OK button, false otherwise
	 */
	public boolean showDialog() {
		isOK = false;
		WindowUtil.centerOwner(this);
		setVisible(true);
		return isOK;
	}

	/**
	 * Create use image/use number option button panel
	 * 
	 * @return the use image/use number panel
	 */
	private JPanel createImageNumberOptionPanel() {
		JPanel imageNumberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		ButtonGroup imageNumberButtonGroup = new ButtonGroup();

		imageRadioButton = createImageNumberRadioButton(imageNumberPanel,
				imageNumberButtonGroup, "Use image", gameInfo.isUsedImage());
		numberRadioButton = createImageNumberRadioButton(imageNumberPanel,
				imageNumberButtonGroup, "Use number", !gameInfo.isUsedImage());

		return imageNumberPanel;
	}

	/**
	 * Create use image or use number radio button
	 * 
	 * @param owner
	 *            owner of use image and use number radio button
	 * @param text
	 *            caption display
	 * @param selected
	 *            selected or not
	 */
	private JRadioButton createImageNumberRadioButton(JPanel owner,
			ButtonGroup buttonGroup, String text, boolean selected) {
		JRadioButton radioButton = new JRadioButton(text, selected);
		radioButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// If user choose to use image
				if (e.getSource() == imageRadioButton) {
					puzzleImageList.setEnabled(true);
					browseButton.setEnabled(true);

					// Display selected image
					imagePanel.setPuzzleImage((PuzzleImage) puzzleImageList
							.getSelectedValue());
				} else if (e.getSource() == numberRadioButton) {

					// If user choose to use number
					puzzleImageList.setEnabled(false);
					browseButton.setEnabled(false);

					// Display number piece
					imagePanel.setNumbers();
				}
			}
		});
		buttonGroup.add(radioButton);
		owner.add(radioButton);

		return radioButton;
	}

	/**
	 * Create panel used to display and browse image
	 * 
	 * @return the image panel
	 */
	private JPanel createImagePanel() {
		JPanel panel = new JPanel(new BorderLayout());

		// Create panel display image
		imagePanel = new ImagePanel(320, 200);
		panel.add(imagePanel, BorderLayout.NORTH);

		// Create panel browse image
		JPanel imageControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		// Create puzzle image list box
		final DefaultListModel<PuzzleImage> listModel = new DefaultListModel<PuzzleImage>();
		listModel.addElement(new PuzzleImage("default.jpg", true));

		// Add puzzle image list load from data file
		for (Object im : loadPuzzleImageList()) {
			listModel.addElement((PuzzleImage) im);
		}
		puzzleImageList = new JList<PuzzleImage>(listModel);
		puzzleImageList.setFixedCellWidth(200);
		puzzleImageList.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {

				// Display selected image
				if (!imagePanel.setPuzzleImage((PuzzleImage) puzzleImageList
						.getSelectedValue())) {

					// If selected puzzle image invalid, remove it from the
					// puzzle image list
					listModel.removeElement(puzzleImageList.getSelectedValue());
				}
			}
		});
		puzzleImageList.setSelectedValue(gameInfo.getPuzzleImage(), true);
		JScrollPane imageScrollBar = new JScrollPane(puzzleImageList);
		imageControlPanel.add(imageScrollBar);

		// Create image files chooser
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.addChoosableFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return "Image files";
			}

			@Override
			public boolean accept(File f) {

				// Accept directory and image file
				if (f.isDirectory()) {
					return true;
				}

				String fileName = f.getName().toLowerCase();
				return fileName.endsWith(".bmp") || fileName.endsWith(".gif")
						|| fileName.endsWith(".jpg")
						|| fileName.endsWith(".jpeg")
						|| fileName.endsWith(".jpe")
						|| fileName.endsWith(".jfif")
						|| fileName.endsWith(".png");
			}
		});

		// Create browse button
		browseButton = new JButton("Browse...");
		browseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (fileChooser.showOpenDialog(OptionDialog.this) == JFileChooser.APPROVE_OPTION) {
					PuzzleImage puzzleImage = new PuzzleImage(fileChooser
							.getSelectedFile().getPath(), false);

					// Check puzzle image already exist
					for (Enumeration<?> ins = listModel.elements(); ins
							.hasMoreElements();) {
						if (puzzleImage.equals(ins.nextElement())) {
							puzzleImageList.setSelectedValue(puzzleImage, true);
							return;
						}
					}

					// Add new puzzle image to puzzle image list and save image
					// list to data file
					listModel.addElement(puzzleImage);
					puzzleImageList.setSelectedValue(puzzleImage, true);
					savePuzzleImageList();
				}
			}
		});

		imageControlPanel.add(browseButton);
		panel.add(imageControlPanel, BorderLayout.CENTER);

		return panel;
	}

	/**
	 * Create panel container subdivision text field and operator button
	 * 
	 * @return the subdivision and operator panel
	 */
	private JPanel createSubdivisionAndOperatorPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

		// Create panel container rows and columns text field
		JPanel rowColPanel = new JPanel(new GridLayout(2, 2, 1, 6));
		rowColPanel.add(new JLabel("Rows"));
		rowColPanel.add(rowTextField = new JTextField(Byte.toString(gameInfo
				.getxSplit()), 2));
		rowColPanel.add(new JLabel("Columns"));
		rowColPanel.add(colTextField = new JTextField(Byte.toString(gameInfo
				.getySplit()), 2));
		panel.add(rowColPanel);

		// Create box container cancel and OK button
		Box operatorButtonBox = Box.createHorizontalBox();
		operatorButtonBox.add(Box.createHorizontalStrut(110));

		// Create cancel button
		JButton cancelButton;
		operatorButtonBox.add(cancelButton = new JButton("Cancel"));
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				OptionDialog.this.setVisible(false);
			}
		});
		operatorButtonBox.add(Box.createHorizontalStrut(5));

		// Create OK button
		JButton okButton;
		operatorButtonBox.add(okButton = new JButton("OK"));
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// Validate rows text field value
				if (!GameInfo.isPieceNumber(rowTextField.getText())) {
					JOptionPane.showMessageDialog(OptionDialog.this,
							"Please enter a number from 2 to 10 for rows!");
					rowTextField.requestFocus();
					rowTextField.selectAll();
					return;
				}

				// Validate columns text field value
				if (!GameInfo.isPieceNumber(colTextField.getText())) {
					JOptionPane.showMessageDialog(OptionDialog.this,
							"Please enter a number from 2 to 10 for columns!");
					colTextField.requestFocus();
					colTextField.selectAll();
					return;
				}

				// Validate selected image
				boolean usedImage = imageRadioButton.isSelected();
				if (usedImage && puzzleImageList.isSelectionEmpty()) {
					JOptionPane.showMessageDialog(OptionDialog.this,
							"Please choose image!");
					return;
				}

				// Set option to game information
				gameInfo.setxSplit(Byte.valueOf(rowTextField.getText()));
				gameInfo.setySplit(Byte.valueOf(colTextField.getText()));
				gameInfo.setUsedImage(usedImage);
				gameInfo.setPuzzleImage((PuzzleImage) puzzleImageList
						.getSelectedValue());

				// Save game information to data file
				gameInfo.saveData();

				// Update image panel
				if (usedImage) {
					imagePanel.setPuzzleImage(gameInfo.getPuzzleImage());
				} else {
					imagePanel.setNumbers();
				}

				isOK = true;
				OptionDialog.this.setVisible(false);
			}
		});
		panel.add(operatorButtonBox);

		return panel;
	}

	/**
	 * Save puzzle image list from list box to data file
	 */
	private void savePuzzleImageList() {

		// Create external puzzle image list
		ArrayList<PuzzleImage> imageNameList = new ArrayList<PuzzleImage>();
		for (int i = 0; i < puzzleImageList.getModel().getSize(); i++) {
			PuzzleImage in = (PuzzleImage) puzzleImageList.getModel()
					.getElementAt(i);
			if (!in.isInternalResource()) {
				imageNameList.add(in);
			}
		}

		// Save external puzzle image list to data file
		ObjectOutputStream osw = null;
		try {
			osw = new ObjectOutputStream(new FileOutputStream(
					PUZZLE_DATA_FILENAME));
			osw.writeObject(imageNameList);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (osw != null) {
				try {
					osw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Load puzzle image list from saved data file
	 * 
	 * @return puzzle image list
	 */
	private ArrayList<?> loadPuzzleImageList() {
		ObjectInputStream ots = null;
		try {
			ots = new ObjectInputStream(new FileInputStream(
					PUZZLE_DATA_FILENAME));
			return (ArrayList<?>) ots.readObject();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (ots != null) {
				try {
					ots.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return new ArrayList<PuzzleImage>();
	}

	/**
	 * Image radio button
	 */
	private JRadioButton imageRadioButton;

	/**
	 * Number radio button
	 */
	private JRadioButton numberRadioButton;

	/**
	 * Panel display image or numbers
	 */
	private ImagePanel imagePanel;

	/**
	 * Puzzle image list
	 */
	private JList<PuzzleImage> puzzleImageList;

	/**
	 * Browse button
	 */
	private JButton browseButton;

	/**
	 * Subdivision vertical text field
	 */
	private JTextField rowTextField;

	/**
	 * Subdivision horizontal text field
	 */
	private JTextField colTextField;

	/**
	 * Is user clicked OK button
	 */
	private boolean isOK;

	/**
	 * Game information
	 */
	private GameInfo gameInfo;

	/**
	 * Name of file contain image file name that user added
	 */
	private static final String PUZZLE_DATA_FILENAME = "Puzzle.dat";
	private static final long serialVersionUID = 1L;

	/**
	 * Panel display used image or numbers
	 */
	private class ImagePanel extends JPanel {

		/**
		 * Create image panel
		 * 
		 * @param width
		 *            panel width
		 * @param height
		 *            panel width
		 */
		public ImagePanel(int width, int height) {

			// Create sub panel what display image or numbers
			innerPanel = new JPanel() {
				@Override
				protected void paintComponent(Graphics g) {
					super.paintComponent(g);

					if (pieceObject != null) {
						pieceObject.drawAll(g, 0, 0);
					}
				}

				private static final long serialVersionUID = 1L;
			};

			innerPanel.setPreferredSize(new Dimension(width, height));
			innerPanel.setBackground(Color.BLACK);
			add(innerPanel);
		}

		/**
		 * Set puzzle image to display panel
		 * 
		 * @param puzzleImage
		 *            puzzle image
		 * @return true if no errors, false otherwise
		 */
		public boolean setPuzzleImage(PuzzleImage puzzleImage) {
			boolean success = true;

			if (puzzleImage == null) {
				pieceObject = null;
			} else {
				try {
					pieceObject = new PieceImage(gameInfo.getxSplit(),
							gameInfo.getySplit(), innerPanel.getWidth(),
							innerPanel.getHeight(), puzzleImage);
				} catch (IOException e) {
					success = false;
				}
			}

			innerPanel.repaint();
			return success;
		}

		/**
		 * Set numbers piece to display panel
		 */
		public void setNumbers() {
			pieceObject = new PieceNumber(gameInfo.getxSplit(),
					gameInfo.getySplit(), innerPanel.getWidth(),
					innerPanel.getHeight());

			innerPanel.repaint();
		}

		/**
		 * Draw panel
		 */
		private JPanel innerPanel;

		/**
		 * Piece image or piece number
		 */
		private PieceAbstract pieceObject;
		private static final long serialVersionUID = 1L;
	}
}
