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
import de.fhg.iais.roberta.syntax.lang.stmt.TextAppendStmt;
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
            this.src.add("___", var.name);
            this.src.add(" = ");
            var.value.accept(this);
            this.src.add(";");
            nlIndent();
        }
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration var) {
        this.src.add(getLanguageVarTypeFromBlocklyType(var.typeVar));
        this.src.add(" ", var.getCodeSafeName());
        return null;
    }

    @Override
    public Void visitBinary(Binary binary) {
        Op op = binary.op;
        if ( op == Op.MOD ) {
            appendFloatModulo(binary);
            return null;
        }
        generateSubExpr(this.src, false, binary.left, binary);
        String sym = getBinaryOperatorSymbol(op);
        this.src.add(" ", sym, " ");
        if ( op == Op.DIVIDE ) {
            appendCastToFloat(binary);
            return null;
        } else {
            generateSubExpr(this.src, parenthesesCheck(binary), binary.getRight(), binary);
        }
        return null;
    }

    private void appendFloatModulo(Binary binary) {
        this.src.add("fmod(");
        generateSubExpr(this.src, false, binary.left, binary);
        this.src.add(", ");
        generateSubExpr(this.src, parenthesesCheck(binary), binary.getRight(), binary);
        this.src.add(")");
    }

    private void appendCastToFloat(Binary binary) {
        this.src.add("((float) ");
        generateSubExpr(this.src, parenthesesCheck(binary), binary.getRight(), binary);
        this.src.add(")");
    }

    private void convertToString(Expr expr) {
        switch ( expr.getVarType() ) {
            case BOOLEAN:
            case NUMBER:
            case NUMBER_INT:
            case COLOR:
                this.src.add("String(");
                expr.accept(this);
                this.src.add(")");
                break;
            default:
                expr.accept(this);
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
                this.src.add("void loop()");
                nlIndent();
                this.src.add("{");
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
                this.src.add("delay(1);");
            }
        } else {
            appendBreakStmt();
        }
        decrIndentation();
        nlIndent();
        this.src.add("}");
        addBreakLabelToLoop(isWaitStmt);

        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt waitStmt) {
        this.src.add("while (true) {");
        incrIndentation();
        visitStmtList(waitStmt.statements);
        nlIndent();
        this.src.add("delay(1);");
        decrIndentation();
        nlIndent();
        this.src.add("}");
        return null;
    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct indexOfFunct) {
        String methodName = indexOfFunct.location == IndexLocation.LAST ? "_getLastOccuranceOfElement(" : "_getFirstOccuranceOfElement(";
        this.src.add(methodName);
        indexOfFunct.value.accept(this);
        this.src.add(", ");
        if ( indexOfFunct.find.getClass().equals(StringConst.class) ) {
            this.src.add("String(");
            indexOfFunct.find.accept(this);
            this.src.add(")");
        } else {
            indexOfFunct.find.accept(this);
        }
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct textJoinFunct) {
        List<Expr> texts = textJoinFunct.param.get();
        for ( int i = 0; i < texts.size(); i++ ) {
            this.src.add("String(");
            texts.get(i).accept(this);
            this.src.add(")");
            if ( i < texts.size() - 1 ) {
                this.src.add(" + ");
            }
        }
        return null;
    }

    @Override
    public Void visitTextPrintFunct(TextPrintFunct textPrintFunct) {
        return null;
    }

    @Override
    public Void visitTextAppendStmt(TextAppendStmt textAppendStmt) {
        textAppendStmt.var.accept(this);
        this.src.add(" += ");
        convertToString(textAppendStmt.text);
        this.src.add(";");
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt waitTimeStmt) {
        this.src.add("delay(");
        waitTimeStmt.time.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct mathRandomIntFunct) {
        this.src.add("_randomIntegerInRange(");
        mathRandomIntFunct.from.accept(this);
        this.src.add(", ");
        mathRandomIntFunct.to.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        String timerNumber = timerSensor.getUserDefinedPort();
        this.src.add("(int) (millis() - __time_", timerNumber, ")");
        return null;
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        String timerNumber = timerReset.sensorPort;
        this.src.add("__time_", timerNumber, " = millis();");
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst colorConst) {
        this.src.add("RGB(");
        this.src.add(colorConst.getRedChannelHex());
        this.src.add(", ");
        this.src.add(colorConst.getGreenChannelHex());
        this.src.add(", ");
        this.src.add(colorConst.getBlueChannelHex());
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitDebugAction(DebugAction debugAction) {
        writeToSerial(debugAction.value);
        return null;
    }

    private void writeToSerial(Expr valueToWrite) {
        this.src.add("Serial.println(");
        valueToWrite.accept(this);
        this.src.add(");");
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
                this.src.add("unsigned long __time_", port, " = millis();");
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

    protected final String transformOnOff2HighLow(String actionMode) {
        String mode = "";
        switch ( actionMode ) {
            case "OFF":
                mode = "LOW";
                break;
            case "ON":
                mode = "HIGH";
                break;
            default:
                throw new DbcException("Invalid MODE encountered in LedAction: " + actionMode);
        }
        return mode;
    }
}
