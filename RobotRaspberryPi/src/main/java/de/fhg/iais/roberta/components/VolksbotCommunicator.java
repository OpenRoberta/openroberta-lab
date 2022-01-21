package de.fhg.iais.roberta.components;

import java.io.File;
import java.io.IOException;

import de.fhg.iais.roberta.util.dbc.DbcException;

public class VolksbotCommunicator {
    String pathToSource;
    String pathToResource;
    String program;

    public VolksbotCommunicator(String pathToSource, String pathToResource, String program) {
        this.pathToSource = pathToSource;
        this.pathToResource = pathToResource;
        this.program = program;
    }

    /**
     * Copies the driver next to to the program and executes the program
     */
    public void runProgram() throws Exception {
        String copyCommand = "cp " + this.pathToResource + "epos4.py " + this.pathToSource;
        String exCommand = "python3 " + this.pathToSource + File.separator + this.program;

        try {
            Process process = Runtime.getRuntime().exec(copyCommand);
            int exitValue = process.waitFor();
            if ( exitValue != 0 ) {
                throw new DbcException("Failed to copy epos4.py");
            }
            process = Runtime.getRuntime().exec(exCommand);
            exitValue = process.waitFor();
            if ( exitValue != 0 ) {
                throw new DbcException("Failed to execute program");
            }
        } catch ( IOException e ) {
            throw new DbcException("Failed to open system call process to execute program", e);
        }
    }
}
