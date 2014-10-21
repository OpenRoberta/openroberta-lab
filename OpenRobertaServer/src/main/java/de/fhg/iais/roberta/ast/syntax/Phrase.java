package de.fhg.iais.roberta.ast.syntax;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.ast.typecheck.NepoInfo;
import de.fhg.iais.roberta.ast.typecheck.NepoInfos;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;

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
abstract public class Phrase<V> {
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(Phrase.class);

    private boolean readOnly = false;

    private final BlocklyBlockProperties id;
    private final BlocklyComment comment;
    private final Kind kind;

    private final NepoInfos infos = new NepoInfos(); // the content of the info object is MUTABLE !!!

    /**
     * This constructor set the kind of the object used in the AST (abstract syntax tree). All possible kinds can be found in {@link Kind}.
     *
     * @param kind of the the object used in AST,
     * @param disabled,
     * @param comment that the user added to the block
     */
    public Phrase(Kind kind, BlocklyBlockProperties id, BlocklyComment comment) {
        this.kind = kind;
        this.id = id;
        this.comment = comment;
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
     * @return the kind of the expression. See enum {@link Kind} for all kinds possible<br>
     */
    public final Kind getKind() {
        return this.kind;
    }

    public BlocklyBlockProperties getProperty() {
        return this.id;
    }

    /**
     * @return comment that the user added to the block
     */
    public final BlocklyComment getComment() {
        return this.comment;
    }

    /**
     * add an info (error, warning e.g.) to this phrase
     *
     * @param info to be added
     */
    public final void addInfo(NepoInfo info) {
        this.infos.addInfo(info);
    }

    public final NepoInfos getInfos() {
        return this.infos;
    }

    /**
     * visit this phrase. Inside this method is a LOG statement, usually commented out. If it is commented in, it will generate a nice trace of the phrases
     * of the AST when they are visited.
     *
     * @param visitor to be used
     */
    public final V visit(AstVisitor<V> visitor) {
        // LOG.info("{}", this);
        return this.accept(visitor);
    }

    /**
     * accept an visitor
     */
    protected abstract V accept(AstVisitor<V> visitor);

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
     * This enumeration gives all possible kind of objects that we can have to represent the AST (abstract syntax tree). All kind's are separated in four main
     * {@link Category}.
     */
    public static enum Kind {
        COLOR_SENSING( Category.SENSOR ),
        TOUCH_SENSING( Category.SENSOR ),
        ULTRASONIC_SENSING( Category.SENSOR ),
        INFRARED_SENSING( Category.SENSOR ),
        ENCODER_SENSING( Category.SENSOR ),
        BRICK_SENSIG( Category.SENSOR ),
        GYRO_SENSIG( Category.SENSOR ),
        TIMER_SENSING( Category.SENSOR ),
        SENSOR_GET_SAMPLE( Category.SENSOR ),
        EXPR_LIST( Category.EXPR ),
        STRING_CONST( Category.EXPR ),
        PICK_COLOR_CONST( Category.EXPR ),
        NULL_CONST( Category.EXPR ),
        BOOL_CONST( Category.EXPR ),
        NUM_CONST( Category.EXPR ),
        MATH_CONST( Category.EXPR ),
        EMPTY_LIST( Category.EXPR ),
        VAR( Category.EXPR ),
        UNARY( Category.EXPR ),
        BINARY( Category.EXPR ),
        SENSOR_EXPR( Category.EXPR ),
        ACTION_EXPR( Category.EXPR ),
        EMPTY_EXPR( Category.EXPR ),
        FUNCTIONS( Category.EXPR ),
        START_ACTIVITY_TASK( Category.EXPR ),
        IF_STMT( Category.STMT ),
        REPEAT_STMT( Category.STMT ),
        EXPR_STMT( Category.STMT ),
        STMT_LIST( Category.STMT ),
        ASSIGN_STMT( Category.STMT ),
        AKTION_STMT( Category.STMT ),
        SENSOR_STMT( Category.STMT ),
        STMT_FLOW_CONTROL( Category.STMT ),
        WAIT_STMT( Category.STMT ),
        TURN_ACTION( Category.ACTOR ),
        DRIVE_ACTION( Category.ACTOR ),
        SHOW_TEXT_ACTION( Category.ACTOR ),
        SHOW_PICTURE_ACTION( Category.ACTOR ),
        TONE_ACTION( Category.ACTOR ),
        LIGHT_ACTION( Category.ACTOR ),
        CLEAR_DISPLAY_ACTION( Category.ACTOR ),
        MOTOR_ON_ACTION( Category.ACTOR ),
        MOTOR_GET_POWER_ACTION( Category.ACTOR ),
        MOTOR_SET_POWER_ACTION( Category.ACTOR ),
        MOTOR_STOP_ACTION( Category.ACTOR ),
        PLAY_FILE_ACTION( Category.ACTOR ),
        VOLUME_ACTION( Category.ACTOR ),
        LIGHT_STATUS_ACTION( Category.ACTOR ),
        STOP_ACTION( Category.ACTOR ),
        MAIN_TASK( Category.TASK ),
        ACTIVITY_TASK( Category.TASK ),
        LOCATION( Category.TASK );

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
