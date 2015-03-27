package de.fhg.iais.roberta.ast.transformer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import de.fhg.iais.roberta.ast.syntax.Phrase;
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
import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.dbc.DbcException;

public enum BlocklyBlocks {
    // @formatter: off
    CLEAR_DISPLAY_ACTION( ClearDisplayAction.class, "robActions_display_clear" ),
    MOTOR_ON_ACTION( MotorOnAction.class, "robActions_motor_on", "robActions_motor_on_for" ),
    DRIVE_ACTION( DriveAction.class, "robActions_motorDiff_on", "robActions_motorDiff_on_for" ),
    TURN_ACTION( TurnAction.class, "robActions_motorDiff_turn", "robActions_motorDiff_turn_for" ),
    MOTOR_DRIVE_STOP( MotorDriveStopAction.class, "robActions_motorDiff_stop" ),
    MOTOR_GET_POWER( MotorGetPowerAction.class, "robActions_motor_getPower" ),
    MOTOR_SET_POWER( MotorSetPowerAction.class, "robActions_motor_setPower" ),
    MOTOR_STOP( MotorStopAction.class, "robActions_motor_stop" ),
    SHOW_TEXT( ShowTextAction.class, "robActions_display_text" ),
    DISPLAY_PICTURE( ShowPictureAction.class, "robActions_display_picture" ),
    TONE_ACTION( ToneAction.class, "robActions_play_tone" ),
    PLAY_FILE( PlayFileAction.class, "robActions_play_file" ),
    SET_VOLUME( VolumeAction.class, "robActions_play_setVolume", "robActions_play_getVolume" ),
    LIGHT_ACTION( LightAction.class, "robActions_brickLight_on" ),
    LIGHT_STATUS_ACTION( LightStatusAction.class, "robActions_brickLight_off", "robActions_brickLight_reset" ),
    TOUCH_SENSOR( TouchSensor.class, "robSensors_touch_isPressed" ),
    ULTRASONIC_SENSOR( UltrasonicSensor.class, "robSensors_ultrasonic_getSample" ),
    COLOR_SENSOR( ColorSensor.class, "robSensors_colour_getSample" ),
    INFRARED_SENSOR( InfraredSensor.class, "robSensors_infrared_getSample" ),
    ENCODESR_SENSOR( EncoderSensor.class, "robSensors_encoder_getSample", "robSensors_encoder_reset" ),
    BRICK_SENSOR( BrickSensor.class, "robSensors_key_isPressed" ),
    GYRO_SENSOR( GyroSensor.class, "robSensors_gyro_getSample", "robSensors_gyro_reset" ),
    TIMER_SENSOR( TimerSensor.class, "robSensors_timer_getSample", "robSensors_timer_reset" ),
    GET_SAMPLE_SENSOR( GetSampleSensor.class, "robSensors_getSample" ),
    BINARY( Binary.class, "logic_compare", "logic_operation", "math_arithmetic", "math_change", "math_modulo", "text_append" ),
    UNARY( Unary.class, "logic_negate" ),
    BOOL_CONST( BoolConst.class, "logic_boolean" ),
    COLOR_CONST( ColorConst.class, "robColour_picker" ),
    NULL_CONST( NullConst.class, "logic_null" ),
    IF_STMT( IfStmt.class, "logic_ternary", "controls_if", "robControls_if", "robControls_ifElse" ),
    MATH_CONST( MathConst.class, "math_constant" ),
    MATH_NUM_PROPERTY( MathNumPropFunct.class, "math_number_property" ),
    MATH_ON_LIST( MathOnListFunct.class, "math_on_list" ),
    MATH_CONSTRAIN( MathConstrainFunct.class, "math_constrain" ),
    MATH_RANDOM_INT( MathRandomIntFunct.class, "math_random_int" ),
    MATH_RANDOM_FLOAT( MathRandomFloatFunct.class, "math_random_float" ),
    STRING_CONST( StringConst.class, "text" ),
    NUM_CONST( NumConst.class, "math_number" ),
    TEXT_JOIN_FUNCT( TextJoinFunct.class, "robText_join", "text_join" ),
    TEXT_PRINT_FUNCT( TextPrintFunct.class, "text_print" ),
    LENGTH_ISEMPTY_FUNC( LenghtOfIsEmptyFunct.class, "lists_length", "lists_isEmpty" ),
    INDEX_OF( IndexOfFunct.class, "lists_indexOf" ),
    EMPTY_LIST( EmptyList.class, "lists_create_empty" ),
    LIST_CREATE( ListCreate.class, "lists_create_with", "robLists_create_with" ),
    LIST_REPEAT( ListRepeat.class, "lists_repeat" ),
    LIST_GET_INDEX( ListGetIndex.class, "lists_getIndex" ),
    LIST_SET_INDEX( ListSetIndex.class, "lists_setIndex" ),
    GET_SUB_FUNC( GetSubFunct.class, "lists_getSublist" ),
    ASSIGN_STMT( AssignStmt.class, "variables_set" ),
    VAR_GET( Var.class, "variables_get" ),
    VAR_DECLARATION( VarDeclaration.class, "robLocalVariables_declare", "robGlobalvariables_declare" ),
    WAIT_STMT( WaitStmt.class, "robControls_wait_for", "robControls_wait" ),
    WAIT_TIME_STMT( WaitTimeStmt.class, "robControls_wait_time" ),
    REPEAT_STMT(
        RepeatStmt.class,
        "robControls_loopForever",
        "controls_whileUntil",
        "controls_for",
        "controls_repeat_ext",
        "controls_repeat",
        "controls_forEach" ),
    FLOW_CONTROL_STMT( StmtFlowCon.class, "controls_flow_statements" ),
    MAIN_TASK( MainTask.class, "robControls_start" ),
    ACTIVITY_TASK( ActivityTask.class, "robControls_activity" ),
    START_ACTIVITY_TASK( StartActivityTask.class, "robControls_start_activity" ),
    METHOD_VOID( MethodVoid.class, "robProcedures_defnoreturn" ),
    METHOD_RETURN( MethodReturn.class, "robProcedures_defreturn" ),
    METHOD_IF_RETURN( MethodIfReturn.class, "robProcedures_ifreturn" ),
    METHOD_CALL( MethodCall.class, "robProcedures_callnoreturn", "robProcedures_callreturn" ),
    MATH_SINGLE_FUNCT( MathSingleFunct.class, "math_round", "math_trig", "math_single" );
    // @formatter:on

    private final Class<?> astClass;
    private final String[] blocklyNames;

    private BlocklyBlocks(Class<?> astClass, String... values) {
        this.astClass = astClass;
        this.blocklyNames = values;
    }

    @SuppressWarnings("unchecked")
    public static <V> Phrase<V> get(Block block, JaxbBlocklyProgramTransformer<V> helper) {

        String className = "";
        if ( block == null ) {
            throw new DbcException("Invalid block: " + block);
        }
        String sUpper = block.getType().trim();
        for ( BlocklyBlocks co : BlocklyBlocks.values() ) {
            if ( co.toString().equals(sUpper) ) {
                className = co.astClass.getName();
                break;
            }
            for ( String value : co.blocklyNames ) {
                if ( sUpper.equals(value) ) {
                    className = co.astClass.getName();
                    break;
                }
            }
        }
        if ( className.equals("") ) {
            throw new DbcException("Invalid Block: " + block.getType());
        }
        Method method;

        try {
            method = Class.forName(className).getMethod("jaxbToAst", Block.class, JaxbAstTransformer.class);
            return (Phrase<V>) method.invoke(null, block, helper);
        } catch ( NoSuchMethodException | SecurityException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException
            | InvocationTargetException | DbcException e ) {
            throw new DbcException(e.getCause().getMessage());
        }
    }
}
