package de.fhg.iais.roberta.codegen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.transformer.BlocklyProgramAndConfigTransformer;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.visitor.codegen.NaoPythonVisitor;

public class NaoCompilerWorkflow extends AbstractCompilerWorkflow {

    private static final Logger LOG = LoggerFactory.getLogger(NaoCompilerWorkflow.class);

    private final HelperMethodGenerator helperMethodGenerator; // TODO pull up to abstract compiler workflow once implemented for all robots

    public NaoCompilerWorkflow(PluginProperties pluginProperties, HelperMethodGenerator helperMethodGenerator) {
        super(pluginProperties);
        this.helperMethodGenerator = helperMethodGenerator;
    }

    @Override
    public void generateSourceCode(String token, String programName, BlocklyProgramAndConfigTransformer data, ILanguage language) {
        if ( data.getErrorMessage() != null ) {
            this.workflowResult = Key.COMPILERWORKFLOW_ERROR_PROGRAM_TRANSFORM_FAILED;
            return;
        }
        try {
            this.generatedSourceCode = generateProgram(programName, data, language);
            LOG.info("nao code generated");
        } catch ( Exception e ) {
            LOG.error("nao code generation failed", e);
            this.workflowResult = Key.COMPILERWORKFLOW_ERROR_PROGRAM_GENERATION_FAILED;
        }
    }

    @Override
    public void compileSourceCode(String token, String programName, ILanguage language, Object flagProvider) {
        storeGeneratedProgram(token, programName, ".py");
    }

    @Override
    public Configuration generateConfiguration(IRobotFactory factory, String blocklyXml) throws Exception {
        return new Configuration.Builder().build();
    }

    @Override
    public String getCompiledCode() {
        return null;
    }

    private String generateProgram(String programName, BlocklyProgramAndConfigTransformer data, ILanguage language) {
        String sourceCode =
            NaoPythonVisitor.generate(data.getRobotConfiguration(), data.getProgramTransformer().getTree(), true, language, this.helperMethodGenerator);
        LOG.info("generating {} code", toString().toLowerCase());
        return sourceCode;
    }

}
