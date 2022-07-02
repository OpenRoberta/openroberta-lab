package de.fhg.iais.roberta.syntax.action.ev3;

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
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "SHOW_PICTURE_ACTION", category = "ACTOR", blocklyNames = {"robActions_display_picture", "robActions_display_picture_new"})
public final class ShowPictureAction<V> extends Action<V> {
    public final String pic;
    public final Expr<V> x;
    public final Expr<V> y;

    public ShowPictureAction(String pic, Expr<V> x, Expr<V> y, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(properties, comment);
        Assert.isTrue(pic != null && x != null && y != null);
        this.pic = pic;
        this.x = x;
        this.y = y;
        setReadOnly();
    }

    @Override
    public String toString() {
        return "ShowPictureAction [" + this.pic + ", " + this.x + ", " + this.y + "]";
    }

    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 2);
        String pic = Jaxb2Ast.extractField(fields, BlocklyConstants.PICTURE);
        Phrase<V> x = helper.extractValue(values, new ExprParam(BlocklyConstants.X, BlocklyType.NUMBER_INT));
        Phrase<V> y = helper.extractValue(values, new ExprParam(BlocklyConstants.Y, BlocklyType.NUMBER_INT));
        return new ShowPictureAction<>(pic, Jaxb2Ast.convertPhraseToExpr(x), Jaxb2Ast.convertPhraseToExpr(y), Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        String fieldValue = this.pic.toString();
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.PICTURE, fieldValue);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.X, this.x);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.Y, this.y);

        return jaxbDestination;

    }
}
