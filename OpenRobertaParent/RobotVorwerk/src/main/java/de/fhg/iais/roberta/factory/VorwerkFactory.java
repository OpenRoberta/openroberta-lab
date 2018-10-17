package de.fhg.iais.roberta.factory;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import de.fhg.iais.roberta.codegen.ICompilerWorkflow;
import de.fhg.iais.roberta.codegen.VorwerkCompilerWorkflow;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.action.IShowPicture;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.inter.mode.sensor.ISlot;
import de.fhg.iais.roberta.mode.action.ActorPort;
import de.fhg.iais.roberta.mode.sensor.SensorPort;
import de.fhg.iais.roberta.mode.sensor.UltrasonicSensorMode;
import de.fhg.iais.roberta.mode.sensor.vorwerk.Slot;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.GetSampleType;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.syntax.sensor.SensorMetaDataBean;
import de.fhg.iais.roberta.syntax.sensor.vorwerk.DropOffSensor;
import de.fhg.iais.roberta.syntax.sensor.vorwerk.WallSensor;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.visitor.validate.AbstractProgramValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractSimValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.VorwerkBrickValidatorVisitor;

public class VorwerkFactory extends AbstractRobotFactory {
    protected ICompilerWorkflow robotCompilerWorkflow;
    Map<String, SensorPort> sensorToPorts = IRobotFactory.getSensorPortsFromProperties(Util1.loadProperties("classpath:Vorwerkports.properties"));
    Map<String, ActorPort> actorToPorts = IRobotFactory.getActorPortsFromProperties(Util1.loadProperties("classpath:Vorwerkports.properties"));

    public VorwerkFactory(String robotName, Properties robotProperties, String tempDirForUserProjects) {
        super(robotName, robotProperties);
        this.robotCompilerWorkflow = new VorwerkCompilerWorkflow(tempDirForUserProjects, robotProperties.getProperty("robot.plugin.compiler.resources.dir"));
    }

    @Override
    public ISensorPort getSensorPort(String port) {
        return getSensorPortValue(port, this.sensorToPorts);
    }

    @Override
    public IActorPort getActorPort(String port) {
        return getActorPortValue(port, this.actorToPorts);
    }

    @Override
    public ISlot getSlot(String slot) {
        return IRobotFactory.getModeValue(slot, Slot.class);
    }

    @Override
    public ICompilerWorkflow getRobotCompilerWorkflow() {
        return this.robotCompilerWorkflow;
    }

    @Override
    public String getFileExtension() {
        return "py";
    }

    @Override
    public AbstractSimValidatorVisitor getSimProgramCheckVisitor(Configuration brickConfiguration) {
        return null;
    }

    @Override
    public AbstractProgramValidatorVisitor getRobotProgramCheckVisitor(Configuration brickConfiguration) {
        return new VorwerkBrickValidatorVisitor(brickConfiguration);
    }

    @Override
    public String generateCode(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) {
        return null;
    }

    @Override
    public ICompilerWorkflow getSimCompilerWorkflow() {
        return null;
    }

    @Override
    public IShowPicture getShowPicture(String picture) {
        return null;
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
            case BlocklyConstants.WALL:
                sensorMetaDataBean = new SensorMetaDataBean(getSensorPort(port), UltrasonicSensorMode.DISTANCE, getSlot(slot), isPortInMutation);
                return WallSensor.make(sensorMetaDataBean, properties, comment);
            case BlocklyConstants.DROP_OFF:
                sensorMetaDataBean = new SensorMetaDataBean(getSensorPort(port), UltrasonicSensorMode.DISTANCE, getSlot(slot), isPortInMutation);
                return DropOffSensor.make(sensorMetaDataBean, properties, comment);
            default:
                return super.createSensor(sensorType, port, slot, isPortInMutation, properties, comment);
        }
    }
}
