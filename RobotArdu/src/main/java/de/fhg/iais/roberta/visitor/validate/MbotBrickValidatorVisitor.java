package de.fhg.iais.roberta.visitor.validate;

import de.fhg.iais.roberta.bean.UsedHardwareBean;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.syntax.action.serial.SerialWriteAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.ReceiveIRAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.SendIRAction;
import de.fhg.iais.roberta.syntax.expressions.arduino.LedMatrix;
import de.fhg.iais.roberta.syntax.sensor.generic.AccelerometerSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.VoltageSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.mbot.FlameSensor;
import de.fhg.iais.roberta.syntax.sensors.arduino.mbot.Joystick;
import de.fhg.iais.roberta.visitor.hardware.IMbotVisitor;

public final class MbotBrickValidatorVisitor extends AbstractBrickValidatorVisitor implements IMbotVisitor<Void> {

    public MbotBrickValidatorVisitor(UsedHardwareBean.Builder builder, ConfigurationAst brickConfiguration) {
        super(builder, brickConfiguration);
    }

    @Override
    public Void visitTemperatureSensor(TemperatureSensor<Void> temperatureSensor) {
        return null;
    }

    @Override
    public Void visitJoystick(Joystick<Void> joystick) {
        return null;
    }

    @Override
    public Void visitAccelerometer(AccelerometerSensor<Void> accelerometer) {
        return null;
    }

    @Override
    public Void visitFlameSensor(FlameSensor<Void> flameSensor) {
        return null;
    }

    @Override
    public Void visitImage(LedMatrix<Void> ledMatrix) {
        return null;
    }

    @Override
    public Void visitVoltageSensor(VoltageSensor<Void> voltageSensor) {
        return null;
    }

    @Override
    public Void visitSerialWriteAction(SerialWriteAction<Void> serialWriteAction) {
        return null;
    }

    @Override
    public Void visitSendIRAction(SendIRAction<Void> sendIRAction) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visitReceiveIRAction(ReceiveIRAction<Void> receiveIRAction) {
        // TODO Auto-generated method stub
        return null;
    }
}
