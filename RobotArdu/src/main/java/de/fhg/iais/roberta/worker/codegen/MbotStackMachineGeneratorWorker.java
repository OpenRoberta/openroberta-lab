package de.fhg.iais.roberta.worker.codegen;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.codegen.MbotCppStackMachineVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineVisitor;
import de.fhg.iais.roberta.worker.AbstractStackMachineGeneratorWorker;

public class MbotStackMachineGeneratorWorker extends AbstractStackMachineGeneratorWorker {
    @Override
    protected AbstractStackMachineVisitor<Void> getVisitor(Project project, UsedHardwareBean usedHardwareBean) {
        return new MbotCppStackMachineVisitor<>(project.getConfigurationAst(), project.getProgramAst().getTree());

    }
}
