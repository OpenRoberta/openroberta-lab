package de.fhg.iais.roberta.javaServer.restServices.all;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;

import javax.mail.MessagingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.fhg.iais.roberta.util.*;
import de.fhg.iais.roberta.util.Statistics;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.google.inject.Inject;

import de.fhg.iais.roberta.javaServer.provider.OraData;
import de.fhg.iais.roberta.main.MailManagement;
import de.fhg.iais.roberta.persistence.LostPasswordProcessor;
import de.fhg.iais.roberta.persistence.PendingEmailConfirmationsProcessor;
import de.fhg.iais.roberta.persistence.UserProcessor;
import de.fhg.iais.roberta.persistence.bo.LostPassword;
import de.fhg.iais.roberta.persistence.bo.PendingEmailConfirmations;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;

@Path("/user")
public class ClientUser {
    private static final Logger LOG = LoggerFactory.getLogger(ClientUser.class);

    private final RobotCommunicator brickCommunicator;
    private final MailManagement mailManagement;

    private final boolean isPublicServer;

    @Inject
    public ClientUser(RobotCommunicator brickCommunicator, RobertaProperties robertaProperties, MailManagement mailManagement) {
        this.brickCommunicator = brickCommunicator;
        this.mailManagement = mailManagement;
        this.isPublicServer = robertaProperties.getBooleanProperty("server.public");
    }

    private static String[] statusText = new String[2];
    private static long statusTextTimestamp;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response command(@OraData HttpSessionState httpSessionState, @OraData DbSession dbSession, JSONObject fullRequest) throws Exception {
        AliveData.rememberClientCall();
        MDC.put("sessionId", String.valueOf(httpSessionState.getSessionNumber()));
        MDC.put("userId", String.valueOf(httpSessionState.getUserId()));
        MDC.put("robotName", String.valueOf(httpSessionState.getRobotName()));
        new ClientLogger().log(ClientUser.LOG, fullRequest);
        final int userId = httpSessionState.getUserId();
        JSONObject response = new JSONObject();
        try {
            JSONObject request = fullRequest.getJSONObject("data");
            String cmd = request.getString("cmd");
            ClientUser.LOG.info("command is: " + cmd);
            response.put("cmd", cmd);
            UserProcessor up = new UserProcessor(dbSession, httpSessionState);
            LostPasswordProcessor lostPasswordProcessor = new LostPasswordProcessor(dbSession, httpSessionState);
            PendingEmailConfirmationsProcessor pendingConfirmationProcessor = new PendingEmailConfirmationsProcessor(dbSession, httpSessionState);
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
                    String name = user.getUserName();
                    httpSessionState.setUserClearDataKeepTokenAndRobotId(id);
                    user.setLastLogin();
                    response.put("userId", id);
                    response.put("userRole", user.getRole());
                    response.put("userAccountName", account);
                    response.put("userName", name);
                    response.put("isAccountActivated", user.isActivated());
                    ClientUser.LOG.info("login: user {} (id {}) logged in", account, id);
                    Statistics.info("UserLogin");
                    AliveData.rememberLogin();
                }

            } else if ( cmd.equals("getUser") && httpSessionState.isUserLoggedIn() ) {
                User user = up.getUser(httpSessionState.getUserId());
                Util.addResultInfo(response, up);
                if ( user != null ) {
                    int id = user.getId();
                    String account = user.getAccount();
                    String userName = user.getUserName();
                    String email = user.getEmail();
                    boolean age = user.isYoungerThen14();
                    response.put("userId", id);
                    response.put("userAccountName", account);
                    response.put("userName", userName);
                    response.put("userEmail", email);
                    response.put("isYoungerThen14", age);
                }

            } else if ( cmd.equals("logout") && httpSessionState.isUserLoggedIn() ) {
                httpSessionState.setUserClearDataKeepTokenAndRobotId(HttpSessionState.NO_USER);
                response.put("rc", "ok");
                response.put("message", Key.USER_LOGOUT_SUCCESS.getKey());
                ClientUser.LOG.info("logout of user " + userId);
                Statistics.info("UserLogout");
            } else if ( cmd.equals("createUser") ) {
                String account = request.getString("accountName");
                String password = request.getString("password");
                String email = request.getString("userEmail");
                String userName = request.getString("userName");
                String role = request.getString("role");
                //String tag = request.getString("tag");
                boolean isYoungerThen14 = request.getString("isYoungerThen14").equals("1");
                up.createUser(account, password, userName, role, email, null, isYoungerThen14);
                if ( this.isPublicServer && !email.equals("") && up.isOk() ) {
                    String lang = request.getString("language");
                    PendingEmailConfirmations confirmation = pendingConfirmationProcessor.createEmailConfirmation(account);
                    sendActivationMail(up, confirmation.getUrlPostfix(), account, email, lang, isYoungerThen14);
                }
                Util.addResultInfo(response, up);

            } else if ( cmd.equals("updateUser") ) {
                String account = request.getString("accountName");
                String userName = request.getString("userName");
                String email = request.getString("userEmail");
                String role = request.getString("role");
                //String tag = request.getString("tag");
                boolean isYoungerThen14 = request.getString("isYoungerThen14").equals("1");
                User user = up.getUser(account);
                String oldEmail = user.getEmail();
                up.updateUser(account, userName, role, email, null, isYoungerThen14);
                if ( this.isPublicServer && !oldEmail.equals(email) && up.isOk() ) {
                    String lang = request.getString("language");
                    PendingEmailConfirmations confirmation = pendingConfirmationProcessor.createEmailConfirmation(account);
                    sendActivationMail(up, confirmation.getUrlPostfix(), account, email, lang, isYoungerThen14);
                    up.deactivateAccount(user.getId());
                }
                Util.addResultInfo(response, up);

            } else if ( cmd.equals("changePassword") ) {
                String account = request.getString("accountName");
                String oldPassword = request.getString("oldPassword");
                String newPassword = request.getString("newPassword");
                up.updatePassword(account, oldPassword, newPassword);
                Util.addResultInfo(response, up);
            } else if ( cmd.equals("resetPassword") ) {
                String resetPasswordLink = request.getString("resetPasswordLink");
                String newPassword = request.getString("newPassword");
                LostPassword lostPassword = lostPasswordProcessor.loadLostPassword(resetPasswordLink);
                if ( lostPassword != null ) {
                    up.resetPassword(lostPassword.getUserID(), newPassword);
                }
                if ( up.getMessage() == Key.USER_UPDATE_SUCCESS ) {
                    lostPasswordProcessor.deleteLostPassword(resetPasswordLink);
                }
                Util.addResultInfo(response, up);
            } else if ( cmd.equals("isResetPasswordLinkExpired") ) {
                String resetPasswordLink = request.getString("resetPasswordLink");
                LostPassword lostPassword = lostPasswordProcessor.loadLostPassword(resetPasswordLink);
                boolean isExpired = true;
                if ( lostPassword != null ) {
                    Date currentTime = new Date();
                    isExpired = (currentTime.getTime() - lostPassword.getCreated().getTime()) / 3600000.0 > 24;
                }
                if ( isExpired ) {
                    up.setSuccess(Key.USER_PASSWORD_RECOVERY_EXPIRED_URL);
                }
                response.put("resetPasswordLinkExpired", isExpired);
                Util.addResultInfo(response, up);
            } else if ( cmd.equals("passwordRecovery") ) {
                String lostEmail = request.getString("lostEmail");
                String lang = request.getString("language");
                User user = up.getUserByEmail(lostEmail);
                Util.addResultInfo(response, up);
                if ( user != null ) {
                    LostPassword lostPassword = lostPasswordProcessor.createLostPassword(user.getId());
                    ClientUser.LOG.info("url postfix generated: " + lostPassword.getUrlPostfix());
                    String[] body =
                        {
                            user.getAccount(),
                            lostPassword.getUrlPostfix()
                        };
                    try {
                        this.mailManagement.send(user.getEmail(), "reset", body, lang, false);
                        up.setSuccess(Key.USER_PASSWORD_RECOVERY_SENT_MAIL_SUCCESS);
                    } catch ( MessagingException e ) {
                        up.setError(Key.USER_PASSWORD_RECOVERY_SENT_MAIL_FAIL);
                    }
                }
                Util.addResultInfo(response, up);
            } else if ( cmd.equals("activateUser") ) {
                String userActivationLink = request.getString("userActivationLink");
                PendingEmailConfirmations confirmation = pendingConfirmationProcessor.loadConfirmation(userActivationLink);
                if ( confirmation != null ) {
                    up.activateAccount(confirmation.getUserID());
                }
                if ( up.getMessage() == Key.USER_ACTIVATION_SUCCESS ) {
                    pendingConfirmationProcessor.deleteEmailConfirmation(userActivationLink);
                    Util.addResultInfo(response, up);
                } else {
                    Util.addErrorInfo(response, Key.USER_ACTIVATION_INVALID_URL);
                }
            } else if ( cmd.equals("resendActivation") ) {
                String account = request.getString("accountName");
                String lang = request.getString("language");
                User user = up.getUser(account);
                if ( this.isPublicServer && user != null && !user.getEmail().equals("") ) {
                    PendingEmailConfirmations confirmation = pendingConfirmationProcessor.createEmailConfirmation(account);
                    // TODO ask here again for the age
                    sendActivationMail(up, confirmation.getUrlPostfix(), account, user.getEmail(), lang, false);
                }
                Util.addResultInfo(response, up);
            } else if ( cmd.equals("deleteUser") ) {
                String account = request.getString("accountName");
                String password = request.getString("password");
                up.deleteUser(account, password);
                Util.addResultInfo(response, up);

            } else if ( cmd.equals("getStatusText") ) {
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                Long current = timestamp.getTime() / 1000L;
                if ( current > statusTextTimestamp ) {
                    statusText[0] = "";
                    statusText[1] = "";
                }
                JSONArray statusTextJSON = new JSONArray(Arrays.asList(statusText));
                response.put("statustext", statusTextJSON);
                response.put("rc", "ok");
                // Util.addResultInfo(response, up); // should not be necessary

            } else if ( cmd.equals("setStatusText") && userId == 1 ) {
                statusText[0] = request.getString("english");
                statusText[1] = request.getString("german");
                statusTextTimestamp = request.getLong("timestamp");
                response.put("rc", "ok");
            } else {
                ClientUser.LOG.error("Invalid command: " + cmd);
                Util.addErrorInfo(response, Key.COMMAND_INVALID);
            }
            dbSession.commit();
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util1.getErrorTicketId();
            ClientUser.LOG.error("Exception. Error ticket: " + errorTicketId, e);
            Util.addErrorInfo(response, Key.SERVER_ERROR).append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
        Util.addFrontendInfo(response, httpSessionState, this.brickCommunicator);
        MDC.clear();
        return Response.ok(response).build();
    }

    private void sendActivationMail(UserProcessor up, String urlPostfix, String account, String email, String lang, boolean isYoungerThen14) throws Exception {
        String[] body =
            {
                account,
                urlPostfix
            };
        try {
            this.mailManagement.send(email, "activate", body, lang, isYoungerThen14);
            up.setSuccess(Key.USER_ACTIVATION_SENT_MAIL_SUCCESS);
        } catch ( Exception e ) {
            up.setError(Key.USER_ACTIVATION_SENT_MAIL_FAIL);
        }
    }
}