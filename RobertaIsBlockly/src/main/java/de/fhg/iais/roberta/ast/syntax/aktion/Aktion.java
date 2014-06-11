package de.fhg.iais.roberta.ast.syntax.aktion;

import de.fhg.iais.roberta.ast.syntax.Phrase;

/**
 * the top class of all aktion blocks.
 */
public abstract class Aktion extends Phrase {

    public Aktion(Kind kind) {
        super(kind);
    }

}
