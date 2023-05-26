package de.fhg.iais.roberta.worker.codegen;

import de.fhg.iais.roberta.bean.NNBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.codegen.MicrobitStackMachineVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineVisitor;

public class MicrobitStackMachineGeneratorWorker extends CalliopeStackMachineGeneratorWorker {
    @Override
    protected AbstractStackMachineVisitor getVisitor(Project project, UsedHardwareBean usedHardwareBean, NNBean nnBean) {
        return new MicrobitStackMachineVisitor(project.getConfigurationAst(), project.getProgramAst().getTree(), usedHardwareBean, nnBean);
    }
}
