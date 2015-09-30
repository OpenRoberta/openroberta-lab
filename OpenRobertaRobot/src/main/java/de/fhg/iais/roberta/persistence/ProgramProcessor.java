package de.fhg.iais.roberta.persistence;

import java.sql.Timestamp;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;

import de.fhg.iais.roberta.persistence.bo.AccessRight;
import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.Robot;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.dao.AccessRightDao;
import de.fhg.iais.roberta.persistence.dao.ProgramDao;
import de.fhg.iais.roberta.persistence.dao.RobotDao;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.util.Util;

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
    public Program getProgram(String programName, int ownerId, int robotId) {
        if ( !Util.isValidJavaIdentifier(programName) ) {
            setError(Key.PROGRAM_ERROR_ID_INVALID, programName);
            return null;
        } else if ( this.httpSessionState.isUserLoggedIn() || ownerId == 1 ) {
            UserDao userDao = new UserDao(this.dbSession);
            RobotDao robotDao = new RobotDao(this.dbSession);
            ProgramDao programDao = new ProgramDao(this.dbSession);
            User owner = userDao.get(ownerId);
            Robot robot = robotDao.get(robotId);
            Program program = programDao.load(programName, owner, robot);
            if ( program != null ) {
                setSuccess(Key.PROGRAM_GET_ONE_SUCCESS);
                return program;
            } else {
                program = getProgramWithAccessRight(programName, ownerId);
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
     * Get information about all the programs owned by a user and with whom they are shared
     *
     * @param ownerId the owner of the program
     */
    public JSONArray getProgramInfo(int ownerId, int robotId) {
        UserDao userDao = new UserDao(this.dbSession);
        RobotDao robotDao = new RobotDao(this.dbSession);
        ProgramDao programDao = new ProgramDao(this.dbSession);
        AccessRightDao accessRightDao = new AccessRightDao(this.dbSession);
        User owner = userDao.get(ownerId);
        Robot robot = robotDao.get(robotId);
        // First we obtain all programs owned by the user
        List<Program> programs = programDao.loadAll(owner, robot);
        JSONArray programInfos = new JSONArray();
        for ( Program program : programs ) {
            JSONArray programInfo = new JSONArray();
            programInfo.put(program.getName());
            programInfo.put(program.getOwner().getAccount());
            // programInfo.put(program.getNumberOfBlocks());

            List<AccessRight> accessRights = accessRightDao.loadAccessRightsByProgram(program);
            boolean sharedWithUser = true;
            if ( accessRights.isEmpty() ) {
                sharedWithUser = false;
            }

            programInfo.put(sharedWithUser);
            programInfo.put(program.getCreated().getTime());
            programInfo.put(program.getLastChanged().getTime());
            programInfo.put(program.getLastChanged().getTime());
            programInfos.put(programInfo);
        }
        // Now we find all the programs which are not owned by the user but have been shared to him
        List<AccessRight> accessRights2 = accessRightDao.loadAccessRightsForUser(owner);
        for ( AccessRight accessRight : accessRights2 ) {
            JSONArray programInfo2 = new JSONArray();
            programInfo2.put(accessRight.getProgram().getName());
            programInfo2.put(accessRight.getProgram().getOwner().getAccount());
            //            programInfo2.put(userProgram.getProgram().getNumberOfBlocks());
            programInfo2.put(accessRight.getRelation().toString());
            programInfo2.put(accessRight.getProgram().getCreated().getTime());
            programInfo2.put(accessRight.getProgram().getLastChanged().getTime());
            programInfo2.put(accessRight.getProgram().getLastChanged().getTime());
            programInfos.put(programInfo2);
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
    public JSONArray getProgramRelations(String programName, int ownerId, int robotId) {
        UserDao userDao = new UserDao(this.dbSession);
        ProgramDao programDao = new ProgramDao(this.dbSession);
        RobotDao robotDao = new RobotDao(this.dbSession);
        AccessRightDao accessRightDao = new AccessRightDao(this.dbSession);
        User owner = userDao.get(ownerId);
        Robot robot = robotDao.get(robotId);
        JSONArray relations = new JSONArray();
        Program program = programDao.load(programName, owner, robot);
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
    private Program getProgramWithAccessRight(String programName, int ownerId) {
        UserDao userDao = new UserDao(this.dbSession);
        AccessRightDao accessRightDao = new AccessRightDao(this.dbSession);

        // Find whether a program has been shared to the user logged in
        AccessRight accessRight = accessRightDao.loadAccessRightForUser(this.httpSessionState.getUserId(), programName, ownerId);
        if ( accessRight == null ) {
            return null;
        } else {
            return accessRight.getProgram();
        }
    }

    /**
     * insert or update a given program owned by a given user. Overwrites an existing program if mayExist == true.
     *
     * @param programName the name of the program
     * @param userId the owner of the program
     * @param robotId the id of the robot the program was written for
     * @param programText the program text
     * @param programTimestamp timestamp of the last change of the program (if it already existed); <code>null</code> if a new program is saved
     * @param isOwner true, if the owner updates a program; false if a user with access right WRITE updates a program
     */
    public Program persistProgramText(String programName, int userId, int robotId, String programText, Timestamp programTimestamp, boolean isOwner) {
        if ( !Util.isValidJavaIdentifier(programName) ) {
            setError(Key.PROGRAM_ERROR_ID_INVALID, programName);
            return null;
        }
        if ( this.httpSessionState.isUserLoggedIn() ) {
            UserDao userDao = new UserDao(this.dbSession);
            RobotDao robotDao = new RobotDao(this.dbSession);
            ProgramDao programDao = new ProgramDao(this.dbSession);
            User user = userDao.get(userId);
            Robot robot = robotDao.get(robotId);
            Pair<Key, Program> result;
            if ( isOwner ) {
                result = programDao.persistOwnProgram(programName, user, robot, programText, programTimestamp);
            } else {
                result = programDao.persistSharedProgramText(programName, user, robot, programText, programTimestamp);
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
    public void deleteByName(String programName, int ownerId, int robotId) {
        UserDao userDao = new UserDao(this.dbSession);
        ProgramDao programDao = new ProgramDao(this.dbSession);
        RobotDao robotDao = new RobotDao(this.dbSession);
        User owner = userDao.get(ownerId);
        Robot robot = robotDao.get(robotId);
        int rowCount = programDao.deleteByName(programName, owner, robot);
        if ( rowCount > 0 ) {
            setSuccess(Key.PROGRAM_DELETE_SUCCESS);
        } else {
            setError(Key.PROGRAM_DELETE_ERROR);
        }
    }
}
