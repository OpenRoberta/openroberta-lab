package de.fhg.iais.roberta.visitor.collect;

import de.fhg.iais.roberta.bean.UsedMethodBean;

public class NaoUsedMethodCollectorVisitor extends AbstractUsedMethodCollectorVisitor implements INaoCollectorVisitor {
    public NaoUsedMethodCollectorVisitor(UsedMethodBean.Builder builder) {
        super(builder);
    }
}
