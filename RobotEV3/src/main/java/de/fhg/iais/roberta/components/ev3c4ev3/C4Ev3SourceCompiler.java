package de.fhg.iais.roberta.components.ev3c4ev3;

import org.apache.commons.lang3.SystemUtils;

import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class C4Ev3SourceCompiler {
    private final String compilerResourcesDir;
    private final String compilerExecutableFileName;
    private final String staticLibraryFolderName;
    private final boolean isNativeEditorCode;

    public C4Ev3SourceCompiler(String compilerResourcesDir, boolean isNativeEditorCode) {
        this.compilerResourcesDir = compilerResourcesDir;
        this.compilerExecutableFileName = getCompilerExecutableFileName();
        this.staticLibraryFolderName = getStaticLibraryFolderName();
        this.isNativeEditorCode = isNativeEditorCode;
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
        if ( this.compilerExecutableFileName.contains("uclibc") ) {
            /*
             * Since we use a compiler built against uclibc on raspberry, we also need to use a different static library
             * of c4ev3, one built with a uclibc compiler
             */
            return "lib-uclibc";
        }
        return "lib";
    }

    public Pair<Boolean, String> compile(String sourceCodeFileName, String binaryOutputFile, String crosscompilerSourceForDebuggingOnly) {
        String[] compilerArguments = getCompilerArguments(this.compilerExecutableFileName, sourceCodeFileName, binaryOutputFile);
        return Util.runCrossCompiler(compilerArguments, crosscompilerSourceForDebuggingOnly, this.isNativeEditorCode);
    }

    private String[] getCompilerArguments(String compilerExecutableFileName, String sourceCodeFileName, String binaryOutputFile) {
        return new String[] {
            compilerExecutableFileName,
            sourceCodeFileName,
            "-o",
            binaryOutputFile,
            "-I",
            this.compilerResourcesDir + "c4ev3/include/NEPO",
            "-I",
            this.compilerResourcesDir + "c4ev3/include/c4ev3",
            "-L",
            this.compilerResourcesDir + "c4ev3/" + this.staticLibraryFolderName,
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
