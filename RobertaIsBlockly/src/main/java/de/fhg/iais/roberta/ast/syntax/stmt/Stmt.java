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

    abstract public void toStringBuilder(StringBuilder sb, int indentation);

    protected final void appendNewLine(StringBuilder sb, int indentation, String text) {
        sb.append("\n");
        for ( int i = 0; i < indentation; i++ ) {
            sb.append(" ");
        }
        if ( text != null ) {
            sb.append(text);
        }
    }

    public static enum Kind {
        If( IfStmt.class ), Repeat( RepeatStmt.class ), Expr( ExprStmt.class ), StmtList( StmtList.class ), Assign( AssignStmt.class );

        private final Class<?> clazz;

        private Kind(Class<?> clazz) {
            this.clazz = clazz;
        }

        public Class<?> getStmtClass() {
            return this.clazz;
        }
    }
}
