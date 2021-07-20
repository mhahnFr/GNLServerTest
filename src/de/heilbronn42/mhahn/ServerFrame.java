package de.heilbronn42.mhahn;

import java.awt.Container;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

/**
 * This class represents the GUI for the server for testing
 * 42cursus/get_next_line. It also has the main starting point.
 * 
 * unistd.h
 * sys/socket.h
 * 		socket()
 * 		bind() || connect()
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
	 * Constructs a new window with the server controls.
	 */
	public ServerFrame() {
		super();
		setTitle("Server for get_next_line");
		Container contentPane = getContentPane();
		contentPane.setLayout(new GridLayout(4, 1));
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
		// TODO Save window size & position
		pack();
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (killServer())
					dispose();
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

			default:
				throw new IllegalArgumentException("Wrong action command used!");
		}
	}

	/**
	 * Starts the server in its own thread. If it is already running, does nothing.
	 */
	private void startServer() {
		statusText.setText("Starting server...");
		if (server == null) {
			String port = JOptionPane.showInputDialog(this, "Enter the port to use by the server:", "Server port", JOptionPane.PLAIN_MESSAGE);
			int p;
			try {
				p = Integer.parseInt(port);
			} catch (NumberFormatException e) {
				JOptionPane.showMessageDialog(this, "No port number given, action canceled!", "Port number", JOptionPane.ERROR_MESSAGE);
				statusText.setText("Server isn't running");
				return;
			}
			server = new Server(p);
		}
		System.out.println(server.thread.getState());
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
				"Close server", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.OK_OPTION) {
					statusText.setText("Closing server");
					try {
						server.kill();
					} catch (IOException e) {
						JOptionPane.showMessageDialog(this, "Could not close server!", "Error", JOptionPane.ERROR_MESSAGE);
						// Endless loop on error?
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
			String message = JOptionPane.showInputDialog(this, "Enter the message to send:", null);
			if (message == null) {
				message = "";
			}
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