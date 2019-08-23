package de.fhg.iais.roberta.visitor.collect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.functions.FunctionNames;
import de.fhg.iais.roberta.syntax.lang.functions.ListRepeat;
import de.fhg.iais.roberta.syntax.lang.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathPowerFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathSingleFunct;

/**
 * A visitor that keeps track of all methods visited at any point in the AST, that need an additional helper method definition.
 */
public abstract class AbstractUsedMethodCollectorVisitor implements ICollectorVisitor {

    protected final Set<FunctionNames> usedFunctions = new HashSet<>();

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
        ICollectorVisitor.super.visitMathNumPropFunct(mathNumPropFunct);
        this.usedFunctions.add(mathNumPropFunct.getFunctName());
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        ICollectorVisitor.super.visitMathOnListFunct(mathOnListFunct);
        this.usedFunctions.add(mathOnListFunct.getFunctName());
        return null;
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct<Void> mathSingleFunct) {
        ICollectorVisitor.super.visitMathSingleFunct(mathSingleFunct);
        if (mathSingleFunct.getFunctName() == FunctionNames.POW10) {
            this.usedFunctions.add(FunctionNames.POWER); // combine pow10 and power into one
        } else {
            this.usedFunctions.add(mathSingleFunct.getFunctName());
        }
        return null;
    }

    @Override
    public Void visitListRepeat(ListRepeat<Void> listRepeat) {
        ICollectorVisitor.super.visitListRepeat(listRepeat);
        this.usedFunctions.add(FunctionNames.LISTS_REPEAT);
        return null;
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct<Void> mathPowerFunct) {
        ICollectorVisitor.super.visitMathPowerFunct(mathPowerFunct);
        this.usedFunctions.add(FunctionNames.POWER);
        return null;
    }
}
