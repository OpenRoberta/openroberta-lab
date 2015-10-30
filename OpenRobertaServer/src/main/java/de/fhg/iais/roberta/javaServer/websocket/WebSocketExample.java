package de.fhg.iais.roberta.javaServer.websocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebSocket
public class WebSocketExample {
    private static final Logger LOG = LoggerFactory.getLogger(WebSocketExample.class);

    @OnWebSocketConnect
    public void handleConnect(Session session) throws Exception {
        LOG.info("@OnWebSocketConnect");
    }

    @OnWebSocketClose
    public void handleClose(int statusCode, String reason) {
        LOG.info("@OnWebSocketClose with statusCode: " + statusCode + " and reason: " + reason);
    }

    @OnWebSocketMessage
    public void handleMessage(String requestString) throws Exception {
        LOG.info("@OnWebSocketMessage: " + requestString);
    }

    @OnWebSocketError
    public void handleError(Throwable e) {
        LOG.error("@OnWebSocketError occured with attached throwable", e);
    }
}