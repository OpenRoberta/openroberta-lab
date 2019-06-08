package de.fhg.iais.roberta.visitor.codegen;

import com.google.common.collect.Lists;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.inter.mode.action.IDriveDirection;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.inter.mode.action.IMotorMoveMode;
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
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.action.speech.SayTextAction;
import de.fhg.iais.roberta.syntax.action.speech.SetLanguageAction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.MathConst;
import de.fhg.iais.roberta.syntax.lang.functions.*;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
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
        this.sb.append("#include \"NEPODefs.h\"\n");
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        mainTask.getVariables().visit(this);
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
        this.sb.append("setAllSensorMode(").append(getDefaultSensorModesString()).append( ");");
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

    private String getCMathConstantName (MathConst.Const constant) {
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
    public Void visitBinary(Binary<Void> binary) {
        return null;
    }

    @Override
    public Void visitRepeatStmt(RepeatStmt<Void> repeatStmt) {
        return null;
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
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        this.sb.append("readSensor(" ).append(getPrefixedInputPort(touchSensor.getPort())).append(")");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        String port = ultrasonicSensor.getPort();
        this.sb.append("ReadSensorInMode(").append(getPrefixedInputPort(port)).append(", ");
        if ( ultrasonicSensor.getMode().equals(SC.DISTANCE) ) {
            this.sb.append("US_DIST_CM");
        } else {
            this.sb.append("US_LISTEN");
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
        String port = soundSensor.getPort();
        this.sb.append("readSensor(").append(getPrefixedInputPort(port) + ")");
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        this.sb.append("LcdClear();");
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
        //this.sb.append("LcdText(TEXT_COLOR_BLACK, " + showTextAction.getX() + ", " + showTextAction.getY() + ", ");
        return null;
    }

    @Override
    public Void visitShowPictureAction(ShowPictureAction<Void> showPictureAction) {
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
