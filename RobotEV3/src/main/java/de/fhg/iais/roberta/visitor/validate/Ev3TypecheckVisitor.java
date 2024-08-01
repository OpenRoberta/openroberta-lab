package de.fhg.iais.roberta.visitor.validate;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
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
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.typecheck.BlocklyType;
import de.fhg.iais.roberta.typecheck.Sig;
import de.fhg.iais.roberta.visitor.IEv3Visitor;

public class Ev3TypecheckVisitor extends TypecheckCommonLanguageVisitor implements IEv3Visitor<BlocklyType> {
    public Ev3TypecheckVisitor(UsedHardwareBean usedHardwareBean) {
        super(usedHardwareBean);
    }

    @Override
    public BlocklyType visitBrickLightOnAction(BrickLightOnAction brickLightOnAction) {
        return Sig.of(BlocklyType.VOID).typeCheckPhrases(brickLightOnAction, this);
    }

    @Override
    public BlocklyType visitBrickLightOffAction(BrickLightOffAction brickLightOffAction) {
        return Sig.of(BlocklyType.VOID).typeCheckPhrases(brickLightOffAction, this);
    }

    @Override
    public BlocklyType visitPlayFileAction(PlayFileAction playFileAction) {
        return Sig.of(BlocklyType.VOID).typeCheckPhrases(playFileAction, this);
    }

    @Override
    public BlocklyType visitPlayNoteAction(PlayNoteAction playNoteAction) {
        return Sig.of(BlocklyType.VOID).typeCheckPhrases(playNoteAction, this);
    }

    @Override
    public BlocklyType visitSayTextAction(SayTextAction sayTextAction) {
        return Sig.of(BlocklyType.VOID, BlocklyType.ANY).typeCheckPhrases(sayTextAction, this, sayTextAction.msg);
    }

    @Override
    public BlocklyType visitSayTextWithSpeedAndPitchAction(SayTextWithSpeedAndPitchAction sayTextAction) {
        return Sig.of(BlocklyType.VOID, BlocklyType.ANY, BlocklyType.NUMBER, BlocklyType.NUMBER).typeCheckPhrases(sayTextAction, this, sayTextAction.msg, sayTextAction.speed, sayTextAction.pitch);
    }

    @Override
    public BlocklyType visitSetLanguageAction(SetLanguageAction setLanguageAction) {
        return Sig.of(BlocklyType.VOID).typeCheckPhrases(setLanguageAction, this);
    }

    @Override
    public BlocklyType visitShowPictureAction(ShowPictureAction showPictureAction) {
        return Sig.of(BlocklyType.VOID).typeCheckPhrases(showPictureAction, this);
    }

    @Override
    public BlocklyType visitKeysSensor(KeysSensor keysSensor) {
        return Sig.of(BlocklyType.BOOLEAN).typeCheckPhrases(keysSensor, this);
    }

    @Override
    public BlocklyType visitBluetoothCheckConnectAction(BluetoothCheckConnectAction bluetoothCheckConnectAction) {
        return BlocklyType.CONNECTION;
    }

    @Override
    public BlocklyType visitBluetoothConnectAction(BluetoothConnectAction bluetoothConnectAction) {
        return Sig.of(BlocklyType.CONNECTION, BlocklyType.STRING).typeCheckPhrases(bluetoothConnectAction, this, bluetoothConnectAction.address);
    }

    @Override
    public BlocklyType visitBluetoothReceiveAction(BluetoothReceiveAction bluetoothReceiveAction) {
        return Sig.of(BlocklyType.STRING, BlocklyType.CONNECTION).typeCheckPhrases(bluetoothReceiveAction, this, bluetoothReceiveAction.connection);
    }

    @Override
    public BlocklyType visitBluetoothSendAction(BluetoothSendAction bluetoothSendAction) {
        return Sig.of(BlocklyType.VOID, BlocklyType.STRING, BlocklyType.CONNECTION).typeCheckPhrases(bluetoothSendAction, this, bluetoothSendAction.msg, bluetoothSendAction.connection);
    }

    @Override
    public BlocklyType visitBluetoothWaitForConnectionAction(BluetoothWaitForConnectionAction bluetoothWaitForConnection) {
        return Sig.of(BlocklyType.CONNECTION).typeCheckPhrases(bluetoothWaitForConnection, this);
    }

    @Override
    public BlocklyType visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        return Sig.of(BlocklyType.VOID).typeCheckPhrases(clearDisplayAction, this);
    }

    @Override
    public BlocklyType visitColorSensor(ColorSensor colorSensor) {
        switch ( colorSensor.getMode() ) {
            case "COLOUR":
                return Sig.of(BlocklyType.COLOR).typeCheckPhrases(colorSensor, this);
            case "LIGHT":
            case "AMBIENTLIGHT":
                return Sig.of(BlocklyType.NUMBER).typeCheckPhrases(colorSensor, this);
            case "RGB":
                return Sig.of(BlocklyType.ARRAY_NUMBER).typeCheckPhrases(colorSensor, this);
            default:
                colorSensor.addTextlyError("Invalid mode for the color sensor", true);
                return BlocklyType.NOTHING;
        }
    }

    @Override
    public BlocklyType visitShowTextAction(ShowTextAction showTextAction) {
        return Sig.of(BlocklyType.VOID, BlocklyType.ANY, BlocklyType.NUMBER, BlocklyType.NUMBER).typeCheckPhrases(showTextAction, this,
            showTextAction.msg, showTextAction.x, showTextAction.y);
    }

    @Override
    public BlocklyType visitSoundSensor(SoundSensor soundSensor) {
        return Sig.of(BlocklyType.NUMBER).typeCheckPhrases(soundSensor, this);
    }

    @Override
    public BlocklyType visitEncoderSensor(EncoderSensor encoderSensor) {
        return Sig.of(BlocklyType.NUMBER).typeCheckPhrases(encoderSensor, this);
    }

    @Override
    public BlocklyType visitEncoderReset(EncoderReset encoderReset) {
        return Sig.of(BlocklyType.VOID).typeCheckPhrases(encoderReset, this);
    }

    @Override
    public BlocklyType visitGyroSensor(GyroSensor gyroSensor) {
        return Sig.of(BlocklyType.NUMBER).typeCheckPhrases(gyroSensor, this);
    }

    @Override
    public BlocklyType visitGyroReset(GyroReset gyroReset) {
        return Sig.of(BlocklyType.VOID).typeCheckPhrases(gyroReset, this);
    }

    @Override
    public BlocklyType visitInfraredSensor(InfraredSensor infraredSensor) {
        if ( infraredSensor.getMode().equals("DISTANCE") ) {
            return Sig.of(BlocklyType.NUMBER).typeCheckPhrases(infraredSensor, this);
        } else {
            return Sig.of(BlocklyType.ARRAY_NUMBER).typeCheckPhrases(infraredSensor, this);
        }
    }

    @Override
    public BlocklyType visitToneAction(ToneAction toneAction) {
        return Sig.of(BlocklyType.VOID, BlocklyType.NUMBER, BlocklyType.NUMBER).typeCheckPhrases(toneAction, this, toneAction.frequency, toneAction.duration);
    }

    @Override
    public BlocklyType visitTouchSensor(TouchSensor touchSensor) {
        return Sig.of(BlocklyType.BOOLEAN).typeCheckPhrases(touchSensor, this);
    }

    @Override
    public BlocklyType visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        if ( ultrasonicSensor.getMode().equals("DISTANCE") )
            return Sig.of(BlocklyType.NUMBER).typeCheckPhrases(ultrasonicSensor, this);
        else {
            return Sig.of(BlocklyType.BOOLEAN).typeCheckPhrases(ultrasonicSensor, this);
        }
    }

    @Override
    public BlocklyType visitCompassSensor(CompassSensor compassSensor) {
        return Sig.of(BlocklyType.NUMBER).typeCheckPhrases(compassSensor, this);
    }

    @Override
    public BlocklyType visitCompassCalibrate(CompassCalibrate compassCalibrate) {
        return Sig.of(BlocklyType.VOID).typeCheckPhrases(compassCalibrate, this);
    }

    @Override
    public BlocklyType visitIRSeekerSensor(IRSeekerSensor irSeekerSensor) {
        return Sig.of(BlocklyType.NUMBER).typeCheckPhrases(irSeekerSensor, this);
    }

    @Override
    public BlocklyType visitHTColorSensor(HTColorSensor htColorSensor) {
        switch ( htColorSensor.getMode() ) {
            case "COLOUR":
                return Sig.of(BlocklyType.COLOR).typeCheckPhrases(htColorSensor, this);
            case "LIGHT":
            case "AMBIENTLIGHT":
                return Sig.of(BlocklyType.NUMBER).typeCheckPhrases(htColorSensor, this);
            case "RGB":
                return Sig.of(BlocklyType.ARRAY_NUMBER).typeCheckPhrases(htColorSensor, this);
            default:
                htColorSensor.addTextlyError("Invalid mode for the color sensor", true);
                return BlocklyType.NOTHING;
        }
    }

    @Override
    public BlocklyType visitGetVolumeAction(GetVolumeAction getVolumeAction) {
        return Sig.of(BlocklyType.NUMBER).typeCheckPhrases(getVolumeAction, this);
    }

    @Override
    public BlocklyType visitSetVolumeAction(SetVolumeAction setVolumeAction) {
        return Sig.of(BlocklyType.VOID, BlocklyType.NUMBER).typeCheckPhrases(setVolumeAction, this, setVolumeAction.volume);
    }

    @Override
    public BlocklyType visitCurveAction(CurveAction curveAction) {
        if ( curveAction.paramLeft.getDuration() != null && curveAction.paramRight.getDuration() != null ) {
            return Sig.of(BlocklyType.VOID, BlocklyType.NUMBER, BlocklyType.NUMBER, BlocklyType.NUMBER).typeCheckPhrases(curveAction, this, curveAction.paramLeft.getSpeed(), curveAction.paramRight.getSpeed(), curveAction.paramLeft.getDuration().getValue());
        } else {
            return Sig.of(BlocklyType.VOID, BlocklyType.NUMBER, BlocklyType.NUMBER).typeCheckPhrases(curveAction, this, curveAction.paramLeft.getSpeed(), curveAction.paramRight.getSpeed());
        }
    }

    @Override
    public BlocklyType visitTurnAction(TurnAction turnAction) {
        if ( turnAction.param.getDuration() != null ) {
            return Sig.of(BlocklyType.VOID, BlocklyType.NUMBER, BlocklyType.NUMBER).typeCheckPhrases(turnAction, this, turnAction.param.getSpeed(), turnAction.param.getDuration().getValue());
        } else {
            return Sig.of(BlocklyType.VOID, BlocklyType.NUMBER).typeCheckPhrases(turnAction, this, turnAction.param.getSpeed());
        }
    }

    @Override
    public BlocklyType visitMotorOnAction(MotorOnAction motorOnAction) {
        if ( motorOnAction.param.getDuration() != null ) {
            return Sig.of(BlocklyType.VOID, BlocklyType.NUMBER, BlocklyType.NUMBER).typeCheckPhrases(motorOnAction, this, motorOnAction.param.getSpeed(), motorOnAction.param.getDuration().getValue());
        } else {
            return Sig.of(BlocklyType.VOID, BlocklyType.NUMBER).typeCheckPhrases(motorOnAction, this, motorOnAction.param.getSpeed());
        }
    }

    @Override
    public BlocklyType visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction) {
        return Sig.of(BlocklyType.VOID, BlocklyType.NUMBER).typeCheckPhrases(motorSetPowerAction, this, motorSetPowerAction.power);
    }

    @Override
    public BlocklyType visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction) {
        return Sig.of(BlocklyType.NUMBER).typeCheckPhrases(motorGetPowerAction, this);
    }

    @Override
    public BlocklyType visitMotorStopAction(MotorStopAction motorStopAction) {
        return Sig.of(BlocklyType.VOID).typeCheckPhrases(motorStopAction, this);
    }

    @Override
    public BlocklyType visitDriveAction(DriveAction driveAction) {
        if ( driveAction.param.getDuration() != null ) {
            return Sig.of(BlocklyType.VOID, BlocklyType.NUMBER, BlocklyType.NUMBER).typeCheckPhrases(driveAction, this, driveAction.param.getSpeed(), driveAction.param.getDuration().getValue());
        } else {
            return Sig.of(BlocklyType.VOID, BlocklyType.NUMBER).typeCheckPhrases(driveAction, this, driveAction.param.getSpeed());
        }
    }

    @Override
    public BlocklyType visitMotorDriveStopAction(MotorDriveStopAction stopAction) {
        return Sig.of(BlocklyType.VOID).typeCheckPhrases(stopAction, this);
    }
}
