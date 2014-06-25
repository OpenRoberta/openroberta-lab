package de.fhg.iais.roberta.ast.syntax.expr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;

public class ExprList extends Expr {
    private final List<Expr> el = new ArrayList<Expr>();

    private ExprList() {
        super(Phrase.Kind.ExprList);
    }

    public static ExprList make() {
        return new ExprList();
    }

    public final void addExpr(Expr expr) {
        Assert.isTrue(mayChange() && expr != null && expr.isReadOnly());
        this.el.add(expr);
    }

    public final List<Expr> get() {
        Assert.isTrue(isReadOnly());
        return Collections.unmodifiableList(this.el);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        toStringBuilder(sb, 0);
        return sb.toString();
    }

    @Override
    public void toStringBuilder(StringBuilder sb, int indentation) {
        boolean first = true;
        for ( Expr expr : this.el ) {
            if ( first ) {
                first = false;
            } else {
                sb.append(", ");
            }
            expr.toStringBuilder(sb, indentation);
        }
    }
}
