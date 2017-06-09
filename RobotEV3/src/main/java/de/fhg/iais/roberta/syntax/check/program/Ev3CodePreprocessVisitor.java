package de.fhg.iais.roberta.syntax.check.program;

import java.util.ArrayList;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.program.PreprocessProgramVisitor;

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

}
