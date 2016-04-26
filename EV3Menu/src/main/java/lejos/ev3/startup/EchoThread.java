package lejos.ev3.startup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class EchoThread extends Thread {
	private BufferedReader in;
	private PrintStream out;
	private PrintStream log;
	private String fileName;
	
	public EchoThread(String fileName, BufferedReader in, PrintStream out) {
		super();
		this.in= in;
		this.out = out;
		this.fileName = fileName;
		// Delete any previous log
		if (fileName != null) {
			new File(fileName).delete();
		}
	}
	
	public void close() {
		try {
			//System.out.println("Closing echo stream");
			if (in != null) in.close();
			if (log != null) log.close();
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
					if (log == null && fileName != null)
						try {
							log = new PrintStream(new FileOutputStream(fileName));
						} catch (FileNotFoundException e) {
							System.err.println("Failed to open log");
						}
					if (log != null) log.println(line);
					out.println(line);
				}
			} catch (IOException e) {
				System.err.println("Echo stream Exception: " + e);
				close();
			}
		}
	}
}

