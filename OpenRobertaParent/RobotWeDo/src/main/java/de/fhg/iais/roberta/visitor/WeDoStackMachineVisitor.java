package de.fhg.iais.roberta.visitor;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import de.fhg.iais.roberta.components.ConfigurationBlock;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.components.wedo.WeDoConfiguration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.display.ShowTextAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.lang.expr.VarDeclaration;
import de.fhg.iais.roberta.syntax.sensor.generic.BrickSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.collect.WedoUsedHardwareCollectorVisitor;

public final class WeDoStackMachineVisitor<V> extends AbstractWeDoVisitor<V> {
    protected Set<UsedSensor> usedSensors;
    protected Map<String, ConfigurationBlock> usedConfigurationBlocks;
    protected Set<UsedActor> usedActors;
    protected ArrayList<VarDeclaration<Void>> usedVars;

    private WeDoStackMachineVisitor(WeDoConfiguration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrases) {
        super(brickConfiguration);
        WedoUsedHardwareCollectorVisitor codePreprocessVisitor = new WedoUsedHardwareCollectorVisitor(phrases, brickConfiguration);
        this.usedVars = codePreprocessVisitor.getVisitedVars();
        this.usedConfigurationBlocks = codePreprocessVisitor.getUsedConfigurationBlocks();
    }

    public static String generate(WeDoConfiguration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet) {
        Assert.isTrue(!phrasesSet.isEmpty());
        Assert.notNull(brickConfiguration);

        WeDoStackMachineVisitor<Void> astVisitor = new WeDoStackMachineVisitor<>(brickConfiguration, phrasesSet);
        astVisitor.generateCodeFromPhrases(phrasesSet);
        JSONObject generatedCode = new JSONObject();
        generatedCode.put(C.OPS, astVisitor.opArray).put(C.FUNCTION_DECLARATION, astVisitor.fctDecls);
        return generatedCode.toString(2);
    }

    @Override
    public V visitLightAction(LightAction<V> lightAction) {
        String actorName = lightAction.getPort().getOraName();
        ConfigurationBlock confLedBlock = this.usedConfigurationBlocks.get(actorName);
        if ( confLedBlock == null ) {
            throw new DbcException("no LED declared in the configuration");
        }
        String brickName = confLedBlock.getConfPortOf("VAR");
        if ( (brickName != null) ) {
            lightAction.getRgbLedColor().visit(this);
            JSONObject o = mk(C.LED_ON_ACTION).put(C.NAME, brickName);
            return app(o);
        } else {
            throw new DbcException("No robot name or no port!");
        }
    }

    @Override
    public V visitLightStatusAction(LightStatusAction<V> lightStatusAction) {
        String actorName = lightStatusAction.getPort().getOraName();
        ConfigurationBlock confLedBlock = this.usedConfigurationBlocks.get(actorName);
        if ( confLedBlock == null ) {
            throw new DbcException("no LED declared in the configuration");
        }
        String brickName = confLedBlock.getConfPortOf("VAR");
        if ( (brickName != null) ) {
            // for wedo this block is only for setting off the led, so no test for status required lightStatusAction.getStatus()

            JSONObject o = mk(C.STATUS_LIGHT_ACTION).put(C.NAME, brickName);
            return app(o);
        } else {
            throw new DbcException("No robot name or no port!");
        }
    }

    @Override
    public V visitMotorOnAction(MotorOnAction<V> motorOnAction) {
        boolean isDuration = motorOnAction.getParam().getDuration() != null;
        String actorName = motorOnAction.getPort().getOraName();
        ConfigurationBlock confMotorBlock = this.usedConfigurationBlocks.get(actorName);
        if ( confMotorBlock == null ) {
            throw new DbcException("no motor declared in the configuration");
        }
        String brickName = confMotorBlock.getConfPortOf("VAR");
        String port = confMotorBlock.getConfPortOf("CONNECTOR");
        if ( (brickName != null) && (port != null) ) {
            motorOnAction.getParam().getSpeed().visit(this);
            if ( isDuration ) {
                motorOnAction.getParam().getDuration().getValue().visit(this);
            } else {
                JSONObject nullDuration = mk(C.EXPR).put(C.EXPR, C.NUM_CONST).put(C.VALUE, -1);
                this.opArray.add(nullDuration);
            }
            JSONObject o = mk(C.MOTOR_ON_ACTION).put(C.NAME, brickName).put(C.PORT, port);
            return app(o);
        } else {
            throw new DbcException("No robot name or no port");
        }
    }

    @Override
    public V visitMotorStopAction(MotorStopAction<V> motorStopAction) {
        String actorName = motorStopAction.getPort().getOraName();
        ConfigurationBlock confMotorBlock = this.usedConfigurationBlocks.get(actorName);
        if ( confMotorBlock == null ) {
            throw new DbcException("no motor declared in the configuration");
        }
        String brickName = confMotorBlock.getConfPortOf("VAR");
        String port = confMotorBlock.getConfPortOf("CONNECTOR");
        if ( (brickName != null) && (port != null) ) {
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
        showTextAction.getMsg().visit(this);
        JSONObject o = mk(C.SHOW_TEXT_ACTION);
        return app(o);
    }

    @Override
    public V visitBrickSensor(BrickSensor<V> brickSensor) {
        String sensorName = brickSensor.getPort().getOraName();
        ConfigurationBlock brickConfBlock = this.usedConfigurationBlocks.get(sensorName);
        if ( brickConfBlock == null ) {
            throw new DbcException("no button sensor declared in the configuration");
        }
        String brickName = brickConfBlock.getConfPortOf("VAR");
        String port = brickConfBlock.getConfPortOf("CONNECTOR");
        if ( (brickName != null) ) {
            JSONObject o = mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.BUTTONS).put(C.NAME, brickName).put(C.PORT, port);
            return app(o);
        } else {
            throw new DbcException("operation not supported");
        }
    }

    @Override
    public V visitGyroSensor(GyroSensor<V> gyroSensor) {
        String sensorName = gyroSensor.getPort().getOraName();
        ConfigurationBlock confGyroSensor = this.usedConfigurationBlocks.get(sensorName);
        if ( confGyroSensor == null ) {
            throw new DbcException("no gyro sensor declared in the configuration");
        }
        String brickName = confGyroSensor.getConfPortOf("VAR");
        String port = confGyroSensor.getConfPortOf("CONNECTOR");
        String slot = gyroSensor.getSlot().toString();
        if ( (brickName != null) && (port != null) ) {
            JSONObject o = mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.GYRO).put(C.NAME, brickName).put(C.PORT, port).put(C.SLOT, slot);
            return app(o);
        } else {
            throw new DbcException("operation not supported");
        }
    }

    @Override
    public V visitInfraredSensor(InfraredSensor<V> infraredSensor) {
        String sensorName = infraredSensor.getPort().getOraName();
        ConfigurationBlock confInfraredSensor = this.usedConfigurationBlocks.get(sensorName);
        if ( confInfraredSensor == null ) {
            throw new DbcException("no infrared sensor declared in the configuration");
        }
        String brickName = confInfraredSensor.getConfPortOf("VAR");
        String port = confInfraredSensor.getConfPortOf("CONNECTOR");
        if ( (brickName != null) && (port != null) ) {
            JSONObject o = mk(C.GET_SAMPLE).put(C.GET_SAMPLE, C.INFRARED).put(C.NAME, brickName).put(C.PORT, port);
            return app(o);
        } else {
            throw new DbcException("No robot name or no port!");
        }
    }

    @Override
    public V visitPlayNoteAction(PlayNoteAction<V> playNoteAction) {
        String actorName = playNoteAction.getPort().getOraName();
        ConfigurationBlock confLedBlock = this.usedConfigurationBlocks.get(actorName);
        if ( confLedBlock == null ) {
            throw new DbcException("no piezo actuator declared in the configuration");
        }
        String brickName = confLedBlock.getConfPortOf("VAR");
        if ( (brickName != null) ) {
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
        String actorName = toneAction.getPort().getOraName();
        ConfigurationBlock confToneBlock = this.usedConfigurationBlocks.get(actorName);
        if ( confToneBlock == null ) {
            throw new DbcException("no piezo actuator declared in the configuration");
        }
        String brickName = confToneBlock.getConfPortOf("VAR");
        if ( (brickName != null) ) {
            toneAction.getFrequency().visit(this);
            toneAction.getDuration().visit(this);
            JSONObject o = mk(C.TONE_ACTION).put(C.NAME, brickName);
            return app(o);
        } else {
            throw new DbcException("No robot name or no port!");
        }
    }
}
