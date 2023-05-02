package de.fhg.iais.roberta.visitor;

import java.util.HashMap;
import java.util.Map;

import de.fhg.iais.roberta.bean.NewUsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.generic.MbedPinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.mbed.LedOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.SingleMotorOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.SingleMotorStopAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.SC;

public class MbedThree2ThreeOneTransformerVisitor extends MbedTransformerVisitor {

    private static final Map<String, String> NEW_NAMES = new HashMap<>();
    static {
        NEW_NAMES.put(SC.ACCELEROMETER, "_A");
        NEW_NAMES.put(SC.BUZZER, "_B");
        NEW_NAMES.put(SC.COMPASS, "_C");
        NEW_NAMES.put(SC.GYRO, "_G");
        NEW_NAMES.put(SC.LIGHT, "_L");
        NEW_NAMES.put(SC.RGBLED, "_R");
        NEW_NAMES.put(SC.SOUND, "_S");
        NEW_NAMES.put(SC.TEMPERATURE, "_T");
    }

    private final BlocklyDropdownFactory blocklyDropdownFactory;
    private final ConfigurationAst configuration;

    public MbedThree2ThreeOneTransformerVisitor(
        NewUsedHardwareBean.Builder builder,
        BlocklyDropdownFactory blocklyDropdownFactory,
        ConfigurationAst configuration) {
        this.blocklyDropdownFactory = blocklyDropdownFactory;
        this.configuration = configuration;

        // Rewrite previous configuration with updated names
        for ( ConfigurationComponent cc : configuration.getConfigurationComponentsValues() ) {
            if ( NEW_NAMES.containsKey(cc.componentType) ) {
                builder
                    .addUsedConfigurationComponent(
                        new ConfigurationComponent(
                            cc.componentType,
                            cc.isActor(),
                            NEW_NAMES.get(cc.componentType),
                            NEW_NAMES.get(cc.componentType),
                            cc.getComponentProperties(),
                            cc.getProperty(),
                            cc.x,
                            cc.y));
            } else {
                builder.addUsedConfigurationComponent(cc);
            }
        }
    }

    @Override
    public BlocklyDropdownFactory getBlocklyDropdownFactory() {
        return this.blocklyDropdownFactory;
    }

    @Override
    public Phrase visitLedOnAction(LedOnAction ledOnAction) {
        String newName = getNewName(ledOnAction.getUserDefinedPort());

        return new LedOnAction(ledOnAction.getProperty(), (Expr) ledOnAction.ledColor.modify(this), newName, ledOnAction.hide);
    }

    @Override
    public Phrase visitSingleMotorOnAction(SingleMotorOnAction singleMotorOnAction) {
        return null;
    }

    @Override
    public Phrase visitSingleMotorStopAction(SingleMotorStopAction singleMotorStopAction) {
        return null;
    }

    @Override
    public Phrase visitLightAction(LightAction lightAction) {
        String newName = getNewName(lightAction.port);

        return new LightAction(newName, lightAction.color, lightAction.mode, (Expr) lightAction.rgbLedColor.modify(this), lightAction.getProperty());
    }

    @Override
    public Phrase visitLightStatusAction(LightStatusAction lightStatusAction) {
        String newName = getNewName(lightStatusAction.getUserDefinedPort());

        return new LightStatusAction(newName, lightStatusAction.status, lightStatusAction.getProperty());
    }

    @Override
    public Phrase visitPlayNoteAction(PlayNoteAction playNoteAction) {
        String newName = getNewName(playNoteAction.port);

        return new PlayNoteAction(playNoteAction.getProperty(), playNoteAction.duration, playNoteAction.frequency, newName, playNoteAction.hide);
    }

    @Override
    public Phrase visitToneAction(ToneAction toneAction) {
        String newName = getNewName(toneAction.port);
        return new ToneAction(toneAction.getProperty(), (Expr) toneAction.frequency.modify(this), (Expr) toneAction.duration.modify(this), newName, toneAction.hide);
    }

    @Override
    public Phrase visitCompassSensor(CompassSensor compassSensor) {
        return new CompassSensor(compassSensor.getProperty(), getNewBean(compassSensor));
    }

    @Override
    public Phrase visitMbedPinWriteValueAction(MbedPinWriteValueAction mbedPinWriteValueAction) {
        return null;
    }

    @Override
    public Phrase visitTemperatureSensor(TemperatureSensor temperatureSensor) {
        return new TemperatureSensor(temperatureSensor.getProperty(), getNewBean(temperatureSensor));
    }

    @Override
    public Phrase visitSoundSensor(SoundSensor soundSensor) {
        return new SoundSensor(soundSensor.getProperty(), getNewBean(soundSensor));
    }

    @Override
    public Phrase visitLightSensor(LightSensor lightSensor) {
        return new LightSensor(lightSensor.getProperty(), getNewBean(lightSensor));
    }

    @Override
    public Phrase visitAccelerometerSensor(AccelerometerSensor accelerometerSensor) {
        return new AccelerometerSensor(accelerometerSensor.getProperty(), getNewBean(accelerometerSensor));
    }

    @Override
    public Phrase visitGyroSensor(GyroSensor gyroSensor) {
        return new GyroSensor(gyroSensor.getProperty(), getNewBean(gyroSensor));
    }

    @Override
    public Phrase visitGetSampleSensor(GetSampleSensor sensorGetSample) {
        ExternalSensor sensor;
        if ( sensorGetSample.sensor instanceof ExternalSensor ) {
            sensor = (ExternalSensor) sensorGetSample.sensor;
        } else {
            throw new DbcException("Could not get sensor info, because " + sensorGetSample.sensor.getKind() + " is not of type ExternalSensor!");
        }
        return new GetSampleSensor(sensorGetSample.sensorTypeAndMode, getNewBean(sensor).getPort(), sensorGetSample.slot, sensorGetSample.mutation, sensorGetSample.hide, sensorGetSample.getProperty(), getBlocklyDropdownFactory());
    }

    private String getNewName(String port) {
        ConfigurationComponent confComp = this.configuration.optConfigurationComponent(port);
        if ( confComp != null ) {
            String newName = NEW_NAMES.get(confComp.componentType);
            if ( newName != null ) {
                return newName;
            } else {
                return port;
            }
        } else {
            return port;
        }
    }

    private ExternalSensorBean getNewBean(ExternalSensor sensor) {
        String newName = getNewName(sensor.getUserDefinedPort());
        return new ExternalSensorBean(newName, sensor.getMode(), sensor.getSlot(), sensor.getMutation());
    }
}
