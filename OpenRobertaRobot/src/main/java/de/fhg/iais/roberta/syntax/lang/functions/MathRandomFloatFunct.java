package de.fhg.iais.roberta.syntax.lang.functions;

import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.transformer.NepoOp;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.syntax.FunctionNames;

/**
 * This class represents the <b>math_random_float</b> block from Blockly into the AST (abstract syntax tree).<br>
 * <br>
 * The user must provide name of the function and list of parameters. <br>
 * To create an instance from this class use the method {@link #make(BlocklyBlockProperties, BlocklyComment)}.<br>
 * The enumeration {@link FunctionNames} contains all allowed functions.
 */
@NepoOp(containerType = "MATH_RANDOM_FLOAT_FUNCT", blocklyType = BlocklyType.NUMBER, precedence = 10)
public class MathRandomFloatFunct<V> extends Function<V> {

    public MathRandomFloatFunct(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(kind, properties, comment);
        setReadOnly();
    }

    /**
     * Creates instance of {@link MathRandomFloatFunct}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment that user has added to the block,
     * @return read only object of class {@link MathRandomFloatFunct}
     */
    public static <V> MathRandomFloatFunct<V> make(BlocklyBlockProperties properties, BlocklyComment comment) {
        return new MathRandomFloatFunct<V>(BlockTypeContainer.getByName("MATH_RANDOM_FLOAT_FUNCT"), properties, comment);
    }

}
