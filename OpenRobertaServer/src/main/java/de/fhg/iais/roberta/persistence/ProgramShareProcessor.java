package de.fhg.iais.roberta.persistence;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.ProgramShare;
import de.fhg.iais.roberta.persistence.bo.Relation;
import de.fhg.iais.roberta.persistence.bo.Robot;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.bo.UserGroup;
import de.fhg.iais.roberta.persistence.bo.UserGroupProgramShare;
import de.fhg.iais.roberta.persistence.bo.UserProgramShare;
import de.fhg.iais.roberta.persistence.dao.ProgramDao;
import de.fhg.iais.roberta.persistence.dao.RobotDao;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.dao.UserGroupDao;
import de.fhg.iais.roberta.persistence.dao.UserGroupProgramShareDao;
import de.fhg.iais.roberta.persistence.dao.UserProgramShareDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.util.Key;

public class ProgramShareProcessor extends AbstractProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(ProgramShareProcessor.class);
    private static final String ENTITY_ALL = "ALL";

    public ProgramShareProcessor(DbSession dbSession, HttpSessionState httpSessionState) {
        super(dbSession, httpSessionState.getUserId());
    }

    /**
     * A program, which is identified by the quadruple (ownerId, robotId, programName, authorId) has to be shared with another user.
     * The right on the shared program may be either "WRITE", "X_WRITE" or "READ".
     * Used to show a program in the gallery. Not used to share programs with user groups or members of user groups
     *
     * @param ownerId the owner, that is the actor on which behalf this request is executed, or the gallery user, in which case the actor is the author
     * @param robotId
     * @param programName
     * @param userToShareName the account name (a String!) of the user who should get access to a program
     * @param right "WRITE" or "READ"
     */
    public void shareToUser(int ownerId, String robotName, String programName, int authorId, String userToShareName, String right) {
        UserDao userDao = new UserDao(this.dbSession);
        User owner = userDao.get(ownerId);
        User author = userDao.get(authorId);
        User userToShare = userDao.loadUser(null, userToShareName);
        this.executeUserShare(owner, robotName, programName, author, userToShare, right);
    }

    /**
     * A program, which is identified by the quadruple (ownerId, robotId, programName, authorId) has to be shared with another user.
     * The right on the shared program may be either "WRITE", "X_WRITE" or "READ".
     * Used to share a program with all type of users and user groups
     *
     * @param ownerId the owner (that is the actor on which behalf this request is executed)
     * @param robotId
     * @param programName
     * @param authorId The author of the program. Currently it is not possible to share programs, for which you are not the author.
     * @param entityToShareLabel the account name (a String!) of the user who should get access to a program, or the name of a user-group, with which's members
     *        the program shall be shared
     * @param right "WRITE" or "READ"
     * @return A set of all entities to which the specified program was being shared to because of this action with their corresponding relation
     */
    public Set<ProgramShare> shareToEntity(int ownerId, String robotName, String programName, String entityToShareLabel, String entityType, String right) {
        UserDao userDao = new UserDao(this.dbSession);
        UserGroupDao userGroupDao = new UserGroupDao(this.dbSession);

        User owner = userDao.get(ownerId);

        if ( owner == null || ownerId != getIdOfLoggedInUser() ) {
            this.setStatus(ProcessorStatus.FAILED, Key.USER_ERROR_NOT_LOGGED_IN, new HashMap<>());
            return new HashSet<>();
        }

        Set<ProgramShare> result = new HashSet<ProgramShare>();
        UserGroup userGroupToShareWith = null;
        User userToShare = null;

        if ( entityType.equals(UserGroup.class.getSimpleName()) || entityType.equals(ENTITY_ALL) ) {

            userGroupToShareWith = userGroupDao.load(entityToShareLabel, owner);

            if ( userGroupToShareWith != null ) {
                UserGroupProgramShare userGroupShare = executeUserGroupShare(owner, robotName, programName, owner, userGroupToShareWith, right);
                if ( userGroupShare != null ) {
                    result.add(userGroupShare);
                }
            }

        }

        if ( entityType.equals(User.class.getSimpleName()) || entityType.equals(ENTITY_ALL) ) {
            UserGroup ownerGroup = owner.getUserGroup();
            if ( !UserGroupProcessor.isGroupMember(entityToShareLabel) ) {
                //The userToShare is a global user
                if ( ownerGroup == null ) {
                    //Both users are global users. They are free to share to each other.
                    userToShare = userDao.loadUser(null, entityToShareLabel);
                } else if ( ownerGroup.getOwner().getAccount().equals(entityToShareLabel) ) {
                    //The only global user a group member can share to is the user group owner
                    userToShare = ownerGroup.getOwner();
                } else {
                    //Illegal state
                    userToShare = null;
                }
            } else {
                //The userToShare is a member of a user group
                if ( ownerGroup == null ) {
                    //The user must be a member of one of the groups of the owner
                    userToShare = userDao.loadUserOfGroupByGroupOwner(owner, entityToShareLabel);
                } else {
                    //The user and the owner must be in the same group
                    userToShare = userDao.loadUser(ownerGroup, entityToShareLabel);
                }
            }

            if ( owner.equals(userToShare) ) {
                this.setStatus(ProcessorStatus.FAILED, Key.USER_TO_SHARE_SAME_AS_LOGIN_USER, new HashMap<>());
                return new HashSet<>();
            }

            if ( userToShare != null ) {
                UserProgramShare userShare = executeUserShare(owner, robotName, programName, owner, userToShare, right);
                if ( userShare != null ) {
                    result.add(userShare);
                }
            }

        }

        if ( userToShare == null && userGroupToShareWith == null ) {
            Key errorKey;
            if ( entityType.equals(UserGroup.class.getSimpleName()) ) {
                errorKey = Key.GROUP_TO_SHARE_DOES_NOT_EXIST;
            } else {
                //Expect that the user wanted to share the program with another user in case of ALL
                errorKey = Key.USER_TO_SHARE_DOES_NOT_EXIST;
            }
            this.setStatus(ProcessorStatus.FAILED, errorKey, new HashMap<>());
            return new HashSet<>();
        }

        //In this case the processor status has been set in execute[User|UserGroup]Share

        return result;
    }

    /**
     * a program, which is identified by the triple (ownerId, robotId, programName) has to be shared with another user. The right on the shared program may be
     * either "WRITE" or "READ".
     *
     * @param ownerId the owner of the program. Might be the actor on which behalf this request is done.
     * @param robotId
     * @param programName
     * @param authorName The name of the author. Necessary if a share with the gallery shall be deleted, but should be the same as the owner normally
     * @param userToShareId the account id of the user, who shall no longer have access to the program. Might be the actor on which behalf this request is done.
     */
    public void shareDelete(String ownerName, String robotName, String programName, String authorName, int userToShareId) {
        UserDao userDao = new UserDao(this.dbSession);
        User owner;
        User userToShare = userDao.get(userToShareId);
        User author;

        if ( userToShare == null ) {
            this.setStatus(ProcessorStatus.FAILED, Key.USER_TO_SHARE_DOES_NOT_EXIST, new HashMap<>());
            return;
        }

        if ( UserGroupProcessor.isGroupMember(ownerName) ) {
            //Owner is a user group member
            UserGroup userToShareGroup = userToShare.getUserGroup();
            if ( userToShareGroup != null ) {
                //The user and the owner must be in the same user group
                owner = userDao.loadUser(userToShareGroup, ownerName);
            } else {
                //the user must be the owner of the owner's user group
                owner = userDao.loadUserOfGroupByGroupOwner(userToShare, ownerName);
            }
            author = owner;
        } else {
            //The owner is a global user
            owner = userDao.loadUser(null, ownerName);
            if ( ownerName.equals("Gallery") ) {
                //A user that puts a program in the gallery must be a global user
                author = userDao.loadUser(null, authorName);
            } else {
                author = owner;
            }
        }

        this.executeUserShare(owner, robotName, programName, author, userToShare, "NONE");
    }

    /**
     * a program, identified by ownerId, robotId, programName<br>
     * - is either shared with another user (right is either "WRITE" or "READ") or <br>
     * - the access right has to be removed (right is "NONE")
     *
     * @param owner
     * @param robotId
     * @param programName
     * @param userToShare
     * @param right
     * @return The touched share object of the program with the user, if one exists afterwards (also if unchanged)
     */
    private UserProgramShare executeUserShare(User owner, String robotName, String programName, User author, User userToShare, String right) {
        ProgramDao programDao = new ProgramDao(this.dbSession);
        RobotDao robotDao = new RobotDao(this.dbSession);

        Program programToShare = null;
        Key responseKey = null;
        if ( owner == null ) {
            responseKey = Key.OWNER_DOES_NOT_EXIST;
        } else if ( userToShare == null || userToShare.getId() <= 1 ) {
            responseKey = Key.USER_TO_SHARE_DOES_NOT_EXIST;
        } else {
            if ( owner.getUserGroup() != null
                && !owner.getUserGroup().equals(userToShare.getUserGroup())
                && !owner.getUserGroup().getOwner().equals(userToShare) ) {
                responseKey = Key.GROUP_MEMBER_SHARE_RESTRICTION_EXCEEDED;
            } else {
                Robot robot = robotDao.loadRobot(robotName);
                if ( robot == null ) {
                    responseKey = Key.ROBOT_DOES_NOT_EXIST;
                } else {
                    programToShare = programDao.load(programName, owner, robot, author);
                    if ( programToShare == null ) {
                        responseKey = Key.PROGRAM_TO_SHARE_DOES_NOT_EXIST;
                    }
                }
            }
        }
        if ( responseKey != null ) {
            this.setStatus(ProcessorStatus.FAILED, responseKey, new HashMap<>());
            return null;
        }

        UserProgramShareDao userProgramShareDao = new UserProgramShareDao(this.dbSession);
        try {
            if ( right.equals("NONE") ) {
                userProgramShareDao.deleteUserProgramShare(userToShare, programToShare);
                this.setStatus(ProcessorStatus.SUCCEEDED, Key.ACCESS_RIGHT_DELETED, new HashMap<>());
                return null;
            } else {
                Relation relation = Relation.valueOf(right);
                UserProgramShare userProgramShare = userProgramShareDao.persistUserProgramShare(userToShare, programToShare, relation);
                this.setStatus(ProcessorStatus.SUCCEEDED, Key.ACCESS_RIGHT_CHANGED, new HashMap<>());
                return userProgramShare;
            }
        } catch ( Exception e ) {
            String msg =
                "Invalid share request. Owner:" + owner + ", robot:" + robotName + ", program:" + programName + ", with:" + userToShare + ", right:" + right;
            LOG.error(msg);
            Map<String, String> processorParameters = new HashMap<>();
            processorParameters.put(Key.SERVER_ERROR.getKey(), "Invalid share request");
            processorParameters.put("owner", owner.toString());
            processorParameters.put("robot", robotName);
            processorParameters.put("program", programName);
            processorParameters.put("with", userToShare.toString());
            processorParameters.put("right", right);
            this.setStatus(ProcessorStatus.FAILED, Key.SERVER_ERROR, processorParameters);
            return null;
        }
    }

    /**
     * a program, identified by ownerId, robotId, programName<br>
     * - is either shared with another user (right is either "WRITE" or "READ") or <br>
     * - the access right has to be removed (right is "NONE")
     *
     * @param owner The owner of the program and the user-group
     * @param robotName The name of the robot, for which the program is written
     * @param programName The actual program that shall be shared
     * @param userGroupToShare The user-group with which the owner wants to share the program with.
     * @param revoke True, if the sharing shall re revoked, false of it shall be added. There is only a read only right
     */
    private UserGroupProgramShare executeUserGroupShare(
        User owner,
        String robotName,
        String programName,
        User author,
        UserGroup userGroupToShare,
        String right) {
        ProgramDao programDao = new ProgramDao(this.dbSession);
        RobotDao robotDao = new RobotDao(this.dbSession);

        Program programToShare = null;
        Key responseKey = null;
        if ( owner == null ) {
            responseKey = Key.OWNER_DOES_NOT_EXIST;
        } else if ( userGroupToShare == null || userGroupToShare.getOwner().getId() != owner.getId() ) {
            responseKey = Key.GROUP_TO_SHARE_DOES_NOT_EXIST;
        } else {
            Robot robot = robotDao.loadRobot(robotName);
            if ( robot == null ) {
                responseKey = Key.ROBOT_DOES_NOT_EXIST;
            } else {
                programToShare = programDao.load(programName, owner, robot, author);
                if ( programToShare == null ) {
                    responseKey = Key.PROGRAM_TO_SHARE_DOES_NOT_EXIST;
                }
            }
        }
        if ( responseKey != null ) {
            this.setStatus(ProcessorStatus.FAILED, responseKey, new HashMap<>());
            return null;
        }

        UserGroupProgramShareDao userGroupProgramShareDao = new UserGroupProgramShareDao(this.dbSession);
        try {
            Relation relation;
            try {
                relation = Relation.valueOf(right);
            } catch ( IllegalArgumentException e ) {
                relation = null;
            }

            if ( relation == null ) {
                userGroupProgramShareDao.deleteUserGroupProgramShare(userGroupToShare, programToShare);
                this.setStatus(ProcessorStatus.SUCCEEDED, Key.ACCESS_RIGHT_DELETED, new HashMap<>());
                return null;
            } else {
                UserGroupProgramShare userGroupProgramShare = userGroupProgramShareDao.persistUserProgramShare(userGroupToShare, programToShare, relation);
                this.setStatus(ProcessorStatus.SUCCEEDED, Key.ACCESS_RIGHT_CHANGED, new HashMap<>());
                return userGroupProgramShare;
            }
        } catch ( Exception e ) {
            String msg =
                "Invalid share request. Owner:"
                    + owner
                    + ", robot:"
                    + robotName
                    + ", program:"
                    + programName
                    + ", with usergroup:"
                    + userGroupToShare
                    + ", right:"
                    + (right == null ? "revoke" : right.toString());
            LOG.error(msg);
            Map<String, String> processorParameters = new HashMap<>();
            processorParameters.put(Key.SERVER_ERROR.getKey(), "Invalid share request");
            processorParameters.put("owner", owner.toString());
            processorParameters.put("robot", robotName);
            processorParameters.put("program", programName);
            processorParameters.put("usergroup", userGroupToShare.getName());
            processorParameters.put("right", right == null ? "revoke" : right.toString());
            this.setStatus(ProcessorStatus.FAILED, Key.SERVER_ERROR, processorParameters);
            return null;
        }
    }
}