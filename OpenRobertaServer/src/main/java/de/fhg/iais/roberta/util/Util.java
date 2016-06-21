package de.fhg.iais.roberta.util;

import java.util.Date;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.persistence.AbstractProcessor;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.robotCommunication.Ev3CommunicationData;
import de.fhg.iais.roberta.robotCommunication.Ev3Communicator;
import de.fhg.iais.roberta.robotCommunication.Ev3CommunicationData.State;

public class Util {
    private static final Logger LOG = LoggerFactory.getLogger(Util.class);
    private static final String openRobertaVersion = VersionChecker.retrieveVersionOfOpenRobertaServer();

    private Util() {
        // no objects
    }

    public static void addResultInfo(JSONObject response, AbstractProcessor processor) throws JSONException {
        String realKey = processor.getMessage().getKey();
        response.put("rc", processor.getRC());
        response.put("message", realKey);
        response.put("cause", realKey);
        response.put("parameter", processor.getParameter()); // if getParameters returns null, nothing bad happens :-)
    }

    public static JSONObject addSuccessInfo(JSONObject response, Key key) throws JSONException {
        Util.addResultInfo(response, "ok", key);
        return response;
    }

    public static JSONObject addErrorInfo(JSONObject response, Key key) throws JSONException {
        Util.addResultInfo(response, "error", key);
        return response;
    }

    private static void addResultInfo(JSONObject response, String rc, Key key) throws JSONException {
        String realKey = key.getKey();
        response.put("rc", rc);
        response.put("message", realKey);
        response.put("cause", realKey);
    }

    /**
     * add information for the Javascript client to the result json, especially about the state of the robot.<br>
     * This method must be <b>total</b>, i.e. must <b>never</b> throw exceptions.
     *
     * @param response the response object to enrich with data
     * @param httpSessionState needed to access the token
     * @param brickCommunicator needed to access the robot's state
     */
    public static void addFrontendInfo(JSONObject response, HttpSessionState httpSessionState, Ev3Communicator brickCommunicator) {
        try {
            response.put("serverTime", new Date());
            response.put("server.version", Util.openRobertaVersion);
            if ( httpSessionState != null ) {
                String token = httpSessionState.getToken();
                if ( token != null ) {
                    Ev3CommunicationData state = brickCommunicator.getState(token);
                    if ( state != null ) {
                        response.put("robot.wait", state.getRobotConnectionTime());
                        response.put("robot.battery", state.getBattery());
                        response.put("robot.name", state.getRobotName());
                        response.put("robot.version", state.getMenuVersion());
                        response.put("robot.firmwareName", state.getFirmwareName());
                        response.put("robot.sensorvalues", state.getSensorValues());
                        response.put("robot.nepoexitvalue", state.getNepoExitValue());
                        State communicationState = state.getState();
                        String infoAboutState;
                        if ( communicationState == State.BRICK_IS_BUSY ) {
                            infoAboutState = "busy";
                        } else if ( communicationState == State.WAIT_FOR_PUSH_CMD_FROM_BRICK && state.getElapsedMsecOfStartOfLastRequest() > 5000 ) {
                            infoAboutState = "disconnected";
                            brickCommunicator.disconnect(token);
                        } else if ( communicationState == State.BRICK_WAITING_FOR_PUSH_FROM_SERVER ) {
                            infoAboutState = "wait";
                        } else if ( communicationState == State.GARBAGE ) {
                            infoAboutState = "disconnected";
                        } else {
                            infoAboutState = "wait"; // is there a need to distinguish the communication state more detailed?
                        }
                        response.put("robot.state", infoAboutState);
                    }
                }
            }
        } catch ( Exception e ) {
            Util.LOG.error("when adding info for the client, an unexpected exception occurred. Some info for the client may be missing", e);
        }
    }
}
