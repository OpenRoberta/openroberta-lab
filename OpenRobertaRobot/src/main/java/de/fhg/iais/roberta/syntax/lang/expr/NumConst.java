package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.transformer.forClass.NepoExpr;
import de.fhg.iais.roberta.transformer.forField.NepoField;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

/**
 * This class represents the blockly block for constant numbers in the AST . Object from this class represent one read-only numerical value.
 */
@NepoExpr(category = "EXPR", blocklyNames = {"math_integer", "math_number", "rob_math_u999"}, containerType = "NUM_CONST", blocklyType = BlocklyType.NUMBER)
public final class NumConst<V> extends Expr<V> {
    @NepoField(name = BlocklyConstants.NUM)
    public final String value;

    public NumConst(BlocklyBlockProperties properties, BlocklyComment comment, String value) {
        super(properties, comment);
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
        return new NumConst<>(properties, comment, value);
    }

    /**
     * factory method: create an AST instance of {@link NumConst}.<br>
     * <b>Main use: either testing or textual representation of programs (because in this case no graphical regeneration is required.</b>
     *
     * @param value of the numerical constant; must be <b>non-empty</b> string,
     * @return read only object representing the number constant in the AST
     */
    public static <V> NumConst<V> make(String value) {
        return new NumConst<>(BlocklyBlockProperties.make("NUM_CONST", "1"), null, value);
    }

    /**
     * @return value of the numerical constant
     */
    public String getValue() {
        return this.value;
    }

}
