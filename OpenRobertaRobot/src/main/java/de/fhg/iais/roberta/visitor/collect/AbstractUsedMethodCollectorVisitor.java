package de.fhg.iais.roberta.visitor.collect;

import de.fhg.iais.roberta.bean.UsedMethodBean;
import de.fhg.iais.roberta.inter.mode.general.IListElementOperations;
import de.fhg.iais.roberta.mode.general.ListElementOperations;
import de.fhg.iais.roberta.syntax.lang.functions.FunctionNames;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.ListRepeat;
import de.fhg.iais.roberta.syntax.lang.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastCharFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastStringFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathPowerFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathSingleFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextCharCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextStringCastNumberFunct;

/**
 * A visitor that keeps track of all methods visited at any point in the AST, that need an additional helper method definition.
 */
public abstract class AbstractUsedMethodCollectorVisitor implements ICollectorVisitor {

    protected UsedMethodBean.Builder builder;

    public AbstractUsedMethodCollectorVisitor(UsedMethodBean.Builder builder) {
        this.builder = builder;
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct<Void> mathNumPropFunct) {
        this.builder.addUsedMethod(mathNumPropFunct.getFunctName());
        return ICollectorVisitor.super.visitMathNumPropFunct(mathNumPropFunct);
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        this.builder.addUsedMethod(mathOnListFunct.getFunctName());
        return ICollectorVisitor.super.visitMathOnListFunct(mathOnListFunct);
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct<Void> mathSingleFunct) {
        if ( mathSingleFunct.getFunctName() == FunctionNames.POW10 ) {
            this.builder.addUsedMethod(FunctionNames.POWER); // combine pow10 and power into one
        } else {
            this.builder.addUsedMethod(mathSingleFunct.getFunctName());
        }
        return ICollectorVisitor.super.visitMathSingleFunct(mathSingleFunct);
    }

    @Override
    public Void visitListRepeat(ListRepeat<Void> listRepeat) {
        this.builder.addUsedMethod(FunctionNames.LISTS_REPEAT);
        return ICollectorVisitor.super.visitListRepeat(listRepeat);
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct<Void> mathPowerFunct) {
        this.builder.addUsedMethod(FunctionNames.POWER);
        return ICollectorVisitor.super.visitMathPowerFunct(mathPowerFunct);
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        this.builder.addUsedMethod(FunctionNames.RANDOM);
        return ICollectorVisitor.super.visitMathRandomIntFunct(mathRandomIntFunct);
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
        this.builder.addUsedMethod(FunctionNames.RANDOM_DOUBLE);
        return ICollectorVisitor.super.visitMathRandomFloatFunct(mathRandomFloatFunct);
    }

    @Override
    public Void visitMathCastStringFunct(MathCastStringFunct<Void> mathCastStringFunct) {
        this.builder.addUsedMethod(FunctionNames.CAST);
        return ICollectorVisitor.super.visitMathCastStringFunct(mathCastStringFunct);
    }

    @Override
    public Void visitMathCastCharFunct(MathCastCharFunct<Void> mathCastCharFunct) {
        this.builder.addUsedMethod(FunctionNames.CAST);
        return ICollectorVisitor.super.visitMathCastCharFunct(mathCastCharFunct);
    }

    @Override
    public Void visitTextCharCastNumberFunct(TextCharCastNumberFunct<Void> textCharCastNumberFunct) {
        this.builder.addUsedMethod(FunctionNames.CAST);
        return ICollectorVisitor.super.visitTextCharCastNumberFunct(textCharCastNumberFunct);
    }

    @Override
    public Void visitTextStringCastNumberFunct(TextStringCastNumberFunct<Void> textStringCastNumberFunct) {
        this.builder.addUsedMethod(FunctionNames.CAST);
        return ICollectorVisitor.super.visitTextStringCastNumberFunct(textStringCastNumberFunct);
    }

    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        IListElementOperations iOp = listGetIndex.getElementOperation();
        if ( iOp instanceof ListElementOperations ) {
            ListElementOperations op = (ListElementOperations) iOp;
            this.builder.addUsedMethod(op);
        }
        return ICollectorVisitor.super.visitListGetIndex(listGetIndex);
    }

    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        IListElementOperations iOp = listSetIndex.getElementOperation();
        if ( iOp instanceof ListElementOperations ) {
            ListElementOperations op = (ListElementOperations) iOp;
            this.builder.addUsedMethod(op);
        }
        return ICollectorVisitor.super.visitListSetIndex(listSetIndex);
    }
}
