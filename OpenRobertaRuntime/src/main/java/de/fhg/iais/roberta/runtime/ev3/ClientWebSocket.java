package de.fhg.iais.roberta.runtime.ev3;

import java.net.URI;

import org.java_websocket.WebSocket.READYSTATE;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;

class ClientWebSocket extends WebSocketClient {

    private final String token;

    public ClientWebSocket(URI serverUri, String token) {
        super(serverUri, new Draft_17());
        this.token = token;
    }

    /**
     * Call in a separate thread for sending sensor values to Open Roberta Lab via websocket.
     * The thread should be interrupted from the NEPO program to stop logging.
     */
    public void logSensorValues() {
        while ( !Thread.currentThread().isInterrupted() ) {
            try {
                switch ( this.getReadyState() ) {
                    case NOT_YET_CONNECTED:
                        this.connectBlocking();
                        break;
                    case CONNECTING:
                        // do nothing
                        break;
                    case OPEN:
                        this.sendSensorValue();
                        Thread.sleep(900);
                        break;
                    case CLOSING:
                        // do nothing
                        break;
                    case CLOSED:
                        this.connectBlocking();
                        break;
                    default:
                        break;
                }
                Thread.sleep(100);
            } catch ( InterruptedException ie ) {
                // ok
            } catch ( Exception e ) {
                // connection is not necessarily stable.
                // Attempt to reconnect in the next iteration if connection is lost.
            }
        }
    };

    /**
     * Write sensor values to the websocket.
     */
    private void sendSensorValue() {
        JSONObject sensorvalues = new JSONObject();
        sensorvalues.put("token", this.token);
        sensorvalues.put("Ultrasonic_S1", "50");
        this.send(sensorvalues.toString());
    }

    /**
     * Send a message to the server which block is currently active and add token to it.
     *
     * @param uniqueBlockID
     */
    public void sendActiveBlockInfo(String uniqueBlockID) {
        if ( this.getReadyState() == READYSTATE.OPEN ) {
            JSONObject debugMsg = new JSONObject();
            debugMsg.put("token", this.token);
            debugMsg.put("activeblock", uniqueBlockID);
            this.send(debugMsg.toString());
        }
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Receive: " + message);

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Disconnected " + code + " " + reason);
    }

    @Override
    public void onError(Exception e) {
        System.out.println(e.getMessage());
    }
}
