package de.fhg.iais.roberta.visitor.collect;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;

public class VorwerkUsedMethodCollectorVisitor extends AbstractUsedMethodCollectorVisitor implements IVorwerkCollectorVisitor {
    public VorwerkUsedMethodCollectorVisitor(CodeGeneratorSetupBean.Builder builder) {
        super(builder);
    }
}
