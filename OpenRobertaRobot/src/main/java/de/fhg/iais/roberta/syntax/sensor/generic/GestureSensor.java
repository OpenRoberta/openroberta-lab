package de.fhg.iais.roberta.syntax.sensor.generic;

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

@NepoBasic(sampleValues = {@NepoSampleValue(blocklyFieldName = "GESTURE_UP", sensor = "GESTURE", mode = "UP"), @NepoSampleValue(blocklyFieldName = "GESTURE_DOWN", sensor = "GESTURE", mode = "DOWN"), @NepoSampleValue(blocklyFieldName = "GESTURE_FACE_DOWN", sensor = "GESTURE", mode = "FACE_DOWN"), @NepoSampleValue(blocklyFieldName = "GESTURE_FREEFALL", sensor = "GESTURE", mode = "FREEFALL"), @NepoSampleValue(blocklyFieldName = "GESTURE_ACTIVE", sensor = "GESTURE", mode = "GESTURE_ACTIVE"), @NepoSampleValue(blocklyFieldName = "GESTURE_SHAKE", sensor = "GESTURE", mode = "SHAKE"), @NepoSampleValue(blocklyFieldName = "GESTURE_FACE_UP", sensor = "GESTURE", mode = "FACE_UP")}, containerType = "GESTURE_SENSING", category = "SENSOR", blocklyNames = {"robSensors_gesture_getSample"})
public final class GestureSensor<V> extends ExternalSensor<V> {

    private GestureSensor(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(properties, comment, sensorMetaDataBean);
        setReadOnly();
    }

    /**
     * Creates instance of {@link GestureSensor}. This instance is read only and can not be modified.
     *
     * @return read only object of class {@link GestureSensor}
     */
    public static <V> GestureSensor<V> make(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new GestureSensor<>(sensorMetaDataBean, properties, comment);
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
        return GestureSensor.make(sensorData, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    Phrase<V> getSensor() {
        // TODO Auto-generated method stub
        return null;
    }
}
