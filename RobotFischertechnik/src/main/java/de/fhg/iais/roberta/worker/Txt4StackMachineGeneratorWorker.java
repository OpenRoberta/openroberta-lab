package de.fhg.iais.roberta.worker;

import de.fhg.iais.roberta.bean.NNBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.Txt4StackMachineVisitor;

public class Txt4StackMachineGeneratorWorker extends AbstractStackMachineGeneratorWorker {

    @Override
    protected Txt4StackMachineVisitor getVisitor(Project project, UsedHardwareBean usedHardwareBean, NNBean nnBean) {
        return new Txt4StackMachineVisitor(project.getConfigurationAst(), project.getProgramAst().getTree(), usedHardwareBean, nnBean);
    }
}
