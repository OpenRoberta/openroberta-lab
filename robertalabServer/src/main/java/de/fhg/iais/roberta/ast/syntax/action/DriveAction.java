package de.fhg.iais.roberta.ast.syntax.action;

import java.util.Locale;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;

/**
 * This class represents the <b>robActions_motor_on_for</b> and <b>robActions_motor_on</b> blocks from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for setting the motors in pilot mode.<br/>
 * <br>
 * The client must provide the {@link Direction} and {@link MotionParam} (distance the robot should cover and speed). <br>
 * <br>
 * To create an instance from this class use the method {@link #make(Direction, MotionParam)}.<br>
 */
public class DriveAction extends Action {
    private final Direction direction;
    private final MotionParam param;

    private DriveAction(Direction direction, MotionParam param) {
        super(Phrase.Kind.DriveAction);
        Assert.isTrue(direction != null && param != null);
        this.direction = direction;
        this.param = param;
        setReadOnly();
    }

    /**
     * Creates instance of {@link DriveAction}. This instance is read only and can not be modified.
     * 
     * @param direction {@link Direction} in which the robot will drive,
     * @param param {@link MotionParam} that set up the parameters for the movement of the robot (distance the robot should cover and speed),
     * @return read only object of class {@link DriveAction}
     */
    public static DriveAction make(Direction direction, MotionParam param) {
        return new DriveAction(direction, param);
    }

    /**
     * @return {@link Direction} in which the robot will drive
     */
    public Direction getDirection() {
        return this.direction;
    }

    /**
     * @return {@link MotionParam} for the motor (speed and distance in which the motors are set).
     */
    public MotionParam getParam() {
        return this.param;
    }

    @Override
    public void generateJava(StringBuilder sb, int indentation) {
        // TODO Auto-generated method stub

    }

    @Override
    public String toString() {
        return "DriveAction [" + this.direction + ", " + this.param + "]";
    }

    /**
     * Direction in which the robot will drive.
     */
    public static enum Direction {
        FOREWARD(), BACKWARD();

        private final String[] values;

        private Direction(String... values) {
            this.values = values;
        }

        /**
         * Get direction from {@link Direction} from string parameter. It is possible for one direction to have multiple string mappings.
         * Throws exception if the direction does not exists.
         * 
         * @param name of the direction
         * @return name of the direction from the enum {@link Direction}
         */
        public static Direction get(String s) {
            if ( s == null || s.isEmpty() ) {
                throw new DbcException("Invalid direction: " + s);
            }
            String sUpper = s.trim().toUpperCase(Locale.GERMAN);
            for ( Direction sp : Direction.values() ) {
                if ( sp.toString().equals(sUpper) ) {
                    return sp;
                }
                for ( String value : sp.values ) {
                    if ( sUpper.equals(value) ) {
                        return sp;
                    }
                }
            }
            throw new DbcException("Invalid direction: " + s);
        }
    }

}
