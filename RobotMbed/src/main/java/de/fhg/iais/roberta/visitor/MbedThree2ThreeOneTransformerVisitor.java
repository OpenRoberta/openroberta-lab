package de.fhg.iais.roberta.visitor;

import java.util.HashMap;
import java.util.Map;

import de.fhg.iais.roberta.bean.NewUsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.mbed.LedOnAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.SensorMetaDataBean;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;

public class MbedThree2ThreeOneTransformerVisitor implements IMbedTransformerVisitor<Void> {

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
            if ( NEW_NAMES.containsKey(cc.getComponentType()) ) {
                builder.addUsedConfigurationComponent(new ConfigurationComponent(
                    cc.getComponentType(),
                    cc.isActor(),
                    NEW_NAMES.get(cc.getComponentType()),
                    NEW_NAMES.get(cc.getComponentType()),
                    cc.getComponentProperties(),
                    cc.getProperty(),
                    cc.getComment(),
                    cc.getX(),
                    cc.getY()));
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
        String newName = getNewName(ledOnAction.getPort());

        return LedOnAction.make(newName, (Expr<Void>) ledOnAction.getLedColor().modify(this), ledOnAction.getProperty(), ledOnAction.getComment());
    }

    @Override
    public Phrase<Void> visitLightAction(LightAction<Phrase<Void>> lightAction) {
        String newName = getNewName(lightAction.getPort());

        return LightAction
            .make(
                newName,
                lightAction.getColor(),
                lightAction.getMode(),
                (Expr<Void>) lightAction.getRgbLedColor().modify(this),
                lightAction.getProperty(),
                lightAction.getComment());
    }

    @Override
    public Phrase<Void> visitLightStatusAction(LightStatusAction<Phrase<Void>> lightStatusAction) {
        String newName = getNewName(lightStatusAction.getPort());

        return LightStatusAction.make(newName, lightStatusAction.getStatus(), lightStatusAction.getProperty(), lightStatusAction.getComment());
    }

    @Override
    public Phrase<Void> visitPlayNoteAction(PlayNoteAction<Phrase<Void>> playNoteAction) {
        String newName = getNewName(playNoteAction.getPort());

        return PlayNoteAction
            .make(newName, playNoteAction.getDuration(), playNoteAction.getFrequency(), playNoteAction.getProperty(), playNoteAction.getComment());
    }

    @Override
    public Phrase<Void> visitToneAction(ToneAction<Phrase<Void>> toneAction) {
        String newName = getNewName(toneAction.getPort());

        return ToneAction
            .make(
                (Expr<Void>) toneAction.getFrequency().modify(this),
                (Expr<Void>) toneAction.getDuration().modify(this),
                newName,
                toneAction.getProperty(),
                toneAction.getComment());
    }

    @Override
    public Phrase<Void> visitCompassSensor(CompassSensor<Phrase<Void>> compassSensor) {
        return CompassSensor.make(getNewBean(compassSensor), compassSensor.getProperty(), compassSensor.getComment());
    }

    @Override
    public Phrase<Void> visitTemperatureSensor(TemperatureSensor<Phrase<Void>> temperatureSensor) {
        return TemperatureSensor.make(getNewBean(temperatureSensor), temperatureSensor.getProperty(), temperatureSensor.getComment());
    }

    @Override
    public Phrase<Void> visitSoundSensor(SoundSensor<Phrase<Void>> soundSensor) {
        return SoundSensor.make(getNewBean(soundSensor), soundSensor.getProperty(), soundSensor.getComment());
    }

    @Override
    public Phrase<Void> visitLightSensor(LightSensor<Phrase<Void>> lightSensor) {
        return LightSensor.make(getNewBean(lightSensor), lightSensor.getProperty(), lightSensor.getComment());
    }

    @Override
    public Phrase<Void> visitAccelerometer(AccelerometerSensor<Phrase<Void>> accelerometerSensor) {
        return AccelerometerSensor.make(getNewBean(accelerometerSensor), accelerometerSensor.getProperty(), accelerometerSensor.getComment());
    }

    @Override
    public Phrase<Void> visitGyroSensor(GyroSensor<Phrase<Void>> gyroSensor) {
        return GyroSensor.make(getNewBean(gyroSensor), gyroSensor.getProperty(), gyroSensor.getComment());
    }

    @Override
    public Phrase<Void> visitGetSampleSensor(GetSampleSensor<Phrase<Void>> sensorGetSample) {
        ExternalSensor<Phrase<Void>> sensor;
        if ( sensorGetSample.getSensor() instanceof ExternalSensor ) {
            sensor = (ExternalSensor<Phrase<Void>>) sensorGetSample.getSensor();
        } else {
            throw new DbcException("Could not get sensor info, because " + sensorGetSample.getSensor().getKind() + " is not of type ExternalSensor!");
        }
        return GetSampleSensor
            .make(
                sensorGetSample.getSensorTypeAndMode(),
                getNewBean(sensor).getPort(),
                sensorGetSample.getSlot(),
                sensorGetSample.isPortInMutation(),
                sensorGetSample.getProperty(),
                sensorGetSample.getComment(),
                getBlocklyDropdownFactory());
    }

    private String getNewName(String port) {
        ConfigurationComponent confComp = this.configuration.optConfigurationComponent(port);
        if (confComp != null) {
            String newName = NEW_NAMES.get(confComp.getComponentType());
            if (newName != null) {
                return newName;
            } else {
                return port;
            }
        } else {
            return port;
        }
    }

    private SensorMetaDataBean getNewBean(ExternalSensor<?> sensor) {
        String newName = getNewName(sensor.getPort());
        return new SensorMetaDataBean(newName, sensor.getMode(), sensor.getSlot(), sensor.isPortInMutation());
    }
}
