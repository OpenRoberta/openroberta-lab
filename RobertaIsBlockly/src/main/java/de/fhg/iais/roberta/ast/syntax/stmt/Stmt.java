package de.fhg.iais.roberta.ast.syntax.stmt;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;

public abstract class Stmt extends Phrase {
    @SuppressWarnings("unchecked")
    public final <T> T getAs(Class<T> clazz) {
        Assert.isTrue(clazz.equals(getKind().getStmtClass()));
        return (T) this;
    }

    abstract public Kind getKind();

    public static enum Kind {
        If( IfStmt.class ), Repeat( RepeatStmt.class );

        private final Class<?> clazz;

        private Kind(Class<?> clazz) {
            this.clazz = clazz;
        }

        public Class<?> getStmtClass() {
            return this.clazz;
        }
    }
}
