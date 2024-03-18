package de.fhg.iais.roberta.worker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
import de.fhg.iais.roberta.util.ast.BlocklyProperties;

/**
 * Abstract CollectorWorker, keeps shared methods for Lego and Pybricks
 */
public abstract class AbstractSpikeValidatorAndCollectorWorker extends AbstractValidatorAndCollectorWorker {

    private static final List<String> NON_BLOCKING_PROPERTIES = Collections.unmodifiableList(Arrays.asList("MOTOR_L", "MOTOR_R", "BRICK_WHEEL_DIAMETER", "BRICK_TRACK_WIDTH"));
    @Override
    final public void execute(Project project) {
        validateConfig(project);
        super.execute(project);
    }

    final void validateConfig(Project project) {
        List<String> takenPins = new ArrayList<>();

        project.getConfigurationAst().getConfigurationComponents().forEach((k, v) -> checkIfPortTaken(project, v, takenPins));
        Map<String, ConfigurationComponent> diffDrives = project.getConfigurationAst().getAllConfigurationComponentByType("DIFFERENTIALDRIVE");

        boolean diffDriveCompUnique = diffDrives.size() == 1;
        diffDrives.forEach((a, diffDrive) -> {
            boolean rightMotorMissing = true;
            boolean leftMotorMissing = true;
            for ( ConfigurationComponent configComp : project.getConfigurationAst().getConfigurationComponents().values() ) {
                if ( configComp.componentType.equals("MOTOR") ) {
                    if ( configComp.getProperty("PORT").equals(diffDrive.getProperty("MOTOR_R")) ) {
                        rightMotorMissing = false;
                    }
                    if ( configComp.getProperty("PORT").equals(diffDrive.getProperty("MOTOR_L")) ) {
                        leftMotorMissing = false;
                    }
                }
            }
            if ( rightMotorMissing || leftMotorMissing || !diffDriveCompUnique ) {
                BlocklyProperties blocklyProperties = diffDrive.getProperty();
                String blockId = blocklyProperties.blocklyId;
                project.setResult(Key.PROGRAM_INVALID_STATEMETNS);
                project.addToErrorCounter(1, null);
                if ( leftMotorMissing ) {
                    project.addToConfAnnotationList(blockId, NepoInfo.error("CONFIGURATION_ERROR_MOTOR_LEFT_MISSING"));
                }
                if ( rightMotorMissing ) {
                    project.addToConfAnnotationList(blockId, NepoInfo.error("CONFIGURATION_ERROR_MOTOR_RIGHT_MISSING"));
                }
                if ( !diffDriveCompUnique ) {
                    project.addToConfAnnotationList(blockId, NepoInfo.error("CONFIGURATION_ERROR_DIFFDRIVE_NOT_UNIQUE"));
                }
            }
            if ( diffDrive.getProperty("MOTOR_L").equals(diffDrive.getProperty(("MOTOR_R"))) ) {
                BlocklyProperties blocklyProperties = diffDrive.getProperty();
                String blockId = blocklyProperties.blocklyId;
                project.addToErrorCounter(1, null);
                project.setResult(Key.PROGRAM_INVALID_STATEMETNS);
                project.addToConfAnnotationList(blockId, NepoInfo.error("CONFIGURATION_ERROR_OVERLAPPING_PORTS"));
            }
        });
    }

    final void checkIfPortTaken(Project project, ConfigurationComponent configurationComponent, List<String> takenPins) {
        Map<String, String> componentProperties = configurationComponent.getComponentProperties();
        for ( Map.Entry<String, String> property : componentProperties.entrySet() ) {
            if ( NON_BLOCKING_PROPERTIES.contains(property.getKey()) ) {
                continue;
            }
            if ( takenPins.contains(property.getValue()) ) {
                BlocklyProperties blocklyProperties = configurationComponent.getProperty();
                String blockId = blocklyProperties.blocklyId;
                project.addToErrorCounter(1, null);
                project.setResult(Key.PROGRAM_INVALID_STATEMETNS);
                project.addToConfAnnotationList(blockId, NepoInfo.error("CONFIGURATION_ERROR_OVERLAPPING_PORTS"));
                break;
            }
            takenPins.add(property.getValue());
        }
    }
}