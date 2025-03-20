/**
 * Puzzle game using Java AWT
 */
package hoaftq.puzzle;

import hoaftq.puzzle.utility.WindowUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.Desktop.Action;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Puzzle about dialog
 */
public class AboutDialog extends JDialog {
    private static final String HOME_PAGE = "https://github.com/hoaftq";

    /**
     * Create about dialog
     *
     * @param owner owner of the dialog
     */
    public AboutDialog(JFrame owner) {
        super(owner, "About Puzzle", true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        createLayout();

        WindowUtil.centerOwner(this);
        setResizable(false);
    }

    private void createLayout() {

        // Create puzzle label
        var titleLabel = new JLabel("""
                <html>
                  <center><font size=+2 color=red>Game Puzzle</font></center>
                </html>""");
        var titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // Create home page panel
        var infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBorder(new EmptyBorder(0, 7, 0, 5));
        infoPanel.add(new JLabel("Home page:"));
        infoPanel.add(createHomePageLabel());
        add(infoPanel, BorderLayout.CENTER);

        // Create a panel for OK button
        var buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        buttonPanel.add(createOKButton());
        add(buttonPanel, BorderLayout.SOUTH);

        pack();
    }

    private static JLabel createHomePageLabel() {
        final var hoverContent = "<html><a href='" + HOME_PAGE + "'>"
                                 + "<i>" + HOME_PAGE + "</i></a></html>";
        final var normalContent = "<html><a href='" + HOME_PAGE + "'>"
                                  + HOME_PAGE + "</a></html>";

        var label = new JLabel(normalContent);
        label.setBorder(new EmptyBorder(0, 0, 0, 2));
        label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Make entered/exited animation and open home page link on clicking
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                var desktop = Desktop.getDesktop();
                if (desktop.isSupported(Action.BROWSE)) {
                    try {
                        // Open my home page with default browser
                        desktop.browse(new URI(HOME_PAGE));
                    } catch (IOException | URISyntaxException ex) {
                        ex.printStackTrace();
                    }
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                ((JLabel) e.getSource()).setText(hoverContent);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                ((JLabel) e.getSource()).setText(normalContent);
            }
        });

        return label;
    }

    private JButton createOKButton() {
        var okButton = new JButton("OK");
        okButton.addActionListener(e -> {

            // Destroy about dialog
            setVisible(false);
            dispose();
        });

        return okButton;
    }
}
