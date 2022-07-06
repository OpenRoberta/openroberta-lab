package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.Assoc;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

@NepoExpr(category = "EXPR", blocklyNames = {"naoColour_rgb", "robColour_rgb", "mbedColour_rgb"}, name = "RGB_COLOR", blocklyType = BlocklyType.COLOR, precedence = 999, assoc = Assoc.NONE)
public final class RgbColor<V> extends Expr<V> {
    @NepoValue(name = BlocklyConstants.RED, type = BlocklyType.NUMBER_INT)
    public final Expr<V> R;

    @NepoValue(name = BlocklyConstants.GREEN, type = BlocklyType.NUMBER_INT)
    public final Expr<V> G;

    @NepoValue(name = BlocklyConstants.BLUE, type = BlocklyType.NUMBER_INT)
    public final Expr<V> B;

    @NepoValue(name = BlocklyConstants.ALPHA, type = BlocklyType.NUMBER_INT)
    public final Expr<V> A;

    public RgbColor(BlocklyProperties properties, Expr<V> r, Expr<V> g, Expr<V> b, Expr<V> a) {
        super(properties);
        R = r;
        G = g;
        B = b;
        A = a;
        setReadOnly();
    }

}
