package de.fhg.iais.roberta.visitor.codegen;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.Lists;

import de.fhg.iais.roberta.bean.CodeGeneratorSetupBean;
import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
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
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothCheckConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothWaitForConnectionAction;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.ev3.ShowPictureAction;
import de.fhg.iais.roberta.syntax.action.light.BrickLightOffAction;
import de.fhg.iais.roberta.syntax.action.light.BrickLightOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.syntax.action.sound.GetVolumeAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.SetVolumeAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.speech.SayTextAction;
import de.fhg.iais.roberta.syntax.action.speech.SayTextWithSpeedAndPitchAction;
import de.fhg.iais.roberta.syntax.action.speech.SetLanguageAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
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
import de.fhg.iais.roberta.syntax.sensor.generic.CompassCalibrate;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderReset;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroReset;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HTColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.IRSeekerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.basic.Pair;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.FunctionNames;
import de.fhg.iais.roberta.util.syntax.MotorDuration;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.IEv3Visitor;
import de.fhg.iais.roberta.visitor.codegen.utilities.TTSLanguageMapper;
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
        List<List<Phrase>> programPhrases,
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
        this.src.add("#define PROGRAM_NAME \"", this.programName, "\"\n");
        this.src.add("#define WHEEL_DIAMETER ", this.brickConfiguration.getWheelDiameter(), "\n");
        this.src.add("#define TRACK_WIDTH ", this.brickConfiguration.getTrackWidth(), "\n");
        decrIndentation();
    }

    private void generateImports() {
        this.src.nlI().add("#include <ev3.h>").nlI().add("#include <math.h>").nlI().add("#include <list>").nlI().add("#include \"NEPODefs.h\"").nlI();
    }

    @Override
    public Void visitMainTask(MainTask mainTask) {
        mainTask.variables.accept(this);
        nlIndent();
        generateUserDefinedMethods();
        nlIndent();
        this.src.add("int main () {");
        incrIndentation();
        nlIndent();
        this.src.add("NEPOInitEV3();");
        nlIndent();
        generateSensorInitialization();
        generateTTSInitialization();
        generateGyroInitialization();
        generateDebugInitialization(mainTask);
        generateRandomSeed();
        nlIndent();
        return null;
    }

    private void generateSensorInitialization() {
        this.src.add("NEPOSetAllSensors(", getSensorsInitializationArguments(), ");").nlI();
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
        switch ( component.componentType ) {
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

    private void generateDebugInitialization(MainTask mainTask) {
        boolean isDebug = mainTask.debug.equals("TRUE");
        if ( isDebug ) {
            this.src.add("startLoggingThread(");
            this.src.add(getConnectedMotorPorts());
            this.src.add(");");
            nlIndent();
        }
    }

    private String getConnectedMotorPorts() {
        String ports = this.brickConfiguration.getActors().stream().map(configurationComponent -> configurationComponent.userDefinedPortName).collect(Collectors.joining());
        if ( ports.length() == 0 ) {
            return "0"; // TODO: Create unit test case
        }
        return Ev3C4ev3Visitor.getMotorPortConstant(ports);
    }

    private void generateTTSInitialization() {
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.VOICE) ) {
            this.src.add("SetLanguage(\"", TTSLanguageMapper.getLanguageString(this.language), "\");");
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
        this.src.add("NEPOFreeEV3();");
        nlIndent();
        this.src.add("return 0;");
        decrIndentation();
        nlIndent();
        this.src.add("}");
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
    public Void visitMathConst(MathConst mathConst) {
        if ( mathConst.mathConst == MathConst.Const.INFINITY ) {
            this.src.add("HUGE_VAL");
            return null;
        } else {
            return super.visitMathConst(mathConst);
        }
    }

    @Override
    public Void visitColorConst(ColorConst colorConst) {
        String colorConstant = getColorConstantByHex(colorConst.getHexValueAsString());
        this.src.add(colorConstant);
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
    public Void visitBinary(Binary binary) {
        Binary.Op op = binary.op;
        if ( op == Binary.Op.MOD ) {
            appendFloatModulo(binary);
            return null;
        }
        generateSubExpr(this.src, false, binary.left, binary);
        String sym = getBinaryOperatorSymbol(op);
        this.src.add(" ", sym, " ");
        if ( op == Binary.Op.DIVIDE ) {
            appendCastToFloat(binary);
            return null;
        } else {
            generateSubExpr(this.src, parenthesesCheck(binary), binary.getRight(), binary);
        }
        return null;
    }

    private void appendFloatModulo(Binary binary) {
        this.src.add("fmod(");
        generateSubExpr(this.src, false, binary.left, binary);
        this.src.add(", ");
        generateSubExpr(this.src, parenthesesCheck(binary), binary.getRight(), binary);
        this.src.add(")");
    }

    private void appendCastToFloat(Binary binary) {
        this.src.add("((double) ");
        generateSubExpr(this.src, parenthesesCheck(binary), binary.getRight(), binary);
        this.src.add(")");
    }

    private void convertToString(Binary binary) {
        switch ( binary.getRight().getBlocklyType() ) {
            case BOOLEAN:
            case NUMBER:
            case NUMBER_INT:
            case COLOR:
                this.src.add("ToString(");
                generateSubExpr(this.src, false, binary.getRight(), binary);
                this.src.add(")");
                break;
            default:
                generateSubExpr(this.src, false, binary.getRight(), binary);
                break;
        }
    }
    // end copied from AbstractCommonArduinoCppVisitor

    @Override
    public Void visitRepeatStmt(RepeatStmt repeatStmt) {
        Expr condition = repeatStmt.expr;
        boolean isWaitStmt = repeatStmt.mode == RepeatStmt.Mode.WAIT;
        if ( !isWaitStmt ) {
            increaseLoopCounter();
        }
        switch ( repeatStmt.mode ) {
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
                break;
            case FOREVER_ARDU:
                throw new DbcException("FOREVER_ARDU is invalid with ev3");
        }
        incrIndentation();
        repeatStmt.list.accept(this);
        if ( !isWaitStmt ) {
            addContinueLabelToLoop();
        } else {
            appendBreakStmt();
        }
        decrIndentation();
        nlIndent();
        this.src.add("}");
        addBreakLabelToLoop(isWaitStmt);
        return null;
    }

    private void generateCodeFromStmtConditionFor(Expr expr) {
        final ExprList expressions = (ExprList) expr;
        Expr counterName = expressions.get().get(0);
        Expr counterInitialValue = expressions.get().get(1);
        Expr counterTargetValue = expressions.get().get(2);
        Expr counterStep = expressions.get().get(3);
        this.src.add("for (", "float ");
        counterName.accept(this);
        this.src.add(" = ");
        counterInitialValue.accept(this);
        this.src.add("; ");
        counterName.accept(this);
        this.src.add(" < ");
        counterTargetValue.accept(this);
        this.src.add("; ");
        counterName.accept(this);
        this.src.add(" += ");
        counterStep.accept(this);
        this.src.add(") {");
    }

    private void generateForEachPrefix(Expr expression) {
        ((Binary) expression).left.accept(this);
        this.src.add(";");
        nlIndent();
        this.src.add("for(int i = 0; i < ");
        ((Binary) expression).getRight().accept(this);
        this.src.add(".size(); ++i) {");
        incrIndentation();
        nlIndent();
        ((Binary) expression).left.accept(this);
        this.src.add(" = _getListElementByIndex(");
        ((Binary) expression).getRight().accept(this);
        this.src.add(", i);");
        decrIndentation();
    }

    @Override
    public Void visitDebugAction(DebugAction debugAction) {
        this.src.add("printf(\"%s\\n\", ToString(");
        debugAction.value.accept(this);
        this.src.add(").c_str());");
        return null;
    }

    @Override
    public Void visitWaitStmt(WaitStmt waitStmt) {
        this.src.add("while ( true ) {");
        incrIndentation();
        visitStmtList(waitStmt.statements);
        //nlIndent();
        //this.src.add("Wait(15);");
        decrIndentation();
        nlIndent();
        this.src.add("}");
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt waitTimeStmt) {
        this.src.add("Wait(");
        waitTimeStmt.time.accept(this);
        this.src.add(");");
        return null;
    }

    // copied from Arduino
    @Override
    public Void visitIndexOfFunct(IndexOfFunct indexOfFunct) {
        if ( indexOfFunct.value.toString().contains("ListCreate ") ) {
            this.src.add("null");
            return null;
        }
        String methodName = indexOfFunct.location == IndexLocation.LAST ? "_getLastOccuranceOfElement(" : "_getFirstOccuranceOfElement(";
        this.src.add(methodName);
        indexOfFunct.value.accept(this);
        this.src.add(", ");
        if ( indexOfFunct.find.getClass().equals(StringConst.class) ) {
            this.src.add("ToString(");
            indexOfFunct.find.accept(this);
            this.src.add(")");
        } else {
            indexOfFunct.find.accept(this);
        }
        this.src.add(")");
        return null;
    }

    // end copied from Arduino

    @Override
    public Void visitListCreate(ListCreate listCreate) {
        this.src.add("(", getListCreateCasting(listCreate.getBlocklyType()));
        super.visitListCreate(listCreate);
        this.src.add(")");
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
    public Void visitListSetIndex(ListSetIndex listSetIndex) {
        super.visitListSetIndex(listSetIndex);
        this.src.add(";");
        return null;
    }

    /*
     * TODO: There is something wrong with semicolon generation for calliope. Artem Vinokurov 25.10.2018
     */
    @Override
    public Void visitListGetIndex(ListGetIndex listGetIndex) {
        super.visitListGetIndex(listGetIndex);
        if ( ((ListElementOperations) listGetIndex.getElementOperation()).equals(ListElementOperations.REMOVE) ) {
            this.src.add(";");
        }
        return null;
    }

    // end copied from calliope

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct mathRandomIntFunct) {
        Expr min = mathRandomIntFunct.from;
        Expr max = mathRandomIntFunct.to;
        this.src.add("((rand() % (int) ((");
        min.accept(this);
        this.src.add(") - (");
        max.accept(this);
        this.src.add("))) + (");
        min.accept(this);
        this.src.add("))");
        return null;
    }

    @Override
    public Void visitMathCastStringFunct(MathCastStringFunct mathCastStringFunct) {
        // TODO check why this is not working for Arduinos!
        this.src.add("(ToString(");
        mathCastStringFunct.value.accept(this);
        this.src.add("))");
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct textJoinFunct) {
        List<Expr> texts = textJoinFunct.param.get();
        for ( int i = 0; i < texts.size(); i++ ) {
            this.src.add("ToString(");
            texts.get(i).accept(this);
            this.src.add(")");
            if ( i < texts.size() - 1 ) {
                this.src.add(" + ");
            }
        }
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        String port = motorOnAction.getUserDefinedPort();
        MotorDuration duration = motorOnAction.param.getDuration();
        Expr speedExpression = motorOnAction.param.getSpeed();
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

    private void generateRotateMotorForDuration(String port, Expr speedExpression, IMotorMoveMode durationMode, Expr durationExpression) {
        this.src.add("RotateMotorForAngle(", Ev3C4ev3Visitor.getPrefixedOutputPort(port), ", ");
        visitSpeedExpression(speedExpression);
        this.src.add(", ");
        if ( durationMode == MotorMoveMode.ROTATIONS ) {
            this.src.add("360 * ");
        }
        durationExpression.accept(this);
        this.src.add(");");
    }

    private void generateTurnOnMotor(String port, Expr speedExpression, boolean isRegulated) {
        String functionName = isRegulated ? "OnFwdReg" : "OnFwdEx";
        this.src.add(functionName, "(", Ev3C4ev3Visitor.getPrefixedOutputPort(port), ", ");
        visitSpeedExpression(speedExpression);
        if ( !isRegulated ) {
            this.src.add(", RESET_NONE");
        }
        this.src.add(");");
    }

    @Override
    public Void visitDriveAction(DriveAction driveAction) {
        MotorDuration duration = driveAction.param.getDuration();
        Expr speedExpression = driveAction.param.getSpeed();
        boolean reverse = isReverseGivenBrickConfigurationAndAction(driveAction.direction);
        if ( duration != null ) {
            generateDriveForDistance(speedExpression, duration.getValue(), reverse);
        } else {
            generateDrive(speedExpression, reverse);
        }
        return null;
    }

    private void generateDriveForDistance(Expr speedExpression, Expr distanceExpression, boolean reverse) {
        this.src.add("RotateMotorForAngle(", getDriveMotorPortsConstant(), ", ");
        visitSpeedExpression(speedExpression, reverse);
        this.src.add(", ");
        visitDistanceOfDrive(distanceExpression);
        this.src.add(");");
    }

    private void visitDistanceOfDrive(Expr distanceExpression) {
        this.src.add("(");
        distanceExpression.accept(this);
        this.src.add(" * 360) / (M_PI * WHEEL_DIAMETER)");
    }

    private void generateDrive(Expr speedExpression, boolean reverse) {
        String methodName = reverse ? "OnRevSync" : "OnFwdSync";
        this.src.add(methodName, "(", getDriveMotorPortsConstant(), ", ");
        visitSpeedExpression(speedExpression);
        this.src.add(");");
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction) {
        String port = motorGetPowerAction.getUserDefinedPort();
        if ( isActorOnPort(port) ) {
            this.src.add("MotorPower(OUT_", motorGetPowerAction.getUserDefinedPort(), ")");
        }
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction) {
        String port = motorSetPowerAction.getUserDefinedPort();
        this.src.add("SetPower(", Ev3C4ev3Visitor.getPrefixedOutputPort(port), ", ");
        visitSpeedExpression(motorSetPowerAction.power, isMotorReverse(port));
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        String port = motorStopAction.getUserDefinedPort();
        if ( motorStopAction.mode == MotorStopMode.FLOAT ) {
            this.src.add("Float(", Ev3C4ev3Visitor.getPrefixedOutputPort(port), ");");
        } else {
            this.src.add("Off(", Ev3C4ev3Visitor.getPrefixedOutputPort(port), ");");
        }
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction stopAction) {
        this.src.add("Off(", getDriveMotorPortsConstant(), ");");
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction curveAction) {
        MotorDuration duration = curveAction.paramLeft.getDuration();
        boolean isReverse = isReverseGivenBrickConfigurationAndAction(curveAction.direction);
        this.src.add(duration != null ? "SteerDriveForDistance(" : "SteerDrive(");
        this.src.add(getLeftDriveMotorPort(), ", ", getRightDriveMotorPort(), ", ");
        visitSpeedExpression(curveAction.paramLeft.getSpeed(), isReverse);
        this.src.add(", ");
        visitSpeedExpression(curveAction.paramRight.getSpeed(), isReverse);
        if ( duration != null ) {
            this.src.add(", ");
            duration.getValue().accept(this);
        }
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction turnAction) {
        MotorDuration duration = turnAction.param.getDuration();
        Expr speedExpression = turnAction.param.getSpeed();
        int turn = getTurn(turnAction);
        if ( duration != null ) {
            generateTurnForDistance(speedExpression, duration.getValue(), turn);
        } else {
            generateTurn(speedExpression, turn);
        }
        return null;
    }

    /**
     * Returns whether the port order of the left and right motors needs to be reversed. This happens in {@link #getDriveMotorPortsConstant()} as the c4ev3 API
     * only accepts the ports via ordered constants. This method is then used to check whether the order of the left and right (e.g C B - > B C) is reversed.
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

    private int getTurn(TurnAction turnAction) {
        /**
         * Turn is from -200 to 200 O: motor run at the same power 100: one motor run at the specified power the other doesn't 200: one motor run at the
         * specified power and the other at negative power
         */
        int turn = 200;
        if ( isAnyDriveMotorReverse() ) {
            turn *= -1;
        }
        if ( !isMotorLeftRightOrderReversed() ) {
            turn *= -1;
        }
        if ( turnAction.direction == TurnDirection.RIGHT ) {
            turn *= -1;
        }
        return turn;
    }

    private void generateTurnForDistance(Expr speedExpression, Expr distanceExpression, int turn) {
        this.src.add("RotateMotorForAngleWithTurn(", getDriveMotorPortsConstant(), ", ");
        visitSpeedExpression(speedExpression);
        this.src.add(", ");
        visitDistanceOfTurn(distanceExpression);
        this.src.add(", ", turn, ");");
    }

    private void visitDistanceOfTurn(Expr distanceExpression) {
        this.src.add("(");
        distanceExpression.accept(this);
        this.src.add(" * TRACK_WIDTH / WHEEL_DIAMETER)");
    }

    private void generateTurn(Expr speedExpression, int turn) {
        this.src.add("OnFwdSyncEx(", getDriveMotorPortsConstant(), ", ");
        visitSpeedExpression(speedExpression);
        this.src.add(", ", turn, ", RESET_NONE);");

    }

    private void visitSpeedExpression(Expr speedExpression) {
        visitSpeedExpression(speedExpression, false);
    }

    private void visitSpeedExpression(Expr speedExpression, boolean reverse) {
        this.src.add(reverse ? "-Speed(" : "Speed(");
        speedExpression.accept(this);
        this.src.add(")");
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
        String leftMotorPort = leftMotor.userDefinedPortName;
        String rightMotorPort = rightMotor.userDefinedPortName;
        return Pair.of(leftMotorPort, rightMotorPort);
    }

    private String getDriveMotorPortsConstant() {
        Pair<String, String> driveMotorPorts = getDriveMotorPorts();
        return Ev3C4ev3Visitor.getMotorPortConstant(driveMotorPorts.getFirst() + driveMotorPorts.getSecond());
    }

    private static String getMotorPortConstant(String ports) {
        char[] charArray = ports.toCharArray();
        Arrays.sort(charArray);
        return PREFIX_OUTPUT_PORT + new String(charArray);
    }

    private String getLeftDriveMotorPort() {
        return Ev3C4ev3Visitor.getPrefixedOutputPort(this.brickConfiguration.getFirstMotor(SC.LEFT).userDefinedPortName);
    }

    private String getRightDriveMotorPort() {
        return Ev3C4ev3Visitor.getPrefixedOutputPort(this.brickConfiguration.getFirstMotor(SC.RIGHT).userDefinedPortName);
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
    public Void visitEncoderSensor(EncoderSensor encoderSensor) {
        String port = encoderSensor.getUserDefinedPort();
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

    @Override
    public Void visitEncoderReset(EncoderReset encoderReset) {
        String port = encoderReset.sensorPort;
        this.src.add("ResetRotationCount(", Ev3C4ev3Visitor.getPrefixedOutputPort(port), ");");
        return null;
    }

    private void generateGetEncoderInDegrees(String port) {
        this.src.add("MotorRotationCount(", Ev3C4ev3Visitor.getPrefixedOutputPort(port), ")");
    }

    private void generateGetEncoderInRotations(String port) {
        this.src.add("(");
        this.generateGetEncoderInDegrees(port);
        this.src.add(" / 360.0)");
    }

    private void generateGetEncoderInDistance(String port) {
        this.src.add("(");
        this.generateGetEncoderInRotations(port);
        this.src.add(" * M_PI * WHEEL_DIAMETER)");
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        String timerNumber = timerSensor.getUserDefinedPort();
        this.src.add("GetTimerValue(", timerNumber, ")");
        return null;
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        String timerNumber = timerReset.sensorPort;
        this.src.add("ResetTimer(", timerNumber, ");");
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor touchSensor) {
        this.src.add("ReadEV3TouchSensor(", Ev3C4ev3Visitor.getPrefixedInputPort(touchSensor.getUserDefinedPort()), ")");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        String port = ultrasonicSensor.getUserDefinedPort();
        if ( ultrasonicSensor.getMode().equals(SC.DISTANCE) ) {
            generateRealUltrasonicDistance(port);
        } else {
            generateRealUltrasonicPresence(port);
        }
        return null;
    }

    private void generateRealUltrasonicDistance(String port) {
        this.src.add("ReadEV3UltrasonicSensorDistance(", Ev3C4ev3Visitor.getPrefixedInputPort(port), ", CM)");
    }

    private void generateRealUltrasonicPresence(String port) {
        this.src.add("ReadEV3UltrasonicSensorListen(", Ev3C4ev3Visitor.getPrefixedInputPort(port), ")");
    }

    @Override
    public Void visitSoundSensor(SoundSensor soundSensor) {
        this.src.add("ReadNXTSoundSensor(", Ev3C4ev3Visitor.getPrefixedInputPort(soundSensor.getUserDefinedPort()), ", DB)");
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor gyroSensor) {
        String port = gyroSensor.getUserDefinedPort();
        String mode = gyroSensor.getMode();
        generateReadGyro(port, mode);
        return null;
    }

    @Override
    public Void visitGyroReset(GyroReset gyroReset) {
        String port = gyroReset.sensorPort;
        generateResetGyroSensor(port);
        return null;
    }

    private void generateResetGyroSensor(String port) {
        this.src.add("NEPOResetEV3GyroSensor(", Ev3C4ev3Visitor.getPrefixedInputPort(port), ");");
    }

    private void generateReadGyro(String port, String mode) {
        this.src.add("ReadEV3GyroSensor(", Ev3C4ev3Visitor.getPrefixedInputPort(port), ", ", getGyroSensorReadModeConstant(mode), ")");
    }

    private String getGyroSensorReadModeConstant(String mode) {
        if ( mode.equals(SC.ANGLE) ) {
            return "EV3GyroAngle";
        } else {
            return "EV3GyroRate";
        }
    }

    @Override
    public Void visitColorSensor(ColorSensor colorSensor) {
        String mode = colorSensor.getMode();
        String port = Ev3C4ev3Visitor.getPrefixedInputPort(colorSensor.getUserDefinedPort());
        switch ( mode ) {
            case SC.COLOUR:
                this.src.add("ReadEV3ColorSensor(", port, ")");
                break;
            case SC.LIGHT:
                this.src.add("ReadEV3ColorSensorLight(", port, ", ReflectedLight)");
                break;
            case SC.AMBIENTLIGHT:
                this.src.add("ReadEV3ColorSensorLight(", port, ", AmbientLight)");
                break;
            case SC.RGB:
                this.src.add("NEPOReadEV3ColorSensorRGB(", port, ")");
                break;
        }
        return null;
    }

    @Override
    public Void visitHTColorSensor(HTColorSensor htColorSensor) {
        String mode = htColorSensor.getMode();
        String port = Ev3C4ev3Visitor.getPrefixedInputPort(htColorSensor.getUserDefinedPort());
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
        this.src.add(functionName, "(", port, ")");
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        String port = infraredSensor.getUserDefinedPort();
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
        this.src.add("ReadEV3IrSensorProximity(", Ev3C4ev3Visitor.getPrefixedInputPort(port), ")");
    }

    private void generateEV3IRSeeker(String port) {
        this.src.add("_ReadIRSeekAllChannels(", Ev3C4ev3Visitor.getPrefixedInputPort(port), ")");
    }

    @Override
    public Void visitCompassSensor(CompassSensor compassSensor) {
        String mode = getCompassSensorReadModeConstant(compassSensor.getMode());
        this.src.add("ReadHTCompassSensor(", Ev3C4ev3Visitor.getPrefixedInputPort(compassSensor.getUserDefinedPort()), ", ", mode, ")");
        return null;
    }

    @Override
    public Void visitCompassCalibrate(CompassCalibrate compassCalibrate) {
        String port = Ev3C4ev3Visitor.getPrefixedInputPort(compassCalibrate.getUserDefinedPort());
        nlIndent();
        this.src.add("StartHTCompassCalibration(", port, ");");
        this.src.add("Wait(40000);");
        this.src.add("StopHTCompassCalibration(", port, ");");
        nlIndent();
        return null;
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
    public Void visitIRSeekerSensor(IRSeekerSensor irSeekerSensor) {
        this.src.add("ReadHTIrSensor(", Ev3C4ev3Visitor.getPrefixedInputPort(irSeekerSensor.getUserDefinedPort()), ", ", getIRSeekerSensorConstantMode(irSeekerSensor.getMode()), ")");
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
        this.src.add("ReadSensorInMode(", Ev3C4ev3Visitor.getPrefixedInputPort(port), ", ", mode, ")");
    }

    private void generateReadSensor(String port) {
        this.src.add("readSensor(", Ev3C4ev3Visitor.getPrefixedInputPort(port), ")");
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        this.src.add("ButtonIsDown(", getKeyConstant(keysSensor.getUserDefinedPort()), ")");
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
    public Void visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        this.src.add("LcdClean();");
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction showTextAction) {
        this.src.add("DrawString(ToString(");
        showTextAction.msg.accept(this);
        this.src.add("), ");
        showTextAction.x.accept(this);
        this.src.add(", ");
        showTextAction.y.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction playFileAction) {
        String soundConstant = getSoundConstantByFileName(playFileAction.fileName);
        this.src.add("PlaySystemSound(", soundConstant, ");");
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
    public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        this.src.add("NEPOPlayTone(", playNoteAction.frequency, ", ", playNoteAction.duration, ");");
        return null;
    }

    @Override
    public Void visitGetVolumeAction(GetVolumeAction getVolumeAction) {
        this.src.add("GetVolume()");
        return null;
    }

    @Override
    public Void visitSetVolumeAction(SetVolumeAction setVolumeAction) {
        this.src.add("SetVolume(");
        setVolumeAction.volume.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction toneAction) {
        this.src.add("NEPOPlayTone(");
        toneAction.frequency.accept(this);
        this.src.add(", ");
        toneAction.duration.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitShowPictureAction(ShowPictureAction showPictureAction) {
        String picture = showPictureAction.pic.toString();
        this.src.add("LcdPicture(LCD_COLOR_BLACK, 0, 0, ", picture, ");");
        return null;
    }

    @Override
    public Void visitBrickLightOnAction(BrickLightOnAction brickLightOnAction) {
        String pattern = getLedPattern(brickLightOnAction.colour, brickLightOnAction.mode);
        this.src.add("SetLedPattern(", pattern, ");");
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
    public Void visitBrickLightOffAction(BrickLightOffAction brickLightOffAction) {
        switch ( brickLightOffAction.mode ) {
            case "OFF":
                this.src.add("SetLedPattern(LED_BLACK);");
                break;
            case "RESET":
                // TODO: Light reset not implemented for C4EV3 yet...;
                break;
            default:
                throw new DbcException("Invalid MODE encountered in BrickLightOffAction: " + brickLightOffAction.mode);
        }
        return null;
    }

    @Override
    public Void visitBluetoothReceiveAction(BluetoothReceiveAction bluetoothReceiveAction) {
        this.src.add("NEPOReceiveStringFrom(");
        bluetoothReceiveAction.connection.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitBluetoothConnectAction(BluetoothConnectAction bluetoothConnectAction) {
        this.src.add("NEPOConnectTo(");
        bluetoothConnectAction.address.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitBluetoothSendAction(BluetoothSendAction bluetoothSendAction) {
        this.src.add("NEPOSendStringTo(");
        bluetoothSendAction.connection.accept(this);
        this.src.add(", ");
        bluetoothSendAction.msg.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction bluetoothWaitForConnection) {
        this.src.add("NEPOWaitConnection()");
        return null;
    }

    @Override
    public Void visitBluetoothCheckConnectAction(BluetoothCheckConnectAction bluetoothCheckConnectAction) {
        return null;
    }

    @Override
    public Void visitSetLanguageAction(SetLanguageAction setLanguageAction) {
        this.src.add("SetLanguage(\"");
        this.src.add(TTSLanguageMapper.getLanguageString(setLanguageAction.language));
        this.src.add("\");");
        return null;
    }

    @Override
    public Void visitSayTextAction(SayTextAction sayTextAction) {
        this.src.add("Say(ToString(");
        sayTextAction.msg.accept(this);
        this.src.add("), 30, 50);"); // set defaults
        return null;
    }

    @Override
    public Void visitSayTextWithSpeedAndPitchAction(SayTextWithSpeedAndPitchAction sayTextAction) {
        this.src.add("Say(ToString(");
        sayTextAction.msg.accept(this);
        this.src.add("), ");
        sayTextAction.speed.accept(this);
        this.src.add(", ");
        sayTextAction.pitch.accept(this);
        this.src.add(");");

        return null;
    }

    private void generateRandomSeed() {
        if ( this.getBean(CodeGeneratorSetupBean.class).getUsedMethods().contains(FunctionNames.RANDOM)
            || this.getBean(CodeGeneratorSetupBean.class).getUsedMethods().contains(FunctionNames.RANDOM_DOUBLE) ) {
            this.src.add("srand (time(NULL));");
            this.nlIndent();
        }
    }
}
