package de.fhg.iais.roberta.visitor;

import java.util.HashMap;
import java.util.Map;

import de.fhg.iais.roberta.bean.NewUsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.mbed.LedOnAction;
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

public class MbedThree2ThreeOneTransformerVisitor extends BaseVisitor<Phrase<Void>> implements IMbedTransformerVisitor<Void> {

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
    public Phrase<Void> visitLedOnAction(LedOnAction<Phrase<Void>> ledOnAction) {
        String newName = getNewName(ledOnAction.getUserDefinedPort());

        return new LedOnAction<>(ledOnAction.getProperty(), (Expr<Void>) ledOnAction.ledColor.modify(this), newName, ledOnAction.hide);
    }

    @Override
    public Phrase<Void> visitLightAction(LightAction<Phrase<Void>> lightAction) {
        String newName = getNewName(lightAction.port);

        return new LightAction<>(newName, lightAction.color, lightAction.mode, (Expr<Void>) lightAction.rgbLedColor.modify(this), lightAction.getProperty());
    }

    @Override
    public Phrase<Void> visitLightStatusAction(LightStatusAction<Phrase<Void>> lightStatusAction) {
        String newName = getNewName(lightStatusAction.getUserDefinedPort());

        return new LightStatusAction<>(newName, lightStatusAction.status, lightStatusAction.getProperty());
    }

    @Override
    public Phrase<Void> visitPlayNoteAction(PlayNoteAction<Phrase<Void>> playNoteAction) {
        String newName = getNewName(playNoteAction.port);

        return new PlayNoteAction<>(playNoteAction.getProperty(), playNoteAction.duration, playNoteAction.frequency, newName, playNoteAction.hide);
    }

    @Override
    public Phrase<Void> visitToneAction(ToneAction<Phrase<Void>> toneAction) {
        String newName = getNewName(toneAction.port);
        return new ToneAction<>(toneAction.getProperty(), (Expr<Void>) toneAction.frequency.modify(this), (Expr<Void>) toneAction.duration.modify(this), newName, toneAction.hide);
    }

    @Override
    public Phrase<Void> visitCompassSensor(CompassSensor<Phrase<Void>> compassSensor) {
        return new CompassSensor<>(compassSensor.getProperty(), getNewBean(compassSensor));
    }

    @Override
    public Phrase<Void> visitTemperatureSensor(TemperatureSensor<Phrase<Void>> temperatureSensor) {
        return new TemperatureSensor<Void>(temperatureSensor.getProperty(), getNewBean(temperatureSensor));
    }

    @Override
    public Phrase<Void> visitSoundSensor(SoundSensor<Phrase<Void>> soundSensor) {
        return new SoundSensor<Void>(soundSensor.getProperty(), getNewBean(soundSensor));
    }

    @Override
    public Phrase<Void> visitLightSensor(LightSensor<Phrase<Void>> lightSensor) {
        return new LightSensor<Void>(lightSensor.getProperty(), getNewBean(lightSensor));
    }

    @Override
    public Phrase<Void> visitAccelerometerSensor(AccelerometerSensor<Phrase<Void>> accelerometerSensor) {
        return new AccelerometerSensor<Void>(accelerometerSensor.getProperty(), getNewBean(accelerometerSensor));
    }

    @Override
    public Phrase<Void> visitGyroSensor(GyroSensor<Phrase<Void>> gyroSensor) {
        return new GyroSensor<>(gyroSensor.getProperty(), getNewBean(gyroSensor));
    }

    @Override
    public Phrase<Void> visitGetSampleSensor(GetSampleSensor<Phrase<Void>> sensorGetSample) {
        ExternalSensor<Phrase<Void>> sensor;
        if ( sensorGetSample.sensor instanceof ExternalSensor ) {
            sensor = (ExternalSensor<Phrase<Void>>) sensorGetSample.sensor;
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

    private ExternalSensorBean getNewBean(ExternalSensor<?> sensor) {
        String newName = getNewName(sensor.getUserDefinedPort());
        return new ExternalSensorBean(newName, sensor.getMode(), sensor.getSlot(), sensor.getMutation());
    }
}
