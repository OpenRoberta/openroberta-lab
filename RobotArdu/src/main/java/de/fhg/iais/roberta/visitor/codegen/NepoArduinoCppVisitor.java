package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.Binary.Op;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.StringConst;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.lang.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastCharFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastStringFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextCharCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextJoinFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextPrintFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextStringCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.stmt.DebugAction;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractCppVisitor;

/**
 * This class is implementing {@link IVisitor} and appends a human-readable, correct C++ representation of a phrase to a StringBuilder.<br>
 * <b>The phrases covered are the NEPO parts common to all robots (if, variables, ...).</b>
 */
public abstract class NepoArduinoCppVisitor extends AbstractCppVisitor {

    protected ConfigurationAst configuration;

    protected NepoArduinoCppVisitor(List<List<Phrase>> programPhrases, ConfigurationAst configuration, ClassToInstanceMap<IProjectBean> beans) {
        super(programPhrases, beans);
        this.configuration = configuration;
    }

    protected void generateUsedVars() {
        for ( VarDeclaration var : this.getBean(UsedHardwareBean.class).getVisitedVars() ) {
            this.sb.append("___" + var.name);
            this.sb.append(" = ");
            var.value.accept(this);
            this.sb.append(";");
            nlIndent();
        }
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration var) {
        this.sb.append(getLanguageVarTypeFromBlocklyType(var.typeVar));
        this.sb.append(whitespace() + var.getCodeSafeName());
        return null;
    }

    @Override
    public Void visitBinary(Binary binary) {
        Op op = binary.op;
        if ( op == Op.MOD ) {
            appendFloatModulo(binary);
            return null;
        }
        generateSubExpr(this.sb, false, binary.left, binary);
        String sym = getBinaryOperatorSymbol(op);
        this.sb.append(whitespace() + sym + whitespace());
        if ( op == Op.TEXT_APPEND ) {
            convertToString(binary);
            return null;
        } else if ( op == Op.DIVIDE ) {
            appendCastToFloat(binary);
            return null;
        } else {
            generateSubExpr(this.sb, parenthesesCheck(binary), binary.getRight(), binary);
        }
        return null;
    }

    private void appendFloatModulo(Binary binary) {
        this.sb.append("fmod(");
        generateSubExpr(this.sb, false, binary.left, binary);
        this.sb.append(", ");
        generateSubExpr(this.sb, parenthesesCheck(binary), binary.getRight(), binary);
        this.sb.append(")");
    }

    private void appendCastToFloat(Binary binary) {
        this.sb.append("((float) ");
        generateSubExpr(this.sb, parenthesesCheck(binary), binary.getRight(), binary);
        this.sb.append(")");
    }

    private void convertToString(Binary binary) {
        switch ( binary.getRight().getVarType() ) {
            case BOOLEAN:
            case NUMBER:
            case NUMBER_INT:
            case COLOR:
                this.sb.append("String(");
                generateSubExpr(this.sb, false, binary.getRight(), binary);
                this.sb.append(")");
                break;
            default:
                generateSubExpr(this.sb, false, binary.getRight(), binary);
                break;
        }
    }

    @Override
    public Void visitRepeatStmt(RepeatStmt repeatStmt) {
        boolean isWaitStmt = repeatStmt.mode == RepeatStmt.Mode.WAIT;
        boolean isArduinoLoop = repeatStmt.mode == RepeatStmt.Mode.FOREVER_ARDU;
        switch ( repeatStmt.mode ) {
            case UNTIL:
            case WHILE:
            case FOREVER:
                increaseLoopCounter();
                generateCodeFromStmtCondition("while", repeatStmt.expr);
                break;
            case TIMES:
            case FOR:
                increaseLoopCounter();
                generateCodeFromStmtConditionFor("for", repeatStmt.expr);
                break;
            case WAIT:
                generateCodeFromStmtCondition("if", repeatStmt.expr);
                break;
            case FOR_EACH:
                increaseLoopCounter();
                generateCodeFromStmtCondition("for", repeatStmt.expr);
                break;
            case FOREVER_ARDU:
                increaseLoopCounter();
                this.sb.append("void loop()");
                nlIndent();
                this.sb.append("{");
                break;
            default:
                break;
        }
        incrIndentation();
        repeatStmt.list.accept(this);
        if ( !isWaitStmt ) {
            addContinueLabelToLoop();
            if ( !isArduinoLoop ) {
                nlIndent();
                this.sb.append("delay(1);");
            }
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
    public Void visitWaitStmt(WaitStmt waitStmt) {
        this.sb.append("while (true) {");
        incrIndentation();
        visitStmtList(waitStmt.statements);
        nlIndent();
        this.sb.append("delay(1);");
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        return null;
    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct indexOfFunct) {
        String methodName = indexOfFunct.location == IndexLocation.LAST ? "_getLastOccuranceOfElement(" : "_getFirstOccuranceOfElement(";
        this.sb.append(methodName);
        indexOfFunct.param.get(0).accept(this);
        this.sb.append(", ");
        if ( indexOfFunct.param.get(1).getClass().equals(StringConst.class) ) {
            this.sb.append("String(");
            indexOfFunct.param.get(1).accept(this);
            this.sb.append(")");
        } else {
            indexOfFunct.param.get(1).accept(this);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct textJoinFunct) {
        List<Expr> texts = textJoinFunct.param.get();
        for ( int i = 0; i < texts.size(); i++ ) {
            this.sb.append("String(");
            texts.get(i).accept(this);
            this.sb.append(")");
            if ( i < texts.size() - 1 ) {
                this.sb.append(" + ");
            }
        }
        return null;
    }

    @Override
    public Void visitTextPrintFunct(TextPrintFunct textPrintFunct) {
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt waitTimeStmt) {
        this.sb.append("delay(");
        waitTimeStmt.time.accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct mathRandomIntFunct) {
        this.sb.append("_randomIntegerInRange(");
        mathRandomIntFunct.param.get(0).accept(this);
        this.sb.append(", ");
        mathRandomIntFunct.param.get(1).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        String timerNumber = timerSensor.getUserDefinedPort();
        this.sb.append("(int) (millis() - __time_").append(timerNumber).append(")");
        return null;
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        String timerNumber = timerReset.sensorPort;
        this.sb.append("__time_").append(timerNumber).append(" = millis();");
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst colorConst) {
        this.sb.append("RGB(");
        this.sb.append(colorConst.getRedChannelHex());
        this.sb.append(", ");
        this.sb.append(colorConst.getGreenChannelHex());
        this.sb.append(", ");
        this.sb.append(colorConst.getBlueChannelHex());
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitDebugAction(DebugAction debugAction) {
        writeToSerial(debugAction.value);
        return null;
    }

    private void writeToSerial(Expr valueToWrite) {
        this.sb.append("Serial.println(");
        valueToWrite.accept(this);
        this.sb.append(");");
    }

    protected void generateTimerVariables() {
        this
            .getBean(UsedHardwareBean.class)
            .getUsedSensors()
            .stream()
            .filter(usedSensor -> usedSensor.getType().equals(SC.TIMER))
            .collect(Collectors.groupingBy(UsedSensor::getPort))
            .keySet()
            .stream()
            .forEach(port -> {
                this.sb.append("unsigned long __time_").append(port).append(" = millis();");
                nlIndent();
            });
    }

    @Override
    public Void visitMathCastStringFunct(MathCastStringFunct mathCastStringFunct) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitMathCastCharFunct(MathCastCharFunct mathCastCharFunct) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitTextStringCastNumberFunct(TextStringCastNumberFunct textStringCastNumberFunct) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitTextCharCastNumberFunct(TextCharCastNumberFunct textCharCastNumberFunct) {
        throw new DbcException("Not supported!");
    }
}
