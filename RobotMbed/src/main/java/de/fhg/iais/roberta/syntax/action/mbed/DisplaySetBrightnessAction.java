package de.fhg.iais.roberta.syntax.action.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
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
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "DISPLAY_SET_BRIGHTNESS", category = "ACTOR", blocklyNames = {"mbedActions_display_setBrightness"})
public final class DisplaySetBrightnessAction<V> extends Action<V> {
    public final Expr<V> brightness;

    public DisplaySetBrightnessAction(Expr<V> brightness, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(properties, comment);
        Assert.notNull(brightness);
        this.brightness = brightness;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "DisplaySetBrightnessAction [ " + this.brightness + " ]";
    }

    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 1);

        Phrase<V> brightness = helper.extractValue(values, new ExprParam(BlocklyConstants.BRIGHTNESS, BlocklyType.NUMBER_INT));

        return new DisplaySetBrightnessAction<>(Jaxb2Ast.convertPhraseToExpr(brightness), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));

    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.BRIGHTNESS, this.brightness);

        return jaxbDestination;

    }
}
