package de.fhg.iais.roberta.factory.wedo;

import java.util.ArrayList;
import java.util.Properties;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.wedo.WeDoConfiguration;
import de.fhg.iais.roberta.factory.AbstractRobotFactory;
import de.fhg.iais.roberta.factory.ICompilerWorkflow;
import de.fhg.iais.roberta.factory.IRobotFactory;
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
import de.fhg.iais.roberta.syntax.check.program.RobotBrickCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.RobotSimulationCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.wedo.BrickCheckVisitor;
import de.fhg.iais.roberta.syntax.codegen.wedo.WeDoStackMachineVisitor;
import de.fhg.iais.roberta.syntax.sensor.GetSampleType;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.syntax.sensor.SensorMetaDataBean;
import de.fhg.iais.roberta.syntax.sensor.generic.GyroSensor;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;

public class WeDoFactory extends AbstractRobotFactory {
    private final WeDoCompilerWorkflow compilerWorkflow;
    private final Properties wedoProperties;
    private final String name;
    private final int robotPropertyNumber;

    public WeDoFactory(RobertaProperties robertaProperties) {
        super(robertaProperties);
        this.wedoProperties = Util1.loadProperties("classpath:WeDo.properties");
        this.name = this.wedoProperties.getProperty("robot.name");
        this.robotPropertyNumber = robertaProperties.getRobotNumberFromProperty(this.name);
        this.compilerWorkflow = new WeDoCompilerWorkflow();
        addBlockTypesFromProperties("wedo.properties", this.wedoProperties);
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
    public String getProgramToolboxBeginner() {
        return this.wedoProperties.getProperty("robot.program.toolbox.beginner");
    }

    @Override
    public String getProgramToolboxExpert() {
        return this.wedoProperties.getProperty("robot.program.toolbox.expert");
    }

    @Override
    public String getProgramDefault() {
        return this.wedoProperties.getProperty("robot.program.default");
    }

    @Override
    public String getConfigurationToolbox() {
        return this.wedoProperties.getProperty("robot.configuration.toolbox");
    }

    @Override
    public String getConfigurationDefault() {
        return this.wedoProperties.getProperty("robot.configuration.default");
    }

    @Override
    public String getRealName() {
        return this.wedoProperties.getProperty("robot.real.name");
    }

    @Override
    public Boolean hasSim() {
        return this.wedoProperties.getProperty("robot.sim").equals("true") ? true : false;
    }

    @Override
    public String getInfo() {
        return this.wedoProperties.getProperty("robot.info") != null ? this.wedoProperties.getProperty("robot.info") : "#";
    }

    @Override
    public Boolean isBeta() {
        return this.wedoProperties.getProperty("robot.beta") != null ? true : false;
    }

    @Override
    public String getConnectionType() {
        return this.wedoProperties.getProperty("robot.connection");
    }

    @Override
    public String getVendorId() {
        return this.wedoProperties.getProperty("robot.vendor");
    }

    @Override
    public RobotSimulationCheckVisitor getSimProgramCheckVisitor(Configuration brickConfiguration) {
        return null;
    }

    @Override
    public RobotBrickCheckVisitor getRobotProgramCheckVisitor(Configuration brickConfiguration) {
        return new BrickCheckVisitor<Void>(brickConfiguration);
    }

    @Override
    public Boolean hasConfiguration() {
        return Boolean.parseBoolean(this.wedoProperties.getProperty("robot.configuration"));
    }

    @Override
    public String getGroup() {
        return this.robertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".group") != null
            ? this.robertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".group")
            : this.name;
    }

    @Override
    public String generateCode(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) {
        return WeDoStackMachineVisitor.generate((WeDoConfiguration) brickConfiguration, phrasesSet);
    }

    @Override
    public String getCommandline() {
        return this.wedoProperties.getProperty("robot.connection.commandLine");
    }

    @Override
    public String getSignature() {
        return this.wedoProperties.getProperty("robot.connection.signature");
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
