package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;

/**
 * This class represents the <b>robActions_motorDiff_stop</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code to stop the work of the motors.<br/>
 */
public class MotorDriveStopAction<V> extends Action<V> {

    private MotorDriveStopAction(boolean disabled, String comment) {
        super(Phrase.Kind.STOP_ACTION, disabled, comment);
        setReadOnly();
    }

    /**
     * Creates instance of {@link MotorDriveStopAction}. This instance is read only and can not be modified.
     * 
     * @param disabled state of the block,
     * @param comment added from the user
     * @return read only object of class {@link MotorDriveStopAction}.
     */
    public static <V> MotorDriveStopAction<V> make(boolean disabled, String comment) {
        return new MotorDriveStopAction<V>(disabled, comment);
    }

    @Override
    public String toString() {
        return "StopAction []";
    }

    @Override
    protected V accept(AstVisitor<V> visitor) {
        return visitor.visitMotorDriveStopAction(this);
    }
}
