package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.Assoc;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoExpr(category = "EXPR", blocklyNames = {"naoColour_rgb", "robColour_rgb", "mbedColour_rgb"}, name = "RGB_COLOR", blocklyType = BlocklyType.COLOR, precedence = 999, assoc = Assoc.NONE)
public final class RgbColor extends Expr {
    @NepoValue(name = BlocklyConstants.RED, type = BlocklyType.NUMBER_INT)
    public final Expr R;

    @NepoValue(name = BlocklyConstants.GREEN, type = BlocklyType.NUMBER_INT)
    public final Expr G;

    @NepoValue(name = BlocklyConstants.BLUE, type = BlocklyType.NUMBER_INT)
    public final Expr B;

    @NepoValue(name = BlocklyConstants.ALPHA, type = BlocklyType.NUMBER_INT)
    public final Expr A;

    public RgbColor(BlocklyProperties properties, Expr r, Expr g, Expr b, Expr a) {
        super(properties);
        R = r;
        G = g;
        B = b;
        A = a;
        setReadOnly();
    }

}
