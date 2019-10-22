package de.fhg.iais.roberta.worker;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.visitor.collect.ICollectorVisitor;
import de.fhg.iais.roberta.visitor.collect.RaspberryPiUsedMethodCollectorVisitor;

public class RaspberryPiUsedMethodCollectorWorker extends AbstractUsedMethodCollectorWorker {
    @Override
    protected ICollectorVisitor getVisitor(CodeGeneratorSetupBean.Builder builder) {
        return new RaspberryPiUsedMethodCollectorVisitor(builder);
    }
}
