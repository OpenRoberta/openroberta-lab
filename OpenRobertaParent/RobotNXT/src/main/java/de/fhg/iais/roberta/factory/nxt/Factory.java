package de.fhg.iais.roberta.factory.nxt;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.factory.AbstractRobotFactory;
import de.fhg.iais.roberta.factory.ICompilerWorkflow;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.action.IShowPicture;
import de.fhg.iais.roberta.inter.mode.general.IPickColor;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.mode.action.ActorPort;
import de.fhg.iais.roberta.mode.general.nxt.PickColor;
import de.fhg.iais.roberta.mode.sensor.SensorPort;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.program.RobotCommonCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.RobotSimulationCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.nxt.BrickCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.nxt.SimulationCheckVisitor;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;

public class Factory extends AbstractRobotFactory {
    private final CompilerWorkflow robotCompilerWorkflow;
    private final SimCompilerWorkflow simCompilerWorkflow;
    private final Properties nxtProperties;
    private final String name;
    private final int robotPropertyNumber;
    Map<String, SensorPort> sensorToPorts = IRobotFactory.getSensorPortsFromProperties(Util1.loadProperties("classpath:NXTports.properties"));
    Map<String, ActorPort> actorToPorts = IRobotFactory.getActorPortsFromProperties(Util1.loadProperties("classpath:NXTports.properties"));

    public Factory(RobertaProperties robertaProperties) {
        super(robertaProperties);
        this.nxtProperties = Util1.loadProperties("classpath:NXT.properties");
        this.name = this.nxtProperties.getProperty("robot.name");
        this.robotPropertyNumber = robertaProperties.getRobotNumberFromProperty(this.name);
        this.robotCompilerWorkflow =
            new CompilerWorkflow(
                robertaProperties.getTempDirForUserProjects(),
                robertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".compiler.resources.dir"));
        this.simCompilerWorkflow = new SimCompilerWorkflow();
        addBlockTypesFromProperties("NXT.properties", this.nxtProperties);
    }

    @Override
    public IPickColor getPickColor(String color) {
        return IRobotFactory.getModeValue(color, PickColor.class);
    }

    @Override
    public IShowPicture getShowPicture(String picture) {
        return null;
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
    public String getFileExtension() {
        return "nxc";
    }

    @Override
    public ICompilerWorkflow getRobotCompilerWorkflow() {
        return this.robotCompilerWorkflow;
    }

    @Override
    public ICompilerWorkflow getSimCompilerWorkflow() {
        return this.simCompilerWorkflow;
    }

    @Override
    public String getProgramToolboxBeginner() {
        return this.nxtProperties.getProperty("robot.program.toolbox.beginner");
    }

    @Override
    public String getProgramToolboxExpert() {
        return this.nxtProperties.getProperty("robot.program.toolbox.expert");
    }

    @Override
    public String getProgramDefault() {
        return this.nxtProperties.getProperty("robot.program.default");
    }

    @Override
    public String getConfigurationToolbox() {
        return this.nxtProperties.getProperty("robot.configuration.toolbox");
    }

    @Override
    public String getConfigurationDefault() {
        return this.nxtProperties.getProperty("robot.configuration.default");
    }

    @Override
    public String getRealName() {
        return this.nxtProperties.getProperty("robot.real.name");
    }

    @Override
    public Boolean hasSim() {
        return this.nxtProperties.getProperty("robot.sim").equals("true") ? true : false;
    }

    @Override
    public String getInfo() {
        return this.nxtProperties.getProperty("robot.info") != null ? this.nxtProperties.getProperty("robot.info") : "#";
    }

    @Override
    public Boolean isBeta() {
        return this.nxtProperties.getProperty("robot.beta") != null ? true : false;
    }

    @Override
    public String getConnectionType() {
        return this.nxtProperties.getProperty("robot.connection");
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
        return Boolean.parseBoolean(this.nxtProperties.getProperty("robot.configuration"));
    }

    @Override
    public String getGroup() {
        return this.robertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".group") != null
            ? this.robertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".group")
            : this.name;
    }

    @Override
    public String generateCode(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) {
        return null;
    }

}
