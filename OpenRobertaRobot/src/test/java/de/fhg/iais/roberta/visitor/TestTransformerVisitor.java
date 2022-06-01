package de.fhg.iais.roberta.visitor;

import de.fhg.iais.roberta.factory.BlocklyDropdownFactory;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.sensor.generic.GetSampleSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.KeysSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.TemperatureSensor;
import de.fhg.iais.roberta.util.syntax.SensorMetaDataBean;

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
        return KeysSensor
            .make(
                new SensorMetaDataBean("KeysPort", "KeysMode", "KeysSlot", keysSensor.getMutation()),
                keysSensor.getProperty(),
                keysSensor.getComment());
    }

    @Override
    public Phrase<Void> visitTemperatureSensor(TemperatureSensor<Phrase<Void>> temperatureSensor) {
        return TemperatureSensor
            .make(
                new SensorMetaDataBean("TempPort", "TempMode", "TempSlot", temperatureSensor.getMutation()),
                temperatureSensor.getProperty(),
                temperatureSensor.getComment());
    }

    @Override
    public Phrase<Void> visitMotorOnAction(MotorOnAction<Phrase<Void>> motorOnAction) {
        return MotorOnAction.make("MotorPort", modifyMotionParam(motorOnAction.getParam()), motorOnAction.getProperty(), motorOnAction.getComment());
    }

    @Override
    public Phrase<Void> visitGetSampleSensor(GetSampleSensor<Phrase<Void>> sensorGetSample) {
        return GetSampleSensor
            .make(
                "TEMPERATURE_VALUE",
                "GetSamplePort",
                "GetSampleSlot",
                sensorGetSample.getMutation(),
                sensorGetSample.getHide(),
                sensorGetSample.getProperty(),
                sensorGetSample.getComment(),
                this.blocklyDropdownFactory);
    }
}
