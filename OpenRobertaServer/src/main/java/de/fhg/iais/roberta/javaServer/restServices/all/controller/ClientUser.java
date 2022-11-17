package de.fhg.iais.roberta.javaServer.restServices.all.controller;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

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

    /**
     * it is possible, that our frontend sends multiple overlapping create user requests to the server. We experience
     * data base dead locks, that seem to be triggered by this. The serialization feature (module WRAP) seems not to
     * help in this situation. Until this problem is fixed in the frontend, the following strategy is used using the
     * concurrent map declared below:
     * <ul>
     *     <li>the create user REST-call arrives at the server</li>
     *     <li>if the account name of a create user request is found in the map, the REST-call is aborted</li>
     *     <li>otherwise it is saved in the map</li>
     *     <li>if the create user REST-call terminates, the account name of a create user request is removed from the map</li>
     *     <li>if an entry in the the map is older than TIMEOUT_CREATE_USER_REQUESTS, it is removed silently</li>
     * </ul>
     * - the account name of a create user request is saved in the concurrent hashmap declared below when the
     * -
     */
    private static final long TIMEOUT_CREATE_USER_REQUESTS = TimeUnit.MINUTES.toMillis(10);
    private static final ConcurrentHashMap<String, Long> mapOfOpenCreateUserRequests = new ConcurrentHashMap<>();

    private final RobotCommunicator brickCommunicator;
    private final MailManagement mailManagement;

    private final boolean isPublicServer;

    @Inject
    public ClientUser(RobotCommunicator brickCommunicator, ServerProperties serverProperties, MailManagement mailManagement) {
        this.brickCommunicator = brickCommunicator;
        this.mailManagement = mailManagement;
        this.isPublicServer = serverProperties.getBooleanProperty("server.public");
    }

    // TODO: static variables (they are changed!) in a REST resource are very dangerous. Refactor, i.e. remove!
    private static final String[] statusText = new String[2];
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
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(dbSession, LOG, fullRequest, true);
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
                UserProcessor up = new UserProcessor(dbSession, httpSessionState.getUserId());
                String userAccountName = request.getAccountName();
                String password = request.getPassword();

                UserGroup userGroup = null;
                String userGroupOwnerAccount = request.getUserGroupOwner();
                String userGroupName = request.getUserGroupName();

                User user;

                if ( userGroupOwnerAccount != null && userGroupName != null ) {
                    UserGroupProcessor ugp = new UserGroupProcessor(dbSession, httpSessionState.getUserId(), this.isPublicServer);

                    User userGroupOwner = up.getStandardUser(userGroupOwnerAccount);
                    if ( userGroupOwner == null ) {
                        UtilForREST.addResultInfo(response, up);
                        return UtilForREST.makeBaseResponseForError(Key.GROUP_GET_ONE_ERROR_NOT_FOUND, httpSessionState, this.brickCommunicator);
                    }

                    userGroup = ugp.getGroup(userGroupName, userGroupOwner, true);
                    if ( userGroup == null ) {
                        return UtilForREST.makeBaseResponseForError(ugp.getMessage(), httpSessionState, this.brickCommunicator);
                    }

                    user = up.getGroupUserForLogin(userGroup, userAccountName, password);
                } else {
                    user = up.getStandardUserForLogin(userAccountName, password);
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
                    dbSession.addToLog("login", "setLastLogin()");
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

    /**
     * used to check wether or not the current user is logged in
     */
    @POST
    @Path("/loggedInCheck")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loggedInCheck(@OraData DbSession dbSession, FullRestRequest request) {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(dbSession, LOG, request, true);
        try {
            if ( !httpSessionState.isUserLoggedIn() ) {
                LOG.error("Unauthorized export request");
                return UtilForREST.makeBaseResponseForError(Key.USER_ERROR_NOT_LOGGED_IN, httpSessionState, null);
            }

            BaseResponse response = BaseResponse.make(); // baseresponse
            UtilForREST.addSuccessInfo(response, Key.SERVER_SUCCESS);
            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, null);
        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: {}", errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, null);
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
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(dbSession, LOG, fullRequest, true);
        try {
            GetUserResponse response = GetUserResponse.make();
            String cmd = "getUser";
            ClientUser.LOG.info("command is: " + cmd);
            response.setCmd(cmd);

            if ( httpSessionState.isUserLoggedIn() ) {
                UserProcessor up = new UserProcessor(dbSession, httpSessionState.getUserId());
                User user = up.getUser(httpSessionState.getUserId());
                UtilForREST.addResultInfo(response, up);
                if ( user != null ) {
                    int id = user.getId();
                    String account = user.getAccount();
                    String userName = user.getUserName();
                    String email = user.getEmail();
                    email = email == null ? "" : email; // because email is a required field in the response. Null occurs for group members only.
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
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(dbSession, LOG, fullRequest, true);
        String account = "to:be:replaced:later";
        try {
            BaseResponse response = BaseResponse.make();
            UserRequest request = UserRequest.make(fullRequest.getData());
            String cmd = "createUser";
            LOG.info("command is: " + cmd);
            response.setCmd(cmd);

            // +++ avoid possible deadlocks (See doc at the beginning of this class)
            long removeBarrier = new Date().getTime() - TIMEOUT_CREATE_USER_REQUESTS;
            Set<String> accountsToRemove = new HashSet<>();
            for ( Map.Entry<String, Long> accountTime : mapOfOpenCreateUserRequests.entrySet() ) {
                if ( accountTime.getValue() < removeBarrier ) {
                    accountsToRemove.add(accountTime.getKey());
                }
            }
            for ( String accountToRemove : accountsToRemove ) {
                LOG.error("a create user request for " + accountToRemove + " is outdated and removed - this should NEVER happen");
                mapOfOpenCreateUserRequests.remove(accountToRemove);
            }
            account = request.getAccountName();
            Long timeOfEarlierAttempt = mapOfOpenCreateUserRequests.putIfAbsent(account, new Date().getTime());
            if ( timeOfEarlierAttempt != null ) {
                LOG.error("a create user request for " + account + " is rejected, because it is duplicate - this should NEVER happen");
                account = "no:valid:key:for:OPEN_CREATE_USER_REQUESTS"; // ... thus, the entry for account continues to be rejected
                return UtilForREST.makeBaseResponseForError(Key.COMMAND_INVALID, httpSessionState, this.brickCommunicator);
            }
            // --- avoid possible deadlocks (See doc at the beginning of this class)

            UserProcessor up = new UserProcessor(dbSession, httpSessionState.getUserId());
            String password = request.getPassword();
            String email = request.getUserEmail();
            email = email == null ? "" : email.trim();
            String userName = request.getUserName();
            String role = request.getRole();
            //String tag = request.getString("tag");
            boolean isYoungerThen14 = request.getIsYoungerThen14();
            User newUser = up.createUser(account, password, userName, role, email, null, isYoungerThen14);
            if ( up.succeeded() && newUser != null && this.isPublicServer && !email.equals("") ) {
                PendingEmailConfirmationsProcessor pendingConfirmationProcessor = new PendingEmailConfirmationsProcessor(dbSession, httpSessionState.getUserId());
                String lang = request.getLanguage();
                PendingEmailConfirmations confirmation = pendingConfirmationProcessor.createEmailConfirmation(newUser);
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
            // +++ avoid possible deadlocks (See doc at the beginning of this class)
            mapOfOpenCreateUserRequests.remove(account);
            // --- avoid possible deadlocks (See doc at the beginning of this class)
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/updateUser")
    public Response updateUser(@OraData DbSession dbSession, FullRestRequest fullRequest) throws Exception {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(dbSession, LOG, fullRequest, true);
        try {
            BaseResponse response = BaseResponse.make();
            UserRequest request = UserRequest.make(fullRequest.getData());
            String cmd = "updateUser";
            ClientUser.LOG.info("command is: " + cmd);
            response.setCmd(cmd);
            UserProcessor up = new UserProcessor(dbSession, httpSessionState.getUserId());
            PendingEmailConfirmationsProcessor pendingConfirmationProcessor = new PendingEmailConfirmationsProcessor(dbSession, httpSessionState.getUserId());
            String account = request.getAccountName();
            String userName = request.getUserName();
            String email = request.getUserEmail();
            email = email == null ? "" : email.trim();
            String role = request.getRole();
            //String tag = request.getString("tag");
            boolean isYoungerThen14 = request.getIsYoungerThen14();

            User user = httpSessionState.isUserLoggedIn() ? up.getUser(httpSessionState.getUserId()) : null;
            if ( user == null ) {
                return UtilForREST.makeBaseResponseForError(Key.USER_ERROR_NOT_LOGGED_IN, httpSessionState, this.brickCommunicator);
            } else if ( !user.getAccount().equals(account) || user.getUserGroup() != null ) {
                return UtilForREST.makeBaseResponseForError(Key.USER_UPDATE_ERROR_ACCOUNT_WRONG, httpSessionState, this.brickCommunicator);
            } else if ( !email.equals("") && !Util.isValidEmailAddress(email) ) {
                return UtilForREST.makeBaseResponseForError(Key.USER_ERROR_EMAIL_INVALID, httpSessionState, this.brickCommunicator);
            } else {
                String oldEmail = user.getEmail();
                boolean isMailChanged = !email.equals(oldEmail);
                if ( isMailChanged && !email.equals("") ) {
                    // new email: check whether already used or not
                    final User userByEmail = up.getUserByEmail(email);
                    boolean emailInUseByAnotherUser = userByEmail != null && userByEmail.getId() != user.getId();
                    if ( emailInUseByAnotherUser ) {
                        return UtilForREST.makeBaseResponseForError(Key.USER_ERROR_EMAIL_USED, httpSessionState, this.brickCommunicator);
                    }
                }
                final boolean deactivateAccount = this.isPublicServer && isMailChanged;
                up.updateUser(user, userName, role, email, null, isYoungerThen14, deactivateAccount);
                if ( deactivateAccount && up.succeeded() ) {
                    PendingEmailConfirmations confirmation = pendingConfirmationProcessor.createEmailConfirmation(user);
                    sendActivationMail(up, confirmation.getUrlPostfix(), account, email, request.getLanguage(), isYoungerThen14);
                }
                UtilForREST.addResultInfo(response, up);
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
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(dbSession, LOG, fullRequest, true);
        try {
            BaseResponse response = BaseResponse.make();
            ChangePasswordRequest request = ChangePasswordRequest.make(fullRequest.getData());
            String cmd = "changePassword";
            ClientUser.LOG.info("command is: " + cmd);
            response.setCmd(cmd);
            UserProcessor up = new UserProcessor(dbSession, httpSessionState.getUserId());

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
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(dbSession, LOG, fullRequest, true);
        try {
            BaseResponse response = BaseResponse.make();
            Map<String, String> responseParameters = new HashMap<>();
            PasswordRecoveryRequest request = PasswordRecoveryRequest.make(fullRequest.getData());
            String cmd = "passwordRecovery";
            ClientUser.LOG.info("command is: " + cmd);
            response.setCmd(cmd);
            UserProcessor up = new UserProcessor(dbSession, httpSessionState.getUserId());
            LostPasswordProcessor lostPasswordProcessor = new LostPasswordProcessor(dbSession, httpSessionState.getUserId());

            String lostEmail = request.getLostEmail();
            String lang = request.getLanguage();
            User user = up.getUserByEmail(lostEmail);
            UtilForREST.addResultInfo(response, up);
            if ( user != null ) {
                LostPassword lostPassword = lostPasswordProcessor.createLostPassword(user);
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
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(dbSession, LOG, fullRequest, true);
        try {
            IsResetPasswordLinkExpiredResponse response = IsResetPasswordLinkExpiredResponse.make();
            Map<String, String> responseParameters = new HashMap<>();
            ResetPasswordRequest request = ResetPasswordRequest.make(fullRequest.getData());
            String cmd = "isResetPasswordLinkExpired";
            ClientUser.LOG.info("command is: " + cmd);
            response.setCmd(cmd);
            UserProcessor up = new UserProcessor(dbSession, httpSessionState.getUserId());
            LostPasswordProcessor lostPasswordProcessor = new LostPasswordProcessor(dbSession, httpSessionState.getUserId());

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
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(dbSession, LOG, fullRequest, true);
        try {
            BaseResponse response = BaseResponse.make();
            ResetPasswordRequest request = ResetPasswordRequest.make(fullRequest.getData());
            String cmd = "resetPassword";
            ClientUser.LOG.info("command is: " + cmd);
            response.setCmd(cmd);
            UserProcessor up = new UserProcessor(dbSession, httpSessionState.getUserId());
            LostPasswordProcessor lostPasswordProcessor = new LostPasswordProcessor(dbSession, httpSessionState.getUserId());

            String resetPasswordLink = request.getResetPasswordLink();
            String newPassword = request.getNewPassword();
            LostPassword lostPassword = lostPasswordProcessor.loadLostPassword(resetPasswordLink);
            if ( lostPassword != null ) {
                up.resetPassword(lostPassword.getUserID(), newPassword);
            } else {
                up.setStatus(ProcessorStatus.FAILED, lostPasswordProcessor.getMessage(), new HashMap<>());
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
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(dbSession, LOG, fullRequest, true);
        try {
            BaseResponse response = BaseResponse.make();
            ActivateUserRequest request = ActivateUserRequest.make(fullRequest.getData());
            String cmd = "activateUser";
            ClientUser.LOG.info("command is: " + cmd);
            response.setCmd(cmd);
            UserProcessor up = new UserProcessor(dbSession, httpSessionState.getUserId());
            PendingEmailConfirmationsProcessor pendingConfirmationProcessor = new PendingEmailConfirmationsProcessor(dbSession, httpSessionState.getUserId());

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
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(dbSession, LOG, fullRequest, true);
        try {
            BaseResponse response = BaseResponse.make();
            ResendActivationRequest request = ResendActivationRequest.make(fullRequest.getData());
            String cmd = "resendActivation";
            ClientUser.LOG.info("command is: " + cmd);
            response.setCmd(cmd);
            UserProcessor up = new UserProcessor(dbSession, httpSessionState.getUserId());
            PendingEmailConfirmationsProcessor pendingConfirmationProcessor = new PendingEmailConfirmationsProcessor(dbSession, httpSessionState.getUserId());

            String account = request.getAccountName();
            String lang = request.getLanguage();
            User user = up.getStandardUser(account);
            if ( this.isPublicServer && user != null ) {
                String email = user.getEmail();
                email = email == null ? "" : email;
                if ( !email.equals("") ) {
                    PendingEmailConfirmations confirmation = pendingConfirmationProcessor.createEmailConfirmation(user);
                    // TODO ask here again for the age
                    sendActivationMail(up, confirmation.getUrlPostfix(), account, email, lang, false);
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

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/deleteUser")
    public Response deleteUser(@OraData DbSession dbSession, FullRestRequest fullRequest) throws Exception {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(dbSession, LOG, fullRequest, true);
        try {
            BaseResponse response = BaseResponse.make();
            DeleteUserRequest request = DeleteUserRequest.make(fullRequest.getData());
            String cmd = "deleteUser";
            ClientUser.LOG.info("command is: " + cmd);
            response.setCmd(cmd);
            UserProcessor up = new UserProcessor(dbSession, httpSessionState.getUserId());

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