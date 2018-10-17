package de.fhg.iais.roberta.factory;

import java.util.ArrayList;
import java.util.Properties;

import de.fhg.iais.roberta.codegen.ICompilerWorkflow;
import de.fhg.iais.roberta.codegen.WeDoCompilerWorkflow;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.wedo.WeDoConfiguration;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.action.IShowPicture;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.mode.action.ActorPort;
import de.fhg.iais.roberta.mode.sensor.GyroSensorMode;
import de.fhg.iais.roberta.mode.sensor.SensorPort;
import de.fhg.iais.roberta.mode.sensor.wedo.Slot;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.GetSampleType;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.syntax.sensor.SensorMetaDataBean;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.visitor.WeDoStackMachineVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractBrickValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractSimValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.WedoBrickValidatorVisitor;

public class WeDoFactory extends AbstractRobotFactory {
    private final WeDoCompilerWorkflow compilerWorkflow;

    public WeDoFactory(String robotName, Properties robotProperties, String tempDirForUserProjects) {
        super(robotName, robotProperties);
        this.compilerWorkflow = new WeDoCompilerWorkflow();
        addBlockTypesFromProperties(robotName, this.robotProperties);
    }

    public SensorPort getSensorName(String port) {
        return new SensorPort(port, port);
    }

    public ActorPort getActorName(String port) {
        return new ActorPort(port, port);
    }

    @Override
    public ISensorPort getSensorPort(String port) {
        return getSensorName(port);
    }

    @Override
    public IActorPort getActorPort(String port) {
        return getActorName(port);
    }

    @Override
    public IShowPicture getShowPicture(String picture) {
        return null;
    }

    @Override
    public ICompilerWorkflow getRobotCompilerWorkflow() {
        return this.compilerWorkflow;
    }

    @Override
    public ICompilerWorkflow getSimCompilerWorkflow() {
        return null;
    }

    @Override
    public String getFileExtension() {
        return "json";
    }

    @Override
    public AbstractSimValidatorVisitor getSimProgramCheckVisitor(Configuration brickConfiguration) {
        return null;
    }

    @Override
    public AbstractBrickValidatorVisitor getRobotProgramCheckVisitor(Configuration brickConfiguration) {
        return new WedoBrickValidatorVisitor<Void>(brickConfiguration);
    }

    @Override
    public String generateCode(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) {
        return WeDoStackMachineVisitor.generate((WeDoConfiguration) brickConfiguration, phrasesSet);
    }

    @Override
    public Slot getSlot(String slot) {
        return IRobotFactory.getModeValue(slot, Slot.class);
    }

    @Override
    public Sensor<?> createSensor(
        GetSampleType sensorType,
        String port,
        String slot,
        boolean isPortInMutation,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        SensorMetaDataBean sensorMetaDataBean;
        switch ( sensorType.getSensorType() ) {
            case BlocklyConstants.GYRO:
                sensorMetaDataBean = new SensorMetaDataBean(getSensorPort(port), GyroSensorMode.TILTED, getSlot(slot), isPortInMutation);
                return GyroSensor.make(sensorMetaDataBean, properties, comment);
            default:
                return super.createSensor(sensorType, port, slot, isPortInMutation, properties, comment);
        }
    }
}
