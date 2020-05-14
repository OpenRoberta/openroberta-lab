package de.fhg.iais.roberta.components.ev3c4ev3;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class C4Ev3SourceCompiler {

    private static final Logger LOG = LoggerFactory.getLogger(C4Ev3SourceCompiler.class);

    private final String compilerResourcesDir;
    private final String staticLibraryFolderName;
    private final String compilerBinaryName;

    public C4Ev3SourceCompiler(String compilerResourcesDir, String compilerBinaryName) {
        this.compilerResourcesDir = compilerResourcesDir;
        this.staticLibraryFolderName = getStaticLibraryFolderName();
        this.compilerBinaryName = compilerBinaryName;
    }

    private String getStaticLibraryFolderName() {
        if ( compilerBinaryName.contains("uclibc") ) {
            /*
             * Since we use a compiler built against uclibc on raspberry, we also need to use a different static library
             * of c4ev3, one built with a uclibc compiler
             */
            return "lib-uclibc";
        }
        return "lib";
    }

    public Pair<Boolean, String> compile(String sourceCodeFileName, String binaryOutputFile, String crosscompilerSourceForDebuggingOnly) {
        String[] compilerArguments = getCompilerArguments(compilerBinaryName, sourceCodeFileName, binaryOutputFile);
        return Util.runCrossCompiler(compilerArguments, crosscompilerSourceForDebuggingOnly);
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
}
