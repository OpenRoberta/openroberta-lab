package de.fhg.iais.roberta.visitor;

import java.util.List;

import org.json.JSONObject;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastCharFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastStringFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextCharCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextStringCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.stmt.AssertStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.DebugAction;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.hardware.IWeDoVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineVisitor;

public final class WeDoStackMachineVisitor<V> extends AbstractStackMachineVisitor<V> implements IWeDoVisitor<V> {

    public WeDoStackMachineVisitor(UsedHardwareBean usedHardwareBean, ConfigurationAst configuration, List<List<Phrase<Void>>> phrases) {
        super(configuration);
    }

    @Override
    protected V app(JSONObject o) {
        this.getOpArray().add(o);
        return null;
    }

    @Override
    protected JSONObject mk(String opCode, Phrase<V> phrase) {
        return super.mk(opCode);
    }

    @Override
    public V visitLightAction(LightAction<V> lightAction) {
        ConfigurationComponent confLedBlock = getConfigurationComponent(lightAction.getPort());
        String brickName = confLedBlock.getProperty("VAR");
        if ( brickName != null ) {
            lightAction.getRgbLedColor().accept(this);
            JSONObject o = mk(C.LED_ON_ACTION).put(C.NAME, brickName);
            return app(o);
        } else {
            throw new DbcException("No robot name or no port!");
        }
    }

    @Override
    public V visitLightStatusAction(LightStatusAction<V> lightStatusAction) {
        ConfigurationComponent confLedBlock = getConfigurationComponent(lightStatusAction.getPort());
        String brickName = confLedBlock.getProperty("VAR");
        if ( brickName != null ) {
            // for wedo this block is only for setting off the led, so no test for status required lightStatusAction.getStatus()

            JSONObject o = mk(C.STATUS_LIGHT_ACTION).put(C.NAME, brickName);
            return app(o);
        } else {
            throw new DbcException("No robot name or no port!");
        }
    }

    @Override
    public V visitMotorOnAction(MotorOnAction<V> motorOnAction) {
        ConfigurationComponent confMotorBlock = getConfigurationComponent(motorOnAction.getUserDefinedPort());
        String brickName = confMotorBlock.getProperty("VAR");
        String port = confMotorBlock.getProperty("CONNECTOR");
        if ( brickName != null && port != null ) {
            motorOnAction.getParam().getSpeed().accept(this);
            MotorDuration<V> duration = motorOnAction.getParam().getDuration();
            boolean speedOnly = !processOptionalDuration(duration);
            JSONObject o = mk(C.MOTOR_ON_ACTION).put(C.NAME, brickName).put(C.PORT, port).put(C.SPEED_ONLY, speedOnly).put(C.SPEED_ONLY, speedOnly);
            if ( speedOnly ) {
                return app(o);
            } else {
                app(o);
                return app(mk(C.MOTOR_STOP).put(C.NAME, brickName).put(C.PORT, port));
            }
        } else {
            throw new DbcException("No robot name or no port");
        }
    }

    @Override
    public V visitMotorStopAction(MotorStopAction<V> motorStopAction) {
        ConfigurationComponent confMotorBlock = getConfigurationComponent(motorStopAction.getUserDefinedPort());
        String brickName = confMotorBlock.getProperty("VAR");
        String port = confMotorBlock.getProperty("CONNECTOR");
        if ( brickName != null && port != null ) {
            JSONObject o = mk(C.MOTOR_STOP).put(C.NAME, brickName).put(C.PORT, port);
            return app(o);
        } else {
            throw new DbcException("No robot name or no port");
        }
    }

    @Override
    public V visitClearDisplayAction(ClearDisplayAction<V> clearDisplayAction) {
        JSONObject o = mk(C.CLEAR_DISPLAY_ACTION);
        return app(o);
    }

    @Override
    public V visitShowTextAction(ShowTextAction<V> showTextAction) {
        showTextAction.getMsg().accept(this);
        JSONObject o = mk(C.SHOW_TEXT_ACTION);
        return app(o);
    }

    @Override
    public V visitKeysSensor(KeysSensor<V> keysSensor) {
        ConfigurationComponent keysSensorBlock = getConfigurationComponent(keysSensor.getPort());
        String brickName = keysSensorBlock.getProperty("VAR");
        if ( brickName != null ) {
            JSONObject o = mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.BUTTONS).put(C.NAME, brickName);
            return app(o);
        } else {
            throw new DbcException("operation not supported");
        }
    }

    @Override
    public V visitGyroSensor(GyroSensor<V> gyroSensor) {
        ConfigurationComponent confGyroSensor = getConfigurationComponent(gyroSensor.getPort());
        String brickName = confGyroSensor.getProperty("VAR");
        String port = confGyroSensor.getProperty("CONNECTOR");
        String slot = gyroSensor.getSlot().toString(); // the mode is in the slot?
        if ( brickName != null && port != null ) {
            JSONObject o = mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.GYRO).put(C.NAME, brickName).put(C.PORT, port).put(C.MODE, slot);
            return app(o);
        } else {
            throw new DbcException("operation not supported");
        }
    }

    @Override
    public V visitInfraredSensor(InfraredSensor<V> infraredSensor) {
        ConfigurationComponent confInfraredSensor = getConfigurationComponent(infraredSensor.getPort());
        String brickName = confInfraredSensor.getProperty("VAR");
        String port = confInfraredSensor.getProperty("CONNECTOR");
        if ( brickName != null && port != null ) {
            JSONObject o = mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.INFRARED).put(C.NAME, brickName).put(C.PORT, port);
            return app(o);
        } else {
            throw new DbcException("No robot name or no port!");
        }
    }

    @Override
    public V visitPlayNoteAction(PlayNoteAction<V> playNoteAction) {
        ConfigurationComponent playNoteBlock = getConfigurationComponent(playNoteAction.getPort());
        String brickName = playNoteBlock.getProperty("VAR");
        if ( brickName != null ) {
            JSONObject frequency = mk(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, playNoteAction.getFrequency());
            app(frequency);
            JSONObject duration = mk(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, playNoteAction.getDuration());
            app(duration);
            JSONObject o = mk(C.TONE_ACTION).put(C.NAME, brickName);
            return app(o);
        } else {
            throw new DbcException("No robot name or no port!");
        }
    }

    @Override
    public V visitToneAction(ToneAction<V> toneAction) {
        ConfigurationComponent toneBlock = getConfigurationComponent(toneAction.getPort());
        String brickName = toneBlock.getProperty("VAR");
        if ( brickName != null ) {
            toneAction.getFrequency().accept(this);
            toneAction.getDuration().accept(this);
            JSONObject o = mk(C.TONE_ACTION).put(C.NAME, brickName);
            return app(o);
        } else {
            throw new DbcException("No robot name or no port!");
        }
    }

    @Override
    public V visitTimerSensor(TimerSensor<V> timerSensor) {
        JSONObject o;
        switch ( timerSensor.getMode() ) {
            case "DEFAULT":
            case "VALUE":
                o = mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.TIMER).put(C.PORT, timerSensor.getPort());
                break;
            case "RESET":
                o = mk(C.TIMER_SENSOR_RESET).put(C.PORT, timerSensor.getPort());
                break;
            default:
                throw new DbcException("Invalid Timer Mode " + timerSensor.getMode());
        }
        return app(o);
    }

    @Override
    public V visitGetSampleSensor(GetSampleSensor<V> sensorGetSample) {
        sensorGetSample.getSensor().accept(this);
        return null;
    }

    @Override
    public V visitAssertStmt(AssertStmt<V> assertStmt) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public V visitDebugAction(DebugAction<V> debugAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public V visitMathCastStringFunct(MathCastStringFunct<V> mathCastStringFunct) {
        throw new DbcException("Not supported!");
    }

    @Override
    public V visitMathCastCharFunct(MathCastCharFunct<V> mathCastCharFunct) {
        throw new DbcException("Not supported!");
    }

    @Override
    public V visitTextStringCastNumberFunct(TextStringCastNumberFunct<V> textStringCastNumberFunct) {
        throw new DbcException("Not supported!");
    }

    @Override
    public V visitTextCharCastNumberFunct(TextCharCastNumberFunct<V> textCharCastNumberFunct) {
        throw new DbcException("Not supported!");
    }
}
