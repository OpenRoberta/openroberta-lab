package de.fhg.iais.roberta.factory.ev3.lejos;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import com.google.inject.AbstractModule;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.factory.AbstractRobotFactory;
import de.fhg.iais.roberta.factory.ICompilerWorkflow;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.factory.ev3.Ev3GuiceModule;
import de.fhg.iais.roberta.factory.ev3.Ev3SimCompilerWorkflow;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.action.IShowPicture;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.mode.action.ActorPort;
import de.fhg.iais.roberta.mode.action.ev3.ShowPicture;
import de.fhg.iais.roberta.mode.sensor.SensorPort;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.program.RobotCommonCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.RobotSimulationCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.ev3.BrickCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.ev3.SimulationCheckVisitor;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;

public abstract class EV3AbstractFactory extends AbstractRobotFactory {
    private static final String ROBOT_PLUGIN_PREFIX = "robot.plugin.";
    protected Ev3SimCompilerWorkflow simCompilerWorkflow;
    protected ICompilerWorkflow robotCompilerWorkflow;
    protected Properties ev3Properties;
    protected String name;
    protected int robotPropertyNumber;
    Map<String, SensorPort> sensorToPorts = IRobotFactory.getSensorPortsFromProperties(Util1.loadProperties("classpath:EV3ports.properties"));
    Map<String, ActorPort> actorToPorts = IRobotFactory.getActorPortsFromProperties(Util1.loadProperties("classpath:EV3ports.properties"));

    public EV3AbstractFactory(RobertaProperties robertaProperties, String propertyName) {
        super(robertaProperties);
        this.ev3Properties = Util1.loadProperties("classpath:" + propertyName);
        this.name = this.ev3Properties.getProperty("robot.name");
        this.robotPropertyNumber = robertaProperties.getRobotNumberFromProperty(this.name);
        this.robotCompilerWorkflow =
            new CompilerWorkflow(
                robertaProperties.getTempDirForUserProjects(),
                robertaProperties.getStringProperty(ROBOT_PLUGIN_PREFIX + this.robotPropertyNumber + ".compiler.resources.dir"));

        this.simCompilerWorkflow = new Ev3SimCompilerWorkflow();

        addBlockTypesFromProperties(propertyName, this.ev3Properties);

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
        return IRobotFactory.getModeValue(picture, ShowPicture.class);
    }

    @Override
    public ICompilerWorkflow getSimCompilerWorkflow() {
        return this.simCompilerWorkflow;
    }

    @Override
    public ICompilerWorkflow getRobotCompilerWorkflow() {
        return this.robotCompilerWorkflow;
    }

    @Override
    public AbstractModule getGuiceModule() {
        return new Ev3GuiceModule(this.robertaProperties.getRobertaProperties());
    }

    @Override
    public String getFileExtension() {
        return "java";
    }

    @Override
    public String getProgramToolboxBeginner() {
        return this.ev3Properties.getProperty("robot.program.toolbox.beginner");
    }

    @Override
    public String getProgramToolboxExpert() {
        return this.ev3Properties.getProperty("robot.program.toolbox.expert");
    }

    @Override
    public String getProgramDefault() {
        return this.ev3Properties.getProperty("robot.program.default");
    }

    @Override
    public String getConfigurationToolbox() {
        return this.ev3Properties.getProperty("robot.configuration.toolbox");
    }

    @Override
    public String getConfigurationDefault() {
        return this.ev3Properties.getProperty("robot.configuration.default");
    }

    @Override
    public String getRealName() {
        return this.ev3Properties.getProperty("robot.real.name");
    }

    @Override
    public Boolean hasSim() {
        return this.ev3Properties.getProperty("robot.sim").equals("true") ? true : false;
    }

    @Override
    public String getInfo() {
        return this.ev3Properties.getProperty("robot.info") != null ? this.ev3Properties.getProperty("robot.info") : "#";
    }

    @Override
    public Boolean isBeta() {
        return this.ev3Properties.getProperty("robot.beta") != null ? true : false;
    }

    @Override
    public String getConnectionType() {
        return this.ev3Properties.getProperty("robot.connection");
    }

    @Override
    public RobotSimulationCheckVisitor getSimProgramCheckVisitor(Configuration brickConfiguration) {
        return new SimulationCheckVisitor(brickConfiguration);
    }

    @Override
    public RobotCommonCheckVisitor getRobotProgramCheckVisitor(Configuration brickConfiguration) {
        return new BrickCheckVisitor(brickConfiguration);
    }

    @Override
    public Boolean hasConfiguration() {
        return Boolean.parseBoolean(this.ev3Properties.getProperty("robot.configuration"));
    }

    @Override
    public String getGroup() {
        return this.robertaProperties.getStringProperty(ROBOT_PLUGIN_PREFIX + this.robotPropertyNumber + ".group") != null
            ? this.robertaProperties.getStringProperty(ROBOT_PLUGIN_PREFIX + this.robotPropertyNumber + ".group")
            : this.name;
    }

    @Override
    public String generateCode(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) {
        return null;
    }

    @Override
    public String getMenuVersion() {
        return this.ev3Properties.getProperty("robot.menu.verision");
    }
}
