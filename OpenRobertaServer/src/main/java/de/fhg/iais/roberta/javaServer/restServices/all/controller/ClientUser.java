package de.fhg.iais.roberta.javaServer.restServices.all.controller;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.mail.MessagingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import de.fhg.iais.roberta.generated.restEntities.ActivateUserRequest;
import de.fhg.iais.roberta.generated.restEntities.BaseResponse;
import de.fhg.iais.roberta.generated.restEntities.ChangePasswordRequest;
import de.fhg.iais.roberta.generated.restEntities.DeleteUserRequest;
import de.fhg.iais.roberta.generated.restEntities.FullRestRequest;
import de.fhg.iais.roberta.generated.restEntities.GetStatusTextResponse;
import de.fhg.iais.roberta.generated.restEntities.GetUserResponse;
import de.fhg.iais.roberta.generated.restEntities.IsResetPasswordLinkExpiredResponse;
import de.fhg.iais.roberta.generated.restEntities.LoginRequest;
import de.fhg.iais.roberta.generated.restEntities.LoginResponse;
import de.fhg.iais.roberta.generated.restEntities.PasswordRecoveryRequest;
import de.fhg.iais.roberta.generated.restEntities.ResendActivationRequest;
import de.fhg.iais.roberta.generated.restEntities.ResetPasswordRequest;
import de.fhg.iais.roberta.generated.restEntities.SetStatusTextRequest;
import de.fhg.iais.roberta.generated.restEntities.UserRequest;
import de.fhg.iais.roberta.javaServer.provider.OraData;
import de.fhg.iais.roberta.main.MailManagement;
import de.fhg.iais.roberta.persistence.LostPasswordProcessor;
import de.fhg.iais.roberta.persistence.PendingEmailConfirmationsProcessor;
import de.fhg.iais.roberta.persistence.ProcessorStatus;
import de.fhg.iais.roberta.persistence.UserGroupProcessor;
import de.fhg.iais.roberta.persistence.UserProcessor;
import de.fhg.iais.roberta.persistence.bo.LostPassword;
import de.fhg.iais.roberta.persistence.bo.PendingEmailConfirmations;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.bo.UserGroup;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.util.AliveData;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.ServerProperties;
import de.fhg.iais.roberta.util.Statistics;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.UtilForREST;

@Path("/user")
public class ClientUser {
    private static final Logger LOG = LoggerFactory.getLogger(ClientUser.class);

    private final RobotCommunicator brickCommunicator;
    private final MailManagement mailManagement;

    private final boolean isPublicServer;

    @Inject
    public ClientUser(RobotCommunicator brickCommunicator, ServerProperties serverProperties, MailManagement mailManagement) {
        this.brickCommunicator = brickCommunicator;
        this.mailManagement = mailManagement;
        this.isPublicServer = serverProperties.getBooleanProperty("server.public");
    }

    private static String[] statusText = new String[2];
    private static long statusTextTimestamp;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/clear")
    public Response clear(FullRestRequest fullRequest) throws Exception {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRequest, true);
        try {
            BaseResponse response = BaseResponse.make();
            final int userId = httpSessionState.getUserId();
            String cmd = "clear";
            ClientUser.LOG.info("command is: " + cmd);
            response.setCmd(cmd);

            httpSessionState.setUserClearDataKeepTokenAndRobotId(HttpSessionState.NO_USER);
            if ( userId != HttpSessionState.NO_USER ) {
                ClientUser.LOG.info("clear for (logged in) user " + userId + ". Has the user reloaded the page?");
            }
            UtilForREST.addSuccessInfo(response, Key.INIT_SUCCESS);
            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
        } catch ( Exception e ) {
            String errorTicketId = Util.getErrorTicketId();
            ClientUser.LOG.error("Exception. Error ticket: " + errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, this.brickCommunicator); // TODO: refactor error ticket .append("parameters", errorTicketId);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/login")
    public Response login(@OraData DbSession dbSession, FullRestRequest fullRequest) throws Exception {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRequest, true);
        try {
            LoginResponse response = LoginResponse.make();
            LoginRequest request = LoginRequest.make(fullRequest.getData());
            String cmd = "login";
            ClientUser.LOG.info("command is: " + cmd);
            response.setCmd(cmd);

            if ( httpSessionState.isUserLoggedIn() ) {
                ClientUser.LOG.error("Invalid command: " + cmd);
                return UtilForREST.makeBaseResponseForError(Key.COMMAND_INVALID, httpSessionState, this.brickCommunicator);
            } else {
                UserProcessor up = new UserProcessor(dbSession, httpSessionState);
                String userAccountName = request.getAccountName();
                String password = request.getPassword();

                UserGroup userGroup = null;
                String userGroupOwnerAccount = request.getUserGroupOwner();
                String userGroupName = request.getUserGroupName();

                User user;

                if ( userGroupOwnerAccount != null && userGroupName != null ) {
                    UserGroupProcessor ugp = new UserGroupProcessor(dbSession, httpSessionState, this.isPublicServer);

                    User userGroupOwner = up.getUser(userGroupOwnerAccount);
                    if ( userGroupOwner == null ) {
                        UtilForREST.addResultInfo(response, up);
                        return UtilForREST.makeBaseResponseForError(Key.GROUP_GET_ONE_ERROR_NOT_FOUND, httpSessionState, this.brickCommunicator);
                    }

                    userGroup = ugp.getGroup(userGroupName, userGroupOwner);
                    if ( userGroup == null ) {
                        return UtilForREST.makeBaseResponseForError(ugp.getMessage(), httpSessionState, this.brickCommunicator);
                    }

                    user = up.getUser(userGroup, userAccountName, password);
                } else {
                    user = up.getUser(userAccountName, password);
                    userGroupName = "";
                    userGroupOwnerAccount = "";
                }

                UtilForREST.addResultInfo(response, up);

                if ( user != null ) {
                    int id = user.getId();
                    String account = user.getAccount();
                    String name = user.getUserName();
                    httpSessionState.setUserClearDataKeepTokenAndRobotId(id);
                    user.setLastLogin();
                    response.setUserId(id);
                    response.setUserRole(user.getRole().toString());
                    response.setUserAccountName(account);
                    response.setUserName(name);
                    response.setIsAccountActivated(user.isActivated());
                    response.setUserGroupName(userGroupName);
                    response.setUserGroupOwner(userGroupOwnerAccount);
                    ClientUser.LOG.info("login: user {} (id {}) logged in", account, id);
                    Statistics.info("UserLogin", "success", true);
                    AliveData.rememberLogin();
                    return UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
                } else {
                    Statistics.info("UserLogin", "success", false);
                    return UtilForREST.makeBaseResponseForError(Key.USER_GET_ONE_ERROR_ID_OR_PASSWORD_WRONG, httpSessionState, this.brickCommunicator);
                }
            }
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            ClientUser.LOG.error("Exception. Error ticket: " + errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, this.brickCommunicator); // TODO: refactor error ticket .append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getUser")
    public Response getUser(@OraData DbSession dbSession, FullRestRequest fullRequest) throws Exception {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRequest, true);
        try {
            GetUserResponse response = GetUserResponse.make();
            String cmd = "getUser";
            ClientUser.LOG.info("command is: " + cmd);
            response.setCmd(cmd);

            if ( httpSessionState.isUserLoggedIn() ) {
                UserProcessor up = new UserProcessor(dbSession, httpSessionState);
                User user = up.getUser(httpSessionState.getUserId());
                UtilForREST.addResultInfo(response, up);
                if ( user != null ) {
                    int id = user.getId();
                    String account = user.getAccount();
                    String userName = user.getUserName();
                    String email = user.getEmail();
                    boolean age = user.isYoungerThen14();
                    response.setUserId(id);
                    response.setUserAccountName(account);
                    response.setUserName(userName);
                    response.setUserEmail(email);
                    response.setIsYoungerThen14(age);
                }
                return UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
            } else {
                ClientUser.LOG.error("Invalid command: " + cmd);
                UtilForREST.addErrorInfo(response, Key.COMMAND_INVALID);
                return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, this.brickCommunicator);
            }
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            ClientUser.LOG.error("Exception. Error ticket: " + errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, this.brickCommunicator); // TODO: refactor error ticket .append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/logout")
    public Response logout(FullRestRequest fullRequest) throws Exception {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRequest, true);
        try {
            BaseResponse response = BaseResponse.make();
            final int userId = httpSessionState.getUserId();
            String cmd = "logout";
            ClientUser.LOG.info("command is: " + cmd);
            response.setCmd(cmd);

            if ( httpSessionState.isUserLoggedIn() ) {
                httpSessionState.setUserClearDataKeepTokenAndRobotId(HttpSessionState.NO_USER);
                response.setRc("ok");
                response.setMessage(Key.USER_LOGOUT_SUCCESS.getKey());
                // failing isn't logged in the statistics
                ClientUser.LOG.info("logout of user " + userId);
                Statistics.info("UserLogout", "success", true);
                return UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
            } else {
                ClientUser.LOG.error("Invalid command: " + cmd);
                return UtilForREST.makeBaseResponseForError(Key.COMMAND_INVALID, httpSessionState, this.brickCommunicator);
            }
        } catch ( Exception e ) {
            String errorTicketId = Util.getErrorTicketId();
            ClientUser.LOG.error("Exception. Error ticket: " + errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, this.brickCommunicator); // TODO: refactor error ticket .append("parameters", errorTicketId);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/createUser")
    public Response createUser(@OraData DbSession dbSession, FullRestRequest fullRequest) throws Exception {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRequest, true);
        try {
            BaseResponse response = BaseResponse.make();
            UserRequest request = UserRequest.make(fullRequest.getData());
            String cmd = "createUser";
            ClientUser.LOG.info("command is: " + cmd);
            response.setCmd(cmd);
            UserProcessor up = new UserProcessor(dbSession, httpSessionState);

            String account = request.getAccountName();
            String password = request.getPassword();
            String email = request.getUserEmail();
            String userName = request.getUserName();
            String role = request.getRole();
            //String tag = request.getString("tag");
            boolean isYoungerThen14 = request.getIsYoungerThen14();
            up.createUser(account, password, userName, role, email, null, isYoungerThen14);
            if ( this.isPublicServer && !email.equals("") && up.succeeded() ) {
                PendingEmailConfirmationsProcessor pendingConfirmationProcessor = new PendingEmailConfirmationsProcessor(dbSession, httpSessionState);
                String lang = request.getLanguage();
                PendingEmailConfirmations confirmation = pendingConfirmationProcessor.createEmailConfirmation(account);
                sendActivationMail(up, confirmation.getUrlPostfix(), account, email, lang, isYoungerThen14);
            }
            Statistics.info("UserCreate", "success", up.succeeded());
            UtilForREST.addResultInfo(response, up);
            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            ClientUser.LOG.error("Exception. Error ticket: " + errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, this.brickCommunicator); // TODO: refactor error ticket .append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/updateUser")
    public Response updateUser(@OraData DbSession dbSession, FullRestRequest fullRequest) throws Exception {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRequest, true);
        try {
            BaseResponse response = BaseResponse.make();
            UserRequest request = UserRequest.make(fullRequest.getData());
            String cmd = "updateUser";
            ClientUser.LOG.info("command is: " + cmd);
            response.setCmd(cmd);
            UserProcessor up = new UserProcessor(dbSession, httpSessionState);
            PendingEmailConfirmationsProcessor pendingConfirmationProcessor = new PendingEmailConfirmationsProcessor(dbSession, httpSessionState);

            String account = request.getAccountName();
            String userName = request.getUserName();
            String email = request.getUserEmail();
            String role = request.getRole();
            //String tag = request.getString("tag");
            boolean isYoungerThen14 = request.getIsYoungerThen14();

            User user = httpSessionState.isUserLoggedIn() ? up.getUser(httpSessionState.getUserId()) : null;
            if ( user == null ) {
                return UtilForREST.makeBaseResponseForError(Key.USER_ERROR_NOT_LOGGED_IN, httpSessionState, this.brickCommunicator);
            } else {
                if ( !user.getAccount().equals(account) || user.getUserGroup() != null ) {
                    return UtilForREST.makeBaseResponseForError(Key.USER_UPDATE_ERROR_ACCOUNT_WRONG, httpSessionState, this.brickCommunicator);
                } else {
                    String oldEmail = user.getEmail();
                    up.updateUser(account, userName, role, email, null, isYoungerThen14);
                    if ( this.isPublicServer && !oldEmail.equals(email) && up.succeeded() ) {
                        String lang = request.getLanguage();
                        PendingEmailConfirmations confirmation = pendingConfirmationProcessor.createEmailConfirmation(account);
                        sendActivationMail(up, confirmation.getUrlPostfix(), account, email, lang, isYoungerThen14);
                        up.deactivateAccount(user.getId());
                    }
                    UtilForREST.addResultInfo(response, up);
                }
            }
            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            ClientUser.LOG.error("Exception. Error ticket: " + errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, this.brickCommunicator); // TODO: refactor error ticket .append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/changePassword")
    public Response changePassword(@OraData DbSession dbSession, FullRestRequest fullRequest) throws Exception {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRequest, true);
        try {
            BaseResponse response = BaseResponse.make();
            ChangePasswordRequest request = ChangePasswordRequest.make(fullRequest.getData());
            String cmd = "changePassword";
            ClientUser.LOG.info("command is: " + cmd);
            response.setCmd(cmd);
            UserProcessor up = new UserProcessor(dbSession, httpSessionState);

            String account = request.getAccountName();
            String oldPassword = request.getOldPassword();
            String newPassword = request.getNewPassword();
            up.updatePassword(account, oldPassword, newPassword);
            UtilForREST.addResultInfo(response, up);
            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            ClientUser.LOG.error("Exception. Error ticket: " + errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, this.brickCommunicator); // TODO: refactor error ticket .append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    /**
     * if the user forgot his/her password, he/she is asked for the mail address for password recovery. The REST-service <b>passwordRecovery</b> is responsible
     * to store the user request for recovery in the database and send mail to the user. If the user follows the link there, the REST-service
     * <b>isResetPasswordLinkExpired</b> is called. It either allows the user to type a new password or rejects the attempt, if the link is expired. The new
     * password is given to the backend by calling the REST-service <b>resetPassword</b> <b>This is the first REST-service passwordRecovery</b>
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/passwordRecovery")
    public Response passwordRecovery(@OraData DbSession dbSession, FullRestRequest fullRequest) throws Exception {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRequest, true);
        try {
            BaseResponse response = BaseResponse.make();
            Map<String, String> responseParameters = new HashMap<>();
            PasswordRecoveryRequest request = PasswordRecoveryRequest.make(fullRequest.getData());
            String cmd = "passwordRecovery";
            ClientUser.LOG.info("command is: " + cmd);
            response.setCmd(cmd);
            UserProcessor up = new UserProcessor(dbSession, httpSessionState);
            LostPasswordProcessor lostPasswordProcessor = new LostPasswordProcessor(dbSession, httpSessionState);

            String lostEmail = request.getLostEmail();
            String lang = request.getLanguage();
            User user = up.getUserByEmail(lostEmail);
            UtilForREST.addResultInfo(response, up);
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
                    up.setStatus(ProcessorStatus.SUCCEEDED, Key.USER_PASSWORD_RECOVERY_SENT_MAIL_SUCCESS, responseParameters);
                } catch ( MessagingException e ) {
                    up.setStatus(ProcessorStatus.FAILED, Key.USER_PASSWORD_RECOVERY_SENT_MAIL_FAIL, responseParameters);
                }
            }
            UtilForREST.addResultInfo(response, up);
            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            ClientUser.LOG.error("Exception. Error ticket: " + errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, this.brickCommunicator); // TODO: refactor error ticket .append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    /**
     * if the user forgot his/her password, he/she is asked for the mail address for password recovery. The REST-service <b>passwordRecovery</b> is responsible
     * to store the user request for recovery in the database and send mail to the user. If the user follows the link there, the REST-service
     * <b>isResetPasswordLinkExpired</b> is called. It either allows the user to type a new password or rejects the attempt, if the link is expired. The new
     * password is given to the backend by calling the REST-service <b>resetPassword</b> <b>This is the second REST-service isResetPasswordLinkExpired</b>
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/isResetPasswordLinkExpired")
    public Response isResetPasswordLinkExpired(@OraData DbSession dbSession, FullRestRequest fullRequest) throws Exception {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRequest, true);
        try {
            IsResetPasswordLinkExpiredResponse response = IsResetPasswordLinkExpiredResponse.make();
            Map<String, String> responseParameters = new HashMap<>();
            ResetPasswordRequest request = ResetPasswordRequest.make(fullRequest.getData());
            String cmd = "isResetPasswordLinkExpired";
            ClientUser.LOG.info("command is: " + cmd);
            response.setCmd(cmd);
            UserProcessor up = new UserProcessor(dbSession, httpSessionState);
            LostPasswordProcessor lostPasswordProcessor = new LostPasswordProcessor(dbSession, httpSessionState);

            String resetPasswordLink = request.getResetPasswordLink();
            LostPassword lostPassword = lostPasswordProcessor.loadLostPassword(resetPasswordLink);
            boolean recoverySuccessful;
            if ( lostPassword != null ) {
                // check for expiration of the lost-password-link
                Date currentTime = new Date();
                recoverySuccessful = (currentTime.getTime() - lostPassword.getCreated().getTime()) / 3600000.0 <= 24;
            } else {
                recoverySuccessful = false;
            }
            Key statusKey = recoverySuccessful ? Key.SERVER_SUCCESS : Key.USER_PASSWORD_RECOVERY_EXPIRED_URL;
            up.setStatus(ProcessorStatus.SUCCEEDED, statusKey, responseParameters);
            if ( recoverySuccessful ) {
            }
            response.setResetPasswordLinkExpired(recoverySuccessful);
            UtilForREST.addResultInfo(response, up);
            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            ClientUser.LOG.error("Exception. Error ticket: " + errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, this.brickCommunicator); // TODO: refactor error ticket .append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    /**
     * if the user forgot his/her password, he/she is asked for the mail address for password recovery. The REST-service <b>passwordRecovery</b> is responsible
     * to store the user request for recovery in the database and send mail to the user. If the user follows the link there, the REST-service
     * <b>isResetPasswordLinkExpired</b> is called. It either allows the user to type a new password or rejects the attempt, if the link is expired. The new
     * password is given to the backend by calling the REST-service <b>resetPassword</b> <b>This is the third REST-service resetPassword</b>
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/resetPassword")
    public Response resetPassword(@OraData DbSession dbSession, FullRestRequest fullRequest) throws Exception {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRequest, true);
        try {
            BaseResponse response = BaseResponse.make();
            ResetPasswordRequest request = ResetPasswordRequest.make(fullRequest.getData());
            String cmd = "resetPassword";
            ClientUser.LOG.info("command is: " + cmd);
            response.setCmd(cmd);
            UserProcessor up = new UserProcessor(dbSession, httpSessionState);
            LostPasswordProcessor lostPasswordProcessor = new LostPasswordProcessor(dbSession, httpSessionState);

            String resetPasswordLink = request.getResetPasswordLink();
            String newPassword = request.getNewPassword();
            LostPassword lostPassword = lostPasswordProcessor.loadLostPassword(resetPasswordLink);
            if ( lostPassword != null ) {
                up.resetPassword(lostPassword.getUserID(), newPassword);
            }
            if ( up.getMessage() == Key.USER_UPDATE_SUCCESS ) {
                lostPasswordProcessor.deleteLostPassword(resetPasswordLink);
            }
            UtilForREST.addResultInfo(response, up);
            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            ClientUser.LOG.error("Exception. Error ticket: " + errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, this.brickCommunicator); // TODO: refactor error ticket .append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/activateUser")
    public Response activateUser(@OraData DbSession dbSession, FullRestRequest fullRequest) throws Exception {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRequest, true);
        try {
            BaseResponse response = BaseResponse.make();
            ActivateUserRequest request = ActivateUserRequest.make(fullRequest.getData());
            String cmd = "activateUser";
            ClientUser.LOG.info("command is: " + cmd);
            response.setCmd(cmd);
            UserProcessor up = new UserProcessor(dbSession, httpSessionState);
            PendingEmailConfirmationsProcessor pendingConfirmationProcessor = new PendingEmailConfirmationsProcessor(dbSession, httpSessionState);

            String userActivationLink = request.getUserActivationLink();
            PendingEmailConfirmations confirmation = pendingConfirmationProcessor.loadConfirmation(userActivationLink);
            if ( confirmation != null ) {
                up.activateAccount(confirmation.getUserID());
            }
            if ( up.getMessage() == Key.USER_ACTIVATION_SUCCESS ) {
                pendingConfirmationProcessor.deleteEmailConfirmation(userActivationLink);
                UtilForREST.addResultInfo(response, up);
                return UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
            } else {
                return UtilForREST.makeBaseResponseForError(Key.USER_ACTIVATION_INVALID_URL, httpSessionState, this.brickCommunicator);
            }
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            ClientUser.LOG.error("Exception. Error ticket: " + errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, this.brickCommunicator); // TODO: refactor error ticket .append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/resendActivation")
    public Response resendActivation(@OraData DbSession dbSession, FullRestRequest fullRequest) throws Exception {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRequest, true);
        try {
            BaseResponse response = BaseResponse.make();
            ResendActivationRequest request = ResendActivationRequest.make(fullRequest.getData());
            String cmd = "resendActivation";
            ClientUser.LOG.info("command is: " + cmd);
            response.setCmd(cmd);
            UserProcessor up = new UserProcessor(dbSession, httpSessionState);
            PendingEmailConfirmationsProcessor pendingConfirmationProcessor = new PendingEmailConfirmationsProcessor(dbSession, httpSessionState);

            String account = request.getAccountName();
            String lang = request.getLanguage();
            User user = up.getUser(account);
            if ( this.isPublicServer && user != null && !user.getEmail().equals("") ) {
                PendingEmailConfirmations confirmation = pendingConfirmationProcessor.createEmailConfirmation(account);
                // TODO ask here again for the age
                sendActivationMail(up, confirmation.getUrlPostfix(), account, user.getEmail(), lang, false);
            }
            UtilForREST.addResultInfo(response, up);
            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            ClientUser.LOG.error("Exception. Error ticket: " + errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, this.brickCommunicator); // TODO: refactor error ticket .append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/deleteUser")
    public Response deleteUser(@OraData DbSession dbSession, FullRestRequest fullRequest) throws Exception {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRequest, true);
        try {
            BaseResponse response = BaseResponse.make();
            DeleteUserRequest request = DeleteUserRequest.make(fullRequest.getData());
            String cmd = "deleteUser";
            ClientUser.LOG.info("command is: " + cmd);
            response.setCmd(cmd);
            UserProcessor up = new UserProcessor(dbSession, httpSessionState);

            String account = request.getAccountName();
            String password = request.getPassword();
            up.deleteUser(account, password);
            Statistics.info("UserDelete", "success", up.succeeded());
            UtilForREST.addResultInfo(response, up);
            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            ClientUser.LOG.error("Exception. Error ticket: " + errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, this.brickCommunicator); // TODO: refactor error ticket .append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getStatusText")
    public Response getStatusText(FullRestRequest fullRequest) throws Exception {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRequest, true);
        try {
            GetStatusTextResponse response = GetStatusTextResponse.make();
            String cmd = "getStatusText";
            ClientUser.LOG.info("command is: " + cmd);
            response.setCmd(cmd);

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Long current = timestamp.getTime() / 1000L;
            if ( current > statusTextTimestamp ) {
                statusText[0] = "";
                statusText[1] = "";
            }
            JSONArray statusTextJSON = new JSONArray(Arrays.asList(statusText));
            response.setStatustext(statusTextJSON);
            response.setRc("ok");
            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
        } catch ( Exception e ) {
            String errorTicketId = Util.getErrorTicketId();
            ClientUser.LOG.error("Exception. Error ticket: " + errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, this.brickCommunicator); // TODO: refactor error ticket .append("parameters", errorTicketId);
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/setStatusText")
    public Response setStatusText(FullRestRequest fullRequest) throws Exception {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRequest, true);
        try {
            BaseResponse response = BaseResponse.make();
            final int userId = httpSessionState.getUserId();
            SetStatusTextRequest request = SetStatusTextRequest.make(fullRequest.getData());
            String cmd = "setStatusText";
            ClientUser.LOG.info("command is: " + cmd);
            response.setCmd(cmd);

            if ( userId == 1 ) {
                statusText[0] = request.getEnglish();
                statusText[1] = request.getGerman();
                statusTextTimestamp = request.getTimestamp();
                response.setRc("ok");
                return UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);
            } else {
                ClientUser.LOG.error("Invalid command: " + cmd);
                return UtilForREST.makeBaseResponseForError(Key.COMMAND_INVALID, httpSessionState, this.brickCommunicator);
            }
        } catch ( Exception e ) {
            String errorTicketId = Util.getErrorTicketId();
            ClientUser.LOG.error("Exception. Error ticket: " + errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, this.brickCommunicator); // TODO: refactor error ticket .append("parameters", errorTicketId);
        }
    }

    private void sendActivationMail(UserProcessor up, String urlPostfix, String account, String email, String lang, boolean isYoungerThen14) throws Exception {
        Map<String, String> responseParameters = new HashMap<>();
        String[] body =
            {
                account,
                urlPostfix
            };
        try {
            this.mailManagement.send(email, "activate", body, lang, isYoungerThen14);
            up.setStatus(ProcessorStatus.SUCCEEDED, Key.USER_ACTIVATION_SENT_MAIL_SUCCESS, responseParameters);
        } catch ( Exception e ) {
            up.setStatus(ProcessorStatus.FAILED, Key.USER_ACTIVATION_SENT_MAIL_FAIL, responseParameters);
        }
    }
}