package de.fhg.iais.roberta.worker;

import de.fhg.iais.roberta.bean.UsedHardwareBean.Builder;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.visitor.collect.EdisonUsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractCollectorVisitor;

public final class EdisonUsedHardwareCollectorWorker extends AbstractUsedHardwareCollectorWorker {
    @Override
    protected AbstractCollectorVisitor getVisitor(Builder builder, Project project) {
        return new EdisonUsedHardwareCollectorVisitor(builder, project.getProgramAst().getTree(), project.getConfigurationAst());
    }
}
