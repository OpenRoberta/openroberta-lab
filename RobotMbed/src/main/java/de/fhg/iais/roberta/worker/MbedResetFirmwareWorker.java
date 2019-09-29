package de.fhg.iais.roberta.worker;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.PluginProperties;

public class MbedResetFirmwareWorker implements IWorker {
    private static final Logger LOG = LoggerFactory.getLogger(MbedResetFirmwareWorker.class);

    @Override
    public void execute(Project project) {
        IRobotFactory factory = project.getRobotFactory();
        PluginProperties properties = factory.getPluginProperties();

        Key resultKey;
        String path = properties.getCompilerResourceDir() + "/" + project.getCompiledProgramPath() + "." + project.getBinaryFileExtension();
        try {
            project.setCompiledHex(FileUtils.readFileToString(new File(path), Charset.forName("utf-8")));
            resultKey = Key.FIRMWARE_RESET_SUCCESS;
        } catch ( IOException e ) {
            LOG.error("Reading default firmware from mbed failed", e);
            resultKey = Key.FIRMWARE_RESET_ERROR;
        }
        project.setResult(resultKey);
    }
}
