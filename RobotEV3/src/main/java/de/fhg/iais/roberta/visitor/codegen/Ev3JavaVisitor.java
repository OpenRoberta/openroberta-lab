package de.fhg.iais.roberta.visitor.codegen;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import static de.fhg.iais.roberta.mode.general.IndexLocation.FROM_END;
import static de.fhg.iais.roberta.mode.general.IndexLocation.FROM_START;
import de.fhg.iais.roberta.mode.general.ListElementOperations;
import static de.fhg.iais.roberta.mode.general.ListElementOperations.GET;
import static de.fhg.iais.roberta.mode.general.ListElementOperations.GET_REMOVE;
import static de.fhg.iais.roberta.mode.general.ListElementOperations.INSERT;
import static de.fhg.iais.roberta.mode.general.ListElementOperations.REMOVE;
import static de.fhg.iais.roberta.mode.general.ListElementOperations.SET;
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
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyList;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.lang.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.lang.functions.IsListEmptyFunct;
import de.fhg.iais.roberta.syntax.lang.functions.LengthOfListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.ListSetIndex;
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
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.MotorDuration;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.IEv3Visitor;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.codegen.utilities.TTSLanguageMapper;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractJavaVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable JAVA code representation of a phrase to a
 * StringBuilder. <b>This representation is correct JAVA code.</b> <br>
 */

public final class Ev3JavaVisitor extends AbstractJavaVisitor implements IEv3Visitor<Void> {
    private static final Logger LOG = LoggerFactory.getLogger(Ev3JavaVisitor.class);

    protected final ConfigurationAst brickConfiguration;

    protected Map<String, String> predefinedImage = new HashMap<>();
    protected ILanguage language;

    /**
     * initialize the Java code generator visitor.
     *
     * @param programName name of the program
     * @param brickConfiguration hardware configuration of the brick
     */
    public Ev3JavaVisitor(
        List<List<Phrase>> programPhrases,
        ConfigurationAst brickConfiguration,
        String programName,
        ILanguage language,
        ClassToInstanceMap<IProjectBean> beans) {
        super(programPhrases, programName, beans);

        this.brickConfiguration = brickConfiguration;
        this.language = language;
        // Picture strings are UTF-16 encoded with extra \0 padding bytes
        initPredefinedImages();

    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        generateImports();

        this.src.add("public class ", this.programName, " {");
        incrIndentation();
        nlIndent();
        this.src.add("private static Configuration brickConfiguration;");
        nlIndent();
        nlIndent();
        this.src.add(generateRegenerateUsedSensors());
        nlIndent();
        if ( !this.getBean(UsedHardwareBean.class).getUsedImages().isEmpty() ) {
            this.src.add("private static Map<String, String> predefinedImages = new HashMap<String, String>();");
            nlIndent();
            nlIndent();
        }

        this.src.add("private Hal hal = new Hal(brickConfiguration, usedSensors);");
        nlIndent();
        generateUserDefinedMethods();
        generateNNStuff("java");
        nlIndent();
        this.src.add("public static void main(String[] args) {");
        incrIndentation();
        nlIndent();
        this.src.add("try {");
        incrIndentation();
        nlIndent();
        generateRegenerateConfiguration();
        nlIndent();

        generateUsedImages();
        nlIndent();
        this.src.add("new ", this.programName, "().run();");
        decrIndentation();
        nlIndent();
        this.src.add("} catch ( Exception e ) {");
        incrIndentation();
        nlIndent();
        this.src.add("Hal.displayExceptionWaitForKeyPress(e);");
        decrIndentation();
        nlIndent();
        this.src.add("}");
        decrIndentation();
        nlIndent();
        this.src.add("}");
    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        if ( withWrapping ) {
            if ( this.isInDebugMode ) {
                nlIndent();
                this.src.add("hal.closeResources();");
                nlIndent();
            }
            decrIndentation();
            nlIndent();
            this.src.add("}");
        }
        decrIndentation();
        nlIndent();
        super.generateProgramSuffix(withWrapping);
    }

    @Override
    public Void visitWaitStmt(WaitStmt waitStmt) {
        this.src.add("while ( true ) {");
        incrIndentation();
        visitStmtList(waitStmt.statements);
        nlIndent();
        this.src.add("hal.waitFor(15);");
        decrIndentation();
        nlIndent();
        this.src.add("}");
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt waitTimeStmt) {
        this.src.add("hal.waitFor(");
        waitTimeStmt.time.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        this.src.add("hal.clearDisplay();");
        return null;
    }

    @Override
    public Void visitGetVolumeAction(GetVolumeAction getVolumeAction) {
        this.src.add("hal.getVolume()");
        return null;
    }

    @Override
    public Void visitSetVolumeAction(SetVolumeAction setVolumeAction) {
        this.src.add("hal.setVolume(");
        setVolumeAction.volume.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitSetLanguageAction(SetLanguageAction setLanguageAction) {
        if ( !this.brickConfiguration.getRobotName().equals("ev3lejosv0") ) {
            this.src.add("hal.setLanguage(\"");
            this.src.add(TTSLanguageMapper.getLanguageString(setLanguageAction.language));
            this.src.add("\");");
        }
        return null;
    }

    @Override
    public Void visitSayTextAction(SayTextAction sayTextAction) {
        if ( !this.brickConfiguration.getRobotName().equals("ev3lejosv0") ) {
            this.src.add("hal.sayText(");
            if ( !sayTextAction.msg.getKind().hasName("STRING_CONST") ) {
                this.src.add("String.valueOf(");
                sayTextAction.msg.accept(this);
                this.src.add(")");
            } else {
                sayTextAction.msg.accept(this);
            }
            this.src.add(");");
        }
        return null;
    }

    @Override
    public Void visitSayTextWithSpeedAndPitchAction(SayTextWithSpeedAndPitchAction sayTextAction) {
        if ( !this.brickConfiguration.getRobotName().equals("ev3lejosv0") ) {
            this.src.add("hal.sayText(");
            if ( !sayTextAction.msg.getKind().hasName("STRING_CONST") ) {
                this.src.add("String.valueOf(");
                sayTextAction.msg.accept(this);
                this.src.add(")");
            } else {
                sayTextAction.msg.accept(this);
            }
            this.src.add(",");
            sayTextAction.speed.accept(this);
            this.src.add(",");
            sayTextAction.pitch.accept(this);
            this.src.add(");");
        }
        return null;
    }

    @Override
    public Void visitBrickLightOnAction(BrickLightOnAction brickLightOnAction) {
        this.src.add("hal.ledOn(", getEnumCode(brickLightOnAction.colour), ", BlinkMode.", brickLightOnAction.mode, ");");
        return null;
    }

    @Override
    public Void visitBrickLightOffAction(BrickLightOffAction brickLightOffAction) {
        switch ( brickLightOffAction.mode ) {
            case "OFF":
                this.src.add("hal.ledOff();");
                break;
            case "RESET":
                this.src.add("hal.resetLED();");
                break;
            default:
                throw new DbcException("Invalid MODE encountered in BrickLightOffAction: " + brickLightOffAction.mode);
        }
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction playFileAction) {
        this.src.add("hal.playFile(", playFileAction.fileName, ");");
        return null;
    }

    @Override
    public Void visitShowPictureAction(ShowPictureAction showPictureAction) {
        this.src.add("hal.drawPicture(predefinedImages.get(\"", showPictureAction.pic, "\"), ");
        showPictureAction.x.accept(this);
        this.src.add(", ");
        showPictureAction.y.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction showTextAction) {
        this.src.add("hal.drawText(");
        if ( !showTextAction.msg.getKind().hasName("STRING_CONST") ) {
            this.src.add("String.valueOf(");
            showTextAction.msg.accept(this);
            this.src.add(")");
        } else {
            showTextAction.msg.accept(this);
        }
        this.src.add(", ");
        showTextAction.x.accept(this);
        this.src.add(", ");
        showTextAction.y.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction toneAction) {
        this.src.add("hal.playTone(");
        toneAction.frequency.accept(this);
        this.src.add(", ");
        toneAction.duration.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        this.src.add("hal.playTone(");
        this.src.add(" (float) ", playNoteAction.frequency);
        this.src.add(", ");
        this.src.add(" (float) ", playNoteAction.duration);
        this.src.add(");");
        return null;
    }

    private boolean isActorOnPort(String port) {
        boolean isActorOnPort = false;
        for ( UsedActor actor : this.getBean(UsedHardwareBean.class).getUsedActors() ) {
            isActorOnPort = isActorOnPort ? isActorOnPort : actor.getPort().equals(port);
        }
        return isActorOnPort;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        String userDefinedPort = motorOnAction.getUserDefinedPort();
        if ( isActorOnPort(userDefinedPort) ) {
            String methodName;
            boolean isRegulated = this.brickConfiguration.isMotorRegulated(userDefinedPort);
            boolean duration = motorOnAction.param.getDuration() != null;
            if ( duration ) {
                methodName = isRegulated ? "hal.rotateRegulatedMotor(" : "hal.rotateUnregulatedMotor(";
            } else {
                methodName = isRegulated ? "hal.turnOnRegulatedMotor(" : "hal.turnOnUnregulatedMotor(";
            }
            this.src.add(methodName, "ActorPort.", userDefinedPort, ", ");
            motorOnAction.param.getSpeed().accept(this);
            if ( duration ) {
                this.src.add(", ", getEnumCode(motorOnAction.getDurationMode()), ", ");
                motorOnAction.getDurationValue().accept(this);
            }
            this.src.add(");");
        }
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction) {
        String userDefinedPort = motorSetPowerAction.getUserDefinedPort();
        if ( isActorOnPort(userDefinedPort) ) {
            boolean isRegulated = this.brickConfiguration.isMotorRegulated(userDefinedPort);
            String methodName = isRegulated ? "hal.setRegulatedMotorSpeed(" : "hal.setUnregulatedMotorSpeed(";
            this.src.add(methodName, "ActorPort.", userDefinedPort, ", ");
            motorSetPowerAction.power.accept(this);
            this.src.add(");");
        }
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction) {
        String userDefinedPort = motorGetPowerAction.getUserDefinedPort();
        if ( isActorOnPort(userDefinedPort) ) {
            boolean isRegulated = this.brickConfiguration.isMotorRegulated(userDefinedPort);
            String methodName = isRegulated ? "hal.getRegulatedMotorSpeed(" : "hal.getUnregulatedMotorSpeed(";
            this.src.add(methodName, "ActorPort.", userDefinedPort, ")");
        }
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        String userDefinedPort = motorStopAction.getUserDefinedPort();
        if ( isActorOnPort(userDefinedPort) ) {
            boolean isRegulated = this.brickConfiguration.isMotorRegulated(userDefinedPort);
            String methodName = isRegulated ? "hal.stopRegulatedMotor(" : "hal.stopUnregulatedMotor(";
            this.src.add(methodName, "ActorPort.", userDefinedPort, ", ", getEnumCode(motorStopAction.mode), ");");
        }
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction driveAction) {
        boolean isDuration = driveAction.param.getDuration() != null;
        String methodName = isDuration ? "hal.driveDistance(" : "hal.regulatedDrive(";
        this.src.add(methodName);
        this.src.add(getEnumCode(driveAction.direction), ", ");
        driveAction.param.getSpeed().accept(this);
        if ( isDuration ) {
            this.src.add(", ");
            driveAction.param.getDuration().getValue().accept(this);
        }
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction curveAction) {
        MotorDuration duration = curveAction.paramLeft.getDuration();

        this.src.add("hal.driveInCurve(");
        this.src.add(getEnumCode(curveAction.direction), ", ");
        curveAction.paramLeft.getSpeed().accept(this);
        this.src.add(", ");
        curveAction.paramRight.getSpeed().accept(this);
        if ( duration != null ) {
            this.src.add(", ");
            duration.getValue().accept(this);
        }
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction turnAction) {
        boolean isDuration = turnAction.param.getDuration() != null;
        String methodName = "hal.rotateDirection" + (isDuration ? "Angle" : "Regulated") + "(";
        this.src.add(methodName);
        this.src.add(getEnumCode(turnAction.direction), ", ");
        turnAction.param.getSpeed().accept(this);
        if ( isDuration ) {
            this.src.add(", ");
            turnAction.param.getDuration().getValue().accept(this);
        }
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction stopAction) {
        this.src.add("hal.stopRegulatedDrive();");
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        String brickSensorPort = "BrickKey." + keysSensor.getUserDefinedPort();
        switch ( keysSensor.getMode() ) {
            case SC.PRESSED:
                this.src.add("hal.isPressed(", brickSensorPort, ")");
                break;
            case SC.WAIT_FOR_PRESS_AND_RELEASE:
                this.src.add("hal.isPressedAndReleased(", brickSensorPort, ")");
                break;
            default:
                throw new DbcException("Invalide mode for KeysSensor!");
        }
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor colorSensor) {
        String port = "SensorPort.S" + colorSensor.getUserDefinedPort();
        String mode = colorSensor.getMode();
        String methodName;
        switch ( mode ) {
            case SC.COLOUR:
                methodName = "hal.getColorSensorColour";
                break;
            case SC.LIGHT:
                methodName = "hal.getColorSensorRed";
                break;
            case SC.AMBIENTLIGHT:
                methodName = "hal.getColorSensorAmbient";
                break;
            case SC.RGB:
                methodName = "hal.getColorSensorRgb";
                break;
            default:
                throw new DbcException("Invalid mode for EV3 Color Sensor!");
        }
        this.src.add(methodName, "(", port, ")");
        return null;
    }

    @Override
    public Void visitHTColorSensor(HTColorSensor htColorSensor) {
        String port = "SensorPort.S" + htColorSensor.getUserDefinedPort();
        String mode = htColorSensor.getMode();
        String methodName;
        switch ( mode ) {
            case SC.COLOUR:
                methodName = "hal.getHiTecColorSensorV2Colour";
                break;
            case SC.LIGHT:
                methodName = "hal.getHiTecColorSensorV2Light";
                break;
            case SC.AMBIENTLIGHT:
                methodName = "hal.getHiTecColorSensorV2Ambient";
                break;
            case SC.RGB:
                methodName = "hal.getHiTecColorSensorV2Rgb";
                break;
            default:
                throw new DbcException("Invalid mode for EV3 Color Sensor!");
        }
        this.src.add(methodName, "(", port, ")");
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor encoderSensor) {
        String encoderMotorPort = encoderSensor.getUserDefinedPort();
        boolean isRegulated = this.brickConfiguration.isMotorRegulated(encoderMotorPort);
        String methodName = isRegulated ? "hal.getRegulatedMotorTachoValue(" : "hal.getUnregulatedMotorTachoValue(";
        this.src.add(methodName, "ActorPort.", encoderMotorPort, ", MotorTachoMode.", encoderSensor.getMode(), ")");
        return null;
    }

    @Override
    public Void visitEncoderReset(EncoderReset encoderReset) {
        String encoderMotorPort = encoderReset.sensorPort;
        boolean isRegulated = this.brickConfiguration.isMotorRegulated(encoderMotorPort);
        String methodName = isRegulated ? "hal.resetRegulatedMotorTacho(" : "hal.resetUnregulatedMotorTacho(";
        this.src.add(methodName, "ActorPort.", encoderMotorPort, ");");
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor gyroSensor) {
        String gyroSensorPort = "SensorPort.S" + gyroSensor.getUserDefinedPort();
        switch ( gyroSensor.getMode() ) {
            case SC.ANGLE:
                this.src.add("hal.getGyroSensorAngle(", gyroSensorPort, ")");
                break;
            case SC.RATE:
                this.src.add("hal.getGyroSensorRate(", gyroSensorPort, ")");
                break;
            default:
                throw new DbcException("Invalid GyroSensorMode");
        }
        return null;
    }

    @Override
    public Void visitGyroReset(GyroReset gyroReset) {
        String gyroSensorPort = "SensorPort.S" + gyroReset.sensorPort;
        this.src.add("hal.resetGyroSensor(", gyroSensorPort, ");");
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        String infraredSensorPort = "SensorPort.S" + infraredSensor.getUserDefinedPort();
        switch ( infraredSensor.getMode() ) {
            case SC.DISTANCE:
                this.src.add("hal.getInfraredSensorDistance(", infraredSensorPort, ")");
                break;
            case SC.PRESENCE:
                this.src.add("hal.getInfraredSensorSeek(", infraredSensorPort, ")");
                break;
            default:
                throw new DbcException("Invalid Infrared Sensor Mode: " + infraredSensor.getMode());
        }
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        String timerNumber = timerSensor.getUserDefinedPort();
        this.src.add("hal.getTimerValue(", timerNumber, ")");
        return null;
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        String timerNumber = timerReset.sensorPort;
        this.src.add("hal.resetTimer(", timerNumber, ");");
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor touchSensor) {
        this.src.add("hal.isPressed(", "SensorPort.S", touchSensor.getUserDefinedPort(), ")");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        String ultrasonicSensorPort = "SensorPort.S" + ultrasonicSensor.getUserDefinedPort();
        if ( ultrasonicSensor.getMode().equals(SC.DISTANCE) ) {
            this.src.add("hal.getUltraSonicSensorDistance(", ultrasonicSensorPort, ")");
        } else {
            this.src.add("hal.getUltraSonicSensorPresence(", ultrasonicSensorPort, ")");
        }
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor soundSensor) {
        this.src.add("hal.getSoundLevel(", "SensorPort.S", soundSensor.getUserDefinedPort(), ")");
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor compassSensor) {
        String compassSensorPort = "SensorPort.S" + compassSensor.getUserDefinedPort();
        switch ( compassSensor.getMode() ) {
            case SC.ANGLE:
                this.src.add("hal.getHiTecCompassAngle(", compassSensorPort, ")");
                break;
            case SC.COMPASS:
                this.src.add("hal.getHiTecCompassCompass(", compassSensorPort, ")");
                break;
            default:
                throw new DbcException("Invalid Compass Mode!");
        }
        return null;
    }

    @Override
    public Void visitCompassCalibrate(CompassCalibrate compassCalibrate) {
        String compassSensorPort = "SensorPort.S" + compassCalibrate.getUserDefinedPort();
        this.src.add("hal.hiTecCompassStartCalibration(", compassSensorPort, ");");
        nlIndent();
        this.src.add("hal.waitFor(40000);");
        nlIndent();
        this.src.add("hal.hiTecCompassStopCalibration(", compassSensorPort, ");");
        return null;
    }

    @Override
    public Void visitIRSeekerSensor(IRSeekerSensor irSeekerSensor) {
        String irSeekerSensorPort = "SensorPort.S" + irSeekerSensor.getUserDefinedPort();
        switch ( irSeekerSensor.getMode() ) {
            case SC.MODULATED:
                this.src.add("hal.getHiTecIRSeekerModulated(", irSeekerSensorPort, ")");
                break;
            case SC.UNMODULATED:
                this.src.add("hal.getHiTecIRSeekerUnmodulated(", irSeekerSensorPort, ")");
                break;
            default:
                throw new DbcException("Invalid IRSeeker Mode!");
        }
        return null;
    }

    @Override
    public Void visitMainTask(MainTask mainTask) {
        mainTask.variables.accept(this);
        nlIndent();
        nlIndent();
        this.src.add("public void run() throws Exception {");
        incrIndentation();
        // this is needed for testing
        if ( mainTask.debug.equals("TRUE") ) {
            nlIndent();
            this.src.add("hal.startLogging();");
            this.isInDebugMode = true;
        }
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.VOICE) && !this.brickConfiguration.getRobotName().equals("ev3lejosv0") ) {
            nlIndent();
            this.src.add("hal.setLanguage(\"");
            this.src.add(TTSLanguageMapper.getLanguageString(this.language));
            this.src.add("\");");
        }
        return null;
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct getSubFunct) {
        IndexLocation loc0 = (IndexLocation) getSubFunct.strParam.get(0);
        IndexLocation loc1 = (IndexLocation) getSubFunct.strParam.get(1);

        boolean isLeftAParam = loc0 == FROM_START || loc0 == FROM_END;
        this.src.add("new ArrayList<>(");
        getSubFunct.param.get(0).accept(this);
        this.src.add(".subList(");

        switch ( loc0 ) {
            case FIRST:
                this.src.add("0");
                break;
            case LAST:
                getSubFunct.param.get(0).accept(this);
                this.src.add(".size() - 1");
                break;
            case FROM_START:
                this.src.add("(int) ");
                getSubFunct.param.get(1).accept(this);
                break;
            case FROM_END:
                this.src.add("(");
                getSubFunct.param.get(0).accept(this);
                this.src.add(".size() - 1) - ");
                this.src.add("(int) ");
                getSubFunct.param.get(1).accept(this);
                break;
            case RANDOM:
                throw new DbcException("RANDOM is invalid (has been removed)");
        }

        this.src.add(", ");

        switch ( loc1 ) {
            case FIRST:
                this.src.add("0");
                break;
            case LAST:
                getSubFunct.param.get(0).accept(this);
                this.src.add(".size()");
                break;
            case FROM_START:
                this.src.add("(int) ");
                if ( isLeftAParam ) {
                    getSubFunct.param.get(2).accept(this);
                } else {
                    getSubFunct.param.get(1).accept(this);
                }
                break;
            case FROM_END:
                this.src.add("(");
                getSubFunct.param.get(0).accept(this);
                this.src.add(".size() - 1) - ");
                this.src.add("(int) ");
                if ( isLeftAParam ) {
                    getSubFunct.param.get(2).accept(this);
                } else {
                    getSubFunct.param.get(1).accept(this);
                }
                break;
            case RANDOM:
                throw new DbcException("RANDOM is invalid (has been removed)");
        }
        this.src.add("))");
        return null;

    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct indexOfFunct) {
        indexOfFunct.value.accept(this);
        switch ( (IndexLocation) indexOfFunct.location ) {
            case FIRST:
                this.src.add(".indexOf(");
                break;
            case LAST:
                this.src.add(".lastIndexOf(");
                break;
            default:
                // nothing to do
        }
        if ( indexOfFunct.find.getVarType() == BlocklyType.NUMBER ) {
            this.src.add(" (float) ");
        }
        indexOfFunct.find.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitLengthOfListFunct(LengthOfListFunct lengthOfListFunct) {
        lengthOfListFunct.value.accept(this);
        this.src.add(".size()");
        return null;
    }

    @Override
    public Void visitIsListEmptyFunct(IsListEmptyFunct isListEmptyFunct) {
        isListEmptyFunct.value.accept(this);
        this.src.add(".isEmpty()");
        return null;
    }

    @Override
    public Void visitEmptyList(EmptyList emptyList) {
        this.src.add("new ArrayList<",
            getLanguageVarTypeFromBlocklyType(emptyList.typeVar).substring(0, 1).toUpperCase(),
            getLanguageVarTypeFromBlocklyType(emptyList.typeVar).substring(1).toLowerCase(),
            ">()");
        return null;
    }

    @Override
    public Void visitListCreate(ListCreate listCreate) {
        this.src.add("new ArrayList<>(");

        if ( listCreate.exprList.get().size() > 0 ) {
            this.src.add("Arrays.");
            if ( listCreate.getVarType() == BlocklyType.CONNECTION ) {
                this.src.add("<NXTConnection>");
            } else if ( listCreate.getVarType() == BlocklyType.COLOR ) {
                this.src.add("<PickColor>");
            } else if ( listCreate.getVarType() == BlocklyType.STRING ) {
                this.src.add("<String>");
            } else if ( listCreate.getVarType() == BlocklyType.BOOLEAN ) {
                this.src.add("<Boolean>");
            }

            this.src.add("asList(");
            // manually go through value list to cast numbers to float
            if ( listCreate.getVarType() == BlocklyType.NUMBER ) {
                List<Expr> expressions = listCreate.exprList.get();
                for ( int i = 0; i < expressions.size(); i++ ) {
                    this.src.add("(float) ");
                    expressions.get(i).accept(this);
                    if ( i != expressions.size() - 1 ) {
                        this.src.add(", ");
                    }
                }

            } else {
                listCreate.exprList.accept(this);
            }
            this.src.add(")");
        }

        this.src.add(")");
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex listGetIndex) {
        ListElementOperations op = (ListElementOperations) listGetIndex.getElementOperation();
        IndexLocation loc = (IndexLocation) listGetIndex.location;

        listGetIndex.param.get(0).accept(this);
        if ( op == GET ) {
            this.src.add(".get( (int) (");
        } else if ( op == GET_REMOVE || op == REMOVE ) {
            this.src.add(".remove( (int) (");
        }
        switch ( loc ) {
            case FIRST:
                this.src.add("0");
                break;
            case LAST:
                listGetIndex.param.get(0).accept(this);
                this.src.add(".size() - 1");
                break;
            case FROM_START:
                listGetIndex.param.get(1).accept(this);
                break;
            case FROM_END:
                this.src.add("(");
                listGetIndex.param.get(0).accept(this);
                this.src.add(".size() - 1) - ");
                listGetIndex.param.get(1).accept(this);
                break;
            case RANDOM:
                this.src.add("0 /* absolutely random number */");
                break;
        }
        this.src.add("))");

        // This means its a remove statement and a semicolon is required
        if ( op == REMOVE ) {
            this.src.add(";");
        }
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex listSetIndex) {
        ListElementOperations op = (ListElementOperations) listSetIndex.mode;
        IndexLocation loc = (IndexLocation) listSetIndex.location;

        listSetIndex.param.get(0).accept(this);
        if ( op == SET ) {
            this.src.add(".set(");
        } else if ( op == INSERT ) {
            this.src.add(".add(");
        }
        switch ( loc ) {
            case FIRST:
                this.src.add("0, ");
                break;
            case LAST:
                if ( op == SET ) {
                    this.src.add("(int) (");
                    listSetIndex.param.get(0).accept(this);
                    this.src.add(".size() - 1");
                    this.src.add("), ");
                }
                break;
            case FROM_START:
                this.src.add("(int) (");
                listSetIndex.param.get(2).accept(this);
                this.src.add("), ");
                break;
            case FROM_END:
                this.src.add("(int) (");
                this.src.add("(");
                listSetIndex.param.get(0).accept(this);
                this.src.add(".size() - 1) - ");
                listSetIndex.param.get(2).accept(this);
                this.src.add("), ");
                break;
            case RANDOM:
                this.src.add("0 /* absolutely random number */");
                break;
        }

        if ( listSetIndex.param.get(1).getVarType() == BlocklyType.NUMBER ) {
            this.src.add("(float) ");
        }

        listSetIndex.param.get(1).accept(this);

        this.src.add(");");
        return null;
    }

    @Override
    public Void visitBluetoothReceiveAction(BluetoothReceiveAction bluetoothReadAction) {
        this.src.add("hal.readMessage(");
        bluetoothReadAction.connection.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitBluetoothConnectAction(BluetoothConnectAction bluetoothConnectAction) {
        this.src.add("hal.establishConnectionTo(");
        if ( !bluetoothConnectAction.address.getKind().hasName("STRING_CONST") ) {
            this.src.add("String.valueOf(");
            bluetoothConnectAction.address.accept(this);
            this.src.add(")");
        } else {
            bluetoothConnectAction.address.accept(this);
        }
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitBluetoothSendAction(BluetoothSendAction bluetoothSendAction) {
        this.src.add("hal.sendMessage(");
        if ( !bluetoothSendAction.msg.getKind().hasName("STRING_CONST") ) {
            this.src.add("String.valueOf(");
            bluetoothSendAction.msg.accept(this);
            this.src.add(")");
        } else {
            bluetoothSendAction.msg.accept(this);
        }
        this.src.add(", ");
        bluetoothSendAction.connection.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction bluetoothWaitForConnection) {
        this.src.add("hal.waitForConnection()");
        return null;
    }

    @Override
    public Void visitBluetoothCheckConnectAction(BluetoothCheckConnectAction bluetoothCheckConnectAction) {
        return null;
    }

    private void generateRegenerateConfiguration() {
        this.src.add(" brickConfiguration = new EV3Configuration.Builder()");
        incrIndentation();
        nlIndent();
        this.src.add(".setWheelDiameter(", this.brickConfiguration.getWheelDiameter(), ")");
        nlIndent();
        this.src.add(".setTrackWidth(", this.brickConfiguration.getTrackWidth(), ")");
        nlIndent();
        appendActors();
        appendSensors();
        this.src.add(".build();");
        decrIndentation();
    }

    private void generateUsedImages() {
        for ( String image : this.getBean(UsedHardwareBean.class).getUsedImages() ) {
            this.src.add("predefinedImages.put(\"", image, "\", \"", this.predefinedImage.get(image), "\");");
            nlIndent();
        }
    }

    private void generateImports() {
        this.src.add("package generated.main;");
        nlIndent();
        nlIndent();
        this.src.add("import de.fhg.iais.roberta.runtime.*;");
        nlIndent();
        this.src.add("import de.fhg.iais.roberta.runtime.ev3.*;");
        nlIndent();
        nlIndent();

        this.src.add("import de.fhg.iais.roberta.mode.general.*;");
        nlIndent();
        this.src.add("import de.fhg.iais.roberta.mode.action.*;");
        nlIndent();
        this.src.add("import de.fhg.iais.roberta.mode.sensor.*;");
        nlIndent();
        this.src.add("import de.fhg.iais.roberta.mode.action.ev3.*;");
        nlIndent();
        this.src.add("import de.fhg.iais.roberta.mode.sensor.ev3.*;");
        nlIndent();
        nlIndent();

        this.src.add("import de.fhg.iais.roberta.components.*;");
        nlIndent();
        nlIndent();

        this.src.add("import java.util.LinkedHashSet;");
        nlIndent();
        this.src.add("import java.util.HashMap;");
        nlIndent();
        this.src.add("import java.util.Set;");
        nlIndent();
        this.src.add("import java.util.Map;");
        nlIndent();
        this.src.add("import java.util.List;");
        nlIndent();
        this.src.add("import java.util.ArrayList;");
        nlIndent();
        this.src.add("import java.util.Arrays;");
        nlIndent();
        this.src.add("import java.util.Collections;");
        nlIndent();
        nlIndent();

        this.src.add("import lejos.remote.nxt.NXTConnection;");
        nlIndent();
        nlIndent();
    }

    private void appendSensors() {
        for ( ConfigurationComponent sensor : this.brickConfiguration.getSensors() ) {
            boolean isUsed = false;
            for ( UsedSensor usedSensor : this.getBean(UsedHardwareBean.class).getUsedSensors() ) {
                if ( sensor.componentType.contains(usedSensor.getType()) && usedSensor.getPort().equals(sensor.userDefinedPortName) ) {
                    isUsed = true;
                }
            }
            if ( isUsed ) {
                this.src.add(".addSensor(");
                this.src.add("SensorPort.S", sensor.userDefinedPortName, ", ");
                this.src.add(generateRegenerateSensor(sensor));
                this.src.add(")");
                nlIndent();
            }
        }
    }

    private void appendActors() {
        for ( ConfigurationComponent actor : this.brickConfiguration.getActors() ) {
            boolean isUsed = false;
            for ( UsedActor usedActor : this.getBean(UsedHardwareBean.class).getUsedActors() ) {
                if ( usedActor.getType().equals(actor.componentType) && usedActor.getPort().equals(actor.userDefinedPortName) ) {
                    isUsed = true;
                }
            }
            if ( isUsed ) {
                this.src.add(".addActor(ActorPort.", actor.userDefinedPortName, ", ");
                this.src.add(generateRegenerateActor(actor));
                this.src.add(")");
                nlIndent();
            }
        }
    }

    private String generateRegenerateUsedSensors() {
        StringBuilder sb = new StringBuilder();
        String arrayOfSensors = "";
        for ( UsedSensor usedSensor : this.getBean(UsedHardwareBean.class).getUsedSensors() ) {
            if ( !usedSensor.getType().equals(SC.TIMER) ) { // TODO this should be handled differently
                arrayOfSensors += generateRegenerateUsedSensor(usedSensor);
                arrayOfSensors += ", ";
            }
        }

        sb.append("private Set<UsedSensor> usedSensors = " + "new LinkedHashSet<UsedSensor>(");
        if ( !this.getBean(UsedHardwareBean.class).getUsedSensors().isEmpty() ) {
            if ( arrayOfSensors.length() >= 2 ) {
                sb.append("Arrays.asList(").append(arrayOfSensors, 0, arrayOfSensors.length() - 2).append(")");
            }
        }
        sb.append(");");
        return sb.toString();
    }

    private String generateRegenerateUsedSensor(UsedSensor usedSensor) {
        StringBuilder sb = new StringBuilder();
        ConfigurationComponent sensor = this.brickConfiguration.getConfigurationComponent(usedSensor.getPort());

        String mode = usedSensor.getMode();
        String componentType = sensor.componentType;

        sb.append("new UsedSensor(");
        sb.append("SensorPort.S" + usedSensor.getPort()).append(", ");
        sb.append("SensorType." + componentType).append(", ");
        sb.append(getOldModeClass(componentType, mode)).append(")");
        return sb.toString();
    }

    private String getOldModeClass(String sensorType, String sensorMode) {
        switch ( sensorType ) {
            case SC.TOUCH:
                if ( sensorMode.equals("PRESSED") || sensorMode.equals("DEFAULT") ) {
                    return "TouchSensorMode.TOUCH";
                } else {
                    return "TouchSensorMode." + sensorMode;
                }

            case SC.COLOR:
            case SC.COLOUR:
                if ( sensorMode.equals(SC.LIGHT) ) {
                    sensorMode = SC.RED;
                }
                return "ColorSensorMode." + sensorMode;
            case SC.ULTRASONIC:
                return "UltrasonicSensorMode." + sensorMode;
            case SC.INFRARED:
                return "InfraredSensorMode." + sensorMode;
            case SC.GYRO:
                return "GyroSensorMode." + sensorMode;
            case SC.COMPASS:
                return "CompassSensorMode." + sensorMode;
            case SC.IRSEEKER:
                return "IRSeekerSensorMode." + sensorMode;
            case SC.SOUND:
                return "SoundSensorMode." + sensorMode;
            case SC.HT_COLOR:
                return "HiTecColorSensorV2Mode." + sensorMode;
            default:
                throw new DbcException("There is mapping missing for " + sensorType + " with the old enums!");
        }
    }

    private String generateRegenerateActor(ConfigurationComponent actor) {
        StringBuilder sb = new StringBuilder();
        String driveDirection;
        if ( actor.componentType.equals("OTHER") ) {
            driveDirection = "FOREWARD";
        } else {
            driveDirection = actor.getProperty(SC.MOTOR_REVERSE).equals(SC.OFF) ? "FOREWARD" : "BACKWARD";
        }
        sb.append("new Actor(ActorType.").append(actor.componentType);
        if ( actor.componentType.equals("OTHER") ) {
            sb.append(", false");
        } else {
            sb.append(", ").append(actor.getProperty(SC.MOTOR_REGULATION).toLowerCase());
        }
        sb.append(", DriveDirection.").append(driveDirection);
        if ( actor.componentType.equals("MEDIUM") || actor.componentType.equals("OTHER") ) {
            sb.append(", MotorSide.NONE)");
        } else {
            sb.append(", MotorSide.").append(actor.getProperty(SC.MOTOR_DRIVE)).append(")");
        }
        return sb.toString();
    }

    private String generateRegenerateSensor(ConfigurationComponent sensor) {
        StringBuilder sb = new StringBuilder();
        sb.append("new Sensor(SensorType.");
        if ( sensor.componentType.equals(SC.COLOUR) ) {
            sb.append(SC.COLOR);
        } else {
            sb.append(sensor.componentType);
        }
        sb.append(")");
        return sb.toString();
    }

    private void initPredefinedImages() {
        this.predefinedImage
            .put(
                "OLDGLASSES",
                "\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u0007\\u00fc\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u0001\\u00f0\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u003f\\u00f0\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u0000\\u00f0\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u000f\\u00c0\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u00f0\\u003f\\u0000\\u00e0\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u0003\\u00c0\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u00f8\\u001f\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u00fc\\u000f\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u00e0\\u007f\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0007\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u00f0\\u003f\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u00ff\\u0003\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u00f8\\u001f\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0080\\u00ff\\u0001\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u00f8\\u000f\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u00c0\\u00ff\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u00fc\\u0007\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u00c0\\u00ff\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u00fe\\u0003\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u00c0\\u007f\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u00fe\\u0001\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u00e0\\u003f\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u00ff\\u0001\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u00f0\\u003f\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u00ff\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u00f0\\u001f\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0080\\u00ff\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u001f\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0080\\u007f\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u000f\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u00c0\\u007f\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u00c0\\u003f\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u00fc\\u0007\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u00c0\\u003f\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u00fc\\u0007\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u00e0\\u001f\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u00e0\\u001f\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u00e0\\u001f\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u00fe\\u0003\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u00e0\\u001f\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0080\\u0001\\u00fe\\u0003\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u00e0\\u001f\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00f0\\u000f\\u00ff\\u0003\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u00f0\\u001f\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00f8\\u001f\\u00ff\\u0003\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00f0\\u003f\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u003e\\u007c\\u00ff\\u0007\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00f0\\u003f\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u00f8\\u00ff\\u0007\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00f0\\u00ff\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u00f0\\u00ff\\u001f\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00e3\\u00c7\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00fb\\u00df\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000");
        this.predefinedImage
            .put(
                "EYESOPEN",
                "\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0040\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00b0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00b0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00b0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00d8\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00d8\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00d8\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00d8\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00d8\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00d8\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00cc\\u0060\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u00cc\\u007c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u0003\\u0000\\u0000\\u0000\\u00cc\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u008c\\u0037\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u000c\\u0018\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u000c\\u0018\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u0000\\u0000\\u000c\\u001c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u0000\\u0000\\u0006\\u000c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u007f\\u0000\\u0000\\u0006\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u003f\\u0000\\u0000\\u0006\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u001e\\u0000\\u0000\\u0006\\u0007\\u0000\\u0000\\u0000\\u0000\\u0080\\u0000\\u0000\\u0000\\u00fc\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0006\\u0003\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0001\\u0000\\u0000\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0006\\u0003\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0003\\u0000\\u00c0\\u003f\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0083\\u0001\\u0000\\u0000\\u0000\\u0000\\u0060\\u0003\\u0000\\u00f0\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0083\\u0001\\u0000\\u0000\\u0000\\u0000\\u0060\\u0003\\u0000\\u00f8\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c3\\u0000\\u0000\\u0000\\u0000\\u0000\\u0020\\u0003\\u0000\\u00fc\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c3\\u0018\\u0000\\u0000\\u0000\\u0018\\u0030\\u0003\\u0000\\u00fe\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u001f\\u0000\\u0000\\u0000\\u0000\\u00c3\\u003f\\u0000\\u0000\\u0000\\u00fc\\u0030\\u0003\\u0080\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0083\\u001f\\u0000\\u0000\\u0000\\u00ee\\u003b\\u0003\\u00c0\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0080\\u0001\\u000e\\u0000\\u0000\\u0000\\u0086\\u001f\\u0003\\u00e0\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0007\\u00e0\\u001f\\u0000\\u0000\\u0080\\u0001\\u0007\\u0000\\u0000\\u0000\\u0006\\u001e\\u0003\\u00f0\\u000f\\u0000\\u0000\\u00fe\\u0007\\u0000\\u0000\\u00fc\\u0000\\u0000\\u003f\\u0000\\u0000\\u0080\\u0081\\u0003\\u0000\\u0000\\u0000\\u000c\\u0000\\u0003\\u00f8\\u0007\\u0000\\u00e0\\u00ff\\u007f\\u0000\\u0000\\u003f\\u0000\\u0000\\u00fc\\u0000\\u00c0\\u009f\\u00c1\\u0001\\u0000\\u0000\\u0000\\u000c\\u0000\\u0003\\u00f8\\u0007\\u0000\\u00fc\\u00ff\\u00ff\\u0003\\u0080\\u000f\\u0000\\u0000\\u00f0\\u0081\\u00ff\\u009f\\u00e1\\u0000\\u0000\\u0000\\u0000\\u0018\\u0000\\u0003\\u00e0\\u0003\\u0000\\u00ff\\u0001\\u00f8\\u000f\\u00c0\\u0007\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u009f\\u0071\\u0000\\u0000\\u0000\\u0000\\u0018\\u0000\\u0003\\u00c0\\u0003\\u0080\\u001f\\u0000\\u0080\\u001f\\u00e0\\u0001\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u009f\\u003b\\u0000\\u0000\\u0000\\u0000\\u0030\\u0000\\u0003\\u00c0\\u0001\\u00e0\\u0007\\u0000\\u0000\\u007e\\u00f0\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u001f\\u001f\\u0000\\u0000\\u0000\\u0000\\u0060\\u0000\\u0003\\u0000\\u0000\\u00f0\\u0001\\u0000\\u0000\\u00f8\\u0070\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u001f\\u000c\\u0000\\u0000\\u0000\\u0000\\u0060\\u0000\\u0003\\u0000\\u0000\\u00f8\\u0000\\u0000\\u0000\\u00f0\\u0079\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0000\\u0003\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u00c0\\u003f\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0001\\u0003\\u0000\\u0000\\u001e\\u0000\\u0000\\u0000\\u0080\\u001f\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0003\\u0003\\u0000\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u001f\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0003\\u0003\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0006\\u0003\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u007e\\u000c\\u0003\\u0000\\u00c0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f6\\r\\u0003\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u001f\\u0000\\u00e0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c6\\u000f\\u0003\\u0000\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u00c0\\u007f\\u0000\\u00e0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0006\\u0003\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u0080\\u0003\\u0000\\u00e0\\u00c3\\u0000\\u00c0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u000c\\u0000\\u0007\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u0080\\u0003\\u0000\\u00f0\\u0081\\u0001\\u00c0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0018\\u0000\\u0007\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u0080\\u0003\\u0000\\u00f0\\u0081\\u0001\\u00c0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0018\\u0000\\u0006\\u0000\\u0038\\u0000\\u0000\\u0000\\u003e\\u0080\\u0003\\u0000\\u00f8\\u0081\\u0003\\u00c0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0030\\u0000\\u0006\\u0000\\u0038\\u0000\\u0000\\u0080\\u00ff\\u0080\\u0003\\u0000\\u00f8\\u0081\\u0003\\u00c0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0030\\u0000\\u0006\\u0000\\u0038\\u0000\\u0000\\u00e0\\u00ff\\u0083\\u0003\\u0000\\u00f8\\u00c3\\u0003\\u00c0\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0060\\u0000\\u0006\\u0000\\u003f\\u0000\\u0000\\u00e0\\u000f\\u0083\\u0003\\u0000\\u00f8\\u00ff\\u0003\\u00c0\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0000\\u0006\\u00f0\\u001f\\u0000\\u0000\\u00f0\\u0007\\u0086\\u0003\\u0000\\u00f8\\u00ff\\u0003\\u00c0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0001\\u0006\\u00ff\\u001f\\u0000\\u0000\\u00f0\\u0007\\u0086\\u0003\\u0000\\u00f0\\u00ff\\u0001\\u00c0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0001\\u00fe\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u0007\\u008e\\u0003\\u0000\\u00f0\\u00ff\\u0001\\u00c0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0003\\u00fe\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u0007\\u000e\\u0007\\u0000\\u00e0\\u00ff\\u0000\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u00fe\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u000f\\u000f\\u0007\\u0000\\u00c0\\u007f\\u0000\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0006\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u00ff\\u000f\\u0007\\u0000\\u0000\\u001f\\u0000\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u00ff\\u000f\\u000f\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003c\\u00fb\\u00ff\\u001f\\u0000\\u0000\\u00f0\\u00ff\\u0007\\u000e\\u0000\\u0000\\u0000\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00fb\\u00ff\\u001f\\u0000\\u0000\\u00f0\\u00ff\\u0007\\u001e\\u0000\\u0000\\u0000\\u0000\\u0078\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00f9\\u00ff\\u001f\\u0000\\u0000\\u00e0\\u00ff\\u0003\\u001c\\u0000\\u0000\\u0000\\u0000\\u0038\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u00e0\\u00ff\\u0003\\u003c\\u0000\\u0000\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u0080\\u00ff\\u0000\\u0078\\u0000\\u0000\\u0000\\u0000\\u001e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u003f\\u0000\\u0000\\u0000\\u003e\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0007\\u0000\\u0000\\u00e0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u000f\\u0000\\u0000\\u00f0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u003f\\u0000\\u0000\\u00fc\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u0000\\u0000\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u0007\\u00e0\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00c3\\u0003\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u001f\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0001\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00fb\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u001e\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0001\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0007\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u001f\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u0001\\u00f8\\u00ef\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00f3\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u007f\\u00f0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0007\\u00f0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000");
        this.predefinedImage
            .put(
                "EYESCLOSED",
                "\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0038\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0000\\u0000\\u0000\\u00f0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0001\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u0001\\u0000\\u0000\\u0078\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u0001\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u0001\\u0000\\u0000\\u001c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0030\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0007\\u00e0\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0007\\u0000\\u0000\\u00fc\\u0000\\u0000\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u007f\\u0000\\u0000\\u003f\\u0000\\u0000\\u00fc\\u0000\\u00c0\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u0003\\u0080\\u000f\\u0000\\u0000\\u00f0\\u0081\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u0001\\u00f8\\u000f\\u00c0\\u0007\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u001f\\u0000\\u0080\\u001f\\u00e0\\u0001\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0007\\u0000\\u0000\\u007e\\u00f0\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0001\\u0000\\u0000\\u00f8\\u0070\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0000\\u0000\\u0000\\u00f0\\u0079\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u00c0\\u003f\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u001e\\u0000\\u0000\\u0000\\u0080\\u001f\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u001f\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u003f\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u007f\\u0000\\u00e0\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u007f\\u0000\\u00fe\\u00c3\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u007f\\u00f8\\u001f\\u0080\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u00fc\\u00c3\\u00ff\\u007f\\u00f8\\u0001\\u00c0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u0080\\u0003\\u0000\\u0000\\u0080\\u00ff\\u00c7\\u00ff\\u007f\\u000c\\u0000\\u0060\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0038\\u0000\\u0000\\u0000\\u003e\\u0080\\u0003\\u0000\\u0000\\u00fc\\u00ff\\u00cf\\u00ff\\u007f\\u000c\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0038\\u0000\\u0000\\u0080\\u00ff\\u0080\\u0003\\u0000\\u00c0\\u00ff\\u00ff\\u00cf\\u00ff\\u007f\\u000c\\u0000\\u0038\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0038\\u0000\\u0000\\u00e0\\u00ff\\u0083\\u0003\\u0000\\u00fe\\u00ff\\u00ff\\u00c7\\u00ff\\u0007\\u0006\\u0000\\u001c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003f\\u0000\\u0000\\u00e0\\u000f\\u0083\\u0003\\u00c0\\u00ff\\u00ff\\u00ff\\u00c1\\u003f\\u0000\\u0006\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u001f\\u0000\\u0000\\u00f0\\u0007\\u0086\\u0003\\u00fe\\u00ff\\u00ff\\u0003\\u00c0\\u0001\\u0000\\u001e\\u0000\\u00fe\\u0003\\u0000\\u0000\\u0000\\u0000\\u00ff\\u001f\\u0000\\u0000\\u00f0\\u0007\\u0086\\u0083\\u00ff\\u007f\\u0000\\u0000\\u00c0\\u0001\\u0000\\u003c\\u0000\\u00fe\\u0003\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u0007\\u008e\\u00c3\\u007f\\u0000\\u0000\\u0000\\u00c0\\u0001\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u0007\\u000e\\u00c7\\u0003\\u0000\\u0000\\u0000\\u00e0\\u0000\\u0000\\u00c0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u000f\\u000f\\u0007\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0000\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u00ff\\u000f\\u0007\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u00ff\\u000f\\u000f\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u001f\\u0000\\u0000\\u00f0\\u00ff\\u0007\\u000e\\u0000\\u0000\\u0000\\u0000\\u0070\\u0000\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u001f\\u0000\\u0000\\u00f0\\u00ff\\u0007\\u001e\\u0000\\u0000\\u0000\\u0000\\u0078\\u0000\\u0000\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u001f\\u0000\\u0000\\u00e0\\u00ff\\u0003\\u001c\\u0000\\u0000\\u0000\\u0000\\u0038\\u0000\\u0000\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u00e0\\u00ff\\u0003\\u003c\\u0000\\u0000\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u0080\\u00ff\\u0000\\u0078\\u0000\\u0000\\u0000\\u0000\\u001e\\u0000\\u0000\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u003f\\u0000\\u0000\\u0000\\u003e\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0078\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0007\\u0000\\u0000\\u00e0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0003\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u000f\\u0000\\u0000\\u00f0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u003f\\u0000\\u0000\\u00fc\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u0000\\u0000\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0007\\u00e0\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u0007\\u00e0\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00c1\\u00ff\\u00c3\\u0003\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u00fc\\u00c3\\u001f\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u001c\\u0000\\u00c3\\u0001\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00fb\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0078\\u0000\\u0003\\u0000\\u0000\\u001e\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0000\\u0003\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0000\\u0003\\u0000\\u0000\\u00f8\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0080\\u0001\\u0000\\u0000\\u00f0\\u0001\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0070\\u0080\\u0001\\u0000\\u0000\\u00e0\\u0007\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003c\\u00c0\\u0000\\u0000\\u0000\\u0080\\u001f\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000f\\u00c0\\u0000\\u0000\\u0000\\u0000\\u00ff\\u0001\\u00f8\\u00ef\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0003\\u00e0\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00f3\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u007f\\u00f0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003e\\u0000\\u0038\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0007\\u00f0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u001c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u0001\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0003\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0083\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00c1\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0070\\u0070\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0038\\u003c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u001c\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0087\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c3\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e1\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0078\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000");
        this.predefinedImage
            .put(
                "FLOWERS",
                "\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0003\\u001e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0000\\u0078\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0038\\u0000\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0018\\u0000\\u00c0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000c\\u0000\\u0080\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0006\\u0000\\u0000\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0006\\u0000\\u0000\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0003\\u0000\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0003\\u0000\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0003\\u0000\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0001\\u0003\\u0000\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0080\\u0007\\u000e\\u0003\\u0000\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u00c0\\u0000\\u0010\\u0003\\u0000\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0070\\u0000\\u0060\\u0003\\u0000\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u000f\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0038\\u0000\\u00c0\\u0006\\u0000\\u0000\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u007f\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0018\\u0000\\u0080\\u0006\\u0000\\u0000\\u0003\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00e1\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u000c\\u0000\\u0000\\u000f\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00e7\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0006\\u0000\\u0000\\u00ee\\u001f\\u0080\\u00e1\\u000f\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ef\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0006\\u0000\\u0000\\u00fe\\u00ff\\u00c0\\u00fc\\u007f\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0006\\u0000\\u0000\\u00ff\\u00ff\\u00c3\\u001e\\u00f0\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0003\\u0000\\u0080\\u00ff\\u00ff\\u00ef\\u0007\\u00c0\\u0003\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u00ff\\u0001\\u0000\\u0003\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0007\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00e7\\u00ff\\u000f\\u0000\\u0003\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0006\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00fb\\u00ff\\u003f\\u0000\\u0003\\u0000\\u00f0\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u000c\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0003\\u0000\\u00f8\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u001c\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0003\\u0000\\u00f8\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0018\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0007\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0018\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0006\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0030\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0006\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0030\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u000e\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0030\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u001c\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0030\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u003f\\u0038\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0030\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u003f\\u0078\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0030\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u007f\\u00f0\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0030\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u007f\\u00c0\\u0007\\u00fe\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0018\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u007f\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0018\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u001c\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u000c\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u001f\\u00fc\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0006\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00c0\\u0007\\u00fc\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0007\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00e0\\u0001\\u00f8\\u00ff\\u00ff\\u00ff\\u0007\\u00c0\\u0003\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00e0\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u001f\\u00f0\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u0070\\u0000\\u00f0\\u00ff\\u00ff\\u003f\\u00ff\\u007f\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u0078\\u0000\\u00e0\\u00ff\\u00ff\\u001f\\u00fc\\u000f\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u0038\\u0000\\u00c0\\u00ff\\u00ff\\u000f\\u0018\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u007f\\u0038\\u0000\\u0080\\u00ff\\u00ff\\u0007\\u0030\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u007f\\u001c\\u0000\\u0000\\u00ff\\u00ff\\u0003\\u0070\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u007f\\u001c\\u0000\\u0000\\u00fc\\u00ff\\u0000\\u0060\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u003f\\u001c\\u0000\\u0000\\u00f0\\u001f\\u0000\\u0060\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u003f\\u001c\\u0000\\u0000\\u0070\\u0000\\u0000\\u00c0\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u001f\\u001c\\u0000\\u0000\\u0070\\u0000\\u0000\\u00c0\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u001f\\u001c\\u0000\\u0000\\u0070\\u0000\\u0000\\u00c0\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u000f\\u001c\\u0000\\u0000\\u0070\\u0000\\u0000\\u00c0\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u0007\\u0038\\u0000\\u0000\\u0068\\u0000\\u0000\\u00c0\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u0007\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u0003\\u0038\\u0000\\u0000\\u0068\\u0000\\u0000\\u00c0\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u0007\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u0001\\u0078\\u0000\\u0000\\u006c\\u0000\\u0000\\u00c0\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u000f\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u0000\\u0070\\u0000\\u0000\\u00c4\\u0000\\u0000\\u0060\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u003f\\u0000\\u00e0\\u0000\\u0000\\u00c2\\u0000\\u0000\\u0060\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u000f\\u0000\\u00e0\\u0001\\u0000\\u00c3\\u0001\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u0001\\u0000\\u00c0\\u0007\\u00c0\\u0081\\u0001\\u0000\\u0030\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u0003\\u0000\\u0000\\u0000\\u001f\\u0070\\u0000\\u0003\\u0000\\u0018\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u0007\\u0000\\u0000\\u0000\\u00fe\\u003f\\u0000\\u0007\\u0000\\u001c\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u00f0\\u0007\\u0000\\u001e\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00c0\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0078\\u00c0\\u0003\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u00f0\\u00ff\\u00ff\\u001f\\u0000\\u00e0\\u0007\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u00fe\\u00ff\\u00ff\\u001f\\u0000\\u00f8\\u001f\\u0000\\u0000\\u0000\\u0080\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u00fe\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u00ff\\u00ff\\u0000\\u007e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u007f\\u00f0\\u0080\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0080\\u001f\\u00c0\\u00e1\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0080\\u0007\\u0000\\u00f1\\u00e0\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u00c0\\u0007\\u0000\\u0033\\u0080\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u00c0\\u0003\\u0000\\n\\u0000\\u001e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u00c0\\u0003\\u0000\\n\\u0000\\u001e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u00c0\\u0001\\u0000\\u0004\\u0000\\u003c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u00c0\\u0001\\u0000\\u0004\\u0000\\u003c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u00c0\\u0001\\u0000\\u0000\\u0000\\u0038\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0080\\u0001\\u0000\\u0000\\u0000\\u0038\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00fe\\u00ff\\u00ff\\u00ff\\u003f\\u0080\\u0001\\u0000\\u0000\\u0000\\u0038\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00fe\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0001\\u0000\\u0000\\u0000\\u0038\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u007f\\u00fe\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0003\\u0000\\u0002\\u0000\\u0018\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u007f\\u00fc\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0002\\u0000\\u0006\\u0000\\u001c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u003f\\u00fc\\u00ff\\u00ff\\u00ff\\u001f\\u00c0\\u0007\\u0000\\u0007\\u0000\\u000c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u001f\\u00f8\\u00ff\\u00ff\\u00ff\\u000f\\u00f0\\u0001\\u0000\\u000f\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u000f\\u00f8\\u00ff\\u00ff\\u00ff\\u000f\\u007c\\u0000\\u00c0\\u000f\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u0007\\u00f0\\u00ff\\u00ff\\u00ff\\u0007\\u001e\\u0000\\u00f0\\u003f\\u0080\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u0003\\u00e0\\u00ff\\u00ff\\u00ff\\u0003\\u001e\\u0000\\u00fc\\u00ff\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u0001\\u000f\\u0000\\u00f8\\u00ff\\u0003\\u0038\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u003f\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u0000\\u000f\\u0000\\u00f8\\u00ff\\u0000\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0007\\u0000\\u0000\\u00ff\\u00ff\\u007f\\u0080\\u0007\\u0000\\u00f0\\u00ff\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u001f\\u0080\\u0007\\u0000\\u00f0\\u007f\\u0000\\u00c0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u0007\\u0080\\u0007\\u0000\\u00f0\\u007f\\u0000\\u00c0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u0000\\u0080\\u0007\\u0000\\u00f0\\u003f\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0007\\u0000\\u00f0\\u003f\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u000f\\u0000\\u0038\\u0038\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000f\\u0000\\u0008\\u0020\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u001f\\u0000\\u0000\\u0000\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u001e\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007e\\u0000\\u0001\\u0000\\u0000\\u00c0\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00c1\\u0001\\u0000\\u0000\\u00e0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u0000\\u0000\\u0000\\u00e0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u0000\\u0000\\u0002\\u00f8\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u009f\\u0000\\u0000\\u000e\\u00fe\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0000\\u0000\\u00fe\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0000\\u0000\\u00f2\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0001\\u0000\\u00c3\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0001\\u0000\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0003\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0003\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u000f\\u00e0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u003f\\u00f8\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000");
        this.predefinedImage
            .put(
                "TACHO",
                "\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u007f\\u0009\\u0010\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u0007\\u0009\\u0010\\u00e0\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u0000\\r\\u0010\\u0000\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u001f\\u0000\\u0005\\u0010\\u0000\\u00f9\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0003\\u0000\\u0005\\u0000\\u0000\\u00c9\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u0002\\u0000\\u0005\\u0000\\u0000\\u0005\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u003f\\u0002\\u0000\\u0005\\u0000\\u0080\\u0004\\u007c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u000f\\u0006\\u0000\\u0002\\u0000\\u0080\\u0002\\u00f0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0003\\u0004\\u0000\\u0002\\u0000\\u0080\\u0002\\u00c0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00c1\\u0000\\u0000\\u0002\\u0000\\u0080\\u0001\\u0040\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u00c0\\u0001\\u0000\\u0002\\u0000\\u0040\\u0001\\u0060\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u003f\\u0080\\u0001\\u0000\\u0002\\u0000\\u00c0\\u0000\\u0020\\u001c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u001f\\u0080\\u0003\\u0000\\u0000\\u0000\\u00c0\\u0000\\u0000\\u0078\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u001f\\u0000\\u0007\\u0000\\u0000\\u0000\\u0040\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0013\\u0000\\u0007\\u00cc\\u0003\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0027\\u0000\\n\\u0008\\u00fa\\u00c0\\u0002\\u0000\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u002c\\u0000\\u000e\\u0008\\u008a\\u0080\\u0082\\u000f\\u0000\\u0040\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0068\\u0000\\u0014\\u00e8\\u008b\\u0080\\u0082\\u0008\\u0000\\u0020\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u003f\\u0050\\u0000\\u001c\\u0028\\u0088\\u0080\\u00a2\\u0008\\u0000\\u00d0\\u001c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u001f\\u0060\\u0000\\u002c\\u0028\\u0088\\u0080\\u00be\\u0008\\u0000\\u0038\\u0018\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u001f\\u00c0\\u0000\\u0038\\u00e8\\u00fb\\u0080\\u00a0\\u0008\\u0000\\u000c\\u0038\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u000f\\u00c0\\u0000\\u0058\\u0000\\u0000\\u0080\\u00a0\\u000f\\u0000\\u0003\\u0070\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0007\\u0080\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0001\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0003\\u0000\\u0000\\u00b0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0003\\u0000\\u0000\\u0070\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0006\\u0000\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0000\\u00ec\\u0001\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u000c\\u0060\\u0000\\u00a0\\u0002\\u0000\\u0000\\u0000\\u0028\\u00f8\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007e\\u0018\\u0040\\u00df\\u00c7\\u0003\\u0000\\u0000\\u0000\\u0028\\u0088\\u0000\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u0040\\u0051\\u0044\\u0005\\u0000\\u0000\\u0000\\u00e8\\u008b\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003f\\u0000\\u0040\\u0051\\u00c4\\u000e\\u0000\\u0000\\u0000\\u0028\\u008a\\u0000\\u0000\\u000c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u003f\\u0000\\u0040\\u0051\\u0084\\n\\u0000\\u0000\\u0000\\u0028\\u008a\\u0000\\u0000\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u001f\\u0000\\u0040\\u0051\\u0084\\u0014\\u0000\\u0000\\u0000\\u00e8\\u00fb\\u0000\\u0080\\u0019\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u001f\\u0000\\u0040\\u00df\\u0007\\u0015\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0038\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u000f\\u0000\\u0000\\u0000\\u0000\\u0029\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0030\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u000f\\u0000\\u0000\\u0000\\u0000\\u002b\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0007\\u0000\\u0000\\u0000\\u0000\\u0052\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0060\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0007\\u0000\\u0000\\u0000\\u0000\\u0072\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u000f\\u0000\\u0000\\u0000\\u0000\\u00a4\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0033\\u0000\\u0000\\u0000\\u0000\\u00e4\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0043\\u0000\\u0000\\u0000\\u0000\\u004c\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u008f\\u0001\\u0000\\u0000\\u0000\\u0088\\u0002\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0071\\u00c6\\u0007\\u0000\\u0000\\u0088\\u0002\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0081\\u004f\\u00f4\\u0001\\u0000\\u0010\\u0005\\u0000\\u0000\\u0000\\u00c0\\u003e\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0001\\u0040\\u0014\\u0001\\u0000\\u0010\\u0005\\u0000\\u0000\\u0000\\u0080\\u00a2\\u000f\\u00e0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0000\\u00c0\\u0017\\u0001\\u0000\\u0030\\n\\u0000\\u0000\\u0000\\u0080\\u00a2\\u0008\\u0038\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0000\\u0040\\u0014\\u0001\\u0000\\u0020\\u001a\\u0000\\u0000\\u0000\\u0080\\u00be\\u0008\\u0007\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u0040\\u0014\\u0001\\u0000\\u0020\\u0014\\u0000\\u0000\\u0000\\u0080\\u00a2\\u00e8\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u00c0\\u00f7\\u0001\\u0000\\u0040\\u002c\\u0000\\u0000\\u0000\\u0080\\u00a2\\u0008\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u0000\\u0000\\u0000\\u0000\\u0040\\u0028\\u0000\\u0000\\u0000\\u0080\\u00be\\u000f\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0050\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0050\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00a0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u00a1\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007e\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e1\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0072\\u0070\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u001e\\u00c0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0080\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0006\\u0000\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0003\\u0000\\u0002\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0080\\u0003\\u0000\\u0002\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u0020\\u00f8\\u0000\\u0000\\u0080\\u0003\\u0000\\u0002\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u0000\\u0020\\u0088\\u0000\\u0000\\u0080\\u0003\\u0000\\u0002\\u0000\\u0000\\u0000\\u0000\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u000f\\u00e0\\u008b\\u0000\\u0000\\u0000\\u0003\\u0000\\u0002\\u0000\\u0000\\u0000\\u0000\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00f0\\u0020\\u008a\\u0000\\u0000\\u0000\\u0007\\u0000\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00c0\\u0027\\u008a\\u0000\\u0000\\u0000\\u0007\\u0000\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u003f\\u00e0\\u00fb\\u0000\\u0000\\u0000\\u000e\\u0080\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003e\\u00c0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007c\\u0070\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0002\\u0000\\u0000\\u00c0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0004\\u0000\\n\\u0000\\u0000\\u00c0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0067\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0004\\u0000\\n\\u0000\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u001f\\u0000\\u0002\\u0000\\u0000\\u0000\\u0000\\u0000\\u0024\\u0000\\u0009\\u0000\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0007\\u0000\\u0082\\u000f\\u0000\\u0000\\u0000\\u0000\\u0094\\n\\u0079\\u0000\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u000f\\u0000\\u0082\\u0008\\u0000\\u0000\\u0000\\u0000\\u008c\\u0095\\u0048\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u000f\\u0000\\u00a2\\u0008\\u0000\\u0000\\u0000\\u0000\\u0094\\u0094\\u0048\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u001f\\u0000\\u00be\\u0008\\u0000\\u0000\\u0000\\u0000\\u00a4\\u0054\\u0048\\u0000\\u0000\\u00f8\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u001f\\u0000\\u00a0\\u0008\\u0000\\u0000\\u0000\\u0000\\u0000\\u0040\\u0000\\u0000\\u0000\\u0078\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u003f\\u0000\\u00a0\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007c\\u003e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u003f\\u00f8\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u0067\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u007f\\u0018\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u003e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u001f\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u000f\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0000\\u0000\\u00e0\\u001f\\u00fe\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0003\\u0000\\u0080\\u0001\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0001\\u00f8\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0003\\u0000\\u0000\\u0000\\u0000\\u007c\\u0000\\u00e0\\u0007\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u00be\\u0000\\u0090\\u000f\\u0000\\u0000\\u00a0\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0000\\u003e\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u009f\\u0000\\u0010\\u001e\\u0000\\u0000\\u00a0\\u0008\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0078\\u0080\\u00c1\\u0000\\u000c\\u0000\\u0000\\u0000\\u0000\\u00cf\\u0000\\u0030\\u001e\\u0000\\u0000\\u00be\\u0008\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003c\\u0080\\u0080\\u0000\\u001c\\u0000\\u0000\\u0000\\u0080\\u0087\\u0004\\u0012\\u003c\\u0000\\u0000\\u0082\\u0008\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003c\\u0080\\u0080\\u0000\\u001c\\u0000\\u0000\\u0000\\u00c0\\u00c3\\u009b\\u003d\\u0078\\u0020\\u0000\\u0082\\u0008\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003e\\u0080\\u00be\\u0000\\u0018\\u0000\\u0000\\u0000\\u00c0\\u0083\\u0091\\u0018\\u00f8\\u0010\\u0000\\u00be\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u001f\\u0080\\u0080\\u0000\\u0018\\u0000\\u0000\\u0000\\u00c0\\u00c3\\u0064\\u0032\\u00f8\\u0009\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u001f\\u0080\\u00be\\u0000\\u0018\\u0000\\u0000\\u0000\\u00c0\\u00c1\\u00f1\\u0038\\u00f0\\u0007\\u0000\\u0010\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u001f\\u0080\\u0080\\u0000\\u0038\\u0000\\u0000\\u0000\\u00c0\\u0001\\u00fa\\u0005\\u00f0\\u000f\\u0000\\u0018\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u001f\\u0080\\u00be\\u0000\\u0038\\u0000\\u0000\\u0000\\u00e0\\u0001\\u0064\\u0002\\u00f0\\u001f\\u0000\\u000c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u001f\\u0080\\u0080\\u0000\\u0038\\u0000\\u0000\\u0000\\u00e0\\u0001\\u0060\\u0000\\u00f0\\u003f\\u0000\\n\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u001f\\u0080\\u00be\\u0000\\u0038\\u0000\\u0000\\u0000\\u00e0\\u0001\\u0060\\u0000\\u00f0\\u007f\\u0080\\u0005\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u001f\\u0080\\u0080\\u0000\\u0038\\u0000\\u0000\\u0000\\u00e0\\u0001\\u00fe\\u0007\\u00f0\\u00ff\\u0041\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u001c\\u0080\\u00be\\u0000\\u0018\\u0000\\u0000\\u0000\\u00c0\\u0003\\u0062\\u0004\\u00f8\\u00ff\\u0033\\u0002\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u007f\\u003c\\u0080\\u0080\\u0000\\u001c\\u0000\\u0000\\u0000\\u00c0\\u0003\\u0063\\u000c\\u0088\\u00ff\\u001f\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u001f\\u003c\\u0080\\u00be\\u0000\\u001c\\u0000\\u0000\\u0000\\u00c0\\u0083\\u0003\\u001c\\u0008\\u00ff\\u003f\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u000f\\u003c\\u0080\\u0080\\u0000\\u001c\\u0000\\u0000\\u0000\\u00c0\\u0087\\u0003\\u001c\\u000c\\u00fe\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u0007\\u007c\\u0080\\u00ff\\u0000\\u001e\\u0000\\u0000\\u0000\\u00c0\\u008f\\u00ff\\u001f\\u000e\\u00fc\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u0003\\u00f8\\u0000\\u0000\\u0000\\u000f\\u0000\\u0000\\u0000\\u0080\\u009f\\u0007\\u001e\\u0006\\u00f8\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u0001\\u00f0\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u003f\\u0003\\u000c\\u0003\\u00e0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u00ff\\u007f\\u0000\\u00f0\\u0001\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u00c0\\u0003\\u0080\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u00e0\\u00ff\\u001f\\u0000\\u00e0\\u0003\\u0000\\u00c0\\u0003\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0001\\u00e0\\u0001\\u0000\\u00fe\\u00ff\\u00ff\\u0007\\u00e0\\u00ff\\u00ff\\u0007\\u0000\\u00c0\\u000f\\u0000\\u00f0\\u0001\\u0000\\u0000\\u0000\\u0000\\u00fc\\u000f\\u00fc\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u00c0\\u001f\\u0000\\u00f8\\u0001\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u007f\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u00ff\\u0000\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u001f\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u000f\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u003e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000");
    }

    @Override
    public Void visitColorConst(ColorConst colorConst) {
        String color = "";
        switch ( colorConst.getHexValueAsString().toUpperCase() ) {
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
            case "#585858":
                color = "NONE";
                break;
            default:
                throw new DbcException("Invalid color constant: " + colorConst.getHexValueAsString());
        }
        this.src.add("PickColor.", color);
        return null;
    }

}
