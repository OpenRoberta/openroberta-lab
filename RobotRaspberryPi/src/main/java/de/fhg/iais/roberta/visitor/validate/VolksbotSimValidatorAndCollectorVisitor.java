package de.fhg.iais.roberta.visitor.validate;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.actors.raspberrypi.Dummy;
import de.fhg.iais.roberta.syntax.actors.raspberrypi.RotateLeft;
import de.fhg.iais.roberta.syntax.actors.raspberrypi.RotateRight;
import de.fhg.iais.roberta.syntax.actors.raspberrypi.StepBackward;
import de.fhg.iais.roberta.syntax.actors.raspberrypi.StepForward;
import de.fhg.iais.roberta.syntax.lang.blocksequence.raspberrypi.MainTaskSimple;
import de.fhg.iais.roberta.visitor.IVolksbotVisitor;

public class VolksbotSimValidatorAndCollectorVisitor extends RaspberryPiSimValidatorAndCollectorVisitor implements IVolksbotVisitor<Void> {
    public VolksbotSimValidatorAndCollectorVisitor(ConfigurationAst robotConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(robotConfiguration, beanBuilders);
    }

    @Override
    public Void visitStepForward(StepForward<Void> stepForward) {
        return null;
    }

    @Override
    public Void visitStepBackward(StepBackward<Void> stepBackward) {
        return null;
    }

    @Override
    public Void visitRotateRight(RotateRight<Void> rotateRight) {
        return null;
    }

    @Override
    public Void visitRotateLeft(RotateLeft<Void> rotateLeft) {
        return null;
    }

    @Override
    public Void visitMainTaskSimple(MainTaskSimple<Void> mainTaskSimple) {
        return null;
    }

    @Override
    public Void visitDummy(Dummy<Void> dummy) {
        return null;
    }
}