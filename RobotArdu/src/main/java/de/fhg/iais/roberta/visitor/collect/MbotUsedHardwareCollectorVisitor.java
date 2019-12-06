package de.fhg.iais.roberta.visitor.collect;

import java.util.ArrayList;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LEDMatrixImageAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LEDMatrixSetBrightnessAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LEDMatrixTextAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.SendIRAction;
import de.fhg.iais.roberta.syntax.expressions.arduino.LEDMatrixImage;
import de.fhg.iais.roberta.syntax.functions.arduino.LEDMatrixImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.arduino.LEDMatrixImageShiftFunction;
import de.fhg.iais.roberta.syntax.sensors.arduino.mbot.FlameSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.mbot.Joystick;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.visitor.hardware.IMbotVisitor;

/**
 * This visitor collects information for used actors and sensors in blockly program.
 *
 * @author kcvejoski
 */
public final class MbotUsedHardwareCollectorVisitor extends AbstractUsedHardwareCollectorVisitor implements IMbotVisitor<Void> {
    public MbotUsedHardwareCollectorVisitor(UsedHardwareBean.Builder builder, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, ConfigurationAst configuration) {
        super(builder, configuration);
    }

    @Override
    public Void visitJoystick(Joystick<Void> joystick) {
        this.builder.addUsedSensor(new UsedSensor(joystick.getPort(), SC.JOYSTICK, SC.DEFAULT));
        return null;
    }

    @Override
    public Void visitFlameSensor(FlameSensor<Void> flameSensor) {
        this.builder.addUsedSensor(new UsedSensor(flameSensor.getPort(), SC.FLAMESENSOR, SC.DEFAULT));
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        super.visitToneAction(toneAction);
        this.builder.addUsedActor(new UsedActor(toneAction.getPort(), SC.BUZZER));
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        super.visitPlayNoteAction(playNoteAction);
        this.builder.addUsedActor(new UsedActor(playNoteAction.getPort(), SC.BUZZER));
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        driveAction.getParam().getSpeed().accept(this);
        if ( driveAction.getParam().getDuration() != null ) {
            driveAction.getParam().getDuration().getValue().accept(this);
        }
        this.builder.addUsedActor(new UsedActor(this.robotConfiguration.getFirstMotorPort(SC.LEFT), SC.DIFFERENTIAL_DRIVE));
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {
        curveAction.getParamLeft().getSpeed().accept(this);
        curveAction.getParamRight().getSpeed().accept(this);
        if ( curveAction.getParamLeft().getDuration() != null ) {
            curveAction.getParamLeft().getDuration().getValue().accept(this);
        }
        this.builder.addUsedActor(new UsedActor(this.robotConfiguration.getFirstMotorPort(SC.LEFT), SC.DIFFERENTIAL_DRIVE));
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        turnAction.getParam().getSpeed().accept(this);
        if ( turnAction.getParam().getDuration() != null ) {
            turnAction.getParam().getDuration().getValue().accept(this);
        }
        this.builder.addUsedActor(new UsedActor(this.robotConfiguration.getFirstMotorPort(SC.LEFT), SC.DIFFERENTIAL_DRIVE));
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        if ( (this.robotConfiguration.getFirstMotorPort(SC.LEFT) != null) && (this.robotConfiguration.getFirstMotorPort(SC.RIGHT) != null) ) {
            this.builder.addUsedActor(new UsedActor(this.robotConfiguration.getFirstMotorPort(SC.LEFT), SC.GEARED_MOTOR));
            this.builder.addUsedActor(new UsedActor(this.robotConfiguration.getFirstMotorPort(SC.RIGHT), SC.GEARED_MOTOR));
        }
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        this.builder.addUsedActor(new UsedActor("0", SC.LED_ON_BOARD));
        return null;
    }

    @Override
    public Void visitSerialWriteAction(SerialWriteAction<Void> serialWriteAction) {
        serialWriteAction.getValue().accept(this);
        return null;
    }

    @Override
    public Void visitSendIRAction(SendIRAction<Void> sendIRAction) {
        this.builder.addUsedActor(new UsedActor("INTERNAL", SC.IR_TRANSMITTER));
        return null;
    }

    @Override
    public Void visitReceiveIRAction(ReceiveIRAction<Void> receiveIRAction) {
        this.builder.addUsedActor(new UsedActor("INTERNAL", SC.IR_TRANSMITTER));
        return null;
    }

    @Override
    public Void visitLEDMatrixImageAction(LEDMatrixImageAction<Void> ledMatrixImageAction) {
        this.builder.addUsedActor(new UsedActor(ledMatrixImageAction.getPort(), SC.LED_MATRIX));
        return null;
    }

    @Override
    public Void visitLEDMatrixTextAction(LEDMatrixTextAction<Void> ledMatrixTextAction) {
        ledMatrixTextAction.getMsg().accept(this);
        this.builder.addUsedActor(new UsedActor(ledMatrixTextAction.getPort(), SC.LED_MATRIX));
        return null;
    }

    @Override
    public Void visitLEDMatrixImageShiftFunction(LEDMatrixImageShiftFunction<Void> ledMatrixImageShiftFunction) {
        ledMatrixImageShiftFunction.getImage().accept(this);
        this.builder.addUsedActor(new UsedActor("0", SC.LED_MATRIX));       
        return null;
    }

    @Override
    public Void visitLEDMatrixImageInvertFunction(LEDMatrixImageInvertFunction<Void> ledMatrixImageInverFunction) {
        ledMatrixImageInverFunction.getImage().accept(this);
        this.builder.addUsedActor(new UsedActor("0", SC.LED_MATRIX));       
        return null;
    }

    @Override
    public Void visitLEDMatrixImage(LEDMatrixImage<Void> ledMatrixImage) {
        return null;
    }

    @Override
    public Void visitLEDMatrixSetBrightnessAction(LEDMatrixSetBrightnessAction<Void> ledMatrixSetBrightnessAction) {
        ledMatrixSetBrightnessAction.getBrightness().accept(this);
        return null;
    }
}
