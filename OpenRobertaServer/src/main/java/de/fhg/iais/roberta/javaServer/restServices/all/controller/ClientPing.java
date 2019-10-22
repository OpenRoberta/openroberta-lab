package de.fhg.iais.roberta.javaServer.restServices.all.controller;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.fhg.iais.roberta.javaServer.provider.OraData;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.VersionChecker;
import de.fhg.iais.roberta.util.dbc.DbcKeyException;

@Path("/{version:([^/]+/)?}ping")
public class ClientPing {
    private static final Logger LOG = LoggerFactory.getLogger(ClientPing.class);

    private static final int EVERY_REQUEST = 100; // after arrival of EVERY_PING many ping requests , a log entry is written
    private static final AtomicInteger pingCounterForLogging = new AtomicInteger(0);
    private static final AtomicInteger pingKeyExceptionsSuppressed = new AtomicInteger(0);

    private final String openRobertaServerVersion;
    private final RobotCommunicator brickCommunicator;

    @Inject
    public ClientPing(@Named("openRobertaServer.version") String openRobertaServerVersion, RobotCommunicator brickCommunicator) {
        this.openRobertaServerVersion = openRobertaServerVersion;
        this.brickCommunicator = brickCommunicator;
    }

    /**
     * the ping request is sent from the browser frontend to get information about the robots state (and whether the server is alive).<br>
     * The ping request <i>ignores</i> the init token
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response command(@OraData HttpSessionState httpSessionState, JSONObject fullRequest, @PathParam("version") String version) throws Exception {
        Date date = new Date();
        JSONObject response = new JSONObject().put("version", this.openRobertaServerVersion).put("date", date.getTime()).put("dateAsString", date.toString());
        try {
            MDC.put("sessionId", String.valueOf(httpSessionState.getSessionNumber()));
            MDC.put("userId", String.valueOf(httpSessionState.getUserId()));
            MDC.put("robotName", String.valueOf(httpSessionState.getRobotName()));

            VersionChecker.checkRestVersion(version);
            int counter = pingCounterForLogging.incrementAndGet();
            if ( counter % EVERY_REQUEST == 0 ) {
                LOG.info("/ping [count:" + counter + "]");
            }
        } catch ( DbcKeyException e ) {
            int counter = pingKeyExceptionsSuppressed.incrementAndGet();
            if ( counter % EVERY_REQUEST == 0 ) {
                LOG.info("suppressed now " + counter + " DbcKeyExceptions. Last message was: " + e.getMessage());
            }
            Util.addErrorInfo(response, Key.INIT_FAIL_PING_ERROR);
        } catch ( Exception e ) {
            LOG.info("suppressed exception is: " + e.getMessage());
            Util.addErrorInfo(response, Key.INIT_FAIL_PING_ERROR);
        }
        return Util.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
    }
}