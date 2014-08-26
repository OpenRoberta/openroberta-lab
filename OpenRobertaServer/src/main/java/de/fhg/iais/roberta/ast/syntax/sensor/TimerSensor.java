package de.fhg.iais.roberta.ast.syntax.sensor;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.codegen.lejos.Visitor;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robSensors_timer_reset</b> and <b>robSensors_timer_getSample</b> blocks from Blockly into
 * the AST (abstract syntax
 * tree).
 * Object from this class will generate code for reset the sensor or getting a sample from the sensor.<br/>
 * <br>
 * The client must provide the {@link SensorPort} and {@link TimerSensorMode}. See enum {@link TimerSensorMode} for all possible modes of the sensor.<br>
 * <br>
 * To create an instance from this class use the method {@link #make(TimerSensorMode, SensorPort)}.<br>
 */
public class TimerSensor<V> extends Sensor<V> {
    private final TimerSensorMode mode;
    private final int timer;

    private TimerSensor(TimerSensorMode mode, int timer) {
        super(Phrase.Kind.TIMER_SENSING);
        Assert.isTrue(timer < 10);
        this.mode = mode;
        this.timer = timer;
        setReadOnly();
    }

    /**
     * Create object of the class {@link TimerSensor}.
     * 
     * @param mode in which the sensor is operating. See enum {@link TimerSensorMode} for all possible modes that the sensor have.
     * @param timer integer value
     * @return read only object of {@link TimerSensor}
     */
    public static <V> TimerSensor<V> make(TimerSensorMode mode, int timer) {
        return new TimerSensor<V>(mode, timer);
    }

    /**
     * @return get the mode of sensor. See enum {@link TimerSensorMode} for all possible modes that the sensor have.
     */
    public TimerSensorMode getMode() {
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
    protected V accept(Visitor<V> visitor) {
        return visitor.visitTimerSensor(this);
    }
}
