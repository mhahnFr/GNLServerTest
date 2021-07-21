package de.heilbronn42.mhahn;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

/**
 * This class represents the GUI for the server for testing
 * 42cursus/get_next_line. It also has the main starting point.
 * 
 * @author mhahn
 * @since 17.07.21
 */
public class ServerFrame extends JFrame implements ActionListener {

	/**
	 * Displays a status text.
	 */
	private JLabel statusText;

	/**
	 * The start button. When invoked, the server should accept new connections.
	 */
	private JButton start;

	/**
	 * The kill button. When invoked, the server should close all established
	 * connections.
	 */
	private JButton kill;

	/**
	 * The button to send a message to the clients using the server. When invoked, a
	 * message to all clients should be sent.
	 */
	private JButton sendMessage;

	/**
	 * The button to show the settings window.
	 */
	private JButton settings;

	/**
	 * The {@link Preferences} instance for this package.
	 */
	private Preferences prefs;

	/**
	 * The server.
	 */
	private Server server;

	/**
	 * The send action command.
	 */
	private static final String SEND = "send";

	/**
	 * The killing action command.
	 */
	private static final String KILL = "kill";

	/**
	 * The start server action command.
	 */
	private static final String START = "start";

	/**
	 * The show settings pane action command.
	 */
	private static final String SETTINGS = "settings";

	/**
	 * The preferences identifier for the x-coordinate of the windows
	 * position.
	 */
	private static final String W_POS_X = "window-position-x";

	/**
	 * The preferences identifier for the y-coordinate of the windows
	 * position.
	 */
	private static final String W_POS_Y = "window-position-y";

	/**
	 * The preferences identifier for the width of the window.
	 */
	private static final String W_SIZE_W = "window-size-width";

	/**
	 * The preferences identifier for the height of the window.
	 */
	private static final String W_SIZE_H = "window-size-height";

	/**
	 * The preferences identifier for the port number.
	 */
	private static final String PORT_NO = "serv-port-no";

	/**
	 * Constructs a new window with the server controls.
	 */
	public ServerFrame() {
		super();
		setTitle("Server for get_next_line");
		Container contentPane = getContentPane();
		contentPane.setLayout(new GridLayout(5, 1));
		statusText = new JLabel();
		JScrollPane sp = new JScrollPane(statusText);
		contentPane.add(sp);
		start = new JButton("Start server");
		start.setActionCommand(START);
		start.addActionListener(this);
		contentPane.add(start);
		kill = new JButton("Close server");
		kill.setActionCommand(KILL);
		kill.addActionListener(this);
		contentPane.add(kill);
		sendMessage = new JButton("Send message");
		sendMessage.setActionCommand(SEND);
		sendMessage.addActionListener(this);
		contentPane.add(sendMessage);
		settings = new JButton("Settings...");
		settings.setActionCommand(SETTINGS);
		settings.addActionListener(this);
		contentPane.add(settings);
		prefs = Preferences.userNodeForPackage(this.getClass());
		int width = prefs.getInt(W_SIZE_W, -1);
		int height = prefs.getInt(W_SIZE_H, -1);
		if (width == -1 || height == -1)
			pack();
		else
			setSize(width, height);
		int x = prefs.getInt(W_POS_X, -1);
		int y = prefs.getInt(W_POS_Y, -1);
		if (x == -1 || y == -1)
			setLocationRelativeTo(null);
		else
			setLocation(x, y);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (killServer()) {
					prefs.putInt(W_POS_X, getX());
					prefs.putInt(W_POS_Y, getY());
					prefs.putInt(W_SIZE_H, getHeight());
					prefs.putInt(W_SIZE_W, getWidth());
					dispose();
				}
			}
		});
		startServer();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand())
		{
			case START:
				startServer();
				break;

			case KILL:
				killServer();
				break;

			case SEND:
				sendMessage();
				break;

			case SETTINGS:
				showSettings();
				break;

			default:
				throw new IllegalArgumentException("Wrong action command used!");
		}
	}

	/**
	 * Shows a dialog with the settings.
	 */
	private void showSettings() {
		JDialog settingsWindow = new JDialog();
		settingsWindow.setTitle("Settings");
		settingsWindow.setLayout(new GridLayout(4, 1));
		settingsWindow.add(new JLabel("Port number:"));
		JTextField portField = new JTextField();
		settingsWindow.add(portField);
		JButton reset = new JButton("Reset settings");
		reset.addActionListener(event -> {
			if (JOptionPane.showConfirmDialog(
				settingsWindow,
				"Do you really want to reset all settings?",
				"Reset settings",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION) {
				try {
					prefs.clear();
				} catch (BackingStoreException e) {
					JOptionPane.showMessageDialog(settingsWindow,
					"Could not clear settings:\n" + e.getLocalizedMessage(),
					"Error clearing settings",
					JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		settingsWindow.add(reset);
		settingsWindow.add(
			new JLabel("Port change will apply to next started server."));
		settingsWindow.addWindowStateListener(event -> {
			switch (event.getNewState()) {
				case WindowEvent.WINDOW_DEACTIVATED:
					int port = -1;
					try {
						port = Integer.parseInt(portField.getText());
					} catch (NumberFormatException e) {} // I mean, what else should I do here?
					if (port != -1)
						prefs.putInt(PORT_NO, port);
					break;

				case WindowEvent.WINDOW_ACTIVATED:
					portField.setText(Integer.toString(prefs.getInt(PORT_NO, 42)));
					break;
			}
		});
		settingsWindow.pack();
		settingsWindow.setVisible(true);
	}

	/**
	 * Starts the server in its own thread. If it is already running, does nothing.
	 */
	private void startServer() {
		statusText.setText("Starting server...");
		if (server == null) {
			String port = JOptionPane.showInputDialog(
				this,
				"Enter the port to use by the server:",
				"Server port",
				JOptionPane.PLAIN_MESSAGE);
			int p;
			try {
				p = Integer.parseInt(port);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this,
				"No port number given, action canceled!",
				"Port number",
				JOptionPane.ERROR_MESSAGE);
				statusText.setText("Server isn't running");
				return;
			}
			server = new Server(p);
		}
		server.start();
		start.setEnabled(false);
		statusText.setText("Server running");
	}

	/**
	 * Kills the server. Prompts the user if he really wants to do that. If so,
	 * closes all active connections. If no server is running, {@code true} is
	 * returned.
	 * 
	 * @return Returns wether the user wants to close the server.
	 */
	private boolean killServer() {
		statusText.setText("Killing server...");
		if (server.isRunning()) {
			if (JOptionPane.showConfirmDialog(
				this,
				"Do you really want to close all connections and stop the service?",
				"Close server",
				JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION) {
					statusText.setText("Closing server");
					try {
						server.kill();
					} catch (IOException e) {
						JOptionPane.showMessageDialog(this,
						"Could not close server!",
						"Error",
						JOptionPane.ERROR_MESSAGE);
						return false;
					}
					//if (!server.isRunning()) {
						statusText.setText("Server killed.");
						start.setEnabled(true);
						kill.setEnabled(false);
					//}
					return true;
				}
		} else {
			statusText.setText("Server isn't running.");
			return true;
		}
		statusText.setText("Server running");
		return false;
	}

	/**
	 * Sends a message to the clients. Prompts the user to enter the message they
	 * would like to send. If the server is not already running, asks the user if
	 * he wants to start the service.
	 */
	private void sendMessage() {
		if (server.isRunning()) {
			String message = JOptionPane.showInputDialog(
				this, "Enter the message to send:", null);
			if (message == null) {
				message = "";
			}
			message += '\n';
			statusText.setText("Sending message...");
			try {
				server.sendMessage(message);
			} catch (IOException e) {
				JOptionPane.showMessageDialog(
					this,
					"Could not send message:\n" + e.getLocalizedMessage(),
					"Error sending message",
					JOptionPane.ERROR_MESSAGE);
			}
			statusText.setText("Server running");
		} else if (JOptionPane.showConfirmDialog(
			this,
			"Server is not running,\ndo you want to start it now?",
			"Server not running",
			JOptionPane.OK_CANCEL_OPTION,
			JOptionPane.ERROR_MESSAGE) == JOptionPane.OK_OPTION) {
				startServer();
		}
	}

	public static void main(final String[] args) {
		EventQueue.invokeLater(() -> new ServerFrame().setVisible(true));
	}
}