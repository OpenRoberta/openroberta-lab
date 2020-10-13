package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.bean.NewUsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
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
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
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
import de.fhg.iais.roberta.util.Pair;
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
        BlocklyDropdownFactory blocklyDropdownFactory,
        ConfigurationAst configuration) {
        this.helper = helper;
        this.builder = builder;
        this.blocklyDropdownFactory = blocklyDropdownFactory;

        // Add the default configuration onto the used configuration components
        for ( ConfigurationComponent cc : configuration.getConfigurationComponentsValues() ) {
            this.builder.addUsedConfigurationComponent(cc);
        }
    }

    @Override
    public Phrase<Void> visitLedOnAction(LedOnAction<Phrase<Void>> ledOnAction) {
        Pair<ConfigurationComponent, String>
            compAndName = this.helper.getComponentAndName(ledOnAction.getKind().getName(), "", ledOnAction.getPort());

        this.builder.addUsedConfigurationComponent(compAndName.getFirst());

        return LedOnAction
            .make(
                compAndName.getSecond(),
                (Expr<Void>) ledOnAction.getLedColor().modify(this),
                ledOnAction.getProperty(),
                ledOnAction.getComment());
    }

    @Override
    public Phrase<Void> visitPinSetPullAction(PinSetPullAction<Phrase<Void>> pinSetPullAction) {
        Pair<ConfigurationComponent, String>
            compAndName =
            this.helper.getComponentAndName(pinSetPullAction.getKind().getName(), pinSetPullAction.getMode(), pinSetPullAction.getPort());

        this.builder.addUsedConfigurationComponent(compAndName.getFirst());

        return PinSetPullAction
            .make(pinSetPullAction.getMode(), compAndName.getSecond(), pinSetPullAction.getProperty(), pinSetPullAction.getComment());
    }

    @Override
    public BlocklyDropdownFactory getBlocklyDropdownFactory() {
        return this.blocklyDropdownFactory;
    }

    @Override
    public Phrase<Void> visitLightAction(LightAction<Phrase<Void>> lightAction) {
        Pair<ConfigurationComponent, String>
            compAndName =
            this.helper.getComponentAndName(lightAction.getKind().getName(), lightAction.getMode().toString(), lightAction.getPort());

        this.builder.addUsedConfigurationComponent(compAndName.getFirst());

        return LightAction
            .make(
                compAndName.getSecond(),
                lightAction.getColor(),
                lightAction.getMode(),
                (Expr<Void>) lightAction.getRgbLedColor().modify(this),
                lightAction.getProperty(),
                lightAction.getComment());
    }

    @Override
    public Phrase<Void> visitLightStatusAction(LightStatusAction<Phrase<Void>> lightStatusAction) {
        Pair<ConfigurationComponent, String>
            compAndName =
            this.helper.getComponentAndName(lightStatusAction.getKind().getName(), lightStatusAction.getStatus().name(), lightStatusAction.getPort());

        this.builder.addUsedConfigurationComponent(compAndName.getFirst());

        return LightStatusAction
            .make(compAndName.getSecond(), lightStatusAction.getStatus(), lightStatusAction.getProperty(), lightStatusAction.getComment());
    }

    @Override
    public Phrase<Void> visitPinWriteValueAction(PinWriteValueAction<Phrase<Void>> pinWriteValueAction) {
        Pair<ConfigurationComponent, String>
            compAndName =
            this.helper.getComponentAndName(pinWriteValueAction.getKind().getName(), pinWriteValueAction.getMode(), pinWriteValueAction.getPort());

        this.builder.addUsedConfigurationComponent(compAndName.getFirst());

        return PinWriteValueAction
            .make(
                pinWriteValueAction.getMode(),
                compAndName.getSecond(),
                (Expr<Void>) pinWriteValueAction.getValue().modify(this),
                pinWriteValueAction.isActorPortAndMode(),
                pinWriteValueAction.getProperty(),
                pinWriteValueAction.getComment());
    }

    @Override
    public Phrase<Void> visitServoSetAction(ServoSetAction<Phrase<Void>> servoSetAction) {
        Pair<ConfigurationComponent, String>
            compAndName = this.helper.getComponentAndName(servoSetAction.getKind().getName(), "", servoSetAction.getPort());

        this.builder.addUsedConfigurationComponent(compAndName.getFirst());

        return ServoSetAction
            .make(
                compAndName.getSecond(),
                (Expr<Void>) servoSetAction.getValue().modify(this),
                servoSetAction.getProperty(),
                servoSetAction.getComment());
    }

    @Override
    public Phrase<Void> visitLedBarSetAction(LedBarSetAction<Phrase<Void>> ledBarSetAction) {
        // did not have a port previously, 5 = A1
        Pair<ConfigurationComponent, String>
            compAndName = this.helper.getComponentAndName(ledBarSetAction.getKind().getName(), "", "5");

        this.builder.addUsedConfigurationComponent(compAndName.getFirst());

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
        Pair<ConfigurationComponent, String>
            compAndName = this.helper.getComponentAndName(fourDigitDisplayShowAction.getKind().getName(), "", "5");

        this.builder.addUsedConfigurationComponent(compAndName.getFirst());

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
        Pair<ConfigurationComponent, String>
            compAndName = this.helper.getComponentAndName(fourDigitDisplayClearAction.getKind().getName(), "", "5");

        this.builder.addUsedConfigurationComponent(compAndName.getFirst());

        return FourDigitDisplayClearAction.make(fourDigitDisplayClearAction.getProperty(), fourDigitDisplayClearAction.getComment());
    }

    @Override
    public Phrase<Void> visitPlayNoteAction(PlayNoteAction<Phrase<Void>> playNoteAction) {
        Pair<ConfigurationComponent, String>
            compAndName = this.helper.getComponentAndName(playNoteAction.getKind().getName(), "", playNoteAction.getPort());

        this.builder.addUsedConfigurationComponent(compAndName.getFirst());

        return PlayNoteAction
            .make(
                compAndName.getSecond(),
                playNoteAction.getDuration(),
                playNoteAction.getFrequency(),
                playNoteAction.getProperty(),
                playNoteAction.getComment());
    }

    @Override
    public Phrase<Void> visitToneAction(ToneAction<Phrase<Void>> toneAction) {
        Pair<ConfigurationComponent, String>
            compAndName = this.helper.getComponentAndName(toneAction.getKind().getName(), "", toneAction.getPort());

        this.builder.addUsedConfigurationComponent(compAndName.getFirst());

        return ToneAction
            .make(
                (Expr<Void>) toneAction.getFrequency().modify(this),
                (Expr<Void>) toneAction.getDuration().modify(this),
                compAndName.getSecond(),
                toneAction.getProperty(),
                toneAction.getComment());
    }

    @Override
    public Phrase<Void> visitSingleMotorOnAction(SingleMotorOnAction<Phrase<Void>> singleMotorOnAction) {
        // replace this block with motor on action
        Pair<ConfigurationComponent, String>
            compAndName = this.helper.getComponentAndName("MOTOR_ON_ACTION", "", "A");

        this.builder.addUsedConfigurationComponent(compAndName.getFirst());

        MotionParam.Builder<Void> motionParamBuilder = new MotionParam.Builder<>();
        motionParamBuilder.speed((Expr<Void>) singleMotorOnAction.getSpeed().modify(this));

        return MotorOnAction
            .make(
                compAndName.getSecond(),
                motionParamBuilder.build(),
                modifyPropertyType(singleMotorOnAction.getProperty(), "mbedActions_motor_on"),
                singleMotorOnAction.getComment());
    }

    @Override
    public Phrase<Void> visitSingleMotorStopAction(SingleMotorStopAction<Phrase<Void>> singleMotorStopAction) {
        // replace this block with motor stop action
        Pair<ConfigurationComponent, String>
            compAndName = this.helper.getComponentAndName("MOTOR_STOP_ACTION", "", "A");

        this.builder.addUsedConfigurationComponent(compAndName.getFirst());

        return MotorStopAction
            .make(
                compAndName.getSecond(),
                singleMotorStopAction.getMode(),
                modifyPropertyType(singleMotorStopAction.getProperty(), "mbedActions_motor_stop"),
                singleMotorStopAction.getComment());
    }

    @Override
    public Phrase<Void> visitMotorOnAction(MotorOnAction<Phrase<Void>> motorOnAction) {
        // The ports A+B and Calli:bot both are replaced by a BothMotorsOnAction with two same values
        String port = motorOnAction.getUserDefinedPort();
        if ( port.equals("AB") || port.equals("3") ) {
            Pair<ConfigurationComponent, String>
            compAndNameA;
            Pair<ConfigurationComponent, String>
            compAndNameB;
            if ( port.equals("AB") ) {
                compAndNameA = this.helper.getComponentAndName(motorOnAction.getKind().getName(), "", "A");
                compAndNameB = this.helper.getComponentAndName(motorOnAction.getKind().getName(), "", "B");
            } else { // Calli:bot both was represented by 3 in the XML
                compAndNameA = this.helper.getComponentAndName(motorOnAction.getKind().getName(), "", "0"); // 0 = left
                compAndNameB = this.helper.getComponentAndName(motorOnAction.getKind().getName(), "", "2"); // 2 = right
            }
            this.builder.addUsedConfigurationComponent(compAndNameA.getFirst());
            this.builder.addUsedConfigurationComponent(compAndNameB.getFirst());

            Expr<Void> speed = (Expr<Void>) motorOnAction.getParam().getSpeed().modify(this);

            return BothMotorsOnAction
                .make(
                    compAndNameA.getSecond(),
                    compAndNameB.getSecond(),
                    speed,
                    speed,
                    modifyPropertyType(motorOnAction.getProperty(), "mbedActions_motors_on"),
                    motorOnAction.getComment());
        } else { // only replace the port for the others
            Pair<ConfigurationComponent, String>
            compAndName = this.helper.getComponentAndName(motorOnAction.getKind().getName(), "", port);

            this.builder.addUsedConfigurationComponent(compAndName.getFirst());

            return MotorOnAction
                .make(
                    compAndName.getSecond(),
                    modifyMotionParam(motorOnAction.getParam()),
                    motorOnAction.getProperty(),
                    motorOnAction.getComment());
        }
    }

    @Override
    public Phrase<Void> visitMotorStopAction(MotorStopAction<Phrase<Void>> motorStopAction) {
        String port = motorStopAction.getUserDefinedPort();
        if ( port.equals("AB") || port.equals("3") ) {
            Pair<ConfigurationComponent, String>
            compAndNameA;
            Pair<ConfigurationComponent, String>
            compAndNameB;
            if ( port.equals("AB") ) {
                compAndNameA = this.helper.getComponentAndName(motorStopAction.getKind().getName(), "", "A");
                compAndNameB = this.helper.getComponentAndName(motorStopAction.getKind().getName(), "", "B");
            } else { // Calli:bot both was represented by 3 in the XML
                compAndNameA = this.helper.getComponentAndName(motorStopAction.getKind().getName(), "", "0");
                compAndNameB = this.helper.getComponentAndName(motorStopAction.getKind().getName(), "", "2");
            }

            this.builder.addUsedConfigurationComponent(compAndNameA.getFirst());
            this.builder.addUsedConfigurationComponent(compAndNameB.getFirst());

            return BothMotorsStopAction.make(modifyPropertyType(motorStopAction.getProperty(), "mbedActions_motors_stop"), motorStopAction.getComment());
        } else {
            Pair<ConfigurationComponent, String>
            compAndName = this.helper.getComponentAndName(motorStopAction.getKind().getName(), "", motorStopAction.getUserDefinedPort());

            this.builder.addUsedConfigurationComponent(compAndName.getFirst());

            return MotorStopAction
                .make(compAndName.getSecond(), motorStopAction.getMode(), motorStopAction.getProperty(), motorStopAction.getComment());
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

        Pair<ConfigurationComponent, String>
            compAndNameA = this.helper.getComponentAndName("MOTOR_ON_ACTION", "", portA);
        Pair<ConfigurationComponent, String>
            compAndNameB = this.helper.getComponentAndName("MOTOR_ON_ACTION", "", portB);

        this.builder.addUsedConfigurationComponent(compAndNameA.getFirst());
        this.builder.addUsedConfigurationComponent(compAndNameB.getFirst());

        return BothMotorsOnAction
            .make(
                compAndNameA.getSecond(),
                compAndNameB.getSecond(),
                (Expr<Void>) bothMotorsOnAction.getSpeedA().modify(this),
                (Expr<Void>) bothMotorsOnAction.getSpeedB().modify(this),
                bothMotorsOnAction.getProperty(),
                bothMotorsOnAction.getComment());
    }

    @Override
    public Phrase<Void> visitBothMotorsStopAction(BothMotorsStopAction<Phrase<Void>> bothMotorsStopAction) {
        Pair<ConfigurationComponent, String>
            compAndNameA = this.helper.getComponentAndName("MOTOR_STOP_ACTION", "", "A");
        Pair<ConfigurationComponent, String>
            compAndNameB = this.helper.getComponentAndName("MOTOR_STOP_ACTION", "", "B");

        this.builder.addUsedConfigurationComponent(compAndNameA.getFirst());
        this.builder.addUsedConfigurationComponent(compAndNameB.getFirst());

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
        Pair<ConfigurationComponent, String>
            compAndName =
            this.helper.getComponentAndName(accelerometerSensor.getKind().getName(), accelerometerSensor.getMode(), accelerometerSensor.getPort());

        this.builder.addUsedConfigurationComponent(compAndName.getFirst());
        // Previously X, Y, Z, STRENGTH were saved in the port, now the should be in the slot
        SensorMetaDataBean bean =
            new SensorMetaDataBean(
                compAndName.getSecond(),
                accelerometerSensor.getMode(),
                accelerometerSensor.getPort(),
                accelerometerSensor.isPortInMutation());

        return AccelerometerSensor.make(bean, accelerometerSensor.getProperty(), accelerometerSensor.getComment());
    }

    @Override
    public Phrase<Void> visitGyroSensor(GyroSensor<Phrase<Void>> gyroSensor) {
        Pair<ConfigurationComponent, String>
            compAndName = this.helper.getComponentAndName(gyroSensor.getKind().getName(), gyroSensor.getMode(), gyroSensor.getPort());

        this.builder.addUsedConfigurationComponent(compAndName.getFirst());
        // Previously X, Y were saved in the port, now the should be in the slot
        SensorMetaDataBean bean =
            new SensorMetaDataBean(compAndName.getSecond(), gyroSensor.getMode(), gyroSensor.getPort(), gyroSensor.isPortInMutation());

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
            Pair<ConfigurationComponent, String>
            compAndName = this.helper.getComponentAndName(sensor.getKind().getName(), sensor.getMode(), sensor.getPort());

            this.builder.addUsedConfigurationComponent(compAndName.getFirst());

            return GetSampleSensor
                .make(
                    sensorGetSample.getSensorTypeAndMode(),
                    compAndName.getSecond(),
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

    @Override
    public Phrase<Void> visitWaitStmt(WaitStmt<Phrase<Void>> waitStmt) {
        // replaces the specific and now deprecated mbedControls_wait_for with the generic robControls_wait_for
        return WaitStmt
            .make(
                (StmtList<Void>) waitStmt.getStatements().modify(this),
                modifyPropertyType(waitStmt.getProperty(), "robControls_wait_for"),
                waitStmt.getComment());
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
        Pair<ConfigurationComponent, String>
            compAndName = this.helper.getComponentAndName(sensor.getKind().getName(), sensor.getMode(), sensor.getPort());

        this.builder.addUsedConfigurationComponent(compAndName.getFirst());
        return new SensorMetaDataBean(compAndName.getSecond(), sensor.getMode(), sensor.getSlot(), sensor.isPortInMutation());
    }
}
