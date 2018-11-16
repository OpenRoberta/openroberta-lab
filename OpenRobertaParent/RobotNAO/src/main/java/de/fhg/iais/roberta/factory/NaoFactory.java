package de.fhg.iais.roberta.factory;

import java.util.ArrayList;

import de.fhg.iais.roberta.codegen.ICompilerWorkflow;
import de.fhg.iais.roberta.codegen.NaoCompilerWorkflow;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.mode.action.Language;
import de.fhg.iais.roberta.mode.sensor.DetectFaceSensorMode;
import de.fhg.iais.roberta.mode.sensor.DetectMarkSensorMode;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.visitor.codegen.NaoPythonVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractProgramValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractSimValidatorVisitor;

public class NaoFactory extends AbstractRobotFactory {

    public NaoFactory(PluginProperties pluginProperties) {
        super(pluginProperties);
    }

    public IMode getDetectMarkMode(String mode) {
        return BlocklyDropdownFactoryHelper.getModeValue(mode, DetectMarkSensorMode.class);
    }

    public IMode getDetectFaceMode(String mode) {
        return BlocklyDropdownFactoryHelper.getModeValue(mode, DetectFaceSensorMode.class);
    }

    @Override
    public String getFileExtension() {
        return "py";
    }

    @Override
    public ICompilerWorkflow getRobotCompilerWorkflow() {
        return new NaoCompilerWorkflow(this.pluginProperties);
    }

    @Override
    public ICompilerWorkflow getSimCompilerWorkflow() {
        return null;
    }

    @Override
    public AbstractSimValidatorVisitor getSimProgramCheckVisitor(Configuration brickConfiguration) {
        return null;
    }

    @Override
    public AbstractProgramValidatorVisitor getRobotProgramCheckVisitor(Configuration brickConfiguration) {
        return null;
    }

    @Override
    public String generateCode(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) {
        return NaoPythonVisitor.generate(brickConfiguration, phrasesSet, withWrapping, Language.GERMAN);
    }

}
