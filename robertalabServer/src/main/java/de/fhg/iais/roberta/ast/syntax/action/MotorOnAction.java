package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robActions_motor_on_for</b> and <b>robActions_motor_on</b> blocks from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for setting the motor speed and type of movement connected on given port and turn the motor on.<br/>
 * <br/>
 * The client must provide the {@link ActorPort} and {@link MotionParam} (number of rotations or degrees and speed).
 */
public class MotorOnAction extends Action {
    ActorPort port;
    MotionParam param;

    private MotorOnAction(ActorPort port, MotionParam param) {
        super(Phrase.Kind.MOTOR_ON_ACTION);
        Assert.isTrue(param != null);
        this.param = param;
        this.port = port;
        setReadOnly();
    }

    /**
     * Creates instance of {@link MotorOnAction}. This instance is read only and can not be modified.
     * 
     * @param port {@link ActorPort} on which the motor is connected,
     * @param param {@link MotionParam} that set up the parameters for the movement of the robot (number of rotations or degrees and speed),
     * @return read only object of class {@link MotorOnAction}.
     */
    public static MotorOnAction make(ActorPort port, MotionParam param) {
        return new MotorOnAction(port, param);
    }

    /**
     * @return {@link MotionParam} for the motor (number of rotations or degrees and speed).
     */
    public MotionParam getParam() {
        return this.param;
    }

    /**
     * @return port on which the motor is connected.
     */
    public ActorPort getPort() {
        return this.port;
    }

    @Override
    public String toString() {
        return "MotorOnAction [" + this.port + ", " + this.param + "]";
    }

    @Override
    public void generateJava(StringBuilder sb, int indentation) {
        sb.append("hal.setMotorSpeed(" + this.port.toString() + ", ");
        this.param.getSpeed().generateJava(sb, indentation);
        sb.append(")");
        //sb.append("(" + this.param + ", " + this.port + ")");
    }

}
