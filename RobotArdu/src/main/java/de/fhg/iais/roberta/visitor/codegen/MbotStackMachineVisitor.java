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
import de.fhg.iais.roberta.syntax.Phrase;
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
import de.fhg.iais.roberta.syntax.actors.arduino.LEDMatrixSetBrightnessAction;
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
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.util.basic.C;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.syntax.MotorDuration;
import de.fhg.iais.roberta.visitor.hardware.IMbotVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineVisitor;

public class MbotStackMachineVisitor extends AbstractStackMachineVisitor implements IMbotVisitor<Void> {

    public MbotStackMachineVisitor(ConfigurationAst configuration, List<List<Phrase>> phrases) {
        super(configuration);
        Assert.isTrue(!phrases.isEmpty());
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.BUTTONS).put(C.MODE, "center");
        return add(o);
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        String port = timerSensor.getUserDefinedPort();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.TIMER).put(C.PORT, port).put(C.NAME, "mbot");
        return add(o);
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        String port = timerReset.sensorPort;
        JSONObject o = makeNode(C.TIMER_SENSOR_RESET).put(C.PORT, port).put(C.NAME, "mbot");
        return add(o);
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        String mode = ultrasonicSensor.getMode();
        String port = ultrasonicSensor.getUserDefinedPort();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.ULTRASONIC).put(C.PORT, port).put(C.MODE, mode.toLowerCase()).put(C.NAME, "mbot");
        return add(o);
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        String port = infraredSensor.getUserDefinedPort();
        String slot = infraredSensor.getSlot();
        if ( slot.equals("1") ) {
            slot = C.LEFT;
        } else if ( slot.equals("2") ) {
            slot = C.RIGHT;
        }
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.INFRARED).put(C.PORT, port).put(C.MODE, slot).put(C.NAME, "mbot");
        return add(o);
    }

    @Override
    public Void visitLightAction(LightAction lightAction) {
        String mode = lightAction.mode.toString().toLowerCase();
        lightAction.rgbLedColor.accept(this);
        String port = lightAction.port;
        JSONObject o = makeNode(C.LIGHT_ACTION).put(C.MODE, mode).put(C.PORT, port).put(C.NAME, "mbot");
        return add(o);
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction lightStatusAction) {
        JSONObject o =
            makeNode(C.STATUS_LIGHT_ACTION)
                .put(C.MODE, lightStatusAction.status)
                .put(C.PORT, lightStatusAction.getUserDefinedPort())
                .put(C.NAME, "mbot");
        return add(o);
    }

    @Override
    public Void visitColorConst(ColorConst colorConst) {
        int r = colorConst.getRedChannelInt();
        int g = colorConst.getGreenChannelInt();
        int b = colorConst.getBlueChannelInt();

        JSONObject o = makeNode(C.EXPR).put(C.EXPR, "COLOR_CONST").put(C.VALUE, new JSONArray(Arrays.asList(r, g, b)));
        return add(o);
    }

    @Override
    public Void visitMotorGetPowerAction(MotorGetPowerAction motorGetPowerAction) {
        String port = motorGetPowerAction.getUserDefinedPort();
        JSONObject o = makeNode(C.MOTOR_GET_POWER).put(C.PORT, port.toLowerCase());
        return add(o);
    }

    @Override
    public Void visitDriveAction(DriveAction driveAction) {
        driveAction.param.getSpeed().accept(this);
        boolean speedOnly = !processOptionalDuration(driveAction.param.getDuration());
        DriveDirection driveDirection = (DriveDirection) driveAction.direction;
        JSONObject o = makeNode(C.DRIVE_ACTION).put(C.DRIVE_DIRECTION, driveDirection).put(C.NAME, "mbot").put(C.SPEED_ONLY, speedOnly);
        if ( speedOnly ) {
            return add(o.put(C.SET_TIME, false));
        } else {
            add(o.put(C.SET_TIME, true));
            return add(makeNode(C.STOP_DRIVE).put(C.NAME, "mbot"));
        }
    }

    @Override
    public Void visitTurnAction(TurnAction turnAction) {
        turnAction.param.getSpeed().accept(this);
        boolean speedOnly = !processOptionalDuration(turnAction.param.getDuration());
        ITurnDirection turnDirection = turnAction.direction;
        JSONObject o =
            makeNode(C.TURN_ACTION).put(C.TURN_DIRECTION, turnDirection.toString().toLowerCase()).put(C.NAME, "mbot").put(C.SPEED_ONLY, speedOnly);
        if ( speedOnly ) {
            return add(o.put(C.SET_TIME, false));
        } else {
            add(o.put(C.SET_TIME, true));
            return add(makeNode(C.STOP_DRIVE).put(C.NAME, "mbot"));
        }
    }

    @Override
    public Void visitCurveAction(CurveAction curveAction) {
        curveAction.paramLeft.getSpeed().accept(this);
        curveAction.paramRight.getSpeed().accept(this);
        boolean speedOnly = !processOptionalDuration(curveAction.paramLeft.getDuration());
        DriveDirection driveDirection = (DriveDirection) curveAction.direction;

        JSONObject o = makeNode(C.CURVE_ACTION).put(C.DRIVE_DIRECTION, driveDirection).put(C.NAME, "mbot").put(C.SPEED_ONLY, speedOnly);
        if ( speedOnly ) {
            return add(o.put(C.SET_TIME, false));
        } else {
            add(o.put(C.SET_TIME, true));
            return add(makeNode(C.STOP_DRIVE).put(C.NAME, "mbot"));
        }
    }

    @Override
    public Void visitMotorDriveStopAction(MotorDriveStopAction stopAction) {
        JSONObject o = makeNode(C.STOP_DRIVE).put(C.NAME, "mbot");
        return add(o);
    }

    @Override
    public Void visitLightSensor(LightSensor lightSensor) {
        return null;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        motorOnAction.param.getSpeed().accept(this);
        MotorDuration duration = motorOnAction.param.getDuration();
        boolean speedOnly = !processOptionalDuration(duration);
        String port = motorOnAction.getUserDefinedPort();
        port = getMbotMotorPort(port);

        JSONObject o = makeNode(C.MOTOR_ON_ACTION).put(C.PORT, port.toLowerCase()).put(C.NAME, "mbot").put(C.SPEED_ONLY, speedOnly);
        if ( speedOnly ) {
            return add(o.put(C.SET_TIME, false));
        } else {
            add(o.put(C.SET_TIME, true));
            if ( duration.getType() == null ) {
                o.put(C.MOTOR_DURATION, C.TIME);

            } else {
                String durationType = duration.getType().toString().toLowerCase();
                o.put(C.MOTOR_DURATION, durationType);
            }
            add(o);
            return add(makeNode(C.MOTOR_STOP).put(C.PORT, port.toLowerCase()));
        }
    }

    @Override
    public Void visitMotorSetPowerAction(MotorSetPowerAction motorSetPowerAction) {
        String port = motorSetPowerAction.getUserDefinedPort();
        port = getMbotMotorPort(port);

        motorSetPowerAction.power.accept(this);
        JSONObject o = makeNode(C.MOTOR_SET_POWER).put(C.PORT, port.toLowerCase());
        return add(o);
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        String port = motorStopAction.getUserDefinedPort();
        port = getMbotMotorPort(port);
        JSONObject o = makeNode(C.MOTOR_STOP).put(C.PORT, port.toLowerCase());
        return add(o);
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
    public Void visitToneAction(ToneAction toneAction) {
        toneAction.frequency.accept(this);
        toneAction.duration.accept(this);
        JSONObject o = makeNode(C.TONE_ACTION);
        return add(o);
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        String freq = playNoteAction.frequency;
        String duration = playNoteAction.duration;
        add(makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, freq));
        add(makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, duration));
        JSONObject o = makeNode(C.TONE_ACTION);
        return add(o);
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        JSONObject o = makeNode(C.CLEAR_DISPLAY_ACTION);

        return add(o);
    }

    @Override
    public Void visitLEDMatrixImageAction(LEDMatrixImageAction ledMatrixImageAction) {
        ledMatrixImageAction.valuesToDisplay.accept(this);
        JSONObject o = makeNode(C.SHOW_IMAGE_ACTION).put(C.MODE, ledMatrixImageAction.displayImageMode.toString().toLowerCase());
        return add(o);
    }

    @Override
    public Void visitLEDMatrixTextAction(LEDMatrixTextAction ledMatrixTextAction) {
        ledMatrixTextAction.msg.accept(this);
        JSONObject o = makeNode(C.SHOW_TEXT_ACTION).put(C.MODE, C.TEXT);
        return add(o);
    }

    @Override
    public Void visitLEDMatrixImage(LEDMatrixImage ledMatrixImage) {
        JSONArray jsonImage = new JSONArray();
        for ( int i = 0; i < 16; i++ ) {
            ArrayList<Integer> a = new ArrayList<>();
            for ( int j = 0; j < 8; j++ ) {
                String pixel = ledMatrixImage.image[i][7 - j].trim();
                if ( pixel.equals("#") ) {
                    pixel = "9";
                } else if ( pixel.equals("") ) {
                    pixel = "0";
                }
                a.add(map(Integer.parseInt(pixel), 0, 9, 0, 255));
            }
            jsonImage.put(new JSONArray(a));
        }
        JSONObject o = makeNode(C.EXPR).put(C.EXPR, C.IMAGE);
        o.put(C.VALUE, jsonImage);
        return add(o);
    }

    @Override
    public Void visitLEDMatrixImageShiftFunction(LEDMatrixImageShiftFunction ledMatrixImageShiftFunction) {
        ledMatrixImageShiftFunction.image.accept(this);
        ledMatrixImageShiftFunction.positions.accept(this);
        IDirection direction = ledMatrixImageShiftFunction.shiftDirection;
        JSONObject o = makeNode(C.IMAGE_SHIFT_ACTION).put(C.DIRECTION, direction.toString().toLowerCase()).put(C.NAME, "mbot");
        return add(o);
    }

    @Override
    public Void visitLEDMatrixImageInvertFunction(LEDMatrixImageInvertFunction ledMatrixImageInverFunction) {
        ledMatrixImageInverFunction.image.accept(this);
        JSONObject o = makeNode(C.EXPR).put(C.EXPR, C.SINGLE_FUNCTION).put(C.OP, C.IMAGE_INVERT_ACTION);
        return add(o);
    }

    @Override
    public Void visitLEDMatrixSetBrightnessAction(LEDMatrixSetBrightnessAction ledMatrixSetBrightnessAction) {
        ledMatrixSetBrightnessAction.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitSendIRAction(SendIRAction sendIRAction) {
        sendIRAction.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitReceiveIRAction(ReceiveIRAction receiveIRAction) {
        receiveIRAction.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    @Override
    public Void visitSerialWriteAction(SerialWriteAction serialWriteAction) {
        serialWriteAction.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

    public Void visitVoltageSensor(VoltageSensor voltageSensor) {
        voltageSensor.addInfo(NepoInfo.warning("SIM_BLOCK_NOT_SUPPORTED"));
        return null;
    }

}
