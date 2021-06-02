package de.fhg.iais.roberta.syntax.sensors.arduino.sensebox;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.SensorMetaDataBean;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;

/**
 * This class represents the <b>robSensors_touch_isPressed</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will generate
 * code for checking if the sensor is pressed.<br/>
 * <br>
 * The client must provide the {@link SensorPort}.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(SensorPort, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class GpsSensor<V> extends ExternalSensor<V> {

    private GpsSensor(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(sensorMetaDataBean, BlockTypeContainer.getByName("GPS"), properties, comment);
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
