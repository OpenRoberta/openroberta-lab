package de.fhg.iais.roberta.visitor.collect;

import de.fhg.iais.roberta.bean.UsedMethodBean;
import de.fhg.iais.roberta.syntax.MotionParam;
import de.fhg.iais.roberta.syntax.MotorDuration;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.actors.arduino.RelayAction;
import de.fhg.iais.roberta.syntax.lang.expr.Expr;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkAddRawData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkAddTrainingsData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkClassify;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkInitRawData;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkSetup;
import de.fhg.iais.roberta.syntax.neuralnetwork.NeuralNetworkTrain;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.DropSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.HumiditySensor;
import de.fhg.iais.roberta.syntax.sensor.generic.InfraredSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.LightSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MoistureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.MotionSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PinGetValueSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.PulseSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.RfidSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TimerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.UltrasonicSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Apds9960ColorSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Apds9960DistanceSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Apds9960GestureSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Hts221HumiditySensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Hts221TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Lps22hbPressureSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Lsm9ds1AccSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Lsm9ds1GyroSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.nano33blesense.Lsm9ds1MagneticFieldSensor;
import de.fhg.iais.roberta.visitor.hardware.IArduinoVisitor;

public class ArduinoUsedMethodCollectorVisitor extends AbstractUsedMethodCollectorVisitor implements IArduinoVisitor<Void> {
    public ArduinoUsedMethodCollectorVisitor(UsedMethodBean.Builder builder) {
        super(builder);
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        Expr<Void> durationValue = motorOnAction.getDurationValue();
        if ( durationValue != null ) { // TODO why is this necessary?
            motorOnAction.getDurationValue().accept(this);
        }
        MotionParam<Void> param = motorOnAction.getParam();
        MotorDuration<Void> duration = param.getDuration();
        if ( duration != null ) { // TODO why is this necessary?
            duration.getValue().accept(this);
        }
        param.getSpeed().accept(this);
        return null;
    }

    @Override
    public Void visitKeysSensor(KeysSensor<Void> keysSensor) {
        return null;
    }

    @Override
    public Void visitLightSensor(LightSensor<Void> lightSensor) {
        return null;
    }

    @Override
    public Void visitGyroSensor(GyroSensor<Void> gyroSensor) {
        return null;
    }

    @Override
    public Void visitInfraredSensor(InfraredSensor<Void> infraredSensor) {
        return null;
    }

    @Override
    public Void visitTimerSensor(TimerSensor<Void> timerSensor) {
        return null;
    }

    @Override
    public Void visitUltrasonicSensor(UltrasonicSensor<Void> ultrasonicSensor) {
        return null;
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        return null;
    }

    @Override
    public Void visitVoltageSensor(VoltageSensor<Void> voltageSensor) {
        return null;
    }

    @Override
    public Void visitAccelerometerSensor(AccelerometerSensor<Void> accelerometerSensor) {
        return null;
    }

    @Override
    public Void visitPinGetValueSensor(PinGetValueSensor<Void> pinGetValueSensor) {
        return null;
    }

    @Override
    public Void visitGetSampleSensor(GetSampleSensor<Void> sensorGetSample) {
        return null;
    }

    @Override
    public Void visitMoistureSensor(MoistureSensor<Void> moistureSensor) {
        return null;
    }

    @Override
    public Void visitHumiditySensor(HumiditySensor<Void> humiditySensor) {
        return null;
    }

    @Override
    public Void visitMotionSensor(MotionSensor<Void> motionSensor) {
        return null;
    }

    @Override
    public Void visitDropSensor(DropSensor<Void> dropSensor) {
        return null;
    }

    @Override
    public Void visitPulseSensor(PulseSensor<Void> pulseSensor) {
        return null;
    }

    @Override
    public Void visitRfidSensor(RfidSensor<Void> rfidSensor) {
        return null;
    }

    @Override
    public Void visitRelayAction(RelayAction<Void> relayAction) {
        return null;
    }

    @Override
    public Void visitLsm9ds1AccSensor(Lsm9ds1AccSensor<Void> lsm9ds1AccSensor) {
        lsm9ds1AccSensor.getX().accept(this);
        lsm9ds1AccSensor.getY().accept(this);
        lsm9ds1AccSensor.getZ().accept(this);
        return null;
    }

    @Override
    public Void visitLsm9ds1GyroSensor(Lsm9ds1GyroSensor<Void> lsm9ds1GyroSensor) {
        lsm9ds1GyroSensor.x.accept(this);
        lsm9ds1GyroSensor.y.accept(this);
        lsm9ds1GyroSensor.z.accept(this);
        return null;
    }

    @Override
    public Void visitLsm9ds1MagneticFieldSensor(Lsm9ds1MagneticFieldSensor<Void> lsm9ds1MagneticFieldSensor) {
        lsm9ds1MagneticFieldSensor.getX().accept(this);
        lsm9ds1MagneticFieldSensor.getY().accept(this);
        lsm9ds1MagneticFieldSensor.getZ().accept(this);
        return null;
    }

    @Override
    public Void visitApds9960DistanceSensor(Apds9960DistanceSensor<Void> apds9960DistanceSensor) {
        apds9960DistanceSensor.getDistance().accept(this);
        return null;
    }

    @Override
    public Void visitApds9960GestureSensor(Apds9960GestureSensor<Void> apds9960GestureSensor) {
        apds9960GestureSensor.getGesture().accept(this);
        return null;
    }

    @Override
    public Void visitApds9960ColorSensor(Apds9960ColorSensor<Void> apds9960ColorSensor) {
        apds9960ColorSensor.getR().accept(this);
        apds9960ColorSensor.getG().accept(this);
        apds9960ColorSensor.getB().accept(this);
        return null;
    }

    @Override
    public Void visitLps22hbPressureSensor(Lps22hbPressureSensor<Void> lps22hbPressureSensor) {
        lps22hbPressureSensor.getPressure().accept(this);
        return null;
    }

    @Override
    public Void visitHts221TemperatureSensor(Hts221TemperatureSensor<Void> hts221TemperatureSensor) {
        hts221TemperatureSensor.getTemperature().accept(this);
        return null;
    }

    @Override
    public Void visitHts221HumiditySensor(Hts221HumiditySensor<Void> hts221HumiditySensor) {
        hts221HumiditySensor.getHumidity().accept(this);
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
        return null;
    }
}
