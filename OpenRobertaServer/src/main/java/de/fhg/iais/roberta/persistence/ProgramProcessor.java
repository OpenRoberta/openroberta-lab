package de.fhg.iais.roberta.persistence;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.persistence.bo.AccessRight;
import de.fhg.iais.roberta.persistence.bo.Configuration;
import de.fhg.iais.roberta.persistence.bo.ConfigurationData;
import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.Robot;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.dao.AccessRightDao;
import de.fhg.iais.roberta.persistence.dao.ConfigurationDao;
import de.fhg.iais.roberta.persistence.dao.ProgramDao;
import de.fhg.iais.roberta.persistence.dao.RobotDao;
import de.fhg.iais.roberta.persistence.dao.UserDao;
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
     * load a program from the data base. Either the program is owned by the user with the id given or the program is shared by the user given to the user
     * requesting the program
     *
     * @param programName the program to load
     * @param ownerId the owner (either the user logged in or an owner who shared the program R/W with the user logged in
     * @param robotId the robot the program was written for
     * @return the program; null, if no program was found
     */
    public Program getProgram(String programName, String ownerName, String robotName, String authorName) {
        UserDao userDao = new UserDao(this.dbSession);
        User owner = userDao.loadUser(ownerName);
        Map<String, String> processorParameters = new HashMap<>();
        processorParameters.put("PROGRAM_NAME", programName);
        processorParameters.put("OWNER_NAME", ownerName);
        processorParameters.put("ROBOT_NAME", robotName);
        processorParameters.put("AUTHOR_NAME", authorName);

        if ( !Util.isValidJavaIdentifier(programName) ) {
            setStatus(ProcessorStatus.FAILED, Key.PROGRAM_ERROR_ID_INVALID, processorParameters);
            return null;
        } else if ( isUserLoggedIn() || owner.getId() < 3 ) {
            RobotDao robotDao = new RobotDao(this.dbSession);
            ProgramDao programDao = new ProgramDao(this.dbSession);
            Robot robot = robotDao.loadRobot(robotName);
            User author = userDao.loadUser(authorName);
            Program program = programDao.load(programName, owner, robot, author);
            if ( program != null ) {
                setStatus(ProcessorStatus.SUCCEEDED, Key.PROGRAM_GET_ONE_SUCCESS, processorParameters);
                return program;
            } else {
                program = getProgramWithAccessRight(programName, owner.getId(), authorName);
                if ( program != null ) {
                    setStatus(ProcessorStatus.SUCCEEDED, Key.PROGRAM_GET_ONE_SUCCESS, processorParameters);
                    return program;
                } else {
                    setStatus(ProcessorStatus.FAILED, Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND, processorParameters);
                    return null;
                }
            }
        } else {
            setStatus(ProcessorStatus.FAILED, Key.PROGRAM_GET_ONE_ERROR_NOT_LOGGED_IN, processorParameters);
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
        AccessRightDao accessRightDao = new AccessRightDao(this.dbSession);
        User owner = userDao.get(ownerId);
        Robot robot = robotDao.loadRobot(robotName);

        // First we obtain all programs owned by the user
        List<Program> programs = programDao.loadAll(owner, robot);

        JSONArray programInfos = new JSONArray();
        for ( Program program : programs ) {
            JSONArray programInfo = new JSONArray();
            programInfo.put(program.getName());
            programInfo.put(program.getOwner().getAccount());
            List<AccessRight> accessRights = accessRightDao.loadAccessRightsByProgram(program);
            JSONObject sharedWith = new JSONObject();
            try {
                if ( !accessRights.isEmpty() ) {
                    JSONArray sharedWithArray = new JSONArray();
                    for ( AccessRight accessRight : accessRights ) {
                        JSONObject sharedWithUser = new JSONObject();
                        sharedWithUser.put(accessRight.getUser().getAccount(), accessRight.getRelation().toString());
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
        // Now we find all the programs which are not owned by the user but have been shared to him
        List<AccessRight> accessRights2 = accessRightDao.loadAccessRightsForUser(owner, robot);
        for ( AccessRight accessRight : accessRights2 ) {
            // Don't return programs with wrong robot type
            Program program = accessRight.getProgram();
            if ( program != null ) {
                JSONArray programInfo = new JSONArray();
                programInfo.put(program.getName());
                programInfo.put(program.getOwner().getAccount());
                JSONObject sharedFrom = new JSONObject();
                try {
                    sharedFrom.put("sharedFrom", accessRight.getRelation().toString());
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
        AccessRightDao accessRightDao = new AccessRightDao(this.dbSession);
        User owner = userDao.get(ownerId);
        Robot robot = robotDao.loadRobot(robotName);
        User author = userDao.get(authorId);
        JSONArray relations = new JSONArray();
        Program program = programDao.load(programName, owner, robot, author);
        //If shared find with whom and under which rights
        List<AccessRight> accessRights = accessRightDao.loadAccessRightsByProgram(program);
        for ( AccessRight accessRight : accessRights ) {
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
        AccessRightDao accessRightDao = new AccessRightDao(this.dbSession);

        // Find whether a program has been shared to the user logged in
        AccessRight accessRight = accessRightDao.loadAccessRightForUser(getIdOfLoggedInUser(), programName, ownerId, authorName);
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
        int galleryId = userDao.loadUser("Gallery").getId();
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
     * @param programTimestamp timestamp of the last change of the program (if it already existed); <code>null</code> if a new program is saved
     * @param isOwner true, if the owner updates a program; false if a user with access right WRITE updates a program
     * @param robotId the id of the robot the program was written for
     */
    public Program persistProgramText(
        String programName,
        String programText,
        String configName,
        String configText,
        int userId,
        String robotName,
        int authorId,
        Timestamp programTimestamp,
        boolean isOwner) //
    {
        if ( !Util.isValidJavaIdentifier(programName) || programName.length() > 255 ) {
            Map<String, String> processorParameters = new HashMap<>();
            processorParameters.put("PROGRAM_NAME", programName);
            setStatus(ProcessorStatus.FAILED, Key.PROGRAM_ERROR_ID_INVALID, processorParameters);
            return null;
        }
        if ( isUserLoggedIn() ) {
            UserDao userDao = new UserDao(this.dbSession);
            RobotDao robotDao = new RobotDao(this.dbSession);
            ProgramDao programDao = new ProgramDao(this.dbSession);
            ConfigurationDao confDao = new ConfigurationDao(this.dbSession);
            programDao.lockTable();
            User user = userDao.get(userId);
            Robot robot = robotDao.loadRobot(robotName);
            User author = userDao.get(authorId);
            String confHash;
            if ( configName == null && configText == null ) { // default configuration
                confHash = null;
            } else if ( configName == null && configText != null ) { // anonymous configuration
                confHash = confDao.persistConfigurationHash(configText);
            } else if ( configName != null && configText == null ) { // named configuration (must be persisted already! Check that!)
                if ( confDao.load(configName, author, robot) == null ) {
                    setStatus(ProcessorStatus.FAILED, Key.SERVER_ERROR, new HashMap<>());
                    return null;
                }
                confHash = null;
            } else { // illegal call (frontend error)
                setStatus(ProcessorStatus.FAILED, Key.SERVER_ERROR, new HashMap<>());
                return null;
            }
            Pair<Key, Program> result;
            if ( isOwner ) {
                result = programDao.persistOwnProgram(programName, programText, configName, confHash, user, robot, author, programTimestamp);
            } else {
                result = programDao.persistSharedProgramText(programName, programText, configName, confHash, user, robot, author, programTimestamp);
            }
            // a bit strange, but necessary as Java has no N-tuple
            if ( result.getFirst() == Key.PROGRAM_SAVE_SUCCESS ) {
                setStatus(ProcessorStatus.SUCCEEDED, Key.PROGRAM_SAVE_SUCCESS, new HashMap<>());
            } else {
                setStatus(ProcessorStatus.FAILED, result.getFirst(), new HashMap<>());
            }
            return result.getSecond();
        } else {
            setStatus(ProcessorStatus.FAILED, Key.USER_ERROR_NOT_LOGGED_IN, new HashMap<>());
            return null;
        }
    }

    /**
     * delete a given program owned by a given user
     *
     * @param programName the name of the program
     * @param ownerId the owner of the program
     */
    public void deleteByName(String programName, int ownerId, String robotName, String authorName) {
        UserDao userDao = new UserDao(this.dbSession);
        int authorId = userDao.loadUser(authorName).getId();
        deleteByName(programName, ownerId, robotName, authorId);
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
