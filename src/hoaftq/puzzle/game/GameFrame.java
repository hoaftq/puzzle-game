/**
 * Puzzle game using Java AWT
 * Copyright @ 2011 Trac Quang Hoa
 */
package hoaftq.puzzle.game;

import hoaftq.puzzle.AboutDialog;
import hoaftq.puzzle.info.GameInfoView;
import hoaftq.puzzle.option.OptionDialog;
import hoaftq.puzzle.option.OptionStorage;
import hoaftq.puzzle.option.GameOption;
import hoaftq.puzzle.option.GameOptionStorage;
import hoaftq.puzzle.option.GameOptionValidator;
import hoaftq.puzzle.utility.WindowUtil;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

/**
 * Game frame
 */
public class GameFrame extends JFrame {

    private final GameOptionStorage gameOptionStorage;

    /**
     * Create game frame
     */
    public GameFrame(GameOptionStorage gameOptionStorage) {
        this.gameOptionStorage = gameOptionStorage;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Puzzle");
        setWindowLookAndFeel();

        addMenu();

        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        WindowUtil.centerOwner(this);

        // Create game panel with game information get from properties file
        try {
            gamePanel = new GamePanel(gameOption = gameOptionStorage.get(), new GameInfoView());
            add(gamePanel);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "An error occurred while loading game", "Puzzle",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        // Update game panel when frame resize
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Insets inserts = getInsets();
                gamePanel.setGameBoardSize(getWidth()
                                           - (inserts.left + inserts.right), getHeight()
                                                                             - (inserts.top + inserts.bottom + getJMenuBar()
                        .getHeight()));
            }
        });

        // Set focus for game panel when frame has focus
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                gamePanel.requestFocus();
            }
        });
    }

    /**
     * Add menu to game frame
     */
    private void addMenu() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Add game menu
        JMenu gameMenu = new JMenu("Game");
        gameMenu.setMnemonic('G');
        menuBar.add(gameMenu);

        // Add new game menu item
        JMenuItem newGameMenuItem = gameMenu.add("New Game");
        newGameMenuItem.setMnemonic('N');
        newGameMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
                InputEvent.CTRL_MASK));
        newGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gamePanel.newGame(gameOption);
            }
        });

        // Add customize game menu item
        JMenuItem customizeGameMenuItem = new JMenuItem("Customize Game", 'C');
        gameMenu.add(customizeGameMenuItem);
        customizeGameMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (optionDialog == null) {
                    var optionStorage = new OptionStorage();
                    var gameInfoValidator = new GameOptionValidator();
                    var gameInfoStorage = new GameOptionStorage(gameInfoValidator);
                    optionDialog = new OptionDialog(GameFrame.this, optionStorage, gameInfoValidator, gameInfoStorage, gameOption);
                }

                if (optionDialog.showDialog()) {
                    gameOption = optionDialog.getGameInfo();

                    // new game with game information get from option dialog
                    gamePanel.newGame(gameOption);
                }
            }
        });

        gameMenu.addSeparator();

        // Add exit menu item
        AbstractAction exitAction;
        gameMenu.add(exitAction = new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }

            private static final long serialVersionUID = 1L;
        });
        exitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);

        // Add about menu
        JMenu aboutMenu = new JMenu("About");
        aboutMenu.setMnemonic('A');
        aboutMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                AboutDialog aboutDialog = new AboutDialog(GameFrame.this);
                aboutDialog.setVisible(true);
            }

            @Override
            public void menuDeselected(MenuEvent e) {
            }

            @Override
            public void menuCanceled(MenuEvent e) {
            }
        });
        menuBar.add(aboutMenu);
    }

    /**
     * Set window look and feel for the application
     */
    private void setWindowLookAndFeel() {
        try {
            UIManager
                    .setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Game panel where display game board
     */
    private GamePanel gamePanel;

    /**
     * Game information
     */
    private GameOption gameOption;

    /**
     * Option dialog
     */
    private OptionDialog optionDialog;

    /**
     * Default width of game frame
     */
    private static final int DEFAULT_WIDTH = 500;

    /**
     * Default height of game frame
     */
    private static final int DEFAULT_HEIGHT = 500;
    private static final long serialVersionUID = 1L;
}