package de.fhg.iais.roberta.syntax.lang.functions;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Assoc;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

/**
 * This class represents the <b>math_random_float</b> block from Blockly into the AST (abstract syntax tree).<br>
 * <br>
 * The user must provide name of the function and list of parameters. <br>
 * To create an instance from this class use the method {@link #make(BlocklyBlockProperties, BlocklyComment)}.<br>
 * The enumeration {@link FunctionNames} contains all allowed functions.
 */
public class MathRandomFloatFunct<V> extends Function<V> {
    private MathRandomFloatFunct(BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("MATH_RANDOM_FLOAT_FUNCT"), properties, comment);
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
        return new MathRandomFloatFunct<V>(properties, comment);
    }

    @Override
    public int getPrecedence() {
        return 10;
    }

    @Override
    public Assoc getAssoc() {
        return Assoc.NONE;
    }

    @Override
    public BlocklyType getReturnType() {
        return BlocklyType.NUMBER;
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitMathRandomFloatFunct(this);
    }

    @Override
    public String toString() {
        return "MathRandomFloatFunct []";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        return MathRandomFloatFunct.make(helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        return jaxbDestination;
    }

}
