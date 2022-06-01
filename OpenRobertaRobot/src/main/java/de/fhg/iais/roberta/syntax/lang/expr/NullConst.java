package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.transformer.NepoOp;
import de.fhg.iais.roberta.typecheck.BlocklyType;

/**
 * This class represents the <b>logic_null</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for creating
 * <b>null</b>.<br/>
 * <br>
 * To create an instance from this class use the method {@link #make(BlocklyBlockProperties, BlocklyComment)}.<br>
 */
@NepoOp(containerType = "NULL_CONST", blocklyType = BlocklyType.NULL)
public class NullConst<V> extends Expr<V> {

    public NullConst(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(kind, properties, comment);
        setReadOnly();
    }

    /**
     * creates instance of {@link NullConst}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link NullConst}
     */
    public static <V> NullConst<V> make(BlocklyBlockProperties properties, BlocklyComment comment) {
        return new NullConst<V>(BlockTypeContainer.getByName("NULL_CONST"), properties, comment);
    }

}
