package de.fhg.iais.roberta.factory;

import java.util.List;

import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.worker.IWorker;

public interface IRobotFactory {

    BlocklyDropdownFactory getBlocklyDropdownFactory();

    /**
     * Get the file extension of the specific language for this robot. This is used when we want to download locally the source code into a file.
     */
    String getFileExtension();

    String getProgramToolboxBeginner();

    String getProgramToolboxExpert();

    String getProgramDefault();

    String getConfigurationToolbox();

    String getConfigurationDefault();

    String getRealName();

    Boolean hasSim();

    Boolean hasMultipleSim();

    String getInfo();

    Boolean isBeta();

    String getConnectionType();

    default String getVendorId() {
        return null;
    }

    default String getCommandline() {
        return null;
    }

    default String getSignature() {
        return null;
    }

    Boolean hasConfiguration();

    String getConfigurationType();

    String getSensorPrefix();

    String getTopBlockOfOldConfiguration();

    String getGroup();

    default String getMenuVersion() {
        return null;
    }

    PluginProperties getPluginProperties();

    Boolean hasWlanCredentials();

    List<IWorker> getWorkerPipe(String workflow);
}