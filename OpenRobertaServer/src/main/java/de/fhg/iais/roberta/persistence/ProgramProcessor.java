package de.fhg.iais.roberta.persistence;

import java.sql.Timestamp;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import de.fhg.iais.roberta.persistence.bo.AccessRight;
import de.fhg.iais.roberta.persistence.bo.Configuration;
import de.fhg.iais.roberta.persistence.bo.ConfigurationData;
import de.fhg.iais.roberta.persistence.bo.Like;
import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.Relation;
import de.fhg.iais.roberta.persistence.bo.Robot;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.dao.AccessRightDao;
import de.fhg.iais.roberta.persistence.dao.ConfigurationDao;
import de.fhg.iais.roberta.persistence.dao.LikeDao;
import de.fhg.iais.roberta.persistence.dao.ProgramDao;
import de.fhg.iais.roberta.persistence.dao.RobotDao;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.util.Util1;

public class ProgramProcessor extends AbstractProcessor {
    public ProgramProcessor(DbSession dbSession, HttpSessionState httpSessionState) {
        super(dbSession, httpSessionState);
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
        if ( !Util1.isValidJavaIdentifier(programName) ) {
            setError(Key.PROGRAM_ERROR_ID_INVALID, programName);
            return null;
        } else if ( this.httpSessionState.isUserLoggedIn() || owner.getId() < 3 ) {
            RobotDao robotDao = new RobotDao(this.dbSession);
            ProgramDao programDao = new ProgramDao(this.dbSession);
            Robot robot = robotDao.loadRobot(robotName);
            User author = userDao.loadUser(authorName);
            Program program = programDao.load(programName, owner, robot, author);
            if ( program != null ) {
                setSuccess(Key.PROGRAM_GET_ONE_SUCCESS);
                return program;
            } else {
                program = getProgramWithAccessRight(programName, owner.getId(), authorName);
                if ( program != null ) {
                    setSuccess(Key.PROGRAM_GET_ONE_SUCCESS);
                    return program;
                } else {
                    setError(Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND);
                    return null;
                }
            }
        } else {
            setError(Key.PROGRAM_GET_ONE_ERROR_NOT_LOGGED_IN);
            return null;
        }
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
                setError(Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND);
                return null;
            } else {
                ConfigurationData configData = configDao.load(config.getConfigurationHash());
                if ( configData == null ) {
                    setError(Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND);
                    return null;
                } else {
                    return configData.getConfigurationText();
                }
            }
        } else if ( configHash != null ) {
            ConfigurationData configData = configDao.load(configHash);
            if ( configData == null ) {
                setError(Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND);
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
            // programInfo.put(program.getNumberOfBlocks());
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
            Program program = programDao.get(accessRight.getProgram().getId());
            if ( program != null ) {
                JSONArray programInfo2 = new JSONArray();
                programInfo2.put(accessRight.getProgram().getName());
                programInfo2.put(accessRight.getProgram().getOwner().getAccount());
                //            programInfo2.put(userProgram.getProgram().getNumberOfBlocks());
                JSONObject sharedFrom = new JSONObject();
                try {
                    sharedFrom.put("sharedFrom", accessRight.getRelation().toString());
                } catch ( JSONException e ) {
                }
                programInfo2.put(sharedFrom);
                programInfo2.put(program.getAuthor().getAccount());
                programInfo2.put(accessRight.getProgram().getCreated().getTime());
                programInfo2.put(accessRight.getProgram().getLastChanged().getTime());
                programInfos.put(programInfo2);
            }
        }

        setSuccess(Key.PROGRAM_GET_ALL_SUCCESS, "" + programInfos.length());
        return programInfos;
    }

    /**
     * Find out with whom a program is shared and under which rights
     *
     * @param programName the name of the program
     * @param ownerId the owner of the program
     */
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
        setSuccess(Key.PROGRAM_GET_ALL_SUCCESS, "" + relations.length());
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
        AccessRight accessRight = accessRightDao.loadAccessRightForUser(this.httpSessionState.getUserId(), programName, ownerId, authorName);
        if ( accessRight == null ) {
            return null;
        } else {
            return accessRight.getProgram();
        }
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
        if ( !Util1.isValidJavaIdentifier(programName) ) {
            setError(Key.PROGRAM_ERROR_ID_INVALID, programName);
            return null;
        }
        if ( this.httpSessionState.isUserLoggedIn() ) {
            UserDao userDao = new UserDao(this.dbSession);
            RobotDao robotDao = new RobotDao(this.dbSession);
            ProgramDao programDao = new ProgramDao(this.dbSession);
            ConfigurationDao confDao = new ConfigurationDao(this.dbSession);
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
                    setError(Key.SERVER_ERROR);
                    return null;
                }
                confHash = null;
            } else { // illegal call (frontend error)
                setError(Key.SERVER_ERROR);
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
                setSuccess(Key.PROGRAM_SAVE_SUCCESS);
            } else {
                setError(result.getFirst());
            }
            return result.getSecond();
        } else {
            setError(Key.USER_ERROR_NOT_LOGGED_IN);
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
            setSuccess(Key.PROGRAM_DELETE_SUCCESS);
        } else {
            setError(Key.PROGRAM_DELETE_ERROR);
        }
    }

    /**
     * Get information about all the programs owned by the gallery
     *
     * @param galleryId the gallery user
     */

    public JSONArray getProgramGallery(int userId) {

        UserDao userDao = new UserDao(this.dbSession);
        AccessRightDao accessRightDao = new AccessRightDao(this.dbSession);
        ProgramDao programDao = new ProgramDao(this.dbSession);
        LikeDao likeDao = new LikeDao(this.dbSession);

        User gallery = userDao.loadUser("Gallery");
        JSONArray programs = new JSONArray();

        // Find all the programs which are owned by the gallery
        List<Program> programsList = programDao.loadAll(gallery);
        for ( Program program : programsList ) {
            // check if this program only is shared with one user (the original owner) with special exclusive right X_WRITE.
            List<AccessRight> accessRights = accessRightDao.loadAccessRightsByProgram(program);
            List<Like> likes = likeDao.loadLikesByProgram(program);
            Like like = null;
            if ( userId > 0 ) {
                like = likeDao.loadLike(userDao.load(userId), program);
            }
            if ( !accessRights.isEmpty() && accessRights.size() == 1 && accessRights.get(0).getRelation() == Relation.X_WRITE ) {
                JSONArray tempProgram = new JSONArray();
                tempProgram.put(program.getRobot().getName());
                tempProgram.put(program.getName());
                tempProgram.put(program.getProgramText()); // only needed if we want to show the description of the program
                tempProgram.put(accessRights.get(0).getUser().getAccount());
                tempProgram.put(program.getCreated().getTime());
                tempProgram.put(program.getNumberOfViews());
                tempProgram.put(likes.size());
                tempProgram.put(program.getTags());
                tempProgram.put(like == null ? false : true);
                programs.put(tempProgram);
            } else {
                // this should not happen!
                System.out.println("User gallery owns programs that are not shared exactly with the origin user with right X_WRITE: " + program.getId());
            }
        }
        setSuccess(Key.PROGRAM_GET_ALL_SUCCESS, "" + programs.length());
        return programs;
    }

    public JSONArray getProgramEntity(String programName, int ownerId, String robotName, int authorId) {

        if ( this.httpSessionState.isUserLoggedIn() ) {
            UserDao userDao = new UserDao(this.dbSession);
            RobotDao robotDao = new RobotDao(this.dbSession);
            ProgramDao programDao = new ProgramDao(this.dbSession);
            User owner = userDao.get(ownerId);
            Robot robot = robotDao.loadRobot(robotName);
            User author = userDao.get(authorId);
            Program program = programDao.load(programName, owner, robot, author);
            if ( program != null ) {
                setSuccess(Key.PROGRAM_GET_ONE_SUCCESS);
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
                setError(Key.PROGRAM_GET_ONE_ERROR_NOT_LOGGED_IN);
                return null;
            }
        }
        return null;
    }

    public void addOneView(Program program) {
        program.incrViewed();
    }
}
