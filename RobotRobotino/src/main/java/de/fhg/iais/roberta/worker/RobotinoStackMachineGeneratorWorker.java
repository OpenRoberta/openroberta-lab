package de.fhg.iais.roberta.worker;

import de.fhg.iais.roberta.bean.NNBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.RobotinoStackMachineVisitor;

public class RobotinoStackMachineGeneratorWorker extends AbstractStackMachineGeneratorWorker {

    @Override
    protected RobotinoStackMachineVisitor getVisitor(Project project, UsedHardwareBean usedHardwareBean, NNBean nnBean) {
        return new RobotinoStackMachineVisitor(project.getConfigurationAst(), project.getProgramAst().getTree(), usedHardwareBean, nnBean);
    }
}
