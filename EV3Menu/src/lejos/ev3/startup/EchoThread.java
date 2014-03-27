package lejos.ev3.startup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

public class EchoThread extends Thread {
	BufferedReader in;
	PrintStream out;
	
	public EchoThread(BufferedReader in, PrintStream out) {
		super();
		this.in= in;
		this.out = out;
	}
	
	public void close() {
		try {
			System.out.println("Closing echo stream");
			if (in != null) in.close();
		} catch (IOException e) {
			System.err.println("Close of echo stream failed");
		}
		in = null;
	}
	
	@Override
	public void run()
	{
		while(in != null) {
			try {
				String line = in.readLine();
				if (line == null) {
					close();
					break;
				} else {
					out.println(line);
				}
			} catch (IOException e) {
				System.err.println("Echo stream Exception: " + e);
				close();
			}
		}
	}
}

