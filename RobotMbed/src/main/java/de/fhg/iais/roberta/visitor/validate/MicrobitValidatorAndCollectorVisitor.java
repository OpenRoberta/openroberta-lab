package de.fhg.iais.roberta.visitor.validate;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.generic.MbedPinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSendAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSetChannelAction;
import de.fhg.iais.roberta.syntax.action.mbed.SwitchLedMatrixAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.IMicrobitVisitor;

public class MicrobitValidatorAndCollectorVisitor extends MbedValidatorAndCollectorVisitor implements IMicrobitVisitor<Void> {

    private final boolean isSim;

    protected List<String> occupiedPins = Arrays.asList("3", "4", "5", "6", "7", "9", "10", "11", "12", "19", "20");
    protected List<String> ledPins = Arrays.asList("3", "4", "6", "7", "9", "10");

    private final boolean displaySwitchUsed;

    public MicrobitValidatorAndCollectorVisitor(
        ConfigurationAst brickConfiguration,
        ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders,
        boolean isSim,
        boolean displaySwitchUsed) //
    {
        super(brickConfiguration, beanBuilders);
        this.isSim = isSim;
        this.displaySwitchUsed = displaySwitchUsed;
    }

    @Override
    public Void visitRadioSendAction(RadioSendAction radioSendAction) {
        addToPhraseIfUnsupportedInSim(radioSendAction, false, isSim);
        return super.visitRadioSendAction(radioSendAction);
    }

    @Override
    public Void visitRadioReceiveAction(RadioReceiveAction radioReceiveAction) {
        addToPhraseIfUnsupportedInSim(radioReceiveAction, true, isSim);
        return super.visitRadioReceiveAction(radioReceiveAction);
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor pinValueSensor) {
        addToPhraseIfUnsupportedInSim(pinValueSensor, true, isSim);

        checkInternalPorts(pinValueSensor, pinValueSensor.getUserDefinedPort());
        return super.visitPinGetValueSensor(pinValueSensor);
    }

    @Override
    public Void visitMbedPinWriteValueAction(MbedPinWriteValueAction mbedPinWriteValueAction) {
        checkInternalPorts(mbedPinWriteValueAction, mbedPinWriteValueAction.port);
        return super.visitMbedPinWriteValueAction(mbedPinWriteValueAction);
    }

    private void checkInternalPorts(Phrase pinValueSensor, String port) {
        ConfigurationComponent configurationComponent = this.robotConfiguration.getConfigurationComponent(port);
        String pin = configurationComponent.getProperty("PIN1");
        if ( this.ledPins.contains(pin) ) {
            if ( !this.displaySwitchUsed ) {
                addWarningToPhrase(pinValueSensor, "VALIDATION_PIN_TAKEN_BY_LED_MATRIX");
            }
        } else if ( this.occupiedPins.contains(pin) ) {
            addWarningToPhrase(pinValueSensor, "VALIDATION_PIN_TAKEN_BY_INTERNAL_COMPONENT");
        }
    }

    @Override
    public Void visitRadioSetChannelAction(RadioSetChannelAction radioSetChannelAction) {
        addToPhraseIfUnsupportedInSim(radioSetChannelAction, false, isSim);
        return super.visitRadioSetChannelAction(radioSetChannelAction);
    }

    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor accelerometerSensor) {
        addToPhraseIfUnsupportedInSim(accelerometerSensor, true, isSim);
        return super.visitAccelerometerSensor(accelerometerSensor);
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction playFileAction) {
        addToPhraseIfUnsupportedInSim(playFileAction, false, isSim);
        checkActorByTypeExists(playFileAction, "BUZZER");
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.MUSIC));
        return null;
    }

    @Override
    public Void visitSwitchLedMatrixAction(SwitchLedMatrixAction switchLedMatrixAction) {
        return null;
    }
}
