package de.fhg.iais.roberta.persistence;

import java.util.List;

import org.codehaus.jettison.json.JSONArray;

import de.fhg.iais.roberta.javaServer.resources.HttpSessionState;
import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.dao.ProgramDao;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.persistence.util.DbSession;
import de.fhg.iais.roberta.util.Util;

public class ProgramProcessor extends AbstractProcessor {
    public ProgramProcessor(DbSession dbSession, HttpSessionState httpSessionState) {
        super(dbSession, httpSessionState);
    }

    public Program getProgram(String programName, int ownerId) {
        if ( !Util.isValidJavaIdentifier(programName) ) {
            setError("program.error.id_invalid", programName);
            return null;
        } else if ( this.httpSessionState.isUserLoggedIn() ) {
            UserDao userDao = new UserDao(this.dbSession);
            ProgramDao programDao = new ProgramDao(this.dbSession);
            User owner = userDao.get(ownerId);
            Program program = programDao.load(programName, owner);
            if ( program != null ) {
                setSuccess("program.get_one.success");
                return program;
            } else {
                setError("program.get_one.error.not_found");
                return null;
            }
        } else {
            setError("program.get_one.error.not_logged_in");
            return null;
        }
    }

    public void updateProgram(String programName, int ownerId, String programText) {
        if ( !Util.isValidJavaIdentifier(programName) ) {
            setError("program.error.id_invalid", programName);
            return;
        }
        this.httpSessionState.setProgramNameAndProgramText(programName, programText);
        if ( this.httpSessionState.isUserLoggedIn() ) {
            UserDao userDao = new UserDao(this.dbSession);
            ProgramDao programDao = new ProgramDao(this.dbSession);
            User owner = userDao.get(ownerId);
            Program program = programDao.persistProgramText(programName, owner, programText);
            if ( program == null ) {
                setError("program.save.error.not_saved_to_db");
                return;
            }
        }
        setSuccess("program.save.success");
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
        setSuccess("program.get_all.success", "" + programInfos.length());
        return programInfos;
    }

    public void deleteByName(String programName, int ownerId) {
        UserDao userDao = new UserDao(this.dbSession);
        ProgramDao programDao = new ProgramDao(this.dbSession);
        User owner = userDao.get(ownerId);
        int rowCount = programDao.deleteByName(programName, owner);
        if ( rowCount > 0 ) {
            setSuccess("program.delete.success");
        } else {
            setError("program.delete.error");
        }
    }
}