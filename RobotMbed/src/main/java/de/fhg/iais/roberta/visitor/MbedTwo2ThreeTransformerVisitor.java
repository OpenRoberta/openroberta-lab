package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.bean.NewUsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.MotionParam;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.generic.PinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.mbed.BothMotorsOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.BothMotorsStopAction;
import de.fhg.iais.roberta.syntax.action.mbed.FourDigitDisplayClearAction;
import de.fhg.iais.roberta.syntax.action.mbed.FourDigitDisplayShowAction;
import de.fhg.iais.roberta.syntax.action.mbed.LedBarSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.LedOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.PinSetPullAction;
import de.fhg.iais.roberta.syntax.action.mbed.ServoSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.SingleMotorOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.SingleMotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.SensorMetaDataBean;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.CompassSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.SoundSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.worker.MbedTwo2ThreeTransformerHelper;

/**
 * Used to replace port names of old Mbed programs to fit with the new configuration.
 * Also keeps track of configuration components used, in order to only use the actually used ones.
 */
public class MbedTwo2ThreeTransformerVisitor implements IMbedTransformerVisitor<Void> {

    private final MbedTwo2ThreeTransformerHelper helper;
    private final NewUsedHardwareBean.Builder builder;
    private final BlocklyDropdownFactory blocklyDropdownFactory;

    public MbedTwo2ThreeTransformerVisitor(
        MbedTwo2ThreeTransformerHelper helper,
        NewUsedHardwareBean.Builder builder,
        BlocklyDropdownFactory blocklyDropdownFactory) {
        this.helper = helper;
        this.builder = builder;
        this.blocklyDropdownFactory = blocklyDropdownFactory;
    }

    @Override
    public Phrase<Void> visitLedOnAction(LedOnAction<Phrase<Void>> ledOnAction) {
        ConfigurationComponent defaultComponent = this.helper.getComponent(ledOnAction.getKind().getName(), "", ledOnAction.getPort());

        this.builder.addUsedConfigurationComponent(defaultComponent);

        return LedOnAction
            .make(
                defaultComponent.getUserDefinedPortName(),
                (Expr<Void>) ledOnAction.getLedColor().modify(this),
                ledOnAction.getProperty(),
                ledOnAction.getComment());
    }

    @Override
    public Phrase<Void> visitPinSetPullAction(PinSetPullAction<Phrase<Void>> pinSetPullAction) {
        ConfigurationComponent defaultComponent =
            this.helper.getComponent(pinSetPullAction.getKind().getName(), pinSetPullAction.getMode(), pinSetPullAction.getPort());

        this.builder.addUsedConfigurationComponent(defaultComponent);

        return PinSetPullAction
            .make(pinSetPullAction.getMode(), defaultComponent.getUserDefinedPortName(), pinSetPullAction.getProperty(), pinSetPullAction.getComment());
    }

    @Override
    public BlocklyDropdownFactory getBlocklyDropdownFactory() {
        return this.blocklyDropdownFactory;
    }

    @Override
    public Phrase<Void> visitLightAction(LightAction<Phrase<Void>> lightAction) {
        ConfigurationComponent defaultComponent =
            this.helper.getComponent(lightAction.getKind().getName(), lightAction.getMode().toString(), lightAction.getPort());

        this.builder.addUsedConfigurationComponent(defaultComponent);

        return LightAction
            .make(
                defaultComponent.getUserDefinedPortName(),
                lightAction.getColor(),
                lightAction.getMode(),
                (Expr<Void>) lightAction.getRgbLedColor().modify(this),
                lightAction.getProperty(),
                lightAction.getComment());
    }

    @Override
    public Phrase<Void> visitLightStatusAction(LightStatusAction<Phrase<Void>> lightStatusAction) {
        ConfigurationComponent defaultComponent =
            this.helper.getComponent(lightStatusAction.getKind().getName(), lightStatusAction.getStatus().name(), lightStatusAction.getPort());

        this.builder.addUsedConfigurationComponent(defaultComponent);

        return LightStatusAction
            .make(defaultComponent.getUserDefinedPortName(), lightStatusAction.getStatus(), lightStatusAction.getProperty(), lightStatusAction.getComment());
    }

    @Override
    public Phrase<Void> visitPinWriteValueAction(PinWriteValueAction<Phrase<Void>> pinWriteValueAction) {
        ConfigurationComponent defaultComponent =
            this.helper.getComponent(pinWriteValueAction.getKind().getName(), pinWriteValueAction.getMode(), pinWriteValueAction.getPort());

        this.builder.addUsedConfigurationComponent(defaultComponent);

        return PinWriteValueAction
            .make(
                pinWriteValueAction.getMode(),
                defaultComponent.getUserDefinedPortName(),
                (Expr<Void>) pinWriteValueAction.getValue().modify(this),
                pinWriteValueAction.isActorPortAndMode(),
                pinWriteValueAction.getProperty(),
                pinWriteValueAction.getComment());
    }

    @Override
    public Phrase<Void> visitServoSetAction(ServoSetAction<Phrase<Void>> servoSetAction) {
        ConfigurationComponent defaultComponent = this.helper.getComponent(servoSetAction.getKind().getName(), "", servoSetAction.getPort());

        this.builder.addUsedConfigurationComponent(defaultComponent);

        return ServoSetAction
            .make(
                defaultComponent.getUserDefinedPortName(),
                (Expr<Void>) servoSetAction.getValue().modify(this),
                servoSetAction.getProperty(),
                servoSetAction.getComment());
    }

    @Override
    public Phrase<Void> visitLedBarSetAction(LedBarSetAction<Phrase<Void>> ledBarSetAction) {
        // did not have a port previously, 5 = A1
        ConfigurationComponent defaultComponent = this.helper.getComponent(ledBarSetAction.getKind().getName(), "", "5");

        this.builder.addUsedConfigurationComponent(defaultComponent);

        return LedBarSetAction
            .make(
                (Expr<Void>) ledBarSetAction.getX().modify(this),
                (Expr<Void>) ledBarSetAction.getBrightness().modify(this),
                ledBarSetAction.getProperty(),
                ledBarSetAction.getComment());
    }

    @Override
    public Phrase<Void> visitFourDigitDisplayShowAction(FourDigitDisplayShowAction<Phrase<Void>> fourDigitDisplayShowAction) {
        // did not have a port previously, 5 = A1
        ConfigurationComponent defaultComponent = this.helper.getComponent(fourDigitDisplayShowAction.getKind().getName(), "", "5");

        this.builder.addUsedConfigurationComponent(defaultComponent);

        return FourDigitDisplayShowAction
            .make(
                (Expr<Void>) fourDigitDisplayShowAction.getValue().modify(this),
                (Expr<Void>) fourDigitDisplayShowAction.getPosition().modify(this),
                (Expr<Void>) fourDigitDisplayShowAction.getColon().modify(this),
                fourDigitDisplayShowAction.getProperty(),
                fourDigitDisplayShowAction.getComment());
    }

    @Override
    public Phrase<Void> visitFourDigitDisplayClearAction(FourDigitDisplayClearAction<Phrase<Void>> fourDigitDisplayClearAction) {
        // did not have a port previously, 5 = A1
        ConfigurationComponent defaultComponent = this.helper.getComponent(fourDigitDisplayClearAction.getKind().getName(), "", "5");

        this.builder.addUsedConfigurationComponent(defaultComponent);

        return FourDigitDisplayClearAction.make(fourDigitDisplayClearAction.getProperty(), fourDigitDisplayClearAction.getComment());
    }

    @Override
    public Phrase<Void> visitPlayNoteAction(PlayNoteAction<Phrase<Void>> playNoteAction) {
        ConfigurationComponent defaultComponent = this.helper.getComponent(playNoteAction.getKind().getName(), "", playNoteAction.getPort());

        this.builder.addUsedConfigurationComponent(defaultComponent);

        return PlayNoteAction
            .make(
                defaultComponent.getUserDefinedPortName(),
                playNoteAction.getDuration(),
                playNoteAction.getFrequency(),
                playNoteAction.getProperty(),
                playNoteAction.getComment());
    }

    @Override
    public Phrase<Void> visitToneAction(ToneAction<Phrase<Void>> toneAction) {
        ConfigurationComponent defaultComponent = this.helper.getComponent(toneAction.getKind().getName(), "", toneAction.getPort());

        this.builder.addUsedConfigurationComponent(defaultComponent);

        return ToneAction
            .make(
                (Expr<Void>) toneAction.getFrequency().modify(this),
                (Expr<Void>) toneAction.getDuration().modify(this),
                defaultComponent.getUserDefinedPortName(),
                toneAction.getProperty(),
                toneAction.getComment());
    }

    @Override
    public Phrase<Void> visitSingleMotorOnAction(SingleMotorOnAction<Phrase<Void>> singleMotorOnAction) {
        // replace this block with motor on action
        ConfigurationComponent defaultComponent = this.helper.getComponent("MOTOR_ON_ACTION", "", "A");

        this.builder.addUsedConfigurationComponent(defaultComponent);

        MotionParam.Builder<Void> motionParamBuilder = new MotionParam.Builder<>();
        motionParamBuilder.speed((Expr<Void>) singleMotorOnAction.getSpeed().modify(this));

        return MotorOnAction
            .make(
                defaultComponent.getUserDefinedPortName(),
                motionParamBuilder.build(),
                modifyPropertyType(singleMotorOnAction.getProperty(), "mbedActions_motor_on"),
                singleMotorOnAction.getComment());
    }

    @Override
    public Phrase<Void> visitSingleMotorStopAction(SingleMotorStopAction<Phrase<Void>> singleMotorStopAction) {
        // replace this block with motor stop action
        ConfigurationComponent defaultComponent = this.helper.getComponent("MOTOR_STOP_ACTION", "", "A");

        this.builder.addUsedConfigurationComponent(defaultComponent);

        return MotorStopAction
            .make(
                defaultComponent.getUserDefinedPortName(),
                singleMotorStopAction.getMode(),
                modifyPropertyType(singleMotorStopAction.getProperty(), "mbedActions_motor_stop"),
                singleMotorStopAction.getComment());
    }

    @Override
    public Phrase<Void> visitMotorOnAction(MotorOnAction<Phrase<Void>> motorOnAction) {
        // The ports A+B and Calli:bot both are replaced by a BothMotorsOnAction with two same values
        String port = motorOnAction.getUserDefinedPort();
        if ( port.equals("AB") || port.equals("3") ) {
            ConfigurationComponent defaultComponentA;
            ConfigurationComponent defaultComponentB;
            if ( port.equals("AB") ) {
                defaultComponentA = this.helper.getComponent(motorOnAction.getKind().getName(), "", "A");
                defaultComponentB = this.helper.getComponent(motorOnAction.getKind().getName(), "", "B");
            } else { // Calli:bot both was represented by 3 in the XML
                defaultComponentA = this.helper.getComponent(motorOnAction.getKind().getName(), "", "0"); // 0 = left
                defaultComponentB = this.helper.getComponent(motorOnAction.getKind().getName(), "", "2"); // 2 = right
            }
            this.builder.addUsedConfigurationComponent(defaultComponentA);
            this.builder.addUsedConfigurationComponent(defaultComponentB);

            Expr<Void> speed = (Expr<Void>) motorOnAction.getParam().getSpeed().modify(this);

            return BothMotorsOnAction
                .make(
                    defaultComponentA.getUserDefinedPortName(),
                    defaultComponentB.getUserDefinedPortName(),
                    speed,
                    speed,
                    modifyPropertyType(motorOnAction.getProperty(), "mbedActions_motors_on"),
                    motorOnAction.getComment());
        } else { // only replace the port for the others
            ConfigurationComponent defaultComponent = this.helper.getComponent(motorOnAction.getKind().getName(), "", port);

            this.builder.addUsedConfigurationComponent(defaultComponent);

            return MotorOnAction
                .make(
                    defaultComponent.getUserDefinedPortName(),
                    modifyMotionParam(motorOnAction.getParam()),
                    motorOnAction.getProperty(),
                    motorOnAction.getComment());
        }
    }

    @Override
    public Phrase<Void> visitMotorStopAction(MotorStopAction<Phrase<Void>> motorStopAction) {
        String port = motorStopAction.getUserDefinedPort();
        if ( port.equals("AB") || port.equals("3") ) {
            ConfigurationComponent defaultComponentA;
            ConfigurationComponent defaultComponentB;
            if ( port.equals("AB") ) {
                defaultComponentA = this.helper.getComponent(motorStopAction.getKind().getName(), "", "A");
                defaultComponentB = this.helper.getComponent(motorStopAction.getKind().getName(), "", "B");
            } else { // Calli:bot both was represented by 3 in the XML
                defaultComponentA = this.helper.getComponent(motorStopAction.getKind().getName(), "", "0");
                defaultComponentB = this.helper.getComponent(motorStopAction.getKind().getName(), "", "2");
            }

            this.builder.addUsedConfigurationComponent(defaultComponentA);
            this.builder.addUsedConfigurationComponent(defaultComponentB);

            return BothMotorsStopAction.make(modifyPropertyType(motorStopAction.getProperty(), "mbedActions_motors_stop"), motorStopAction.getComment());
        } else {
            ConfigurationComponent defaultComponent = this.helper.getComponent(motorStopAction.getKind().getName(), "", motorStopAction.getUserDefinedPort());

            this.builder.addUsedConfigurationComponent(defaultComponent);

            return MotorStopAction
                .make(defaultComponent.getUserDefinedPortName(), motorStopAction.getMode(), motorStopAction.getProperty(), motorStopAction.getComment());
        }
    }

    @Override
    public Phrase<Void> visitBothMotorsOnAction(BothMotorsOnAction<Phrase<Void>> bothMotorsOnAction) {
        String portA = bothMotorsOnAction.getPortA();
        String portB = bothMotorsOnAction.getPortB();

        // Calli:bot mapping for this block was LEFT, RIGHT, for the other motor block it is 0 for left and 2 for right, this rectifies the ports
        if ( portA.equals("LEFT") ) {
            portA = "0";
            portB = "2";
        }

        ConfigurationComponent defaultComponentA = this.helper.getComponent("MOTOR_ON_ACTION", "", portA);
        ConfigurationComponent defaultComponentB = this.helper.getComponent("MOTOR_ON_ACTION", "", portB);

        this.builder.addUsedConfigurationComponent(defaultComponentA);
        this.builder.addUsedConfigurationComponent(defaultComponentB);

        return BothMotorsOnAction
            .make(
                defaultComponentA.getUserDefinedPortName(),
                defaultComponentB.getUserDefinedPortName(),
                (Expr<Void>) bothMotorsOnAction.getSpeedA().modify(this),
                (Expr<Void>) bothMotorsOnAction.getSpeedB().modify(this),
                bothMotorsOnAction.getProperty(),
                bothMotorsOnAction.getComment());
    }

    @Override
    public Phrase<Void> visitBothMotorsStopAction(BothMotorsStopAction<Phrase<Void>> bothMotorsStopAction) {
        ConfigurationComponent defaultComponentA = this.helper.getComponent("MOTOR_STOP_ACTION", "", "A");
        ConfigurationComponent defaultComponentB = this.helper.getComponent("MOTOR_STOP_ACTION", "", "B");

        this.builder.addUsedConfigurationComponent(defaultComponentA);
        this.builder.addUsedConfigurationComponent(defaultComponentB);

        return BothMotorsStopAction.make(bothMotorsStopAction.getProperty(), bothMotorsStopAction.getComment());
    }

    @Override
    public Phrase<Void> visitKeysSensor(KeysSensor<Phrase<Void>> keysSensor) {
        return KeysSensor.make(collectSensorAndGetNewBean(keysSensor), keysSensor.getProperty(), keysSensor.getComment());
    }

    @Override
    public Phrase<Void> visitUltrasonicSensor(UltrasonicSensor<Phrase<Void>> ultrasonicSensor) {
        return UltrasonicSensor.make(collectSensorAndGetNewBean(ultrasonicSensor), ultrasonicSensor.getProperty(), ultrasonicSensor.getComment());
    }

    @Override
    public Phrase<Void> visitCompassSensor(CompassSensor<Phrase<Void>> compassSensor) {
        return CompassSensor.make(collectSensorAndGetNewBean(compassSensor), compassSensor.getProperty(), compassSensor.getComment());
    }

    @Override
    public Phrase<Void> visitTemperatureSensor(TemperatureSensor<Phrase<Void>> temperatureSensor) {
        return TemperatureSensor.make(collectSensorAndGetNewBean(temperatureSensor), temperatureSensor.getProperty(), temperatureSensor.getComment());
    }

    @Override
    public Phrase<Void> visitSoundSensor(SoundSensor<Phrase<Void>> soundSensor) {
        return SoundSensor.make(collectSensorAndGetNewBean(soundSensor), soundSensor.getProperty(), soundSensor.getComment());
    }

    @Override
    public Phrase<Void> visitLightSensor(LightSensor<Phrase<Void>> lightSensor) {
        return LightSensor.make(collectSensorAndGetNewBean(lightSensor), lightSensor.getProperty(), lightSensor.getComment());
    }

    @Override
    public Phrase<Void> visitHumiditySensor(HumiditySensor<Phrase<Void>> humiditySensor) {
        return HumiditySensor.make(collectSensorAndGetNewBean(humiditySensor), humiditySensor.getProperty(), humiditySensor.getComment());
    }

    @Override
    public Phrase<Void> visitAccelerometer(AccelerometerSensor<Phrase<Void>> accelerometerSensor) {
        ConfigurationComponent defaultComponent =
            this.helper.getComponent(accelerometerSensor.getKind().getName(), accelerometerSensor.getMode(), accelerometerSensor.getPort());

        this.builder.addUsedConfigurationComponent(defaultComponent);
        // Previously X, Y, Z, STRENGTH were saved in the port, now the should be in the slot
        SensorMetaDataBean bean =
            new SensorMetaDataBean(
                defaultComponent.getUserDefinedPortName(),
                accelerometerSensor.getMode(),
                accelerometerSensor.getPort(),
                accelerometerSensor.isPortInMutation());

        return AccelerometerSensor.make(bean, accelerometerSensor.getProperty(), accelerometerSensor.getComment());
    }

    @Override
    public Phrase<Void> visitGyroSensor(GyroSensor<Phrase<Void>> gyroSensor) {
        ConfigurationComponent defaultComponent = this.helper.getComponent(gyroSensor.getKind().getName(), gyroSensor.getMode(), gyroSensor.getPort());

        this.builder.addUsedConfigurationComponent(defaultComponent);
        // Previously X, Y were saved in the port, now the should be in the slot
        SensorMetaDataBean bean =
            new SensorMetaDataBean(defaultComponent.getUserDefinedPortName(), gyroSensor.getMode(), gyroSensor.getPort(), gyroSensor.isPortInMutation());

        return GyroSensor.make(bean, gyroSensor.getProperty(), gyroSensor.getComment());
    }

    @Override
    public Phrase<Void> visitInfraredSensor(InfraredSensor<Phrase<Void>> infraredSensor) {
        return InfraredSensor.make(collectSensorAndGetNewBean(infraredSensor), infraredSensor.getProperty(), infraredSensor.getComment());
    }

    @Override
    public Phrase<Void> visitPinGetValueSensor(PinGetValueSensor<Phrase<Void>> pinGetValueSensor) {
        return PinGetValueSensor.make(collectSensorAndGetNewBean(pinGetValueSensor), pinGetValueSensor.getProperty(), pinGetValueSensor.getComment());
    }

    @Override
    public Phrase<Void> visitGetSampleSensor(GetSampleSensor<Phrase<Void>> sensorGetSample) {
        ExternalSensor<Phrase<Void>> sensor;
        if ( sensorGetSample.getSensor() instanceof ExternalSensor ) {
            sensor = (ExternalSensor<Phrase<Void>>) sensorGetSample.getSensor();
        } else {
            throw new DbcException("Could not get sensor info, because " + sensorGetSample.getSensor().getKind() + " is not of type ExternalSensor!");
        }

        // gyro and accelerometer are handled differently, their ports are written into the slot instead
        if ( sensor.getKind().getName().equals("ACCELEROMETER_SENSING") || sensor.getKind().getName().equals("GYRO_SENSING") ) {
            ConfigurationComponent defaultComponent = this.helper.getComponent(sensor.getKind().getName(), sensor.getMode(), sensor.getPort());

            this.builder.addUsedConfigurationComponent(defaultComponent);

            return GetSampleSensor
                .make(
                    sensorGetSample.getSensorTypeAndMode(),
                    defaultComponent.getUserDefinedPortName(),
                    sensorGetSample.getSensorPort(),
                    sensorGetSample.isPortInMutation(),
                    sensorGetSample.getProperty(),
                    sensorGetSample.getComment(),
                    getBlocklyDropdownFactory());
        } else if ( sensor.getKind().getName().equals("TIMER_SENSING")
            || sensor.getKind().getName().equals("PIN_TOUCH_SENSING")
            || sensor.getKind().getName().contains("GESTURE") ) {
            return GetSampleSensor
                .make(
                    sensorGetSample.getSensorTypeAndMode(),
                    sensorGetSample.getSensorPort(),
                    sensorGetSample.getSensorPort(),
                    sensorGetSample.isPortInMutation(),
                    sensorGetSample.getProperty(),
                    sensorGetSample.getComment(),
                    getBlocklyDropdownFactory());
        } else {
            return GetSampleSensor
                .make(
                    sensorGetSample.getSensorTypeAndMode(),
                    collectSensorAndGetNewBean(sensor).getPort(),
                    sensorGetSample.getSlot(),
                    sensorGetSample.isPortInMutation(),
                    sensorGetSample.getProperty(),
                    sensorGetSample.getComment(),
                    getBlocklyDropdownFactory());
        }
    }

    private BlocklyBlockProperties modifyPropertyType(BlocklyBlockProperties oldProperty, String newType) {
        return BlocklyBlockProperties
            .make(
                newType, // TODO blockly type is hardcoded in this solution, do it differently?
                oldProperty.getBlocklyId(),
                oldProperty.isDisabled(),
                oldProperty.isCollapsed(),
                oldProperty.isInline(),
                oldProperty.isDeletable(),
                oldProperty.isMovable(),
                oldProperty.isInTask(),
                oldProperty.isShadow(),
                oldProperty.isErrorAttribute());
    }

    /**
     * Helper method.
     * Adds the sensor to the list of used components and constructs a sensor bean for shorter sensor implementations.
     *
     * @param sensor the sensor to base the bean on
     * @return a new, modified sensor bean containing the changed port
     */
    private SensorMetaDataBean collectSensorAndGetNewBean(ExternalSensor<?> sensor) {
        ConfigurationComponent defaultComponent = this.helper.getComponent(sensor.getKind().getName(), sensor.getMode(), sensor.getPort());

        this.builder.addUsedConfigurationComponent(defaultComponent);
        return new SensorMetaDataBean(defaultComponent.getUserDefinedPortName(), sensor.getMode(), sensor.getSlot(), sensor.isPortInMutation());
    }
}
