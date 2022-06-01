package de.fhg.iais.roberta.syntax.sensor.nao;

import org.checkerframework.checker.units.qual.degrees;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.util.syntax.SensorMetaDataBean;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;

/**
 * This class represents the <b>naoActions_walk</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for making
 * the robot walk for a distance.<br/>
 * <br/>
 * The client must provide the {@link joint} and {@link degrees} (direction and distance to walk).
 */
public final class ElectricCurrentSensor<V> extends ExternalSensor<V> {

    private ElectricCurrentSensor(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(sensorMetaDataBean, BlockTypeContainer.getByName("ELECTRIC_CURRENT"), properties, comment);
        setReadOnly();
    }

    /**
     * Creates instance of {@link ElectricCurrentSensor}. This instance is read only and can not be modified.
     *
     * @param joint {@link joint} the sensors data will be read from,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link ElectricCurrentSensor}
     */
    public static <V> ElectricCurrentSensor<V> make(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new ElectricCurrentSensor<V>(sensorMetaDataBean, properties, comment);
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
        return ElectricCurrentSensor.make(sensorData, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

}
