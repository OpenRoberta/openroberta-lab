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
    public String generateSourceCode(String token, String programName, BlocklyProgramAndConfigTransformer data, ILanguage language) {
        if ( data.getErrorMessage() != null ) {
            return null;
        }
        return MicrobitPythonVisitor.generate((MicrobitConfiguration) data.getBrickConfiguration(), data.getProgramTransformer().getTree(), true);
    }

    @Override
    public Key compileSourceCode(String token, String programName, String sourceCode, ILanguage language, Object flagProvider) {
        Key messageKey = runBuild(sourceCode);
        if ( messageKey == Key.COMPILERWORKFLOW_SUCCESS ) {
            MicrobitCompilerWorkflow.LOG.info("hex for program {} generated successfully", programName);
        } else {
            MicrobitCompilerWorkflow.LOG.info(messageKey.toString());
        }
        return messageKey;
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
