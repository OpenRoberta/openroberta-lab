package de.fhg.iais.roberta.javaServer.restServices.all.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.generated.restEntities.BaseResponse;
import de.fhg.iais.roberta.generated.restEntities.FullRestRequest;
import de.fhg.iais.roberta.generated.restEntities.SetRobotRequest;
import de.fhg.iais.roberta.generated.restEntities.SetRobotResponse;
import de.fhg.iais.roberta.generated.restEntities.SetTokenRequest;
import de.fhg.iais.roberta.generated.restEntities.SetTokenResponse;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.RandomUrlPostfix;
import de.fhg.iais.roberta.util.ServerProperties;
import de.fhg.iais.roberta.util.Statistics;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.UtilForREST;

@Path("/admin")
public class ClientAdmin {
    private static final Logger LOG = LoggerFactory.getLogger(ClientAdmin.class);
    /**
     * simulate a connected robot. See also class {@link ClientProgram}
     * <ul>
     * <li>if set to "NOCONNCT", then no robot can be connected, but connection is simulated. This setting is NOT dangerous. If set and if a user enters
     * "NOCONNCT" as token, he or she can work as usual, but generated code is NOT transferred to the robot. For debugging purposes.
     * <li>if set to null, this debug feature is disabled
     * </ul>
     */
    public static final String NO_CONNECT = "NOCONNCT";

    private final RobotCommunicator brickCommunicator;
    private final ServerProperties serverProperties;

    @Inject
    public ClientAdmin(RobotCommunicator brickCommunicator, ServerProperties serverProperties) {
        this.brickCommunicator = brickCommunicator;
        this.serverProperties = serverProperties;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/setToken")
    public Response setToken(FullRestRequest fullRequest) throws Exception //
    {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRequest, true);
        try {
            SetTokenResponse setTokenResponse = SetTokenResponse.make();
            SetTokenRequest request = SetTokenRequest.make(fullRequest.getData());
            String cmd = "setToken";
            LOG.info("command is: " + cmd);
            setTokenResponse.setCmd(cmd);
            String token = request.getToken();
            if ( NO_CONNECT != null && NO_CONNECT.equals(token) ) {
                LOG.info("debug token is presented by a user. Download to robots is disabled for this user. This is a debugging feature and not risky.");
                httpSessionState.setToken(token);
                addRobotUpdateInfo(setTokenResponse, null, null);
                UtilForREST.addSuccessInfo(setTokenResponse, Key.TOKEN_SET_SUCCESS);
                LOG.info("success: debug token is registered in the session");
                Statistics.info("ConnectRobot", "success", true);
            } else {
                Key tokenAgreement = this.brickCommunicator.aTokenAgreementWasSent(token, httpSessionState.getRobotName());

                switch ( tokenAgreement ) {
                    case TOKEN_SET_SUCCESS:
                        httpSessionState.setToken(token);
                        String robotMenuVersion = this.brickCommunicator.getState(token).getMenuVersion();
                        String serverMenuVersion = httpSessionState.getRobotFactory().getMenuVersion();
                        addRobotUpdateInfo(setTokenResponse, robotMenuVersion, serverMenuVersion);
                        UtilForREST.addSuccessInfo(setTokenResponse, Key.TOKEN_SET_SUCCESS);
                        LOG.info("success: token " + token + " is registered in the session");
                        Statistics.info("ConnectRobot", "success", true);
                        break;
                    case TOKEN_SET_ERROR_WRONG_ROBOTTYPE:
                        addRobotUpdateInfo(setTokenResponse, null, null);
                        UtilForREST.addErrorInfo(setTokenResponse, Key.TOKEN_SET_ERROR_WRONG_ROBOTTYPE);
                        LOG.info("error: token " + token + " not registered in the session, wrong robot type");
                        Statistics.info("ConnectRobot", "success", false);
                        break;
                    case TOKEN_SET_ERROR_NO_ROBOT_WAITING:
                        addRobotUpdateInfo(setTokenResponse, null, null);
                        UtilForREST.addErrorInfo(setTokenResponse, Key.TOKEN_SET_ERROR_NO_ROBOT_WAITING);
                        LOG.info("error: token " + token + " not registered in the session");
                        Statistics.info("ConnectRobot", "success", false);
                        break;
                    default:
                        addRobotUpdateInfo(setTokenResponse, null, null);
                        LOG.error("invalid response for token agreement: " + tokenAgreement);
                        UtilForREST.addErrorInfo(setTokenResponse, Key.SERVER_ERROR);
                        Statistics.info("ConnectRobot", "success", false);
                        break;
                }
            }
            return UtilForREST.responseWithFrontendInfo(setTokenResponse, httpSessionState, this.brickCommunicator);
        } catch ( Exception e ) {
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: " + errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, null); // TODO: redesign error ticker number and add then: append("parameters", errorTicketId);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/updateFirmware")
    public Response updateFirmware(FullRestRequest fullRequest) throws Exception //
    {
        // TODO: This should be moved to an update server
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRequest, true);
        try {
            BaseResponse response = BaseResponse.make();
            String cmd = "updateFirmware";
            LOG.info("command is: " + cmd);

            response.setCmd(cmd);
            String token = httpSessionState.getToken();
            if ( token != null ) {
                // everything is fine
                boolean isPossible = this.brickCommunicator.firmwareUpdateRequested(token);
                if ( isPossible ) {
                    UtilForREST.addSuccessInfo(response, Key.ROBOT_FIRMWAREUPDATE_POSSIBLE);
                    return UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
                } else {
                    return UtilForREST.makeBaseResponseForError(Key.ROBOT_FIRMWAREUPDATE_IMPOSSIBLE, httpSessionState, null);
                }
            } else {
                return UtilForREST.makeBaseResponseForError(Key.ROBOT_NOT_CONNECTED, httpSessionState, null);
            }
        } catch ( Exception e ) {
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: " + errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, null);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/setRobot")
    public Response setRobot(FullRestRequest fullRequest) throws Exception //
    {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRequest, true);
        try {
            SetRobotResponse response = SetRobotResponse.make();
            SetRobotRequest request = SetRobotRequest.make(fullRequest.getData());
            String cmd = "setRobot";
            LOG.info("command is: " + cmd);

            response.setCmd(cmd);
            String robot = request.getRobot();
            if ( robot != null && this.serverProperties.getRobotWhitelist().contains(robot) ) {
                if ( httpSessionState.getRobotName() != robot ) {
                    // disconnect previous robot
                    // TODO consider keeping it so that we can switch between robot and simulation
                    // see: https://github.com/OpenRoberta/robertalab/issues/43
                    this.brickCommunicator.disconnect(httpSessionState.getToken());
                    // TODO remove this and use a communicator
                    if ( robot.equals("oraSim") ) {
                        httpSessionState.setToken("00000000");
                    } else {
                        httpSessionState.setToken(RandomUrlPostfix.generate(12, 12, 3, 3, 3));
                    }
                    httpSessionState.setRobotName(robot);
                    IRobotFactory robotFactory = httpSessionState.getRobotFactory();
                    response.setRobot(robot);

                    JSONObject program = new JSONObject();
                    JSONObject toolbox = new JSONObject();
                    toolbox.put("beginner", robotFactory.getProgramToolboxBeginner());
                    toolbox.put("expert", robotFactory.getProgramToolboxExpert());
                    program.put("toolbox", toolbox);
                    program.put("prog", robotFactory.getProgramDefault());
                    response.setProgram(program);
                    JSONObject configuration = new JSONObject();
                    configuration.put("toolbox", robotFactory.getConfigurationToolbox());
                    configuration.put("conf", robotFactory.getConfigurationDefault());
                    response.setConfiguration(configuration);
                    response.setSim(robotFactory.hasSim());
                    response.setMultipleSim(robotFactory.hasMultipleSim());
                    response.setConnection(robotFactory.getConnectionType());
                    response.setVendor(robotFactory.getVendorId());
                    response.setConfigurationUsed(robotFactory.hasConfiguration());
                    response.setCommandLine(robotFactory.getCommandline());
                    response.setSignature(robotFactory.getSignature());
                    response.setSourceCodeFileExtension(robotFactory.getSourceCodeFileExtension());
                    response.setBinaryFileExtension(robotFactory.getBinaryFileExtension());
                    response.setHasWlan(robotFactory.hasWlanCredentials());
                    response.setFirmwareDefault(robotFactory.getFirmwareDefaultProgramName());
                    LOG.info("set robot to {}", robot);
                    Statistics.info("ChangeRobot", "success", true);
                } else {
                    LOG.info("set Robot: robot {} was already set", robot);
                    Statistics.info("ChangeRobot", "success", true);
                }
                UtilForREST.addSuccessInfo(response, Key.ROBOT_SET_SUCCESS);
                return UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
            } else {
                LOG.error("Invalid command: " + cmd + " setting robot name to " + robot);
                return UtilForREST.makeBaseResponseForError(Key.ROBOT_DOES_NOT_EXIST, httpSessionState, null);
            }
        } catch ( Exception e ) {
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: " + errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, null); // TODO: redesign error ticker number and add then: append("parameters", errorTicketId);
        }
    }

    private static void addRobotUpdateInfo(SetTokenResponse response, String robotMenuVersion, String serverMenuVersion) throws JSONException {
        if ( robotMenuVersion != null && serverMenuVersion != null ) {
            response.setUpdate(Util.versionCompare(robotMenuVersion, serverMenuVersion));
            response.setServerVersion(serverMenuVersion);
        } else {
            response.setUpdate(0);
            response.setServerVersion("0");
        }
    }
}