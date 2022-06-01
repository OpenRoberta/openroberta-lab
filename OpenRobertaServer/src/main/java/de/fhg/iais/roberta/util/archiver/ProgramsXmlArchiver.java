package de.fhg.iais.roberta.util.archiver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import de.fhg.iais.roberta.persistence.bo.Program;
import de.fhg.iais.roberta.persistence.bo.User;
import de.fhg.iais.roberta.util.basic.Pair;

public class ProgramsXmlArchiver {
    private boolean hasGroups;
    boolean hasPrograms;

    private int ownerId;

    private List<Pair<Program, String>> programConfigList;

    public ProgramsXmlArchiver(int ownerId) {
        this.ownerId = ownerId;
        programConfigList = new ArrayList<>();
    }

    public void putProgram(Program program, String configuration) {
        programConfigList.add(Pair.of(program, configuration));

        if ( program.getAuthor().getId() == ownerId ) {
            hasPrograms = true;
        } else {
            hasGroups = true;
        }
    }

    public ByteArrayOutputStream getArchive() throws Exception {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); ZipOutputStream zipOutputStream = new ZipOutputStream(baos)) {
            for ( Pair<Program, String> progConf : programConfigList ) {
                addProgramToZip(progConf.getFirst(), progConf.getSecond(), zipOutputStream);
            }
            return baos;
        }
    }


    private void addProgramToZip(Program program, String configuration, ZipOutputStream zip) throws IOException {
        String programXml = buildProgramXml(program, configuration);
        String path = getProgramPath(program, configuration);

        zip.putNextEntry(new ZipEntry(path));
        zip.write(programXml.getBytes());
        zip.closeEntry();
    }

    private static String buildProgramXml(Program program, String configuration) {
        return
            "<export xmlns=\"http://de.fhg.iais.roberta.blockly\"><program>"
                + program.getProgramText()
                + "</program><config>"
                + configuration
                + "</config></export>";
    }

    private String getProgramPath(Program program, String config) {
        String directory = getDirectory(program, config);
        String fileName = program.getName() + ".xml";

        return directory + "/" + fileName;
    }

    private String getDirectory(Program program, String config) {
        if ( isArchiveOwner(program) ) {
            return getOwnerDirectoryName(config);
        } else {
            return getGroupMemberDirectoryName(program, config);
        }
    }

    private boolean isArchiveOwner(Program program) {
        User author = program.getAuthor();
        return author.getId() == ownerId;
    }

    private String getGroupMemberDirectoryName(Program program, String config) {
        User author = program.getAuthor();
        String username = getGroupMemberName(author);

        if ( hasPrograms ) {
            return "GroupPrograms/" + author.getUserGroup().getName() + "/" + getRobotGroup(config) + "/" + username;
        } else {
            return author.getUserGroup().getName() + "/" + getRobotGroup(config) + "/" + username;
        }
    }

    private String getGroupMemberName(User author) {
        String fullUserName = author.getAccount();
        //Group members are given in the Format Grpname:Username this cuts of the group name
        return fullUserName.substring(fullUserName.indexOf(":") + 1);
    }

    private String getOwnerDirectoryName(String config) {
        String directory;
        if ( hasGroups ) {
            directory = "MyPrograms/" + getRobotGroup(config);
        } else {
            directory = getRobotGroup(config);
        }
        return directory;
    }

    private String getRobotGroup(String config) {
        int offset = 11; // length of "robottype\""
        int indexOfGroupName = config.indexOf("robottype=\"") + offset;
        int indexEndOfGroupName = config.indexOf("\"", indexOfGroupName);
        return config.substring(indexOfGroupName, indexEndOfGroupName);
    }
}
