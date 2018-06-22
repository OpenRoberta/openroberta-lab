package de.fhg.iais.roberta.factory.arduino.botnroll;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.SystemUtils;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.arduino.BotNrollConfiguration;
import de.fhg.iais.roberta.factory.AbstractRobotFactory;
import de.fhg.iais.roberta.factory.ICompilerWorkflow;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.action.IShowPicture;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.mode.action.ActorPort;
import de.fhg.iais.roberta.mode.sensor.SensorPort;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.program.RobotCommonCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.RobotSimulationCheckVisitor;
import de.fhg.iais.roberta.syntax.codegen.arduino.botnroll.CppVisitor;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;

public class Factory extends AbstractRobotFactory {
    private final CompilerWorkflow compilerWorkflow;
    private final Properties botnrollProperties;
    private final String name;
    private final int robotPropertyNumber;
    Map<String, SensorPort> sensorToPorts = IRobotFactory.getSensorPortsFromProperties(Util1.loadProperties("classpath:botnrollports.properties"));
    Map<String, ActorPort> actorToPorts = IRobotFactory.getActorPortsFromProperties(Util1.loadProperties("classpath:botnrollports.properties"));

    public Factory(RobertaProperties robertaProperties) {
        super(robertaProperties);
        String os = "linux";
        if ( SystemUtils.IS_OS_WINDOWS ) {
            os = "windows";
        }
        this.botnrollProperties = Util1.loadProperties("classpath:botnroll.properties");
        this.name = this.botnrollProperties.getProperty("robot.name");
        this.robotPropertyNumber = robertaProperties.getRobotNumberFromProperty(this.name);
        this.compilerWorkflow =
            new CompilerWorkflow(
                robertaProperties.getTempDirForUserProjects(),
                robertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".compiler.resources.dir"),
                robertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".compiler." + os + ".dir"));
        addBlockTypesFromProperties("botnroll.properties", this.botnrollProperties);
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
    public ICompilerWorkflow getRobotCompilerWorkflow() {
        return this.compilerWorkflow;
    }

    @Override
    public ICompilerWorkflow getSimCompilerWorkflow() {
        return null;
    }

    @Override
    public String getFileExtension() {
        return "ino";
    }

    @Override
    public String getProgramToolboxBeginner() {
        return this.botnrollProperties.getProperty("robot.program.toolbox.beginner");
    }

    @Override
    public String getProgramToolboxExpert() {
        return this.botnrollProperties.getProperty("robot.program.toolbox.expert");
    }

    @Override
    public String getProgramDefault() {
        return this.botnrollProperties.getProperty("robot.program.default");
    }

    @Override
    public String getConfigurationToolbox() {
        return this.botnrollProperties.getProperty("robot.configuration.toolbox");
    }

    @Override
    public String getConfigurationDefault() {
        return this.botnrollProperties.getProperty("robot.configuration.default");
    }

    @Override
    public String getRealName() {
        return this.botnrollProperties.getProperty("robot.real.name");
    }

    @Override
    public Boolean hasSim() {
        return this.botnrollProperties.getProperty("robot.sim").equals("true") ? true : false;
    }

    @Override
    public String getInfo() {
        return this.botnrollProperties.getProperty("robot.info") != null ? this.botnrollProperties.getProperty("robot.info") : "#";
    }

    @Override
    public Boolean isBeta() {
        return this.botnrollProperties.getProperty("robot.beta") != null ? true : false;
    }

    @Override
    public String getConnectionType() {
        return this.botnrollProperties.getProperty("robot.connection");
    }

    @Override
    public String getVendorId() {
        return this.botnrollProperties.getProperty("robot.vendor");
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
        return Boolean.parseBoolean(this.botnrollProperties.getProperty("robot.configuration"));
    }

    @Override
    public String getGroup() {
        return this.robertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".group") != null
            ? this.robertaProperties.getStringProperty("robot.plugin." + this.robotPropertyNumber + ".group")
            : this.name;
    }

    @Override
    public String generateCode(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) {
        return CppVisitor.generate((BotNrollConfiguration) brickConfiguration, phrasesSet, withWrapping);
    }

    @Override
    public String getCommandline() {
        return this.botnrollProperties.getProperty("robot.connection.commandLine");
    }

    @Override
    public String getSignature() {
        return this.botnrollProperties.getProperty("robot.connection.signature");
    }

    @Override
    public ISensorPort getSensorPort(String port) {
        return getSensorPortValue(port, this.sensorToPorts);
    }
}
