package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.syntax.Assoc;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

/**
 * This class represents the <b>robColour_rgb</b> block from Blockly
 */
@NepoExpr(category = "EXPR", blocklyNames = {"naoColour_rgb", "robColour_rgb", "mbedColour_rgb"}, containerType = "RGB_COLOR", blocklyType = BlocklyType.COLOR, precedence = 999, assoc = Assoc.NONE)
public class RgbColor<V> extends Expr<V> {
    @NepoValue(name = BlocklyConstants.RED, type = BlocklyType.NUMBER_INT)
    public final Expr<V> R;
    @NepoValue(name = BlocklyConstants.GREEN, type = BlocklyType.NUMBER_INT)
    public final Expr<V> G;
    @NepoValue(name = BlocklyConstants.BLUE, type = BlocklyType.NUMBER_INT)
    public final Expr<V> B;
    @NepoValue(name = BlocklyConstants.ALPHA, type = BlocklyType.NUMBER_INT)
    public final Expr<V> A;

    public RgbColor(BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> r, Expr<V> g, Expr<V> b, Expr<V> a) {
        super(properties, comment);
        R = r;
        G = g;
        B = b;
        A = a;
        setReadOnly();
    }

    // TODO: inline, if proposal is accepted
    public Expr<V> getR() {
        return R;
    }

    // TODO: inline, if proposal is accepted
    public Expr<V> getG() {
        return G;
    }

    // TODO: inline, if proposal is accepted
    public Expr<V> getB() {
        return B;
    }

    // TODO: inline, if proposal is accepted
    public Expr<V> getA() {
        return A;
    }

    // TODO: remove, if Transformer is better engineered
    public static <V> RgbColor<V> make(BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> r, Expr<V> g, Expr<V> b, Expr<V> a) {
        return new RgbColor(properties, comment, r, g, b, a);
    }

}
