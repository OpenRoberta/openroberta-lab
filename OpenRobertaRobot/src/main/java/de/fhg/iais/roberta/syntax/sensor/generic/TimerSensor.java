package de.fhg.iais.roberta.syntax.sensor.generic;

import java.util.List;

import de.fhg.iais.roberta.blockly.generated.Block;
import de.fhg.iais.roberta.blockly.generated.Field;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.sensor.ITimerSensorMode;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;import de.fhg.iais.roberta.syntax.BlockTypeContainer.BlockType;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.transformer.Jaxb2AstTransformer;
import de.fhg.iais.roberta.transformer.JaxbTransformerHelper;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.AstVisitor;

/**
 * This class represents the <b>robSensors_timer_reset</b> and <b>robSensors_timer_getSample</b> blocks from Blockly into
 * the AST (abstract syntax
 * tree).
 * Object from this class will generate code for reset the sensor or getting a sample from the sensor.<br/>
 * <br>
 * The client must provide the {@link SensorPort} and {@link TimerSensorMode}. See enum {@link TimerSensorMode} for all possible modes of the sensor.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(TimerSensorMode, int, BlocklyBlockProperties, BlocklyComment)}.<br>
 */
public class TimerSensor<V> extends Sensor<V> {
    private final ITimerSensorMode mode;
    private final int timer;

    private TimerSensor(ITimerSensorMode mode, int timer, BlocklyBlockProperties properties, BlocklyComment comment) {
        super(BlockTypeContainer.getByName("TIMER_SENSING"),properties, comment);
        Assert.isTrue(timer < 10);
        this.mode = mode;
        this.timer = timer;
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
    static <V> TimerSensor<V> make(ITimerSensorMode mode, int timer, BlocklyBlockProperties properties, BlocklyComment comment) {
        return new TimerSensor<V>(mode, timer, properties, comment);
    }

    /**
     * @return get the mode of sensor. See enum {@link TimerSensorMode} for all possible modes that the sensor have.
     */
    public ITimerSensorMode getMode() {
        return this.mode;
    }

    /**
     * @return number of the timer
     */
    public int getTimer() {
        return this.timer;
    }

    @Override
    public String toString() {
        return "TimerSensor [mode=" + this.mode + ", timer=" + this.timer + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitTimerSensor(this);
    }

    /**
     * Transformation from JAXB object to corresponding AST object.
     *
     * @param block for transformation
     * @param helper class for making the transformation
     * @return corresponding AST object
     */
    public static <V> Phrase<V> jaxbToAst(Block block, Jaxb2AstTransformer<V> helper) {
        IRobotFactory factory = helper.getModeFactory();
        List<Field> fields = helper.extractFields(block, (short) 1);
        String portName = helper.extractField(fields, BlocklyConstants.SENSORNUM);

        if ( block.getType().equals(BlocklyConstants.ROB_SENSORS_TIMER_RESET) ) {
            return TimerSensor
                .make(factory.getTimerSensorMode("RESET"), Integer.valueOf(portName), helper.extractBlockProperties(block), helper.extractComment(block));
        }
        return TimerSensor
            .make(factory.getTimerSensorMode("GET_SAMPLE"), Integer.valueOf(portName), helper.extractBlockProperties(block), helper.extractComment(block));
    }

    @Override
    public Block astToBlock() {
        Block jaxbDestination = new Block();
        JaxbTransformerHelper.setBasicProperties(this, jaxbDestination);

        String fieldValue = String.valueOf(getTimer());
        JaxbTransformerHelper.addField(jaxbDestination, BlocklyConstants.SENSORNUM, fieldValue);
        return jaxbDestination;
    }
}