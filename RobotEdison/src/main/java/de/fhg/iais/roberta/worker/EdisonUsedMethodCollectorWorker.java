package de.fhg.iais.roberta.worker;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.visitor.collect.EdisonUsedMethodCollectorVisitor;
import de.fhg.iais.roberta.visitor.collect.ICollectorVisitor;

public class EdisonUsedMethodCollectorWorker extends AbstractUsedMethodCollectorWorker {
    @Override
    protected ICollectorVisitor getVisitor(CodeGeneratorSetupBean.Builder builder) {
        return new EdisonUsedMethodCollectorVisitor(builder);
    }
}
