package de.fhg.iais.roberta.syntax.check.hardware.ev3;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.SensorType;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.mode.sensor.GyroSensorMode;
import de.fhg.iais.roberta.mode.sensor.InfraredSensorMode;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.display.ShowPictureAction;
import de.fhg.iais.roberta.syntax.action.sound.SayTextAction;
import de.fhg.iais.roberta.syntax.check.hardware.RobotUsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;

/**
 * This visitor collects information for used actors and sensors in blockly program.
 *
 * @author kcvejoski
 */
public class UsedHardwareCollectorVisitor extends RobotUsedHardwareCollectorVisitor {
    private final Set<String> usedImages = new HashSet<>();

    private boolean isSayTextUsed = false;

    public UsedHardwareCollectorVisitor(ArrayList<ArrayList<Phrase<Void>>> phrasesSet, Configuration brickConfiguration) {
        super(brickConfiguration);
        check(phrasesSet);
    }

    public Set<String> getUsedImages() {
        return this.usedImages;
    }

    public boolean isSayTextUsed() {
        return this.isSayTextUsed;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        if ( gyroSensor.getMode() != GyroSensorMode.RESET ) {
            super.visitGyroSensor(gyroSensor);
        }
        return null;
    }

    @Override
    public Void visitSayTextAction(SayTextAction<Void> sayTextAction) {
        super.visitSayTextAction(sayTextAction);
        this.isSayTextUsed = true;
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        return null;
    }

    @Override
    public Void visitShowPictureAction(ShowPictureAction<Void> showPictureAction) {
        super.visitShowPictureAction(showPictureAction);
        this.usedImages.add(showPictureAction.getPicture().toString());
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        IMode mode = infraredSensor.getMode();
        if ( infraredSensor.getMode().equals(InfraredSensorMode.PRESENCE) ) {
            mode = InfraredSensorMode.SEEK;
        }
        this.usedSensors.add(new UsedSensor((ISensorPort) infraredSensor.getPort(), SensorType.INFRARED, mode));
        return null;
    }

}
