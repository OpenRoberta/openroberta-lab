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
import de.fhg.iais.roberta.syntax.action.light.RgbLedOffAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOnAction;
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
                this.src.add("\"\"");
                break;
            case BOOLEAN:
                this.src.add("true");
                break;
            case NUMBER:
            case NUMBER_INT:
                this.src.add("0");
                break;
            case ARRAY:
                break;
            case NULL:
                break;
            case COLOR:
                this.src.add("RGB(255, 255, 255)");
                break;
            case IMAGE:
                this.src.add("{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}");
                break;
            default:
                this.src.add("NULL");
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
        this.src.add("__meLEDMatrix_", clearDisplayAction.port, ".clearScreen();");
        return null;
    }

    @Override
    public Void visitRgbLedOnAction(RgbLedOnAction rgbLedOnAction) {
        if ( rgbLedOnAction.colour.getClass().equals(Var.class) ) {
            String tempVarName = "___" + ((Var) rgbLedOnAction.colour).name;
            generateCodeForRgbLed(rgbLedOnAction, tempVarName);
        } else if ( rgbLedOnAction.colour.getClass().equals(ColorConst.class) ) {
            ColorConst colorConst = (ColorConst) rgbLedOnAction.colour;
            this.src.add("_meRgbLed.setColor(");
            this.src.add(rgbLedOnAction.port);
            this.src.add(", ");
            this.src.add(colorConst.getRedChannelHex());
            this.src.add(", ");
            this.src.add(colorConst.getGreenChannelHex());
            this.src.add(", ");
            this.src.add(colorConst.getBlueChannelHex());
            this.src.add(");");
            nlIndent();
            this.src.add("_meRgbLed.show();");
        } else if ( rgbLedOnAction.colour.getClass().equals(MethodExpr.class) ) {
            String tempVarName = "_v_colour_temp";
            this.src.add(tempVarName, " = ");
            visitMethodCall((MethodCall) ((MethodExpr) rgbLedOnAction.colour).method);
            this.src.add(";");
            nlIndent();
            generateCodeForRgbLed(rgbLedOnAction, tempVarName);
        } else if ( rgbLedOnAction.colour.getClass().equals(FunctionExpr.class) ) {
            String tempVarName = "_v_colour_temp";
            this.src.add(tempVarName, " = ");
            ((FunctionExpr) rgbLedOnAction.colour).function.accept(this);
            this.src.add(";");
            nlIndent();
            generateCodeForRgbLed(rgbLedOnAction, tempVarName);
        } else {
            Map<String, Expr> Channels = new HashMap<>();
            this.src.add("_meRgbLed.setColor(");
            this.src.add(rgbLedOnAction.port);
            Channels.put("red", ((RgbColor) rgbLedOnAction.colour).R);
            Channels.put("green", ((RgbColor) rgbLedOnAction.colour).G);
            Channels.put("blue", ((RgbColor) rgbLedOnAction.colour).B);
            Channels.forEach((k, v) -> {
                this.src.add(", ");
                v.accept(this);
            });
            this.src.add(");");
            nlIndent();
            this.src.add("_meRgbLed.show();");
        }
        return null;
    }

    private void generateCodeForRgbLed(RgbLedOnAction rgbLedOnAction, String tempVarName) {
        this.src.add("_meRgbLed.setColor(");
        this.src.add(rgbLedOnAction.port);
        this.src.add(", ");
        this.src.add("RCHANNEL(");
        this.src.add(tempVarName);
        this.src.add("), ");
        this.src.add("GCHANNEL(");
        this.src.add(tempVarName);
        this.src.add("), ");
        this.src.add("BCHANNEL(");
        this.src.add(tempVarName);
        this.src.add("));");
        nlIndent();
        this.src.add("_meRgbLed.show();");
    }

    @Override
    public Void visitRgbLedOffAction(RgbLedOffAction rgbLedOffAction) {
        this.src.add("_meRgbLed.setColor(", rgbLedOffAction.port);
        this.src.add(", 0, 0, 0);");
        nlIndent();
        this.src.add("_meRgbLed.show();");
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction toneAction) {
        //8 - sound port
        this.src.add("_meBuzzer.tone(8, ");
        toneAction.frequency.accept(this);
        this.src.add(", ");
        toneAction.duration.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        //8 - sound port
        this.src.add("_meBuzzer.tone(8, ");
        this.src.add(playNoteAction.frequency);
        this.src.add(", ");
        this.src.add(playNoteAction.duration);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        final MotorDuration duration = motorOnAction.param.getDuration();
        this.src.add("_meDCmotor", motorOnAction.getUserDefinedPort(), ".run(");
        if ( !Objects.equals(this.configuration.getFirstMotorPort(SC.RIGHT), motorOnAction.getUserDefinedPort()) ) {
            this.src.add("-1*");
        }
        this.src.add("(");
        motorOnAction.param.getSpeed().accept(this);
        this.src.add(")*255/100);");
        if ( duration != null ) {
            nlIndent();
            this.src.add("delay(");
            motorOnAction.getDurationValue().accept(this);
            this.src.add(");");
            nlIndent();
            this.src.add("_meDCmotor", motorOnAction.getUserDefinedPort(), ".stop();");
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
        this.src.add("_meDCmotor", motorStopAction.getUserDefinedPort(), ".stop();");
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction driveAction) {
        final MotorDuration duration = driveAction.param.getDuration();
        this.src.add("_meDrive.drive(");
        driveAction.param.getSpeed().accept(this);
        this.src.add(", ");
        this.src.add(driveAction.direction == DriveDirection.FOREWARD ? 1 : 0);
        if ( duration != null ) {
            this.src.add(", ");
            duration.getValue().accept(this);
        }
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction curveAction) {
        final MotorDuration duration = curveAction.paramLeft.getDuration();
        this.src.add("_meDrive.steer(");
        curveAction.paramLeft.getSpeed().accept(this);
        this.src.add(", ");
        curveAction.paramRight.getSpeed().accept(this);
        this.src.add(", ", curveAction.direction == DriveDirection.FOREWARD ? 1 : 0);
        if ( duration != null ) {
            this.src.add(", ");
            duration.getValue().accept(this);
        }
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction turnAction) {
        final MotorDuration duration = turnAction.param.getDuration();
        this.src.add("_meDrive.turn(");
        turnAction.param.getSpeed().accept(this);
        this.src.add(", ", turnAction.direction == TurnDirection.LEFT ? 1 : 0);
        if ( duration != null ) {
            this.src.add(", ");
            duration.getValue().accept(this);
        }
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction stopAction) {
        for ( final UsedActor actor : this.getBean(UsedHardwareBean.class).getUsedActors() ) {
            if ( actor.getType().equals(SC.DIFFERENTIAL_DRIVE) ) {
                this.src.add("_meDrive.stop();");
                break;
            }
        }
        return null;
    }

    @Override
    public Void visitLEDMatrixImageAction(LEDMatrixImageAction ledMatrixImageAction) {
        String end = ");";
        if ( ledMatrixImageAction.displayImageMode.equals("IMAGE") ) {
            this.src.add("__meLEDMatrix_", ledMatrixImageAction.port, ".drawBitmap(0, 0, 16, ");
            this.src.add("&");
            ledMatrixImageAction.valuesToDisplay.accept(this);
            this.src.add("[0]");
            this.src.add(end);
        } else if ( ledMatrixImageAction.displayImageMode.equals("ANIMATION") ) {
            this.src.add("drawAnimationLEDMatrix(&__meLEDMatrix_", ledMatrixImageAction.port, ", ");
            ledMatrixImageAction.valuesToDisplay.accept(this);
            this.src.add(", 200");
            this.src.add(end);
        } else {
            throw new DbcException("LEDMatrix display mode is not supported: " + ledMatrixImageAction.displayImageMode);
        }
        return null;
    }

    @Override
    public Void visitLEDMatrixTextAction(LEDMatrixTextAction ledMatrixTextAction) {
        this.src.add("drawStrLEDMatrix(&__meLEDMatrix_", ledMatrixTextAction.port, ", ");
        if ( !ledMatrixTextAction.msg.getVarType().equals(BlocklyType.STRING) ) {
            this.src.add("String(");
            ledMatrixTextAction.msg.accept(this);
            this.src.add(")");
        } else {
            ledMatrixTextAction.msg.accept(this);
        }
        this.src.add(", 100);");
        return null;
    }

    @Override
    public Void visitLEDMatrixImage(LEDMatrixImage ledMatrixImage) {
        Map<String, String[][]> usedIDImages = this.getBean(UsedHardwareBean.class).getUsedIDImages();
        this.src.add("__ledMatrix", this.imageList.get(ledMatrixImage.getProperty().getBlocklyId()));
        return null;
    }

    @Override
    public Void visitLEDMatrixImageShiftFunction(LEDMatrixImageShiftFunction ledMatrixImageShiftFunction) {
        this.src.add("(shiftLEDMatrix");
        this.src.add(capitalizeFirstLetter(ledMatrixImageShiftFunction.shiftDirection.toString()));
        this.src.add("Vec(");
        ledMatrixImageShiftFunction.image.accept(this);

        this.src.add(", ");
        ledMatrixImageShiftFunction.positions.accept(this);
        this.src.add("))");
        return null;
    }

    @Override
    public Void visitLEDMatrixImageInvertFunction(LEDMatrixImageInvertFunction ledMatrixImageInverFunction) {
        this.src.add("(invertLEDMatrixVec(");
        ledMatrixImageInverFunction.image.accept(this);

        this.src.add("))");
        return null;
    }

    @Override
    public Void visitLEDMatrixSetBrightnessAction(LEDMatrixSetBrightnessAction ledMatrixSetBrightnessAction) {
        this.src.add("__meLEDMatrix_", ledMatrixSetBrightnessAction.port, ".setBrightness(");
        ledMatrixSetBrightnessAction.brightness.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitSendIRAction(SendIRAction sendIRAction) {
        this.src.add("_meIr.sendString(");
        sendIRAction.message.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitReceiveIRAction(ReceiveIRAction receiveIRAction) {
        this.src.add("_meIr.getString()");
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor lightSensor) {
        this.src.add("_meLight", lightSensor.getUserDefinedPort(), ".read() * ANALOG2PERCENT");
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        this.src.add("(analogRead(PORT_7) < 512)");
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        this.src.add("!__meLineFollower", infraredSensor.getUserDefinedPort(), ".readSensor", infraredSensor.getSlot(), "()");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        this.src.add("_meUltraSensor", ultrasonicSensor.getUserDefinedPort(), ".distanceCm()");
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
        this.src.add("void setup()");
        nlIndent();
        this.src.add("{");
        incrIndentation();
        nlIndent();
        this.src.add("Serial.begin(9600);");
        //      TODO test the following lines when these sensors are available
        //        for ( final UsedSensor usedSensor : this.usedSensors ) {
        //            switch ( usedSensor.getType() ) {
        //                case SC.GYRO:
        //                case SC.ACCELEROMETER:
        //                    nlIndent();
        //                    this.src.add("myGyro", usedSensor.getPort(), ".begin();");
        //                    break;
        //                default:
        //                    break;
        //            }
        //        }
        nlIndent();
        generateUsedVars();
        //      TODO test the following lines when these sensors are available
        //        this.src.add("\n}");
        //        decrIndentation();
        //        for ( final UsedSensor usedSensor : this.usedSensors ) {
        //            switch ( usedSensor.getType() ) {
        //                case SC.GYRO:
        //                    nlIndent();
        //                    this.src.add("myGyro", usedSensor.getPort(), ".update();");
        //                    break;
        //                case SC.ACCELEROMETER:
        //                    nlIndent();
        //                    this.src.add("myGyro", usedSensor.getPort(), ".update();");
        //                    break;
        //                case SC.TEMPERATURE:
        //                    nlIndent();
        //                    this.src.add("myTemp", usedSensor.getPort(), ".update();");
        //                    break;
        //                default:
        //                    break;
        //            }
        //        }
        StringBuilder sb = src.getStringBuilder();
        sb.delete(sb.lastIndexOf("\n"), sb.length());
        decrIndentation();
        nlIndent();
        this.src.add("}");
        nlIndent();
        return null;
    }

    private void predefineImages() {
        Map<String, String[][]> usedIDImages = this.getBean(UsedHardwareBean.class).getUsedIDImages();
        int i = 0;
        for ( Map.Entry<String, String[][]> entry : usedIDImages.entrySet() ) {
            this.src.add("const std::vector<uint8_t> __ledMatrix", i, " = ");
            writeImage(entry.getValue());
            this.src.add(";");
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
        this.src.add("// This file is automatically generated by the Open Roberta Lab.");
        nlIndent();
        nlIndent();
        this.src.add("#define ANALOG2PERCENT 0.0978");
        nlIndent();
        nlIndent();
        this.src.add("#include <MeMCore.h>");
        nlIndent();
        this.src.add("#include <MeDrive.h>");
        nlIndent();
        this.src.add("#include <NEPODefs.h>");
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
                    this.src.add("pinMode(7, INPUT);");
                    break;
                case SC.COLOR:
                    break;
                case SC.ULTRASONIC:
                    nlIndent();
                    this.src.add("MeUltrasonicSensor _meUltraSensor", usedSensor.getPort(), "(PORT_", usedSensor.getPort(), ");");
                    break;
                case SC.MOTION:
                    nlIndent();
                    this.src.add("MePIRMotionSensor _mePir", usedSensor.getPort(), "(", usedSensor.getPort(), ");");
                    break;
                case SC.TEMPERATURE:
                    nlIndent();
                    this.src.add("MeHumiture _meTemp", usedSensor.getPort(), "(", usedSensor.getPort(), ");");
                    break;
                case SC.TOUCH:
                    nlIndent();
                    this.src.add("MeTouchSensor _meTouch", usedSensor.getPort(), "(", usedSensor.getPort(), ");");
                    break;
                case SC.LIGHT:
                    nlIndent();
                    this.src.add("MeLightSensor _meLight", usedSensor.getPort(), "(PORT_", usedSensor.getPort(), ");");
                    break;
                case SC.INFRARED:
                    int port = Integer.parseInt(usedSensor.getPort());
                    if ( !usedInfraredPort.contains(port) ) {
                        nlIndent();
                        this.src.add("MeLineFollower __meLineFollower", usedSensor.getPort(), "(PORT_", usedSensor.getPort(), ");");
                    }
                    usedInfraredPort.add(port);
                    break;
                case SC.COMPASS:
                    break;
                case SC.GYRO:
                case SC.ACCELEROMETER:
                    nlIndent();
                    this.src.add("MeGyro myGyro", usedSensor.getPort(), "(", usedSensor.getPort(), ");");
                    break;
                case SC.SOUND:
                    nlIndent();
                    this.src.add("MeSoundSensor _meSoundSensor", usedSensor.getPort(), "(", usedSensor.getPort(), ");");
                    break;
                case SC.JOYSTICK:
                    nlIndent();
                    this.src.add("MeJoystick _meJoystick", usedSensor.getPort(), "(", usedSensor.getPort(), ");");
                    break;
                case SC.FLAMESENSOR:
                    nlIndent();
                    this.src.add("MeFlameSensor _meFlameSensor", usedSensor.getPort(), "(", usedSensor.getPort(), ");");
                    break;
                case SC.VOLTAGE:
                    nlIndent();
                    this.src.add("MePotentiometer _mePotentiometer", usedSensor.getPort(), "(", usedSensor.getPort(), ");");
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
                    this.src.add("MeRGBLed _meRgbLed(7, 2);");
                    nlIndent();
                    this.src.add("int _v_colour_temp;");
                    break;
                case SC.GEARED_MOTOR:
                    nlIndent();
                    this.src.add("MeDCMotor _meDCmotor", usedActor.getPort(), "(M", usedActor.getPort(), ");");
                    break;
                case SC.DIFFERENTIAL_DRIVE:
                    nlIndent();
                    this.src.add("MeDrive _meDrive(M", this.configuration.getFirstMotorPort(SC.LEFT), ", M", this.configuration.getFirstMotorPort(SC.RIGHT), ");");
                    break;
                case SC.LED_MATRIX:
                    nlIndent();
                    this.src.add("MeLEDMatrix __meLEDMatrix_", usedActor.getPort(), "(", usedActor.getPort(), ");");
                    break;
                case SC.BUZZER:
                    nlIndent();
                    this.src.add("MeBuzzer _meBuzzer;");
                    break;
                case SC.IR_TRANSMITTER:
                    nlIndent();
                    this.src.add("MeIR _meIr;");
                    break;
                case SC.SERIAL:
                    break;
                default:
                    throw new DbcException("Actor is not supported! " + usedActor.getType());
            }
        }
    }

    private void writeImage(String[][] image) {
        this.src.add("{");
        for ( int i = 0; i < 16; i++ ) {
            this.src.add("0x");
            int hex = 0;
            for ( int j = 0; j < 8; j++ ) {
                String pixel = image[i][j].trim();
                if ( pixel.equals("#") ) {
                    hex += Math.pow(2, j);
                }
            }
            this.src.add(Integer.toHexString(hex));
            if ( i < 15 ) {
                this.src.add(", ");
            }
        }
        this.src.add("}");
    }

    private String capitalizeFirstLetter(String original) {
        if ( original == null || original.length() == 0 ) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1).toLowerCase();
    }
}
