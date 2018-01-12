package de.fhg.iais.roberta.factory.nxt;

import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.nxt.NxtConfiguration;
import de.fhg.iais.roberta.factory.AbstractCompilerWorkflow;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.syntax.codegen.nxt.NxcVisitor;
import de.fhg.iais.roberta.transformer.BlocklyProgramAndConfigTransformer;
import de.fhg.iais.roberta.transformer.nxt.Jaxb2NxtConfigurationTransformer;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;

public class CompilerWorkflow extends AbstractCompilerWorkflow {

    private static final Logger LOG = LoggerFactory.getLogger(CompilerWorkflow.class);

    public final String pathToCrosscompilerBaseDir;
    public final String robotCompilerResourcesDir;

    public CompilerWorkflow(String pathToCrosscompilerBaseDir, String robotCompilerResourcesDir) {
        this.pathToCrosscompilerBaseDir = pathToCrosscompilerBaseDir;
        this.robotCompilerResourcesDir = robotCompilerResourcesDir;
    }

    @Override
    public String generateSourceCode(String token, String programName, BlocklyProgramAndConfigTransformer data, ILanguage language) //
    {
        if ( data.getErrorMessage() != null ) {
            return null;
        }
        return NxcVisitor.generate((NxtConfiguration) data.getBrickConfiguration(), data.getProgramTransformer().getTree(), true);
    }

    @Override
    public Key compileSourceCode(String token, String programName, String sourceCode, ILanguage language, Object flagProvider) {
        try {
            storeGeneratedProgram(token, programName, sourceCode, this.pathToCrosscompilerBaseDir, ".nxc");
        } catch ( Exception e ) {
            CompilerWorkflow.LOG.error("Storing the generated program into directory " + token + " failed", e);
            return Key.COMPILERWORKFLOW_ERROR_PROGRAM_STORE_FAILED;
        }

        Key messageKey = runBuild(token, programName, "generated.main");
        if ( messageKey == Key.COMPILERWORKFLOW_SUCCESS ) {
            CompilerWorkflow.LOG.info("rxc for program {} generated successfully", programName);
        } else {
            CompilerWorkflow.LOG.info(messageKey.toString());
        }
        return messageKey;
    }

    @Override
    public Configuration generateConfiguration(IRobotFactory factory, String blocklyXml) throws Exception {
        BlockSet project = JaxbHelper.xml2BlockSet(blocklyXml);
        Jaxb2NxtConfigurationTransformer transformer = new Jaxb2NxtConfigurationTransformer(factory);
        return transformer.transform(project);
    }

    @Override
    public String getCompiledCode() {
        return null;
    }

    /**
     * 1. Make target folder (if not exists).<br>
     * 2. Clean target folder (everything inside).<br>
     * 3. Compile .java files to .class.<br>
     * 4. Make jar from class files and add META-INF entries.<br>
     *
     * @param token
     * @param mainFile
     * @param mainPackage
     */
    Key runBuild(String token, String mainFile, String mainPackage) {
        final StringBuilder sb = new StringBuilder();

        Path path = Paths.get(this.robotCompilerResourcesDir);
        Path base = Paths.get("");

        String nbcCompilerFileName = this.robotCompilerResourcesDir + "/windows/nbc.exe";
        if ( SystemUtils.IS_OS_LINUX ) {
            nbcCompilerFileName = "nbc";
        } else if ( SystemUtils.IS_OS_MAC ) {
            nbcCompilerFileName = this.robotCompilerResourcesDir + "/osx/nbc";
        }

        try {
            ProcessBuilder procBuilder =
                new ProcessBuilder(
                    new String[] {
                        nbcCompilerFileName,
                        "-q",
                        this.pathToCrosscompilerBaseDir + token + "/" + mainFile + "/src/" + mainFile + ".nxc",
                        "-O=" + this.pathToCrosscompilerBaseDir + token + "/" + mainFile + "/target/" + mainFile + ".rxe",
                        "-I=" + base.resolve(path).toAbsolutePath().normalize().toString()
                    });
            procBuilder.redirectInput(Redirect.INHERIT);
            procBuilder.redirectOutput(Redirect.INHERIT);
            procBuilder.redirectError(Redirect.INHERIT);
            Process p = procBuilder.start();
            int ecode = p.waitFor();
            System.err.println("Exit code " + ecode);

            if ( ecode != 0 ) {
                return Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
            }
            return Key.COMPILERWORKFLOW_SUCCESS;
        } catch ( Exception e ) {
            if ( sb.length() > 0 ) {
                CompilerWorkflow.LOG.error("build exception. Messages from the build script are:\n" + sb.toString(), e);
            } else {
                CompilerWorkflow.LOG.error("exception when preparing the build", e);
            }
            return Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
        }
    }
}
