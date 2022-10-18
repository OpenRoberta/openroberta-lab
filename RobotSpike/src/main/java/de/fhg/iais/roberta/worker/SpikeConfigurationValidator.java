package de.fhg.iais.roberta.worker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.util.Key;

public class SpikeConfigurationValidator {
    private static final Set<String> OVERLAPPING_PINS = Stream.of("A", "B", "C", "D", "E", "F").collect(Collectors.toCollection(HashSet::new));

    private final Project project;

    public SpikeConfigurationValidator(Project project) {
        this.project = project;
    }

    public void validateConfiguration(List<String> freePins) {
        List<String> currentFreePins = new ArrayList<>(freePins);
        project.getConfigurationAst().getConfigurationComponents().forEach((k, v) -> checkPinOverlap(v, currentFreePins, freePins));
    }

    public void checkPinOverlap(ConfigurationComponent configurationComponent, List<String> currentFreePins, List<String> availablePins) {
        Map<String, String> componentProperties = configurationComponent.getComponentProperties();
        List<String> blockPins = new ArrayList<>();
        componentProperties.forEach((k, v) -> {
            if ( OVERLAPPING_PINS.contains(k) ) {
                if ( currentFreePins.contains(v) ) {
                    blockPins.add(v);
                } else {
                    project.addToErrorCounter(1, null);
                    project.setResult(Key.PROGRAM_INVALID_STATEMETNS);
                    String blockId = configurationComponent.getProperty().getBlocklyId();
                    if ( !availablePins.contains(v) ) {
                        project.addToConfAnnotationList(blockId, NepoInfo.error("CONFIGURATION_ERROR_MISSING_PIN"));
                    } else {
                        project.addToConfAnnotationList(blockId, NepoInfo.error("CONFIGURATION_ERROR_OVERLAPPING_PORTS"));
                    }
                }
            }
        });
        if ( blockPins.stream().distinct().count() != blockPins.size() ) {
            project.addToErrorCounter(1, null);
            project.setResult(Key.PROGRAM_INVALID_STATEMETNS);
            String blockId = configurationComponent.getProperty().getBlocklyId();
            project.addToConfAnnotationList(blockId, NepoInfo.error("CONFIGURATION_ERROR_OVERLAPPING_PORTS"));
        }
    }
}
