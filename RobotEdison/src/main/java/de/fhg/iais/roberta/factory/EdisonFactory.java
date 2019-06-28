package de.fhg.iais.roberta.factory;

import de.fhg.iais.roberta.codegen.EdisonCompilerWorkflow;
import de.fhg.iais.roberta.codegen.ICompilerWorkflow;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.visitor.validate.AbstractProgramValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractSimValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.EdisonBrickValidatorVisitor;

import java.util.ArrayList;

public class EdisonFactory extends AbstractRobotFactory {

    /**
     * Creates a new instance of EdisonFactory by calling it's super() method
     *
     * @param pluginProperties the Edison robots properties
     */
    public EdisonFactory(PluginProperties pluginProperties) {
        super(pluginProperties);
    }

    /**
     * This robots compilation workflow for the properties
     *
     * @return the compiler workflow
     */
    @Override public ICompilerWorkflow getRobotCompilerWorkflow() {
        return new EdisonCompilerWorkflow(this.pluginProperties);
    }

    /**
     * If an online simulation is available, return the compilation workflow for for the simulation
     *
     * @return null, currently
     */
    @Override public ICompilerWorkflow getSimCompilerWorkflow() {
        return null; //online simulation not available
    }

    /**
     * returns the file extension for the generated code. Note that there is a difference between generated and compiled code.
     *
     * @return a String representing the file extension
     */
    @Override public String getFileExtension() {
        return "py";
    }

    /**
     * If a simulation is available, return the code validator for the blockly program
     *
     * @param brickConfiguration the robot configuration
     * @return null, currently
     */
    @Override public AbstractSimValidatorVisitor getSimProgramCheckVisitor(Configuration brickConfiguration) {
        return null;
    }

    /**
     * Return the validator for the Blockly program according to the robot configuration
     *
     * @param brickConfiguration the robot configuration
     * @return the visitor
     */
    @Override public AbstractProgramValidatorVisitor getRobotProgramCheckVisitor(Configuration brickConfiguration) {
        return new EdisonBrickValidatorVisitor(brickConfiguration);
    }

    /**
     * Unfortunately, I do not know (yet) what is method is exactly used for.
     *
     * @param brickConfiguration
     * @param phrasesSet
     * @param withWrapping
     * @return null, just like every other robot
     */
    @Override public String generateCode(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) {
        return null;
    }
}
