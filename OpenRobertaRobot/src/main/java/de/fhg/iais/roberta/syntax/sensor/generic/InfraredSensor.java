package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.transformer.forClass.NepoSampleValue;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;

/**
 * This class represents the <b>robSensors_infrared_getMode</b>, <b>robSensors_infrared_getSample</b> and <b>robSensors_infrared_setMode</b> blocks from Blockly
 * into the AST (abstract syntax tree). Object from this class will generate code for setting the mode of the sensor or getting a sample from the sensor.<br/>
 * <br>
 * The client must provide the {@link SensorPort} and {@link InfraredSensorMode}. See enum {@link InfraredSensorMode} for all possible modes of the sensor.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(InfraredSensorMode, SensorPort, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
@NepoBasic(sampleValues = {@NepoSampleValue(blocklyFieldName = "INFRARED_LINE", sensor = "INFRARED", mode = "LINE"), @NepoSampleValue(blocklyFieldName = "INFRARED_REFLEXION", sensor = "INFRARED", mode = "REFLEXION"), @NepoSampleValue(blocklyFieldName = "INFRARED_SEEK", sensor = "INFRARED", mode = "SEEK"), @NepoSampleValue(blocklyFieldName = "INFRARED_DISTANCE", sensor = "INFRARED", mode = "DISTANCE"), @NepoSampleValue(blocklyFieldName = "INFRARED_VALUE", sensor = "INFRARED", mode = "VALUE"), @NepoSampleValue(blocklyFieldName = "INFRARED_OBSTACLE", sensor = "INFRARED", mode = "OBSTACLE"), @NepoSampleValue(blocklyFieldName = "INFRARED_PRESENCE", sensor = "INFRARED", mode = "PRESENCE")}, containerType = "INFRARED_SENSING", category = "SENSOR", blocklyNames = {"robSensors_infrared_getSample"})
public final class InfraredSensor<V> extends ExternalSensor<V> {

    private InfraredSensor(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }

    /**
     * Create object of the class {@link InfraredSensor}.
     *
     * @param mode in which the sensor is operating; must be <b>not</b> null; see enum {@link InfraredSensorMode} for all possible modes that the sensor have,
     * @param port on where the sensor is connected; must be <b>not</b> null; see enum {@link SensorPort} for all possible sensor ports,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link InfraredSensor}
     */
    public static <V> InfraredSensor<V> make(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new InfraredSensor<V>(sensorMetaDataBean, properties, comment);
    }

}
