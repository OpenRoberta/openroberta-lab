package de.fhg.iais.roberta.syntax.action.nao;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.mode.action.nao.Led;
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

@NepoBasic(name = "SET_INTENSITY", category = "ACTOR", blocklyNames = {"naoActions_setIntensity"})
public final class SetIntensity extends Action {

    public final Led led;
    public final Expr Intensity;

    public SetIntensity(Led led, Expr Intensity, BlocklyProperties properties) {
        super(properties);
        this.led = led;
        Assert.notNull(Intensity);
        this.Intensity = Intensity;
        setReadOnly();
    }

    public static  Phrase jaxbToAst(Block block, Jaxb2ProgramAst helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);

        Phrase Intensity = helper.extractValue(values, new ExprParam(BlocklyConstants.INTENSITY, BlocklyType.NUMBER_INT));

        String leds = Jaxb2Ast.extractField(fields, BlocklyConstants.LED);

        return new SetIntensity(Led.get(leds), Jaxb2Ast.convertPhraseToExpr(Intensity), Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.LED, this.led.toString());
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.INTENSITY, this.Intensity);

        return jaxbDestination;
    }

    @Override
    public String toString() {
        return "SetLeds [" + this.led + ", " + this.Intensity + ", " + "]";
    }
}
