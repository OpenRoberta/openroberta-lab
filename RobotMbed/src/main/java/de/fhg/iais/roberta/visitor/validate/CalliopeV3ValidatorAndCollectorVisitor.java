package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.mbed.microbitV2.SoundToggleAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.SetVolumeAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.sensor.mbed.microbitV2.LogoSetTouchMode;
import de.fhg.iais.roberta.syntax.sensor.mbed.microbitV2.LogoTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.microbitV2.PinSetTouchMode;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.ICalliopeV3Visitor;

public class CalliopeV3ValidatorAndCollectorVisitor extends CalliopeValidatorAndCollectorVisitor implements ICalliopeV3Visitor<Void> {

    private final boolean isSim;


    public CalliopeV3ValidatorAndCollectorVisitor(
        ConfigurationAst brickConfiguration,
        ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders,
        boolean isSim,
        boolean hasBlueTooth) {
        super(brickConfiguration, beanBuilders, isSim, hasBlueTooth);
        this.isSim = isSim;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction playFileAction) {
        addToPhraseIfUnsupportedInSim(playFileAction, false, isSim);
        checkActorByTypeExists(playFileAction, "BUZZER");
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.MUSIC));
        return null;
    }


    @Override
    public Void visitSoundToggleAction(SoundToggleAction soundToggleAction) {
        addToPhraseIfUnsupportedInSim(soundToggleAction, false, isSim);
        ConfigurationComponent usedConfigurationBlock = this.robotConfiguration.optConfigurationComponent(soundToggleAction.hide.getValue());
        if ( usedConfigurationBlock == null ) {
            Phrase actionAsPhrase = soundToggleAction;
            addErrorToPhrase(actionAsPhrase, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.BUZZER));
        return null;
    }

    @Override
    public Void visitSetVolumeAction(SetVolumeAction setVolumeAction) {
        ConfigurationComponent usedConfigurationBlock = this.robotConfiguration.optConfigurationComponent(setVolumeAction.hide.getValue());
        if ( usedConfigurationBlock == null ) {
            Phrase actionAsPhrase = setVolumeAction;
            addErrorToPhrase(actionAsPhrase, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        requiredComponentVisited(setVolumeAction, setVolumeAction.volume);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.BUZZER));
        return null;
    }

    @Override
    public Void visitLogoTouchSensor(LogoTouchSensor logoTouchSensor) {
        checkSensorExists(logoTouchSensor);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(logoTouchSensor.getUserDefinedPort(), "LOGOTOUCH", logoTouchSensor.getSlot()));

        return null;
    }

    @Override
    public Void visitLogoSetTouchMode(LogoSetTouchMode logoSetTouchMode) {

        ConfigurationComponent usedConfigurationBlock = this.robotConfiguration.optConfigurationComponent(logoSetTouchMode.getUserDefinedPort());
        if ( usedConfigurationBlock == null ) {
            Phrase actionAsPhrase = logoSetTouchMode;
            addErrorToPhrase(actionAsPhrase, "CONFIGURATION_ERROR_SENSOR_MISSING");
        }
        usedHardwareBuilder.addUsedSensor(new UsedSensor("LOGOTOUCH", "LOGOTOUCH", logoSetTouchMode.mode));
        return null;
    }


    @Override
    public Void visitPinSetTouchMode(PinSetTouchMode pinSetTouchMode) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(pinSetTouchMode.sensorport, "PIN", pinSetTouchMode.mode));
        return null;
    }
}
