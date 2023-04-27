package de.fhg.iais.roberta.visitor.codegen;

import org.json.JSONObject;

import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastCharFunct;
import de.fhg.iais.roberta.syntax.lang.functions.MathCastStringFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextCharCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.functions.TextStringCastNumberFunct;
import de.fhg.iais.roberta.syntax.lang.stmt.AssertStmt;
import de.fhg.iais.roberta.syntax.lang.stmt.DebugAction;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.util.basic.C;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.MotorDuration;
import de.fhg.iais.roberta.visitor.IWeDoVisitor;
import de.fhg.iais.roberta.visitor.lang.codegen.AbstractStackMachineVisitor;

public final class WeDoStackMachineVisitor extends AbstractStackMachineVisitor implements IWeDoVisitor<Void> {

    public WeDoStackMachineVisitor(ConfigurationAst configuration) {
        super(configuration);
        debugger = false;
    }

    @Override
    public Void visitLightAction(LightAction lightAction) {
        ConfigurationComponent confLedBlock = getConfigurationComponent(lightAction.port);
        String brickName = confLedBlock.getProperty("VAR");
        if ( brickName != null ) {
            lightAction.rgbLedColor.accept(this);
            JSONObject o = makeNode(C.LED_ON_ACTION).put(C.NAME, brickName);
            return add(o);
        } else {
            throw new DbcException("No robot name or no port!");
        }
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction lightStatusAction) {
        ConfigurationComponent confLedBlock = getConfigurationComponent(lightStatusAction.getUserDefinedPort());
        String brickName = confLedBlock.getProperty("VAR");
        if ( brickName != null ) {
            // for wedo this block is only for setting off the led, so no test for status required lightStatusAction.getStatus()

            JSONObject o = makeNode(C.STATUS_LIGHT_ACTION).put(C.NAME, brickName);
            return add(o);
        } else {
            throw new DbcException("No robot name or no port!");
        }
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction motorOnAction) {
        ConfigurationComponent confMotorBlock = getConfigurationComponent(motorOnAction.getUserDefinedPort());
        String brickName = confMotorBlock.getProperty("VAR");
        String port = confMotorBlock.getProperty("CONNECTOR");
        if ( brickName != null && port != null ) {
            motorOnAction.param.getSpeed().accept(this);
            MotorDuration duration = motorOnAction.param.getDuration();
            boolean speedOnly = !processOptionalDuration(duration);
            JSONObject o = makeNode(C.MOTOR_ON_ACTION).put(C.NAME, brickName).put(C.PORT, port).put(C.SPEED_ONLY, speedOnly).put(C.SPEED_ONLY, speedOnly);
            if ( speedOnly ) {
                return add(o);
            } else {
                add(o);
                return add(makeNode(C.MOTOR_STOP).put(C.NAME, brickName).put(C.PORT, port));
            }
        } else {
            throw new DbcException("No robot name or no port");
        }
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction motorStopAction) {
        ConfigurationComponent confMotorBlock = getConfigurationComponent(motorStopAction.getUserDefinedPort());
        String brickName = confMotorBlock.getProperty("VAR");
        String port = confMotorBlock.getProperty("CONNECTOR");
        if ( brickName != null && port != null ) {
            JSONObject o = makeNode(C.MOTOR_STOP).put(C.NAME, brickName).put(C.PORT, port);
            return add(o);
        } else {
            throw new DbcException("No robot name or no port");
        }
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        JSONObject o = makeNode(C.CLEAR_DISPLAY_ACTION);
        return add(o);
    }

    @Override
    public Void visitShowTextAction(ShowTextAction showTextAction) {
        showTextAction.msg.accept(this);
        JSONObject o = makeNode(C.SHOW_TEXT_ACTION);
        return add(o);
    }

    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        ConfigurationComponent keysSensorBlock = getConfigurationComponent(keysSensor.getUserDefinedPort());
        String brickName = keysSensorBlock.getProperty("VAR");
        if ( brickName != null ) {
            JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.BUTTONS).put(C.NAME, brickName);
            return add(o);
        } else {
            throw new DbcException("operation not supported");
        }
    }

    @Override
    public Void visitGyroSensor(GyroSensor gyroSensor) {
        ConfigurationComponent confGyroSensor = getConfigurationComponent(gyroSensor.getUserDefinedPort());
        String brickName = confGyroSensor.getProperty("VAR");
        String port = confGyroSensor.getProperty("CONNECTOR");
        String slot = gyroSensor.getSlot(); // the mode is in the slot?
        if ( brickName != null && port != null ) {
            JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.GYRO).put(C.NAME, brickName).put(C.PORT, port).put(C.MODE, slot);
            return add(o);
        } else {
            throw new DbcException("operation not supported");
        }
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor infraredSensor) {
        ConfigurationComponent confInfraredSensor = getConfigurationComponent(infraredSensor.getUserDefinedPort());
        String brickName = confInfraredSensor.getProperty("VAR");
        String port = confInfraredSensor.getProperty("CONNECTOR");
        if ( brickName != null && port != null ) {
            JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.INFRARED).put(C.NAME, brickName).put(C.PORT, port);
            return add(o);
        } else {
            throw new DbcException("No robot name or no port!");
        }
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        ConfigurationComponent playNoteBlock = getConfigurationComponent(playNoteAction.port);
        String brickName = playNoteBlock.getProperty("VAR");
        if ( brickName != null ) {
            JSONObject frequency = makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, playNoteAction.frequency);
            add(frequency);
            JSONObject duration = makeNode(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, playNoteAction.duration);
            add(duration);
            JSONObject o = makeNode(C.TONE_ACTION).put(C.NAME, brickName);
            return add(o);
        } else {
            throw new DbcException("No robot name or no port!");
        }
    }

    @Override
    public Void visitToneAction(ToneAction toneAction) {
        ConfigurationComponent toneBlock = getConfigurationComponent(toneAction.port);
        String brickName = toneBlock.getProperty("VAR");
        if ( brickName != null ) {
            toneAction.frequency.accept(this);
            toneAction.duration.accept(this);
            JSONObject o = makeNode(C.TONE_ACTION).put(C.NAME, brickName);
            return add(o);
        } else {
            throw new DbcException("No robot name or no port!");
        }
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        JSONObject o = makeNode(C.GET_SAMPLE).put(C.GET_SAMPLE, C.TIMER).put(C.PORT, timerSensor.getUserDefinedPort());
        return add(o);
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        JSONObject o = makeNode(C.TIMER_SENSOR_RESET).put(C.PORT, timerReset.sensorPort);
        return add(o);
    }

    @Override
    public Void visitAssertStmt(AssertStmt assertStmt) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitDebugAction(DebugAction debugAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitMathCastStringFunct(MathCastStringFunct mathCastStringFunct) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitMathCastCharFunct(MathCastCharFunct mathCastCharFunct) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitTextStringCastNumberFunct(TextStringCastNumberFunct textStringCastNumberFunct) {
        throw new DbcException("Not supported!");
    }

    @Override
    public Void visitTextCharCastNumberFunct(TextCharCastNumberFunct textCharCastNumberFunct) {
        throw new DbcException("Not supported!");
    }
}
