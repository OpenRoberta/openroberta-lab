package de.fhg.iais.roberta.worker.compile;

import com.google.common.base.Charsets;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.fhg.iais.roberta.bean.CompilerSetupBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.ZipHelper;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.worker.IWorker;

public class ArduinoCompilerWorker implements IWorker {

    private static final Logger LOG = LoggerFactory.getLogger(ArduinoCompilerWorker.class);

    @Override
    public void execute(Project project) {
        CompilerSetupBean compilerWorkflowBean = project.getWorkerResult(CompilerSetupBean.class);
        String compilerResourcesDir = compilerWorkflowBean.getCompilerResourcesDir();
        String tempDir = compilerWorkflowBean.getTempDir();
        String programName = project.getProgramName();
        String token = project.getToken();
        final String crosscompilerSource = project.getSourceCode().toString();
        Util.storeGeneratedProgram(tempDir, crosscompilerSource, token, programName, "." + project.getSourceCodeFileExtension());
        String scriptName = compilerResourcesDir + "arduino-resources/build_project.sh";

        String boardVariant = "";
        String mmcu = "";
        String arduinoVariant = "";
        String arduinoArch = "";

        switch ( project.getRobot() ) {
            case "uno":
            case "nano":
                boardVariant = "standard";
                mmcu = "atmega328p";
                arduinoVariant = "ARDUINO_AVR_" + project.getRobot().toUpperCase();
                arduinoArch = "avr";
                break;
            case "mbot":
                boardVariant = "standard";
                mmcu = "atmega328p";
                arduinoVariant = "ARDUINO_AVR_UNO";
                scriptName = compilerResourcesDir + "arduino-resources/build_project_mbot.sh";
                arduinoArch = "avr";
                break;
            case "botnroll":
                boardVariant = "standard";
                mmcu = "atmega328p";
                arduinoVariant = "ARDUINO_AVR_UNO";
                scriptName = compilerResourcesDir + "arduino-resources/build_project_botnroll.sh";
                arduinoArch = "avr";
                break;
            case "unowifirev2":
                boardVariant = "uno2018";
                mmcu = "atmega4809";
                arduinoVariant = "ARDUINO_AVR_UNO_WIFI_REV2";
                scriptName = compilerResourcesDir + "arduino-resources/build_project_unowifirev2.sh";
                arduinoArch = "megaavr";
                break;
            case "mega":
                boardVariant = "mega";
                mmcu = "atmega2560";
                arduinoVariant = "ARDUINO_AVR_MEGA2560";
                arduinoArch = "avr";
                break;
            case "sensebox":
                boardVariant = "sensebox_mcu";
                arduinoVariant = "ARDUINO_SAMD_MKR1000";
                scriptName = compilerResourcesDir + "arduino-resources/build_project_sensebox.sh";
                arduinoArch = "samd";
                break;
            case "bob3":
                mmcu = "atmega88";
                scriptName = compilerResourcesDir + "arduino-resources/build_project_bob3.sh";
                break;
            case "festobionic":
                boardVariant = "esp32";
                arduinoVariant = "ARDUINO_ESP32_DEV";
                scriptName = compilerResourcesDir + "arduino-resources/build_project_festobionic.sh";
                arduinoArch = "esp32";
                break;
            case "nano33ble":
                boardVariant = "nano_33_iot";
                arduinoVariant = "ARDUINO_ARDUINO_NANO33BLE";
                scriptName = compilerResourcesDir + "arduino-resources/build_project_nano33ble.sh";
                arduinoArch = "mbed";
                break;
            default:
                throw new DbcException("Unsupported Arduino type: " + project.getRobot());
        }

        String sourceDir = tempDir + token + "/" + programName + "/source/";
        String targetDir = tempDir + token + "/" + programName + "/target/";

        String[] executableWithParameters =
            {
                scriptName,
                boardVariant,
                mmcu,
                arduinoVariant,
                sourceDir,
                programName,
                compilerResourcesDir,
                project.getRobot(),
                arduinoArch
            };
        Pair<Boolean, String> result = Util.runCrossCompiler(executableWithParameters, crosscompilerSource);
        Key resultKey = result.getFirst() ? Key.COMPILERWORKFLOW_SUCCESS : Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
        if ( result.getFirst() ) {
            String base64EncodedHex = null;
            switch ( project.getBinaryFileExtension() ) {
                case "hex":
                    base64EncodedHex = Util.getBase64EncodedHex(targetDir + programName + "." + project.getBinaryFileExtension());
                    break;
                case "bin":
                    base64EncodedHex = Util.getBase64EncodedBinary(targetDir + programName + "." + project.getBinaryFileExtension());
                    break;
                case "zip":
                    // as we currently only support sending a single file to robots and esp32 needs two files, they are zipped and send the zip is sent to the Connector
                    // the connector then unzips the files and correctly flashes them to the robot
                    // in order to circumvent large changes
                    try {
                        ZipHelper
                            .zipFiles(
                                Stream
                                    .concat(
                                        Files.walk(Paths.get(targetDir)).filter(Files::isRegularFile),
                                        Stream
                                            .of(
                                                Paths.get(compilerResourcesDir + "arduino-resources/hardware/esp32/esp32/tools/sdk/bin/bootloader_qio_80m.bin"),
                                                Paths.get(compilerResourcesDir + "arduino-resources/hardware/esp32/esp32/tools/partitions/boot_app0.bin")))
                                    .collect(Collectors.toList()),
                                Paths.get(targetDir, programName + "." + project.getBinaryFileExtension()));
                        base64EncodedHex =
                            FileUtils.readFileToString(Paths.get(targetDir + programName + "." + project.getBinaryFileExtension()).toFile(), Charsets.UTF_8);
                    } catch ( IOException e ) {
                        LOG.warn("The generated esp32 build files could not be zipped:", e);
                    }
                    break;
                default:
                    throw new DbcException("Unsupported file extension: " + project.getRobot());
            }
            project.setCompiledHex(base64EncodedHex);
            if ( project.getCompiledHex() != null ) {
                resultKey = Key.COMPILERWORKFLOW_SUCCESS;
            } else {
                resultKey = Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
            }
        }
        project.setResult(resultKey);
        project.addResultParam("MESSAGE", result.getSecond());
        String robot = project.getRobot();
        if ( resultKey == Key.COMPILERWORKFLOW_SUCCESS ) {
            LOG.info("compile {} program {} successful", robot, programName);
        } else {
            LOG.error("compile {} program {} failed with {}", robot, programName, result.getSecond());
        }
    }
}
