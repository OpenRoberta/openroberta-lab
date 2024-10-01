package de.fhg.iais.roberta.worker;

import de.fhg.iais.roberta.exprEvaluator.CommonTextlyJavaVisitor;
import de.fhg.iais.roberta.textlyJava.Ev3TextlyJavaVisitor;

public class Ev3TextlyToNepoWorker extends AbstractTextlyJavaToNepoWorker {
    protected CommonTextlyJavaVisitor getVisitor() {
        return new Ev3TextlyJavaVisitor();
    }
}
