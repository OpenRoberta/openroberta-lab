package de.fhg.iais.roberta.ast.syntax;

import de.fhg.iais.roberta.dbc.Assert;

/**
 * the top class of all class used to represent the AST (abstract syntax tree) of a program. After construction an AST should be immutable. The logic to achieve
 * that is in this class. An object of a subclass of {@link Phrase} is initially writable, after the construction of the object has finished,
 * {@link #setReadOnly()} is called.
 * This cannot be undone later. It is expected that all subclasses of {@link #Phrase} do the following:<br>
 * - if in construction phase, they should use {@link #mayChange()} to assert that.<br>
 * - if the construction has finished and {@link #setReadOnly()} has been called, they should use {@link #isReadOnly()} to assert their immutability.<br>
 * <br>
 * There are two ways for a client to find out which kind a {@link #Phrase}-object is:<br>
 * - {@link #getKind()}<br>
 * - {@link #getAs(Class)}<br>
 */
abstract public class Phrase {
    private boolean readOnly = false;
    private final Kind kind;

    public Phrase(Kind kind) {
        this.kind = kind;
    }

    /**
     * @return true, if the object is writable/mutable. This is true, if {@link #setReadOnly()} has not yet been called for this object
     */
    public final boolean mayChange() {
        return !this.readOnly;
    }

    /**
     * @return true, if the object is read-only/immutable. This is true, if {@link #setReadOnly()} has been called for this object
     */
    public final boolean isReadOnly() {
        return this.readOnly;
    }

    /**
     * make this {@link Phrase}-object read-only/immutable. Should be called if the construction phase has finished
     */
    public final void setReadOnly() {
        this.readOnly = true;
    }

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
        Assert.isTrue(clazz.equals(this.getClass()));
        return (T) this;
    }

    /**
     * @return the kind of the expression. See enum {@link Kind} for all kinds possible<br>
     */
    public final Kind getKind() {
        return this.kind;
    }

    /**
     * append a newline, then append spaces up to an indentation level, then append an (optional) text<br>
     * helper for constructing readable {@link #toString()}- and {@link #generateJava(StringBuilder, int)}-methods for statement trees
     * 
     * @param sb the string builder, to which has to be appended
     * @param indentation number defining the level of indentation
     * @param text an (optional) text to append; may be null
     */
    protected final void appendNewLine(StringBuilder sb, int indentation, String text) {
        sb.append("\n");
        for ( int i = 0; i < indentation; i++ ) {
            sb.append(" ");
        }
        if ( text != null ) {
            sb.append(text);
        }
    }

    /**
     * append a human-readable Java representation of a phrase to a StringBuilder. <b>This representation MUST be correct Java code.</b>
     * 
     * @param sb the string builder, to which the Java representation has to be appended
     * @param indentation number defining the level of indentation, if needed for nested statement lists, e.g.
     */
    abstract public void generateJava(StringBuilder sb, int indentation);

    public static enum Kind {
        ColorSensor( Category.SENSOR ),
        TouchSensor( Category.SENSOR ),
        UltraSSensor( Category.SENSOR ),
        InfraredSensor( Category.SENSOR ),
        DrehSensor( Category.SENSOR ),
        SteinSensor( Category.SENSOR ),
        GyroSensor( Category.SENSOR ),
        ExprList( Category.EXPR ),
        StringConst( Category.EXPR ),
        ColourConst( Category.EXPR ),
        NullConst( Category.EXPR ),
        BoolConst( Category.EXPR ),
        NumConst( Category.EXPR ),
        MathConst( Category.EXPR ),
        Var( Category.EXPR ),
        Unary( Category.EXPR ),
        Binary( Category.EXPR ),
        SensorExpr( Category.EXPR ),
        EmptyExpr( Category.EXPR ),
        Funct( Category.EXPR ),
        IfStmt( Category.STMT ),
        RepeatStmt( Category.STMT ),
        ExprStmt( Category.STMT ),
        StmtList( Category.STMT ),
        AssignStmt( Category.STMT ),
        AktionStmt( Category.STMT ),
        SensorStmt( Category.STMT ),
        StmtFlowCon( Category.STMT ),
        TurnAktion( Category.AKTOR ),
        DriveAktion( Category.AKTOR ),
        ShowAktion( Category.AKTOR ),
        ToneAktion( Category.AKTOR ),
        LightAktion( Category.AKTOR ),
        MotorOnAction( Category.AKTOR );

        private final Category category;

        private Kind(Category category) {
            this.category = category;
        }

        public Category getCategory() {
            return this.category;
        }
    }

    public static enum Category {
        EXPR, SENSOR, AKTOR, STMT;
    }
}
