package de.fhg.iais.roberta.visitor.collect;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.syntax.Phrase;

public class VorwerkUsedMethodCollectorVisitor extends AbstractUsedMethodCollectorVisitor implements IVorwerkCollectorVisitor  {
    public VorwerkUsedMethodCollectorVisitor(List<ArrayList<Phrase<Void>>> programPhrases) {
        super(programPhrases);
    }
}
