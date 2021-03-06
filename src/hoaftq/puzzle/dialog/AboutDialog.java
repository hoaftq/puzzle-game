/**
 * Puzzle game using Java AWT
 * Copyright @ 2011 Trac Quang Hoa
 */
package hoaftq.puzzle.dialog;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import hoaftq.puzzle.utility.WindowUtil;

/**
 * Puzzle about dialog
 */
public class AboutDialog extends JDialog {

	/**
	 * Create about dialog
	 * 
	 * @param owner
	 *            owner of the dialog
	 */
	public AboutDialog(JFrame owner) {
		super(owner, "About Puzzle", true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

		// Create puzzle information label
		JLabel infoLabel = new JLabel(
				"<html><table>"
						+ "<tr><td colspan=2 align=center><font size=6 color=Red>Game Puzzle 1.0</font></td></tr>"
						+ "<tr><td>Write by:</td><td>Trac Quang Hoa</td></tr>"
						+ "<tr><td></td><td>Cam Binh, Cam Xuyen, Ha Tinh</td></tr>"
						+ "<tr><td>Date:</td><td>09-10-2011</td></tr>"
						+ "</table></html>");
		JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		infoPanel.add(infoLabel);
		add(infoPanel, BorderLayout.NORTH);

		final String enteredVisitLabel = "<html><table><tr><a href='"
				+ THBT_WEB_ADDRESS + "'><i>" + THBT_WEB_ADDRESS
				+ "</i></a></tr></table></html>";
		final String exitedVisitLabel = "<html><table><tr><a href='"
				+ THBT_WEB_ADDRESS + "'>" + THBT_WEB_ADDRESS
				+ "</a></tr></table></html>";

		// Create my home page panel
		JPanel visitPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		visitPanel.add(new JLabel(
				"<html><table><tr><td>Visit:</td></tr></table></html>"));

		JLabel visitLabel = new JLabel(exitedVisitLabel);
		visitLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		// Make entered/exited animate and process my home page link clicked
		visitLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				// Open my home page with default browser
				Desktop desktop = Desktop.getDesktop();
				if (desktop.isSupported(Action.BROWSE)) {
					try {
						desktop.browse(new URI(THBT_WEB_ADDRESS));
					} catch (IOException ex) {
						ex.printStackTrace();
					} catch (URISyntaxException ex) {
						ex.printStackTrace();
					}
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				((JLabel) e.getSource()).setText(enteredVisitLabel);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				((JLabel) e.getSource()).setText(exitedVisitLabel);
			}
		});
		visitPanel.add(visitLabel);
		add(visitPanel, BorderLayout.CENTER);

		// Create panel container OK button
		JPanel buttonPanel = new JPanel(
				new FlowLayout(FlowLayout.CENTER, 0, 15));
		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// Destroy about dialog
				setVisible(false);
				dispose();
			}
		});
		buttonPanel.add(okButton);
		add(buttonPanel, BorderLayout.SOUTH);

		pack();
		WindowUtil.centerOwner(this);
		setResizable(false);
	}

	/**
	 * My home page address
	 */
	private static final String THBT_WEB_ADDRESS = "http://thbt.webng.com";
	private static final long serialVersionUID = 1L;
}
