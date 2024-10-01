package de.fhg.iais.roberta.visitor.codegen;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.components.ConfigurationAst;
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
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.lang.expr.NNGetBias;
import de.fhg.iais.roberta.syntax.lang.expr.NNGetOutputNeuronVal;
import de.fhg.iais.roberta.syntax.lang.expr.NNGetWeight;
import de.fhg.iais.roberta.syntax.lang.stmt.NNSetBiasStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNSetInputNeuronVal;
import de.fhg.iais.roberta.syntax.lang.stmt.NNSetWeightStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.NNStepStmt;
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
import de.fhg.iais.roberta.textlyJava.Ev3TextlyJavaVisitor;
import de.fhg.iais.roberta.visitor.IEv3Visitor;
import de.fhg.iais.roberta.visitor.IVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.prog.AbstractRegenerateTextlyJavaVisitor;

/**
 * This class is implementing {@link IVisitor}. All methods are implemented and they append a human-readable JAVA code representation of a phrase to a
 * StringBuilder. <b>This representation is correct JAVA code.</b> <br>
 */
public final class Ev3RegenerateTextlyJavaVisitor extends AbstractRegenerateTextlyJavaVisitor implements IEv3Visitor<Void> {
    private static final Logger LOG = LoggerFactory.getLogger(Ev3RegenerateTextlyJavaVisitor.class);

    protected final ConfigurationAst brickConfiguration;

    /**
     * initialize the Java code generator visitor.
     *
     * @param programPhrases the program
     * @param brickConfiguration hardware configuration of the brick
     */
    public Ev3RegenerateTextlyJavaVisitor(
        List<List<Phrase>> programPhrases,
        ConfigurationAst brickConfiguration) {
        super(programPhrases);
        this.brickConfiguration = brickConfiguration;
    }

    @Override
    public Void visitBrickLightOnAction(BrickLightOnAction brickLightOnAction) {
        this.src.nlI().add("ev3.ledOn(");
        String textlyRepresentation = null;
        for ( Ev3TextlyJavaVisitor.BrickLightModes modes : Ev3TextlyJavaVisitor.BrickLightModes.values() ) {
            if ( modes.name().equalsIgnoreCase(brickLightOnAction.mode.toString()) ) {
                textlyRepresentation = modes.name;
            }
        }
        this.src.add(textlyRepresentation).add(",");
        this.src.add(brickLightOnAction.colour.name().toLowerCase()).add(");");
        return null;
    }

    @Override
    public Void visitBrickLightOffAction(BrickLightOffAction brickLightOffAction) {
        if ( brickLightOffAction.mode.equals("OFF") ) {
            this.src.nlI().add("ev3.ledOff();");
        } else {
            this.src.nlI().add("ev3.resetLed();");
        }
        return null;
    }

    @Override
    public Void visitPlayFileAction(PlayFileAction playFileAction) {
        this.src.nlI().add("ev3.playFile(").add(Integer.toString(Integer.parseInt(playFileAction.fileName) + 1)).add(");");
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        this.src.nlI().add("ev3.playTone(").add(playNoteAction.frequency).add(",").add(playNoteAction.duration).add(");");
        return null;
    }

    @Override
    public Void visitSayTextAction(SayTextAction sayTextAction) {
        this.src.nlI().add("ev3.sayText(");
        sayTextAction.msg.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitSayTextWithSpeedAndPitchAction(SayTextWithSpeedAndPitchAction sayTextAction) {
        this.src.nlI().add("ev3.sayText(");
        sayTextAction.msg.accept(this);
        this.src.add(",");
        sayTextAction.speed.accept(this);
        this.src.add(",");
        sayTextAction.pitch.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitSetLanguageAction(SetLanguageAction setLanguageAction) {
        this.src.nlI().add("ev3.setLanguage(").add(setLanguageAction.language.getAbbreviation()).add(");");
        return null;
    }

    @Override
    public Void visitShowPictureAction(ShowPictureAction showPictureAction) {
        this.src.nlI().add("ev3.drawPicture(");
        String textlyRepresentation = null;
        for ( Ev3TextlyJavaVisitor.Ev3PredefinedImages imageEnum : Ev3TextlyJavaVisitor.Ev3PredefinedImages.values() ) {
            if ( imageEnum.name().equalsIgnoreCase(showPictureAction.pic) ) {
                textlyRepresentation = imageEnum.name;
            }
        }
        this.src.add(textlyRepresentation).add(");");
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        this.src.add("ev3.keysSensor.isPressed(").add(keysSensor.getUserDefinedPort().toLowerCase()).add(")");
        return null;
    }

    @Override
    public Void visitBluetoothCheckConnectAction(BluetoothCheckConnectAction bluetoothCheckConnectAction) {
        return null;
    }

    @Override
    public Void visitBluetoothConnectAction(BluetoothConnectAction bluetoothConnectAction) {
        this.src.add("ev3.connectToRobot(");
        bluetoothConnectAction.address.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitBluetoothReceiveAction(BluetoothReceiveAction bluetoothReceiveAction) {
        this.src.add("ev3.receiveMessage(");
        bluetoothReceiveAction.connection.accept(this);
        this.src.add(")");
        return null;
    }

    @Override
    public Void visitBluetoothSendAction(BluetoothSendAction bluetoothSendAction) {
        this.src.nlI().add("ev3.sendMessage(");
        bluetoothSendAction.msg.accept(this);
        this.src.add(",");
        bluetoothSendAction.connection.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction bluetoothWaitForConnection) {
        this.src.add("ev3.waitForConnection()");
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        this.src.nlI().add("ev3.clearDisplay();");
        return null;
    }

    @Override
    public Void visitColorSensor(ColorSensor colorSensor) {
        this.src.add("ev3.colorSensor(").add(colorSensor.getMode().toLowerCase()).add(",").add(colorSensor.getUserDefinedPort()).add(")");
        return null;
    }

    @Override
    public Void visitShowTextAction(ShowTextAction showTextAction) {
        this.src.nlI().add("ev3.drawText(");
        showTextAction.msg.accept(this);
        this.src.add(",");
        showTextAction.x.accept(this);
        this.src.add(",");
        showTextAction.y.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitSoundSensor(SoundSensor soundSensor) {
        this.src.add("ev3.soundSensor.getSoundLevel(").add(soundSensor.getUserDefinedPort()).add(")");
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor encoderSensor) {
        this.src.add("ev3.encoderSensor.");
        if ( encoderSensor.getMode().equals("DEGREE") ) {
            this.src.add("getDegree(");
        } else if ( encoderSensor.getMode().equals("ROTATION") ) {
            this.src.add("getRotation(");
        } else {
            this.src.add("getDistance(");
        }
        this.src.add(encoderSensor.getUserDefinedPort()).add(")");
        return null;
    }

    @Override
    public Void visitEncoderReset(EncoderReset encoderReset) {
        this.src.nlI().add("ev3.encoderReset(").add(encoderReset.sensorPort).add(");");
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor gyroSensor) {
        if ( gyroSensor.getMode().equals("ANGLE") ) {
            this.src.add("ev3.gyroSensor.getAngle(").add(gyroSensor.getUserDefinedPort()).add(")");
        } else {
            this.src.add("ev3.gyroSensor.getRate(").add(gyroSensor.getUserDefinedPort()).add(")");
        }
        return null;
    }

    @Override
    public Void visitGyroReset(GyroReset gyroReset) {
        this.src.nlI().add("ev3.gyroReset(").add(gyroReset.sensorPort).add(");");
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        if ( infraredSensor.getMode().equals("DISTANCE") ) {
            this.src.add("ev3.infraredSensor.getDistance(").add(infraredSensor.getUserDefinedPort()).add(")");
        } else {
            this.src.add("ev3.infraredSensor.getPresence(").add(infraredSensor.getUserDefinedPort()).add(")");
        }
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction toneAction) {
        this.src.nlI().add("ev3.playTone(");
        toneAction.frequency.accept(this);
        this.src.add(",");
        toneAction.duration.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitTouchSensor(TouchSensor touchSensor) {
        this.src.add("ev3.touchSensor.isPressed(").add(touchSensor.getUserDefinedPort()).add(")");
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        if ( ultrasonicSensor.getMode().equals("DISTANCE") ) {
            this.src.add("ev3.ultrasonicSensor.getDistance(").add(ultrasonicSensor.getUserDefinedPort()).add(")");
        } else {
            this.src.add("ev3.ultrasonicSensor.getPresence(").add(ultrasonicSensor.getUserDefinedPort()).add(")");
        }
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor compassSensor) {
        if ( compassSensor.getMode().equals("ANGLE") ) {
            this.src.add("ev3.hiTechCompassSensor.getAngle(");
        } else {
            this.src.add("ev3.hiTechCompassSensor.getCompass(");
        }
        this.src.add(compassSensor.getUserDefinedPort()).add(")");
        return null;
    }

    @Override
    public Void visitCompassCalibrate(CompassCalibrate compassCalibrate) {
        this.src.nlI().add("ev3.hiTecCompassStartCalibration(").add(compassCalibrate.getUserDefinedPort()).add(");");
        return null;
    }

    @Override
    public Void visitIRSeekerSensor(IRSeekerSensor irSeekerSensor) {
        if ( irSeekerSensor.getMode().equals("MODULATED") ) {
            this.src.add("ev3.hiTechInfraredSensor.getModulated(");
        } else {
            this.src.add("ev3.hiTechInfraredSensor.getUnmodulated(");
        }
        this.src.add(irSeekerSensor.getUserDefinedPort()).add(")");
        return null;
    }

    @Override
    public Void visitHTColorSensor(HTColorSensor htColorSensor) {
        this.src.add("ev3.hiTechColorSensor(").add(htColorSensor.getMode().toLowerCase()).add(",").add(htColorSensor.getUserDefinedPort()).add(")");
        return null;
    }

    @Override
    public Void visitGetVolumeAction(GetVolumeAction getVolumeAction) {
        this.src.add("ev3.getVolume()");
        return null;
    }

    @Override
    public Void visitSetVolumeAction(SetVolumeAction setVolumeAction) {
        this.src.nlI().add("ev3.setVolume(");
        setVolumeAction.volume.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitCurveAction(CurveAction curveAction) {
        this.src.nlI().add("ev3.driveInCurve(").add(curveAction.direction.toString().equals("BACKWARD") ? "backward" : "forward").add(",");
        curveAction.paramLeft.speed.accept(this);
        this.src.add(",");
        curveAction.paramRight.speed.accept(this);
        if ( curveAction.paramLeft.getDuration() != null ) {
            this.src.add(",");
            curveAction.paramLeft.getDuration().value.accept(this);
        }
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitTurnAction(TurnAction turnAction) {
        this.src.nlI();
        if ( turnAction.param.getDuration() == null ) {
            this.src.add("ev3.rotateDirectionRegulated(").add(turnAction.direction.toString().toLowerCase()).add(",");
            turnAction.param.speed.accept(this);
            this.src.add(");");
        } else {
            this.src.add("ev3.rotateDirectionAngle(").add(turnAction.direction.toString().toLowerCase()).add(",");
            turnAction.param.speed.accept(this);
            this.src.add(",");
            turnAction.param.getDuration().value.accept(this);
            this.src.add(");");
        }
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        if ( motorOnAction.param.getDuration() == null ) {
            this.src.nlI().add("ev3.turnOnRegulatedMotor(").add(motorOnAction.port).add(",");
            motorOnAction.param.speed.accept(this);
            this.src.add(");");
        } else {
            this.src.nlI().add("ev3.rotateRegulatedMotor(").add(motorOnAction.port).add(",");
            motorOnAction.param.speed.accept(this);
            this.src.add(", ").add(motorOnAction.param.getDuration().type.toString().toLowerCase()).add(",");
            motorOnAction.param.getDuration().value.accept(this);
            this.src.add(");");
        }
        return null;
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction) {
        this.src.nlI().add("ev3.setRegulatedMotorSpeed(").add(motorSetPowerAction.port).add(",");
        motorSetPowerAction.power.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction) {
        this.src.add("ev3.getSpeedMotor(").add(motorGetPowerAction.port).add(")");
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        this.src.nlI().add("ev3.stopRegulatedMotor(").add(motorStopAction.port).add(",").add(motorStopAction.mode.toString().equals("FLOAT") ? "float" : "brake").add(");");
        return null;
    }

    @Override
    public Void visitDriveAction(DriveAction driveAction) {
        this.src.nlI().add("ev3.driveDistance(").add(driveAction.direction.toString().equals("BACKWARD") ? "backward" : "forward").add(",");
        driveAction.param.speed.accept(this);
        if ( driveAction.param.getDuration() == null ) {
            this.src.add(");");
        } else {
            this.src.add(", ");
            driveAction.param.getDuration().value.accept(this);
            this.src.add(");");
        }
        return null;
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction stopAction) {
        this.src.nlI().add("ev3.stopRegulatedDrive();");
        return null;
    }

    @Override
    public Void visitColorConst(ColorConst colorConst) {
        this.src.add("#rgb(").add(colorConst.hexValue.replace("#", "")).add(")");
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        this.src.add("ev3.timerSensor(").add(timerSensor.getUserDefinedPort()).add(")");
        return null;
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        this.src.nlI().add("ev3.timerReset(").add(timerReset.sensorPort).add(");");
        return null;
    }

    @Override
    public Void visitNNStepStmt(NNStepStmt nnStepStmt) {
        this.src.nlI().add("ev3.nnStep();");
        return null;
    }

    @Override
    public Void visitNNSetInputNeuronVal(NNSetInputNeuronVal nnSetInputNeuronVal) {
        this.src.nlI().add("ev3.setInputNeuron(").add(nnSetInputNeuronVal.name).add(",");
        nnSetInputNeuronVal.value.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitNNGetOutputNeuronVal(NNGetOutputNeuronVal nnGetOutputNeuronVal) {
        this.src.add("ev3.getOutputNeuron(").add(nnGetOutputNeuronVal.name).add(")");
        return null;
    }

    @Override
    public Void visitNNSetWeightStmt(NNSetWeightStmt nnSetWeightStmt) {
        this.src.nlI().add("ev3.setWeight(").add(nnSetWeightStmt.from).add(",").add(nnSetWeightStmt.to).add(",");
        nnSetWeightStmt.value.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitNNSetBiasStmt(NNSetBiasStmt nnSetBiasStmt) {
        this.src.nlI().add("ev3.setBias(").add(nnSetBiasStmt.name).add(",");
        nnSetBiasStmt.value.accept(this);
        this.src.add(");");
        return null;
    }

    @Override
    public Void visitNNGetWeight(NNGetWeight nnGetWeight) {
        this.src.add("ev3.getWeight(").add(nnGetWeight.from).add(",").add(nnGetWeight.to).add(")");
        return null;
    }

    @Override
    public Void visitNNGetBias(NNGetBias nnGetBias) {
        this.src.add("ev3.getBias(").add(nnGetBias.name).add(")");
        return null;
    }
}
