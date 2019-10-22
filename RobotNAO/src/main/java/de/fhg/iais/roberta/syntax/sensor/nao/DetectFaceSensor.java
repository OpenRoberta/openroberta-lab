package de.fhg.iais.roberta.syntax.sensor.nao;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.SensorMetaDataBean;
import de.fhg.iais.roberta.transformer.AbstractJaxb2Ast;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.INaoVisitor;

/**
 * This class represents the <b>naoSensors_detectFace</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * detecting a face previously saved in NAOs database.<br/>
 * <br/>
 */
public final class DetectFaceSensor<V> extends ExternalSensor<V> {

    private DetectFaceSensor(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(sensorMetaDataBean, BlockTypeContainer.getByName("DETECT_FACE"), properties, comment);
        setReadOnly();
    }

    /**
     * Creates instance of {@link DETECTFACE}. This instance is read only and can not be modified.
     *
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     */
    public static <V> DetectFaceSensor<V> make(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new DetectFaceSensor<V>(sensorMetaDataBean, properties, comment);
    }

    @Override
    protected V acceptImpl(IVisitor<V> visitor) {
        return ((INaoVisitor<V>) visitor).visitDetectFace(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, AbstractJaxb2Ast<V> helper) {
        SensorMetaDataBean sensorData = extractPortAndModeAndSlot(block, helper);
        return DetectFaceSensor.make(sensorData, helper.extractBlockProperties(block), helper.extractComment(block));
    }
}
