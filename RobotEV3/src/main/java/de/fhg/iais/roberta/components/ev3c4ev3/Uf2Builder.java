package de.fhg.iais.roberta.components.ev3c4ev3;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Creates a UF2FileContainer containing all the needed files to install and run a NEPO program.
 * When the program starts the first time (when it is copied to the robot) the C program will move the program from the BrkProg_SAVE folder
 * to a folder with the same name as the program. This is done to allow the user to delete the program, which otherwise is not possible.
 * The files included in the built UF2 are:
 * - binary file (compiled C program)
 * - RBF to launch the program the first time
 * - RBF to launch the program once the binary has been moved to the subfolder
 * - Flag to indicate that the program has just been copied
 */
public class Uf2Builder {

    private static final String EV3_PROJECTS_FOLDER_PATH = "../prjs";
    private static final String EV3_FIRST_TIME_FOLDER_PATH = EV3_PROJECTS_FOLDER_PATH + "/BrkProg_SAVE";

    private static final String JUST_COPIED_FLAG_FILE_NAME = EV3_FIRST_TIME_FOLDER_PATH + "/NEPO-just-uploaded.txt";
    private static final byte[] JUST_COPIED_FLAG_FILE_CONTENT = "NEPO".getBytes(StandardCharsets.UTF_8);

    private final RbfBuilder rbfBuilder;

    public Uf2Builder(String compilerResourceDir) {
        rbfBuilder = new RbfBuilder(compilerResourceDir);
    }

    public Uf2FileContainer createUf2File(String programName, String binaryFileName) throws IOException {
        Uf2FileContainer uf2 = new Uf2FileContainer();
        addBinaryToUf2(uf2, binaryFileName, programName);
        addRbfFirstTimeLauncherToUf2(uf2, programName);
        addRbfToUf2(uf2, programName);
        addJustUploadedFlagToUf2(uf2);
        return uf2;
    }

    private void addBinaryToUf2(Uf2FileContainer uf2, String binaryFileName, String programName) throws IOException {
        uf2.add(new File(binaryFileName), getBinaryNameInEv3(programName));
    }

    private void addRbfFirstTimeLauncherToUf2(Uf2FileContainer uf2, String programName) {
        byte[] firstTimeLauncher = rbfBuilder.build(getBinaryNameInEv3(programName));
        uf2.add(firstTimeLauncher, getRbfFirstTimeLauncherNameInEv3(programName));
    }

    private void addRbfToUf2(Uf2FileContainer uf2, String programName) {
        byte[] launcher = rbfBuilder.build(getMovedBinaryNameInEv3(programName));
        uf2.add(launcher, getRbfNameInEv3(programName));
    }

    private void addJustUploadedFlagToUf2(Uf2FileContainer uf2) {
        uf2.add(JUST_COPIED_FLAG_FILE_CONTENT, JUST_COPIED_FLAG_FILE_NAME);
    }

    private String getBinaryNameInEv3(String programName) {
        return EV3_FIRST_TIME_FOLDER_PATH + "/" + programName + ".elf";
    }

    private String getMovedBinaryNameInEv3(String programName) {
        return EV3_PROJECTS_FOLDER_PATH + "/" + programName + "/" + programName + ".elf";
    }

    private String getRbfFirstTimeLauncherNameInEv3(String programName) {
        return EV3_FIRST_TIME_FOLDER_PATH + "/" + programName + ".rbf";
    }

    private String getRbfNameInEv3(String programName) {
        // we use the .tmp extension to prevent the EV3 to execute it automatically
        return EV3_FIRST_TIME_FOLDER_PATH + "/" + programName + ".tmp";
    }

}
