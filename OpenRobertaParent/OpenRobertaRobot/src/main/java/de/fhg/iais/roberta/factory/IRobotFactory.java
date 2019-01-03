package de.fhg.iais.roberta.factory;

import java.util.ArrayList;

import de.fhg.iais.roberta.codegen.ICompilerWorkflow;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.visitor.validate.AbstractProgramValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractSimValidatorVisitor;

public interface IRobotFactory {

    public BlocklyDropdownFactory getBlocklyDropdownFactory();

    /**
     * Get the compiler workflow object for this robot.
     *
     * @return
     */
    ICompilerWorkflow getRobotCompilerWorkflow();

    /**
     * Get the compiler workflow object for the simulation of this robot.
     *
     * @return
     */
    ICompilerWorkflow getSimCompilerWorkflow();

    /**
     * Get the file extension of the specific language for this robot. This is used when we want to download locally the source code into a file.
     */
    String getFileExtension();

    String getProgramToolboxBeginner();

    String getProgramToolboxExpert();

    String getProgramDefault();

    String getConfigurationToolbox();

    String getConfigurationDefault();

    String getRealName();

    Boolean hasSim();
    
    Boolean hasMultipleSim();

    String getInfo();

    Boolean isBeta();

    String getConnectionType();

    default String getVendorId() {
        return null;
    }

    default String getCommandline() {
        return null;
    }

    default String getSignature() {
        return null;
    }

    Boolean hasConfiguration();

    AbstractSimValidatorVisitor getSimProgramCheckVisitor(Configuration brickConfiguration);

    AbstractProgramValidatorVisitor getRobotProgramCheckVisitor(Configuration brickConfiguration);

    String getGroup();

    String generateCode(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping);

    default String getMenuVersion() {
        return null;
    }
}
