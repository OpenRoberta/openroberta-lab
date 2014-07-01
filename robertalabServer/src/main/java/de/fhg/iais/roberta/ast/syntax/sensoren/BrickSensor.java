package de.fhg.iais.roberta.ast.syntax.sensoren;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * Sensor that checks if a button on the brick is pressed.
 * 
 * @author kcvejoski
 */
public class BrickSensor extends Sensor {
    private final BrickKey key;
    private final Mode mode;

    private BrickSensor(Mode mode, BrickKey key) {
        super(Phrase.Kind.SteinSensor);
        Assert.isTrue(mode != null && !key.equals(""));
        this.mode = mode;
        this.key = key;
        setReadOnly();
    }

    /**
     * Create object of the class {@link BrickSensor}.
     * 
     * @param mode in which the sensor is operating. See enum {@link Mode} for all possible modes that the sensor have.
     * @param key on the brick. See enum {@link BrickKey} for all possible keys.
     * @return
     */
    public static BrickSensor make(Mode mode, BrickKey key) {
        return new BrickSensor(mode, key);
    }

    /**
     * @return get the key. See enum {@link BrickKey} for all possible keys.
     */
    public BrickKey getKey() {
        return this.key;
    }

    /**
     * @return get the mode of sensor. See enum {@link Mode} for all possible modes that the sensor have.
     */
    public Mode getMode() {
        return this.mode;
    }

    @Override
    public void generateJava(StringBuilder sb, int indentation) {
        sb.append("(" + this.mode + ", " + this.key + ")");
    }

    @Override
    public String toString() {
        return "BrickSensor [key=" + this.key + ", mode=" + this.mode + "]";
    }

    /**
     * Modes in which the sensor can operate.
     * 
     * @author kcvejoski
     */
    public static enum Mode {
        IS_PRESSED, WAIT_FOR_PRESS, WAIT_FOR_PRESS_AND_RELEASE;
    }
}
