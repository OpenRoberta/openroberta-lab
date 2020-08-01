package de.fhg.iais.roberta.visitor.codegen;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.Lists;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.inter.mode.action.IBrickLedColor;
import de.fhg.iais.roberta.inter.mode.action.IDriveDirection;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.inter.mode.action.ILightMode;
import de.fhg.iais.roberta.inter.mode.action.IMotorMoveMode;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.MotorMoveMode;
import de.fhg.iais.roberta.mode.action.MotorStopMode;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.general.ListElementOperations;
import de.fhg.iais.roberta.syntax.BlockType;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothCheckConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothWaitForConnectionAction;
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
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.action.speech.SayTextAction;
import de.fhg.iais.roberta.syntax.action.speech.SetLanguageAction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.Binary;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ExprList;
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.expr.MathConst;
import de.fhg.iais.roberta.syntax.lang.expr.StringConst;
import de.fhg.iais.roberta.syntax.lang.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastStringFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextJoinFunct;
import de.fhg.iais.roberta.syntax.lang.stmt.DebugAction;
import de.fhg.iais.roberta.syntax.lang.stmt.RepeatStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HTColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.IRSeekerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.codegen.utilities.TTSLanguageMapper;
import de.fhg.iais.roberta.visitor.hardware.IEv3Visitor;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractCppVisitor;

public class Ev3C4ev3Visitor extends AbstractCppVisitor implements IEv3Visitor<Void> {

    private static final List<String> EV3_SENSOR_PORTS = Lists.newArrayList("1", "2", "3", "4");

    private static final String PREFIX_OUTPUT_PORT = "OUT_";
    private static final String PREFIX_IN_PORT = "IN_";

    private final ConfigurationAst brickConfiguration;
    private final String programName;
    private final ILanguage language;

    /**
     * initialize the EV3 c4ev3 code generator visitor.
     *
     * @param programPhrases
     */
    public Ev3C4ev3Visitor(
        List<List<Phrase<Void>>> programPhrases,
        ConfigurationAst brickConfiguration,
        String programName,
        ILanguage language,
        ClassToInstanceMap<IProjectBean> beans) {
        super(programPhrases, beans);
        this.brickConfiguration = brickConfiguration;
        this.programName = programName;
        this.language = language;
    }

    private static String getPrefixedOutputPort(String port) {
        return PREFIX_OUTPUT_PORT + port;
    }

    private static String getPrefixedInputPort(String port) {
        return PREFIX_IN_PORT + port;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }

        generateConstants();
        generateImports();
        nlIndent();

        super.generateProgramPrefix(withWrapping);
    }

    private void generateConstants() {
        this.sb.append("#define PROGRAM_NAME \"" + this.programName + "\"\n");
        this.sb.append("#define WHEEL_DIAMETER " + this.brickConfiguration.getWheelDiameter() + "\n");
        this.sb.append("#define TRACK_WIDTH " + this.brickConfiguration.getTrackWidth() + "\n");
        decrIndentation();
    }

    private void generateImports() {
        nlIndent();
        this.sb.append("#include <ev3.h>");
        nlIndent();
        this.sb.append("#include <math.h>");
        nlIndent();
        this.sb.append("#include <list>");
        nlIndent();
        this.sb.append("#include \"NEPODefs.h\"");
        nlIndent();
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        mainTask.getVariables().accept(this);
        nlIndent();
        generateUserDefinedMethods();
        nlIndent();
        this.sb.append("int main () {");
        incrIndentation();
        nlIndent();
        this.sb.append("NEPOInitEV3();");
        nlIndent();
        generateSensorInitialization();
        generateTTSInitialization();
        generateGyroInitialization();
        generateDebugInitialization(mainTask);
        nlIndent();
        return null;
    }

    private void generateSensorInitialization() {
        this.sb.append("NEPOSetAllSensors(").append(getSensorsInitializationArguments()).append(");");
        nlIndent();
    }

    private String getSensorsInitializationArguments() {
        Map<String, ConfigurationComponent> usedSensorMap = new HashMap<>(4);
        for ( UsedSensor usedSensor : this.getBean(UsedHardwareBean.class).getUsedSensors() ) {
            String port = usedSensor.getPort();
            usedSensorMap.put(port, this.brickConfiguration.optConfigurationComponent(port));
        }

        return EV3_SENSOR_PORTS.stream().map(usedSensorMap::get).map(this::getSensorFromConfigurationComponent).collect(Collectors.joining(", "));
    }

    private String getSensorFromConfigurationComponent(ConfigurationComponent component) {
        if ( component == null ) {
            return "NULL";
        }
        switch ( component.getComponentType() ) {
            case SC.TOUCH:
                return "EV3Touch";
            case SC.COLOR:
                return "EV3Color";
            case SC.GYRO:
                return "EV3Gyro";
            case SC.ULTRASONIC:
                return "EV3Ultrasonic";
            case SC.INFRARED:
                return "EV3Ir";
            case SC.SOUND:
                return "NXTSound";
            case SC.COMPASS:
                return "HTCompass";
            case SC.IRSEEKER:
                return "HTIr";
            case SC.HT_COLOR:
                return "HTColorV2";
            default:
                return "NULL";
        }
    }

    private void generateDebugInitialization(MainTask<Void> mainTask) {
        boolean isDebug = mainTask.getDebug().equals("TRUE");
        if ( isDebug ) {
            this.sb.append("startLoggingThread(");
            this.sb.append(getConnectedMotorPorts());
            this.sb.append(");");
            nlIndent();
        }
    }

    private String getConnectedMotorPorts() {
        String ports = this.brickConfiguration.getActors().stream().map(ConfigurationComponent::getUserDefinedPortName).collect(Collectors.joining());
        if ( ports.length() == 0 ) {
            return "0"; // TODO: Create unit test case
        }
        return getMotorPortConstant(ports);
    }

    private void generateTTSInitialization() {
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.VOICE) ) {
            this.sb.append("SetLanguage(\"" + TTSLanguageMapper.getLanguageString(this.language) + "\");");
        }
    }

    private void generateGyroInitialization() {
        for ( UsedSensor usedSensor : this.getBean(UsedHardwareBean.class).getUsedSensors() ) {
            if ( usedSensor.getType().equals(SC.GYRO) ) {
                this.generateResetGyroSensor(usedSensor.getPort());
                this.nlIndent();
            }
        }
    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        nlIndent();
        nlIndent();
        this.sb.append("NEPOFreeEV3();");
        nlIndent();
        this.sb.append("return 0;");
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        nlIndent();

        super.generateProgramSuffix(withWrapping);
    }

    @Override
    protected String getLanguageVarTypeFromBlocklyType(BlocklyType type) {
        switch ( type ) {
            case STRING:
                return "std::string";
            case COLOR:
                return "Color";
            case ARRAY_STRING:
                return "std::list<std::string>";
            case ARRAY_COLOUR:
                return "std::list<Color>";
            case CONNECTION:
                return "BluetoothConnectionHandle";
            case ARRAY_CONNECTION:
                return "std::list<BluetoothConnectionHandle>";
            default:
                return super.getLanguageVarTypeFromBlocklyType(type);
        }
    }

    @Override
    public Void visitMathConst(MathConst<Void> mathConst) {
        if ( mathConst.getMathConst() == MathConst.Const.INFINITY ) {
            this.sb.append("HUGE_VAL");
            return null;
        } else {
            return super.visitMathConst(mathConst);
        }
    }

    @Override
    public Void visitColorConst(ColorConst<Void> colorConst) {
        String colorConstant = getColorConstantByHex(colorConst.getHexValueAsString());
        this.sb.append(colorConstant);
        return null;
    }

    private String getColorConstantByHex(String hex) {
        switch ( hex.toUpperCase() ) {
            case "#000000":
                return "Black";
            case "#0057A6":
                return "Blue";
            case "#00642E":
                return "Green";
            case "#F7D117":
                return "Yellow";
            case "#B30006":
                return "Red";
            case "#FFFFFF":
                return "White";
            case "#532115":
                return "Brown";
            case "#EE82EE":
                return "Violet";
            case "#800080":
                return "Purple";
            case "#00FF00":
                return "Lime";
            case "#FFA500":
                return "Orange";
            case "#FF00FF":
                return "Magenta";
            case "#DC143C":
                return "Crismon";
            case "#585858":
                return "None";
            default:
                throw new DbcException("Invalid color constant: " + hex);
        }
    }

    // copied from AbstractCommonArduinoCppVisitor
    @Override
    public Void visitBinary(Binary<Void> binary) {
        Binary.Op op = binary.getOp();
        if ( op == Binary.Op.MOD ) {
            appendFloatModulo(binary);
            return null;
        }
        generateSubExpr(this.sb, false, binary.getLeft(), binary);
        String sym = getBinaryOperatorSymbol(op);
        this.sb.append(whitespace() + sym + whitespace());
        if ( op == Binary.Op.TEXT_APPEND ) {
            convertToString(binary);
            return null;
        } else if ( op == Binary.Op.DIVIDE ) {
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
        this.sb.append("((double) ");
        generateSubExpr(this.sb, parenthesesCheck(binary), binary.getRight(), binary);
        this.sb.append(")");
    }

    private void convertToString(Binary<Void> binary) {
        switch ( binary.getRight().getVarType() ) {
            case BOOLEAN:
            case NUMBER:
            case NUMBER_INT:
            case COLOR:
                this.sb.append("ToString(");
                generateSubExpr(this.sb, false, binary.getRight(), binary);
                this.sb.append(")");
                break;
            default:
                generateSubExpr(this.sb, false, binary.getRight(), binary);
                break;
        }
    }
    // end copied from AbstractCommonArduinoCppVisitor

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
        repeatStmt.getList().accept(this);
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

    private void generateCodeFromStmtConditionFor(Expr<Void> expr) {
        final ExprList<Void> expressions = (ExprList<Void>) expr;
        Expr<Void> counterName = expressions.get().get(0);
        Expr<Void> counterInitialValue = expressions.get().get(1);
        Expr<Void> counterTargetValue = expressions.get().get(2);
        Expr<Void> counterStep = expressions.get().get(3);
        this.sb.append("for (" + "float ");
        counterName.accept(this);
        this.sb.append(" = ");
        counterInitialValue.accept(this);
        this.sb.append("; ");
        counterName.accept(this);
        this.sb.append(" < ");
        counterTargetValue.accept(this);
        this.sb.append("; ");
        counterName.accept(this);
        this.sb.append(" += ");
        counterStep.accept(this);
        this.sb.append(") {");
    }

    private void generateForEachPrefix(Expr<Void> expression) {
        ((Binary<Void>) expression).getLeft().accept(this);
        this.sb.append(";");
        nlIndent();
        this.sb.append("for(int i = 0; i < ");
        ((Binary<Void>) expression).getRight().accept(this);
        this.sb.append(".size(); ++i) {");
        incrIndentation();
        nlIndent();
        ((Binary<Void>) expression).getLeft().accept(this);
        this.sb.append(" = _getListElementByIndex(");
        ((Binary<Void>) expression).getRight().accept(this);
        this.sb.append(", i);");
        decrIndentation();
    }

    @Override
    public Void visitDebugAction(DebugAction<Void> debugAction) {
        this.sb.append("printf(\"%s\\n\", ToString(");
        debugAction.getValue().accept(this);
        this.sb.append(").c_str());");
        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        this.sb.append("while ( true ) {");
        incrIndentation();
        visitStmtList(waitStmt.getStatements());
        //nlIndent();
        //this.sb.append("Wait(15);");
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        this.sb.append("Wait(");
        waitTimeStmt.getTime().accept(this);
        this.sb.append(");");
        return null;
    }

    // copied from Arduino
    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        if ( indexOfFunct.getParam().get(0).toString().contains("ListCreate ") ) {
            this.sb.append("null");
            return null;
        }
        String methodName = indexOfFunct.getLocation() == IndexLocation.LAST ? "_getLastOccuranceOfElement(" : "_getFirstOccuranceOfElement(";
        this.sb.append(methodName);
        indexOfFunct.getParam().get(0).accept(this);
        this.sb.append(", ");
        if ( indexOfFunct.getParam().get(1).getClass().equals(StringConst.class) ) {
            this.sb.append("ToString(");
            indexOfFunct.getParam().get(1).accept(this);
            this.sb.append(")");
        } else {
            indexOfFunct.getParam().get(1).accept(this);
        }
        this.sb.append(")");
        return null;
    }

    // end copied from Arduino

    @Override
    public Void visitListCreate(ListCreate<Void> listCreate) {
        this.sb.append("(" + getListCreateCasting(listCreate.getVarType()));
        super.visitListCreate(listCreate);
        this.sb.append(")");
        return null;
    }

    private String getListCreateCasting(BlocklyType type) {
        switch ( type ) {
            case NUMBER:
                return "(std::list<double>)";
            case STRING:
                return "(std::list<std::string>)";
            case BOOLEAN:
                return "(std::list<bool>)";
            case COLOR:
                return "(std::list<Color>)";
            case CONNECTION:
                return "(std::list<BluetoothConnectionHandle>)";
            default:
                throw new DbcException("unknown BlocklyType for ListCreate statement");
        }
    }

    // copied from calliope
    /*
     * TODO: I don't know why I am doing this, but it seems that without this a semicolon is lost, somehow... Artem Vinokurov 25.10.2018
     */
    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        super.visitListSetIndex(listSetIndex);
        this.sb.append(";");
        return null;
    }

    /*
     * TODO: There is something wrong with semicolon generation for calliope. Artem Vinokurov 25.10.2018
     */
    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        super.visitListGetIndex(listGetIndex);
        if ( ((ListElementOperations) listGetIndex.getElementOperation()).equals(ListElementOperations.REMOVE) ) {
            this.sb.append(";");
        }
        return null;
    }

    // end copied from calliope

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        Expr<Void> min = mathRandomIntFunct.getParam().get(0);
        Expr<Void> max = mathRandomIntFunct.getParam().get(1);
        this.sb.append("((rand() % (int) (");
        min.accept(this);
        this.sb.append(" - ");
        max.accept(this);
        this.sb.append(")) + ");
        min.accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathCastStringFunct(MathCastStringFunct<Void> mathCastStringFunct) {
        // TODO check why this is not working for Arduinos!
        this.sb.append("(ToString(");
        mathCastStringFunct.getParam().get(0).accept(this);
        this.sb.append("))");
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct<Void> textJoinFunct) {
        List<Expr<Void>> texts = textJoinFunct.getParam().get();
        for ( int i = 0; i < texts.size(); i++ ) {
            this.sb.append("ToString(");
            texts.get(i).accept(this);
            this.sb.append(")");
            if ( i < texts.size() - 1 ) {
                this.sb.append(" + ");
            }
        }
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        String port = motorOnAction.getUserDefinedPort();
        MotorDuration<Void> duration = motorOnAction.getParam().getDuration();
        Expr<Void> speedExpression = motorOnAction.getParam().getSpeed();
        if ( isActorOnPort(port) ) {
            boolean isRegulated = this.brickConfiguration.isMotorRegulated(port);
            if ( duration != null ) {
                generateRotateMotorForDuration(port, speedExpression, motorOnAction.getDurationMode(), duration.getValue());
            } else {
                generateTurnOnMotor(port, speedExpression, isRegulated);
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
        durationExpression.accept(this);
        this.sb.append(");");
    }

    private void generateTurnOnMotor(String port, Expr<Void> speedExpression, boolean isRegulated) {
        String functionName = isRegulated ? "OnFwdReg" : "OnFwdEx";
        this.sb.append(functionName + "(" + getPrefixedOutputPort(port) + ", ");
        visitSpeedExpression(speedExpression);
        if ( !isRegulated ) {
            this.sb.append(", RESET_NONE");
        }
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
        this.sb.append("RotateMotorForAngle(" + getDriveMotorPortsConstant() + ", ");
        visitSpeedExpression(speedExpression, reverse);
        this.sb.append(", ");
        visitDistanceOfDrive(distanceExpression);
        this.sb.append(");");
    }

    private void visitDistanceOfDrive(Expr<Void> distanceExpression) {
        this.sb.append("(");
        distanceExpression.accept(this);
        this.sb.append(" * 360) / (M_PI * WHEEL_DIAMETER)");
    }

    private void generateDrive(Expr<Void> speedExpression, boolean reverse) {
        String methodName = reverse ? "OnRevSync" : "OnFwdSync";
        this.sb.append(methodName + "(" + getDriveMotorPortsConstant() + ", ");
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
        this.sb.append("Off(" + getDriveMotorPortsConstant() + ");");
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
            duration.getValue().accept(this);
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

    /**
     * Returns whether the port order of the left and right motors needs to be reversed.
     * This happens in {@link #getDriveMotorPortsConstant()} as the c4ev3 API only accepts the ports via ordered constants.
     * This method is then used to check whether the order of the left and right (e.g C B - > B C) is reversed.
     *
     * @return whether the order of the left and right motors is reversed
     */
    private boolean isMotorLeftRightOrderReversed() {
        Pair<String, String> driveMotorPorts = getDriveMotorPorts();
        char[] charArray = (driveMotorPorts.getFirst() + driveMotorPorts.getSecond()).toCharArray();
        char[] sortedCharArray = Arrays.copyOf(charArray, charArray.length);
        Arrays.sort(sortedCharArray);
        return !Arrays.equals(charArray, sortedCharArray);
    }

    private int getTurn(TurnAction<Void> turnAction) {
        /**
         * Turn is from -200 to 200
         * O: motor run at the same power
         * 100: one motor run at the specified power the other doesn't
         * 200: one motor run at the specified power and the other at negative power
         */
        int turn = 200;
        if ( isAnyDriveMotorReverse() ) {
            turn *= -1;
        }
        if ( !isMotorLeftRightOrderReversed() ) {
            turn *= -1;
        }
        if ( turnAction.getDirection() == TurnDirection.RIGHT ) {
            turn *= -1;
        }
        return turn;
    }

    private void generateTurnForDistance(Expr<Void> speedExpression, Expr<Void> distanceExpression, int turn) {
        this.sb.append("RotateMotorForAngleWithTurn(" + getDriveMotorPortsConstant() + ", ");
        visitSpeedExpression(speedExpression);
        this.sb.append(", ");
        visitDistanceOfTurn(distanceExpression);
        this.sb.append(", " + turn + ");");
    }

    private void visitDistanceOfTurn(Expr<Void> distanceExpression) {
        this.sb.append("(");
        distanceExpression.accept(this);
        this.sb.append(" * TRACK_WIDTH / WHEEL_DIAMETER)");
    }

    private void generateTurn(Expr<Void> speedExpression, int turn) {
        this.sb.append("OnFwdSyncEx(" + getDriveMotorPortsConstant() + ", ");
        visitSpeedExpression(speedExpression);
        this.sb.append(", " + turn + ", RESET_NONE);");

    }

    private void visitSpeedExpression(Expr<Void> speedExpression) {
        visitSpeedExpression(speedExpression, false);
    }

    private void visitSpeedExpression(Expr<Void> speedExpression, boolean reverse) {
        this.sb.append(reverse ? "-Speed(" : "Speed(");
        speedExpression.accept(this);
        this.sb.append(")");
    }

    private boolean isActorOnPort(String port) {
        if ( port != null ) {
            for ( UsedActor actor : this.getBean(UsedHardwareBean.class).getUsedActors() ) {
                if ( actor.getPort().equals(port) ) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isMotorReverse(String port) {
        String reverseProperty = this.brickConfiguration.getConfigurationComponent(port).getOptProperty(SC.MOTOR_REVERSE);
        return reverseProperty != null && reverseProperty.equals(SC.ON);
    }

    private Pair<String, String> getDriveMotorPorts() {
        ConfigurationComponent leftMotor = this.brickConfiguration.getFirstMotor(SC.LEFT);
        ConfigurationComponent rightMotor = this.brickConfiguration.getFirstMotor(SC.RIGHT);
        String leftMotorPort = leftMotor.getUserDefinedPortName();
        String rightMotorPort = rightMotor.getUserDefinedPortName();
        return Pair.of(leftMotorPort, rightMotorPort);
    }

    private String getDriveMotorPortsConstant() {
        Pair<String, String> driveMotorPorts = getDriveMotorPorts();
        return getMotorPortConstant(driveMotorPorts.getFirst() + driveMotorPorts.getSecond());
    }

    private static String getMotorPortConstant(String ports) {
        char[] charArray = ports.toCharArray();
        Arrays.sort(charArray);
        return PREFIX_OUTPUT_PORT + new String(charArray);
    }

    private String getLeftDriveMotorPort() {
        return getPrefixedOutputPort(this.brickConfiguration.getFirstMotor(SC.LEFT).getUserDefinedPortName());
    }

    private String getRightDriveMotorPort() {
        return getPrefixedOutputPort(this.brickConfiguration.getFirstMotor(SC.RIGHT).getUserDefinedPortName());
    }

    private boolean isReverseGivenBrickConfigurationAndAction(IDriveDirection direction) {
        boolean reverse = isAnyDriveMotorReverse();
        boolean localReverse = direction == DriveDirection.BACKWARD;
        return reverse && !localReverse || localReverse && !reverse;
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
            case SC.RESET:
                generateResetEncoder(port);
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

    private void generateResetEncoder(String port) {
        this.sb.append("ResetRotationCount(" + getPrefixedOutputPort(port) + ");");
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        String timerNumber = timerSensor.getPort();
        switch ( timerSensor.getMode() ) {
            case SC.DEFAULT:
            case SC.VALUE:
                generateGetTimer(timerNumber);
                break;
            case SC.RESET:
                generateResetTimer(timerNumber);
                break;
            default:
                throw new DbcException("Unknown timer mode");
        }
        return null;
    }

    private void generateGetTimer(String timerNumber) {
        this.sb.append("GetTimerValue(" + timerNumber + ")");
    }

    private void generateResetTimer(String timerNumber) {
        this.sb.append("ResetTimer(" + timerNumber + ");");
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        this.sb.append("ReadEV3TouchSensor(").append(getPrefixedInputPort(touchSensor.getPort())).append(")");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        String port = ultrasonicSensor.getPort();
        if ( ultrasonicSensor.getMode().equals(SC.DISTANCE) ) {
            generateRealUltrasonicDistance(port);
        } else {
            generateRealUltrasonicPresence(port);
        }
        return null;
    }

    private void generateRealUltrasonicDistance(String port) {
        this.sb.append("ReadEV3UltrasonicSensorDistance(").append(getPrefixedInputPort(port)).append(", CM)");
    }

    private void generateRealUltrasonicPresence(String port) {
        this.sb.append("ReadEV3UltrasonicSensorListen(").append(getPrefixedInputPort(port)).append(")");
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
        this.sb.append("ReadNXTSoundSensor(").append(getPrefixedInputPort(soundSensor.getPort())).append(", DB)");
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        String port = gyroSensor.getPort();
        String mode = gyroSensor.getMode();
        if ( mode.equals(SC.RESET) ) {
            generateResetGyroSensor(port);
        } else {
            generateReadGyro(port, mode);
        }
        return null;
    }

    private void generateResetGyroSensor(String port) {
        this.sb.append("NEPOResetEV3GyroSensor(" + getPrefixedInputPort(port) + ");");
    }

    private void generateReadGyro(String port, String mode) {
        this.sb.append("ReadEV3GyroSensor(").append(getPrefixedInputPort(port)).append(", ").append(getGyroSensorReadModeConstant(mode)).append(")");
    }

    private String getGyroSensorReadModeConstant(String mode) {
        if ( mode.equals(SC.ANGLE) ) {
            return "EV3GyroAngle";
        } else {
            return "EV3GyroRate";
        }
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        String mode = colorSensor.getMode();
        String port = getPrefixedInputPort(colorSensor.getPort());
        switch ( mode ) {
            case SC.COLOUR:
                this.sb.append("ReadEV3ColorSensor(").append(port).append(")");
                break;
            case SC.LIGHT:
                this.sb.append("ReadEV3ColorSensorLight(").append(port).append(", ReflectedLight)");
                break;
            case SC.AMBIENTLIGHT:
                this.sb.append("ReadEV3ColorSensorLight(").append(port).append(", AmbientLight)");
                break;
            case SC.RGB:
                this.sb.append("NEPOReadEV3ColorSensorRGB(").append(port).append(")");
                break;
        }
        return null;
    }

    @Override
    public Void visitHTColorSensor(HTColorSensor<Void> htColorSensor) {
        String mode = htColorSensor.getMode();
        String port = getPrefixedInputPort(htColorSensor.getPort());
        String functionName;
        switch ( mode ) {
            case SC.COLOUR:
                functionName = "NEPOReadHTColorSensorV2";
                break;
            case SC.LIGHT:
                functionName = "NEPOReadHTColorSensorV2Light";
                break;
            case SC.AMBIENTLIGHT:
                functionName = "NEPOReadHTColorSensorV2AmbientLight";
                break;
            case SC.RGB:
                functionName = "NEPOReadHTColorSensorV2RGB";
                break;
            default:
                throw new DbcException("Invalid mode for HT Color Sensor V2!");
        }
        this.sb.append(functionName).append("(").append(port).append(")");
        return null;
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

    private void generateEV3IRDistance(String port) {
        this.sb.append("ReadEV3IrSensorProximity(").append(getPrefixedInputPort(port)).append(")");
    }

    private void generateEV3IRSeeker(String port) {
        this.sb.append("_ReadIRSeekAllChannels(" + getPrefixedInputPort(port) + ")");
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        if ( compassSensor.getMode().equals(SC.CALIBRATE) ) {
            visitCalibrateCompass(compassSensor);
        } else {
            visitReadCompass(compassSensor);
        }

        return null;
    }

    private void visitCalibrateCompass(CompassSensor<Void> compassSensor) {
        String port = getPrefixedInputPort(compassSensor.getPort());
        nlIndent();
        this.sb.append("StartHTCompassCalibration(" + port + ");");
        this.sb.append("Wait(40000);");
        this.sb.append("StopHTCompassCalibration(" + port + ");");
        nlIndent();
    }

    private void visitReadCompass(CompassSensor<Void> compassSensor) {
        String mode = getCompassSensorReadModeConstant(compassSensor.getMode());
        this.sb.append("ReadHTCompassSensor(" + getPrefixedInputPort(compassSensor.getPort()) + ", " + mode + ")");
    }

    private String getCompassSensorReadModeConstant(String mode) {
        switch ( mode ) {
            case SC.COMPASS:
                return "HTCompassCompass";
            case SC.ANGLE:
                return "HTCompassAngle";
            default:
                throw new DbcException("Unknown read compass mode");
        }
    }

    @Override
    public Void visitIRSeekerSensor(IRSeekerSensor<Void> irSeekerSensor) {
        this.sb
            .append("ReadHTIrSensor(")
            .append(getPrefixedInputPort(irSeekerSensor.getPort()))
            .append(", ")
            .append(getIRSeekerSensorConstantMode(irSeekerSensor.getMode()))
            .append(")");
        return null;
    }

    private String getIRSeekerSensorConstantMode(String mode) {
        switch ( mode ) {
            case SC.MODULATED:
                return "Modulated";
            case SC.UNMODULATED:
                return "Unmodulated";
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

    private String getKeyConstant(String keyPort) {
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
            case SC.ANY:
                return "BTNANY";
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
        this.sb.append("DrawString(ToString(");
        showTextAction.getMsg().accept(this);
        this.sb.append("), ");
        showTextAction.getX().accept(this);
        this.sb.append(", ");
        showTextAction.getY().accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        String soundConstant = getSoundConstantByFileName(playFileAction.getFileName());
        this.sb.append("PlaySystemSound(" + soundConstant + ");");
        return null;
    }

    private String getSoundConstantByFileName(String fileName) {
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
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        this.sb.append("NEPOPlayTone(" + playNoteAction.getFrequency() + ", " + playNoteAction.getDuration() + ");");
        return null;
    }

    @Override
    public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        switch ( volumeAction.getMode() ) {
            case SET:
                generateSetVolume(volumeAction);
                break;
            case GET:
                generateGetVolume();
                break;
            default:
                throw new DbcException("Unknown volume action mode");
        }
        return null;
    }

    private void generateSetVolume(VolumeAction<Void> volumeAction) {
        this.sb.append("SetVolume(");
        volumeAction.getVolume().accept(this);
        this.sb.append(");");
    }

    private void generateGetVolume() {
        this.sb.append("GetVolume()");
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        this.sb.append("NEPOPlayTone(");
        toneAction.getFrequency().accept(this);
        this.sb.append(", ");
        toneAction.getDuration().accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitShowPictureAction(ShowPictureAction<Void> showPictureAction) {
        String picture = showPictureAction.getPicture().toString();
        this.sb.append("LcdPicture(LCD_COLOR_BLACK, 0, 0, " + picture + ");");
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
        this.sb.append("NEPOReceiveStringFrom(");
        bluetoothReceiveAction.getConnection().accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitBluetoothConnectAction(BluetoothConnectAction<Void> bluetoothConnectAction) {
        this.sb.append("NEPOConnectTo(");
        bluetoothConnectAction.getAddress().accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitBluetoothSendAction(BluetoothSendAction<Void> bluetoothSendAction) {
        this.sb.append("NEPOSendStringTo(");
        bluetoothSendAction.getConnection().accept(this);
        this.sb.append(", ");
        bluetoothSendAction.getMsg().accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction<Void> bluetoothWaitForConnection) {
        this.sb.append("NEPOWaitConnection()");
        return null;
    }

    @Override
    public Void visitBluetoothCheckConnectAction(BluetoothCheckConnectAction<Void> bluetoothCheckConnectAction) {
        return null;
    }

    @Override
    public Void visitSetLanguageAction(SetLanguageAction<Void> setLanguageAction) {
        this.sb.append("SetLanguage(\"");
        this.sb.append(TTSLanguageMapper.getLanguageString(setLanguageAction.getLanguage()));
        this.sb.append("\");");
        return null;
    }

    @Override
    public Void visitSayTextAction(SayTextAction<Void> sayTextAction) {
        this.sb.append("Say(ToString(");
        sayTextAction.getMsg().accept(this);
        this.sb.append("), ");
        this.generateSpeedAndPitchArgumentsOrDefault(sayTextAction);
        this.sb.append(");");
        return null;
    }

    private void generateSpeedAndPitchArgumentsOrDefault(SayTextAction<Void> sayTextAction) {
        Expr<Void> speed = sayTextAction.getSpeed();
        Expr<Void> pitch = sayTextAction.getPitch();
        if ( !isExprEmptyBlock(speed) && !isExprEmptyBlock(pitch) ) {
            generateSpeedAndPitchArguments(speed, pitch);
        } else {
            generateDefaultSpeedAndPitchArguments();
        }
    }

    private boolean isExprEmptyBlock(Expr<Void> expr) {
        BlockType emptyBlock = BlockTypeContainer.getByName("EMPTY_EXPR");
        return expr.getKind().equals(emptyBlock);
    }

    private void generateSpeedAndPitchArguments(Expr<Void> speed, Expr<Void> pitch) {
        speed.accept(this);
        this.sb.append(", ");
        pitch.accept(this);
    }

    private void generateDefaultSpeedAndPitchArguments() {
        this.sb.append("30, 50");
    }
}
