package de.fhg.iais.roberta.worker;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractLanguageVisitor;

/**
 * Uses the {@link AbstractLanguageVisitor} to visit the current AST and generate the robot's specific source code.
 */
public abstract class AbstractLanguageGeneratorWorker implements IWorker {

    /**
     * Returns the appropriate visitor for this worker. Used by subclasses to keep the execute method generic.
     * Could be removed in the future, when visitors are specified in the properties as well, or inferred from the worker name.
     *
     * @param usedHardwareBean the used hardware bean
     * @param codeGeneratorSetupBean the code generator setup bean
     * @param project the project
     * @return the appropriate visitor for the current robot
     */
    protected abstract AbstractLanguageVisitor getVisitor(UsedHardwareBean usedHardwareBean, CodeGeneratorSetupBean codeGeneratorSetupBean, Project project);

    @Override
    public void execute(Project project) {
        Object usedHardwareBean = project.getWorkerResult("CollectedHardware");
        Object codeGeneratorSetupBean = project.getWorkerResult("CodeGeneratorSetup");
        AbstractLanguageVisitor visitor = getVisitor((UsedHardwareBean) usedHardwareBean, (CodeGeneratorSetupBean) codeGeneratorSetupBean, project);
        visitor.setStringBuilders(project.getSourceCode(), project.getIndentation());
        visitor.generateCode(project.isWithWrapping());
        project.setResult(Key.COMPILERWORKFLOW_PROGRAM_GENERATION_SUCCESS);
    }
}
