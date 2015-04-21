package main;

import java.io.IOException;
import java.net.InetAddress;

import brick.ORApushCmdUSB;

public class Main {

    public static final String brickIp = "10.0.1.1";
    public static final String serverIp = "localhost";
    public static final String serverPort = "1999";

    private static final ORApushCmdUSB orlabUSB = new ORApushCmdUSB(serverIp + ":" + serverPort);

    public static final String TEMPDIRECTORY = System.getenv("USERPROFILE");

    public static void main(String[] args) throws IOException, InterruptedException {

        while ( !InetAddress.getByName(brickIp).isReachable(3000) ) {
            Thread.sleep(1000);
        }
        orlabUSB.mainloop();

    }

    private void disconnect() {
        orlabUSB.interruptMainLoop();
    }

}
