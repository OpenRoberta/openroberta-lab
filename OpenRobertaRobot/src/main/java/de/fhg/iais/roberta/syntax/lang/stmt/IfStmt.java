package de.fhg.iais.roberta.syntax.lang.stmt;

import java.math.BigInteger;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Repetitions;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.util.dbc.Assert;

public class IfStmt<V> extends Stmt<V> {
    private final List<Expr<V>> expr;
    private final List<StmtList<V>> thenList;
    private final StmtList<V> elseList;
    private final int _else;
    private final int _elseIf;

    private IfStmt(
        List<Expr<V>> expr,
        List<StmtList<V>> thenList,
        StmtList<V> elseList,
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        int _else,
        int _elseIf) {
        super(BlockTypeContainer.getByName("IF_STMT"), properties, comment);
        Assert.isTrue(expr.size() == thenList.size() && elseList.isReadOnly());
        this.expr = expr;
        this.thenList = thenList;
        this.elseList = elseList;
        this._else = _else;
        this._elseIf = _elseIf;
        setReadOnly();
    }

    /**
     * if with else
     */
    public static <V> IfStmt<V> make(
        List<Expr<V>> expr,
        List<StmtList<V>> thenList,
        StmtList<V> elseList,
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        int _else,
        int _elseIf) {
        return new IfStmt<V>(expr, thenList, elseList, properties, comment, _else, _elseIf);
    }

    /**
     * if without else
     */
    public static <V> IfStmt<V> make(
        List<Expr<V>> expr,
        List<StmtList<V>> thenList,
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        int _else,
        int _elseIf) {
        StmtList<V> elseList = StmtList.make();
        elseList.setReadOnly();
        return new IfStmt<V>(expr, thenList, elseList, properties, comment, _else, _elseIf);
    }

    /**
     * @return list with all expressions that should be evaluated in the <b>if</b> part.
     */
    public final List<Expr<V>> getExpr() {
        return this.expr;
    }

    /**
     * @return list with all statements that are in <b>then</b> part.
     */
    public final List<StmtList<V>> getThenList() {
        return this.thenList;
    }

    /**
     * @return list with all statements that are in <b>else</b> part.
     */
    public final StmtList<V> getElseList() {
        return this.elseList;
    }

    /**
     * @return 1 if there is else statement
     */
    public int get_else() {
        return this._else;
    }

    /**
     * @return number of if statements
     */
    public int get_elseIf() {
        return this._elseIf;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        appendNewLine(sb, 0, null);
        int exprListSize = this.expr.size();
        for ( int i = 0; i < exprListSize; i++ ) {
            sb.append("if ").append(this.expr.get(i));
            appendNewLine(sb, 0, ",then");
            sb.append(this.thenList.get(i).toString());
            if ( i + 1 < exprListSize ) {
                appendNewLine(sb, 0, ",else ");
            }
        }
        if ( !this.elseList.get().isEmpty() ) {
            appendNewLine(sb, 0, ",else");
            sb.append(this.elseList.toString());
        }
        appendNewLine(sb, 0, "");
        return sb.toString();
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        Mutation mutation = block.getMutation();
        int _else = Jaxb2Ast.getElse(mutation);
        int _elseIf = Jaxb2Ast.getElseIf(mutation);
        return helper.blocksToIfStmt(block, _else, _elseIf);
    }

    @Override
    public Block astToBlock() {
        Mutation mutation;
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        if ( getProperty().getBlockType().equals(BlocklyConstants.LOGIC_TERNARY) ) {
            Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.IF, getExpr().get(0));
            Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.THEN, getThenList().get(0).get().get(0));
            Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.ELSE, getElseList().get().get(0));
            return jaxbDestination;
        }
        int _else = get_else();
        int _elseIf = get_elseIf();

        StmtList<?> elseList = getElseList();
        int expr = 0;
        expr = getExpr().size();

        if ( _else != 0 || _elseIf != 0 ) {
            mutation = new Mutation();
            if ( _else != 0 ) {
                mutation.setElse(BigInteger.ONE);
            }
            if ( _elseIf > 0 ) {
                mutation.setElseif(BigInteger.valueOf(_elseIf));
            }
            jaxbDestination.setMutation(mutation);
            Repetitions repetitions = new Repetitions();
            for ( int i = 0; i < expr; i++ ) {
                Ast2Jaxb.addValue(repetitions, BlocklyConstants.IF + i, getExpr().get(i));
                Ast2Jaxb.addStatement(repetitions, BlocklyConstants.DO + i, getThenList().get(i));
            }
            if ( !elseList.get().isEmpty() ) {
                Ast2Jaxb.addStatement(repetitions, BlocklyConstants.ELSE, getElseList());
            }
            jaxbDestination.setRepetitions(repetitions);
            return jaxbDestination;
        }

        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.IF + "0", getExpr().get(0));
        Ast2Jaxb.addStatement(jaxbDestination, BlocklyConstants.DO + "0", getThenList().get(0));
        if ( !elseList.get().isEmpty() ) {
            Ast2Jaxb.addStatement(jaxbDestination, BlocklyConstants.ELSE, getElseList());
        }

        return jaxbDestination;
    }

}
