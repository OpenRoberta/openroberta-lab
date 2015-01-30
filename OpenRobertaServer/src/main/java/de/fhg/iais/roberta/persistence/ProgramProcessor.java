package de.fhg.iais.roberta.persistence;

import java.util.List;

import org.codehaus.jettison.json.JSONArray;

import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.dao.ProgramDao;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Util;

public class ProgramProcessor extends AbstractProcessor {
    public ProgramProcessor(DbSession dbSession, HttpSessionState httpSessionState) {
        super(dbSession, httpSessionState);
    }

    public Program getProgram(String programName, int ownerId) {
        if ( !Util.isValidJavaIdentifier(programName) ) {
            setError(Key.PROGRAM_ERROR_ID_INVALID, programName);
            return null;
        } else if ( this.httpSessionState.isUserLoggedIn() ) {
            UserDao userDao = new UserDao(this.dbSession);
            ProgramDao programDao = new ProgramDao(this.dbSession);
            User owner = userDao.get(ownerId);
            Program program = programDao.load(programName, owner);
            if ( program != null ) {
                setSuccess(Key.PROGRAM_GET_ONE_SUCCESS);
                return program;
            } else {
                setError(Key.PROGRAM_GET_ONE_ERROR_NOT_FOUND);
                return null;
            }
        } else {
            setError(Key.PROGRAM_GET_ONE_ERROR_NOT_LOGGED_IN);
            return null;
        }
    }

    /**
     * update a given program owned by a given user. Overwrites an existing program if mayExist == true.
     *
     * @param programName the name of the program
     * @param ownerId the owner of the program
     * @param programText the new program text
     * @param mayExist true, if an existing program may be changed; false if a program may be stored only, if it does not exist in the database
     */
    public void updateProgram(String programName, int ownerId, String programText, boolean mayExist) {
        if ( !Util.isValidJavaIdentifier(programName) ) {
            setError(Key.PROGRAM_ERROR_ID_INVALID, programName);
            return;
        }
        if ( this.httpSessionState.isUserLoggedIn() ) {
            UserDao userDao = new UserDao(this.dbSession);
            ProgramDao programDao = new ProgramDao(this.dbSession);
            User owner = userDao.get(ownerId);
            boolean success = programDao.persistProgramText(programName, owner, programText, mayExist);
            if ( success ) {
                setSuccess(Key.PROGRAM_SAVE_SUCCESS);
            } else {
                setError(Key.PROGRAM_SAVE_ERROR_NOT_SAVED_TO_DB);
            }
        } else {
            setError(Key.USER_ERROR_NOT_LOGGED_IN);
        }
    }

    public JSONArray getProgramInfo(int ownerId) {
        UserDao userDao = new UserDao(this.dbSession);
        ProgramDao programDao = new ProgramDao(this.dbSession);
        User owner = userDao.get(ownerId);
        List<Program> programs = programDao.loadAll(owner);
        JSONArray programInfos = new JSONArray();
        for ( Program program : programs ) {
            JSONArray programInfo = new JSONArray();
            programInfos.put(programInfo);
            programInfo.put(program.getName());
            programInfo.put(program.getOwner().getAccount());
            //            programInfo.put(program.getNumberOfBlocks());
            programInfo.put(program.getCreated().toString());
            programInfo.put(program.getLastChanged().toString());
        }
        setSuccess(Key.PROGRAM_GET_ALL_SUCCESS, "" + programInfos.length());
        return programInfos;
    }

    public void deleteByName(String programName, int ownerId) {
        UserDao userDao = new UserDao(this.dbSession);
        ProgramDao programDao = new ProgramDao(this.dbSession);
        User owner = userDao.get(ownerId);
        int rowCount = programDao.deleteByName(programName, owner);
        if ( rowCount > 0 ) {
            setSuccess(Key.PROGRAM_DELETE_SUCCESS);
        } else {
            setError(Key.PROGRAM_DELETE_ERROR);
        }
    }
}
