package de.fhg.iais.roberta.javaServer.restServices.all;

import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.util.AliveData;
import de.fhg.iais.roberta.util.ServerProperties;
import de.fhg.iais.roberta.util.Util;

@Path("/data")
public class ServerData {
    private static final Logger LOG = LoggerFactory.getLogger(ServerData.class);
    private static final int EVERY_REQUEST = 100; // after arrival of EVERY_REQUEST many /alive requests, a log entry is written
    private static final AtomicLong aliveRequestCounterForLogging = new AtomicLong(0);

    private final RobotCommunicator robotCommunicator;
    private final ServerProperties serverProperties;

    @Inject
    public ServerData(RobotCommunicator robotCommunicator, ServerProperties robotProperties) {
        this.robotCommunicator = robotCommunicator;
        this.serverProperties = robotProperties;
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
        String dbSessionData = DbSession.getInfoAboutOpenDbDessions();
        return Response.ok(dbSessionData).build();
    }

    @Path("/robot/whitelist")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response returnRobotWhitelist() throws Exception {
        List<String> robotWhitelist = this.serverProperties.getRobotWhitelist();
        JSONArray jsonArray = new JSONArray();
        for ( String robotName : robotWhitelist ) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", robotName);
            if ( !Objects.equals(robotName, "sim") ) {
                Properties robotProperties = Util.loadProperties("classpath:/" + robotName + ".properties");
                jsonObject.put("realName", robotProperties.getProperty("robot.real.name"));
                jsonObject.put("group", robotProperties.getProperty("robot.plugin.group", robotName));
            }
            jsonArray.put(jsonObject);
        }
        return Response.ok(jsonArray.toString()).build();
    }

    @Path("/robot/xml")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response returnRobotXMLData(@QueryParam("robotName") String robotName) throws Exception {
        JSONObject robotObject = new JSONObject();
        JSONObject xmlObject = new JSONObject();
        JSONObject toolboxObject = new JSONObject();
        JSONObject configurationObject = new JSONObject();

        Properties robotProperties = Util.loadProperties("classpath:/" + robotName + ".properties");
        List<String> robotXml =
            Stream
                .of("robot.program.toolbox.beginner", "robot.program.toolbox.expert", "robot.configuration.toolbox", "robot.configuration.default", "robot.program.default")
                .map(robotProperties::getProperty)
                .map(Util::readResourceContent)
                .collect(Collectors.toList());

        toolboxObject.put("beginner", robotXml.get(0));
        toolboxObject.put("expert", robotXml.get(1));
        configurationObject.put("toolbox", robotXml.get(2));
        configurationObject.put("default", robotXml.get(3));
        xmlObject.put("configuration", configurationObject);
        xmlObject.put("toolbox", toolboxObject);
        xmlObject.put("prog", robotXml.get(4));
        robotObject.put(robotName, xmlObject);

        return Response.ok(robotObject.toString()).build();
    }
}