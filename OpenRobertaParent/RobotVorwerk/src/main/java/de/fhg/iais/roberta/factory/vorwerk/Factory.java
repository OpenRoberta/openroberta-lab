package de.fhg.iais.roberta.factory.vorwerk;

import java.util.ArrayList;
import java.util.Properties;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.factory.AbstractRobotFactory;
import de.fhg.iais.roberta.factory.ICompilerWorkflow;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.ILightSensorActionMode;
import de.fhg.iais.roberta.inter.mode.action.IShowPicture;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.mode.action.vorwerk.ShowPicture;
import de.fhg.iais.roberta.mode.sensor.SensorPort;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.program.RobotCommonCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.RobotSimulationCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.vorwerk.BrickCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.vorwerk.SimulationCheckVisitor;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;

public class Factory extends AbstractRobotFactory {
    private static final String ROBOT_PLUGIN_PREFIX = "robot.plugin.";
    protected ICompilerWorkflow robotCompilerWorkflow;
    protected Properties vorwerkProperties;
    protected String name;

    public Factory(RobertaProperties robertaProperties) {
        super(robertaProperties);
        this.vorwerkProperties = Util1.loadProperties("classpath:Vorwerk.properties");
        this.name = this.vorwerkProperties.getProperty("robot.name");
        this.robotPropertyNumber = robertaProperties.getRobotNumberFromProperty(this.name);
        this.robotCompilerWorkflow = new CompilerWorkflow(robertaProperties.getTempDirForUserProjects());

        addBlockTypesFromProperties("Vorwerk.property", this.vorwerkProperties);

    }

    protected int robotPropertyNumber;

    @Override
    public IShowPicture getShowPicture(String picture) {
        return IRobotFactory.getModeValue(picture, ShowPicture.class);
    }

    @Override
    public ISensorPort getSensorPort(String port) {
        return IRobotFactory.getModeValue(port, SensorPort.class);
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
    public String getProgramToolboxBeginner() {
        return this.vorwerkProperties.getProperty("robot.program.toolbox.beginner");
    }

    @Override
    public String getProgramToolboxExpert() {
        return this.vorwerkProperties.getProperty("robot.program.toolbox.expert");
    }

    @Override
    public String getProgramDefault() {
        return this.vorwerkProperties.getProperty("robot.program.default");
    }

    @Override
    public String getConfigurationToolbox() {
        return this.vorwerkProperties.getProperty("robot.configuration.toolbox");
    }

    @Override
    public String getConfigurationDefault() {
        return this.vorwerkProperties.getProperty("robot.configuration.default");
    }

    @Override
    public String getRealName() {
        return this.vorwerkProperties.getProperty("robot.real.name");
    }

    @Override
    public Boolean hasSim() {
        return this.vorwerkProperties.getProperty("robot.sim").equals("true") ? true : false;
    }

    @Override
    public String getInfo() {
        return this.vorwerkProperties.getProperty("robot.info") != null ? this.vorwerkProperties.getProperty("robot.info") : "#";
    }

    @Override
    public Boolean isBeta() {
        return this.vorwerkProperties.getProperty("robot.beta") != null ? true : false;
    }

    @Override
    public String getConnectionType() {
        return this.vorwerkProperties.getProperty("robot.connection");
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
        return this.vorwerkProperties.getProperty("robot.configuration") != null ? false : true;
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
    public ILightSensorActionMode getLightActionColor(String mode) {
        return null;
    }

    @Override
    public ICompilerWorkflow getSimCompilerWorkflow() {
        return null;
    }
}
