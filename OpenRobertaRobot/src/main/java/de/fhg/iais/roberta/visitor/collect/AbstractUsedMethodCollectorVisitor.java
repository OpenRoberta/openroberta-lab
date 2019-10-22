package de.fhg.iais.roberta.visitor.collect;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.syntax.lang.functions.FunctionNames;
import de.fhg.iais.roberta.syntax.lang.functions.ListRepeat;
import de.fhg.iais.roberta.syntax.lang.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathPowerFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathSingleFunct;

/**
 * A visitor that keeps track of all methods visited at any point in the AST, that need an additional helper method definition.
 */
public abstract class AbstractUsedMethodCollectorVisitor implements ICollectorVisitor {

    protected CodeGeneratorSetupBean.Builder builder;

    public AbstractUsedMethodCollectorVisitor(CodeGeneratorSetupBean.Builder builder) {
        this.builder = builder;
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct<Void> mathNumPropFunct) {
        this.builder.addUsedFunction(mathNumPropFunct.getFunctName());
        return ICollectorVisitor.super.visitMathNumPropFunct(mathNumPropFunct);
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        this.builder.addUsedFunction(mathOnListFunct.getFunctName());
        return ICollectorVisitor.super.visitMathOnListFunct(mathOnListFunct);
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct<Void> mathSingleFunct) {
        if ( mathSingleFunct.getFunctName() == FunctionNames.POW10 ) {
            this.builder.addUsedFunction(FunctionNames.POWER); // combine pow10 and power into one
        } else {
            this.builder.addUsedFunction(mathSingleFunct.getFunctName());
        }
        return ICollectorVisitor.super.visitMathSingleFunct(mathSingleFunct);
    }

    @Override
    public Void visitListRepeat(ListRepeat<Void> listRepeat) {
        this.builder.addUsedFunction(FunctionNames.LISTS_REPEAT);
        return ICollectorVisitor.super.visitListRepeat(listRepeat);
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct<Void> mathPowerFunct) {
        this.builder.addUsedFunction(FunctionNames.POWER);
        return ICollectorVisitor.super.visitMathPowerFunct(mathPowerFunct);
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        this.builder.addUsedFunction(FunctionNames.RANDOM);
        return ICollectorVisitor.super.visitMathRandomIntFunct(mathRandomIntFunct);
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
        this.builder.addUsedFunction(FunctionNames.RANDOM_DOUBLE);
        return ICollectorVisitor.super.visitMathRandomFloatFunct(mathRandomFloatFunct);
    }
}
