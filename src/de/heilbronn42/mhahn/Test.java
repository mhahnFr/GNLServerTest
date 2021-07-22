package de.heilbronn42.mhahn;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

public class Test {
	public static void main(String[] args) throws Exception {
		Socket s = new Socket("localhost", 42);
		System.out.println(
			new BufferedReader(
				new InputStreamReader(s.getInputStream())).readLine());
		s.close();
	}
}