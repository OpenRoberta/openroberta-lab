package de.fhg.iais.roberta.syntax.lang.stmt;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Repetitions;
import de.fhg.iais.roberta.blockly.generated.Statement;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt.Mode;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.typecheck.NepoInfos;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>robControls_wait_for</b> and <b>robControls_wait</b> blocks from Blockly into the AST (abstract syntax tree). Object from this
 * class will generate if statements nested into repeat statement.<br/>
 * <br>
 * See {@link #getMode()} for the kind of the repeat statements.
 */
public class WaitStmt<V> extends Stmt<V> {
    private final StmtList<V> statements;

    private WaitStmt(StmtList<V> statements, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("WAIT_STMT"), properties, comment);
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

    /**
     * @return statements in the blocks
     */
    public StmtList<V> getStatements() {
        return this.statements;
    }

    @Override
    public String toString() {
        return "WaitStmt [" + this.statements + "]";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Value> values;
        StmtList<V> statement;
        StmtList<V> list = StmtList.make();
        int mutat = block.getMutation() == null ? 0 : block.getMutation().getWait().intValue();
        List<Statement> statementss;
        if ( mutat == 0 ) {
            values = Jaxb2Ast.extractValues(block, (short) (mutat + 1));
            statementss = Jaxb2Ast.extractStatements(block, (short) (mutat + 1));
        } else {
            List<Object> valAndStmt = block.getRepetitions().getValueAndStatement();
            values = new ArrayList<>();
            statementss = new ArrayList<>();
            Jaxb2Ast.convertStmtValList(values, statementss, valAndStmt);
        }
        for ( int i = 0; i <= mutat; i++ ) {
            Phrase<V> expr = helper.extractValue(values, new ExprParam(BlocklyConstants.WAIT + i, BlocklyType.BOOLEAN));
            statement = helper.extractStatement(statementss, BlocklyConstants.DO + i);
            list
                .addStmt(
                    RepeatStmt
                        .make(Mode.WAIT, Jaxb2Ast.convertPhraseToExpr(expr), statement, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block)));
        }
        list.setReadOnly();
        return WaitStmt.make(list, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Mutation mutation;
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        StmtList<?> waitStmtList = getStatements();
        int numOfWait = waitStmtList.get().size();
        if ( numOfWait == 1 ) {
            RepeatStmt<?> generatedRepeatStmt = (RepeatStmt<?>) waitStmtList.get().get(0);
            NepoInfos infos = generatedRepeatStmt.getInfos();
            if (infos.getErrorCount() > 0) {
                Ast2Jaxb.addError(generatedRepeatStmt, jaxbDestination);
            }
            Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.WAIT + "0", generatedRepeatStmt.getExpr());
            Ast2Jaxb.addStatement(jaxbDestination, BlocklyConstants.DO + "0", generatedRepeatStmt.getList());
            return jaxbDestination;
        }
        mutation = new Mutation();
        mutation.setWait(BigInteger.valueOf(numOfWait - 1L));
        jaxbDestination.setMutation(mutation);
        Repetitions repetitions = new Repetitions();
        for ( int i = 0; i < numOfWait; i++ ) {
            RepeatStmt<?> generatedRepeatStmt = (RepeatStmt<?>) waitStmtList.get().get(i);
            NepoInfos infos = generatedRepeatStmt.getInfos();
            if (infos.getErrorCount() > 0) {
                Ast2Jaxb.addError(generatedRepeatStmt, jaxbDestination);
            }
            Ast2Jaxb.addValue(repetitions, BlocklyConstants.WAIT + i, generatedRepeatStmt.getExpr());
            Ast2Jaxb.addStatement(repetitions, BlocklyConstants.DO + i, generatedRepeatStmt.getList());
        }
        jaxbDestination.setRepetitions(repetitions);
        return jaxbDestination;
    }
}
