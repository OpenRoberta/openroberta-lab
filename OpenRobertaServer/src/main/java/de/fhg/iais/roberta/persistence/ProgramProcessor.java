package de.fhg.iais.roberta.persistence;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.javaServer.resources.OpenRobertaSessionState;
import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.connector.SessionWrapper;
import de.fhg.iais.roberta.persistence.dao.ProgramDao;
import de.fhg.iais.roberta.persistence.dao.UserDao;
import de.fhg.iais.roberta.util.Util;

public class ProgramProcessor extends Processor {
    private static final Logger LOG = LoggerFactory.getLogger(ProgramProcessor.class);

    private final SessionWrapper dbSession;
    private final OpenRobertaSessionState httpSessionState;

    public ProgramProcessor(SessionWrapper dbSession, OpenRobertaSessionState httpSessionState) {
        this.dbSession = dbSession;
        this.httpSessionState = httpSessionState;
    }

    public Program getProgram(String programName, int ownerId) {
        if ( !Util.isValidJavaIdentifier(programName) ) {
            setError("program name is not a valid identifier: " + programName);
            return null;
        } else if ( this.httpSessionState.isUserLoggedIn() ) {
            UserDao userDao = new UserDao(this.dbSession);
            ProgramDao programDao = new ProgramDao(this.dbSession);
            User owner = userDao.get(ownerId);
            Program program = programDao.load(programName, owner);
            setResult(program != null, "loading of program " + programName + ".");
            return program;
        } else {
            setError("program load illegal if not logged in");
            return null;
        }
    }

    public void updateProgram(String programName, int ownerId, String programText) {
        if ( !Util.isValidJavaIdentifier(programName) ) {
            setError("program name is not a valid identifier: " + programName);
        } else if ( this.httpSessionState.isUserLoggedIn() ) {
            UserDao userDao = new UserDao(this.dbSession);
            ProgramDao programDao = new ProgramDao(this.dbSession);
            User owner = userDao.get(ownerId);
            Program program = programDao.persistProgramText(programName, owner, programText);
            setResult(program == null, "saving program " + programName + " to db.");
        } else {
            this.httpSessionState.setProgramNameAndProgramText(programName, programText);
            setResult(true, "saving program " + programName + " to session.");
        }
    }

    public List<String> getProgramNames(int ownerId) {
        UserDao userDao = new UserDao(this.dbSession);
        ProgramDao programDao = new ProgramDao(this.dbSession);
        User owner = userDao.get(ownerId);
        List<Program> programs = programDao.loadAll(owner);
        List<String> programNames = new ArrayList<>();
        for ( Program program : programs ) {
            programNames.add(program.getName());
        }
        setSuccess("found " + programNames.size() + " programs");
        return programNames;
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
            programInfo.put(program.getIconNumber());
            programInfo.put(program.getCreated().toString());
            programInfo.put(program.getLastChanged().toString());
        }
        setSuccess();
        return programInfos;
    }

    public void deleteByName(String programName, int ownerId) {
        UserDao userDao = new UserDao(this.dbSession);
        ProgramDao programDao = new ProgramDao(this.dbSession);
        User owner = userDao.get(ownerId);
        int rowCount = programDao.deleteByName(programName, owner);
        setResult(rowCount > 0, "delete of program " + programName + ".");
    }
}