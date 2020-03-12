package de.fhg.iais.roberta.visitor.collect;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.sensor.generic.HTColorSensor;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.INxtVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable JAVA code representation of a phrase to a
 * StringBuilder. <b>This representation is correct JAVA code.</b> <br>
 */
public final class NxtUsedHardwareCollectorVisitor extends AbstractUsedHardwareCollectorVisitor implements INxtVisitor<Void> {

    public NxtUsedHardwareCollectorVisitor(
        ConfigurationAst robotConfiguration,
        ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(robotConfiguration, beanBuilders);
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedSensor(new UsedSensor(lightAction.getPort(), SC.HT_COLOR, "COLOR"));
        return super.visitLightAction(lightAction);
    }

    @Override
    public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor("", SC.SOUND));
        return super.visitVolumeAction(volumeAction);
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor("", SC.SOUND));
        return super.visitToneAction(toneAction);
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor("", SC.SOUND));
        return super.visitPlayNoteAction(playNoteAction);
    }

    @Override
    public Void visitHTColorSensor(HTColorSensor<Void> htColorSensor) {
        String mode = htColorSensor.getMode();
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedSensor(new UsedSensor(htColorSensor.getPort(), SC.HT_COLOR, mode));
        return null;
    }
}