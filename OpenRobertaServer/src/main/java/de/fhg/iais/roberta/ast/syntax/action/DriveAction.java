package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.codegen.lejos.Visitor;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robActions_motor_on_for</b> and <b>robActions_motor_on</b> blocks from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for setting the motors in pilot mode.<br/>
 * <br>
 * The client must provide the {@link DriveDirection} and {@link MotionParam} (distance the robot should cover and speed). <br>
 * <br>
 * To create an instance from this class use the method {@link #make(DriveDirection, MotionParam)}.<br>
 */
public class DriveAction extends Action {
    private final DriveDirection direction;
    private final MotionParam param;

    private DriveAction(DriveDirection direction, MotionParam param) {
        super(Phrase.Kind.DRIVE_ACTION);
        Assert.isTrue(direction != null && param != null);
        this.direction = direction;
        this.param = param;
        setReadOnly();
    }

    /**
     * Creates instance of {@link DriveAction}. This instance is read only and can not be modified.
     * 
     * @param direction {@link DriveDirection} in which the robot will drive,
     * @param param {@link MotionParam} that set up the parameters for the movement of the robot (distance the robot should cover and speed),
     * @return read only object of class {@link DriveAction}
     */
    public static DriveAction make(DriveDirection direction, MotionParam param) {
        return new DriveAction(direction, param);
    }

    /**
     * @return {@link DriveDirection} in which the robot will drive
     */
    public DriveDirection getDirection() {
        return this.direction;
    }

    /**
     * @return {@link MotionParam} for the motor (speed and distance in which the motors are set).
     */
    public MotionParam getParam() {
        return this.param;
    }

    @Override
    public String toString() {
        return "DriveAction [" + this.direction + ", " + this.param + "]";
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
