package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoOp;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the blockly block for constant numbers in the AST . Object from this class represent one read-only numerical value.
 */
@NepoOp(containerType = "NUM_CONST", blocklyType = BlocklyType.NUMBER)
public class NumConst<V> extends Expr<V> {
    @NepoField(name = BlocklyConstants.NUM)
    public final String value;

    public NumConst(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, String value) {
        super(kind, properties, comment);
        Assert.isTrue(!value.equals(""));
        this.value = value;
        setReadOnly();
    }

    public NumConst(String value) {
        super(BlockTypeContainer.getByName("NUM_CONST"));
        Assert.isTrue(!value.equals(""));
        this.value = value;
        setReadOnly();
    }

    /**
     * factory method: create an AST instance of {@link NumConst}
     *
     * @param value of the numerical constant; must be <b>non-empty</b> string,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object representing the number constant in the AST
     */
    public static <V> NumConst<V> make(String value, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new NumConst<>(BlockTypeContainer.getByName("NUM_CONST"), properties, comment, value);
    }

    /**
     * factory method: create an AST instance of {@link NumConst}.<br>
     * <b>Main use: either testing or textual representation of programs (because in this case no graphical regeneration is required.</b>
     *
     * @param value of the numerical constant; must be <b>non-empty</b> string,
     * @return read only object representing the number constant in the AST
     */
    public static <V> NumConst<V> make(String value) {
        return new NumConst<>(BlockTypeContainer.getByName("NUM_CONST"), BlocklyBlockProperties.make("1", "1"), null, value);
    }

    /**
     * @return value of the numerical constant
     */
    public String getValue() {
        return this.value;
    }

}
