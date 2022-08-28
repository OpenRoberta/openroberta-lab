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
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "LED_MATRIX__SET_BRIGHTNESS", category = "ACTOR", blocklyNames = {"mBotactions_display_setbrightness"})
public final class LEDMatrixSetBrightnessAction extends Action {
    public final Expr brightness;
    public final String port;

    public LEDMatrixSetBrightnessAction(String port, Expr brightness, BlocklyProperties properties) {
        super(properties);
        Assert.notNull(brightness);
        this.port = port;
        this.brightness = brightness;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "LEDMatrixSetBrightnessAction [ " + this.port + ", " + this.brightness + " ]";
    }

    public static  Phrase jaxbToAst(Block block, Jaxb2ProgramAst helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);
        final String port = Jaxb2Ast.extractField(fields, BlocklyConstants.ACTORPORT);

        Phrase brightness = helper.extractValue(values, new ExprParam(BlocklyConstants.BRIGHTNESS, BlocklyType.NUMBER_INT));

        return new LEDMatrixSetBrightnessAction(port, Jaxb2Ast.convertPhraseToExpr(brightness), Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.ACTORPORT, this.port);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.BRIGHTNESS, this.brightness);

        return jaxbDestination;
    }
}
