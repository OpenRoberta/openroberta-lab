package de.fhg.iais.roberta.syntax.codegen;

import java.util.ArrayList;
import java.util.Set;

import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.program.LoopsCounterVisitor;
import de.fhg.iais.roberta.syntax.expr.Binary;
import de.fhg.iais.roberta.syntax.expr.Binary.Op;
import de.fhg.iais.roberta.syntax.expr.MathConst;
import de.fhg.iais.roberta.syntax.expr.Var;
import de.fhg.iais.roberta.syntax.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.functions.MathPowerFunct;
import de.fhg.iais.roberta.syntax.functions.TextJoinFunct;
import de.fhg.iais.roberta.syntax.functions.TextPrintFunct;
import de.fhg.iais.roberta.syntax.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.typecheck.BlocklyType;

public abstract class Ast2ArduVisitor extends Ast2CppVisitor {

    protected Set<UsedSensor> usedSensors;
    protected Set<UsedActor> usedActors;

    protected Ast2ArduVisitor(ArrayList<ArrayList<Phrase<Void>>> programPhrases, int indentation) {
        super(programPhrases, indentation);

        this.loopsLabels = new LoopsCounterVisitor(programPhrases).getloopsLabelContainer();
    }

    @Override
    public Void visitMathConst(MathConst<Void> mathConst) { // TODO Unify the math consts for all systems
        switch ( mathConst.getMathConst() ) {
            case PI:
                this.sb.append("PI");
                break;
            case E:
                this.sb.append("M_E");
                break;
            case GOLDEN_RATIO:
                this.sb.append("GOLDEN_RATIO");
                break;
            case SQRT2:
                this.sb.append("M_SQRT2");
                break;
            case SQRT1_2:
                this.sb.append("M_SQRT1_2");
                break;
            // IEEE 754 floating point representation
            case INFINITY:
                this.sb.append("INFINITY");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitBinary(Binary<Void> binary) {
        generateSubExpr(this.sb, false, binary.getLeft(), binary);
        Op op = binary.getOp();
        String sym = getBinaryOperatorSymbol(op);
        this.sb.append(whitespace() + sym + whitespace());
        switch ( op ) {
            case TEXT_APPEND:
                if ( binary.getRight().getVarType() == BlocklyType.BOOLEAN ) {
                    this.sb.append("rob.boolToString(");
                    generateSubExpr(this.sb, false, binary.getRight(), binary);
                    this.sb.append(")");
                } else {
                    generateSubExpr(this.sb, false, binary.getRight(), binary);
                }
                break;
            case DIVIDE:
                this.sb.append("((float) ");
                generateSubExpr(this.sb, parenthesesCheck(binary), binary.getRight(), binary);
                this.sb.append(")");
                break;
            default:
                generateSubExpr(this.sb, parenthesesCheck(binary), binary.getRight(), binary);
        }
        return null;
    }

    @Override
    public Void visitRepeatStmt(RepeatStmt<Void> repeatStmt) {
        boolean isWaitStmt = repeatStmt.getMode() == RepeatStmt.Mode.WAIT || repeatStmt.getMode() == RepeatStmt.Mode.FOREVER_ARDU;
        switch ( repeatStmt.getMode() ) {
            case UNTIL:
            case WHILE:
            case FOREVER:
                increaseLoopCounter();
                generateCodeFromStmtCondition("while", repeatStmt.getExpr());
                break;
            case TIMES:
            case FOR:
                increaseLoopCounter();
                generateCodeFromStmtConditionFor("for", repeatStmt.getExpr());
                break;
            case WAIT:
                generateCodeFromStmtCondition("if", repeatStmt.getExpr());
                break;
            case FOR_EACH:
                increaseLoopCounter();
                String varType;
                String expression = repeatStmt.getExpr().toString();
                String segments[] = expression.split(",");
                String element = segments[2];
                String arr = null;
                if ( expression.contains("NUMBER") ) {
                    varType = "double";
                } else if ( expression.contains("BOOLEAN") ) {
                    varType = "bool";
                } else {
                    varType = "String";
                }
                if ( !segments[6].contains("java.util") ) {
                    arr = segments[6].substring(segments[6].indexOf("[") + 1, segments[6].indexOf("]"));
                    this.sb.append(
                        "for("
                            + varType
                            + whitespace()
                            + element
                            + " = 0;"
                            + element
                            + " < sizeof("
                            + arr
                            + "Raw"
                            + ") / sizeof("
                            + arr
                            + "Raw"
                            + "[0]); "
                            + element
                            + "++) {");
                } else {
                    this.sb.append("while(false){");
                }
                break;
            case FOREVER_ARDU:
                repeatStmt.getList().visit(this);
                return null;
            default:
                break;
        }
        incrIndentation();
        repeatStmt.getList().visit(this);
        if ( !isWaitStmt ) {
            addContinueLabelToLoop();
        } else {
            appendBreakStmt();
        }
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        addBreakLabelToLoop(isWaitStmt);

        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        this.sb.append("while (true) {");
        incrIndentation();
        visitStmtList(waitStmt.getStatements());
        nlIndent();
        this.sb.append("delay(15);");
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        return null;
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct<Void> mathPowerFunct) {
        this.sb.append("pow(");
        super.visitMathPowerFunct(mathPowerFunct);
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct<Void> textJoinFunct) {
        return null;
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct<Void> getSubFunct) {
        return null;
    }

    @Override
    public Void visitTextPrintFunct(TextPrintFunct<Void> textPrintFunct) {
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        this.sb.append("delay(");
        waitTimeStmt.getTime().visit(this);
        this.sb.append(");");
        return null;
    }

    protected void arrayLen(Var<Void> arr) {
        this.sb.append("sizeof(" + arr.getValue() + "Raw" + ")/sizeof(" + arr.getValue() + "Raw" + "[0])");
    }

}
