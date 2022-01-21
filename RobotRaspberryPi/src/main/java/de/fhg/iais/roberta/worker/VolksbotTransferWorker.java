package de.fhg.iais.roberta.worker;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.bean.CompilerSetupBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.components.VolksbotCommunicator;
import de.fhg.iais.roberta.util.Key;

public class VolksbotTransferWorker implements IWorker {
    private static final Logger LOG = LoggerFactory.getLogger(VolksbotTransferWorker.class);

    @Override
    public void execute(Project project) {
        CompilerSetupBean compilerWorkflowBean = project.getWorkerResult(CompilerSetupBean.class);
        String pathToSource = compilerWorkflowBean.getTempDir() + project.getToken() + File.separator + project.getProgramName() + File.separator + "source";
        String program = project.getProgramName() + "." + project.getBinaryFileExtension();
        String pathToResource = compilerWorkflowBean.getCompilerResourcesDir();
        VolksbotCommunicator communicator = new VolksbotCommunicator(pathToSource, pathToResource, program);
        try {
            communicator.runProgram();
            project.setResult(Key.COMPILERWORKFLOW_SUCCESS);
        } catch ( Exception e ) {
            LOG.error("Running the generated program to failed", e);
            project.setResult(Key.VOLKSBOT_PROGRAM_RUN_ERROR);
        }
    }
}
