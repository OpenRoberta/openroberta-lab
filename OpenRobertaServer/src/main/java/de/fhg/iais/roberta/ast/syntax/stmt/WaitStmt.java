package de.fhg.iais.roberta.ast.syntax.stmt;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.dbc.Assert;

public class WaitStmt<V> extends Stmt<V> {
    private final StmtList<V> statements;

    private WaitStmt(StmtList<V> statements, boolean disabled, String comment) {
        super(Phrase.Kind.WAIT_STMT, disabled, comment);
        Assert.isTrue(statements.isReadOnly());
        this.statements = statements;
        setReadOnly();
    }

    public static <V> WaitStmt<V> make(StmtList<V> statements, boolean disabled, String comment) {
        return new WaitStmt<>(statements, disabled, comment);
    }

    public StmtList<V> getStatements() {
        return this.statements;
    }

    @Override
    public String toString() {
        return "WaitStmt [statements=" + this.statements + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitWaitStmt(this);
    }
}
