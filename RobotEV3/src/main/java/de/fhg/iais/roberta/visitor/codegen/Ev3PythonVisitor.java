package de.fhg.iais.roberta.visitor.codegen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.fhg.iais.roberta.codegen.HelperMethodGenerator;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
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
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
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
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.codegen.utilities.Ev3VisitorHelper;
import de.fhg.iais.roberta.visitor.codegen.utilities.TTSLanguageMapper;
import de.fhg.iais.roberta.visitor.collect.Ev3UsedMethodCollectorVisitor;
import de.fhg.iais.roberta.visitor.collect.Ev3UsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.visitor.hardware.IEv3Visitor;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractPythonVisitor;

import static de.fhg.iais.roberta.visitor.codegen.utilities.ColorSensorUtils.isHiTecColorSensor;
import static de.fhg.iais.roberta.visitor.codegen.utilities.ColorSensorUtils.isEV3ColorSensor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable Python code representation of a phrase to a
 * StringBuilder. <b>This representation is correct Python code.</b> <br>
 */
public final class Ev3PythonVisitor extends AbstractPythonVisitor implements IEv3Visitor<Void> {

    protected final Configuration brickConfiguration;

    protected Map<String, String> predefinedImage = new HashMap<>();

    protected final Set<UsedSensor> usedSensors;
    protected final Set<UsedActor> usedActors;
    protected final Set<String> usedImages;

    protected ILanguage language;
    private final boolean isSayTextUsed;

    /**
     * initialize the Python code generator visitor.
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param programPhrases to generate the code from
     * @param indentation to start with. Will be ince/decr depending on block structure
     */
    private Ev3PythonVisitor(
        Configuration brickConfiguration,
        ArrayList<ArrayList<Phrase<Void>>> programPhrases,
        int indentation,
        ILanguage language,
        HelperMethodGenerator helperMethodGenerator) {
        super(programPhrases, indentation, helperMethodGenerator, new Ev3UsedMethodCollectorVisitor(programPhrases));

        Ev3UsedHardwareCollectorVisitor checkVisitor = new Ev3UsedHardwareCollectorVisitor(programPhrases, brickConfiguration);

        this.brickConfiguration = brickConfiguration;

        this.usedActors = checkVisitor.getUsedActors();
        this.usedSensors = checkVisitor.getUsedSensors();
        this.usedImages = checkVisitor.getUsedImages();
        this.isSayTextUsed = checkVisitor.isSayTextUsed();

        this.usedGlobalVarInFunctions = checkVisitor.getMarkedVariablesAsGlobal();
        this.isProgramEmpty = checkVisitor.isProgramEmpty();
        this.loopsLabels = checkVisitor.getloopsLabelContainer();

        this.language = language;

        this.predefinedImage = Ev3VisitorHelper.getPredefinedImages(this.brickConfiguration, true);
    }

    /**
     * factory method to generate Python code from an AST.<br>
     *
     * @param brickConfiguration hardware configuration of the brick
     * @param programPhrases to generate the code from
     */
    public static String generate(
        Configuration brickConfiguration,
        ArrayList<ArrayList<Phrase<Void>>> programPhrases,
        boolean withWrapping,
        ILanguage language,
        HelperMethodGenerator helperMethodGenerator) {
        Assert.notNull(brickConfiguration);

        Ev3PythonVisitor astVisitor = new Ev3PythonVisitor(brickConfiguration, programPhrases, 0, language, helperMethodGenerator);
        astVisitor.generateCode(withWrapping);

        return astVisitor.sb.toString();
    }

    @Override
    public Void visitWaitStmt(WaitStmt<Void> waitStmt) {
        this.sb.append("while True:");
        incrIndentation();
        visitStmtList(waitStmt.getStatements());
        nlIndent();
        this.sb.append("hal.waitFor(15)");
        decrIndentation();
        return null;
    }

    @Override
    public Void visitWaitTimeStmt(WaitTimeStmt<Void> waitTimeStmt) {
        this.sb.append("hal.waitFor(");
        waitTimeStmt.getTime().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        this.sb.append("hal.clearDisplay()");
        return null;
    }

    @Override
    public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        switch ( volumeAction.getMode() ) {
            case SET:
                this.sb.append("hal.setVolume(");
                volumeAction.getVolume().visit(this);
                this.sb.append(")");
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
        this.sb.append("hal.setLanguage(\"");
        this.sb.append(TTSLanguageMapper.getLanguageString(setLanguageAction.getLanguage()));
        this.sb.append("\")");
        return null;
    }

    @Override
    public Void visitSayTextAction(SayTextAction<Void> sayTextAction) {
        this.sb.append("hal.sayText(");
        if ( !sayTextAction.getMsg().getKind().hasName("STRING_CONST") ) {
            this.sb.append("str(");
            sayTextAction.getMsg().visit(this);
            this.sb.append(")");
        } else {
            sayTextAction.getMsg().visit(this);
        }
        BlockType emptyBlock = BlockTypeContainer.getByName("EMPTY_EXPR");
        if ( !(sayTextAction.getSpeed().getKind().equals(emptyBlock) && sayTextAction.getPitch().getKind().equals(emptyBlock)) ) {
            this.sb.append(",");
            sayTextAction.getSpeed().visit(this);
            this.sb.append(",");
            sayTextAction.getPitch().visit(this);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        this.sb.append("hal.ledOn(" + getEnumCode(lightAction.getColor()) + ", " + getEnumCode(lightAction.getMode()) + ")");
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        switch ( lightStatusAction.getStatus() ) {
            case OFF:
                this.sb.append("hal.ledOff()");
                break;
            case RESET:
                this.sb.append("hal.resetLED()");
                break;
            default:
                throw new DbcException("Invalid LED status mode!");
        }
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        this.sb.append("hal.playFile(" + playFileAction.getFileName() + ")");
        return null;
    }

    @Override
    public Void visitShowPictureAction(ShowPictureAction<Void> showPictureAction) {
        this.sb.append("hal.drawPicture(predefinedImages['").append(showPictureAction.getPicture()).append("'], ");
        showPictureAction.getX().visit(this);
        this.sb.append(", ");
        showPictureAction.getY().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
        this.sb.append("hal.drawText(");
        if ( !showTextAction.getMsg().getKind().hasName("STRING_CONST") ) {
            this.sb.append("str(");
            showTextAction.getMsg().visit(this);
            this.sb.append(")");
        } else {
            showTextAction.getMsg().visit(this);
        }
        this.sb.append(", ");
        showTextAction.getX().visit(this);
        this.sb.append(", ");
        showTextAction.getY().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        this.sb.append("hal.playTone(");
        toneAction.getFrequency().visit(this);
        this.sb.append(", ");
        toneAction.getDuration().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        this.sb.append("hal.playTone(float(");
        this.sb.append(playNoteAction.getFrequency());
        this.sb.append("), float(");
        this.sb.append(playNoteAction.getDuration());
        this.sb.append("))");
        return null;
    }

    private boolean isActorOnPort(String port) {
        boolean isActorOnPort = false;
        for ( UsedActor actor : this.usedActors ) {
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
                methodName = isRegulated ? "hal.rotateRegulatedMotor('" : "hal.rotateUnregulatedMotor('";
            } else {
                methodName = isRegulated ? "hal.turnOnRegulatedMotor('" : "hal.turnOnUnregulatedMotor('";
            }
            this.sb.append(methodName + userDefinedPort.toString() + "', ");
            motorOnAction.getParam().getSpeed().visit(this);
            if ( duration ) {
                this.sb.append(", " + getEnumCode(motorOnAction.getDurationMode()));
                this.sb.append(", ");
                motorOnAction.getDurationValue().visit(this);
            }
            this.sb.append(")");
        }
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        String userDefinedPort = motorSetPowerAction.getUserDefinedPort();
        if ( isActorOnPort(userDefinedPort) ) {
            boolean isRegulated = this.brickConfiguration.isMotorRegulated(userDefinedPort);
            String methodName = isRegulated ? "hal.setRegulatedMotorSpeed('" : "hal.setUnregulatedMotorSpeed('";
            this.sb.append(methodName + userDefinedPort + "', ");
            motorSetPowerAction.getPower().visit(this);
            this.sb.append(")");
        }
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        String userDefinedPort = motorGetPowerAction.getUserDefinedPort();
        if ( isActorOnPort(userDefinedPort) ) {
            boolean isRegulated = this.brickConfiguration.isMotorRegulated(userDefinedPort);
            String methodName = isRegulated ? "hal.getRegulatedMotorSpeed('" : "hal.getUnregulatedMotorSpeed('";
            this.sb.append(methodName + userDefinedPort + "')");
        }
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        if ( isActorOnPort(motorStopAction.getUserDefinedPort()) ) {
            this.sb
                .append("hal.stopMotor('")
                .append(motorStopAction.getUserDefinedPort())
                .append("', ")
                .append(getEnumCode(motorStopAction.getMode()))
                .append(')');
        }
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        if ( isActorOnPort(this.brickConfiguration.getFirstMotorPort(SC.LEFT)) && isActorOnPort(this.brickConfiguration.getFirstMotorPort(SC.RIGHT)) ) {
            boolean isDuration = driveAction.getParam().getDuration() != null;
            String methodName = isDuration ? "hal.driveDistance(" : "hal.regulatedDrive(";
            this.sb.append(methodName);
            this.sb.append("'" + this.brickConfiguration.getFirstMotorPort(SC.LEFT) + "', ");
            this.sb.append("'" + this.brickConfiguration.getFirstMotorPort(SC.RIGHT) + "', False, ");
            this.sb.append(getEnumCode(driveAction.getDirection()) + ", ");
            driveAction.getParam().getSpeed().visit(this);
            if ( isDuration ) {
                this.sb.append(", ");
                driveAction.getParam().getDuration().getValue().visit(this);
            }
            this.sb.append(")");
        }
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        String leftMotorPort = this.brickConfiguration.getFirstMotorPort(SC.LEFT);
        String rightMotorPort = this.brickConfiguration.getFirstMotorPort(SC.RIGHT);
        if ( isActorOnPort(leftMotorPort) && isActorOnPort(rightMotorPort) ) {
            boolean isDuration = turnAction.getParam().getDuration() != null;
            boolean isRegulated = this.brickConfiguration.isMotorRegulated(leftMotorPort);
            String methodName = "hal.rotateDirection" + (isDuration ? "Angle" : isRegulated ? "Regulated" : "Unregulated") + "(";
            this.sb.append(methodName);
            this.sb.append("'" + leftMotorPort + "', ");
            this.sb.append("'" + rightMotorPort + "', False, ");
            this.sb.append(getEnumCode(turnAction.getDirection()) + ", ");
            turnAction.getParam().getSpeed().visit(this);
            if ( isDuration ) {
                this.sb.append(", ");
                turnAction.getParam().getDuration().getValue().visit(this);
            }
            this.sb.append(")");
        }
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        String leftMotorPort = this.brickConfiguration.getFirstMotorPort(SC.LEFT);
        String rightMotorPort = this.brickConfiguration.getFirstMotorPort(SC.RIGHT);
        if ( isActorOnPort(leftMotorPort) && isActorOnPort(rightMotorPort) ) {
            this.sb.append("hal.stopMotors(");
            this.sb.append("'" + leftMotorPort + "', ");
            this.sb.append("'" + rightMotorPort + "')");
        }
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {
        String leftMotorPort = this.brickConfiguration.getFirstMotorPort(SC.LEFT);
        String rightMotorPort = this.brickConfiguration.getFirstMotorPort(SC.RIGHT);
        if ( isActorOnPort(leftMotorPort) && isActorOnPort(rightMotorPort) ) {
            MotorDuration<Void> duration = curveAction.getParamLeft().getDuration();

            this.sb.append("hal.driveInCurve(");
            this.sb.append(getEnumCode(curveAction.getDirection()) + ", ");
            this.sb.append("'" + leftMotorPort + "', ");
            curveAction.getParamLeft().getSpeed().visit(this);
            this.sb.append(", '" + rightMotorPort + "', ");
            curveAction.getParamRight().getSpeed().visit(this);
            if ( duration != null ) {
                this.sb.append(", ");
                duration.getValue().visit(this);
            }
            this.sb.append(")");
        }
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor<Void> keysSensor) {
        switch ( keysSensor.getMode() ) {
            case SC.PRESSED:
                this.sb.append("hal.isKeyPressed('" + keysSensor.getPort().toLowerCase() + "')");
                break;
            case SC.WAIT_FOR_PRESS_AND_RELEASE:
                this.sb.append("hal.isKeyPressedAndReleased(" + keysSensor.getPort().toLowerCase() + ")");
                break;
            default:
                throw new DbcException("Invalide mode for KeysSensor!");
        }
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        String colorSensorPort = colorSensor.getPort();
        String colorSensorType = this.brickConfiguration.getConfigurationComponent(colorSensor.getPort()).getComponentType();
        String colorSensorMode = colorSensor.getMode();
        String methodName;
        if ( isHiTecColorSensor(colorSensorType) ) {
            methodName = getHiTecColorSensorMethodName(colorSensorMode);
        } else if ( isEV3ColorSensor(colorSensorType) ) {
            methodName = getEV3ColorSensorMethodName(colorSensorMode);
        } else {
            throw new DbcException("Invalid color sensor type: " + colorSensorType);
        }
        this.sb.append(methodName + "('" + colorSensorPort + "')");
        return null;
    }

    private String getEV3ColorSensorMethodName (String colorSensorMode) {
        switch ( colorSensorMode ) {
            case SC.AMBIENTLIGHT:
                return "hal.getColorSensorAmbient";
            case SC.COLOUR:
                return "hal.getColorSensorColour";
            case SC.LIGHT:
                return "hal.getColorSensorRed";
            case SC.RGB:
                return "hal.getColorSensorRgb";
            default:
                throw new DbcException("Invalid mode for EV3 Color Sensor!");
        }
    }

    private String getHiTecColorSensorMethodName (String colorSensorMode) {
        switch ( colorSensorMode ) {
            case SC.AMBIENTLIGHT:
                return "hal.getHiTecColorSensorV2Ambient";
            case SC.COLOUR:
                return "hal.getHiTecColorSensorV2Colour";
            case SC.LIGHT:
                return "hal.getHiTecColorSensorV2Light";
            case SC.RGB:
                return "hal.getHiTecColorSensorV2Rgb";
            default:
                throw new DbcException("Invalid mode for HiTec Color Sensor V2!");
        }
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        String encoderSensorPort = encoderSensor.getPort().toString();
        if ( encoderSensor.getMode().equals(SC.RESET) ) {
            this.sb.append("hal.resetMotorTacho('" + encoderSensorPort + "')");
        } else {
            this.sb.append("hal.getMotorTachoValue('" + encoderSensorPort + "', " + getEnumCode(encoderSensor.getMode()) + ")");
        }
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        String gyroSensorPort = gyroSensor.getPort();
        if ( gyroSensor.getMode().equals(SC.RESET) ) {
            this.sb.append("hal.resetGyroSensor('" + gyroSensorPort + "')");
        } else {
            this.sb.append("hal.getGyroSensorValue('" + gyroSensorPort + "', " + getEnumCode(gyroSensor.getMode()) + ")");
        }
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        String compassSensorPort = compassSensor.getPort();
        switch ( compassSensor.getMode() ) {
            case SC.CALIBRATE:
                // Calibration is not supported by ev3dev hitechnic sensor for now
                break;
            case SC.ANGLE:
            case SC.COMPASS:
                this.sb.append("hal.getHiTecCompassSensorValue('" + compassSensorPort + "', " + getEnumCode(compassSensor.getMode()) + ")");
                break;
            default:
                throw new DbcException("Invalid Compass Mode!");
        }
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        String infraredSensorPort = infraredSensor.getPort();
        switch ( infraredSensor.getMode() ) {
            case SC.DISTANCE:
                this.sb.append("hal.getInfraredSensorDistance('" + infraredSensorPort + "')");
                break;
            case SC.PRESENCE:
                this.sb.append("hal.getInfraredSensorSeek('" + infraredSensorPort + "')");
                break;
            default:
                throw new DbcException("Invalid Infrared Sensor Mode!");
        }

        return null;
    }

    @Override
    public Void visitIRSeekerSensor(IRSeekerSensor<Void> irSeekerSensor) {
        String irSeekerSensorPort = irSeekerSensor.getPort();
        switch ( irSeekerSensor.getMode() ) {
            case SC.MODULATED:
                this.sb.append("hal.getHiTecIRSeekerSensorValue('" + irSeekerSensorPort + "', 'AC')");
                break;
            case SC.UNMODULATED:
                this.sb.append("hal.getHiTecIRSeekerSensorValue('" + irSeekerSensorPort + "', 'DC')");
                break;
            default:
                throw new DbcException("Invalid IRSeeker Mode!");
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
                this.sb.append("hal.resetTimer(" + timerNumber + ")");
                break;
            default:
                throw new DbcException("Invalid Time Mode!");
        }
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        this.sb.append("hal.isPressed('" + touchSensor.getPort() + "')");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        String ultrasonicSensorPort = ultrasonicSensor.getPort();
        if ( ultrasonicSensor.getMode().equals(SC.DISTANCE) ) {
            this.sb.append("hal.getUltraSonicSensorDistance('" + ultrasonicSensorPort + "')");
        } else {
            this.sb.append("hal.getUltraSonicSensorPresence('" + ultrasonicSensorPort + "')");
        }
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
        String soundSensorPort = soundSensor.getPort();
        this.sb.append("hal.getSoundLevel('" + soundSensorPort + "')");
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        StmtList<Void> variables = mainTask.getVariables();
        variables.visit(this);
        generateUserDefinedMethods();
        nlIndent();
        this.sb.append("def run():");
        incrIndentation();
        if ( !this.usedGlobalVarInFunctions.isEmpty() ) {
            nlIndent();
            this.sb.append("global ").append(String.join(", ", this.usedGlobalVarInFunctions));
        } else {
            addPassIfProgramIsEmpty();
        }
        return null;
    }

    @Override
    public Void visitBluetoothReceiveAction(BluetoothReceiveAction<Void> bluetoothReadAction) {
        this.sb.append("hal.readMessage(");
        bluetoothReadAction.getConnection().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitBluetoothConnectAction(BluetoothConnectAction<Void> bluetoothConnectAction) {
        this.sb.append("hal.establishConnectionTo(");
        if ( !bluetoothConnectAction.getAddress().getKind().hasName("STRING_CONST") ) {
            this.sb.append("str(");
            bluetoothConnectAction.getAddress().visit(this);
            this.sb.append(")");
        } else {
            bluetoothConnectAction.getAddress().visit(this);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitBluetoothSendAction(BluetoothSendAction<Void> bluetoothSendAction) {
        this.sb.append("hal.sendMessage(");
        bluetoothSendAction.getConnection().visit(this);
        this.sb.append(", ");
        if ( !bluetoothSendAction.getMsg().getKind().hasName("STRING_CONST") ) {
            this.sb.append("str(");
            bluetoothSendAction.getMsg().visit(this);
            this.sb.append(")");
        } else {
            bluetoothSendAction.getMsg().visit(this);
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction<Void> bluetoothWaitForConnection) {
        this.sb.append("hal.waitForConnection()");
        return null;
    }

    @Override
    public Void visitConnectConst(ConnectConst<Void> connectConst) {
        return null;
    }

    @Override
    public Void visitBluetoothCheckConnectAction(BluetoothCheckConnectAction<Void> bluetoothCheckConnectAction) {
        return null;
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        this.usedGlobalVarInFunctions.clear();
        this.sb.append("#!/usr/bin/python");
        nlIndent();
        nlIndent();
        this.sb.append("from __future__ import absolute_import");
        nlIndent();
        this.sb.append("from roberta.ev3 import Hal");
        nlIndent();
        this.sb.append("from ev3dev import ev3 as ev3dev");
        nlIndent();
        this.sb.append("import math");
        nlIndent();
        nlIndent();
        this.sb.append("class BreakOutOfALoop(Exception): pass");
        nlIndent();
        this.sb.append("class ContinueLoop(Exception): pass");
        nlIndent();
        nlIndent();
        this.sb.append(generateUsedImages());
        this.sb.append(generateRegenerateConfiguration());
        nlIndent();
        this.sb.append("hal = Hal(_brickConfiguration)");

        if ( this.isSayTextUsed ) {
            nlIndent();
            this.sb.append("hal.setLanguage(\"");
            this.sb.append(TTSLanguageMapper.getLanguageString(this.language));
            this.sb.append("\")");
        }
    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        decrIndentation(); // everything is still indented from main program
        nlIndent();
        nlIndent();
        this.sb.append("def main():");
        incrIndentation();
        nlIndent();
        this.sb.append("try:");
        incrIndentation();
        nlIndent();
        this.sb.append("run()");
        decrIndentation();
        nlIndent();
        this.sb.append("except Exception as e:");
        incrIndentation();
        nlIndent();
        this.sb.append("hal.drawText('Fehler im EV3', 0, 0)");
        nlIndent();
        this.sb.append("hal.drawText(e.__class__.__name__, 0, 1)");
        nlIndent();
        // FIXME: we can only print about 30 chars
        this.sb.append("hal.drawText(str(e), 0, 2)");
        nlIndent();
        this.sb.append("hal.drawText('Press any key', 0, 4)");
        nlIndent();
        this.sb.append("while not hal.isKeyPressed('any'): hal.waitFor(500)");
        nlIndent();
        this.sb.append("raise");
        decrIndentation();
        decrIndentation();
        nlIndent();

        super.generateProgramSuffix(withWrapping);
    }

    private String generateUsedImages() {
        if ( this.usedImages.size() != 0 ) {
            StringBuilder sb = new StringBuilder();

            sb.append("predefinedImages = {\n");
            for ( String image : this.usedImages ) {
                sb.append("    '" + image + "': u'" + this.predefinedImage.get(image) + "',\n");
            }
            sb.append("}\n");
            return sb.toString();

        } else {
            return "";
        }
    }

    private String generateRegenerateConfiguration() {
        StringBuilder sb = new StringBuilder();
        sb.append("_brickConfiguration = {\n");
        sb.append("    'wheel-diameter': " + this.brickConfiguration.getWheelDiameterCM() + ",\n");
        sb.append("    'track-width': " + this.brickConfiguration.getTrackWidthCM() + ",\n");
        appendActors(sb);
        appendSensors(sb);
        sb.append("}");
        return sb.toString();
    }

    private boolean isActorUsed(ConfigurationComponent actor, String port) {
        for ( UsedActor usedActor : this.usedActors ) {
            String usedActorComponentType = this.brickConfiguration.getConfigurationComponent(usedActor.getPort()).getComponentType();
            if ( port.equals(usedActor.getPort()) && actor.getComponentType().equals(usedActorComponentType) ) {
                return true;
            }
        }
        return false;
    }

    private void appendActors(StringBuilder sb) {
        sb.append("    'actors': {\n");
        for ( ConfigurationComponent actor : this.brickConfiguration.getActors() ) {
            String port = actor.getUserDefinedPortName();
            if ( actor != null && isActorUsed(actor, port) ) {
                sb.append("        '").append(port.toString()).append("':");
                sb.append(generateRegenerateActor(actor, port));
                sb.append(",\n");
            }
        }
        sb.append("    },\n");
    }

    private boolean isSensorUsed(ConfigurationComponent sensor, String port) {
        for ( UsedSensor usedSensor : this.usedSensors ) {
            String usedSctorComponentType = this.brickConfiguration.getConfigurationComponent(usedSensor.getPort()).getComponentType();
            if ( port.equals(usedSensor.getPort()) && sensor.getComponentType().equals(usedSctorComponentType) ) {
                return true;
            }
        }
        return false;
    }

    private void appendSensors(StringBuilder sb) {
        sb.append("    'sensors': {\n");
        for ( ConfigurationComponent sensor : this.brickConfiguration.getSensors() ) {
            String port = sensor.getUserDefinedPortName();
            if ( sensor != null && isSensorUsed(sensor, port) ) {
                sb.append("        '").append(port).append("':");
                sb.append(generateRegenerateSensor(sensor, port));
                sb.append(",\n");
            }
        }
        sb.append("    },\n");
    }

    private String generateRegenerateActor(ConfigurationComponent actor, String port) {
        StringBuilder sb = new StringBuilder();
        // FIXME: that won't scale
        String name = null;
        switch ( actor.getComponentType() ) {
            case SC.MEDIUM:
                name = "MediumMotor";
                break;
            case SC.LARGE:
                name = "LargeMotor";
                break;
            case SC.OTHER:
                name = "OtherConsumer";
                break;
            default:
                throw new IllegalArgumentException("no mapping for " + actor.getComponentType() + "to ev3dev-lang-python");
        }

        sb.append("Hal.make").append(name).append("(ev3dev.OUTPUT_").append(port.toString());
        boolean isRegulated;
        boolean isReverse;
        if ( actor.getComponentType().equals("OTHER") ) {
            isReverse = false;
            isRegulated = false;
        } else {
            isReverse = actor.getProperty(SC.MOTOR_REVERSE).equals(SC.ON);
            isRegulated = actor.getProperty(SC.MOTOR_REGULATION).equals(SC.TRUE);
        }

        sb.append(", ").append(isRegulated ? "'on'" : "'off'");
        sb.append(", ").append(isReverse ? "'backward'" : "'foreward'");
        sb.append(")");
        return sb.toString();
    }

    private static String generateRegenerateSensor(ConfigurationComponent sensor, String port) {
        StringBuilder sb = new StringBuilder();
        // FIXME: that won't scale
        String name = null;
        // [m for m in dir(ev3dev) if m.find("_sensor") != -1]
        // ['ColorSensor', 'GyroSensor', 'I2cSensor', 'InfraredSensor', 'LightSensor', 'SoundSensor', 'TouchSensor', 'UltrasonicSensor']
        switch ( sensor.getComponentType() ) {
            case SC.COLOR:
                name = "ColorSensor";
                break;
            case SC.GYRO:
                name = "GyroSensor";
                break;
            case SC.INFRARED:
                name = "InfraredSensor";
                break;
            case SC.TOUCH:
                name = "TouchSensor";
                break;
            case SC.ULTRASONIC:
                name = "UltrasonicSensor";
                break;
            case SC.SOUND:
                name = "SoundSensor";
                break;
            case SC.COMPASS:
                name = "CompassSensor";
                break;
            case SC.IRSEEKER:
                name = "IRSeekerSensor";
                break;
            case SC.HT_COLOR:
                name = "HTColorSensorV2";
                break;
            default:
                throw new IllegalArgumentException("no mapping for " + sensor.getComponentType() + "to ev3dev-lang-python");
        }
        sb.append("Hal.make").append(name).append("(ev3dev.INPUT_").append(port).append(")");
        return sb.toString();
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
        this.sb.append("'" + color.toLowerCase() + "'");
        return null;
    }

}
