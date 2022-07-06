package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.bean.NewUsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
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
import de.fhg.iais.roberta.syntax.action.mbed.ServoSetAction;
import de.fhg.iais.roberta.syntax.action.mbed.SingleMotorOnAction;
import de.fhg.iais.roberta.syntax.action.mbed.SingleMotorStopAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.lang.stmt.StmtList;
import de.fhg.iais.roberta.syntax.lang.stmt.WaitStmt;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
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
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;
import de.fhg.iais.roberta.util.basic.Pair;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.syntax.MotionParam;
import de.fhg.iais.roberta.worker.MbedTwo2ThreeTransformerHelper;

/**
 * Used to replace port names of old Mbed programs to fit with the new configuration. Also keeps track of configuration components used, in order to only use
 * the actually used ones.
 */
public class MbedTwo2ThreeTransformerVisitor extends BaseVisitor<Phrase<Void>> implements IMbedTransformerVisitor<Void> {

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
        Pair<ConfigurationComponent, String> compAndName = this.helper.getComponentAndName(ledOnAction.getKind().getName(), "", ledOnAction.getUserDefinedPort());

        this.builder.addUsedConfigurationComponent(compAndName.getFirst());

        return new LedOnAction<>(ledOnAction.getProperty(), (Expr<Void>) ledOnAction.ledColor.modify(this), compAndName.getSecond(), ledOnAction.hide);
    }

    @Override
    public BlocklyDropdownFactory getBlocklyDropdownFactory() {
        return this.blocklyDropdownFactory;
    }

    @Override
    public Phrase<Void> visitLightAction(LightAction<Phrase<Void>> lightAction) {
        Pair<ConfigurationComponent, String> compAndName =
            this.helper.getComponentAndName(lightAction.getKind().getName(), lightAction.mode.toString(), lightAction.port);

        this.builder.addUsedConfigurationComponent(compAndName.getFirst());

        return new LightAction<>(compAndName.getSecond(), lightAction.color, lightAction.mode, (Expr<Void>) lightAction.rgbLedColor.modify(this), lightAction.getProperty());
    }

    @Override
    public Phrase<Void> visitLightStatusAction(LightStatusAction<Phrase<Void>> lightStatusAction) {
        Pair<ConfigurationComponent, String> compAndName =
            this.helper.getComponentAndName(lightStatusAction.getKind().getName(), lightStatusAction.status.name(), lightStatusAction.getUserDefinedPort());

        this.builder.addUsedConfigurationComponent(compAndName.getFirst());

        return new LightStatusAction<>(compAndName.getSecond(), lightStatusAction.status, lightStatusAction.getProperty());
    }

    @Override
    public Phrase<Void> visitPinWriteValueAction(PinWriteValueAction<Phrase<Void>> pinWriteValueAction) {
        Pair<ConfigurationComponent, String> compAndName =
            this.helper.getComponentAndName(pinWriteValueAction.getKind().getName(), pinWriteValueAction.pinValue, pinWriteValueAction.port);

        this.builder.addUsedConfigurationComponent(compAndName.getFirst());

        return new PinWriteValueAction<>(pinWriteValueAction.pinValue, compAndName.getSecond(), (Expr<Void>) pinWriteValueAction.value.modify(this), pinWriteValueAction.actorPortAndMode, pinWriteValueAction.getProperty());
    }

    @Override
    public Phrase<Void> visitServoSetAction(ServoSetAction<Phrase<Void>> servoSetAction) {
        Pair<ConfigurationComponent, String> compAndName = this.helper.getComponentAndName(servoSetAction.getKind().getName(), "", servoSetAction.getUserDefinedPort());

        this.builder.addUsedConfigurationComponent(compAndName.getFirst());

        return new ServoSetAction<>(servoSetAction.getProperty(), compAndName.getSecond(), (Expr<Void>) servoSetAction.value.modify(this));
    }

    @Override
    public Phrase<Void> visitLedBarSetAction(LedBarSetAction<Phrase<Void>> ledBarSetAction) {
        // did not have a port previously, 5 = A1
        Pair<ConfigurationComponent, String> compAndName = this.helper.getComponentAndName(ledBarSetAction.getKind().getName(), "", "5");

        this.builder.addUsedConfigurationComponent(compAndName.getFirst());

        return new LedBarSetAction<>(ledBarSetAction.getProperty(), (Expr<Void>) ledBarSetAction.x.modify(this), (Expr<Void>) ledBarSetAction.brightness.modify(this));
    }

    @Override
    public Phrase<Void> visitFourDigitDisplayShowAction(FourDigitDisplayShowAction<Phrase<Void>> fourDigitDisplayShowAction) {
        // did not have a port previously, 5 = A1
        Pair<ConfigurationComponent, String> compAndName = this.helper.getComponentAndName(fourDigitDisplayShowAction.getKind().getName(), "", "5");

        this.builder.addUsedConfigurationComponent(compAndName.getFirst());

        return new FourDigitDisplayShowAction<>(fourDigitDisplayShowAction.getProperty(), (Expr<Void>) fourDigitDisplayShowAction.value.modify(this), (Expr<Void>) fourDigitDisplayShowAction.position.modify(this), (Expr<Void>) fourDigitDisplayShowAction.colon.modify(this));
    }

    @Override
    public Phrase<Void> visitFourDigitDisplayClearAction(FourDigitDisplayClearAction<Phrase<Void>> fourDigitDisplayClearAction) {
        // did not have a port previously, 5 = A1
        Pair<ConfigurationComponent, String> compAndName = this.helper.getComponentAndName(fourDigitDisplayClearAction.getKind().getName(), "", "5");

        this.builder.addUsedConfigurationComponent(compAndName.getFirst());

        return new FourDigitDisplayClearAction<>(fourDigitDisplayClearAction.getProperty());
    }

    @Override
    public Phrase<Void> visitPlayNoteAction(PlayNoteAction<Phrase<Void>> playNoteAction) {
        Pair<ConfigurationComponent, String> compAndName = this.helper.getComponentAndName(playNoteAction.getKind().getName(), "", playNoteAction.port);

        this.builder.addUsedConfigurationComponent(compAndName.getFirst());

        return new PlayNoteAction<>(playNoteAction.getProperty(), playNoteAction.duration, playNoteAction.frequency, compAndName.getSecond(), playNoteAction.hide);
    }

    @Override
    public Phrase<Void> visitToneAction(ToneAction<Phrase<Void>> toneAction) {
        Pair<ConfigurationComponent, String> compAndName = this.helper.getComponentAndName(toneAction.getKind().getName(), "", toneAction.port);

        this.builder.addUsedConfigurationComponent(compAndName.getFirst());

        return new ToneAction<>(toneAction.getProperty(), (Expr<Void>) toneAction.frequency.modify(this), (Expr<Void>) toneAction.duration.modify(this), compAndName.getSecond(), toneAction.hide);
    }

    @Override
    public Phrase<Void> visitSingleMotorOnAction(SingleMotorOnAction<Phrase<Void>> singleMotorOnAction) {
        // replace this block with motor on action
        Pair<ConfigurationComponent, String> compAndName = this.helper.getComponentAndName("MOTOR_ON_ACTION", "", "A");

        this.builder.addUsedConfigurationComponent(compAndName.getFirst());

        MotionParam.Builder<Void> motionParamBuilder = new MotionParam.Builder<>();
        motionParamBuilder.speed((Expr<Void>) singleMotorOnAction.speed.modify(this));

        return new MotorOnAction<>(compAndName.getSecond(), motionParamBuilder.build(), modifyPropertyType(singleMotorOnAction.getProperty(), "mbedActions_motor_on"));
    }

    @Override
    public Phrase<Void> visitSingleMotorStopAction(SingleMotorStopAction<Phrase<Void>> singleMotorStopAction) {
        // replace this block with motor stop action
        Pair<ConfigurationComponent, String> compAndName = this.helper.getComponentAndName("MOTOR_STOP_ACTION", "", "A");

        this.builder.addUsedConfigurationComponent(compAndName.getFirst());

        return new MotorStopAction<Void>(compAndName.getSecond(), singleMotorStopAction.mode, modifyPropertyType(singleMotorStopAction.getProperty(), "mbedActions_motor_stop"));
    }

    @Override
    public Phrase<Void> visitMotorOnAction(MotorOnAction<Phrase<Void>> motorOnAction) {
        // The ports A+B and Calli:bot both are replaced by a BothMotorsOnAction with two same values
        String port = motorOnAction.getUserDefinedPort();
        if ( port.equals("AB") || port.equals("3") ) {
            Pair<ConfigurationComponent, String> compAndNameA;
            Pair<ConfigurationComponent, String> compAndNameB;
            if ( port.equals("AB") ) {
                compAndNameA = this.helper.getComponentAndName(motorOnAction.getKind().getName(), "", "A");
                compAndNameB = this.helper.getComponentAndName(motorOnAction.getKind().getName(), "", "B");
            } else { // Calli:bot both was represented by 3 in the XML
                compAndNameA = this.helper.getComponentAndName(motorOnAction.getKind().getName(), "", "0"); // 0 = left
                compAndNameB = this.helper.getComponentAndName(motorOnAction.getKind().getName(), "", "2"); // 2 = right
            }
            this.builder.addUsedConfigurationComponent(compAndNameA.getFirst());
            this.builder.addUsedConfigurationComponent(compAndNameB.getFirst());

            Expr<Void> speed = (Expr<Void>) motorOnAction.param.getSpeed().modify(this);

            return new BothMotorsOnAction<Void>(modifyPropertyType(motorOnAction.getProperty(), "mbedActions_motors_on"), speed, speed, compAndNameA.getSecond(), compAndNameB.getSecond());
        } else { // only replace the port for the others
            Pair<ConfigurationComponent, String> compAndName = this.helper.getComponentAndName(motorOnAction.getKind().getName(), "", port);

            this.builder.addUsedConfigurationComponent(compAndName.getFirst());

            return new MotorOnAction<>(compAndName.getSecond(), modifyMotionParam(motorOnAction.param), motorOnAction.getProperty());
        }
    }

    @Override
    public Phrase<Void> visitMotorStopAction(MotorStopAction<Phrase<Void>> motorStopAction) {
        String port = motorStopAction.getUserDefinedPort();
        if ( port.equals("AB") || port.equals("3") ) {
            Pair<ConfigurationComponent, String> compAndNameA;
            Pair<ConfigurationComponent, String> compAndNameB;
            if ( port.equals("AB") ) {
                compAndNameA = this.helper.getComponentAndName(motorStopAction.getKind().getName(), "", "A");
                compAndNameB = this.helper.getComponentAndName(motorStopAction.getKind().getName(), "", "B");
            } else { // Calli:bot both was represented by 3 in the XML
                compAndNameA = this.helper.getComponentAndName(motorStopAction.getKind().getName(), "", "0");
                compAndNameB = this.helper.getComponentAndName(motorStopAction.getKind().getName(), "", "2");
            }

            this.builder.addUsedConfigurationComponent(compAndNameA.getFirst());
            this.builder.addUsedConfigurationComponent(compAndNameB.getFirst());

            return new BothMotorsStopAction<>(modifyPropertyType(motorStopAction.getProperty(), "mbedActions_motors_stop"));
        } else {
            Pair<ConfigurationComponent, String> compAndName =
                this.helper.getComponentAndName(motorStopAction.getKind().getName(), "", motorStopAction.getUserDefinedPort());

            this.builder.addUsedConfigurationComponent(compAndName.getFirst());

            return new MotorStopAction<Void>(compAndName.getSecond(), motorStopAction.mode, motorStopAction.getProperty());
        }
    }

    @Override
    public Phrase<Void> visitBothMotorsOnAction(BothMotorsOnAction<Phrase<Void>> bothMotorsOnAction) {
        String portA = bothMotorsOnAction.portA;
        String portB = bothMotorsOnAction.portB;

        // Calli:bot mapping for this block was LEFT, RIGHT, for the other motor block it is 0 for left and 2 for right, this rectifies the ports
        if ( portA.equals("LEFT") ) {
            portA = "0";
            portB = "2";
        }

        Pair<ConfigurationComponent, String> compAndNameA = this.helper.getComponentAndName("MOTOR_ON_ACTION", "", portA);
        Pair<ConfigurationComponent, String> compAndNameB = this.helper.getComponentAndName("MOTOR_ON_ACTION", "", portB);

        this.builder.addUsedConfigurationComponent(compAndNameA.getFirst());
        this.builder.addUsedConfigurationComponent(compAndNameB.getFirst());

        return new BothMotorsOnAction<Void>(bothMotorsOnAction.getProperty(), (Expr<Void>) bothMotorsOnAction.speedA.modify(this), (Expr<Void>) bothMotorsOnAction.speedB.modify(this), compAndNameA.getSecond(), compAndNameB.getSecond());
    }

    @Override
    public Phrase<Void> visitBothMotorsStopAction(BothMotorsStopAction<Phrase<Void>> bothMotorsStopAction) {
        Pair<ConfigurationComponent, String> compAndNameA = this.helper.getComponentAndName("MOTOR_STOP_ACTION", "", "A");
        Pair<ConfigurationComponent, String> compAndNameB = this.helper.getComponentAndName("MOTOR_STOP_ACTION", "", "B");

        this.builder.addUsedConfigurationComponent(compAndNameA.getFirst());
        this.builder.addUsedConfigurationComponent(compAndNameB.getFirst());

        return new BothMotorsStopAction<>(bothMotorsStopAction.getProperty());
    }

    @Override
    public Phrase<Void> visitKeysSensor(KeysSensor<Phrase<Void>> keysSensor) {
        return new KeysSensor<Void>(keysSensor.getProperty(), collectSensorAndGetNewBean(keysSensor));
    }

    @Override
    public Phrase<Void> visitUltrasonicSensor(UltrasonicSensor<Phrase<Void>> ultrasonicSensor) {
        return new UltrasonicSensor<Void>(ultrasonicSensor.getProperty(), collectSensorAndGetNewBean(ultrasonicSensor));
    }

    @Override
    public Phrase<Void> visitCompassSensor(CompassSensor<Phrase<Void>> compassSensor) {
        return new CompassSensor<>(compassSensor.getProperty(), collectSensorAndGetNewBean(compassSensor));
    }

    @Override
    public Phrase<Void> visitTemperatureSensor(TemperatureSensor<Phrase<Void>> temperatureSensor) {
        return new TemperatureSensor<Void>(temperatureSensor.getProperty(), collectSensorAndGetNewBean(temperatureSensor));
    }

    @Override
    public Phrase<Void> visitSoundSensor(SoundSensor<Phrase<Void>> soundSensor) {
        return new SoundSensor<Void>(soundSensor.getProperty(), collectSensorAndGetNewBean(soundSensor));
    }

    @Override
    public Phrase<Void> visitLightSensor(LightSensor<Phrase<Void>> lightSensor) {
        return new LightSensor<Void>(lightSensor.getProperty(), collectSensorAndGetNewBean(lightSensor));
    }

    @Override
    public Phrase<Void> visitHumiditySensor(HumiditySensor<Phrase<Void>> humiditySensor) {
        return new HumiditySensor<Void>(humiditySensor.getProperty(), collectSensorAndGetNewBean(humiditySensor));
    }

    @Override
    public Phrase<Void> visitAccelerometerSensor(AccelerometerSensor<Phrase<Void>> accelerometerSensor) {
        Pair<ConfigurationComponent, String> compAndName =
            this.helper.getComponentAndName(accelerometerSensor.getKind().getName(), accelerometerSensor.getMode(), accelerometerSensor.getUserDefinedPort());

        this.builder.addUsedConfigurationComponent(compAndName.getFirst());
        // Previously X, Y, Z, STRENGTH were saved in the port, now the should be in the slot
        ExternalSensorBean bean =
            new ExternalSensorBean(
                compAndName.getSecond(),
                accelerometerSensor.getMode(),
                accelerometerSensor.getUserDefinedPort(),
                accelerometerSensor.getMutation());

        return new AccelerometerSensor<Void>(accelerometerSensor.getProperty(), bean);
    }

    @Override
    public Phrase<Void> visitGyroSensor(GyroSensor<Phrase<Void>> gyroSensor) {
        Pair<ConfigurationComponent, String> compAndName =
            this.helper.getComponentAndName(gyroSensor.getKind().getName(), gyroSensor.getMode(), gyroSensor.getUserDefinedPort());

        this.builder.addUsedConfigurationComponent(compAndName.getFirst());
        // Previously X, Y were saved in the port, now the should be in the slot
        ExternalSensorBean bean = new ExternalSensorBean(compAndName.getSecond(), gyroSensor.getMode(), gyroSensor.getUserDefinedPort(), gyroSensor.getMutation());

        return new GyroSensor<>(gyroSensor.getProperty(), bean);
    }

    @Override
    public Phrase<Void> visitInfraredSensor(InfraredSensor<Phrase<Void>> infraredSensor) {
        return new InfraredSensor<Void>(infraredSensor.getProperty(), collectSensorAndGetNewBean(infraredSensor));
    }

    @Override
    public Phrase<Void> visitPinGetValueSensor(PinGetValueSensor<Phrase<Void>> pinGetValueSensor) {
        return new PinGetValueSensor<Void>(pinGetValueSensor.getProperty(), collectSensorAndGetNewBean(pinGetValueSensor));
    }

    @Override
    public Phrase<Void> visitGetSampleSensor(GetSampleSensor<Phrase<Void>> sensorGetSample) {
        ExternalSensor<Phrase<Void>> sensor;
        if ( sensorGetSample.sensor instanceof ExternalSensor ) {
            sensor = (ExternalSensor<Phrase<Void>>) sensorGetSample.sensor;
        } else {
            throw new DbcException("Could not get sensor info, because " + sensorGetSample.sensor.getKind() + " is not of type ExternalSensor!");
        }

        // gyro and accelerometer are handled differently, their ports are written into the slot instead
        if ( sensor.hasName("ACCELEROMETER_SENSING") || sensor.hasName("GYRO_SENSING") ) {
            Pair<ConfigurationComponent, String> compAndName = this.helper.getComponentAndName(sensor.getKind().getName(), sensor.getMode(), sensor.getUserDefinedPort());

            this.builder.addUsedConfigurationComponent(compAndName.getFirst());

            return new GetSampleSensor(sensorGetSample.sensorTypeAndMode, compAndName.getSecond(), sensorGetSample.sensorPort, sensorGetSample.mutation, sensorGetSample.hide, sensorGetSample.getProperty(), getBlocklyDropdownFactory());
        } else if ( sensor.hasName("TIMER_SENSING")
            || sensor.hasName("PIN_TOUCH_SENSING")
            || sensor.getKind().getName().contains("GESTURE") ) {
            return new GetSampleSensor(sensorGetSample.sensorTypeAndMode, sensorGetSample.sensorPort, sensorGetSample.sensorPort, sensorGetSample.mutation, sensorGetSample.hide, sensorGetSample.getProperty(), getBlocklyDropdownFactory());
        } else {
            return new GetSampleSensor(sensorGetSample.sensorTypeAndMode, collectSensorAndGetNewBean(sensor).getPort(), sensorGetSample.slot, sensorGetSample.mutation, sensorGetSample.hide, sensorGetSample.getProperty(), getBlocklyDropdownFactory());
        }
    }

    @Override
    public Phrase<Void> visitWaitStmt(WaitStmt<Phrase<Void>> waitStmt) {
        // replaces the specific and now deprecated mbedControls_wait_for with the generic robControls_wait_for
        return new WaitStmt<>(modifyPropertyType(waitStmt.getProperty(), "robControls_wait_for"), (StmtList<Void>) waitStmt.statements.modify(this));
    }

    private BlocklyProperties modifyPropertyType(BlocklyProperties oldProperty, String newType) {
        // TODO blockly type is hardcoded in this solution, do it differently?
        return new BlocklyProperties(newType, oldProperty.getBlocklyId(), oldProperty.isDisabled(), oldProperty.isCollapsed(), oldProperty.isInline(), oldProperty.isDeletable(), oldProperty.isMovable(), oldProperty.isInTask(), oldProperty.isShadow(), oldProperty.isErrorAttribute(), oldProperty.getComment());
    }

    /**
     * Helper method. Adds the sensor to the list of used components and constructs a sensor bean for shorter sensor implementations.
     *
     * @param sensor the sensor to base the bean on
     * @return a new, modified sensor bean containing the changed port
     */
    private ExternalSensorBean collectSensorAndGetNewBean(ExternalSensor<?> sensor) {
        Pair<ConfigurationComponent, String> compAndName = this.helper.getComponentAndName(sensor.getKind().getName(), sensor.getMode(), sensor.getUserDefinedPort());

        this.builder.addUsedConfigurationComponent(compAndName.getFirst());
        return new ExternalSensorBean(compAndName.getSecond(), sensor.getMode(), sensor.getSlot(), sensor.getMutation());
    }
}
