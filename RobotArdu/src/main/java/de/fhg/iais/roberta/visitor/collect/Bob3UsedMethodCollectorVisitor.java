package de.fhg.iais.roberta.visitor.collect;

import de.fhg.iais.roberta.bean.UsedMethodBean;

public class Bob3UsedMethodCollectorVisitor extends AbstractUsedMethodCollectorVisitor implements INIBOCollectorVisitor {
    public Bob3UsedMethodCollectorVisitor(UsedMethodBean.Builder builder) {
        super(builder);
    }
}
