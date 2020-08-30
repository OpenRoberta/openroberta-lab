package de.fhg.iais.roberta.visitor.codegen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.inter.mode.action.ITurnDirection;
import de.fhg.iais.roberta.inter.mode.general.IDirection;
import de.fhg.iais.roberta.mode.action.DriveDirection;
import de.fhg.iais.roberta.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
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
import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LEDMatrixImageAction;
import de.fhg.iais.roberta.syntax.actors.arduino.LEDMatrixTextAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.SendIRAction;
import de.fhg.iais.roberta.syntax.expressions.arduino.LEDMatrixImage;
import de.fhg.iais.roberta.syntax.functions.arduino.LEDMatrixImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.arduino.LEDMatrixImageShiftFunction;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.visitor.C;
import de.fhg.iais.roberta.visitor.hardware.IMbotVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineVisitor;

public class MbotCppStackMachineVisitor<V> extends AbstractStackMachineVisitor<V> implements IMbotVisitor<V> {

    public MbotCppStackMachineVisitor(ConfigurationAst configuration, List<List<Phrase<Void>>> phrases) {
        super(configuration);
        Assert.isTrue(!phrases.isEmpty());
    }

    @Override
    public V visitKeysSensor(KeysSensor<V> keysSensor) {
        JSONObject o = mk(C.GET_SAMPLE, keysSensor).put(C.GET_SAMPLE, C.BUTTONS).put(C.MODE, "center");
        return app(o);
    }

    @Override
    public V visitTimerSensor(TimerSensor<V> timerSensor) {
        String port = timerSensor.getPort();
        JSONObject o;
        if ( timerSensor.getMode().equals(SC.DEFAULT) || timerSensor.getMode().equals(SC.VALUE) ) {
            o = mk(C.GET_SAMPLE, timerSensor).put(C.GET_SAMPLE, C.TIMER).put(C.PORT, port).put(C.NAME, "mbot");
        } else {
            o = mk(C.TIMER_SENSOR_RESET, timerSensor).put(C.PORT, port).put(C.NAME, "mbot");
        }
        return app(o);
    }

    @Override
    public V visitUltrasonicSensor(UltrasonicSensor<V> ultrasonicSensor) {
        String mode = ultrasonicSensor.getMode();
        String port = ultrasonicSensor.getPort();
        JSONObject o = mk(C.GET_SAMPLE, ultrasonicSensor).put(C.GET_SAMPLE, C.ULTRASONIC).put(C.PORT, port).put(C.MODE, mode.toLowerCase()).put(C.NAME, "mbot");
        return app(o);
    }

    @Override
    public V visitInfraredSensor(InfraredSensor<V> infraredSensor) {
        String port = infraredSensor.getPort();
        String slot = infraredSensor.getSlot();
        if ( slot.equals("1") ) {
            slot = C.LEFT;
        } else if ( slot.equals("2") ) {
            slot = C.RIGHT;
        }
        JSONObject o = mk(C.GET_SAMPLE, infraredSensor).put(C.GET_SAMPLE, C.INFRARED).put(C.PORT, port).put(C.MODE, slot).put(C.NAME, "mbot");
        return app(o);
    }

    @Override
    public V visitLightAction(LightAction<V> lightAction) {
        String mode = lightAction.getMode().toString().toLowerCase();
        lightAction.getRgbLedColor().accept(this);
        String port = lightAction.getPort();
        JSONObject o = mk(C.LIGHT_ACTION, lightAction).put(C.MODE, mode).put(C.PORT, port).put(C.NAME, "mbot");
        return app(o);
    }

    @Override
    public V visitLightStatusAction(LightStatusAction<V> lightStatusAction) {
        JSONObject o =
            mk(C.STATUS_LIGHT_ACTION, lightStatusAction)
                .put(C.MODE, lightStatusAction.getStatus())
                .put(C.PORT, lightStatusAction.getPort())
                .put(C.NAME, "mbot");
        return app(o);
    }

    @Override
    public V visitColorConst(ColorConst<V> colorConst) {
        int r = colorConst.getRedChannelInt();
        int g = colorConst.getGreenChannelInt();
        int b = colorConst.getBlueChannelInt();

        JSONObject o = mk(C.EXPR, colorConst).put(C.EXPR, "COLOR_CONST").put(C.VALUE, new JSONArray(Arrays.asList(r, g, b)));
        return app(o);
    }

    @Override
    public V visitMotorGetPowerAction(MotorGetPowerAction<V> motorGetPowerAction) {
        String port = motorGetPowerAction.getUserDefinedPort();
        JSONObject o = mk(C.MOTOR_GET_POWER, motorGetPowerAction).put(C.PORT, port.toLowerCase());
        return app(o);
    }

    @Override
    public V visitDriveAction(DriveAction<V> driveAction) {
        driveAction.getParam().getSpeed().accept(this);
        boolean speedOnly = !processOptionalDuration(driveAction.getParam().getDuration());
        DriveDirection driveDirection = (DriveDirection) driveAction.getDirection();
        JSONObject o = mk(C.DRIVE_ACTION, driveAction).put(C.DRIVE_DIRECTION, driveDirection).put(C.NAME, "mbot").put(C.SPEED_ONLY, speedOnly);
        if ( speedOnly ) {
            return app(o.put(C.SET_TIME, false));
        } else {
            app(o.put(C.SET_TIME, true));
            return app(mk(C.STOP_DRIVE, driveAction).put(C.NAME, "mbot"));
        }
    }

    @Override
    public V visitTurnAction(TurnAction<V> turnAction) {
        turnAction.getParam().getSpeed().accept(this);
        boolean speedOnly = !processOptionalDuration(turnAction.getParam().getDuration());
        ITurnDirection turnDirection = turnAction.getDirection();
        JSONObject o =
            mk(C.TURN_ACTION, turnAction).put(C.TURN_DIRECTION, turnDirection.toString().toLowerCase()).put(C.NAME, "mbot").put(C.SPEED_ONLY, speedOnly);
        if ( speedOnly ) {
            return app(o.put(C.SET_TIME, false));
        } else {
            app(o.put(C.SET_TIME, true));
            return app(mk(C.STOP_DRIVE, turnAction).put(C.NAME, "mbot"));
        }
    }

    @Override
    public V visitCurveAction(CurveAction<V> curveAction) {
        curveAction.getParamLeft().getSpeed().accept(this);
        curveAction.getParamRight().getSpeed().accept(this);
        boolean speedOnly = !processOptionalDuration(curveAction.getParamLeft().getDuration());
        DriveDirection driveDirection = (DriveDirection) curveAction.getDirection();

        JSONObject o = mk(C.CURVE_ACTION, curveAction).put(C.DRIVE_DIRECTION, driveDirection).put(C.NAME, "mbot").put(C.SPEED_ONLY, speedOnly);
        if ( speedOnly ) {
            return app(o.put(C.SET_TIME, false));
        } else {
            app(o.put(C.SET_TIME, true));
            return app(mk(C.STOP_DRIVE, curveAction).put(C.NAME, "mbot"));
        }
    }

    @Override
    public V visitMotorDriveStopAction(MotorDriveStopAction<V> stopAction) {
        JSONObject o = mk(C.STOP_DRIVE, stopAction).put(C.NAME, "mbot");
        return app(o);
    }

    @Override
    public V visitMotorOnAction(MotorOnAction<V> motorOnAction) {
        motorOnAction.getParam().getSpeed().accept(this);
        MotorDuration<V> duration = motorOnAction.getParam().getDuration();
        boolean speedOnly = !processOptionalDuration(duration);
        String port = motorOnAction.getUserDefinedPort();
        port = getMbotMotorPort(port);

        JSONObject o = mk(C.MOTOR_ON_ACTION, motorOnAction).put(C.PORT, port.toLowerCase()).put(C.NAME, "mbot").put(C.SPEED_ONLY, speedOnly);
        if ( speedOnly ) {
            return app(o);
        } else {
            if ( duration.getType() == null ) {
                o.put(C.MOTOR_DURATION, C.TIME);

            } else {
                String durationType = duration.getType().toString().toLowerCase();
                o.put(C.MOTOR_DURATION, durationType);
            }
            app(o);
            return app(mk(C.MOTOR_STOP).put(C.PORT, port.toLowerCase()));
        }
    }

    @Override
    public V visitMotorSetPowerAction(MotorSetPowerAction<V> motorSetPowerAction) {
        String port = motorSetPowerAction.getUserDefinedPort();
        port = getMbotMotorPort(port);

        motorSetPowerAction.getPower().accept(this);
        JSONObject o = mk(C.MOTOR_SET_POWER, motorSetPowerAction).put(C.PORT, port.toLowerCase());
        return app(o);
    }

    @Override
    public V visitMotorStopAction(MotorStopAction<V> motorStopAction) {
        String port = motorStopAction.getUserDefinedPort();
        port = getMbotMotorPort(port);
        JSONObject o = mk(C.MOTOR_STOP, motorStopAction).put(C.PORT, port.toLowerCase());
        return app(o);
    }

    private int map(int x, int in_min, int in_max, int out_min, int out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    // translates to simulation motor port names.
    private String getMbotMotorPort(String port) {
        switch ( port ) {
            case "1": {
                port = C.MOTOR_LEFT;
                break;
            }
            case "2": {
                port = C.MOTOR_RIGHT;
                break;
            }
        }
        return port;
    }

    @Override
    public V visitToneAction(ToneAction<V> toneAction) {
        toneAction.getFrequency().accept(this);
        toneAction.getDuration().accept(this);
        JSONObject o = mk(C.TONE_ACTION, toneAction);
        return app(o);
    }

    @Override
    public V visitPlayNoteAction(PlayNoteAction<V> playNoteAction) {
        String freq = playNoteAction.getFrequency();
        String duration = playNoteAction.getDuration();
        app(mk(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, freq));
        app(mk(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, duration));
        JSONObject o = mk(C.TONE_ACTION, playNoteAction);
        return app(o);
    }

    @Override
    public V visitClearDisplayAction(ClearDisplayAction<V> clearDisplayAction) {
        JSONObject o = mk(C.CLEAR_DISPLAY_ACTION, clearDisplayAction);

        return app(o);
    }

    @Override
    public V visitLEDMatrixImageAction(LEDMatrixImageAction<V> ledMatrixImageAction) {
        ledMatrixImageAction.getValuesToDisplay().accept(this);
        JSONObject o = mk(C.SHOW_IMAGE_ACTION, ledMatrixImageAction).put(C.MODE, ledMatrixImageAction.getDisplayImageMode().toString().toLowerCase());
        return app(o);
    }

    @Override
    public V visitLEDMatrixTextAction(LEDMatrixTextAction<V> ledMatrixTextAction) {
        ledMatrixTextAction.getMsg().accept(this);
        JSONObject o = mk(C.SHOW_TEXT_ACTION, ledMatrixTextAction).put(C.MODE, C.TEXT);
        return app(o);
    }

    @Override
    public V visitLEDMatrixImage(LEDMatrixImage<V> ledMatrixImage) {
        JSONArray jsonImage = new JSONArray();
        for ( int i = 0; i < 16; i++ ) {
            ArrayList<Integer> a = new ArrayList<>();
            for ( int j = 0; j < 8; j++ ) {
                String pixel = ledMatrixImage.getImage()[i][7 - j].trim();
                if ( pixel.equals("#") ) {
                    pixel = "9";
                } else if ( pixel.equals("") ) {
                    pixel = "0";
                }
                a.add(map(Integer.parseInt(pixel), 0, 9, 0, 255));
            }
            jsonImage.put(new JSONArray(a));
        }
        JSONObject o = mk(C.EXPR, ledMatrixImage).put(C.EXPR, C.IMAGE);
        o.put(C.VALUE, jsonImage);
        return app(o);
    }

    @Override
    public V visitLEDMatrixImageShiftFunction(LEDMatrixImageShiftFunction<V> ledMatrixImageShiftFunction) {
        ledMatrixImageShiftFunction.getImage().accept(this);
        ledMatrixImageShiftFunction.getPositions().accept(this);
        IDirection direction = ledMatrixImageShiftFunction.getShiftDirection();
        JSONObject o = mk(C.IMAGE_SHIFT_ACTION, ledMatrixImageShiftFunction).put(C.DIRECTION, direction.toString().toLowerCase()).put(C.NAME, "mbot");
        return app(o);
    }

    @Override
    public V visitLEDMatrixImageInvertFunction(LEDMatrixImageInvertFunction<V> ledMatrixImageInverFunction) {
        ledMatrixImageInverFunction.getImage().accept(this);
        JSONObject o = mk(C.EXPR, ledMatrixImageInverFunction).put(C.EXPR, C.SINGLE_FUNCTION).put(C.OP, C.IMAGE_INVERT_ACTION);
        return app(o);
    }

    @Override
    public V visitSendIRAction(SendIRAction<V> sendIRAction) {
        sendIRAction.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public V visitReceiveIRAction(ReceiveIRAction<V> receiveIRAction) {
        receiveIRAction.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public V visitSerialWriteAction(SerialWriteAction<V> serialWriteAction) {
        serialWriteAction.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public V visitLightSensor(LightSensor<V> lightSensor) {
        lightSensor.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }
}
