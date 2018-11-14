package de.fhg.iais.roberta.visitor.validate;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.syntax.action.ev3.ShowPictureAction;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.IRSeekerSensor;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.visitor.hardware.IEv3Visitor;

public final class Ev3SimValidatorVisitor extends AbstractSimValidatorVisitor implements IEv3Visitor<Void> {

    public Ev3SimValidatorVisitor(Configuration brickConfiguration) {
        super(brickConfiguration);
    }

    @Override
    public Void visitIRSeekerSensor(IRSeekerSensor<Void> irSeekerSensor) {
        irSeekerSensor.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        super.visitCompassSensor(compassSensor);
        compassSensor.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitShowPictureAction(ShowPictureAction<Void> showPictureAction) {
        showPictureAction.getX().visit(this);
        showPictureAction.getY().visit(this);
        return null;
    }
}
