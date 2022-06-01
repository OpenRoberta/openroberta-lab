package de.fhg.iais.roberta.syntax.sensor.generic;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.util.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.util.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.util.syntax.BlocklyComment;
import de.fhg.iais.roberta.util.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.util.syntax.SensorMetaDataBean;
import de.fhg.iais.roberta.transformer.Ast2Jaxb;
import de.fhg.iais.roberta.transformer.Jaxb2Ast;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;

/**
 * This class represents the <b>robSensors_timer_reset</b> and <b>robSensors_timer_getSample</b> blocks from Blockly into the AST (abstract syntax tree). Object
 * from this class will generate code for reset the sensor or getting a sample from the sensor.<br/>
 * <br>
 * The client must provide the {@link SensorPort} and {@link TimerSensorMode}. See enum {@link TimerSensorMode} for all possible modes of the sensor.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(TimerSensorMode, int, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class TimerSensor<V> extends ExternalSensor<V> {

    private TimerSensor(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(sensorMetaDataBean, BlockTypeContainer.getByName("TIMER_SENSING"), properties, comment);
        setReadOnly();
    }

    /**
     * Create object of the class {@link TimerSensor}.
     *
     * @param mode in which the sensor is operating; must be <b>not</b> null; see enum {@link TimerSensorMode} for all possible modes that the sensor have,
     * @param timer integer value,
     * @param properties of the block (see {@link BlocklyBlockProperties}),
     * @param comment added from the user,
     * @return read only object of {@link TimerSensor}
     */
    static public <V> TimerSensor<V> make(SensorMetaDataBean sensorMetaDataBean, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new TimerSensor<>(sensorMetaDataBean, properties, comment);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2ProgramAst<V> helper) {
        BlocklyDropdownFactory factory = helper.getDropdownFactory();
        SensorMetaDataBean sensorMetaDataBean;
        //TODO This if statement should be removed when we have new implementation of reset sensor blockly block
        if ( block.getType().equals(BlocklyConstants.ROB_SENSORS_TIMER_RESET) || block.getType().equals(BlocklyConstants.ROB_SENSORS_TIMER_RESET_CALLIOPE) ) {
            List<Field> fields = Jaxb2Ast.extractFields(block, (short) 1);
            String portName = Jaxb2Ast.extractField(fields, BlocklyConstants.SENSORPORT);
            sensorMetaDataBean =
                new SensorMetaDataBean(Jaxb2Ast.sanitizePort(portName), factory.getMode("RESET"), Jaxb2Ast.sanitizeSlot(BlocklyConstants.NO_SLOT), null);
            return TimerSensor.make(sensorMetaDataBean, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
        }
        sensorMetaDataBean = extractPortAndModeAndSlot(block, helper);
        return TimerSensor.make(sensorMetaDataBean, Jaxb2Ast.extractBlockProperties(block), Jaxb2Ast.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        if ( getMode().toString().equals("RESET") ) {
            Block jaxbDestination = new Block();
            Ast2Jaxb.setBasicProperties(this, jaxbDestination);
            String fieldValue = getUserDefinedPort();
            Ast2Jaxb.addField(jaxbDestination, BlocklyConstants.SENSORPORT, fieldValue);
            return jaxbDestination;
        } else {
            return super.astToBlock();
        }

    }

}
