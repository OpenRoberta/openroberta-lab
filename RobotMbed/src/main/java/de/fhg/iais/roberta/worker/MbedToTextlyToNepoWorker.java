package de.fhg.iais.roberta.worker;

import de.fhg.iais.roberta.exprEvaluator.CommonTextlyJavaVisitor;
import de.fhg.iais.roberta.textlyJava.Microbitv2TextlyJavaVisitor;

public class MbedToTextlyToNepoWorker extends AbstractTextlyJavaToNepoWorker {
    protected CommonTextlyJavaVisitor getVisitor() {
        return new Microbitv2TextlyJavaVisitor<>();
    }
}
