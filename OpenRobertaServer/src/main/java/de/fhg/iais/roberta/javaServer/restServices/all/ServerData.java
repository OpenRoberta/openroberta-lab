package de.fhg.iais.roberta.javaServer.restServices.all;

import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.util.AliveData;

@Path("/data")
public class ServerData {
    private static final Logger LOG = LoggerFactory.getLogger(ServerData.class);
    private static final int EVERY_REQUEST = 100; // after arrival of EVERY_REQUEST many /alive requests, a log entry is written
    private static final AtomicLong aliveRequestCounterForLogging = new AtomicLong(0);

    private final RobotCommunicator robotCommunicator;

    @Inject
    public ServerData(RobotCommunicator robotCommunicator) {
        this.robotCommunicator = robotCommunicator;
    }

    @Path("/server/alive")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response tellTheState() throws Exception {
        JSONObject answer = AliveData.getAndUpdateAliveState();
        long counter = aliveRequestCounterForLogging.incrementAndGet();
        boolean logAlive = counter % EVERY_REQUEST == 0;
        if ( logAlive ) {
            LOG.info("the response to the the " + EVERY_REQUEST + ". /alive request is: " + answer.toString());
        }
        return Response.ok(answer.toString()).build();
    }

    @Path("/robot/summary")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response returnSummary(@QueryParam("log") String log) throws Exception {
        String summary = this.robotCommunicator.getSummaryOfRobotCommunicator();
        if ( log != null && log.equalsIgnoreCase("true") ) {
            LOG.info(summary);
        }
        return Response.ok(summary).build();
    }

    @Path("/robot/detail")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response returnDetail() throws Exception {
        String detail = this.robotCommunicator.getDetailsOfRobotConnections();
        if ( detail.equals("") ) {
            LOG.info("no robots connected");
        } else {
            LOG.info("details of the robots connected:\n" + detail);
        }
        return Response.ok("done and written to the log file").build();
    }

    @Path("/server/resources")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response tellUsageOfHttpAndDatabaseSessions() throws Exception {
        JSONObject answer = new JSONObject();
        answer.put("dbSessions", DbSession.getOpenSessionCounter());
        answer.put("unusedDbSessions", DbSession.getUnusedSessionCounter());
        answer.put("cleanedDbSessions", DbSession.getCleanedSessionCounter());
        answer.put("httpSessions", HttpSessionState.getNumberOfHttpSessionStates());
        return Response.ok(answer.toString()).build();
    }

    @Path("/server/dbsessions")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response tellUsageOfDatabaseSessions() throws Exception {
        String dbSessionData = DbSession.getFullInfo();
        return Response.ok(dbSessionData).build();
    }
}