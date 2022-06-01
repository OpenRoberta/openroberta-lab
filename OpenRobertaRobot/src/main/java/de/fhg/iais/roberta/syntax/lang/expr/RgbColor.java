package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.transformer.NepoOp;
import de.fhg.iais.roberta.transformer.NepoValue;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.syntax.Assoc;

/**
 * This class represents the <b>robColour_rgb</b> block from Blockly
 */
@NepoOp(containerType = "RGB_COLOR", blocklyType = BlocklyType.COLOR, precedence = 999, assoc = Assoc.NONE)
public class RgbColor<V> extends Expr<V> {
    @NepoValue(name = BlocklyConstants.RED, type = BlocklyType.NUMBER_INT)
    public final Expr<V> R;
    @NepoValue(name = BlocklyConstants.GREEN, type = BlocklyType.NUMBER_INT)
    public final Expr<V> G;
    @NepoValue(name = BlocklyConstants.BLUE, type = BlocklyType.NUMBER_INT)
    public final Expr<V> B;
    @NepoValue(name = BlocklyConstants.ALPHA, type = BlocklyType.NUMBER_INT)
    public final Expr<V> A;

    public RgbColor(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, Expr<V> r, Expr<V> g, Expr<V> b, Expr<V> a) {
        super(kind, properties, comment);
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
        return new RgbColor(BlockTypeContainer.getByName("RGB_COLOR"), properties, comment, r, g, b, a);
    }

}
