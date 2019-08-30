package de.fhg.iais.roberta.factory;

import java.util.ArrayList;

import de.fhg.iais.roberta.codegen.RaspberryPiCompilerWorkflow;
import de.fhg.iais.roberta.codegen.ICompilerWorkflow;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.visitor.validate.AbstractProgramValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractSimValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.RaspberryPiBrickValidatorVisitor;

public class RaspberryPiFactory extends AbstractRobotFactory {

    public RaspberryPiFactory(PluginProperties pluginProperties) {
        super(pluginProperties);

        this.helperMethodGenerator.addAdditionalEnum(RaspberryPiMethods.class);
    }

    @Override
    public ICompilerWorkflow getRobotCompilerWorkflow() {
        return new RaspberryPiCompilerWorkflow(this.pluginProperties, this.helperMethodGenerator);
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
        return new RaspberryPiBrickValidatorVisitor(brickConfiguration);
    }

    @Override
    public String generateCode(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) {
        return null;
    }

    @Override
    public ICompilerWorkflow getSimCompilerWorkflow() {
        return null;
    }

    public void setRaspiIP(String raspiProperty, String raspiIP) {
        this.pluginProperties.getPluginProperties().setProperty(raspiProperty, raspiIP);
    }
}
