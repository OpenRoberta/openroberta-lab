package de.fhg.iais.roberta.persistence;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.persistence.bo.AccessRight;
import de.fhg.iais.roberta.persistence.bo.Configuration;
import de.fhg.iais.roberta.persistence.bo.ConfigurationData;
import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.Relation;
import de.fhg.iais.roberta.persistence.bo.Robot;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.bo.UserGroup;
import de.fhg.iais.roberta.persistence.bo.UserGroupProgramShare;
import de.fhg.iais.roberta.persistence.bo.UserProgramShare;
import de.fhg.iais.roberta.persistence.dao.ConfigurationDao;
import de.fhg.iais.roberta.persistence.dao.ProgramDao;
import de.fhg.iais.roberta.persistence.dao.RobotDao;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.dao.UserGroupProgramShareDao;
import de.fhg.iais.roberta.persistence.dao.UserProgramShareDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.util.Util;

public class ProgramProcessor extends AbstractProcessor {
    private static final Logger LOG = LoggerFactory.getLogger(ProgramProcessor.class);

    public ProgramProcessor(DbSession dbSession, HttpSessionState httpSessionState) {
        super(dbSession, httpSessionState.getUserId());
    }

    public Program getProgramAndLockTable(String programName, String ownerName, String robotName, String authorName) {
        ProgramDao programDao = new ProgramDao(this.dbSession);
        programDao.lockTable();
        return getProgram(programName, ownerName, robotName, authorName);
    }

    /**
     * load a program from the data base. Either the program is owned by the user with the id given
     * or the program is shared by the user given to the user requesting the program
     *
     * @param programName the name of the program to load
     * @param ownerName the account name of the owner. Either the logged in user, or an owner, who shared the program R/W with the logged in user
     * @param robotName the name of the robot the program was written for
     * @param authorName in case the program is from the gallery or an example program, this is the name of the actual author. Ignored otherwise.
     * @return the program; null, if no program was found
     */
    public Program getProgram(String programName, String ownerName, String robotName, String authorName) {
        UserDao userDao = new UserDao(this.dbSession);
        User loggedInUser = this.isUserLoggedIn() ? userDao.load(this.idOfLoggedInUser) : null;
        User owner, author;

        Map<String, String> processorParameters = new HashMap<>();
        processorParameters.put("PROGRAM_NAME", programName);
        processorParameters.put("OWNER_NAME", ownerName);
        processorParameters.put("ROBOT_NAME", robotName);
        processorParameters.put("AUTHOR_NAME", authorName);

        if ( !Util.isValidJavaIdentifier(programName) ) {
            setStatus(ProcessorStatus.FAILED, Key.PROGRAM_ERROR_ID_INVALID, processorParameters);
            return null;
        }

        if ( !UserGroupProcessor.isGroupMember(ownerName) ) {
            //The owner of the program is a global user

            if ( loggedInUser != null && loggedInUser.getAccount().equals(ownerName) ) {
                //The logged in user is loading one of his own programs.
                owner = loggedInUser;
                author = loggedInUser;
            } else {
                owner = userDao.loadUser(null, ownerName);

                if ( owner == null ) {
                    //The specified owner does not exist, so we can not find the program
                    setStatus(ProcessorStatus.FAILED, Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND, processorParameters);
                    return null;
                }

                if ( owner.getId() < 3 ) {
                    //The logged in user loads a program from the gallery or an example program.
                    //Only global users can put programs into the gallery. All users can load programs from it.
                    author = userDao.loadUser(null, authorName);
                } else if ( loggedInUser == null ) {
                    //Since the user is not loading from the gallery or an example program, he can only load programs if he is logged in
                    setStatus(ProcessorStatus.FAILED, Key.PROGRAM_GET_ONE_ERROR_NOT_LOGGED_IN, processorParameters);
                    return null;
                } else if ( loggedInUser.getUserGroup() != null && !loggedInUser.getUserGroup().getOwner().equals(owner) ) {
                    //The logged in user is a member of a user group, so the only global user that is allowed to
                    //share programs with him, is the owner of his user group.
                    //The owner of the program is not the owner of his user group, so loading the program is not allowed!
                    setStatus(ProcessorStatus.FAILED, Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND, processorParameters);
                    return null;
                } else {
                    //Two global users are sharing a program with each other, or the owner of a user group is sharing a program
                    //with one of its members
                    author = owner;
                }
            }
        } else {
            //The owner of the program is a group member
            if ( loggedInUser == null ) {
                //Since the user is not loading from the gallery or an example program, he can only load programs if he is logged in
                setStatus(ProcessorStatus.FAILED, Key.PROGRAM_GET_ONE_ERROR_NOT_LOGGED_IN, processorParameters);
                return null;
            }
            if ( loggedInUser.getUserGroup() == null ) {
                //The currently logged in user is a global user. Since the owner of the program is a group member,
                //the only global user he is allowed to share to is the owner of his user group.
                owner = userDao.loadUserOfGroupByGroupOwner(loggedInUser, ownerName);
            } else {
                //Both users are in a user group, so to share a program, they must be in the same group
                owner = userDao.loadUser(loggedInUser.getUserGroup(), ownerName);
            }
            author = owner;
        }

        if ( owner == null || author == null ) {
            setStatus(ProcessorStatus.FAILED, Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND, processorParameters);
            return null;
        }

        RobotDao robotDao = new RobotDao(this.dbSession);
        ProgramDao programDao = new ProgramDao(this.dbSession);
        Robot robot = robotDao.loadRobot(robotName);
        Program program = programDao.load(programName, owner, robot, author);

        boolean readRight = owner.equals(loggedInUser) || owner.getId() < 3;

        if ( !readRight && program != null ) {
            UserProgramShareDao userProgramShareDao = new UserProgramShareDao(this.dbSession);
            UserProgramShare userProgramShare = userProgramShareDao.loadUserProgramShare(loggedInUser, program);
            readRight = userProgramShare != null;

            if ( !readRight && loggedInUser.getUserGroup() != null && owner.equals(loggedInUser.getUserGroup().getOwner()) ) {
                UserGroupProgramShareDao userGroupProgramShareDao = new UserGroupProgramShareDao(this.dbSession);
                UserGroupProgramShare userGroupProgramShare = userGroupProgramShareDao.loadUserGroupProgramShare(loggedInUser.getUserGroup(), program);
                readRight = userGroupProgramShare != null;
            }
        }

        if ( readRight && program != null ) {
            setStatus(ProcessorStatus.SUCCEEDED, Key.PROGRAM_GET_ONE_SUCCESS, processorParameters);
            return program;
        } else {
            setStatus(ProcessorStatus.FAILED, Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND, processorParameters);
            return null;
        }
    }

    public JSONArray getProgramEntity(String programName, int ownerId, String robotName, int authorId) {
        if ( isUserLoggedIn() ) {
            UserDao userDao = new UserDao(this.dbSession);
            RobotDao robotDao = new RobotDao(this.dbSession);
            ProgramDao programDao = new ProgramDao(this.dbSession);
            User owner = userDao.get(ownerId);
            Robot robot = robotDao.loadRobot(robotName);
            User author = userDao.get(authorId);
            Program program = programDao.load(programName, owner, robot, author);
            if ( program != null ) {
                setStatus(ProcessorStatus.SUCCEEDED, Key.PROGRAM_GET_ONE_SUCCESS, new HashMap<>());
                JSONArray prog = new JSONArray();
                prog.put(program.getRobot().getName());
                prog.put(program.getName());
                prog.put(program.getProgramText()); // only needed if we want to show the description of the program
                prog.put(program.getAuthor().getAccount());
                prog.put(program.getLastChanged().getTime());
                prog.put(program.getNumberOfViews());
                prog.put(0);
                prog.put(program.getTags());
                return prog;
            } else {
                setStatus(ProcessorStatus.FAILED, Key.PROGRAM_GET_ONE_ERROR_NOT_LOGGED_IN, new HashMap<>());
                return null;
            }
        }
        return null;
    }

    /**
     * get the configuration attached to the program.
     *
     * @param program the program, whose configuration is looked up. Never null.
     * @return the configuration's XML as String. If the configuration is not found, returns null.
     */
    public String getProgramsConfig(Program program) {
        ConfigurationDao configDao = new ConfigurationDao(this.dbSession);
        String configName = program.getConfigName();
        String configHash = program.getConfigHash();
        if ( configName != null ) {
            Configuration config = configDao.load(configName, program.getOwner(), program.getRobot());
            if ( config == null ) {
                setStatus(ProcessorStatus.FAILED, Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND, new HashMap<>());
                return null;
            } else {
                ConfigurationData configData = configDao.load(config.getConfigurationHash());
                if ( configData == null ) {
                    setStatus(ProcessorStatus.FAILED, Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND, new HashMap<>());
                    return null;
                } else {
                    return configData.getConfigurationText();
                }
            }
        } else if ( configHash != null ) {
            ConfigurationData configData = configDao.load(configHash);
            if ( configData == null ) {
                setStatus(ProcessorStatus.FAILED, Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND, new HashMap<>());
                return null;
            } else {
                return configData.getConfigurationText();
            }
        } else {
            return null; // null to indicate, that the default configuration has to be used.
        }
    }

    /**
     * Get information about all the programs owned by a user and with whom they are shared
     *
     * @param ownerId the owner of the program
     */
    public JSONArray getProgramInfo(int ownerId, String robotName, int authorId) {
        UserDao userDao = new UserDao(this.dbSession);
        RobotDao robotDao = new RobotDao(this.dbSession);
        ProgramDao programDao = new ProgramDao(this.dbSession);
        UserProgramShareDao userProgramShareDao = new UserProgramShareDao(this.dbSession);
        UserGroupProgramShareDao userGroupProgramShareDao = new UserGroupProgramShareDao(this.dbSession);
        User owner = userDao.get(ownerId);
        Robot robot = robotDao.loadRobot(robotName);

        if ( owner == null ) {
            setStatus(ProcessorStatus.FAILED, Key.PROGRAM_GET_ALL_ERROR_USER_NOT_FOUND, new HashMap<>());
            return null;
        }

        // First we obtain all programs owned by the user
        List<Program> programs = programDao.loadAll(owner, robot);

        JSONArray programInfos = new JSONArray();
        for ( Program program : programs ) {
            JSONArray programInfo = new JSONArray();
            programInfo.put(program.getName());
            programInfo.put(program.getOwner().getAccount());
            List<UserProgramShare> userProgramShares = userProgramShareDao.loadUserProgramSharesByProgram(program);
            List<UserGroupProgramShare> userGroupProgramShares = userGroupProgramShareDao.loadUserGroupProgramSharesByProgram(program);
            JSONObject sharedWith = new JSONObject(), sharedWithTmp;
            try {
                if ( !userProgramShares.isEmpty()
                    || !userGroupProgramShares.isEmpty()
                    || owner.getUserGroup() != null && !owner.getUserGroup().getAccessRight().equals(AccessRight.NO_OTHER_READ) ) {
                    JSONArray sharedWithArray = new JSONArray();
                    UserGroup ownerGroup = owner.getUserGroup();
                    User ownerGroupOwner = null;

                    if ( ownerGroup != null && !ownerGroup.getAccessRight().equals(AccessRight.NO_OTHER_READ) ) {
                        ownerGroupOwner = ownerGroup.getOwner();
                        sharedWithTmp = new JSONObject();
                        sharedWithTmp.put("type", User.class.getSimpleName());
                        sharedWithTmp.put("label", ownerGroupOwner.getAccount());
                        Relation relation = Relation.READ;
                        for ( UserProgramShare userProgramShare : userProgramShares ) {
                            if ( userProgramShare.getUser().equals(ownerGroupOwner) ) {
                                relation = userProgramShare.getRelation();
                                break;
                            }
                        }
                        sharedWithTmp.put("right", relation.toString());
                        sharedWithArray.put(sharedWithTmp);
                    }

                    for ( UserGroupProgramShare userGroupProgramShare : userGroupProgramShares ) {
                        sharedWithTmp = new JSONObject();
                        sharedWithTmp.put("type", userGroupProgramShare.getEntityType());
                        sharedWithTmp.put("label", userGroupProgramShare.getEntityLabel());
                        sharedWithTmp.put("right", userGroupProgramShare.getRelation().toString());
                        sharedWithArray.put(sharedWithTmp);
                    }
                    for ( UserProgramShare userProgramShare : userProgramShares ) {
                        if ( ownerGroupOwner != null && userProgramShare.getUser().equals(ownerGroupOwner) ) {
                            continue;
                        }
                        sharedWithTmp = new JSONObject();
                        sharedWithTmp.put("type", userProgramShare.getEntityType());
                        sharedWithTmp.put("label", userProgramShare.getEntityLabel());
                        sharedWithTmp.put("right", userProgramShare.getRelation().toString());
                        sharedWithArray.put(sharedWithTmp);
                    }
                    sharedWith.put("sharedWith", sharedWithArray);
                }
            } catch ( JSONException e ) {
            }
            programInfo.put(sharedWith);
            programInfo.put(program.getAuthor().getAccount());
            programInfo.put(program.getCreated().getTime());
            programInfo.put(program.getLastChanged().getTime());
            programInfos.put(programInfo);
        }

        // Now we find all the programs which are not owned by the user but have been shared to him
        List<UserProgramShare> sharedPrograms = userProgramShareDao.loadUserProgramSharesForUser(owner, robot);

        //Now, if the user is part of a user group, show all programs that the owner of the group provided for it
        if ( owner.getUserGroup() != null ) {
            List<UserGroupProgramShare> sharedProgramsForUserGroup =
                userGroupProgramShareDao.loadUserGroupProgramSharesForUserGroup(owner.getUserGroup(), robot);
            Program tmpProgram;
            for ( UserGroupProgramShare sharedUserGroupProgram : sharedProgramsForUserGroup ) {
                tmpProgram = sharedUserGroupProgram.getProgram();
                boolean directlyShared = false;
                for ( UserProgramShare sharedProgram : sharedPrograms ) {
                    if ( sharedProgram.getProgram().equals(sharedUserGroupProgram.getProgram()) ) {
                        directlyShared = true;
                        break;
                    }
                }
                if ( directlyShared ) {
                    continue;
                }
                if ( tmpProgram != null ) {
                    JSONArray programInfo = new JSONArray();
                    programInfo.put(tmpProgram.getName());
                    programInfo.put(tmpProgram.getOwner().getAccount());
                    JSONObject sharedFrom = new JSONObject();
                    try {
                        sharedFrom.put("sharedFrom", sharedUserGroupProgram.getRelation().toString());
                    } catch ( JSONException e ) {
                    }
                    programInfo.put(sharedFrom);
                    programInfo.put(tmpProgram.getAuthor().getAccount());
                    programInfo.put(tmpProgram.getCreated().getTime());
                    programInfo.put(tmpProgram.getLastChanged().getTime());
                    programInfos.put(programInfo);
                }
            }
        }
        for ( UserProgramShare sharedProgram : sharedPrograms ) {
            // Don't return programs with wrong robot type
            Program program = sharedProgram.getProgram();
            if ( program != null ) {
                JSONArray programInfo = new JSONArray();
                programInfo.put(program.getName());
                programInfo.put(program.getOwner().getAccount());
                JSONObject sharedFrom = new JSONObject();
                try {
                    sharedFrom.put("sharedFrom", sharedProgram.getRelation().toString());
                } catch ( JSONException e ) {
                }
                programInfo.put(sharedFrom);
                programInfo.put(program.getAuthor().getAccount());
                programInfo.put(program.getCreated().getTime());
                programInfo.put(program.getLastChanged().getTime());
                programInfos.put(programInfo);
            }
        }

        Map<String, String> processorParameters = new HashMap<>();
        processorParameters.put("PROGRAM_LENGTH", "" + programInfos.length());
        setStatus(ProcessorStatus.SUCCEEDED, Key.PROGRAM_GET_ALL_SUCCESS, processorParameters);
        return programInfos;
    }

    public JSONArray getProgramInfoForUserGroupMembers(UserGroup userGroup, String robotName) {
        if ( userGroup == null || userGroup.getAccessRight().equals(AccessRight.NO_OTHER_READ) ) {
            return new JSONArray();
        }

        ProgramDao programDao = new ProgramDao(this.dbSession);
        RobotDao robotDao = new RobotDao(this.dbSession);

        Robot robot = robotDao.loadRobot(robotName);

        Set<User> members = userGroup.getMembers();

        JSONArray programInfos = new JSONArray();

        for ( User member : members ) {
            List<Program> memberPrograms = programDao.loadAll(member, robot);

            for ( Program program : memberPrograms ) {
                JSONArray programInfo = new JSONArray();
                programInfo.put(program.getName());
                programInfo.put(program.getOwner().getAccount());
                JSONObject sharedFrom = new JSONObject();
                try {
                    Relation relation;
                    switch ( userGroup.getAccessRight() ) {
                        case ADMIN_READ:
                        case ALL_READ:
                        default:
                            relation = Relation.READ;
                            break;
                    }
                    sharedFrom.put("sharedFrom", relation.toString());
                } catch ( JSONException e ) {
                }
                programInfo.put(sharedFrom);
                programInfo.put(program.getAuthor().getAccount());
                programInfo.put(program.getCreated().getTime());
                programInfo.put(program.getLastChanged().getTime());
                programInfos.put(programInfo);
            }
        }

        Map<String, String> processorParameters = new HashMap<>();
        processorParameters.put("PROGRAM_LENGTH", "" + programInfos.length());
        setStatus(ProcessorStatus.SUCCEEDED, Key.PROGRAM_GET_ALL_SUCCESS, processorParameters);
        return programInfos;

    }

    public JSONArray getProgramsSharedWithUserGroup(UserGroup userGroup, String robotName) {
        UserGroupProgramShareDao userGroupProgramShareDao = new UserGroupProgramShareDao(this.dbSession);
        UserProgramShareDao userProgramShareDao = new UserProgramShareDao(this.dbSession);
        RobotDao robotDao = new RobotDao(this.dbSession);

        Robot robot = robotDao.loadRobot(robotName);

        JSONArray programInfos = new JSONArray(), programInfo, sharedWithArray;
        Program program;
        JSONObject sharedWith;

        List<UserGroupProgramShare> programShares = userGroupProgramShareDao.loadUserGroupProgramSharesForUserGroup(userGroup, robot);

        for ( UserGroupProgramShare programShare : programShares ) {
            programInfo = new JSONArray();
            program = programShare.getProgram();
            programInfo.put(program.getName());
            programInfo.put(program.getOwner().getAccount());
            List<UserProgramShare> userProgramShares = userProgramShareDao.loadUserProgramSharesByProgram(program);
            List<UserGroupProgramShare> userGroupProgramShares = userGroupProgramShareDao.loadUserGroupProgramSharesByProgram(program);
            sharedWith = new JSONObject();
            try {
                if ( !userProgramShares.isEmpty() || !userGroupProgramShares.isEmpty() ) {
                    sharedWithArray = new JSONArray();
                    for ( UserGroupProgramShare userGroupProgramShare : userGroupProgramShares ) {
                        JSONObject sharedWithUserGroup = new JSONObject();
                        sharedWithUserGroup.put("type", userGroupProgramShare.getEntityType());
                        sharedWithUserGroup.put("label", userGroupProgramShare.getEntityLabel());
                        sharedWithUserGroup.put("right", userGroupProgramShare.getRelation().toString());
                        sharedWithArray.put(sharedWithUserGroup);
                    }
                    for ( UserProgramShare userProgramShare : userProgramShares ) {
                        JSONObject sharedWithUser = new JSONObject();

                        sharedWithUser.put("type", userProgramShare.getEntityType());
                        sharedWithUser.put("label", userProgramShare.getEntityLabel());
                        sharedWithUser.put("right", userProgramShare.getRelation().toString());
                        sharedWithArray.put(sharedWithUser);
                    }
                    sharedWith.put("sharedWith", sharedWithArray);
                }
            } catch ( JSONException e ) {
            }
            programInfo.put(sharedWith);
            programInfo.put(program.getAuthor().getAccount());
            programInfo.put(program.getCreated().getTime());
            programInfo.put(program.getLastChanged().getTime());
            programInfos.put(programInfo);

        }

        return programInfos;
    }

    /**
     * TODO: really needed? No use found in client. Find out with whom a program is shared and under which rights
     *
     * @param programName the name of the program
     * @param ownerId the owner of the program
     */
    @Deprecated
    public JSONArray getProgramRelations(String programName, int ownerId, String robotName, int authorId) {
        UserDao userDao = new UserDao(this.dbSession);
        ProgramDao programDao = new ProgramDao(this.dbSession);
        RobotDao robotDao = new RobotDao(this.dbSession);
        UserProgramShareDao accessRightDao = new UserProgramShareDao(this.dbSession);
        User owner = userDao.get(ownerId);
        Robot robot = robotDao.loadRobot(robotName);
        User author = userDao.get(authorId);
        JSONArray relations = new JSONArray();
        Program program = programDao.load(programName, owner, robot, author);
        //If shared find with whom and under which rights
        List<UserProgramShare> accessRights = accessRightDao.loadUserProgramSharesByProgram(program);
        for ( UserProgramShare accessRight : accessRights ) {
            JSONArray relation = new JSONArray();
            relation.put(programName);
            relation.put(ownerId);
            relation.put(accessRight.getUser().getAccount());
            relation.put(accessRight.getRelation().toString());
            relation.put(accessRight.getRelation().toString());
            relations.put(relation);
        }
        Map<String, String> processorParameters = new HashMap<>();
        processorParameters.put("RELATIONS_LENGTH", "" + relations.length());
        setStatus(ProcessorStatus.SUCCEEDED, Key.PROGRAM_GET_ALL_SUCCESS, processorParameters);
        return relations;
    }

    /**
     * Test if a given user has write or read access rights for a given program that was created by another user
     *
     * @param programName the name of the program
     * @param ownerId the owner of the program
     */
    private Program getProgramWithAccessRight(String programName, int ownerId, String authorName) {
        UserProgramShareDao accessRightDao = new UserProgramShareDao(this.dbSession);

        // Find whether a program has been shared to the user logged in
        UserProgramShare accessRight = accessRightDao.loadUserProgramShareForUser(this.getIdOfLoggedInUser(), programName, ownerId, authorName);
        if ( accessRight == null ) {
            return null;
        } else {
            return accessRight.getProgram();
        }
    }

    /**
     * Get information about all the programs owned by the gallery
     *
     * @param galleryId the gallery user
     */

    public JSONArray getProgramGallery(int userId, String robotGroup) {
        UserDao userDao = new UserDao(this.dbSession);
        ProgramDao programDao = new ProgramDao(this.dbSession);
        int galleryId = userDao.loadUser(null, "Gallery").getId();
        JSONArray programs = programDao.loadGallery(galleryId, userId, robotGroup);
        Map<String, String> processorParameters = new HashMap<>();
        processorParameters.put("PROGRAMS_LENGTH", "" + programs.length());
        setStatus(ProcessorStatus.SUCCEEDED, Key.PROGRAM_GET_ALL_SUCCESS, processorParameters);
        return programs;
    }

    /**
     * insert or update a given program owned by a given user. Overwrites an existing program if mayExist == true.
     *
     * @param programName the name of the program. Never null.
     * @param programText the program text. Never null.
     * @param configName the name of the attached configuration. Null, if the configurartion is anonymous.
     * @param configText the XML definition of the attached configuration. Null, if the configuration is the default configuration.
     * @param userId the owner of the program
     * @param robotName the name of the robot the program was written for
     * @param programTimestamp timestamp of the last change of the program (if it already existed); <code>null</code> if a new program is saved
     * @param isOwner true, if the owner updates a program; false if a user with access right WRITE updates a program
     */
    public Program persistProgramText(
        String programName,
        String ownerName,
        String programText,
        String configName,
        String configText,
        String robotName,
        Timestamp programTimestamp) //
    {
        if ( !Util.isValidJavaIdentifier(programName) || programName.length() > 255 ) {
            Map<String, String> processorParameters = new HashMap<>();
            processorParameters.put("PROGRAM_NAME", programName);
            setStatus(ProcessorStatus.FAILED, Key.PROGRAM_ERROR_ID_INVALID, processorParameters);
            return null;
        }

        UserDao userDao = new UserDao(this.dbSession);
        User user = this.isUserLoggedIn() ? userDao.get(this.getIdOfLoggedInUser()) : null;

        if ( user == null ) {
            //Can happen, if the logged in user is a member of a user group that just got deleted.
            this.setStatus(ProcessorStatus.FAILED, Key.USER_ERROR_NOT_LOGGED_IN, new HashMap<>());
            return null;
        }

        RobotDao robotDao = new RobotDao(this.dbSession);
        ProgramDao programDao = new ProgramDao(this.dbSession);
        ConfigurationDao confDao = new ConfigurationDao(this.dbSession);

        programDao.lockTable();

        Robot robot = robotDao.loadRobot(robotName);
        User owner, author;

        if ( user.getAccount().equals(ownerName) ) {
            //Its a program of the current user
            owner = user;
            author = user;
        } else if ( ownerName.equals("Gallery") ) {
            //It is a program from the user, that he published in the gallery
            owner = userDao.loadUser(null, "Gallery");
            author = user;
        } else {
            //The program is owned by someone else.
            if ( UserGroupProcessor.isGroupMember(ownerName) ) {
                //The owner of the program is a member of a user group
                if ( user.getUserGroup() != null ) {
                    //If the current user is in a user group and the owner is in a user group, both must be in the same user group
                    owner = userDao.loadUser(user.getUserGroup(), ownerName);
                } else {
                    //If the current user is not in a user group, but the owner is in a user group,
                    //the current user must be the owner of that user group
                    owner = userDao.loadUserOfGroupByGroupOwner(user, ownerName);
                }
            } else {
                //The owner of the program is a global user
                if ( user.getUserGroup() != null ) {
                    //The current user is a member of a user group. The user group owner is the only global user that can
                    //share programs with the current user
                    if ( user.getUserGroup().getOwner().getAccount().equals(ownerName) ) {
                        owner = user.getUserGroup().getOwner();
                    } else {
                        //Illegal state, so owner is null
                        owner = null;
                    }
                } else {
                    //Both users, the currently logged in user and the owner, are global users.
                    owner = userDao.loadUser(null, ownerName);
                }
            }
            author = owner;
        }

        if ( owner == null || author == null ) {
            this.setStatus(ProcessorStatus.FAILED, Key.PROGRAM_SAVE_ERROR_PROGRAM_TO_UPDATE_NOT_FOUND, new HashMap<>());
            return null;
        }

        String confHash;
        if ( configName == null && configText == null ) { // default configuration
            confHash = null;
        } else if ( configName == null && configText != null ) { // anonymous configuration
            confHash = confDao.persistConfigurationHash(configText);
        } else if ( configName != null && configText == null ) { // named configuration (must be persisted already! Check that!)
            if ( confDao.load(configName, author, robot) == null ) {
                this.setStatus(ProcessorStatus.FAILED, Key.SERVER_ERROR, new HashMap<>());
                return null;
            }
            confHash = null;
        } else { // illegal call (frontend error)
            this.setStatus(ProcessorStatus.FAILED, Key.SERVER_ERROR, new HashMap<>());
            return null;
        }

        Pair<Key, Program> result;
        if ( user.equals(owner) || ownerName.equals("Gallery") ) {
            result = programDao.persistOwnProgram(programName, programText, configName, confHash, owner, robot, author, programTimestamp);
        } else {
            result = programDao.persistSharedProgramText(programName, programText, configName, confHash, owner, robot, author, user, programTimestamp);
        }
        // a bit strange, but necessary as Java has no N-tuple
        if ( result.getFirst() == Key.PROGRAM_SAVE_SUCCESS ) {
            this.setStatus(ProcessorStatus.SUCCEEDED, Key.PROGRAM_SAVE_SUCCESS, new HashMap<>());
        } else {
            setStatus(ProcessorStatus.FAILED, result.getFirst(), new HashMap<>());
        }
        return result.getSecond();
    }

    /**
     * delete a given program owned by a given user
     *
     * @param programName the name of the program
     * @param ownerId the owner of the program
     */
    public void deleteByName(String programName, int ownerId, String robotName, String authorName) {
        UserDao userDao = new UserDao(this.dbSession);

        User owner = userDao.load(ownerId);
        if ( owner == null ) {
            //The owner does not exist. This can happen, if a user group gets deleted, while a member is logged in
            this.setStatus(ProcessorStatus.FAILED, Key.USER_ERROR_NOT_LOGGED_IN, new HashMap<>());
            return;
        }

        User author;
        if ( owner.getAccount().equals(authorName) ) {
            author = owner;
        } else if ( owner.getAccount().equals("Gallery") ) {
            //Author must be a global user, because he put a program into the gallery
            author = userDao.loadUser(null, authorName);
        } else {
            //There is no other case, in which the author and owner are not equal
            LOG
                .error(
                    "Tried to delete a program with the owner \""
                        + owner.getAccount()
                        + "\" and author \""
                        + authorName
                        + "\", but this combination is not possible.");
            this.setStatus(ProcessorStatus.FAILED, Key.SERVER_ERROR, new HashMap<>());
            return;
        }

        deleteByName(programName, ownerId, robotName, author.getId());
    }

    /**
     * delete a given program owned by a given user
     *
     * @param programName the name of the program
     * @param ownerId the owner of the program
     */
    public void deleteByName(String programName, int ownerId, String robotName, int authorId) {
        UserDao userDao = new UserDao(this.dbSession);
        ProgramDao programDao = new ProgramDao(this.dbSession);
        RobotDao robotDao = new RobotDao(this.dbSession);
        User owner = userDao.get(ownerId);
        User author = userDao.get(authorId);
        Robot robot = robotDao.loadRobot(robotName);
        int rowCount = programDao.deleteByName(programName, owner, robot, author);
        if ( rowCount > 0 ) {
            setStatus(ProcessorStatus.SUCCEEDED, Key.PROGRAM_DELETE_SUCCESS, new HashMap<>());
        } else {
            setStatus(ProcessorStatus.FAILED, Key.PROGRAM_DELETE_ERROR, new HashMap<>());
        }
    }

    public void addOneView(Program program) {
        program.incrViewed();
    }
}
