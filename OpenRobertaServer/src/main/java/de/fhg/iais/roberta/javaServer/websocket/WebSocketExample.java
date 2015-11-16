package de.fhg.iais.roberta.javaServer.websocket;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.json.JSONObject;
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
        JSONObject request = new JSONObject(requestString);
        String token = (String) request.remove("token");
        LOG.info("@OnWebSocketMessage: " + token + " " + request);
        // TODO
        // @Reinhard sensor logging example
        // @OnWebSocketMessage: 969F09TE {"S3-color-COLOUR":"NONE","S3-color-RED":0,"C-big motorDEGREE":534,"S4-touch":false,"S3-color-RGB":[0,0,0],"S1-ultrasonicDISTANCE":36,"S1-ultrasonicPRESENCE":false,"S2-gyroRATE":-7,"B-big motorDEGREE":536,"S2-gyroANGLE":0,"S3-color-AMBIENTLIGHT":1}
    }

    @OnWebSocketError
    public void handleError(Throwable e) {
        LOG.error("@OnWebSocketError occured with attached throwable", e);
    }
}