package de.fhg.iais.roberta.codegen;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.raspberrypi.RaspberryPiCommunicator;
import de.fhg.iais.roberta.components.raspberrypi.RaspberryPiConfiguration;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.transformer.BlocklyProgramAndConfigTransformer;
import de.fhg.iais.roberta.transformer.raspberrypi.Jaxb2RaspberryPiConfigurationTransformer;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;
import de.fhg.iais.roberta.visitor.codegen.RaspberryPiPythonVisitor;

public class RaspberryPiCompilerWorkflow extends AbstractCompilerWorkflow {
    private static final Logger LOG = LoggerFactory.getLogger(RaspberryPiCompilerWorkflow.class);

    private final RaspberryPiCommunicator communicator;

    public RaspberryPiCompilerWorkflow(PluginProperties pluginProperties) {
        super(pluginProperties);
        this.communicator = new RaspberryPiCommunicator(pluginProperties);
    }

    @Override
    public void generateSourceCode(String token, String programName, BlocklyProgramAndConfigTransformer data, ILanguage language) {

        if ( data.getErrorMessage() != null ) {
            this.workflowResult = Key.COMPILERWORKFLOW_ERROR_PROGRAM_TRANSFORM_FAILED;
            return;
        }
        try {
            this.generatedSourceCode =
                RaspberryPiPythonVisitor
                    .generate((RaspberryPiConfiguration) data.getRobotConfiguration(), data.getProgramTransformer().getTree(), true, language);
            LOG.info("vorwerk code generated");
        } catch ( Exception e ) {
            LOG.error("vorwerk code generation failed", e);
            this.workflowResult = Key.COMPILERWORKFLOW_ERROR_PROGRAM_GENERATION_FAILED;
        }
    }

    @Override
    public void compileSourceCode(String token, String programName, ILanguage language, Object flagProvider) {
        storeGeneratedProgram(token, programName, ".py");
        if ( this.workflowResult == Key.COMPILERWORKFLOW_SUCCESS ) {
            try {
                final String tempDir = this.pluginProperties.getTempDir();
                String programLocation = tempDir + token + File.separator + programName + File.separator + "source";
                this.communicator.uploadFile(programLocation, programName);
                this.workflowResult = Key.COMPILERWORKFLOW_SUCCESS;
            } catch ( Exception e ) {
                String ip = this.pluginProperties.getStringProperty("raspi." + programName + ".ip");
                LOG.error("Uploading the generated program to " + ip + " failed", e.getMessage());
                this.workflowResult = Key.RASPBERRY_PROGRAM_UPLOAD_ERROR;
            }
        }
    }

    @Override
    public Configuration generateConfiguration(IRobotFactory factory, String blocklyXml) throws Exception {
        BlockSet project = JaxbHelper.xml2BlockSet(blocklyXml);
        Jaxb2RaspberryPiConfigurationTransformer transformer = new Jaxb2RaspberryPiConfigurationTransformer(factory);
        return transformer.transform(project);
    }

    @Override
    public String getCompiledCode() {
        return null;
    }
}
