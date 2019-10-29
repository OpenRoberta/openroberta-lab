package de.fhg.iais.roberta.visitor.collect;

import de.fhg.iais.roberta.bean.UsedMethodBean;

public class MbedUsedMethodCollectorVisitor extends AbstractUsedMethodCollectorVisitor implements IMbedCollectorVisitor {
    public MbedUsedMethodCollectorVisitor(UsedMethodBean.Builder builder) {
        super(builder);
    }
}
