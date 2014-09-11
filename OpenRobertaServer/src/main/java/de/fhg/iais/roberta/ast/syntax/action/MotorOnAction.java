package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.dbc.Assert;

/**
 * This class represents the <b>robActions_motor_on_for</b> and <b>robActions_motor_on</b> blocks from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code for setting the motor speed and type of movement connected on given port and turn the motor on.<br/>
 * <br/>
 * The client must provide the {@link ActorPort} and {@link MotionParam} (number of rotations or degrees and speed).
 */
public final class MotorOnAction<V> extends Action<V> {
    private final ActorPort port;
    private final MotionParam<V> param;

    private MotorOnAction(ActorPort port, MotionParam<V> param, boolean disabled, String comment) {
        super(Phrase.Kind.MOTOR_ON_ACTION, disabled, comment);
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
     * @param disabled state of the block,
     * @param comment added from the user
     * @return read only object of class {@link MotorOnAction}.
     */
    public static <V> MotorOnAction<V> make(ActorPort port, MotionParam<V> param, boolean disabled, String comment) {
        return new MotorOnAction<V>(port, param, disabled, comment);
    }

    /**
     * @return {@link MotionParam} for the motor (number of rotations or degrees and speed).
     */
    public MotionParam<V> getParam() {
        return this.param;
    }

    /**
     * @return port on which the motor is connected.
     */
    public ActorPort getPort() {
        return this.port;
    }

    public MotorMoveMode getDurationMode() {
        return this.param.getDuration().getType();
    }

    public Expr<V> getDurationValue() {
        return this.param.getDuration().getValue();
    }

    @Override
    public String toString() {
        return "MotorOnAction [" + this.port + ", " + this.param + "]";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitMotorOnAction(this);
    }

}
