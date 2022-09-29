package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * Wraps subclasses of the class {@link Action} so they can be used as {@link Stmt} in statements.
 */
@NepoBasic(name = "AKTION_STMT", category = "STMT", blocklyNames = {})
public final class ActionStmt extends Stmt {
    public final Action action;

    public ActionStmt(Action action) {
        super(action.getProperty());
        Assert.isTrue(action.isReadOnly());
        this.action = action;
        setReadOnly();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nAktionStmt [" + this.action + "]");
        return sb.toString();
    }

    @Override
    public Block ast2xml() {
        return this.action.ast2xml();
    }
}
