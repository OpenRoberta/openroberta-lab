package de.fhg.iais.roberta.factory.arduino.botnroll;

import java.io.File;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.arduino.BotNrollConfiguration;
import de.fhg.iais.roberta.factory.AbstractCompilerWorkflow;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.syntax.codegen.arduino.botnroll.CppVisitor;
import de.fhg.iais.roberta.transformer.BlocklyProgramAndConfigTransformer;
import de.fhg.iais.roberta.transformers.arduino.Jaxb2BotNrollConfigurationTransformer;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;

public class CompilerWorkflow extends AbstractCompilerWorkflow {

    private static final Logger LOG = LoggerFactory.getLogger(CompilerWorkflow.class);

    public final String pathToCrosscompilerBaseDir;
    public final String robotCompilerResourcesDir;
    public final String robotCompilerDir;

    private String compiledHex = "";

    public CompilerWorkflow(String pathToCrosscompilerBaseDir, String robotCompilerResourcesDir, String robotCompilerDir) {
        this.pathToCrosscompilerBaseDir = pathToCrosscompilerBaseDir;
        this.robotCompilerResourcesDir = robotCompilerResourcesDir;
        this.robotCompilerDir = robotCompilerDir;

    }

    @Override
    public String generateSourceCode(String token, String programName, BlocklyProgramAndConfigTransformer data, ILanguage language) {
        if ( data.getErrorMessage() != null ) {
            return null;
        }
        return CppVisitor.generate((BotNrollConfiguration) data.getBrickConfiguration(), data.getProgramTransformer().getTree(), true);
    }

    @Override
    public Key compileSourceCode(String token, String programName, String sourceCode, ILanguage language, Object flagProvider) {
        try {
            storeGeneratedProgram(token, programName, sourceCode, this.pathToCrosscompilerBaseDir, ".ino");
        } catch ( Exception e ) {
            CompilerWorkflow.LOG.error("Storing the generated program into directory " + token + " failed", e);
            return Key.COMPILERWORKFLOW_ERROR_PROGRAM_STORE_FAILED;
        }

        Key messageKey = runBuild(token, programName, "generated.main");
        if ( messageKey == Key.COMPILERWORKFLOW_SUCCESS ) {
            CompilerWorkflow.LOG.info("hex for program {} generated successfully", programName);
        } else {
            CompilerWorkflow.LOG.info(messageKey.toString());
        }
        return messageKey;
    }

    @Override
    public Configuration generateConfiguration(IRobotFactory factory, String blocklyXml) throws Exception {
        BlockSet project = JaxbHelper.xml2BlockSet(blocklyXml);
        Jaxb2BotNrollConfigurationTransformer transformer = new Jaxb2BotNrollConfigurationTransformer(factory);
        return transformer.transform(project);
    }

    @Override
    public String getCompiledCode() {
        return this.compiledHex;
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
    private Key runBuild(String token, String mainFile, String mainPackage) {
        final StringBuilder sb = new StringBuilder();

        String scriptName = this.robotCompilerResourcesDir + "/linux/arduino-builder";
        String os = "linux";

        if ( SystemUtils.IS_OS_WINDOWS ) {
            scriptName = this.robotCompilerResourcesDir + "/windows/arduino-builder.exe";
            os = "windows";
        } else if ( SystemUtils.IS_OS_MAC ) {
            scriptName = this.robotCompilerResourcesDir + "/osx/arduino-builder";
            os = "osx";
        }

        Path path = Paths.get(this.pathToCrosscompilerBaseDir + token + "/" + mainFile);
        Path base = Paths.get("");

        try {
            ProcessBuilder procBuilder =
                new ProcessBuilder(
                    new String[] {
                        scriptName,
                        "-hardware=" + this.robotCompilerResourcesDir + "/hardware",
                        "-tools=" + this.robotCompilerResourcesDir + "/" + os + "/tools-builder",
                        "-libraries=" + this.robotCompilerResourcesDir + "/libraries",
                        "-fqbn=arduino:avr:uno",
                        "-prefs=compiler.path=" + this.robotCompilerDir,
                        "-build-path=" + base.resolve(path).toAbsolutePath().normalize().toString() + "/target/",
                        base.resolve(path).toAbsolutePath().normalize().toString() + "/src/" + mainFile + ".ino"
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
            this.compiledHex = FileUtils.readFileToString(new File(path + "/target/" + mainFile + ".ino.hex"), "UTF-8");
            Base64.Encoder urec = Base64.getEncoder();
            this.compiledHex = urec.encodeToString(this.compiledHex.getBytes());
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
