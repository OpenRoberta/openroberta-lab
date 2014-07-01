package de.fhg.iais.roberta.ast.syntax.expr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * this class allows to create list of {@link Expr} elements.
 * Initially object from this class is writable. After adding all the elements to the list call {@link #setReadOnly()}.
 * 
 * @author kcvejoski
 */
public class ExprList extends Expr {
    private final List<Expr> el = new ArrayList<Expr>();

    private ExprList() {
        super(Phrase.Kind.ExprList);
    }

    /**
     * @return writable object of type {@link ExprList}.
     */
    public static ExprList make() {
        return new ExprList();
    }

    /**
     * Add new element to the list.
     * 
     * @param expr
     */
    public final void addExpr(Expr expr) {
        Assert.isTrue(mayChange() && expr != null && expr.isReadOnly());
        this.el.add(expr);
    }

    /**
     * @return list with elements of type {@link Expr}.
     */
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
