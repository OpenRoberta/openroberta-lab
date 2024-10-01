package de.fhg.iais.roberta.worker;

import de.fhg.iais.roberta.exprEvaluator.CommonTextlyJavaVisitor;
import de.fhg.iais.roberta.textlyJava.WedoTextlyJavaVisitor;

public class WedoTextlyToNepoWorker extends AbstractTextlyJavaToNepoWorker {
    protected CommonTextlyJavaVisitor getVisitor() {
        return new WedoTextlyJavaVisitor();
    }
}
