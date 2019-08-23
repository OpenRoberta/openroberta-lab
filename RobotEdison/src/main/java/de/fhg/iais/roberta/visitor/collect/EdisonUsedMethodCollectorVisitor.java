package de.fhg.iais.roberta.visitor.collect;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.functions.ListRepeat;

import java.util.ArrayList;
import java.util.List;

public class EdisonUsedMethodCollectorVisitor extends AbstractUsedMethodCollectorVisitor implements IEdisonCollectorVisitor {
    public EdisonUsedMethodCollectorVisitor(List<ArrayList<Phrase<Void>>> programPhrases) {
        super(programPhrases);
    }

    @Override
    public Void visitListRepeat(ListRepeat<Void> listRepeat) {

        return null;
    }
}
