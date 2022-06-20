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

@NepoBasic(sampleValues = {@NepoSampleValue(blocklyFieldName="ENVIRONMENTAL_TEMPERATURE",sensor="ENVIRONMENTAL",mode="TEMPERATURE"),@NepoSampleValue(blocklyFieldName="ENVIRONMENTAL_VOCEQUIVALENT",sensor="ENVIRONMENTAL",mode="VOCEQUIVALENT"),@NepoSampleValue(blocklyFieldName="ENVIRONMENTAL_CALIBRATION",sensor="ENVIRONMENTAL",mode="CALIBRATION"),@NepoSampleValue(blocklyFieldName="ENVIRONMENTAL_IAQ",sensor="ENVIRONMENTAL",mode="IAQ"),@NepoSampleValue(blocklyFieldName="ENVIRONMENTAL_PRESSURE",sensor="ENVIRONMENTAL",mode="PRESSURE"),@NepoSampleValue(blocklyFieldName="ENVIRONMENTAL_HUMIDITY",sensor="ENVIRONMENTAL",mode="HUMIDITY"),@NepoSampleValue(blocklyFieldName="ENVIRONMENTAL_CO2EQUIVALENT",sensor="ENVIRONMENTAL",mode="CO2EQUIVALENT")}, containerType = "ENVIRONMENTAL", category = "SENSOR", blocklyNames = {"robSensors_environmental_getSample"})
public final class EnvironmentalSensor<V> extends ExternalSensor<V> {

    private EnvironmentalSensor(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }

    public static <V> EnvironmentalSensor<V> make(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new EnvironmentalSensor<>(sensorMetaDataBean, properties, comment);
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
        return EnvironmentalSensor.make(sensorData, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }
}
