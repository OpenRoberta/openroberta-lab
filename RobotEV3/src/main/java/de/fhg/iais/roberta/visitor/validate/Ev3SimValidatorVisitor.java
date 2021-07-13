package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.action.ev3.ShowPictureAction;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.DropSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HTColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.IRSeekerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MoistureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MotionSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ParticleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PulseSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.RfidSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VemlLightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.hardware.IEv3Visitor;

public final class Ev3SimValidatorVisitor extends AbstractSimValidatorVisitor implements IEv3Visitor<Void> {

    public Ev3SimValidatorVisitor(ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(brickConfiguration, beanBuilders);
    }

    @Override
    public Void visitIRSeekerSensor(IRSeekerSensor<Void> irSeekerSensor) {
        irSeekerSensor.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitMoistureSensor(MoistureSensor<Void> moistureSensor) {
        return super.visitMoistureSensor(moistureSensor);
    }

    @Override
    public Void visitHumiditySensor(HumiditySensor<Void> humiditySensor) {
        return super.visitHumiditySensor(humiditySensor);
    }

    @Override
    public Void visitMotionSensor(MotionSensor<Void> motionSensor) {
        return super.visitMotionSensor(motionSensor);
    }

    @Override
    public Void visitDropSensor(DropSensor<Void> dropSensor) {
        return super.visitDropSensor(dropSensor);
    }

    @Override
    public Void visitPulseSensor(PulseSensor<Void> pulseSensor) {
        return super.visitPulseSensor(pulseSensor);
    }

    @Override
    public Void visitRfidSensor(RfidSensor<Void> rfidSensor) {
        return super.visitRfidSensor(rfidSensor);
    }

    @Override
    public Void visitVemlLightSensor(VemlLightSensor<Void> vemlLightSensor) {
        return super.visitVemlLightSensor(vemlLightSensor);
    }

    @Override
    public Void visitParticleSensor(ParticleSensor<Void> particleSensor) {
        return super.visitParticleSensor(particleSensor);
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        super.visitCompassSensor(compassSensor);
        compassSensor.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitVoltageSensor(VoltageSensor<Void> voltageSensor) {
        throw new DbcException("Weg hier!");
    }

    @Override
    public Void visitGestureSensor(GestureSensor<Void> gestureSensor) {
        throw new DbcException("Weg hier!");
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor<Void> pinGetValueSensor) {
        return super.visitPinGetValueSensor(pinGetValueSensor);
    }

    @Override
    public Void visitGetSampleSensor(GetSampleSensor<Void> sensorGetSample) {
        return super.visitGetSampleSensor(sensorGetSample);
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
        soundSensor.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitShowPictureAction(ShowPictureAction<Void> showPictureAction) {
        showPictureAction.getX().accept(this);
        showPictureAction.getY().accept(this);
        return null;
    }

    @Override
    public Void visitHTColorSensor(HTColorSensor<Void> htColorSensor) {
        htColorSensor.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }
}
