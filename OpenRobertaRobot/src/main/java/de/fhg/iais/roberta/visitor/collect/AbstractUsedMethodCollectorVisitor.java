package de.fhg.iais.roberta.visitor.collect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.functions.FunctionNames;
import de.fhg.iais.roberta.syntax.lang.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;

/**
 * A visitor that keeps track of all methods visited at any point in the AST, that need an additional helper method definition.
 */
public abstract class AbstractUsedMethodCollectorVisitor implements ICollectorVisitor {

    private final Set<FunctionNames> usedFunctions = new HashSet<>();

    public AbstractUsedMethodCollectorVisitor(List<ArrayList<Phrase<Void>>> programPhrases) {
        collect(programPhrases);
    }

    /**
     * Returns the used methods.
     *
     * @return the set of used methods
     */
    public Set<FunctionNames> getUsedFunctions() {
        return Collections.unmodifiableSet(this.usedFunctions);
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct<Void> mathNumPropFunct) {
        this.usedFunctions.add(mathNumPropFunct.getFunctName());
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        this.usedFunctions.add(mathOnListFunct.getFunctName());
        return null;
    }
}
