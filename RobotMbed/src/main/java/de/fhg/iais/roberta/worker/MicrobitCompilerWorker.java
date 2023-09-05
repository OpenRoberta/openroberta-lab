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

public abstract class MicrobitCompilerWorker implements ICompilerWorker {

    private static final Logger LOG = LoggerFactory.getLogger(MicrobitCompilerWorker.class);
    private final String version;

    public MicrobitCompilerWorker(String version) {
        this.version = version;
    }

    @Override
    public void execute(Project project) {
        this.runBuild(project);
    }

    /**
     * run the build and create the complied hex file
     */
    private Key runBuild(Project project) {
        final CompilerSetupBean compilerWorkflowBean = project.getWorkerResult(CompilerSetupBean.class);
        final String tempDir = compilerWorkflowBean.getTempDir();
        String compilerBinDir = compilerWorkflowBean.getCompilerBinDir();
        String compilerResourcesDir = compilerWorkflowBean.getCompilerResourcesDir();
        String sourceCode = project.getSourceCode().toString();
        String scriptName = compilerResourcesDir + "/compile.py";

        String[] executableWithParameters =
                {
                        compilerBinDir + "python",
                        scriptName,
                        sourceCode,
                        version
                };
        
        Util.storeGeneratedProgram(tempDir, this.getBinaryFromCrossCompiler(executableWithParameters), project.getToken(), project.getProgramName(), "." + project.getBinaryFileExtension());
        return Key.COMPILERWORKFLOW_SUCCESS;
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
            return compiledHex;
        } catch ( Exception e ) {
            LOG.error("exception when calling the cross compiler", e);
            return null;
        }
    }
}
