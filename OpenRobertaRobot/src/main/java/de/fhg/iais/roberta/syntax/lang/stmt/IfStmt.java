package de.fhg.iais.roberta.syntax.lang.stmt;

import java.math.BigInteger;
import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.blockly.generated.Repetitions;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "IF_STMT", category = "STMT", blocklyNames = {"robControls_ifElse", "controls_if", "robControls_if"})
public final class IfStmt<V> extends Stmt<V> {
    public final List<Expr<V>> expr;
    public final List<StmtList<V>> thenList;
    public final StmtList<V> elseList;
    public final int _else;
    public final int _elseIf;

    public IfStmt(
        List<Expr<V>> expr,
        List<StmtList<V>> thenList,
        StmtList<V> elseList,
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        int _else,
        int _elseIf) {
        super(properties, comment);
        Assert.isTrue(expr.size() == thenList.size() && elseList.isReadOnly());
        this.expr = expr;
        this.thenList = thenList;
        this.elseList = elseList;
        this._else = _else;
        this._elseIf = _elseIf;
        setReadOnly();
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
        StmtList<V> elseList = new StmtList<V>();
        elseList.setReadOnly();
        return new IfStmt<V>(expr, thenList, elseList, properties, comment, _else, _elseIf);
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
            Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.IF, this.expr.get(0));
            Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.THEN, this.thenList.get(0).get().get(0));
            Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.ELSE, this.elseList.get().get(0));
            return jaxbDestination;
        }
        int _else = this._else;
        int _elseIf = this._elseIf;

        StmtList<?> elseList = this.elseList;
        int expr = 0;
        expr = this.expr.size();

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
                Ast2Jaxb.addValue(repetitions, BlocklyConstants.IF + i, this.expr.get(i));
                Ast2Jaxb.addStatement(repetitions, BlocklyConstants.DO + i, this.thenList.get(i));
            }
            if ( !elseList.get().isEmpty() ) {
                Ast2Jaxb.addStatement(repetitions, BlocklyConstants.ELSE, this.elseList);
            }
            jaxbDestination.setRepetitions(repetitions);
            return jaxbDestination;
        }

        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.IF + "0", this.expr.get(0));
        Ast2Jaxb.addStatement(jaxbDestination, BlocklyConstants.DO + "0", this.thenList.get(0));
        if ( !elseList.get().isEmpty() ) {
            Ast2Jaxb.addStatement(jaxbDestination, BlocklyConstants.ELSE, this.elseList);
        }

        return jaxbDestination;
    }

}
