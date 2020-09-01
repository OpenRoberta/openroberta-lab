package de.fhg.iais.roberta.worker;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.bean.CompilerSetupBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util;

/**
 * The workflow for the Edison compiler. Blockly blocks are first converted into EdPy Python2 code and then the code is converted into a WAV audio file. See
 * also: https://github.com/Bdanilko/EdPy
 */
public class EdisonCompilerWorker implements IWorker {
    private static final Logger LOG = LoggerFactory.getLogger(EdisonCompilerWorker.class);

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
     * Builds the WAV file from the .py source file using the EdPy Python2 compiler (https://github.com/OpenRoberta/EdPy) by starting an external Python2
     * process. The file will be stored as {@link PluginProperties#getTempDir()}/token/source/XXXX.wav and also in {@link #compiledWav} as a Base64 String.
     *
     * @return a Key that gives information about the building process (success, failure, interrupted,...)
     */
    private Pair<Key, String> runBuild(Project project) {
        final CompilerSetupBean compilerWorkflowBean = project.getWorkerResult(CompilerSetupBean.class);
        final String compilerResourcesDir = compilerWorkflowBean.getCompilerResourcesDir();
        final String tempDir = compilerWorkflowBean.getTempDir();
        final String crosscompilerSource = project.getSourceCode().toString();
        Util.storeGeneratedProgram(tempDir, crosscompilerSource, project.getToken(), project.getProgramName(), "." + project.getSourceCodeFileExtension());
        //get all directories
        String token = project.getToken();
        String pyFile = project.getProgramName();
        String sourceFilePath = tempDir + "/" + token + "/" + pyFile + "/source/";
        String targetFilePath = tempDir + "/" + token + "/" + pyFile + "/target/";

        //build and start the Python process
        String[] executableWithParameters =
            {
                "python2",
                compilerResourcesDir + "EdPy.py",
                compilerResourcesDir + "en_lang.json",
                sourceFilePath + pyFile + ".py",
                "-t",
                targetFilePath + pyFile + ".wav"
            };

        Pair<Boolean, String> result = Util.runCrossCompiler(executableWithParameters, crosscompilerSource);
        Key resultKey = result.getFirst() ? Key.COMPILERWORKFLOW_SUCCESS : Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
        if ( result.getFirst() ) {
            try {
                byte[] wavBytes = FileUtils.readFileToByteArray(new File(targetFilePath + pyFile + "." + project.getBinaryFileExtension()));
                project.setCompiledHex(Base64.getEncoder().encodeToString(wavBytes));
                resultKey = Key.COMPILERWORKFLOW_SUCCESS;
            } catch ( IOException e ) {
                LOG.error("Compilation successful, but reading WAV file failed (IOException)", e);
                resultKey = Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
            }
        }
        return Pair.of(resultKey, result.getSecond());
    }
}