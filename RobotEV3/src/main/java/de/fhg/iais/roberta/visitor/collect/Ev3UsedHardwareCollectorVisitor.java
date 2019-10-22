package de.fhg.iais.roberta.visitor.collect;

import java.util.ArrayList;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.ev3.ShowPictureAction;
import de.fhg.iais.roberta.syntax.action.speech.SayTextAction;
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
        UsedHardwareBean.Builder builder,
        ArrayList<ArrayList<Phrase<Void>>> phrasesSet,
        ConfigurationAst brickConfiguration) {
        super(builder, brickConfiguration);
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        if ( !gyroSensor.getMode().equals(SC.RESET) ) {
            super.visitGyroSensor(gyroSensor);
        }
        return null;
    }

    @Override
    public Void visitSayTextAction(SayTextAction<Void> sayTextAction) {
        super.visitSayTextAction(sayTextAction);
        this.builder.setSayTextUsed(true);
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        return null;
    }

    @Override
    public Void visitShowPictureAction(ShowPictureAction<Void> showPictureAction) {
        this.builder.addUsedImage(showPictureAction.getPicture().toString());
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        String mode = infraredSensor.getMode();
        if ( infraredSensor.getMode().equals(SC.PRESENCE) ) {
            mode = SC.SEEK;
        }
        this.builder.addUsedSensor(new UsedSensor(infraredSensor.getPort(), SC.INFRARED, mode));
        return null;
    }

}
