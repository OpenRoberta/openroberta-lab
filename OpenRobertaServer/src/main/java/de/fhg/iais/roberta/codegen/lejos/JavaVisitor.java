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
import de.fhg.iais.roberta.ast.syntax.functions.Funct;
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
import de.fhg.iais.roberta.helper.StringManipulation;

/**
 * This class is implementing {@link Visitor}. All methods are implemented and they
 * append a human-readable JAVA code representation of a phrase to a StringBuilder. <b>This representation is correct JAVA code.</b> <br>
 * <br>
 * To create object of this class use {@link #JavaVisitor(StringBuilder, int, BrickConfiguration)} constructor.
 * 
 * @author kcvejoski
 */
public class JavaVisitor implements Visitor {
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
     * Get the current indentation of the visitor
     * 
     * @return indentation value of the visitor.
     */
    public int getIndentation() {
        return this.indentation;
    }

    /**
     * Set the indentation of the visitor.
     * 
     * @param indentation value.
     */
    public void setIndentation(int indentation) {
        this.indentation = indentation;
    }

    /**
     * Get the string builder of the visitor.
     * 
     * @return current state of the string builder
     */
    public StringBuilder getSb() {
        return this.sb;
    }

    @Override
    public void visit(NumConst numConst) {
        this.sb.append(numConst.getValue());
    }

    @Override
    public void visit(BoolConst boolConst) {
        this.sb.append(boolConst.isValue());
    }

    @Override
    public void visit(MathConst mathConst) {
        this.sb.append(mathConst.getMathConst());
    }

    @Override
    public void visit(ColorConst colorConst) {
        this.sb.append("\"").append(StringEscapeUtils.escapeJava(colorConst.getValue())).append("\"");
    }

    @Override
    public void visit(StringConst stringConst) {
        this.sb.append("\"").append(StringEscapeUtils.escapeJava(stringConst.getValue())).append("\"");
    }

    @Override
    public void visit(NullConst nullConst) {
        this.sb.append("null");
    }

    @Override
    public void visit(Var var) {
        switch ( var.getTypeVar() ) {
            case INTEGER:
                StringManipulation.appendCustomString(this.sb, this.indentation, false, false, "int " + var.getValue());
                break;
            default:
                StringManipulation.appendCustomString(this.sb, this.indentation, false, false, var.getValue());
                break;
        }
    }

    @Override
    public void visit(Unary unary) {
        if ( unary.getOp() == Unary.Op.POSTFIX_INCREMENTS ) {
            generateExprCode(unary, this.sb, this.indentation);
            this.sb.append(unary.getOp().getOpSymbol());
        } else {
            this.sb.append(unary.getOp().getOpSymbol());
            generateExprCode(unary, this.sb, this.indentation);
        }
    }

    @Override
    public void visit(Binary binary) {
        generateSubExpr(this.sb, false, binary.getLeft(), binary);
        this.sb.append(StringManipulation.generateString(PreattyPrintSettings.whiteSpaceSize, false, false, null)
            + binary.getOp().getOpSymbol()
            + StringManipulation.generateString(PreattyPrintSettings.whiteSpaceSize, false, false, null));
        generateSubExpr(this.sb, parenthesesCheck(binary), binary.getRight(), binary);
    }

    @Override
    public void visit(ActionExpr actionExpr) {
        this.sb.append(actionExpr.getAction());
    }

    @Override
    public void visit(SensorExpr sensorExpr) {
        JavaVisitor visitor = new JavaVisitor(this.sb, this.indentation, this.brickConfiguration);
        sensorExpr.getSens().accept(visitor);
    }

    @Override
    public void visit(EmptyExpr emptyExpr) {
        switch ( emptyExpr.getDefVal().getName() ) {
            case "java.lang.String":
                this.sb.append("\"\"");
                break;

            default:
                this.sb.append("[[EmptyExpr [defVal=" + emptyExpr.getDefVal() + "]]]");
                break;
        }
    }

    @Override
    public void visit(ExprList exprList) {
        JavaVisitor visitor = new JavaVisitor(this.sb, this.indentation, this.brickConfiguration);
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
            expr.accept(visitor);
        }
    }

    @Override
    public void visit(Funct funct) {
        switch ( funct.getFunctName() ) {
            case PRINT:
                JavaVisitor visitor = new JavaVisitor(this.sb, this.indentation, this.brickConfiguration);
                StringManipulation.appendCustomString(this.sb, this.indentation, false, false, "System.out.println(");
                funct.getParam().get(0).accept(visitor);
                this.sb.append(")");
                break;
            default:
                break;
        }
    }

    @Override
    public void visit(ActionStmt actionStmt) {
        JavaVisitor visitor = new JavaVisitor(this.sb, this.indentation, this.brickConfiguration);
        actionStmt.getAction().accept(visitor);
    }

    @Override
    public void visit(AssignStmt assignStmt) {
        JavaVisitor visitor = new JavaVisitor(this.sb, this.indentation, this.brickConfiguration);
        assignStmt.getName().accept(visitor);
        this.sb.append(" = ");
        assignStmt.getExpr().accept(visitor);
        this.sb.append(";");
    }

    @Override
    public void visit(ExprStmt exprStmt) {
        JavaVisitor visitor = new JavaVisitor(this.sb, this.indentation, this.brickConfiguration);
        exprStmt.getExpr().accept(visitor);
        this.sb.append(";");
    }

    @Override
    public void visit(IfStmt ifStmt) {
        int next = this.indentation + PreattyPrintSettings.indentationSize;
        JavaVisitor visitor = new JavaVisitor(this.sb, this.indentation, this.brickConfiguration);
        if ( ifStmt.isTernary() ) {
            generateCodeFromTernary(ifStmt, visitor);
        } else {
            generateCodeFromIfElse(ifStmt, next, visitor);
            generateCodeFromElse(ifStmt, next, visitor);
        }
    }

    @Override
    public void visit(RepeatStmt repeatStmt) {
        int next = this.indentation + PreattyPrintSettings.indentationSize;
        JavaVisitor visitor = new JavaVisitor(this.sb, 0, this.brickConfiguration);
        switch ( repeatStmt.getMode() ) {
            case UNTIL:
            case WHILE:
                generateCodeFromStmtCondition("while", repeatStmt.getExpr(), visitor, this.indentation);
                break;
            case TIMES:
            case FOR:
                generateCodeFromStmtCondition("for", repeatStmt.getExpr(), visitor, this.indentation);
                break;
            case FOR_EACH:
                break;

            default:
                break;
        }
        visitor.setIndentation(next);
        repeatStmt.getList().accept(visitor);
        StringManipulation.appendCustomString(this.sb, this.indentation, PreattyPrintSettings.newLineBeforeCloseBracket, false, "}");
    }

    @Override
    public void visit(SensorStmt sensorStmt) {
        JavaVisitor visitor = new JavaVisitor(this.sb, this.indentation, this.brickConfiguration);
        sensorStmt.getSensor().accept(visitor);
    }

    @Override
    public void visit(StmtFlowCon stmtFlowCon) {
        StringManipulation.appendCustomString(this.sb, this.indentation, false, false, stmtFlowCon.getFlow().toString().toLowerCase() + ";");
    }

    @Override
    public void visit(StmtList stmtList) {
        boolean first = true;
        JavaVisitor visitor = new JavaVisitor(this.sb, this.indentation, this.brickConfiguration);
        for ( Stmt stmt : stmtList.get() ) {
            if ( first ) {
                first = false;
                StringManipulation.appendCustomString(this.sb, 0, PreattyPrintSettings.newLineAfterOpenBracket, false, null);
            } else {
                StringManipulation.appendCustomString(this.sb, 0, true, false, null);
            }
            stmt.accept(visitor);
        }
    }

    @Override
    public void visit(ClearDisplayAction clearDisplayAction) {
        StringManipulation.appendCustomString(this.sb, this.indentation, false, false, "hal.clearDisplay();");
    }

    @Override
    public void visit(VolumeAction volumeAction) {
        JavaVisitor visitor = new JavaVisitor(this.sb, this.indentation, this.brickConfiguration);
        switch ( volumeAction.getMode() ) {
            case SET:
                StringManipulation.appendCustomString(this.sb, this.indentation, false, false, "hal.setVolume(");
                volumeAction.getVolume().accept(visitor);
                this.sb.append(");");
                break;
            case GET:
                StringManipulation.appendCustomString(this.sb, this.indentation, false, false, "hal.getVolume()");
                break;
            default:
                throw new DbcException("Invalid volume action mode!");
        }
    }

    @Override
    public void visit(LightAction lightAction) {
        StringManipulation.appendCustomString(
            this.sb,
            this.indentation,
            false,
            false,
            "hal.ledOn(" + lightAction.getColor().toString() + ", " + lightAction.isBlink() + ");");
    }

    @Override
    public void visit(LightStatusAction lightStatusAction) {
        switch ( lightStatusAction.getStatus() ) {
            case OFF:
                StringManipulation.appendCustomString(this.sb, this.indentation, false, false, "hal.ledOff();");
                break;
            case RESET:
                StringManipulation.appendCustomString(this.sb, this.indentation, false, false, "hal.resetLED();");
                break;
            default:
                throw new DbcException("Invalid LED status mode!");
        }
    }

    @Override
    public void visit(PlayFileAction playFileAction) {
        StringManipulation.appendCustomString(this.sb, this.indentation, false, false, "hal.playFile(\"" + playFileAction.getFileName() + "\");");
    }

    @Override
    public void visit(ShowPictureAction showPictureAction) {
        JavaVisitor visitor = new JavaVisitor(this.sb, this.indentation, this.brickConfiguration);
        StringManipulation.appendCustomString(this.sb, this.indentation, false, false, "hal.drawPicture(\"" + showPictureAction.getPicture() + "\", ");
        showPictureAction.getX().accept(visitor);
        this.sb.append(", ");
        showPictureAction.getY().accept(visitor);
        this.sb.append(");");
    }

    @Override
    public void visit(ShowTextAction showTextAction) {
        JavaVisitor visitor = new JavaVisitor(this.sb, this.indentation, this.brickConfiguration);
        StringManipulation.appendCustomString(this.sb, this.indentation, false, false, "hal.drawText(");
        showTextAction.getMsg().accept(visitor);
        this.sb.append(", ");
        showTextAction.getX().accept(visitor);
        this.sb.append(", ");
        showTextAction.getY().accept(visitor);
        this.sb.append(");");
    }

    @Override
    public void visit(ToneAction toneAction) {
        JavaVisitor visitor = new JavaVisitor(this.sb, this.indentation, this.brickConfiguration);
        StringManipulation.appendCustomString(this.sb, this.indentation, false, false, "hal.playTone(");
        toneAction.getFrequency().accept(visitor);
        this.sb.append(", ");
        toneAction.getDuration().accept(visitor);
        this.sb.append(");");
    }

    @Override
    public void visit(MotorSetPowerAction motorSetPowerAction) {
        // it is just temporary variable just to be able to write the code. This will be replaced by the brick configuration method.
        boolean isRegulated = true;
        String methodName = "hal.setRegulatedMotorSpeed(";
        JavaVisitor visitor = new JavaVisitor(this.sb, this.indentation, this.brickConfiguration);
        if ( !isRegulated ) {
            methodName = "hal.setUnregulatedMotorSpeed(";
        }
        StringManipulation.appendCustomString(this.sb, this.indentation, false, false, methodName + motorSetPowerAction.getPort().name() + ", ");
        motorSetPowerAction.getPower().accept(visitor);
        this.sb.append(");");
    }

    @Override
    public void visit(MotorGetPowerAction motorGetPowerAction) {
        // it is just temporary variable just to be able to write the code. This will be replaced by the brick configuration method.
        boolean isRegulated = true;
        String methodName = "hal.getRegulatedMotorSpeed(";
        if ( !isRegulated ) {
            methodName = "hal.getUnregulatedMotorSpeed(";
        }
        StringManipulation.appendCustomString(this.sb, this.indentation, false, false, methodName + motorGetPowerAction.getPort().toString() + ")");
    }

    @Override
    public void visit(MotorOnAction motorOnAction) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(MotorStopAction motorStopAction) {
        // it is just temporary variable just to be able to write the code. This will be replaced by the brick configuration method.
        boolean isRegulated = true;
        String methodName = "hal.stopRegulatedMotor(";
        if ( !isRegulated ) {
            methodName = "hal.stopUnregulatedMotor(";
        }
        StringManipulation.appendCustomString(this.sb, this.indentation, false, false, methodName
            + motorStopAction.getPort().toString()
            + ", "
            + motorStopAction.getMode().toString()
            + ");");
    }

    @Override
    public void visit(TurnAction turnAction) {
        StringBuilder tmpSB = new StringBuilder();
        JavaVisitor visitor = new JavaVisitor(this.sb, this.indentation, this.brickConfiguration);
        JavaVisitor visitor1 = new JavaVisitor(tmpSB, this.indentation, this.brickConfiguration);
        boolean isRegulated = true;
        String methodName = "hal.rotateDirectionRegulated(";
        if ( !isRegulated ) {
            methodName = "hal.rotateDirectionUnregulated(";
        }
        //check for distance parameter
        if ( turnAction.getParam().getDuration() != null ) {
            methodName = "hal.rotateDirectionDistanceRegulated(";
            tmpSB.append(", ");
            turnAction.getParam().getDuration().getValue().accept(visitor1);
        }
        StringManipulation.appendCustomString(this.sb, this.indentation, false, false, methodName);
        //Set the left motor to motor port A, this will be changed when brick configuration provides information on which port the left motor is set. 
        StringManipulation.appendCustomString(this.sb, 0, false, false, ActorPort.A.toString() + ", ");
        //Set the right motor to motor port B, this will be changed when brick configuration provides information on which port the right motor is set. 
        StringManipulation.appendCustomString(this.sb, 0, false, false, ActorPort.B.toString() + ", ");
        StringManipulation.appendCustomString(this.sb, 0, false, false, turnAction.getDirection().toString() + ", ");
        turnAction.getParam().getSpeed().accept(visitor);
        this.sb.append(tmpSB);
        StringManipulation.appendCustomString(this.sb, 0, false, false, ");");
    }

    @Override
    public void visit(DriveAction driveAction) {
        StringBuilder tmpSB = new StringBuilder();
        JavaVisitor visitor = new JavaVisitor(this.sb, this.indentation, this.brickConfiguration);
        JavaVisitor visitor1 = new JavaVisitor(tmpSB, this.indentation, this.brickConfiguration);
        boolean isRegulated = true;
        String methodName = "hal.regulatedDrive(";
        if ( !isRegulated ) {
            methodName = "hal.unregulatedDrive(";
        }
        //check for distance parameter
        if ( driveAction.getParam().getDuration() != null ) {
            methodName = "hal.driveDistance(";
            tmpSB.append(", ");
            driveAction.getParam().getDuration().getValue().accept(visitor1);
        }

        StringManipulation.appendCustomString(this.sb, this.indentation, false, false, methodName);
        //Set the left motor to motor port A, this will be changed when brick configuration provides information on which port the left motor is set. 
        StringManipulation.appendCustomString(this.sb, 0, false, false, ActorPort.A.toString() + ", ");
        //Set the right motor to motor port B, this will be changed when brick configuration provides information on which port the right motor is set. 
        StringManipulation.appendCustomString(this.sb, 0, false, false, ActorPort.B.toString() + ", ");
        StringManipulation.appendCustomString(this.sb, 0, false, false, driveAction.getDirection().toString() + ", ");
        driveAction.getParam().getSpeed().accept(visitor);
        this.sb.append(tmpSB);
        StringManipulation.appendCustomString(this.sb, 0, false, false, ");");
    }

    @Override
    public void visit(MotorDriveStopAction stopAction) {
        boolean isRegulated = true;
        String methodName = "hal.stopRegulatedDrive(";
        if ( !isRegulated ) {
            methodName = "hal.stopUnregulatedDrive(";
        }
        StringManipulation.appendCustomString(this.sb, this.indentation, false, false, methodName);
        //Set the left motor to motor port A, this will be changed when brick configuration provides information on which port the left motor is set. 
        StringManipulation.appendCustomString(this.sb, 0, false, false, ActorPort.A.toString() + ", ");
        //Set the right motor to motor port B, this will be changed when brick configuration provides information on which port the right motor is set. 
        StringManipulation.appendCustomString(this.sb, 0, false, false, ActorPort.B.toString() + ");");
    }

    @Override
    public void visit(BrickSensor brickSensor) {
        switch ( brickSensor.getMode() ) {
            case IS_PRESSED:
                StringManipulation.appendCustomString(this.sb, this.indentation, false, false, "hal.isPressed(" + brickSensor.getKey().toString() + ")");
                break;
            case WAIT_FOR_PRESS_AND_RELEASE:
                StringManipulation.appendCustomString(this.sb, this.indentation, false, false, "hal.isPressedAndReleased("
                    + brickSensor.getKey().toString()
                    + ")");
                break;
            default:
                throw new DbcException("Invalide mode for BrickSensor!");
        }
    }

    @Override
    public void visit(ColorSensor colorSensor) {
        switch ( colorSensor.getMode() ) {
            case GET_MODE:
                StringManipulation.appendCustomString(this.sb, this.indentation, false, false, "hal.getColorSensorModeName("
                    + colorSensor.getPort().toString()
                    + ")");
                break;
            case GET_SAMPLE:
                StringManipulation.appendCustomString(this.sb, this.indentation, false, false, "hal.getColorSensorValue("
                    + colorSensor.getPort().toString()
                    + ")");
                break;
            default:
                StringManipulation.appendCustomString(this.sb, this.indentation, false, false, "hal.setColorSensorMode("
                    + colorSensor.getPort().toString()
                    + ", "
                    + colorSensor.getMode().toString()
                    + ");");
                break;
        }
    }

    @Override
    public void visit(EncoderSensor encoderSensor) {
        switch ( encoderSensor.getMode() ) {
            case GET_MODE:
                StringManipulation.appendCustomString(this.sb, this.indentation, false, false, "hal.getMotorTachoMode("
                    + encoderSensor.getMotor().toString()
                    + ")");
                break;
            case GET_SAMPLE:
                StringManipulation.appendCustomString(this.sb, this.indentation, false, false, "hal.getMotorTachoValue("
                    + encoderSensor.getMotor().toString()
                    + ")");
                break;
            case RESET:
                StringManipulation.appendCustomString(this.sb, this.indentation, false, false, "hal.resetMotorTacho("
                    + encoderSensor.getMotor().toString()
                    + ");");
                break;
            default:
                StringManipulation.appendCustomString(this.sb, this.indentation, false, false, "hal.setMotorTachoMode("
                    + encoderSensor.getMotor().toString()
                    + ", "
                    + encoderSensor.getMode().toString()
                    + ");");
                break;
        }
    }

    @Override
    public void visit(GyroSensor gyroSensor) {
        switch ( gyroSensor.getMode() ) {
            case GET_MODE:
                StringManipulation.appendCustomString(this.sb, this.indentation, false, false, "hal.getGyroSensorModeName("
                    + gyroSensor.getPort().toString()
                    + ")");
                break;
            case GET_SAMPLE:
                StringManipulation.appendCustomString(this.sb, this.indentation, false, false, "hal.getGyroSensorValue("
                    + gyroSensor.getPort().toString()
                    + ")");
                break;
            case RESET:
                StringManipulation.appendCustomString(this.sb, this.indentation, false, false, "hal.resetGyroSensor(" + gyroSensor.getPort().toString() + ");");
                break;
            default:
                StringManipulation.appendCustomString(this.sb, this.indentation, false, false, "hal.setGyroSensorMode("
                    + gyroSensor.getPort().toString()
                    + ", "
                    + gyroSensor.getMode().toString()
                    + ");");
                break;
        }
    }

    @Override
    public void visit(InfraredSensor infraredSensor) {
        switch ( infraredSensor.getMode() ) {
            case GET_MODE:
                StringManipulation.appendCustomString(this.sb, this.indentation, false, false, "hal.getInfraredSensorModeName("
                    + infraredSensor.getPort().toString()
                    + ")");
                break;
            case GET_SAMPLE:
                StringManipulation.appendCustomString(this.sb, this.indentation, false, false, "hal.getInfraredSensorValue("
                    + infraredSensor.getPort().toString()
                    + ")");
                break;
            default:
                StringManipulation.appendCustomString(this.sb, this.indentation, false, false, "hal.setInfraredSensorMode("
                    + infraredSensor.getPort().toString()
                    + ", "
                    + infraredSensor.getMode().toString()
                    + ");");
                break;
        }
    }

    @Override
    public void visit(TimerSensor timerSensor) {
        switch ( timerSensor.getMode() ) {
            case GET_SAMPLE:
                StringManipulation.appendCustomString(this.sb, this.indentation, false, false, "hal.getTimerValue(" + timerSensor.getTimer() + ")");
                break;
            case RESET:
                StringManipulation.appendCustomString(this.sb, this.indentation, false, false, "hal.resetTimer(" + timerSensor.getTimer() + ");");
                break;
            default:
                throw new DbcException("Invalid Time Mode!");
        }
    }

    @Override
    public void visit(TouchSensor touchSensor) {
        StringManipulation.appendCustomString(this.sb, this.indentation, false, false, "hal.isPressed(" + touchSensor.getPort() + ")");
    }

    @Override
    public void visit(UltrasonicSensor ultrasonicSensor) {
        switch ( ultrasonicSensor.getMode() ) {
            case GET_MODE:
                StringManipulation.appendCustomString(this.sb, this.indentation, false, false, "hal.getUltraSonicSensorModeName("
                    + ultrasonicSensor.getPort().toString()
                    + ")");
                break;
            case GET_SAMPLE:
                StringManipulation.appendCustomString(this.sb, this.indentation, false, false, "hal.getUltraSonicSensorValue("
                    + ultrasonicSensor.getPort().toString()
                    + ")");
                break;
            default:
                StringManipulation.appendCustomString(this.sb, this.indentation, false, false, "hal.setUltrasonicSensorMode("
                    + ultrasonicSensor.getPort().toString()
                    + ", "
                    + ultrasonicSensor.getMode().toString()
                    + ");");
                break;
        }
    }

    private boolean parenthesesCheck(Binary binary) {
        return binary.getOp() == Op.MINUS && binary.getRight().getKind() == Kind.BINARY && binary.getRight().getPrecedence() <= binary.getPrecedence();
    }

    private void generateSubExpr(StringBuilder sb, boolean minusAdaption, Expr expr, Binary binary) {
        JavaVisitor visitor = new JavaVisitor(sb, this.indentation, this.brickConfiguration);
        if ( expr.getPrecedence() >= binary.getPrecedence() && !minusAdaption ) {
            // parentheses are omitted
            expr.accept(visitor);
        } else {
            sb.append("(" + StringManipulation.generateString(PreattyPrintSettings.whiteSpaceSize, false, false, null));
            expr.accept(visitor);
            sb.append(StringManipulation.generateString(PreattyPrintSettings.whiteSpaceSize, false, false, ")"));
        }
    }

    private void generateExprCode(Unary unary, StringBuilder sb, int indentation) {
        JavaVisitor visitor = new JavaVisitor(this.sb, this.indentation, this.brickConfiguration);
        if ( unary.getExpr().getPrecedence() < unary.getPrecedence() ) {
            sb.append("(");
            unary.getExpr().accept(visitor);
            sb.append(")");
        } else {
            unary.getExpr().accept(visitor);
        }
    }

    private void generateCodeFromTernary(IfStmt ifStmt, JavaVisitor visitor) {
        this.sb.append("(" + StringManipulation.generateString(PreattyPrintSettings.whiteSpaceSize, false, false, null));
        ifStmt.getExpr().get(0).accept(visitor);
        this.sb.append(StringManipulation.generateString(PreattyPrintSettings.whiteSpaceSize, false, false, ")")
            + StringManipulation.generateString(PreattyPrintSettings.whiteSpaceSize, false, false, "?")
            + StringManipulation.generateString(PreattyPrintSettings.whiteSpaceSize, false, false, null));
        ((ExprStmt) ifStmt.getThenList().get(0).get().get(0)).getExpr().accept(visitor);
        StringManipulation.appendCustomString(
            this.sb,
            PreattyPrintSettings.whiteSpaceSize,
            false,
            false,
            ":" + StringManipulation.generateString(PreattyPrintSettings.whiteSpaceSize, false, false, null));
        ((ExprStmt) ifStmt.getElseList().get().get(0)).getExpr().accept(visitor);
    }

    private void generateCodeFromIfElse(IfStmt ifStmt, int next, JavaVisitor visitor) {
        for ( int i = 0; i < ifStmt.getExpr().size(); i++ ) {
            if ( i == 0 ) {
                generateCodeFromStmtCondition("if", ifStmt.getExpr().get(i), visitor, this.indentation);
            } else {
                generateCodeFromStmtCondition("else if", ifStmt.getExpr().get(i), visitor, PreattyPrintSettings.whiteSpaceSize);
            }
            visitor.setIndentation(next);
            ifStmt.getThenList().get(i).accept(visitor);
            if ( i + 1 < ifStmt.getExpr().size() ) {
                StringManipulation.appendCustomString(this.sb, this.indentation, PreattyPrintSettings.newLineBeforeCloseBracket, false, "}");
            }
        }
    }

    private void generateCodeFromElse(IfStmt ifStmt, int next, JavaVisitor visitor) {
        visitor.setIndentation(next);
        if ( ifStmt.getElseList().get().size() != 0 ) {
            StringManipulation.appendCustomString(
                this.sb,
                this.indentation,
                PreattyPrintSettings.newLineBeforeCloseBracket,
                false,
                "}" + StringManipulation.generateString(PreattyPrintSettings.whiteSpaceSize, PreattyPrintSettings.newLineAfterCloseBracket, false, "else"));
            this.sb.append(StringManipulation.generateString(PreattyPrintSettings.whiteSpaceSize, PreattyPrintSettings.newLineBeforeOpenBracket, false, "{"));
            ifStmt.getElseList().accept(visitor);
        }
        StringManipulation.appendCustomString(this.sb, this.indentation, PreattyPrintSettings.newLineBeforeCloseBracket, false, "}");
    }

    private void generateCodeFromStmtCondition(String stmtType, Expr expr, JavaVisitor visitor, int indentitation) {
        StringManipulation.appendCustomString(
            this.sb,
            indentitation,
            false,
            false,
            stmtType
                + StringManipulation.generateString(PreattyPrintSettings.whiteSpaceSize, false, false, "(")
                + StringManipulation.generateString(PreattyPrintSettings.whiteSpaceSize, false, false, null));
        visitor.setIndentation(0);
        expr.accept(visitor);
        this.sb.append(StringManipulation.generateString(PreattyPrintSettings.whiteSpaceSize, false, false, ")")
            + StringManipulation.generateString(PreattyPrintSettings.whiteSpaceSize, PreattyPrintSettings.newLineBeforeOpenBracket, false, "{"));
    }

}
