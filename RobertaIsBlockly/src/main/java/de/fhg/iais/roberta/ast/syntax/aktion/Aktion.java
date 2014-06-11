package de.fhg.iais.roberta.ast.syntax.aktion;

import de.fhg.iais.roberta.ast.syntax.Phrase;

/**
 * the top class of all aktion blocks. There are two ways for a client to find out which kind of aktion an {@link #Aktion}-object is:<br>
 * - {@link #getKind()}<br>
 * - {@link #getAs(Class)}<br>
 */
public abstract class Aktion extends Phrase {

    public Aktion(Kind kind) {
        super(kind);
    }

}
