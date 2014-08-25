package de.fhg.iais.roberta.codegen.lejos;

import org.apache.commons.lang3.StringEscapeUtils;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.Phrase.Kind;
import de.fhg.iais.roberta.ast.syntax.action.ActorPort;
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
import de.fhg.iais.roberta.ast.syntax.stmt.SensorStmt;
import de.fhg.iais.roberta.ast.syntax.stmt.Stmt;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtFlowCon;
import de.fhg.iais.roberta.ast.syntax.stmt.StmtList;
import de.fhg.iais.roberta.dbc.DbcException;

/**
 * This class is implementing {@link Visitor}. All methods are implemented and they
 * append a human-readable JAVA code representation of a phrase to a StringBuilder. <b>This representation is correct JAVA code.</b> <br>
 * <br>
 * To create object of this class use {@link #JavaVisitor(StringBuilder, int, BrickConfiguration)} constructor.
 * 
 * @author kcvejoski
 */
public class JavaVisitor implements Visitor<JavaVisitor> {
    private final StringBuilder sb;
    private int indentation;
    private final BrickConfiguration brickConfiguration;

    /**
     * This constructor create valid object of the class {@link JavaVisitor}. <br>
     * <br>
     * Client must provide empty or non-empty {@link StringBuilder} object on which
     * generated JAVA code will be appended, value for the indentation and valid {@link BrickConfiguration} object.
     * 
     * @param sb on which code will be appended,
     * @param indentation value,
     * @param brickConfiguration object with valid configuration.
     */
    public JavaVisitor(StringBuilder sb, int indentation, BrickConfiguration brickConfiguration) {
        this.sb = sb;
        this.indentation = indentation;
        this.brickConfiguration = brickConfiguration;
    }

    /**
     * Get the current indentation of the visitor. Meaningful for tests only.
     * 
     * @return indentation value of the visitor.
     */
    public int getIndentation() {
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
    public JavaVisitor visitNumConst(NumConst numConst) {
        this.sb.append(numConst.getValue());
        return this;
    }

    @Override
    public JavaVisitor visitBoolConst(BoolConst boolConst) {
        this.sb.append(boolConst.isValue());
        return this;
    };

    @Override
    public JavaVisitor visitMathConst(MathConst mathConst) {
        this.sb.append(mathConst.getMathConst());
        return this;
    }

    @Override
    public JavaVisitor visitColorConst(ColorConst colorConst) {
        this.sb.append(colorConst.getValue().getJavaCode());
        return this;
    }

    @Override
    public JavaVisitor visitStringConst(StringConst stringConst) {
        this.sb.append("\"").append(StringEscapeUtils.escapeJava(stringConst.getValue())).append("\"");
        return this;
    }

    @Override
    public JavaVisitor visitNullConst(NullConst nullConst) {
        this.sb.append("null");
        return this;
    }

    @Override
    public JavaVisitor visitVar(Var var) {
        switch ( var.getTypeVar() ) {
            case INTEGER:
                this.sb.append("int " + var.getValue());
                break;
            default:
                this.sb.append(var.getValue());
                break;
        }
        return this;
    }

    @Override
    public JavaVisitor visitUnary(Unary unary) {
        if ( unary.getOp() == Unary.Op.POSTFIX_INCREMENTS ) {
            generateExprCode(unary, this.sb);
            this.sb.append(unary.getOp().getOpSymbol());
        } else {
            this.sb.append(unary.getOp().getOpSymbol());
            generateExprCode(unary, this.sb);
        }
        return this;
    }

    @Override
    public JavaVisitor visitBinary(Binary binary) {
        generateSubExpr(this.sb, false, binary.getLeft(), binary);
        this.sb.append(whitespace() + binary.getOp().getOpSymbol() + whitespace());
        generateSubExpr(this.sb, parenthesesCheck(binary), binary.getRight(), binary);
        return this;
    }

    @Override
    public JavaVisitor visitActionExpr(ActionExpr actionExpr) {
        actionExpr.getAction().accept(this);
        return this;
    }

    @Override
    public JavaVisitor visitSensorExpr(SensorExpr sensorExpr) {
        sensorExpr.getSens().accept(this);
        return this;
    }

    @Override
    public JavaVisitor visitEmptyExpr(EmptyExpr emptyExpr) {
        switch ( emptyExpr.getDefVal().getName() ) {
            case "java.lang.String":
                this.sb.append("\"\"");
                break;
            default:
                this.sb.append("[[EmptyExpr [defVal=" + emptyExpr.getDefVal() + "]]]");
                break;
        }
        return this;
    }

    @Override
    public JavaVisitor visitExprList(ExprList exprList) {
        boolean first = true;
        for ( Expr expr : exprList.get() ) {
            if ( first ) {
                first = false;
            } else {
                if ( expr.getKind() == Kind.BINARY || expr.getKind() == Kind.UNARY ) {
                    this.sb.append("; ");
                } else {
                    this.sb.append(", ");
                }
            }
            expr.accept(this);
        }
        return this;
    }

    @Override
    public JavaVisitor visitFunc(Func funct) {
        switch ( funct.getFunctName() ) {
            case PRINT:
                this.sb.append("System.out.println(");
                funct.getParam().get(0).accept(this);
                this.sb.append(")");
                break;
            default:
                break;
        }
        return this;
    }

    @Override
    public JavaVisitor visitActionStmt(ActionStmt actionStmt) {
        actionStmt.getAction().accept(this);
        return this;
    }

    @Override
    public JavaVisitor visitAssignStmt(AssignStmt assignStmt) {
        assignStmt.getName().accept(this);
        this.sb.append(" = ");
        assignStmt.getExpr().accept(this);
        this.sb.append(";");
        return this;
    }

    @Override
    public JavaVisitor visitExprStmt(ExprStmt exprStmt) {
        exprStmt.getExpr().accept(this);
        this.sb.append(";");
        return this;
    }

    @Override
    public JavaVisitor visitIfStmt(IfStmt ifStmt) {
        if ( ifStmt.isTernary() ) {
            generateCodeFromTernary(ifStmt);
        } else {
            generateCodeFromIfElse(ifStmt);
            generateCodeFromElse(ifStmt);
        }
        return this;
    }

    @Override
    public JavaVisitor visitRepeatStmt(RepeatStmt repeatStmt) {
        switch ( repeatStmt.getMode() ) {
            case UNTIL:
            case WHILE:
                generateCodeFromStmtCondition("while", repeatStmt.getExpr());
                break;
            case TIMES:
            case FOR:
                generateCodeFromStmtCondition("for", repeatStmt.getExpr());
                break;
            case FOR_EACH:
                break;
            default:
                break;
        }
        incrIndentation();
        repeatStmt.getList().accept(this);
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        return this;
    }

    @Override
    public JavaVisitor visitSensorStmt(SensorStmt sensorStmt) {
        sensorStmt.getSensor().accept(this);
        return this;
    }

    @Override
    public JavaVisitor visitStmtFlowCon(StmtFlowCon stmtFlowCon) {
        this.sb.append(stmtFlowCon.getFlow().toString().toLowerCase() + ";");
        return this;
    }

    @Override
    public JavaVisitor visitStmtList(StmtList stmtList) {
        for ( Stmt stmt : stmtList.get() ) {
            nlIndent();
            stmt.accept(this);
        }
        return this;
    }

    @Override
    public JavaVisitor visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        this.sb.append("hal.clearDisplay();");
        return this;
    }

    @Override
    public JavaVisitor visitVolumeAction(VolumeAction volumeAction) {
        switch ( volumeAction.getMode() ) {
            case SET:
                this.sb.append("hal.setVolume(");
                volumeAction.getVolume().accept(this);
                this.sb.append(");");
                break;
            case GET:
                this.sb.append("hal.getVolume()");
                break;
            default:
                throw new DbcException("Invalid volume action mode!");
        }
        return this;
    }

    @Override
    public JavaVisitor visitLightAction(LightAction lightAction) {
        this.sb.append("hal.ledOn(" + lightAction.getColor().getJavaCode() + ", " + lightAction.isBlink() + ");");
        return this;
    }

    @Override
    public JavaVisitor visitLightStatusAction(LightStatusAction lightStatusAction) {
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
        return this;
    }

    @Override
    public JavaVisitor visitPlayFileAction(PlayFileAction playFileAction) {
        this.sb.append("hal.playFile(\"" + playFileAction.getFileName() + "\");");
        return this;
    }

    @Override
    public JavaVisitor visitShowPictureAction(ShowPictureAction showPictureAction) {
        this.sb.append("hal.drawPicture(\"" + showPictureAction.getPicture() + "\", ");
        showPictureAction.getX().accept(this);
        this.sb.append(", ");
        showPictureAction.getY().accept(this);
        this.sb.append(");");
        return this;
    }

    @Override
    public JavaVisitor visitShowTextAction(ShowTextAction showTextAction) {
        this.sb.append("hal.drawText(");
        showTextAction.getMsg().accept(this);
        this.sb.append(", ");
        showTextAction.getX().accept(this);
        this.sb.append(", ");
        showTextAction.getY().accept(this);
        this.sb.append(");");
        return this;
    }

    @Override
    public JavaVisitor visitToneAction(ToneAction toneAction) {
        this.sb.append("hal.playTone(");
        toneAction.getFrequency().accept(this);
        this.sb.append(", ");
        toneAction.getDuration().accept(this);
        this.sb.append(");");
        return this;
    }

    @Override
    public JavaVisitor visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction) {
        // it is just temporary variable just to be able to write the code. This will be replaced by the brick configuration method.
        boolean isRegulated = true;
        String methodName = isRegulated ? "hal.setRegulatedMotorSpeed(" : "hal.setUnregulatedMotorSpeed(";
        this.sb.append(methodName + motorSetPowerAction.getPort().getJavaCode() + ", ");
        motorSetPowerAction.getPower().accept(this);
        this.sb.append(");");
        return this;
    }

    @Override
    public JavaVisitor visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction) {
        // it is just temporary variable just to be able to write the code. This will be replaced by the brick configuration method.
        boolean isRegulated = true;
        String methodName = isRegulated ? "hal.getRegulatedMotorSpeed(" : "hal.getUnregulatedMotorSpeed(";
        this.sb.append(methodName + motorGetPowerAction.getPort().getJavaCode() + ")");
        return this;
    }

    @Override
    public JavaVisitor visitMotorOnAction(MotorOnAction motorOnAction) {
        // TODO Auto-generated method stub
        return this;
    }

    @Override
    public JavaVisitor visitMotorStopAction(MotorStopAction motorStopAction) {
        // it is just temporary variable just to be able to write the code. This will be replaced by the brick configuration method.
        boolean isRegulated = true;
        String methodName = isRegulated ? "hal.stopRegulatedMotor(" : "hal.stopUnregulatedMotor(";
        this.sb.append(methodName + motorStopAction.getPort().getJavaCode() + ", " + motorStopAction.getMode().getJavaCode() + ");");
        return this;
    }

    @Override
    public JavaVisitor visitTurnAction(TurnAction turnAction) {
        boolean isDuration = turnAction.getParam().getDuration() != null;
        boolean isRegulated = true;
        String methodName = "hal.rotateDirection" + (isDuration ? "DistanceRegulated" : isRegulated ? "Regulated" : "Unregulated") + "(";
        this.sb.append(methodName);
        //Set the left motor to motor port A, this will be changed when brick configuration provides information on which port the left motor is set. 
        this.sb.append(ActorPort.A.getJavaCode() + ", ");
        //Set the right motor to motor port B, this will be changed when brick configuration provides information on which port the right motor is set. 
        this.sb.append(ActorPort.B.getJavaCode() + ", ");
        this.sb.append(turnAction.getDirection().getJavaCode() + ", ");
        turnAction.getParam().getSpeed().accept(this);
        if ( isDuration ) {
            this.sb.append(", ");
            turnAction.getParam().getDuration().getValue().accept(this);
        }
        this.sb.append(");");
        return this;
    }

    @Override
    public JavaVisitor visitDriveAction(DriveAction driveAction) {
        boolean isDuration = driveAction.getParam().getDuration() != null;
        boolean isRegulated = true;
        String methodName = isDuration ? "hal.driveDistance(" : isRegulated ? "hal.regulatedDrive(" : "hal.unregulatedDrive(";

        this.sb.append(methodName);
        //Set the left motor to motor port A, this will be changed when brick configuration provides information on which port the left motor is set. 
        this.sb.append(ActorPort.A.getJavaCode() + ", ");
        //Set the right motor to motor port B, this will be changed when brick configuration provides information on which port the right motor is set. 
        this.sb.append(ActorPort.B.getJavaCode() + ", ");
        this.sb.append(driveAction.getDirection().getJavaCode() + ", ");
        driveAction.getParam().getSpeed().accept(this);
        if ( isDuration ) {
            this.sb.append(", ");
            driveAction.getParam().getDuration().getValue().accept(this);
        }
        this.sb.append(");");
        return this;
    }

    @Override
    public JavaVisitor visitMotorDriveStopAction(MotorDriveStopAction stopAction) {
        boolean isRegulated = true;
        String methodName = isRegulated ? "hal.stopRegulatedDrive(" : "hal.stopUnregulatedDrive(";
        this.sb.append(methodName);
        //Set the left motor to motor port A, this will be changed when brick configuration provides information on which port the left motor is set. 
        this.sb.append(ActorPort.A.getJavaCode() + ", ");
        //Set the right motor to motor port B, this will be changed when brick configuration provides information on which port the right motor is set. 
        this.sb.append(ActorPort.B.getJavaCode() + ");");
        return this;
    }

    @Override
    public JavaVisitor visitBrickSensor(BrickSensor brickSensor) {
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
        return this;
    }

    @Override
    public JavaVisitor visitColorSensor(ColorSensor colorSensor) {
        switch ( colorSensor.getMode() ) {
            case GET_MODE:
                this.sb.append("hal.getColorSensorModeName(" + colorSensor.getPort().getJavaCode() + ")");
                break;
            case GET_SAMPLE:
                this.sb.append("hal.getColorSensorValue(" + colorSensor.getPort().getJavaCode() + ")");
                break;
            default:
                this.sb.append("hal.setColorSensorMode(" + colorSensor.getPort().getJavaCode() + ", " + colorSensor.getMode().getJavaCode() + ");");
                break;
        }
        return this;
    }

    @Override
    public JavaVisitor visitEncoderSensor(EncoderSensor encoderSensor) {
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
        return this;
    }

    @Override
    public JavaVisitor visitGyroSensor(GyroSensor gyroSensor) {
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
        return this;
    }

    @Override
    public JavaVisitor visitInfraredSensor(InfraredSensor infraredSensor) {
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
        return this;
    }

    @Override
    public JavaVisitor visitTimerSensor(TimerSensor timerSensor) {
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
        return this;
    }

    @Override
    public JavaVisitor visitTouchSensor(TouchSensor touchSensor) {
        this.sb.append("hal.isPressed(" + touchSensor.getPort().getJavaCode() + ")");
        return this;
    }

    @Override
    public JavaVisitor visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
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
        return this;
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
                this.sb.append(JavaGenerateCode.INDENT);
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

    private boolean parenthesesCheck(Binary binary) {
        return binary.getOp() == Op.MINUS && binary.getRight().getKind() == Kind.BINARY && binary.getRight().getPrecedence() <= binary.getPrecedence();
    }

    private void generateSubExpr(StringBuilder sb, boolean minusAdaption, Expr expr, Binary binary) {
        if ( expr.getPrecedence() >= binary.getPrecedence() && !minusAdaption ) {
            // parentheses are omitted
            expr.accept(this);
        } else {
            sb.append("(" + whitespace());
            expr.accept(this);
            sb.append(whitespace() + ")");
        }
    }

    private void generateExprCode(Unary unary, StringBuilder sb) {
        if ( unary.getExpr().getPrecedence() < unary.getPrecedence() ) {
            sb.append("(");
            unary.getExpr().accept(this);
            sb.append(")");
        } else {
            unary.getExpr().accept(this);
        }
    }

    private void generateCodeFromTernary(IfStmt ifStmt) {
        this.sb.append("(" + whitespace());
        ifStmt.getExpr().get(0).accept(this);
        this.sb.append(whitespace() + ")" + whitespace() + "?" + whitespace());
        ((ExprStmt) ifStmt.getThenList().get(0).get().get(0)).getExpr().accept(this);
        this.sb.append(whitespace() + ":" + whitespace());
        ((ExprStmt) ifStmt.getElseList().get().get(0)).getExpr().accept(this);
    }

    private void generateCodeFromIfElse(IfStmt ifStmt) {
        for ( int i = 0; i < ifStmt.getExpr().size(); i++ ) {
            if ( i == 0 ) {
                generateCodeFromStmtCondition("if", ifStmt.getExpr().get(i));
            } else {
                generateCodeFromStmtCondition("else if", ifStmt.getExpr().get(i));
            }
            incrIndentation();
            ifStmt.getThenList().get(i).accept(this);
            decrIndentation();
            if ( i + 1 < ifStmt.getExpr().size() ) {
                nlIndent();
                this.sb.append("}").append(whitespace());
            }
        }
    }

    private void generateCodeFromElse(IfStmt ifStmt) {
        if ( ifStmt.getElseList().get().size() != 0 ) {
            nlIndent();
            this.sb.append("}").append(whitespace()).append("else").append(whitespace() + "{");
            incrIndentation();
            ifStmt.getElseList().accept(this);
            decrIndentation();
        }
        nlIndent();
        this.sb.append("}");
    }

    private void generateCodeFromStmtCondition(String stmtType, Expr expr) {
        this.sb.append(stmtType + whitespace() + "(" + whitespace());
        expr.accept(this);
        this.sb.append(whitespace() + ")" + whitespace() + "{");
    }

}
