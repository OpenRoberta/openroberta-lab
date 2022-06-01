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
 * This class represents the blockly block for constant numbers in the AST . Object from this class represent one read-only numerical value.
 */
@NepoOp(containerType = "NN_GET_OUTPUT_NEURON_VAL", blocklyType = BlocklyType.NUMBER)
public class NNGetOutputNeuronVal<V> extends Expr<V> {
    @NepoField(name = BlocklyConstants.NAME)
    public final String name;

    public NNGetOutputNeuronVal(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, String name) {
        super(kind, properties, comment);
        this.name = name;
        setReadOnly();
    }

    /**
     * factory method: create an AST instance of {@link NNGetOutputNeuronVal}
     *
     * @param value of the numerical constant; must be <b>non-empty</b> string,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object representing the number constant in the AST
     */
    public static <V> NNGetOutputNeuronVal<V> make(BlocklyBlockProperties properties, BlocklyComment comment, String name) {
        return new NNGetOutputNeuronVal<>(BlockTypeContainer.getByName("NUM_CONST"), properties, comment, name);
    }

    /**
     * factory method: create an AST instance of {@link NNGetOutputNeuronVal}.<br>
     * <b>Main use: either testing or textual representation of programs (because in this case no graphical regeneration is required.</b>
     *
     * @param value of the numerical constant; must be <b>non-empty</b> string,
     * @return read only object representing the number constant in the AST
     */
    public static <V> NNGetOutputNeuronVal<V> make(String name) {
        return new NNGetOutputNeuronVal<>(BlockTypeContainer.getByName("NUM_CONST"), BlocklyBlockProperties.make("1", "1"), null, name);
    }

    /**
     * @return value of the numerical constant
     */
    public String getName() {
        return this.name;
    }

}
