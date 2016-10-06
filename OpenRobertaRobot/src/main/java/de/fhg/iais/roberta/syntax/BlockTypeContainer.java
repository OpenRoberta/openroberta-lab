package de.fhg.iais.roberta.syntax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothCheckConnectAction;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothConnectAction;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothWaitForConnectionAction;
import de.fhg.iais.roberta.syntax.action.generic.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.generic.CurveAction;
import de.fhg.iais.roberta.syntax.action.generic.DriveAction;
import de.fhg.iais.roberta.syntax.action.generic.LightAction;
import de.fhg.iais.roberta.syntax.action.generic.LightSensorAction;
import de.fhg.iais.roberta.syntax.action.generic.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.generic.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.generic.ShowPictureAction;
import de.fhg.iais.roberta.syntax.action.generic.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.generic.ToneAction;
import de.fhg.iais.roberta.syntax.action.generic.TurnAction;
import de.fhg.iais.roberta.syntax.action.generic.VolumeAction;
import de.fhg.iais.roberta.syntax.blocksequence.ActivityTask;
import de.fhg.iais.roberta.syntax.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.blocksequence.StartActivityTask;
import de.fhg.iais.roberta.syntax.expr.Binary;
import de.fhg.iais.roberta.syntax.expr.BoolConst;
import de.fhg.iais.roberta.syntax.expr.ColorConst;
import de.fhg.iais.roberta.syntax.expr.ConnectConst;
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
import de.fhg.iais.roberta.syntax.functions.LengthOfIsEmptyFunct;
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
import de.fhg.iais.roberta.syntax.sensor.generic.BrickSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.stmt.AssignStmt;
import de.fhg.iais.roberta.syntax.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This container holds all possible kind of objects that we can have to represent the AST (abstract syntax tree). The objects are separated in four main
 * {@link Category}. They have a unique (String) name to refer to them (in a code generator visitor, for instance). They can be retrieved<br>
 * - either by their (globally) unique name or<br>
 * - a list of (globally) unique names, each of which is used as element name in blockly XML.<br>
 * A concrete root can add dynamically new elements to this container, when the robot is registered during robot startup (this is why this container is
 * <i>not</i> implemented as a enum type. Duriong registraion of new block types the uniqueness of names is guaranteed by this container.
 */
public class BlockTypeContainer {
    private static final Logger LOG = LoggerFactory.getLogger(BlockTypeContainer.class);

    private static final String[] NO_BLOCKLY_NAMES = new String[0];

    private static final List<String> loadedPropertyFiles = new ArrayList<>();

    private static final Map<String, BlockType> blockTypesByName = new HashMap<>();
    private static final Map<String, BlockType> blockTypesByBlocklyName = new HashMap<>();

    static {
        add("COLOR_SENSING", Category.SENSOR, ColorSensor.class, BlocklyConstants.ROB_SENSOR_COLOUR_GET_SAMPLE, BlocklyConstants.SIM_COLOUR_GET_SAMPLE);
        add("LIGHT_SENSING", Category.SENSOR, LightSensor.class, BlocklyConstants.ROB_SENSOR_LIGHT_GET_SAMPLE, BlocklyConstants.SIM_LIGHT_GET_SAMPLE);
        add("SOUND_SENSING", Category.SENSOR, SoundSensor.class, BlocklyConstants.ROB_SENSOR_SOUND_GET_SAMPLE, BlocklyConstants.SIM_SOUND_GET_SAMPLE);
        add("TOUCH_SENSING", Category.SENSOR, TouchSensor.class, BlocklyConstants.ROB_SENSOR_TOUCH_IS_PRESSED, BlocklyConstants.SIM_TOUCH_IS_PRESSED);
        add("COMPASS_SENSING", Category.SENSOR, CompassSensor.class, BlocklyConstants.ROB_SENSOR_COMPASS_GET_SAMPLE);
        add(
            "ULTRASONIC_SENSING",
            Category.SENSOR,
            UltrasonicSensor.class,
            BlocklyConstants.ROB_SENSOR_ULTRASONIC_GET_SAMPLE,
            BlocklyConstants.SIM_ULTRASONIC_GET_SAMPLE);
        add("INFRARED_SENSING", Category.SENSOR, InfraredSensor.class, BlocklyConstants.ROB_SENSOR_INFRARED_GET_SAMPLE);
        add(
            "ENCODER_SENSING",
            Category.SENSOR,
            EncoderSensor.class,
            BlocklyConstants.ROB_SENSOR_ENCODER_GET_SAMPLE,
            BlocklyConstants.ROB_SENSORS_ENCODER_RESET);
        add("BRICK_SENSING", Category.SENSOR, BrickSensor.class, BlocklyConstants.ROB_SENSOR_KEY_IS_PRESSED);
        add("GYRO_SENSING", Category.SENSOR, GyroSensor.class, BlocklyConstants.ROB_SENSOR_GYRO_GET_SAMPLE, BlocklyConstants.ROB_SENSORS_GYRO_RESET);
        add("TIMER_SENSING", Category.SENSOR, TimerSensor.class, BlocklyConstants.ROB_SENSOR_TIMER_GET_SAMPLE, BlocklyConstants.ROB_SENSORS_TIMER_RESET);
        add("SENSOR_GET_SAMPLE", Category.SENSOR, GetSampleSensor.class, BlocklyConstants.ROB_SENSOR_GET_SAMPLE, BlocklyConstants.SIM_GET_SAMPLE);
        add("EXPR_LIST", Category.EXPR);
        add("STRING_CONST", Category.EXPR, StringConst.class, BlocklyConstants.STRING_CONST_TEXT);
        add("COLOR_CONST", Category.EXPR, ColorConst.class, BlocklyConstants.ROB_COLOUR_PICKER);
        add("NULL_CONST", Category.EXPR, NullConst.class, BlocklyConstants.LOGICAL_NULL);
        add("BOOL_CONST", Category.EXPR, BoolConst.class, BlocklyConstants.LOGICAL_BOOLEAN);
        add("NUM_CONST", Category.EXPR, NumConst.class, BlocklyConstants.MATH_NUMBER);
        add("MATH_CONST", Category.EXPR, MathConst.class, BlocklyConstants.MATH_CONSTANT);
        add("CONNECTION_CONST", Category.EXPR, ConnectConst.class, BlocklyConstants.CONNECTION_NXT);
        add("EMPTY_LIST", Category.EXPR, EmptyList.class, BlocklyConstants.LISTS_CREATE_EMPTY);
        add("VAR", Category.EXPR, Var.class, BlocklyConstants.VARIABLE_GET);
        add(
            "VAR_DECLARATION",
            Category.EXPR,
            VarDeclaration.class,
            BlocklyConstants.ROB_LOCAL_VARIABLES_DECLARE,
            BlocklyConstants.ROB_GLOBAL_VARIABLES_DECLARE);
        add("UNARY", Category.EXPR, Unary.class, BlocklyConstants.LOGIC_NEGATE);
        add(
            "BINARY",
            Category.EXPR,
            Binary.class,
            BlocklyConstants.LOGIC_COMPARE,
            BlocklyConstants.LOGIC_COPERATION,
            BlocklyConstants.MATH_ARITHMETIC,
            BlocklyConstants.MATH_CHANGE,
            BlocklyConstants.ROB_MATH_CHANGE,
            BlocklyConstants.MATH_MODULO,
            BlocklyConstants.TEXT_APPEND);
        add("SENSOR_EXPR", Category.EXPR);
        add("ACTION_EXPR", Category.EXPR);
        add("EMPTY_EXPR", Category.EXPR);
        add("SHADOW_EXPR", Category.EXPR);
        add("FUNCTION_EXPR", Category.EXPR);
        add("METHOD_EXPR", Category.EXPR);
        add("FUNCTIONS", Category.EXPR);
        add("START_ACTIVITY_TASK", Category.EXPR, StartActivityTask.class, BlocklyConstants.ROB_CONTROLS_START_ACTIVITY);
        add(
            "IF_STMT",
            Category.STMT,
            IfStmt.class,
            BlocklyConstants.LOGIC_TERNARY,
            BlocklyConstants.CONTROLS_IF,
            BlocklyConstants.ROB_CONTROLS_IF,
            BlocklyConstants.ROB_CONTROLS_IF_ELSE);
        add(
            "REPEAT_STMT",
            Category.STMT,
            RepeatStmt.class,
            BlocklyConstants.ROB_CONTROLS_LOOP_FOREVER,
            BlocklyConstants.ROB_CONTROLS_LOOP_FOREVER_ARDU,
            BlocklyConstants.CONTROLS_WHILE_UNTIL,
            BlocklyConstants.CONTROLS_FOR,
            BlocklyConstants.ROB_CONTROLS_FOR,
            BlocklyConstants.CONTROLS_REPEAT_EXT,
            BlocklyConstants.CONTROLS_REPEAT,
            BlocklyConstants.CONTROLS_FOR_EACH,
            BlocklyConstants.ROB_CONTROLS_FOR_EACH);
        add("EXPR_STMT", Category.STMT);
        add("STMT_LIST", Category.STMT);
        add("ASSIGN_STMT", Category.STMT, AssignStmt.class, BlocklyConstants.VARIABLE_SET);
        add("AKTION_STMT", Category.STMT);
        add("SENSOR_STMT", Category.STMT);
        add("FUNCTION_STMT", Category.STMT);
        add("METHOD_STMT", Category.STMT);
        add("STMT_FLOW_CONTROL", Category.STMT, StmtFlowCon.class, BlocklyConstants.CONTROLS_FLOW_STATEMENT);
        add("WAIT_STMT", Category.STMT, WaitStmt.class, BlocklyConstants.ROB_CONTROLS_WAIT_FOR, BlocklyConstants.ROB_CONTROLS_WAIT);
        add("WAIT_TIME", Category.STMT, WaitTimeStmt.class, BlocklyConstants.ROB_CONTROLS_WAIT_TIME);
        add("TURN_ACTION", Category.ACTOR, TurnAction.class, BlocklyConstants.ROB_ACTIONS_MOTOR_DIFF_TURN, BlocklyConstants.ROB_ACTIONS_MOTOR_DIFF_TURN_FOR);
        add("DRIVE_ACTION", Category.ACTOR, DriveAction.class, BlocklyConstants.ROB_ACTIONS_MOTOR_DIFF_ON, BlocklyConstants.ROB_ACTIONS_MOTOR_DIFF_ON_FOR);
        add(
            "CURVE_ACTION",
            Category.ACTOR,
            CurveAction.class,
            BlocklyConstants.ROB_ACTIONS_MOTOR_DIFF_CURVE,
            BlocklyConstants.ROB_ACTIONS_MOTOR_DIFF_CURVE_FOR);
        add("SHOW_TEXT_ACTION", Category.ACTOR, ShowTextAction.class, BlocklyConstants.ROB_ACTIONS_DISPLAY_TEXT);
        add("SHOW_PICTURE_ACTION", Category.ACTOR, ShowPictureAction.class, BlocklyConstants.ROB_ACTIONS_DISPLAY_PICTURE);
        add("TONE_ACTION", Category.ACTOR, ToneAction.class, BlocklyConstants.ROB_ACTIONS_PLAY_TONE);
        add(
            "LIGHT_SENSOR_ACTION",
            Category.ACTOR,
            LightSensorAction.class,
            BlocklyConstants.ROB_ACTIONS_LIGHT_SENSOR_LIGHT_ON,
            BlocklyConstants.ROB_ACTIONS_LIGHT_SENSOR_LIGHT_OFF);
        add("LIGHT_ACTION", Category.ACTOR, LightAction.class, BlocklyConstants.ROB_ACTIONS_BRICK_LIGHT_ON, BlocklyConstants.SIM_LED_ON);
        add("CLEAR_DISPLAY_ACTION", Category.ACTOR, ClearDisplayAction.class, BlocklyConstants.ROB_ACTIONS_DISPLAY_CLEAR);
        add(
            "MOTOR_ON_ACTION",
            Category.ACTOR,
            MotorOnAction.class,
            BlocklyConstants.ROB_ACTIONS_MOTOR_ON,
            BlocklyConstants.ROB_ACTIONS_MOTOR_ON_FOR,
            BlocklyConstants.SIM_MOTOR_ON,
            BlocklyConstants.SIM_MOTOR_ON_FOR);
        add("MOTOR_GET_POWER_ACTION", Category.ACTOR, MotorGetPowerAction.class, BlocklyConstants.ROB_ACTIONS_MOTOR_GET_POWER);
        add("MOTOR_SET_POWER_ACTION", Category.ACTOR, MotorSetPowerAction.class, BlocklyConstants.ROB_ACTIONS_MOTOR_SET_POWER);
        add("MOTOR_STOP_ACTION", Category.ACTOR, MotorStopAction.class, BlocklyConstants.ROB_ACTIONS_MOTOR_STOP, BlocklyConstants.SIM_MOTOR_STOP);
        add("PLAY_FILE_ACTION", Category.ACTOR, PlayFileAction.class, BlocklyConstants.ROB_ACTIONS_PLAY_FILE);
        add("VOLUME_ACTION", Category.ACTOR, VolumeAction.class, BlocklyConstants.ROB_ACTIONS_PLAY_SET_VOLUME, BlocklyConstants.ROB_ACTIONS_PLAY_GET_VOLUME);
        add(
            "LIGHT_STATUS_ACTION",
            Category.ACTOR,
            LightStatusAction.class,
            BlocklyConstants.ROB_ACTIONS_BRICK_LIGHT_OFF,
            BlocklyConstants.ROB_ACTIONS_BRICK_LIGHT_RESET,
            BlocklyConstants.SIM_LED_OFF);
        add("STOP_ACTION", Category.ACTOR, MotorDriveStopAction.class, BlocklyConstants.ROB_ACTIONS_MOTOR_DIFF_STOP);
        add("MAIN_TASK", Category.TASK, MainTask.class, BlocklyConstants.ROB_CONTROLS_START, BlocklyConstants.ROB_CONTROLS_START_ARDU);
        add("ACTIVITY_TASK", Category.TASK, ActivityTask.class, BlocklyConstants.ROB_CONTROLS_ACTIVITY);
        add("LOCATION", Category.TASK);
        add("TEXT_INDEX_OF_FUNCT", Category.FUNCTION, IndexOfFunct.class, BlocklyConstants.LISTS_INDEX_OF, BlocklyConstants.ROB_LISTS_INDEX_OF);
        add("TEXT_CHAR_AT_FUNCT", Category.FUNCTION);
        add("GET_SUB_FUNCT", Category.FUNCTION, GetSubFunct.class, BlocklyConstants.LISTS_GET_SUBLIST, BlocklyConstants.ROB_LISTS_GET_SUBLIST);
        add(
            "MATH_SINGLE_FUNCT",
            Category.FUNCTION,
            MathSingleFunct.class,
            BlocklyConstants.MATH_ROUND,
            BlocklyConstants.MATH_TRIG,
            BlocklyConstants.MATH_SINGLE);
        add("MATH_ON_LIST_FUNCT", Category.FUNCTION, MathOnListFunct.class, BlocklyConstants.MATH_ON_LIST);
        add("MATH_CONSTRAIN_FUNCT", Category.FUNCTION, MathConstrainFunct.class, BlocklyConstants.MATH_ON_CONSTRAINT);
        add("MATH_RANDOM_INT_FUNCT", Category.FUNCTION, MathRandomIntFunct.class, BlocklyConstants.MATH_RANDOM_INT);
        add("MATH_RANDOM_FLOAT_FUNCT", Category.FUNCTION, MathRandomFloatFunct.class, BlocklyConstants.MATH_RANDOM_FLOAT);
        add("MATH_NUM_PROP_FUNCT", Category.FUNCTION, MathNumPropFunct.class, BlocklyConstants.MATH_NUMBER_PROPERTY);
        add(
            "LENGTH_OF_IS_EMPTY_FUNCT",
            Category.FUNCTION,
            LengthOfIsEmptyFunct.class,
            BlocklyConstants.LISTS_LENGTH,
            BlocklyConstants.ROB_LISTS_LENGTH,
            BlocklyConstants.LISTS_IS_EMPTY,
            BlocklyConstants.ROB_LISTS_IS_EMPTY);
        add("TEXT_JOIN_FUNCT", Category.FUNCTION, TextJoinFunct.class, BlocklyConstants.ROB_TEXT_JOIN, BlocklyConstants.TEXT_JOIN);
        add("TEXT_TRIM_FUNCT", Category.FUNCTION);
        add("TEXT_PRINT_FUNCT", Category.FUNCTION, TextPrintFunct.class, BlocklyConstants.TEXT_PRINT);
        add("TEXT_PROMPT_FUNCT", Category.FUNCTION);
        add("LIST_REPEAT_FUNCT", Category.FUNCTION, ListRepeat.class, BlocklyConstants.LISTS_REPEAT, BlocklyConstants.ROB_LISTS_REPEAT);
        add("LIST_CREATE", Category.EXPR, ListCreate.class, BlocklyConstants.LISTS_CREATE_WITH, BlocklyConstants.ROB_LISTS_CREATE_WITH);
        add("LIST_INDEX_OF", Category.FUNCTION, ListGetIndex.class, BlocklyConstants.LISTS_GET_INDEX, BlocklyConstants.ROB_LISTS_GET_INDEX);
        add("LIST_SET_INDEX", Category.FUNCTION, ListSetIndex.class, BlocklyConstants.LISTS_SET_INDEX, BlocklyConstants.ROB_LISTS_SET_INDEX);
        add("TEXT_CHANGE_CASE_FUNCT", Category.FUNCTION);
        add("METHOD_IF_RETURN", Category.METHOD, MethodIfReturn.class, BlocklyConstants.ROB_PROCEDURES_IF_RETURN);
        add("METHOD_VOID", Category.METHOD, MethodVoid.class, BlocklyConstants.ROB_PROCEDURES_NO_RETURN);
        add("METHOD_CALL", Category.METHOD, MethodCall.class, BlocklyConstants.ROB_PROCEDURES_CALL_NO_RETURN, BlocklyConstants.ROB_PROCEDURES_CALL_RETURN);
        add("METHOD_RETURN", Category.METHOD, MethodReturn.class, BlocklyConstants.ROB_PROCEDURES_RETURN);
        add("BLUETOOTH_CHECK_CONNECT_ACTION", Category.ACTOR, BluetoothCheckConnectAction.class, BlocklyConstants.COM_CHECK_CONNECTION);
        add("BLUETOOTH_CONNECT_ACTION", Category.ACTOR, BluetoothConnectAction.class, BlocklyConstants.COM_START_CONNECTION);
        add("BLUETOOTH_SEND_ACTION", Category.ACTOR, BluetoothSendAction.class, BlocklyConstants.COM_SEND_BLOCK);
        add("BLUETOOTH_WAIT_FOR_CONNECTION_ACTION", Category.ACTOR, BluetoothWaitForConnectionAction.class, BlocklyConstants.COM_WAIT_CONNECTION);
        add("BLUETOOTH_RECEIVED_ACTION", Category.ACTOR, BluetoothReceiveAction.class, BlocklyConstants.COM_RECEIVE_BLOCK);
    }

    public static void add(String name, Category category, Class<?> astClass, String... blocklyNames) {
        BlockType blockType = new BlockType(name, category, astClass, blocklyNames);
        BlockType oldValue = blockTypesByName.put(name.toLowerCase(), blockType);
        Assert.isNull(oldValue, "Block name %s is mapped twice. Initialization aborted", name);
        for ( String blocklyName : blocklyNames ) {
            oldValue = blockTypesByBlocklyName.put(blocklyName.toLowerCase(), blockType);
            Assert.isNull(oldValue, "Blockly name %s is mapped twice. Initialization aborted", blocklyName);
        }
    }

    private static void add(String name, Category category) {
        add(name, category, null, NO_BLOCKLY_NAMES);
    }

    /**
     * access block type values by its unique name
     *
     * @param name the unique name of the block type, never null
     * @return the block type value, never null
     */
    public static BlockType getByName(String name) {
        Assert.notNull(name);
        BlockType blockType = blockTypesByName.get(name.toLowerCase());
        Assert.notNull(blockType);
        return blockType;
    }

    /**
     * access block type values by its unique blockly name
     *
     * @param blocklyName the unique blockly name of the block type, never null
     * @return the block type value, never null
     */
    public static BlockType getByBlocklyName(String blocklyName) {
        BlockType blockType = blockTypesByBlocklyName.get(blocklyName.toLowerCase());
        Assert.notNull(blockType, "blockly name is not found: " + blocklyName);
        return blockType;
    }

    /**
     * Registers a property file to avoid loading property files more than once.
     * Properties are not loaded by calling this method use the {@link #add(String, Category, Class, String...)}
     *
     * @param propertyFileName
     * @return true if the property file was already loaded or false otherwise
     */
    public static boolean register(String propertyFileName) {
        if ( loadedPropertyFiles.contains(propertyFileName) ) {
            return true;
        } else {
            loadedPropertyFiles.add(propertyFileName);
            return false;
        }
    }

    public static class BlockType {
        private final String name;
        private final Category category;
        private final Class<?> astClass;
        private final String[] blocklyNames;

        private BlockType(String name, Category category, Class<?> astClass, String... blocklyNames) {
            this.name = name;
            this.category = category;
            this.astClass = astClass;
            this.blocklyNames = blocklyNames;
        }

        /**
         * @return the unique name in which {@link BlockType} belongs.
         */
        public String getName() {
            return this.name;
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

        /**
         * check whether this block type has the name as expected
         *
         * @param nameToCheck
         * @return true, if the block type has the name expected; false otherwise
         */
        public boolean hasName(String... namesToCheck) {
            for ( String nameToCheck : namesToCheck ) {
                boolean found = this.name.equals(nameToCheck);
                if ( found ) {
                    return true;
                }
            }
            return false;
        }

    }
}
