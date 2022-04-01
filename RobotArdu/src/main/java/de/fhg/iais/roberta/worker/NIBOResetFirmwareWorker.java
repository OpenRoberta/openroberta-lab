package de.fhg.iais.roberta.worker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.visitor.codegen.NIBOHexPrefix;

public class NIBOResetFirmwareWorker implements IWorker {
    private static final Logger LOG = LoggerFactory.getLogger(NIBOResetFirmwareWorker.class);

    @Override
    public void execute(Project project) {
        IRobotFactory factory = project.getRobotFactory();
        PluginProperties properties = factory.getPluginProperties();

        Key resultKey;
        String path = properties.getCompilerResourceDir() + "/" + project.getCompiledProgramPath() + "." + project.getBinaryFileExtension();

        try {
            project.setCompiledHex(Util.getBase64EncodedHex(path, NIBOHexPrefix.getHexPrefixForRobot(project.getRobot())));
            resultKey = Key.FIRMWARE_RESET_SUCCESS;
        } catch ( IllegalArgumentException e ) {
            LOG.error("Reading default firmware for NIBO robots failed", e);
            resultKey = Key.FIRMWARE_RESET_ERROR;
        }
        project.setResult(resultKey);
    }
}
