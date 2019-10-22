package de.fhg.iais.roberta.javaServer.restServices.all.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.javaServer.provider.OraData;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.RandomUrlPostfix;
import de.fhg.iais.roberta.util.ServerProperties;
import de.fhg.iais.roberta.util.Statistics;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.Util1;

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
    public Response command(@OraData HttpSessionState httpSessionState, @OraData DbSession dbSession, JSONObject fullRequest, @Context HttpHeaders httpHeaders)
        throws Exception //
    {
        Util.handleRequestInit(httpSessionState, LOG, fullRequest);
        JSONObject response = new JSONObject();
        try {
            JSONObject request = fullRequest.getJSONObject("data");
            String cmd = request.getString("cmd");
            LOG.info("command is: " + cmd);

            response.put("cmd", cmd);
            if ( cmd.equals("setToken") ) {
                String token = request.getString("token");
                if ( NO_CONNECT != null && NO_CONNECT.equals(token) ) {
                    LOG.info("debug token is presented by a user. Download to robots is disabled for this user. Debugging feature and not risky.");
                    httpSessionState.setToken(token);
                    addRobotUpdateInfo(response, null, null);
                    Util.addSuccessInfo(response, Key.TOKEN_SET_SUCCESS);
                    LOG.info("success: debug token is registered in the session");
                    Statistics.info("ConnectRobot", "success", true);
                } else {
                    Key tokenAgreement = this.brickCommunicator.aTokenAgreementWasSent(token, httpSessionState.getRobotName());

                    switch ( tokenAgreement ) {
                        case TOKEN_SET_SUCCESS:
                            httpSessionState.setToken(token);
                            String robotMenuVersion = this.brickCommunicator.getState(token).getMenuVersion();
                            String serverMenuVersion = httpSessionState.getRobotFactory().getMenuVersion();
                            addRobotUpdateInfo(response, robotMenuVersion, serverMenuVersion);
                            Util.addSuccessInfo(response, Key.TOKEN_SET_SUCCESS);
                            LOG.info("success: token " + token + " is registered in the session");
                            Statistics.info("ConnectRobot", "success", true);
                            break;
                        case TOKEN_SET_ERROR_WRONG_ROBOTTYPE:
                            Util.addErrorInfo(response, Key.TOKEN_SET_ERROR_WRONG_ROBOTTYPE);
                            LOG.info("error: token " + token + " not registered in the session, wrong robot type");
                            Statistics.info("ConnectRobot", "success", false);
                            break;
                        case TOKEN_SET_ERROR_NO_ROBOT_WAITING:
                            Util.addErrorInfo(response, Key.TOKEN_SET_ERROR_NO_ROBOT_WAITING);
                            LOG.info("error: token " + token + " not registered in the session");
                            Statistics.info("ConnectRobot", "success", false);
                            break;
                        default:
                            LOG.error("invalid response for token agreement: " + tokenAgreement);
                            Util.addErrorInfo(response, Key.SERVER_ERROR);
                            Statistics.info("ConnectRobot", "success", false);
                            break;
                    }
                }

            } else if ( cmd.equals("updateFirmware") ) {
                // TODO: This should be moved to update server
                String token = httpSessionState.getToken();
                if ( token != null ) {
                    // everything is fine
                    boolean isPossible = this.brickCommunicator.firmwareUpdateRequested(token);
                    if ( isPossible ) {
                        Util.addSuccessInfo(response, Key.ROBOT_FIRMWAREUPDATE_POSSIBLE);
                    } else {
                        Util.addErrorInfo(response, Key.ROBOT_FIRMWAREUPDATE_IMPOSSIBLE);
                    }
                } else {
                    Util.addErrorInfo(response, Key.ROBOT_NOT_CONNECTED);
                }
            } else if ( cmd.equals("setRobot") ) {
                String robot = request.getString("robot");
                if ( robot != null && this.serverProperties.getRobotWhitelist().contains(robot) ) {
                    Util.addSuccessInfo(response, Key.ROBOT_SET_SUCCESS);
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
                        response.put("robot", robot);
                        JSONObject program;
                        JSONObject configuration;
                        JSONObject toolbox;
                        program = new JSONObject();
                        configuration = new JSONObject();
                        toolbox = new JSONObject();
                        toolbox.put("beginner", robotFactory.getProgramToolboxBeginner());
                        toolbox.put("expert", robotFactory.getProgramToolboxExpert());
                        program.put("toolbox", toolbox);
                        program.put("prog", robotFactory.getProgramDefault());
                        response.put("program", program);
                        configuration.put("toolbox", robotFactory.getConfigurationToolbox());
                        configuration.put("conf", robotFactory.getConfigurationDefault());
                        response.put("configuration", configuration);
                        response.put("sim", robotFactory.hasSim());
                        response.put("multipleSim", robotFactory.hasMultipleSim());
                        response.put("connection", robotFactory.getConnectionType());
                        response.put("vendor", robotFactory.getVendorId());
                        response.put("configurationUsed", robotFactory.hasConfiguration());
                        response.put("commandLine", robotFactory.getCommandline());
                        response.put("signature", robotFactory.getSignature());
                        response.put("fileExtension", robotFactory.getFileExtension());
                        LOG.info("set robot to {}", robot);
                        Statistics.info("ChangeRobot", "success", true);
                    } else {
                        LOG.info("set Robot: robot {} was already set", robot);
                        Statistics.info("ChangeRobot", "success", false);
                    }
                } else {
                    LOG.error("Invalid command: " + cmd + " setting robot name to " + robot);
                    Util.addErrorInfo(response, Key.ROBOT_DOES_NOT_EXIST);
                }
            } else {
                LOG.error("Invalid command: " + cmd);
                Util.addErrorInfo(response, Key.COMMAND_INVALID);
            }
            dbSession.commit();
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util1.getErrorTicketId();
            LOG.error("Exception. Error ticket: " + errorTicketId, e);
            Util.addErrorInfo(response, Key.SERVER_ERROR, errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
        return Util.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
    }

    private void addRobotUpdateInfo(JSONObject response, String robotMenuVersion, String serverMenuVersion) throws JSONException {
        if ( robotMenuVersion != null && serverMenuVersion != null ) {
            response.put("robot.update", Util.versionCompare(robotMenuVersion, serverMenuVersion));
            response.put("robot.serverVersion", serverMenuVersion);
        } else {
            response.put("robot.update", 0);
            response.put("robot.serverVersion", 0);
        }
    }

}