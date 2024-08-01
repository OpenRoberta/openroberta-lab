package de.fhg.iais.roberta.visitor.validate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.display.ClearDisplayAction;
import de.fhg.iais.roberta.syntax.action.generic.MbedPinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayGetPixelAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayImageAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplaySetPixelAction;
import de.fhg.iais.roberta.syntax.action.mbed.DisplayTextAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioReceiveAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSendAction;
import de.fhg.iais.roberta.syntax.action.mbed.RadioSetChannelAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.expr.mbed.Image;
import de.fhg.iais.roberta.syntax.expr.mbed.PredefinedImage;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageInvertFunction;
import de.fhg.iais.roberta.syntax.functions.mbed.ImageShiftFunction;
import de.fhg.iais.roberta.syntax.lang.expr.NumConst;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GestureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinTouchSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerReset;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.CalliopeMethods;
import de.fhg.iais.roberta.visitor.IMbedVisitor;
import de.fhg.iais.roberta.visitor.MicrobitMethods;

public abstract class MbedValidatorAndCollectorVisitor extends CommonNepoValidatorAndCollectorVisitor implements IMbedVisitor<Void> {

    public static final double DOUBLE_EPS = 1E-7;

    protected List<String> occupiedPins = Arrays.asList("3", "4", "5", "6", "7", "9", "10", "11", "12", "19", "20");
    protected List<String> ledPins = Arrays.asList("3", "4", "6", "7", "9", "10");
    final boolean displaySwitchUsed;

    public MbedValidatorAndCollectorVisitor(
        ConfigurationAst brickConfiguration, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders,
        boolean displaySwitchUsed) {
        super(brickConfiguration, beanBuilders);
        this.displaySwitchUsed = displaySwitchUsed;
    }

    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor accelerometerSensor) {
        checkSensorExists(accelerometerSensor, SC.ACCELEROMETER);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(accelerometerSensor.getUserDefinedPort(), SC.ACCELEROMETER, accelerometerSensor.getMode()));
        return null;
    }

    @Override
    public Void visitClearDisplayAction(ClearDisplayAction clearDisplayAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY));
        return null;
    }

    @Override
    public Void visitCompassSensor(CompassSensor compassSensor) {
        checkSensorExists(compassSensor, SC.COMPASS);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(compassSensor.getUserDefinedPort(), SC.COMPASS, compassSensor.getMode()));
        return null;
    }

    @Override
    public Void visitDisplayGetPixelAction(DisplayGetPixelAction displayGetPixelAction) {
        requiredComponentVisited(displayGetPixelAction, displayGetPixelAction.x, displayGetPixelAction.y);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY_GRAYSCALE));
        return null;
    }

    @Override
    public Void visitDisplayImageAction(DisplayImageAction displayImageAction) {
        requiredComponentVisited(displayImageAction, displayImageAction.valuesToDisplay);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY));
        return null;
    }


    @Override
    public Void visitDisplaySetPixelAction(DisplaySetPixelAction displaySetPixelAction) {
        requiredComponentVisited(displaySetPixelAction, displaySetPixelAction.brightness, displaySetPixelAction.x, displaySetPixelAction.y);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY_GRAYSCALE));
        return null;
    }

    @Override
    public Void visitDisplayTextAction(DisplayTextAction displayTextAction) {
        requiredComponentVisited(displayTextAction, displayTextAction.msg);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY));
        return null;
    }

    @Override
    public Void visitGestureSensor(GestureSensor gestureSensor) {
        if ( gestureSensor.getMode().equals("SHAKE") ) {
            usedMethodBuilder.addUsedMethod(CalliopeMethods.IS_GESTURE_SHAKE);
        }
        usedHardwareBuilder.addUsedSensor(new UsedSensor(gestureSensor.getUserDefinedPort(), SC.ACCELEROMETER, gestureSensor.getMode()));
        return null;
    }


    @Override
    public Void visitImage(Image image) {
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY_GRAYSCALE));
        return null;
    }

    @Override
    public Void visitImageInvertFunction(ImageInvertFunction imageInvertFunction) {
        requiredComponentVisited(imageInvertFunction, imageInvertFunction.image);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY));
        return null;
    }

    @Override
    public Void visitImageShiftFunction(ImageShiftFunction imageShiftFunction) {
        requiredComponentVisited(imageShiftFunction, imageShiftFunction.image, imageShiftFunction.positions);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.DISPLAY));
        return null;
    }


    @Override
    public Void visitKeysSensor(KeysSensor keysSensor) {
        checkSensorExists(keysSensor, SC.KEY);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.KEY));
        return null;
    }


    @Override
    public Void visitLightSensor(LightSensor lightSensor) {
        checkSensorExists(lightSensor, SC.LIGHT);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(lightSensor.getUserDefinedPort(), SC.LIGHT, lightSensor.getMode()));
        return null;
    }


    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor pinValueSensor) {
        checkInternalPorts(pinValueSensor, pinValueSensor.getUserDefinedPort());
        String sensorType = pinValueSensor.getMode().equals("ANALOG") ? "ANALOG_PIN" : "DIGITAL_PIN";
        checkSensorExists(pinValueSensor, sensorType);
        usedHardwareBuilder.addUsedActor(new UsedActor(pinValueSensor.getUserDefinedPort(), SC.PIN_VALUE));
        return null;
    }


    @Override
    public Void visitPinTouchSensor(PinTouchSensor pinTouchSensor) {
        usedHardwareBuilder.addUsedActor(new UsedActor(pinTouchSensor.getUserDefinedPort(), SC.PIN_VALUE));
        return null;
    }

    @Override
    public Void visitMbedPinWriteValueAction(MbedPinWriteValueAction mbedPinWriteValueAction) {
        checkInternalPorts(mbedPinWriteValueAction, mbedPinWriteValueAction.port);
        ConfigurationComponent configurationComponent = checkActorByPortExists(mbedPinWriteValueAction, mbedPinWriteValueAction.port);
        requiredComponentVisited(mbedPinWriteValueAction, mbedPinWriteValueAction.value);
        if ( configurationComponent != null ) {
            usedHardwareBuilder.addUsedActor(new UsedActor(configurationComponent.userDefinedPortName, configurationComponent.componentType));
        }
        return null;
    }

    private void checkInternalPorts(Phrase pinValueSensor, String port) {
        ConfigurationComponent configurationComponent = this.robotConfiguration.optConfigurationComponent(port);
        if ( configurationComponent != null ) {
            String pin = configurationComponent.getProperty("PIN1");
            if ( this.ledPins.contains(pin) ) {
                if ( !this.displaySwitchUsed ) {
                    addWarningToPhrase(pinValueSensor, "VALIDATION_PIN_TAKEN_BY_LED_MATRIX");
                }
            } else if ( this.occupiedPins.contains(pin) ) {
                addWarningToPhrase(pinValueSensor, "VALIDATION_PIN_TAKEN_BY_INTERNAL_COMPONENT");
            } else if ( configurationComponent.componentType.equals(SC.KEY) ) {
                pinValueSensor.addTextlyError("The defined actuator port: " + port + " is incorrect, please check the robot configuration", true);
            }
        }
    }


    @Override
    public Void visitPlayNoteAction(PlayNoteAction playNoteAction) {
        checkActorByTypeExists(playNoteAction, "BUZZER");
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.MUSIC));
        return null;
    }

    @Override
    public Void visitPredefinedImage(PredefinedImage predefinedImage) {
        return null;
    }

    @Override
    public Void visitRadioReceiveAction(RadioReceiveAction radioReceiveAction) {
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.RADIO));
        usedMethodBuilder.addUsedMethod(MicrobitMethods.RECEIVE_MESSAGE);
        return null;
    }

    @Override
    public Void visitRadioSendAction(RadioSendAction radioSendAction) {
        requiredComponentVisited(radioSendAction, radioSendAction.message);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.RADIO));
        return null;
    }

    @Override
    public Void visitRadioSetChannelAction(RadioSetChannelAction radioSetChannelAction) {
        requiredComponentVisited(radioSetChannelAction, radioSetChannelAction.channel);
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.RADIO));
        return null;
    }


    @Override
    public Void visitTemperatureSensor(TemperatureSensor temperatureSensor) {
        checkSensorExists(temperatureSensor, SC.TEMPERATURE);
        usedHardwareBuilder.addUsedSensor(new UsedSensor(temperatureSensor.getUserDefinedPort(), SC.TEMPERATURE, temperatureSensor.getMode()));
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor timerSensor) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(timerSensor.getUserDefinedPort(), SC.TIMER, timerSensor.getMode()));
        return null;
    }

    @Override
    public Void visitTimerReset(TimerReset timerReset) {
        usedHardwareBuilder.addUsedSensor(new UsedSensor(timerReset.sensorPort, SC.TIMER, SC.DEFAULT));
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction toneAction) {
        requiredComponentVisited(toneAction, toneAction.duration, toneAction.frequency);
        checkActorByTypeExists(toneAction, "BUZZER");
        usedHardwareBuilder.addUsedActor(new UsedActor("", SC.MUSIC));
        if ( toneAction.duration.getKind().hasName("NUM_CONST") ) {
            double toneActionConst = Double.parseDouble(((NumConst) toneAction.duration).value);
            if ( toneActionConst <= 0 ) {
                addWarningToPhrase(toneAction, "BLOCK_NOT_EXECUTED");
            }
        }
        return null;
    }


    protected ConfigurationComponent checkActorByPortExists(Phrase actor, String port) {
        ConfigurationComponent usedActor = robotConfiguration.optConfigurationComponent(port);
        if ( usedActor == null ) {
            addErrorToPhrase(actor, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        return usedActor;
    }

    protected ConfigurationComponent checkActorByTypeExists(Phrase actor, String type) {
        ConfigurationComponent usedActor = robotConfiguration.optConfigurationComponentByType(type);
        if ( usedActor == null ) {
            addErrorToPhrase(actor, "CONFIGURATION_ERROR_ACTOR_MISSING");
        }
        return usedActor;
    }

    protected ConfigurationComponent checkSensorExists(ExternalSensor sensor, String sensorType) {

        if ( !robotConfiguration.isComponentTypePresent(sensorType) ) {
            addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_MISSING");
        } else {
            Map<String, ConfigurationComponent> usedSensor = null;
            usedSensor = robotConfiguration.getAllConfigurationComponentByType(sensorType);

            boolean portCorrect = false;
            for ( String portName : usedSensor.keySet() ) {
                if ( portName.equals(sensor.getUserDefinedPort()) ) {
                    portCorrect = true;
                }
            }

            if ( !portCorrect ) {
                sensor.addTextlyError("The defined sensor port: " + sensor.getUserDefinedPort() + " is incorrect, please check the robot configuration", true);
            }
        }

        ConfigurationComponent usedSensor = robotConfiguration.optConfigurationComponent(sensor.getUserDefinedPort());
        ConfigurationComponent isCallibotUsed = robotConfiguration.optConfigurationComponentByType(SC.CALLIBOT);
        if ( usedSensor == null && isCallibotUsed == null ) {
            addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_MISSING");
        } else if ( isCallibotUsed != null ) {
            //            addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_MISSING");
        } else {
            String type = usedSensor.componentType;
            switch ( sensor.getKind().getName() ) {
                case "COLOR_SENSING":
                    if ( !type.equals("COLOUR") ) { // Mbed has COLOUR instead of COLOR
                        addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
                    }
                    break;
                case "ULTRASONIC_SENSING":
                    if ( !(type.equals("ULTRASONIC") || type.equals("CALLIBOT")) ) {
                        addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
                    }
                    break;
                case "INFRARED_SENSING":
                    if ( !(type.equals("INFRARED") || type.equals("CALLIBOT")) ) {
                        addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
                    }
                    break;
                case "GYRO_SENSING":
                    if ( !type.equals("GYRO") ) {
                        addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
                    }
                    break;
                case "SOUND_SENSING":
                    if ( !type.equals("SOUND") ) {
                        addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
                    }
                    break;
                case "LIGHT_SENSING":
                    if ( !type.equals("LIGHT") ) {
                        addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
                    }
                    break;
                case "COMPASS_SENSING":
                    if ( !type.equals("COMPASS") ) {
                        addErrorToPhrase(sensor, "CONFIGURATION_ERROR_SENSOR_WRONG");
                    }
                    break;
                default:
                    break;
            }
        }
        return usedSensor;
    }
}
