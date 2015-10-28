package de.fhg.iais.roberta.javaServer.restServices.all;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.fhg.iais.roberta.javaServer.provider.OraData;
import de.fhg.iais.roberta.persistence.UserProcessor;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.robotCommunication.ev3.Ev3Communicator;
import de.fhg.iais.roberta.util.AliveData;
import de.fhg.iais.roberta.util.ClientLogger;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Util;

@Path("/user")
public class ClientUser {
    private static final Logger LOG = LoggerFactory.getLogger(ClientUser.class);

    private final Ev3Communicator brickCommunicator;

    @Inject
    public ClientUser(Ev3Communicator brickCommunicator) {
        this.brickCommunicator = brickCommunicator;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response command(@OraData HttpSessionState httpSessionState, @OraData DbSession dbSession, JSONObject fullRequest) throws Exception {
        AliveData.rememberClientCall();
        new ClientLogger().log(ClientUser.LOG, fullRequest);
        final int userId = httpSessionState.getUserId();
        JSONObject response = new JSONObject();
        try {
            JSONObject request = fullRequest.getJSONObject("data");
            String cmd = request.getString("cmd");
            ClientUser.LOG.info("command is: " + cmd);
            response.put("cmd", cmd);
            UserProcessor up = new UserProcessor(dbSession, httpSessionState);

            if ( cmd.equals("clear") ) {
                httpSessionState.setUserClearDataKeepTokenAndRobotId(HttpSessionState.NO_USER);
                response.put("rc", "ok");
                if ( userId != HttpSessionState.NO_USER ) {
                    ClientUser.LOG.info("clear for (logged in) user " + userId + ". Has the user reloaded the page?");
                }

            } else if ( cmd.equals("login") && !httpSessionState.isUserLoggedIn() ) {
                String userAccountName = request.getString("accountName");
                String password = request.getString("password");
                User user = up.getUser(userAccountName, password);
                Util.addResultInfo(response, up);
                if ( user != null ) {
                    int id = user.getId();
                    String account = user.getAccount();
                    httpSessionState.setUserClearDataKeepTokenAndRobotId(id);
                    user.setLastLogin();
                    response.put("userId", id);
                    response.put("userRole", user.getRole());
                    response.put("userAccountName", account);
                    ClientUser.LOG.info("login: user {} (id {}) logged in", account, id);
                    AliveData.rememberLogin();
                }
            } else if ( cmd.equals("getUser") && httpSessionState.isUserLoggedIn() ) {
                String userAccountName = request.getString("accountName");
                User user = up.getUser(userAccountName);
                Util.addResultInfo(response, up);
                if ( user != null ) {
                    int id = user.getId();
                    String account = user.getAccount();
                    String userName = user.getUserName();
                    String email = user.getEmail();
                    response.put("userId", id);
                    response.put("userAccountName", account);
                    response.put("userName", userName);
                    response.put("userEmail", email);
                }

            } else if ( cmd.equals("logout") && httpSessionState.isUserLoggedIn() ) {
                httpSessionState.setUserClearDataKeepTokenAndRobotId(HttpSessionState.NO_USER);
                response.put("rc", "ok");
                response.put("message", Key.USER_LOGOUT_SUCCESS.getKey());
                ClientUser.LOG.info("logout of user " + userId);

            } else if ( cmd.equals("createUser") ) {
                String account = request.getString("accountName");
                String password = request.getString("password");
                String email = request.getString("userEmail");
                String userName = request.getString("userName");
                String role = request.getString("role");
                //String tag = request.getString("tag");
                up.createUser(account, password, userName, role, email, null);
                Util.addResultInfo(response, up);

            } else if ( cmd.equals("updateUser") ) {
                String account = request.getString("accountName");
                String userName = request.getString("userName");
                String email = request.getString("userEmail");
                String role = request.getString("role");
                //String tag = request.getString("tag");
                up.updateUser(account, userName, role, email, null);
                Util.addResultInfo(response, up);

            } else if ( cmd.equals("changePassword") ) {
                String account = request.getString("accountName");
                String oldPassword = request.getString("oldPassword");
                String newPassword = request.getString("newPassword");
                up.updatePassword(account, oldPassword, newPassword);
                Util.addResultInfo(response, up);
            } else if ( cmd.equals("obtainUsers") ) {

                String sortBy = request.getString("sortBy");
                int offset = request.getInt("offset");
                String tagFilter = request.getString("tagFilter");
                if ( tagFilter == "null" ) {
                    tagFilter = null;
                }
                JSONArray usersJSONArray = up.getUsers(sortBy, offset, tagFilter);
                response.put("usersList", usersJSONArray);
                Util.addResultInfo(response, up);

            } else if ( cmd.equals("deleteUser") ) {
                String account = request.getString("accountName");
                String password = request.getString("password");
                up.deleteUser(account, password);
                Util.addResultInfo(response, up);

            } else {
                ClientUser.LOG.error("Invalid command: " + cmd);
                Util.addErrorInfo(response, Key.COMMAND_INVALID);
            }
            dbSession.commit();
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            ClientUser.LOG.error("Exception. Error ticket: " + errorTicketId, e);
            Util.addErrorInfo(response, Key.SERVER_ERROR).append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
        Util.addFrontendInfo(response, httpSessionState, this.brickCommunicator);
        return Response.ok(response).build();
    }
}