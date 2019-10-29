package de.fhg.iais.roberta.visitor.collect;

import de.fhg.iais.roberta.bean.UsedMethodBean;

public class RaspberryPiUsedMethodCollectorVisitor extends AbstractUsedMethodCollectorVisitor implements IRaspberryPiCollectorVisitor {
    public RaspberryPiUsedMethodCollectorVisitor(UsedMethodBean.Builder builder) {
        super(builder);
    }
}
