package de.fhg.iais.roberta.worker;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractLanguageVisitor;

public abstract class AbstractLanguageGeneratorWorker implements IWorker {

    @Override
    public void execute(Project project) {
        Object usedHardwareBean = project.getWorkerResult("CollectedHardware");
        Object codeGeneratorSetupBean = project.getWorkerResult("CodeGeneratorSetup");
        AbstractLanguageVisitor visitor = getVisitor((UsedHardwareBean) usedHardwareBean, (CodeGeneratorSetupBean) codeGeneratorSetupBean, project);
        visitor.setStringBuilders(project.getSourceCode(), project.getIndentation());
        visitor.generateCode(project.isWithWrapping());
        project.setResult(Key.COMPILERWORKFLOW_PROGRAM_GENERATION_SUCCESS);
    }

    protected abstract AbstractLanguageVisitor getVisitor(UsedHardwareBean usedHardwareBean, CodeGeneratorSetupBean codeGeneratorSetupBean, Project project);
}
