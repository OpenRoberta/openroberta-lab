package de.fhg.iais.roberta.javaServer.restServices.robot;

import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.fhg.iais.roberta.robotCommunication.Ev3CommunicationData;
import de.fhg.iais.roberta.robotCommunication.Ev3Communicator;
import de.fhg.iais.roberta.util.AliveData;

@Path("/pushcmd")
public class RobotCommand {
    private static final Logger LOG = LoggerFactory.getLogger(RobotCommand.class);

    private static final int EVERY_REQUEST = 100; // after EVERY_REQUEST many /pushcmd requests have arrived, a log entry is written
    private static final AtomicInteger pushRequestCounterForLogging = new AtomicInteger(0);

    private static final String CMD = "cmd";
    private static final String CMD_REGISTER = "register";
    private static final String CMD_PUSH = "push";
    private static final String CMD_REPEAT = "repeat";
    private static final String CMD_ABORT = "abort";

    private final Ev3Communicator brickCommunicator;
    //    @Context
    //    private HttpServletRequest servletRequest;

    @Inject
    public RobotCommand(Ev3Communicator brickCommunicator) {
        this.brickCommunicator = brickCommunicator;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response handle(JSONObject requestEntity) throws JSONException, InterruptedException {
        AliveData.rememberRobotCall();
        String cmd = requestEntity.getString(CMD);
        String macaddr = null;
        String token = null;
        String brickname = null;
        String batteryvoltage = null;
        String menuversion = null;
        String firmwarename = null;
        String firmwareversion = null;
        int nepoExitValue = 0;
        try {
            macaddr = requestEntity.getString("macaddr");
            token = requestEntity.getString("token");
            brickname = requestEntity.getString("brickname");
            batteryvoltage = requestEntity.getString("battery");
            menuversion = requestEntity.getString("menuversion");
        } catch ( Exception e ) {
            LOG.error("Robot request aborted. Robot uses a wrong JSON: " + requestEntity);
            return Response.serverError().build();
        }
        try {
            firmwarename = requestEntity.getString("firmwarename");
            firmwareversion = requestEntity.getString("firmwareversion");
        } catch ( Exception e ) {
            try {
                // legacy
                firmwareversion = requestEntity.getString("lejosversion");
                firmwarename = "leJOS";
            } catch ( Exception ee ) {
                LOG.error("Robot request aborted. Robot uses a wrong JSON: " + requestEntity);
                return Response.serverError().build();
            }
        }
        try {
            nepoExitValue = requestEntity.getInt("nepoexitvalue");
        } catch ( Exception e ) {
            // no program was executed yet on the robot, field in requestEntity does not exist
            // or the robot system does not support it (nxt)
            nepoExitValue = 0;
        }
        // todo: validate version serverside
        JSONObject response;
        switch ( cmd ) {
            case CMD_REGISTER:
                LOG.info("Robot [" + macaddr + "] token " + token + " received for registration");
                // LOG.info("Robot [" + macaddr + "] token " + token + " received for registration, user-agent: " + this.servletRequest.getHeader("User-Agent"));
                Ev3CommunicationData state = new Ev3CommunicationData(token, macaddr, brickname, batteryvoltage, menuversion, firmwarename, firmwareversion);
                boolean result = this.brickCommunicator.brickWantsTokenToBeApproved(state);
                response = new JSONObject().put("response", result ? "ok" : "error").put("cmd", result ? CMD_REPEAT : CMD_ABORT);
                return Response.ok(response).build();
            case CMD_PUSH:
                int counter = pushRequestCounterForLogging.incrementAndGet();
                boolean logPush = counter % EVERY_REQUEST == 0;
                if ( logPush ) {
                    pushRequestCounterForLogging.set(0);
                    LOG.info("/pushcmd - push request for token " + token + " [count:" + counter + "]");
                }
                String command = this.brickCommunicator.brickWaitsForAServerPush(token, batteryvoltage, nepoExitValue);
                if ( command == null ) {
                    LOG.error("No valid command issued by the server as response to a push command request for token " + token);
                    return Response.serverError().build();
                } else {
                    if ( !command.equals(CMD_REPEAT) || logPush ) {
                        LOG.info("the command " + command + " is pushed to the robot [count:" + counter + "]");
                    }
                    response = new JSONObject().put(CMD, command);
                    return Response.ok(response).build();
                }
            default:
                LOG.error("Robot request aborted. Robot uses a wrong JSON: " + requestEntity);
                return Response.serverError().build();
        }
    }
}
