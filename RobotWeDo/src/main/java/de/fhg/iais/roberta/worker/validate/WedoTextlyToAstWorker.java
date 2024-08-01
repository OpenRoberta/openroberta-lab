package de.fhg.iais.roberta.worker.validate;

import de.fhg.iais.roberta.exprEvaluator.CommonTextlyVisitor;
import de.fhg.iais.roberta.textly.WedoTextlyVisitor;
import de.fhg.iais.roberta.worker.AbstractTextlyToAstWorker;

public class WedoTextlyToAstWorker extends AbstractTextlyToAstWorker {

    protected CommonTextlyVisitor getVisitor() {
        return new WedoTextlyVisitor();
    }
}
