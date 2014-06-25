package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.Phrase;

/**
 * the top class of all aktion blocks.
 */
public abstract class Action extends Phrase {

    public Action(Kind kind) {
        super(kind);
    }

}
