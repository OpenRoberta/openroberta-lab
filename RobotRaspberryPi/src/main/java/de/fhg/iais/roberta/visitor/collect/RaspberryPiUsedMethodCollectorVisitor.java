package de.fhg.iais.roberta.visitor.collect;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;

public class RaspberryPiUsedMethodCollectorVisitor extends AbstractUsedMethodCollectorVisitor implements IRaspberryPiCollectorVisitor {
    public RaspberryPiUsedMethodCollectorVisitor(CodeGeneratorSetupBean.Builder builder) {
        super(builder);
    }
}
