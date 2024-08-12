package de.fhg.iais.roberta.worker.compile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.worker.ICompilerWorker;

/**
 * The workflow for the Edison compiler. Blockly blocks are first converted into EdPy Python2 code and then the code is converted into a WAV audio file. See
 * also: https://github.com/Bdanilko/EdPy
 */
public class EdisonCompilerWorker implements ICompilerWorker {
    private static final Logger LOG = LoggerFactory.getLogger(EdisonCompilerWorker.class);

    @Override
    public void execute(Project project) {
        String sourceCode = project.getSourceCodeBuilder().toString();
        project.setCompiledHex(sourceCode);
        if (!sourceCode.isEmpty()) {
            LOG.info("compile {} program {} successful", project.getRobot(), project.getProgramName());
            project.setResult(Key.COMPILERWORKFLOW_SUCCESS);
        } else {
            LOG.error("compile {} program {} failed with {}", project.getRobot(), project.getProgramName());
            project.setResult(Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED);
        }
    }
}
