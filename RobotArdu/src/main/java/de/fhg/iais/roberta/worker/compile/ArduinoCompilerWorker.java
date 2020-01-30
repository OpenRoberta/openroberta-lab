package de.fhg.iais.roberta.worker.compile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.bean.CompilerSetupBean;
import de.fhg.iais.roberta.codegen.AbstractCompilerWorkflow;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.util.Util;
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
        Util.storeGeneratedProgram(tempDir, project.getSourceCode().toString(), token, programName, "." + project.getSourceCodeFileExtension());
        String scriptName = compilerResourcesDir + "arduino-resources/build_project.sh";
        String userProgramDirPath = tempDir + token + "/" + programName;

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
            default:
                throw new DbcException("Unsupported Arduino type: " + project.getRobot());
        }

        String buildDir = tempDir + token + "/" + programName + "/source";
        String arduinoDirName = project.getRobot();

        String[] executableWithParameters =
            {
                scriptName,
                boardVariant,
                mmcu,
                arduinoVariant,
                buildDir,
                programName,
                compilerResourcesDir,
                arduinoDirName,
                arduinoArch
            };
        Pair<Boolean, String> result = AbstractCompilerWorkflow.runCrossCompiler(executableWithParameters);
        Key resultKey = result.getFirst() ? Key.COMPILERWORKFLOW_SUCCESS : Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
        if ( result.getFirst() ) {
            String base64EncodedHex = null;
            switch ( project.getBinaryFileExtension() ) {
                case "hex":
                    base64EncodedHex =
                        AbstractCompilerWorkflow.getBase64EncodedHex(Paths.get(userProgramDirPath) + "/target/" + programName + "." + project.getBinaryFileExtension());
                    break;
                case "bin":
                    base64EncodedHex =
                        AbstractCompilerWorkflow.getBase64EncodedBinary(Paths.get(userProgramDirPath) + "/target/" + programName + "." + project.getBinaryFileExtension());
                    break;
                case "zip":
                    // as we currently only support sending a single file to robots and esp32 needs two files, they are zipped and send the zip is sent to the Connector
                    // the connector then unzips the files and correctly flashes them to the robot
                    // in order to circumvent large changes
                    Path path = Paths.get(tempDir + project.getToken() + "/" + project.getProgramName());
                    Path base = Paths.get("");
                    try (FileOutputStream fos = new FileOutputStream(base.resolve(path).toAbsolutePath().normalize() + "/target/" + project.getProgramName() + "." + project.getBinaryFileExtension());
                        ZipOutputStream zos = new ZipOutputStream(fos)) {
                        for ( File srcFile : base.resolve(path + "/target").toFile().listFiles() ) {
                            if (!srcFile.getName().endsWith(project.getBinaryFileExtension())) {
                                try (FileInputStream fis = new FileInputStream(srcFile)) {
                                    ZipEntry zipEntry = new ZipEntry(srcFile.getName());
                                    zos.putNextEntry(zipEntry);
                                    byte[] bytes = new byte[1024];
                                    int length;
                                    while ( (length = fis.read(bytes)) >= 0 ) {
                                        zos.write(bytes, 0, length);
                                    }
                                }
                            }
                        }
                        FileUtils.readFileToString(new File(base.resolve(path).toAbsolutePath().normalize() + "/target/" + project.getProgramName() + "." + project.getBinaryFileExtension()), "UTF-8");
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
