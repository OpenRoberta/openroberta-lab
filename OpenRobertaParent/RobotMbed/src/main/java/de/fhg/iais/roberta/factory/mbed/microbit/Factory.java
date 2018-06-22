package de.fhg.iais.roberta.factory.mbed.microbit;

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
import de.fhg.iais.roberta.syntax.check.program.mbed.microbit.SimulationCheckVisitor;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;

public class Factory extends AbstractRobotFactory {

    private final CompilerWorkflow compilerWorkflow;
    private final SimCompilerWorkflow microbitSimCompilerWorkflow;
    private final Properties microbitProperties;
    private final String name;
    private final int robotPropertyNumber;
    Map<String, SensorPort> sensorToPorts = IRobotFactory.getSensorPortsFromProperties(Util1.loadProperties("classpath:Microbitports.properties"));
    Map<String, ActorPort> actorToPorts = IRobotFactory.getActorPortsFromProperties(Util1.loadProperties("classpath:Microbitports.properties"));

    public Factory(RobertaProperties robertaProperties) {
        super(robertaProperties);
        this.microbitProperties = Util1.loadProperties("classpath:Microbit.properties");
        this.name = this.microbitProperties.getProperty("robot.name");
        this.robotPropertyNumber = robertaProperties.getRobotNumberFromProperty(this.name);
        this.compilerWorkflow =
            new CompilerWorkflow(
                robertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".compiler.resources.dir"),
                robertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".compiler.dir"));
        this.microbitSimCompilerWorkflow = new SimCompilerWorkflow();
        Properties mbedProperties = Util1.loadProperties("classpath:Mbed.properties");
        addBlockTypesFromProperties("Mbed.properties", mbedProperties);
        addBlockTypesFromProperties("Microbit.properties", this.microbitProperties);
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
    public String getFileExtension() {
        return "py";
    }

    @Override
    public ICompilerWorkflow getRobotCompilerWorkflow() {
        return this.compilerWorkflow;
    }

    @Override
    public ICompilerWorkflow getSimCompilerWorkflow() {
        return this.microbitSimCompilerWorkflow;
    }

    @Override
    public String getProgramToolboxBeginner() {
        return this.microbitProperties.getProperty("robot.program.toolbox.beginner");
    }

    @Override
    public String getProgramToolboxExpert() {
        return this.microbitProperties.getProperty("robot.program.toolbox.expert");
    }

    @Override
    public String getProgramDefault() {
        return this.microbitProperties.getProperty("robot.program.default");
    }

    @Override
    public String getConfigurationToolbox() {
        return this.microbitProperties.getProperty("robot.configuration.toolbox");
    }

    @Override
    public String getConfigurationDefault() {
        return this.microbitProperties.getProperty("robot.configuration.default");
    }

    @Override
    public String getRealName() {
        return this.microbitProperties.getProperty("robot.real.name");
    }

    @Override
    public Boolean hasSim() {
        return this.microbitProperties.getProperty("robot.sim").equals("true") ? true : false;
    }

    @Override
    public String getInfo() {
        return this.microbitProperties.getProperty("robot.info") != null ? this.microbitProperties.getProperty("robot.info") : "#";
    }

    @Override
    public Boolean isBeta() {
        return this.microbitProperties.getProperty("robot.beta") != null ? true : false;
    }

    @Override
    public String getConnectionType() {
        return this.microbitProperties.getProperty("robot.connection");
    }

    @Override
    public RobotSimulationCheckVisitor getSimProgramCheckVisitor(Configuration brickConfiguration) {
        return new SimulationCheckVisitor(brickConfiguration);
    }

    @Override
    public RobotCommonCheckVisitor getRobotProgramCheckVisitor(Configuration brickConfiguration) {
        return new BoardCheckVisitor(brickConfiguration);
    }

    @Override
    public Boolean hasConfiguration() {
        return Boolean.parseBoolean(this.microbitProperties.getProperty("robot.configuration"));
    }

    @Override
    public String getGroup() {
        return this.microbitProperties.getProperty("robot.group") != null ? this.microbitProperties.getProperty("robot.group") : this.name;
    }

    @Override
    public String generateCode(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) {
        // TODO Auto-generated method stub
        return null;
    }

}
