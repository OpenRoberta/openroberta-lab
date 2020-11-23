package de.fhg.iais.roberta.visitor.collect;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.action.generic.PinWriteValueAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
import de.fhg.iais.roberta.syntax.actors.arduino.RelayAction;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkAddRawData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkAddTrainingsData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkClassify;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkInitRawData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkSetup;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkTrain;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Apds9960ColorSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Apds9960DistanceSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Apds9960GestureSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Hts221HumiditySensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Hts221TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Lps22hbPressureSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Lsm9ds1AccSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Lsm9ds1GyroSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Lsm9ds1MagneticFieldSensor;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.visitor.hardware.IArduinoVisitor;

/**
 * This visitor collects information for used actors and sensors in the NEPO AST
 */
public final class ArduinoUsedHardwareCollectorVisitor extends AbstractUsedHardwareCollectorVisitor implements IArduinoVisitor<Void> {

    public ArduinoUsedHardwareCollectorVisitor(ConfigurationAst robotConfiguration, ClassToInstanceMap<IProjectBean.IBuilder<?>> beanBuilders) {
        super(robotConfiguration, beanBuilders);
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        motorOnAction.getParam().getSpeed().accept(this);
        if ( motorOnAction.getParam().getDuration() != null ) {
            motorOnAction.getDurationValue().accept(this);
        }
        if ( this.robotConfiguration != null ) {
            //TODO: nothing done on MotorOnAction
            //Actor actor = this.brickConfiguration.getActors().get(motorOnAction.getPort());
            //if ( actor != null ) {
            //    this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor(motorOnAction.getPort(), actor.getName()));
            //}
        }
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        //        this.getBuilder(UsedHardwareBean.Builder.class).addUsedSensor(new UsedSensor(encoderSensor.getPort(), SC.ENCODER, encoderSensor.getMode()));
        return null;
    }

    @Override
    public Void visitRelayAction(RelayAction<Void> relayAction) {
        return null;
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor<Void> pinGetValueSensor) {
        ConfigurationComponent sensor = this.robotConfiguration.optConfigurationComponent(pinGetValueSensor.getPort());
        if ( sensor == null ) {
            throw new DbcException("Inconsistent configuration and program " + pinGetValueSensor);
        }

        this.getBuilder(UsedHardwareBean.Builder.class).addUsedSensor(new UsedSensor(pinGetValueSensor.getPort(), SC.PIN_VALUE, pinGetValueSensor.getMode()));
        return null;
    }

    @Override
    public Void visitPinWriteValueAction(PinWriteValueAction<Void> pinWriteValueSensor) {
        pinWriteValueSensor.getValue().accept(this);
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor(pinWriteValueSensor.getPort(), SC.ANALOG_PIN));
        return null;
    }

    @Override
    public Void visitSerialWriteAction(SerialWriteAction<Void> serialWriteAction) {
        serialWriteAction.getValue().accept(this);
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor(SC.SERIAL, SC.SERIAL));
        return null;
    }

    @Override
    public Void visitLsm9ds1AccSensor(Lsm9ds1AccSensor<Void> sensor) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor(SC.LSM9DS1, SC.LSM9DS1));
        return null;
    }

    @Override
    public Void visitLsm9ds1GyroSensor(Lsm9ds1GyroSensor<Void> sensor) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor(SC.LSM9DS1, SC.LSM9DS1));
        return null;
    }

    @Override
    public Void visitLsm9ds1MagneticFieldSensor(Lsm9ds1MagneticFieldSensor<Void> sensor) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor(SC.LSM9DS1, SC.LSM9DS1));
        return null;
    }

    @Override
    public Void visitApds9960DistanceSensor(Apds9960DistanceSensor<Void> sensor) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor(SC.APDS9960, SC.APDS9960));
        return null;
    }

    @Override
    public Void visitApds9960GestureSensor(Apds9960GestureSensor<Void> sensor) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor(SC.APDS9960, SC.APDS9960));
        return null;
    }

    @Override
    public Void visitApds9960ColorSensor(Apds9960ColorSensor<Void> sensor) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor(SC.APDS9960, SC.APDS9960));
        return null;
    }

    @Override
    public Void visitLps22hbPressureSensor(Lps22hbPressureSensor<Void> sensor) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor(SC.LPS22HB, SC.LPS22HB));
        return null;
    }

    @Override
    public Void visitHts221TemperatureSensor(Hts221TemperatureSensor<Void> sensor) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor(SC.HTS221, SC.HTS221));
        return null;
    }

    @Override
    public Void visitHts221HumiditySensor(Hts221HumiditySensor<Void> sensor) {
        this.getBuilder(UsedHardwareBean.Builder.class).addUsedActor(new UsedActor(SC.HTS221, SC.HTS221));
        return null;
    }

    @Override
    public Void visitNeuralNetworkSetup(NeuralNetworkSetup<Void> nn) {
        nn.getNumberOfClasses().accept(this);
        nn.getNumberInputNeurons().accept(this);
        nn.getMaxNumberOfNeurons().accept(this);
        return null;
    }

    @Override
    public Void visitNeuralNetworkInitRawData(NeuralNetworkInitRawData<Void> nn) {
        return null;
    }

    @Override
    public Void visitNeuralNetworkAddRawData(NeuralNetworkAddRawData<Void> nn) {
        nn.getRawData().accept(this);
        return null;
    }

    @Override
    public Void visitNeuralNetworkAddTrainingsData(NeuralNetworkAddTrainingsData<Void> nn) {
        nn.getClassNumber().accept(this);
        return null;
    }

    @Override
    public Void visitNeuralNetworkTrain(NeuralNetworkTrain<Void> nn) {
        return null;
    }

    @Override
    public Void visitNeuralNetworkClassify(NeuralNetworkClassify<Void> nn) {
        nn.getProbabilities().accept(this);
        return null;
    }
}
