package de.fhg.iais.roberta.worker;

import de.fhg.iais.roberta.bean.CompilerSetupBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Util;

/**
 * Saves the generated program directly to the temporary directory for further processing.
 * Used by robots/languages that need no compilation like ev3dev.
 */
public class SaveWorker implements IWorker {
    @Override
    public final void execute(Project project) {
        CompilerSetupBean compilerWorkflowBean = project.getWorkerResult(CompilerSetupBean.class);
        String tempDir = compilerWorkflowBean.getTempDir();
        Util
            .storeGeneratedProgram(tempDir, project.getSourceCode().toString(), project.getToken(), project.getProgramName(), "." + project.getSourceCodeFileExtension());
        project.setResult(Key.COMPILERWORKFLOW_SUCCESS);
    }
}
