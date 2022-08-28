package de.fhg.iais.roberta.util.archiver;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.persistence.ProgramProcessor;
import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.util.HttpSessionState;

public class UserProgramsArchiver {

    private HttpSessionState userSession;
    private ProgramProcessor programProcessor;
    private int userId;

    public UserProgramsArchiver(HttpSessionState userSession, ProgramProcessor programProcessor) {
        this.programProcessor = programProcessor;
        this.userSession = userSession;
        userId = userSession.getUserId();
    }

    public InputStream getArchive() throws Exception {
        ProgramsXmlArchiver archive = new ProgramsXmlArchiver(userId);
        fillArchive(archive);
        try (InputStream zip = new ByteArrayInputStream(archive.getArchive().toByteArray())) {
            return zip;
        }
    }

    private void fillArchive(ProgramsXmlArchiver archive) {
        List<Program> programList = programProcessor.getProgramsByUserAndHisGroups(userId);
        for ( Program program : programList ) {
            String config = getProgramConfigOrDefault(program);
            archive.putProgram(program, config);
        }
    }


    private String getProgramConfigOrDefault(Program program) {
        if ( programProcessor.getProgramsConfig(program) == null ) {
            return getDefualtConfig(program);
        }
        return programProcessor.getProgramsConfig(program);
    }

    private String getDefualtConfig(Program program) {
        RobotFactory factory = userSession.getRobotFactoriesOfGroup(program.getRobot().getName()).get(0);
        return factory.getConfigurationDefault();
    }

}
