package de.fhg.iais.roberta.worker;

import de.fhg.iais.roberta.bean.UsedHardwareBean.Builder;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.collect.MbedUsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractCollectorVisitor;

public final class MbedUsedHardwareCollectorWorker extends AbstractUsedHardwareCollectorWorker {
    @Override
    protected AbstractCollectorVisitor getVisitor(Builder builder, Project project) {
        return new MbedUsedHardwareCollectorVisitor(builder, project.getProgramAst().getTree(), project.getConfigurationAst());
    }
}