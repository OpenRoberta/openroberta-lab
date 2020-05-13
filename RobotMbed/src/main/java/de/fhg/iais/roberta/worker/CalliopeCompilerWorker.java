package de.fhg.iais.roberta.worker;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.bean.CompilerSetupBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.util.Util;

public class CalliopeCompilerWorker implements IWorker {
    private static final Logger LOG = LoggerFactory.getLogger(CalliopeCompilerWorker.class);

    @Override
    public void execute(Project project) {
        String programName = project.getProgramName();
        String robot = project.getRobot();

        // TODO: check how to do this sensibly, without having the UsedHardwareWorker beforehand
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
        final CompilerSetupBean compilerWorkflowBean = project.getWorkerResult(CompilerSetupBean.class);
        final String compilerBinDir = compilerWorkflowBean.getCompilerBinDir();
        final String compilerResourcesDir = compilerWorkflowBean.getCompilerResourcesDir();
        final String tempDir = compilerWorkflowBean.getTempDir();
        final String crosscompilerSource = project.getSourceCode().toString();
        Util.storeGeneratedProgram(tempDir, crosscompilerSource, project.getToken(), project.getProgramName(), "." + project.getSourceCodeFileExtension());
        String scriptName = compilerResourcesDir + "../compile." + (SystemUtils.IS_OS_WINDOWS ? "bat" : "sh");
        Boolean bluetooth = project.getRobot().equals("calliope2017");
        String bluetoothParam = bluetooth ? "-b" : "";
        Path pathToSrcFile = Paths.get(tempDir + project.getToken() + "/" + project.getProgramName());

        String[] executableWithParameters =
            {
                scriptName,
                compilerBinDir,
                project.getProgramName(),
                Paths.get("").resolve(pathToSrcFile).toAbsolutePath().normalize() + "/",
                compilerResourcesDir,
                bluetoothParam
            };

        Pair<Boolean, String> result = Util.runCrossCompiler(executableWithParameters, crosscompilerSource);
        Key resultKey = result.getFirst() ? Key.COMPILERWORKFLOW_SUCCESS : Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
        if ( result.getFirst() ) {
            try {
                project
                    .setCompiledHex(
                        FileUtils
                            .readFileToString(
                                new File(pathToSrcFile + "/target/" + project.getProgramName() + "." + project.getBinaryFileExtension()),
                                Charset.forName("utf-8")));
                resultKey = Key.COMPILERWORKFLOW_SUCCESS;
            } catch ( IOException e ) {
                LOG.error("compilation of Calliope program successful, but reading the binary failed", e);
                resultKey = Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
            }
        }
        return Pair.of(resultKey, result.getSecond());
    }
}
