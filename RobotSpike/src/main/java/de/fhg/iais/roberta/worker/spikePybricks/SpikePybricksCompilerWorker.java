package de.fhg.iais.roberta.worker.spikePybricks;

import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.bean.CompilerSetupBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.worker.ICompilerWorker;

public class SpikePybricksCompilerWorker implements ICompilerWorker {
    private static final Logger LOG = LoggerFactory.getLogger(SpikePybricksCompilerWorker.class);

    @Override
    public void execute(Project project) {
        project.setResult(this.runBuild(project));
    }

    private Key runBuild(Project project) {
        CompilerSetupBean compilerWorkflowBean = project.getWorkerResult(CompilerSetupBean.class);
        String compilerResourcesDir = compilerWorkflowBean.getCompilerResourcesDir();
        String sourceCode = project.getSourceCode().toString();

        String[] executableWithParameters =
            {
                "python",
                compilerResourcesDir + "compile.py",
                sourceCode
            };

        project.setCompiledHex(this.getBinaryFromCrossCompiler(executableWithParameters));

        if ( project.getCompiledHex() != null && !project.getCompiledHex().isEmpty() ) {
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
            procBuilder.redirectInput(ProcessBuilder.Redirect.INHERIT);
            procBuilder.redirectOutput(ProcessBuilder.Redirect.PIPE);
            Process p = procBuilder.start();
            String compiledHex = IOUtils.toString(p.getInputStream(), StandardCharsets.US_ASCII);
            p.waitFor();
            p.destroy();
            return compiledHex;
        } catch ( Exception e ) {
            LOG.error("exception when calling the cross compiler", e);
            return null;
        }
    }
}
