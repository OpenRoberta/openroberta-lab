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
    public Response command(JSONObject fullRequest, @Context HttpHeaders httpHeaders) throws Exception //
    {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRequest);
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
                    UtilForREST.addSuccessInfo(response, Key.TOKEN_SET_SUCCESS);
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
                            UtilForREST.addSuccessInfo(response, Key.TOKEN_SET_SUCCESS);
                            LOG.info("success: token " + token + " is registered in the session");
                            Statistics.info("ConnectRobot", "success", true);
                            break;
                        case TOKEN_SET_ERROR_WRONG_ROBOTTYPE:
                            UtilForREST.addErrorInfo(response, Key.TOKEN_SET_ERROR_WRONG_ROBOTTYPE);
                            LOG.info("error: token " + token + " not registered in the session, wrong robot type");
                            Statistics.info("ConnectRobot", "success", false);
                            break;
                        case TOKEN_SET_ERROR_NO_ROBOT_WAITING:
                            UtilForREST.addErrorInfo(response, Key.TOKEN_SET_ERROR_NO_ROBOT_WAITING);
                            LOG.info("error: token " + token + " not registered in the session");
                            Statistics.info("ConnectRobot", "success", false);
                            break;
                        default:
                            LOG.error("invalid response for token agreement: " + tokenAgreement);
                            UtilForREST.addErrorInfo(response, Key.SERVER_ERROR);
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
                        UtilForREST.addSuccessInfo(response, Key.ROBOT_FIRMWAREUPDATE_POSSIBLE);
                    } else {
                        UtilForREST.addErrorInfo(response, Key.ROBOT_FIRMWAREUPDATE_IMPOSSIBLE);
                    }
                } else {
                    UtilForREST.addErrorInfo(response, Key.ROBOT_NOT_CONNECTED);
                }
            } else if ( cmd.equals("setRobot") ) {
                String robot = request.getString("robot");
                if ( robot != null && this.serverProperties.getRobotWhitelist().contains(robot) ) {
                    UtilForREST.addSuccessInfo(response, Key.ROBOT_SET_SUCCESS);
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
                        response.put("sourceCodeFileExtension", robotFactory.getSourceCodeFileExtension());
                        response.put("binaryFileExtension", robotFactory.getBinaryFileExtension());
                        response.put("hasWlan", robotFactory.hasWlanCredentials());
                        response.put("firmwareDefault", robotFactory.getFirmwareDefaultProgramName());
                        LOG.info("set robot to {}", robot);
                        Statistics.info("ChangeRobot", "success", true);
                    } else {
                        LOG.info("set Robot: robot {} was already set", robot);
                        Statistics.info("ChangeRobot", "success", false);
                    }
                } else {
                    LOG.error("Invalid command: " + cmd + " setting robot name to " + robot);
                    UtilForREST.addErrorInfo(response, Key.ROBOT_DOES_NOT_EXIST);
                }
            } else {
                LOG.error("Invalid command: " + cmd);
                UtilForREST.addErrorInfo(response, Key.COMMAND_INVALID);
            }
        } catch ( Exception e ) {
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: " + errorTicketId, e);
            UtilForREST.addErrorInfo(response, Key.SERVER_ERROR, errorTicketId);
        }
        return UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
    }

    private static void addRobotUpdateInfo(JSONObject response, String robotMenuVersion, String serverMenuVersion) throws JSONException {
        if ( robotMenuVersion != null && serverMenuVersion != null ) {
            response.put("robot.update", ClientAdmin.versionCompare(robotMenuVersion, serverMenuVersion));
            response.put("robot.serverVersion", serverMenuVersion);
        } else {
            response.put("robot.update", 0);
            response.put("robot.serverVersion", 0);
        }
    }

    /**
     * Compares two version strings.
     *
     * @note It does not work if "1.10" is supposed to be equal to "1.10.0".
     * @param str1 a string of ordinal numbers separated by decimal points.
     * @param str2 a string of ordinal numbers separated by decimal points.
     * @return The result is a negative integer if str1 is _numerically_ less than str2. The result is a positive integer if str1 is _numerically_ greater than
     *         str2. The result is zero if the strings are _numerically_ equal.
     */
    private static int versionCompare(String str1, String str2) {
        String[] vals1 = str1.split("\\.");
        String[] vals2 = str2.split("\\.");
        int i = 0;

        while ( i < vals1.length && i < vals2.length && vals1[i].equals(vals2[i]) ) {
            i++;
        }

        if ( i < vals1.length && i < vals2.length ) {
            int diff = Integer.valueOf(vals1[i]).compareTo(Integer.valueOf(vals2[i]));
            return Integer.signum(diff);
        }

        return Integer.signum(vals1.length - vals2.length);
    }
}