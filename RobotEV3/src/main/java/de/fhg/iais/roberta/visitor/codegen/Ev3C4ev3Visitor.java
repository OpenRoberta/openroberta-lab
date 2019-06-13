package de.fhg.iais.roberta.visitor.codegen;

import com.google.common.collect.Lists;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.inter.mode.action.*;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.MotorMoveMode;
import de.fhg.iais.roberta.mode.action.MotorStopMode;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.communication.*;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.ev3.ShowPictureAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.action.speech.SayTextAction;
import de.fhg.iais.roberta.syntax.action.speech.SetLanguageAction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.*;
import de.fhg.iais.roberta.syntax.lang.functions.*;
import de.fhg.iais.roberta.syntax.lang.stmt.AssignStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.*;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.collect.Ev3UsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.visitor.hardware.IEv3Visitor;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractCppVisitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Ev3C4ev3Visitor extends AbstractCppVisitor implements IEv3Visitor<Void> {

    private static final List<String> ev3SensorPorts = Lists.newArrayList("1", "2", "3", "4");

    // TODO: Are those constants already defined somewhere?
    private static final String PROPERTY_ON = "ON";

    private static final String PREFIX_OUTPUT_PORT = "OUT_";
    private static final String PREFIX_IN_PORT = "IN_";

    private final ILanguage language;

    private final Configuration brickConfiguration;

    private final Set<UsedActor> usedActors;

    /**
     * initialize the EV3 c4ev3 code generator visitor.
     *
     * @param programPhrases
     * @param indentation to start with. Will be incr/decr depending on block structure
     */
    private Ev3C4ev3Visitor(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> programPhrases, int indentation, ILanguage language) {
        super(programPhrases, indentation);
        Ev3UsedHardwareCollectorVisitor checkVisitor = new Ev3UsedHardwareCollectorVisitor(programPhrases, brickConfiguration);
        this.brickConfiguration = brickConfiguration;
        this.language = language;
        this.usedActors = checkVisitor.getUsedActors();
        this.loopsLabels = checkVisitor.getloopsLabelContainer();
    }

    private static String getPrefixedOutputPort(String port) {
        return PREFIX_OUTPUT_PORT + port;
    }

    private static String getPrefixedInputPort(String port) {
        return PREFIX_IN_PORT + port;
    }

    /**
     * factory method to generate EV3 c4ev3 code from an AST.<br>
     *
     * @param programName
     * @param brickConfiguration
     * @param phrasesSet
     * @param withWrapping
     * @param language
     * @return
     */
    public static String generate(
        String programName, Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping, ILanguage language) {
        Assert.notNull(programName);
        Assert.notNull(brickConfiguration);

        Ev3C4ev3Visitor astVisitor = new Ev3C4ev3Visitor(brickConfiguration, phrasesSet, 0, language);
        astVisitor.generateCode(withWrapping);
        return astVisitor.sb.toString();
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        generateConstants();
        generateImports();
    }

    private void generateConstants() {
        this.sb.append("#define WHEEL_DIAMETER " + brickConfiguration.getWheelDiameterCM() + "\n");
        this.sb.append("#define TRACK_WIDTH " + brickConfiguration.getTrackWidthCM() + "\n");
        nlIndent();
    }

    private void generateImports() {
        this.sb.append("#include <ev3.h>\n");
        this.sb.append("#include <math.h>\n");
        this.sb.append("#include <list>\n");
        this.sb.append("#include \"NEPODefs.h\"");
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        mainTask.getVariables().visit(this);
        nlIndent();
        this.sb.append("int main () {");
        incrIndentation();
        nlIndent();
        this.sb.append("InitEV3();");
        nlIndent();
        generateSensorInitialization();
        nlIndent();
        return null;
    }

    private void generateSensorInitialization() {
        this.sb.append("setAllSensorMode(").append(getDefaultSensorModesString()).append(");");
    }

    private String getDefaultSensorModesString() {
        return ev3SensorPorts.stream()
            .map(brickConfiguration::optConfigurationComponent)
            .map(configuration -> configuration == null ? null : "DEFAULT_MODE_" + configuration.getComponentType())
            .map(componentType -> componentType == null ? "NO_SEN" : componentType)
            .collect(Collectors.joining(", "));
    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        nlIndent();
        nlIndent();
        this.sb.append("FreeEV3();");
        nlIndent();
        this.sb.append("return 0;");
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        nlIndent();
    }

    @Override
    public Void visitMathConst(MathConst<Void> mathConst) {
        String constantName = getCMathConstantName(mathConst.getMathConst());
        this.sb.append(constantName);
        return null;
    }

    private String getCMathConstantName(MathConst.Const constant) {
        switch ( constant ) {
            case PI:
                return "M_PI";
            case E:
                return "M_E";
            case GOLDEN_RATIO:
                return "GOLDEN_RATIO";
            case SQRT2:
                return "M_SQRT2";
            case SQRT1_2:
                return "M_SQRT1_2";
            // IEEE 754 floating point representation
            case INFINITY:
                return "HUGE_VAL";
            default:
                throw new DbcException("unknown constant");
        }
    }

    @Override
    public Void visitColorConst(ColorConst<Void> colorConst) {
        String colorConstant = getColorConstantByHex(colorConst.getHexValueAsString());
        this.sb.append(colorConstant);
        return null;
    }

    private String getColorConstantByHex(String hex) {
        String color;
        switch ( hex.toUpperCase() ) {
            case "#000000":
                color = "BLACK";
                break;
            case "#0057A6":
                color = "BLUE";
                break;
            case "#00642E":
                color = "GREEN";
                break;
            case "#F7D117":
                color = "YELLOW";
                break;
            case "#B30006":
                color = "RED";
                break;
            case "#FFFFFF":
                color = "WHITE";
                break;
            case "#532115":
                color = "BROWN";
                break;
            case "#EE82EE":
                color = "VIOLET";
                break;
            case "#800080":
                color = "PURPLE";
                break;
            case "#00FF00":
                color = "LIME";
                break;
            case "#FFA500":
                color = "ORANGE";
                break;
            case "#FF00FF":
                color = "MAGENTA";
                break;
            case "#DC143C":
                color = "CRIMSON";
                break;
            case "#585858":
                color = "NULL";
                break;
            default:
                throw new DbcException("Invalid color constant: " + hex);
        }
        return "INPUT_" + color + "COLOR";
    }

    @Override
    public Void visitBinary(Binary<Void> binary) {
        // TODO: Clean
        Binary.Op op = binary.getOp();
        if ( op == Binary.Op.ADD || op == Binary.Op.MINUS || op == Binary.Op.DIVIDE || op == Binary.Op.MULTIPLY ) {
            this.sb.append("(");
        }
        generateSubExpr(this.sb, false, binary.getLeft(), binary);
        String sym = getBinaryOperatorSymbol(op);
        this.sb.append(whitespace() + sym + whitespace());
        switch ( op ) {
            case TEXT_APPEND:
                if ( binary.getRight().getVarType().toString().contains("NUMBER") ) {
                    this.sb.append("NumToStr(");
                    generateSubExpr(this.sb, false, binary.getRight(), binary);
                    this.sb.append(")");
                } else {
                    generateSubExpr(this.sb, false, binary.getRight(), binary);
                }
                break;
            case DIVIDE:
                this.sb.append("((");
                generateSubExpr(this.sb, parenthesesCheck(binary), binary.getRight(), binary);
                this.sb.append(")*1.0)");
                break;

            default:
                generateSubExpr(this.sb, parenthesesCheck(binary), binary.getRight(), binary);
        }
        if ( op == Binary.Op.ADD || op == Binary.Op.MINUS || op == Binary.Op.DIVIDE || op == Binary.Op.MULTIPLY ) {
            this.sb.append(")");
        }
        return null;
    }

    @Override
    public Void visitAssignStmt(AssignStmt<Void> assignStmt) {
        if(isSensorColorExpression(assignStmt.getExpr()) || isEV3IRSeekExpression(assignStmt.getExpr())) {
            generateAssignmentOfList(assignStmt);
            return null;
        }
        return super.visitAssignStmt(assignStmt);
    }

    private boolean isSensorColorExpression (Expr<Void> expression) {
        return expression instanceof SensorExpr && ((SensorExpr)expression).getSens() instanceof ColorSensor;
    }

    private void generateAssignmentOfList(AssignStmt<Void> assignStmt) {
        this.sb.append("_copyList(");
        assignStmt.getExpr().visit(this);
        this.sb.append(", ");
        assignStmt.getName().visit(this);
        this.sb.append(");");
    }

    private boolean isEV3IRSeekExpression (Expr<Void> expression) {
        return expression instanceof SensorExpr
            && ((SensorExpr)expression).getSens() instanceof InfraredSensor
            && ((InfraredSensor) ((SensorExpr)expression).getSens()).getMode().equals(SC.PRESENCE);
    }

    @Override
    public Void visitRepeatStmt(RepeatStmt<Void> repeatStmt) {
        Expr<Void> condition = repeatStmt.getExpr();
        boolean isWaitStmt = repeatStmt.getMode() == RepeatStmt.Mode.WAIT;
        if ( !isWaitStmt ) {
            increaseLoopCounter();
        }
        switch ( repeatStmt.getMode() ) {
            case UNTIL:
            case WHILE:
            case FOREVER:
                generateCodeFromStmtCondition("while", condition);
                break;
            case TIMES:
            case FOR:
                generateCodeFromStmtConditionFor(condition);
                break;
            case WAIT:
                generateCodeFromStmtCondition("if", condition);
                break;
            case FOR_EACH:
                generateForEachPrefix(condition);
        }
        incrIndentation();
        repeatStmt.getList().visit(this);
        if ( !isWaitStmt ) {
            addContinueLabelToLoop();
        } else {
            appendBreakStmt();
        }
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        addBreakLabelToLoop(isWaitStmt);
        return null;
    }

    private  void generateCodeFromStmtConditionFor(Expr<Void> expr) {
        final ExprList<Void> expressions = (ExprList<Void>) expr;
        Expr<Void> counterName = expressions.get().get(0);
        Expr<Void> counterInitialValue = expressions.get().get(1);
        Expr<Void> counterTargetValue = expressions.get().get(2);
        Expr<Void> counterStep = expressions.get().get(3);
        this.sb.append("for (" + "float ");
        counterName.visit(this);
        this.sb.append(" = ");
        counterInitialValue.visit(this);
        this.sb.append("; ");
        counterName.visit(this);
        this.sb.append(" < ");
        counterTargetValue.visit(this);
        this.sb.append("; ");
        counterName.visit(this);
        this.sb.append(" += ");
        counterStep.visit(this);
        this.sb.append(") {");
    }

    private void generateForEachPrefix(Expr<Void> expression) {
        ((VarDeclaration<Void>) ((Binary<Void>) expression).getLeft()).visit(this);
        this.sb.append(";");
        nlIndent();
        this.sb.append("for(int i = 0; i < ArrayLen(");
        this.sb.append(((Var<Void>) ((Binary<Void>) expression).getRight()).getValue());
        this.sb.append("); ++i) {");
        incrIndentation();
        nlIndent();
        this.sb.append(((VarDeclaration<Void>) ((Binary<Void>) expression).getLeft()).getName());
        this.sb.append(" = _getListElementByIndex(");
        this.sb.append(((Var<Void>) ((Binary<Void>) expression).getRight()).getValue());
        this.sb.append(", i);");
        decrIndentation();
    }

    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        this.sb.append("Wait(");
        waitTimeStmt.getTime().visit(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        return null;
    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct<Void> mathConstrainFunct) {
        return null;
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct<Void> mathNumPropFunct) {
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct<Void> textJoinFunct) {
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        String port = motorOnAction.getUserDefinedPort();
        MotorDuration<Void> duration = motorOnAction.getParam().getDuration();
        Expr<Void> speedExpression = motorOnAction.getParam().getSpeed();
        if ( isActorOnPort(port) ) {
            boolean isReverse = isMotorReverse(port);
            boolean isRegulated = brickConfiguration.isMotorRegulated(port);
            if ( duration != null ) {
                generateRotateMotorForDuration(port, speedExpression, motorOnAction.getDurationMode(), duration.getValue());
            } else {
                generateTurnOnMotor(port, speedExpression, isReverse, isRegulated);
            }
        }
        return null;
    }

    private void generateRotateMotorForDuration(String port, Expr<Void> speedExpression, IMotorMoveMode durationMode, Expr<Void> durationExpression) {
        this.sb.append("RotateMotorForAngle(" + getPrefixedOutputPort(port) + ", ");
        visitSpeedExpression(speedExpression);
        this.sb.append(", ");
        if ( durationMode == MotorMoveMode.ROTATIONS ) {
            this.sb.append("360 * ");
        }
        durationExpression.visit(this);
        this.sb.append(");");
    }

    private void generateTurnOnMotor(String port, Expr<Void> speedExpression, boolean isReverse, boolean isRegulated) {
        String methodNamePart = isReverse ? "OnRev" : "OnFwd";
        if ( isRegulated ) {
            methodNamePart += "Reg";
        }
        this.sb.append(methodNamePart + "(" + getPrefixedOutputPort(port) + ", ");
        visitSpeedExpression(speedExpression);
        this.sb.append(");");
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        MotorDuration<Void> duration = driveAction.getParam().getDuration();
        Expr<Void> speedExpression = driveAction.getParam().getSpeed();
        boolean reverse = isReverseGivenBrickConfigurationAndAction(driveAction.getDirection());
        if ( duration != null ) {
            generateDriveForDistance(speedExpression, duration.getValue(), reverse);
        } else {
            generateDrive(speedExpression, reverse);
        }
        return null;
    }

    private void generateDriveForDistance(Expr<Void> speedExpression, Expr<Void> distanceExpression, boolean reverse) {
        this.sb.append("RotateMotorForAngle(" + getDriveMotorPorts() + ", ");
        visitSpeedExpression(speedExpression, reverse);
        this.sb.append(", ");
        visitDistanceOfDrive(distanceExpression);
        this.sb.append(");");
    }

    private void visitDistanceOfDrive(Expr<Void> distanceExpression) {
        this.sb.append("(");
        distanceExpression.visit(this);
        this.sb.append(" * 360) / (M_PI * WHEEL_DIAMETER)");
    }

    private void generateDrive(Expr<Void> speedExpression, boolean reverse) {
        String methodName = reverse ? "OnRevSync" : "OnFwdSync";
        this.sb.append(methodName + "(" + getDriveMotorPorts() + ", ");
        visitSpeedExpression(speedExpression);
        this.sb.append(");");
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        String port = motorGetPowerAction.getUserDefinedPort();
        if ( isActorOnPort(port) ) {
            this.sb.append("MotorPower(OUT_" + motorGetPowerAction.getUserDefinedPort() + ")");
        }
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        String port = motorSetPowerAction.getUserDefinedPort();
        this.sb.append("SetPower(" + getPrefixedOutputPort(port) + ", ");
        visitSpeedExpression(motorSetPowerAction.getPower(), isMotorReverse(port));
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        String port = motorStopAction.getUserDefinedPort();
        if ( motorStopAction.getMode() == MotorStopMode.FLOAT ) {
            this.sb.append("Float(" + getPrefixedOutputPort(port) + ");");
        } else {
            this.sb.append("Off(" + getPrefixedOutputPort(port) + ");");
        }
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        this.sb.append("Off(" + getDriveMotorPorts() + ");");
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {
        MotorDuration<Void> duration = curveAction.getParamLeft().getDuration();
        boolean isReverse = isReverseGivenBrickConfigurationAndAction(curveAction.getDirection());
        this.sb.append(duration != null ? "SteerDriveForDistance(" : "SteerDrive(");
        this.sb.append(getLeftDriveMotorPort() + ", " + getRightDriveMotorPort() + ", ");
        visitSpeedExpression(curveAction.getParamLeft().getSpeed(), isReverse);
        this.sb.append(", ");
        visitSpeedExpression(curveAction.getParamRight().getSpeed(), isReverse);
        if ( duration != null ) {
            this.sb.append(", ");
            duration.getValue().visit(this);
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        MotorDuration<Void> duration = turnAction.getParam().getDuration();
        Expr<Void> speedExpression = turnAction.getParam().getSpeed();
        int turn = getTurn(turnAction);
        if ( duration != null ) {
            generateTurnForDistance(speedExpression, duration.getValue(), turn);
        } else {
            generateTurn(speedExpression, turn);
        }
        return null;
    }

    private int getTurn(TurnAction<Void> turnAction) {
        int turn = 100;
        if ( isAnyDriveMotorReverse() ) {
            turn *= -1;
        }
        if ( turnAction.getDirection() == TurnDirection.LEFT ) {
            turn *= -1;
        }
        return turn;
    }

    private void generateTurnForDistance(Expr<Void> speedExpression, Expr<Void> distanceExpression, int turn) {
        this.sb.append("RotateMotorForAngleWithTurn(" + getDriveMotorPorts() + ", ");
        visitSpeedExpression(speedExpression);
        this.sb.append(", ");
        visitDistanceOfTurn(distanceExpression);
        this.sb.append(", " + turn + ");");
    }

    private void visitDistanceOfTurn(Expr<Void> distanceExpression) {
        this.sb.append("(");
        distanceExpression.visit(this);
        this.sb.append(" * TRACK_WIDTH / WHEEL_DIAMETER)");
    }

    private void generateTurn(Expr<Void> speedExpression, int turn) {
        this.sb.append("OnFwdSyncEx(" + getDriveMotorPorts() + ", ");
        visitSpeedExpression(speedExpression);
        this.sb.append(", " + turn + ", RESET_NONE);");

    }

    private void visitSpeedExpression(Expr<Void> speedExpression) {
        visitSpeedExpression(speedExpression, false);
    }

    private void visitSpeedExpression(Expr<Void> speedExpression, boolean reverse) {
        this.sb.append(reverse ? "-Speed(" : "Speed(");
        speedExpression.visit(this);
        this.sb.append(")");
    }

    private boolean isActorOnPort(String port) {
        if ( port != null ) {
            for ( UsedActor actor : this.usedActors ) {
                if ( actor.getPort().equals(port) ) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isMotorReverse(String port) {
        String reverseProperty = this.brickConfiguration.getConfigurationComponent(port).getOptProperty(SC.MOTOR_REVERSE);
        return reverseProperty != null && reverseProperty.equals(PROPERTY_ON);
    }

    private String getDriveMotorPorts() {
        ConfigurationComponent leftMotor = this.brickConfiguration.getFirstMotor(SC.LEFT);
        ConfigurationComponent rightMotor = this.brickConfiguration.getFirstMotor(SC.RIGHT);
        String leftMotorPort = leftMotor.getUserDefinedPortName();
        String rightMotorPort = rightMotor.getUserDefinedPortName();
        return PREFIX_OUTPUT_PORT + createSortedPorts(leftMotorPort, rightMotorPort);
    }

    private static String createSortedPorts(String port1, String port2) {
        Assert.isTrue(port1.length() == 1 && port2.length() == 1);
        char[] charArray = (port1 + port2).toCharArray();
        Arrays.sort(charArray);
        String port = new String(charArray);
        return port;
    }

    private String getLeftDriveMotorPort() {
        return getPrefixedOutputPort(brickConfiguration.getFirstMotor(SC.LEFT).getUserDefinedPortName());
    }

    private String getRightDriveMotorPort() {
        return getPrefixedOutputPort(brickConfiguration.getFirstMotor(SC.RIGHT).getUserDefinedPortName());
    }

    private boolean isReverseGivenBrickConfigurationAndAction(IDriveDirection direction) {
        boolean reverse = isAnyDriveMotorReverse();
        boolean localReverse = direction == DriveDirection.BACKWARD;
        return (reverse && !localReverse) || (localReverse && !reverse);
    }

    private boolean isAnyDriveMotorReverse() {
        ConfigurationComponent leftMotor = this.brickConfiguration.getFirstMotor(SC.LEFT);
        ConfigurationComponent rightMotor = this.brickConfiguration.getFirstMotor(SC.RIGHT);
        return leftMotor.isReverse() || rightMotor.isReverse();
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        String port = encoderSensor.getPort();
        switch ( encoderSensor.getMode() ) {
            case SC.DEGREE:
                generateGetEncoderInDegrees(port);
                break;
            case SC.ROTATION:
                generateGetEncoderInRotations(port);
                break;
            case SC.DISTANCE:
                generateGetEncoderInDistance(port);
                break;
            default:
                throw new DbcException("Unknown encoder mode");
        }
        return null;
    }

    private void generateGetEncoderInDegrees(String port) {
        this.sb.append("MotorRotationCount(" + getPrefixedOutputPort(port) + ")");
    }

    private void generateGetEncoderInRotations(String port) {
        this.sb.append("(");
        this.generateGetEncoderInDegrees(port);
        this.sb.append(" / 360.0)");
    }

    private void generateGetEncoderInDistance(String port) {
        this.sb.append("(");
        this.generateGetEncoderInRotations(port);
        this.sb.append(" * M_PI * WHEEL_DIAMETER)");
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        this.sb.append("readSensor(").append(getPrefixedInputPort(touchSensor.getPort())).append(")");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        String mode = getUltrasonicSensorModeConstant(ultrasonicSensor.getMode());
        generateReadSensorInMode(ultrasonicSensor.getPort(), mode);
        return null;
    }

    private String getUltrasonicSensorModeConstant(String mode) {
        if ( mode.equals(SC.DISTANCE) ) {
            return "US_DIST_CM";
        } else {
            return "US_LISTEN";
        }
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
        generateReadSensor(soundSensor.getPort());
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        String port = gyroSensor.getPort();
        String mode = gyroSensor.getMode();
        if ( isGyroResetMode(mode) ) {
            generateResetGyroSensor(port);
        } else {
            generateReadSensorInMode(port, getGyroSensorReadModeConstant(mode));
        }
        return null;
    }

    private boolean isGyroResetMode(String mode) {
        return mode.equals(SC.RESET);
    }

    private String getGyroSensorReadModeConstant(String mode) {
        if ( mode.equals(SC.ANGLE) ) {
            return "GYRO_ANG";
        } else {
            return "GYRO_RATE";
        }
    }

    private void generateResetGyroSensor(String port) {
        this.sb.append("ResetGyroSensor(" + getPrefixedInputPort(port) + ");");
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        if ( isColorSensorRGBMode(colorSensor.getMode()) ) {
            generateReadColorSensorRGB(colorSensor.getPort());
        } else {
            generateReadSensorInMode(colorSensor.getPort(), getColorSensorReadModeConstant(colorSensor.getMode()));
        }
        return null;
    }

    private boolean isColorSensorRGBMode(String mode){
        return mode.equals(SC.RGB);
    }

    private void generateReadColorSensorRGB(String port) {
        this.sb.append("ReadColorSensorRGB(" +  getPrefixedInputPort(port) + ")");
    }

    private String getColorSensorReadModeConstant(String mode) {
        switch ( mode ) {
            case SC.COLOUR:
                return "COL_COLOR";
            case SC.LIGHT:
                return "COL_REFLECT";
            case SC.AMBIENTLIGHT:
                return "COL_AMBIENT";
            case SC.RGB:
                throw new IllegalArgumentException("Use the dedicated function defined in NEPODefs.h to read RGB values from color sensor");
            default:
                throw new DbcException("Unknown color sensor mode");
        }
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        String port = infraredSensor.getPort();
        switch ( infraredSensor.getMode() ) {
            case SC.DISTANCE:
                generateEV3IRDistance(port);
                break;
            case SC.PRESENCE:
                generateEV3IRSeeker(port);
                break;
            default:
                throw new DbcException("Unknown Infrared sensor mode");
        }
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        String functionName = getFunctionNameForCompassMode(compassSensor.getMode());
        this.sb.append(functionName + "(" + getPrefixedInputPort(compassSensor.getPort()) + ")");
        return null;
    }

    private String getFunctionNameForCompassMode(String mode) {
        switch ( mode ) {
            case SC.COMPASS:
                return "ReadCompass";
            case SC.ANGLE:
                return "ReadCompass";
            default:
                throw new DbcException("Unknown compass sensor mode");
        }
    }

    private void generateEV3IRDistance (String port) {
        generateReadSensorInMode(port, "IR_PROX");
    }

    private void generateEV3IRSeeker (String port) {
        this.sb.append("_ReadIRSeekAllChannels("+ getPrefixedInputPort(port) + ")");
    }

    @Override
    public Void visitIRSeekerSensor(IRSeekerSensor<Void> irSeekerSensor) {
        generateReadSensorInMode(irSeekerSensor.getPort(), getIRSeekerSensorModeConstant(irSeekerSensor.getMode()));
        return null;
    }

    private String getIRSeekerSensorModeConstant (String mode) {
        switch ( mode ) {
            case SC.MODULATED:
                return "";
            case SC.UNMODULATED:
                return "";
            default:
                throw new DbcException("Unknown IR seeker sensor mode");
        }
    }

    private void generateReadSensorInMode(String port, String mode) {
        this.sb.append("ReadSensorInMode(" + getPrefixedInputPort(port) + ", " + mode + ")");
    }

    private void generateReadSensor(String port) {
        this.sb.append("readSensor(" + getPrefixedInputPort(port) + ")");
    }

    @Override
    public Void visitKeysSensor(KeysSensor<Void> keysSensor) {
        this.sb.append("ButtonIsDown(" + getKeyConstant(keysSensor.getPort()) + ")");
        return null;
    }

    private String getKeyConstant (String keyPort) {
        switch ( keyPort ) {
            case SC.ENTER:
                return "BTNCENTER";
            case SC.RIGHT:
                return "BTNRIGHT";
            case SC.LEFT:
                return "BTNLEFT";
            case SC.UP:
                return "BTNUP";
            case SC.DOWN:
                return "BTNDOWN";
            case SC.ESCAPE:
                return "BTNEXIT";
            default:
                throw new DbcException("Unknown key port");
        }
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        this.sb.append("LcdClean();");
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
        this.sb.append(this.getLcdFunctionNameForMsg(showTextAction.getMsg()) + "(");
        showTextAction.getMsg().visit(this);
        this.sb.append(", ");
        showTextAction.getX().visit(this);
        this.sb.append(", ");
        showTextAction.getY().visit(this);
        this.sb.append(");");
        return null;
    }

    private String getLcdFunctionNameForMsg(Expr<Void> msg) {
        switch ( msg.getVarType() ) {
            case STRING:
            case ARRAY_STRING:
                return "LcdTextString";
            case BOOLEAN:
            case ARRAY_BOOLEAN:
                return "LcdTextBool";
            case COLOR:
            case ARRAY_COLOUR:
                return "LcdTextColor";
            case NUMBER:
            case ARRAY_NUMBER:
                return "LcdTextNum";
            case NOTHING:
                return getLcdFunctionNameForMsgOfTypeNothing(msg);
            case CAPTURED_TYPE:
                return getLcdFunctionNameForMsgOfTypeCapturedType(msg);
            default:
                return "LcdTextNum";
        }
    }

    private String getLcdFunctionNameForMsgOfTypeNothing(Expr<Void> msg) {
        String blockType = msg.getProperty().getBlockType().toString();
        if ( msg.toString().contains("LIGHT") ) {
            return "LcdTextNum"; // FIXME
        }

        if ( blockType.contains("isPressed") || blockType.contains("logic_ternary") ) {
            return "LcdTextBool";
        } else if ( blockType.contains("colour") ) {
            return "LcdTextColor";
        } else if ( blockType.contains("robSensors") || blockType.contains("robActions") || msg.toString().contains("POWER") ) {
            return "LcdTextNum";
        } else {
            return "LcdTextString";
        }
    }

    private String getLcdFunctionNameForMsgOfTypeCapturedType(Expr<Void> msg) {
        String msgString = msg.toString();
        if ( msgString.contains("Number")
            || msgString.contains("ADD")
            || msgString.contains("MINUS")
            || msgString.contains("MULTIPLY")
            || msgString.contains("DIVIDE")
            || msgString.contains("MOD")
            || msgString.contains("NEG")
            || msgString.contains("LISTS_LENGTH")
            || msgString.contains("IndexOfFunct")
            || msgString.contains("[ListGetIndex [GET, FROM_START, [ListCreate [NUMBER")
            || msgString.contains("[ListGetIndex [GET, FROM_START, [ListCreate [CONNECTION")
            || msgString.contains("MotorGetPower")
            || msgString.contains("VolumeAction") ) {
            return "LcdTextNum";
        } else if ( msgString.contains("EQ")
            || msgString.contains("NEQ")
            || msgString.contains("LT")
            || msgString.contains("LTE")
            || msgString.contains("GT")
            || msgString.contains("GTE")
            || msgString.contains("LIST_IS_EMPTY")
            || msgString.contains("AND")
            || msgString.contains("OR")
            || msgString.contains("NOT")
            || msgString.contains("[ListGetIndex [GET, FROM_START, [ListCreate [BOOLEAN")
            || msgString.contains("BluetoothConnectAction") ) {
            return "LcdTextBool";
        } else {
            return "LcdTextString";
        }
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        String soundConstant = getSoundConstantByFileName(playFileAction.getFileName());
        this.sb.append("_PlaySound(" + soundConstant + ");");
        return null;
    }

    private String getSoundConstantByFileName (String fileName) {
        switch ( fileName ) {
            case "0":
                return "SOUND_CLICK";
            case "1":
                return "SOUND_DOUBLE_BEEP";
            case "2":
                return "SOUND_DOWN";
            case "3":
                return "SOUND_UP";
            case "4":
                return "SOUND_LOW_BEEP";
            default:
                throw new DbcException("Unknown system sound file");
        }
    }

    @Override
    public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        this.sb.append("SetVolume(");
        volumeAction.getVolume().visit(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitShowPictureAction(ShowPictureAction<Void> showPictureAction) {
        String picture = showPictureAction.getPicture().toString();
        this.sb.append("LcdBmpFile(TEXT_COLOR_BLACK, 0, 0, " + picture + ");");
        // TODO: Implement
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        String pattern = getLedPattern(lightAction.getColor(), lightAction.getMode());
        this.sb.append("SetLedPattern(" + pattern + ");");
        return null;
    }

    private String getLedPattern(IBrickLedColor color, ILightMode mode) {
        return "LED_" + getLedPatternColorPrefix(color) + getLedPatternModePostfix(mode);
    }

    private String getLedPatternColorPrefix(IBrickLedColor color) {
        return color.toString();
    }

    private String getLedPatternModePostfix(ILightMode mode) {
        switch ( mode.toString() ) {
            case SC.ON:
                return "";
            case "FLASH":
                return "_FLASH";
            case "DOUBLE_FLASH":
                return "_PULSE";
            default:
                throw new DbcException("Unknown LightMode");
        }
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        switch ( lightStatusAction.getStatus() ) {
            case OFF:
                this.sb.append("SetLedPattern(LED_BLACK);");
                break;
            case RESET:
                // TODO: Implement
                break;
            default:
                throw new DbcException("Invalid LED status mode!");
        }
        return null;
    }

    @Override
    public Void visitBluetoothReceiveAction(BluetoothReceiveAction<Void> bluetoothReceiveAction) {
        return null;
    }

    @Override
    public Void visitBluetoothConnectAction(BluetoothConnectAction<Void> bluetoothConnectAction) {
        return null;
    }

    @Override
    public Void visitBluetoothSendAction(BluetoothSendAction<Void> bluetoothSendAction) {
        return null;
    }

    @Override
    public Void visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction<Void> bluetoothWaitForConnection) {
        return null;
    }

    @Override
    public Void visitBluetoothCheckConnectAction(BluetoothCheckConnectAction<Void> bluetoothCheckConnectAction) {
        return null;
    }

    @Override
    public Void visitSetLanguageAction(SetLanguageAction<Void> setLanguageAction) {
        return null;
    }

    @Override
    public Void visitSayTextAction(SayTextAction<Void> sayTextAction) {
        return null;
    }

}
