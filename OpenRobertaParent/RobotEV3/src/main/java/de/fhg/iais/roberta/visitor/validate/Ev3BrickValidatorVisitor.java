package de.fhg.iais.roberta.visitor.validate;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.ev3.ShowPictureAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.speech.SayTextAction;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.visitor.hardware.IEv3Visitor;

public final class Ev3BrickValidatorVisitor extends AbstractBrickValidatorVisitor implements IEv3Visitor<Void> {

    public Ev3BrickValidatorVisitor(Configuration brickConfiguration) {
        super(brickConfiguration);
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        super.visitMotorOnAction(motorOnAction);
        if ( motorOnAction.getInfos().getErrorCount() == 0 ) {
            String componentType = this.robotConfiguration.getConfigurationComponent(motorOnAction.getUserDefinedPort()).getComponentType();
            boolean duration = motorOnAction.getParam().getDuration() != null;
            if ( componentType.equals(SC.OTHER) && duration ) {
                motorOnAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_OTHER_NOT_SUPPORTED"));
            }
        }
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        super.visitCompassSensor(compassSensor);
        if ( this.robotConfiguration.getRobotName().equals("ev3dev") && (compassSensor.getMode().equals(SC.CALIBRATE)) ) {
            compassSensor.addInfo(NepoInfo.warning("BLOCK_NOT_EXECUTED"));
        }
        return null;
    }

    @Override
    public Void visitSayTextAction(SayTextAction<Void> sayTextAction) {
        super.visitSayTextAction(sayTextAction);
        if ( this.robotConfiguration.getRobotName().equals("ev3lejosV0") ) {
            sayTextAction.addInfo(NepoInfo.warning("BLOCK_NOT_EXECUTED"));
        }
        return null;
    }

    @Override
    public Void visitShowPictureAction(ShowPictureAction<Void> showPictureAction) {
        showPictureAction.getX().visit(this);
        showPictureAction.getY().visit(this);
        return null;
    }

}
