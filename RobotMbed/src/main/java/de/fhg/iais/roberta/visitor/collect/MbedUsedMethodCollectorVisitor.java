package de.fhg.iais.roberta.visitor.collect;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;

public class MbedUsedMethodCollectorVisitor extends AbstractUsedMethodCollectorVisitor implements IMbedCollectorVisitor {
    public MbedUsedMethodCollectorVisitor(CodeGeneratorSetupBean.Builder builder) {
        super(builder);
    }
}
