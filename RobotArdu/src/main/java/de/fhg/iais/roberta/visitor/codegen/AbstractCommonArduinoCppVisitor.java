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
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
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
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractCppVisitor;

public abstract class AbstractCommonArduinoCppVisitor extends AbstractCppVisitor {

    protected ConfigurationAst configuration;

    protected AbstractCommonArduinoCppVisitor(List<List<Phrase<Void>>> programPhrases, ConfigurationAst configuration, ClassToInstanceMap<IProjectBean> beans) {
        super(programPhrases, beans);
        this.configuration = configuration;
    }

    protected void generateUsedVars() {
        for ( VarDeclaration<Void> var : this.getBean(UsedHardwareBean.class).getVisitedVars() ) {
            this.sb.append("___" + var.getName());
            this.sb.append(" = ");
            var.getValue().accept(this);
            this.sb.append(";");
            nlIndent();
        }
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration<Void> var) {
        this.sb.append(getLanguageVarTypeFromBlocklyType(var.getTypeVar()));
        this.sb.append(whitespace() + var.getCodeSafeName());
        return null;
    }

    @Override
    public Void visitBinary(Binary<Void> binary) {
        Op op = binary.getOp();
        if ( op == Op.MOD ) {
            appendFloatModulo(binary);
            return null;
        }
        generateSubExpr(this.sb, false, binary.getLeft(), binary);
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

    private void appendFloatModulo(Binary<Void> binary) {
        this.sb.append("fmod(");
        generateSubExpr(this.sb, false, binary.getLeft(), binary);
        this.sb.append(", ");
        generateSubExpr(this.sb, parenthesesCheck(binary), binary.getRight(), binary);
        this.sb.append(")");
    }

    private void appendCastToFloat(Binary<Void> binary) {
        this.sb.append("((float) ");
        generateSubExpr(this.sb, parenthesesCheck(binary), binary.getRight(), binary);
        this.sb.append(")");
    }

    private void convertToString(Binary<Void> binary) {
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
    public Void visitRepeatStmt(RepeatStmt<Void> repeatStmt) {
        boolean isWaitStmt = repeatStmt.getMode() == RepeatStmt.Mode.WAIT;
        boolean isArduinoLoop = repeatStmt.getMode() == RepeatStmt.Mode.FOREVER_ARDU;
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
                generateCodeFromStmtCondition("for", repeatStmt.getExpr());
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
        repeatStmt.getList().accept(this);
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
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        this.sb.append("while (true) {");
        incrIndentation();
        visitStmtList(waitStmt.getStatements());
        nlIndent();
        this.sb.append("delay(1);");
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        return null;
    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        String methodName = indexOfFunct.getLocation() == IndexLocation.LAST ? "_getLastOccuranceOfElement(" : "_getFirstOccuranceOfElement(";
        this.sb.append(methodName);
        indexOfFunct.getParam().get(0).accept(this);
        this.sb.append(", ");
        if ( indexOfFunct.getParam().get(1).getClass().equals(StringConst.class) ) {
            this.sb.append("String(");
            indexOfFunct.getParam().get(1).accept(this);
            this.sb.append(")");
        } else {
            indexOfFunct.getParam().get(1).accept(this);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct<Void> textJoinFunct) {
        return null;
    }

    @Override
    public Void visitTextPrintFunct(TextPrintFunct<Void> textPrintFunct) {
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        this.sb.append("delay(");
        waitTimeStmt.getTime().accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        this.sb.append("_randomIntegerInRange(");
        mathRandomIntFunct.getParam().get(0).accept(this);
        this.sb.append(", ");
        mathRandomIntFunct.getParam().get(1).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        String timerNumber = timerSensor.getPort();
        switch ( timerSensor.getMode() ) {
            case SC.DEFAULT:
            case SC.VALUE:
                this.sb.append("(int) (millis() - __time_").append(timerNumber).append(")");
                break;
            case SC.RESET:
                this.sb.append("__time_").append(timerNumber).append(" = millis();");
                break;
            default:
                throw new DbcException("Invalid Time Mode!");
        }
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst<Void> colorConst) {
        this.sb.append("RGB(");
        this.sb.append(colorConst.getRedChannelHex());
        this.sb.append(", ");
        this.sb.append(colorConst.getGreenChannelHex());
        this.sb.append(", ");
        this.sb.append(colorConst.getBlueChannelHex());
        this.sb.append(")");
        return null;
    }

    public Void visitSerialWriteAction(SerialWriteAction<Void> serialWriteAction) {
        writeToSerial(serialWriteAction.getValue());
        return null;
    }

    @Override
    public Void visitDebugAction(DebugAction<Void> debugAction) {
        writeToSerial(debugAction.getValue());
        return null;
    }

    private void writeToSerial(Expr<Void> valueToWrite) {
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
    public Void visitMathCastStringFunct(MathCastStringFunct<Void> mathCastStringFunct) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitMathCastCharFunct(MathCastCharFunct<Void> mathCastCharFunct) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitTextStringCastNumberFunct(TextStringCastNumberFunct<Void> textStringCastNumberFunct) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitTextCharCastNumberFunct(TextCharCastNumberFunct<Void> textCharCastNumberFunct) {
        throw new DbcException("Not supported!");
    }
}
