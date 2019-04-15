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

import de.fhg.iais.roberta.robotCommunication.RobotCommunicationData;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
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
    private static final String SUBTYPE = "subtype";

    private final RobotCommunicator brickCommunicator;

    @Inject
    public RobotCommand(RobotCommunicator brickCommunicator) {
        this.brickCommunicator = brickCommunicator;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response handle(JSONObject requestEntity) throws JSONException, InterruptedException {
        AliveData.rememberRobotCall();
        String cmd = requestEntity.getString(CMD);
        String token = null;
        String firmwarename = null;
        try {
            token = requestEntity.getString("token");
            firmwarename = requestEntity.getString("firmwarename");
        } catch ( Exception e ) {
            LOG.error("Robot request aborted. Robot uses a wrong JSON: " + requestEntity, e);
            return Response.serverError().build();
        }
        // TODO: move robot to the requested properties for the next version
        String robot = requestEntity.optString("robot", "ev3");
        String macaddr = requestEntity.optString("macaddr", "usb");
        String brickname = requestEntity.optString("brickname", robot);
        String batteryvoltage = requestEntity.optString("battery", "");
        String menuversion = requestEntity.optString("menuversion", "");
        String runtimeVersion = requestEntity.optString("runtimeversion", "");
        String firmwareversion = requestEntity.optString("firmwareversion");
        firmwareversion = firmwareversion == null ? requestEntity.optString("lejosversion", "") : firmwareversion;
        int nepoExitValue = requestEntity.optInt("nepoexitvalue", 0);
        // TODO: validate version here!
        JSONObject response;
        switch ( cmd ) {
            case CMD_REGISTER:
                LOG.info("Robot [" + macaddr + "] token " + token + " received for registration");
                // LOG.info("Robot [" + macaddr + "] token " + token + " received for registration, user-agent: " + this.servletRequest.getHeader("User-Agent"));
                RobotCommunicationData state =
                    new RobotCommunicationData(token, robot, macaddr, brickname, batteryvoltage, menuversion, runtimeVersion, firmwarename, firmwareversion);
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

                if ( command == null || this.brickCommunicator.getState(token) == null ) {
                    LOG.error("No valid command issued by the server as response to a push command request for token " + token);
                    return Response.serverError().build();
                } else {
                    if ( !command.equals(CMD_REPEAT) || logPush ) {
                        LOG.info("the command " + command + " is pushed to the robot [count:" + counter + "]");
                    }
                    response = new JSONObject().put(CMD, command);
                    response.put(SUBTYPE, this.brickCommunicator.getSubtype());
                    return Response.ok(response).build();
                }
            default:
                LOG.error("Robot request aborted. Robot uses an invalid \"cmd\" in JSON: " + requestEntity);
                return Response.serverError().build();
        }
    }
}
