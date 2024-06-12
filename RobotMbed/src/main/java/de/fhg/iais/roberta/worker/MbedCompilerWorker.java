package de.fhg.iais.roberta.worker;

import java.lang.ProcessBuilder.Redirect;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.bean.CompilerSetupBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Util;

public abstract class MbedCompilerWorker implements ICompilerWorker {

    private static final Logger LOG = LoggerFactory.getLogger(MbedCompilerWorker.class);
    private final String robotType;

    public MbedCompilerWorker(String robotType) {
        this.robotType = robotType;
    }

    @Override
    public void execute(Project project) {
        this.runBuild(project);
    }

    /**
     * run the build and create the complied hex file
     */
    private Key runBuild(Project project) {
//        CompilerSetupBean compilerWorkflowBean = project.getWorkerResult(CompilerSetupBean.class);
//        String compilerBinDir = compilerWorkflowBean.getCompilerBinDir();
//        String compilerResourcesDir = compilerWorkflowBean.getCompilerResourcesDir();
//
//        String scriptName = compilerResourcesDir + "/compile.py";
//        String runtimeHexDir = compilerResourcesDir + "/runtimeHex";
//
//        String[] executableWithParameters =
//            {
//                compilerBinDir + "python",
//                scriptName,
//                sourceCode,
//                runtimeHexDir,
//                robotType
//            };
        String sourceCode = project.getSourceCode().toString();
        project.setCompiledHex(sourceCode);
        if ( project.getCompiledHex() != null ) {
            return Key.COMPILERWORKFLOW_SUCCESS;
        } else {
            Util.logCrosscompilerError(LOG, "no binary returned", sourceCode, project.isNativeEditorCode());
            return Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
        }
    }

    protected final String getBinaryFromCrossCompiler(String[] executableWithParameters) {
        try {
            ProcessBuilder procBuilder = new ProcessBuilder(executableWithParameters);
            procBuilder.redirectErrorStream(true);
            procBuilder.redirectInput(Redirect.INHERIT);
            procBuilder.redirectOutput(Redirect.PIPE);
            Process p = procBuilder.start();
            String compiledHex = IOUtils.toString(p.getInputStream(), StandardCharsets.US_ASCII);
            p.waitFor();
            p.destroy();
            if ( p.waitFor() != 0 ) {
                throw new RuntimeException(compiledHex);
            }
            return compiledHex;
        } catch ( Exception e ) {
            LOG.error("exception when calling the cross compiler: {}", e.getMessage());
            return null;
        }
    }
}
