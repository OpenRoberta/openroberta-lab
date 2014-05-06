package de.fhg.iais.roberta.ast.syntax.expr;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * the top class of all expressions. There are two ways for a client to find out which kind of expression an {@link #Expr}-object is:<br>
 * - {@link #getKind()}<br>
 * - {@link #getAs(Class)}<br>
 */
public abstract class Expr extends Phrase {
    /**
     * returns the expression if and only if it is an object of the class given as parameter. Otherwise an exception is thrown. The type of the returned
     * expression is
     * the type of the requested class.<br>
     * <i>Is this complexity really needed? Isn't brute-force casting at the client side sufficient?</i>
     * 
     * @param clazz the class the object must be an instance of to return without exception
     * @return the object casted to the type requested
     */
    @SuppressWarnings("unchecked")
    public final <T> T getAs(Class<T> clazz) {
        Assert.isTrue(clazz.equals(getKind().getExprClass()));
        return (T) this;
    }

    /**
     * @return the kind of the expression. See enum {@link Kind} for all kinds possible<br>
     */
    abstract public Kind getKind();

    /**
     * define the different kinds of expressions. If a new subclass of {@link #Expr} is created, this enum has to be extended. The new enum value has be
     * the return value of the method {@link Expr#getKind()} of the new class.
     */
    public static enum Kind {
        IntConst( IntConst.class ), Var( Var.class ), Unary( Unary.class ), Binary( Binary.class ), SensorExpr( SensorExpr.class );

        private final Class<?> clazz;

        private Kind(Class<?> clazz) {
            this.clazz = clazz;
        }

        public Class<?> getExprClass() {
            return this.clazz;
        }
    }
}
