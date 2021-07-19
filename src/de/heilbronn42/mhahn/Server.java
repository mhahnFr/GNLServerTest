package de.heilbronn42.mhahn;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * The representation of the server. It includes the thread in which it
 * will be running.
 * 
 * @author mhahn
 * @since 19.07.21
 */
public class Server {
	/**
	 * The thread in which the server is running.
	 */
	// TODO Use SwingWorker? instead of Thread.
	public Thread thread;

	/**
	 * The port on which the server is listening.
	 */
	private int port;

	/**
	 * The socket of the server.
	 */
	private ServerSocket socket;

	/**
	 * The runnable containing the server settling up.
	 */
	private Runnable r = () -> {
		try {
			socket = new ServerSocket(port);
			socket.accept();
		} catch (IOException e) {
			// Doesn't matter if the thread crashes.
			throw new RuntimeException(e);
		}
	};

	/**
	 * Creates the server with the given port. It won't run right away,
	 * call {@link start()} to settle it up.
	 * 
	 * @param port The port on which the server should be running.
	 */
	public Server(int port) {
		this.port = port;
		thread = new Thread(r);
	}

	/**
	 * Returns wether the server is running. Running means it should accept
	 * new connections.
	 * 
	 * @return Returns if the server is running or not.
	 */
	public boolean isRunning() {
		return !thread.isInterrupted();
	}

	/**
	 * Starts the server.
	 */
	public void start() {
		if (thread.isInterrupted()) {
			thread = new Thread(r);
		}
		thread.start();
	}

	/**
	 * Kills the server. Should close all connections and stop accepting new ones.
	 */
	public void kill() throws IOException {
		socket.close();
		thread.interrupt();
	}

	/**
	 * Sends a message. Sends the given message to all connected clients.
	 * 
	 * @param message The message to be sent.
	 */
	public void sendMessage(String message) {

	}
}