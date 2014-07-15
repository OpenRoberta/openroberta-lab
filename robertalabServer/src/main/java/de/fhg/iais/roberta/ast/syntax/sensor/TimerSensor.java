package de.fhg.iais.roberta.ast.syntax.sensor;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * Time sensor that can operate in multiple modes. See enum {@link Mode} for all possible modes that the sensor have.
 * 
 * @author kcvejoski
 */
public class TimerSensor extends Sensor {
    private final Mode mode;
    private final int timer;

    private TimerSensor(Mode mode, int timer) {
        super(Phrase.Kind.BrickSensor);
        Assert.isTrue(timer < 10);
        this.mode = mode;
        this.timer = timer;
        setReadOnly();
    }

    /**
     * Create object of the class {@link TimerSensor}.
     * 
     * @param mode in which the sensor is operating. See enum {@link Mode} for all possible modes that the sensor have.
     * @param timer integer value
     * @return
     */
    public static TimerSensor make(Mode mode, int timer) {
        return new TimerSensor(mode, timer);
    }

    /**
     * @return get the mode of sensor. See enum {@link Mode} for all possible modes that the sensor have.
     */
    public Mode getMode() {
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
    public void generateJava(StringBuilder sb, int indentation) {
        sb.append("(" + this.mode + ", " + this.timer + ")");
    }

    /**
     * Mode of the time sensor.
     * 
     * @author kcvejoski
     */
    public static enum Mode {
        RESET, GET_SAMPLE;
    }
}
