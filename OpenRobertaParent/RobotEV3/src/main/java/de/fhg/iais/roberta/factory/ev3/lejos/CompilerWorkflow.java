package de.fhg.iais.roberta.factory.ev3.lejos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ev3.EV3Configuration;
import de.fhg.iais.roberta.components.ev3.JavaSourceCompiler;
import de.fhg.iais.roberta.factory.AbstractCompilerWorkflow;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.syntax.codegen.ev3.JavaVisitor;
import de.fhg.iais.roberta.transformer.BlocklyProgramAndConfigTransformer;
import de.fhg.iais.roberta.transformer.ev3.Jaxb2Ev3ConfigurationTransformer;
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
        return JavaVisitor.generate(programName, (EV3Configuration) data.getBrickConfiguration(), data.getProgramTransformer().getTree(), true, language);
    }

    @Override
    public Key compileSourceCode(String token, String programName, String sourceCode, ILanguage language, Object flagProvider) {
        //Ev3CompilerWorkflow.LOG.info("generated code:\n{}", sourceCode); // only needed for EXTREME debugging
        try {
            storeGeneratedProgram(token, programName, sourceCode, this.pathToCrosscompilerBaseDir, ".java");
        } catch ( Exception e ) {
            CompilerWorkflow.LOG.error("Storing the generated program into directory " + token + " failed", e);
            return Key.COMPILERWORKFLOW_ERROR_PROGRAM_STORE_FAILED;
        }

        JavaSourceCompiler scp = new JavaSourceCompiler(programName, sourceCode, this.crossCompilerResourcesDir);
        boolean isSuccess = scp.compileAndPackage(this.pathToCrosscompilerBaseDir, token);
        if ( !isSuccess ) {
            LOG.error("build exception. Messages from the build script are:\n" + scp.getCompilationMessages());
            return Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
        } else {
            LOG.info("jar for program {} generated successfully", programName);
            return Key.COMPILERWORKFLOW_SUCCESS;
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
