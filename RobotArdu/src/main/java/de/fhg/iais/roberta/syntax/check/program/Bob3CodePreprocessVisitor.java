package de.fhg.iais.roberta.syntax.check.program;

import java.util.ArrayList;

import de.fhg.iais.roberta.components.Bob3Configuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.bob3.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.bob3.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.visitor.Bob3AstVisitor;

/**
 * This visitor collects information for used actors and sensors in blockly program.
 *
 * @author VinArt
 */
public class Bob3CodePreprocessVisitor extends PreprocessProgramVisitor implements Bob3AstVisitor<Void> {

    public Bob3CodePreprocessVisitor(ArrayList<ArrayList<Phrase<Void>>> phrasesSet, Bob3Configuration configuration) {
        super(configuration);
        check(phrasesSet);
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        // TODO Auto-generated method stub
        return null;
    }
}
