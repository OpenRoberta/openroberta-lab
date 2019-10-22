package de.fhg.iais.roberta.components.ev3c4ev3;

import java.io.IOException;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.util.dbc.DbcException;

public class C4Ev3SourceCompiler {

    private static final Logger LOG = LoggerFactory.getLogger(C4Ev3SourceCompiler.class);

    private final String compilerResourcesDir;
    private final String compilerExecutableFileName;
    private final String staticLibraryFolderName;

    public C4Ev3SourceCompiler(String compilerResourcesDir) {
        this.compilerResourcesDir = compilerResourcesDir;
        this.compilerExecutableFileName = getCompilerExecutableFileName();
        this.staticLibraryFolderName = getStaticLibraryFolderName();
    }

    private String getCompilerExecutableFileName() {
        if ( SystemUtils.IS_OS_LINUX ) {
            if ( System.getProperty("os.arch").contains("arm") ) {
                return "arm-c4ev3-linux-uclibceabi-g++";
            }
            return "arm-linux-gnueabi-g++";
        } else if ( SystemUtils.IS_OS_MAC ) {
            return "arm-none-linux-gnueabi-g++";
        } else if ( SystemUtils.IS_OS_WINDOWS ) {
            // TODO: Set path for Windows
            return "";
        }
        throw new DbcException("Unknown c4ev3 compiler executable file name for current platform");
    }

    private String getStaticLibraryFolderName() {
        if ( compilerExecutableFileName.contains("uclibc") ) {
            /*
             * Since we sue a compiler built against uclibc on raspberry, we also need to use a different static library
             * of c4ev3, one built with a uclibc compiler
             */
            return "lib-uclibc";
        }
        return "lib";
    }

    public boolean compile(String sourceCodeFileName, String binaryOutputFile) {
        String[] compilerArguments = getCompilerArguments(compilerExecutableFileName, sourceCodeFileName, binaryOutputFile);
        return executeCompilation(compilerArguments);
    }

    private String[] getCompilerArguments(String compilerExecutableFileName, String sourceCodeFileName, String binaryOutputFile) {
        return new String[] {
            compilerExecutableFileName,
            sourceCodeFileName,
            "-o",
            binaryOutputFile,
            "-I",
            compilerResourcesDir + "c4ev3/include/NEPO",
            "-I",
            compilerResourcesDir + "c4ev3/include/c4ev3",
            "-L",
            compilerResourcesDir + "c4ev3/" + staticLibraryFolderName,
            "-l",
            "ev3api",
            "-static-libstdc++",
            "-std=c++11",
            "-pthread",

            // flags to minimize binary size (ex: remove unused functions)
            "-Os",
            "-fdata-sections",
            "-ffunction-sections",
            "-Wl,--gc-sections",
            "-s",
        };
    }

    private boolean executeCompilation(String[] compilerArguments) {
        ProcessBuilder processBuilder = getProcessBuilder(compilerArguments);
        try {
            final Process p = processBuilder.start();
            final int exitCode = p.waitFor();
            if ( exitCode != 0 ) {
                LOG.error("c4ev3 compiler exited with non zero code: " + exitCode);
                return false;
            }
            return true;
        } catch ( IOException | InterruptedException e ) {
            LOG.error("c4ev3 compilation failed", e);
            return false;
        }
    }

    private ProcessBuilder getProcessBuilder(String[] compilerArguments) {
        ProcessBuilder processBuilder = new ProcessBuilder(compilerArguments);
        processBuilder.redirectInput(ProcessBuilder.Redirect.INHERIT);
        processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT);
        processBuilder.redirectError(ProcessBuilder.Redirect.INHERIT);
        return processBuilder;
    }
}
