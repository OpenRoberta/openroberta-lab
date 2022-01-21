package de.fhg.iais.roberta.worker;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.codegen.VolksbotStackMachineVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineVisitor;

public final class VolksbotStackMachineGeneratorWorker extends AbstractStackMachineGeneratorWorker {
    @Override
    protected AbstractStackMachineVisitor<Void> getVisitor(Project project, UsedHardwareBean usedHardwareBean) {
        return new VolksbotStackMachineVisitor<>(project.getConfigurationAst(), project.getProgramAst().getTree());
    }
}
