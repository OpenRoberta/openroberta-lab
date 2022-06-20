package de.fhg.iais.roberta.syntax.sensors.arduino.sensebox;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.transformer.forClass.NepoSampleValue;
import de.fhg.iais.roberta.util.ast.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.ast.BlocklyComment;
import de.fhg.iais.roberta.util.ast.SensorMetaDataBean;

/**
 * This class represents the <b>robSensors_touch_isPressed</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will generate
 * code for checking if the sensor is pressed.<br/>
 * <br>
 * The client must provide the {@link SensorPort}.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(SensorPort, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
@NepoBasic(sampleValues = {@NepoSampleValue(blocklyFieldName="GPS_LATITUDE",sensor="GPS",mode="LATITUDE"),@NepoSampleValue(blocklyFieldName="GPS_SPEED",sensor="GPS",mode="SPEED"),@NepoSampleValue(blocklyFieldName="GPS_DATE",sensor="GPS",mode="DATE"),@NepoSampleValue(blocklyFieldName="GPS_TIME",sensor="GPS",mode="TIME"),@NepoSampleValue(blocklyFieldName="GPS_ALTITUDE",sensor="GPS",mode="ALTITUDE"),@NepoSampleValue(blocklyFieldName="GPS_LONGITUDE",sensor="GPS",mode="LONGITUDE")}, containerType = "GPS", category = "SENSOR", blocklyNames = {"robSensors_gps_getSample"})
public final class GpsSensor<V> extends ExternalSensor<V> {

    private GpsSensor(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }

    @Override
    public String toString() {
        return "GpsSensor []";
    }

    public static <V> GpsSensor<V> make(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new GpsSensor<>(sensorMetaDataBean, properties, comment);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        SensorMetaDataBean sensorData = extractPortAndModeAndSlot(block, helper);
        return GpsSensor.make(sensorData, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }
}
