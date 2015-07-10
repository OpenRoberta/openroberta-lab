package lejos.ev3.startup;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ORAserver extends Thread {

    ServerSocket ss = null;

    @Override
    public void run() {
        try {
            this.ss = new ServerSocket(8080);
        } catch ( IOException e ) {
            e.printStackTrace();
        }

        try {
            while ( true ) {
                Socket conn = this.ss.accept();
                System.out.println("New connection from: " + conn.getInetAddress());

                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder builder = new StringBuilder();
                while ( true ) {
                    if ( br.ready() ) {
                        builder.append(br.readLine());
                    } else {
                        break;
                    }
                }
                System.out.println(builder.toString());

                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes("This is the response!");
                dos.close();
                //conn.close();
            }
        } catch ( IOException e ) {
            e.printStackTrace();
        }
    }
}
