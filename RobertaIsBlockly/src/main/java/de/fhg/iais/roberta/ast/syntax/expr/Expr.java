package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;

public abstract class Expr extends Phrase {
    @SuppressWarnings("unchecked")
    public final <T> T getAs(Class<T> clazz) {
        Assert.isTrue(clazz.equals(getKind().getExprClass()));
        return (T) this;
    }

    abstract public Kind getKind();

    public static enum Kind {
        IntConst( IntConst.class ), Var( Var.class ), Unary( Unary.class ), Binary( Binary.class );

        private final Class<?> clazz;

        private Kind(Class<?> clazz) {
            this.clazz = clazz;
        }

        public Class<?> getExprClass() {
            return this.clazz;
        }
    }
}
