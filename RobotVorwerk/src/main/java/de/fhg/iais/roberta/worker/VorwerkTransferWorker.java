package de.fhg.iais.roberta.worker;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.bean.CompilerSetupBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.components.vorwerk.VorwerkCommunicator;
import de.fhg.iais.roberta.util.Key;

public class VorwerkTransferWorker implements IWorker {
    private static final Logger LOG = LoggerFactory.getLogger(VorwerkTransferWorker.class);

    @Override
    public void execute(Project project) {
        CompilerSetupBean compilerWorkflowBean = project.getWorkerResult(CompilerSetupBean.class);
        VorwerkCommunicator vorwerkCommunicator = new VorwerkCommunicator(compilerWorkflowBean.getCompilerResourcesDir());
        vorwerkCommunicator.setCredentials(project.getConfigurationAst().getIpAddress(), project.getConfigurationAst().getUserName(), project.getConfigurationAst().getPassword());
        final String tempDir = compilerWorkflowBean.getTempDir();
        try {
            String programLocation = tempDir + project.getToken() + File.separator + project.getProgramName() + File.separator + "source";
            vorwerkCommunicator.uploadFile(programLocation, project.getProgramName() + "." + project.getSourceCodeFileExtension());
            project.setResult(Key.COMPILERWORKFLOW_SUCCESS);
        } catch ( Exception e ) {
            LOG.error("Uploading the generated program to {} failed", vorwerkCommunicator.getIp(), e);
            project.setResult(Key.VORWERK_PROGRAM_UPLOAD_ERROR);
        }
    }
}
