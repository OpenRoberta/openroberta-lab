package de.fhg.iais.roberta.codegen.lejos;

import org.apache.commons.lang3.StringEscapeUtils;

import de.fhg.iais.roberta.ast.syntax.Phrase.Kind;
import de.fhg.iais.roberta.ast.syntax.action.ClearDisplayAction;
import de.fhg.iais.roberta.ast.syntax.action.DriveAction;
import de.fhg.iais.roberta.ast.syntax.action.LightAction;
import de.fhg.iais.roberta.ast.syntax.action.LightStatusAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorGetPowerAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorOnAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorSetPowerAction;
import de.fhg.iais.roberta.ast.syntax.action.MotorStopAction;
import de.fhg.iais.roberta.ast.syntax.action.PlayFileAction;
import de.fhg.iais.roberta.ast.syntax.action.ShowPictureAction;
import de.fhg.iais.roberta.ast.syntax.action.ShowTextAction;
import de.fhg.iais.roberta.ast.syntax.action.StopAction;
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

public class JavaVisitor implements Visitor {

    private final int indentationSize = 4;
    private final int whiteSpaceSize = 1;
    private final boolean newLineAfterOpenBracket = true;
    private final boolean newLineBeforeOpenBracket = false;
    private final boolean newLineAfterCloseBracket = false;
    private final boolean newLineBeforeCloseBracket = true;

    private StringBuilder sb;
    private int indentation;

    public JavaVisitor(StringBuilder sb, int indentation) {
        this.sb = sb;
        this.indentation = indentation;
    }

    public int getIndentation() {
        return this.indentation;
    }

    public void setIndentation(int indentation) {
        this.indentation = indentation;
    }

    public StringBuilder getSb() {
        return this.sb;
    }

    public void setSb(StringBuilder sb) {
        this.sb = sb;
    }

    @Override
    public void visit(NumConst numConst) {
        this.sb.append(numConst.getValue());
    }

    @Override
    public void visit(ActionExpr actionExpr) {
        this.sb.append(actionExpr.getAction());
    }

    @Override
    public void visit(Binary binary) {
        generateSubExpr(this.sb, false, binary.getLeft(), binary);
        this.sb.append(StringManipulation.generateString(this.whiteSpaceSize, false, null)
            + binary.getOp().getOpSymbol()
            + StringManipulation.generateString(this.whiteSpaceSize, false, null));
        generateSubExpr(this.sb, parenthesesCheck(binary), binary.getRight(), binary);
    }

    private boolean parenthesesCheck(Binary binary) {
        return binary.getOp() == Op.MINUS && binary.getRight().getKind() == Kind.BINARY && binary.getRight().getPrecedence() <= binary.getPrecedence();
    }

    private void generateSubExpr(StringBuilder sb, boolean minusAdaption, Expr expr, Binary binary) {
        JavaVisitor visitor = new JavaVisitor(sb, this.indentation);
        if ( expr.getPrecedence() >= binary.getPrecedence() && !minusAdaption ) {
            // parentheses are omitted
            expr.accept(visitor);
        } else {
            sb.append("(" + StringManipulation.generateString(this.whiteSpaceSize, false, null));
            expr.accept(visitor);
            sb.append(StringManipulation.generateString(this.whiteSpaceSize, false, ")"));
        }
    }

    @Override
    public void visit(BoolConst boolConst) {
        this.sb.append(boolConst.isValue());
    }

    @Override
    public void visit(ColorConst colorConst) {
        this.sb.append("\"").append(StringEscapeUtils.escapeJava(colorConst.getValue())).append("\"");
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
        JavaVisitor visitor = new JavaVisitor(this.sb, this.indentation);
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
    public void visit(MathConst mathConst) {
        this.sb.append(mathConst.getMathConst());
    }

    @Override
    public void visit(NullConst nullConst) {
        this.sb.append("null");
    }

    @Override
    public void visit(SensorExpr sensorExpr) {
        this.sb.append(sensorExpr.getSens());
    }

    @Override
    public void visit(StringConst stringConst) {
        this.sb.append("\"").append(StringEscapeUtils.escapeJava(stringConst.getValue())).append("\"");
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

    private void generateExprCode(Unary unary, StringBuilder sb, int indentation) {
        JavaVisitor visitor = new JavaVisitor(this.sb, this.indentation);
        if ( unary.getExpr().getPrecedence() < unary.getPrecedence() ) {
            sb.append("(");
            unary.getExpr().accept(visitor);
            sb.append(")");
        } else {
            unary.getExpr().accept(visitor);
        }
    }

    @Override
    public void visit(Var var) {
        switch ( var.getTypeVar() ) {
            case INTEGER:
                this.sb.append(StringManipulation.generateString(this.indentation, false, "int ") + var.getValue());
                break;
            default:
                this.sb.append(var.getValue());
                break;
        }
    }

    @Override
    public void visit(Funct funct) {
        switch ( funct.getFunctName() ) {
            case PRINT:
                JavaVisitor visitor = new JavaVisitor(this.sb, this.indentation);
                this.sb.append("System.out.println(");
                funct.getParam().get(0).accept(visitor);
                this.sb.append(")");
                break;
            default:
                break;
        }
    }

    @Override
    public void visit(BrickSensor brickSensor) {
        switch ( brickSensor.getMode() ) {
            case IS_PRESSED:
                this.sb.append("hal.isPressed(" + brickSensor.getKey().toString() + ")");
                break;
            case WAIT_FOR_PRESS_AND_RELEASE:
                this.sb.append("hal.isPressedAndReleased(" + brickSensor.getKey().toString() + ")");
                break;
            default:
                throw new DbcException("Invalide mode for BrickSensor!");
        }
    }

    @Override
    public void visit(ColorSensor colorSensor) {
        switch ( colorSensor.getMode() ) {
            case GET_MODE:
                this.sb.append("hal.getColorSensorModeName(" + colorSensor.getPort().toString() + ")");
                break;
            case GET_SAMPLE:
                this.sb.append("hal.getColorSensorValue(" + colorSensor.getPort().toString() + ")");
                break;
            default:
                this.sb.append("hal.setColorSensorMode(" + colorSensor.getPort().toString() + ", " + colorSensor.getMode().toString() + ");");
                break;
        }
    }

    @Override
    public void visit(EncoderSensor encoderSensor) {
        switch ( encoderSensor.getMode() ) {
            case GET_MODE:
                this.sb.append("hal.getMotorTachoMode(" + encoderSensor.getMotor().toString() + ")");
                break;
            case GET_SAMPLE:
                this.sb.append("hal.getMotorTachoValue(" + encoderSensor.getMotor().toString() + ")");
                break;
            case RESET:
                this.sb.append("hal.resetMotorTacho(" + encoderSensor.getMotor().toString() + ");");
                break;
            default:
                this.sb.append("hal.setMotorTachoMode(" + encoderSensor.getMotor().toString() + ", " + encoderSensor.getMode().toString() + ");");
                break;
        }
    }

    @Override
    public void visit(GyroSensor gyroSensor) {
        switch ( gyroSensor.getMode() ) {
            case GET_MODE:
                this.sb.append("hal.getGyroSensorModeName(" + gyroSensor.getPort().toString() + ")");
                break;
            case GET_SAMPLE:
                this.sb.append("hal.getGyroSensorValue(" + gyroSensor.getPort().toString() + ")");
                break;
            case RESET:
                this.sb.append("hal.resetGyroSensor(" + gyroSensor.getPort().toString() + ");");
                break;
            default:
                this.sb.append("hal.setGyroSensorMode(" + gyroSensor.getPort().toString() + ", " + gyroSensor.getMode().toString() + ");");
                break;
        }
    }

    @Override
    public void visit(InfraredSensor infraredSensor) {
        switch ( infraredSensor.getMode() ) {
            case GET_MODE:
                this.sb.append("hal.getInfraredSensorModeName(" + infraredSensor.getPort().toString() + ")");
                break;
            case GET_SAMPLE:
                this.sb.append("hal.getInfraredSensorValue(" + infraredSensor.getPort().toString() + ")");
                break;
            default:
                this.sb.append("hal.setInfraredSensorMode(" + infraredSensor.getPort().toString() + ", " + infraredSensor.getMode().toString() + ");");
                break;
        }
    }

    @Override
    public void visit(TimerSensor timerSensor) {
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
    }

    @Override
    public void visit(TouchSensor touchSensor) {
        this.sb.append("hal.isPressed(" + touchSensor.getPort() + ")");
    }

    @Override
    public void visit(UltrasonicSensor ultrasonicSensor) {
        switch ( ultrasonicSensor.getMode() ) {
            case GET_MODE:
                this.sb.append("hal.getUltraSonicSensorModeName(" + ultrasonicSensor.getPort().toString() + ")");
                break;
            case GET_SAMPLE:
                this.sb.append("hal.getUltraSonicSensorValue(" + ultrasonicSensor.getPort().toString() + ")");
                break;
            default:
                this.sb.append("hal.setUltrasonicSensorMode(" + ultrasonicSensor.getPort().toString() + ", " + ultrasonicSensor.getMode().toString() + ");");
                break;
        }
    }

    @Override
    public void visit(ActionStmt actionStmt) {
        StringManipulation.appendCustomString(this.sb, this.indentation, true, null);
        this.sb.append("SensorStmt ").append(actionStmt.getAction());
    }

    @Override
    public void visit(AssignStmt assignStmt) {
        JavaVisitor visitor = new JavaVisitor(this.sb, this.indentation);
        assignStmt.getName().accept(visitor);
        this.sb.append(" = ");
        assignStmt.getExpr().accept(visitor);
        this.sb.append(";");
    }

    @Override
    public void visit(ExprStmt exprStmt) {
        JavaVisitor visitor = new JavaVisitor(this.sb, this.indentation);
        exprStmt.getExpr().accept(visitor);
        this.sb.append(";");
    }

    @Override
    public void visit(IfStmt ifStmt) {
        int next = this.indentation + this.indentationSize;
        JavaVisitor visitor = new JavaVisitor(this.sb, this.indentation);
        if ( ifStmt.isTernary() ) {
            generateCodeFromTernary(ifStmt, visitor);
        } else {
            generateCodeFromIfElse(ifStmt, next, visitor);
            generateCodeFromElse(ifStmt, visitor);
        }
    }

    private void generateCodeFromTernary(IfStmt ifStmt, JavaVisitor visitor) {
        this.sb.append("(" + StringManipulation.generateString(this.whiteSpaceSize, false, null));
        ifStmt.getExpr().get(0).accept(visitor);
        this.sb.append(StringManipulation.generateString(this.whiteSpaceSize, false, ")")
            + StringManipulation.generateString(this.whiteSpaceSize, false, "?")
            + StringManipulation.generateString(this.whiteSpaceSize, false, null));
        ((ExprStmt) ifStmt.getThenList().get(0).get().get(0)).getExpr().accept(visitor);
        StringManipulation.appendCustomString(this.sb, this.whiteSpaceSize, false, ":" + StringManipulation.generateString(this.whiteSpaceSize, false, null));
        ((ExprStmt) ifStmt.getElseList().get().get(0)).getExpr().accept(visitor);
    }

    private void generateCodeFromIfElse(IfStmt ifStmt, int next, JavaVisitor visitor) {
        for ( int i = 0; i < ifStmt.getExpr().size(); i++ ) {
            generateCodeFromStmtCondition("if", ifStmt.getExpr().get(i), visitor);
            visitor.setIndentation(next);
            ifStmt.getThenList().get(i).accept(visitor);
            if ( i + 1 < ifStmt.getExpr().size() ) {
                StringManipulation.appendCustomString(
                    this.sb,
                    this.indentation,
                    this.newLineBeforeCloseBracket,
                    "}"
                        + StringManipulation.generateString(
                            this.whiteSpaceSize,
                            this.newLineAfterCloseBracket,
                            "else" + StringManipulation.generateString(this.whiteSpaceSize, false, null)));
            }
        }
    }

    private void generateCodeFromElse(IfStmt ifStmt, JavaVisitor visitor) {
        if ( ifStmt.getElseList().get().size() != 0 ) {
            StringManipulation.appendCustomString(
                this.sb,
                this.indentation,
                this.newLineBeforeCloseBracket,
                "}" + StringManipulation.generateString(this.whiteSpaceSize, this.newLineAfterCloseBracket, "else"));
            this.sb.append(StringManipulation.generateString(this.whiteSpaceSize, this.newLineBeforeOpenBracket, "{"));
            ifStmt.getElseList().accept(visitor);
        }
        StringManipulation.appendCustomString(this.sb, this.indentation, this.newLineBeforeCloseBracket, "}");
    }

    private void generateCodeFromStmtCondition(String stmtType, Expr expr, JavaVisitor visitor) {
        this.sb.append(stmtType
            + StringManipulation.generateString(this.whiteSpaceSize, false, "(")
            + StringManipulation.generateString(this.whiteSpaceSize, false, null));
        expr.accept(visitor);
        this.sb.append(StringManipulation.generateString(this.whiteSpaceSize, false, ")")
            + StringManipulation.generateString(this.whiteSpaceSize, this.newLineBeforeOpenBracket, "{"));
    }

    @Override
    public void visit(RepeatStmt repeatStmt) {
        int next = this.indentation + this.indentationSize;
        JavaVisitor visitor = new JavaVisitor(this.sb, 0);
        switch ( repeatStmt.getMode() ) {
            case UNTIL:
            case WHILE:
                generateCodeFromStmtCondition("while", repeatStmt.getExpr(), visitor);
                break;
            case TIMES:
            case FOR:
                generateCodeFromStmtCondition("for", repeatStmt.getExpr(), visitor);
                break;
            case FOR_EACH:
                break;

            default:
                break;
        }
        visitor.setIndentation(next);
        repeatStmt.getList().accept(visitor);
        StringManipulation.appendCustomString(this.sb, this.indentation, this.newLineBeforeCloseBracket, "}");
    }

    @Override
    public void visit(SensorStmt sensorStmt) {
        StringManipulation.appendCustomString(this.sb, this.indentation, true, null);
        this.sb.append("SensorStmt ").append(sensorStmt.getSensor());
    }

    @Override
    public void visit(StmtFlowCon stmtFlowCon) {
        StringManipulation.appendCustomString(this.sb, 0, false, stmtFlowCon.getFlow().toString().toLowerCase() + ";");
    }

    @Override
    public void visit(StmtList stmtList) {
        boolean first = true;
        JavaVisitor visitor = new JavaVisitor(this.sb, this.indentation);
        for ( Stmt stmt : stmtList.get() ) {
            if ( first ) {
                first = false;
                StringManipulation.appendCustomString(this.sb, this.indentation, this.newLineAfterOpenBracket, null);
            } else {
                StringManipulation.appendCustomString(this.sb, this.indentation, true, null);
            }
            stmt.accept(visitor);
        }
    }

    @Override
    public void visit(VolumeAction volumeAction) {
        JavaVisitor visitor = new JavaVisitor(this.sb, this.indentation);
        switch ( volumeAction.getMode() ) {
            case SET:
                this.sb.append("hal.setVolume(");
                volumeAction.getVolume().accept(visitor);
                this.sb.append(");");
                break;
            case GET:
                this.sb.append("hal.getVolume()");
                break;
            default:
                throw new DbcException("Invalid volume action mode!");
        }
    }

    @Override
    public void visit(ClearDisplayAction clearDisplayAction) {
        this.sb.append("hal.clearDisplay();");
    }

    @Override
    public void visit(DriveAction driveAction) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(LightAction lightAction) {
        this.sb.append("hal.ledOn(" + lightAction.getColor().toString() + ", " + lightAction.isBlink() + ");");
    }

    @Override
    public void visit(LightStatusAction lightStatusAction) {
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
    }

    @Override
    public void visit(MotorGetPowerAction motorGetPowerAction) {
        this.sb.append("hal.getSpeed(" + motorGetPowerAction.getPort().toString() + ")");
    }

    @Override
    public void visit(MotorOnAction motorOnAction) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(MotorSetPowerAction motorSetPowerAction) {
        JavaVisitor visitor = new JavaVisitor(this.sb, this.indentation);
        this.sb.append("hal.setMotorSpeed(" + motorSetPowerAction.getPort().name() + ", ");
        motorSetPowerAction.getPower().accept(visitor);
        this.sb.append(");");
    }

    @Override
    public void visit(MotorStopAction motorStopAction) {
        this.sb.append("hal.stopMotor(" + motorStopAction.getPort().toString() + ", " + motorStopAction.getMode().toString() + ");");
    }

    @Override
    public void visit(PlayFileAction playFileAction) {
        this.sb.append("hal.playFile(\"" + playFileAction.getFileName() + "\");");
    }

    @Override
    public void visit(ShowPictureAction showPictureAction) {
        JavaVisitor visitor = new JavaVisitor(this.sb, this.indentation);
        this.sb.append("hal.drawPicture(\"" + showPictureAction.getPicture() + "\", ");
        showPictureAction.getX().accept(visitor);
        this.sb.append(", ");
        showPictureAction.getY().accept(visitor);
        this.sb.append(");");
    }

    @Override
    public void visit(ShowTextAction showTextAction) {
        JavaVisitor visitor = new JavaVisitor(this.sb, this.indentation);
        this.sb.append("hal.drawText(");
        showTextAction.getMsg().accept(visitor);
        this.sb.append(", ");
        showTextAction.getX().accept(visitor);
        this.sb.append(", ");
        showTextAction.getY().accept(visitor);
        this.sb.append(");");
    }

    @Override
    public void visit(StopAction stopAction) {
        // TODO Auto-generated method stub

    }

    @Override
    public void visit(ToneAction toneAction) {
        JavaVisitor visitor = new JavaVisitor(this.sb, this.indentation);
        this.sb.append("hal.playTone(");
        toneAction.getFrequency().accept(visitor);
        this.sb.append(", ");
        toneAction.getDuration().accept(visitor);
        this.sb.append(");");
    }

    @Override
    public void visit(TurnAction turnAction) {
        // TODO Auto-generated method stub

    }
}
