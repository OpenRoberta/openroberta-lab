package de.fhg.iais.roberta.worker;

import java.util.Collections;
import java.util.List;

import de.fhg.iais.roberta.visitor.CalliopeMethods;

public abstract class CalliopeValidatorAndCollectorWorker extends AbstractValidatorAndCollectorWorker {

    @Override
    protected List<Class<? extends Enum<?>>> getAdditionalMethodEnums() {
        return Collections.singletonList(CalliopeMethods.class);
    }
}
