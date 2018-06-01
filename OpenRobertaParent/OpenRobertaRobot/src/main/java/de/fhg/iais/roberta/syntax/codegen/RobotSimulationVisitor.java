package de.fhg.iais.roberta.syntax.codegen;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.sensor.TimerSensorMode;
import de.fhg.iais.roberta.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothCheckConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothWaitForConnectionAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.ActivityTask;
import de.fhg.iais.roberta.syntax.lang.blocksequence.Location;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.blocksequence.StartActivityTask;
import de.fhg.iais.roberta.syntax.lang.expr.ActionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.BoolConst;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyList;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.expr.MathConst;
import de.fhg.iais.roberta.syntax.lang.expr.MethodExpr;
import de.fhg.iais.roberta.syntax.lang.expr.NullConst;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.expr.SensorExpr;
import de.fhg.iais.roberta.syntax.lang.expr.ShadowExpr;
import de.fhg.iais.roberta.syntax.lang.expr.StmtExpr;
import de.fhg.iais.roberta.syntax.lang.expr.StringConst;
import de.fhg.iais.roberta.syntax.lang.expr.Unary;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.lang.functions.FunctionNames;
import de.fhg.iais.roberta.syntax.lang.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.lang.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.lang.functions.LengthOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.ListRepeat;
import de.fhg.iais.roberta.syntax.lang.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathPowerFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathSingleFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextJoinFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextPrintFunct;
import de.fhg.iais.roberta.syntax.lang.methods.MethodCall;
import de.fhg.iais.roberta.syntax.lang.methods.MethodIfReturn;
import de.fhg.iais.roberta.syntax.lang.methods.MethodReturn;
import de.fhg.iais.roberta.syntax.lang.methods.MethodVoid;
import de.fhg.iais.roberta.syntax.lang.stmt.ActionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.AssignStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.ExprStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.FunctionStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.MethodStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt.Mode;
import de.fhg.iais.roberta.syntax.lang.stmt.SensorStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtTextComment;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.actor.AstActorCommunicationVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorDisplayVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorLightVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorMotorVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorSoundVisitor;
import de.fhg.iais.roberta.visitor.lang.AstLanguageVisitor;
import de.fhg.iais.roberta.visitor.sensor.AstSensorsVisitor;

public abstract class RobotSimulationVisitor<V> implements AstLanguageVisitor<V>, AstSensorsVisitor<V>, AstActorCommunicationVisitor<V>,
    AstActorDisplayVisitor<V>, AstActorMotorVisitor<V>, AstActorLightVisitor<V>, AstActorSoundVisitor<V> {
    protected int loopsCounter = 0;
    protected int currentLoop = 0;
    protected int stmtsNumber = 0;
    protected int methodsNumber = 0;
    private final ArrayList<Boolean> inStmt = new ArrayList<>();

    protected final StringBuilder sb = new StringBuilder();
    protected final Configuration brickConfiguration;

    protected RobotSimulationVisitor(Configuration brickConfiguration) {
        this.brickConfiguration = brickConfiguration;
    }

    @Override
    public V visitNumConst(NumConst<V> numConst) {
        this.sb.append("createConstant(CONST." + numConst.getKind().getName() + ", " + numConst.getValue() + ")");
        return null;
    }

    @Override
    public V visitMathConst(MathConst<V> mathConst) {
        this.sb.append("createMathConstant('" + mathConst.getMathConst() + "')");
        return null;
    }

    @Override
    public V visitBoolConst(BoolConst<V> boolConst) {
        this.sb.append("createConstant(CONST." + boolConst.getKind().getName() + ", " + boolConst.isValue() + ")");
        return null;
    }

    @Override
    public V visitStringConst(StringConst<V> stringConst) {
        this.sb.append(
            "createConstant(CONST."
                + stringConst.getKind().getName()
                + ", '"
                + StringEscapeUtils.escapeEcmaScript(stringConst.getValue().replaceAll("[<>\\$]", ""))
                + "')");
        return null;
    }

    @Override
    public V visitNullConst(NullConst<V> nullConst) {
        this.sb.append("createConstant(CONST." + nullConst.getKind().getName() + ", undefined)");
        return null;
    }

    @Override
    public V visitColorConst(ColorConst<V> colorConst) {
        this.sb.append("createConstant(CONST." + colorConst.getKind().getName() + ", CONST.COLOR_ENUM." + colorConst.getValue() + ")");
        return null;
    }

    @Override
    public V visitRgbColor(RgbColor<V> rgbColor) {
        this.sb.append("createRgbColor([");
        rgbColor.getR().visit(this);
        this.sb.append(", ");
        rgbColor.getG().visit(this);
        this.sb.append(", ");
        rgbColor.getB().visit(this);
        this.sb.append("])");
        return null;
    }

    @Override
    public V visitShadowExpr(ShadowExpr<V> shadowExpr) {
        if ( shadowExpr.getBlock() != null ) {
            shadowExpr.getBlock().visit(this);
        } else {
            shadowExpr.getShadow().visit(this);
        }
        return null;
    }

    @Override
    public V visitVar(Var<V> var) {
        this.sb.append("createVarReference(CONST." + var.getVarType() + ", \"" + var.getValue() + "\")");
        return null;
    }

    @Override
    public V visitVarDeclaration(VarDeclaration<V> var) {
        this.sb.append("createVarDeclaration(CONST." + var.getTypeVar() + ", \"" + var.getName() + "\", ");
        if ( var.getValue().getKind().hasName("EXPR_LIST") ) {
            ExprList<V> list = (ExprList<V>) var.getValue();
            if ( list.get().size() == 2 ) {
                list.get().get(1).visit(this);
            } else {
                list.get().get(0).visit(this);
            }
        } else {
            var.getValue().visit(this);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public V visitUnary(Unary<V> unary) {
        this.sb.append("createUnaryExpr(CONST." + unary.getOp() + ", ");
        unary.getExpr().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public V visitBinary(Binary<V> binary) {
        String method = "createBinaryExpr(CONST." + binary.getOp() + ", ";
        String end = ")";
        // FIXME: The math change should be removed from the binary expression since it is a statement
        switch ( binary.getOp() ) {
            case MATH_CHANGE:
                method = "createMathChange(";
                //                end = createClosingBracket();
                break;
            case TEXT_APPEND:
                method = "createTextAppend(";
                end = createClosingBracket();
                break;
            default:
                break;
        }
        this.sb.append(method);
        binary.getLeft().visit(this);
        this.sb.append(", ");
        binary.getRight().visit(this);
        this.sb.append(end);
        return null;
    }

    @Override
    public V visitToneAction(ToneAction<V> toneAction) {
        String end = createClosingBracket();
        this.sb.append("createToneAction(");
        toneAction.getFrequency().visit(this);
        this.sb.append(", ");
        toneAction.getDuration().visit(this);
        this.sb.append(end);
        return null;
    }

    @Override
    public V visitPlayNoteAction(PlayNoteAction<V> playNoteAction) {
        String end = createClosingBracket();
        this.sb.append("createToneAction(");
        this.sb.append("createConstant(CONST.NUM_CONST, " + playNoteAction.getFrequency() + ")");
        this.sb.append(", ");
        this.sb.append("createConstant(CONST.NUM_CONST, " + playNoteAction.getDuration() + ")");
        this.sb.append(end);
        return null;
    }

    @Override
    public V visitMathPowerFunct(MathPowerFunct<V> mathPowerFunct) {
        this.sb.append("createBinaryExpr(CONST." + mathPowerFunct.getFunctName() + ", ");
        mathPowerFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        mathPowerFunct.getParam().get(1).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public V visitActionExpr(ActionExpr<V> actionExpr) {
        actionExpr.getAction().visit(this);
        return null;
    }

    @Override
    public V visitSensorExpr(SensorExpr<V> sensorExpr) {
        sensorExpr.getSens().visit(this);
        return null;
    }

    @Override
    public V visitMethodExpr(MethodExpr<V> methodExpr) {
        methodExpr.getMethod().visit(this);
        return null;
    }

    @Override
    public V visitEmptyList(EmptyList<V> emptyList) {
        return null;
    }

    @Override
    public V visitEmptyExpr(EmptyExpr<V> emptyExpr) {
        switch ( emptyExpr.getDefVal() ) {
            case STRING:
                this.sb.append("createConstant(CONST.STRING_CONST, '')");
                break;
            case BOOLEAN:
                this.sb.append("createConstant(CONST.BOOL_CONST, true)");
                break;
            case NUMBER_INT:
            case NUMBER:
                this.sb.append("createConstant(CONST.NUM_CONST, 0)");
                break;
            case COLOR:
                this.sb.append("createConstant(CONST.LED_COLOR_CONST, [153, 153, 153])");
                break;
            case ARRAY:
                this.sb.append("[]");
                break;
            case ARRAY_NUMBER:
                this.sb.append("createConstant(CONST.ARRAY_NUMBER, [createConstant(CONST.NUM_CONST, 0)])");
                break;
            case ARRAY_BOOLEAN:
                this.sb.append("createConstant(CONST.ARRAY_BOOLEAN, [createConstant(CONST.BOOLEAN, false)])");
                break;
            case ARRAY_CONNECTION:
                this.sb.append("createConstant(CONST.ARRAY_CONNECTION, [createConstant(CONST.CONNECTION, false)])");
                break;
            case ARRAY_STRING:
                this.sb.append("createConstant(CONST.ARRAY_STRING, [createConstant(CONST.STRING, '')])");
                break;
            case ARRAY_COLOUR:
                this.sb.append("createConstant(CONST.ARRAY_COLOUR, [createConstant(CONST.COLOUR, '')])");
                break;
            case ARRAY_IMAGE:
                this.sb
                    .append("createConstant(CONST.ARRAY_IMAGE, [createConstant(CONST.IMAGE, [[0,0,0,0,0],[0,0,0,0,0],[0,0,0,0,0],[0,0,0,0,0],[0,0,0,0,0]])])");
                break;
            case NULL:
                this.sb.append("createConstant(CONST.NULL_CONST, null)");
                break;
            case IMAGE:
                this.sb.append("createConstant(CONST.IMAGE, [[0,0,0,0,0],[0,0,0,0,0],[0,0,0,0,0],[0,0,0,0,0],[0,0,0,0,0]])");
                break;
            default:
                this.sb.append("[[EmptyExpr [defVal=" + emptyExpr.getDefVal() + "]]]");
                break;
        }
        return null;
    }

    @Override
    public V visitExprList(ExprList<V> exprList) {
        boolean first = true;
        for ( Expr<V> expr : exprList.get() ) {
            if ( !expr.getKind().hasName("EMPTY_EXPR") ) {
                if ( first ) {
                    first = false;
                } else {
                    this.sb.append(", ");
                }
                expr.visit(this);
            }
        }
        return null;
    }

    @Override
    public V visitStmtExpr(StmtExpr<V> stmtExpr) {
        stmtExpr.getStmt().visit(this);
        return null;
    }

    @Override
    public V visitActionStmt(ActionStmt<V> actionStmt) {
        actionStmt.getAction().visit(this);
        return null;
    }

    @Override
    public V visitAssignStmt(AssignStmt<V> assignStmt) {
        String end = createClosingBracket();
        this.sb.append("createAssignStmt(\"" + assignStmt.getName().getValue());
        this.sb.append("\", ");
        assignStmt.getExpr().visit(this);
        this.sb.append(end);
        return null;
    }

    @Override
    public V visitExprStmt(ExprStmt<V> exprStmt) {
        String end = "";
        if ( !isInStmt() ) {
            this.sb.append("var stmt" + this.stmtsNumber + " = ");
            increaseStmt();
            end = ";";
        }
        exprStmt.getExpr().visit(this);
        this.sb.append(end);

        return null;
    }

    @Override
    public V visitIfStmt(IfStmt<V> ifStmt) {
        if ( ifStmt.isTernary() ) {
            this.sb.append("createTernaryExpr(");
            ifStmt.getExpr().get(0).visit(this);
            this.sb.append(", ");
            ((ExprStmt<V>) ifStmt.getThenList().get(0).get().get(0)).getExpr().visit(this);
            this.sb.append(", ");
            ((ExprStmt<V>) ifStmt.getElseList().get().get(0)).getExpr().visit(this);
            this.sb.append(")");
        } else {
            String end = createClosingBracket();
            this.sb.append("createIfStmt([");
            appendIfStmtConditions(ifStmt);
            this.sb.append("], [");
            appendThenStmts(ifStmt);
            this.sb.append("], [");
            appendElseStmt(ifStmt);
            this.sb.append("]");

            this.sb.append(end);
        }
        return null;
    }

    @Override
    public V visitRepeatStmt(RepeatStmt<V> repeatStmt) {
        increaseLoopCounter(repeatStmt);
        String end = createClosingBracket();
        appendRepeatStmtCondition(repeatStmt);
        addInStmt();
        appendRepeatStmtStatements(repeatStmt);
        this.sb.append("]");
        this.sb.append(end);
        removeInStmt();
        exitLoop(repeatStmt);
        return null;
    }

    private void increaseLoopCounter(RepeatStmt<V> repeatStmt) {
        if ( repeatStmt.getMode() != RepeatStmt.Mode.WAIT ) {
            this.loopsCounter++;
            this.currentLoop = this.loopsCounter;
        }
    }

    private void exitLoop(RepeatStmt<V> repeatStmt) {
        if ( repeatStmt.getMode() != RepeatStmt.Mode.WAIT ) {
            this.currentLoop--;
        }
    }

    @Override
    public V visitSensorStmt(SensorStmt<V> sensorStmt) {
        sensorStmt.getSensor().visit(this);
        return null;
    }

    @Override
    public V visitStmtFlowCon(StmtFlowCon<V> stmtFlowCon) {
        String end = createClosingBracket();
        this.sb.append("createStmtFlowControl('loop_" + this.currentLoop + "', CONST." + stmtFlowCon.getFlow());
        this.sb.append(end);
        return null;
    }

    @Override
    public V visitStmtList(StmtList<V> stmtList) {
        if ( stmtList.get().size() == 0 ) {
            return null;
        }
        String symbol = isInStmt() ? ", " : "\n";
        for ( int i = 0; i < stmtList.get().size(); i++ ) {
            stmtList.get().get(i).visit(this);
            this.sb.append(symbol);
        }
        removeLastComma();
        return null;
    }

    @Override
    public V visitTimerSensor(TimerSensor<V> timerSensor) {
        switch ( (TimerSensorMode) timerSensor.getMode() ) {
            case DEFAULT:
            case VALUE:
                this.sb.append("createGetSample(CONST.TIMER, 'timer" + timerSensor.getPort().getOraName() + "')");
                break;
            case RESET:
                String end = createClosingBracket();
                this.sb.append("createResetTimer('timer" + timerSensor.getPort().getOraName() + "'");
                this.sb.append(end);
                break;
            default:
                throw new DbcException("Invalid Time Mode!");
        }
        return null;
    }

    @Override
    public V visitGetSampleSensor(GetSampleSensor<V> sensorGetSample) {
        sensorGetSample.getSensor().visit(this);
        return null;
    }

    @Override
    public V visitMainTask(MainTask<V> mainTask) {
        if ( mainTask.getDebug().equals("TRUE") ) {
            String end = createClosingBracket();
            this.sb.append("createDebugAction(");
            this.sb.append(end);
        }
        mainTask.getVariables().visit(this);
        return null;
    }

    @Override
    public V visitActivityTask(ActivityTask<V> activityTask) {
        return null;
    }

    @Override
    public V visitStartActivityTask(StartActivityTask<V> startActivityTask) {
        return null;
    }

    @Override
    public V visitWaitStmt(WaitStmt<V> waitStmt) {
        String end = createClosingBracket();
        this.sb.append("createWaitStmt([");
        addInStmt();
        visitStmtList(waitStmt.getStatements());
        removeInStmt();
        this.sb.append("]");
        this.sb.append(end);
        return null;
    }

    @Override
    public V visitWaitTimeStmt(WaitTimeStmt<V> waitTimeStmt) {
        String end = createClosingBracket();
        this.sb.append("createWaitTimeStmt(");
        waitTimeStmt.getTime().visit(this);
        this.sb.append(end);
        return null;
    }

    @Override
    public V visitLocation(Location<V> location) {
        return null;
    }

    @Override
    public V visitTextPrintFunct(TextPrintFunct<V> textPrintFunct) {
        return null;
    }

    @Override
    public V visitStmtTextComment(StmtTextComment<V> textComment) {
        String end = createClosingBracket();
        this.sb.append("createNoopStmt(");
        this.sb.append(end);
        return null;
    }

    @Override
    public V visitFunctionStmt(FunctionStmt<V> functionStmt) {
        functionStmt.getFunction().visit(this);
        return null;
    }

    @Override
    public V visitFunctionExpr(FunctionExpr<V> functionExpr) {
        functionExpr.getFunction().visit(this);
        return null;
    }

    @Override
    public V visitGetSubFunct(GetSubFunct<V> getSubFunct) {
        this.sb.append("createGetSubList({list: ");
        getSubFunct.getParam().get(0).visit(this);
        this.sb.append(", where1: CONST.");
        IndexLocation where1 = (IndexLocation) getSubFunct.getStrParam().get(0);
        this.sb.append(where1);
        if ( where1 == IndexLocation.FROM_START || where1 == IndexLocation.FROM_END ) {
            this.sb.append(", at1: ");
            getSubFunct.getParam().get(1).visit(this);
        }
        this.sb.append(", where2: CONST.");
        IndexLocation where2 = (IndexLocation) getSubFunct.getStrParam().get(1);
        this.sb.append(where2);
        if ( where2 == IndexLocation.FROM_START || where2 == IndexLocation.FROM_END ) {
            this.sb.append(", at2: ");
            if ( getSubFunct.getParam().size() == 3 ) {
                getSubFunct.getParam().get(2).visit(this);
            } else {
                getSubFunct.getParam().get(1).visit(this);
            }
        }
        this.sb.append("})");
        return null;
    }

    @Override
    public V visitIndexOfFunct(IndexOfFunct<V> indexOfFunct) {
        this.sb.append("createListFindItem(CONST." + indexOfFunct.getLocation() + ", ");
        indexOfFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        indexOfFunct.getParam().get(1).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public V visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<V> lengthOfIsEmptyFunct) {
        String methodName = "createListLength(";
        if ( lengthOfIsEmptyFunct.getFunctName() == FunctionNames.LIST_IS_EMPTY ) {
            methodName = "createListIsEmpty(";
        }
        this.sb.append(methodName);
        lengthOfIsEmptyFunct.getParam().get(0).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public V visitListCreate(ListCreate<V> listCreate) {
        this.sb.append("createCreateListWith(CONST.ARRAY_" + listCreate.getTypeVar() + ", [");
        listCreate.getValue().visit(this);
        this.sb.append("])");
        return null;
    }

    @Override
    public V visitListSetIndex(ListSetIndex<V> listSetIndex) {
        String end = createClosingBracket();
        this.sb.append("createListsSetIndex(");
        listSetIndex.getParam().get(0).visit(this);
        this.sb.append(", CONST.");
        this.sb.append(listSetIndex.getElementOperation());
        this.sb.append(", ");
        listSetIndex.getParam().get(1).visit(this);
        this.sb.append(", CONST.");
        this.sb.append(listSetIndex.getLocation());
        if ( listSetIndex.getParam().size() == 3 ) {
            this.sb.append(", ");
            listSetIndex.getParam().get(2).visit(this);
        }
        this.sb.append(end);
        return null;
    }

    @Override
    public V visitListGetIndex(ListGetIndex<V> listGetIndex) {
        String methodName = "createListsGetIndex(";
        String end = ")";
        if ( listGetIndex.getElementOperation().isStatment() ) {
            methodName = "createListsGetIndexStmt(";
            end = createClosingBracket();
        }
        this.sb.append(methodName);
        listGetIndex.getParam().get(0).visit(this);
        this.sb.append(", CONST.");
        this.sb.append(listGetIndex.getElementOperation());
        this.sb.append(", CONST.");
        this.sb.append(listGetIndex.getLocation());
        if ( listGetIndex.getParam().size() == 2 ) {
            this.sb.append(", ");
            listGetIndex.getParam().get(1).visit(this);
        }
        this.sb.append(end);
        return null;
    }

    @Override
    public V visitListRepeat(ListRepeat<V> listRepeat) {
        this.sb.append("createCreateListWithItem(");
        listRepeat.getParam().get(0).visit(this);
        this.sb.append(", ");
        listRepeat.getParam().get(1).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public V visitMathConstrainFunct(MathConstrainFunct<V> mathConstrainFunct) {
        this.sb.append("createMathConstrainFunct(");
        mathConstrainFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        mathConstrainFunct.getParam().get(1).visit(this);
        this.sb.append(", ");
        mathConstrainFunct.getParam().get(2).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public V visitMathNumPropFunct(MathNumPropFunct<V> mathNumPropFunct) {
        this.sb.append("createMathPropFunct('" + mathNumPropFunct.getFunctName() + "', ");
        mathNumPropFunct.getParam().get(0).visit(this);
        if ( mathNumPropFunct.getFunctName() == FunctionNames.DIVISIBLE_BY ) {
            this.sb.append(", ");
            mathNumPropFunct.getParam().get(1).visit(this);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public V visitMathOnListFunct(MathOnListFunct<V> mathOnListFunct) {
        this.sb.append("createMathOnList(CONST." + mathOnListFunct.getFunctName() + ", ");
        mathOnListFunct.getParam().get(0).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public V visitMathRandomFloatFunct(MathRandomFloatFunct<V> mathRandomFloatFunct) {
        this.sb.append("createRandDouble()");
        return null;
    }

    @Override
    public V visitMathRandomIntFunct(MathRandomIntFunct<V> mathRandomIntFunct) {
        this.sb.append("createRandInt(");
        mathRandomIntFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        mathRandomIntFunct.getParam().get(1).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public V visitMathSingleFunct(MathSingleFunct<V> mathSingleFunct) {
        this.sb.append("createSingleFunction('" + mathSingleFunct.getFunctName() + "', ");
        mathSingleFunct.getParam().get(0).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public V visitTextJoinFunct(TextJoinFunct<V> textJoinFunct) {
        this.sb.append("createTextJoin([");
        textJoinFunct.getParam().visit(this);
        this.sb.append("])");
        return null;
    }

    @Override
    public V visitMethodVoid(MethodVoid<V> methodVoid) {
        this.sb.append("var method" + this.methodsNumber + " = createMethodVoid('" + methodVoid.getMethodName() + "', [");
        methodVoid.getParameters().visit(this);
        this.sb.append("], [");
        addInStmt();
        methodVoid.getBody().visit(this);
        removeInStmt();
        this.sb.append("]);\n");
        increaseMethods();
        return null;
    }

    @Override
    public V visitMethodReturn(MethodReturn<V> methodReturn) {
        this.sb.append("var method" + this.methodsNumber + " = createMethodReturn('" + methodReturn.getMethodName() + "', [");
        addInStmt();
        methodReturn.getBody().visit(this);
        this.sb.append("], CONST." + methodReturn.getReturnType().toString());

        this.sb.append(", ");
        methodReturn.getReturnValue().visit(this);
        this.sb.append(");\n");
        removeInStmt();
        increaseMethods();
        return null;
    }

    @Override
    public V visitMethodIfReturn(MethodIfReturn<V> methodIfReturn) {
        this.sb.append("createIfReturn(");
        methodIfReturn.getCondition().visit(this);
        this.sb.append(", CONST." + methodIfReturn.getReturnType().toString());
        this.sb.append(", ");
        methodIfReturn.getReturnValue().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public V visitMethodStmt(MethodStmt<V> methodStmt) {
        methodStmt.getMethod().visit(this);
        return null;
    }

    @Override
    public V visitMethodCall(MethodCall<V> methodCall) {
        String end = ")";
        String name = "createMethodCallReturn('";
        if ( methodCall.getReturnType() == BlocklyType.VOID ) {
            name = "createMethodCallVoid('";
            end = createClosingBracket();
        }
        this.sb.append(name + methodCall.getMethodName() + "', [");
        List<Expr<V>> parametersNames = methodCall.getParameters().get();
        List<Expr<V>> parametersValues = methodCall.getParametersValues().get();
        for ( int i = 0; i < parametersNames.size(); i++ ) {
            this.sb.append("createAssignMethodParameter(\"");
            this.sb.append(((Var<V>) parametersNames.get(i)).getValue());
            this.sb.append("\", ");
            parametersValues.get(i).visit(this);
            this.sb.append(")");
            boolean isLastMethodParameter = i != parametersNames.size() - 1;
            if ( isLastMethodParameter ) {
                this.sb.append(", ");
            }

        }
        this.sb.append("]" + end);
        return null;
    }

    @Override
    public V visitBluetoothReceiveAction(BluetoothReceiveAction<V> bluetoothReceiveAction) {
        return null;
    }

    @Override
    public V visitBluetoothConnectAction(BluetoothConnectAction<V> bluetoothConnectAction) {
        return null;
    }

    @Override
    public V visitBluetoothSendAction(BluetoothSendAction<V> bluetoothSendAction) {
        return null;
    }

    @Override
    public V visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction<V> bluetoothWaitForConnection) {
        return null;
    }

    @Override
    public V visitConnectConst(ConnectConst<V> connectConst) {
        return null;
    }

    @Override
    public V visitBluetoothCheckConnectAction(BluetoothCheckConnectAction<V> bluetoothCheckConnectAction) {
        return null;
    }

    protected void increaseStmt() {
        this.stmtsNumber++;
    }

    protected void increaseMethods() {
        this.methodsNumber++;
    }

    /**
     * @return the inStmt
     */
    protected boolean isInStmt() {
        if ( this.inStmt.size() == 0 ) {
            return false;
        }
        return this.inStmt.get(this.inStmt.size() - 1);
    }

    /**
     * @param inStmt the inStmt to set
     */
    protected void addInStmt() {
        this.inStmt.add(true);
    }

    protected void removeInStmt() {
        if ( !this.inStmt.isEmpty() ) {
            this.inStmt.remove(this.inStmt.size() - 1);
        }
    }

    protected void appendIfStmtConditions(IfStmt<V> ifStmt) {
        int exprSize = ifStmt.getExpr().size();
        for ( int i = 0; i < exprSize; i++ ) {
            ifStmt.getExpr().get(i).visit(this);
            if ( i < exprSize - 1 ) {
                this.sb.append(", ");
            }
        }
    }

    protected void appendElseStmt(IfStmt<V> ifStmt) {
        if ( !ifStmt.getElseList().get().isEmpty() ) {
            addInStmt();
            ifStmt.getElseList().visit(this);
            removeInStmt();
        }
    }

    protected void appendThenStmts(IfStmt<V> ifStmt) {
        int thenListSize = ifStmt.getThenList().size();
        for ( int i = 0; i < thenListSize; i++ ) {
            addInStmt();
            this.sb.append("[");
            ifStmt.getThenList().get(i).visit(this);
            boolean isLastStmt = i < thenListSize - 1;
            this.sb.append("]");
            if ( isLastStmt ) {
                this.sb.append(", ");
            }
            removeInStmt();
        }
    }

    protected void appendRepeatStmtStatements(RepeatStmt<V> repeatStmt) {
        if ( repeatStmt.getMode() == Mode.WAIT ) {
            if ( !repeatStmt.getList().get().isEmpty() ) {
                this.sb.append("[");
                repeatStmt.getList().visit(this);
                this.sb.append("]");
            }
        } else {
            repeatStmt.getList().visit(this);
        }
    }

    protected void appendRepeatStmtCondition(RepeatStmt<V> repeatStmt) {
        String methodName = "createRepeatStmt('loop_" + this.loopsCounter + "', CONST." + repeatStmt.getMode() + ", ";
        switch ( repeatStmt.getMode() ) {
            case WAIT:
                this.sb.append("createIfStmt([");
                repeatStmt.getExpr().visit(this);
                this.sb.append("], [");
                break;
            case FOREVER:
            case WHILE:
            case UNTIL:
            case FOR_EACH:
                this.sb.append(methodName);
                repeatStmt.getExpr().visit(this);
                this.sb.append(", [");
                break;
            case TIMES:
            case FOR:
                this.sb.append(methodName + "[");
                repeatStmt.getExpr().visit(this);
                this.sb.append("], [");
                break;

            default:
                throw new DbcException("Invalid repeat mode");

        }
    }

    protected String createClosingBracket() {
        String end = ")";
        if ( !isInStmt() ) {
            this.sb.append("var stmt" + this.stmtsNumber + " = ");
            increaseStmt();
            end = ");\n";
        }
        return end;
    }

    protected void removeLastComma() {
        if ( isInStmt() ) {
            this.sb.setLength(this.sb.length() - 2);
        }
    }

    protected void appendDuration(MotorDuration<V> duration) {
        if ( duration != null ) {
            this.sb.append(", ");
            duration.getValue().visit(this);
        }
    }

    protected DriveDirection getDriveDirection(boolean isReverse) {
        if ( isReverse ) {
            return DriveDirection.BACKWARD;
        }
        return DriveDirection.FOREWARD;
    }

    protected TurnDirection getTurnDirection(boolean isReverse) {
        if ( isReverse ) {
            return TurnDirection.RIGHT;
        }
        return TurnDirection.LEFT;
    }

    protected void generateCodeFromPhrases(ArrayList<ArrayList<Phrase<V>>> phrasesSet) {
        for ( ArrayList<Phrase<V>> phrases : phrasesSet ) {
            for ( Phrase<V> phrase : phrases ) {
                phrase.visit(this);
            }
        }
        appendProgramInitialization(this);
    }

    private void appendStmtsInitialization(RobotSimulationVisitor<V> astVisitor) {
        astVisitor.sb.append("'programStmts': [");
        if ( astVisitor.stmtsNumber > 0 ) {
            for ( int i = 0; i < astVisitor.stmtsNumber; i++ ) {
                astVisitor.sb.append("stmt" + i);
                if ( i != astVisitor.stmtsNumber - 1 ) {
                    astVisitor.sb.append(",");
                }

            }
        }
        astVisitor.sb.append("]");
    }

    private void appendMethodsInitialization(RobotSimulationVisitor<V> astVisitor) {
        if ( astVisitor.methodsNumber > 0 ) {
            astVisitor.sb.append("'programMethods': [");
            for ( int i = 0; i < astVisitor.methodsNumber; i++ ) {
                astVisitor.sb.append("method" + i);
                if ( i != astVisitor.methodsNumber - 1 ) {
                    astVisitor.sb.append(",");
                } else {
                    astVisitor.sb.append("], ");
                }
            }
        }
    }

    private void appendProgramInitialization(RobotSimulationVisitor<V> astVisitor) {
        astVisitor.sb.append("var blocklyProgram = {");
        appendMethodsInitialization(astVisitor);
        appendStmtsInitialization(astVisitor);
        astVisitor.sb.append("};");
    }
}
