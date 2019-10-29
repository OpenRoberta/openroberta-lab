package de.fhg.iais.roberta.visitor.collect;

import de.fhg.iais.roberta.bean.UsedMethodBean;

public class Ev3UsedMethodCollectorVisitor extends AbstractUsedMethodCollectorVisitor implements IEv3CollectorVisitor {
    public Ev3UsedMethodCollectorVisitor(UsedMethodBean.Builder builder) {
        super(builder);
    }
}
