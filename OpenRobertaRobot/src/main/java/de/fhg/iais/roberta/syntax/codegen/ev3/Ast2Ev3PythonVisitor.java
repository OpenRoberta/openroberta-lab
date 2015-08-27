package de.fhg.iais.roberta.syntax.codegen.ev3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringEscapeUtils;

import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.components.HardwareComponent;
import de.fhg.iais.roberta.components.HardwareComponentType;
import de.fhg.iais.roberta.components.ev3.EV3Actor;
import de.fhg.iais.roberta.components.ev3.EV3Sensor;
import de.fhg.iais.roberta.components.ev3.EV3Sensors;
import de.fhg.iais.roberta.components.ev3.Ev3Configuration;
import de.fhg.iais.roberta.shared.IndexLocation;
import de.fhg.iais.roberta.shared.action.ev3.ActorPort;
import de.fhg.iais.roberta.shared.sensor.ev3.GyroSensorMode;
import de.fhg.iais.roberta.shared.sensor.ev3.MotorTachoMode;
import de.fhg.iais.roberta.shared.sensor.ev3.SensorPort;
import de.fhg.iais.roberta.shared.sensor.ev3.UltrasonicSensorMode;
import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.ev3.BluetoothConnectAction;
import de.fhg.iais.roberta.syntax.action.ev3.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.ev3.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.ev3.BluetoothWaitForConnectionAction;
import de.fhg.iais.roberta.syntax.action.ev3.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.ev3.DriveAction;
import de.fhg.iais.roberta.syntax.action.ev3.LightAction;
import de.fhg.iais.roberta.syntax.action.ev3.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.ev3.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.ev3.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.ev3.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.ev3.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.ev3.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.ev3.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.ev3.ShowPictureAction;
import de.fhg.iais.roberta.syntax.action.ev3.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.ev3.ToneAction;
import de.fhg.iais.roberta.syntax.action.ev3.TurnAction;
import de.fhg.iais.roberta.syntax.action.ev3.VolumeAction;
import de.fhg.iais.roberta.syntax.blocksequence.ActivityTask;
import de.fhg.iais.roberta.syntax.blocksequence.Location;
import de.fhg.iais.roberta.syntax.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.blocksequence.StartActivityTask;
import de.fhg.iais.roberta.syntax.expr.ActionExpr;
import de.fhg.iais.roberta.syntax.expr.Binary;
import de.fhg.iais.roberta.syntax.expr.Binary.Op;
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
import de.fhg.iais.roberta.syntax.expr.StringConst;
import de.fhg.iais.roberta.syntax.expr.Unary;
import de.fhg.iais.roberta.syntax.expr.Var;
import de.fhg.iais.roberta.syntax.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.functions.LenghtOfIsEmptyFunct;
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
import de.fhg.iais.roberta.syntax.hardwarecheck.ev3.UsedSensorsCheckVisitor;
import de.fhg.iais.roberta.syntax.methods.MethodCall;
import de.fhg.iais.roberta.syntax.methods.MethodIfReturn;
import de.fhg.iais.roberta.syntax.methods.MethodReturn;
import de.fhg.iais.roberta.syntax.methods.MethodVoid;
import de.fhg.iais.roberta.syntax.sensor.ev3.BrickSensor;
import de.fhg.iais.roberta.syntax.sensor.ev3.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.ev3.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.ev3.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.ev3.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.ev3.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.ev3.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.ev3.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.ev3.UltrasonicSensor;
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
public class Ast2Ev3PythonVisitor implements AstVisitor<Void> {
    public static final String INDENT = "    ";

    private final Ev3Configuration brickConfiguration;
    private final String programName;
    private final StringBuilder sb = new StringBuilder();
    private final Set<EV3Sensors> usedSensors;
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
    Ast2Ev3PythonVisitor(String programName, Ev3Configuration brickConfiguration, Set<EV3Sensors> usedSensors, int indentation) {
        this.programName = programName;
        this.brickConfiguration = brickConfiguration;
        this.indentation = indentation;
        this.usedSensors = usedSensors;
        for ( int i = 0; i < indentation; i++ ) {
            this.indent.append(INDENT);
        }
    }

    /**
     * factory method to generate Python code from an AST.<br>
     *
     * @param programName name of the program
     * @param brickConfiguration hardware configuration of the brick
     * @param phrases to generate the code from
     */
    public static String generate(String programName, Ev3Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) //
    {
        Assert.notNull(programName);
        Assert.notNull(brickConfiguration);
        Assert.isTrue(phrasesSet.size() >= 1);

        Set<EV3Sensors> usedSensors = UsedSensorsCheckVisitor.check(phrasesSet);
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
        return this.indentation;
    }

    /**
     * Get the string builder of the visitor. Meaningful for tests only.
     *
     * @return (current state of) the string builder
     */
    public StringBuilder getSb() {
        return this.sb;
    }

    private void incrIndentation() {
        this.indentation += 1;
        this.indent.append(INDENT);
    }

    private void decrIndentation() {
        this.indentation -= 1;
        this.indent.delete(0, INDENT.length());
    }

    private void nlIndent() {
        this.sb.append("\n").append(this.indent);
    }

    private static String getEnumCode(@SuppressWarnings("rawtypes") Enum value) {
        return "'" + value.toString().toLowerCase() + "'";
    }

    @Override
    public Void visitNumConst(NumConst<Void> numConst) {
        if ( isInteger(numConst.getValue()) ) {
            this.sb.append(numConst.getValue());
        } else {
            this.sb.append("float(");
            this.sb.append(numConst.getValue());
            this.sb.append(")");
        }
        return null;
    }

    @Override
    public Void visitBoolConst(BoolConst<Void> boolConst) {
        this.sb.append(boolConst.isValue() ? "True" : "False");
        return null;
    };

    @Override
    public Void visitMathConst(MathConst<Void> mathConst) {
        switch ( mathConst.getMathConst() ) {
            case PI:
                this.sb.append("math.pi");
                break;
            case E:
                this.sb.append("math.e");
                break;
            case GOLDEN_RATIO:
                this.sb.append("BlocklyMethods.GOLDEN_RATIO");
                break;
            case SQRT2:
                this.sb.append("math.sqrt(2)");
                break;
            case SQRT1_2:
                this.sb.append("math.sqrt(1.0/2.0)");
                break;
            case INFINITY:
                this.sb.append("float('inf')");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst<Void> colorConst) {
        this.sb.append(getEnumCode(colorConst.getValue()));
        return null;
    }

    @Override
    public Void visitStringConst(StringConst<Void> stringConst) {
        this.sb.append("\"").append(StringEscapeUtils.escapeJava(stringConst.getValue())).append("\"");
        return null;
    }

    @Override
    public Void visitNullConst(NullConst<Void> nullConst) {
        this.sb.append("None");
        return null;
    }

    @Override
    public Void visitVar(Var<Void> var) {
        this.sb.append(var.getValue());
        return null;
    }

    @Override
    public Void visitVarDeclaration(VarDeclaration<Void> var) {
        this.sb.append(var.getName());
        if ( var.getValue().getKind() != BlockType.EMPTY_EXPR ) {
            this.sb.append(" = ");
            var.getValue().visit(this);
        }
        return null;
    }

    @Override
    public Void visitUnary(Unary<Void> unary) {
        if ( unary.getOp() == Unary.Op.POSTFIX_INCREMENTS ) {
            generateExprCode(unary, this.sb);
            this.sb.append(unary.getOp().getOpSymbol());
        } else {
            this.sb.append(unary.getOp().getOpSymbol());
            generateExprCode(unary, this.sb);
        }
        return null;
    }

    @Override
    public Void visitBinary(Binary<Void> binary) {
        generateSubExpr(this.sb, false, binary.getLeft(), binary);
        this.sb.append(' ').append(binary.getOp().getOpSymbol()).append(' ');
        if ( binary.getOp() == Op.TEXT_APPEND ) {
            this.sb.append("str(");
            generateSubExpr(this.sb, parenthesesCheck(binary), binary.getRight(), binary);
            this.sb.append(")");
        } else {
            generateSubExpr(this.sb, parenthesesCheck(binary), binary.getRight(), binary);
        }
        if ( binary.getOp() == Op.MATH_CHANGE ) {
            this.sb.append(";"); // FIXME
        }
        return null;
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
        switch ( emptyExpr.getDefVal().getName() ) { // FIXME
            case "java.lang.String":
                this.sb.append("\"\"");
                break;
            case "java.lang.Boolean":
                this.sb.append("True");
                break;
            case "java.lang.Integer":
                this.sb.append("0");
                break;
            case "java.util.ArrayList":
                break;
            case "de.fhg.iais.roberta.syntax.expr.NullConst":
                break;
            default:
                this.sb.append("[[EmptyExpr [defVal=" + emptyExpr.getDefVal() + "]]]");
                break;
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
                        this.sb.append("; "); // FIXME
                    } else {
                        this.sb.append(", ");
                    }
                }
                expr.visit(this);
            }
        }
        return null;
    }

    @Override
    public Void visitFunc(MathPowerFunct<Void> funct) {
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
        this.sb.append(" = ");
        assignStmt.getExpr().visit(this);
        return null;
    }

    @Override
    public Void visitExprStmt(ExprStmt<Void> exprStmt) {
        exprStmt.getExpr().visit(this);
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
                this.sb.append("if TRUE:");
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
        repeatStmt.getList().visit(this);
        appendBreakStmt(repeatStmt);
        decrIndentation();
        nlIndent();
        if ( additionalClosingScope ) {
            decrIndentation();
            nlIndent();
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
        this.sb.append(stmtFlowCon.getFlow().toString().toLowerCase() + ";");
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
        this.sb.append("if TRUE:");
        incrIndentation();
        nlIndent();
        this.sb.append("while True:");
        incrIndentation();
        visitStmtList(waitStmt.getStatements());
        nlIndent();
        this.sb.append("hal.waitFor(15)");
        decrIndentation();
        decrIndentation();
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        this.sb.append("hal.waitFor(");
        waitTimeStmt.getTime().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        this.sb.append("hal.clearDisplay()");
        return null;
    }

    @Override
    public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        switch ( volumeAction.getMode() ) {
            case SET:
                this.sb.append("hal.setVolume(");
                volumeAction.getVolume().visit(this);
                this.sb.append(")");
                break;
            case GET:
                this.sb.append("hal.getVolume()");
                break;
            default:
                throw new DbcException("Invalid volume action mode!");
        }
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        this.sb.append("hal.ledOn(" + getEnumCode(lightAction.getColor()) + ", " + getEnumCode(lightAction.getBlinkMode()) + ")");
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        switch ( lightStatusAction.getStatus() ) {
            case OFF:
                this.sb.append("hal.ledOff()");
                break;
            case RESET:
                this.sb.append("hal.resetLED()");
                break;
            default:
                throw new DbcException("Invalid LED status mode!");
        }
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        this.sb.append("hal.playFile(" + playFileAction.getFileName() + ")");
        return null;
    }

    @Override
    public Void visitShowPictureAction(ShowPictureAction<Void> showPictureAction) {
        this.sb.append("hal.drawPicture(" + getEnumCode(showPictureAction.getPicture()) + ", ");
        showPictureAction.getX().visit(this);
        this.sb.append(", ");
        showPictureAction.getY().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
        this.sb.append("hal.drawText(");
        if ( showTextAction.getMsg().getKind() != BlockType.STRING_CONST ) {
            this.sb.append("str(");
            showTextAction.getMsg().visit(this);
            this.sb.append(")");
        } else {
            showTextAction.getMsg().visit(this);
        }
        this.sb.append(", ");
        showTextAction.getX().visit(this);
        this.sb.append(", ");
        showTextAction.getY().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        this.sb.append("hal.playTone(");
        toneAction.getFrequency().visit(this);
        this.sb.append(", ");
        toneAction.getDuration().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        String methodName;
        boolean isRegulated = this.brickConfiguration.isMotorRegulated(motorOnAction.getPort());
        boolean duration = motorOnAction.getParam().getDuration() != null;
        if ( duration ) {
            methodName = isRegulated ? "hal.rotateRegulatedMotor('" : "hal.rotateUnregulatedMotor('";
        } else {
            methodName = isRegulated ? "hal.turnOnRegulatedMotor('" : "hal.turnOnUnregulatedMotor('";
        }
        this.sb.append(methodName + motorOnAction.getPort().toString() + "', ");
        motorOnAction.getParam().getSpeed().visit(this);
        if ( duration ) {
            this.sb.append(", " + getEnumCode(motorOnAction.getDurationMode()));
            this.sb.append(", ");
            motorOnAction.getDurationValue().visit(this);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        boolean isRegulated = this.brickConfiguration.isMotorRegulated(motorSetPowerAction.getPort());
        String methodName = isRegulated ? "hal.setRegulatedMotorSpeed('" : "hal.setUnregulatedMotorSpeed('";
        this.sb.append(methodName + motorSetPowerAction.getPort().toString() + ", ");
        motorSetPowerAction.getPower().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        boolean isRegulated = this.brickConfiguration.isMotorRegulated(motorGetPowerAction.getPort());
        String methodName = isRegulated ? "hal.getRegulatedMotorSpeed('" : "hal.getUnregulatedMotorSpeed('";
        this.sb.append(methodName + motorGetPowerAction.getPort().toString() + "')");
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        this.sb.append("hal.stopMotor('").append(motorStopAction.getPort().toString()).append("', ").append(getEnumCode(motorStopAction.getMode())).append(')');
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        boolean isDuration = driveAction.getParam().getDuration() != null;
        String methodName = isDuration ? "hal.driveDistance(" : "hal.regulatedDrive(";
        this.sb.append(methodName);
        this.sb.append("'" + this.brickConfiguration.getLeftMotorPort().toString() + "', ");
        this.sb.append("'" + this.brickConfiguration.getRightMotorPort().toString() + "', False, ");
        this.sb.append(getEnumCode(driveAction.getDirection()) + ", ");
        driveAction.getParam().getSpeed().visit(this);
        if ( isDuration ) {
            this.sb.append(", ");
            driveAction.getParam().getDuration().getValue().visit(this);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        boolean isDuration = turnAction.getParam().getDuration() != null;
        boolean isRegulated = this.brickConfiguration.getActorOnPort(this.brickConfiguration.getLeftMotorPort()).isRegulated();
        String methodName = "hal.rotateDirection" + (isDuration ? "Angle" : isRegulated ? "Regulated" : "Unregulated") + "(";
        this.sb.append(methodName);
        this.sb.append("'" + this.brickConfiguration.getLeftMotorPort().toString() + "', ");
        this.sb.append("'" + this.brickConfiguration.getRightMotorPort().toString() + "', False, ");
        this.sb.append(getEnumCode(turnAction.getDirection()) + ", ");
        turnAction.getParam().getSpeed().visit(this);
        if ( isDuration ) {
            this.sb.append(", ");
            turnAction.getParam().getDuration().getValue().visit(this);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        this.sb.append("hal.stopMotors(");
        this.sb.append("'" + this.brickConfiguration.getLeftMotorPort().toString() + "', ");
        this.sb.append("'" + this.brickConfiguration.getRightMotorPort().toString() + "')");
        return null;
    }

    @Override
    public Void visitBrickSensor(BrickSensor<Void> brickSensor) {
        switch ( brickSensor.getMode() ) {
            case IS_PRESSED:
                this.sb.append("hal.isKeyPressed(" + getEnumCode(brickSensor.getKey()) + ")");
                break;
            case WAIT_FOR_PRESS_AND_RELEASE:
                this.sb.append("hal.isKeyPressedAndReleased(" + getEnumCode(brickSensor.getKey()) + ")");
                break;
            default:
                throw new DbcException("Invalide mode for BrickSensor!");
        }
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        String colorSensorPort = colorSensor.getPort().getPortNumber();
        switch ( colorSensor.getMode() ) {
            case AMBIENTLIGHT:
                this.sb.append("hal.getColorSensorAmbient('" + colorSensorPort + "')");
                break;
            case COLOUR:
                this.sb.append("hal.getColorSensorColour('" + colorSensorPort + "')");
                break;
            case RED:
                this.sb.append("hal.getColorSensorRed('" + colorSensorPort + "')");
                break;
            case RGB:
                this.sb.append("hal.getColorSensorRgb('" + colorSensorPort + "')");
                break;
            default:
                throw new DbcException("Invalide mode for Color Sensor!");
        }
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        boolean isRegulated = this.brickConfiguration.isMotorRegulated(encoderSensor.getMotor());
        String encoderSensorPort = getEnumCode(encoderSensor.getMotor());
        if ( encoderSensor.getMode() == MotorTachoMode.RESET ) {
            String methodName = isRegulated ? "hal.resetRegulatedMotorTacho(" : "hal.resetUnregulatedMotorTacho(";
            this.sb.append(methodName + encoderSensorPort + ")");
        } else {
            String methodName = isRegulated ? "hal.getRegulatedMotorTachoValue(" : "hal.getUnregulatedMotorTachoValue(";
            this.sb.append(methodName + encoderSensorPort + ", " + getEnumCode(encoderSensor.getMode()) + ")");
        }
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        String gyroSensorPort = gyroSensor.getPort().getPortNumber();
        if ( gyroSensor.getMode() == GyroSensorMode.RESET ) {
            this.sb.append("hal.resetGyroSensor('" + gyroSensorPort + "')");
        } else {
            this.sb.append("hal.getGyroSensorValue('" + gyroSensorPort + ", " + getEnumCode(gyroSensor.getMode()) + "')");
        }
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        String infraredSensorPort = infraredSensor.getPort().getPortNumber();
        switch ( infraredSensor.getMode() ) {
            case DISTANCE:
                this.sb.append("hal.getInfraredSensorDistance('" + infraredSensorPort + "')");
                break;
            case SEEK:
                this.sb.append("hal.getInfraredSensorSeek('" + infraredSensorPort + "')");
                break;
            default:
                throw new DbcException("Invalid Infrared Sensor Mode!");
        }

        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        switch ( timerSensor.getMode() ) {
            case GET_SAMPLE:
                this.sb.append("hal.getTimerValue(" + timerSensor.getTimer() + ")");
                break;
            case RESET:
                this.sb.append("hal.resetTimer(" + timerSensor.getTimer() + ")");
                break;
            default:
                throw new DbcException("Invalid Time Mode!");
        }
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        this.sb.append("hal.isPressed('" + touchSensor.getPort().getPortNumber() + "')");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        String ultrasonicSensorPort = ultrasonicSensor.getPort().getPortNumber();
        if ( ultrasonicSensor.getMode() == UltrasonicSensorMode.DISTANCE ) {
            this.sb.append("hal.getUltraSonicSensorDistance('" + ultrasonicSensorPort + "')");
        } else {
            this.sb.append("hal.getUltraSonicSensorPresence('" + ultrasonicSensorPort + "')");
        }
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        this.sb.append("\n").append("def run():");
        incrIndentation();
        mainTask.getVariables().visit(this);
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
        this.sb.append("print(");
        textPrintFunct.getParam().get(0).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitFunctionStmt(FunctionStmt<Void> functionStmt) {
        functionStmt.getFunction().visit(this);
        this.sb.append(";");
        return null;
    }

    @Override
    public Void visitFunctionExpr(FunctionExpr<Void> functionExpr) {
        functionExpr.getFunction().visit(this);
        return null;
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct<Void> getSubFunct) {
        this.sb.append("BlocklyMethods.listsGetSubList( ");
        getSubFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        IndexLocation where1 = IndexLocation.get(getSubFunct.getStrParam().get(0));
        this.sb.append(getEnumCode(where1));
        if ( where1 == IndexLocation.FROM_START || where1 == IndexLocation.FROM_END ) {
            this.sb.append(", ");
            getSubFunct.getParam().get(1).visit(this);
        }
        this.sb.append(", ");
        IndexLocation where2 = IndexLocation.get(getSubFunct.getStrParam().get(1));
        this.sb.append(getEnumCode(where2));
        if ( where2 == IndexLocation.FROM_START || where2 == IndexLocation.FROM_END ) {
            this.sb.append(", ");
            if ( getSubFunct.getParam().size() == 3 ) {
                getSubFunct.getParam().get(2).visit(this);
            } else {
                getSubFunct.getParam().get(1).visit(this);
            }
        }
        this.sb.append(")");
        return null;

    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        switch ( indexOfFunct.getLocation() ) {
            case FIRST:
                this.sb.append("BlocklyMethods.findFirst( ");
                indexOfFunct.getParam().get(0).visit(this);
                this.sb.append(", ");
                indexOfFunct.getParam().get(1).visit(this);
                this.sb.append(")");
                break;
            case LAST:
                this.sb.append("BlocklyMethods.findLast( ");
                indexOfFunct.getParam().get(0).visit(this);
                this.sb.append(", ");
                indexOfFunct.getParam().get(1).visit(this);
                this.sb.append(")");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitLenghtOfIsEmptyFunct(LenghtOfIsEmptyFunct<Void> lenghtOfIsEmptyFunct) {
        switch ( lenghtOfIsEmptyFunct.getFunctName() ) {
            case LISTS_LENGTH:
                this.sb.append("BlocklyMethods.lenght( ");
                lenghtOfIsEmptyFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;

            case LIST_IS_EMPTY:
                this.sb.append("BlocklyMethods.isEmpty( ");
                lenghtOfIsEmptyFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitEmptyList(EmptyList<Void> emptyList) {
        this.sb.append("[]");
        return null;
    }

    @Override
    public Void visitListCreate(ListCreate<Void> listCreate) {
        this.sb.append("BlocklyMethods.createListWith(");
        listCreate.getValue().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitListRepeat(ListRepeat<Void> listRepeat) {
        this.sb.append("BlocklyMethods.createListWithItem(");
        listRepeat.getParam().get(0).visit(this);
        this.sb.append(", ");
        listRepeat.getParam().get(1).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        this.sb.append("BlocklyMethods.listsIndex(");
        listGetIndex.getParam().get(0).visit(this);
        this.sb.append(", ");
        this.sb.append(getEnumCode(listGetIndex.getElementOperation()));
        this.sb.append(", ");
        this.sb.append(getEnumCode(listGetIndex.getLocation()));
        if ( listGetIndex.getParam().size() == 2 ) {
            this.sb.append(", ");
            listGetIndex.getParam().get(1).visit(this);
        }
        this.sb.append(")");
        //if ( listGetIndex.getElementOperation().isStatment() ) {
        //    this.sb.append(";");
        //}
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        this.sb.append("BlocklyMethods.listsIndex(");
        listSetIndex.getParam().get(0).visit(this);
        this.sb.append(", ");
        this.sb.append(getEnumCode(listSetIndex.getElementOperation()));
        this.sb.append(", ");
        listSetIndex.getParam().get(1).visit(this);
        this.sb.append(", ");
        this.sb.append(getEnumCode(listSetIndex.getLocation()));
        if ( listSetIndex.getParam().size() == 3 ) {
            this.sb.append(", ");
            listSetIndex.getParam().get(2).visit(this);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct<Void> mathConstrainFunct) {
        this.sb.append("BlocklyMethods.clamp(");
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
                this.sb.append("BlocklyMethods.isEven(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case ODD:
                this.sb.append("BlocklyMethods.isOdd(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case PRIME:
                this.sb.append("BlocklyMethods.isPrime(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case WHOLE:
                this.sb.append("BlocklyMethods.isWhole(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case POSITIVE:
                this.sb.append("BlocklyMethods.isPositive(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case NEGATIVE:
                this.sb.append("BlocklyMethods.isNegative(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case DIVISIBLE_BY:
                this.sb.append("BlocklyMethods.isDivisibleBy(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(", ");
                mathNumPropFunct.getParam().get(1).visit(this);
                this.sb.append(")");
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
                this.sb.append("BlocklyMethods.sumOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MIN:
                this.sb.append("BlocklyMethods.minOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MAX:
                this.sb.append("BlocklyMethods.maxOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case AVERAGE:
                this.sb.append("BlocklyMethods.averageOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MEDIAN:
                this.sb.append("BlocklyMethods.medianOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case STD_DEV:
                this.sb.append("BlocklyMethods.standardDeviatioin(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case RANDOM:
                this.sb.append("BlocklyMethods.randOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MODE:
                this.sb.append("BlocklyMethods.modeOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            default:
                break;
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
        this.sb.append("BlocklyMethods.randDouble()");
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        this.sb.append("BlocklyMethods.randInt(");
        mathRandomIntFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        mathRandomIntFunct.getParam().get(1).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathSingleFunct(MathSingleFunct<Void> mathSingleFunct) {
        switch ( mathSingleFunct.getFunctName() ) {
            case ROOT:
                this.sb.append("math.sqrt(");
                break;
            case ABS:
                this.sb.append("math.fabs(");
                break;
            case LN:
                this.sb.append("math.log(");
                break;
            case LOG10:
                this.sb.append("math.log10(");
                break;
            case EXP:
                this.sb.append("math.exp(");
                break;
            case POW10:
                this.sb.append("math.pow(10, ");
                break;
            case SIN:
                this.sb.append("math.sin(");
                break;
            case COS:
                this.sb.append("math.cos(");
                break;
            case TAN:
                this.sb.append("math.tan(");
                break;
            case ASIN:
                this.sb.append("math.asin(");
                break;
            case ATAN:
                this.sb.append("math.atan(");
                break;
            case ACOS:
                this.sb.append("math.acos(");
                break;
            case ROUND:
                this.sb.append("round(");
                break;
            case ROUNDUP:
                this.sb.append("math.ceil(");
                break;
            case ROUNDDOWN:
                this.sb.append("math.floor(");
                break;
            default:
                break;
        }
        mathSingleFunct.getParam().get(0).visit(this);
        this.sb.append(")");

        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct<Void> textJoinFunct) {
        boolean isFirst = true;
        List<Expr<Void>> params = textJoinFunct.getParam();
        this.sb.append("BlocklyMethods.textJoin(");
        for ( Expr<Void> expr : params ) {
            if ( isFirst ) {
                isFirst = false;
            } else {
                this.sb.append(", ");
            }
            expr.visit(this);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMethodVoid(MethodVoid<Void> methodVoid) {
        this.sb.append("\n").append("def ");
        this.sb.append(methodVoid.getMethodName() + "(");
        methodVoid.getParameters().visit(this);
        this.sb.append("):");
        methodVoid.getBody().visit(this);
        return null;
    }

    @Override
    public Void visitMethodReturn(MethodReturn<Void> methodReturn) {
        this.sb.append("\n").append("def ");
        this.sb.append(" " + methodReturn.getMethodName() + "(");
        methodReturn.getParameters().visit(this);
        this.sb.append("):");
        methodReturn.getBody().visit(this);
        this.nlIndent();
        this.sb.append("return ");
        methodReturn.getReturnValue().visit(this);
        return null;
    }

    @Override
    public Void visitMethodIfReturn(MethodIfReturn<Void> methodIfReturn) {
        this.sb.append("if ");
        methodIfReturn.getCondition().visit(this);
        this.sb.append(": return");
        methodIfReturn.getReturnValue().visit(this);
        return null;
    }

    @Override
    public Void visitMethodStmt(MethodStmt<Void> methodStmt) {
        methodStmt.getMethod().visit(this);
        //this.sb.append(";");
        return null;
    }

    @Override
    public Void visitMethodCall(MethodCall<Void> methodCall) {
        this.sb.append(methodCall.getMethodName() + "(");
        methodCall.getParametersValues().visit(this);
        this.sb.append(")");
        if ( methodCall.getReturnType() == null ) {
            //this.sb.append(";");
        }
        return null;
    }

    @Override
    public Void visitBluetoothReceiveAction(BluetoothReceiveAction<Void> clearDisplayAction) {
        this.sb.append("hal.readMessage()");
        return null;
    }

    @Override
    public Void visitBluetoothConnectAction(BluetoothConnectAction<Void> bluetoothConnectAction) {
        this.sb.append("hal.establishConnectionTo(");
        if ( bluetoothConnectAction.get_address().getKind() != BlockType.STRING_CONST ) {
            this.sb.append("str(");
            bluetoothConnectAction.get_address().visit(this);
            this.sb.append(")");
        } else {
            bluetoothConnectAction.get_address().visit(this);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitBluetoothSendAction(BluetoothSendAction<Void> bluetoothSendAction) {
        this.sb.append("hal.sendMessage(");
        if ( bluetoothSendAction.get_msg().getKind() != BlockType.STRING_CONST ) {
            this.sb.append("str(");
            bluetoothSendAction.get_msg().visit(this);
            this.sb.append(")");
        } else {
            bluetoothSendAction.get_msg().visit(this);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction<Void> bluetoothWaitForConnection) {
        this.sb.append("hal.waitForConnection()");
        return null;
    }

    private boolean parenthesesCheck(Binary<Void> binary) {
        return binary.getOp() == Op.MINUS && binary.getRight().getKind() == BlockType.BINARY && binary.getRight().getPrecedence() <= binary.getPrecedence();
    }

    private void generateSubExpr(StringBuilder sb, boolean minusAdaption, Expr<Void> expr, Binary<Void> binary) {
        if ( expr.getPrecedence() >= binary.getPrecedence() && !minusAdaption ) {
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
        this.sb.append("if ( ");
        ifStmt.getExpr().get(0).visit(this);
        this.sb.append(" ) else ");
        ((ExprStmt<Void>) ifStmt.getElseList().get().get(0)).getExpr().visit(this);
    }

    private void generateCodeFromIfElse(IfStmt<Void> ifStmt) {
        for ( int i = 0; i < ifStmt.getExpr().size(); i++ ) {
            if ( i == 0 ) {
                generateCodeFromStmtCondition("if", ifStmt.getExpr().get(i));
            } else {
                generateCodeFromStmtCondition("else if", ifStmt.getExpr().get(i));
            }
            incrIndentation();
            ifStmt.getThenList().get(i).visit(this);
            decrIndentation();
            if ( i + 1 < ifStmt.getExpr().size() ) {
                nlIndent();
                //this.sb.append("} "));
            }
        }
    }

    private void generateCodeFromElse(IfStmt<Void> ifStmt) {
        if ( ifStmt.getElseList().get().size() != 0 ) {
            nlIndent();
            this.sb.append("else:");
            incrIndentation();
            ifStmt.getElseList().visit(this);
            decrIndentation();
        }
        nlIndent();
    }

    private void generateCodeFromStmtCondition(String stmtType, Expr<Void> expr) {
        this.sb.append(stmtType).append(' ');
        expr.visit(this);
        this.sb.append(" :");
    }

    private void generateCodeFromStmtConditionFor(String stmtType, Expr<Void> expr) {
        this.sb.append(stmtType).append(' ');
        ExprList<Void> expressions = (ExprList<Void>) expr;
        expressions.get().get(0).visit(this);
        this.sb.append(" in xrange(");
        expressions.get().get(1).visit(this);
        this.sb.append(", ");
        expressions.get().get(2).visit(this);
        this.sb.append(", ");
        expressions.get().get(3).visit(this);
        this.sb.append("):");
    }

    private void appendBreakStmt(RepeatStmt<Void> repeatStmt) {
        if ( repeatStmt.getMode() == Mode.WAIT ) {
            nlIndent();
            this.sb.append("break;");
        }
    }

    private void generatePrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        this.sb.append("#!/usr/bin/python\n\n");
        this.sb.append("from __future__ import absolute_import\n");
        this.sb.append("from roberta.ev3 import Hal,BlocklyMethods\n");
        this.sb.append("from sets import Set\n");
        this.sb.append("import ev3dev\n");
        this.sb.append("import math\n\n");

        this.sb.append("TRUE = True;\n");
        this.sb.append(generateRegenerateConfiguration()).append("\n");
        this.sb.append(generateRegenerateUsedSensors()).append("\n");
        this.sb.append("hal = Hal(brickConfiguration, usedSensors)\n");
    }

    private void generateSuffix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        this.sb.append("\n\n");
        this.sb.append("def main():\n");
        this.sb.append(INDENT).append("try:\n");
        this.sb.append(INDENT).append(INDENT).append("run()\n");
        this.sb.append(INDENT).append("except Exception as e:\n");
        this.sb.append(INDENT).append(INDENT).append("hal.drawText('Fehler im EV3', 0, 0)\n");
        this.sb.append(INDENT).append(INDENT).append("if e.message:\n");
        this.sb.append(INDENT).append(INDENT).append(INDENT).append("hal.drawText(e.message, 0, 1)\n");
        this.sb.append(INDENT).append(INDENT).append("hal.drawText('Press any key', 0, 3)\n");
        this.sb.append(INDENT).append(INDENT).append("while not hal.isKeyPressed('any'): hal.waitFor(500)\n");
        this.sb.append(INDENT).append(INDENT).append("raise\n");

        this.sb.append("\n");
        this.sb.append("if __name__ == \"__main__\":\n");
        this.sb.append(INDENT).append("main()");
    }

    private String generateRegenerateConfiguration() {
        StringBuilder sb = new StringBuilder();
        sb.append("brickConfiguration = {\n");
        sb.append("    'wheel-diameter': " + this.brickConfiguration.getWheelDiameterCM() + ",\n");
        sb.append("    'track-width': " + this.brickConfiguration.getTrackWidthCM() + ",\n");
        appendActors(sb);
        appendSensors(sb);
        sb.append("}");
        return sb.toString();
    }

    private void appendActors(StringBuilder sb) {
        sb.append("    'actors': {\n");
        for ( Map.Entry<ActorPort, EV3Actor> entry : this.brickConfiguration.getActors().entrySet() ) {
            HardwareComponent hc = entry.getValue();
            if ( hc != null ) {
                ActorPort port = entry.getKey();
                sb.append("        '").append(port.toString()).append("':");
                sb.append(generateRegenerateEV3Actor(hc, port));
                sb.append(",\n");
            }
        }
        sb.append("    },\n");
    }

    private void appendSensors(StringBuilder sb) {
        sb.append("    'sensors': {\n");
        for ( Map.Entry<SensorPort, EV3Sensor> entry : this.brickConfiguration.getSensors().entrySet() ) {
            HardwareComponent hc = entry.getValue();
            if ( hc != null ) {
                SensorPort port = entry.getKey();
                sb.append("        '").append(port.getPortNumber()).append("':");
                sb.append(generateRegenerateEV3Sensor(hc, port));
                sb.append(",\n");
            }
        }
        sb.append("    },\n");
    }

    private String generateRegenerateUsedSensors() {
        StringBuilder sb = new StringBuilder();
        String arrayOfSensors = "";
        // FIXME: what is this used for?
        for ( EV3Sensors usedSensor : this.usedSensors ) {
            arrayOfSensors += "'" + getHardwareComponentTypeCode(usedSensor) + "',";
        }
        sb.append("usedSensors = Set([");
        if ( this.usedSensors.size() > 0 ) {
            sb.append(arrayOfSensors.substring(0, arrayOfSensors.length() - 1));
        }
        sb.append("])");
        return sb.toString();
    }

    private static String generateRegenerateEV3Actor(HardwareComponent actor, ActorPort port) {
        StringBuilder sb = new StringBuilder();
        // FIXME: that won't scale
        String name = null;
        switch ( actor.getComponentType().getShortName() ) {
            case "middle motor":
                name = "MediumMotor";
                break;
            case "big motor":
                name = "LargeMotor";
                break;
            default:
                throw new IllegalArgumentException("no mapping for " + actor.getComponentType().getShortName() + "to ev3dev-lang-python");
        }
        EV3Actor ev3Actor = (EV3Actor) actor;
        sb.append("Hal.make").append(name).append("(ev3dev.OUTPUT_").append(port.toString());
        sb.append(", ").append(ev3Actor.isRegulated() ? "'on'" : "'off'");
        sb.append(", ").append(getEnumCode(ev3Actor.getRotationDirection()));
        sb.append(", ").append(getEnumCode(ev3Actor.getMotorSide()));
        sb.append(")");
        return sb.toString();
    }

    private static String generateRegenerateEV3Sensor(HardwareComponent sensor, SensorPort port) {
        StringBuilder sb = new StringBuilder();
        // FIXME: that won't scale
        String name = null;
        // [m for m in dir(ev3dev) if m.find("_sensor") != -1]
        // ['color_sensor', 'gyro_sensor', 'infrared_sensor', 'light_sensor', 'sound_sensor', 'touch_sensor', 'ultrasonic_sensor']
        switch ( sensor.getComponentType().getShortName() ) {
            case "color":
                name = "color_sensor";
                break;
            case "touch":
                name = "touch_sensor";
                break;
            case "ultrasonic":
                name = "ultrasonic_sensor";
                break;
            case "infrared":
                name = "infrared_sensor";
                break;
            case "gyro":
                name = "gyro_sensor";
                break;
            default:
                throw new IllegalArgumentException("no mapping for " + sensor.getComponentType().getShortName() + "to ev3dev-lang-python");
        }
        sb.append("ev3dev.").append(name).append("(ev3dev.INPUT_").append(port.getPortNumber()).append(")");
        return sb.toString();
    }

    private static String getHardwareComponentTypeCode(HardwareComponentType type) {
        return type.getClass().getSimpleName() + "." + type.getTypeName();
    }

    private static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch ( NumberFormatException e ) {
            return false;
        }
    }

}
