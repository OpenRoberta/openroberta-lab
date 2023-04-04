package de.fhg.iais.roberta.worker.validate;

import java.util.ArrayList;
import java.util.Collection;
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
import de.fhg.iais.roberta.util.syntax.SC;

public class ArduinoConfigurationValidator {
    private static final Set<String> OVERLAPPING_PINS = Stream.of("INPUT", "OUTPUT", "+", "S", "PULSE", "TRIG", "ECHO", "RED", "GREEN", "BLUE", "RST", "IN", "SDA", "RS", "E", "D4", "D5", "D6", "D7", "PIN1", "IN1", "IN2", "IN3", "IN4", "OUT").collect(Collectors.toCollection(HashSet::new));

    private final Project project;

    public ArduinoConfigurationValidator(Project project) {
        this.project = project;
    }

    public void validateConfiguration(List<String> freePins) {
        List<String> currentFreePins = new ArrayList<>(freePins);
        project.getConfigurationAst().getConfigurationComponents().forEach((k, v) -> checkPinOverlap(v, currentFreePins, freePins));
        project.getConfigurationAst().getConfigurationComponents().forEach((k, v) -> checkRgbInternalUse(v));
        if ( project.getRobot().equals("nano") || project.getRobot().equals("uno") ) {
            checkTimerOverlap(project.getConfigurationAst().getConfigurationComponents().values());
        }
    }

    private void checkTimerOverlap(Collection<ConfigurationComponent> configurationComponents) {
        List<ConfigurationComponent> buzzer = new ArrayList<ConfigurationComponent>();
        List<ConfigurationComponent> ir = new ArrayList<ConfigurationComponent>();
        List<ConfigurationComponent> servo = new ArrayList<ConfigurationComponent>();
        for ( ConfigurationComponent cc : configurationComponents ) {
            if ( cc.componentType.equals(SC.BUZZER) ) {
                buzzer.add(cc);
            } else if ( cc.componentType.equals(SC.SERVOMOTOR) ) {
                servo.add(cc);
            } else if ( cc.componentType.equals(SC.INFRARED) ) {
                ir.add(cc);
            }
        }
        if ( buzzer.size() > 0 && servo.size() > 0 && ir.size() > 0 ) {
            project.addToErrorCounter(1, null);
            project.setResult(Key.PROGRAM_INVALID_STATEMETNS);
            Stream.of(buzzer, servo, ir).flatMap(i -> i.stream()).map(cc -> cc.getProperty().getBlocklyId())
                .forEach(id -> project.addToConfAnnotationList(id, NepoInfo.error("CONFIGURATION_ERROR_TIMER_CONFLICT")));
        }
    }

    public void checkPinOverlap(ConfigurationComponent configurationComponent, List<String> currentFreePins, List<String> availablePins) {
        Map<String, String> componentProperties = configurationComponent.getComponentProperties();
        List<String> blockPins = new ArrayList<>();
        componentProperties.forEach((k, v) -> {
            if ( OVERLAPPING_PINS.contains(k) ) {
                if ( currentFreePins.contains(v) ) {
                    blockPins.add(v);
                    currentFreePins.removeIf(s -> s.equals(v) && !v.equals(SC.LED_BUILTIN)); // built in LED cannot overlap
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

    // TODO restructure validator worker for more generic usage patterns
    public void checkRgbInternalUse(ConfigurationComponent configurationComponent) {
        if ( configurationComponent.componentType.equals(SC.RGBLED) ) {
            Map<String, String> componentProperties = configurationComponent.getComponentProperties();
            componentProperties.forEach((k, v) -> {
                if ( v.equals(SC.LED_BUILTIN) && !project.getRobot().equals("unowifirev2") ) {
                    project.addToErrorCounter(1, null);
                    project.setResult(Key.PROGRAM_INVALID_STATEMETNS);
                    project.addToConfAnnotationList(configurationComponent.getProperty().getBlocklyId(), NepoInfo.error("CONFIGURATION_ERROR_NO_BUILTIN_RGBLED"));
                }
            });
        }
    }
}
