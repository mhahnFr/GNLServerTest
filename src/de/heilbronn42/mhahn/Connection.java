package de.heilbronn42.mhahn;

import java.net.Socket;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.Closeable;

/**
 * This class is a representation of a connection. It holds a {@link Socket},
 * which has to be given to any new object. 
 * 
 * @author enijakow
 * @since 22.07.21
 */
public class Connection implements Runnable, Closeable {
	/**
	 * The socket whose connection to manage.
	 */
	private final Socket socket;
	/**
	 * The reader for the {@link socket}s {@link InputStream}.
	 */
	private BufferedReader reader;

	/**
	 * Inits the connection. It opens a reader on the {@link InputStream}. Once the
	 * connection is established, the test can be run by calling {@link startInNewThread()}.
	 * 
	 * @param socket The socket whose connection shall be managed.
	 */
	public Connection(Socket socket) {
		this.socket = socket;
		try {
			this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			this.reader = null;
		}
	}

	/**
	 * Starts the test in a new {@link Thread}.
	 */
	public void startInNewThread() { new Thread(this).start(); }

	public void close() {
		try {
			socket.close();
		} catch (IOException exception) {
		}
	}

	/**
	 * Writes the given string to the {@link OutputStream} of the socket.
	 * 
	 * @param s The string to write.
	 * @throws IOException If something goes wrong.
	 */
	private void print(String s) throws IOException {
		socket.getOutputStream().write(s.getBytes("UTF-8"));
		socket.getOutputStream().flush();
	}

	/**
	 * Writes the given string to the {@link OutputStream} of the socket
	 * and appends a newline character.
	 * 
	 * @param s The string to write.
	 * @throws IOException If something goes wrong.
	 */
	private void println(String s) throws IOException {
		print(s);
		print("\n");
	}

	/**
	 * Reads and returns a line from the {@link InputStream}.
	 * 
	 * @throws IOException If something goes wrong.
	 * @return The string read
	 */
	private String readln() throws IOException {
		return reader.readLine();
	}

	public void run() {
		try {
			println("Hello world!");
			println("Sending yet another message!");
			println("socket = new ServerSocket(port);socket.setReuseAddress(true);while (!socket.isClosed()) {bound = socket.accept();Connection c = new Connection(bound);c.startInNewThread();}} catch (IOException e) {// Doesn't matter if the thread crashes.throw new RuntimeException(e);}};/*** Creates the server with the given port. It won't run right away,* call {@link start()} to settle it up.* * @param port The port on which the server should be running.*/public Server(int port) {this.port = port;thread = new Thread(r);}/*** Returns wether the server is running. Running means it should accept* new connections.* * @return Returns if the server is running or not.*/public boolean isRunning() {return !thread.isInterrupted();}/*** Starts the server.*/public void start() {if (thread.isInterrupted()) {thread = new Thread(r);}thread.start();}/*** Kills the server. Should close all connections and stop ac");
			close();
		} catch (IOException e) {
			// As of now, the thread should finish either way.
			throw new RuntimeException(e);
		}
	}
}
