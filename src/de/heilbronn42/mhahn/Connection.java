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
			close();
		} catch (IOException e) {
		}
	}
}
