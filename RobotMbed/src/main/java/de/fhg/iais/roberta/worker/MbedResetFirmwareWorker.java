package de.fhg.iais.roberta.worker;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.PluginProperties;

public class MbedResetFirmwareWorker implements IWorker {
    private static final Logger LOG = LoggerFactory.getLogger(MbedResetFirmwareWorker.class);

    @Override
    public void execute(Project project) {
        RobotFactory factory = project.getRobotFactory();
        PluginProperties properties = factory.getPluginProperties();

        Key resultKey;
        String path = properties.getCompilerResourceDir() + "/" + project.getCompiledProgramPath() + "." + project.getBinaryFileExtension();
        if (new File(path).exists()){
            project.setBinaryURLPath(path);
            resultKey = Key.FIRMWARE_RESET_SUCCESS;
        } else {
            LOG.error("Reading default firmware from mbed failed, file doesnt exist");
            resultKey = Key.FIRMWARE_RESET_ERROR;
        }
        project.setResult(resultKey);
    }
}
