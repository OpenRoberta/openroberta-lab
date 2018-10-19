package de.fhg.iais.roberta.codegen;

import java.lang.ProcessBuilder.Redirect;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.mbed.MicrobitConfiguration;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.transformer.BlocklyProgramAndConfigTransformer;
import de.fhg.iais.roberta.transformer.mbed.Jaxb2MicrobitConfigurationTransformer;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;
import de.fhg.iais.roberta.visitor.codegen.MicrobitPythonVisitor;

public class MicrobitCompilerWorkflow extends AbstractCompilerWorkflow {

    private static final Logger LOG = LoggerFactory.getLogger(MicrobitCompilerWorkflow.class);

    private String compiledHex = "";

    public MicrobitCompilerWorkflow(PluginProperties pluginProperties) {
        super(pluginProperties);
    }

    @Override
    public void generateSourceCode(String token, String programName, BlocklyProgramAndConfigTransformer data, ILanguage language) {
        if ( data.getErrorMessage() != null ) {
            workflowResult = Key.COMPILERWORKFLOW_ERROR_PROGRAM_TRANSFORM_FAILED;
            return;
        }
        try {
            generatedSourceCode =
                MicrobitPythonVisitor.generate((MicrobitConfiguration) data.getBrickConfiguration(), data.getProgramTransformer().getTree(), true);
            LOG.info("microbit python code generated");
        } catch ( Exception e ) {
            LOG.error("microbit python code generation failed", e);
            workflowResult = Key.COMPILERWORKFLOW_ERROR_PROGRAM_GENERATION_FAILED;
        }
    }

    @Override
    public void compileSourceCode(String token, String programName, ILanguage language, Object flagProvider) {
        workflowResult = runBuild(generatedSourceCode);
        if ( workflowResult == Key.COMPILERWORKFLOW_SUCCESS ) {
            LOG.info("compile microbit program {} successful", programName);
        } else {
            LOG.error("compile microbit program {} failed with {}", programName, workflowResult);
        }
    }

    @Override
    public Configuration generateConfiguration(IRobotFactory factory, String blocklyXml) throws Exception {
        BlockSet project = JaxbHelper.xml2BlockSet(blocklyXml);
        Jaxb2MicrobitConfigurationTransformer transformer = new Jaxb2MicrobitConfigurationTransformer(factory);
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
        final String compilerBinDir = pluginProperties.getCompilerBinDir();
        final String compilerResourcesDir = pluginProperties.getCompilerResourceDir();

        final StringBuilder sb = new StringBuilder();

        String scriptName = compilerResourcesDir + "/compile.py";

        try {
            ProcessBuilder procBuilder =
                new ProcessBuilder(
                    new String[] {
                        compilerBinDir + "python",
                        scriptName,
                        sourceCode
                    });

            procBuilder.redirectInput(Redirect.INHERIT);
            procBuilder.redirectError(Redirect.INHERIT);
            Process p = procBuilder.start();

            this.compiledHex = IOUtils.toString(p.getInputStream(), "US-ASCII");
            return Key.COMPILERWORKFLOW_SUCCESS;
        } catch ( Exception e ) {
            if ( sb.length() > 0 ) {
                MicrobitCompilerWorkflow.LOG.error("build exception. Messages from the build script are:\n" + sb.toString(), e);
            } else {
                MicrobitCompilerWorkflow.LOG.error("exception when preparing the build", e);
            }
            return Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
        }
    }

}
