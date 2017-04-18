package de.fhg.iais.roberta.syntax.check;

import java.util.ArrayList;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.nxt.LightSensorAction;
import de.fhg.iais.roberta.syntax.check.program.LoopsCounterVisitor;
import de.fhg.iais.roberta.visitor.NxtAstVisitor;

public class NxcLoopsCounterVisitor extends LoopsCounterVisitor implements NxtAstVisitor<Void> {

    public NxcLoopsCounterVisitor(ArrayList<ArrayList<Phrase<Void>>> phrasesSet) {
        super(phrasesSet);
    }

    @Override
    public Void visitLightSensorAction(LightSensorAction<Void> lightSensorAction) {
        return null;
    }
}
