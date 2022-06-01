package de.fhg.iais.roberta.syntax.lang.expr;

import java.util.Locale;

import de.fhg.iais.roberta.util.syntax.BlockType;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.transformer.NepoField;
import de.fhg.iais.roberta.transformer.NepoOp;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

/**
 * This class represents the <b>math_constant</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate mathematical
 * constant. See enum {@link Const} for all defined constants.<br/>
 * <br>
 * To create an instance from this class use the method {@link #make(Const, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
@NepoOp(containerType = "MATH_CONST", blocklyType = BlocklyType.NUMBER)
public class MathConst<V> extends Expr<V> {
    @NepoField(name = BlocklyConstants.CONSTANT)
    public final Const mathConst;

    public MathConst(BlockType kind, BlocklyBlockProperties properties, BlocklyComment comment, Const mathConst) {
        super(kind, properties, comment);
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
        return new MathConst<V>(BlockTypeContainer.getByName("MATH_CONST"), properties, comment, mathConst);
    }

    /**
     * @return math constant.
     */
    public Const getMathConst() {
        return this.mathConst;
    }

    /**
     * This enum defines all possible math constant.
     */
    public enum Const {
        GOLDEN_RATIO(), PI(), E(), SQRT2(), SQRT1_2(), INFINITY();

        private final String[] values;

        Const(String... values) {
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

}
