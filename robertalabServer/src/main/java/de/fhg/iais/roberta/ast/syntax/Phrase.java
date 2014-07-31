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

    /**
     * This constructor set the kind of the object used in the AST (abstract syntax tree). All possible kinds can be found in {@link Kind}.
     * 
     * @param kind of the the object used in AST
     */
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

    /**
     * This enumeration gives all possible kind of objects that we can have to represent the AST (abstract syntax tree). All kind's are separated in four main
     * {@link Category}.
     */
    public static enum Kind {
        COLOR_SENSING( Category.SENSOR ),
        TOUCH_SENSING( Category.SENSOR ),
        ULTRASONIC_SENSING( Category.SENSOR ),
        INFRARED_SENSING( Category.SENSOR ),
        TACHO_SENSING( Category.SENSOR ),
        BRICK_SENSIG( Category.SENSOR ),
        GYRO_SENSIG( Category.SENSOR ),
        TIMER_SENSING( Category.SENSOR ),
        EXPR_LIST( Category.EXPR ),
        STRING_CONST( Category.EXPR ),
        COLOR_CONST( Category.EXPR ),
        NULL_CONST( Category.EXPR ),
        BOOL_CONST( Category.EXPR ),
        NUM_CONST( Category.EXPR ),
        MATH_CONST( Category.EXPR ),
        VAR( Category.EXPR ),
        UNARY( Category.EXPR ),
        BINARY( Category.EXPR ),
        SENSOR_EXPR( Category.EXPR ),
        ACTION_EXPR( Category.EXPR ),
        EMPTY_EXPR( Category.EXPR ),
        FUNCTIONS( Category.EXPR ),
        IF_STMT( Category.STMT ),
        REPEAT_STMT( Category.STMT ),
        EXPR_STMT( Category.STMT ),
        STMT_LIST( Category.STMT ),
        ASSIGN_STMT( Category.STMT ),
        AKTION_STMT( Category.STMT ),
        SENSOR_STMT( Category.STMT ),
        STMT_FLOW_CONTROL( Category.STMT ),
        TURN_ACTION( Category.ACTOR ),
        DRIVE_ACTION( Category.ACTOR ),
        SHOW_TEXT_ACTION( Category.ACTOR ),
        SHOW_PICTURE_ACTION( Category.ACTOR ),
        TONE_ACTION( Category.ACTOR ),
        LIGHT_ACTION( Category.ACTOR ),
        CLEAR_DISPLAY_ACTION( Category.ACTOR ),
        MOTOR_ON_ACTION( Category.ACTOR ),
        MOTOR_GET_POWER_ACTION( Category.ACTOR ),
        MOTOR_STOP_ACTION( Category.ACTOR ),
        PLAY_FILE_ACTION( Category.ACTOR ),
        VOLUME_ACTION( Category.ACTOR ),
        LIGHT_STATUS_ACTION( Category.ACTOR ),
        STOP_ACTION( Category.ACTOR );

        private final Category category;

        private Kind(Category category) {
            this.category = category;
        }

        /**
         * @return category in which {@link Kind} belongs.
         */
        public Category getCategory() {
            return this.category;
        }
    }
}
