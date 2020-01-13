package de.fhg.iais.roberta.util;

import java.util.Date;

import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import de.fhg.iais.roberta.javaServer.restServices.all.controller.ClientAdmin;
import de.fhg.iais.roberta.javaServer.restServices.all.controller.ClientInit;
import de.fhg.iais.roberta.persistence.AbstractProcessor;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicationData;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicationData.State;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.dbc.DbcKeyException;

public class UtilForREST {
    private static final Logger LOG = LoggerFactory.getLogger(UtilForREST.class);
    private static String serverVersion;

    private UtilForREST() {
        // no objects
    }

    /**
     * TODO: remove this global setting. Injected from the <b>ServerStarter</b> to add version info to all frontend JSON objects. But not nice ... .
     *
     * @param serverVersion the version to set.
     */
    public static void setServerVersion(String serverVersion) {
        UtilForREST.serverVersion = serverVersion;
    }

    /**
     * all REST services, excluded is only the /init and the /ping request, have to call this method. It processes the init-token, which protects user and
     * server against<br>
     * - multiple frontend sessions connected to one backend session (see class {@link ClientInit})<br>
     * - a frontend session not backed by a backend session (occurs when the server is restarted)<br>
     *
     * @param httpSessionState
     * @param loggerForRequest
     * @param fullRequest
     * @return
     */
    public static HttpSessionState handleRequestInit(Logger loggerForRequest, JSONObject fullRequest) {
        AliveData.rememberClientCall();
        String initToken = fullRequest.optString("initToken");
        HttpSessionState httpSessionState = validateInitToken(initToken);
        MDC.put("sessionId", String.valueOf(httpSessionState.getSessionNumber()));
        MDC.put("userId", String.valueOf(httpSessionState.getUserId()));
        MDC.put("robotName", String.valueOf(httpSessionState.getRobotName()));
        new ClientLogger().log(loggerForRequest, fullRequest);
        return httpSessionState;
    }

    /**
     * validate the init-token from the frontend-request and the init-token from the state stored in this object.<br>
     * If an error is detected a {@linkplain DbcKeyException} is thrown.<br>
     * <i>Only for debugging:</i> if the init-token in this object is set to "", all checks are disabled. This should <i>NEVER</i> happen, when a real server is
     * started
     *
     * @param checkInitToken true: consistency checks; false: avoid them (used by ping services)
     * @param initToken the token from the frontend-request, retrieved from the server when the connection front-end to server was established
     * @return a HttpSessionState object matching the initToken
     */
    private static HttpSessionState validateInitToken(String initToken) {
        if ( initToken == null ) {
            String errorMsgIfError = "frontend request has no initToken";
            LOG.error(errorMsgIfError);
            throw new DbcKeyException(errorMsgIfError, Key.INIT_FAIL_INVALID_INIT_TOKEN, null);
        } else {
            HttpSessionState httpSessionState = HttpSessionState.initToken2HttpSessionstate.get(initToken);
            if ( httpSessionState == null ) {
                String errorMsgIfError = "initToken is not initialized in the session";
                LOG.error(errorMsgIfError);
                throw new DbcKeyException(errorMsgIfError, Key.INIT_FAIL_INVALID_INIT_TOKEN, null);
            } else {
                httpSessionState.rememberFrontendAccessTime();
                return httpSessionState;
            }
        }
    }

    public static JSONObject extractDataPart(JSONObject request) {
        JSONObject dataPart;
        try {
            dataPart = request.getJSONObject("data");
        } catch ( JSONException e1 ) {
            throw new DbcException("Invalid JSON object: data not found", e1);
        }
        return dataPart;
    }

    public static void addResultInfo(JSONObject response, AbstractProcessor processor) throws JSONException {
        String realKey = processor.getMessage().getKey();
        String responseCode = processor.succeeded() ? "ok" : "error";
        response.put("rc", responseCode);
        response.put("message", realKey);
        response.put("cause", realKey);
        response.put("parameters", processor.getParameters());
    }

    public static JSONObject addSuccessInfo(JSONObject response, Key key) throws JSONException {
        UtilForREST.addResultInfo(response, "ok", key);
        return response;
    }

    public static JSONObject addErrorInfo(JSONObject response, Key key) throws JSONException {
        UtilForREST.addResultInfo(response, "error", key);
        return response;
    }

    public static void addErrorInfo(JSONObject response, Key key, String compilerResponse) throws JSONException {
        UtilForREST.addResultInfo(response, "error", key);
        JSONObject parameters = new JSONObject();
        parameters.put("MESSAGE", compilerResponse);
        response.put("parameters", parameters);
    }

    /**
     * generate a response with frontend info (see {@link #addFrontendInfo})
     *
     * @param response
     * @param httpSessionState
     * @param brickCommunicator
     * @return
     */
    public static Response responseWithFrontendInfo(JSONObject response, HttpSessionState httpSessionState, RobotCommunicator brickCommunicator) {
        UtilForREST.addFrontendInfo(response, httpSessionState, brickCommunicator);
        MDC.clear();
        return Response.ok(response).build();
    }

    /**
     * add information for the Javascript client to the result json, especially about the state of the robot.<br>
     * This method must be <b>total</b>, i.e. must <b>never</b> throw exceptions.
     *
     * @param response the response object to enrich with data
     * @param httpSessionState needed to access the token
     * @param brickCommunicator needed to access the robot's state
     */
    public static void addFrontendInfo(JSONObject response, HttpSessionState httpSessionState, RobotCommunicator brickCommunicator) {
        try {
            response.put("serverTime", new Date());
            response.put("server.version", UtilForREST.serverVersion);
            if ( httpSessionState != null ) {
                String token = httpSessionState.getToken();
                if ( token != null ) {
                    if ( token.equals(ClientAdmin.NO_CONNECT) ) {
                        response.put("robot.state", "wait");
                    } else if ( brickCommunicator != null ) {
                        RobotCommunicationData state = brickCommunicator.getState(token);
                        if ( state != null ) {
                            response.put("robot.wait", state.getElapsedMsecOfStartApproval());
                            response.put("robot.battery", state.getBattery());
                            response.put("robot.name", state.getRobotName());
                            response.put("robot.version", state.getMenuVersion());
                            response.put("robot.firmwareName", state.getFirmwareName());
                            response.put("robot.sensorvalues", state.getSensorValues());
                            response.put("robot.nepoexitvalue", state.getNepoExitValue());
                            State communicationState = state.getState();
                            String infoAboutState;
                            if ( httpSessionState.isProcessing() || communicationState == State.ROBOT_IS_BUSY ) {
                                infoAboutState = "busy";
                            } else if ( state.isRobotProbablyDisconnected() || communicationState == State.GARBAGE ) {
                                infoAboutState = "disconnected";
                            } else {
                                infoAboutState = "wait"; // is there a need to distinguish the communication state more detailed?
                            }
                            response.put("robot.state", infoAboutState);
                        }
                    }
                }
                response.put("initToken", httpSessionState.getInitToken());
            }
        } catch ( Exception e ) {
            UtilForREST.LOG.error("when adding info for the client, an unexpected exception occurred. Some info for the client may be missing", e);
        }
    }

    private static void addResultInfo(JSONObject response, String restCallResultOkOrError, Key key) throws JSONException {
        String realKey = key.getKey();
        response.put("rc", restCallResultOkOrError);
        response.put("message", realKey);
        response.put("cause", realKey);
    }
}
