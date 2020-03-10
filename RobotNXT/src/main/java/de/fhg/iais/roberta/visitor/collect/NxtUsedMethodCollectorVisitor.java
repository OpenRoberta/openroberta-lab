package de.fhg.iais.roberta.visitor.collect;

import de.fhg.iais.roberta.bean.UsedMethodBean;
import de.fhg.iais.roberta.syntax.lang.functions.FunctionNames;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathSingleFunct;

public class NxtUsedMethodCollectorVisitor extends AbstractUsedMethodCollectorVisitor implements INxtCollectorVisitor {
    public NxtUsedMethodCollectorVisitor(UsedMethodBean.Builder builder) {
        super(builder);
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        switch ( mathOnListFunct.getFunctName() ) {
            case STD_DEV:
                this.builder.addUsedMethod(FunctionNames.POWER);
                this.builder.addUsedMethod(FunctionNames.STD_DEV);
                break;
            default:
                break; // no action necessary
        }
        return super.visitMathOnListFunct(mathOnListFunct);
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct<Void> mathSingleFunct) {
        switch ( mathSingleFunct.getFunctName() ) {
            case ACOS:
                this.builder.addUsedMethod(FunctionNames.ASIN);
                this.builder.addUsedMethod(FunctionNames.POWER);
                this.builder.addUsedMethod(NxtMethods.FACTORIAL);
                break;
            case ASIN:
            case COS:
            case SIN:
                this.builder.addUsedMethod(FunctionNames.POWER);
                this.builder.addUsedMethod(NxtMethods.FACTORIAL);
                break;
            case ATAN:
            case EXP:
            case LN:
                this.builder.addUsedMethod(FunctionNames.POWER);
                break;
            case LOG10:
                this.builder.addUsedMethod(FunctionNames.LN);
                this.builder.addUsedMethod(FunctionNames.POWER);
                break;
            case ROUND:
            case ROUNDUP:
                this.builder.addUsedMethod(FunctionNames.ROUNDDOWN);
                break;
            case TAN:
                this.builder.addUsedMethod(FunctionNames.SIN);
                this.builder.addUsedMethod(FunctionNames.COS);
                this.builder.addUsedMethod(FunctionNames.POWER);
                this.builder.addUsedMethod(NxtMethods.FACTORIAL);
                break;
            default:
                break; // no action necessary
        }
        return super.visitMathSingleFunct(mathSingleFunct);
    }
}
