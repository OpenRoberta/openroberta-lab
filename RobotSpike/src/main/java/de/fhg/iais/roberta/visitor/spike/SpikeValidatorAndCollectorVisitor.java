package de.fhg.iais.roberta.visitor.spike;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOnHiddenAction;
import de.fhg.iais.roberta.syntax.action.spike.PlayToneAction;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.visitor.AbstractSpikeValidatorAndCollectorVisitor;


public class SpikeValidatorAndCollectorVisitor extends AbstractSpikeValidatorAndCollectorVisitor {
    public SpikeValidatorAndCollectorVisitor(ConfigurationAst robotConfiguration, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        super(robotConfiguration, beanBuilders);
    }

    @Override
    final public Void visitRgbLedOnHiddenAction(RgbLedOnHiddenAction rgbLedOnHiddenAction) {
        super.visitRgbLedOnHiddenAction(rgbLedOnHiddenAction);
        usedMethodBuilder.addUsedMethod(SpikeMethods.SETSTATUSLIGHT);
        return null;
    }

    @Override
    final public Void visitPlayToneAction(PlayToneAction playToneAction) {
        super.visitPlayToneAction(playToneAction);
        usedMethodBuilder.addUsedMethod(SpikeMethods.GETMIDI);
        return null;
    }

    @Override
    final public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        super.visitUltrasonicSensor(ultrasonicSensor);
        usedMethodBuilder.addUsedMethod(SpikeMethods.GETSAMPLEULTRASONIC);
        return null;
    }

}