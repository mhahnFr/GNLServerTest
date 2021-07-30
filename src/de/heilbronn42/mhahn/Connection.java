package de.heilbronn42.mhahn;

import java.net.Socket;
import java.util.Random;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;

/**
 * This class is a representation of a connection. It holds a {@link Socket},
 * which has to be given to any new object. 
 * 
 * @author enijakow
 * @since 22.07.21
 */
public class Connection implements Runnable, Closeable {
	/**
	 * The path to the test files.
	 */
	private static final String PATH = "testData/";
	/**
	 * The name of the test files. It will be appended by {@link TEST_COUNT}
	 * + {@code .txt}.
	 */
	private static final String FILE_NAME = "test";
	/**
	 * The total number of available tests. Counting starts at one (1).
	 */
	private static final int TEST_COUNT = 9;
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

	@Override
	public void close() throws IOException {
		socket.close();
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
	 * Writes the end of file to the stream.
	 * 
	 * @throws IOException In case that something goes wrong.
	 * @author mhahn
	 */
	private void printEOF() throws IOException {
		socket.getOutputStream().write('\0');
		socket.getOutputStream().flush();
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

	@Override
	public void run() {
		try {
			test();
			close();
		} catch (IOException e) {
			// As of now, the thread should finish either way.
			throw new RuntimeException(e);
		}
	}

	/**
	 * Sends all the test files using the established connection.
	 * 
	 * @throws IOException If something goes wrong.
	 * @author mhahn
	 */
	private void test() throws IOException {
		for (int i = 1; i <= TEST_COUNT; i++) {
			test(new File(PATH + FILE_NAME + Integer.toString(i) + ".txt").getAbsoluteFile());
		}
	}

	/**
	 * Sends the given file using the established connection. Adds randomly a delay.
	 * 
	 * @param f The file to be sent line by line.
	 * @throws IOException If something goes wrong.
	 * @author mhahn
	 */
	private void test(File f) throws IOException {
		Random random = new Random();
		BufferedReader reader = new BufferedReader(new FileReader(f));
		while (reader.ready()) {
			if (random.nextBoolean()) {
				try {
					Thread.sleep(random.nextInt(500));
				} catch (InterruptedException e) {
					// Doesn't matter, just continue without delay.
				}
			}
			println(reader.readLine());
		}
		print("\n");
		reader.close();
	}
}
