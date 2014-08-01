package lejos.ev3.startup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.TextLCD;

public class RConsole extends Thread
{
    static final int MODE_SWITCH = 0xff;
    static final int MODE_LCD = 0x0;
    
    static final int RCONSOLE_PORT = 8001;    
    ServerSocket ss = null;
    Socket conn = null;
    PrintStream origOut = System.out, origErr = System.err;
    
    private TextLCD lcd = LocalEV3.get().getTextLCD();
    
	public RConsole()
	{
		super();
        setDaemon(true);
	}
	
	public boolean isConnected() {
		return (conn != null && conn.isConnected());
	}

    /**
     * Main console I/O thread.
     */
    @Override
    public void run()
    {      
    	// Create a server socket
    	try {
			ss = new ServerSocket(RCONSOLE_PORT);
			System.out.println("Server socket created");
		} catch (IOException e1) {
			e1.printStackTrace();
			return;
		}
    	
    	// Loop accepting remote console connections
    	while (true) {
            try {
            	System.out.println("Waiting for a connection");
        		conn = ss.accept();
        		conn.setSoTimeout(2000);
        		SynchronizedOutputStream os = new SynchronizedOutputStream(conn.getOutputStream());
        		BufferedReader input = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        		System.setOut(new PrintStream(os));
        		System.setErr(new PrintStream(os));
        		System.out.println("Output redirected");
                
                // Loop waiting for commands
                while (true)
                {
                    try {
                    	String line = input.readLine(); 
                    	if (line == null) break;
                    } catch (SocketTimeoutException e) {
                        os.writeLCD(lcd.getHWDisplay());
                    }                
                }
                os.close();
                input.close();
                conn.close();
                System.setOut(origOut);
                System.setErr(origErr);
                System.out.println("System output set back to original");
            }
            catch(IOException e)
            {
            	System.err.println("Error accepting connection " + e);
            }
    	}
    }
}
