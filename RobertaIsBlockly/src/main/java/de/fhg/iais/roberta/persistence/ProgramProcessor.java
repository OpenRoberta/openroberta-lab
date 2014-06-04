package de.fhg.iais.roberta.persistence;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.Project;
import de.fhg.iais.roberta.persistence.connector.SessionWrapper;
import de.fhg.iais.roberta.persistence.dao.ProgramDao;
import de.fhg.iais.roberta.persistence.dao.ProjectDao;

public class ProgramProcessor {
    public Program getProgram(SessionWrapper session, String projectName, String programName) {
        ProjectDao projectDao = new ProjectDao(session);
        Project project = projectDao.load(projectName);
        Assert.notNull(project);
        ProgramDao programDao = new ProgramDao(session);
        Program program = programDao.load(project, programName);
        return program;
    }

    public Program updateProgram(SessionWrapper session, String projectName, String programName, String programText) {
        ProjectDao projectDao = new ProjectDao(session);
        Project project = projectDao.load(projectName);
        Assert.notNull(project);
        ProgramDao programDao = new ProgramDao(session);
        Program program = programDao.persistProgramText(project, programName, programText);
        return program;
    }

    public List<String> getProgramNames(SessionWrapper session) {
        ProgramDao programDao = new ProgramDao(session);
        List<Program> programs = programDao.loadAll();
        List<String> programNames = new ArrayList<>();
        for ( Program program : programs ) {
            programNames.add(program.getName());
        }
        return programNames;
    }

    public int deleteByName(SessionWrapper session, String projectName, String programName) {
        ProgramDao programDao = new ProgramDao(session);
        return programDao.deleteByName(projectName, programName);
    }

}
