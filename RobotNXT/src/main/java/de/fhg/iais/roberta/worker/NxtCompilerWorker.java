package de.fhg.iais.roberta.worker;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.bean.CompilerSetupBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.util.Util;

public class NxtCompilerWorker implements IWorker {

    private static final Logger LOG = LoggerFactory.getLogger(NxtCompilerWorker.class);

    @Override
    public void execute(Project project) {
        String programName = project.getProgramName();
        String robot = project.getRobot();
        Pair<Key, String> workflowResult = this.runBuild(project);
        project.setResult(workflowResult.getFirst());
        project.addResultParam("MESSAGE", workflowResult.getSecond());
        if ( workflowResult.getFirst() == Key.COMPILERWORKFLOW_SUCCESS ) {
            LOG.info("compile {} program {} successful", robot, programName);
        } else {
            LOG.error("compile {} program {} failed with {}", robot, programName, workflowResult);
        }
    }

    /**
     * create command to call the cross compiler and execute the call.
     *
     * @return a pair of Key.COMPILERWORKFLOW_SUCCESS or Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED and the cross compiler output
     */
    private Pair<Key, String> runBuild(Project project) {
        final String token = project.getToken();
        final String mainFile = project.getProgramName();
        final CompilerSetupBean compilerWorkflowBean = project.getWorkerResult(CompilerSetupBean.class);
        final String compilerResourcesDir = compilerWorkflowBean.getCompilerResourcesDir();
        final String tempDir = compilerWorkflowBean.getTempDir();
        final String crosscompilerSource = project.getSourceCode().toString();
        Util.storeGeneratedProgram(tempDir, crosscompilerSource, project.getToken(), project.getProgramName(), "." + project.getSourceCodeFileExtension());

        Path path = Paths.get(compilerResourcesDir);
        Path base = Paths.get("");

        String nbcCompilerFileName = compilerResourcesDir + "/windows/nbc.exe";
        if ( SystemUtils.IS_OS_LINUX ) {
            nbcCompilerFileName = "nbc";
        } else if ( SystemUtils.IS_OS_MAC ) {
            nbcCompilerFileName = compilerResourcesDir + "/osx/nbc";
        }

        String[] executableWithParameters =
            {
                nbcCompilerFileName,
                "-q",
                "-sm-",
                tempDir + token + "/" + mainFile + "/source/" + mainFile + "." + project.getSourceCodeFileExtension(),
                "-O=" + tempDir + token + "/" + mainFile + "/target/" + mainFile + "." + project.getBinaryFileExtension(),
                "-I=" + base.resolve(path).toAbsolutePath().normalize()
            };
        Pair<Boolean, String> result = Util.runCrossCompiler(executableWithParameters, crosscompilerSource);
        Key resultKey = result.getFirst() ? Key.COMPILERWORKFLOW_SUCCESS : Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
        return Pair.of(resultKey, result.getSecond());
    }
}
