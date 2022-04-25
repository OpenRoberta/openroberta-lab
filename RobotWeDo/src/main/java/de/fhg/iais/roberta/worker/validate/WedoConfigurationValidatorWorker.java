package de.fhg.iais.roberta.worker.validate;

import java.util.HashMap;

import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.util.Key;

public class WedoConfigurationValidatorWorker {

    private final Project project;
    private final HashMap<String, Boolean> availableHubs = new HashMap<String, Boolean>() {{
        put("1", true);
        put("2", true);
    }};

    public WedoConfigurationValidatorWorker(Project project) {
        this.project = project;
    }

    public void validateConfiguration() {

        project.getConfigurationAst().getConfigurationComponents().forEach((key, configurationComponent) -> {
            String connector = configurationComponent.getComponentProperties().get("CONNECTOR");
            if ( null != connector ) {
                if ( availableHubs.get(connector) ) {
                    availableHubs.put(connector, false);
                } else {
                    project.addToErrorCounter(1, null);
                    project.setResult(Key.PROGRAM_INVALID_STATEMETNS);
                    String blockId = configurationComponent.getProperty().getBlocklyId();
                    project.addToConfAnnotationList(blockId, NepoInfo.error("CONFIGURATION_ERROR_OVERLAPPING_PORTS"));
                }
            }
        });
    }
}
