package de.fhg.iais.roberta.ast.syntax.stmt;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robControls_wait_for</b> and <b>robControls_wait</b> blocks from Blockly into the AST (abstract syntax
 * tree).
 * Object from this class will generate if statements nested into repeat statement.<br/>
 * <br>
 * See {@link #getMode()} for the kind of the repeat statements.
 */
public class WaitStmt<V> extends Stmt<V> {
    private final StmtList<V> statements;

    private WaitStmt(StmtList<V> statements, boolean disabled, String comment) {
        super(Phrase.Kind.WAIT_STMT, disabled, comment);
        Assert.isTrue(statements.isReadOnly());
        this.statements = statements;
        setReadOnly();
    }

    /**
     * Create read only object of type {@link WaitStmt}
     * 
     * @param statements
     * @param disabled is true if the block is disabled
     * @param comment for the block
     * @return
     */
    public static <V> WaitStmt<V> make(StmtList<V> statements, boolean disabled, String comment) {
        return new WaitStmt<>(statements, disabled, comment);
    }

    /**
     * @return statements in the blocks
     */
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
