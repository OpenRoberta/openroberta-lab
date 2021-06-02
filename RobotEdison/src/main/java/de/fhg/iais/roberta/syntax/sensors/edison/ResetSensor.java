package de.fhg.iais.roberta.syntax.sensors.edison;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the "edisonSensors_sensor_reset" block which is used to reset the sensors of the Edison robot. This is needed bcause some sensors
 * always listen for new data and write it. This calls the sensor read() method and just doesn't save the value into a variable. This block should be used
 * before every loop.
 *
 * @param <V>
 */
public class ResetSensor<V> extends Sensor<V> {

    private final String sensor;

    /**
     * This constructor set the kind of the sensor object used in the AST (abstract syntax tree). All possible kinds can be found in {@link BlockType}.
     *
     * @param kind of the the sensor object used in AST,
     * @param sensor the sensor to reset
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment of the user for the specific block
     */
    public ResetSensor(String sensor, BlocklyBlockProperties props, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("SENSOR_RESET"), props, comment);
        Assert.nonEmptyString(sensor);
        setReadOnly();
        this.sensor = sensor;
    }

    private static <V> ResetSensor<V> make(String sensor, BlocklyBlockProperties props, BlocklyComment comment) {
        return new ResetSensor<>(sensor, props, comment);
    }

    @Override
    public String toString() {
        return "ResetSensor[" + this.sensor + "]";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
        String sensorName = Jaxb2Ast.extractField(fields, BlocklyConstants.SENSOR);
        return ResetSensor.make(sensorName, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    /**
     * @return converts AST representation of block to JAXB representation of block
     */
    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this, jaxbDestination);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.SENSOR, this.getSensor());

        return jaxbDestination;
    }

    /**
     * Getter for the sensor that will be resetted
     *
     * @return the sensor to be resetted as a String values will be one of the following: - 'OBSTACLEDETECTOR' - 'KEYPAD' - 'SOUND' - 'RCCODE' - 'IRCODE'
     */
    public String getSensor() {
        return this.sensor;
    }
}
