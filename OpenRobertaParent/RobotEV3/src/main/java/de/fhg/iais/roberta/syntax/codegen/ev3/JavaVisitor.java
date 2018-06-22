package de.fhg.iais.roberta.syntax.codegen.ev3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.fhg.iais.roberta.components.Actor;
import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.Sensor;
import de.fhg.iais.roberta.components.SensorType;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.components.ev3.EV3Configuration;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.inter.mode.sensor.IRSeekerSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.mode.action.Language;
import de.fhg.iais.roberta.mode.general.IndexLocation;
import de.fhg.iais.roberta.mode.sensor.BrickKeyPressMode;
import de.fhg.iais.roberta.mode.sensor.ColorSensorMode;
import de.fhg.iais.roberta.mode.sensor.CompassSensorMode;
import de.fhg.iais.roberta.mode.sensor.GyroSensorMode;
import de.fhg.iais.roberta.mode.sensor.InfraredSensorMode;
import de.fhg.iais.roberta.mode.sensor.MotorTachoMode;
import de.fhg.iais.roberta.mode.sensor.TimerSensorMode;
import de.fhg.iais.roberta.mode.sensor.UltrasonicSensorMode;
import de.fhg.iais.roberta.syntax.BlockTypeContainer;
import de.fhg.iais.roberta.syntax.BlockTypeContainer.BlockType;
import de.fhg.iais.roberta.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothCheckConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothConnectAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothReceiveAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothSendAction;
import de.fhg.iais.roberta.syntax.action.communication.BluetoothWaitForConnectionAction;
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
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.SayTextAction;
import de.fhg.iais.roberta.syntax.action.sound.SetLanguageAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.action.sound.VolumeAction;
import de.fhg.iais.roberta.syntax.check.hardware.ev3.UsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.syntax.codegen.RobotJavaVisitor;
import de.fhg.iais.roberta.syntax.lang.blocksequence.MainTask;
import de.fhg.iais.roberta.syntax.lang.expr.ConnectConst;
import de.fhg.iais.roberta.syntax.lang.expr.EmptyList;
import de.fhg.iais.roberta.syntax.lang.expr.ListCreate;
import de.fhg.iais.roberta.syntax.lang.functions.FunctionNames;
import de.fhg.iais.roberta.syntax.lang.functions.GetSubFunct;
import de.fhg.iais.roberta.syntax.lang.functions.IndexOfFunct;
import de.fhg.iais.roberta.syntax.lang.functions.LengthOfIsEmptyFunct;
import de.fhg.iais.roberta.syntax.lang.functions.ListGetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.ListRepeat;
import de.fhg.iais.roberta.syntax.lang.functions.ListSetIndex;
import de.fhg.iais.roberta.syntax.lang.functions.MathConstrainFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathNumPropFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathOnListFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathPowerFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomFloatFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathRandomIntFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextJoinFunct;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitTimeStmt;
import de.fhg.iais.roberta.syntax.sensor.generic.BrickSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.IRSeekerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.AstVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorCommunicationVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorDisplayVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorLightVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorMotorVisitor;
import de.fhg.iais.roberta.visitor.actor.AstActorSoundVisitor;
import de.fhg.iais.roberta.visitor.sensor.AstSensorsVisitor;

/**
 * This class is implementing {@link AstVisitor}. All methods are implemented and they append a human-readable JAVA code representation of a phrase to a
 * StringBuilder. <b>This representation is correct JAVA code.</b> <br>
 */

public class JavaVisitor extends RobotJavaVisitor implements AstSensorsVisitor<Void>, AstActorCommunicationVisitor<Void>, AstActorDisplayVisitor<Void>,
    AstActorMotorVisitor<Void>, AstActorLightVisitor<Void>, AstActorSoundVisitor<Void> {
    protected final EV3Configuration brickConfiguration;

    protected Map<String, String> predefinedImage = new HashMap<>();
    protected final Set<UsedSensor> usedSensors;
    protected final Set<UsedActor> usedActors;
    protected final Set<String> usedImages;

    protected ILanguage language;
    private final boolean isSayTextUsed;

    /**
     * initialize the Java code generator visitor.
     *
     * @param programName name of the program
     * @param brickConfiguration hardware configuration of the brick
     * @param usedSensors in the current program
     * @param indentation to start with. Will be ince/decr depending on block structure
     */
    private JavaVisitor(
        String programName,
        ArrayList<ArrayList<Phrase<Void>>> programPhrases,
        EV3Configuration brickConfiguration,
        int indentation,
        ILanguage language) {
        super(programPhrases, programName, indentation);

        UsedHardwareCollectorVisitor checkVisitor = new UsedHardwareCollectorVisitor(programPhrases, brickConfiguration);

        this.brickConfiguration = brickConfiguration;
        this.usedSensors = checkVisitor.getUsedSensors();
        this.usedActors = checkVisitor.getUsedActors();
        this.usedImages = checkVisitor.getUsedImages();
        this.isSayTextUsed = checkVisitor.isSayTextUsed();

        this.loopsLabels = checkVisitor.getloopsLabelContainer();

        this.language = language;
        // Picture strings are UTF-16 encoded with extra \0 padding bytes
        initPredefinedImages();

    }

    /**
     * factory method to generate Java code from an AST.<br>
     *
     * @param programName name of the program
     * @param brickConfiguration hardware configuration of the brick
     * @param phrases to generate the code from
     */
    public static String generate(
        String programName,
        EV3Configuration brickConfiguration,
        ArrayList<ArrayList<Phrase<Void>>> phrasesSet,
        boolean withWrapping,
        ILanguage language) {
        Assert.notNull(programName);
        Assert.notNull(brickConfiguration);

        JavaVisitor astVisitor = new JavaVisitor(programName, phrasesSet, brickConfiguration, withWrapping ? 1 : 0, language);
        astVisitor.generateCode(withWrapping);
        return astVisitor.sb.toString();
    }

    @Override
    protected void generateProgramPrefix(boolean withWrapping) {
        if ( !withWrapping ) {
            return;
        }
        generateImports();

        this.sb.append("public class " + this.programName + " {\n");
        this.sb.append(INDENT).append("private static Configuration brickConfiguration;").append("\n\n");
        this.sb.append(INDENT).append(generateRegenerateUsedSensors()).append("\n");
        if ( this.usedImages.size() != 0 ) {
            this.sb.append(INDENT).append("private static Map<String, String> predefinedImages = new HashMap<String, String>();\n\n");
        }

        this.sb.append(INDENT).append("private Hal hal = new Hal(brickConfiguration, usedSensors);\n");
        generateUserDefinedMethods();
        this.sb.append("\n");
        this.sb.append(INDENT).append("public static void main(String[] args) {\n");
        this.sb.append(INDENT).append(INDENT).append("try {\n");
        this.sb.append(INDENT).append(INDENT).append(INDENT).append(generateRegenerateConfiguration()).append("\n");

        this.sb.append(generateUsedImages()).append("\n");
        this.sb.append(INDENT).append(INDENT).append(INDENT).append("new ").append(this.programName).append("().run();\n");
        this.sb.append(INDENT).append(INDENT).append("} catch ( Exception e ) {\n");
        this.sb.append(INDENT).append(INDENT).append(INDENT).append("Hal.displayExceptionWaitForKeyPress(e);\n");
        this.sb.append(INDENT).append(INDENT).append("}\n");
        this.sb.append(INDENT).append("}");
    }

    @Override
    protected void generateProgramSuffix(boolean withWrapping) {
        if ( withWrapping ) {
            if ( this.isInDebugMode ) {
                this.sb.append("\n");
                this.sb.append(INDENT).append(INDENT).append("hal.closeResources();\n");
            }

            this.sb.append(INDENT).append("}");
        }
        this.sb.append("\n").append("}");
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
        waitTimeStmt.getTime().visit(this);
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
                volumeAction.getVolume().visit(this);
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

    private String getLanguageString(ILanguage language) {
        switch ( (Language) language ) {
            case GERMAN:
                return "de";
            case ENGLISH:
                return "en";
            case FRENCH:
                return "fr";
            case SPANISH:
                return "es";
            case ITALIAN:
                return "it";
            case DUTCH:
                return "nl";
            case FINNISH:
                return "fi";
            case POLISH:
                return "pl";
            case RUSSIAN:
                return "ru";
            case TURKISH:
                return "tu";
            case CZECH:
                return "cs";
            case PORTUGUESE:
                return "pt-pt";
            case DANISH:
                return "da";
            default:
                return "en";
        }
    }

    @Override
    public Void visitSetLanguageAction(SetLanguageAction<Void> setLanguageAction) {
        if ( !this.brickConfiguration.getRobotName().equals("ev3lejos") ) {
            this.sb.append("hal.setLanguage(\"");
            this.sb.append(this.getLanguageString(setLanguageAction.getLanguage()));
            this.sb.append("\");");
        }
        return null;
    }

    @Override
    public Void visitSayTextAction(SayTextAction<Void> sayTextAction) {
        if ( !this.brickConfiguration.getRobotName().equals("ev3lejos") ) {
            this.sb.append("hal.sayText(");
            if ( !sayTextAction.getMsg().getKind().hasName("STRING_CONST") ) {
                this.sb.append("String.valueOf(");
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
        showPictureAction.getX().visit(this);
        this.sb.append(", ");
        showPictureAction.getY().visit(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction<Void> showTextAction) {
        this.sb.append("hal.drawText(");
        if ( !showTextAction.getMsg().getKind().hasName("STRING_CONST") ) {
            this.sb.append("String.valueOf(");
            showTextAction.getMsg().visit(this);
            this.sb.append(")");
        } else {
            showTextAction.getMsg().visit(this);
        }
        this.sb.append(", ");
        showTextAction.getX().visit(this);
        this.sb.append(", ");
        showTextAction.getY().visit(this);
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        this.sb.append("hal.playTone(");
        toneAction.getFrequency().visit(this);
        this.sb.append(", ");
        toneAction.getDuration().visit(this);
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

    private boolean isActorOnPort(IActorPort port) {
        boolean isActorOnPort = false;
        for ( UsedActor actor : this.usedActors ) {
            isActorOnPort = isActorOnPort ? isActorOnPort : actor.getPort().equals(port);
        }
        return isActorOnPort;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        if ( isActorOnPort(motorOnAction.getPort()) ) {
            String methodName;

            boolean isRegulated = this.brickConfiguration.isMotorRegulated(motorOnAction.getPort());
            boolean duration = motorOnAction.getParam().getDuration() != null;
            if ( duration ) {
                methodName = isRegulated ? "hal.rotateRegulatedMotor(" : "hal.rotateUnregulatedMotor(";
            } else {
                methodName = isRegulated ? "hal.turnOnRegulatedMotor(" : "hal.turnOnUnregulatedMotor(";
            }
            this.sb.append(methodName + "ActorPort." + motorOnAction.getPort().getOraName() + ", ");
            motorOnAction.getParam().getSpeed().visit(this);
            if ( duration ) {
                this.sb.append(", " + getEnumCode(motorOnAction.getDurationMode()));
                this.sb.append(", ");
                motorOnAction.getDurationValue().visit(this);
            }
            this.sb.append(");");
        }
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction<Void> motorSetPowerAction) {
        if ( isActorOnPort(motorSetPowerAction.getPort()) ) {
            boolean isRegulated = this.brickConfiguration.isMotorRegulated(motorSetPowerAction.getPort());
            String methodName = isRegulated ? "hal.setRegulatedMotorSpeed(" : "hal.setUnregulatedMotorSpeed(";
            this.sb.append(methodName + "ActorPort." + motorSetPowerAction.getPort().getOraName() + ", ");
            motorSetPowerAction.getPower().visit(this);
            this.sb.append(");");
        }
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction<Void> motorGetPowerAction) {
        if ( isActorOnPort(motorGetPowerAction.getPort()) ) {
            boolean isRegulated = this.brickConfiguration.isMotorRegulated(motorGetPowerAction.getPort());
            String methodName = isRegulated ? "hal.getRegulatedMotorSpeed(" : "hal.getUnregulatedMotorSpeed(";
            this.sb.append(methodName + "ActorPort." + motorGetPowerAction.getPort().getOraName() + ")");
        }
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        if ( isActorOnPort(motorStopAction.getPort()) ) {
            boolean isRegulated = this.brickConfiguration.isMotorRegulated(motorStopAction.getPort());
            String methodName = isRegulated ? "hal.stopRegulatedMotor(" : "hal.stopUnregulatedMotor(";
            this.sb.append(methodName + "ActorPort." + motorStopAction.getPort().getOraName() + ", " + getEnumCode(motorStopAction.getMode()) + ");");
        }
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction<Void> driveAction) {
        boolean isDuration = driveAction.getParam().getDuration() != null;
        String methodName = isDuration ? "hal.driveDistance(" : "hal.regulatedDrive(";
        this.sb.append(methodName);
        this.sb.append(getEnumCode(driveAction.getDirection()) + ", ");
        driveAction.getParam().getSpeed().visit(this);
        if ( isDuration ) {
            this.sb.append(", ");
            driveAction.getParam().getDuration().getValue().visit(this);
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction<Void> curveAction) {
        MotorDuration<Void> duration = curveAction.getParamLeft().getDuration();

        this.sb.append("hal.driveInCurve(");
        this.sb.append(getEnumCode(curveAction.getDirection()) + ", ");
        curveAction.getParamLeft().getSpeed().visit(this);
        this.sb.append(", ");
        curveAction.getParamRight().getSpeed().visit(this);
        if ( duration != null ) {
            this.sb.append(", ");
            duration.getValue().visit(this);
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
        turnAction.getParam().getSpeed().visit(this);
        if ( isDuration ) {
            this.sb.append(", ");
            turnAction.getParam().getDuration().getValue().visit(this);
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
    public Void visitBrickSensor(BrickSensor<Void> brickSensor) {
        String brickSensorPort = "BrickKey." + brickSensor.getPort().getOraName();
        switch ( (BrickKeyPressMode) brickSensor.getMode() ) {
            case PRESSED:
                this.sb.append("hal.isPressed(" + brickSensorPort + ")");
                break;
            case WAIT_FOR_PRESS_AND_RELEASE:
                this.sb.append("hal.isPressedAndReleased(" + brickSensorPort + ")");
                break;
            default:
                throw new DbcException("Invalide mode for BrickSensor!");
        }
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor<Void> colorSensor) {
        String colorSensorPort = "SensorPort." + colorSensor.getPort().getCodeName();
        switch ( (ColorSensorMode) colorSensor.getMode() ) {
            case AMBIENTLIGHT:
                this.sb.append("hal.getColorSensorAmbient(" + colorSensorPort + ")");
                break;
            case COLOUR:
                this.sb.append("hal.getColorSensorColour(" + colorSensorPort + ")");
                break;
            case LIGHT:
                this.sb.append("hal.getColorSensorRed(" + colorSensorPort + ")");
                break;
            case RGB:
                this.sb.append("hal.getColorSensorRgb(" + colorSensorPort + ")");
                break;
            default:
                throw new DbcException("Invalide mode for Color Sensor!");
        }
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        IActorPort encoderMotorPort = (IActorPort) encoderSensor.getPort();
        boolean isRegulated = this.brickConfiguration.isMotorRegulated(encoderMotorPort);
        if ( encoderSensor.getMode() == MotorTachoMode.RESET ) {
            String methodName = isRegulated ? "hal.resetRegulatedMotorTacho(" : "hal.resetUnregulatedMotorTacho(";
            this.sb.append(methodName + "ActorPort." + encoderMotorPort.getOraName() + ");");
        } else {
            String methodName = isRegulated ? "hal.getRegulatedMotorTachoValue(" : "hal.getUnregulatedMotorTachoValue(";
            this.sb.append(methodName + "ActorPort." + encoderMotorPort.getOraName() + ", " + getEnumCode(encoderSensor.getMode()) + ")");
        }
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        String gyroSensorPort = "SensorPort." + gyroSensor.getPort().getCodeName();
        switch ( (GyroSensorMode) gyroSensor.getMode() ) {
            case ANGLE:
                this.sb.append("hal.getGyroSensorAngle(" + gyroSensorPort + ")");
                break;
            case RATE:
                this.sb.append("hal.getGyroSensorRate(" + gyroSensorPort + ")");
                break;
            case RESET:
                this.sb.append("hal.resetGyroSensor(" + gyroSensorPort + ");");
                break;
            default:
                throw new DbcException("Invalid GyroSensorMode");
        }
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        String infraredSensorPort = "SensorPort." + infraredSensor.getPort().getCodeName();
        switch ( (InfraredSensorMode) infraredSensor.getMode() ) {
            case DISTANCE:
                this.sb.append("hal.getInfraredSensorDistance(" + infraredSensorPort + ")");
                break;
            case PRESENCE:
                this.sb.append("hal.getInfraredSensorSeek(" + infraredSensorPort + ")");
                break;
            default:
                throw new DbcException("Invalid Infrared Sensor Mode: " + infraredSensor.getMode());
        }
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        String timerNumber = timerSensor.getPort().getOraName();
        switch ( (TimerSensorMode) timerSensor.getMode() ) {
            case DEFAULT:
            case VALUE:
                this.sb.append("hal.getTimerValue(" + timerNumber + ")");
                break;
            case RESET:
                this.sb.append("hal.resetTimer(" + timerNumber + ");");
                break;
            default:
                throw new DbcException("Invalid Time Mode!");
        }
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor<Void> touchSensor) {
        this.sb.append("hal.isPressed(" + "SensorPort." + touchSensor.getPort().getCodeName() + ")");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        String ultrasonicSensorPort = "SensorPort." + ultrasonicSensor.getPort().getCodeName();
        if ( ultrasonicSensor.getMode() == UltrasonicSensorMode.DISTANCE ) {
            this.sb.append("hal.getUltraSonicSensorDistance(" + ultrasonicSensorPort + ")");
        } else {
            this.sb.append("hal.getUltraSonicSensorPresence(" + ultrasonicSensorPort + ")");
        }
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor<Void> soundSensor) {
        this.sb.append("hal.getSoundLevel(" + "SensorPort." + soundSensor.getPort().getCodeName() + ")");
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor<Void> compassSensor) {
        String compassSensorPort = "SensorPort." + compassSensor.getPort().getCodeName();
        switch ( (CompassSensorMode) compassSensor.getMode() ) {
            case CALIBRATE:
                this.sb.append("hal.hiTecCompassStartCalibration(" + compassSensorPort + ");");
                nlIndent();
                this.sb.append("hal.waitFor(40000);");
                nlIndent();
                this.sb.append("hal.hiTecCompassStopCalibration(" + compassSensorPort + ");");
                break;
            case ANGLE:
                this.sb.append("hal.getHiTecCompassAngle(" + compassSensorPort + ")");
                break;
            case COMPASS:
                this.sb.append("hal.getHiTecCompassCompass(" + compassSensorPort + ")");
                break;
            default:
                throw new DbcException("Invalid Compass Mode!");
        }
        return null;
    }

    @Override
    public Void visitIRSeekerSensor(IRSeekerSensor<Void> irSeekerSensor) {
        String irSeekerSensorPort = "SensorPort." + irSeekerSensor.getPort().getCodeName();
        switch ( (IRSeekerSensorMode) irSeekerSensor.getMode() ) {
            case MODULATED:
                this.sb.append("hal.getHiTecIRSeekerModulated(" + irSeekerSensorPort + ")");
                break;
            case UNMODULATED:
                this.sb.append("hal.getHiTecIRSeekerUnmodulated(" + irSeekerSensorPort + ")");
                break;
            default:
                throw new DbcException("Invalid IRSeeker Mode!");
        }
        return null;
    }

    @Override
    public Void visitMainTask(MainTask<Void> mainTask) {
        mainTask.getVariables().visit(this);
        this.sb.append("\n\n").append(INDENT).append("public void run() throws Exception {\n");
        incrIndentation();
        // this is needed for testing
        if ( mainTask.getDebug().equals("TRUE") ) {
            this.sb.append(INDENT).append(INDENT).append("hal.startLogging();");
            //this.sb.append(INDENT).append(INDENT).append(INDENT).append("\nhal.startScreenLoggingThread();");
            this.isInDebugMode = true;
        }
        if ( this.isSayTextUsed && !this.brickConfiguration.getRobotName().equals("ev3lejos") ) {
            this.sb.append("\n");
            this.sb.append(INDENT).append(INDENT).append("hal.setLanguage(\"");
            this.sb.append(this.getLanguageString(this.language));
            this.sb.append("\");");
        }
        return null;
    }

    @Override
    public Void visitGetSubFunct(GetSubFunct<Void> getSubFunct) {
        this.sb.append("BlocklyMethods.listsGetSubList( ");
        getSubFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        IndexLocation where1 = (IndexLocation) getSubFunct.getStrParam().get(0);
        this.sb.append(getEnumCode(where1));
        if ( (where1 == IndexLocation.FROM_START) || (where1 == IndexLocation.FROM_END) ) {
            this.sb.append(", ");
            getSubFunct.getParam().get(1).visit(this);
        }
        this.sb.append(", ");
        IndexLocation where2 = (IndexLocation) getSubFunct.getStrParam().get(1);
        this.sb.append(getEnumCode(where2));
        if ( (where2 == IndexLocation.FROM_START) || (where2 == IndexLocation.FROM_END) ) {
            this.sb.append(", ");
            if ( getSubFunct.getParam().size() == 3 ) {
                getSubFunct.getParam().get(2).visit(this);
            } else {
                getSubFunct.getParam().get(1).visit(this);
            }
        }
        this.sb.append(")");
        return null;

    }

    @Override
    public Void visitIndexOfFunct(IndexOfFunct<Void> indexOfFunct) {
        String methodName = "BlocklyMethods.findFirst( ";
        if ( indexOfFunct.getLocation() == IndexLocation.LAST ) {
            methodName = "BlocklyMethods.findLast( ";
        }
        this.sb.append(methodName);
        indexOfFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        if ( indexOfFunct.getParam().get(1).getVarType() == BlocklyType.NUMBER ) {
            this.sb.append(" (float) ");
        }
        indexOfFunct.getParam().get(1).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitLengthOfIsEmptyFunct(LengthOfIsEmptyFunct<Void> lengthOfIsEmptyFunct) {
        String methodName = "BlocklyMethods.length( ";
        if ( lengthOfIsEmptyFunct.getFunctName() == FunctionNames.LIST_IS_EMPTY ) {
            methodName = "BlocklyMethods.isEmpty( ";
        }
        this.sb.append(methodName);
        lengthOfIsEmptyFunct.getParam().get(0).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitEmptyList(EmptyList<Void> emptyList) {
        this.sb.append(
            "new ArrayList<"
                + getLanguageVarTypeFromBlocklyType(emptyList.getTypeVar()).substring(0, 1).toUpperCase()
                + getLanguageVarTypeFromBlocklyType(emptyList.getTypeVar()).substring(1).toLowerCase()
                + ">()");
        return null;
    }

    @Override
    public Void visitListCreate(ListCreate<Void> listCreate) {
        this.sb.append("BlocklyMethods.createListWith" + listCreate.getTypeVar().getBlocklyName() + "(");
        listCreate.getValue().visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitListRepeat(ListRepeat<Void> listRepeat) {
        this.sb.append("BlocklyMethods.createListWithItem(");
        if ( listRepeat.getParam().get(0).getVarType() == BlocklyType.NUMBER ) {
            this.sb.append(" (float) ");
        }
        listRepeat.getParam().get(0).visit(this);
        this.sb.append(", ");
        listRepeat.getParam().get(1).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitListGetIndex(ListGetIndex<Void> listGetIndex) {
        this.sb.append("BlocklyMethods.listsIndex(");
        listGetIndex.getParam().get(0).visit(this);
        this.sb.append(", ");
        this.sb.append(getEnumCode(listGetIndex.getElementOperation()));
        this.sb.append(", ");
        this.sb.append(getEnumCode(listGetIndex.getLocation()));
        if ( listGetIndex.getParam().size() == 2 ) {
            this.sb.append(", ");
            listGetIndex.getParam().get(1).visit(this);
        }
        this.sb.append(")");
        if ( listGetIndex.getElementOperation().isStatment() ) {
            this.sb.append(";");
        }
        return null;
    }

    @Override
    public Void visitListSetIndex(ListSetIndex<Void> listSetIndex) {
        this.sb.append("BlocklyMethods.listsIndex(");
        listSetIndex.getParam().get(0).visit(this);
        this.sb.append(", ");
        this.sb.append(getEnumCode(listSetIndex.getElementOperation()));
        this.sb.append(", ");
        if ( listSetIndex.getParam().get(1).getVarType() == BlocklyType.NUMBER ) {
            this.sb.append(" (float) ");
        }
        listSetIndex.getParam().get(1).visit(this);
        this.sb.append(", ");
        this.sb.append(getEnumCode(listSetIndex.getLocation()));
        if ( listSetIndex.getParam().size() == 3 ) {
            this.sb.append(", ");
            listSetIndex.getParam().get(2).visit(this);
        }
        this.sb.append(");");
        return null;
    }

    @Override
    public Void visitMathConstrainFunct(MathConstrainFunct<Void> mathConstrainFunct) {
        this.sb.append("BlocklyMethods.clamp(");
        mathConstrainFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        mathConstrainFunct.getParam().get(1).visit(this);
        this.sb.append(", ");
        mathConstrainFunct.getParam().get(2).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathNumPropFunct(MathNumPropFunct<Void> mathNumPropFunct) {
        switch ( mathNumPropFunct.getFunctName() ) {
            case EVEN:
                this.sb.append("BlocklyMethods.isEven(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case ODD:
                this.sb.append("BlocklyMethods.isOdd(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case PRIME:
                this.sb.append("BlocklyMethods.isPrime(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case WHOLE:
                this.sb.append("BlocklyMethods.isWhole(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case POSITIVE:
                this.sb.append("BlocklyMethods.isPositive(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case NEGATIVE:
                this.sb.append("BlocklyMethods.isNegative(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(")");
                break;
            case DIVISIBLE_BY:
                this.sb.append("BlocklyMethods.isDivisibleBy(");
                mathNumPropFunct.getParam().get(0).visit(this);
                this.sb.append(", ");
                mathNumPropFunct.getParam().get(1).visit(this);
                this.sb.append(")");
                break;
            default:
                break;
        }
        return null;
    }

    @Override
    public Void visitMathOnListFunct(MathOnListFunct<Void> mathOnListFunct) {
        switch ( mathOnListFunct.getFunctName() ) {
            case SUM:
                this.sb.append("BlocklyMethods.sumOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MIN:
                this.sb.append("BlocklyMethods.minOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MAX:
                this.sb.append("BlocklyMethods.maxOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case AVERAGE:
                this.sb.append("BlocklyMethods.averageOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MEDIAN:
                this.sb.append("BlocklyMethods.medianOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case STD_DEV:
                this.sb.append("BlocklyMethods.standardDeviatioin(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case RANDOM:
                this.sb.append("BlocklyMethods.randOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            case MODE:
                this.sb.append("BlocklyMethods.modeOnList(");
                mathOnListFunct.getParam().get(0).visit(this);
                break;
            default:
                break;
        }
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathRandomFloatFunct(MathRandomFloatFunct<Void> mathRandomFloatFunct) {
        this.sb.append("BlocklyMethods.randDouble()");
        return null;
    }

    @Override
    public Void visitMathRandomIntFunct(MathRandomIntFunct<Void> mathRandomIntFunct) {
        this.sb.append("BlocklyMethods.randInt(");
        mathRandomIntFunct.getParam().get(0).visit(this);
        this.sb.append(", ");
        mathRandomIntFunct.getParam().get(1).visit(this);
        this.sb.append(")");
        return null;
    }

    @Override
    public Void visitMathPowerFunct(MathPowerFunct<Void> mathPowerFunct) {
        this.sb.append("BlocklyMethods.pow(");
        super.visitMathPowerFunct(mathPowerFunct);
        return null;
    }

    @Override
    public Void visitTextJoinFunct(TextJoinFunct<Void> textJoinFunct) {
        this.sb.append("BlocklyMethods.textJoin(");
        textJoinFunct.getParam().visit(this);
        this.sb.append(")");
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
            this.sb.append("String.valueOf(");
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
        if ( !bluetoothSendAction.getMsg().getKind().hasName("STRING_CONST") ) {
            this.sb.append("String.valueOf(");
            bluetoothSendAction.getMsg().visit(this);
            this.sb.append(")");
        } else {
            bluetoothSendAction.getMsg().visit(this);
        }
        this.sb.append(", ");
        bluetoothSendAction.getConnection().visit(this);
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

    /**
     * @return Java code used in the code generation to regenerates the same brick configuration
     */
    public String generateRegenerateConfiguration() {
        StringBuilder sb = new StringBuilder();
        sb.append(" brickConfiguration = new EV3Configuration.Builder()\n");
        sb.append(INDENT).append(INDENT).append(INDENT).append("    .setWheelDiameter(" + this.brickConfiguration.getWheelDiameterCM() + ")\n");
        sb.append(INDENT).append(INDENT).append(INDENT).append("    .setTrackWidth(" + this.brickConfiguration.getTrackWidthCM() + ")\n");
        appendActors(sb);
        appendSensors(sb);
        sb.append(INDENT).append(INDENT).append(INDENT).append("    .build();");
        return sb.toString();
    }

    private String generateUsedImages() {
        StringBuilder sb = new StringBuilder();
        for ( String image : this.usedImages ) {
            sb.append(INDENT).append(INDENT).append(INDENT).append("predefinedImages.put(\"" + image + "\", \"" + this.predefinedImage.get(image) + "\");\n");
        }
        return sb.toString();
    }

    private void generateImports() {
        this.sb.append("package generated.main;\n\n");
        this.sb.append("import de.fhg.iais.roberta.runtime.*;\n");
        this.sb.append("import de.fhg.iais.roberta.runtime.ev3.*;\n\n");

        this.sb.append("import de.fhg.iais.roberta.mode.general.*;\n");
        this.sb.append("import de.fhg.iais.roberta.mode.action.*;\n");
        this.sb.append("import de.fhg.iais.roberta.mode.sensor.*;\n");
        this.sb.append("import de.fhg.iais.roberta.mode.action.ev3.*;\n");
        this.sb.append("import de.fhg.iais.roberta.mode.sensor.ev3.*;\n\n");

        this.sb.append("import de.fhg.iais.roberta.components.*;\n\n");

        this.sb.append("import java.util.LinkedHashSet;\n");
        this.sb.append("import java.util.HashMap;\n");
        this.sb.append("import java.util.Set;\n");
        this.sb.append("import java.util.Map;\n");
        this.sb.append("import java.util.List;\n");
        this.sb.append("import java.util.ArrayList;\n");
        this.sb.append("import java.util.Arrays;\n\n");

        this.sb.append("import lejos.remote.nxt.NXTConnection;\n\n");
    }

    private void appendSensors(StringBuilder sb) {
        for ( Map.Entry<ISensorPort, Sensor> entry : this.brickConfiguration.getSensors().entrySet() ) {
            sb.append(INDENT).append(INDENT).append(INDENT);
            sb.append("    .addSensor(");
            sb.append("SensorPort." + entry.getKey().getCodeName()).append(", ");
            sb.append(generateRegenerateSensor(entry.getValue()));
            sb.append(")\n");
        }
    }

    private void appendActors(StringBuilder sb) {
        for ( Map.Entry<IActorPort, Actor> entry : this.brickConfiguration.getActors().entrySet() ) {
            sb.append(INDENT).append(INDENT).append(INDENT);
            sb.append("    .addActor(");
            sb.append("ActorPort." + entry.getKey().getOraName()).append(", ");
            sb.append(generateRegenerateActor(entry.getValue()));
            sb.append(")\n");
        }
    }

    private String generateRegenerateUsedSensors() {
        StringBuilder sb = new StringBuilder();
        String arrayOfSensors = "";
        for ( UsedSensor usedSensor : this.usedSensors ) {
            arrayOfSensors += generateRegenerateUsedSensor(usedSensor);
            arrayOfSensors += ", ";
        }

        sb.append("private Set<UsedSensor> usedSensors = " + "new LinkedHashSet<UsedSensor>(");
        if ( !this.usedSensors.isEmpty() ) {
            sb.append("Arrays.asList(" + arrayOfSensors.substring(0, arrayOfSensors.length() - 2) + ")");
        }
        sb.append(");");
        return sb.toString();
    }

    private String generateRegenerateUsedSensor(UsedSensor usedSensor) {
        StringBuilder sb = new StringBuilder();
        SensorType sensor = (SensorType) usedSensor.getType();
        IMode mode = usedSensor.getMode();

        sb.append("new UsedSensor(");
        sb.append("SensorPort." + usedSensor.getPort().getCodeName()).append(", ");
        sb.append(sensor.getClass().getSimpleName() + "." + sensor.name()).append(", ");
        sb.append(mode.getClass().getSimpleName() + "." + mode.toString()).append(")");
        return sb.toString();
    }

    private String generateRegenerateActor(Actor actor) {
        StringBuilder sb = new StringBuilder();

        sb.append("new Actor(").append(getActorTypeCode(actor.getName()));
        sb.append(", ").append(actor.isRegulated());
        sb.append(", ").append(getEnumCode(actor.getRotationDirection())).append(", ").append(getEnumCode(actor.getMotorSide())).append(")");
        return sb.toString();
    }

    private String generateRegenerateSensor(Sensor sensor) {
        StringBuilder sb = new StringBuilder();
        sb.append("new Sensor(").append(getSensorTypeCode(sensor.getType()));
        sb.append(")");
        return sb.toString();
    }

    private String getActorTypeCode(ActorType type) {
        return type.getClass().getSimpleName() + "." + type.name();
    }

    private String getSensorTypeCode(SensorType type) {
        return type.getClass().getSimpleName() + "." + type.name();
    }

    private void initPredefinedImages() {
        this.predefinedImage.put(
            "OLDGLASSES",
            "\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u0007\\u00fc\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u0001\\u00f0\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u003f\\u00f0\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u0000\\u00f0\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u000f\\u00c0\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u00f0\\u003f\\u0000\\u00e0\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u0003\\u00c0\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u00f8\\u001f\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u00fc\\u000f\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u00e0\\u007f\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0007\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u00f0\\u003f\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u00ff\\u0003\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u00f8\\u001f\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0080\\u00ff\\u0001\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u00f8\\u000f\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u00c0\\u00ff\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u00fc\\u0007\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u00c0\\u00ff\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u00fe\\u0003\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u00c0\\u007f\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u00fe\\u0001\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u00e0\\u003f\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u00ff\\u0001\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u00f0\\u003f\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u00ff\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u00f0\\u001f\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0080\\u00ff\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u001f\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0080\\u007f\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u000f\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u00c0\\u007f\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u00c0\\u003f\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u00fc\\u0007\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u00c0\\u003f\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u00fc\\u0007\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u00e0\\u001f\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u00e0\\u001f\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u00e0\\u001f\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u00fe\\u0003\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u00e0\\u001f\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0080\\u0001\\u00fe\\u0003\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u00e0\\u001f\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00f0\\u000f\\u00ff\\u0003\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u00f0\\u001f\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00f8\\u001f\\u00ff\\u0003\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00f0\\u003f\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u003e\\u007c\\u00ff\\u0007\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00f0\\u003f\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u00f8\\u00ff\\u0007\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00f0\\u00ff\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u00f0\\u00ff\\u001f\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00e3\\u00c7\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00fb\\u00df\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000");
        this.predefinedImage.put(
            "EYESOPEN",
            "\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0040\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00b0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00b0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00b0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00d8\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00d8\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00d8\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00d8\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00d8\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00d8\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00cc\\u0060\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u00cc\\u007c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u0003\\u0000\\u0000\\u0000\\u00cc\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u008c\\u0037\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u000c\\u0018\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u000c\\u0018\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u0000\\u0000\\u000c\\u001c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u0000\\u0000\\u0006\\u000c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u007f\\u0000\\u0000\\u0006\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u003f\\u0000\\u0000\\u0006\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u001e\\u0000\\u0000\\u0006\\u0007\\u0000\\u0000\\u0000\\u0000\\u0080\\u0000\\u0000\\u0000\\u00fc\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0006\\u0003\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0001\\u0000\\u0000\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0006\\u0003\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0003\\u0000\\u00c0\\u003f\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0083\\u0001\\u0000\\u0000\\u0000\\u0000\\u0060\\u0003\\u0000\\u00f0\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0083\\u0001\\u0000\\u0000\\u0000\\u0000\\u0060\\u0003\\u0000\\u00f8\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c3\\u0000\\u0000\\u0000\\u0000\\u0000\\u0020\\u0003\\u0000\\u00fc\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c3\\u0018\\u0000\\u0000\\u0000\\u0018\\u0030\\u0003\\u0000\\u00fe\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u001f\\u0000\\u0000\\u0000\\u0000\\u00c3\\u003f\\u0000\\u0000\\u0000\\u00fc\\u0030\\u0003\\u0080\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0083\\u001f\\u0000\\u0000\\u0000\\u00ee\\u003b\\u0003\\u00c0\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0080\\u0001\\u000e\\u0000\\u0000\\u0000\\u0086\\u001f\\u0003\\u00e0\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0007\\u00e0\\u001f\\u0000\\u0000\\u0080\\u0001\\u0007\\u0000\\u0000\\u0000\\u0006\\u001e\\u0003\\u00f0\\u000f\\u0000\\u0000\\u00fe\\u0007\\u0000\\u0000\\u00fc\\u0000\\u0000\\u003f\\u0000\\u0000\\u0080\\u0081\\u0003\\u0000\\u0000\\u0000\\u000c\\u0000\\u0003\\u00f8\\u0007\\u0000\\u00e0\\u00ff\\u007f\\u0000\\u0000\\u003f\\u0000\\u0000\\u00fc\\u0000\\u00c0\\u009f\\u00c1\\u0001\\u0000\\u0000\\u0000\\u000c\\u0000\\u0003\\u00f8\\u0007\\u0000\\u00fc\\u00ff\\u00ff\\u0003\\u0080\\u000f\\u0000\\u0000\\u00f0\\u0081\\u00ff\\u009f\\u00e1\\u0000\\u0000\\u0000\\u0000\\u0018\\u0000\\u0003\\u00e0\\u0003\\u0000\\u00ff\\u0001\\u00f8\\u000f\\u00c0\\u0007\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u009f\\u0071\\u0000\\u0000\\u0000\\u0000\\u0018\\u0000\\u0003\\u00c0\\u0003\\u0080\\u001f\\u0000\\u0080\\u001f\\u00e0\\u0001\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u009f\\u003b\\u0000\\u0000\\u0000\\u0000\\u0030\\u0000\\u0003\\u00c0\\u0001\\u00e0\\u0007\\u0000\\u0000\\u007e\\u00f0\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u001f\\u001f\\u0000\\u0000\\u0000\\u0000\\u0060\\u0000\\u0003\\u0000\\u0000\\u00f0\\u0001\\u0000\\u0000\\u00f8\\u0070\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u001f\\u000c\\u0000\\u0000\\u0000\\u0000\\u0060\\u0000\\u0003\\u0000\\u0000\\u00f8\\u0000\\u0000\\u0000\\u00f0\\u0079\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0000\\u0003\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u00c0\\u003f\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0001\\u0003\\u0000\\u0000\\u001e\\u0000\\u0000\\u0000\\u0080\\u001f\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0003\\u0003\\u0000\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u001f\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0003\\u0003\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0006\\u0003\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u007e\\u000c\\u0003\\u0000\\u00c0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f6\\r\\u0003\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u001f\\u0000\\u00e0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c6\\u000f\\u0003\\u0000\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u00c0\\u007f\\u0000\\u00e0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0006\\u0003\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u0080\\u0003\\u0000\\u00e0\\u00c3\\u0000\\u00c0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u000c\\u0000\\u0007\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u0080\\u0003\\u0000\\u00f0\\u0081\\u0001\\u00c0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0018\\u0000\\u0007\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u0080\\u0003\\u0000\\u00f0\\u0081\\u0001\\u00c0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0018\\u0000\\u0006\\u0000\\u0038\\u0000\\u0000\\u0000\\u003e\\u0080\\u0003\\u0000\\u00f8\\u0081\\u0003\\u00c0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0030\\u0000\\u0006\\u0000\\u0038\\u0000\\u0000\\u0080\\u00ff\\u0080\\u0003\\u0000\\u00f8\\u0081\\u0003\\u00c0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0030\\u0000\\u0006\\u0000\\u0038\\u0000\\u0000\\u00e0\\u00ff\\u0083\\u0003\\u0000\\u00f8\\u00c3\\u0003\\u00c0\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0060\\u0000\\u0006\\u0000\\u003f\\u0000\\u0000\\u00e0\\u000f\\u0083\\u0003\\u0000\\u00f8\\u00ff\\u0003\\u00c0\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0000\\u0006\\u00f0\\u001f\\u0000\\u0000\\u00f0\\u0007\\u0086\\u0003\\u0000\\u00f8\\u00ff\\u0003\\u00c0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0001\\u0006\\u00ff\\u001f\\u0000\\u0000\\u00f0\\u0007\\u0086\\u0003\\u0000\\u00f0\\u00ff\\u0001\\u00c0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0001\\u00fe\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u0007\\u008e\\u0003\\u0000\\u00f0\\u00ff\\u0001\\u00c0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0003\\u00fe\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u0007\\u000e\\u0007\\u0000\\u00e0\\u00ff\\u0000\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u00fe\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u000f\\u000f\\u0007\\u0000\\u00c0\\u007f\\u0000\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0006\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u00ff\\u000f\\u0007\\u0000\\u0000\\u001f\\u0000\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u00ff\\u000f\\u000f\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003c\\u00fb\\u00ff\\u001f\\u0000\\u0000\\u00f0\\u00ff\\u0007\\u000e\\u0000\\u0000\\u0000\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00fb\\u00ff\\u001f\\u0000\\u0000\\u00f0\\u00ff\\u0007\\u001e\\u0000\\u0000\\u0000\\u0000\\u0078\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00f9\\u00ff\\u001f\\u0000\\u0000\\u00e0\\u00ff\\u0003\\u001c\\u0000\\u0000\\u0000\\u0000\\u0038\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u00e0\\u00ff\\u0003\\u003c\\u0000\\u0000\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u0080\\u00ff\\u0000\\u0078\\u0000\\u0000\\u0000\\u0000\\u001e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u003f\\u0000\\u0000\\u0000\\u003e\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0007\\u0000\\u0000\\u00e0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u000f\\u0000\\u0000\\u00f0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u003f\\u0000\\u0000\\u00fc\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u0000\\u0000\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u0007\\u00e0\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00c3\\u0003\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u001f\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0001\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00fb\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u001e\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0001\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0007\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u001f\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u0001\\u00f8\\u00ef\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00f3\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u007f\\u00f0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0007\\u00f0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000");
        this.predefinedImage.put(
            "EYESCLOSED",
            "\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0038\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0000\\u0000\\u0000\\u00f0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0001\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u0001\\u0000\\u0000\\u0078\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u0001\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u0001\\u0000\\u0000\\u001c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0030\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0007\\u00e0\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0007\\u0000\\u0000\\u00fc\\u0000\\u0000\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u007f\\u0000\\u0000\\u003f\\u0000\\u0000\\u00fc\\u0000\\u00c0\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u0003\\u0080\\u000f\\u0000\\u0000\\u00f0\\u0081\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u0001\\u00f8\\u000f\\u00c0\\u0007\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u001f\\u0000\\u0080\\u001f\\u00e0\\u0001\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0007\\u0000\\u0000\\u007e\\u00f0\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0001\\u0000\\u0000\\u00f8\\u0070\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0000\\u0000\\u0000\\u00f0\\u0079\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u00c0\\u003f\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u001e\\u0000\\u0000\\u0000\\u0080\\u001f\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u001f\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u003f\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u007f\\u0000\\u00e0\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u007f\\u0000\\u00fe\\u00c3\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u007f\\u00f8\\u001f\\u0080\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u00fc\\u00c3\\u00ff\\u007f\\u00f8\\u0001\\u00c0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u0080\\u0003\\u0000\\u0000\\u0080\\u00ff\\u00c7\\u00ff\\u007f\\u000c\\u0000\\u0060\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0038\\u0000\\u0000\\u0000\\u003e\\u0080\\u0003\\u0000\\u0000\\u00fc\\u00ff\\u00cf\\u00ff\\u007f\\u000c\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0038\\u0000\\u0000\\u0080\\u00ff\\u0080\\u0003\\u0000\\u00c0\\u00ff\\u00ff\\u00cf\\u00ff\\u007f\\u000c\\u0000\\u0038\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0038\\u0000\\u0000\\u00e0\\u00ff\\u0083\\u0003\\u0000\\u00fe\\u00ff\\u00ff\\u00c7\\u00ff\\u0007\\u0006\\u0000\\u001c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003f\\u0000\\u0000\\u00e0\\u000f\\u0083\\u0003\\u00c0\\u00ff\\u00ff\\u00ff\\u00c1\\u003f\\u0000\\u0006\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u001f\\u0000\\u0000\\u00f0\\u0007\\u0086\\u0003\\u00fe\\u00ff\\u00ff\\u0003\\u00c0\\u0001\\u0000\\u001e\\u0000\\u00fe\\u0003\\u0000\\u0000\\u0000\\u0000\\u00ff\\u001f\\u0000\\u0000\\u00f0\\u0007\\u0086\\u0083\\u00ff\\u007f\\u0000\\u0000\\u00c0\\u0001\\u0000\\u003c\\u0000\\u00fe\\u0003\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u0007\\u008e\\u00c3\\u007f\\u0000\\u0000\\u0000\\u00c0\\u0001\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u0007\\u000e\\u00c7\\u0003\\u0000\\u0000\\u0000\\u00e0\\u0000\\u0000\\u00c0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u000f\\u000f\\u0007\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0000\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u00ff\\u000f\\u0007\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u001f\\u0000\\u0000\\u00f8\\u00ff\\u000f\\u000f\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u001f\\u0000\\u0000\\u00f0\\u00ff\\u0007\\u000e\\u0000\\u0000\\u0000\\u0000\\u0070\\u0000\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u001f\\u0000\\u0000\\u00f0\\u00ff\\u0007\\u001e\\u0000\\u0000\\u0000\\u0000\\u0078\\u0000\\u0000\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u001f\\u0000\\u0000\\u00e0\\u00ff\\u0003\\u001c\\u0000\\u0000\\u0000\\u0000\\u0038\\u0000\\u0000\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u00e0\\u00ff\\u0003\\u003c\\u0000\\u0000\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u0080\\u00ff\\u0000\\u0078\\u0000\\u0000\\u0000\\u0000\\u001e\\u0000\\u0000\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u003f\\u0000\\u0000\\u0000\\u003e\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0078\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0007\\u0000\\u0000\\u00e0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0003\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u000f\\u0000\\u0000\\u00f0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u003f\\u0000\\u0000\\u00fc\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u0000\\u0000\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0007\\u00e0\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u0007\\u00e0\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00c1\\u00ff\\u00c3\\u0003\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u00fc\\u00c3\\u001f\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u001c\\u0000\\u00c3\\u0001\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00fb\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0078\\u0000\\u0003\\u0000\\u0000\\u001e\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0000\\u0003\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0000\\u0003\\u0000\\u0000\\u00f8\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0080\\u0001\\u0000\\u0000\\u00f0\\u0001\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0070\\u0080\\u0001\\u0000\\u0000\\u00e0\\u0007\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003c\\u00c0\\u0000\\u0000\\u0000\\u0080\\u001f\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000f\\u00c0\\u0000\\u0000\\u0000\\u0000\\u00ff\\u0001\\u00f8\\u00ef\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0003\\u00e0\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00f3\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u007f\\u00f0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003e\\u0000\\u0038\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0007\\u00f0\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u001c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u0001\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0003\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0083\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00c1\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0070\\u0070\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0038\\u003c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u001c\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0087\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c3\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e1\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0078\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000");
        this.predefinedImage.put(
            "FLOWERS",
            "\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0003\\u001e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0000\\u0078\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0038\\u0000\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0018\\u0000\\u00c0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000c\\u0000\\u0080\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0006\\u0000\\u0000\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0006\\u0000\\u0000\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0003\\u0000\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0003\\u0000\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0003\\u0000\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0001\\u0003\\u0000\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0080\\u0007\\u000e\\u0003\\u0000\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u00c0\\u0000\\u0010\\u0003\\u0000\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0070\\u0000\\u0060\\u0003\\u0000\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u000f\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0038\\u0000\\u00c0\\u0006\\u0000\\u0000\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u007f\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0018\\u0000\\u0080\\u0006\\u0000\\u0000\\u0003\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00e1\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u000c\\u0000\\u0000\\u000f\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00e7\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0006\\u0000\\u0000\\u00ee\\u001f\\u0080\\u00e1\\u000f\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ef\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0006\\u0000\\u0000\\u00fe\\u00ff\\u00c0\\u00fc\\u007f\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0006\\u0000\\u0000\\u00ff\\u00ff\\u00c3\\u001e\\u00f0\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0003\\u0000\\u0080\\u00ff\\u00ff\\u00ef\\u0007\\u00c0\\u0003\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u00ff\\u0001\\u0000\\u0003\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0007\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00e7\\u00ff\\u000f\\u0000\\u0003\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0006\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00fb\\u00ff\\u003f\\u0000\\u0003\\u0000\\u00f0\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u000c\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0003\\u0000\\u00f8\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u001c\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0003\\u0000\\u00f8\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0018\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0007\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0018\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0006\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0030\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u000f\\u0006\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0030\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u000e\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0030\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u001c\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0030\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u003f\\u0038\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0030\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u003f\\u0078\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0030\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u007f\\u00f0\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0030\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u007f\\u00c0\\u0007\\u00fe\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0018\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u007f\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0018\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u001c\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u00fe\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u000c\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u001f\\u00fc\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0006\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00c0\\u0007\\u00fc\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0007\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00e0\\u0001\\u00f8\\u00ff\\u00ff\\u00ff\\u0007\\u00c0\\u0003\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00e0\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u001f\\u00f0\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u0070\\u0000\\u00f0\\u00ff\\u00ff\\u003f\\u00ff\\u007f\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u0078\\u0000\\u00e0\\u00ff\\u00ff\\u001f\\u00fc\\u000f\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u00ff\\u0038\\u0000\\u00c0\\u00ff\\u00ff\\u000f\\u0018\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u007f\\u0038\\u0000\\u0080\\u00ff\\u00ff\\u0007\\u0030\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u007f\\u001c\\u0000\\u0000\\u00ff\\u00ff\\u0003\\u0070\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u007f\\u001c\\u0000\\u0000\\u00fc\\u00ff\\u0000\\u0060\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u003f\\u001c\\u0000\\u0000\\u00f0\\u001f\\u0000\\u0060\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u003f\\u001c\\u0000\\u0000\\u0070\\u0000\\u0000\\u00c0\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u001f\\u001c\\u0000\\u0000\\u0070\\u0000\\u0000\\u00c0\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u001f\\u001c\\u0000\\u0000\\u0070\\u0000\\u0000\\u00c0\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u000f\\u001c\\u0000\\u0000\\u0070\\u0000\\u0000\\u00c0\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u0007\\u0038\\u0000\\u0000\\u0068\\u0000\\u0000\\u00c0\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u0007\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u0003\\u0038\\u0000\\u0000\\u0068\\u0000\\u0000\\u00c0\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u0007\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u0001\\u0078\\u0000\\u0000\\u006c\\u0000\\u0000\\u00c0\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u000f\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u0000\\u0070\\u0000\\u0000\\u00c4\\u0000\\u0000\\u0060\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u003f\\u0000\\u00e0\\u0000\\u0000\\u00c2\\u0000\\u0000\\u0060\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u000f\\u0000\\u00e0\\u0001\\u0000\\u00c3\\u0001\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u0001\\u0000\\u00c0\\u0007\\u00c0\\u0081\\u0001\\u0000\\u0030\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u0003\\u0000\\u0000\\u0000\\u001f\\u0070\\u0000\\u0003\\u0000\\u0018\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u0007\\u0000\\u0000\\u0000\\u00fe\\u003f\\u0000\\u0007\\u0000\\u001c\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u0000\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u00f0\\u0007\\u0000\\u001e\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u0007\\u0000\\u00c0\\u00ff\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0078\\u00c0\\u0003\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u00f0\\u00ff\\u00ff\\u001f\\u0000\\u00e0\\u0007\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u0000\\u00fe\\u00ff\\u00ff\\u001f\\u0000\\u00f8\\u001f\\u0000\\u0000\\u0000\\u0080\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u00fe\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u00ff\\u00ff\\u0000\\u007e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u007f\\u00f0\\u0080\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0080\\u001f\\u00c0\\u00e1\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0080\\u0007\\u0000\\u00f1\\u00e0\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u00c0\\u0007\\u0000\\u0033\\u0080\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u00c0\\u0003\\u0000\\n\\u0000\\u001e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u00c0\\u0003\\u0000\\n\\u0000\\u001e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u00c0\\u0001\\u0000\\u0004\\u0000\\u003c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u00c0\\u0001\\u0000\\u0004\\u0000\\u003c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u00c0\\u0001\\u0000\\u0000\\u0000\\u0038\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0080\\u0001\\u0000\\u0000\\u0000\\u0038\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00fe\\u00ff\\u00ff\\u00ff\\u003f\\u0080\\u0001\\u0000\\u0000\\u0000\\u0038\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00fe\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0001\\u0000\\u0000\\u0000\\u0038\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u007f\\u00fe\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0003\\u0000\\u0002\\u0000\\u0018\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u007f\\u00fc\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0002\\u0000\\u0006\\u0000\\u001c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u003f\\u00fc\\u00ff\\u00ff\\u00ff\\u001f\\u00c0\\u0007\\u0000\\u0007\\u0000\\u000c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u00ff\\u001f\\u00f8\\u00ff\\u00ff\\u00ff\\u000f\\u00f0\\u0001\\u0000\\u000f\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u000f\\u00f8\\u00ff\\u00ff\\u00ff\\u000f\\u007c\\u0000\\u00c0\\u000f\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u0007\\u00f0\\u00ff\\u00ff\\u00ff\\u0007\\u001e\\u0000\\u00f0\\u003f\\u0080\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u0003\\u00e0\\u00ff\\u00ff\\u00ff\\u0003\\u001e\\u0000\\u00fc\\u00ff\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u0001\\u000f\\u0000\\u00f8\\u00ff\\u0003\\u0038\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u003f\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u0000\\u000f\\u0000\\u00f8\\u00ff\\u0000\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0007\\u0000\\u0000\\u00ff\\u00ff\\u007f\\u0080\\u0007\\u0000\\u00f0\\u00ff\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u001f\\u0080\\u0007\\u0000\\u00f0\\u007f\\u0000\\u00c0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u0007\\u0080\\u0007\\u0000\\u00f0\\u007f\\u0000\\u00c0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u0000\\u0080\\u0007\\u0000\\u00f0\\u003f\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0007\\u0000\\u00f0\\u003f\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u000f\\u0000\\u0038\\u0038\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000f\\u0000\\u0008\\u0020\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u001f\\u0000\\u0000\\u0000\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u001e\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007e\\u0000\\u0001\\u0000\\u0000\\u00c0\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00c1\\u0001\\u0000\\u0000\\u00e0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u0000\\u0000\\u0000\\u00e0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u0000\\u0000\\u0002\\u00f8\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u009f\\u0000\\u0000\\u000e\\u00fe\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0000\\u0000\\u00fe\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0000\\u0000\\u00f2\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0001\\u0000\\u00c3\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0001\\u0000\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0003\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0003\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u000f\\u00e0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u003f\\u00f8\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000");
        this.predefinedImage.put(
            "TACHO",
            "\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u007f\\u0009\\u0010\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u0007\\u0009\\u0010\\u00e0\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u0000\\r\\u0010\\u0000\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u001f\\u0000\\u0005\\u0010\\u0000\\u00f9\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0003\\u0000\\u0005\\u0000\\u0000\\u00c9\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00ff\\u0002\\u0000\\u0005\\u0000\\u0000\\u0005\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u003f\\u0002\\u0000\\u0005\\u0000\\u0080\\u0004\\u007c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u000f\\u0006\\u0000\\u0002\\u0000\\u0080\\u0002\\u00f0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0003\\u0004\\u0000\\u0002\\u0000\\u0080\\u0002\\u00c0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00c1\\u0000\\u0000\\u0002\\u0000\\u0080\\u0001\\u0040\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u00c0\\u0001\\u0000\\u0002\\u0000\\u0040\\u0001\\u0060\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u003f\\u0080\\u0001\\u0000\\u0002\\u0000\\u00c0\\u0000\\u0020\\u001c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u001f\\u0080\\u0003\\u0000\\u0000\\u0000\\u00c0\\u0000\\u0000\\u0078\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u001f\\u0000\\u0007\\u0000\\u0000\\u0000\\u0040\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0013\\u0000\\u0007\\u00cc\\u0003\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0027\\u0000\\n\\u0008\\u00fa\\u00c0\\u0002\\u0000\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u002c\\u0000\\u000e\\u0008\\u008a\\u0080\\u0082\\u000f\\u0000\\u0040\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0068\\u0000\\u0014\\u00e8\\u008b\\u0080\\u0082\\u0008\\u0000\\u0020\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u003f\\u0050\\u0000\\u001c\\u0028\\u0088\\u0080\\u00a2\\u0008\\u0000\\u00d0\\u001c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u001f\\u0060\\u0000\\u002c\\u0028\\u0088\\u0080\\u00be\\u0008\\u0000\\u0038\\u0018\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u001f\\u00c0\\u0000\\u0038\\u00e8\\u00fb\\u0080\\u00a0\\u0008\\u0000\\u000c\\u0038\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u000f\\u00c0\\u0000\\u0058\\u0000\\u0000\\u0080\\u00a0\\u000f\\u0000\\u0003\\u0070\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0007\\u0080\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0001\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0003\\u0000\\u0000\\u00b0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0003\\u0000\\u0000\\u0070\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0006\\u0000\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0000\\u00ec\\u0001\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u000c\\u0060\\u0000\\u00a0\\u0002\\u0000\\u0000\\u0000\\u0028\\u00f8\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007e\\u0018\\u0040\\u00df\\u00c7\\u0003\\u0000\\u0000\\u0000\\u0028\\u0088\\u0000\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u0040\\u0051\\u0044\\u0005\\u0000\\u0000\\u0000\\u00e8\\u008b\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003f\\u0000\\u0040\\u0051\\u00c4\\u000e\\u0000\\u0000\\u0000\\u0028\\u008a\\u0000\\u0000\\u000c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u003f\\u0000\\u0040\\u0051\\u0084\\n\\u0000\\u0000\\u0000\\u0028\\u008a\\u0000\\u0000\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u001f\\u0000\\u0040\\u0051\\u0084\\u0014\\u0000\\u0000\\u0000\\u00e8\\u00fb\\u0000\\u0080\\u0019\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u001f\\u0000\\u0040\\u00df\\u0007\\u0015\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0038\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u000f\\u0000\\u0000\\u0000\\u0000\\u0029\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0030\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u000f\\u0000\\u0000\\u0000\\u0000\\u002b\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0007\\u0000\\u0000\\u0000\\u0000\\u0052\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0060\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0007\\u0000\\u0000\\u0000\\u0000\\u0072\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u000f\\u0000\\u0000\\u0000\\u0000\\u00a4\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0033\\u0000\\u0000\\u0000\\u0000\\u00e4\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0043\\u0000\\u0000\\u0000\\u0000\\u004c\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u008f\\u0001\\u0000\\u0000\\u0000\\u0088\\u0002\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0071\\u00c6\\u0007\\u0000\\u0000\\u0088\\u0002\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0081\\u004f\\u00f4\\u0001\\u0000\\u0010\\u0005\\u0000\\u0000\\u0000\\u00c0\\u003e\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0001\\u0040\\u0014\\u0001\\u0000\\u0010\\u0005\\u0000\\u0000\\u0000\\u0080\\u00a2\\u000f\\u00e0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0000\\u00c0\\u0017\\u0001\\u0000\\u0030\\n\\u0000\\u0000\\u0000\\u0080\\u00a2\\u0008\\u0038\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0000\\u0040\\u0014\\u0001\\u0000\\u0020\\u001a\\u0000\\u0000\\u0000\\u0080\\u00be\\u0008\\u0007\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u0040\\u0014\\u0001\\u0000\\u0020\\u0014\\u0000\\u0000\\u0000\\u0080\\u00a2\\u00e8\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u00c0\\u00f7\\u0001\\u0000\\u0040\\u002c\\u0000\\u0000\\u0000\\u0080\\u00a2\\u0008\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u0000\\u0000\\u0000\\u0000\\u0040\\u0028\\u0000\\u0000\\u0000\\u0080\\u00be\\u000f\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0050\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0050\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u00a0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u00a1\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007e\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e1\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0072\\u0070\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u001e\\u00c0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0080\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0006\\u0000\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0003\\u0000\\u0002\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0080\\u0003\\u0000\\u0002\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u0020\\u00f8\\u0000\\u0000\\u0080\\u0003\\u0000\\u0002\\u0000\\u0000\\u0000\\u0000\\u0000\\u000e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u0000\\u0020\\u0088\\u0000\\u0000\\u0080\\u0003\\u0000\\u0002\\u0000\\u0000\\u0000\\u0000\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u000f\\u00e0\\u008b\\u0000\\u0000\\u0000\\u0003\\u0000\\u0002\\u0000\\u0000\\u0000\\u0000\\u0000\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00f0\\u0020\\u008a\\u0000\\u0000\\u0000\\u0007\\u0000\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00c0\\u0027\\u008a\\u0000\\u0000\\u0000\\u0007\\u0000\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u003f\\u00e0\\u00fb\\u0000\\u0000\\u0000\\u000e\\u0080\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003e\\u00c0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007c\\u0070\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u001f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0002\\u0000\\u0000\\u00c0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0004\\u0000\\n\\u0000\\u0000\\u00c0\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0067\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0004\\u0000\\n\\u0000\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u001f\\u0000\\u0002\\u0000\\u0000\\u0000\\u0000\\u0000\\u0024\\u0000\\u0009\\u0000\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0007\\u0000\\u0082\\u000f\\u0000\\u0000\\u0000\\u0000\\u0094\\n\\u0079\\u0000\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u000f\\u0000\\u0082\\u0008\\u0000\\u0000\\u0000\\u0000\\u008c\\u0095\\u0048\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u000f\\u0000\\u00a2\\u0008\\u0000\\u0000\\u0000\\u0000\\u0094\\u0094\\u0048\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u001f\\u0000\\u00be\\u0008\\u0000\\u0000\\u0000\\u0000\\u00a4\\u0054\\u0048\\u0000\\u0000\\u00f8\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u001f\\u0000\\u00a0\\u0008\\u0000\\u0000\\u0000\\u0000\\u0000\\u0040\\u0000\\u0000\\u0000\\u0078\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u003f\\u0000\\u00a0\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u007c\\u003e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u003f\\u00f8\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u0067\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u007f\\u0018\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0000\\u003e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u001f\\u0000\\u0070\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u000f\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0000\\u0000\\u00e0\\u001f\\u00fe\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u0003\\u0000\\u0080\\u0001\\u0000\\u0000\\u0000\\u0000\\u00f8\\u0001\\u00f8\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u0001\\u0000\\u0000\\u0003\\u0000\\u0000\\u0000\\u0000\\u007c\\u0000\\u00e0\\u0007\\u0000\\u0000\\u003c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u00be\\u0000\\u0090\\u000f\\u0000\\u0000\\u00a0\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u0000\\u003e\\u0000\\u0006\\u0000\\u0000\\u0000\\u0000\\u009f\\u0000\\u0010\\u001e\\u0000\\u0000\\u00a0\\u0008\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0078\\u0080\\u00c1\\u0000\\u000c\\u0000\\u0000\\u0000\\u0000\\u00cf\\u0000\\u0030\\u001e\\u0000\\u0000\\u00be\\u0008\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003c\\u0080\\u0080\\u0000\\u001c\\u0000\\u0000\\u0000\\u0080\\u0087\\u0004\\u0012\\u003c\\u0000\\u0000\\u0082\\u0008\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003c\\u0080\\u0080\\u0000\\u001c\\u0000\\u0000\\u0000\\u00c0\\u00c3\\u009b\\u003d\\u0078\\u0020\\u0000\\u0082\\u0008\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u003e\\u0080\\u00be\\u0000\\u0018\\u0000\\u0000\\u0000\\u00c0\\u0083\\u0091\\u0018\\u00f8\\u0010\\u0000\\u00be\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u001f\\u0080\\u0080\\u0000\\u0018\\u0000\\u0000\\u0000\\u00c0\\u00c3\\u0064\\u0032\\u00f8\\u0009\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0080\\u001f\\u0080\\u00be\\u0000\\u0018\\u0000\\u0000\\u0000\\u00c0\\u00c1\\u00f1\\u0038\\u00f0\\u0007\\u0000\\u0010\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u001f\\u0080\\u0080\\u0000\\u0038\\u0000\\u0000\\u0000\\u00c0\\u0001\\u00fa\\u0005\\u00f0\\u000f\\u0000\\u0018\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u001f\\u0080\\u00be\\u0000\\u0038\\u0000\\u0000\\u0000\\u00e0\\u0001\\u0064\\u0002\\u00f0\\u001f\\u0000\\u000c\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f8\\u001f\\u0080\\u0080\\u0000\\u0038\\u0000\\u0000\\u0000\\u00e0\\u0001\\u0060\\u0000\\u00f0\\u003f\\u0000\\n\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u001f\\u0080\\u00be\\u0000\\u0038\\u0000\\u0000\\u0000\\u00e0\\u0001\\u0060\\u0000\\u00f0\\u007f\\u0080\\u0005\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u001f\\u0080\\u0080\\u0000\\u0038\\u0000\\u0000\\u0000\\u00e0\\u0001\\u00fe\\u0007\\u00f0\\u00ff\\u0041\\u0006\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u001c\\u0080\\u00be\\u0000\\u0018\\u0000\\u0000\\u0000\\u00c0\\u0003\\u0062\\u0004\\u00f8\\u00ff\\u0033\\u0002\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u007f\\u003c\\u0080\\u0080\\u0000\\u001c\\u0000\\u0000\\u0000\\u00c0\\u0003\\u0063\\u000c\\u0088\\u00ff\\u001f\\u0003\\u0000\\u0000\\u0000\\u0000\\u0000\\u00f0\\u001f\\u003c\\u0080\\u00be\\u0000\\u001c\\u0000\\u0000\\u0000\\u00c0\\u0083\\u0003\\u001c\\u0008\\u00ff\\u003f\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u000f\\u003c\\u0080\\u0080\\u0000\\u001c\\u0000\\u0000\\u0000\\u00c0\\u0087\\u0003\\u001c\\u000c\\u00fe\\u00ff\\u0001\\u0000\\u0000\\u0000\\u0000\\u0000\\u00ff\\u0007\\u007c\\u0080\\u00ff\\u0000\\u001e\\u0000\\u0000\\u0000\\u00c0\\u008f\\u00ff\\u001f\\u000e\\u00fc\\u00ff\\u0003\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u0003\\u00f8\\u0000\\u0000\\u0000\\u000f\\u0000\\u0000\\u0000\\u0080\\u009f\\u0007\\u001e\\u0006\\u00f8\\u00ff\\u001f\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u0001\\u00f0\\u0000\\u0000\\u0000\\u0007\\u0000\\u0000\\u0000\\u0000\\u003f\\u0003\\u000c\\u0003\\u00e0\\u00ff\\u00ff\\u0000\\u0000\\u0000\\u0000\\u00ff\\u007f\\u0000\\u00f0\\u0001\\u0000\\u0080\\u0007\\u0000\\u0000\\u0000\\u0000\\u007f\\u0000\\u00c0\\u0003\\u0080\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u00e0\\u00ff\\u001f\\u0000\\u00e0\\u0003\\u0000\\u00c0\\u0003\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0001\\u00e0\\u0001\\u0000\\u00fe\\u00ff\\u00ff\\u0007\\u00e0\\u00ff\\u00ff\\u0007\\u0000\\u00c0\\u000f\\u0000\\u00f0\\u0001\\u0000\\u0000\\u0000\\u0000\\u00fc\\u000f\\u00fc\\u0000\\u0000\\u00f8\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u0001\\u0000\\u00c0\\u001f\\u0000\\u00f8\\u0001\\u0000\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u007f\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u00ff\\u0000\\u007f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u001f\\u0000\\u0000\\u0080\\u00ff\\u00ff\\u00ff\\u00ff\\u00ff\\u001f\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u003f\\u0000\\u0000\\u0000\\u0000\\u0000\\u00c0\\u00ff\\u000f\\u0000\\u0000\\u0000\\u00fc\\u00ff\\u00ff\\u00ff\\u00ff\\u0003\\u0000\\u0000\\u0000\\u00f8\\u00ff\\u000f\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fc\\u0000\\u0000\\u0000\\u0000\\u00e0\\u00ff\\u00ff\\u00ff\\u007f\\u0000\\u0000\\u0000\\u0000\\u00f0\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u00ff\\u00ff\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u003e\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u00fe\\u0007\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000\\u0000");
    }

}
