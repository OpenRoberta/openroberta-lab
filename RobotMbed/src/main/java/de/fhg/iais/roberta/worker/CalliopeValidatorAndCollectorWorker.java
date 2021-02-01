package de.fhg.iais.roberta.worker;

import de.fhg.iais.roberta.visitor.CalliopeMethods;

import java.util.Collections;
import java.util.List;

public abstract class CalliopeValidatorAndCollectorWorker extends AbstractValidatorAndCollectorWorker {

    @Override
    protected List<Class<? extends Enum<?>>> getAdditionalMethodEnums() {
        return Collections.singletonList(CalliopeMethods.class);
    }
}
