package de.fhg.iais.roberta.syntax.check.hardware;

import java.util.ArrayList;

import de.fhg.iais.roberta.components.ArduConfiguration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.hardware.UsedHardwareVisitor;
import de.fhg.iais.roberta.syntax.sensor.arduino.VoltageSensor;
import de.fhg.iais.roberta.visitor.ArduAstVisitor;

/**
 * This visitor collects information for used actors and sensors in blockly program.
 *
 * @author kcvejoski
 */
public class BotNrollUsedHardwareVisitor extends UsedHardwareVisitor implements ArduAstVisitor<Void> {

    public BotNrollUsedHardwareVisitor(ArrayList<ArrayList<Phrase<Void>>> phrasesSet, ArduConfiguration configuration) {
        super(configuration);
        check(phrasesSet);
    }

    @Override
    public Void visitVoltageSensor(VoltageSensor<Void> voltageSensor) {
        return null;
    }

}
