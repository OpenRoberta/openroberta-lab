package de.fhg.iais.roberta.codegen;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ev3.EV3Configuration;
import de.fhg.iais.roberta.components.ev3.JavaSourceCompiler;
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
            workflowResult = Key.COMPILERWORKFLOW_ERROR_PROGRAM_TRANSFORM_FAILED;
            return;
        }
        try {
            generatedSourceCode =
                Ev3JavaVisitor.generate(programName, (EV3Configuration) data.getBrickConfiguration(), data.getProgramTransformer().getTree(), true, language);
            LOG.info("ev3lejos java code generated");
        } catch ( Exception e ) {
            LOG.error("ev3lejos java code generation failed", e);
            workflowResult = Key.COMPILERWORKFLOW_ERROR_PROGRAM_GENERATION_FAILED;
        }
    }

    @Override
    public void compileSourceCode(String token, String programName, ILanguage language, Object flagProvider) {
        storeGeneratedProgram(token, programName, ".java");
        if ( workflowResult == Key.COMPILERWORKFLOW_SUCCESS ) {
            final String compilerResourcesDir = pluginProperties.getCompilerResourceDir();
            final String tempDir = pluginProperties.getTempDir();

            JavaSourceCompiler scp = new JavaSourceCompiler(programName, generatedSourceCode, compilerResourcesDir);
            boolean isSuccess = scp.compileAndPackage(tempDir, token);
            if ( !isSuccess ) {
                LOG.error("build exception. Messages from the build script are:\n" + scp.getCompilationMessages());
                workflowResult = Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
            } else {
                LOG.info("jar for program {} generated successfully", programName);
                workflowResult = Key.COMPILERWORKFLOW_SUCCESS;
            }
        }
    }

    @Override
    public Configuration generateConfiguration(IRobotFactory factory, String blocklyXml) throws Exception {
        BlockSet project = JaxbHelper.xml2BlockSet(blocklyXml);
        Jaxb2Ev3ConfigurationTransformer transformer = new Jaxb2Ev3ConfigurationTransformer(factory);
        return transformer.transform(project);
    }

    @Override
    public String getCompiledCode() {
        return null;
    }
}
