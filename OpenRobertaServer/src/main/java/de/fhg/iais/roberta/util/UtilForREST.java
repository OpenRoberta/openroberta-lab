package de.fhg.iais.roberta.util;

import java.io.InputStream;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentHashMap.KeySetView;
import java.util.concurrent.atomic.AtomicLong;

import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.google.inject.Inject;

import de.fhg.iais.roberta.generated.restEntities.BaseResponse;
import de.fhg.iais.roberta.generated.restEntities.FullRestRequest;
import de.fhg.iais.roberta.javaServer.restServices.all.controller.ClientAdmin;
import de.fhg.iais.roberta.persistence.AbstractProcessor;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicationData;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicationData.State;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.util.dbc.DbcKeyException;

public class UtilForREST {
    private static final Logger LOG = LoggerFactory.getLogger(UtilForREST.class);
    private static final long INVALID_INIT_TOKEN_REPORT_LIMIT = 100;
    private static String serverVersion = "undefined";
    private static final AtomicLong invalidInitTokenCounter = new AtomicLong(0);
    private static final KeySetView<String, Boolean> invalidTokenThatAccess = ConcurrentHashMap.newKeySet();

    private static NotificationService notificationService;

    @Inject
    public static void setNotificationService(NotificationService notificationService) {
        UtilForREST.notificationService = notificationService;
    }

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
     * all REST services, excluded is only the /init request, have to call this method. It processes the init-token, which protects user and server against a
     * frontend session not backed up by a backend session (occurs only when the server is restarted)<br>
     *
     * @param loggerForRequest
     * @param fullRequest
     * @param rememberTheCall if true, count the request as a real call (a login, e.g.); otherwise don't increase the call counter (a ping, e.g.)
     * @return
     */
    public static HttpSessionState handleRequestInit(Logger loggerForRequest, FullRestRequest fullRequest, boolean rememberTheCall) {
        if ( rememberTheCall ) {
            AliveData.rememberClientCall();
        }
        String initToken = fullRequest.getInitToken();
        HttpSessionState httpSessionState = validateInitToken(initToken);
        MDC.put("sessionId", String.valueOf(httpSessionState.getSessionNumber()));
        MDC.put("userId", String.valueOf(httpSessionState.getUserId()));
        MDC.put("robotName", String.valueOf(httpSessionState.getRobotName()));
        new ClientLogger().log(loggerForRequest, fullRequest.getLog());
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
            HttpSessionState httpSessionState = HttpSessionState.get(initToken);
            if ( httpSessionState == null ) {
                long invalidCounter = invalidInitTokenCounter.incrementAndGet();
                if ( invalidCounter > INVALID_INIT_TOKEN_REPORT_LIMIT ) {
                    invalidTokenThatAccess.add(initToken);
                    LOG.info("got " + INVALID_INIT_TOKEN_REPORT_LIMIT + " many invalid init calls with tokens " + invalidTokenThatAccess.toString());
                    invalidInitTokenCounter.set(0);
                    invalidTokenThatAccess.clear();
                }
                throw new DbcKeyException("invalid init token", Key.INIT_FAIL_INVALID_INIT_TOKEN, null);
            }
            return httpSessionState;
        }
    }

    public static <T extends BaseResponse> void addResultInfo(T response, AbstractProcessor processor) throws JSONException {
        String realKey = processor.getMessage().getKey();
        String responseCode = processor.succeeded() ? "ok" : "error";
        response.setRc(responseCode);
        response.setMessage(realKey);
        response.setCause(realKey);
        response.setParameters(processor.getParameters());
    }

    public static <T extends BaseResponse> T addSuccessInfo(T response, Key key) throws JSONException {
        UtilForREST.addResultInfo(response, "ok", key);
        return response;
    }

    public static Response makeBaseResponseForError(Key key, HttpSessionState httpSessionState, RobotCommunicator brickCommunicator) throws JSONException {
        BaseResponse errorResponse = BaseResponse.make();
        UtilForREST.addResultInfo(errorResponse, "error", key);
        if ( key == Key.INIT_FAIL_PING_ERROR ) {
            errorResponse.setInitToken("invalid-token");
        }
        UtilForREST.addFrontendInfo(errorResponse, httpSessionState, brickCommunicator);
        return UtilForREST.responseWithFrontendInfo(errorResponse, httpSessionState, brickCommunicator);
    }

    public static <T extends BaseResponse> T addErrorInfo(T response, Key key) throws JSONException {
        UtilForREST.addResultInfo(response, "error", key);
        return response;
    }

    public static <T extends BaseResponse> void addErrorInfo(T response, Key key, String compilerResponse) throws JSONException {
        UtilForREST.addResultInfo(response, "error", key);
        JSONObject parameters = new JSONObject();
        parameters.put("MESSAGE", compilerResponse);
        response.setParameters(parameters);
    }

    /**
     * generate a response with frontend info (see {@link #addFrontendInfo})
     *
     * @param response
     * @param httpSessionState
     * @param brickCommunicator
     * @return
     */
    public static <T extends BaseResponse> Response responseWithFrontendInfo(T response, HttpSessionState httpSessionState, RobotCommunicator brickCommunicator) //
    {
        UtilForREST.addFrontendInfo(response, httpSessionState, brickCommunicator);
        MDC.clear();
        return Response.ok(response.immutable().toJson().toString()).build();
    }

    /**
     * add information for the Javascript client to the result json, especially about the state of the robot.<br>
     * This method must be <b>total</b>, i.e. must <b>never</b> throw exceptions.
     *
     * @param response the response object to enrich with data
     * @param httpSessionState needed to access the token
     * @param brickCommunicator needed to access the robot's state
     */
    public static <T extends BaseResponse> void addFrontendInfo(T response, HttpSessionState httpSessionState, RobotCommunicator brickCommunicator) {
        try {
            response.setServerTime(new Date().getTime());
            response.setServerVersion(UtilForREST.serverVersion);
            if ( httpSessionState != null ) {

                if ( notificationService != null ) {
                    boolean newNotifications = !notificationService.getCurrentDigest().equals(httpSessionState.getReceivedNotificationsDigest());
                    response.setNotificationsAvailable(newNotifications);
                } else {
                    response.setNotificationsAvailable(false);
                }
                String token = httpSessionState.getToken();
                if ( token != null ) {
                    if ( token.equals(ClientAdmin.NO_CONNECT) ) {
                        response.setRobotState("wait");
                    } else if ( brickCommunicator != null ) {
                        RobotCommunicationData state = brickCommunicator.getState(token);
                        if ( state != null ) {
                            response.setRobotWait(state.getElapsedMsecOfStartApproval());
                            response.setRobotBattery(state.getBattery());
                            response.setRobotName(state.getRobotName());
                            response.setRobotVersion(state.getMenuVersion());
                            response.setRobotFirmwareName(state.getFirmwareName());
                            response.setRobotSensorvalues(state.getSensorValues());
                            response.setRobotNepoexitvalue(state.getNepoExitValue());
                            State communicationState = state.getState();
                            String infoAboutState;
                            if ( httpSessionState.isProcessing() || communicationState == State.ROBOT_IS_BUSY ) {
                                infoAboutState = "busy";
                            } else if ( state.isRobotProbablyDisconnected() || communicationState == State.GARBAGE ) {
                                infoAboutState = "disconnected";
                            } else {
                                infoAboutState = "wait"; // is there a need to distinguish the communication state more detailed?
                            }
                            response.setRobotState(infoAboutState);
                        }
                    }
                }
                response.setInitToken(httpSessionState.getInitToken());
            }
        } catch ( Exception e ) {
            UtilForREST.LOG.error("when adding info for the client, an unexpected exception occurred. Some info for the client may be missing", e);
        }
    }

    /**
     * read an input stream until it is exhausted and return it as a String
     *
     * @param is the input stream to be read
     * @return the data of the input stream as String or "" if no data was found
     */
    public static String convertStreamToString(InputStream is) {
        try (Scanner s = new Scanner(is)) {
            s.useDelimiter("\\A");
            return s.hasNext() ? s.next() : "";
        }
    }

    private static void addResultInfo(BaseResponse response, String restCallResultOkOrError, Key key) throws JSONException {
        String realKey = key.getKey();
        response.setRc(restCallResultOkOrError);
        response.setMessage(realKey);
        response.setCause(realKey);
    }
}
