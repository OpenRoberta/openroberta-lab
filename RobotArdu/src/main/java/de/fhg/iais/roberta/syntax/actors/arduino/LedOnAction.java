package de.fhg.iais.roberta.syntax.actors.arduino;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.Action;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "BOB3_RGB_LED_ON", category = "ACTOR", blocklyNames = {"makeblockActions_leds_on"})
public final class LedOnAction<V> extends Action<V> {
    public final Expr<V> ledColor;
    public final String side;

    public LedOnAction(String side, Expr<V> ledColor, BlocklyProperties properties) {
        super(properties);
        Assert.notNull(ledColor);
        this.ledColor = ledColor;
        this.side = side;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "LedOnAction [ " + this.ledColor + " ]";
    }

    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
        String side = Jaxb2Ast.extractField(fields, BlocklyConstants.LED + BlocklyConstants.SIDE);

        Phrase<V> ledColor = helper.extractValue(values, new ExprParam(BlocklyConstants.COLOR, BlocklyType.COLOR));

        return new LedOnAction<>(side, Jaxb2Ast.convertPhraseToExpr(ledColor), Jaxb2Ast.extractBlocklyProperties(block));

    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.LED + BlocklyConstants.SIDE, this.side);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.COLOR, this.ledColor);
        return jaxbDestination;

    }
}
