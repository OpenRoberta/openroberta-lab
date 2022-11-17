package de.fhg.iais.roberta.javaServer.restServices.all.controller;

import java.util.ArrayList;
import java.util.List;

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

import de.fhg.iais.roberta.generated.restEntities.BaseResponse;
import de.fhg.iais.roberta.generated.restEntities.ChangeUserGroupRequest;
import de.fhg.iais.roberta.generated.restEntities.FullRestRequest;
import de.fhg.iais.roberta.generated.restEntities.UpdateUserGroupMemberAccountRequest;
import de.fhg.iais.roberta.generated.restEntities.UserGroupListResponse;
import de.fhg.iais.roberta.generated.restEntities.UserGroupMembersRequest;
import de.fhg.iais.roberta.generated.restEntities.UserGroupRequest;
import de.fhg.iais.roberta.generated.restEntities.UserGroupResponse;
import de.fhg.iais.roberta.generated.restEntities.UserGroupsRequest;
import de.fhg.iais.roberta.javaServer.provider.OraData;
import de.fhg.iais.roberta.persistence.UserGroupProcessor;
import de.fhg.iais.roberta.persistence.UserProcessor;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.bo.UserGroup;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.robotCommunication.RobotCommunicator;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.ServerProperties;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.UtilForREST;

@Path("/userGroup")
public class UserGroupController {

    private static final Logger LOG = LoggerFactory.getLogger(UserGroupController.class);

    private final RobotCommunicator brickCommunicator;

    private final boolean isPublicServer;

    @Inject
    public UserGroupController(RobotCommunicator brickCommunicator, ServerProperties serverProperties) {
        this.brickCommunicator = brickCommunicator;
        this.isPublicServer = serverProperties.getBooleanProperty("server.public");
    }

    /**
     * Returns all information about a single user group. It requires a user to be logged in. The end point expects a UserGroupRequest with the fields: -
     * groupName The name of the user group The user group is determined by the currently logged in user (either by being its owner of by being a member of it)
     * and the provided group name.
     *
     * @param dbSession The database session
     * @param fullRequest The request data
     * @return A UserGroupResponse with a JSONObject of the user group under the key "userGroup"
     * @throws Exception
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getUserGroup")
    public Response getUserGroup(@OraData DbSession dbSession, FullRestRequest fullRequest) throws Exception {
        LOG.info("command is: getUserGroup");
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRequest, true);
        try {
            UserGroupResponse response = UserGroupResponse.make();
            UserGroupRequest request = UserGroupRequest.make(fullRequest.getData());

            UserProcessor userProcessor = new UserProcessor(dbSession, httpSessionState.getUserId());
            UserGroupProcessor userGroupProcessor = new UserGroupProcessor(dbSession, httpSessionState.getUserId(), this.isPublicServer);

            User loggedInUser = httpSessionState.isUserLoggedIn() ? userProcessor.getUser(httpSessionState.getUserId()) : null;
            if ( loggedInUser == null ) {
                return UtilForREST.makeBaseResponseForError(Key.USER_ERROR_NOT_LOGGED_IN, httpSessionState, this.brickCommunicator);
            }

            String groupName = request.getGroupName();

            UserGroup userGroup = userGroupProcessor.getGroup(groupName, loggedInUser, false);

            if ( !userGroupProcessor.succeeded() ) {
                return UtilForREST.makeBaseResponseForError(userGroupProcessor.getMessage(), httpSessionState, this.brickCommunicator);
            }

            response.setUserGroup(userGroup.toListJSON());
            UtilForREST.addResultInfo(response, userGroupProcessor);
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

    /**
     * Returns a list of all user groups of the currently logged in user. It requires a user to be logged in. The end point expects a BaseRequest with no
     * special fields. All user groups that the currently logged in user owns are returned.
     *
     * @param dbSession The database session
     * @param fullRequest The request data
     * @return A UserGroupListResponse with a JSONArray of JSONObjects, containing the data of the user groups under the key "userGroups"
     * @throws Exception
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getUserGroupList")
    public Response getUserGroupListForUser(@OraData DbSession dbSession, FullRestRequest fullRequest) throws Exception {
        LOG.info("command is getUserGroupList");
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRequest, true);

        try {
            UserGroupListResponse response = UserGroupListResponse.make();

            UserProcessor userProcessor = new UserProcessor(dbSession, httpSessionState.getUserId());
            UserGroupProcessor userGroupProcessor = new UserGroupProcessor(dbSession, httpSessionState.getUserId(), this.isPublicServer);

            User loggedInUser = httpSessionState.isUserLoggedIn() ? userProcessor.getUser(httpSessionState.getUserId()) : null;
            if ( loggedInUser == null ) {
                return UtilForREST.makeBaseResponseForError(Key.USER_ERROR_NOT_LOGGED_IN, httpSessionState, this.brickCommunicator);
            }

            List<UserGroup> userGroups = userGroupProcessor.getGroupsByOwner(loggedInUser);

            if ( !userGroupProcessor.succeeded() ) {
                return UtilForREST.makeBaseResponseForError(userGroupProcessor.getMessage(), httpSessionState, this.brickCommunicator);
            }

            JSONArray userGroupList = new JSONArray();
            for ( UserGroup userGroup : userGroups ) {
                userGroupList.put(userGroup.toListJSON());
            }
            response.setUserGroups(userGroupList);
            UtilForREST.addResultInfo(response, userGroupProcessor);
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

    /**
     * Creates a user group. It requires a user to be logged in. The end point expects a ChangeUserGroupRequest with the fields: - groupName The name the new
     * user group shall have - groupMemberCount The number of members the user group shall initially be generated with. The newly created user group will be set
     * with the currently logged in user as its owner.
     *
     * @param dbSession The database session
     * @param fullRequest The request data
     * @return A UserGroupResponse with a JSONObject, containing the data of the newly created user group under the key "userGroup"
     * @throws Exception
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/createUserGroup")
    public Response createUserGroup(@OraData DbSession dbSession, FullRestRequest fullRequest) throws Exception {
        LOG.info("command is createUserGroup");
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRequest, true);
        try {
            UserGroupResponse response = UserGroupResponse.make();
            ChangeUserGroupRequest request = ChangeUserGroupRequest.make(fullRequest.getData());

            UserProcessor userProcessor = new UserProcessor(dbSession, httpSessionState.getUserId());
            UserGroupProcessor userGroupProcessor = new UserGroupProcessor(dbSession, httpSessionState.getUserId(), this.isPublicServer);

            User loggedInUser = httpSessionState.isUserLoggedIn() ? userProcessor.getUser(httpSessionState.getUserId()) : null;
            if ( loggedInUser == null ) {
                return UtilForREST.makeBaseResponseForError(Key.USER_ERROR_NOT_LOGGED_IN, httpSessionState, this.brickCommunicator);
            }

            String groupName = request.getGroupName();
            List<String> initialMembers = request.getGroupMemberNames();

            UserGroup userGroup = userGroupProcessor.createGroup(groupName, loggedInUser, initialMembers);
            if ( !userGroupProcessor.succeeded() ) {
                return UtilForREST.makeBaseResponseForError(userGroupProcessor.getMessage(), httpSessionState, this.brickCommunicator);
            }

            response.setUserGroup(userGroup.toListJSON());
            UtilForREST.addResultInfo(response, userGroupProcessor);
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

    /**
     * Deletes all specified user groups. It requires a user to be logged in. The end point expects a UserGroupsRequest with the fields: - groupNames A
     * JSONArray of Strings with the names of the user groups that shall be deleted. The user groups is determined by the provided user group names and the
     * currently logged in user, who needs to be their owner.
     *
     * @param dbSession The database session
     * @param fullRequest The request data
     * @return A BaseResponse
     * @throws Exception
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/deleteUserGroups")
    public Response deleteUserGroups(@OraData DbSession dbSession, FullRestRequest fullRequest) throws Exception {
        LOG.info("command is deleteUserGroup");
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRequest, true);
        try {
            BaseResponse response = BaseResponse.make();
            UserGroupsRequest request = UserGroupsRequest.make(fullRequest.getData());

            UserProcessor userProcessor = new UserProcessor(dbSession, httpSessionState.getUserId());
            UserGroupProcessor userGroupProcessor = new UserGroupProcessor(dbSession, httpSessionState.getUserId(), this.isPublicServer);

            User loggedInUser = httpSessionState.isUserLoggedIn() ? userProcessor.getUser(httpSessionState.getUserId()) : null;
            if ( loggedInUser == null ) {
                return UtilForREST.makeBaseResponseForError(Key.USER_ERROR_NOT_LOGGED_IN, httpSessionState, this.brickCommunicator);
            }

            List<String> groupNames = request.getGroupNames();
            userGroupProcessor.deleteGroups(groupNames, loggedInUser, true);

            UtilForREST.addResultInfo(response, userGroupProcessor);
            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, null);

        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: {}", errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, null); // TODO: redesign error ticker number and add then: append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    /**
     * Adds new members to a user group. It requires a user to be logged in. The end point expects a ChangeUserGroupRequest with the fields: - groupName The
     * name the user group, for which new members shall be generated. - groupMemberCount The number of members, that shall additionally be generated. The
     * currently logged in user must be the owner of the specified user group.
     *
     * @param dbSession The database session
     * @param fullRequest The request data
     * @return A UserGroupResponse with a JSONObject, containing the new full data of the user group under the key "userGroup"
     * @throws Exception
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/addGroupMembers")
    public Response addGroupMembers(@OraData DbSession dbSession, FullRestRequest fullRequest) throws Exception {
        LOG.info("command is addGroupMembers");
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRequest, true);

        try {
            UserGroupResponse response = UserGroupResponse.make();
            ChangeUserGroupRequest request = ChangeUserGroupRequest.make(fullRequest.getData());

            UserProcessor userProcessor = new UserProcessor(dbSession, httpSessionState.getUserId());
            UserGroupProcessor userGroupProcessor = new UserGroupProcessor(dbSession, httpSessionState.getUserId(), this.isPublicServer);

            User loggedInUser = httpSessionState.isUserLoggedIn() ? userProcessor.getUser(httpSessionState.getUserId()) : null;
            if ( loggedInUser == null ) {
                return UtilForREST.makeBaseResponseForError(Key.USER_ERROR_NOT_LOGGED_IN, httpSessionState, this.brickCommunicator);
            }

            String groupName = request.getGroupName();
            List<String> newMemberNames = request.getGroupMemberNames();

            UserGroup userGroup = userGroupProcessor.getGroup(groupName, loggedInUser, false);
            if ( !userGroupProcessor.succeeded() ) {
                return UtilForREST.makeBaseResponseForError(userGroupProcessor.getMessage(), httpSessionState, this.brickCommunicator);
            }

            userGroupProcessor.addMembersToUserGroup(userGroup, newMemberNames);
            if ( !userGroupProcessor.succeeded() ) {
                return UtilForREST.makeBaseResponseForError(userGroupProcessor.getMessage(), httpSessionState, this.brickCommunicator);
            }

            response.setUserGroup(userGroup.toListJSON());
            UtilForREST.addResultInfo(response, userGroupProcessor);
            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, null);

        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: {}", errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, null); // TODO: redesign error ticker number and add then: append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/updateMemberAccount")
    public Response updateMemberAccount(@OraData DbSession dbSession, FullRestRequest fullRequest) throws Exception {
        LOG.info("command is updateMemberAccount");
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRequest, true);

        try {
            BaseResponse response = BaseResponse.make();
            UpdateUserGroupMemberAccountRequest request = UpdateUserGroupMemberAccountRequest.make(fullRequest.getData());

            UserProcessor userProcessor = new UserProcessor(dbSession, httpSessionState.getUserId());
            UserGroupProcessor userGroupProcessor = new UserGroupProcessor(dbSession, httpSessionState.getUserId(), this.isPublicServer);

            User loggedInUser = httpSessionState.isUserLoggedIn() ? userProcessor.getUser(httpSessionState.getUserId()) : null;
            if ( loggedInUser == null ) {
                return UtilForREST.makeBaseResponseForError(Key.USER_ERROR_NOT_LOGGED_IN, httpSessionState, this.brickCommunicator);
            }

            String groupName = request.getGroupName().trim();
            String memberAccount = request.getCurrentGroupMemberAccount().trim();
            String newMemberAccount = request.getNewGroupMemberAccount().trim();
            if ( newMemberAccount.equals("") ) {
                return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, this.brickCommunicator);
            }
            UserGroup userGroup = userGroupProcessor.getGroup(groupName, loggedInUser, false);
            if ( userGroup == null ) {
                return UtilForREST.makeBaseResponseForError(userGroupProcessor.getMessage(), httpSessionState, this.brickCommunicator);
            }
            if ( memberAccount.equals("") ) {
                List<String> memberAsList = new ArrayList<String>(1);
                memberAsList.add(newMemberAccount.trim());
                userGroupProcessor.addMembersToUserGroup(userGroup, memberAsList);
            } else {
                User member = userProcessor.getUser(userGroup, memberAccount.trim());

                if ( member == null ) {
                    return UtilForREST.makeBaseResponseForError(userProcessor.getMessage(), httpSessionState, this.brickCommunicator);
                }

                userGroupProcessor.updateMemberAccount(member, newMemberAccount);
            }

            UtilForREST.addResultInfo(response, userGroupProcessor);
            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);

        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: {}", errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, null); // TODO: redesign error ticker number and add then: append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    /**
     * Deletes all specified members of the specified user group. It requires a user to be logged in. The end point expects a UserGroupMembersRequest with the
     * fields: - groupName The name the user group, for which new members shall be deleted. - groupMemberAccounts A JSONArray of Strings with the names of the
     * members that shall be deleted. The currently logged in user must be the owner of the specified user group.
     *
     * @param dbSession The database session
     * @param fullRequest The request data
     * @return A BaseResponse
     * @throws Exception
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/deleteGroupMembers")
    public Response deleteGroupMembers(@OraData DbSession dbSession, FullRestRequest fullRequest) throws Exception {
        LOG.info("command is deleteGroupMembers");
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(LOG, fullRequest, true);

        try {
            BaseResponse response = BaseResponse.make();
            UserGroupMembersRequest request = UserGroupMembersRequest.make(fullRequest.getData());

            UserProcessor userProcessor = new UserProcessor(dbSession, httpSessionState.getUserId());
            UserGroupProcessor userGroupProcessor = new UserGroupProcessor(dbSession, httpSessionState.getUserId(), this.isPublicServer);

            User loggedInUser = httpSessionState.isUserLoggedIn() ? userProcessor.getUser(httpSessionState.getUserId()) : null;
            if ( loggedInUser == null ) {
                return UtilForREST.makeBaseResponseForError(Key.USER_ERROR_NOT_LOGGED_IN, httpSessionState, this.brickCommunicator);
            }

            String groupName = request.getGroupName();
            List<String> groupMemberAccounts = request.getGroupMemberAccounts();

            UserGroup userGroup = userGroupProcessor.getGroup(groupName, loggedInUser, false);
            if ( userGroup == null ) {
                return UtilForREST.makeBaseResponseForError(userGroupProcessor.getMessage(), httpSessionState, this.brickCommunicator);
            }

            userProcessor.deleteUserGroupMembers(userGroup, groupMemberAccounts);

            UtilForREST.addResultInfo(response, userGroupProcessor);
            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);

        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: {}", errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, null); // TODO: redesign error ticker number and add then: append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }

    /**
     * Resets the passwords of all specified members of the specified user group to its default value, which is their account name. It requires a user to be
     * logged in. The end point expects a UserGroupMembersRequest with the fields: - groupName The name the user group, for which's specified members the
     * passwords shall be reseted. - groupMemberAccounts A JSONArray of Strings with the names of the members that shall get their password reseted. The
     * currently logged in user must be the owner of the specified user group.
     *
     * @param dbSession The database session
     * @param fullRequest The request data
     * @return A BaseResponse
     * @throws Exception
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/setUserGroupMemberDefaultPasswords")
    public Response setUserGroupMemberDefaultPasswords(@OraData DbSession dbSession, FullRestRequest fullRequest) throws Exception {
        HttpSessionState httpSessionState = UtilForREST.handleRequestInit(dbSession, LOG, fullRequest, true);
        try {
            BaseResponse response = BaseResponse.make();
            UserGroupMembersRequest request = UserGroupMembersRequest.make(fullRequest.getData());

            UserProcessor userProcessor = new UserProcessor(dbSession, httpSessionState.getUserId());
            UserGroupProcessor userGroupProcessor = new UserGroupProcessor(dbSession, httpSessionState.getUserId(), this.isPublicServer);

            User loggedInUser = httpSessionState.isUserLoggedIn() ? userProcessor.getUser(httpSessionState.getUserId()) : null;
            if ( loggedInUser == null ) {
                return UtilForREST.makeBaseResponseForError(Key.USER_ERROR_NOT_LOGGED_IN, httpSessionState, this.brickCommunicator);
            }

            String groupName = request.getGroupName();
            List<String> groupMemberAccounts = request.getGroupMemberAccounts();

            UserGroup userGroup = userGroupProcessor.getGroup(groupName, loggedInUser, false);
            if ( userGroup == null ) {
                return UtilForREST.makeBaseResponseForError(userGroupProcessor.getMessage(), httpSessionState, this.brickCommunicator);
            }

            User tmpUser;
            for ( String memberAccountName : groupMemberAccounts ) {
                tmpUser = userProcessor.getUser(userGroup, memberAccountName);
                if ( tmpUser != null ) {
                    //Set the userName as password. If the user does not exists, do not abort the whole request
                    userProcessor.resetPassword(tmpUser.getId(), tmpUser.getAccount());
                    if ( !userProcessor.succeeded() ) {
                        //If resetting one of the users passwords failed there is likely a server issue, so stop execution
                        LOG.info("Could not reset the password of user \"" + tmpUser.getAccount() + "\". Call aborted. DB rolled back");
                        dbSession.rollback();
                        break;
                    }
                }
            }

            UtilForREST.addResultInfo(response, userGroupProcessor);
            return UtilForREST.responseWithFrontendInfo(response, httpSessionState, this.brickCommunicator);

        } catch ( Exception e ) {
            dbSession.rollback();
            String errorTicketId = Util.getErrorTicketId();
            LOG.error("Exception. Error ticket: {}", errorTicketId, e);
            return UtilForREST.makeBaseResponseForError(Key.SERVER_ERROR, httpSessionState, null); // TODO: redesign error ticker number and add then: append("parameters", errorTicketId);
        } finally {
            if ( dbSession != null ) {
                dbSession.close();
            }
        }
    }
}
