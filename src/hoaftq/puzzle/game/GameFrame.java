/**
 * Puzzle game using Java AWT
 */
package hoaftq.puzzle.game;

import hoaftq.puzzle.AboutDialog;
import hoaftq.puzzle.info.GameInfoView;
import hoaftq.puzzle.option.*;
import hoaftq.puzzle.utility.WindowUtil;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.*;
import java.io.IOException;

public class GameFrame extends JFrame {
    private static final int DEFAULT_WIDTH = 500;
    private static final int DEFAULT_HEIGHT = 500;

    private GamePanel gamePanel;
    private GameOption gameOption;

    public GameFrame(GameOption gameOption) {
        this.gameOption = gameOption;
        initializeFrame();
        createGamePanel();

        gamePanel.newGame(gameOption);

        // Update game panel's size when frame resized
        setUpGamePanelResize();

        // Focus game panel when frame has focus
        setUpFocusGamePanel();
    }

    private void initializeFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Puzzle");
        setWindowLookAndFeel();

        setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        WindowUtil.centerOwner(this);

        addMenu();
    }

    private void createGamePanel() {
        try {
            gamePanel = new GamePanel(new GameInfoView());
        } catch (IOException e) {
            // TODO different approach?
            JOptionPane.showMessageDialog(null,
                    "An error occurred while loading game.", "Puzzle",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        add(gamePanel);
    }

    private void setUpGamePanelResize() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                var inserts = getInsets();
                gamePanel.setGameBoardSize(
                        getWidth() - (inserts.left + inserts.right),
                        getHeight() - (inserts.top + inserts.bottom + getJMenuBar().getHeight()));
            }
        });
    }

    private void setUpFocusGamePanel() {
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
        var menuBar = new JMenuBar();
        menuBar.add(createGameMenu());
        menuBar.add(createAboutMenu());
        setJMenuBar(menuBar);
    }

    private JMenu createGameMenu() {
        var gameMenu = new JMenu("Game");
        gameMenu.setMnemonic('G');

        createNewGameMenuItem(gameMenu);
        gameMenu.add(createCustomizeGameMenuItem());

        gameMenu.addSeparator();

        createExitMenuItem(gameMenu);

        return gameMenu;
    }

    private void createNewGameMenuItem(JMenu gameMenu) {
        var newGameMenuItem = gameMenu.add("New Game");
        newGameMenuItem.setMnemonic('N');
        newGameMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK));
        newGameMenuItem.addActionListener(e -> gamePanel.newGame(gameOption));
    }

    private JMenuItem createCustomizeGameMenuItem() {
        var customizeGameMenuItem = new JMenuItem("Customize Game", 'C');
        customizeGameMenuItem.addActionListener(e -> {
            var gameInfoValidator = new GameOptionValidator();
            var imageListStorage = new ImageListStorage();
            var gameInfoStorage = new GameOptionStorage(gameInfoValidator);
            var optionDialog = new OptionDialog(
                    GameFrame.this,
                    imageListStorage,
                    gameInfoStorage,
                    gameInfoValidator,
                    gameOption);

            if (optionDialog.showDialog()) {
                gameOption = optionDialog.getGameOption();

                // new game with game information get from option dialog
                gamePanel.newGame(gameOption);
            }
        });

        return customizeGameMenuItem;
    }

    private static void createExitMenuItem(JMenu gameMenu) {
        var exitAction = new AbstractAction("Exit") {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };
        exitAction.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_E);
        gameMenu.add(exitAction);
    }

    private JMenu createAboutMenu() {
        var aboutMenu = new JMenu("About");
        aboutMenu.setMnemonic('A');
        aboutMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                var aboutDialog = new AboutDialog(GameFrame.this);
                aboutDialog.setVisible(true);
            }

            @Override
            public void menuDeselected(MenuEvent e) {
            }

            @Override
            public void menuCanceled(MenuEvent e) {
            }
        });

        return aboutMenu;
    }

    private void setWindowLookAndFeel() {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}