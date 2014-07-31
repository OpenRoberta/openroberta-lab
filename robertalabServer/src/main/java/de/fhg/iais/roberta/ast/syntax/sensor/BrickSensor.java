package de.fhg.iais.roberta.ast.syntax.sensor;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;

/**
 * This class represents the <b>robSensors_key_isPressed</b> and <b>robSensors_key_isPressedAndReleased</b> blocks from Blockly into the AST (abstract syntax
 * tree).
 * Object from this class will generate code for checking if a button on the brick is pressed.<br/>
 * <br>
 * The client must provide the {@link BrickKey} and {@link Mode}. <br>
 * <br>
 * To create an instance from this class use the method {@link #make(Mode, BrickKey)}.<br>
 */
public class BrickSensor extends Sensor {
    private final BrickKey key;
    private final Mode mode;

    private BrickSensor(Mode mode, BrickKey key) {
        super(Phrase.Kind.BRICK_SENSIG);
        Assert.isTrue(mode != null && !key.equals(""));
        this.mode = mode;
        this.key = key;
        setReadOnly();
    }

    /**
     * Creates instance of {@link BrickSensor}. This instance is read only and can not be modified.
     * 
     * @param mode in which the sensor is operating. See enum {@link Mode} for all possible modes that the sensor have.
     * @param key on the brick. See enum {@link BrickKey} for all possible keys.
     * @return read only object of class {@link BrickSensor}
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
        switch ( this.mode ) {
            case IS_PRESSED:
                sb.append("hal.isPressed(" + this.key.toString() + ")");
                break;
            case WAIT_FOR_PRESS_AND_RELEASE:
                sb.append("hal.isPressedAndReleased(" + this.key.toString() + ")");
                break;
            default:
                throw new DbcException("Invalide mode for BrickSensor!");
        }
    }

    @Override
    public String toString() {
        return "BrickSensor [key=" + this.key + ", mode=" + this.mode + "]";
    }

    /**
     * Modes in which the sensor can operate.
     */
    public static enum Mode {
        IS_PRESSED, WAIT_FOR_PRESS, WAIT_FOR_PRESS_AND_RELEASE;
    }
}
