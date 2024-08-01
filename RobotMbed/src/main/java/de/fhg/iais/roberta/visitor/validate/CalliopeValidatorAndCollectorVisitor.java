package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.syntax.action.mbed.DisplaySetBrightnessAction;
import de.fhg.iais.roberta.syntax.action.mbed.microbitV2.SoundToggleAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.SetVolumeAction;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.microbitV2.LogoTouchSensor;
import de.fhg.iais.roberta.util.syntax.SC;

public class CalliopeValidatorAndCollectorVisitor extends CalliopeCommonValidatorAndCollectorVisitor {
    private final boolean hasBlueTooth;
    protected final boolean isSim;

    public CalliopeValidatorAndCollectorVisitor(
        ConfigurationAst brickConfiguration,
        ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders,
        boolean isSim,
        boolean displaySwitchUsed,
        boolean hasBlueTooth) //
    {
        super(brickConfiguration, beanBuilders, isSim, displaySwitchUsed, hasBlueTooth);
        this.isSim = isSim;
        this.hasBlueTooth = hasBlueTooth;
    }

    @Override
    public Void visitLogoTouchSensor(LogoTouchSensor logoTouchSensor) {
        addErrorToPhrase(logoTouchSensor, "BLOCK_NOT_SUPPORTED");
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction playFileAction) {
        addWarningToPhrase(playFileAction, "BLOCK_NOT_SUPPORTED");
        return null;
    }

    @Override
    public Void visitSetVolumeAction(SetVolumeAction setVolumeAction) {
        addWarningToPhrase(setVolumeAction, "BLOCK_NOT_SUPPORTED");
        return super.visitSetVolumeAction(setVolumeAction);
    }

    @Override
    public Void visitSoundToggleAction(SoundToggleAction soundToggleAction) {
        addWarningToPhrase(soundToggleAction, "BLOCK_NOT_SUPPORTED");
        return super.visitSoundToggleAction(soundToggleAction);
    }

    // TODO check the following two overridings: removing overriding and adjust tests
    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        addToPhraseIfUnsupportedInSim(ultrasonicSensor, true, isSim);
        checkSensorExists(ultrasonicSensor, SC.ULTRASONIC);
        addActorMaybeCallibot(ultrasonicSensor);
        return null;
    }

    @Override
    public Void visitDisplaySetBrightnessAction(DisplaySetBrightnessAction displaySetBrightnessAction) {
        requiredComponentVisited(displaySetBrightnessAction, displaySetBrightnessAction.brightness);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY_GRAYSCALE));
        return null;
    }
}
