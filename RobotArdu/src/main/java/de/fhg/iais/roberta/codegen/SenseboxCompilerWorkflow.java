package de.fhg.iais.roberta.codegen;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.ILanguage;
import de.fhg.iais.roberta.transformer.BlocklyProgramAndConfigTransformer;
import de.fhg.iais.roberta.transformers.arduino.Jaxb2ArduinoConfigurationTransformer;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;
import de.fhg.iais.roberta.visitor.codegen.SenseboxCppVisitor;

public class SenseboxCompilerWorkflow extends AbstractCompilerWorkflow {

    private static final Logger LOG = LoggerFactory.getLogger(SenseboxCompilerWorkflow.class);
    private String binaryInBase64;

    // public ArduinoCompilerWorkflow(String pathToCrosscompilerBaseDir, String robotCompilerResourcesDir, String robotCompilerDir, String robot) {
    public SenseboxCompilerWorkflow(PluginProperties pluginProperties) {
        super(pluginProperties);
    }

    @Override
    public void generateSourceCode(String token, String programName, BlocklyProgramAndConfigTransformer data, ILanguage language) {
        if ( data.getErrorMessage() != null ) {
            this.workflowResult = Key.COMPILERWORKFLOW_ERROR_PROGRAM_TRANSFORM_FAILED;
            return;
        }
        try {
            Configuration configuration = data.getRobotConfiguration();
            this.generatedSourceCode = SenseboxCppVisitor.generate(configuration, data.getProgramTransformer().getTree(), true);
            LOG.info("senseBox c++ code generated");
        } catch ( Exception e ) {
            LOG.error("senseBox c++ code generation failed", e);
            this.workflowResult = Key.COMPILERWORKFLOW_ERROR_PROGRAM_GENERATION_FAILED;
        }
    }

    @Override
    public void generateSourceCode(
        String token,
        String programName,
        BlocklyProgramAndConfigTransformer data,
        String SSID,
        String password,
        ILanguage language) {
        if ( data.getErrorMessage() != null ) {
            this.workflowResult = Key.COMPILERWORKFLOW_ERROR_PROGRAM_TRANSFORM_FAILED;
            return;
        }
        try {
            Configuration configuration = data.getRobotConfiguration();
            this.generatedSourceCode = SenseboxCppVisitor.generate(configuration, data.getProgramTransformer().getTree(), SSID, password, true);
            LOG.info("senseBox c++ code generated");
        } catch ( Exception e ) {
            LOG.error("senseBox c++ code generation failed", e);
            this.workflowResult = Key.COMPILERWORKFLOW_ERROR_PROGRAM_GENERATION_FAILED;
        }
    }

    @Override
    public void compileSourceCode(String token, String programName, ILanguage language, Object flagProvider) {
        this.storeGeneratedProgram(token, programName, ".ino");
        if ( this.workflowResult == Key.COMPILERWORKFLOW_SUCCESS ) {
            this.workflowResult = this.runBuild(token, programName, "generated.main");
            if ( this.workflowResult == Key.COMPILERWORKFLOW_SUCCESS ) {
                LOG.info("compile arduino program {} successful", programName);
            } else {
                LOG.error("compile arduino program {} failed with {}", programName, this.workflowResult);
            }
        }
    }

    @Override
    public Configuration generateConfiguration(IRobotFactory factory, String blocklyXml) throws Exception {
        BlockSet project = JaxbHelper.xml2BlockSet(blocklyXml);
        Jaxb2ArduinoConfigurationTransformer transformer = new Jaxb2ArduinoConfigurationTransformer(factory.getBlocklyDropdownFactory());
        return transformer.transform(project);
    }

    @Override
    public String getCompiledCode() {
        return this.binaryInBase64;
    }

    /**
     * create command to call the cross compiler and execute the call.
     *
     * @return Key.COMPILERWORKFLOW_SUCCESS or Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED
     */
    private Key runBuild(String token, String mainFile, String mainPackage) {
        final String compilerBinDir = this.pluginProperties.getCompilerBinDir();
        final String compilerResourcesDir = this.pluginProperties.getCompilerResourceDir();
        final String tempDir = this.pluginProperties.getTempDir();

        String scriptName = "";
        String os = "";
        if ( SystemUtils.IS_OS_LINUX ) {
            if ( System.getProperty("os.arch").contains("arm") ) {
                scriptName = compilerResourcesDir + "arduino-builder/linux-arm/arduino-builder";
                os = "arduino-builder/linux-arm";
            } else {
                scriptName = compilerResourcesDir + "arduino-builder/linux/arduino-builder";
                os = "arduino-builder/linux";
            }
        } else if ( SystemUtils.IS_OS_WINDOWS ) {
            scriptName = compilerResourcesDir + "arduino-builder/windows/arduino-builder.exe";
            os = "arduino-builder/windows";
        } else if ( SystemUtils.IS_OS_MAC ) {
            scriptName = compilerResourcesDir + "arduino-builder/osx/arduino-builder";
            os = "arduino-builder/osx";
        }
        String fqbnArg = "-fqbn=sensebox:samd:sb:power=on";
        Path path = Paths.get(tempDir + token + "/" + mainFile);
        Path base = Paths.get("");

        String[] executableWithParameters =
            new String[] {
                scriptName,
                "-hardware=" + compilerResourcesDir + "hardware/builtin",
                "-hardware=" + compilerResourcesDir + "hardware/additional",
                "-tools=" + compilerResourcesDir + "/" + os + "/tools-builder",
                "-tools=" + compilerResourcesDir + "hardware/additional",
                "-libraries=" + compilerResourcesDir + "/libraries",
                fqbnArg,
                "-prefs=compiler.path=" + compilerBinDir,
                "-vid-pid=0X04D8_0XEF66",
                "-ide-version=10805",
                "-prefs=build.warn_data_percentage=75",
                "-prefs=runtime.tools.arduinoOTA.path=" + compilerResourcesDir + "hardware/additional/arduino/tools/arduinoOTA/1.2.0",
                "-prefs=runtime.tools.CMSIS.path=" + compilerResourcesDir + "hardware/additional/arduino/tools/CMSIS/4.5.0",
                "-prefs=runtime.tools.CMSIS-Atmel.path=" + compilerResourcesDir + "hardware/additional/arduino/tools/CMSIS-Atmel/1.1.0",
                "-prefs=runtime.tools.openocd.path=" + compilerResourcesDir + "hardware/additional/arduino/tools/openocd/0.9.0-arduino6-static",
                "-prefs=runtime.tools.arm-none-eabi-gcc.path=" + compilerResourcesDir + "hardware/additional/arduino/tools/arm-none-eabi-gcc/4.8.3-2014q1",
                "-prefs=runtime.tools.bossac.path=" + compilerResourcesDir + "hardware/additional/arduino/tools/bossac/1.7.0",

                "-build-path=" + base.resolve(path).toAbsolutePath().normalize().toString() + "/target/",
                base.resolve(path).toAbsolutePath().normalize().toString() + "/source/" + mainFile + ".ino"
            };
        boolean success = runCrossCompiler(executableWithParameters);
        if ( success ) {
            this.binaryInBase64 = getBase64EncodedBinary(path + "/target/" + mainFile + ".ino.bin");
            return this.binaryInBase64 == null ? Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED : Key.COMPILERWORKFLOW_SUCCESS;
        } else {
            return Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
        }
    }
}
