package de.fhg.iais.roberta.syntax.sensor.generic;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.SensorMetaDataBean;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;

public class ParticleSensor<V> extends ExternalSensor<V> {

    public ParticleSensor(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(sensorMetaDataBean, BlockTypeContainer.getByName("PARTICLE_SENSOR_GETSAMPLE"), properties, comment);
        setReadOnly();
    }

    public static <V> ParticleSensor<V> make(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ParticleSensor<>(sensorMetaDataBean, properties, comment);
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
        return ParticleSensor.make(sensorData, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }
}
