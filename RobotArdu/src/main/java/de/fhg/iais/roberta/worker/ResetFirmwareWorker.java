package de.fhg.iais.roberta.worker;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.PluginProperties;

public class ResetFirmwareWorker implements IWorker {
    private static final Logger LOG = LoggerFactory.getLogger(ResetFirmwareWorker.class);

    @Override
    public void execute(Project project) {
        IRobotFactory factory = project.getRobotFactory();
        PluginProperties properties = factory.getPluginProperties();
        final File source = new File(properties.getCompilerResourceDir() + "/" + project.getCompiledProgramPath() + "." + project.getBinaryFileExtension());
        final File dest =
            new File(
                properties.getTempDir()
                    + project.getToken()
                    + "/"
                    + project.getProgramName()
                    + "/target");
        Key resultKey;
        try {
            FileUtils.copyFileToDirectory(source, dest);
            resultKey = Key.FIRMWARE_RESET_SUCCESS;
        } catch ( IOException e ) {
            resultKey = Key.FIRMWARE_RESET_ERROR;
            LOG.error("Reading default firmware from ardu failed", e);
        }
        project.setResult(resultKey);
    }
}
