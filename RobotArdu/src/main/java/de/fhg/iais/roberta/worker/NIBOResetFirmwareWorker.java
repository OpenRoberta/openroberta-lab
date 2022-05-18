package de.fhg.iais.roberta.worker;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.visitor.codegen.NIBOHexPrefix;

public class NIBOResetFirmwareWorker implements IWorker {
    private static final Logger LOG = LoggerFactory.getLogger(NIBOResetFirmwareWorker.class);

    @Override
    public void execute(Project project) {
        RobotFactory factory = project.getRobotFactory();
        PluginProperties properties = factory.getPluginProperties();

        Key resultKey;
        String path = properties.getCompilerResourceDir() + "/" + project.getCompiledProgramPath() + "." + project.getBinaryFileExtension();
        final File source = new File(path);
        final File dest = new File(properties.getTempDir() + project.getToken() + "/" + project.getProgramName() + "/target");

        try {
            project.setCompiledHex(Util.getBase64EncodedHex(path, NIBOHexPrefix.getHexPrefixForRobot(project.getRobot())));
            FileUtils.copyFileToDirectory(source, dest);
            resultKey = Key.FIRMWARE_RESET_SUCCESS;
        } catch ( IllegalArgumentException | IOException e ) {
            LOG.error("Reading default firmware for NIBO robots failed", e);
            resultKey = Key.FIRMWARE_RESET_ERROR;
        }
        project.setResult(resultKey);
    }
}
