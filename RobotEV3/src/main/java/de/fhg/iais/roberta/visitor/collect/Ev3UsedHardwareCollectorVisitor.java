package de.fhg.iais.roberta.visitor.collect;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.ev3.ShowPictureAction;
import de.fhg.iais.roberta.syntax.action.speech.SayTextAction;
import de.fhg.iais.roberta.syntax.sensor.generic.HTColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.visitor.hardware.IEv3Visitor;

/**
 * This visitor collects information for used actors and sensors in blockly program.
 *
 * @author kcvejoski
 */
public final class Ev3UsedHardwareCollectorVisitor extends AbstractUsedHardwareCollectorVisitor implements IEv3Visitor<Void> {
    public Ev3UsedHardwareCollectorVisitor(
        ConfigurationAst brickConfiguration,
        ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(brickConfiguration, beanBuilders);
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        if ( !gyroSensor.getMode().equals(SC.RESET) ) { // TODO why is this necessary?
            super.visitGyroSensor(gyroSensor);
        }
        return null;
    }

    @Override
    public Void visitSayTextAction(SayTextAction<Void> sayTextAction) {
        super.visitSayTextAction(sayTextAction);
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor(SC.VOICE, SC.VOICE));
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        return null;
    }

    @Override
    public Void visitShowPictureAction(ShowPictureAction<Void> showPictureAction) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedImage(showPictureAction.getPicture().toString());
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        String mode = infraredSensor.getMode();
        if ( infraredSensor.getMode().equals(SC.PRESENCE) ) {
            mode = SC.SEEK;
        }
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedSensor(new UsedSensor(infraredSensor.getPort(), SC.INFRARED, mode));
        return null;
    }

    @Override
    public Void visitHTColorSensor(HTColorSensor<Void> htColorSensor) {
        String mode = htColorSensor.getMode();
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedSensor(new UsedSensor(htColorSensor.getPort(), SC.HT_COLOR, mode));
        return null;
    }
}
