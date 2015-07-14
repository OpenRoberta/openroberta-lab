package de.fhg.iais.roberta.persistence;

import java.sql.Timestamp;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;

import de.fhg.iais.roberta.persistence.bo.AccessRight;
import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.Relation;
import de.fhg.iais.roberta.persistence.bo.Robot;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.dao.AccessRightDao;
import de.fhg.iais.roberta.persistence.dao.ProgramDao;
import de.fhg.iais.roberta.persistence.dao.RobotDao;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Util;

public class ProgramProcessor extends AbstractProcessor {
    public ProgramProcessor(DbSession dbSession, HttpSessionState httpSessionState) {
        super(dbSession, httpSessionState);
    }

    public Program getProgram(String programName, int ownerId, int robotId) {
        if ( !Util.isValidJavaIdentifier(programName) ) {
            setError(Key.PROGRAM_ERROR_ID_INVALID, programName);
            return null;
        } else if ( this.httpSessionState.isUserLoggedIn() ) {
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
            programInfo.put(program.getCreated().toString());
            programInfo.put(program.getLastChanged().toString());
            programInfo.put(program.getLastChanged().toString());
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
            programInfo2.put(accessRight.getProgram().getCreated().toString());
            programInfo2.put(accessRight.getProgram().getLastChanged().toString());
            programInfo2.put(accessRight.getProgram().getLastChanged().toString());
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
        User owner = userDao.get(ownerId);

        // Find all the programs which are not owned by the user but have been shared to him
        List<AccessRight> accessRights = accessRightDao.loadAccessRightsForUser(owner);
        for ( AccessRight accessRight : accessRights ) {
            Program program = accessRight.getProgram();
            String userProgramName = program.getName();
            if ( programName.equals(userProgramName) ) {
                String relation = accessRight.getRelation().toString();
                if ( relation.equals(Relation.READ.toString()) || relation.equals(Relation.WRITE.toString()) ) {
                    return program;
                }
            }
        }
        return null;
    }

    /**
     * update a given program owned by a given user. Overwrites an existing program if mayExist == true.
     *
     * @param programName the name of the program
     * @param userId the owner of the program
     * @param robotId
     * @param programText the new program text
     * @param programTimestamp Program timestamp
     * @param mayExist true, if an existing program may be changed; false if a program may be stored only, if it does not exist in the database
     * @param isOwner true, if the owner updates a program; false if a user with access right WRITE updates a program
     */
    public void updateProgram(String programName, int userId, int robotId, String programText, Timestamp programTimestamp, boolean mayExist, boolean isOwner) {
        if ( !Util.isValidJavaIdentifier(programName) ) {
            setError(Key.PROGRAM_ERROR_ID_INVALID, programName);
            return;
        }
        if ( this.httpSessionState.isUserLoggedIn() ) {
            UserDao userDao = new UserDao(this.dbSession);
            RobotDao robotDao = new RobotDao(this.dbSession);
            ProgramDao programDao = new ProgramDao(this.dbSession);
            User user = userDao.get(userId);
            Robot robot = robotDao.get(robotId);
            boolean success = programDao.persistProgramText(programName, user, robot, programText, programTimestamp, mayExist, isOwner);
            if ( success ) {
                setSuccess(Key.PROGRAM_SAVE_SUCCESS);
            } else {
                setError(Key.PROGRAM_SAVE_ERROR_NOT_SAVED_TO_DB);
            }
        } else {
            setError(Key.USER_ERROR_NOT_LOGGED_IN);
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
