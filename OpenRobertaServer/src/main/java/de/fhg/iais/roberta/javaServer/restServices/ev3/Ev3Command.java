package de.fhg.iais.roberta.javaServer.restServices.ev3;

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

import de.fhg.iais.roberta.robotCommunication.ev3.Ev3CommunicationData;
import de.fhg.iais.roberta.robotCommunication.ev3.Ev3Communicator;
import de.fhg.iais.roberta.util.AliveData;

@Path("/pushcmd")
public class Ev3Command {
    private static final Logger LOG = LoggerFactory.getLogger(Ev3Command.class);

    private static final int EVERY_REQUEST = 1; // after EVERY_REQUEST many /pushcmd requests have arrived, a log entry is written
    private static final AtomicInteger pushRequestCounterForLogging = new AtomicInteger(0);

    private static final String CMD = "cmd";
    private static final String CMD_REGISTER = "register";
    private static final String CMD_PUSH = "push";
    private static final String CMD_REPEAT = "repeat";
    private static final String CMD_ABORT = "abort";

    private final Ev3Communicator brickCommunicator;

    @Inject
    public Ev3Command(Ev3Communicator brickCommunicator) {
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
        String lejosversion = null;
        try {
            macaddr = requestEntity.getString("macaddr");
            token = requestEntity.getString("token");
            brickname = requestEntity.getString("brickname");
            batteryvoltage = requestEntity.getString("battery");
            menuversion = requestEntity.getString("menuversion");
            lejosversion = requestEntity.getString("lejosversion");
        } catch ( Exception e ) {
            LOG.error("Robot request aborted. Robot uses a wrong JSON: " + requestEntity);
            return Response.serverError().build();
        }
        // todo: validate version serverside
        JSONObject response;
        switch ( cmd ) {
            case CMD_REGISTER:
                LOG.info("pushcmd - brick sends token " + token + " for registration");
                Ev3CommunicationData state = new Ev3CommunicationData(token, macaddr, brickname, batteryvoltage, menuversion, lejosversion);
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
                String command = this.brickCommunicator.brickWaitsForAServerPush(token, batteryvoltage);
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
