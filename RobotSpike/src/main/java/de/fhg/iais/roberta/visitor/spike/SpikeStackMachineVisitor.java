package de.fhg.iais.roberta.visitor.spike;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import de.fhg.iais.roberta.bean.NNBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOffHiddenAction;
import de.fhg.iais.roberta.syntax.action.light.RgbLedOnHiddenAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnForAction;
import de.fhg.iais.roberta.syntax.action.spike.DisplayClearAction;
import de.fhg.iais.roberta.syntax.action.spike.DisplayImageAction;
import de.fhg.iais.roberta.syntax.action.spike.DisplayTextAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffCurveAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffCurveForAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffOnAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffOnForAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffStopAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffTurnAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorDiffTurnForAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.spike.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.spike.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.spike.PlayToneAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.expr.ColorConst;
import de.fhg.iais.roberta.syntax.sensor.generic.ColorSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.spike.Image;
import de.fhg.iais.roberta.syntax.spike.PredefinedImage;
import de.fhg.iais.roberta.util.basic.C;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.ISpikeVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineVisitor;

public class SpikeStackMachineVisitor extends AbstractStackMachineVisitor implements ISpikeVisitor<Void> {

    private boolean noColor = false;

    public SpikeStackMachineVisitor(ConfigurationAst configuration, List<List<Phrase>> phrases, UsedHardwareBean usedHardwareBean, NNBean nnBean) {
        super(configuration, usedHardwareBean, nnBean);
        Assert.isTrue(!phrases.isEmpty());
    }

    @Override
    public Void visitColorConst(ColorConst colorConst) {
        this.noColor = false;
        String color = colorConst.getHexValueAsString().toUpperCase();
        switch ( color ) {
            case "#E701A7":
            case "#571CC1":
            case "#3590F5":
            case "#77E7FF":
            case "#0FCB54":
            case "#0BA845":
            case "#F7F700":
            case "#FAAC01":
            case "#FA010C":
            case "#000000":
            case "#FFFFFF":
                break;
            case "#EBC300":
                this.noColor = true;
                return null;
            default:
                throw new DbcException("Invalid color constant: " + color);
        }
        JSONObject o = makeNode(C.EXPR).put(C.EXPR, C.COLOR_CONST).put(C.VALUE, color);
        return add(o);
    }

    @Override
    public Void visitTouchSensor(TouchSensor touchSensor) {
        String port = touchSensor.getUserDefinedPort();
        String mode = touchSensor.getMode().toLowerCase();

        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.TOUCH).put(C.PORT, port).put(C.MODE, mode);
        return add(o);
    }

    @Override
    public Void visitRgbLedOnHiddenAction(RgbLedOnHiddenAction rgbLedOnHiddenAction) {
        rgbLedOnHiddenAction.colour.accept(this);
        JSONObject o;
        if ( this.noColor ) {
            o = makeNode(C.RGBLED_OFF_ACTION);
        } else {
            o = makeNode(C.RGBLED_ON_ACTION);
        }
        return add(o);
    }

    @Override
    public Void visitRgbLedOffHiddenAction(RgbLedOffHiddenAction rgbLedOffHiddenAction) {
        JSONObject o = makeNode(C.RGBLED_OFF_ACTION);
        return add(o);
    }

    @Override
    public Void visitDisplayTextAction(DisplayTextAction displayTextAction) {
        displayTextAction.textToDisplay.accept(this);
        JSONObject o = makeNode(C.SHOW_TEXT_ACTION).put(C.MODE, displayTextAction.displayTextMode.toLowerCase());
        return add(o);
    }

    @Override
    public Void visitDisplayImageAction(DisplayImageAction displayImageAction) {
        displayImageAction.valuesToDisplay.accept(this);
        JSONObject o = makeNode(C.SHOW_IMAGE_ACTION).put(C.MODE, displayImageAction.displayImageMode.toLowerCase());
        return add(o);
    }

    @Override
    public Void visitDisplayClearAction(DisplayClearAction displayClearAction) {
        JSONObject o = makeNode(C.CLEAR_DISPLAY_ACTION);
        return add(o);
    }

    @Override
    public Void visitImage(Image image) {
        JSONArray jsonImage = new JSONArray();
        for ( int i = 0; i < 5; i++ ) {
            ArrayList<Integer> a = new ArrayList<>();
            for ( int j = 0; j < 5; j++ ) {
                String pixel = image.image[i][j].trim();
                if ( pixel.equals("#") ) {
                    pixel = "9";
                } else if ( pixel.equals("") ) {
                    pixel = "0";
                }
                a.add(map(Integer.parseInt(pixel), 0, 9, 0, 255));
            }
            jsonImage.put(new JSONArray(a));
        }
        JSONObject o = makeNode(C.EXPR).put(C.EXPR, image.getKind().getName().toLowerCase());
        o.put(C.VALUE, jsonImage);
        return add(o);
    }

    @Override
    public Void visitPredefinedImage(PredefinedImage predefinedImage) {
        final String image = predefinedImage.getImageName().getImageString();
        JSONArray jsonImage = new JSONArray();
        if ( image.contains(":") ) {
            String[] rows = image.split(":");
            for ( String row : rows ) {
                ArrayList<Integer> a = new ArrayList<>();
                for ( int j = 0; j < row.length(); j++ ) {
                    int val = Character.getNumericValue(row.charAt(j));
                    a.add(map(val, 0, 9, 0, 255));
                }
                jsonImage.put(new JSONArray(a));
            }
        } else {
            for ( String row : image.split("\n") ) {
                ArrayList<Integer> a = new ArrayList<>();
                for ( String col : row.split(",") ) {
                    int val = Integer.parseInt(col.trim());
                    a.add(map(val, 0, 9, 0, 255));
                }
                jsonImage.put(new JSONArray(a));
            }
        }

        JSONObject o = makeNode(C.EXPR).put(C.EXPR, C.IMAGE).put(C.VALUE, jsonImage);
        return add(o);
    }

    private int map(int x, int in_min, int in_max, int out_min, int out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    @Override
    public Void visitColorSensor(ColorSensor colorSensor) {
        String mode = colorSensor.getMode();
        if ( mode.equals("COLOUR") ) {
            mode = C.COLOUR_HEX;
        }
        String port = colorSensor.getUserDefinedPort();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.COLOR).put(C.PORT, port).put(C.MODE, mode.toLowerCase());
        return add(o);
    }

    @Override
    public Void visitMotorOnForAction(MotorOnForAction motorOnForAction) {
        motorOnForAction.power.accept(this);
        motorOnForAction.value.accept(this);
        String name = motorOnForAction.getUserDefinedPort();
        ConfigurationComponent comp = this.configuration.getConfigurationComponent(name);
        String port = comp != null ? comp.componentProperties.get("PORT") : name;
        String unit = motorOnForAction.unit.toLowerCase();
        if ( unit.equals("degrees") ) {
            unit = "degree";
        }
        JSONObject o = makeNode(C.MOTOR_ON_ACTION).put(C.PORT, port.toLowerCase()).put(C.NAME, port.toLowerCase()).put(C.SPEED_ONLY, false).put(C.MOTOR_DURATION, unit);
        add(o);
        return add(makeNode(C.MOTOR_STOP).put(C.PORT, port.toLowerCase()));
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        motorOnAction.power.accept(this);
        String name = motorOnAction.getUserDefinedPort();
        ConfigurationComponent comp = this.configuration.getConfigurationComponent(name);
        String port = comp != null ? comp.componentProperties.get("PORT") : name;
        JSONObject o = makeNode(C.MOTOR_ON_ACTION).put(C.PORT, port.toLowerCase()).put(C.NAME, port.toLowerCase()).put(C.SPEED_ONLY, true);
        return add(o);
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        String name = motorStopAction.getUserDefinedPort();
        ConfigurationComponent comp = this.configuration.getConfigurationComponent(name);
        String port = comp != null ? comp.componentProperties.get("PORT") : name;
        JSONObject o = makeNode(C.MOTOR_STOP).put(C.PORT, port.toLowerCase());
        return add(o);
    }

    @Override
    public Void visitMotorDiffOnForAction(MotorDiffOnForAction motorDiffOnForAction) {
        motorDiffOnForAction.power.accept(this);
        motorDiffOnForAction.distance.accept(this);
        JSONObject o =
            makeNode(C.DRIVE_ACTION).put(C.DRIVE_DIRECTION, motorDiffOnForAction.direction).put(C.SPEED_ONLY, false);
        add(o);
        return add(makeNode(C.STOP_DRIVE));
    }

    @Override
    public Void visitMotorDiffTurnForAction(MotorDiffTurnForAction motorDiffTurnForAction) {
        motorDiffTurnForAction.power.accept(this);
        motorDiffTurnForAction.degrees.accept(this);
        JSONObject o =
            makeNode(C.TURN_ACTION)
                .put(C.TURN_DIRECTION, motorDiffTurnForAction.direction.toLowerCase())
                .put(C.SPEED_ONLY, false);
        return add(o);
    }

    @Override
    public Void visitMotorDiffCurveForAction(MotorDiffCurveForAction motorDiffCurveForAction) {
        motorDiffCurveForAction.powerLeft.accept(this);
        motorDiffCurveForAction.powerRight.accept(this);
        motorDiffCurveForAction.distance.accept(this);
        JSONObject o =
            makeNode(C.CURVE_ACTION).put(C.DRIVE_DIRECTION, motorDiffCurveForAction.direction).put(C.SPEED_ONLY, false);
        add(o);
        return add(makeNode(C.STOP_DRIVE));
    }

    @Override
    public Void visitMotorDiffCurveAction(MotorDiffCurveAction motorDiffCurveAction) {
        motorDiffCurveAction.powerLeft.accept(this);
        motorDiffCurveAction.powerRight.accept(this);
        JSONObject o =
            makeNode(C.CURVE_ACTION).put(C.DRIVE_DIRECTION, motorDiffCurveAction.direction).put(C.SPEED_ONLY, true);
        return add(o);
    }

    @Override
    public Void visitMotorDiffOnAction(MotorDiffOnAction motorDiffOnAction) {
        motorDiffOnAction.power.accept(this);
        JSONObject o =
            makeNode(C.DRIVE_ACTION).put(C.DRIVE_DIRECTION, motorDiffOnAction.direction).put(C.SPEED_ONLY, true);
        return add(o);
    }

    @Override
    public Void visitMotorDiffTurnAction(MotorDiffTurnAction motorDiffTurnAction) {
        motorDiffTurnAction.power.accept(this);
        JSONObject o =
            makeNode(C.TURN_ACTION)
                .put(C.TURN_DIRECTION, motorDiffTurnAction.direction.toLowerCase())
                .put(C.SPEED_ONLY, true);
        return add(o);
    }

    @Override
    public Void visitMotorDiffStopAction(MotorDiffStopAction motorDiffStopAction) {
        JSONObject o = makeNode(C.STOP_DRIVE);
        return add(o);
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        String portName = keysSensor.getUserDefinedPort();
        ConfigurationComponent configurationComponent = this.configuration.getConfigurationComponent(portName);
        String port = configurationComponent != null ? configurationComponent.getProperty("PIN1") : portName;
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.BUTTONS).put(C.PORT, port.toLowerCase());
        return add(o);
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor ultrasonicSensor) {
        String mode = ultrasonicSensor.getMode().toLowerCase();
        String port = ultrasonicSensor.getUserDefinedPort();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.ULTRASONIC).put(C.MODE, mode).put(C.PORT, port);
        return add(o);
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        String port = timerSensor.getUserDefinedPort();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.TIMER).put(C.PORT, port);
        return add(o);
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        String port = timerReset.sensorPort;
        JSONObject o = makeNode(C.TIMER_SENSOR_RESET).put(C.PORT, port);
        return add(o);
    }

    @Override
    public Void visitGyroSensor(GyroSensor gyroSensor) {
        String mode = gyroSensor.getMode().toLowerCase();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.GYRO).put(C.MODE, mode);
        return add(o);
    }

    @Override
    public Void visitGestureSensor(GestureSensor gestureSensor) {
        String mode = gestureSensor.getMode().toLowerCase();
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.GESTURE).put(C.MODE, mode);
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
    public Void visitPlayToneAction(PlayToneAction playToneAction) {
        playToneAction.frequency.accept(this);
        playToneAction.duration.accept(this);
        JSONObject o = makeNode(C.TONE_ACTION);
        return add(o);
    }
}
