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

import com.google.inject.Injector;

import de.fhg.iais.roberta.robotCommunication.RobotCommunicationData;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;

@WebSocket
public class Ev3SensorLoggingWS {
    private static final Logger LOG = LoggerFactory.getLogger(Ev3SensorLoggingWS.class);
    private static Injector guiceInjector = null;

    public static void setGuiceInjector(Injector guiceInjector) {
        if ( Ev3SensorLoggingWS.guiceInjector == null ) {
            Ev3SensorLoggingWS.guiceInjector = guiceInjector;
        } else {
            LOG.error("Guice injector for websockets is set twice, this call is simply ignored");
        }
    }

    @OnWebSocketConnect
    public void handleConnect(Session session) throws Exception {
        LOG.info("@OnWebSocketConnect");
    }

    @OnWebSocketClose
    public void handleClose(int statusCode, String reason) {
        LOG.info("@OnWebSocketClose with statusCode: " + statusCode + " and reason: " + reason);
    }

    @OnWebSocketMessage
    public void handleMessage(JSONObject request) {
        String token = (String) request.remove("token");
        LOG.info("@OnWebSocketMessage: " + token + " " + request);
        RobotCommunicator communicator = guiceInjector.getInstance(RobotCommunicator.class);
        RobotCommunicationData state = communicator.getState(token);
        state.setSensorValues(request);
    }

    @OnWebSocketError
    public void handleError(Throwable e) {
        LOG.error("@OnWebSocketError occurred with attached throwable", e);
    }
}