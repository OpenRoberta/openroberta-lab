package de.fhg.iais.roberta.worker;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.ThymioStackMachineVisitor;

public class ThymioStackMachineGeneratorWorker extends AbstractStackMachineGeneratorWorker {

    @Override
    protected ThymioStackMachineVisitor getVisitor(Project project, UsedHardwareBean usedHardwareBean) {
        return new ThymioStackMachineVisitor(project.getConfigurationAst(), project.getProgramAst().getTree());
    }
}
