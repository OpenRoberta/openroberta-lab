package de.fhg.iais.roberta.visitor.collect;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.syntax.Phrase;

public class Ev3UsedMethodCollectorVisitor extends AbstractUsedMethodCollectorVisitor implements IEv3CollectorVisitor  {
    public Ev3UsedMethodCollectorVisitor(List<ArrayList<Phrase<Void>>> programPhrases) {
        super(programPhrases);
    }
}
