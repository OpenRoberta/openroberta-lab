package de.fhg.iais.roberta.codegen.cebithack;

import lejos.hardware.Bluetooth;
import lejos.remote.nxt.NXTCommConnector;
import lejos.remote.nxt.NXTConnection;

/**
 * Implementation for the basic {@link BluetoothCom} that establishes a connection and can be used to transfer data.
 *
 * @author admin
 */
public class BluetoothComImpl implements BluetoothCom {

    @Override
    public NXTConnection establishConnectionTo(String host, int timeOut) {
        NXTCommConnector connector = Bluetooth.getNXTCommConnector();
        NXTConnection con = null;
        long start = System.currentTimeMillis();
        while ( (con = connector.connect(host, NXTConnection.RAW)) == null && (System.currentTimeMillis() - start < timeOut * 1000) ) {
            try {
                Thread.sleep(100);
            } catch ( InterruptedException e ) {
                e.printStackTrace();
            }
        }
        if ( con == null ) {
            System.err.println("Couldn't connect in given time");
        }
        return con;
    }

    @Override
    public NXTConnection waitForConnection(int timeOut) {
        NXTCommConnector connector = Bluetooth.getNXTCommConnector();
        NXTConnection con = connector.waitForConnection(timeOut * 1000, NXTConnection.RAW);
        return con;
    }

    @Override
    public String readMessage(NXTConnection connection) {
        if ( connection == null ) {
            return "NO CONNECTION";
        }

        byte[] buffer = new byte[128];
        connection.read(buffer, 128);
        return new String(buffer).trim();
    }

    @Override
    public void sendTo(NXTConnection connection, String message) {
        if ( connection == null ) {
            System.err.println("NO CONNECTION");
            return;
        }

        connection.write(message.getBytes(), message.getBytes().length);
    }
}
