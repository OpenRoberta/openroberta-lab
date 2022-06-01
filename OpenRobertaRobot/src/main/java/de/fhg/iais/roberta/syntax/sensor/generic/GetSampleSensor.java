package de.fhg.iais.roberta.syntax.sensor.generic;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.util.dbc.Assert;

/**
 * This class represents the <b>robSensors_getSample</b> block from Blockly into the AST (abstract syntax tree). Object from this class will generate code for
 * setting the mode of the sensor or getting a sample from the sensor.<br/>
 * <br>
 * The client must provide the {@link SensorType} and port. See enum {@link SensorType} for all possible type of sensors.<br>
 * <br>
 * To create an instance from this class use the method {@link #make}.<br>
 */
public class GetSampleSensor<V> extends Sensor<V> {
    private final Sensor<V> sensor;
    private final String sensorPort;
    private final String slot;
    private final String sensorTypeAndMode;
    private final Mutation mutation;
    private final List<Hide> hide;

    @SuppressWarnings("unchecked")
    private GetSampleSensor(
        String sensorTypeAndMode,
        String port,
        String slot,
        Mutation mutation,
        List<Hide> hide,
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        BlocklyDropdownFactory factory) //
    {
        super(BlockTypeContainer.getByName("SENSOR_GET_SAMPLE"), properties, comment);
        Assert.notNull(sensorTypeAndMode);
        Assert.notNull(port);
        this.sensorPort = port;
        this.slot = slot;
        this.sensorTypeAndMode = sensorTypeAndMode;
        this.mutation = mutation;
        this.hide = hide;
        this.sensor = (Sensor<V>) factory.createSensor(sensorTypeAndMode, port, slot, mutation, properties, comment);
        setReadOnly();
    }

    /**
     * Create object of the class {@link GetSampleSensor}.
     *
     * @param sensorType; must be <b>not</b> null,
     * @param port on which the sensor is connected; must be <b>non-empty</b> string,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of class {@link GetSampleSensor}
     */
    public static <V> GetSampleSensor<V> make(
        String sensorTypeAndMode,
        String port,
        String slot,
        Mutation mutation,
        List<Hide> hide,
        BlocklyBlockProperties properties,
        BlocklyComment comment,
        BlocklyDropdownFactory factory) {
        return new GetSampleSensor(sensorTypeAndMode, port, slot, mutation, hide, properties, comment, factory);
    }

    public Sensor<V> getSensor() {
        return this.sensor;
    }

    public String getSensorPort() {
        return this.sensorPort;
    }

    public String getSlot() {
        return this.slot;
    }

    public List<Hide> getHide() {
        return this.hide;
    }

    /**
     * @return type of the sensor who will get the sample
     */
    public String getSensorTypeAndMode() {
        return this.sensorTypeAndMode;
    }

    public Mutation getMutation() {
        return this.mutation;
    }

    @Override
    public String toString() {
        return "GetSampleSensor [" + this.sensor + "]";
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        List<Field> fields = Jaxb2Ast.extractFields(block, (short) 3);
        String mutationInput = block.getMutation().getInput();
        String modeName = Jaxb2Ast.extractField(fields, BlocklyConstants.SENSORTYPE, mutationInput);
        String portName = Jaxb2Ast.extractField(fields, BlocklyConstants.SENSORPORT, BlocklyConstants.EMPTY_PORT);
        String robotGroup = helper.getRobotFactory().getGroup();
        boolean calliopeOrMicrobit = "calliope".equals(robotGroup) || "microbit".equals(robotGroup);
        boolean gyroOrAcc = mutationInput.equals("ACCELEROMETER_VALUE") || mutationInput.equals("GYRO_ANGLE");
        String slotName;
        if ( calliopeOrMicrobit && gyroOrAcc ) {
            slotName = Jaxb2Ast.extractNonEmptyField(fields, BlocklyConstants.SLOT, BlocklyConstants.X);
        } else {
            slotName = Jaxb2Ast.extractField(fields, BlocklyConstants.SLOT, BlocklyConstants.NO_SLOT);
        }
        return GetSampleSensor
            .make(
                modeName,
                portName,
                slotName,
                block.getMutation(),
                block.getHide(),
                Jaxb2Ast.extractBlockProperties(block),
                Jaxb2Ast.extractComment(block),
                helper.getDropdownFactory());
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this.sensor, jaxbDestination);
        if ( this.mutation != null ) {
            jaxbDestination.setMutation(mutation);
        }
        if ( this.hide != null && this.hide.size() > 0 ) {
            jaxbDestination.getHide().addAll(hide);
        }
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.SENSORTYPE, this.sensorTypeAndMode);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.SENSORPORT, this.sensorPort);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.SLOT, this.slot);

        return jaxbDestination;
    }

}
