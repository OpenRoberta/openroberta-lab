package de.fhg.iais.roberta.worker;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.bean.CompilerSetupBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Util;

public class Txt4CompilerWorker implements ICompilerWorker {
    private static final Logger LOG = LoggerFactory.getLogger(Txt4CompilerWorker.class);

    @Override
    public void execute(Project project) {
        project.setResult(this.runBuild(project));
    }

    private Key runBuild(Project project) {

        String sourceCode = project.getSourceCode().toString();
        project.setCompiledHex(sourceCode);

        if ( project.getCompiledHex() != null && !project.getCompiledHex().isEmpty() ) {
            return Key.COMPILERWORKFLOW_SUCCESS;
        } else {
            Util.logCrosscompilerError(LOG, "no binary returned", sourceCode, project.isNativeEditorCode());
            return Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
        }
    }
}
