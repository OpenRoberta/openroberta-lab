package de.fhg.iais.roberta.visitor.collect;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.syntax.Phrase;

public class EdisonUsedMethodCollectorVisitor extends AbstractUsedMethodCollectorVisitor implements IEdisonCollectorVisitor {
    public EdisonUsedMethodCollectorVisitor(List<ArrayList<Phrase<Void>>> programPhrases) {
        super(programPhrases);
    }
}
