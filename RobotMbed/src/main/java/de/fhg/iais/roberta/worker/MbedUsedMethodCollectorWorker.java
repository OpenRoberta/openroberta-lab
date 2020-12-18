package de.fhg.iais.roberta.worker;

import java.util.Collections;
import java.util.List;

import de.fhg.iais.roberta.bean.UsedMethodBean;
import de.fhg.iais.roberta.visitor.collect.CalliopeMethods;
import de.fhg.iais.roberta.visitor.collect.ICollectorVisitor;
import de.fhg.iais.roberta.visitor.collect.MbedUsedMethodCollectorVisitor;

public class MbedUsedMethodCollectorWorker extends AbstractUsedMethodCollectorWorker {

    /**
     * Calliope has additional methods that need to be generated.
     *
     * @return the additional methods
     */
    @Override
    protected List<Class<? extends Enum<?>>> getAdditionalMethodEnums() {
        return Collections.singletonList(CalliopeMethods.class);
    }

    @Override
    protected ICollectorVisitor getVisitor(UsedMethodBean.Builder builder) {
        return new MbedUsedMethodCollectorVisitor(builder);
    }
}
