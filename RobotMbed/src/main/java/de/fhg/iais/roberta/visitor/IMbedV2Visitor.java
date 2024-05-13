package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.syntax.action.mbed.microbitV2.SoundToggleAction;
import de.fhg.iais.roberta.syntax.action.sound.SetVolumeAction;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.microbitV2.LogoSetTouchMode;
import de.fhg.iais.roberta.syntax.sensor.mbed.microbitV2.LogoTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.microbitV2.PinSetTouchMode;

public interface IMbedV2Visitor<V> extends IMbedVisitor<V> {

    V visitSoundToggleAction(SoundToggleAction soundToggleAction);

    V visitSetVolumeAction(SetVolumeAction setVolumeAction);

    V visitSoundSensor(SoundSensor soundSensor);

    V visitLogoTouchSensor(LogoTouchSensor logoTouchSensor);

    V visitLogoSetTouchMode(LogoSetTouchMode logoSetTouchMode);

    V visitPinSetTouchMode(PinSetTouchMode pinSetTouchMode);
}
