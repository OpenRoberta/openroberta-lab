package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.SensorMetaDataBean;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.sensor.ISensorVisitor;

/**
 * This class represents the <b>robSensors_infrared_getMode</b>, <b>robSensors_infrared_getSample</b> and <b>robSensors_infrared_setMode</b> blocks from Blockly
 * into the AST (abstract syntax tree). Object from this class will generate code for setting the mode of the sensor or getting a sample from the sensor.<br/>
 * <br>
 * The client must provide the {@link SensorPort} and {@link InfraredSensorMode}. See enum {@link InfraredSensorMode} for all possible modes of the sensor.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(InfraredSensorMode, SensorPort, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class InfraredSensor<V> extends ExternalSensor<V> {

    private InfraredSensor(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(sensorMetaDataBean, BlockTypeContainer.getByName("INFRARED_SENSING"), properties, comment);
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

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((ISensorVisitor<V>) visitor).visitInfraredSensor(this);
    }
}
