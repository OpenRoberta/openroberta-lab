package de.fhg.iais.roberta.worker.validate;

import de.fhg.iais.roberta.exprEvaluator.CommonTextlyVisitor;
import de.fhg.iais.roberta.textly.RobotMbedTextlyVisitor;
import de.fhg.iais.roberta.worker.AbstractTextlyToAstWorker;

public class MbedTextlyToAstWorker extends AbstractTextlyToAstWorker {

    protected CommonTextlyVisitor getVisitor() {
        return new RobotMbedTextlyVisitor();
    }

}



