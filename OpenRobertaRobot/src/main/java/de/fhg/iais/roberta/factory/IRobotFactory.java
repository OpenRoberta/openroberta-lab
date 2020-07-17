package de.fhg.iais.roberta.factory;

import java.util.List;
import java.util.Set;

import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.worker.IWorker;

public interface IRobotFactory {

    BlocklyDropdownFactory getBlocklyDropdownFactory();

    /**
     * Get the file extension of the specific language for this robot. This is used
     * when we want to download locally the source code into a file.
     *
     * @return file extension
     */
    String getSourceCodeFileExtension();

    /**
     * Get the file extension of the specific executable binary for this robot. This
     * is used when the binary is send back to the client.
     *
     * @return file extension
     */
    String getBinaryFileExtension();

    String getProgramToolboxBeginner();

    String getProgramToolboxExpert();

    String getProgramDefault();

    String getConfigurationToolbox();

    String getConfigurationDefault();

    String getRealName();

    Boolean hasSim();

    Boolean hasMultipleSim();

    String getInfoDE();

    String getInfoEN();

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

    /**
     * Returns the sensor prefix if the robot has on an old configuration, null otherwise.
     * 
     * @return the sensor prefix, may be null
     */
    String optSensorPrefix();

    /**
     * Returns the top block if the robot has on an old configuration, null otherwise.
     * 
     * @return the top block, may be null
     */
    String optTopBlockOfOldConfiguration();

    String getGroup();

    default String getMenuVersion() {
        return null;
    }

    PluginProperties getPluginProperties();

    Boolean hasWlanCredentials();

    /**
     * Returns the name of the factory default program. Used to get the compiled program from the
     * crosscompiler resources.
     *
     * @return the name of the factory default program
     */
    String getFirmwareDefaultProgramName();

    List<IWorker> getWorkerPipe(String workflow);

    /**
     * Returns all workflows this robot has registered in the properties.
     *
     * @return unmodifiable string set of workflows
     */
    Set<String> getWorkflows();

    boolean hasWorkflow(String workflow);
}