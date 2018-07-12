package de.fhg.iais.roberta.factory.wedo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.wedo.WeDoConfiguration;
import de.fhg.iais.roberta.factory.AbstractCompilerWorkflow;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.syntax.codegen.wedo.WeDoStackMachineVisitor;
import de.fhg.iais.roberta.transformer.BlocklyProgramAndConfigTransformer;
import de.fhg.iais.roberta.transformer.wedo.Jaxb2WeDoConfigurationTransformer;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;

public class WeDoCompilerWorkflow extends AbstractCompilerWorkflow {

    private static final Logger LOG = LoggerFactory.getLogger(WeDoCompilerWorkflow.class);

    private String compiledStackOps = "error";

    public WeDoCompilerWorkflow() {
    }

    @Override
    public String generateSourceCode(String token, String programName, BlocklyProgramAndConfigTransformer data, ILanguage language) //
    {
        if ( data.getErrorMessage() != null ) {
            return null;
        }
        try {
            String sourceCode = WeDoStackMachineVisitor.generate((WeDoConfiguration) data.getBrickConfiguration(), data.getProgramTransformer().getTree());
            WeDoCompilerWorkflow.LOG.info("generating javascript code");
            return sourceCode;
        } catch ( Exception e ) {
            LOG.error("generating source code failed", e);
            return null;
        }
    }

    @Override
    public Key compileSourceCode(String token, String programName, String sourceCode, ILanguage language, Object flagProvider) {
        this.compiledStackOps = sourceCode;
        return sourceCode == null ? Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED : Key.COMPILERWORKFLOW_SUCCESS;
    }

    @Override
    public Configuration generateConfiguration(IRobotFactory factory, String blocklyXml) throws Exception {
        BlockSet project = JaxbHelper.xml2BlockSet(blocklyXml);
        Jaxb2WeDoConfigurationTransformer transformer = new Jaxb2WeDoConfigurationTransformer(factory);
        return transformer.transform(project);
    }

    @Override
    public String getCompiledCode() {
        return this.compiledStackOps;
    }
}