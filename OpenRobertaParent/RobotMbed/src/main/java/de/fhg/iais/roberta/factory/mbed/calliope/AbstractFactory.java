package de.fhg.iais.roberta.factory.mbed.calliope;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.factory.AbstractRobotFactory;
import de.fhg.iais.roberta.factory.ICompilerWorkflow;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.factory.mbed.SimCompilerWorkflow;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.action.IShowPicture;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.mode.action.ActorPort;
import de.fhg.iais.roberta.mode.sensor.SensorPort;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.program.RobotCommonCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.RobotSimulationCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.mbed.BoardCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.mbed.calliope.SimulationCheckVisitor;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;

public abstract class AbstractFactory extends AbstractRobotFactory {

    protected ICompilerWorkflow compilerWorkflow;
    protected SimCompilerWorkflow calliopeSimCompilerWorkflow;
    protected Properties calliopeProperties;
    protected String name;
    protected int robotPropertyNumber;
    Map<String, SensorPort> sensorToPorts = IRobotFactory.getSensorPortsFromProperties(Util1.loadProperties("classpath:Calliopeports.properties"));
    Map<String, ActorPort> actorToPorts = IRobotFactory.getActorPortsFromProperties(Util1.loadProperties("classpath:Calliopeports.properties"));

    public AbstractFactory(RobertaProperties robertaProperties) {
        super(robertaProperties);
        final Properties mbedProperties = Util1.loadProperties("classpath:Mbed.properties");
        addBlockTypesFromProperties("Mbed.properties", mbedProperties);

        this.calliopeSimCompilerWorkflow = new SimCompilerWorkflow();
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
    public IShowPicture getShowPicture(String picture) {
        return null;
    }

    @Override
    public String generateCode(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) {
        return null;
    }

    @Override
    public String getFileExtension() {
        return "cpp";
    }

    @Override
    public ICompilerWorkflow getRobotCompilerWorkflow() {
        return this.compilerWorkflow;
    }

    @Override
    public ICompilerWorkflow getSimCompilerWorkflow() {
        return this.calliopeSimCompilerWorkflow;
    }

    @Override
    public String getProgramToolboxBeginner() {
        return this.calliopeProperties.getProperty("robot.program.toolbox.beginner");
    }

    @Override
    public String getProgramToolboxExpert() {
        return this.calliopeProperties.getProperty("robot.program.toolbox.expert");
    }

    @Override
    public String getProgramDefault() {
        return this.calliopeProperties.getProperty("robot.program.default");
    }

    @Override
    public String getConfigurationToolbox() {
        return this.calliopeProperties.getProperty("robot.configuration.toolbox");
    }

    @Override
    public String getConfigurationDefault() {
        return this.calliopeProperties.getProperty("robot.configuration.default");
    }

    @Override
    public String getRealName() {
        return this.calliopeProperties.getProperty("robot.real.name");
    }

    @Override
    public Boolean hasSim() {
        return this.calliopeProperties.getProperty("robot.sim").equals("true") ? true : false;
    }

    @Override
    public String getInfo() {
        return this.calliopeProperties.getProperty("robot.info") != null ? this.calliopeProperties.getProperty("robot.info") : "#";
    }

    @Override
    public Boolean isBeta() {
        return this.calliopeProperties.getProperty("robot.beta") != null ? true : false;
    }

    @Override
    public String getConnectionType() {
        return this.calliopeProperties.getProperty("robot.connection");
    }

    @Override
    public RobotSimulationCheckVisitor getSimProgramCheckVisitor(Configuration brickConfiguration) {
        return new SimulationCheckVisitor(brickConfiguration);
    }

    @Override
    public Boolean hasConfiguration() {
        return Boolean.parseBoolean(this.calliopeProperties.getProperty("robot.configuration"));
    }

    @Override
    public String getGroup() {
        return this.robertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".group") != null
            ? this.robertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".group")
            : this.name;
    }

    @Override
    public RobotCommonCheckVisitor getRobotProgramCheckVisitor(Configuration brickConfiguration) {
        return new BoardCheckVisitor(brickConfiguration);
    }

}