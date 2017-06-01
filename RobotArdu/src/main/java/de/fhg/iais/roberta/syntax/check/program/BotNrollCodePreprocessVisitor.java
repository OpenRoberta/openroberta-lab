package de.fhg.iais.roberta.syntax.check.program;

import java.util.ArrayList;

import de.fhg.iais.roberta.components.ArduConfiguration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.program.PreprocessProgramVisitor;
import de.fhg.iais.roberta.syntax.sensor.arduino.VoltageSensor;
import de.fhg.iais.roberta.visitor.ArduAstVisitor;

/**
 * This visitor collects information for used actors and sensors in blockly program.
 *
 * @author kcvejoski
 */
public class BotNrollCodePreprocessVisitor extends PreprocessProgramVisitor implements ArduAstVisitor<Void> {

    public BotNrollCodePreprocessVisitor(ArrayList<ArrayList<Phrase<Void>>> phrasesSet, ArduConfiguration configuration) {
        super(configuration);
        check(phrasesSet);
    }

    @Override
    public Void visitVoltageSensor(VoltageSensor<Void> voltageSensor) {
        return null;
    }

}
