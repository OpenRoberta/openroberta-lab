package de.fhg.iais.roberta.worker.collect;

import de.fhg.iais.roberta.bean.UsedHardwareBean.Builder;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.collect.SenseboxUsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractCollectorVisitor;
import de.fhg.iais.roberta.worker.AbstractUsedHardwareCollectorWorker;

public final class SenseboxUsedHardwareCollectorWorker extends AbstractUsedHardwareCollectorWorker {
    @Override
    protected AbstractCollectorVisitor getVisitor(Builder builder, Project project) {
        return new SenseboxUsedHardwareCollectorVisitor(builder, project.getProgramAst().getTree());
    }
}
