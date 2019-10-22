package de.fhg.iais.roberta.worker.collect;

import de.fhg.iais.roberta.bean.UsedHardwareBean.Builder;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.collect.Ev3UsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractCollectorVisitor;
import de.fhg.iais.roberta.worker.AbstractUsedHardwareCollectorWorker;

public final class Ev3UsedHardwareCollectorWorker extends AbstractUsedHardwareCollectorWorker {
    @Override
    protected AbstractCollectorVisitor getVisitor(Builder builder, Project project) {
        return new Ev3UsedHardwareCollectorVisitor(builder, project.getProgramAst().getTree(), project.getConfigurationAst());
    }
}