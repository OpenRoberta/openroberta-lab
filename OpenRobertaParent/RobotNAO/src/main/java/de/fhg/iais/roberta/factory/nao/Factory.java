package de.fhg.iais.roberta.factory.nao;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import com.google.inject.AbstractModule;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.nao.NAOConfiguration;
import de.fhg.iais.roberta.factory.AbstractRobotFactory;
import de.fhg.iais.roberta.factory.ICompilerWorkflow;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.action.IShowPicture;
import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.mode.action.ActorPort;
import de.fhg.iais.roberta.mode.action.Language;
import de.fhg.iais.roberta.mode.sensor.SensorPort;
import de.fhg.iais.roberta.mode.sensor.nao.DetectedFaceMode;
import de.fhg.iais.roberta.mode.sensor.nao.DetectedMarkMode;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.program.RobotCommonCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.RobotSimulationCheckVisitor;
import de.fhg.iais.roberta.syntax.codegen.nao.PythonVisitor;
import de.fhg.iais.roberta.syntax.sensor.GetSampleType;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.syntax.sensor.SensorMetaDataBean;
import de.fhg.iais.roberta.syntax.sensor.nao.DetectFace;
import de.fhg.iais.roberta.syntax.sensor.nao.DetectedMark;
import de.fhg.iais.roberta.syntax.sensor.nao.ElectricCurrent;
import de.fhg.iais.roberta.syntax.sensor.nao.FsrSensor;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;

public class Factory extends AbstractRobotFactory {
    private final CompilerWorkflow compilerWorkflow;
    private final Properties naoProperties;
    private final String name;
    private final int robotPropertyNumber;
    Map<String, SensorPort> sensorToPorts = IRobotFactory.getSensorPortsFromProperties(Util1.loadProperties("classpath:NAOports.properties"));
    Map<String, ActorPort> actorToPorts = IRobotFactory.getActorPortsFromProperties(Util1.loadProperties("classpath:NAOports.properties"));

    public Factory(RobertaProperties robertaProperties) {
        super(robertaProperties);
        this.naoProperties = Util1.loadProperties("classpath:NAO.properties");
        this.name = this.naoProperties.getProperty("robot.name");
        this.robotPropertyNumber = robertaProperties.getRobotNumberFromProperty(this.name);
        this.compilerWorkflow =
            new CompilerWorkflow(
                robertaProperties.getTempDirForUserProjects(),
                robertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".compiler.resources.dir"));
        addBlockTypesFromProperties("NAO.properties", this.naoProperties);
    }

    @Override
    public ISensorPort getSensorPort(String port) {
        return getSensorPortValue(port, this.sensorToPorts);
    }

    @Override
    public IActorPort getActorPort(String port) {
        return getActorPortValue(port, this.actorToPorts);
    }

    public IMode getDetectMarkMode(String mode) {
        return IRobotFactory.getModeValue(mode, DetectedMarkMode.class);
    }

    public IMode getDetectFaceMode(String mode) {
        return IRobotFactory.getModeValue(mode, DetectedFaceMode.class);
    }

    @Override
    public IShowPicture getShowPicture(String picture) {
        return null;
    }

    @Override
    public String getFileExtension() {
        return "py";
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
    public AbstractModule getGuiceModule() {
        return new GuiceModule(this.robertaProperties.getRobertaProperties());
    }

    @Override
    public String getProgramToolboxBeginner() {
        return this.naoProperties.getProperty("robot.program.toolbox.beginner");
    }

    @Override
    public String getProgramToolboxExpert() {
        return this.naoProperties.getProperty("robot.program.toolbox.expert");
    }

    @Override
    public String getProgramDefault() {
        return this.naoProperties.getProperty("robot.program.default");
    }

    @Override
    public String getConfigurationToolbox() {
        return this.naoProperties.getProperty("robot.configuration.toolbox");
    }

    @Override
    public String getConfigurationDefault() {
        return this.naoProperties.getProperty("robot.configuration.default");
    }

    @Override
    public String getRealName() {
        return this.naoProperties.getProperty("robot.real.name");
    }

    @Override
    public Boolean hasSim() {
        return this.naoProperties.getProperty("robot.sim").equals("true") ? true : false;
    }

    @Override
    public String getInfo() {
        return this.naoProperties.getProperty("robot.info") != null ? this.naoProperties.getProperty("robot.info") : "#";
    }

    @Override
    public Boolean isBeta() {
        return this.naoProperties.getProperty("robot.beta") != null ? true : false;
    }

    @Override
    public String getConnectionType() {
        return this.naoProperties.getProperty("robot.connection");
    }

    @Override
    public RobotSimulationCheckVisitor getSimProgramCheckVisitor(Configuration brickConfiguration) {
        return null;
    }

    @Override
    public RobotCommonCheckVisitor getRobotProgramCheckVisitor(Configuration brickConfiguration) {
        return null;
    }

    @Override
    public Boolean hasConfiguration() {
        return Boolean.parseBoolean(this.naoProperties.getProperty("robot.configuration"));
    }

    @Override
    public String getGroup() {
        return this.robertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".group") != null
            ? this.robertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".group")
            : this.name;
    }

    @Override
    public String generateCode(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) {
        return PythonVisitor.generate((NAOConfiguration) brickConfiguration, phrasesSet, withWrapping, Language.GERMAN);
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
            case BlocklyConstants.ELECTRIC_CURRENT:
                sensorMetaDataBean =
                    new SensorMetaDataBean(getSensorPort(port), getPlaceholderSensorMode(sensorType.getSensorMode()), getSlot(slot), isPortInMutation);
                return ElectricCurrent.make(sensorMetaDataBean, properties, comment);
            case BlocklyConstants.FSR:
                sensorMetaDataBean =
                    new SensorMetaDataBean(getSensorPort(port), getPlaceholderSensorMode(sensorType.getSensorMode()), getSlot(slot), isPortInMutation);
                return FsrSensor.make(sensorMetaDataBean, properties, comment);
            case BlocklyConstants.DETECT_MARK:
                sensorMetaDataBean =
                    new SensorMetaDataBean(getSensorPort(port), getDetectMarkMode(sensorType.getSensorMode()), getSlot(slot), isPortInMutation);
                return DetectedMark.make(sensorMetaDataBean, properties, comment);
            case BlocklyConstants.DETECT_FACE:
                sensorMetaDataBean =
                    new SensorMetaDataBean(getSensorPort(port), getDetectFaceMode(sensorType.getSensorMode()), getSlot(slot), isPortInMutation);
                return DetectFace.make(sensorMetaDataBean, properties, comment);
            default:
                return super.createSensor(sensorType, port, slot, isPortInMutation, properties, comment);
        }
    }
}
