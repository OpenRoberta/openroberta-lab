package de.fhg.iais.roberta.worker;

import java.util.Collections;
import java.util.List;

import de.fhg.iais.roberta.bean.UsedMethodBean;
import de.fhg.iais.roberta.visitor.collect.ICollectorVisitor;
import de.fhg.iais.roberta.visitor.collect.NxtMethods;
import de.fhg.iais.roberta.visitor.collect.NxtUsedMethodCollectorVisitor;

public class NxtUsedMethodCollectorWorker extends AbstractUsedMethodCollectorWorker {

    /**
     * NXT has additional methods that need to be generated.
     * @return the additional methods
     */
    @Override
    protected List<Class<? extends Enum<?>>> getAdditionalMethodEnums() {
        return Collections.singletonList(NxtMethods.class);
    }

    @Override
    protected ICollectorVisitor getVisitor(UsedMethodBean.Builder builder) {
        return new NxtUsedMethodCollectorVisitor(builder);
    }
}
