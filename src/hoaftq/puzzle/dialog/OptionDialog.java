/**
 * Puzzle game using Java AWT
 * Copyright @ 2011 Trac Quang Hoa
 */
package hoaftq.puzzle.dialog;

import hoaftq.puzzle.entity.*;
import hoaftq.puzzle.utility.WindowUtil;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;

/**
 * Option game dialog
 */
public class OptionDialog extends JDialog {
    private final OptionStorage optionStorage;
    private final GameInfoValidator validator;

    private final GameInfoStorage gameInfoStorage;
    private GameInfo gameInfo;

    private JRadioButton imageRadioButton;

    /**
     * Panel displaying image or numbers
     */
    private ImageNumbersPanel imageNumbersPanel;

    /**
     * Available images list
     */
    private JList<PuzzleImage> puzzleImageList;

    private JButton browseButton;

    private JTextField rowTextField;

    private JTextField colTextField;

    /**
     * Is user clicked OK button
     */
    private boolean isOK;

    public GameInfo getGameInfo() {
        return gameInfo;
    }

    /**
     * Create option dialog
     *
     * @param owner    owner of dialog
     * @param gameInfo game information
     */
    public OptionDialog(JFrame owner, OptionStorage optionStorage, GameInfoValidator validator, GameInfoStorage gameInfoStorage, GameInfo gameInfo) {
        super(owner, "Customize Game", true);
        this.optionStorage = optionStorage;
        this.validator = validator;
        this.gameInfoStorage = gameInfoStorage;
        this.gameInfo = gameInfo;

        createLayout();
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

    private void createLayout() {
        add(createImageNumbersOptionPanel(), BorderLayout.NORTH);
        add(createImageNumbersPanel(), BorderLayout.CENTER);
        add(createRowColumnAndOperatorsPanel(), BorderLayout.SOUTH);
        pack();

        setResizable(false);

        showImageOrNumbers(gameInfo.usedImage(), gameInfo.puzzleImage());
    }

    /**
     * Create a panel with image and number radio button
     */
    private JPanel createImageNumbersOptionPanel() {
        var panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        var optionButtonGroup = new ButtonGroup();

        imageRadioButton = createImageNumberRadioButton(
                optionButtonGroup, "Use image", gameInfo.usedImage());
        panel.add(imageRadioButton);

        var numberRadioButton = createImageNumberRadioButton(
                optionButtonGroup, "Use number", !gameInfo.usedImage());
        panel.add(numberRadioButton);

        return panel;
    }

    /**
     * Create use image or use number radio button
     *
     * @param text       text to display
     * @param isSelected whether selected or not
     */
    private JRadioButton createImageNumberRadioButton(ButtonGroup buttonGroup, String text, boolean isSelected) {
        var radioButton = new JRadioButton(text, isSelected);
        radioButton.addActionListener(e -> {
            var isImageMode = e.getSource() == imageRadioButton;
            showImageOrNumbers(isImageMode, puzzleImageList.getSelectedValue());
        });

        buttonGroup.add(radioButton);
        return radioButton;
    }

    private void showImageOrNumbers(boolean isImageUsed, PuzzleImage puzzleImage) {
        puzzleImageList.setEnabled(isImageUsed);
        browseButton.setEnabled(isImageUsed);
        imageNumbersPanel.setDisplayingObject(isImageUsed ? puzzleImage : null);
    }

    /**
     * Create panel used to display image/numbers and browse image
     */
    private JPanel createImageNumbersPanel() {
        var panel = new JPanel(new BorderLayout());

        // Create panel display image
        imageNumbersPanel = new ImageNumbersPanel(gameInfo, 320, 200);
        panel.add(imageNumbersPanel, BorderLayout.NORTH);

        // Create panel browse image
        var imageControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Create puzzle image list box
        DefaultListModel<PuzzleImage> listModel = new DefaultListModel<>();
        listModel.addElement(new PuzzleImage("default.jpg", true));
        listModel.addAll(optionStorage.loadPuzzleImages());

        puzzleImageList = new JList<>(listModel);
        puzzleImageList.setFixedCellWidth(200);
        puzzleImageList.setSelectedValue(gameInfo.puzzleImage(), true);
        puzzleImageList.addListSelectionListener(e -> {
            if (!imageNumbersPanel.setDisplayingObject(puzzleImageList.getSelectedValue())) {
                listModel.removeElement(puzzleImageList.getSelectedValue());
            }
        });

        imageControlPanel.add(new JScrollPane(puzzleImageList));
        imageControlPanel.add(createBrowseButton(listModel));

        panel.add(imageControlPanel, BorderLayout.CENTER);
        return panel;
    }

    private JButton createBrowseButton(DefaultListModel<PuzzleImage> listModel) {
        var fileChooser = createFileChooser();

        browseButton = new JButton("Browse...");
        browseButton.addActionListener(e -> {
            if (fileChooser.showOpenDialog(OptionDialog.this) == JFileChooser.APPROVE_OPTION) {
                var puzzleImage = new PuzzleImage(fileChooser.getSelectedFile().getPath(), false);

                // Check puzzle image already exists in the list
                if (listModel.contains(puzzleImage)) {
                    puzzleImageList.setSelectedValue(puzzleImage, true);
                    return;
                }

                // Add new puzzle image to puzzle image list and select it
                listModel.addElement(puzzleImage);
                puzzleImageList.setSelectedValue(puzzleImage, true);

//                // Save image list to data file
//                try {
//                    optionStorage.savePuzzleImages(Collections.list(listModel.elements()).stream().filter(i -> !i.isInternalResource()).toList());
//                } catch (IOException ex) {
//                    throw new RuntimeException(ex);
//                }
            }
        });

        return browseButton;
    }

    private static JFileChooser createFileChooser() {
        var fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new FileFilter() {
            final static java.util.List<String> acceptFileExtensions
                    = java.util.List.of(".bmp", ".gif", ".jpg", ".jpeg", ".jpe", ".jfif", ".png");

            @Override
            public String getDescription() {
                return "Image files";
            }

            @Override
            public boolean accept(File f) {

                // Accept directory
                if (f.isDirectory()) {
                    return true;
                }

                var fileName = f.getName().toLowerCase();
                return acceptFileExtensions.stream().anyMatch(fileName::endsWith);
            }
        });

        return fileChooser;
    }

    /**
     * Create a panel that contains Row, Column inputs & OK, Cancel buttons
     */
    private JPanel createRowColumnAndOperatorsPanel() {
        var panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        var rowColPanel = createRowColumnPanel();
        panel.add(rowColPanel);

        // Create a box containing cancel and OK button
        var operatorButtonsBox = Box.createHorizontalBox();
        operatorButtonsBox.add(Box.createHorizontalStrut(110));

        var cancelButton = createCancelButton();
        operatorButtonsBox.add(cancelButton);
        operatorButtonsBox.add(Box.createHorizontalStrut(5));

        var okButton = createOKButton();
        operatorButtonsBox.add(okButton);

        panel.add(operatorButtonsBox);
        return panel;
    }

    private JButton createCancelButton() {
        var cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> OptionDialog.this.setVisible(false));
        return cancelButton;
    }

    private JButton createOKButton() {
        var okButton = new JButton("OK");
        okButton.addActionListener(e -> {
            if (!validator.validateRowOrColumn(rowTextField.getText())) {
                JOptionPane.showMessageDialog(OptionDialog.this,
                        "Please enter a number from 2 to 10.");
                rowTextField.requestFocus();
                rowTextField.selectAll();
                return;
            }

            if (!validator.validateRowOrColumn(colTextField.getText())) {
                JOptionPane.showMessageDialog(OptionDialog.this,
                        "Please enter a number from 2 to 10.");
                colTextField.requestFocus();
                colTextField.selectAll();
                return;
            }

            // Validate selected image
            boolean usedImage = imageRadioButton.isSelected();
            if (usedImage && puzzleImageList.isSelectionEmpty()) {
                JOptionPane.showMessageDialog(OptionDialog.this, "Please select an image.");
                return;
            }

            gameInfo = new GameInfo(usedImage, puzzleImageList.getSelectedValue(), Byte.parseByte(rowTextField.getText()),
                    Byte.parseByte(colTextField.getText()), EmptyTilePosition.BOTTOM_RIGHT);

            // Save game information to data file
            gameInfoStorage.save(gameInfo);

            imageNumbersPanel.setDisplayingObject(usedImage ? gameInfo.puzzleImage() : null);

            isOK = true;
            OptionDialog.this.setVisible(false);
        });

        return okButton;
    }

    /**
     * Create panel container rows and columns text field
     */
    private JPanel createRowColumnPanel() {
        var panel = new JPanel(new GridLayout(2, 2, 1, 6));
        panel.add(new JLabel("Rows (2-10)"));
        panel.add(rowTextField = new JTextField(Byte.toString(gameInfo.row()), 2));
        panel.add(new JLabel("Columns (2-10)"));
        panel.add(colTextField = new JTextField(Byte.toString(gameInfo.column()), 2));
        return panel;
    }
}
