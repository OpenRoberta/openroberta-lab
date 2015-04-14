package de.fhg.iais.roberta.ast.syntax;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.ast.syntax.action.ClearDisplayAction;
import de.fhg.iais.roberta.ast.syntax.action.DriveAction;
import de.fhg.iais.roberta.ast.syntax.action.LightAction;
import de.fhg.iais.roberta.ast.syntax.action.LightStatusAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorDriveStopAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorGetPowerAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorOnAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorSetPowerAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorStopAction;
import de.fhg.iais.roberta.ast.syntax.action.PlayFileAction;
import de.fhg.iais.roberta.ast.syntax.action.ShowPictureAction;
import de.fhg.iais.roberta.ast.syntax.action.ShowTextAction;
import de.fhg.iais.roberta.ast.syntax.action.ToneAction;
import de.fhg.iais.roberta.ast.syntax.action.TurnAction;
import de.fhg.iais.roberta.ast.syntax.action.VolumeAction;
import de.fhg.iais.roberta.ast.syntax.action.communication.BluetoothConnectAction;
import de.fhg.iais.roberta.ast.syntax.action.communication.BluetoothReceiveAction;
import de.fhg.iais.roberta.ast.syntax.action.communication.BluetoothSendAction;
import de.fhg.iais.roberta.ast.syntax.action.communication.BluetoothWaitForConnectionAction;
import de.fhg.iais.roberta.ast.syntax.expr.Binary;
import de.fhg.iais.roberta.ast.syntax.expr.BoolConst;
import de.fhg.iais.roberta.ast.syntax.expr.ColorConst;
import de.fhg.iais.roberta.ast.syntax.expr.EmptyList;
import de.fhg.iais.roberta.ast.syntax.expr.ListCreate;
import de.fhg.iais.roberta.ast.syntax.expr.MathConst;
import de.fhg.iais.roberta.ast.syntax.expr.NullConst;
import de.fhg.iais.roberta.ast.syntax.expr.NumConst;
import de.fhg.iais.roberta.ast.syntax.expr.StringConst;
import de.fhg.iais.roberta.ast.syntax.expr.Unary;
import de.fhg.iais.roberta.ast.syntax.expr.Var;
import de.fhg.iais.roberta.ast.syntax.expr.VarDeclaration;
import de.fhg.iais.roberta.ast.syntax.functions.GetSubFunct;
import de.fhg.iais.roberta.ast.syntax.functions.IndexOfFunct;
import de.fhg.iais.roberta.ast.syntax.functions.LenghtOfIsEmptyFunct;
import de.fhg.iais.roberta.ast.syntax.functions.ListGetIndex;
import de.fhg.iais.roberta.ast.syntax.functions.ListRepeat;
import de.fhg.iais.roberta.ast.syntax.functions.ListSetIndex;
import de.fhg.iais.roberta.ast.syntax.functions.MathConstrainFunct;
import de.fhg.iais.roberta.ast.syntax.functions.MathNumPropFunct;
import de.fhg.iais.roberta.ast.syntax.functions.MathOnListFunct;
import de.fhg.iais.roberta.ast.syntax.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.ast.syntax.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.ast.syntax.functions.MathSingleFunct;
import de.fhg.iais.roberta.ast.syntax.functions.TextJoinFunct;
import de.fhg.iais.roberta.ast.syntax.functions.TextPrintFunct;
import de.fhg.iais.roberta.ast.syntax.methods.MethodCall;
import de.fhg.iais.roberta.ast.syntax.methods.MethodIfReturn;
import de.fhg.iais.roberta.ast.syntax.methods.MethodReturn;
import de.fhg.iais.roberta.ast.syntax.methods.MethodVoid;
import de.fhg.iais.roberta.ast.syntax.sensor.BrickSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.ColorSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.EncoderSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.GetSampleSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.GyroSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.InfraredSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.TimerSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.TouchSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.UltrasonicSensor;
import de.fhg.iais.roberta.ast.syntax.stmt.AssignStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.IfStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.RepeatStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtFlowCon;
import de.fhg.iais.roberta.ast.syntax.stmt.WaitStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.ast.syntax.tasks.ActivityTask;
import de.fhg.iais.roberta.ast.syntax.tasks.MainTask;
import de.fhg.iais.roberta.ast.syntax.tasks.StartActivityTask;
import de.fhg.iais.roberta.ast.typecheck.NepoInfo;
import de.fhg.iais.roberta.ast.typecheck.NepoInfos;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.hardwarecomponents.Category;

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

    private final BlocklyBlockProperties property;
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
    public Phrase(Kind kind, BlocklyBlockProperties property, BlocklyComment comment) {
        Assert.isTrue(property != null, "block property is null!");
        this.kind = kind;
        this.property = property;
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
        return this.property;
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
     * @return converts AST representation of block to JAXB representation of block
     */
    public abstract Block astToBlock();

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
        COLOR_SENSING( Category.SENSOR, ColorSensor.class, BlocklyConstants.ROB_SENSOR_COLOUR_GET_SAMPLE ),
        TOUCH_SENSING( Category.SENSOR, TouchSensor.class, BlocklyConstants.ROB_SENSOR_TOUCH_IS_PRESSED ),
        ULTRASONIC_SENSING( Category.SENSOR, UltrasonicSensor.class, BlocklyConstants.ROB_SENSOR_ULTRASONIC_GET_SAMPLE ),
        INFRARED_SENSING( Category.SENSOR, InfraredSensor.class, BlocklyConstants.ROB_SENSOR_INFRARED_GET_SAMPLE ),
        ENCODER_SENSING( Category.SENSOR, EncoderSensor.class, BlocklyConstants.ROB_SENSOR_ENCODER_GET_SAMPLE, BlocklyConstants.ROB_SENSORS_ENCODER_RESET ),
        BRICK_SENSING( Category.SENSOR, BrickSensor.class, BlocklyConstants.ROB_SENSOR_KEY_IS_PRESSED ),
        GYRO_SENSING( Category.SENSOR, GyroSensor.class, BlocklyConstants.ROB_SENSOR_GYRO_GET_SAMPLE, BlocklyConstants.ROB_SENSORS_GYRO_RESET ),
        TIMER_SENSING( Category.SENSOR, TimerSensor.class, BlocklyConstants.ROB_SENSOR_TIMER_GET_SAMPLE, BlocklyConstants.ROB_SENSORS_TIMER_RESET ),
        SENSOR_GET_SAMPLE( Category.SENSOR, GetSampleSensor.class, BlocklyConstants.ROB_SENSOR_GET_SAMPLE ),
        EXPR_LIST( Category.EXPR, null ),
        STRING_CONST( Category.EXPR, StringConst.class, BlocklyConstants.STRING_CONST_TEXT ),
        PICK_COLOR_CONST( Category.EXPR, ColorConst.class, BlocklyConstants.ROB_COLOUR_PICKER ),
        NULL_CONST( Category.EXPR, NullConst.class, BlocklyConstants.LOGICAL_NULL ),
        BOOL_CONST( Category.EXPR, BoolConst.class, BlocklyConstants.LOGICAL_BOOLEAN ),
        NUM_CONST( Category.EXPR, NumConst.class, BlocklyConstants.MATH_NUMBER ),
        MATH_CONST( Category.EXPR, MathConst.class, BlocklyConstants.MATH_CONSTANT ),
        EMPTY_LIST( Category.EXPR, EmptyList.class, BlocklyConstants.LISTS_CREATE_EMPTY ),
        VAR( Category.EXPR, Var.class, BlocklyConstants.VARIABLE_GET ),
        VAR_DECLARATION( Category.EXPR, VarDeclaration.class, BlocklyConstants.ROB_LOCAL_VARIABLES_DECLARE, BlocklyConstants.ROB_GLOBAL_VARIABLES_DECLARE ),
        UNARY( Category.EXPR, Unary.class, BlocklyConstants.LOGIC_NEGATE ),
        BINARY(
            Category.EXPR,
            Binary.class,
            BlocklyConstants.LOGIC_COMPARE,
            BlocklyConstants.LOGIC_COPERATION,
            BlocklyConstants.MATH_ARITHMETIC,
            BlocklyConstants.MATH_CHANGE,
            BlocklyConstants.MATH_MODULO,
            BlocklyConstants.TEXT_APPEND ),
        SENSOR_EXPR( Category.EXPR, null ),
        ACTION_EXPR( Category.EXPR, null ),
        EMPTY_EXPR( Category.EXPR, null ),
        FUNCTION_EXPR( Category.EXPR, null ),
        METHOD_EXPR( Category.EXPR, null ),
        FUNCTIONS( Category.EXPR, null ),
        START_ACTIVITY_TASK( Category.EXPR, StartActivityTask.class, BlocklyConstants.ROB_CONTROLS_START_ACTIVITY ),
        IF_STMT(
            Category.STMT,
            IfStmt.class,
            BlocklyConstants.LOGIC_TERNARY,
            BlocklyConstants.CONTROLS_IF,
            BlocklyConstants.ROB_CONTROLS_IF,
            BlocklyConstants.ROB_CONTROLS_IF_ELSE ),
        REPEAT_STMT(
            Category.STMT,
            RepeatStmt.class,
            BlocklyConstants.ROB_CONTROLS_LOOP_FOREVER,
            BlocklyConstants.CONTROLS_WHILE_UNTIL,
            BlocklyConstants.CONTROLS_FOR,
            BlocklyConstants.CONTROLS_REPEAT_EXT,
            BlocklyConstants.CONTROLS_REPEAT,
            BlocklyConstants.CONTROLS_FOR_EACH ),
        EXPR_STMT( Category.STMT, null ),
        STMT_LIST( Category.STMT, null ),
        ASSIGN_STMT( Category.STMT, AssignStmt.class, BlocklyConstants.VARIABLE_SET ),
        AKTION_STMT( Category.STMT, null ),
        SENSOR_STMT( Category.STMT, null ),
        FUNCTION_STMT( Category.STMT, null ),
        METHOD_STMT( Category.STMT, null ),
        STMT_FLOW_CONTROL( Category.STMT, StmtFlowCon.class, BlocklyConstants.CONTROLS_FLOW_STATEMENT ),
        WAIT_STMT( Category.STMT, WaitStmt.class, BlocklyConstants.ROB_CONTROLS_WAIT_FOR, BlocklyConstants.ROB_CONTROLS_WAIT ),
        WAIT_TIME( Category.STMT, WaitTimeStmt.class, BlocklyConstants.ROB_CONTROLS_WAIT_TIME ),
        TURN_ACTION( Category.ACTOR, TurnAction.class, BlocklyConstants.ROB_ACTIONS_MOTOR_DIFF_TURN, BlocklyConstants.ROB_ACTIONS_MOTOR_DIFF_TURN_FOR ),
        DRIVE_ACTION( Category.ACTOR, DriveAction.class, BlocklyConstants.ROB_ACTIONS_MOTOR_DIFF_ON, BlocklyConstants.ROB_ACTIONS_MOTOR_DIFF_ON_FOR ),
        SHOW_TEXT_ACTION( Category.ACTOR, ShowTextAction.class, BlocklyConstants.ROB_ACTIONS_DISPLAY_TEXT ),
        SHOW_PICTURE_ACTION( Category.ACTOR, ShowPictureAction.class, BlocklyConstants.ROB_ACTIONS_DISPLAY_PICTURE ),
        TONE_ACTION( Category.ACTOR, ToneAction.class, BlocklyConstants.ROB_ACTIONS_PLAY_TONE ),
        LIGHT_ACTION( Category.ACTOR, LightAction.class, BlocklyConstants.ROB_ACTIONS_BRICK_LIGHT_ON ),
        CLEAR_DISPLAY_ACTION( Category.ACTOR, ClearDisplayAction.class, BlocklyConstants.ROB_ACTIONS_DISPLAY_CLEAR ),
        MOTOR_ON_ACTION( Category.ACTOR, MotorOnAction.class, BlocklyConstants.ROB_ACTIONS_MOTOR_ON, BlocklyConstants.ROB_ACTIONS_MOTOR_ON_FOR ),
        MOTOR_GET_POWER_ACTION( Category.ACTOR, MotorGetPowerAction.class, BlocklyConstants.ROB_ACTIONS_MOTOR_GET_POWER ),
        MOTOR_SET_POWER_ACTION( Category.ACTOR, MotorSetPowerAction.class, BlocklyConstants.ROB_ACTIONS_MOTOR_SET_POWER ),
        MOTOR_STOP_ACTION( Category.ACTOR, MotorStopAction.class, BlocklyConstants.ROB_ACTIONS_MOTOR_STOP ),
        PLAY_FILE_ACTION( Category.ACTOR, PlayFileAction.class, BlocklyConstants.ROB_ACTIONS_PLAY_FILE ),
        VOLUME_ACTION( Category.ACTOR, VolumeAction.class, BlocklyConstants.ROB_ACTIONS_PLAY_SET_VOLUME, BlocklyConstants.ROB_ACTIONS_PLAY_GET_VOLUME ),
        LIGHT_STATUS_ACTION(
            Category.ACTOR,
            LightStatusAction.class,
            BlocklyConstants.ROB_ACTIONS_BRICK_LIGHT_OFF,
            BlocklyConstants.ROB_ACTIONS_BRICK_LIGHT_RESET ),
        STOP_ACTION( Category.ACTOR, MotorDriveStopAction.class, BlocklyConstants.ROB_ACTIONS_MOTOR_DIFF_STOP ),
        MAIN_TASK( Category.TASK, MainTask.class, BlocklyConstants.ROB_CONTROLS_START ),
        ACTIVITY_TASK( Category.TASK, ActivityTask.class, BlocklyConstants.ROB_CONTROLS_ACTIVITY ),
        LOCATION( Category.TASK, null ),
        TEXT_INDEX_OF_FUNCT( Category.FUNCTION, IndexOfFunct.class, BlocklyConstants.LISTS_INDEX_OF ),
        TEXT_CHAR_AT_FUNCT( Category.FUNCTION, null ),
        GET_SUB_FUNCT( Category.FUNCTION, GetSubFunct.class, BlocklyConstants.LISTS_GET_SUBLIST ),
        MATH_SINGLE_FUNCT( Category.FUNCTION, MathSingleFunct.class, BlocklyConstants.MATH_ROUND, BlocklyConstants.MATH_TRIG, BlocklyConstants.MATH_SINGLE ),
        MATH_ON_LIST_FUNCT( Category.FUNCTION, MathOnListFunct.class, BlocklyConstants.MATH_ON_LIST ),
        MATH_CONSTRAIN_FUNCT( Category.FUNCTION, MathConstrainFunct.class, BlocklyConstants.MATH_ON_CONSTRAINT ),
        MATH_RANDOM_INT_FUNCT( Category.FUNCTION, MathRandomIntFunct.class, BlocklyConstants.MATH_RANDOM_INT ),
        MATH_RANDOM_FLOAT_FUNCT( Category.FUNCTION, MathRandomFloatFunct.class, BlocklyConstants.MATH_RANDOM_FLOAT ),
        MATH_NUM_PROP_FUNCT( Category.FUNCTION, MathNumPropFunct.class, BlocklyConstants.MATH_NUMBER_PROPERTY ),
        LENGHT_OF_IS_EMPTY_FUNCT( Category.FUNCTION, LenghtOfIsEmptyFunct.class, BlocklyConstants.LISTS_LENGTH, BlocklyConstants.LISTS_IS_EMPTY ),
        TEXT_JOIN_FUNCT( Category.FUNCTION, TextJoinFunct.class, BlocklyConstants.ROB_TEXT_JOIN, BlocklyConstants.TEXT_JOIN ),
        TEXT_TRIM_FUNCT( Category.FUNCTION, null ),
        TEXT_PRINT_FUNCT( Category.FUNCTION, TextPrintFunct.class, BlocklyConstants.TEXT_PRINT ),
        TEXT_PROMPT_FUNCT( Category.FUNCTION, null ),
        LIST_REPEAT_FUNCT( Category.FUNCTION, ListRepeat.class, BlocklyConstants.LISTS_REPEAT ),
        LIST_CREATE( Category.EXPR, ListCreate.class, BlocklyConstants.LISTS_CREATE_WITH, BlocklyConstants.ROB_LISTS_CREATE_WITH ),
        LIST_INDEX_OF( Category.FUNCTION, ListGetIndex.class, BlocklyConstants.LISTS_GET_INDEX ),
        LIST_SET_INDEX( Category.FUNCTION, ListSetIndex.class, BlocklyConstants.LISTS_SET_INDEX ),
        TEXT_CHANGE_CASE_FUNCT( Category.FUNCTION, null ),
        METHOD_IF_RETURN( Category.METHOD, MethodIfReturn.class, BlocklyConstants.ROB_PROCEDURES_IF_RETURN ),
        METHOD_VOID( Category.METHOD, MethodVoid.class, BlocklyConstants.ROB_PROCEDURES_NO_RETURN ),
        METHOD_CALL( Category.METHOD, MethodCall.class, BlocklyConstants.ROB_PROCEDURES_CALL_NO_RETURN, BlocklyConstants.ROB_PROCEDURES_CALL_RETURN ),
        METHOD_RETURN( Category.METHOD, MethodReturn.class, BlocklyConstants.ROB_PROCEDURES_RETURN ),
        BLUETOOTH_CONNECT_ACTION( Category.ACTOR, BluetoothConnectAction.class, BlocklyConstants.COM_START_CONNECTION ),
        BLUETOOTH_SEND_ACTION( Category.ACTOR, BluetoothSendAction.class, BlocklyConstants.COM_SEND_BLOCK ),
        BLUETOOTH_WAIT_FOR_CONNECTION_ACTION( Category.ACTOR, BluetoothWaitForConnectionAction.class, BlocklyConstants.COM_WAIT_CONNECTION ),
        BLUETOOTH_RECEIVED_ACTION( Category.ACTOR, BluetoothReceiveAction.class, BlocklyConstants.COM_RECEIVE_BLOCK );

        private final Category category;
        private final Class<?> astClass;
        private final String[] blocklyNames;

        private Kind(Category category, Class<?> astClass, String... values) {
            this.category = category;
            this.astClass = astClass;
            this.blocklyNames = values;
        }

        /**
         * @return category in which {@link Kind} belongs.
         */
        public Category getCategory() {
            return this.category;
        }

        /**
         * @return the astClass
         */
        public Class<?> getAstClass() {
            return this.astClass;
        }

        /**
         * @return the blocklyNames
         */
        public String[] getBlocklyNames() {
            return this.blocklyNames;
        }
    }
}
