package de.fhg.iais.roberta.javaServer.restServices.robot;

import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.fhg.iais.roberta.util.dbc.DbcException;
import org.json.JSONException;
import org.json.JSONObject;
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
        AliveData.rememberRobotCall(this.brickCommunicator.getRobotCommunicationDataSize());
        RequestEntityWrapper r = new RequestEntityWrapper(requestEntity);
        String cmd = null;
        String token = null;
        String pluginName = null;
        try {
            cmd = r.get("cmd");
            token = r.get("token");
            pluginName = r.get("pluginname", "firmwarename");
        } catch (Exception e) {
            LOG.error("Robot request aborted. Robot uses a wrong JSON: " + requestEntity, e);
            return Response.serverError().build();
        }

        String robot = r.getWithDefault("?", "robot");
        String robotName = r.getWithDefault("?", "robotname", "brickname");
        String batteryvoltage = r.getWithDefault("?", "battery");
        String menuversion = r.getWithDefault("?", "menuversion");
        String runtimeVersion = r.getWithDefault("?", "runtimeversion");
        String firmwareversion = r.getWithDefault("?", "firmwareversion", "lejosversion");
        int nepoExitValue = requestEntity.optInt("nepoexitvalue", 0);

        JSONObject response;
        switch (cmd) {
            case CMD_REGISTER:
                String macaddr = r.get("macaddr");
                LOG.info("ROBOT_PROTOCOL: robot [" + macaddr + "] send token " + token + " for user approval");
                RobotCommunicationData state =
                        new RobotCommunicationData(token, robot, macaddr, robotName, batteryvoltage, menuversion, runtimeVersion, pluginName, firmwareversion);
                boolean result = this.brickCommunicator.robotWantsTokenToBeApproved(state);
                response = new JSONObject().put("response", result ? "ok" : "error").put("cmd", result ? CMD_REPEAT : CMD_ABORT);
                return Response.ok(response.toString()).build();
            case CMD_PUSH:
                int counter = pushRequestCounterForLogging.incrementAndGet();
                boolean logPush = counter % EVERY_REQUEST == 0;
                if (logPush) {
                    LOG.info("/pushcmd - push request for token " + token + " [count:" + counter + "]");
                }
                String command = this.brickCommunicator.robotWaitsForAServerPush(token, batteryvoltage, nepoExitValue);

                if (command == null || this.brickCommunicator.getState(token) == null) {
                    LOG.error("ROBOT_PROTOCOL: robot was already disconnected, when a /pushcmd for token " + token + " terminated. We return a server error");
                    return Response.serverError().build();
                } else {
                    if (!command.equals(CMD_REPEAT) || logPush) {
                        LOG.info("ROBOT_PROTOCOL: the command " + command + " is pushed to robot with token " + token + " [count: " + counter + "]");
                    }
                    response = new JSONObject().put(CMD, command);
                    response.put(SUBTYPE, this.brickCommunicator.getSubtype());
                    return Response.ok(response.toString()).build();
                }
            default:
                LOG.error("Robot request aborted. Robot uses an invalid \"cmd\" in JSON: " + requestEntity);
                return Response.serverError().build();
        }
    }

    private static class RequestEntityWrapper {
        private final JSONObject json;

        /**
         * wrap a JSONObject to retrieve values for keys presented.
         *
         * @param json to be wrapped
         */
        public RequestEntityWrapper(JSONObject json) {
            this.json = json;
        }

        /**
         * look for the first key from a keys array, that has a value in the JSONObject wrapped. Return this value.<br>
         * if no key is found at all, return the default value
         *
         * @param defaultValue if no keys are present in the JSONObject
         * @param keys         the keys to lookup. The keys can be considered <i>synonyms</i>
         * @return the value of the first key found or the default if no key matched
         */
        public String getWithDefault(String defaultValue, String... keys) {
            for (String key : keys) {
                if (json.has(key)) {
                    return json.getString(key);
                }
            }
            return defaultValue;
        }

        /**
         * look for the first key from a keys array, that has a value in the JSONObject wrapped. Return this value.<br>
         * if no key is found at all, throw an exception
         *
         * @param keys the keys to lookup. The keys can be considered <i>synonyms</i>
         * @return the value of the first key found
         */
        public String get(String... keys) {
            for (String key : keys) {
                if (json.has(key)) {
                    return json.getString(key);
                }
            }
            throw new DbcException("json does not contain required key " + json.toString());
        }
    }
}
