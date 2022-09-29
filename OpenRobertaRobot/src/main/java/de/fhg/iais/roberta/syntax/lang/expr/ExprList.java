package de.fhg.iais.roberta.syntax.lang.expr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.Assoc;

@NepoBasic(name = "EXPR_LIST", category = "EXPR", blocklyNames = {})
public final class ExprList extends Expr {
    public final List<Expr> el = new ArrayList<Expr>();

    public ExprList() {
        super(new BlocklyProperties("1", "1", false, false, false, false, false, null, false, false, null));
    }

    /**
     * Add new expression to the list.
     */
    public final void addExpr(Expr expr) {
        Assert.isTrue(mayChange() && expr != null && expr.isReadOnly());
        this.el.add(expr);
    }

    /**
     * @return the expression list
     */
    public final List<Expr> get() {
        Assert.isTrue(isReadOnly());
        return Collections.unmodifiableList(this.el);
    }

    @Override
    public int getPrecedence() {
        throw new DbcException("not supported");
    }

    @Override
    public Assoc getAssoc() {
        throw new DbcException("not supported");
    }

    @Override
    public BlocklyType getVarType() {
        return BlocklyType.NOTHING;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for ( Expr expr : this.el ) {
            if ( first ) {
                first = false;
            } else {
                sb.append(", ");
            }
            sb.append(expr.toString());
        }
        return sb.toString();
    }

    @Override
    public Block ast2xml() {
        return null;
    }

}
