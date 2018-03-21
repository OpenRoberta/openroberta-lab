package de.fhg.iais.roberta.factory.nao;

import java.util.ArrayList;
import java.util.Properties;

import com.google.inject.AbstractModule;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.factory.AbstractRobotFactory;
import de.fhg.iais.roberta.factory.ICompilerWorkflow;
import de.fhg.iais.roberta.inter.mode.action.ILightSensorActionMode;
import de.fhg.iais.roberta.inter.mode.action.IShowPicture;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.program.RobotCommonCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.RobotSimulationCheckVisitor;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;

public class Factory extends AbstractRobotFactory {
    private final CompilerWorkflow compilerWorkflow;
    private final Properties naoProperties;
    private final String name;
    private final int robotPropertyNumber;

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
        return this.naoProperties.getProperty("robot.configuration") != null ? false : true;
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

    @Override
    public ILightSensorActionMode getLightActionColor(String mode) {
        return null;
    }
}
