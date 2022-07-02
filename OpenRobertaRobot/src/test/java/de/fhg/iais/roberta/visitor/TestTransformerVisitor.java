package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

public class TestTransformerVisitor extends BaseVisitor<Phrase<Void>> implements ITransformerVisitor<Void> {

    private final BlocklyDropdownFactory blocklyDropdownFactory;

    public TestTransformerVisitor(BlocklyDropdownFactory blocklyDropdownFactory) {
        this.blocklyDropdownFactory = blocklyDropdownFactory;
    }

    @Override
    public BlocklyDropdownFactory getBlocklyDropdownFactory() {
        return this.blocklyDropdownFactory;
    }

    @Override
    public Phrase<Void> visitKeysSensor(KeysSensor<Phrase<Void>> keysSensor) {
        return new KeysSensor<Void>(keysSensor.getProperty(), keysSensor.getComment(), new ExternalSensorBean("KeysPort", "KeysMode", "KeysSlot", keysSensor.getMutation()));
    }

    @Override
    public Phrase<Void> visitTemperatureSensor(TemperatureSensor<Phrase<Void>> temperatureSensor) {
        return new TemperatureSensor<Void>(temperatureSensor.getProperty(), temperatureSensor.getComment(), new ExternalSensorBean("TempPort", "TempMode", "TempSlot", temperatureSensor.getMutation()));
    }

    @Override
    public Phrase<Void> visitMotorOnAction(MotorOnAction<Phrase<Void>> motorOnAction) {
        return new MotorOnAction<>("MotorPort", modifyMotionParam(motorOnAction.param), motorOnAction.getProperty(), motorOnAction.getComment());
    }

    @Override
    public Phrase<Void> visitGetSampleSensor(GetSampleSensor<Phrase<Void>> sensorGetSample) {
        return new GetSampleSensor("TEMPERATURE_VALUE", "GetSamplePort", "GetSampleSlot", sensorGetSample.mutation, sensorGetSample.hide, sensorGetSample.getProperty(), sensorGetSample.getComment(), this.blocklyDropdownFactory);
    }
}
