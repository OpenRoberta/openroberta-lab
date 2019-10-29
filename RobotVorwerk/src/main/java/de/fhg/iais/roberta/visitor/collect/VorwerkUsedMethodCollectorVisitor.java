package de.fhg.iais.roberta.visitor.collect;

import de.fhg.iais.roberta.bean.UsedMethodBean;

public class VorwerkUsedMethodCollectorVisitor extends AbstractUsedMethodCollectorVisitor implements IVorwerkCollectorVisitor {
    public VorwerkUsedMethodCollectorVisitor(UsedMethodBean.Builder builder) {
        super(builder);
    }
}
