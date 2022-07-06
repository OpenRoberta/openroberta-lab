package de.fhg.iais.roberta.syntax.lang.functions;

import java.math.BigInteger;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.Assoc;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "TEXT_JOIN_FUNCT", category = "FUNCTION", blocklyNames = {"robText_join", "text_join"})
public final class TextJoinFunct<V> extends Function<V> {
    public final ExprList<V> param;

    public TextJoinFunct(ExprList<V> param, BlocklyProperties properties) {
        super(properties);
        Assert.isTrue(param != null);
        this.param = param;
        setReadOnly();
    }

    @Override
    public int getPrecedence() {
        return 1;
    }

    @Override
    public Assoc getAssoc() {
        return Assoc.LEFT;
    }

    @Override
    public String toString() {
        return "TextJoinFunct [" + this.param + "]";
    }

    @Override
    public BlocklyType getReturnType() {
        return BlocklyType.STRING;
    }

    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        ExprList<V> exprList = helper.blockToExprList(block, BlocklyType.STRING);
        return new TextJoinFunct<V>(exprList, Jaxb2Ast.extractBlocklyProperties(block));

    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        int numOfStrings = this.param.get().size();
        Mutation mutation = new Mutation();
        mutation.setItems(BigInteger.valueOf(numOfStrings));
        jaxbDestination.setMutation(mutation);
        for ( int i = 0; i < numOfStrings; i++ ) {
            Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.ADD + i, this.param.get().get(i));
        }
        return jaxbDestination;
    }
}
