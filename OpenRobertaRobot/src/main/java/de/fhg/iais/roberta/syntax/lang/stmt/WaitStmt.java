package de.fhg.iais.roberta.syntax.lang.stmt;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Repetitions;
import de.fhg.iais.roberta.blockly.generated.Statement;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt.Mode;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.typecheck.NepoInfos;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

/**
 * This class represents the <b>robControls_wait_for</b> and <b>robControls_wait</b> blocks from Blockly into the AST (abstract syntax tree). Object from this
 * class will generate if statements nested into repeat statement.<br/>
 * <br>
 * See {@link #getMode()} for the kind of the repeat statements.
 */
@NepoBasic(name = "WAIT_STMT", category = "STMT", blocklyNames = {"robControls_wait", "mbedControls_wait_for", "robControls_wait_for", "naocontrols_wait_for"})
public final class WaitStmt<V> extends Stmt<V> {
    public final StmtList<V> statements;

    public WaitStmt(StmtList<V> statements, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(properties, comment);
        Assert.isTrue(statements != null && statements.isReadOnly());
        this.statements = statements;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "WaitStmt [" + this.statements + "]";
    }

    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Value> values;
        StmtList<V> statement;
        StmtList<V> list = new StmtList<V>();
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
                    new RepeatStmt<V>(Mode.WAIT, Jaxb2Ast.convertPhraseToExpr(expr), statement, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block)));
        }
        list.setReadOnly();
        return new WaitStmt<>(list, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Mutation mutation;
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        StmtList<?> waitStmtList = this.statements;
        int numOfWait = waitStmtList.get().size();
        if ( numOfWait == 1 ) {
            RepeatStmt<?> generatedRepeatStmt = (RepeatStmt<?>) waitStmtList.get().get(0);
            NepoInfos infos = generatedRepeatStmt.getInfos();
            if ( infos.getErrorCount() > 0 ) {
                Ast2Jaxb.addError(generatedRepeatStmt, jaxbDestination);
            }
            Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.WAIT + "0", generatedRepeatStmt.expr);
            Ast2Jaxb.addStatement(jaxbDestination, BlocklyConstants.DO + "0", generatedRepeatStmt.list);
            return jaxbDestination;
        }
        mutation = new Mutation();
        mutation.setWait(BigInteger.valueOf(numOfWait - 1L));
        jaxbDestination.setMutation(mutation);
        Repetitions repetitions = new Repetitions();
        for ( int i = 0; i < numOfWait; i++ ) {
            RepeatStmt<?> generatedRepeatStmt = (RepeatStmt<?>) waitStmtList.get().get(i);
            NepoInfos infos = generatedRepeatStmt.getInfos();
            if ( infos.getErrorCount() > 0 ) {
                Ast2Jaxb.addError(generatedRepeatStmt, jaxbDestination);
            }
            Ast2Jaxb.addValue(repetitions, BlocklyConstants.WAIT + i, generatedRepeatStmt.expr);
            Ast2Jaxb.addStatement(repetitions, BlocklyConstants.DO + i, generatedRepeatStmt.list);
        }
        jaxbDestination.setRepetitions(repetitions);
        return jaxbDestination;
    }
}
