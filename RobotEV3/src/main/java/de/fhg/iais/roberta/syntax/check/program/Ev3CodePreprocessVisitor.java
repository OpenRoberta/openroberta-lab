package de.fhg.iais.roberta.syntax.check.program;

import java.util.ArrayList;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.mode.sensor.ev3.GyroSensorMode;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;

/**
 * This visitor collects information for used actors and sensors in blockly program.
 *
 * @author kcvejoski
 */
public class Ev3CodePreprocessVisitor extends PreprocessProgramVisitor {

    public Ev3CodePreprocessVisitor(ArrayList<ArrayList<Phrase<Void>>> phrasesSet, Configuration brickConfiguration) {
        super(brickConfiguration);
        check(phrasesSet);
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        if ( gyroSensor.getMode() != GyroSensorMode.RESET ) {
            super.visitGyroSensor(gyroSensor);
        }
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        return null;
    }

}
