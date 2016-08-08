package de.fhg.iais.roberta.syntax.codegen;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;

import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.Sensor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.sensor.ev3.ColorSensorMode;
import de.fhg.iais.roberta.mode.sensor.ev3.GyroSensorMode;
import de.fhg.iais.roberta.mode.sensor.ev3.InfraredSensorMode;
import de.fhg.iais.roberta.mode.sensor.ev3.MotorTachoMode;
import de.fhg.iais.roberta.mode.sensor.ev3.TimerSensorMode;
import de.fhg.iais.roberta.mode.sensor.ev3.UltrasonicSensorMode;
import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothConnectAction;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.generic.BluetoothWaitForConnectionAction;
import de.fhg.iais.roberta.syntax.action.generic.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.generic.DriveAction;
import de.fhg.iais.roberta.syntax.action.generic.LightAction;
import de.fhg.iais.roberta.syntax.action.generic.LightSensorAction;
import de.fhg.iais.roberta.syntax.action.generic.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.generic.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.generic.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.generic.ShowPictureAction;
import de.fhg.iais.roberta.syntax.action.generic.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.generic.ToneAction;
import de.fhg.iais.roberta.syntax.action.generic.TurnAction;
import de.fhg.iais.roberta.syntax.action.generic.VolumeAction;
import de.fhg.iais.roberta.syntax.blocksequence.ActivityTask;
import de.fhg.iais.roberta.syntax.blocksequence.Location;
import de.fhg.iais.roberta.syntax.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.blocksequence.StartActivityTask;
import de.fhg.iais.roberta.syntax.expr.ActionExpr;
import de.fhg.iais.roberta.syntax.expr.Binary;
import de.fhg.iais.roberta.syntax.expr.BoolConst;
import de.fhg.iais.roberta.syntax.expr.ColorConst;
import de.fhg.iais.roberta.syntax.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.expr.EmptyList;
import de.fhg.iais.roberta.syntax.expr.Expr;
import de.fhg.iais.roberta.syntax.expr.ExprList;
import de.fhg.iais.roberta.syntax.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.expr.ListCreate;
import de.fhg.iais.roberta.syntax.expr.MathConst;
import de.fhg.iais.roberta.syntax.expr.MethodExpr;
import de.fhg.iais.roberta.syntax.expr.NullConst;
import de.fhg.iais.roberta.syntax.expr.NumConst;
import de.fhg.iais.roberta.syntax.expr.SensorExpr;
import de.fhg.iais.roberta.syntax.expr.ShadowExpr;
import de.fhg.iais.roberta.syntax.expr.StmtExpr;
import de.fhg.iais.roberta.syntax.expr.StringConst;
import de.fhg.iais.roberta.syntax.expr.Unary;
import de.fhg.iais.roberta.syntax.expr.Var;
import de.fhg.iais.roberta.syntax.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.functions.LengthOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.functions.ListRepeat;
import de.fhg.iais.roberta.syntax.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.functions.MathPowerFunct;
import de.fhg.iais.roberta.syntax.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.functions.MathSingleFunct;
import de.fhg.iais.roberta.syntax.functions.TextJoinFunct;
import de.fhg.iais.roberta.syntax.functions.TextPrintFunct;
import de.fhg.iais.roberta.syntax.hardwarecheck.generic.UsedSensorsCheckVisitor;
import de.fhg.iais.roberta.syntax.methods.MethodCall;
import de.fhg.iais.roberta.syntax.methods.MethodIfReturn;
import de.fhg.iais.roberta.syntax.methods.MethodReturn;
import de.fhg.iais.roberta.syntax.methods.MethodVoid;
import de.fhg.iais.roberta.syntax.sensor.generic.BrickSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.stmt.ActionStmt;
import de.fhg.iais.roberta.syntax.stmt.AssignStmt;
import de.fhg.iais.roberta.syntax.stmt.ExprStmt;
import de.fhg.iais.roberta.syntax.stmt.FunctionStmt;
import de.fhg.iais.roberta.syntax.stmt.IfStmt;
import de.fhg.iais.roberta.syntax.stmt.MethodStmt;
import de.fhg.iais.roberta.syntax.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.stmt.RepeatStmt.Mode;
import de.fhg.iais.roberta.syntax.stmt.SensorStmt;
import de.fhg.iais.roberta.syntax.stmt.Stmt;
import de.fhg.iais.roberta.syntax.stmt.StmtFlowCon;
import de.fhg.iais.roberta.syntax.stmt.StmtList;
import de.fhg.iais.roberta.syntax.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.AstVisitor;

/**
 * This class is implementing {@link AstVisitor}. All methods are implemented and they
 * append a human-readable Python code representation of a phrase to a StringBuilder. <b>This representation is correct Python code.</b> <br>
 */
@SuppressWarnings("rawtypes")
public class Ast2Ev3PythonVisitor implements AstVisitor<Void> {
    public static final String INDENT = "    ";

    private final Configuration brickConfiguration;
    private final String programName;
    private final StringBuilder sb = new StringBuilder();
    private final Set<UsedSensor> usedSensors;
    private int indentation;
    private final StringBuilder indent = new StringBuilder();

    /**
     * initialize the Python code generator visitor.
     *
     * @param programName name of the program
     * @param brickConfiguration hardware configuration of the brick
     * @param usedSensors in the current program
     * @param indentation to start with. Will be ince/decr depending on block structure
     */
    Ast2Ev3PythonVisitor(String programName, Configuration brickConfiguration, Set<UsedSensor> usedSensors, int indentation) {
        this.programName = programName;
        this.brickConfiguration = brickConfiguration;
        this.indentation = indentation;
        this.usedSensors = usedSensors;
        for ( int i = 0; i < indentation; i++ ) {
            indent.append(INDENT);
        }
    }

    /**
     * factory method to generate Python code from an AST.<br>
     *
     * @param programName name of the program
     * @param brickConfiguration hardware configuration of the brick
     * @param phrases to generate the code from
     */
    public static String generate(String programName, Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) //
    {
        Assert.notNull(programName);
        Assert.notNull(brickConfiguration);
        Assert.isTrue(phrasesSet.size() >= 1);

        Set<UsedSensor> usedSensors = UsedSensorsCheckVisitor.check(phrasesSet);
        Ast2Ev3PythonVisitor astVisitor = new Ast2Ev3PythonVisitor(programName, brickConfiguration, usedSensors, 0);
        astVisitor.generatePrefix(withWrapping);

        generateCodeFromPhrases(phrasesSet, withWrapping, astVisitor);

        astVisitor.generateSuffix(withWrapping);

        return astVisitor.sb.toString();
    }

    private static void generateCodeFromPhrases(ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping, Ast2Ev3PythonVisitor astVisitor) {
        boolean mainBlock = false;
        for ( ArrayList<Phrase<Void>> phrases : phrasesSet ) {
            for ( Phrase<Void> phrase : phrases ) {
                mainBlock = handleMainBlocks(astVisitor, mainBlock, phrase);
                phrase.visit(astVisitor);
            }
            if ( mainBlock ) {
                mainBlock = false;
            }
        }
    }

    private static boolean handleMainBlocks(Ast2Ev3PythonVisitor astVisitor, boolean mainBlock, Phrase<Void> phrase) {
        if ( phrase.getKind().getCategory() != Category.TASK ) {
            astVisitor.nlIndent();
        } else if ( phrase.getKind() != BlockType.LOCATION ) {
            mainBlock = true;
        }
        return mainBlock;
    }

    /**
     * Get the current indentation of the visitor. Meaningful for tests only.
     *
     * @return indentation value of the visitor.
     */
    int getIndentation() {
        return indentation;
    }

    /**
     * Get the string builder of the visitor. Meaningful for tests only.
     *
     * @return (current state of) the string builder
     */
    public StringBuilder getSb() {
        return sb;
    }

    private void incrIndentation() {
        indentation += 1;
        indent.append(INDENT);
    }

    private void decrIndentation() {
        indentation -= 1;
        indent.delete(0, INDENT.length());
    }

    private void nlIndent() {
        sb.append("\n").append(indent);
    }

    private static String getEnumCode(IMode value) {
        return "'" + value.toString().toLowerCase() + "'";
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> sensor) {
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> sensor) {
        return null;
    }

    @Override
    public Void visitNumConst(NumConst<Void> numConst) {
        if ( isInteger(numConst.getValue()) ) {
            sb.append(numConst.getValue());
        } else {
            sb.append("float(");
            sb.append(numConst.getValue());
            sb.append(")");
        }
        return null;
    }

    @Override
    public Void visitBoolConst(BoolConst<Void> boolConst) {
        sb.append(boolConst.isValue() ? "True" : "False");
        return null;
    };

    @Override
    public Void visitMathConst(MathConst<Void> mathConst) {
        switch ( mathConst.getMathConst() ) {
            case PI:
                sb.append("math.pi");
                break;
            case E:
                sb.append("math.e");
                break;
            case GOLDEN_RATIO:
                sb.append("BlocklyMethods.GOLDEN_RATIO");
                break;
            case SQRT2:
                sb.append("math.sqrt(2)");
                break;
            case SQRT1_2:
                sb.append("math.sqrt(1.0/2.0)");
                break;
            case INFINITY:
                sb.append("float('inf')");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst<Void> colorConst) {
        sb.append(getEnumCode(colorConst.getValue()));
        return null;
    }

    @Override
    public Void visitStringConst(StringConst<Void> stringConst) {
        sb.append("\"").append(StringEscapeUtils.escapeJava(stringConst.getValue())).append("\"");
        return null;
    }

    @Override
    public Void visitNullConst(NullConst<Void> nullConst) {
        sb.append("None");
        return null;
    }

    @Override
    public Void visitVar(Var<Void> var) {
        sb.append(var.getValue());
        return null;
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration<Void> var) {
        sb.append(var.getName());
        if ( var.getValue().getKind() != BlockType.EMPTY_EXPR ) {
            sb.append(" = ");
            if ( var.getValue().getKind() == BlockType.EXPR_LIST ) {
                ExprList<Void> list = (ExprList<Void>) var.getValue();
                if ( list.get().size() == 2 ) {
                    list.get().get(1).visit(this);
                } else {
                    list.get().get(0).visit(this);
                }
            } else {
                var.getValue().visit(this);
            }
        }
        return null;
    }

    @Override
    public Void visitUnary(Unary<Void> unary) {
        Unary.Op op = unary.getOp();
        String sym = op.getOpSymbol();
        // fixup language specific symbols
        if ( op == Unary.Op.NOT ) {
            sym = "not ";
        }
        if ( unary.getOp() == Unary.Op.POSTFIX_INCREMENTS ) {
            generateExprCode(unary, sb);
            sb.append(sym);
        } else {
            sb.append(sym);
            generateExprCode(unary, sb);
        }
        return null;
    }

    @Override
    public Void visitBinary(Binary<Void> binary) {
        generateSubExpr(sb, false, binary.getLeft(), binary);
        Binary.Op op = binary.getOp();
        String sym = op.getOpSymbol();
        // fixup language specific symbols
        switch ( op ) {
            case OR:
                sym = "or";
                break;
            case AND:
                sym = "and";
                break;
            case IN:
                sym = "in";
                break;
            default:
                break;
        }
        sb.append(' ').append(sym).append(' ');
        generateCodeRightExpression(binary, op);
        return null;
    }

    private void generateCodeRightExpression(Binary<Void> binary, Binary.Op op) {
        switch ( op ) {
            case TEXT_APPEND:
                sb.append("str(");
                generateSubExpr(sb, false, binary.getRight(), binary);
                sb.append(")");
                break;
            case DIVIDE:
                sb.append("float(");
                generateSubExpr(sb, parenthesesCheck(binary), binary.getRight(), binary);
                sb.append(")");
                break;
            default:
                generateSubExpr(sb, parenthesesCheck(binary), binary.getRight(), binary);
                break;
        }
    }

    @Override
    public Void visitActionExpr(ActionExpr<Void> actionExpr) {
        actionExpr.getAction().visit(this);
        return null;
    }

    @Override
    public Void visitSensorExpr(SensorExpr<Void> sensorExpr) {
        sensorExpr.getSens().visit(this);
        return null;
    }

    @Override
    public Void visitMethodExpr(MethodExpr<Void> methodExpr) {
        methodExpr.getMethod().visit(this);
        return null;
    }

    @Override
    public Void visitEmptyExpr(EmptyExpr<Void> emptyExpr) {
        switch ( emptyExpr.getDefVal().getName() ) {
            case "java.lang.String":
                sb.append("\"\"");
                break;
            case "java.lang.Boolean":
                sb.append("True");
                break;
            case "java.lang.Integer":
                sb.append("0");
                break;
            case "java.util.ArrayList":
                break;
            case "de.fhg.iais.roberta.syntax.expr.NullConst":
                break;
            default:
                sb.append("[[EmptyExpr [defVal=" + emptyExpr.getDefVal() + "]]]");
                break;
        }
        return null;
    }

    @Override
    public Void visitShadowExpr(ShadowExpr<Void> shadowExpr) {
        if ( shadowExpr.getBlock() != null ) {
            shadowExpr.getBlock().visit(this);
        } else {
            shadowExpr.getShadow().visit(this);
        }
        return null;
    }

    @Override
    public Void visitExprList(ExprList<Void> exprList) {
        boolean first = true;
        for ( Expr<Void> expr : exprList.get() ) {
            if ( expr.getKind() != BlockType.EMPTY_EXPR ) {
                if ( first ) {
                    first = false;
                } else {
                    if ( expr.getKind() == BlockType.BINARY || expr.getKind() == BlockType.UNARY ) {
                        sb.append("; "); // FIXME
                    } else {
                        sb.append(", ");
                    }
                }
                expr.visit(this);
            }
        }
        return null;
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct<Void> mathPowerFunct) {
        //        switch ( funct.getFunctName() ) {
        //            case PRINT:
        //                this.sb.append("System.out.println(");
        //                funct.getParam().get(0).visit(this);
        //                this.sb.append(")");
        //                break;
        //            default:
        //                break;
        //        }
        return null;
    }

    @Override
    public Void visitActionStmt(ActionStmt<Void> actionStmt) {
        actionStmt.getAction().visit(this);
        return null;
    }

    @Override
    public Void visitAssignStmt(AssignStmt<Void> assignStmt) {
        assignStmt.getName().visit(this);
        sb.append(" = ");
        assignStmt.getExpr().visit(this);
        return null;
    }

    @Override
    public Void visitExprStmt(ExprStmt<Void> exprStmt) {
        exprStmt.getExpr().visit(this);
        return null;
    }

    @Override
    public Void visitStmtExpr(StmtExpr<Void> stmtExpr) {
        stmtExpr.getStmt().visit(this);
        return null;
    }

    @Override
    public Void visitIfStmt(IfStmt<Void> ifStmt) {
        if ( ifStmt.isTernary() ) {
            generateCodeFromTernary(ifStmt);
        } else {
            generateCodeFromIfElse(ifStmt);
            generateCodeFromElse(ifStmt);
        }
        return null;
    }

    @Override
    public Void visitRepeatStmt(RepeatStmt<Void> repeatStmt) {
        boolean additionalClosingScope = false;
        switch ( repeatStmt.getMode() ) {
            case UNTIL:
            case WHILE:
            case FOREVER:
                sb.append("if TRUE:");
                incrIndentation();
                nlIndent();
                generateCodeFromStmtCondition("while", repeatStmt.getExpr());
                additionalClosingScope = true;
                break;
            case TIMES:
            case FOR:
                generateCodeFromStmtConditionFor("for", repeatStmt.getExpr());
                break;
            case WAIT:
                generateCodeFromStmtCondition("if", repeatStmt.getExpr());
                break;
            case FOR_EACH:
                generateCodeFromStmtCondition("for", repeatStmt.getExpr());
                break;
            default:
                break;
        }
        incrIndentation();
        if ( repeatStmt.getList().get().isEmpty() ) {
            nlIndent();
            sb.append("pass");
        } else {
            repeatStmt.getList().visit(this);
        }
        appendBreakStmt(repeatStmt);
        decrIndentation();
        if ( additionalClosingScope ) {
            decrIndentation();
        }
        return null;
    }

    @Override
    public Void visitSensorStmt(SensorStmt<Void> sensorStmt) {
        sensorStmt.getSensor().visit(this);
        return null;
    }

    @Override
    public Void visitStmtFlowCon(StmtFlowCon<Void> stmtFlowCon) {
        sb.append(stmtFlowCon.getFlow().toString().toLowerCase());
        return null;
    }

    @Override
    public Void visitStmtList(StmtList<Void> stmtList) {
        for ( Stmt<Void> stmt : stmtList.get() ) {
            nlIndent();
            stmt.visit(this);
        }
        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        sb.append("if TRUE:");
        incrIndentation();
        nlIndent();
        sb.append("while True:");
        incrIndentation();
        visitStmtList(waitStmt.getStatements());
        nlIndent();
        sb.append("hal.waitFor(15)");
        decrIndentation();
        decrIndentation();
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        sb.append("hal.waitFor(");
        waitTimeStmt.getTime().visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        sb.append("hal.clearDisplay()");
        return null;
    }

    @Override
    public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        switch ( volumeAction.getMode() ) {
            case SET:
                sb.append("hal.setVolume(");
                volumeAction.getVolume().visit(this);
                sb.append(")");
                break;
            case GET:
                sb.append("hal.getVolume()");
                break;
            default:
                throw new DbcException("Invalid volume action mode!");
        }
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        sb.append("hal.ledOn(" + getEnumCode(lightAction.getColor()) + ", " + getEnumCode(lightAction.getBlinkMode()) + ")");
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        switch ( lightStatusAction.getStatus() ) {
            case OFF:
                sb.append("hal.ledOff()");
                break;
            case RESET:
                sb.append("hal.resetLED()");
                break;
            default:
                throw new DbcException("Invalid LED status mode!");
        }
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        sb.append("hal.playFile(" + playFileAction.getFileName() + ")");
        return null;
    }

    @Override
    public Void visitShowPictureAction(ShowPictureAction<Void> showPictureAction) {
        sb.append("hal.drawPicture(" + getEnumCode(showPictureAction.getPicture()) + ", ");
        showPictureAction.getX().visit(this);
        sb.append(", ");
        showPictureAction.getY().visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
        sb.append("hal.drawText(");
        if ( showTextAction.getMsg().getKind() != BlockType.STRING_CONST ) {
            sb.append("str(");
            showTextAction.getMsg().visit(this);
            sb.append(")");
        } else {
            showTextAction.getMsg().visit(this);
        }
        sb.append(", ");
        showTextAction.getX().visit(this);
        sb.append(", ");
        showTextAction.getY().visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        sb.append("hal.playTone(");
        toneAction.getFrequency().visit(this);
        sb.append(", ");
        toneAction.getDuration().visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        String methodName;
        boolean isRegulated = brickConfiguration.isMotorRegulated(motorOnAction.getPort());
        boolean duration = motorOnAction.getParam().getDuration() != null;
        if ( duration ) {
            methodName = isRegulated ? "hal.rotateRegulatedMotor('" : "hal.rotateUnregulatedMotor('";
        } else {
            methodName = isRegulated ? "hal.turnOnRegulatedMotor('" : "hal.turnOnUnregulatedMotor('";
        }
        sb.append(methodName + motorOnAction.getPort().toString() + "', ");
        motorOnAction.getParam().getSpeed().visit(this);
        if ( duration ) {
            sb.append(", " + getEnumCode(motorOnAction.getDurationMode()));
            sb.append(", ");
            motorOnAction.getDurationValue().visit(this);
        }
        sb.append(")");
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        boolean isRegulated = brickConfiguration.isMotorRegulated(motorSetPowerAction.getPort());
        String methodName = isRegulated ? "hal.setRegulatedMotorSpeed('" : "hal.setUnregulatedMotorSpeed('";
        sb.append(methodName + motorSetPowerAction.getPort().toString() + "', ");
        motorSetPowerAction.getPower().visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        boolean isRegulated = brickConfiguration.isMotorRegulated(motorGetPowerAction.getPort());
        String methodName = isRegulated ? "hal.getRegulatedMotorSpeed('" : "hal.getUnregulatedMotorSpeed('";
        sb.append(methodName + motorGetPowerAction.getPort().toString() + "')");
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        sb.append("hal.stopMotor('").append(motorStopAction.getPort().toString()).append("', ").append(getEnumCode(motorStopAction.getMode())).append(')');
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        boolean isDuration = driveAction.getParam().getDuration() != null;
        String methodName = isDuration ? "hal.driveDistance(" : "hal.regulatedDrive(";
        sb.append(methodName);
        sb.append("'" + brickConfiguration.getLeftMotorPort().toString() + "', ");
        sb.append("'" + brickConfiguration.getRightMotorPort().toString() + "', False, ");
        sb.append(getEnumCode(driveAction.getDirection()) + ", ");
        driveAction.getParam().getSpeed().visit(this);
        if ( isDuration ) {
            sb.append(", ");
            driveAction.getParam().getDuration().getValue().visit(this);
        }
        sb.append(")");
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        boolean isDuration = turnAction.getParam().getDuration() != null;
        boolean isRegulated = brickConfiguration.getActorOnPort(brickConfiguration.getLeftMotorPort()).isRegulated();
        String methodName = "hal.rotateDirection" + (isDuration ? "Angle" : isRegulated ? "Regulated" : "Unregulated") + "(";
        sb.append(methodName);
        sb.append("'" + brickConfiguration.getLeftMotorPort().toString() + "', ");
        sb.append("'" + brickConfiguration.getRightMotorPort().toString() + "', False, ");
        sb.append(getEnumCode(turnAction.getDirection()) + ", ");
        turnAction.getParam().getSpeed().visit(this);
        if ( isDuration ) {
            sb.append(", ");
            turnAction.getParam().getDuration().getValue().visit(this);
        }
        sb.append(")");
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        sb.append("hal.stopMotors(");
        sb.append("'" + brickConfiguration.getLeftMotorPort().toString() + "', ");
        sb.append("'" + brickConfiguration.getRightMotorPort().toString() + "')");
        return null;
    }

    @Override
    public Void visitBrickSensor(BrickSensor<Void> brickSensor) {
        switch ( brickSensor.getMode() ) {
            case IS_PRESSED:
                sb.append("hal.isKeyPressed(" + getEnumCode(brickSensor.getKey()) + ")");
                break;
            case WAIT_FOR_PRESS_AND_RELEASE:
                sb.append("hal.isKeyPressedAndReleased(" + getEnumCode(brickSensor.getKey()) + ")");
                break;
            default:
                throw new DbcException("Invalide mode for BrickSensor!");
        }
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        String colorSensorPort = colorSensor.getPort().getPortNumber();
        switch ( (ColorSensorMode) colorSensor.getMode() ) {
            case AMBIENTLIGHT:
                sb.append("hal.getColorSensorAmbient('" + colorSensorPort + "')");
                break;
            case COLOUR:
                sb.append("hal.getColorSensorColour('" + colorSensorPort + "')");
                break;
            case RED:
                sb.append("hal.getColorSensorRed('" + colorSensorPort + "')");
                break;
            case RGB:
                sb.append("hal.getColorSensorRgb('" + colorSensorPort + "')");
                break;
            default:
                throw new DbcException("Invalide mode for Color Sensor!");
        }
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        String encoderSensorPort = encoderSensor.getMotorPort().toString();
        if ( encoderSensor.getMode() == MotorTachoMode.RESET ) {
            sb.append("hal.resetMotorTacho('" + encoderSensorPort + "')");
        } else {
            sb.append("hal.getMotorTachoValue('" + encoderSensorPort + "', " + getEnumCode(encoderSensor.getMode()) + ")");
        }
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        String gyroSensorPort = gyroSensor.getPort().getPortNumber();
        if ( gyroSensor.getMode() == GyroSensorMode.RESET ) {
            sb.append("hal.resetGyroSensor('" + gyroSensorPort + "')");
        } else {
            sb.append("hal.getGyroSensorValue('" + gyroSensorPort + ", " + getEnumCode(gyroSensor.getMode()) + "')");
        }
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        String infraredSensorPort = infraredSensor.getPort().getPortNumber();
        switch ( (InfraredSensorMode) infraredSensor.getMode() ) {
            case DISTANCE:
                sb.append("hal.getInfraredSensorDistance('" + infraredSensorPort + "')");
                break;
            case SEEK:
                sb.append("hal.getInfraredSensorSeek('" + infraredSensorPort + "')");
                break;
            default:
                throw new DbcException("Invalid Infrared Sensor Mode!");
        }

        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        switch ( (TimerSensorMode) timerSensor.getMode() ) {
            case GET_SAMPLE:
                sb.append("hal.getTimerValue(" + timerSensor.getTimer() + ")");
                break;
            case RESET:
                sb.append("hal.resetTimer(" + timerSensor.getTimer() + ")");
                break;
            default:
                throw new DbcException("Invalid Time Mode!");
        }
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        sb.append("hal.isPressed('" + touchSensor.getPort().getPortNumber() + "')");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        String ultrasonicSensorPort = ultrasonicSensor.getPort().getPortNumber();
        if ( ultrasonicSensor.getMode() == UltrasonicSensorMode.DISTANCE ) {
            sb.append("hal.getUltraSonicSensorDistance('" + ultrasonicSensorPort + "')");
        } else {
            sb.append("hal.getUltraSonicSensorPresence('" + ultrasonicSensorPort + "')");
        }
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        mainTask.getVariables().visit(this);
        sb.append("\n").append("def run():");
        incrIndentation();
        return null;
    }

    @Override
    public Void visitActivityTask(ActivityTask<Void> activityTask) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitStartActivityTask(StartActivityTask<Void> startActivityTask) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitLocation(Location<Void> location) {
        return null;
    }

    @Override
    public Void visitGetSampleSensor(GetSampleSensor<Void> sensorGetSample) {
        return sensorGetSample.getSensor().visit(this);
    }

    @Override
    public Void visitTextPrintFunct(TextPrintFunct<Void> textPrintFunct) {
        sb.append("print(");
        textPrintFunct.getParam().get(0).visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitFunctionStmt(FunctionStmt<Void> functionStmt) {
        functionStmt.getFunction().visit(this);
        return null;
    }

    @Override
    public Void visitFunctionExpr(FunctionExpr<Void> functionExpr) {
        functionExpr.getFunction().visit(this);
        return null;
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct<Void> getSubFunct) {
        sb.append("BlocklyMethods.listsGetSubList( ");
        getSubFunct.getParam().get(0).visit(this);
        sb.append(", ");
        IndexLocation where1 = (IndexLocation) getSubFunct.getStrParam().get(0);
        sb.append(getEnumCode(where1));
        if ( where1 == IndexLocation.FROM_START || where1 == IndexLocation.FROM_END ) {
            sb.append(", ");
            getSubFunct.getParam().get(1).visit(this);
        }
        sb.append(", ");
        IndexLocation where2 = (IndexLocation) getSubFunct.getStrParam().get(1);
        sb.append(getEnumCode(where2));
        if ( where2 == IndexLocation.FROM_START || where2 == IndexLocation.FROM_END ) {
            sb.append(", ");
            if ( getSubFunct.getParam().size() == 3 ) {
                getSubFunct.getParam().get(2).visit(this);
            } else {
                getSubFunct.getParam().get(1).visit(this);
            }
        }
        sb.append(")");
        return null;

    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        switch ( (IndexLocation) indexOfFunct.getLocation() ) {
            case FIRST:
                sb.append("BlocklyMethods.findFirst( ");
                indexOfFunct.getParam().get(0).visit(this);
                sb.append(", ");
                indexOfFunct.getParam().get(1).visit(this);
                sb.append(")");
                break;
            case LAST:
                sb.append("BlocklyMethods.findLast( ");
                indexOfFunct.getParam().get(0).visit(this);
                sb.append(", ");
                indexOfFunct.getParam().get(1).visit(this);
                sb.append(")");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
        switch ( lengthOfIsEmptyFunct.getFunctName() ) {
            case LISTS_LENGTH:
                sb.append("BlocklyMethods.length( ");
                lengthOfIsEmptyFunct.getParam().get(0).visit(this);
                sb.append(")");
                break;

            case LIST_IS_EMPTY:
                sb.append("BlocklyMethods.isEmpty( ");
                lengthOfIsEmptyFunct.getParam().get(0).visit(this);
                sb.append(")");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitEmptyList(EmptyList<Void> emptyList) {
        sb.append("[]");
        return null;
    }

    @Override
    public Void visitListCreate(ListCreate<Void> listCreate) {
        sb.append("BlocklyMethods.createListWith(");
        listCreate.getValue().visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitListRepeat(ListRepeat<Void> listRepeat) {
        sb.append("BlocklyMethods.createListWithItem(");
        listRepeat.getParam().get(0).visit(this);
        sb.append(", ");
        listRepeat.getParam().get(1).visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        sb.append("BlocklyMethods.listsGetIndex(");
        listGetIndex.getParam().get(0).visit(this);
        sb.append(", ");
        sb.append(getEnumCode(listGetIndex.getElementOperation()));
        sb.append(", ");
        sb.append(getEnumCode(listGetIndex.getLocation()));
        if ( listGetIndex.getParam().size() == 2 ) {
            sb.append(", ");
            listGetIndex.getParam().get(1).visit(this);
        }
        sb.append(")");
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        sb.append("BlocklyMethods.listsSetIndex(");
        listSetIndex.getParam().get(0).visit(this);
        sb.append(", ");
        sb.append(getEnumCode(listSetIndex.getElementOperation()));
        sb.append(", ");
        listSetIndex.getParam().get(1).visit(this);
        sb.append(", ");
        sb.append(getEnumCode(listSetIndex.getLocation()));
        if ( listSetIndex.getParam().size() == 3 ) {
            sb.append(", ");
            listSetIndex.getParam().get(2).visit(this);
        }
        sb.append(")");
        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct<Void> mathConstrainFunct) {
        sb.append("BlocklyMethods.clamp(");
        mathConstrainFunct.getParam().get(0).visit(this);
        sb.append(", ");
        mathConstrainFunct.getParam().get(1).visit(this);
        sb.append(", ");
        mathConstrainFunct.getParam().get(2).visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct<Void> mathNumPropFunct) {
        switch ( mathNumPropFunct.getFunctName() ) {
            case EVEN:
                sb.append("BlocklyMethods.isEven(");
                mathNumPropFunct.getParam().get(0).visit(this);
                sb.append(")");
                break;
            case ODD:
                sb.append("BlocklyMethods.isOdd(");
                mathNumPropFunct.getParam().get(0).visit(this);
                sb.append(")");
                break;
            case PRIME:
                sb.append("BlocklyMethods.isPrime(");
                mathNumPropFunct.getParam().get(0).visit(this);
                sb.append(")");
                break;
            case WHOLE:
                sb.append("BlocklyMethods.isWhole(");
                mathNumPropFunct.getParam().get(0).visit(this);
                sb.append(")");
                break;
            case POSITIVE:
                sb.append("BlocklyMethods.isPositive(");
                mathNumPropFunct.getParam().get(0).visit(this);
                sb.append(")");
                break;
            case NEGATIVE:
                sb.append("BlocklyMethods.isNegative(");
                mathNumPropFunct.getParam().get(0).visit(this);
                sb.append(")");
                break;
            case DIVISIBLE_BY:
                sb.append("BlocklyMethods.isDivisibleBy(");
                mathNumPropFunct.getParam().get(0).visit(this);
                sb.append(", ");
                mathNumPropFunct.getParam().get(1).visit(this);
                sb.append(")");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        switch ( mathOnListFunct.getFunctName() ) {
            case SUM:
                sb.append("BlocklyMethods.sumOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MIN:
                sb.append("BlocklyMethods.minOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MAX:
                sb.append("BlocklyMethods.maxOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case AVERAGE:
                sb.append("BlocklyMethods.averageOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MEDIAN:
                sb.append("BlocklyMethods.medianOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case STD_DEV:
                sb.append("BlocklyMethods.standardDeviatioin(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case RANDOM:
                sb.append("BlocklyMethods.randOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MODE:
                sb.append("BlocklyMethods.modeOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            default:
                break;
        }
        sb.append(")");
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
        sb.append("BlocklyMethods.randDouble()");
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        sb.append("BlocklyMethods.randInt(");
        mathRandomIntFunct.getParam().get(0).visit(this);
        sb.append(", ");
        mathRandomIntFunct.getParam().get(1).visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct<Void> mathSingleFunct) {
        switch ( mathSingleFunct.getFunctName() ) {
            case ROOT:
                sb.append("math.sqrt(");
                break;
            case ABS:
                sb.append("math.fabs(");
                break;
            case LN:
                sb.append("math.log(");
                break;
            case LOG10:
                sb.append("math.log10(");
                break;
            case EXP:
                sb.append("math.exp(");
                break;
            case POW10:
                sb.append("math.pow(10, ");
                break;
            case SIN:
                sb.append("math.sin(");
                break;
            case COS:
                sb.append("math.cos(");
                break;
            case TAN:
                sb.append("math.tan(");
                break;
            case ASIN:
                sb.append("math.asin(");
                break;
            case ATAN:
                sb.append("math.atan(");
                break;
            case ACOS:
                sb.append("math.acos(");
                break;
            case ROUND:
                sb.append("round(");
                break;
            case ROUNDUP:
                sb.append("math.ceil(");
                break;
            case ROUNDDOWN:
                sb.append("math.floor(");
                break;
            default:
                break;
        }
        mathSingleFunct.getParam().get(0).visit(this);
        sb.append(")");

        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct<Void> textJoinFunct) {
        sb.append("BlocklyMethods.textJoin(");
        textJoinFunct.getParam().visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitMethodVoid(MethodVoid<Void> methodVoid) {
        sb.append("\ndef ").append(methodVoid.getMethodName()).append('(');
        methodVoid.getParameters().visit(this);
        sb.append("):");
        methodVoid.getBody().visit(this);
        return null;
    }

    @Override
    public Void visitMethodReturn(MethodReturn<Void> methodReturn) {
        sb.append("\ndef ").append(methodReturn.getMethodName()).append('(');
        methodReturn.getParameters().visit(this);
        sb.append("):");
        methodReturn.getBody().visit(this);
        this.nlIndent();
        sb.append("return ");
        methodReturn.getReturnValue().visit(this);
        return null;
    }

    @Override
    public Void visitMethodIfReturn(MethodIfReturn<Void> methodIfReturn) {
        sb.append("if ");
        methodIfReturn.getCondition().visit(this);
        if ( methodIfReturn.getReturnValue().getKind() != BlockType.EMPTY_EXPR ) {
            sb.append(": return ");
            methodIfReturn.getReturnValue().visit(this);
        } else {
            sb.append(": return None");
        }
        return null;
    }

    @Override
    public Void visitMethodStmt(MethodStmt<Void> methodStmt) {
        methodStmt.getMethod().visit(this);
        return null;
    }

    @Override
    public Void visitMethodCall(MethodCall<Void> methodCall) {
        sb.append(methodCall.getMethodName() + "(");
        methodCall.getParametersValues().visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitBluetoothReceiveAction(BluetoothReceiveAction<Void> bluetoothReadAction) {
        sb.append("hal.readMessage(");
        bluetoothReadAction.getConnection().visit(this);
        sb.append(")");
        return null;
    }

    @Override
    public Void visitBluetoothConnectAction(BluetoothConnectAction<Void> bluetoothConnectAction) {
        sb.append("hal.establishConnectionTo(");
        if ( bluetoothConnectAction.get_address().getKind() != BlockType.STRING_CONST ) {
            sb.append("str(");
            bluetoothConnectAction.get_address().visit(this);
            sb.append(")");
        } else {
            bluetoothConnectAction.get_address().visit(this);
        }
        sb.append(")");
        return null;
    }

    @Override
    public Void visitBluetoothSendAction(BluetoothSendAction<Void> bluetoothSendAction) {
        sb.append("hal.sendMessage(");
        bluetoothSendAction.getConnection().visit(this);
        sb.append(", ");
        if ( bluetoothSendAction.getMsg().getKind() != BlockType.STRING_CONST ) {
            sb.append("str(");
            bluetoothSendAction.getMsg().visit(this);
            sb.append(")");
        } else {
            bluetoothSendAction.getMsg().visit(this);
        }
        sb.append(")");
        return null;
    }

    @Override
    public Void visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction<Void> bluetoothWaitForConnection) {
        sb.append("hal.waitForConnection()");
        return null;
    }

    private boolean parenthesesCheck(Binary<Void> binary) {
        return binary.getOp() == Binary.Op.MINUS
            && binary.getRight().getKind() == BlockType.BINARY
            && binary.getRight().getPrecedence() <= binary.getPrecedence();
    }

    private void generateSubExpr(StringBuilder sb, boolean minusAdaption, Expr<Void> expr, Binary<Void> binary) {
        if ( expr.getPrecedence() >= binary.getPrecedence() && !minusAdaption && expr.getKind() != BlockType.BINARY ) {
            // parentheses are omitted
            expr.visit(this);
        } else {
            sb.append("( ");
            expr.visit(this);
            sb.append(" )");
        }
    }

    private void generateExprCode(Unary<Void> unary, StringBuilder sb) {
        if ( unary.getExpr().getPrecedence() < unary.getPrecedence() ) {
            sb.append("(");
            unary.getExpr().visit(this);
            sb.append(")");
        } else {
            unary.getExpr().visit(this);
        }
    }

    private void generateCodeFromTernary(IfStmt<Void> ifStmt) {
        ((ExprStmt<Void>) ifStmt.getThenList().get(0).get().get(0)).getExpr().visit(this);
        sb.append(" if ( ");
        ifStmt.getExpr().get(0).visit(this);
        sb.append(" ) else ");
        ((ExprStmt<Void>) ifStmt.getElseList().get().get(0)).getExpr().visit(this);
    }

    private void generateCodeFromIfElse(IfStmt<Void> ifStmt) {
        for ( int i = 0; i < ifStmt.getExpr().size(); i++ ) {
            if ( i == 0 ) {
                generateCodeFromStmtCondition("if", ifStmt.getExpr().get(i));
            } else {
                nlIndent();
                generateCodeFromStmtCondition("elif", ifStmt.getExpr().get(i));
            }
            incrIndentation();
            StmtList<Void> then = ifStmt.getThenList().get(i);
            if ( then.get().isEmpty() ) {
                nlIndent();
                sb.append("pass");
            } else {
                then.visit(this);
            }
            decrIndentation();
        }
    }

    private void generateCodeFromElse(IfStmt<Void> ifStmt) {
        if ( ifStmt.getElseList().get().size() != 0 ) {
            nlIndent();
            sb.append("else:");
            incrIndentation();
            ifStmt.getElseList().visit(this);
            decrIndentation();
        }
    }

    private void generateCodeFromStmtCondition(String stmtType, Expr<Void> expr) {
        sb.append(stmtType).append(' ');
        expr.visit(this);
        sb.append(":");
    }

    private void generateCodeFromStmtConditionFor(String stmtType, Expr<Void> expr) {
        sb.append(stmtType).append(' ');
        ExprList<Void> expressions = (ExprList<Void>) expr;
        expressions.get().get(0).visit(this);
        sb.append(" in xrange(");
        expressions.get().get(1).visit(this);
        sb.append(", ");
        expressions.get().get(2).visit(this);
        sb.append(", ");
        expressions.get().get(3).visit(this);
        sb.append("):");
    }

    private void appendBreakStmt(RepeatStmt<Void> repeatStmt) {
        if ( repeatStmt.getMode() == Mode.WAIT ) {
            nlIndent();
            sb.append("break");
        }
    }

    private void generatePrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        sb.append("#!/usr/bin/python\n\n");
        sb.append("from __future__ import absolute_import\n");
        sb.append("from roberta.ev3 import Hal\n");
        sb.append("from roberta.BlocklyMethods import BlocklyMethods\n");
        sb.append("from ev3dev import ev3 as ev3dev\n");
        sb.append("import math\n\n");

        // FIXME: lejos uses this to stop programs
        sb.append("TRUE = True\n");
        sb.append(generateRegenerateConfiguration()).append("\n");
        sb.append("hal = Hal(_brickConfiguration)\n");
    }

    private void generateSuffix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        sb.append("\n\n");
        sb.append("def main():\n");
        sb.append(INDENT).append("try:\n");
        sb.append(INDENT).append(INDENT).append("run()\n");
        sb.append(INDENT).append("except Exception as e:\n");
        sb.append(INDENT).append(INDENT).append("hal.drawText('Fehler im EV3', 0, 0)\n");
        sb.append(INDENT).append(INDENT).append("hal.drawText(e.__class__.__name__, 0, 1)\n");
        sb.append(INDENT).append(INDENT).append("if e.message:\n");
        sb.append(INDENT).append(INDENT).append(INDENT).append("hal.drawText(e.message, 0, 2)\n");
        sb.append(INDENT).append(INDENT).append("hal.drawText('Press any key', 0, 4)\n");
        sb.append(INDENT).append(INDENT).append("while not hal.isKeyPressed('any'): hal.waitFor(500)\n");
        sb.append(INDENT).append(INDENT).append("raise\n");

        sb.append("\n");
        sb.append("if __name__ == \"__main__\":\n");
        sb.append(INDENT).append("main()");
    }

    private String generateRegenerateConfiguration() {
        StringBuilder sb = new StringBuilder();
        sb.append("_brickConfiguration = {\n");
        sb.append("    'wheel-diameter': " + brickConfiguration.getWheelDiameterCM() + ",\n");
        sb.append("    'track-width': " + brickConfiguration.getTrackWidthCM() + ",\n");
        appendActors(sb);
        appendSensors(sb);
        sb.append("}");
        return sb.toString();
    }

    private void appendActors(StringBuilder sb) {
        sb.append("    'actors': {\n");
        for ( Map.Entry<IActorPort, Actor> entry : brickConfiguration.getActors().entrySet() ) {
            Actor actor = entry.getValue();
            if ( actor != null ) {
                IActorPort port = entry.getKey();
                sb.append("        '").append(port.toString()).append("':");
                sb.append(generateRegenerateActor(actor, port));
                sb.append(",\n");
            }
        }
        sb.append("    },\n");
    }

    private boolean isSensorUsed(Sensor sensor, ISensorPort port) {
        for ( UsedSensor usedSensor : usedSensors ) {
            if ( port == usedSensor.getPort() && sensor.getType() == usedSensor.getType() ) {
                return true;
            }
        }
        return false;
    }

    private void appendSensors(StringBuilder sb) {
        sb.append("    'sensors': {\n");
        for ( Map.Entry<ISensorPort, Sensor> entry : brickConfiguration.getSensors().entrySet() ) {
            Sensor sensor = entry.getValue();
            ISensorPort port = entry.getKey();
            if ( sensor != null && isSensorUsed(sensor, port) ) {
                sb.append("        '").append(port.getPortNumber()).append("':");
                sb.append(generateRegenerateSensor(sensor, port));
                sb.append(",\n");
            }
        }
        sb.append("    },\n");
    }

    private static String generateRegenerateActor(Actor actor, IActorPort port) {
        StringBuilder sb = new StringBuilder();
        // FIXME: that won't scale
        String name = null;
        switch ( actor.getName() ) {
            case MEDIUM:
                name = "MediumMotor";
                break;
            case LARGE:
                name = "LargeMotor";
                break;
            default:
                throw new IllegalArgumentException("no mapping for " + actor.getName() + "to ev3dev-lang-python");
        }

        sb.append("Hal.make").append(name).append("(ev3dev.OUTPUT_").append(port.toString());
        sb.append(", ").append(actor.isRegulated() ? "'on'" : "'off'");
        sb.append(", ").append(getEnumCode(actor.getRotationDirection()));
        sb.append(", ").append(getEnumCode(actor.getMotorSide()));
        sb.append(")");
        return sb.toString();
    }

    private static String generateRegenerateSensor(Sensor sensor, ISensorPort port) {
        StringBuilder sb = new StringBuilder();
        // FIXME: that won't scale
        String name = null;
        // [m for m in dir(ev3dev) if m.find("_sensor") != -1]
        // ['ColorSensor', 'GyroSensor', 'I2cSensor', 'InfraredSensor', 'LightSensor', 'SoundSensor', 'TouchSensor', 'UltrasonicSensor']
        switch ( sensor.getType() ) {
            case COLOR:
                name = "ColorSensor";
                break;
            case GYRO:
                name = "GyroSensor";
                break;
            case INFRARED:
                name = "InfraredSensor";
                break;
            case TOUCH:
                name = "TouchSensor";
                break;
            case ULTRASONIC:
                name = "UltrasonicSensor";
                break;
            default:
                throw new IllegalArgumentException("no mapping for " + sensor.getType() + "to ev3dev-lang-python");
        }
        sb.append("Hal.make").append(name).append("(ev3dev.INPUT_").append(port.getPortNumber()).append(")");
        return sb.toString();
    }

    private static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch ( NumberFormatException e ) {
            return false;
        }
    }

    @Override
    public Void visitLightSensorAction(LightSensorAction<Void> lightSensorAction) {
        // TODO Auto-generated method stub
        return null;
    }

}
