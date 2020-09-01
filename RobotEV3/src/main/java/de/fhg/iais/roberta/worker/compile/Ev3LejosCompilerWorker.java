package de.fhg.iais.roberta.worker.compile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.bean.CompilerSetupBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.components.ev3lejos.JavaSourceCompiler;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.worker.IWorker;

public class Ev3LejosCompilerWorker implements IWorker {

    private static final Logger LOG = LoggerFactory.getLogger(Ev3LejosCompilerWorker.class);

    @Override
    public void execute(Project project) {
        final CompilerSetupBean compilerWorkflowBean = project.getWorkerResult(CompilerSetupBean.class);
        final String compilerResourcesDir = compilerWorkflowBean.getCompilerResourcesDir();
        final String tempDir = compilerWorkflowBean.getTempDir();
        final String crosscompilerSource = project.getSourceCode().toString();
        Util.storeGeneratedProgram(tempDir, crosscompilerSource, project.getToken(), project.getProgramName(), ".java");

        JavaSourceCompiler scp = new JavaSourceCompiler(project.getProgramName(), project.getSourceCode().toString(), compilerResourcesDir);
        scp.compileAndPackage(tempDir, project.getToken());
        if ( scp.isSuccess() ) {
            LOG.info("jar for program {} generated successfully", project.getProgramName());
            project.setResult(Key.COMPILERWORKFLOW_SUCCESS);
            project.addResultParam("MESSAGE", "");
        } else {
            Util.logCrosscompilerError(LOG, scp.getCompilerResponse(), crosscompilerSource);
            project.setResult(Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED);
            project.addResultParam("MESSAGE", scp.getCompilerResponse());
        }
    }
}
