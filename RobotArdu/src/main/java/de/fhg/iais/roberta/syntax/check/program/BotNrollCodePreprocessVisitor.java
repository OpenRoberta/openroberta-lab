package de.fhg.iais.roberta.syntax.check.program;

import java.util.ArrayList;

import de.fhg.iais.roberta.components.BotNrollConfiguration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.program.PreprocessProgramVisitor;
import de.fhg.iais.roberta.syntax.sensor.botnroll.VoltageSensor;
import de.fhg.iais.roberta.visitor.BotnrollAstVisitor;

/**
 * This visitor collects information for used actors and sensors in blockly program.
 *
 * @author kcvejoski
 */
public class BotNrollCodePreprocessVisitor extends PreprocessProgramVisitor implements BotnrollAstVisitor<Void> {

    public BotNrollCodePreprocessVisitor(ArrayList<ArrayList<Phrase<Void>>> phrasesSet, BotNrollConfiguration configuration) {
        super(configuration);
        check(phrasesSet);
    }

	@Override
	public Void visitVoltageSensor(VoltageSensor<Void> voltageSensor) {
		// TODO Auto-generated method stub
		return null;
	}

}
