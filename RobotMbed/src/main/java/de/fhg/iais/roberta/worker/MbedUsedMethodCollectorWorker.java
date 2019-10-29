package de.fhg.iais.roberta.worker;

import de.fhg.iais.roberta.bean.UsedMethodBean;
import de.fhg.iais.roberta.visitor.collect.ICollectorVisitor;
import de.fhg.iais.roberta.visitor.collect.MbedUsedMethodCollectorVisitor;

public class MbedUsedMethodCollectorWorker extends AbstractUsedMethodCollectorWorker {
    @Override
    protected ICollectorVisitor getVisitor(UsedMethodBean.Builder builder) {
        return new MbedUsedMethodCollectorVisitor(builder);
    }
}
