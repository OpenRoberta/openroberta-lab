package de.fhg.iais.roberta.visitor.codegen;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.Category;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
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
import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
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
import de.fhg.iais.roberta.syntax.lang.expr.RgbColor;
import de.fhg.iais.roberta.syntax.lang.expr.Var;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MotionSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.mbot.FlameSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.mbot.Joystick;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.hardware.IMbotVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a hussentation of a phrase to a StringBuilder. <b>This
 * representation is correct C code for Arduino.</b> <br>
 */
public final class MbotCppVisitor extends AbstractCommonArduinoCppVisitor implements IMbotVisitor<Void> {

    private final HashMap<String, Integer> imageList = new HashMap<String, Integer>();

    /**
     * Initialize the C++ code generator visitor.
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param phrases to generate the code from
     */
    public MbotCppVisitor(List<List<Phrase<Void>>> phrases, ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean> beans) {
        super(phrases, brickConfiguration, beans);
    }

    @Override
    public Void visitEmptyExpr(EmptyExpr<Void> emptyExpr) {
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
    protected String getLanguageVarTypeFromBlocklyType(BlocklyType type) {
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
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        this.sb.append("__meLEDMatrix_").append(clearDisplayAction.port).append(".clearScreen();");
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        this.sb.append("_meRgbLed.setColor(");
        this.sb.append(lightAction.getPort());
        if ( lightAction.getRgbLedColor().getClass().equals(Var.class) ) {
            String tempVarName = "___" + ((Var<Void>) lightAction.getRgbLedColor()).getValue();
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
            return null;
        }
        if ( lightAction.getRgbLedColor().getClass().equals(ColorConst.class) ) {
            ColorConst<Void> colorConst = (ColorConst<Void>) lightAction.getRgbLedColor();
            this.sb.append(", ");
            this.sb.append(colorConst.getRedChannelHex());
            this.sb.append(", ");
            this.sb.append(colorConst.getGreenChannelHex());
            this.sb.append(", ");
            this.sb.append(colorConst.getBlueChannelHex());
            this.sb.append(");");
            nlIndent();
            this.sb.append("_meRgbLed.show();");
            return null;
        }
        Map<String, Expr<Void>> Channels = new HashMap<>();
        Channels.put("red", ((RgbColor<Void>) lightAction.getRgbLedColor()).getR());
        Channels.put("green", ((RgbColor<Void>) lightAction.getRgbLedColor()).getG());
        Channels.put("blue", ((RgbColor<Void>) lightAction.getRgbLedColor()).getB());
        Channels.forEach((k, v) -> {
            this.sb.append(", ");
            v.accept(this);
        });
        this.sb.append(");");
        nlIndent();
        this.sb.append("_meRgbLed.show();");
        return null;

    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        this.sb.append("_meRgbLed.setColor(" + lightStatusAction.getUserDefinedPort());
        this.sb.append(", 0, 0, 0);");
        nlIndent();
        this.sb.append("_meRgbLed.show();");
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        //8 - sound port
        this.sb.append("_meBuzzer.tone(8, ");
        toneAction.getFrequency().accept(this);
        this.sb.append(", ");
        toneAction.getDuration().accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        //8 - sound port
        this.sb.append("_meBuzzer.tone(8, ");
        this.sb.append(playNoteAction.getFrequency());
        this.sb.append(", ");
        this.sb.append(playNoteAction.getDuration());
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        final MotorDuration<Void> duration = motorOnAction.getParam().getDuration();
        this.sb.append("_meDCmotor").append(motorOnAction.getUserDefinedPort()).append(".run(");
        if ( !this.configuration.getFirstMotorPort(SC.RIGHT).equals(motorOnAction.getUserDefinedPort()) ) {
            this.sb.append("-1*");
        }
        this.sb.append("(");
        motorOnAction.getParam().getSpeed().accept(this);
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
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        this.sb.append("_meDCmotor").append(motorStopAction.getUserDefinedPort()).append(".stop();");
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        final MotorDuration<Void> duration = driveAction.getParam().getDuration();
        this.sb.append("_meDrive.drive(");
        driveAction.getParam().getSpeed().accept(this);
        this.sb.append(", ");
        this.sb.append(driveAction.getDirection() == DriveDirection.FOREWARD ? 1 : 0);
        if ( duration != null ) {
            this.sb.append(", ");
            duration.getValue().accept(this);
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {
        final MotorDuration<Void> duration = curveAction.getParamLeft().getDuration();
        this.sb.append("_meDrive.steer(");
        curveAction.getParamLeft().getSpeed().accept(this);
        this.sb.append(", ");
        curveAction.getParamRight().getSpeed().accept(this);
        this.sb.append(", ").append(curveAction.getDirection() == DriveDirection.FOREWARD ? 1 : 0);
        if ( duration != null ) {
            this.sb.append(", ");
            duration.getValue().accept(this);
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        final MotorDuration<Void> duration = turnAction.getParam().getDuration();
        this.sb.append("_meDrive.turn(");
        turnAction.getParam().getSpeed().accept(this);
        this.sb.append(", ").append(turnAction.getDirection() == TurnDirection.LEFT ? 1 : 0);
        if ( duration != null ) {
            this.sb.append(", ");
            duration.getValue().accept(this);
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        for ( final UsedActor actor : this.getBean(UsedHardwareBean.class).getUsedActors() ) {
            if ( actor.getType().equals(SC.DIFFERENTIAL_DRIVE) ) {
                this.sb.append("_meDrive.stop();");
                break;
            }
        }
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        this.sb.append("_meLight" + lightSensor.getUserDefinedPort() + ".read() * ANALOG2PERCENT");
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor<Void> keysSensor) {
        this.sb.append("(analogRead(PORT_7) < 512)");
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
        this.sb.append("_meSoundSensor" + soundSensor.getUserDefinedPort() + ".strength()");
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {

        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        this.sb.append("myGyro" + gyroSensor.getUserDefinedPort() + ".getGyro" + gyroSensor.getMode() + "()");
        return null;
    }

    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor<Void> accelerometer) {
        this.sb.append("myGyro" + accelerometer.getUserDefinedPort() + ".getAngle" + accelerometer.getMode().toString() + "()");
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        this.sb.append("!__meLineFollower" + infraredSensor.getUserDefinedPort() + ".readSensor" + infraredSensor.getSlot() + "()");
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        this.sb.append("_meTemp" + temperatureSensor.getUserDefinedPort() + ".getTemperature()");
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        this.sb.append("_meTouch" + touchSensor.getUserDefinedPort() + ".touched()");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        this.sb.append("_meUltraSensor" + ultrasonicSensor.getUserDefinedPort() + ".distanceCm()");
        return null;
    }

    @Override
    public Void visitMotionSensor(MotionSensor<Void> motionSensor) {
        this.sb.append("_mePir" + motionSensor.getUserDefinedPort() + ".isHumanDetected()");
        return null;
    }

    @Override
    public Void visitFlameSensor(FlameSensor<Void> _meFlameSensor) {
        this.sb.append("_meFlameSensor" + _meFlameSensor.getPort() + ".readAnalog()");
        return null;
    }

    @Override
    public Void visitJoystick(Joystick<Void> joystick) {
        this.sb.append("_meJoystick" + joystick.getUserDefinedPort() + ".read" + joystick.getAxis() + "()");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        predefineImages();
        mainTask.getVariables().accept(this);
        if ( !mainTask.getVariables().toString().equals("") ) {
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
        this.sb.append("Serial.begin(9600); ");
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
                default:
                    throw new DbcException("Actor is not supported! " + usedActor.getType());
            }
        }
    }

    @Override
    public Void visitVoltageSensor(VoltageSensor<Void> voltageSensor) {
        //RatedVoltage: 5V
        //Signal type: Analog (range from 0 to 970)
        this.sb.append("_mePotentiometer" + voltageSensor.getUserDefinedPort() + ".read()*5/970");
        return null;
    }

    @Override
    public Void visitSerialWriteAction(SerialWriteAction<Void> serialWriteAction) {
        this.sb.append("Serial.println(");
        serialWriteAction.getValue().accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitSendIRAction(SendIRAction<Void> sendIRAction) {
        this.sb.append("_meIr.sendString(");
        sendIRAction.getMessage().accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitReceiveIRAction(ReceiveIRAction<Void> receiveIRAction) {
        this.sb.append("_meIr.getString()");
        return null;
    }

    @Override
    public Void visitLEDMatrixImageAction(LEDMatrixImageAction<Void> ledMatrixImageAction) {
        String end = ");";
        if ( ledMatrixImageAction.getDisplayImageMode().equals("IMAGE") ) {
            this.sb.append("__meLEDMatrix_").append(ledMatrixImageAction.getPort()).append(".drawBitmap(0, 0, 16, ");
            this.sb.append("&");
            ledMatrixImageAction.getValuesToDisplay().accept(this);
            this.sb.append("[0]");
            this.sb.append(end);
        } else if ( ledMatrixImageAction.getDisplayImageMode().equals("ANIMATION") ) {
            this.sb.append("drawAnimationLEDMatrix(&__meLEDMatrix_").append(ledMatrixImageAction.getPort()).append(", ");
            ledMatrixImageAction.getValuesToDisplay().accept(this);
            this.sb.append(", 200");
            this.sb.append(end);
        } else {
            throw new DbcException("LEDMatrix display mode is not supported: " + ledMatrixImageAction.getDisplayImageMode());
        }
        return null;
    }

    @Override
    public Void visitLEDMatrixTextAction(LEDMatrixTextAction<Void> ledMatrixTextAction) {
        this.sb.append("drawStrLEDMatrix(&__meLEDMatrix_").append(ledMatrixTextAction.getPort()).append(", ");
        if ( !ledMatrixTextAction.getMsg().getVarType().equals(BlocklyType.STRING) ) {
            this.sb.append("String(");
            ledMatrixTextAction.getMsg().accept(this);
            this.sb.append(")");
        } else {
            ledMatrixTextAction.getMsg().accept(this);
        }
        this.sb.append(", 100);");
        return null;
    }

    @Override
    public Void visitLEDMatrixImage(LEDMatrixImage<Void> ledMatrixImage) {
        Map<String, String[][]> usedIDImages = this.getBean(UsedHardwareBean.class).getUsedIDImages();
        this.sb.append("__ledMatrix").append(this.imageList.get(ledMatrixImage.getProperty().getBlocklyId()));
        return null;
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

    @Override
    public Void visitLEDMatrixImageShiftFunction(LEDMatrixImageShiftFunction<Void> ledMatrixImageShiftFunction) {
        this.sb.append("(shiftLEDMatrix");
        this.sb.append(capitalizeFirstLetter(ledMatrixImageShiftFunction.getShiftDirection().toString()));
        this.sb.append("Vec(");
        ledMatrixImageShiftFunction.getImage().accept(this);

        this.sb.append(", ");
        ledMatrixImageShiftFunction.getPositions().accept(this);
        this.sb.append("))");
        return null;
    }

    @Override
    public Void visitLEDMatrixImageInvertFunction(LEDMatrixImageInvertFunction<Void> ledMatrixImageInverFunction) {
        this.sb.append("(invertLEDMatrixVec(");
        ledMatrixImageInverFunction.getImage().accept(this);

        this.sb.append("))");
        return null;
    }

    @Override
    public Void visitLEDMatrixSetBrightnessAction(LEDMatrixSetBrightnessAction<Void> ledMatrixSetBrightnessAction) {
        this.sb.append("__meLEDMatrix_").append(ledMatrixSetBrightnessAction.getPort()).append(".setBrightness(");
        ledMatrixSetBrightnessAction.getBrightness().accept(this);
        this.sb.append(");");
        return null;
    }

    private String capitalizeFirstLetter(String original) {
        if ( original == null || original.length() == 0 ) {
            return original;
        }
        return original.substring(0, 1).toUpperCase() + original.substring(1).toLowerCase();
    }
}
