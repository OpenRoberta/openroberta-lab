package de.fhg.iais.roberta.codegen;

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
import de.fhg.iais.roberta.components.arduino.ArduinoConfiguration;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.transformer.BlocklyProgramAndConfigTransformer;
import de.fhg.iais.roberta.transformers.arduino.Jaxb2ArduinoConfigurationTransformer;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;
import de.fhg.iais.roberta.visitor.codegen.ArduinoCppVisitor;

public class ArduinoCompilerWorkflow extends AbstractCompilerWorkflow {

    private static final Logger LOG = LoggerFactory.getLogger(ArduinoCompilerWorkflow.class);
    private String compiledHex = "error";

    // public ArduinoCompilerWorkflow(String pathToCrosscompilerBaseDir, String robotCompilerResourcesDir, String robotCompilerDir, String robot) {
    public ArduinoCompilerWorkflow(PluginProperties pluginProperties) {
        super(pluginProperties);
    }

    @Override
    public void generateSourceCode(String token, String programName, BlocklyProgramAndConfigTransformer data, ILanguage language) {
        if ( data.getErrorMessage() != null ) {
            workflowResult = Key.COMPILERWORKFLOW_ERROR_PROGRAM_TRANSFORM_FAILED;
            return;
        }
        try {
            ArduinoConfiguration configuration = ((ArduinoConfiguration) data.getBrickConfiguration());
            generatedSourceCode = ArduinoCppVisitor.generate(configuration, data.getProgramTransformer().getTree(), true);
            LOG.info("arduino c++ code generated");
        } catch ( Exception e ) {
            LOG.error("arduino c++ code generation failed", e);
            workflowResult = Key.COMPILERWORKFLOW_ERROR_PROGRAM_GENERATION_FAILED;
        }
    }

    @Override
    public void compileSourceCode(String token, String programName, ILanguage language, Object flagProvider) {
        storeGeneratedProgram(token, programName, ".ino");
        if ( workflowResult == Key.COMPILERWORKFLOW_SUCCESS ) {
            workflowResult = runBuild(token, programName, "generated.main");
            if ( workflowResult == Key.COMPILERWORKFLOW_SUCCESS ) {
                LOG.info("compile arduino program {} successful", programName);
            } else {
                LOG.error("compile arduino program {} failed with {}", programName, workflowResult);
            }
        }
    }

    @Override
    public Configuration generateConfiguration(IRobotFactory factory, String blocklyXml) throws Exception {
        BlockSet project = JaxbHelper.xml2BlockSet(blocklyXml);
        Jaxb2ArduinoConfigurationTransformer transformer = new Jaxb2ArduinoConfigurationTransformer(factory);
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
        final String robotName = pluginProperties.getRobotName();
        final String compilerBinDir = pluginProperties.getCompilerBinDir();
        final String compilerResourcesDir = pluginProperties.getCompilerResourceDir();
        final String tempDir = pluginProperties.getTempDir();

        final StringBuilder sb = new StringBuilder();
        String scriptName = "";
        String os = "";
        System.out.println(System.getProperty("os.arch"));
        if ( SystemUtils.IS_OS_LINUX ) {
            if ( System.getProperty("os.arch").contains("arm") ) {
                scriptName = compilerResourcesDir + "/linux-arm/arduino-builder";
                os = "linux-arm";
            } else {
                scriptName = compilerResourcesDir + "/linux/arduino-builder";
                os = "linux";
            }
        } else if ( SystemUtils.IS_OS_WINDOWS ) {
            scriptName = compilerResourcesDir + "/windows/arduino-builder.exe";
            os = "windows";
        } else if ( SystemUtils.IS_OS_MAC ) {
            scriptName = compilerResourcesDir + "/osx/arduino-builder";
            os = "osx";
        }
        Path path = Paths.get(tempDir + token + "/" + mainFile);
        Path base = Paths.get("");

        try {
            String fqbnArg = "";
            if ( robotName.equals("uno") ) {
                fqbnArg = "-fqbn=arduino:avr:uno";
            } else if ( robotName.equals("mega") ) {
                fqbnArg = "-fqbn=arduino:avr:mega:cpu=atmega2560";
            } else if ( robotName.equals("nano") ) {
                fqbnArg = "-fqbn=arduino:avr:nano:cpu=atmega328";
            }

            ProcessBuilder procBuilder =
                new ProcessBuilder(
                    new String[] {
                        scriptName,
                        "-hardware=" + compilerResourcesDir + "/hardware",
                        "-tools=" + compilerResourcesDir + "/" + os + "/tools-builder",
                        "-libraries=" + compilerResourcesDir + "/libraries",
                        fqbnArg,
                        "-prefs=compiler.path=" + compilerBinDir,
                        "-build-path=" + base.resolve(path).toAbsolutePath().normalize().toString() + "/target/",
                        base.resolve(path).toAbsolutePath().normalize().toString() + "/source/" + mainFile + ".ino"
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
                LOG.error("build exception. Messages from the build script are:\n" + sb.toString(), e);
            } else {
                LOG.error("exception when preparing the build", e);
            }
            return Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
        }
    }
}
