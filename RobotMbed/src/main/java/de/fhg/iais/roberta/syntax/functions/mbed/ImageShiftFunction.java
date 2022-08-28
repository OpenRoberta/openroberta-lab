package de.fhg.iais.roberta.syntax.functions.mbed;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Value;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.inter.mode.general.IDirection;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.functions.Function;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.ExprParam;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.Assoc;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoBasic(name = "IMAGE_SHIFT", category = "FUNCTION", blocklyNames = {"mbedImage_shift"})
public final class ImageShiftFunction extends Function {
    public final Expr image;
    public final Expr positions;
    public final IDirection shiftDirection;

    public ImageShiftFunction(Expr image, Expr positions, IDirection shiftDirection, BlocklyProperties properties) {
        super(properties);
        Assert.notNull(image);
        Assert.notNull(positions);
        Assert.notNull(shiftDirection);
        this.image = image;
        this.shiftDirection = shiftDirection;
        this.positions = positions;
        setReadOnly();
    }

    @Override
    public int getPrecedence() {
        return 10;
    }

    @Override
    public Assoc getAssoc() {
        return Assoc.LEFT;
    }

    @Override
    public BlocklyType getReturnType() {
        return BlocklyType.VOID;
    }

    @Override
    public String toString() {
        return "ImageShiftFunction [" + this.image + ", " + this.positions + ", " + this.shiftDirection + "]";
    }

    public static  Phrase jaxbToAst(Block block, Jaxb2ProgramAst helper) {
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
        List<Value> values = Jaxb2Ast.extractValues(block, (short) 2);
        IDirection shiftingDirection = factory.getDirection(Jaxb2Ast.extractField(fields, BlocklyConstants.OP));
        Phrase image = helper.extractValue(values, new ExprParam(BlocklyConstants.A, BlocklyType.PREDEFINED_IMAGE));
        Phrase numberOfPositions = helper.extractValue(values, new ExprParam(BlocklyConstants.B, BlocklyType.NUMBER_INT));
        return new ImageShiftFunction(Jaxb2Ast.convertPhraseToExpr(image), Jaxb2Ast.convertPhraseToExpr(numberOfPositions), shiftingDirection, Jaxb2Ast.extractBlocklyProperties(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.OP, this.shiftDirection.toString());
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.A, this.image);
        Ast2Jaxb.addValue(jaxbDestination, BlocklyConstants.B, this.positions);

        return jaxbDestination;
    }

}
