package de.fhg.iais.roberta.visitor.collect;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
import de.fhg.iais.roberta.syntax.actors.arduino.PinReadValueAction;
import de.fhg.iais.roberta.syntax.actors.arduino.PinWriteValueAction;
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

    protected final Set<ConfigurationComponent> usedConfigurationBlocks = new LinkedHashSet<>();

    public ArduinoUsedHardwareCollectorVisitor(ArrayList<ArrayList<Phrase<Void>>> phrasesSet, Configuration configuration) {
        super(configuration);

        check(phrasesSet);
    }

    public Set<UsedSensor> getTimer() {
        return this.usedSensors;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        motorOnAction.getParam().getSpeed().visit(this);
        if ( motorOnAction.getParam().getDuration() != null ) {
            motorOnAction.getDurationValue().visit(this);
        }
        if ( this.robotConfiguration != null ) {
            //Actor actor = this.brickConfiguration.getActors().get(motorOnAction.getPort());
            //if ( actor != null ) {
            //    this.usedActors.add(new UsedActor(motorOnAction.getPort(), actor.getName()));
            //}
        }
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        //        this.usedSensors.add(new UsedSensor(encoderSensor.getPort(), SC.ENCODER, encoderSensor.getMode()));
        return null;
    }

    @Override
    public Void visitRelayAction(RelayAction<Void> relayAction) {
        return null;
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor<Void> pinGetValueSensor) {
        this.usedSensors.add(new UsedSensor(pinGetValueSensor.getPort(), SC.PIN_VALUE, pinGetValueSensor.getMode()));
        return null;
    }

    @Override
    public Void visitPinWriteValueAction(PinWriteValueAction<Void> pinWriteValueSensor) {
        pinWriteValueSensor.getValue().visit(this);
        this.usedActors.add(new UsedActor(pinWriteValueSensor.getPort(), SC.ANALOG_PIN));
        return null;
    }

    @Override
    public Void visitSerialWriteAction(SerialWriteAction<Void> serialWriteAction) {
        serialWriteAction.getValue().visit(this);
        return null;
    }

    @Override
    public Void visitPinReadValueAction(PinReadValueAction<Void> pinReadValueActor) {
        this.usedActors.add(new UsedActor(pinReadValueActor.getPort(), SC.ANALOG_INPUT));
        return null;
    }

}
