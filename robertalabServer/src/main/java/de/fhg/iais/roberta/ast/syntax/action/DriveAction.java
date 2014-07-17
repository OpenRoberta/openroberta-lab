package de.fhg.iais.roberta.ast.syntax.action;

import java.util.Locale;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;

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

    public static DriveAction make(Direction direction, MotionParam param) {
        return new DriveAction(direction, param);
    }

    public Direction getDirection() {
        return this.direction;
    }

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

    public static enum Direction {
        FOREWARD(), BACKWARD();

        private final String[] values;

        private Direction(String... values) {
            this.values = values;
        }

        /**
         * get direction from {@link Direction} from string parameter. It is possible for one direction to have multiple string mappings.
         * Throws exception if the direction does not exists.
         * 
         * @param name of the motor mode
         * @return motor mode from the enum {@link MotorDuration}
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
