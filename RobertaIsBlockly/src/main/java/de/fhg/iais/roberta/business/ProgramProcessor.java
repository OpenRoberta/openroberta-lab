package de.fhg.iais.roberta.business;

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

}
