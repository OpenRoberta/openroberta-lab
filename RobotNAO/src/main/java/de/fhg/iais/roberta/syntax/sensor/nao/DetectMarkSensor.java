package de.fhg.iais.roberta.syntax.sensor.nao;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.MotionParam;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.util.syntax.SensorMetaDataBean;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;

/**
 * This class represents the <b>naoSensors_naoMark</b> blocks from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * detecting a NaoMark.<br/>
 * <br/>
 */
public final class DetectMarkSensor<V> extends ExternalSensor<V> {

    private DetectMarkSensor(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(sensorMetaDataBean, BlockTypeContainer.getByName("DETECT_MARK"), properties, comment);
        setReadOnly();
    }

    /**
     * Creates instance of {@link DetectMarkSensor}. This instance is read only and can not be modified.
     *
     * @param param {@link MotionParam} that set up the parameters for the movement of the robot (number of rotations or degrees and speed),
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link DetectMarkSensor}
     */
    public static <V> DetectMarkSensor<V> make(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new DetectMarkSensor<V>(sensorMetaDataBean, properties, comment);
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
        return DetectMarkSensor.make(sensorData, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }
}
