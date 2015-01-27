package de.fhg.iais.roberta.persistence;

import java.util.List;

import org.codehaus.jettison.json.JSONArray;

import de.fhg.iais.roberta.javaServer.resources.HttpSessionState;
import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.dao.ProgramDao;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
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

    public void updateProgram(String programName, int ownerId, String programText) {
        if ( !Util.isValidJavaIdentifier(programName) ) {
            setError(Key.PROGRAM_ERROR_ID_INVALID, programName);
            return;
        }
        this.httpSessionState.setProgramNameAndProgramText(programName, programText);
        if ( this.httpSessionState.isUserLoggedIn() ) {
            UserDao userDao = new UserDao(this.dbSession);
            ProgramDao programDao = new ProgramDao(this.dbSession);
            User owner = userDao.get(ownerId);
            Program program = programDao.persistProgramText(programName, owner, programText);
            if ( program == null ) {
                setError(Key.PROGRAM_SAVE_ERROR_NOT_SAVED_TO_DB);
                return;
            }
        }
        setSuccess(Key.PROGRAM_SAVE_SUCCESS);
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
            programInfo.put(program.getNumberOfBlocks());
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
