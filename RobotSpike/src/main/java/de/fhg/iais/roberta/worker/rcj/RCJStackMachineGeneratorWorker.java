package de.fhg.iais.roberta.worker.rcj;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.NNBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.RCJStackMachineVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractLanguageVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineVisitor;
import de.fhg.iais.roberta.visitor.spikePybricks.SpikePybricksPythonVisitor;
import de.fhg.iais.roberta.worker.AbstractLanguageGeneratorWorker;
import de.fhg.iais.roberta.worker.AbstractStackMachineGeneratorWorker;

public final class RCJStackMachineGeneratorWorker extends AbstractStackMachineGeneratorWorker {

    @Override
    protected AbstractStackMachineVisitor getVisitor(Project project, UsedHardwareBean usedHardwareBean, NNBean nnBean) {
        return new RCJStackMachineVisitor(project.getConfigurationAst(), project.getProgramAst().getTree(), usedHardwareBean, nnBean);

    }
}
