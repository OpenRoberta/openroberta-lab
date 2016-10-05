package de.fhg.iais.roberta.syntax.expr;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;import de.fhg.iais.roberta.syntax.BlockTypeContainer.BlockType;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.stmt.Stmt;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;

/**
 * Wraps subclasses of the class {@link Stmt} so they can be used as {@link Expr} in expressions.
 */
public class StmtExpr<V> extends Expr<V> {
    private final Stmt<V> stmt;

    private StmtExpr(Stmt<V> stmt) {
        super(BlockTypeContainer.getByName("SENSOR_EXPR"),stmt.getProperty(), stmt.getComment());
        Assert.isTrue(stmt.isReadOnly());
        this.stmt = stmt;
        setReadOnly();
    }

    /**
     * Create object of the class {@link StmtExpr}.
     *
     * @param stmt that we want to wrap,
     * @return expression with wrapped sensor inside
     */
    public static <V> StmtExpr<V> make(Stmt<V> stmt) {
        return new StmtExpr<V>(stmt);
    }

    /**
     * @return stmt that is wrapped in the expression
     */
    public Stmt<V> getStmt() {
        return this.stmt;
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
    public BlocklyType getVarType() {
        return BlocklyType.NOTHING;
    }

    @Override
    public String toString() {
        return "SensorExpr [" + this.stmt + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitStmtExpr(this);
    }

    @Override
    public Block astToBlock() {
        Phrase<V> p = getStmt();
        return p.astToBlock();
    }
}
