package de.heilbronn42.mhahn;

import java.net.*;
import java.io.*;

public class Connection implements Runnable, Closeable {
	private final Socket socket;
	private BufferedReader reader;

	public Connection(Socket socket) {
		this.socket = socket;
		try {
			this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			this.reader = null;
		}
	}

	public void startInNewThread() { new Thread(this).start(); }

	public void close() {
		try {
			socket.close();
		} catch (java.io.IOException exception) {
		}
	}


	private void print(String s) throws IOException {
		socket.getOutputStream().write(s.getBytes("UTF-8"));
	}

	private void println(String s) throws IOException {
		print(s);
		print("\n");
	}

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
