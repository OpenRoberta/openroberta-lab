package de.fhg.iais.roberta.visitor.collect;

import java.util.List;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensors.raspberrypi.SmoothedSensor;

/**
 * This visitor collects information for used actors and sensors in Blockly program.
 *
 * @author kcvejoski
 */
public final class RaspberryPiUsedHardwareCollectorVisitor extends AbstractUsedHardwareCollectorVisitor implements IRaspberryPiCollectorVisitor {

    public RaspberryPiUsedHardwareCollectorVisitor(
        List<List<Phrase<Void>>> phrasesSet,
        ConfigurationAst brickConfiguration,
        ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(brickConfiguration, beanBuilders);
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        return super.visitLightSensor(lightSensor);
    }

    @Override
    public Void visitSmoothedSensor(SmoothedSensor<Void> smoothedSensor) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedSensor(new UsedSensor(smoothedSensor.getUserDefinedPort(), SC.SMOOTHED_OUTPUT, smoothedSensor.getMode()));
        return null;
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor<Void> pinGetValueSensor) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedSensor(new UsedSensor(pinGetValueSensor.getUserDefinedPort(), SC.PIN_VALUE, pinGetValueSensor.getMode()));
        return null;
    }
}
