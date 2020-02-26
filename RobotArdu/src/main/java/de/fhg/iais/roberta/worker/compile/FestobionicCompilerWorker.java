package de.fhg.iais.roberta.worker.compile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.bean.CompilerSetupBean;
import de.fhg.iais.roberta.codegen.AbstractCompilerWorkflow;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.Pair;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.worker.IWorker;

public class FestobionicCompilerWorker implements IWorker {

    private static final Logger LOG = LoggerFactory.getLogger(FestobionicCompilerWorker.class);

    @Override
    public void execute(Project project) {
        String programName = project.getProgramName();
        String robot = project.getRobot();
        Pair<Key, String> workflowResult = this.runBuild(project);
        project.setResult(workflowResult.getFirst());
        project.addResultParam("MESSAGE", workflowResult.getSecond());
        if ( workflowResult.getFirst() == Key.COMPILERWORKFLOW_SUCCESS ) {
            LOG.info("compile {} program {} successful", robot, programName);
        } else {
            LOG.error("compile {} program {} failed with {}", robot, programName, workflowResult);
        }
    }

    /**
     * create command to call the cross compiler and execute the call.
     *
     * @return a pair of Key.COMPILERWORKFLOW_SUCCESS or Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED and the cross compiler output
     */
    private Pair<Key, String> runBuild(Project project) {
        CompilerSetupBean compilerWorkflowBean = (CompilerSetupBean) project.getWorkerResult(CompilerSetupBean.class);
        String compilerBinDir = compilerWorkflowBean.getCompilerBinDir();
        String compilerResourcesDir = compilerWorkflowBean.getCompilerResourcesDir();
        String tempDir = compilerWorkflowBean.getTempDir();
        Util
            .storeGeneratedProgram(
                tempDir,
                project.getSourceCode().toString(),
                project.getToken(),
                project.getProgramName(),
                "." + project.getSourceCodeFileExtension());
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
        Path path = Paths.get(tempDir + project.getToken() + "/" + project.getProgramName());
        Path base = Paths.get("");

        // TODO esp32 tmp build directories generated with arduino-builder are about 50 MB, in case this will be used, either arduino-builder has to be skipped
        // or another workaround has to be found, as a 50 MB directory per connected token is too large
        String[] executableWithParameters =
            {
                scriptName,
                "-hardware=" + compilerResourcesDir + "hardware/builtin",
                "-hardware=" + compilerResourcesDir + "hardware/additional",
                "-tools=" + compilerResourcesDir + "/" + os + "/tools-builder",
                "-tools=" + compilerResourcesDir + "hardware/additional/esp32/tools",
                "-libraries=" + compilerResourcesDir + "/libraries",
                compilerWorkflowBean.getFqbn(),
                "-prefs=compiler.path=" + compilerBinDir,
                "-build-path=" + base.resolve(path).toAbsolutePath().normalize() + "/target/",
                base.resolve(path).toAbsolutePath().normalize() + "/source/" + project.getProgramName() + "." + project.getSourceCodeFileExtension()
            };

        Pair<Boolean, String> result = AbstractCompilerWorkflow.runCrossCompiler(executableWithParameters);
        Key resultKey = result.getFirst() ? Key.COMPILERWORKFLOW_SUCCESS : Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;

        // TODO this is a bad workaround and should be changed if this will really be used
        // as we currently only support sending a single file to robots and esp32 needs two files, they are zipped and send the zip is sent to the Connector
        // the connector then unzips the files and correctly flashes them to the robot
        // in order to circumvent large changes
        if ( result.getFirst() ) {
            try (FileOutputStream fos = new FileOutputStream(base.resolve(path).toAbsolutePath().normalize() + "/target/" + project.getProgramName() + ".zip"); // TODO hardcoded due to workaround
                ZipOutputStream zos = new ZipOutputStream(fos)) {
                for ( String srcFilename : Arrays
                    .asList(
                        base.resolve(path).toAbsolutePath().normalize() + "/target/" + project.getProgramName() + "." + project.getBinaryFileExtension(),
                        base.resolve(path).toAbsolutePath().normalize() + "/target/" + project.getProgramName() + ".ino.partitions.bin") ) { // TODO hardcoded due to workaround
                    File srcFile = new File(srcFilename);
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
            } catch ( IOException e ) {
                LOG.warn("The generated esp32 build files could not be zipped:", e);
                resultKey = Key.COMPILERWORKFLOW_ERROR_PROGRAM_COMPILE_FAILED;
            }
        }

        return Pair.of(resultKey, result.getSecond());
    }
}
