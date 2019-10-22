package de.fhg.iais.roberta.worker;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.visitor.collect.ICollectorVisitor;
import de.fhg.iais.roberta.visitor.collect.VorwerkUsedMethodCollectorVisitor;

public class VorwerkUsedMethodCollectorWorker extends AbstractUsedMethodCollectorWorker {
    @Override
    protected ICollectorVisitor getVisitor(CodeGeneratorSetupBean.Builder builder) {
        return new VorwerkUsedMethodCollectorVisitor(builder);
    }
}
