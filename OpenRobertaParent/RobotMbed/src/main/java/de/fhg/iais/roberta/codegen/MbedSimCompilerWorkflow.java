package de.fhg.iais.roberta.codegen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.transformer.BlocklyProgramAndConfigTransformer;
import de.fhg.iais.roberta.transformer.mbed.Jaxb2CalliopeConfigurationTransformer;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;
import de.fhg.iais.roberta.visitor.codegen.MbedSimVisitor;

public class MbedSimCompilerWorkflow extends AbstractCompilerWorkflow {
    private static final Logger LOG = LoggerFactory.getLogger(MbedSimCompilerWorkflow.class);

    public MbedSimCompilerWorkflow(PluginProperties pluginProperties) {
        super(pluginProperties);
    }

    @Override
    public void generateSourceCode(String token, String programName, BlocklyProgramAndConfigTransformer data, ILanguage language) {
        if ( data.getErrorMessage() != null ) {
            workflowResult = Key.COMPILERWORKFLOW_ERROR_PROGRAM_TRANSFORM_FAILED;
            return;
        }
        try {
            generatedSourceCode = MbedSimVisitor.generate(data.getBrickConfiguration(), data.getProgramTransformer().getTree());
            LOG.info("javascript mbed simulation code generated");
        } catch ( Exception e ) {
            LOG.error("javascript mbed simulation code generation failed", e);
            workflowResult = Key.COMPILERWORKFLOW_ERROR_PROGRAM_GENERATION_FAILED;
        }
    }

    @Override
    public void compileSourceCode(String token, String programName, ILanguage language, Object flagProvider) {
        throw new DbcException("Operation not supported");
    }

    @Override
    public void generateSourceAndCompile(String token, String programName, BlocklyProgramAndConfigTransformer transformer, ILanguage language) {
        throw new DbcException("Operation not supported");
    }

    @Override
    public Configuration generateConfiguration(IRobotFactory factory, String blocklyXml) throws Exception {
        BlockSet project = JaxbHelper.xml2BlockSet(blocklyXml);
        Jaxb2CalliopeConfigurationTransformer transformer = new Jaxb2CalliopeConfigurationTransformer(factory);
        return transformer.transform(project);
    }

    @Override
    public String getCompiledCode() {
        return null;
    }
}
