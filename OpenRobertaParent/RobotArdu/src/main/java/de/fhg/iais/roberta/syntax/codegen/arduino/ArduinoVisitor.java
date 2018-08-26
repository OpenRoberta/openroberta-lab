package de.fhg.iais.roberta.syntax.codegen.arduino;

import java.util.ArrayList;
import java.util.Set;

import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedConfigurationBlock;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.sensor.TimerSensorMode;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.codegen.RobotCppVisitor;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.Binary.Op;
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.expr.MathConst;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.lang.functions.FunctionNames;
import de.fhg.iais.roberta.syntax.lang.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.lang.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.lang.functions.LengthOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathPowerFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextJoinFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextPrintFunct;
import de.fhg.iais.roberta.syntax.lang.stmt.AssignStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.DbcException;

public abstract class ArduinoVisitor extends RobotCppVisitor {

    protected Set<UsedSensor> usedSensors;
    protected Set<UsedConfigurationBlock> usedConfigurationBlocks;
    protected Set<UsedActor> usedActors;
    protected ArrayList<VarDeclaration<Void>> usedVars;

    protected ArduinoVisitor(ArrayList<ArrayList<Phrase<Void>>> programPhrases, int indentation) {
        super(programPhrases, indentation);
    }

    protected void generateUsedVars() {
        for ( VarDeclaration<Void> var : this.usedVars ) {
            if ( !var.getValue().getKind().hasName("EMPTY_EXPR") ) {
                if ( var.getTypeVar().isArray() && !var.getValue().getKind().hasName("EMPTY_EXPR") ) {
                    int size = 0;
                    if ( var.getValue().getKind().hasName("SENSOR_EXPR") ) {
                        size = 3;
                    } else {
                        ListCreate<Void> list = (ListCreate<Void>) var.getValue();
                        size = list.getValue().get().size();
                    }
                    this.sb.append("__" + var.getName() + "Len = ").append(size).append(";");
                    this.nlIndent();
                    this.sb.append(getLanguageVarTypeFromBlocklyType(var.getTypeVar())).append(" ");
                    this.sb.append("__" + var.getName()).append("[]").append(" = ");
                    var.getValue().visit(this);
                    this.sb.append(";");
                    this.nlIndent();
                }
                this.sb.append(var.getName());
                if ( var.getTypeVar().isArray() ) {
                    this.sb.append(" = (");
                    this.sb.append(getLanguageVarTypeFromBlocklyType(var.getTypeVar())).append("*)malloc(");
                    this.sb.append("sizeof(");
                    this.sb.append(getLanguageVarTypeFromBlocklyType(var.getTypeVar())).append(")*");
                    this.sb.append("__" + var.getName() + "Len").append(")").append(";");
                    this.nlIndent();
                    this.sb.append("rob.createArray(").append(var.getName()).append(", ");
                    this.sb.append("__" + var.getName() + "Len").append(", ").append("__" + var.getName());
                    this.sb.append(")");
                } else {
                    this.sb.append(" = ");
                    var.getValue().visit(this);
                }
                this.sb.append(";");
                this.nlIndent();
            } else {
                if ( var.getTypeVar().isArray() ) {
                    this.sb.append("__" + var.getName() + "Len = ").append(0);
                    this.sb.append(";");
                    this.nlIndent();
                }
            }
        }
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration<Void> var) {
        if ( var.toString().contains("false, false") ) {
            this.sb.append(getLanguageVarTypeFromBlocklyType(var.getTypeVar())).append(" ");
            this.sb.append(var.getName());
            this.sb.append(var.getTypeVar().isArray() ? "[]" : "");
        } else {
            if ( var.getTypeVar().isArray() ) {
                this.sb.append("int " + "__" + var.getName() + "Len;");
                this.nlIndent();
            }
            this.sb.append(getLanguageVarTypeFromBlocklyType(var.getTypeVar())).append(" ");
            this.sb.append(var.getTypeVar().isArray() ? "*" : "");
            this.sb.append(var.getName());
        }
        return null;
    }

    @Override
    public Void visitAssignStmt(AssignStmt<Void> assignStmt) {
        if ( assignStmt.getExpr().getKind().hasName("LIST_CREATE") && !assignStmt.getExpr().getKind().hasName("EMPTY_EXPR") ) {
            int size = 0;
            this.sb.append(getLanguageVarTypeFromBlocklyType(assignStmt.getExpr().getVarType())).append(" ");
            this.sb.append("__");
            assignStmt.getName().visit(this);
            this.sb.append(assignStmt.getProperty().getBlocklyId().replaceAll("[^A-Za-z]+", "")).append("[]").append(" = ");
            assignStmt.getExpr().visit(this);
            this.sb.append(";");
            this.nlIndent();
            if ( assignStmt.getExpr().getKind().hasName("SENSOR_EXPR") ) {
                size = 3;
            } else {
                ListCreate<Void> list = (ListCreate<Void>) assignStmt.getExpr();
                size = assignStmt.getExpr().getKind().hasName("SENSOR_EXPR") ? 3 : list.getValue().get().size();
            }
            this.sb.append("__");
            assignStmt.getName().visit(this);
            this.sb.append("Len = ").append(size).append(";");
            this.nlIndent();
        }
        assignStmt.getName().visit(this);

        this.sb.append(" = ");
        if ( !assignStmt.getExpr().getKind().hasName("EMPTY_EXPR") ) {
            if ( assignStmt.getExpr().getKind().hasName("LIST_CREATE") ) {
                this.sb.append("(").append(getLanguageVarTypeFromBlocklyType(assignStmt.getExpr().getVarType())).append("*)realloc(");
                assignStmt.getName().visit(this);
                this.sb.append(", ").append("sizeof(");
                this.sb.append(getLanguageVarTypeFromBlocklyType(assignStmt.getExpr().getVarType())).append(")*");
                this.sb.append("__");
                assignStmt.getName().visit(this);
                this.sb.append("Len);");
                this.nlIndent();
                this.sb.append("rob.createArray(");
                assignStmt.getName().visit(this);
                this.sb.append(", ");
                this.sb.append("__");
                assignStmt.getName().visit(this);
                this.sb.append("Len, ").append("__");
                assignStmt.getName().visit(this);
                this.sb.append(assignStmt.getProperty().getBlocklyId().replaceAll("[^A-Za-z]+", "")).append(")");
            } else {
                assignStmt.getExpr().visit(this);
            }
        }
        this.sb.append(";");
        return null;
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
        Op op = binary.getOp();
        if ( op == Op.MOD ) {
            appendFloatModulo(binary);
            return null;
        }
        generateSubExpr(this.sb, false, binary.getLeft(), binary);
        String sym = getBinaryOperatorSymbol(op);
        this.sb.append(whitespace() + sym + whitespace());
        if ( op == Op.TEXT_APPEND ) {
            appendCastToStringIfBoolean(binary);
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

    private void appendCastToStringIfBoolean(Binary<Void> binary) {
        if ( binary.getRight().getVarType() == BlocklyType.BOOLEAN ) {
            this.sb.append("rob.boolToString(");
            generateSubExpr(this.sb, false, binary.getRight(), binary);
            this.sb.append(")");
        } else {
            generateSubExpr(this.sb, false, binary.getRight(), binary);
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
                    this.sb.append("for(" + varType + whitespace() + element + " = 0;" + element + " < " + "__" + arr + "Len; " + element + "++) {");
                } else {
                    this.sb.append("while(false){");
                }
                break;
            case FOREVER_ARDU:
                increaseLoopCounter();
                this.sb.append("void loop()");
                this.nlIndent();
                this.sb.append("{");
                break;
            default:
                break;
        }
        incrIndentation();
        repeatStmt.getList().visit(this);
        if ( !isWaitStmt ) {
            addContinueLabelToLoop();
            if ( !isArduinoLoop ) {
                this.nlIndent();
                this.sb.append("delay(1);");
            }
        } else {
            appendBreakStmt();
        }
        decrIndentation();
        this.nlIndent();
        this.sb.append("}");
        addBreakLabelToLoop(isWaitStmt);

        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        this.sb.append("while (true) {");
        incrIndentation();
        visitStmtList(waitStmt.getStatements());
        this.nlIndent();
        this.sb.append("delay(1);");
        decrIndentation();
        this.nlIndent();
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
        this.sb.append("__" + arr.getValue() + "Len");
    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        if ( indexOfFunct.getParam().get(0).toString().contains("ListCreate ") ) {
            this.sb.append("null");
            return null;
        }
        String methodName = indexOfFunct.getLocation() == IndexLocation.LAST ? "rob.arrFindLast(" : "rob.arrFindFirst(";
        this.sb.append(methodName);
        arrayLen((Var<Void>) indexOfFunct.getParam().get(0));
        this.sb.append(", ");
        indexOfFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        indexOfFunct.getParam().get(1).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
        if ( lengthOfIsEmptyFunct.getParam().get(0).toString().contains("ListCreate ") ) {
            this.sb.append("NULL");
            return null;
        }
        if ( lengthOfIsEmptyFunct.getFunctName() == FunctionNames.LIST_IS_EMPTY ) {
            this.sb.append("(");
            arrayLen((Var<Void>) lengthOfIsEmptyFunct.getParam().get(0));
            this.sb.append(" == 0)");
        } else {
            arrayLen((Var<Void>) lengthOfIsEmptyFunct.getParam().get(0));
        }
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        if ( listGetIndex.getParam().get(0).toString().contains("ListCreate ") ) {
            this.sb.append("null");
            return null;
        }
        listGetIndex.getParam().get(0).visit(this);
        this.sb.append("[");
        switch ( (IndexLocation) listGetIndex.getLocation() ) {
            case FROM_START:
                listGetIndex.getParam().get(1).visit(this);
                break;
            case FROM_END:
                arrayLen((Var<Void>) listGetIndex.getParam().get(0));
                this.sb.append(" - 1 - ");
                listGetIndex.getParam().get(1).visit(this);
                break;
            case FIRST:
                this.sb.append("0");
                break;
            case LAST:
                arrayLen((Var<Void>) listGetIndex.getParam().get(0));
                this.sb.append(" - 1");
                break;
            case RANDOM:
                this.sb.append("rob.randomIntegerInRange(0, ");
                arrayLen((Var<Void>) listGetIndex.getParam().get(0));
                this.sb.append(")");
                break;
        }
        this.sb.append("]");
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        if ( listSetIndex.getParam().get(0).toString().contains("ListCreate ") ) {
            return null;
        }
        listSetIndex.getParam().get(0).visit(this);
        this.sb.append("[");
        switch ( (IndexLocation) listSetIndex.getLocation() ) {
            case FROM_START:
                listSetIndex.getParam().get(2).visit(this);
                break;
            case FROM_END:
                arrayLen((Var<Void>) listSetIndex.getParam().get(0));
                this.sb.append(" - 1 - ");
                listSetIndex.getParam().get(2).visit(this);
                break;
            case FIRST:
                this.sb.append("0");
                break;
            case LAST:
                arrayLen((Var<Void>) listSetIndex.getParam().get(0));
                this.sb.append(" - 1");
                break;
            case RANDOM:
                this.sb.append("rob.randomIntegerInRange(0, ");
                arrayLen((Var<Void>) listSetIndex.getParam().get(0));
                this.sb.append(")");
                break;
        }
        this.sb.append("]");
        this.sb.append(" = ");
        listSetIndex.getParam().get(1).visit(this);
        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct<Void> mathConstrainFunct) {
        this.sb.append("rob.clamp(");
        mathConstrainFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        mathConstrainFunct.getParam().get(1).visit(this);
        this.sb.append(", ");
        mathConstrainFunct.getParam().get(2).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct<Void> mathNumPropFunct) {
        switch ( mathNumPropFunct.getFunctName() ) {
            case EVEN:
                this.sb.append("(fmod(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(", 2) == 0");
                break;
            case ODD:
                this.sb.append("(fmod(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(", 2) != 0");
                break;
            case PRIME:
                this.sb.append("rob.isPrime(");
                mathNumPropFunct.getParam().get(0).visit(this);
                break;
            case WHOLE:
                this.sb.append("rob.isWhole(");
                mathNumPropFunct.getParam().get(0).visit(this);
                break;
            case POSITIVE:
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(" > 0");
                break;
            case NEGATIVE:
                this.sb.append("(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(" < 0");
                break;
            case DIVISIBLE_BY:
                this.sb.append("(fmod(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(",");
                mathNumPropFunct.getParam().get(1).visit(this);
                this.sb.append(") == 0");
                break;
            default:
                break;
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        if ( mathOnListFunct.getParam().get(0).toString().contains("ListCreate ") ) {
            this.sb.append("null");
            return null;
        }
        switch ( mathOnListFunct.getFunctName() ) {
            case SUM:
                this.sb.append("rob.arrSum(");
                break;
            case MIN:
                this.sb.append("rob.arrMin(");
                break;
            case MAX:
                this.sb.append("rob.arrMax(");
                break;
            case AVERAGE:
                this.sb.append("rob.arrMean(");
                break;
            case MEDIAN:
                this.sb.append("rob.arrMedian(");
                break;
            case STD_DEV:
                this.sb.append("rob.arrStandardDeviatioin(");
                break;
            case RANDOM:
                this.sb.append("rob.arrRand(");
                break;
            case MODE:
                this.sb.append("rob.arrMode(");
                break;
            default:
                break;
        }
        arrayLen((Var<Void>) mathOnListFunct.getParam().get(0));
        this.sb.append(", ");
        mathOnListFunct.getParam().get(0).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
        this.sb.append("rob.randomFloat()");
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        this.sb.append("rob.randomIntegerInRange(");
        mathRandomIntFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        mathRandomIntFunct.getParam().get(1).visit(this);
        this.sb.append(")");
        return null;
    }

    //@Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        switch ( (TimerSensorMode) timerSensor.getMode() ) {
            case DEFAULT:
            case VALUE:
                this.sb.append("(int) (millis() - __time)");
                break;
            case RESET:
                this.sb.append("__time = millis();");
                break;
            default:
                throw new DbcException("Invalid Time Mode!");
        }
        return null;
    }

}
