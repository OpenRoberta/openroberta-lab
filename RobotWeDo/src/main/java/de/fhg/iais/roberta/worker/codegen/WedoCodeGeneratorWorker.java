package de.fhg.iais.roberta.worker.codegen;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.codegen.WeDoStackMachineVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineVisitor;
import de.fhg.iais.roberta.worker.AbstractStackMachineGeneratorWorker;

public final class WedoCodeGeneratorWorker extends AbstractStackMachineGeneratorWorker {
    @Override
    protected AbstractStackMachineVisitor<Void> getVisitor(Project project, UsedHardwareBean usedHardwareBean) {
        return new WeDoStackMachineVisitor<>(usedHardwareBean, project.getConfigurationAst(), project.getProgramAst().getTree());
    }
}
