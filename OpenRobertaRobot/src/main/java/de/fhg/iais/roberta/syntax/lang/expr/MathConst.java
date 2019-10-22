package de.fhg.iais.roberta.syntax.lang.expr;

import java.util.List;
import java.util.Locale;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.transformer.Ast2JaxbHelper;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.ILanguageVisitor;

/**
 * This class represents the <b>math_constant</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate mathematical
 * constant. See enum {@link Const} for all defined constants.<br/>
 * <br>
 * To create an instance from this class use the method {@link #make(Const, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class MathConst<V> extends Expr<V> {
    private final Const mathConst;

    private MathConst(Const mathConst, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("MATH_CONST"), properties, comment);
        Assert.isTrue(mathConst != null);
        this.mathConst = mathConst;
        setReadOnly();
    }

    /**
     * creates instance of {@link BoolConst}. This instance is read only and can not be modified.
     *
     * @param mathConst, see enum {@link Const} for all defined constants; must be <b>not</b> null,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link MathConst}
     */
    public static <V> MathConst<V> make(Const mathConst, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new MathConst<V>(mathConst, properties, comment);
    }

    /**
     * @return math constant.
     */
    public Const getMathConst() {
        return this.mathConst;
    }

    @Override
    public int getPrecedence() {
        return 999;
    }

    @Override
    public Assoc getAssoc() {
        return Assoc.NONE;
    }

    @Override
    public BlocklyType getVarType() {
        return BlocklyType.NUMBER;
    }

    @Override
    public String toString() {
        return "MathConst [" + this.mathConst + "]";
    }

    /**
     * This enum defines all possible math constant.
     */
    public static enum Const {
        GOLDEN_RATIO(), PI(), E(), SQRT2(), SQRT1_2(), INFINITY();

        private final String[] values;

        private Const(String... values) {
            this.values = values;
        }

        /**
         * get constant from {@link Const} from string parameter. It is possible for one constant to have multiple string mappings. Throws exception if the
         * constant does not exists.
         *
         * @param name of the constant
         * @return constant from the enum {@link Const}
         */
        public static Const get(String s) {
            if ( s == null || s.isEmpty() ) {
                throw new DbcException("Invalid binary operator symbol: " + s);
            }
            String sUpper = s.trim().toUpperCase(Locale.GERMAN);
            for ( Const co : Const.values() ) {
                if ( co.toString().equals(sUpper) ) {
                    return co;
                }
                for ( String value : co.values ) {
                    if ( sUpper.equals(value) ) {
                        return co;
                    }
                }
            }
            throw new DbcException("Invalid binary constant symbol: " + s);
        }
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ILanguageVisitor<V>) visitor).visitMathConst(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        List<Field> fields = helper.extractFields(block, (short) 1);
        String field = helper.extractField(fields, BlocklyConstants.CONSTANT);
        return MathConst.make(MathConst.Const.get(field), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2JaxbHelper.setBasicProperties(this, jaxbDestination);
        Ast2JaxbHelper.addField(jaxbDestination, BlocklyConstants.CONSTANT, getMathConst().name());
        return jaxbDestination;
    }
}
