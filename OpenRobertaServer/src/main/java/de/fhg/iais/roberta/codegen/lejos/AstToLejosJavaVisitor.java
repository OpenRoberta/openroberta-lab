package de.fhg.iais.roberta.codegen.lejos;

import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.Category;
import de.fhg.iais.roberta.ast.syntax.Phrase;
import de.fhg.iais.roberta.ast.syntax.Phrase.Kind;
import de.fhg.iais.roberta.ast.syntax.action.ClearDisplayAction;
import de.fhg.iais.roberta.ast.syntax.action.DriveAction;
import de.fhg.iais.roberta.ast.syntax.action.LightAction;
import de.fhg.iais.roberta.ast.syntax.action.LightStatusAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorDriveStopAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorGetPowerAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorOnAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorSetPowerAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorStopAction;
import de.fhg.iais.roberta.ast.syntax.action.PlayFileAction;
import de.fhg.iais.roberta.ast.syntax.action.ShowPictureAction;
import de.fhg.iais.roberta.ast.syntax.action.ShowTextAction;
import de.fhg.iais.roberta.ast.syntax.action.ToneAction;
import de.fhg.iais.roberta.ast.syntax.action.TurnAction;
import de.fhg.iais.roberta.ast.syntax.action.VolumeAction;
import de.fhg.iais.roberta.ast.syntax.expr.ActionExpr;
import de.fhg.iais.roberta.ast.syntax.expr.Binary;
import de.fhg.iais.roberta.ast.syntax.expr.Binary.Op;
import de.fhg.iais.roberta.ast.syntax.expr.BoolConst;
import de.fhg.iais.roberta.ast.syntax.expr.ColorConst;
import de.fhg.iais.roberta.ast.syntax.expr.EmptyExpr;
import de.fhg.iais.roberta.ast.syntax.expr.Expr;
import de.fhg.iais.roberta.ast.syntax.expr.ExprList;
import de.fhg.iais.roberta.ast.syntax.expr.MathConst;
import de.fhg.iais.roberta.ast.syntax.expr.NullConst;
import de.fhg.iais.roberta.ast.syntax.expr.NumConst;
import de.fhg.iais.roberta.ast.syntax.expr.SensorExpr;
import de.fhg.iais.roberta.ast.syntax.expr.StringConst;
import de.fhg.iais.roberta.ast.syntax.expr.Unary;
import de.fhg.iais.roberta.ast.syntax.expr.Var;
import de.fhg.iais.roberta.ast.syntax.functions.Func;
import de.fhg.iais.roberta.ast.syntax.sensor.BrickSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.ColorSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.ColorSensorMode;
import de.fhg.iais.roberta.ast.syntax.sensor.EncoderSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.GyroSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.InfraredSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.TimerSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.TouchSensor;
import de.fhg.iais.roberta.ast.syntax.sensor.UltrasonicSensor;
import de.fhg.iais.roberta.ast.syntax.stmt.ActionStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.AssignStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.ExprStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.IfStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.RepeatStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.RepeatStmt.Mode;
import de.fhg.iais.roberta.ast.syntax.stmt.SensorStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.Stmt;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtFlowCon;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtList;
import de.fhg.iais.roberta.ast.syntax.stmt.WaitStmt;
import de.fhg.iais.roberta.ast.syntax.tasks.ActivityTask;
import de.fhg.iais.roberta.ast.syntax.tasks.MainTask;
import de.fhg.iais.roberta.ast.syntax.tasks.StartActivityTask;
import de.fhg.iais.roberta.ast.visitor.AstVisitor;
import de.fhg.iais.roberta.dbc.Assert;
import de.fhg.iais.roberta.dbc.DbcException;

/**
 * This class is implementing {@link AstVisitor}. All methods are implemented and they
 * append a human-readable JAVA code representation of a phrase to a StringBuilder. <b>This representation is correct JAVA code.</b> <br>
 */
public class AstToLejosJavaVisitor implements AstVisitor<Void> {
    public static final String INDENT = "    ";

    private final BrickConfiguration brickConfiguration;
    private final String programName;
    private final StringBuilder sb = new StringBuilder();

    private int indentation;

    /**
     * initialize the Java code generator visitor.
     * 
     * @param programName name of the program
     * @param brickConfiguration hardware configuration of the brick
     * @param indentation to start with. Will be ince/decr depending on block structure
     */
    AstToLejosJavaVisitor(String programName, BrickConfiguration brickConfiguration, int indentation) {
        this.programName = programName;
        this.brickConfiguration = brickConfiguration;
        this.indentation = indentation;
    }

    /**
     * factory method to generate Java code from an AST.<br>
     * 
     * @param programName name of the program
     * @param brickConfiguration hardware configuration of the brick
     * @param phrases to generate the code from
     */
    public static String generate(String programName, BrickConfiguration brickConfiguration, List<Phrase<Void>> phrases, boolean withWrapping) //
    {
        Assert.notNull(programName);
        Assert.notNull(brickConfiguration);
        Assert.isTrue(phrases.size() >= 1);

        AstToLejosJavaVisitor astVisitor = new AstToLejosJavaVisitor(programName, brickConfiguration, withWrapping ? 2 : 0);
        astVisitor.generatePrefix(withWrapping);
        for ( Phrase<Void> phrase : phrases ) {
            if ( phrase.getKind().getCategory() != Category.TASK ) {
                astVisitor.sb.append("\n").append(INDENT).append(INDENT);
            }
            phrase.visit(astVisitor);
        }
        astVisitor.generateSuffix(withWrapping);
        return astVisitor.sb.toString();
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

    @Override
    public Void visitNumConst(NumConst<Void> numConst) {
        this.sb.append(numConst.getValue());
        return null;
    }

    @Override
    public Void visitBoolConst(BoolConst<Void> boolConst) {
        this.sb.append(boolConst.isValue());
        return null;
    };

    @Override
    public Void visitMathConst(MathConst<Void> mathConst) {
        this.sb.append(mathConst.getMathConst());
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst<Void> colorConst) {
        this.sb.append(colorConst.getValue().getJavaCode());
        return null;
    }

    @Override
    public Void visitStringConst(StringConst<Void> stringConst) {
        this.sb.append("\"").append(StringEscapeUtils.escapeJava(stringConst.getValue())).append("\"");
        return null;
    }

    @Override
    public Void visitNullConst(NullConst<Void> nullConst) {
        this.sb.append("null");
        return null;
    }

    @Override
    public Void visitVar(Var<Void> var) {
        switch ( var.getTypeVar() ) {
            case INTEGER:
                this.sb.append("int " + var.getValue());
                break;
            default:
                this.sb.append(var.getValue());
                break;
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
        this.sb.append(whitespace() + binary.getOp().getOpSymbol() + whitespace());
        generateSubExpr(this.sb, parenthesesCheck(binary), binary.getRight(), binary);
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
    public Void visitEmptyExpr(EmptyExpr<Void> emptyExpr) {
        switch ( emptyExpr.getDefVal().getName() ) {
            case "java.lang.String":
                this.sb.append("\"\"");
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
            if ( first ) {
                first = false;
            } else {
                if ( expr.getKind() == Kind.BINARY || expr.getKind() == Kind.UNARY ) {
                    this.sb.append("; ");
                } else {
                    this.sb.append(", ");
                }
            }
            expr.visit(this);
        }
        return null;
    }

    @Override
    public Void visitFunc(Func<Void> funct) {
        switch ( funct.getFunctName() ) {
            case PRINT:
                this.sb.append("System.out.println(");
                funct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            default:
                break;
        }
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
        this.sb.append(";");
        return null;
    }

    @Override
    public Void visitExprStmt(ExprStmt<Void> exprStmt) {
        exprStmt.getExpr().visit(this);
        this.sb.append(";");
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
        switch ( repeatStmt.getMode() ) {
            case UNTIL:
            case WHILE:
                generateCodeFromStmtCondition("while", repeatStmt.getExpr());
                break;
            case TIMES:
            case FOR:
                generateCodeFromStmtCondition("for", repeatStmt.getExpr());
                break;
            case WAIT:
                generateCodeFromStmtCondition("if", repeatStmt.getExpr());
                break;
            case FOR_EACH:
                break;
            default:
                break;
        }
        incrIndentation();
        repeatStmt.getList().visit(this);
        appendBreakStmt(repeatStmt);
        decrIndentation();
        nlIndent();
        this.sb.append("}");
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
        this.sb.append("while ( true ) {");
        incrIndentation();
        visitStmtList(waitStmt.getStatements());
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        this.sb.append("hal.clearDisplay();");
        return null;
    }

    @Override
    public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        switch ( volumeAction.getMode() ) {
            case SET:
                this.sb.append("hal.setVolume(");
                volumeAction.getVolume().visit(this);
                this.sb.append(");");
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
        this.sb.append("hal.ledOn(" + lightAction.getColor().getJavaCode() + ", " + lightAction.isBlink() + ");");
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        switch ( lightStatusAction.getStatus() ) {
            case OFF:
                this.sb.append("hal.ledOff();");
                break;
            case RESET:
                this.sb.append("hal.resetLED();");
                break;
            default:
                throw new DbcException("Invalid LED status mode!");
        }
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        this.sb.append("hal.playFile(\"" + playFileAction.getFileName() + "\");");
        return null;
    }

    @Override
    public Void visitShowPictureAction(ShowPictureAction<Void> showPictureAction) {
        this.sb.append("hal.drawPicture(\"" + showPictureAction.getPicture() + "\", ");
        showPictureAction.getX().visit(this);
        this.sb.append(", ");
        showPictureAction.getY().visit(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
        this.sb.append("hal.drawText(");
        showTextAction.getMsg().visit(this);
        this.sb.append(", ");
        showTextAction.getX().visit(this);
        this.sb.append(", ");
        showTextAction.getY().visit(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        this.sb.append("hal.playTone(");
        toneAction.getFrequency().visit(this);
        this.sb.append(", ");
        toneAction.getDuration().visit(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        String methodName;
        boolean isRegulated = this.brickConfiguration.isMotorRegulated(motorOnAction.getPort());
        boolean duration = motorOnAction.getParam().getDuration() != null;
        if ( duration ) {
            methodName = isRegulated ? "hal.rotateRegulatedMotor(" : "hal.rotateUnregulatedMotor(";
        } else {
            methodName = isRegulated ? "hal.turnOnRegulatedMotor(" : "hal.turnOnUnregulatedMotor(";
        }
        this.sb.append(methodName + motorOnAction.getPort().getJavaCode() + ", ");
        motorOnAction.getParam().getSpeed().visit(this);
        if ( duration ) {
            this.sb.append(", " + motorOnAction.getDurationMode().getJavaCode());
            this.sb.append(", ");
            motorOnAction.getDurationValue().visit(this);
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        boolean isRegulated = this.brickConfiguration.isMotorRegulated(motorSetPowerAction.getPort());
        String methodName = isRegulated ? "hal.setRegulatedMotorSpeed(" : "hal.setUnregulatedMotorSpeed(";
        this.sb.append(methodName + motorSetPowerAction.getPort().getJavaCode() + ", ");
        motorSetPowerAction.getPower().visit(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        boolean isRegulated = this.brickConfiguration.isMotorRegulated(motorGetPowerAction.getPort());
        String methodName = isRegulated ? "hal.getRegulatedMotorSpeed(" : "hal.getUnregulatedMotorSpeed(";
        this.sb.append(methodName + motorGetPowerAction.getPort().getJavaCode() + ")");
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        boolean isRegulated = this.brickConfiguration.isMotorRegulated(motorStopAction.getPort());
        String methodName = isRegulated ? "hal.stopRegulatedMotor(" : "hal.stopUnregulatedMotor(";
        this.sb.append(methodName + motorStopAction.getPort().getJavaCode() + ", " + motorStopAction.getMode().getJavaCode() + ");");
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        boolean isDuration = driveAction.getParam().getDuration() != null;
        String methodName = isDuration ? "hal.driveDistance(" : "hal.regulatedDrive(";
        this.sb.append(methodName);
        this.sb.append(this.brickConfiguration.getLeftMotorPort().getJavaCode() + ", ");
        this.sb.append(this.brickConfiguration.getRightMotorPort().getJavaCode() + ", false, ");
        this.sb.append(driveAction.getDirection().getJavaCode() + ", ");
        driveAction.getParam().getSpeed().visit(this);
        if ( isDuration ) {
            this.sb.append(", ");
            driveAction.getParam().getDuration().getValue().visit(this);
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        boolean isDuration = turnAction.getParam().getDuration() != null;
        boolean isRegulated = this.brickConfiguration.getActorA().isRegulated();
        String methodName = "hal.rotateDirection" + (isDuration ? "Angle" : isRegulated ? "Regulated" : "Unregulated") + "(";
        this.sb.append(methodName);
        this.sb.append(this.brickConfiguration.getLeftMotorPort().getJavaCode() + ", ");
        this.sb.append(this.brickConfiguration.getRightMotorPort().getJavaCode() + ", false, ");
        this.sb.append(turnAction.getDirection().getJavaCode() + ", ");
        turnAction.getParam().getSpeed().visit(this);
        if ( isDuration ) {
            this.sb.append(", ");
            turnAction.getParam().getDuration().getValue().visit(this);
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        boolean isRegulated = true;
        String methodName = isRegulated ? "hal.stopRegulatedDrive(" : "hal.stopUnregulatedDrive(";
        this.sb.append(methodName);
        this.sb.append(this.brickConfiguration.getLeftMotorPort().getJavaCode() + ", ");
        this.sb.append(this.brickConfiguration.getRightMotorPort().getJavaCode() + ");");
        return null;
    }

    @Override
    public Void visitBrickSensor(BrickSensor<Void> brickSensor) {
        switch ( brickSensor.getMode() ) {
            case IS_PRESSED:
                this.sb.append("hal.isPressed(" + brickSensor.getKey().getJavaCode() + ")");
                break;
            case WAIT_FOR_PRESS_AND_RELEASE:
                this.sb.append("hal.isPressedAndReleased(" + brickSensor.getKey().getJavaCode() + ")");
                break;
            default:
                throw new DbcException("Invalide mode for BrickSensor!");
        }
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        switch ( colorSensor.getMode() ) {
            case GET_MODE:
                this.sb.append("hal.getColorSensorModeName(" + colorSensor.getPort().getJavaCode() + ")");
                break;
            case GET_SAMPLE:
                if ( colorSensor.getMode() == ColorSensorMode.COLOUR ) {
                    this.sb.append("PickColor.get(hal.getColorSensorValue(" + colorSensor.getPort().getJavaCode() + "))");
                } else {
                    this.sb.append("hal.getColorSensorValue(" + colorSensor.getPort().getJavaCode() + ")");
                }
                break;
            default:
                this.sb.append("hal.setColorSensorMode(" + colorSensor.getPort().getJavaCode() + ", " + colorSensor.getMode().getJavaCode() + ");");
                break;
        }
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        switch ( encoderSensor.getMode() ) {
            case GET_MODE:
                this.sb.append("hal.getMotorTachoMode(" + encoderSensor.getMotor().getJavaCode() + ")");
                break;
            case GET_SAMPLE:
                boolean isRegulated = true;
                String methodName = isRegulated ? "hal.getRegulatedMotorTachoValue(" : "hal.getUnregulatedMotorTachoValuestop(";
                this.sb.append(methodName + encoderSensor.getMotor().getJavaCode() + ")");
                break;
            case RESET:
                this.sb.append("hal.resetMotorTacho(" + encoderSensor.getMotor().getJavaCode() + ");");
                break;
            default:
                this.sb.append("hal.setMotorTachoMode(" + encoderSensor.getMotor().getJavaCode() + ", " + encoderSensor.getMode().getJavaCode() + ");");
                break;
        }
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        switch ( gyroSensor.getMode() ) {
            case GET_MODE:
                this.sb.append("hal.getGyroSensorModeName(" + gyroSensor.getPort().getJavaCode() + ")");
                break;
            case GET_SAMPLE:
                this.sb.append("hal.getGyroSensorValue(" + gyroSensor.getPort().getJavaCode() + ")");
                break;
            case RESET:
                this.sb.append("hal.resetGyroSensor(" + gyroSensor.getPort().getJavaCode() + ");");
                break;
            default:
                this.sb.append("hal.setGyroSensorMode(" + gyroSensor.getPort().getJavaCode() + ", " + gyroSensor.getMode().getJavaCode() + ");");
                break;
        }
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        switch ( infraredSensor.getMode() ) {
            case GET_MODE:
                this.sb.append("hal.getInfraredSensorModeName(" + infraredSensor.getPort().getJavaCode() + ")");
                break;
            case GET_SAMPLE:
                this.sb.append("hal.getInfraredSensorValue(" + infraredSensor.getPort().getJavaCode() + ")");
                break;
            default:
                this.sb.append("hal.setInfraredSensorMode(" + infraredSensor.getPort().getJavaCode() + ", " + infraredSensor.getMode().getJavaCode() + ");");
                break;
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
                this.sb.append("hal.resetTimer(" + timerSensor.getTimer() + ");");
                break;
            default:
                throw new DbcException("Invalid Time Mode!");
        }
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        this.sb.append("hal.isPressed(" + touchSensor.getPort().getJavaCode() + ")");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        switch ( ultrasonicSensor.getMode() ) {
            case GET_MODE:
                this.sb.append("hal.getUltraSonicSensorModeName(" + ultrasonicSensor.getPort().getJavaCode() + ")");
                break;
            case GET_SAMPLE:
                this.sb.append("hal.getUltraSonicSensorValue(" + ultrasonicSensor.getPort().getJavaCode() + ")");
                break;
            default:
                this.sb.append("hal.setUltrasonicSensorMode("
                    + ultrasonicSensor.getPort().getJavaCode()
                    + ", "
                    + ultrasonicSensor.getMode().getJavaCode()
                    + ");");
                break;
        }
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
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

    private void incrIndentation() {
        this.indentation += 1;
    }

    private void decrIndentation() {
        this.indentation -= 1;
    }

    private void indent() {
        if ( this.indentation <= 0 ) {
            return;
        } else {
            for ( int i = 0; i < this.indentation; i++ ) {
                this.sb.append(INDENT);
            }
        }
    }

    private void nlIndent() {
        this.sb.append("\n");
        indent();
    }

    private String whitespace() {
        return " ";
    }

    private boolean parenthesesCheck(Binary<Void> binary) {
        return binary.getOp() == Op.MINUS && binary.getRight().getKind() == Kind.BINARY && binary.getRight().getPrecedence() <= binary.getPrecedence();
    }

    private void generateSubExpr(StringBuilder sb, boolean minusAdaption, Expr<Void> expr, Binary<Void> binary) {
        if ( expr.getPrecedence() >= binary.getPrecedence() && !minusAdaption ) {
            // parentheses are omitted
            expr.visit(this);
        } else {
            sb.append("(" + whitespace());
            expr.visit(this);
            sb.append(whitespace() + ")");
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
        this.sb.append("(" + whitespace());
        ifStmt.getExpr().get(0).visit(this);
        this.sb.append(whitespace() + ")" + whitespace() + "?" + whitespace());
        ((ExprStmt<Void>) ifStmt.getThenList().get(0).get().get(0)).getExpr().visit(this);
        this.sb.append(whitespace() + ":" + whitespace());
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
                this.sb.append("}").append(whitespace());
            }
        }
    }

    private void generateCodeFromElse(IfStmt<Void> ifStmt) {
        if ( ifStmt.getElseList().get().size() != 0 ) {
            nlIndent();
            this.sb.append("}").append(whitespace()).append("else").append(whitespace() + "{");
            incrIndentation();
            ifStmt.getElseList().visit(this);
            decrIndentation();
        }
        nlIndent();
        this.sb.append("}");
    }

    private void generateCodeFromStmtCondition(String stmtType, Expr<Void> expr) {
        this.sb.append(stmtType + whitespace() + "(" + whitespace());
        expr.visit(this);
        this.sb.append(whitespace() + ")" + whitespace() + "{");
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
        this.sb.append("package generated.main;\n\n");
        this.sb.append("import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;\n");
        this.sb.append("import de.fhg.iais.roberta.ast.syntax.HardwareComponent;\n");
        this.sb.append("import de.fhg.iais.roberta.ast.syntax.action.HardwareComponentType;\n");
        this.sb.append("import de.fhg.iais.roberta.ast.syntax.action.ActorPort;\n");
        this.sb.append("import de.fhg.iais.roberta.ast.syntax.action.MotorMoveMode;\n");
        this.sb.append("import de.fhg.iais.roberta.ast.syntax.action.DriveDirection;\n");
        this.sb.append("import de.fhg.iais.roberta.ast.syntax.action.MotorStopMode;\n");
        this.sb.append("import de.fhg.iais.roberta.ast.syntax.action.MotorSide;\n");
        this.sb.append("import de.fhg.iais.roberta.ast.syntax.action.MotorType;\n");
        this.sb.append("import de.fhg.iais.roberta.ast.syntax.action.TurnDirection;\n");
        this.sb.append("import de.fhg.iais.roberta.ast.syntax.action.BrickLedColor;\n");
        this.sb.append("import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;\n");
        this.sb.append("import de.fhg.iais.roberta.ast.syntax.sensor.BrickKey;\n");
        this.sb.append("import de.fhg.iais.roberta.ast.syntax.sensor.ColorSensorMode;\n");
        this.sb.append("import de.fhg.iais.roberta.ast.syntax.sensor.GyroSensorMode;\n");
        this.sb.append("import de.fhg.iais.roberta.ast.syntax.sensor.InfraredSensorMode;\n");
        this.sb.append("import de.fhg.iais.roberta.ast.syntax.sensor.MotorTachoMode;\n");
        this.sb.append("import de.fhg.iais.roberta.ast.syntax.sensor.TimerSensorMode;\n");
        this.sb.append("import de.fhg.iais.roberta.ast.syntax.sensor.UltrasonicSensorMode;\n");
        this.sb.append("import de.fhg.iais.roberta.ast.syntax.sensor.SensorType;\n");
        this.sb.append("import de.fhg.iais.roberta.codegen.lejos.Hal;\n\n");
        this.sb.append("public class " + this.programName + " {\n");
        this.sb.append(INDENT).append(this.brickConfiguration.generateRegenerate()).append("\n\n");
        this.sb.append(INDENT).append("public static void main(String[] args) {\n");
        this.sb.append(INDENT).append(INDENT).append("new ").append(this.programName).append("().run();\n");
        this.sb.append(INDENT).append("}\n\n");

        this.sb.append(INDENT).append("public void run() {\n");
        this.sb.append(INDENT).append(INDENT).append("Hal hal = new Hal(brickConfiguration);");
    }

    private void generateSuffix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }

        this.sb.append("\n");
        this.sb.append(INDENT).append(INDENT).append("try {\n");
        this.sb.append(INDENT).append(INDENT).append(INDENT).append("Thread.sleep(2000);\n");
        this.sb.append(INDENT).append(INDENT).append("} catch ( InterruptedException e ) {\n");
        this.sb.append(INDENT).append(INDENT).append(INDENT).append("// ok\n");
        this.sb.append(INDENT).append(INDENT).append("}\n");

        try {
            Thread.sleep(2000);
        } catch ( InterruptedException e ) {
            // ok
        }
        this.sb.append(INDENT).append("}\n}\n");
    }

}
