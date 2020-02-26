package de.fhg.iais.roberta.worker;

import java.util.Collections;
import java.util.List;

import de.fhg.iais.roberta.bean.UsedMethodBean;
import de.fhg.iais.roberta.visitor.collect.EdisonMethods;
import de.fhg.iais.roberta.visitor.collect.EdisonUsedMethodCollectorVisitor;
import de.fhg.iais.roberta.visitor.collect.ICollectorVisitor;

public class EdisonUsedMethodCollectorWorker extends AbstractUsedMethodCollectorWorker {
    @Override
    protected ICollectorVisitor getVisitor(UsedMethodBean.Builder builder) {
        return new EdisonUsedMethodCollectorVisitor(builder);
    }

    /**
     * Edison has additional methods that need to be generated.
     * @return the additional methods
     */
    @Override
    protected List<Class<? extends Enum<?>>> getAdditionalMethodEnums() {
        return Collections.singletonList(EdisonMethods.class);
    }
}
