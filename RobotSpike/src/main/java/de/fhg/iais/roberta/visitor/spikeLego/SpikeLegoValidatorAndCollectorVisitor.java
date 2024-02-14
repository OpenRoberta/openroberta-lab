package de.fhg.iais.roberta.visitor.spikeLego;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOnHiddenAction;
import de.fhg.iais.roberta.syntax.action.spike.PlayToneAction;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.visitor.AbstractSpikeValidatorAndCollectorVisitor;


public class SpikeLegoValidatorAndCollectorVisitor extends AbstractSpikeValidatorAndCollectorVisitor {
    public SpikeLegoValidatorAndCollectorVisitor(ConfigurationAst robotConfiguration, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        super(robotConfiguration, beanBuilders);
    }

    @Override
    final public Void visitRgbLedOnHiddenAction(RgbLedOnHiddenAction rgbLedOnHiddenAction) {
        super.visitRgbLedOnHiddenAction(rgbLedOnHiddenAction);
        usedMethodBuilder.addUsedMethod(SpikeLegoMethods.SETSTATUSLIGHT);
        return null;
    }

    @Override
    final public Void visitPlayToneAction(PlayToneAction playToneAction) {
        super.visitPlayToneAction(playToneAction);
        usedMethodBuilder.addUsedMethod(SpikeLegoMethods.GETMIDI);
        return null;
    }

    @Override
    final public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        super.visitUltrasonicSensor(ultrasonicSensor);
        usedMethodBuilder.addUsedMethod(SpikeLegoMethods.GETSAMPLEULTRASONIC);
        return null;
    }

}