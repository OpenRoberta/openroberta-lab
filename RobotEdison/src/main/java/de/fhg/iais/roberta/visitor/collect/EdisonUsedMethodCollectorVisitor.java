package de.fhg.iais.roberta.visitor.collect;

import de.fhg.iais.roberta.bean.UsedMethodBean;

public class EdisonUsedMethodCollectorVisitor extends AbstractUsedMethodCollectorVisitor implements IEdisonCollectorVisitor {
    public EdisonUsedMethodCollectorVisitor(UsedMethodBean.Builder builder) {
        super(builder);
    }
}
