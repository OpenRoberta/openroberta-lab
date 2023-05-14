package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(category = "STMT", blocklyNames = {"robText_append"}, name = "TEXT_APPEND_STMT")
public final class TextAppendStmt extends Stmt {

    @NepoValue(name = "VAR", type = BlocklyType.STRING)
    public final Expr var;

    @NepoValue(name = "TEXT", type = BlocklyType.STRING)
    public final Expr text;

    public TextAppendStmt(BlocklyProperties properties, Expr var, Expr text) {
        super(properties);
        this.var = var;
        this.text = text;
        setReadOnly();
    }
}
