package de.fhg.iais.roberta.runtime.ev3;

import java.net.URI;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

class ClientWebSocket extends WebSocketClient {

    public ClientWebSocket(URI serverUri) {
        super(serverUri, new Draft_17());
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        // ok
    }

    @Override
    public void onMessage(String message) {
        // ok

    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        // ok
    }

    @Override
    public void onError(Exception e) {
        // ok
    }
}
