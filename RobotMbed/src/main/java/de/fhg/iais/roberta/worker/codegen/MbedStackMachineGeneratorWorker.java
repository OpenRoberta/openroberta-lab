package de.fhg.iais.roberta.worker.codegen;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.codegen.MbedStackMachineVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineVisitor;
import de.fhg.iais.roberta.worker.AbstractStackMachineGeneratorWorker;

public class MbedStackMachineGeneratorWorker extends AbstractStackMachineGeneratorWorker {
    @Override
    protected AbstractStackMachineVisitor getVisitor(Project project, UsedHardwareBean usedHardwareBean) {
        return new MbedStackMachineVisitor(project.getConfigurationAst(), project.getProgramAst().getTree());
    }
}
