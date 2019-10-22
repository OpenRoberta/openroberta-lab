package de.fhg.iais.roberta.worker;

import de.fhg.iais.roberta.bean.UsedHardwareBean.Builder;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.collect.RaspberryPiUsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractCollectorVisitor;

public final class RaspberryPiUsedHardwareCollectorWorker extends AbstractUsedHardwareCollectorWorker {
    @Override
    protected AbstractCollectorVisitor getVisitor(Builder builder, Project project) {
        return new RaspberryPiUsedHardwareCollectorVisitor(builder, project.getProgramAst().getTree(), project.getConfigurationAst());
    }
}