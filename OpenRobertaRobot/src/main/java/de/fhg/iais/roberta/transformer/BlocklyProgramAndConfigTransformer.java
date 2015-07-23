package de.fhg.iais.roberta.transformer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.components.ev3.Ev3Configuration;
import de.fhg.iais.roberta.robotCommunication.ev3.Ev3CompilerWorkflow;
import de.fhg.iais.roberta.util.Key;

/**
 * This class stores the AST representation of the program and the brick configuration if transformation is successful, otherwise we have error message
 * 
 * @author kcvejoski
 */
public class BlocklyProgramAndConfigTransformer {
    private static final Logger LOG = LoggerFactory.getLogger(BlocklyProgramAndConfigTransformer.class);
    private Key errorMessage;
    private Jaxb2BlocklyProgramTransformer<Void> programTransformer;
    private Ev3Configuration brickConfiguration;

    private BlocklyProgramAndConfigTransformer(Key errorMessage, Jaxb2BlocklyProgramTransformer<Void> programTransformer, Ev3Configuration brickConfiguration) {
        super();
        this.errorMessage = errorMessage;
        this.programTransformer = programTransformer;
        this.brickConfiguration = brickConfiguration;
    }

    /**
     * @return the errorMessage
     */
    public Key getErrorMessage() {
        return this.errorMessage;
    }

    /**
     * @return the programTransformer
     */
    public Jaxb2BlocklyProgramTransformer<Void> getProgramTransformer() {
        return this.programTransformer;
    }

    /**
     * @return the brickConfiguration
     */
    public Ev3Configuration getBrickConfiguration() {
        return this.brickConfiguration;
    }

    /**
     * Transforms blockly xml program and brick configuration into AST.
     *
     * @param programText as XML
     * @param configurationText as XML
     * @return
     */
    public static BlocklyProgramAndConfigTransformer transform(String programText, String configurationText) {
        Key errorMessage = null;
        if ( programText == null || programText.trim().equals("") ) {
            errorMessage = Key.COMPILERWORKFLOW_ERROR_PROGRAM_NOT_FOUND;
        } else if ( configurationText == null || configurationText.trim().equals("") ) {
            errorMessage = Key.COMPILERWORKFLOW_ERROR_CONFIGURATION_NOT_FOUND;
        }

        Jaxb2BlocklyProgramTransformer<Void> programTransformer = null;
        try {
            programTransformer = Ev3CompilerWorkflow.generateProgramTransformer(programText);
        } catch ( Exception e ) {
            LOG.error("Transformer failed", e);
            errorMessage = Key.COMPILERWORKFLOW_ERROR_PROGRAM_TRANSFORM_FAILED;
        }
        Ev3Configuration brickConfiguration = null;
        try {
            brickConfiguration = Ev3CompilerWorkflow.generateConfiguration(configurationText);
        } catch ( Exception e ) {
            LOG.error("Generation of the configuration failed", e);
            errorMessage = Key.COMPILERWORKFLOW_ERROR_CONFIGURATION_TRANSFORM_FAILED;
        }
        return new BlocklyProgramAndConfigTransformer(errorMessage, programTransformer, brickConfiguration);
    }
}
