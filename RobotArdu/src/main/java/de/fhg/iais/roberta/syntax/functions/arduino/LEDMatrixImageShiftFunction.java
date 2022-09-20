package de.fhg.iais.roberta.syntax.functions.arduino;

import de.fhg.iais.roberta.mode.general.Direction;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.functions.Function;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.Assoc;

@NepoExpr(name = "LED_MATRIX_IMAGE_SHIFT", category = "FUNCTION", blocklyNames = {"mBotImage_shift"}, blocklyType = BlocklyType.IMAGE)
public final class LEDMatrixImageShiftFunction extends Function {

    @NepoField(name = "OP")
    public final Direction shiftDirection;

    @NepoValue(name = "A", type = BlocklyType.IMAGE)
    public final Expr image;

    @NepoValue(name = "B", type = BlocklyType.NUMBER_INT)
    public final Expr positions;

    public LEDMatrixImageShiftFunction(BlocklyProperties properties, Direction shiftDirection, Expr image, Expr positions) {
        super(properties);
        Assert.notNull(shiftDirection);
        Assert.notNull(image);
        Assert.notNull(positions);
        this.shiftDirection = shiftDirection;
        this.image = image;
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
}
