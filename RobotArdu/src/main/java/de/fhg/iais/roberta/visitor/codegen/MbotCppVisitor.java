package de.fhg.iais.roberta.visitor.codegen;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
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
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LEDMatrixImageAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LEDMatrixSetBrightnessAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LEDMatrixTextAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.SendIRAction;
import de.fhg.iais.roberta.syntax.expressions.arduino.LEDMatrixImage;
import de.fhg.iais.roberta.syntax.functions.arduino.LEDMatrixImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.arduino.LEDMatrixImageShiftFunction;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyExpr;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.FunctionExpr;
import de.fhg.iais.roberta.syntax.lang.expr.MethodExpr;
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.lang.methods.MethodCall;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.MotorDuration;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IMbotVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a hussentation of a phrase to a StringBuilder. <b>This
 * representation is correct C code for Arduino.</b> <br>
 */
public final class MbotCppVisitor extends NepoArduinoCppVisitor implements IMbotVisitor<Void> {

    private final HashMap<String, Integer> imageList = new HashMap<String, Integer>();

    /**
     * Initialize the C++ code generator visitor.
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param phrases to generate the code from
     */
    public MbotCppVisitor(List<List<Phrase>> phrases, ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean> beans) {
        super(phrases, brickConfiguration, beans);
    }

    @Override
    public Void visitEmptyExpr(EmptyExpr emptyExpr) {
        switch ( emptyExpr.getDefVal() ) {
            case STRING:
                this.sb.append("\"\"");
                break;
            case BOOLEAN:
                this.sb.append("true");
                break;
            case NUMBER:
            case NUMBER_INT:
                this.sb.append("0");
                break;
            case ARRAY:
                break;
            case NULL:
                break;
            case COLOR:
                this.sb.append("RGB(255, 255, 255)");
                break;
            case IMAGE:
                this.sb.append("{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}");
                break;
            default:
                this.sb.append("NULL");
                break;
        }
        return null;
    }

    @Override
    public String getLanguageVarTypeFromBlocklyType(BlocklyType type) {
        switch ( type ) {
            case ANY:
            case COMPARABLE:
            case ADDABLE:
            case NULL:
            case REF:
            case PRIM:
            case NOTHING:
            case CAPTURED_TYPE:
            case R:
            case S:
            case T:
                return "";
            case ARRAY:
                return "std::list<double>";
            case ARRAY_NUMBER:
                return "std::list<double>";
            case ARRAY_STRING:
                return "std::list<String>";
            case ARRAY_BOOLEAN:
                return "std::list<bool>";
            case ARRAY_COLOUR:
                return "std::list<unsigned int>";
            case ARRAY_IMAGE:
                return "std::list<std::vector<uint8_t>>";
            case BOOLEAN:
                return "bool";
            case NUMBER:
                return "double";
            case NUMBER_INT:
                return "int";
            case STRING:
                return "String";
            case VOID:
                return "void";
            case COLOR:
                return "unsigned int";
            case CONNECTION:
                return "int";
            case IMAGE:
                return "std::vector<uint8_t>";
            default:
                throw new IllegalArgumentException("unhandled type");
        }
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        this.sb.append("__meLEDMatrix_").append(clearDisplayAction.port).append(".clearScreen();");
        return null;
    }

    @Override
    public Void visitLightAction(LightAction lightAction) {
        if ( lightAction.rgbLedColor.getClass().equals(Var.class) ) {
            String tempVarName = "___" + ((Var) lightAction.rgbLedColor).name;
            generateCodeForRgbLed(lightAction, tempVarName);
        } else if ( lightAction.rgbLedColor.getClass().equals(ColorConst.class) ) {
            ColorConst colorConst = (ColorConst) lightAction.rgbLedColor;
            this.sb.append("_meRgbLed.setColor(");
            this.sb.append(lightAction.port);
            this.sb.append(", ");
            this.sb.append(colorConst.getRedChannelHex());
            this.sb.append(", ");
            this.sb.append(colorConst.getGreenChannelHex());
            this.sb.append(", ");
            this.sb.append(colorConst.getBlueChannelHex());
            this.sb.append(");");
            nlIndent();
            this.sb.append("_meRgbLed.show();");
        } else if ( lightAction.rgbLedColor.getClass().equals(MethodExpr.class) ) {
            String tempVarName = "_v_colour_temp";
            this.sb.append(tempVarName).append(" = ");
            visitMethodCall((MethodCall) ((MethodExpr) lightAction.rgbLedColor).method);
            this.sb.append(";");
            nlIndent();
            generateCodeForRgbLed(lightAction, tempVarName);
        } else if ( lightAction.rgbLedColor.getClass().equals(FunctionExpr.class) ) {
            String tempVarName = "_v_colour_temp";
            this.sb.append(tempVarName).append(" = ");
            ((FunctionExpr) lightAction.rgbLedColor).function.accept(this);
            this.sb.append(";");
            nlIndent();
            generateCodeForRgbLed(lightAction, tempVarName);
        } else {
            Map<String, Expr> Channels = new HashMap<>();
            this.sb.append("_meRgbLed.setColor(");
            this.sb.append(lightAction.port);
            Channels.put("red", ((RgbColor) lightAction.rgbLedColor).R);
            Channels.put("green", ((RgbColor) lightAction.rgbLedColor).G);
            Channels.put("blue", ((RgbColor) lightAction.rgbLedColor).B);
            Channels.forEach((k, v) -> {
                this.sb.append(", ");
                v.accept(this);
            });
            this.sb.append(");");
            nlIndent();
            this.sb.append("_meRgbLed.show();");
        }
        return null;
    }

    private void generateCodeForRgbLed(LightAction lightAction, String tempVarName) {
        this.sb.append("_meRgbLed.setColor(");
        this.sb.append(lightAction.port);
        this.sb.append(", ");
        this.sb.append("RCHANNEL(");
        this.sb.append(tempVarName);
        this.sb.append("), ");
        this.sb.append("GCHANNEL(");
        this.sb.append(tempVarName);
        this.sb.append("), ");
        this.sb.append("BCHANNEL(");
        this.sb.append(tempVarName);
        this.sb.append("));");
        nlIndent();
        this.sb.append("_meRgbLed.show();");
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction lightStatusAction) {
        this.sb.append("_meRgbLed.setColor(" + lightStatusAction.getUserDefinedPort());
        this.sb.append(", 0, 0, 0);");
        nlIndent();
        this.sb.append("_meRgbLed.show();");
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction toneAction) {
        //8 - sound port
        this.sb.append("_meBuzzer.tone(8, ");
        toneAction.frequency.accept(this);
        this.sb.append(", ");
        toneAction.duration.accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        //8 - sound port
        this.sb.append("_meBuzzer.tone(8, ");
        this.sb.append(playNoteAction.frequency);
        this.sb.append(", ");
        this.sb.append(playNoteAction.duration);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        final MotorDuration duration = motorOnAction.param.getDuration();
        this.sb.append("_meDCmotor").append(motorOnAction.getUserDefinedPort()).append(".run(");
        if ( !Objects.equals(this.configuration.getFirstMotorPort(SC.RIGHT), motorOnAction.getUserDefinedPort()) ) {
            this.sb.append("-1*");
        }
        this.sb.append("(");
        motorOnAction.param.getSpeed().accept(this);
        this.sb.append(")*255/100);");
        if ( duration != null ) {
            nlIndent();
            this.sb.append("delay(");
            motorOnAction.getDurationValue().accept(this);
            this.sb.append(");");
            nlIndent();
            this.sb.append("_meDCmotor").append(motorOnAction.getUserDefinedPort()).append(".stop();");
        }
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction) {
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction) {
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        this.sb.append("_meDCmotor").append(motorStopAction.getUserDefinedPort()).append(".stop();");
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction driveAction) {
        final MotorDuration duration = driveAction.param.getDuration();
        this.sb.append("_meDrive.drive(");
        driveAction.param.getSpeed().accept(this);
        this.sb.append(", ");
        this.sb.append(driveAction.direction == DriveDirection.FOREWARD ? 1 : 0);
        if ( duration != null ) {
            this.sb.append(", ");
            duration.getValue().accept(this);
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction curveAction) {
        final MotorDuration duration = curveAction.paramLeft.getDuration();
        this.sb.append("_meDrive.steer(");
        curveAction.paramLeft.getSpeed().accept(this);
        this.sb.append(", ");
        curveAction.paramRight.getSpeed().accept(this);
        this.sb.append(", ").append(curveAction.direction == DriveDirection.FOREWARD ? 1 : 0);
        if ( duration != null ) {
            this.sb.append(", ");
            duration.getValue().accept(this);
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction turnAction) {
        final MotorDuration duration = turnAction.param.getDuration();
        this.sb.append("_meDrive.turn(");
        turnAction.param.getSpeed().accept(this);
        this.sb.append(", ").append(turnAction.direction == TurnDirection.LEFT ? 1 : 0);
        if ( duration != null ) {
            this.sb.append(", ");
            duration.getValue().accept(this);
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction stopAction) {
        for ( final UsedActor actor : this.getBean(UsedHardwareBean.class).getUsedActors() ) {
            if ( actor.getType().equals(SC.DIFFERENTIAL_DRIVE) ) {
                this.sb.append("_meDrive.stop();");
                break;
            }
        }
        return null;
    }

    @Override
    public Void visitLEDMatrixImageAction(LEDMatrixImageAction ledMatrixImageAction) {
        String end = ");";
        if ( ledMatrixImageAction.displayImageMode.equals("IMAGE") ) {
            this.sb.append("__meLEDMatrix_").append(ledMatrixImageAction.port).append(".drawBitmap(0, 0, 16, ");
            this.sb.append("&");
            ledMatrixImageAction.valuesToDisplay.accept(this);
            this.sb.append("[0]");
            this.sb.append(end);
        } else if ( ledMatrixImageAction.displayImageMode.equals("ANIMATION") ) {
            this.sb.append("drawAnimationLEDMatrix(&__meLEDMatrix_").append(ledMatrixImageAction.port).append(", ");
            ledMatrixImageAction.valuesToDisplay.accept(this);
            this.sb.append(", 200");
            this.sb.append(end);
        } else {
            throw new DbcException("LEDMatrix display mode is not supported: " + ledMatrixImageAction.displayImageMode);
        }
        return null;
    }

    @Override
    public Void visitLEDMatrixTextAction(LEDMatrixTextAction ledMatrixTextAction) {
        this.sb.append("drawStrLEDMatrix(&__meLEDMatrix_").append(ledMatrixTextAction.port).append(", ");
        if ( !ledMatrixTextAction.msg.getVarType().equals(BlocklyType.STRING) ) {
            this.sb.append("String(");
            ledMatrixTextAction.msg.accept(this);
            this.sb.append(")");
        } else {
            ledMatrixTextAction.msg.accept(this);
        }
        this.sb.append(", 100);");
        return null;
    }

    @Override
    public Void visitLEDMatrixImage(LEDMatrixImage ledMatrixImage) {
        Map<String, String[][]> usedIDImages = this.getBean(UsedHardwareBean.class).getUsedIDImages();
        this.sb.append("__ledMatrix").append(this.imageList.get(ledMatrixImage.getProperty().getBlocklyId()));
        return null;
    }

    @Override
    public Void visitLEDMatrixImageShiftFunction(LEDMatrixImageShiftFunction ledMatrixImageShiftFunction) {
        this.sb.append("(shiftLEDMatrix");
        this.sb.append(capitalizeFirstLetter(ledMatrixImageShiftFunction.shiftDirection.toString()));
        this.sb.append("Vec(");
        ledMatrixImageShiftFunction.image.accept(this);

        this.sb.append(", ");
        ledMatrixImageShiftFunction.positions.accept(this);
        this.sb.append("))");
        return null;
    }

    @Override
    public Void visitLEDMatrixImageInvertFunction(LEDMatrixImageInvertFunction ledMatrixImageInverFunction) {
        this.sb.append("(invertLEDMatrixVec(");
        ledMatrixImageInverFunction.image.accept(this);

        this.sb.append("))");
        return null;
    }

    @Override
    public Void visitLEDMatrixSetBrightnessAction(LEDMatrixSetBrightnessAction ledMatrixSetBrightnessAction) {
        this.sb.append("__meLEDMatrix_").append(ledMatrixSetBrightnessAction.port).append(".setBrightness(");
        ledMatrixSetBrightnessAction.brightness.accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitSendIRAction(SendIRAction sendIRAction) {
        this.sb.append("_meIr.sendString(");
        sendIRAction.message.accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitReceiveIRAction(ReceiveIRAction receiveIRAction) {
        this.sb.append("_meIr.getString()");
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor lightSensor) {
        this.sb.append("_meLight" + lightSensor.getUserDefinedPort() + ".read() * ANALOG2PERCENT");
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        this.sb.append("(analogRead(PORT_7) < 512)");
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        this.sb.append("!__meLineFollower" + infraredSensor.getUserDefinedPort() + ".readSensor" + infraredSensor.getSlot() + "()");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        this.sb.append("_meUltraSensor" + ultrasonicSensor.getUserDefinedPort() + ".distanceCm()");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask mainTask) {
        predefineImages();
        mainTask.variables.accept(this);
        if ( !mainTask.variables.toString().equals("") ) {
            nlIndent();
        }
        nlIndent();
        //generateConfigurationVariables();
        generateTimerVariables();
        long numberConf =
            this.programPhrases
                .stream()
                .filter(phrase -> phrase.getKind().getCategory() == Category.METHOD && !phrase.getKind().hasName("METHOD_CALL"))
                .count();
        if ( (this.configuration.getConfigurationComponents().isEmpty() || this.getBean(UsedHardwareBean.class).isSensorUsed(SC.TIMER)) && numberConf == 0 ) {
            nlIndent();
        }
        generateUserDefinedMethods();
        if ( numberConf != 0 ) {
            nlIndent();
        }
        this.sb.append("void setup()");
        nlIndent();
        this.sb.append("{");
        incrIndentation();
        nlIndent();
        this.sb.append("Serial.begin(9600);");
        //      TODO test the following lines when these sensors are available
        //        for ( final UsedSensor usedSensor : this.usedSensors ) {
        //            switch ( usedSensor.getType() ) {
        //                case SC.GYRO:
        //                case SC.ACCELEROMETER:
        //                    nlIndent();
        //                    this.sb.append("myGyro" + usedSensor.getPort() + ".begin();");
        //                    break;
        //                default:
        //                    break;
        //            }
        //        }
        nlIndent();
        generateUsedVars();
        //      TODO test the following lines when these sensors are available
        //        this.sb.append("\n}");
        //        decrIndentation();
        //        for ( final UsedSensor usedSensor : this.usedSensors ) {
        //            switch ( usedSensor.getType() ) {
        //                case SC.GYRO:
        //                    nlIndent();
        //                    this.sb.append("myGyro" + usedSensor.getPort() + ".update();");
        //                    break;
        //                case SC.ACCELEROMETER:
        //                    nlIndent();
        //                    this.sb.append("myGyro" + usedSensor.getPort() + ".update();");
        //                    break;
        //                case SC.TEMPERATURE:
        //                    nlIndent();
        //                    this.sb.append("myTemp" + usedSensor.getPort() + ".update();");
        //                    break;
        //                default:
        //                    break;
        //            }
        //        }
        this.sb.delete(this.sb.lastIndexOf("\n"), this.sb.length());
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        nlIndent();
        return null;
    }

    private void predefineImages() {
        Map<String, String[][]> usedIDImages = this.getBean(UsedHardwareBean.class).getUsedIDImages();
        int i = 0;
        for ( Map.Entry<String, String[][]> entry : usedIDImages.entrySet() ) {
            this.sb.append("const std::vector<uint8_t> __ledMatrix").append(i).append(" = ");
            writeImage(entry.getValue());
            this.sb.append(";");
            nlIndent();
            this.imageList.put(entry.getKey(), i);
            i++;
        }
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        } else {
            decrIndentation();
        }
        this.sb.append("// This file is automatically generated by the Open Roberta Lab.");
        nlIndent();
        nlIndent();
        this.sb.append("#define ANALOG2PERCENT 0.0978");
        nlIndent();
        nlIndent();
        this.sb.append("#include <MeMCore.h>");
        nlIndent();
        this.sb.append("#include <MeDrive.h>");
        nlIndent();
        this.sb.append("#include <NEPODefs.h>");
        nlIndent();
        generateSensors();
        generateActors();
        nlIndent();
        nlIndent();
        super.generateProgramPrefix(withWrapping);
    }

    private void generateSensors() {
        LinkedHashSet<Integer> usedInfraredPort = new LinkedHashSet<>();
        for ( final UsedSensor usedSensor : this.getBean(UsedHardwareBean.class).getUsedSensors() ) {
            switch ( usedSensor.getType() ) {
                case SC.BUTTON:
                    nlIndent();
                    this.sb.append("pinMode(7, INPUT);");
                    break;
                case SC.COLOR:
                    break;
                case SC.ULTRASONIC:
                    nlIndent();
                    this.sb.append("MeUltrasonicSensor _meUltraSensor" + usedSensor.getPort() + "(PORT_" + usedSensor.getPort() + ");");
                    break;
                case SC.MOTION:
                    nlIndent();
                    this.sb.append("MePIRMotionSensor _mePir" + usedSensor.getPort() + "(" + usedSensor.getPort() + ");");
                    break;
                case SC.TEMPERATURE:
                    nlIndent();
                    this.sb.append("MeHumiture _meTemp" + usedSensor.getPort() + "(" + usedSensor.getPort() + ");");
                    break;
                case SC.TOUCH:
                    nlIndent();
                    this.sb.append("MeTouchSensor _meTouch" + usedSensor.getPort() + "(" + usedSensor.getPort() + ");");
                    break;
                case SC.LIGHT:
                    nlIndent();
                    this.sb.append("MeLightSensor _meLight" + usedSensor.getPort() + "(PORT_" + usedSensor.getPort() + ");");
                    break;
                case SC.INFRARED:
                    int port = Integer.parseInt(usedSensor.getPort());
                    if ( !usedInfraredPort.contains(port) ) {
                        nlIndent();
                        this.sb.append("MeLineFollower __meLineFollower" + usedSensor.getPort() + "(PORT_" + usedSensor.getPort() + ");");
                    }
                    usedInfraredPort.add(port);
                    break;
                case SC.COMPASS:
                    break;
                case SC.GYRO:
                case SC.ACCELEROMETER:
                    nlIndent();
                    this.sb.append("MeGyro myGyro" + usedSensor.getPort() + "(" + usedSensor.getPort() + ");");
                    break;
                case SC.SOUND:
                    nlIndent();
                    this.sb.append("MeSoundSensor _meSoundSensor" + usedSensor.getPort() + "(" + usedSensor.getPort() + ");");
                    break;
                case SC.JOYSTICK:
                    nlIndent();
                    this.sb.append("MeJoystick _meJoystick" + usedSensor.getPort() + "(" + usedSensor.getPort() + ");");
                    break;
                case SC.FLAMESENSOR:
                    nlIndent();
                    this.sb.append("MeFlameSensor _meFlameSensor" + usedSensor.getPort() + "(" + usedSensor.getPort() + ");");
                    break;
                case SC.VOLTAGE:
                    nlIndent();
                    this.sb.append("MePotentiometer _mePotentiometer" + usedSensor.getPort() + "(" + usedSensor.getPort() + ");");
                    break;
                case SC.TIMER:
                    break;
                default:
                    throw new DbcException("Sensor is not supported! " + usedSensor.getType());
            }
        }
    }

    private void generateActors() {
        for ( final UsedActor usedActor : this.getBean(UsedHardwareBean.class).getUsedActors() ) {
            switch ( usedActor.getType() ) {
                case SC.LED_ON_BOARD:
                    nlIndent();
                    this.sb.append("MeRGBLed _meRgbLed(7, 2);");
                    nlIndent();
                    this.sb.append("int _v_colour_temp;");
                    break;
                case SC.GEARED_MOTOR:
                    nlIndent();
                    this.sb.append("MeDCMotor _meDCmotor" + usedActor.getPort() + "(M" + usedActor.getPort() + ");");
                    break;
                case SC.DIFFERENTIAL_DRIVE:
                    nlIndent();
                    this.sb
                        .append(
                            "MeDrive _meDrive(M"
                                + this.configuration.getFirstMotorPort(SC.LEFT)
                                + ", M"
                                + this.configuration.getFirstMotorPort(SC.RIGHT)
                                + ");");
                    break;
                case SC.LED_MATRIX:
                    nlIndent();
                    this.sb.append("MeLEDMatrix __meLEDMatrix_" + usedActor.getPort() + "(" + usedActor.getPort() + ");");
                    break;
                case SC.BUZZER:
                    nlIndent();
                    this.sb.append("MeBuzzer _meBuzzer;");
                    break;
                case SC.IR_TRANSMITTER:
                    nlIndent();
                    this.sb.append("MeIR _meIr;");
                    break;
                case SC.SERIAL:
                    break;
                default:
                    throw new DbcException("Actor is not supported! " + usedActor.getType());
            }
        }
    }

    private void writeImage(String[][] image) {
        this.sb.append("{");
        for ( int i = 0; i < 16; i++ ) {
            this.sb.append("0x");
            int hex = 0;
            for ( int j = 0; j < 8; j++ ) {
                String pixel = image[i][j].trim();
                if ( pixel.equals("#") ) {
                    hex += Math.pow(2, j);
                }
            }
            this.sb.append(Integer.toHexString(hex));
            if ( i < 15 ) {
                this.sb.append(", ");
            }
        }
        this.sb.append("}");
    }

    private String capitalizeFirstLetter(String original) {
        if ( original == null || original.length() == 0 ) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1).toLowerCase();
    }
}
