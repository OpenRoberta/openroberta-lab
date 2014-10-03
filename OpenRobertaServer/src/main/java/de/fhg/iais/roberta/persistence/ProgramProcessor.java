package de.fhg.iais.roberta.persistence;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;

import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.persistence.connector.SessionWrapper;
import de.fhg.iais.roberta.persistence.dao.ProgramDao;
import de.fhg.iais.roberta.persistence.dao.UserDao;

public class ProgramProcessor {
    public Program getProgram(SessionWrapper session, String programName, int ownerId) {
        UserDao userDao = new UserDao(session);
        ProgramDao programDao = new ProgramDao(session);
        User owner = userDao.get(ownerId);
        Program program = programDao.load(programName, owner);
        return program;
    }

    public Program updateProgram(SessionWrapper session, String programName, int ownerId, String programText) {
        UserDao userDao = new UserDao(session);
        ProgramDao programDao = new ProgramDao(session);
        User owner = userDao.get(ownerId);
        Program program = programDao.persistProgramText(programName, owner, programText);
        return program;
    }

    public List<String> getProgramNames(SessionWrapper session, int ownerId) {
        UserDao userDao = new UserDao(session);
        ProgramDao programDao = new ProgramDao(session);
        User owner = userDao.get(ownerId);
        List<Program> programs = programDao.loadAll(owner);
        List<String> programNames = new ArrayList<>();
        for ( Program program : programs ) {
            programNames.add(program.getName());
        }
        return programNames;
    }

    public JSONArray getProgramInfo(SessionWrapper session, int ownerId) {
        UserDao userDao = new UserDao(session);
        ProgramDao programDao = new ProgramDao(session);
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
        return programInfos;
    }

    public int deleteByName(SessionWrapper session, String programName, int ownerId) {
        UserDao userDao = new UserDao(session);
        ProgramDao programDao = new ProgramDao(session);
        User owner = userDao.get(ownerId);
        return programDao.deleteByName(programName, owner);
    }

}
