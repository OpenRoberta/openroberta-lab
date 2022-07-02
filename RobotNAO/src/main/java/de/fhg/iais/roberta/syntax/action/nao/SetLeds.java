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
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "RGB_LED", category = "ACTOR", blocklyNames = {"naoActions_rgbLeds"})
public final class SetLeds<V> extends Action<V> {

    public final Led led;
    public final Expr<V> Color;

    public SetLeds(Led led, Expr<V> Color, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(properties, comment);
        this.led = led;
        Assert.notNull(Color);
        this.Color = Color;
        setReadOnly();
    }

    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);

        Phrase<V> Color = helper.extractValue(values, new ExprParam(BlocklyConstants.COLOR, BlocklyType.COLOR));

        String leds = Jaxb2Ast.extractField(fields, BlocklyConstants.LED);

        return new SetLeds<V>(Led.get(leds), Jaxb2Ast.convertPhraseToExpr(Color), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.LED, this.led.toString());
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.COLOR, this.Color);

        return jaxbDestination;
    }

    @Override
    public String toString() {
        return "SetLeds [" + this.led + ", " + this.Color + ", " + "]";
    }
}
