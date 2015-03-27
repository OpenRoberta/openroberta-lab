package de.fhg.iais.roberta.ast.syntax.stmt;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.BlocklyComment;
import de.fhg.iais.roberta.ast.syntax.BlocklyConstants;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.stmt.RepeatStmt.Mode;
import de.fhg.iais.roberta.ast.transformer.AstJaxbTransformerHelper;
import de.fhg.iais.roberta.ast.transformer.ExprParam;
import de.fhg.iais.roberta.ast.transformer.JaxbAstTransformer;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Repetitions;
import de.fhg.iais.roberta.blockly.generated.Statement;
import de.fhg.iais.roberta.blockly.generated.Value;
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

    private WaitStmt(StmtList<V> statements, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(Phrase.Kind.WAIT_STMT, properties, comment);
        Assert.isTrue(statements != null && statements.isReadOnly());
        this.statements = statements;
        setReadOnly();
    }

    /**
     * Create read only object of type {@link WaitStmt}
     *
     * @param statements; must be <b>not</b> null and <b>read only</b>,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment for the block,
     * @return
     */
    public static <V> WaitStmt<V> make(StmtList<V> statements, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new WaitStmt<>(statements, properties, comment);
    }

    public static <V> Phrase<V> jaxbToAst(Block block, JaxbAstTransformer<V> helper) {
        List<Value> values;
        StmtList<V> statement;
        StmtList<V> list = StmtList.make();
        int mutat = block.getMutation() == null ? 0 : block.getMutation().getWait().intValue();
        List<Statement> statementss;
        if ( mutat == 0 ) {
            values = helper.extractValues(block, (short) (mutat + 1));
            statementss = helper.extractStatements(block, (short) (mutat + 1));
        } else {
            List<Object> valAndStmt = block.getRepetitions().getValueAndStatement();
            values = new ArrayList<Value>();
            statementss = new ArrayList<Statement>();
            helper.convertStmtValList(values, statementss, valAndStmt);
        }
        for ( int i = 0; i <= mutat; i++ ) {
            Phrase<V> expr = helper.extractValue(values, new ExprParam(BlocklyConstants.WAIT + i, Boolean.class));
            statement = helper.extractStatement(statementss, BlocklyConstants.DO + i);
            list.addStmt(RepeatStmt.make(
                Mode.WAIT,
                helper.convertPhraseToExpr(expr),
                statement,
                helper.extractBlockProperties(block),
                helper.extractComment(block)));
        }
        list.setReadOnly();
        return WaitStmt.make(list, helper.extractBlockProperties(block), helper.extractComment(block));
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

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Mutation mutation;
        AstJaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        StmtList<?> waitStmtList = getStatements();
        int numOfWait = waitStmtList.get().size();
        if ( numOfWait == 1 ) {
            AstJaxbTransformerHelper.addValue(jaxbDestination, BlocklyConstants.WAIT + "0", ((RepeatStmt<?>) waitStmtList.get().get(0)).getExpr());
            AstJaxbTransformerHelper.addStatement(jaxbDestination, BlocklyConstants.DO + "0", ((RepeatStmt<?>) waitStmtList.get().get(0)).getList());
            return jaxbDestination;
        }
        mutation = new Mutation();
        mutation.setWait(BigInteger.valueOf(numOfWait - 1));
        jaxbDestination.setMutation(mutation);
        Repetitions repetitions = new Repetitions();
        for ( int i = 0; i < numOfWait; i++ ) {
            AstJaxbTransformerHelper.addValue(repetitions, BlocklyConstants.WAIT + i, ((RepeatStmt<?>) waitStmtList.get().get(i)).getExpr());
            AstJaxbTransformerHelper.addStatement(repetitions, BlocklyConstants.DO + i, ((RepeatStmt<?>) waitStmtList.get().get(i)).getList());
        }
        jaxbDestination.setRepetitions(repetitions);
        return jaxbDestination;
    }
}
