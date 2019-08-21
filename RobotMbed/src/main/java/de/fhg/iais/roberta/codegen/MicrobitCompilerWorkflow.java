package de.fhg.iais.roberta.codegen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.transformer.BlocklyProgramAndConfigTransformer;
import de.fhg.iais.roberta.transformer.mbed.Jaxb2MbedConfigurationAst;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;
import de.fhg.iais.roberta.visitor.codegen.MicrobitPythonVisitor;

public class MicrobitCompilerWorkflow extends AbstractCompilerWorkflow {

    private static final Logger LOG = LoggerFactory.getLogger(MicrobitCompilerWorkflow.class);

    private String compiledHex = "";

    private final HelperMethodGenerator helperMethodGenerator; // TODO pull up to abstract compiler workflow once implemented for all robots

    public MicrobitCompilerWorkflow(PluginProperties pluginProperties, HelperMethodGenerator helperMethodGenerator) {
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
            this.generatedSourceCode =
                MicrobitPythonVisitor.generate(data.getRobotConfiguration(), data.getProgramTransformer().getTree(), true, this.helperMethodGenerator);
            LOG.info("microbit python code generated");
        } catch ( Exception e ) {
            LOG.error("microbit python code generation failed", e);
            this.workflowResult = Key.COMPILERWORKFLOW_ERROR_PROGRAM_GENERATION_FAILED;
        }
    }

    @Override
    public void compileSourceCode(String token, String programName, ILanguage language, Object flagProvider) {
        this.workflowResult = runBuild(this.generatedSourceCode);
        if ( this.workflowResult == Key.COMPILERWORKFLOW_SUCCESS ) {
            LOG.info("compile microbit program {} successful", programName);
        } else {
            LOG.error("compile microbit program {} failed with {}", programName, this.workflowResult);
        }
    }

    @Override
    public Configuration generateConfiguration(IRobotFactory factory, String blocklyXml) throws Exception {
        BlockSet project = JaxbHelper.xml2BlockSet(blocklyXml);
        Jaxb2MbedConfigurationAst transformer = new Jaxb2MbedConfigurationAst(factory);
        return transformer.transform(project);
    }

    @Override
    public String getCompiledCode() {
        return this.compiledHex;
    }

    /**
     * run the build and create the complied hex file
     */
    Key runBuild(String sourceCode) {
        final String compilerBinDir = this.pluginProperties.getCompilerBinDir();
        final String compilerResourcesDir = this.pluginProperties.getCompilerResourceDir();

        final StringBuilder sb = new StringBuilder();

        String scriptName = compilerResourcesDir + "/compile.py";

        String[] executableWithParameters =
            new String[] {
                compilerBinDir + "python",
                scriptName,
                sourceCode
            };
        this.compiledHex = getBinaryFromCrossCompiler(executableWithParameters);
        return this.compiledHex != null ? Key.COMPILERWORKFLOW_SUCCESS : Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
    }

}
