package de.fhg.iais.roberta.visitor.collect;

import java.util.ArrayList;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.generic.PinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
import de.fhg.iais.roberta.syntax.actors.arduino.RelayAction;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.visitor.hardware.IArduinoVisitor;

/**
 * This visitor collects information for used actors and sensors in blockly program.
 *
 * @author eovchinnikova
 */
public final class ArduinoUsedHardwareCollectorVisitor extends AbstractUsedHardwareCollectorVisitor implements IArduinoVisitor<Void> {

    public ArduinoUsedHardwareCollectorVisitor(
        UsedHardwareBean.Builder builder,
        ArrayList<ArrayList<Phrase<Void>>> phrasesSet,
        ConfigurationAst robotConfiguration) {
        super(builder, robotConfiguration);
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        motorOnAction.getParam().getSpeed().accept(this);
        if ( motorOnAction.getParam().getDuration() != null ) {
            motorOnAction.getDurationValue().accept(this);
        }
        if ( this.robotConfiguration != null ) {
            //TODO: nothing done on MotorOnAction
            //Actor actor = this.brickConfiguration.getActors().get(motorOnAction.getPort());
            //if ( actor != null ) {
            //    this.builder.addUsedActor(new UsedActor(motorOnAction.getPort(), actor.getName()));
            //}
        }
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        //        this.builder.addUsedSensor(new UsedSensor(encoderSensor.getPort(), SC.ENCODER, encoderSensor.getMode()));
        return null;
    }

    @Override
    public Void visitRelayAction(RelayAction<Void> relayAction) {
        return null;
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor<Void> pinGetValueSensor) {
        this.builder.addUsedSensor(new UsedSensor(pinGetValueSensor.getPort(), SC.PIN_VALUE, pinGetValueSensor.getMode()));
        return null;
    }

    @Override
    public Void visitPinWriteValueAction(PinWriteValueAction<Void> pinWriteValueSensor) {
        pinWriteValueSensor.getValue().accept(this);
        this.builder.addUsedActor(new UsedActor(pinWriteValueSensor.getPort(), SC.ANALOG_PIN));
        return null;
    }

    @Override
    public Void visitSerialWriteAction(SerialWriteAction<Void> serialWriteAction) {
        serialWriteAction.getValue().accept(this);
        return null;
    }
}
