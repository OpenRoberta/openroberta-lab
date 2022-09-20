package de.fhg.iais.roberta.syntax.functions.arduino;

import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.functions.Function;
import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.Assoc;

@NepoExpr(name = "LED_MATRIX_IMAGE_INVERT", category = "FUNCTION", blocklyNames = {"mBotImage_invert"}, blocklyType = BlocklyType.IMAGE)
public final class LEDMatrixImageInvertFunction extends Function {
    @NepoValue(name = "VAR", type = BlocklyType.IMAGE)
    public final Expr image;

    public LEDMatrixImageInvertFunction(BlocklyProperties properties, Expr image) {
        super(properties);
        Assert.notNull(image);

        this.image = image;
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
