package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.util.ast.ExternalSensorBean;

public class TestTransformerVisitor extends TransformerVisitor {

    private final BlocklyDropdownFactory blocklyDropdownFactory;

    public TestTransformerVisitor(BlocklyDropdownFactory blocklyDropdownFactory) {
        this.blocklyDropdownFactory = blocklyDropdownFactory;
    }

    @Override
    public BlocklyDropdownFactory getBlocklyDropdownFactory() {
        return this.blocklyDropdownFactory;
    }

    @Override
    public Phrase visitKeysSensor(KeysSensor keysSensor) {
        return new KeysSensor(keysSensor.getProperty(), new ExternalSensorBean("KeysPort", "KeysMode", "KeysSlot", keysSensor.getMutation()));
    }

    @Override
    public Phrase visitTemperatureSensor(TemperatureSensor temperatureSensor) {
        return new TemperatureSensor(temperatureSensor.getProperty(), new ExternalSensorBean("TempPort", "TempMode", "TempSlot", temperatureSensor.getMutation()));
    }

    @Override
    public Phrase visitMotorOnAction(MotorOnAction motorOnAction) {
        return new MotorOnAction("MotorPort", modifyMotionParam(motorOnAction.param), motorOnAction.getProperty());
    }

    @Override
    public Phrase visitGetSampleSensor(GetSampleSensor sensorGetSample) {
        return new GetSampleSensor("TEMPERATURE_VALUE", "GetSamplePort", "GetSampleSlot", sensorGetSample.mutation, sensorGetSample.hide, sensorGetSample.getProperty(), this.blocklyDropdownFactory);
    }
}
