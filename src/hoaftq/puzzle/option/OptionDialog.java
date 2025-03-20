/**
 * Puzzle game using Java AWT
 */
package hoaftq.puzzle.option;

import hoaftq.puzzle.common.PuzzleImage;
import hoaftq.puzzle.game.EmptyTilePosition;
import hoaftq.puzzle.utility.WindowUtil;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Objects;

/**
 * Option game dialog
 */
public class OptionDialog extends JDialog {
    private final ImageListStorage imageListStorage;
    private final GameOptionStorage gameOptionStorage;
    private final GameOptionValidator validator;

    private GameOption gameOption;

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

    public GameOption getGameOption() {
        return gameOption;
    }

    /**
     * Create option dialog
     *
     * @param owner      owner of dialog
     * @param gameOption game information
     */
    public OptionDialog(JFrame owner,
                        ImageListStorage imageListStorage,
                        GameOptionStorage gameOptionStorage,
                        GameOptionValidator validator,
                        GameOption gameOption) {
        super(owner, "Customize Game", true);
        this.imageListStorage = imageListStorage;
        this.gameOptionStorage = gameOptionStorage;
        this.validator = validator;
        this.gameOption = gameOption;

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

        showImageOrNumbers(gameOption.usedImage(), gameOption.puzzleImage());
    }

    /**
     * Create a panel with image and number radio button
     */
    private JPanel createImageNumbersOptionPanel() {
        var panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        var optionButtonGroup = new ButtonGroup();

        imageRadioButton = createImageNumberRadioButton(
                optionButtonGroup, "Use image", gameOption.usedImage());
        panel.add(imageRadioButton);

        var numberRadioButton = createImageNumberRadioButton(
                optionButtonGroup, "Use number", !gameOption.usedImage());
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
            var selectedImage = Objects.requireNonNullElse(
                    puzzleImageList.getSelectedValue(),
                    puzzleImageList.getModel().getElementAt(0));
            showImageOrNumbers(isImageMode, selectedImage);
        });


        buttonGroup.add(radioButton);
        return radioButton;
    }

    private void showImageOrNumbers(boolean isImageUsed, PuzzleImage puzzleImage) {
        puzzleImageList.setEnabled(isImageUsed);
        browseButton.setEnabled(isImageUsed);
        if (isImageUsed) {
            imageNumbersPanel.displayImage(puzzleImage);
        } else {
            imageNumbersPanel.displayNumbers();
        }
    }

    /**
     * Create panel used to display image/numbers and browse image
     */
    private JPanel createImageNumbersPanel() {
        var panel = new JPanel(new BorderLayout());

        // Create panel display image
        imageNumbersPanel = new ImageNumbersPanel(gameOption.row(), gameOption.column(), 320, 200);
        panel.add(imageNumbersPanel, BorderLayout.NORTH);

        // Create panel for browsing images
        var imageControlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        // Create puzzle image list box
        puzzleImageList = createImageListBox();
        imageControlPanel.add(new JScrollPane(puzzleImageList));

        imageControlPanel.add(browseButton = createBrowseButton());

        panel.add(imageControlPanel, BorderLayout.CENTER);
        return panel;
    }

    private JList<PuzzleImage> createImageListBox() {
        DefaultListModel<PuzzleImage> listModel = new DefaultListModel<>();
        listModel.addElement(new PuzzleImage("default.jpg", true));
        listModel.addAll(imageListStorage.loadPuzzleImages());
        var puzzleImageList = new JList<>(listModel);

        puzzleImageList.setFixedCellWidth(200);
        puzzleImageList.setSelectedValue(gameOption.puzzleImage(), true);
        puzzleImageList.addListSelectionListener(e -> {
            if (!imageNumbersPanel.displayImage(puzzleImageList.getSelectedValue())) {
                listModel.removeElement(puzzleImageList.getSelectedValue());
            }
        });

        return puzzleImageList;
    }

    private JButton createBrowseButton() {
        var fileChooser = createFileChooser();

        var browseButton = new JButton("Browse...");
        browseButton.addActionListener(e -> {
            if (fileChooser.showOpenDialog(OptionDialog.this) != JFileChooser.APPROVE_OPTION) {
                return;
            }

            var listModel = (DefaultListModel<PuzzleImage>) puzzleImageList.getModel();
            var puzzleImage = new PuzzleImage(fileChooser.getSelectedFile().getPath(), false);

            // Check puzzle image already exists in the list
            if (listModel.contains(puzzleImage)) {
                puzzleImageList.setSelectedValue(puzzleImage, true);
                return;
            }

            // Add new puzzle image to puzzle image list and select it
            listModel.addElement(puzzleImage);
            puzzleImageList.setSelectedValue(puzzleImage, true);

            saveImageList(listModel);
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

    private void saveImageList(DefaultListModel<PuzzleImage> listModel) {
        try {
            var imageList = Collections.list(listModel.elements())
                    .stream()
                    .filter(i -> !i.isInternalResource())
                    .toList();
            imageListStorage.savePuzzleImages(imageList);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error occurred while saving the images.",
                    "Puzzle",
                    JOptionPane.ERROR_MESSAGE);
        }
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
            if (!validateInputs()) {
                return;
            }

            gameOption = new GameOption(
                    imageRadioButton.isSelected(),
                    puzzleImageList.getSelectedValue(),
                    Byte.parseByte(rowTextField.getText()),
                    Byte.parseByte(colTextField.getText()),
                    EmptyTilePosition.BOTTOM_RIGHT);

            // Save game information to data file
            gameOptionStorage.save(gameOption);

            isOK = true;
            OptionDialog.this.setVisible(false);
        });

        return okButton;
    }

    private boolean validateInputs() {
        if (!validator.validateRowOrColumn(rowTextField.getText())) {
            JOptionPane.showMessageDialog(OptionDialog.this,
                    "Please enter a number from 2 to 10.",
                    "Puzzle",
                    JOptionPane.INFORMATION_MESSAGE);
            rowTextField.requestFocus();
            rowTextField.selectAll();
            return false;
        }

        if (!validator.validateRowOrColumn(colTextField.getText())) {
            JOptionPane.showMessageDialog(OptionDialog.this,
                    "Please enter a number from 2 to 10.",
                    "Puzzle",
                    JOptionPane.INFORMATION_MESSAGE);
            colTextField.requestFocus();
            colTextField.selectAll();
            return false;
        }

        // Validate selected image
        if (imageRadioButton.isSelected() && puzzleImageList.isSelectionEmpty()) {
            JOptionPane.showMessageDialog(OptionDialog.this,
                    "Please select an image.",
                    "Puzzle",
                    JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * Create panel container rows and columns text field
     */
    private JPanel createRowColumnPanel() {
        var panel = new JPanel(new GridLayout(2, 2, 1, 6));
        panel.add(new JLabel("Rows"));
        panel.add(rowTextField = new JTextField(Byte.toString(gameOption.row()), 2));
        panel.add(new JLabel("Columns"));
        panel.add(colTextField = new JTextField(Byte.toString(gameOption.column()), 2));
        return panel;
    }
}
