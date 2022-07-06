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
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "DISPLAY_GET_PIXEL", category = "ACTOR", blocklyNames = {"mbedActions_display_getPixel"})
public final class DisplayGetPixelAction<V> extends Action<V> {
    public final Expr<V> x;
    public final Expr<V> y;

    public DisplayGetPixelAction(BlocklyProperties properties, Expr<V> x, Expr<V> y) {
        super(properties);
        Assert.notNull(x);
        Assert.notNull(y);
        this.x = x;
        this.y = y;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "DisplaySetBrightnessAction [ " + this.x + ", " + this.y + " ]";
    }

    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 2);

        Phrase<V> x = helper.extractValue(values, new ExprParam(BlocklyConstants.X, BlocklyType.NUMBER_INT));
        Phrase<V> y = helper.extractValue(values, new ExprParam(BlocklyConstants.Y, BlocklyType.NUMBER_INT));
        return new DisplayGetPixelAction<>(Jaxb2Ast.extractBlocklyProperties(block), Jaxb2Ast.convertPhraseToExpr(x), Jaxb2Ast.convertPhraseToExpr(y));

    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);

        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.X, this.x);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.Y, this.y);

        return jaxbDestination;

    }
}
