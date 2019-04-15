package de.fhg.iais.roberta.factory;

import java.util.ArrayList;

import de.fhg.iais.roberta.codegen.Ev3LejosCompilerWorkflow;
import de.fhg.iais.roberta.codegen.Ev3SimCompilerWorkflow;
import de.fhg.iais.roberta.codegen.ICompilerWorkflow;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.inter.mode.action.IShowPicture;
import de.fhg.iais.roberta.mode.action.ev3.ShowPicture;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.visitor.validate.AbstractProgramValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractSimValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.Ev3BrickValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.Ev3SimValidatorVisitor;

public abstract class AbstractEV3Factory extends AbstractRobotFactory {
    protected String name;

    public AbstractEV3Factory(PluginProperties pluginProperties) {
        super(pluginProperties);
    }

    public IShowPicture getShowPicture(String picture) {
        return BlocklyDropdownFactoryHelper.getModeValue(picture, ShowPicture.class);
    }

    @Override
    public ICompilerWorkflow getSimCompilerWorkflow() {
        return new Ev3SimCompilerWorkflow(this.pluginProperties);
    }

    @Override
    public ICompilerWorkflow getRobotCompilerWorkflow() {
        return new Ev3LejosCompilerWorkflow(this.pluginProperties);
    }

    @Override
    public String getFileExtension() {
        return "java";
    }

    @Override
    public AbstractSimValidatorVisitor getSimProgramCheckVisitor(Configuration brickConfiguration) {
        return new Ev3SimValidatorVisitor(brickConfiguration);
    }

    @Override
    public AbstractProgramValidatorVisitor getRobotProgramCheckVisitor(Configuration brickConfiguration) {
        return new Ev3BrickValidatorVisitor(brickConfiguration);
    }

    @Override
    public String generateCode(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) {
        return null;
    }
}
