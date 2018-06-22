package de.fhg.iais.roberta.syntax.check.program.ev3;

import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.mode.sensor.CompassSensorMode;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.sound.SayTextAction;
import de.fhg.iais.roberta.syntax.check.program.RobotBrickCheckVisitor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.typecheck.NepoInfo;

public class BrickCheckVisitor extends RobotBrickCheckVisitor {

    public BrickCheckVisitor(Configuration brickConfiguration) {
        super(brickConfiguration);
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        super.visitMotorOnAction(motorOnAction);
        if ( motorOnAction.getInfos().getErrorCount() == 0 ) {
            ActorType type = this.brickConfiguration.getActorOnPort(motorOnAction.getPort()).getName();
            boolean duration = motorOnAction.getParam().getDuration() != null;
            if ( (type == ActorType.OTHER) && duration ) {
                motorOnAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_OTHER_NOT_SUPPORTED"));
            }
        }
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        super.visitCompassSensor(compassSensor);
        if ( this.brickConfiguration.getRobotName().equals("ev3dev") && (compassSensor.getMode() == CompassSensorMode.CALIBRATE) ) {
            compassSensor.addInfo(NepoInfo.warning("BLOCK_NOT_EXECUTED"));
        }
        return null;
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
