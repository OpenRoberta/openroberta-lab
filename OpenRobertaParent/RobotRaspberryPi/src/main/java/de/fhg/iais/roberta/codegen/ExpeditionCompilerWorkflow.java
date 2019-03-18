package de.fhg.iais.roberta.codegen;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.expedition.ExpeditionCommunicator;
import de.fhg.iais.roberta.components.expedition.ExpeditionConfiguration;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.transformer.BlocklyProgramAndConfigTransformer;
import de.fhg.iais.roberta.transformer.expedition.Jaxb2ExpeditionConfigurationTransformer;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.ServerProperties;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;
import de.fhg.iais.roberta.visitor.codegen.ExpeditionPythonVisitor;

public class ExpeditionCompilerWorkflow extends AbstractCompilerWorkflow {
    private static final Logger LOG = LoggerFactory.getLogger(ExpeditionCompilerWorkflow.class);

    private final ExpeditionCommunicator expeditionCommunicator;

    public ExpeditionCompilerWorkflow(PluginProperties pluginProperties) {
        super(pluginProperties);
        this.expeditionCommunicator = new ExpeditionCommunicator(pluginProperties);
    }

    @Override
    public void generateSourceCode(String token, String programName, BlocklyProgramAndConfigTransformer data, ILanguage language) {

        if ( data.getErrorMessage() != null ) {
            workflowResult = Key.COMPILERWORKFLOW_ERROR_PROGRAM_TRANSFORM_FAILED;
            return;
        }
        try {
            ExpeditionConfiguration configuration = (ExpeditionConfiguration) data.getRobotConfiguration();
            generatedSourceCode =
                ExpeditionPythonVisitor
                    .generate((ExpeditionConfiguration) data.getRobotConfiguration(), data.getProgramTransformer().getTree(), true, language);
            LOG.info("vorwerk code generated");
        } catch ( Exception e ) {
            LOG.error("vorwerk code generation failed", e);
            workflowResult = Key.COMPILERWORKFLOW_ERROR_PROGRAM_GENERATION_FAILED;
        }
    }

    @Override
    public void compileSourceCode(String token, String programName, ILanguage language, Object flagProvider) {
        storeGeneratedProgram(token, programName, ".py");
        if ( workflowResult == Key.COMPILERWORKFLOW_SUCCESS ) {
            try {
                final String tempDir = pluginProperties.getTempDir();
                String programLocation = tempDir + token + File.separator + programName + File.separator + "source";
                this.expeditionCommunicator.uploadFile(programLocation, programName);
                workflowResult = Key.COMPILERWORKFLOW_SUCCESS;
            } catch ( Exception e ) {
                String ip = this.pluginProperties.getStringProperty("raspi." + programName + ".ip");
                LOG.error("Uploading the generated program to " + ip + " failed", e.getMessage());
                workflowResult = Key.EXPEDITION_PROGRAM_UPLOAD_ERROR;
            }
        }
    }

    @Override
    public Configuration generateConfiguration(IRobotFactory factory, String blocklyXml) throws Exception {
        BlockSet project = JaxbHelper.xml2BlockSet(blocklyXml);
        Jaxb2ExpeditionConfigurationTransformer transformer = new Jaxb2ExpeditionConfigurationTransformer(factory);
        return transformer.transform(project);
    }

    @Override
    public String getCompiledCode() {
        return null;
    }
}
