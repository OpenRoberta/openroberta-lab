package de.fhg.iais.roberta.visitor.codegen;

import static de.fhg.iais.roberta.mode.general.IndexLocation.FROM_END;
import static de.fhg.iais.roberta.mode.general.IndexLocation.FROM_START;
import static de.fhg.iais.roberta.mode.general.ListElementOperations.GET;
import static de.fhg.iais.roberta.mode.general.ListElementOperations.GET_REMOVE;
import static de.fhg.iais.roberta.mode.general.ListElementOperations.INSERT;
import static de.fhg.iais.roberta.mode.general.ListElementOperations.REMOVE;
import static de.fhg.iais.roberta.mode.general.ListElementOperations.SET;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
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
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyList;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.lang.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.lang.functions.LengthOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.HTColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.IRSeekerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.codegen.utilities.TTSLanguageMapper;
import de.fhg.iais.roberta.visitor.hardware.IEv3Visitor;
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
        List<List<Phrase<Void>>> programPhrases,
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

        this.sb.append("public class ").append(this.programName).append(" {");
        incrIndentation();
        nlIndent();
        this.sb.append("private static Configuration brickConfiguration;");
        nlIndent();
        nlIndent();
        this.sb.append(generateRegenerateUsedSensors());
        nlIndent();
        if ( !this.getBean(UsedHardwareBean.class).getUsedImages().isEmpty() ) {
            this.sb.append("private static Map<String, String> predefinedImages = new HashMap<String, String>();");
            nlIndent();
            nlIndent();
        }

        this.sb.append("private Hal hal = new Hal(brickConfiguration, usedSensors);");
        nlIndent();
        generateUserDefinedMethods();
        nlIndent();
        this.sb.append("public static void main(String[] args) {");
        incrIndentation();
        nlIndent();
        this.sb.append("try {");
        incrIndentation();
        nlIndent();
        generateRegenerateConfiguration();
        nlIndent();

        generateUsedImages();
        nlIndent();
        this.sb.append("new ").append(this.programName).append("().run();");
        decrIndentation();
        nlIndent();
        this.sb.append("} catch ( Exception e ) {");
        incrIndentation();
        nlIndent();
        this.sb.append("Hal.displayExceptionWaitForKeyPress(e);");
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        decrIndentation();
        nlIndent();
        this.sb.append("}");
    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        if ( withWrapping ) {
            if ( this.isInDebugMode ) {
                nlIndent();
                this.sb.append("hal.closeResources();");
                nlIndent();
            }
            decrIndentation();
            nlIndent();
            this.sb.append("}");
        }
        decrIndentation();
        nlIndent();
        super.generateProgramSuffix(withWrapping);
    }

    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        this.sb.append("while ( true ) {");
        incrIndentation();
        visitStmtList(waitStmt.getStatements());
        nlIndent();
        this.sb.append("hal.waitFor(15);");
        decrIndentation();
        nlIndent();
        this.sb.append("}");
        return null;
    }

    @Override
    public Void visitConnectConst(ConnectConst<Void> connectConst) {
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        this.sb.append("hal.waitFor(");
        waitTimeStmt.getTime().accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        this.sb.append("hal.clearDisplay();");
        return null;
    }

    @Override
    public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        switch ( volumeAction.getMode() ) {
            case SET:
                this.sb.append("hal.setVolume(");
                volumeAction.getVolume().accept(this);
                this.sb.append(");");
                break;
            case GET:
                this.sb.append("hal.getVolume()");
                break;
            default:
                throw new DbcException("Invalid volume action mode!");
        }
        return null;
    }

    @Override
    public Void visitSetLanguageAction(SetLanguageAction<Void> setLanguageAction) {
        if ( !this.brickConfiguration.getRobotName().equals("ev3lejosv0") ) {
            this.sb.append("hal.setLanguage(\"");
            this.sb.append(TTSLanguageMapper.getLanguageString(setLanguageAction.getLanguage()));
            this.sb.append("\");");
        }
        return null;
    }

    @Override
    public Void visitSayTextAction(SayTextAction<Void> sayTextAction) {
        if ( !this.brickConfiguration.getRobotName().equals("ev3lejosv0") ) {
            this.sb.append("hal.sayText(");
            if ( !sayTextAction.getMsg().getKind().hasName("STRING_CONST") ) {
                this.sb.append("String.valueOf(");
                sayTextAction.getMsg().accept(this);
                this.sb.append(")");
            } else {
                sayTextAction.getMsg().accept(this);
            }
            BlockType emptyBlock = BlockTypeContainer.getByName("EMPTY_EXPR");
            if ( !(sayTextAction.getSpeed().getKind().equals(emptyBlock) && sayTextAction.getPitch().getKind().equals(emptyBlock)) ) {
                this.sb.append(",");
                sayTextAction.getSpeed().accept(this);
                this.sb.append(",");
                sayTextAction.getPitch().accept(this);
            }
            this.sb.append(");");
        }
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        this.sb.append("hal.ledOn(" + getEnumCode(lightAction.getColor()) + ", BlinkMode." + lightAction.getMode() + ");");
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
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
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        this.sb.append("hal.playFile(" + playFileAction.getFileName() + ");");
        return null;
    }

    @Override
    public Void visitShowPictureAction(ShowPictureAction<Void> showPictureAction) {
        this.sb.append("hal.drawPicture(predefinedImages.get(\"" + showPictureAction.getPicture() + "\"), ");
        showPictureAction.getX().accept(this);
        this.sb.append(", ");
        showPictureAction.getY().accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
        this.sb.append("hal.drawText(");
        if ( !showTextAction.getMsg().getKind().hasName("STRING_CONST") ) {
            this.sb.append("String.valueOf(");
            showTextAction.getMsg().accept(this);
            this.sb.append(")");
        } else {
            showTextAction.getMsg().accept(this);
        }
        this.sb.append(", ");
        showTextAction.getX().accept(this);
        this.sb.append(", ");
        showTextAction.getY().accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        this.sb.append("hal.playTone(");
        toneAction.getFrequency().accept(this);
        this.sb.append(", ");
        toneAction.getDuration().accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        this.sb.append("hal.playTone(");
        this.sb.append(" (float) " + playNoteAction.getFrequency());
        this.sb.append(", ");
        this.sb.append(" (float) " + playNoteAction.getDuration());
        this.sb.append(");");
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
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        String userDefinedPort = motorOnAction.getUserDefinedPort();
        if ( isActorOnPort(userDefinedPort) ) {
            String methodName;
            boolean isRegulated = this.brickConfiguration.isMotorRegulated(userDefinedPort);
            boolean duration = motorOnAction.getParam().getDuration() != null;
            if ( duration ) {
                methodName = isRegulated ? "hal.rotateRegulatedMotor(" : "hal.rotateUnregulatedMotor(";
            } else {
                methodName = isRegulated ? "hal.turnOnRegulatedMotor(" : "hal.turnOnUnregulatedMotor(";
            }
            this.sb.append(methodName + "ActorPort." + userDefinedPort + ", ");
            motorOnAction.getParam().getSpeed().accept(this);
            if ( duration ) {
                this.sb.append(", " + getEnumCode(motorOnAction.getDurationMode()));
                this.sb.append(", ");
                motorOnAction.getDurationValue().accept(this);
            }
            this.sb.append(");");
        }
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        String userDefinedPort = motorSetPowerAction.getUserDefinedPort();
        if ( isActorOnPort(userDefinedPort) ) {
            boolean isRegulated = this.brickConfiguration.isMotorRegulated(userDefinedPort);
            String methodName = isRegulated ? "hal.setRegulatedMotorSpeed(" : "hal.setUnregulatedMotorSpeed(";
            this.sb.append(methodName + "ActorPort." + userDefinedPort + ", ");
            motorSetPowerAction.getPower().accept(this);
            this.sb.append(");");
        }
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        String userDefinedPort = motorGetPowerAction.getUserDefinedPort();
        if ( isActorOnPort(userDefinedPort) ) {
            boolean isRegulated = this.brickConfiguration.isMotorRegulated(userDefinedPort);
            String methodName = isRegulated ? "hal.getRegulatedMotorSpeed(" : "hal.getUnregulatedMotorSpeed(";
            this.sb.append(methodName + "ActorPort." + userDefinedPort + ")");
        }
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        String userDefinedPort = motorStopAction.getUserDefinedPort();
        if ( isActorOnPort(userDefinedPort) ) {
            boolean isRegulated = this.brickConfiguration.isMotorRegulated(userDefinedPort);
            String methodName = isRegulated ? "hal.stopRegulatedMotor(" : "hal.stopUnregulatedMotor(";
            this.sb.append(methodName + "ActorPort." + userDefinedPort + ", " + getEnumCode(motorStopAction.getMode()) + ");");
        }
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        boolean isDuration = driveAction.getParam().getDuration() != null;
        String methodName = isDuration ? "hal.driveDistance(" : "hal.regulatedDrive(";
        this.sb.append(methodName);
        this.sb.append(getEnumCode(driveAction.getDirection()) + ", ");
        driveAction.getParam().getSpeed().accept(this);
        if ( isDuration ) {
            this.sb.append(", ");
            driveAction.getParam().getDuration().getValue().accept(this);
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {
        MotorDuration<Void> duration = curveAction.getParamLeft().getDuration();

        this.sb.append("hal.driveInCurve(");
        this.sb.append(getEnumCode(curveAction.getDirection()) + ", ");
        curveAction.getParamLeft().getSpeed().accept(this);
        this.sb.append(", ");
        curveAction.getParamRight().getSpeed().accept(this);
        if ( duration != null ) {
            this.sb.append(", ");
            duration.getValue().accept(this);
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        boolean isDuration = turnAction.getParam().getDuration() != null;
        String methodName = "hal.rotateDirection" + (isDuration ? "Angle" : "Regulated") + "(";
        this.sb.append(methodName);
        this.sb.append(getEnumCode(turnAction.getDirection()) + ", ");
        turnAction.getParam().getSpeed().accept(this);
        if ( isDuration ) {
            this.sb.append(", ");
            turnAction.getParam().getDuration().getValue().accept(this);
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        this.sb.append("hal.stopRegulatedDrive();");
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor<Void> keysSensor) {
        String brickSensorPort = "BrickKey." + keysSensor.getPort();
        switch ( keysSensor.getMode() ) {
            case SC.PRESSED:
                this.sb.append("hal.isPressed(" + brickSensorPort + ")");
                break;
            case SC.WAIT_FOR_PRESS_AND_RELEASE:
                this.sb.append("hal.isPressedAndReleased(" + brickSensorPort + ")");
                break;
            default:
                throw new DbcException("Invalide mode for KeysSensor!");
        }
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        String port = "SensorPort.S" + colorSensor.getPort();
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
        this.sb.append(methodName).append("(").append(port).append(")");
        return null;
    }

    @Override
    public Void visitHTColorSensor(HTColorSensor<Void> htColorSensor) {
        String port = "SensorPort.S" + htColorSensor.getPort();
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
        this.sb.append(methodName).append("(").append(port).append(")");
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        String encoderMotorPort = encoderSensor.getPort();
        boolean isRegulated = this.brickConfiguration.isMotorRegulated(encoderMotorPort);
        if ( encoderSensor.getMode().equals(SC.RESET) ) {
            String methodName = isRegulated ? "hal.resetRegulatedMotorTacho(" : "hal.resetUnregulatedMotorTacho(";
            this.sb.append(methodName + "ActorPort." + encoderMotorPort + ");");
        } else {
            String methodName = isRegulated ? "hal.getRegulatedMotorTachoValue(" : "hal.getUnregulatedMotorTachoValue(";
            this.sb.append(methodName + "ActorPort." + encoderMotorPort + ", MotorTachoMode." + encoderSensor.getMode() + ")");
        }
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        String gyroSensorPort = "SensorPort.S" + gyroSensor.getPort();
        switch ( gyroSensor.getMode() ) {
            case SC.ANGLE:
                this.sb.append("hal.getGyroSensorAngle(" + gyroSensorPort + ")");
                break;
            case SC.RATE:
                this.sb.append("hal.getGyroSensorRate(" + gyroSensorPort + ")");
                break;
            case SC.RESET:
                this.sb.append("hal.resetGyroSensor(" + gyroSensorPort + ");");
                break;
            default:
                throw new DbcException("Invalid GyroSensorMode");
        }
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        String infraredSensorPort = "SensorPort.S" + infraredSensor.getPort();
        switch ( infraredSensor.getMode() ) {
            case SC.DISTANCE:
                this.sb.append("hal.getInfraredSensorDistance(" + infraredSensorPort + ")");
                break;
            case SC.PRESENCE:
                this.sb.append("hal.getInfraredSensorSeek(" + infraredSensorPort + ")");
                break;
            default:
                throw new DbcException("Invalid Infrared Sensor Mode: " + infraredSensor.getMode());
        }
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        String timerNumber = timerSensor.getPort();
        switch ( timerSensor.getMode() ) {
            case SC.DEFAULT:
            case SC.VALUE:
                this.sb.append("hal.getTimerValue(" + timerNumber + ")");
                break;
            case SC.RESET:
                this.sb.append("hal.resetTimer(" + timerNumber + ");");
                break;
            default:
                throw new DbcException("Invalid Time Mode!");
        }
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        this.sb.append("hal.isPressed(" + "SensorPort.S" + touchSensor.getPort() + ")");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        String ultrasonicSensorPort = "SensorPort.S" + ultrasonicSensor.getPort();
        if ( ultrasonicSensor.getMode().equals(SC.DISTANCE) ) {
            this.sb.append("hal.getUltraSonicSensorDistance(" + ultrasonicSensorPort + ")");
        } else {
            this.sb.append("hal.getUltraSonicSensorPresence(" + ultrasonicSensorPort + ")");
        }
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
        this.sb.append("hal.getSoundLevel(" + "SensorPort.S" + soundSensor.getPort() + ")");
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        String compassSensorPort = "SensorPort.S" + compassSensor.getPort();
        switch ( compassSensor.getMode() ) {
            case SC.CALIBRATE:
                this.sb.append("hal.hiTecCompassStartCalibration(" + compassSensorPort + ");");
                nlIndent();
                this.sb.append("hal.waitFor(40000);");
                nlIndent();
                this.sb.append("hal.hiTecCompassStopCalibration(" + compassSensorPort + ");");
                break;
            case SC.ANGLE:
                this.sb.append("hal.getHiTecCompassAngle(" + compassSensorPort + ")");
                break;
            case SC.COMPASS:
                this.sb.append("hal.getHiTecCompassCompass(" + compassSensorPort + ")");
                break;
            default:
                throw new DbcException("Invalid Compass Mode!");
        }
        return null;
    }

    @Override
    public Void visitIRSeekerSensor(IRSeekerSensor<Void> irSeekerSensor) {
        String irSeekerSensorPort = "SensorPort.S" + irSeekerSensor.getPort();
        switch ( irSeekerSensor.getMode() ) {
            case SC.MODULATED:
                this.sb.append("hal.getHiTecIRSeekerModulated(" + irSeekerSensorPort + ")");
                break;
            case SC.UNMODULATED:
                this.sb.append("hal.getHiTecIRSeekerUnmodulated(" + irSeekerSensorPort + ")");
                break;
            default:
                throw new DbcException("Invalid IRSeeker Mode!");
        }
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        mainTask.getVariables().accept(this);
        nlIndent();
        nlIndent();
        this.sb.append("public void run() throws Exception {");
        incrIndentation();
        // this is needed for testing
        if ( mainTask.getDebug().equals("TRUE") ) {
            nlIndent();
            this.sb.append("hal.startLogging();");
            this.isInDebugMode = true;
        }
        if ( this.getBean(UsedHardwareBean.class).isActorUsed(SC.VOICE) && !this.brickConfiguration.getRobotName().equals("ev3lejosv0") ) {
            nlIndent();
            this.sb.append("hal.setLanguage(\"");
            this.sb.append(TTSLanguageMapper.getLanguageString(this.language));
            this.sb.append("\");");
        }
        return null;
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct<Void> getSubFunct) {
        IndexLocation loc0 = (IndexLocation) getSubFunct.getStrParam().get(0);
        IndexLocation loc1 = (IndexLocation) getSubFunct.getStrParam().get(1);

        boolean isLeftAParam = loc0 == FROM_START || loc0 == FROM_END;
        this.sb.append("new ArrayList<>(");
        getSubFunct.getParam().get(0).accept(this);
        this.sb.append(".subList(");

        switch ( loc0 ) {
            case FIRST:
                this.sb.append("0");
                break;
            case LAST:
                getSubFunct.getParam().get(0).accept(this);
                this.sb.append(".size() - 1");
                break;
            case FROM_START:
                this.sb.append("(int) ");
                getSubFunct.getParam().get(1).accept(this);
                break;
            case FROM_END:
                this.sb.append("(");
                getSubFunct.getParam().get(0).accept(this);
                this.sb.append(".size() - 1) - ");
                this.sb.append("(int) ");
                getSubFunct.getParam().get(1).accept(this);
                break;
        }

        this.sb.append(", ");

        switch ( loc1 ) {
            case FIRST:
                this.sb.append("0");
                break;
            case LAST:
                getSubFunct.getParam().get(0).accept(this);
                this.sb.append(".size()");
                break;
            case FROM_START:
                this.sb.append("(int) ");
                if ( isLeftAParam ) {
                    getSubFunct.getParam().get(2).accept(this);
                } else {
                    getSubFunct.getParam().get(1).accept(this);
                }
                break;
            case FROM_END:
                this.sb.append("(");
                getSubFunct.getParam().get(0).accept(this);
                this.sb.append(".size() - 1) - ");
                this.sb.append("(int) ");
                if ( isLeftAParam ) {
                    getSubFunct.getParam().get(2).accept(this);
                } else {
                    getSubFunct.getParam().get(1).accept(this);
                }
                break;
        }
        this.sb.append("))");
        return null;

    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        indexOfFunct.getParam().get(0).accept(this);
        switch ( (IndexLocation) indexOfFunct.getLocation() ) {
            case FIRST:
                this.sb.append(".indexOf(");
                break;
            case LAST:
                this.sb.append(".lastIndexOf(");
                break;
            default:
                // nothing to do
        }
        if ( indexOfFunct.getParam().get(1).getVarType() == BlocklyType.NUMBER ) {
            this.sb.append(" (float) ");
        }
        indexOfFunct.getParam().get(1).accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
        lengthOfIsEmptyFunct.getParam().get(0).accept(this);
        switch ( lengthOfIsEmptyFunct.getFunctName() ) {
            case LIST_IS_EMPTY:
                this.sb.append(".isEmpty()");
                break;
            case LIST_LENGTH:
                this.sb.append(".size()");
                break;
            default:
                LOG.error("this should NOT be executed (" + lengthOfIsEmptyFunct.getFunctName() + ")");
        }
        return null;
    }

    @Override
    public Void visitEmptyList(EmptyList<Void> emptyList) {
        this.sb
            .append(
                "new ArrayList<"
                    + getLanguageVarTypeFromBlocklyType(emptyList.getTypeVar()).substring(0, 1).toUpperCase()
                    + getLanguageVarTypeFromBlocklyType(emptyList.getTypeVar()).substring(1).toLowerCase()
                    + ">()");
        return null;
    }

    @Override
    public Void visitListCreate(ListCreate<Void> listCreate) {
        this.sb.append("new ArrayList<>(");

        if ( listCreate.getValue().get().size() > 0 ) {
            this.sb.append("Arrays.");
            if ( listCreate.getVarType() == BlocklyType.CONNECTION ) {
                this.sb.append("<NXTConnection>");
            } else if ( listCreate.getVarType() == BlocklyType.COLOR ) {
                this.sb.append("<PickColor>");
            } else if ( listCreate.getVarType() == BlocklyType.STRING ) {
                this.sb.append("<String>");
            } else if ( listCreate.getVarType() == BlocklyType.BOOLEAN ) {
                this.sb.append("<Boolean>");
            }

            this.sb.append("asList(");
            // manually go through value list to cast numbers to float
            if ( listCreate.getVarType() == BlocklyType.NUMBER ) {
                List<Expr<Void>> expressions = listCreate.getValue().get();
                for ( int i = 0; i < expressions.size(); i++ ) {
                    this.sb.append("(float) ");
                    expressions.get(i).accept(this);
                    if ( i != expressions.size() - 1 ) {
                        this.sb.append(", ");
                    }
                }

            } else {
                listCreate.getValue().accept(this);
            }
            this.sb.append(")");
        }

        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        ListElementOperations op = (ListElementOperations) listGetIndex.getElementOperation();
        IndexLocation loc = (IndexLocation) listGetIndex.getLocation();

        listGetIndex.getParam().get(0).accept(this);
        if ( op == GET ) {
            this.sb.append(".get( (int) (");
        } else if ( op == GET_REMOVE || op == REMOVE ) {
            this.sb.append(".remove( (int) (");
        }
        switch ( loc ) {
            case FIRST:
                this.sb.append("0");
                break;
            case LAST:
                listGetIndex.getParam().get(0).accept(this);
                this.sb.append(".size() - 1");
                break;
            case FROM_START:
                listGetIndex.getParam().get(1).accept(this);
                break;
            case FROM_END:
                this.sb.append("(");
                listGetIndex.getParam().get(0).accept(this);
                this.sb.append(".size() - 1) - ");
                listGetIndex.getParam().get(1).accept(this);
                break;
            case RANDOM:
                this.sb.append("0 /* absolutely random number */");
                break;
        }
        this.sb.append("))");

        // This means its a remove statement and a semicolon is required
        if ( op == REMOVE ) {
            this.sb.append(";");
        }
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        ListElementOperations op = (ListElementOperations) listSetIndex.getElementOperation();
        IndexLocation loc = (IndexLocation) listSetIndex.getLocation();

        listSetIndex.getParam().get(0).accept(this);
        if ( op == SET ) {
            this.sb.append(".set(");
        } else if ( op == INSERT ) {
            this.sb.append(".add(");
        }
        switch ( loc ) {
            case FIRST:
                this.sb.append("0, ");
                break;
            case LAST:
                if ( op == SET ) {
                    this.sb.append("(int) (");
                    listSetIndex.getParam().get(0).accept(this);
                    this.sb.append(".size() - 1");
                    this.sb.append("), ");
                }
                break;
            case FROM_START:
                this.sb.append("(int) (");
                listSetIndex.getParam().get(2).accept(this);
                this.sb.append("), ");
                break;
            case FROM_END:
                this.sb.append("(int) (");
                this.sb.append("(");
                listSetIndex.getParam().get(0).accept(this);
                this.sb.append(".size() - 1) - ");
                listSetIndex.getParam().get(2).accept(this);
                this.sb.append("), ");
                break;
            case RANDOM:
                this.sb.append("0 /* absolutely random number */");
                break;
        }

        if ( listSetIndex.getParam().get(1).getVarType() == BlocklyType.NUMBER ) {
            this.sb.append("(float) ");
        }

        listSetIndex.getParam().get(1).accept(this);

        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitBluetoothReceiveAction(BluetoothReceiveAction<Void> bluetoothReadAction) {
        this.sb.append("hal.readMessage(");
        bluetoothReadAction.getConnection().accept(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitBluetoothConnectAction(BluetoothConnectAction<Void> bluetoothConnectAction) {
        this.sb.append("hal.establishConnectionTo(");
        if ( !bluetoothConnectAction.getAddress().getKind().hasName("STRING_CONST") ) {
            this.sb.append("String.valueOf(");
            bluetoothConnectAction.getAddress().accept(this);
            this.sb.append(")");
        } else {
            bluetoothConnectAction.getAddress().accept(this);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitBluetoothSendAction(BluetoothSendAction<Void> bluetoothSendAction) {
        this.sb.append("hal.sendMessage(");
        if ( !bluetoothSendAction.getMsg().getKind().hasName("STRING_CONST") ) {
            this.sb.append("String.valueOf(");
            bluetoothSendAction.getMsg().accept(this);
            this.sb.append(")");
        } else {
            bluetoothSendAction.getMsg().accept(this);
        }
        this.sb.append(", ");
        bluetoothSendAction.getConnection().accept(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction<Void> bluetoothWaitForConnection) {
        this.sb.append("hal.waitForConnection()");
        return null;
    }

    @Override
    public Void visitBluetoothCheckConnectAction(BluetoothCheckConnectAction<Void> bluetoothCheckConnectAction) {
        return null;
    }

    private void generateRegenerateConfiguration() {
        this.sb.append(" brickConfiguration = new EV3Configuration.Builder()");
        incrIndentation();
        nlIndent();
        this.sb.append(".setWheelDiameter(").append(this.brickConfiguration.getWheelDiameter()).append(")");
        nlIndent();
        this.sb.append(".setTrackWidth(").append(this.brickConfiguration.getTrackWidth()).append(")");
        nlIndent();
        appendActors();
        appendSensors();
        this.sb.append(".build();");
        decrIndentation();
    }

    private void generateUsedImages() {
        for ( String image : this.getBean(UsedHardwareBean.class).getUsedImages() ) {
            this.sb.append("predefinedImages.put(\"" + image + "\", \"" + this.predefinedImage.get(image) + "\");");
            nlIndent();
        }
    }

    private void generateImports() {
        this.sb.append("package generated.main;");
        nlIndent();
        nlIndent();
        this.sb.append("import de.fhg.iais.roberta.runtime.*;");
        nlIndent();
        this.sb.append("import de.fhg.iais.roberta.runtime.ev3.*;");
        nlIndent();
        nlIndent();

        this.sb.append("import de.fhg.iais.roberta.mode.general.*;");
        nlIndent();
        this.sb.append("import de.fhg.iais.roberta.mode.action.*;");
        nlIndent();
        this.sb.append("import de.fhg.iais.roberta.mode.sensor.*;");
        nlIndent();
        this.sb.append("import de.fhg.iais.roberta.mode.action.ev3.*;");
        nlIndent();
        this.sb.append("import de.fhg.iais.roberta.mode.sensor.ev3.*;");
        nlIndent();
        nlIndent();

        this.sb.append("import de.fhg.iais.roberta.components.*;");
        nlIndent();
        nlIndent();

        this.sb.append("import java.util.LinkedHashSet;");
        nlIndent();
        this.sb.append("import java.util.HashMap;");
        nlIndent();
        this.sb.append("import java.util.Set;");
        nlIndent();
        this.sb.append("import java.util.Map;");
        nlIndent();
        this.sb.append("import java.util.List;");
        nlIndent();
        this.sb.append("import java.util.ArrayList;");
        nlIndent();
        this.sb.append("import java.util.Arrays;");
        nlIndent();
        this.sb.append("import java.util.Collections;");
        nlIndent();
        nlIndent();

        this.sb.append("import lejos.remote.nxt.NXTConnection;");
        nlIndent();
        nlIndent();
    }

    private void appendSensors() {
        for ( ConfigurationComponent sensor : this.brickConfiguration.getSensors() ) {
            boolean isUsed = false;
            for ( UsedSensor usedSensor : this.getBean(UsedHardwareBean.class).getUsedSensors() ) {
                if (sensor.getComponentType().contains(usedSensor.getType()) && usedSensor.getPort().equals(sensor.getUserDefinedPortName())) {
                    isUsed = true;
                }
            }
            if ( isUsed ) {
                this.sb.append(".addSensor(");
                this.sb.append("SensorPort.S" + sensor.getUserDefinedPortName()).append(", ");
                this.sb.append(generateRegenerateSensor(sensor));
                this.sb.append(")");
                nlIndent();
            }
        }
    }

    private void appendActors() {
        for ( ConfigurationComponent actor : this.brickConfiguration.getActors() ) {
            boolean isUsed = false;
            for ( UsedActor usedActor : this.getBean(UsedHardwareBean.class).getUsedActors() ) {
                if (usedActor.getType().equals(actor.getComponentType()) && usedActor.getPort().equals(actor.getUserDefinedPortName())) {
                    isUsed = true;
                }
            }
            if ( isUsed ) {
                this.sb.append(".addActor(");
                this.sb.append("ActorPort." + actor.getUserDefinedPortName()).append(", ");
                this.sb.append(generateRegenerateActor(actor));
                this.sb.append(")");
                nlIndent();
            }
        }
    }

    private String generateRegenerateUsedSensors() {
        StringBuilder sb = new StringBuilder();
        String arrayOfSensors = "";
        for ( UsedSensor usedSensor : this.getBean(UsedHardwareBean.class).getUsedSensors() ) {
            if (!usedSensor.getType().equals(SC.TIMER)) { // TODO this should be handled differently
                arrayOfSensors += generateRegenerateUsedSensor(usedSensor);
                arrayOfSensors += ", ";
            }
        }

        sb.append("private Set<UsedSensor> usedSensors = " + "new LinkedHashSet<UsedSensor>(");
        if ( !this.getBean(UsedHardwareBean.class).getUsedSensors().isEmpty() ) {
            if(arrayOfSensors.length() >= 2) {
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
        String componentType = sensor.getComponentType();

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
        if ( actor.getComponentType().equals("OTHER") ) {
            driveDirection = "FOREWARD";
        } else {
            driveDirection = actor.getProperty(SC.MOTOR_REVERSE).equals(SC.OFF) ? "FOREWARD" : "BACKWARD";
        }
        sb.append("new Actor(ActorType.").append(actor.getComponentType());
        if ( actor.getComponentType().equals("OTHER") ) {
            sb.append(", false");
        } else {
            sb.append(", ").append(actor.getProperty(SC.MOTOR_REGULATION).toLowerCase());
        }
        sb.append(", DriveDirection.").append(driveDirection);
        if ( actor.getComponentType().equals("MEDIUM") || actor.getComponentType().equals("OTHER") ) {
            sb.append(", MotorSide.NONE)");
        } else {
            sb.append(", MotorSide.").append(actor.getProperty(SC.MOTOR_DRIVE)).append(")");
        }
        return sb.toString();
    }

    private String generateRegenerateSensor(ConfigurationComponent sensor) {
        StringBuilder sb = new StringBuilder();
        sb.append("new Sensor(SensorType.");
        if ( sensor.getComponentType().equals(SC.COLOUR) ) {
            sb.append(SC.COLOR);
        } else {
            sb.append(sensor.getComponentType());
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
    public Void visitColorConst(ColorConst<Void> colorConst) {
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
        this.sb.append("PickColor." + color);
        return null;
    }

}
