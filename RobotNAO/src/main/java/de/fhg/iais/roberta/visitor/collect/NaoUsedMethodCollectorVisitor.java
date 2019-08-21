package de.fhg.iais.roberta.visitor.collect;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.syntax.Phrase;

public class NaoUsedMethodCollectorVisitor extends AbstractUsedMethodCollectorVisitor implements INaoCollectorVisitor  {
    public NaoUsedMethodCollectorVisitor(List<ArrayList<Phrase<Void>>> programPhrases) {
        super(programPhrases);
    }
}
