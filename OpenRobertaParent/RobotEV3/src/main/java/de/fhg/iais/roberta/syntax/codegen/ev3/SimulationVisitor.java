package de.fhg.iais.roberta.syntax.codegen.ev3;

import java.util.ArrayList;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.inter.mode.action.IDriveDirection;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.inter.mode.action.ITurnDirection;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.mode.action.Language;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.mode.sensor.ColorSensorMode;
import de.fhg.iais.roberta.mode.sensor.CompassSensorMode;
import de.fhg.iais.roberta.mode.sensor.GyroSensorMode;
import de.fhg.iais.roberta.mode.sensor.LightSensorMode;
import de.fhg.iais.roberta.mode.sensor.MotorTachoMode;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlockTypeContainer.BlockType;
import de.fhg.iais.roberta.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowPictureAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.motor.CurveAction;
import de.fhg.iais.roberta.syntax.action.motor.DriveAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorDriveStopAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorGetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorSetPowerAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.TurnAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayFileAction;
import de.fhg.iais.roberta.syntax.action.sound.SayTextAction;
import de.fhg.iais.roberta.syntax.action.sound.SetLanguageAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.codegen.RobotSimulationVisitor;
import de.fhg.iais.roberta.syntax.sensor.generic.BrickSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.IRSeekerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class SimulationVisitor extends RobotSimulationVisitor<Void> {
    private static final String MOTOR_LEFT = "CONST.MOTOR_LEFT";
    private static final String MOTOR_RIGHT = "CONST.MOTOR_RIGHT";

    private SimulationVisitor(Configuration brickConfiguration, ILanguage language) {
        super(brickConfiguration);
    }

    public static String generate(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, ILanguage language) {
        Assert.isTrue(!phrasesSet.isEmpty());
        Assert.notNull(brickConfiguration);

        SimulationVisitor astVisitor = new SimulationVisitor(brickConfiguration, language);
        astVisitor.generateCodeFromPhrases(phrasesSet);
        return astVisitor.sb.toString();
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        String end = createClosingBracket();
        this.sb.append("createDriveAction(");
        driveAction.getParam().getSpeed().visit(this);
        IDriveDirection leftMotorRotationDirection = this.brickConfiguration.getActorOnPort(this.brickConfiguration.getLeftMotorPort()).getRotationDirection();
        DriveDirection driveDirection = (DriveDirection) driveAction.getDirection();
        if ( leftMotorRotationDirection != DriveDirection.FOREWARD ) {
            driveDirection = getDriveDirection(driveAction.getDirection() == DriveDirection.FOREWARD);
        }
        this.sb.append(", CONST." + driveDirection);
        MotorDuration<Void> duration = driveAction.getParam().getDuration();
        appendDuration(duration);
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {
        String end = createClosingBracket();
        this.sb.append("createCurveAction(");
        curveAction.getParamLeft().getSpeed().visit(this);
        this.sb.append(", ");
        curveAction.getParamRight().getSpeed().visit(this);
        IDriveDirection leftMotorRotationDirection = this.brickConfiguration.getActorOnPort(this.brickConfiguration.getLeftMotorPort()).getRotationDirection();
        DriveDirection driveDirection = (DriveDirection) curveAction.getDirection();
        if ( leftMotorRotationDirection != DriveDirection.FOREWARD ) {
            driveDirection = getDriveDirection(curveAction.getDirection() == DriveDirection.FOREWARD);
        }
        this.sb.append(", CONST." + driveDirection);
        MotorDuration<Void> duration = curveAction.getParamLeft().getDuration();
        appendDuration(duration);
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction<Void> turnAction) {
        String end = createClosingBracket();
        this.sb.append("createTurnAction(");
        turnAction.getParam().getSpeed().visit(this);
        IDriveDirection leftMotorRotationDirection = this.brickConfiguration.getActorOnPort(this.brickConfiguration.getLeftMotorPort()).getRotationDirection();
        ITurnDirection turnDirection = turnAction.getDirection();
        if ( leftMotorRotationDirection != DriveDirection.FOREWARD ) {
            turnDirection = getTurnDirection(turnAction.getDirection() == TurnDirection.LEFT);
        }
        this.sb.append(", CONST." + turnDirection);
        MotorDuration<Void> duration = turnAction.getParam().getDuration();
        appendDuration(duration);
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        String end = createClosingBracket();
        this.sb.append("createTurnLight(CONST." + lightAction.getColor() + ", CONST." + lightAction.getMode());
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        String end = createClosingBracket();
        this.sb.append("createStatusLight(CONST." + lightStatusAction.getStatus());
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        this.sb.append("createGetMotorPower(" + (motorGetPowerAction.getPort().getOraName().equals("B") ? MOTOR_RIGHT : MOTOR_LEFT).toString() + ")");
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        boolean isDuration = motorOnAction.getParam().getDuration() != null;
        String end = createClosingBracket();
        this.sb.append("createMotorOnAction(");
        motorOnAction.getParam().getSpeed().visit(this);
        this.sb.append(", " + (motorOnAction.getPort().getOraName().equals("B") ? MOTOR_RIGHT : MOTOR_LEFT).toString());
        if ( isDuration ) {
            this.sb.append(", createDuration(CONST.");
            this.sb.append(motorOnAction.getParam().getDuration().getType().toString() + ", ");
            motorOnAction.getParam().getDuration().getValue().visit(this);
            this.sb.append(")");
        }
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        String end = createClosingBracket();
        this.sb.append("createSetMotorPowerAction(" + (motorSetPowerAction.getPort().getOraName().equals("B") ? MOTOR_RIGHT : MOTOR_LEFT).toString() + ", ");
        motorSetPowerAction.getPower().visit(this);
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        String end = createClosingBracket();
        this.sb.append("createStopMotorAction(");
        this.sb.append((motorStopAction.getPort().getOraName().equals("B") ? MOTOR_RIGHT : MOTOR_LEFT).toString());
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction<Void> clearDisplayAction) {
        String end = createClosingBracket();
        this.sb.append("createClearDisplayAction(");
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitVolumeAction(VolumeAction<Void> volumeAction) {
        if ( volumeAction.getMode() == VolumeAction.Mode.SET ) {
            String end = createClosingBracket();
            this.sb.append("createSetVolumeAction(CONST." + volumeAction.getMode() + ", ");
            volumeAction.getVolume().visit(this);
            this.sb.append(end);
        } else {
            this.sb.append("createGetVolume()");
        }
        return null;
    }

    private String getLanguageString(ILanguage language) {
        switch ( (Language) language ) {
            case GERMAN:
                return "de-DE";
            case ENGLISH:
                return "en-US";
            case FRENCH:
                return "fr-FR";
            case SPANISH:
                return "es-ES";
            case ITALIAN:
                return "it-IT";
            case DUTCH:
                return "nl-NL";
            case POLISH:
                return "pl-PL";
            case RUSSIAN:
                return "ru-RU";
            case PORTUGUESE:
                return "pt-BR";
            case JAPANESE:
                return "ja-JP";
            case CHINESE:
                return "zh-CN";
            default:
                return "";
        }
    }

    @Override
    public Void visitSetLanguageAction(SetLanguageAction<Void> setLanguageAction) {
        String end = createClosingBracket();
        this.sb.append("createSetLanguageAction(createConstant(CONST.STRING_CONST, \'");
        this.sb.append(getLanguageString(setLanguageAction.getLanguage()));
        this.sb.append("\')");
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitSayTextAction(SayTextAction<Void> sayTextAction) {
        String end = createClosingBracket();
        // this.sb.append("createSetLanguageAction(createConstant(CONST.STRING_CONST,
        // \'");
        // this.sb.append(this.getLanguageString(this.language));
        // this.sb.append("\')");
        // this.sb.append(end);
        // this.visitExprStmt();
        this.sb.append("createSayTextAction(");
        if ( !sayTextAction.getMsg().getKind().hasName("STRING_CONST") ) {
            this.sb.append("String(");
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
        // this.sb.append(")");
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction<Void> playFileAction) {
        String end = createClosingBracket();
        this.sb.append("createPlayFileAction(" + playFileAction.getFileName());
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitShowPictureAction(ShowPictureAction<Void> showPictureAction) {
        String end = createClosingBracket();
        this.sb.append("createShowPictureAction('" + showPictureAction.getPicture() + "', ");
        showPictureAction.getX().visit(this);
        this.sb.append(", ");
        showPictureAction.getY().visit(this);
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
        String end = createClosingBracket();
        this.sb.append("createShowTextAction(");
        showTextAction.getMsg().visit(this);
        this.sb.append(", ");
        showTextAction.getX().visit(this);
        this.sb.append(", ");
        showTextAction.getY().visit(this);
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction<Void> stopAction) {
        String end = createClosingBracket();
        this.sb.append("createStopDrive(");
        this.sb.append(end);
        return null;
    }

    @Override
    public Void visitBrickSensor(BrickSensor<Void> brickSensor) {
        this.sb.append("createGetSample(CONST.BUTTONS, CONST." + brickSensor.getPort() + ")");
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        this.sb.append("createGetSample(CONST.COLOR, CONST." + ((ColorSensorMode) colorSensor.getMode()).getModeValue() + ")");
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        this.sb.append("createGetSample(CONST.LIGHT, CONST." + ((LightSensorMode) lightSensor.getMode()).getModeValue() + ")");
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        String encoderMotor = (encoderSensor.getPort().getOraName().equals("B") ? MOTOR_RIGHT : MOTOR_LEFT).toString();
        if ( encoderSensor.getMode() == MotorTachoMode.RESET ) {
            String end = createClosingBracket();
            this.sb.append("createResetEncoderSensor(" + encoderMotor);
            this.sb.append(end);
        } else {
            this.sb.append("createGetSampleEncoderSensor(" + encoderMotor + ", CONST." + encoderSensor.getMode() + ")");
        }
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        if ( gyroSensor.getMode() == GyroSensorMode.RESET ) {
            String end = createClosingBracket();
            this.sb.append("createResetGyroSensor(");
            this.sb.append(end);
        } else {
            this.sb.append("createGetGyroSensorSample(CONST.GYRO, CONST." + gyroSensor.getMode() + ")");
        }
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        switch ( (CompassSensorMode) compassSensor.getMode() ) {
            case CALIBRATE:
                this.sb.append("");
                break;
            case ANGLE:
                this.sb.append("null");
                break;
            case COMPASS:
                this.sb.append("null");
                break;
            default:
                throw new DbcException("Invalid Compass Mode!");
        }
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        this.sb.append("createGetSample(CONST.INFRARED, CONST." + infraredSensor.getMode() + ")");
        return null;
    }

    @Override
    public Void visitIRSeekerSensor(IRSeekerSensor<Void> irSeekerSensor) {
        this.sb.append("null");
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        this.sb.append("createGetSample(CONST.TOUCH)");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        this.sb.append("createGetSample(CONST.ULTRASONIC, CONST." + ultrasonicSensor.getMode() + ")");
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
        this.sb.append("createGetSample(CONST.SOUND)");
        return null;
    }
}
