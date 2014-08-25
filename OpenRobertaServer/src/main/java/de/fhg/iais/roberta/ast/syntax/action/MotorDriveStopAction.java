package de.fhg.iais.roberta.ast.syntax.action;

import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.codegen.lejos.Visitor;

/**
 * This class represents the <b>robActions_motorDiff_stop</b> block from Blockly into the AST (abstract syntax tree).
 * Object from this class will generate code to stop the work of the motors.<br/>
 */
public class MotorDriveStopAction extends Action {

    private MotorDriveStopAction() {
        super(Phrase.Kind.STOP_ACTION);
        setReadOnly();
    }

    /**
     * Creates instance of {@link MotorDriveStopAction}. This instance is read only and can not be modified.
     * 
     * @return read only object of class {@link MotorDriveStopAction}.
     */
    public static MotorDriveStopAction make() {
        return new MotorDriveStopAction();
    }

    @Override
    public String toString() {
        return "StopAction []";
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitMotorDriveStopAction(this);
    }
}
