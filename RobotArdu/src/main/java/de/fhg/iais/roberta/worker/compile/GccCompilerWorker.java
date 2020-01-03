package de.fhg.iais.roberta.worker.compile;

import java.nio.file.Paths;

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

public class GccCompilerWorker implements IWorker {

    private static final Logger LOG = LoggerFactory.getLogger(GccCompilerWorker.class);

    @Override
    public void execute(Project project) {
        CompilerSetupBean compilerWorkflowBean = project.getWorkerResult(CompilerSetupBean.class);
        final String compilerResourcesDir = compilerWorkflowBean.getCompilerResourcesDir();
        final String tempDir = compilerWorkflowBean.getTempDir();
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
                arduinoArch = "ARDUINO_ARCH_AVR";
                break;
            case "unowifirev2":
                boardVariant = "uno2018";
                mmcu = "atmega4809";
                arduinoVariant = "ARDUINO_AVR_UNO_WIFI_REV2";
                arduinoArch = "ARDUINO_ARCH_MEGAAVR -DUNO_WIFI_REV2_328MODE -DMILLIS_USE_TIMERB3";
                scriptName = compilerResourcesDir + "arduino-resources/build_project_unowifirev2.sh";
                break;
            case "mega":
                boardVariant = "mega";
                mmcu = "atmega2560";
                arduinoVariant = "ARDUINO_AVR_" + project.getRobot().toUpperCase();
                arduinoArch = "ARDUINO_ARCH_MEGAAVR";
                break;
            case "sensebox":
                scriptName = compilerResourcesDir + "arduino-resources/build_project_sensebox.sh";
                break;
            default:
                throw new DbcException("Unsupported Arduino type");
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
            String base64EncodedHex =
                AbstractCompilerWorkflow.getBase64EncodedHex(Paths.get(userProgramDirPath) + "/target/" + programName + "." + project.getBinaryFileExtension());
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
