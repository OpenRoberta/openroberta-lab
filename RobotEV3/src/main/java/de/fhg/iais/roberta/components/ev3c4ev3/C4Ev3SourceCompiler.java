package de.fhg.iais.roberta.components.ev3c4ev3;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class C4Ev3SourceCompiler {

    private static final Logger LOG = LoggerFactory.getLogger(C4Ev3SourceCompiler.class);

    private final String compilerResourcesDir;

    public C4Ev3SourceCompiler(String compilerResourcesDir) {
        this.compilerResourcesDir = compilerResourcesDir;
    }

    public boolean compile (String sourceCodeFileName, String binaryOutputFile) {
        String compilerExecutableFileName = getCompilerExecutableFileName();
        String[] compilerArguments = getCompilerArguments(compilerExecutableFileName, sourceCodeFileName, binaryOutputFile);
        return executeCompilation(compilerArguments);
    }

    private String getCompilerExecutableFileName () {
        // TODO: Return the file name depending on the os
        if ( SystemUtils.IS_OS_LINUX ) {
            return "arm-linux-gnueabi-g++";
        } else if (SystemUtils.IS_OS_MAC) {
            return "arm-none-linux-gnueabi-g++";
        } else if (SystemUtils.IS_OS_WINDOWS) {
            return "";
        }
        return "arm-none-linux-gnueabi-gcc";
    }

    private String[] getCompilerArguments (String compilerExecutableFileName, String sourceCodeFileName, String binaryOutputFile) {
        return new String[]{
            compilerExecutableFileName,
            sourceCodeFileName,
            "-o", binaryOutputFile,
            "-I", compilerResourcesDir + "c4ev3/include",
            "-L", compilerResourcesDir  + "c4ev3/lib",
            "-l", "ev3api",
            "-static-libstdc++",
            "-std=c++11",
            "-Os",

            // flags to remove unused functions
            "-fdata-sections", "-ffunction-sections", "-Wl,--gc-sections"
        };
    }

    private boolean executeCompilation(String[] compilerArguments){
        ProcessBuilder processBuilder = getProcessBuilder(compilerArguments);
        try {
            final Process p = processBuilder.start();
            final int exitCode = p.waitFor();
            if (exitCode != 0) {
                LOG.error("c4ev3 compiler exited with non zero code: " + exitCode);
                return false;
            }
            return true;
        } catch ( IOException | InterruptedException e ) {
            LOG.error("c4ev3 compilation failed: {}", e);
            return false;
        }
    }

    private ProcessBuilder getProcessBuilder (String[] compilerArguments) {
        ProcessBuilder processBuilder = new ProcessBuilder(compilerArguments);
        processBuilder.redirectInput(ProcessBuilder.Redirect.INHERIT);
        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
        return processBuilder;
    }
}

