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
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "IF_STMT", category = "STMT", blocklyNames = {"robControls_ifElse", "controls_if", "robControls_if"})
public final class IfStmt extends Stmt {
    public final List<Expr> expr;
    public final List<StmtList> thenList;
    public final StmtList elseList;
    public final int _else;
    public final int _elseIf;

    public IfStmt(
        BlocklyProperties properties, List<Expr> expr,
        List<StmtList> thenList,
        StmtList elseList,
        int _else,
        int _elseIf) {
        super(properties);
        Assert.isTrue(expr.size() == thenList.size() && elseList.isReadOnly());
        this.expr = expr;
        this.thenList = thenList;
        this.elseList = elseList;
        this._else = _else;
        this._elseIf = _elseIf;
        setReadOnly();
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

    public static Phrase xml2ast(Block block, Jaxb2ProgramAst helper) {
        Mutation mutation = block.getMutation();
        int _else = Jaxb2Ast.getElse(mutation);
        int _elseIf = Jaxb2Ast.getElseIf(mutation);
        return helper.blocksToIfStmt(block, _else, _elseIf);
    }

    @Override
    public Block ast2xml() {
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

        StmtList elseList = this.elseList;
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
