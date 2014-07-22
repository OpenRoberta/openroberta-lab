package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.Phrase;

/**
 * the top class of all action class used to represent the AST (abstract syntax tree) of a program. After construction an AST should be immutable. The logic to
 * achieve
 * that is in the {@link Phrase} class.
 * There are two ways for a client to find out which kind a {@link #Action}-object is:<br>
 * - {@link #getKind()}<br>
 * - {@link #getAs(Class)}<br>
 */
public abstract class Action extends Phrase {

    /**
     * This constructor set the kind of the action object used in the AST (abstract syntax tree). All possible kinds can be found in {@link Kind}.
     * 
     * @param kind of the the action object used in AST
     */
    public Action(Kind kind) {
        super(kind);
    }

}
