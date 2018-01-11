package de.fhg.iais.roberta.syntax.check.program.ev3;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.syntax.action.sound.SayTextAction;
import de.fhg.iais.roberta.syntax.check.program.RobotBrickCheckVisitor;
import de.fhg.iais.roberta.typecheck.NepoInfo;

public class BrickCheckVisitor extends RobotBrickCheckVisitor {

    public BrickCheckVisitor(Configuration brickConfiguration) {
        super(brickConfiguration);
    }

    @Override
    public Void visitSayTextAction(SayTextAction<Void> sayTextAction) {
        super.visitSayTextAction(sayTextAction);
        if ( this.brickConfiguration.getRobotName().equals("ev3lejos") ) {
            sayTextAction.addInfo(NepoInfo.warning("BLOCK_NOT_EXECUTED"));
        }
        return null;
    }
}
