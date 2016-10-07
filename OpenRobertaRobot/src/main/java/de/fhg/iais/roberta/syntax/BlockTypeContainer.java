package de.fhg.iais.roberta.syntax;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.fhg.iais.roberta.components.Category;
<<<<<<< 8474e2a1d929ed4aca7751163134a918afe53491
=======
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
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.syntax.stmt.AssignStmt;
import de.fhg.iais.roberta.syntax.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.stmt.WaitTimeStmt;
>>>>>>> #196 implemented block 'get voltage'
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
    private static final String[] NO_BLOCKLY_NAMES = new String[0];

    private static final List<String> loadedPropertyFiles = new ArrayList<>();

    private static final Map<String, BlockType> blockTypesByName = new HashMap<>();
    private static final Map<String, BlockType> blockTypesByBlocklyName = new HashMap<>();

    static {
<<<<<<< 8474e2a1d929ed4aca7751163134a918afe53491
=======
        add("COLOR_SENSING", Category.SENSOR, ColorSensor.class, BlocklyConstants.ROB_SENSOR_COLOUR_GET_SAMPLE, BlocklyConstants.SIM_COLOUR_GET_SAMPLE);
        add("LIGHT_SENSING", Category.SENSOR, LightSensor.class, BlocklyConstants.ROB_SENSOR_LIGHT_GET_SAMPLE, BlocklyConstants.SIM_LIGHT_GET_SAMPLE);
        add("SOUND_SENSING", Category.SENSOR, SoundSensor.class, BlocklyConstants.ROB_SENSOR_SOUND_GET_SAMPLE, BlocklyConstants.SIM_SOUND_GET_SAMPLE);
        add("TOUCH_SENSING", Category.SENSOR, TouchSensor.class, BlocklyConstants.ROB_SENSOR_TOUCH_IS_PRESSED, BlocklyConstants.SIM_TOUCH_IS_PRESSED);
        add("COMPASS_SENSING", Category.SENSOR, CompassSensor.class, BlocklyConstants.ROB_SENSOR_COMPASS_GET_SAMPLE);
        add("VOLTAGE_SENSING", Category.SENSOR, VoltageSensor.class, BlocklyConstants.ROB_SENSOR_VOLTAGE_GET_SAMPLE);
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
>>>>>>> #196 implemented block 'get voltage'
        add("EXPR_LIST", Category.EXPR);
        add("SENSOR_EXPR", Category.EXPR);
        add("ACTION_EXPR", Category.EXPR);
        add("EMPTY_EXPR", Category.EXPR);
        add("SHADOW_EXPR", Category.EXPR);
        add("FUNCTION_EXPR", Category.EXPR);
        add("METHOD_EXPR", Category.EXPR);
        add("FUNCTIONS", Category.EXPR);
        add("EXPR_STMT", Category.STMT);
        add("STMT_LIST", Category.STMT);
        add("AKTION_STMT", Category.STMT);
        add("SENSOR_STMT", Category.STMT);
        add("FUNCTION_STMT", Category.STMT);
        add("METHOD_STMT", Category.STMT);
        add("LOCATION", Category.TASK);
        add("TEXT_CHAR_AT_FUNCT", Category.FUNCTION);
        add("TEXT_TRIM_FUNCT", Category.FUNCTION);
        add("TEXT_PROMPT_FUNCT", Category.FUNCTION);
        add("TEXT_CHANGE_CASE_FUNCT", Category.FUNCTION);
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