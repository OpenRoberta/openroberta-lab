package de.fhg.iais.roberta.factory;

import java.lang.reflect.Method;
import java.util.Locale;
import java.util.Map;

import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.inter.mode.action.*;
import de.fhg.iais.roberta.mode.action.*;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.bean.WaitUntilSensorBean;
import de.fhg.iais.roberta.inter.mode.general.IDirection;
import de.fhg.iais.roberta.inter.mode.general.IIndexLocation;
import de.fhg.iais.roberta.inter.mode.general.IListElementOperations;
import de.fhg.iais.roberta.inter.mode.general.IWorkingState;
import de.fhg.iais.roberta.mode.general.Direction;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.general.ListElementOperations;
import de.fhg.iais.roberta.mode.general.WorkingState;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.syntax.sensor.SensorMetaDataBean;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class BlocklyDropdownFactory {
    private static final Logger LOG = LoggerFactory.getLogger(BlocklyDropdownFactory.class);
    private final JSONObject robotDescription;

    private final Map<String, WaitUntilSensorBean> waMap;
    private final Map<String, String> modes;
    private final Map<String, String> configurationComponentTypes;

    public BlocklyDropdownFactory(PluginProperties pluginProperties) {
        String robotDescriptor = pluginProperties.getStringProperty("robot.descriptor");
        this.robotDescription = new JSONObject();
        Util.loadYAMLRecursive("", this.robotDescription, robotDescriptor, false);
        BlocklyDropdownFactoryHelper.loadBlocks(this.robotDescription);
        this.waMap = BlocklyDropdownFactoryHelper.getWaitUntils(this.robotDescription);
        this.modes = BlocklyDropdownFactoryHelper.getModes(this.robotDescription);
        this.configurationComponentTypes = BlocklyDropdownFactoryHelper.getConfigurationComponents(this.robotDescription);

    }

    public String getConfigurationComponentTypeByBlocklyName(String blocklyName) {
        Assert.nonEmptyString(blocklyName, "Invalid blockly name");
        String configurationComponentType = this.configurationComponentTypes.get(blocklyName);
        Assert.notNull(configurationComponentType, "No associated component type for %s in the properties", blocklyName);
        return configurationComponentType;
    }

    public String getMode(String mode) {
        Assert.nonEmptyString(mode, "Invalid mode");

        final String sUpper = mode.trim().toUpperCase(Locale.GERMAN);
        String internalMode = this.modes.get(sUpper);
        Assert.notNull(internalMode, "Undefined mode %s", mode);
        return internalMode;
    }

    /**
     * Get index location enumeration from {@link IIndexLocation} given string parameter. It is possible for one index location to have multiple string
     * mappings. Throws exception if the operator does not exists.
     *
     * @param indexLocation of the function
     * @return index location from the enum {@link IIndexLocation}
     */
    public IIndexLocation getIndexLocation(String indexLocation) {
        return BlocklyDropdownFactoryHelper.getModeValue(indexLocation, IndexLocation.class);
    }

    /**
     * Direction in space enumeration from {@link IDirection} given string parameter. It is possible for one direction to have multiple string mappings. Throws
     * exception if the operator does not exists.
     *
     * @param direction of the function
     * @return direction location from the enum {@link IDirection}
     */
    public IDirection getDirection(String direction) {
        return BlocklyDropdownFactoryHelper.getModeValue(direction, Direction.class);
    }

    /**
     * Get array element operation enumeration from {@link IListElementOperations} given string parameter. It is possible for one operation to have multiple
     * string mappings. Throws exception if the operator does not exists.
     *
     * @param operation string name
     * @return operation from the enum {@link IListElementOperations}
     */
    public IListElementOperations getListElementOpertaion(String operation) {
        return BlocklyDropdownFactoryHelper.getModeValue(operation, ListElementOperations.class);
    }

    /**
     * Get a {@link ILightMode} enumeration given string parameter. It is possible for one mode to have multiple string mappings. Throws exception if the mode
     * does not exists.
     *
     * @param name of the mode
     * @return mode from the enum {@link LightMode}
     */
    public ILightMode getBlinkMode(String mode) {
        return BlocklyDropdownFactoryHelper.getModeValue(mode, LightMode.class);
    }

    /**
     * Get a {@link de.fhg.iais.roberta.inter.mode.action.IBuzzerMode} enumeration given string parameter. It is possible for one mode to have multiple string mappings. Throws exception if the mode
     * does not exists.
     *
     * @param mode of the mode
     * @return mode from the enum {@link BuzzerMode}
     */
    public IBuzzerMode getBuzzerMode(String mode) {
        return BlocklyDropdownFactoryHelper.getModeValue(mode, BuzzerMode.class);
    }

    /**
     * Get a {@link IBrickLedColor} enumeration given string parameter. It is possible for one mode to have multiple string mappings. Throws exception if the
     * mode does not exists.
     *
     * @param name of the mode
     * @return mode from the enum {@link ILightMode}
     */
    public IBrickLedColor getBrickLedColor(String mode) {
        return BlocklyDropdownFactoryHelper.getModeValue(mode, BrickLedColor.class);
    }

    public IWorkingState getWorkingState(String mode) {
        return BlocklyDropdownFactoryHelper.getModeValue(mode, WorkingState.class);
    }

    /**
     * Get a {@link ITurnDirection} enumeration given string parameter. It is possible for one turn direction to have multiple string mappings. Throws exception
     * if the mode does not exists.
     *
     * @param name of the turn direction
     * @return turn direction from the enum {@link ITurnDirection}
     */
    public ITurnDirection getTurnDirection(String direction) {
        return BlocklyDropdownFactoryHelper.getModeValue(direction, TurnDirection.class);
    }

    /**
     * Get a {@link IMotorMoveMode} enumeration given string parameter. It is possible for one motor move mode to have multiple string mappings. Throws
     * exception if the mode does not exists.
     *
     * @param name of the motor move mode
     * @return motor move mode from the enum {@link IMotorMoveMode}
     */
    public IMotorMoveMode getMotorMoveMode(String mode) {
        return BlocklyDropdownFactoryHelper.getModeValue(mode, MotorMoveMode.class);
    }

    /**
     * Get stopping mode from {@link IMotorStopMode} from string parameter. It is possible for one stopping mode to have multiple string mappings. Throws
     * exception if the stopping mode does not exists.
     *
     * @param name of the stopping mode
     * @return name of the stopping mode from the enum {@link IMotorStopMode}
     */
    public IMotorStopMode getMotorStopMode(String mode) {
        return BlocklyDropdownFactoryHelper.getModeValue(mode, MotorStopMode.class);
    }

    /**
     * Get motor side from {@link IMotorSide} given string parameter. It is possible for one motor side to have multiple string mappings. Throws exception if
     * the motor side does not exists.
     *
     * @param name of the motor side
     * @return the motor side from the enum {@link IMotorSide}
     */
    public IMotorSide getMotorSide(String motorSide) {
        return BlocklyDropdownFactoryHelper.getModeValue(motorSide, MotorSide.class);
    }
    /**
     * Get motor side from {@link IMotorSide} given string parameter. It is possible for one motor side to have multiple string mappings. Throws exception if
     * the motor side does not exists.
     *
     * @param name of the motor side
     * @return the motor side from the enum {@link IMotorSide}
     */
    public IMotorServoMode getMotorServoMode(String mode) {
        return BlocklyDropdownFactoryHelper.getModeValue(mode, MotorServoMode.class);
    }

    /**
     * Get drive direction from {@link IDriveDirection} given string parameter. It is possible for one drive direction to have multiple string mappings. Throws
     * exception if the motor side does not exists.
     *
     * @param name of the drive direction
     * @return the drive direction from the enum {@link IDriveDirection}
     */
    public IDriveDirection getDriveDirection(String driveDirection) {
        return BlocklyDropdownFactoryHelper.getModeValue(driveDirection, DriveDirection.class);
    }

    /**
     * Get relay mode {@link IRelayMode} given string parameter. Throws exception if the mode does not exists.
     *
     * @param name of the mode
     * @return the drelay mode from the enum {@link IRelayMode}
     */
    public IRelayMode getRelayMode(String relayMode) {
        return BlocklyDropdownFactoryHelper.getModeValue(relayMode, RelayMode.class);
    }

    public ILanguage getLanguageMode(String mode) {
        return BlocklyDropdownFactoryHelper.getModeValue(mode, Language.class);
    }

    /**
     * Creates an AST object representing sensor of specific type. If the type of the sensor does not exists it trows an exception. @param sensorKey see
     * {@link GetSampleType} @param port on which the sensor is connected @param slot on which the sensor is connected @param properties of the block @param
     * comment of the block @return returns instance of the specific sensor {@link Sensor} @throws
     */
    @SuppressWarnings("unchecked")
    public Sensor<?> createSensor(
        String sensorKey,
        String port,
        String slot,
        Mutation mutation,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {

        WaitUntilSensorBean waBean = this.waMap.get(sensorKey);
        String implementingClass = waBean.getImplementingClass();

        try {
            Class<Sensor<?>> sensorClass = (Class<Sensor<?>>) BlocklyDropdownFactory.class.getClassLoader().loadClass(implementingClass);
            String mode = waBean.getMode();
            SensorMetaDataBean sensorMetaDataBean = new SensorMetaDataBean(Jaxb2Ast.sanitizePort(port), getMode(mode), Jaxb2Ast.sanitizeSlot(slot), mutation);
            Method makeMethod = sensorClass.getDeclaredMethod("make", SensorMetaDataBean.class, BlocklyBlockProperties.class, BlocklyComment.class);
            return (Sensor<?>) makeMethod.invoke(null, sensorMetaDataBean, properties, comment);
        } catch ( Exception e ) {
            LOG.error("Sensor " + sensorKey + " could not be created", e);
            throw new DbcException("Sensor " + sensorKey + " could not be created", e);
        }

    }

}
