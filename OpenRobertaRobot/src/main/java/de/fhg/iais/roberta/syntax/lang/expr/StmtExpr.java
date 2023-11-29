package de.fhg.iais.roberta.syntax.lang.expr;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.stmt.Stmt;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.Assoc;

/**
 * Wraps subclasses of the class {@link Stmt} so they can be used as {@link Expr} in expressions.
 */
@NepoBasic(name = "STMT_EXPR", category = "EXPR", blocklyNames = {})
public final class StmtExpr extends Expr {
    public final Stmt stmt;

    public StmtExpr(Stmt stmt) {
        super(stmt.getProperty(), BlocklyType.NOTHING);
        Assert.isTrue(stmt.isReadOnly());
        this.stmt = stmt;
        setReadOnly();
    }

    @Override
    public int getPrecedence() {
        return 999;
    }

    @Override
    public Assoc getAssoc() {
        return Assoc.NONE;
    }

    @Override
    public String toString() {
        return "StmtExpr [" + this.stmt + "]";
    }

    @Override
    public List<Block> ast2xml() {
        Phrase p = this.stmt;
        return p.ast2xml();
    }
}
