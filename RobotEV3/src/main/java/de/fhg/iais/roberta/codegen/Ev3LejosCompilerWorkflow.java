package de.fhg.iais.roberta.codegen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ev3lejos.JavaSourceCompiler;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.transformer.BlocklyProgramAndConfigTransformer;
import de.fhg.iais.roberta.transformer.ev3.Jaxb2Ev3ConfigurationTransformer;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;
import de.fhg.iais.roberta.visitor.codegen.Ev3JavaVisitor;

public class Ev3LejosCompilerWorkflow extends AbstractCompilerWorkflow {
    private static final Logger LOG = LoggerFactory.getLogger(Ev3LejosCompilerWorkflow.class);

    public Ev3LejosCompilerWorkflow(PluginProperties pluginProperties) {
        super(pluginProperties);
    }

    @Override
    public void generateSourceCode(String token, String programName, BlocklyProgramAndConfigTransformer data, ILanguage language) {
        if ( data.getErrorMessage() != null ) {
            this.workflowResult = Key.COMPILERWORKFLOW_ERROR_PROGRAM_TRANSFORM_FAILED;
            return;
        }
        try {
            this.generatedSourceCode =
                Ev3JavaVisitor.generate(programName, data.getRobotConfiguration(), data.getProgramTransformer().getTree(), true, language);
            LOG.info("ev3lejos java code generated");
        } catch ( Exception e ) {
            LOG.error("ev3lejos java code generation failed", e);
            this.workflowResult = Key.COMPILERWORKFLOW_ERROR_PROGRAM_GENERATION_FAILED;
        }
    }

    @Override
    public void compileSourceCode(String token, String programName, ILanguage language, Object flagProvider) {
        storeGeneratedProgram(token, programName, ".java");
        if ( this.workflowResult == Key.COMPILERWORKFLOW_SUCCESS ) {
            final String compilerResourcesDir = this.pluginProperties.getCompilerResourceDir();
            final String tempDir = this.pluginProperties.getTempDir();

            JavaSourceCompiler scp = new JavaSourceCompiler(programName, this.generatedSourceCode, compilerResourcesDir);
            scp.compileAndPackage(tempDir, token);
            this.crosscompilerResponse = scp.getCompilerResponse();
            if ( scp.isSuccess() ) {
                LOG.info("jar for program {} generated successfully", programName);
                this.workflowResult = Key.COMPILERWORKFLOW_SUCCESS;
            } else {
                LOG.error("build exception. Messages from the compiler are:\n" + scp.getCompilerResponse());
                this.workflowResult = Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
            }
        }
    }

    @Override
    public Configuration generateConfiguration(IRobotFactory factory, String blocklyXml) throws Exception {
        BlockSet project = JaxbHelper.xml2BlockSet(blocklyXml);
        Jaxb2Ev3ConfigurationTransformer transformer = new Jaxb2Ev3ConfigurationTransformer(factory.getBlocklyDropdownFactory());
        return transformer.transform(project);
    }

    @Override
    public String getCompiledCode() {
        return null;
    }
}
