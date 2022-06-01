package de.fhg.iais.roberta.syntax.lang.expr;

import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoOp;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>lists_create_empty</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * creating <b>empty list</b>.<br/>
 * <br>
 * To create an instance from this class use the method {@link #make(BlocklyType, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
@NepoOp(containerType = "EMPTY_LIST", blocklyType = BlocklyType.ARRAY)
public class EmptyList<V> extends Expr<V> {
    @NepoField(name = BlocklyConstants.LIST_TYPE)
    public final BlocklyType typeVar;

    public EmptyList(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, BlocklyType typeVar) {
        super(kind, properties, comment);
        Assert.isTrue(typeVar != null);
        this.typeVar = typeVar;
        setReadOnly();
    }

    /**
     * creates instance of {@link EmptyList}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link NullConst}
     */
    public static <V> EmptyList<V> make(BlocklyType typeVar, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new EmptyList<V>(BlockTypeContainer.getByName("EMPTY_LIST"), properties, comment, typeVar);
    }

    /**
     * @return the typeVar
     */
    public BlocklyType getTypeVar() {
        return this.typeVar;
    }

}
