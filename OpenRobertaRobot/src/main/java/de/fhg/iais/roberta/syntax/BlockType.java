package de.fhg.iais.roberta.syntax;

import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.syntax.action.ev3.BluetoothConnectAction;
import de.fhg.iais.roberta.syntax.action.ev3.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.ev3.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.ev3.BluetoothWaitForConnectionAction;
import de.fhg.iais.roberta.syntax.action.ev3.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.ev3.DriveAction;
import de.fhg.iais.roberta.syntax.action.ev3.LightAction;
import de.fhg.iais.roberta.syntax.action.ev3.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.ev3.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.ev3.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.ev3.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.ev3.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.ev3.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.ev3.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.ev3.ShowPictureAction;
import de.fhg.iais.roberta.syntax.action.ev3.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.ev3.ToneAction;
import de.fhg.iais.roberta.syntax.action.ev3.TurnAction;
import de.fhg.iais.roberta.syntax.action.ev3.VolumeAction;
import de.fhg.iais.roberta.syntax.blocksequence.ActivityTask;
import de.fhg.iais.roberta.syntax.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.blocksequence.StartActivityTask;
import de.fhg.iais.roberta.syntax.expr.Binary;
import de.fhg.iais.roberta.syntax.expr.BoolConst;
import de.fhg.iais.roberta.syntax.expr.ColorConst;
import de.fhg.iais.roberta.syntax.expr.EmptyList;
import de.fhg.iais.roberta.syntax.expr.ListCreate;
import de.fhg.iais.roberta.syntax.expr.MathConst;
import de.fhg.iais.roberta.syntax.expr.NullConst;
import de.fhg.iais.roberta.syntax.expr.NumConst;
import de.fhg.iais.roberta.syntax.expr.StringConst;
import de.fhg.iais.roberta.syntax.expr.Unary;
import de.fhg.iais.roberta.syntax.expr.Var;
import de.fhg.iais.roberta.syntax.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.functions.LenghtOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.functions.ListRepeat;
import de.fhg.iais.roberta.syntax.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.functions.MathSingleFunct;
import de.fhg.iais.roberta.syntax.functions.TextJoinFunct;
import de.fhg.iais.roberta.syntax.functions.TextPrintFunct;
import de.fhg.iais.roberta.syntax.methods.MethodCall;
import de.fhg.iais.roberta.syntax.methods.MethodIfReturn;
import de.fhg.iais.roberta.syntax.methods.MethodReturn;
import de.fhg.iais.roberta.syntax.methods.MethodVoid;
import de.fhg.iais.roberta.syntax.sensor.ev3.BrickSensor;
import de.fhg.iais.roberta.syntax.sensor.ev3.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.ev3.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.ev3.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.ev3.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.ev3.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.ev3.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.ev3.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.ev3.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.stmt.AssignStmt;
import de.fhg.iais.roberta.syntax.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.stmt.WaitTimeStmt;

/**
 * This enumeration gives all possible kind of objects that we can have to represent the AST (abstract syntax tree). All kind's are separated in four main
 * {@link Category}.
 */
public enum BlockType {
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

    private BlockType(Category category, Class<?> astClass, String... values) {
        this.category = category;
        this.astClass = astClass;
        this.blocklyNames = values;
    }

    /**
     * @return category in which {@link BlockType} belongs.
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