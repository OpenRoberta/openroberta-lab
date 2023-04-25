package de.fhg.iais.roberta.syntax.lang.stmt;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.forClass.NepoPhrase;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

@NepoPhrase(category = "STMT", blocklyNames = {"math_change", "robMath_change"}, name = "MATH_CHANGE_STMT")
public final class MathChangeStmt extends Stmt {

    @NepoValue(name = "VAR", type = BlocklyType.STRING)
    public final Expr var;

    @NepoValue(name = "DELTA", type = BlocklyType.NUMBER_INT)
    public final Expr delta;

    public MathChangeStmt(BlocklyProperties properties, Expr var, Expr delta) {
        super(properties);
        this.var = var;
        this.delta = delta;
        setReadOnly();
    }
}
