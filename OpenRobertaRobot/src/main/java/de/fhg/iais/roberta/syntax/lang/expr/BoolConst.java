package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoOp;
import de.fhg.iais.roberta.typecheck.BlocklyType;

/**
 * This class represents the <b>logic_boolean</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate boolean
 * constant.<br/>
 * <br>
 * The client must provide the value of the boolean constant. <br>
 * <br>
 * To create an instance from this class use the method {@link #make(boolean, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
@NepoOp(containerType = "BOOL_CONST", blocklyType = BlocklyType.BOOLEAN)
public class BoolConst<V> extends Expr<V> {
    @NepoField(name = BlocklyConstants.BOOL)
    public final boolean value;

    public BoolConst(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, boolean value) {
        super(kind, properties, comment);
        this.value = value;
        setReadOnly();
    }

    /**
     * creates instance of {@link BoolConst}. This instance is read only and can not be modified.
     *
     * @param value that the boolean constant will have,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link BoolConst}
     */
    public static <V> BoolConst<V> make(boolean value, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new BoolConst<>(BlockTypeContainer.getByName("BOOL_CONST"), properties, comment, value);
    }

    /**
     * @return the value of the boolean constant.
     */
    public boolean getValue() {
        return this.value;
    }

}