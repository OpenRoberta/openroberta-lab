package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;

import org.json.JSONObject;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.mbed.microbitV2.SoundToggleAction;
import de.fhg.iais.roberta.syntax.action.sound.SetVolumeAction;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.microbitV2.LogoSetTouchMode;
import de.fhg.iais.roberta.syntax.sensor.mbed.microbitV2.LogoTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.mbed.microbitV2.PinSetTouchMode;
import de.fhg.iais.roberta.util.basic.C;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.IMicrobitV2Visitor;

public class MicrobitV2StackMachineVisitor extends MicrobitStackMachineVisitor implements IMicrobitV2Visitor<Void> {

    public MicrobitV2StackMachineVisitor(ConfigurationAst configuration, List<List<Phrase>> phrases) {
        super(configuration, phrases);
        Assert.isTrue(!phrases.isEmpty());

    }

    @Override
    public Void visitSoundToggleAction(SoundToggleAction soundToggleAction) {
        return null;
    }

    @Override
    public Void visitLogoTouchSensor(LogoTouchSensor logoTouchSensor) {
        String port = logoTouchSensor.getUserDefinedPort();
        String mode = logoTouchSensor.getMode();

        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.PIN + "LOGO").put(C.MODE, mode.toLowerCase());
        return add(o);
    }

    @Override
    public Void visitLogoSetTouchMode(LogoSetTouchMode logoSetTouchMode) {
        return null;
    }

    @Override
    public Void visitPinSetTouchMode(PinSetTouchMode pinSetTouchMode) {
        return null;
    }

    @Override
    public Void visitSetVolumeAction(SetVolumeAction setVolumeAction) {
        JSONObject o;
        setVolumeAction.volume.accept(this);
        o = makeNode(C.SET_VOLUME_ACTION);
        return add(o);
    }

    @Override
    public Void visitSoundSensor(SoundSensor soundSensor) {
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.SOUND).put(C.MODE, C.VOLUME).put(C.NAME, "calliope");
        return add(o);
    }
}
