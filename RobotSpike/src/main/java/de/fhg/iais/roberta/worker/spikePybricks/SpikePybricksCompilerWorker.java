package de.fhg.iais.roberta.worker.spikePybricks;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.bean.CompilerSetupBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.basic.Pair;
import de.fhg.iais.roberta.worker.ICompilerWorker;

public class SpikePybricksCompilerWorker implements ICompilerWorker {
    private static final Logger LOG = LoggerFactory.getLogger(SpikePybricksCompilerWorker.class);

    @Override
    public void execute(Project project) {
        String programName = project.getProgramName();
        String robot = project.getRobot();

        Pair<Key, String> workflowResult = this.runBuild(project);
        project.setResult(workflowResult.getFirst());

        if ( workflowResult.getFirst() == Key.COMPILERWORKFLOW_SUCCESS ) {
            project.setCompiledHex(workflowResult.getSecond());
            LOG.info("compile {} program {} successful", robot, programName);
        } else {
            project.addResultParam("MESSAGE", workflowResult.getSecond());
            LOG.error("compile of program {} for robot {} failed with message: {}", programName, robot, workflowResult);
        }
    }

    private Pair<Key, String> runBuild(Project project) {
        CompilerSetupBean compilerWorkflowBean = project.getWorkerResult(CompilerSetupBean.class);
        String compilerResourcesDir = compilerWorkflowBean.getCompilerResourcesDir();
        String sourceCode = project.getSourceCodeBuilder().toString();
        String mpyCrossVersion = "6";

        String[] executableWithParameters =
            {
                "python",
                compilerResourcesDir + "compile.py",
                sourceCode,
                mpyCrossVersion
            };

        Pair<String, String> result = this.getBinaryFromCrossCompiler(executableWithParameters);
        String status = result.getFirst();
        String compiledhex = result.getSecond();

        if ( status.equals("success") ) {
            return Pair.of(Key.COMPILERWORKFLOW_SUCCESS, compiledhex);
        } else {
            Util.logCrosscompilerError(LOG, "no binary returned", sourceCode, project.isNativeEditorCode());
            return Pair.of(Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED, compiledhex);
        }
    }

    protected final Pair<String, String> getBinaryFromCrossCompiler(String[] executableWithParameters) {
        try {
            ProcessBuilder procBuilder = new ProcessBuilder(executableWithParameters);
            procBuilder.redirectErrorStream(true);
            procBuilder.redirectInput(ProcessBuilder.Redirect.INHERIT);
            procBuilder.redirectOutput(ProcessBuilder.Redirect.PIPE);
            Process p = procBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));
            String status = reader.readLine();
            String compiledHex = reader.lines().collect(Collectors.joining());
            p.waitFor();
            p.destroy();
            return Pair.of(status, compiledHex);
        } catch ( Exception e ) {
            LOG.error("exception when calling the cross compiler", e);
            return null;
        }
    }
}
