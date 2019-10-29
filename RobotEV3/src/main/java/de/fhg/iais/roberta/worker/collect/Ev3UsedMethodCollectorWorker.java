package de.fhg.iais.roberta.worker.collect;

import de.fhg.iais.roberta.bean.UsedMethodBean;
import de.fhg.iais.roberta.visitor.collect.Ev3UsedMethodCollectorVisitor;
import de.fhg.iais.roberta.visitor.collect.ICollectorVisitor;
import de.fhg.iais.roberta.worker.AbstractUsedMethodCollectorWorker;

public class Ev3UsedMethodCollectorWorker extends AbstractUsedMethodCollectorWorker {
    @Override
    protected ICollectorVisitor getVisitor(UsedMethodBean.Builder builder) {
        return new Ev3UsedMethodCollectorVisitor(builder);
    }
}
