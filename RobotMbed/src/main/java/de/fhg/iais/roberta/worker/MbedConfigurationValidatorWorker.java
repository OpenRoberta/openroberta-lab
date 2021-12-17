package de.fhg.iais.roberta.worker;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.util.Key;

public class MbedConfigurationValidatorWorker {
    private static final Set<String> OVERLAPPING_PINS = Stream.of("PIN1").collect(Collectors.toCollection(HashSet::new));
    private static final Set<String> CALLIOPE_PINS = Stream.of("1", "2", "4", "5", "C16", "C17").collect(Collectors.toCollection(HashSet::new));
    private final Project project;

    public MbedConfigurationValidatorWorker(Project project) {
        this.project = project;
    }

    public void validateConfiguration(List<String> freePins, List<String> defaultProps) {
        List<String> currentFreePins = new ArrayList<>(freePins);
        List<String> defaultProperties = new ArrayList<>(defaultProps);
        project.getConfigurationAst().getConfigurationComponents().forEach((k, v) -> {
            if ( !defaultProperties.contains(v.getComponentType()) ) {
                checkPinOverlap(v, currentFreePins);
            }
        });
    }

    public void checkPinOverlap(ConfigurationComponent configurationComponent, List<String> currentFreePins) {
        Map<String, String> componentProperties = configurationComponent.getComponentProperties();
        List<String> blockPins = new ArrayList<>();
        if ( configurationComponent.getComponentType().equals(SC.CALLIBOT) ) {
            CALLIOPE_PINS.forEach((v) -> checkIfContainsPin(configurationComponent, currentFreePins, blockPins, v));
        } else if ( configurationComponent.getComponentType().equals(SC.BUZZER)){
            checkIfContainsPin(configurationComponent,currentFreePins,blockPins, "0");
        } else {
            componentProperties.forEach((k, v) -> {
                if ( OVERLAPPING_PINS.contains(k) ) {
                    checkIfContainsPin(configurationComponent, currentFreePins, blockPins, v);
                }
            });
        }
        if ( blockPins.stream().distinct().count() != blockPins.size() ) {
            project.addToErrorCounter(1, null);
            project.setResult(Key.PROGRAM_INVALID_STATEMETNS);
            String blockId = configurationComponent.getProperty().getBlocklyId();
            project.addToConfAnnotationList(blockId, NepoInfo.error("CONFIGURATION_ERROR_OVERLAPPING_PORTS"));
        }
    }

    private void checkIfContainsPin(ConfigurationComponent configurationComponent, List<String> currentFreePins, List<String> blockPins, String pin) {
        if ( currentFreePins.contains(pin) ) {
            blockPins.add(pin);
            currentFreePins.removeIf(s -> s.equals(pin));
            return;
        }
        project.addToErrorCounter(1, null);
        project.setResult(Key.PROGRAM_INVALID_STATEMETNS);
        String blockId = configurationComponent.getProperty().getBlocklyId();
        project.addToConfAnnotationList(blockId, NepoInfo.error("CONFIGURATION_ERROR_OVERLAPPING_PORTS"));
    }
}
