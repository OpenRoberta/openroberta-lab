package de.fhg.iais.roberta.worker;

import java.lang.ProcessBuilder.Redirect;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.bean.CompilerSetupBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Util;

public class MicrobitCompilerWorker implements IWorker {

    private static final Logger LOG = LoggerFactory.getLogger(MicrobitCompilerWorker.class);

    @Override
    public void execute(Project project) {
        this.runBuild(project);
    }

    /**
     * run the build and create the complied hex file
     */
    private Key runBuild(Project project) {
        CompilerSetupBean compilerWorkflowBean = project.getWorkerResult(CompilerSetupBean.class);
        String compilerBinDir = compilerWorkflowBean.getCompilerBinDir();
        String compilerResourcesDir = compilerWorkflowBean.getCompilerResourcesDir();
        String sourceCode = project.getSourceCode().toString();

        String scriptName = compilerResourcesDir + "/compile.py";

        String[] executableWithParameters =
            {
                compilerBinDir + "python",
                scriptName,
                sourceCode
            };
        project.setCompiledHex(this.getBinaryFromCrossCompiler(executableWithParameters));
        if ( project.getCompiledHex() != null ) {
            return Key.COMPILERWORKFLOW_SUCCESS;
        } else {
            Util.logCrosscompilerError(LOG, "no binary returned", sourceCode);
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
            String compiledHex = IOUtils.toString(p.getInputStream(), "US-ASCII");
            p.waitFor();
            p.destroy();
            return compiledHex;
        } catch ( Exception e ) {
            LOG.error("exception when calling the cross compiler", e);
            return null;
        }
    }
}
