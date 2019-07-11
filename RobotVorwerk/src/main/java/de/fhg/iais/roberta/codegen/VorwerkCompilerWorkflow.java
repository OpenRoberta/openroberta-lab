package de.fhg.iais.roberta.codegen;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.vorwerk.VorwerkCommunicator;
import de.fhg.iais.roberta.components.vorwerk.VorwerkConfiguration;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.transformer.BlocklyProgramAndConfigTransformer;
import de.fhg.iais.roberta.transformer.vorwerk.Jaxb2VorwerkConfigurationTransformer;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;
import de.fhg.iais.roberta.visitor.codegen.VorwerkPythonVisitor;

public class VorwerkCompilerWorkflow extends AbstractCompilerWorkflow {
    private static final Logger LOG = LoggerFactory.getLogger(VorwerkCompilerWorkflow.class);

    private final VorwerkCommunicator vorwerkCommunicator;

    public VorwerkCompilerWorkflow(PluginProperties pluginProperties) {
        super(pluginProperties);
        this.vorwerkCommunicator = new VorwerkCommunicator(pluginProperties.getCompilerResourceDir());
    }

    @Override
    public void generateSourceCode(String token, String programName, BlocklyProgramAndConfigTransformer data, ILanguage language) {
        if ( data.getErrorMessage() != null ) {
            this.workflowResult = Key.COMPILERWORKFLOW_ERROR_PROGRAM_TRANSFORM_FAILED;
            return;
        }
        try {
            VorwerkConfiguration configuration = (VorwerkConfiguration) data.getRobotConfiguration();
            this.vorwerkCommunicator.setCredentials(configuration.getIpAddress(), configuration.getUserName(), configuration.getPassword());
            this.generatedSourceCode =
                VorwerkPythonVisitor.generate((VorwerkConfiguration) data.getRobotConfiguration(), data.getProgramTransformer().getTree(), true, language);
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
                this.vorwerkCommunicator.uploadFile(programLocation, programName + ".py");
                this.workflowResult = Key.COMPILERWORKFLOW_SUCCESS;
            } catch ( Exception e ) {
                LOG.error("Uploading the generated program to " + this.vorwerkCommunicator.getIp() + " failed", e.getCause());
                this.workflowResult = Key.VORWERK_PROGRAM_UPLOAD_ERROR;
            }
        }
    }

    @Override
    public Configuration generateConfiguration(IRobotFactory factory, String blocklyXml) throws Exception {
        BlockSet project = JaxbHelper.xml2BlockSet(blocklyXml);
        Jaxb2VorwerkConfigurationTransformer transformer = new Jaxb2VorwerkConfigurationTransformer(factory);
        return transformer.transform(project);
    }

    @Override
    public String getCompiledCode() {
        throw new DbcException("Operation not supported");
    }
}
