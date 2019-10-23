package de.fhg.iais.roberta.worker;

import de.fhg.iais.roberta.bean.CompilerSetupBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Util1;

public class SaveWorker implements IWorker {
    @Override
    public void execute(Project project) {
        CompilerSetupBean compilerWorkflowBean = (CompilerSetupBean) project.getWorkerResult("CompilerSetup");
        final String tempDir = compilerWorkflowBean.getTempDir();
        Util1
            .storeGeneratedProgram(tempDir, project.getSourceCode().toString(), project.getToken(), project.getProgramName(), "." + project.getFileExtension());
        project.setResult(Key.COMPILERWORKFLOW_SUCCESS);
    }
}
