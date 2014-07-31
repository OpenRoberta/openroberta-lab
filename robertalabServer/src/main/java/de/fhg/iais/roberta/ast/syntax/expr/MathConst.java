package de.fhg.iais.roberta.ast.syntax.expr;

import java.util.Locale;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.DbcException;

/**
 * This class represents the <b>math_constant</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate mathematical constant. See enum {@link Const} for all defined constants.<br/>
 * <br>
 * To create an instance from this class use the method {@link #make(Const)}.<br>
 */
public class MathConst extends Expr {
    private final Const mathConst;

    private MathConst(Const mathConst) {
        super(Phrase.Kind.MATH_CONST);
        this.mathConst = mathConst;
        setReadOnly();
    }

    /**
     * creates instance of {@link BoolConst}. This instance is read only and can not be modified.
     * 
     * @param mathConst, see enum {@link Const} for all defined constants
     * @return read only object of class {@link MathConst}.
     */
    public static MathConst make(Const mathConst) {
        return new MathConst(mathConst);
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
    public String toString() {
        return "MathConst [" + this.mathConst + "]";
    }

    @Override
    public void generateJava(StringBuilder sb, int indentation) {
        sb.append(this.mathConst);
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
         * get constant from {@link Const} from string parameter. It is possible for one constant to have multiple string mappings.
         * Throws exception if the constant does not exists.
         * 
         * @param name of the contant
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
