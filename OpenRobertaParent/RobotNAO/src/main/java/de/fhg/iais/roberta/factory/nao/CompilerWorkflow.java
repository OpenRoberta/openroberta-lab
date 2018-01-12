package de.fhg.iais.roberta.factory.nao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.nao.NAOConfiguration;
import de.fhg.iais.roberta.factory.AbstractCompilerWorkflow;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.syntax.codegen.nao.PythonVisitor;
import de.fhg.iais.roberta.transformer.BlocklyProgramAndConfigTransformer;
import de.fhg.iais.roberta.transformer.nao.Jaxb2NaoConfigurationTransformer;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;

public class CompilerWorkflow extends AbstractCompilerWorkflow {

    private static final Logger LOG = LoggerFactory.getLogger(CompilerWorkflow.class);
    public final String pathToCrosscompilerBaseDir;
    public final String crossCompilerResourcesDir;

    public CompilerWorkflow(String pathToCrosscompilerBaseDir, String crossCompilerResourcesDir) {
        this.pathToCrosscompilerBaseDir = pathToCrosscompilerBaseDir;
        this.crossCompilerResourcesDir = crossCompilerResourcesDir;

    }

    @Override
    public String generateSourceCode(String token, String programName, BlocklyProgramAndConfigTransformer data, ILanguage language) {
        if ( data.getErrorMessage() != null ) {
            return null;
        }
        return generateProgram(programName, data, language);
    }

    @Override
    public Key compileSourceCode(String token, String programName, String sourceCode, ILanguage language, Object flagProvider) {
        //Ev3CompilerWorkflow.LOG.info("generated code:\n{}", sourceCode); // only needed for EXTREME debugging
        try {
            storeGeneratedProgram(token, programName, sourceCode, this.pathToCrosscompilerBaseDir, ".py");
        } catch ( final Exception e ) {
            CompilerWorkflow.LOG.error("Storing the generated program into directory " + token + " failed", e);
            return Key.COMPILERWORKFLOW_ERROR_PROGRAM_STORE_FAILED;
        }

        // maybe copy from /src/ to /target/
        // python -c "import py_compile; py_compile.compile('.../src/...py','.../target/....pyc')"
        return Key.COMPILERWORKFLOW_SUCCESS;

    }

    @Override
    public Configuration generateConfiguration(IRobotFactory factory, String blocklyXml) throws Exception {
        final BlockSet project = JaxbHelper.xml2BlockSet(blocklyXml);
        final Jaxb2NaoConfigurationTransformer transformer = new Jaxb2NaoConfigurationTransformer(factory);
        return transformer.transform(project);
    }

    @Override
    public String getCompiledCode() {
        return null;
    }

    private String generateProgram(String programName, BlocklyProgramAndConfigTransformer data, ILanguage language) {
        String sourceCode = PythonVisitor.generate((NAOConfiguration) data.getBrickConfiguration(), data.getProgramTransformer().getTree(), true, language);
        CompilerWorkflow.LOG.info("generating {} code", toString().toLowerCase());
        return sourceCode;
    }

}
