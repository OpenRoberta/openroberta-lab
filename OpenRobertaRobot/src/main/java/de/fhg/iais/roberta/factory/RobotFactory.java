package de.fhg.iais.roberta.factory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.dbc.Assert;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.worker.IWorker;

public class RobotFactory {
    private static final Logger LOG = LoggerFactory.getLogger(RobotFactory.class);

    private final PluginProperties pluginProperties;
    private final BlocklyDropdownFactory blocklyDropdown2EnumFactory;
    private final String beginnerToolbox;
    private final String expertToolbox;
    private final String programDefault;
    private final String configurationToolbox;
    private final String configurationDefault;
    private Map<String, IWorker> workers = new HashMap<>(); //worker type to implementing class(es) collect->de.fhg.iais.roberta.visitor.collect.Ev3UsedHardwareCollectorWorker
    private Map<String, List<String>> workflows = new HashMap<>(); //workflow name to a list of types of applicable workers: showsource->collect,generate

    public RobotFactory(PluginProperties pluginProperties) {
        this.pluginProperties = pluginProperties;
        this.blocklyDropdown2EnumFactory = new BlocklyDropdownFactory();
        this.beginnerToolbox = Util.readResourceContent(this.pluginProperties.getStringProperty("robot.program.toolbox.beginner"));
        this.expertToolbox = Util.readResourceContent(this.pluginProperties.getStringProperty("robot.program.toolbox.expert"));
        this.programDefault = Util.readResourceContent(this.pluginProperties.getStringProperty("robot.program.default"));
        this.configurationToolbox = Util.readResourceContent(this.pluginProperties.getStringProperty("robot.configuration.toolbox"));
        this.configurationDefault = Util.readResourceContent(this.pluginProperties.getStringProperty("robot.configuration.default"));
        loadWorkers();
    }

    public PluginProperties getPluginProperties() {
        return this.pluginProperties;
    }

    public BlocklyDropdownFactory getBlocklyDropdownFactory() {
        return this.blocklyDropdown2EnumFactory;
    }

    public final String getSourceCodeFileExtension() {
        return this.pluginProperties.getStringProperty("robot.plugin.fileExtension.source");
    }

    public final String getBinaryFileExtension() {
        return this.pluginProperties.getStringProperty("robot.plugin.fileExtension.binary");
    }

    public final String getGroup() {
        String group = this.pluginProperties.getStringProperty("robot.plugin.group");
        return group != null && !group.equals("") ? group : this.pluginProperties.getRobotName();
    }

    public final String getProgramToolboxBeginner() {
        return this.beginnerToolbox;
    }

    public final String getProgramToolboxExpert() {
        return this.expertToolbox;
    }

    public final String getProgramDefault() {
        return this.programDefault;
    }

    public final String getConfigurationToolbox() {
        return this.configurationToolbox;
    }

    public final String getConfigurationDefault() {
        return this.configurationDefault;
    }

    public final String getRealName() {
        return this.pluginProperties.getStringProperty("robot.real.name");
    }

    public final Boolean hasSim() {
        return !this.pluginProperties.getStringProperty("robot.plugin.workflow.getsimulationcode").equals("do.nothing");
    }

    public final Boolean hasMultipleSim() {
        return this.pluginProperties.getStringProperty("robot.multisim") != null && this.pluginProperties.getStringProperty("robot.multisim").equals("true");
    }

    public boolean hasMarkerSim() {
        return this.pluginProperties.getStringProperty("robot.markersim") != null && this.pluginProperties.getStringProperty("robot.markersim").equals("true");
    }

    public final Boolean hasNN() {
        return this.pluginProperties.getStringProperty("robot.nn") != null && this.pluginProperties.getStringProperty("robot.nn").equals("true");
    }

    public Boolean hasWebotsSim() {
        return this.pluginProperties.getStringProperty("robot.webots.sim") != null && this.pluginProperties.getStringProperty("robot.webots.sim").equals("true");
    }

    public String getWebotsUrl() {
        return this.pluginProperties.getStringProperty("robot.webots.url");
    }

    public final String getInfoDE() {
        String robotInfoDE = this.pluginProperties.getStringProperty("robot.info.de");
        if ( robotInfoDE == null ) {
            return "#";
        } else {
            return robotInfoDE;
        }
    }

    public final String getInfoEN() {
        String robotInfoEN = this.pluginProperties.getStringProperty("robot.info.en");
        if ( robotInfoEN == null ) {
            return "#";
        } else {
            return robotInfoEN;
        }
    }

    public final String getRobotAnnouncement() {
        return this.pluginProperties.getStringProperty("robot.announcement");
    }

    public final Boolean hasStopButton() {
        return this.pluginProperties.getStringProperty("robot.stopButton") != null;
    }

    public final String getConnectionType() {
        return this.pluginProperties.getStringProperty("robot.connection");
    }

    public final void setConnectionType(String type) {
        this.pluginProperties.setStringProperty("robot.connection", type);
    }

    public final String getVendorId() {
        return this.pluginProperties.getStringProperty("robot.vendor");
    }

    public final Boolean hasConfiguration() {
        return Boolean.parseBoolean(this.pluginProperties.getStringProperty("robot.configuration"));
    }

    public final String getConfigurationType() {
        String configurationType = this.pluginProperties.getStringProperty("robot.configuration.type");
        if ( configurationType == null ) {
            throw new DbcException("no property robot.configuration.type");
        } else if ( configurationType.equals("new") ) {
            return "new";
        }
        if ( configurationType.startsWith("old") ) {
            return "old";
        } else {
            throw new DbcException("invalid property robot.configuration.type");
        }
    }

    public final String optSensorPrefix() {
        String configurationType = this.pluginProperties.getStringProperty("robot.configuration.type");
        return configurationType.substring(4);
    }

    public final String optTopBlockOfOldConfiguration() {
        return this.pluginProperties.getStringProperty("robot.configuration.old.toplevelblock");
    }

    public final String getCommandline() {
        return this.pluginProperties.getStringProperty("robot.connection.commandLine");
    }

    public final String getSignature() {
        return this.pluginProperties.getStringProperty("robot.connection.signature");
    }

    public final String getMenuVersion() {
        return this.pluginProperties.getStringProperty("robot.menu.version");
    }

    public final Boolean hasWlanCredentials() {
        return this.pluginProperties.getStringProperty("robot.haswlan") != null;
    }

    public final String getFirmwareDefaultProgramName() {
        return this.pluginProperties.getStringProperty("robot.factory.default");
    }

    private void loadWorkers() {
        LOG.trace("Loading workers...");
        this.pluginProperties.getPluginProperties().forEach((k, v) -> {
            if ( k.toString().startsWith("robot.plugin.worker") ) {
                String[] workersForType = k.toString().split("robot\\.plugin\\.worker\\.");
                String workersType = workersForType[workersForType.length - 1];
                List<String> workerClassNames = Stream.of(v.toString().trim().split("\\s*,\\s*")).collect(Collectors.toList());
                workerClassNames.forEach(workerClassName -> {
                    workerClassName = workerClassName.trim();
                    LOG.trace("Loading worker {}", workerClassName);
                    try {
                        IWorker newWorker = (IWorker) Class.forName(workerClassName).getDeclaredConstructor().newInstance();
                        Assert.isNull(this.workers.put(workersType, newWorker));
                    } catch ( Exception e ) {
                        throw new DbcException("Worker class:" + workerClassName + " is invalid", e);
                    }
                });
            }
            if ( k.toString().startsWith("robot.plugin.workflow") ) {
                String[] workflowsForType = k.toString().split("\\.");
                String workflowName = workflowsForType[workflowsForType.length - 1];
                this.workflows.put(workflowName, Stream.of(v.toString().trim().split("\\s*,\\s*")).collect(Collectors.toList()));
            }
        });

    }

    public List<IWorker> getWorkerPipe(String workflow) {
        List<String> workerTypes = this.workflows.get(workflow);
        Assert.notNull(workerTypes, "Workflow %s is null for robot %s, check the properties", workflow, getRealName());
        if ( workerTypes.size() == 1 && "do.nothing".equals(workerTypes.get(0)) ) {
            // handle the special case, that the workflow is empty for this robot plugin
            return Collections.emptyList();
        }
        List<IWorker> workerPipe = new ArrayList<>();
        for ( String type : workerTypes ) {
            IWorker worker = this.workers.get(type);
            Assert.notNull(worker, "Worker for type %s is null, check the properties, worker names may not match", type);
            workerPipe.add(worker);
        }
        return workerPipe;
    }

    public Set<String> getWorkflows() {
        return Collections.unmodifiableSet(this.workflows.keySet());
    }

    public boolean hasWorkflow(String workflow) {
        return this.workflows.get(workflow) != null;
    }
}
