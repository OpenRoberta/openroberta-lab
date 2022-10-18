package de.fhg.iais.roberta.syntax.sensor.generic;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.blockly.generated.Hide;
import de.fhg.iais.roberta.blockly.generated.Mutation;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformer.forClass.NepoBasic;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;

/**
 * This class represents the <b>robSensors_getSample</b> block from Blockly. Both a sensor and a mode are needed (see sensorTypeAndMode)
 */
@NepoBasic(name = "SENSOR_GET_SAMPLE", category = "SENSOR", blocklyNames = {"sim_getSample", "robSensors_getSample_ardu", "mbedsensors_getsample", "robSensors_getSample"})
public final class GetSampleSensor extends Sensor {
    public final Sensor sensor;
    public final String sensorPort;
    public final String slot;
    public final String sensorTypeAndMode;
    public final Mutation mutation;
    public final Hide hide;

    @SuppressWarnings("unchecked")
    public GetSampleSensor(
        String sensorTypeAndMode,
        String port,
        String slot,
        Mutation mutation,
        Hide hide,
        BlocklyProperties properties,

        BlocklyDropdownFactory factory) //
    {
        super(properties);
        Assert.notNull(sensorTypeAndMode);
        Assert.notNull(port);
        this.sensorPort = port;
        this.slot = slot;
        this.sensorTypeAndMode = sensorTypeAndMode;
        this.mutation = mutation;
        this.hide = hide;
        this.sensor = (Sensor) factory.createSensor(sensorTypeAndMode, port, slot, mutation, properties);
        setReadOnly();
    }

    @Override
    public String toString() {
        return "GetSampleSensor [" + this.sensor + "]";
    }

    public static Phrase xml2ast(Block block, Jaxb2ProgramAst helper) {
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
        return new GetSampleSensor(modeName, portName, slotName, block.getMutation(), JaxbHelper.getHideFromBlock(block), Jaxb2Ast.extractBlocklyProperties(block), helper.getDropdownFactory());
    }

    @Override
    public Block ast2xml() {
        Block jaxbDestination = new Block();
        Ast2Jaxb.setBasicProperties(this.sensor, jaxbDestination);
        if ( this.mutation != null ) {
            jaxbDestination.setMutation(mutation);
        }
        if ( this.hide != null ) {
            jaxbDestination.getHide().add(hide);
        }
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.SENSORTYPE, this.sensorTypeAndMode);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.SENSORPORT, this.sensorPort);
        Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.SLOT, this.slot);

        return jaxbDestination;
    }

}
