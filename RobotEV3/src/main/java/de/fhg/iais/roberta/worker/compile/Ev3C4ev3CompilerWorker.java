package de.fhg.iais.roberta.worker.compile;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.bean.CompilerSetupBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.components.ev3c4ev3.C4Ev3SourceCompiler;
import de.fhg.iais.roberta.components.ev3c4ev3.Uf2Builder;
import de.fhg.iais.roberta.components.ev3c4ev3.Uf2FileContainer;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.worker.IWorker;

public class Ev3C4ev3CompilerWorker implements IWorker {

    private static final Logger LOG = LoggerFactory.getLogger(Ev3C4ev3CompilerWorker.class);

    @Override
    public void execute(Project project) {
        CompilerSetupBean compilerWorkflowBean = project.getWorkerResult(CompilerSetupBean.class);
        String robot = project.getRobot();
        String compilerResourcesDir = compilerWorkflowBean.getCompilerResourcesDir();
        String programName = project.getProgramName();
        C4Ev3SourceCompiler compiler = new C4Ev3SourceCompiler(compilerResourcesDir);
        Uf2Builder uf2Builder = new Uf2Builder(compilerResourcesDir);
        Pair<Key, String> workflowResult = this.runBuild(project, compiler, uf2Builder);
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
    private Pair<Key, String> runBuild(Project project, C4Ev3SourceCompiler compiler, Uf2Builder uf2Builder) {
        final CompilerSetupBean compilerWorkflowBean = project.getWorkerResult(CompilerSetupBean.class);
        final String tempDir = compilerWorkflowBean.getTempDir();
        final String token = project.getToken();
        final String programName = project.getProgramName();
        final String crosscompilerSource = project.getSourceCode().toString();
        Util.storeGeneratedProgram(tempDir, crosscompilerSource, token, programName, "." + project.getSourceCodeFileExtension());
        String sourceCodeFileName = tempDir + token + "/" + programName + "/source/" + programName + "." + project.getSourceCodeFileExtension();
        String binaryFileName = tempDir + token + "/" + programName + "/target/" + programName + ".elf";
        Pair<Boolean, String> result = compiler.compile(sourceCodeFileName, binaryFileName, crosscompilerSource);
        Key resultKey = result.getFirst() ? Key.COMPILERWORKFLOW_SUCCESS : Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
        if ( result.getFirst() ) {
            try {
                Uf2FileContainer uf2 = uf2Builder.createUf2File(programName, binaryFileName);
                project.setCompiledHex(uf2.toBase64());
                resultKey = Key.COMPILERWORKFLOW_SUCCESS;
            } catch ( IOException e ) {
                LOG.error("Could not create uf2", e);
                resultKey = Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
            }
        }
        return Pair.of(resultKey, result.getSecond());
    }
}
