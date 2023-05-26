package de.fhg.iais.roberta.worker.codegen;

import de.fhg.iais.roberta.bean.NNBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.codegen.Rob3rtaStackMachineVisitor;
import de.fhg.iais.roberta.worker.AbstractStackMachineGeneratorWorker;

public class Rob3rtaStackMachineGeneratorWorker extends AbstractStackMachineGeneratorWorker {

    @Override
    protected Rob3rtaStackMachineVisitor getVisitor(Project project, UsedHardwareBean usedHardwareBean, NNBean nnBean) {
        return new Rob3rtaStackMachineVisitor(project.getConfigurationAst(), project.getProgramAst().getTree(), usedHardwareBean, nnBean);
    }
}
