package de.fhg.iais.roberta.worker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ClassToInstanceMap;

import de.fhg.iais.roberta.bean.IProjectBean;
import de.fhg.iais.roberta.components.Project;
import de.fhg.iais.roberta.constants.FischertechnikConstants;
import de.fhg.iais.roberta.syntax.configuration.ConfigurationComponent;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.util.Key;
import de.fhg.iais.roberta.util.ast.BlocklyProperties;
import de.fhg.iais.roberta.util.syntax.SC;
import de.fhg.iais.roberta.visitor.Txt4Methods;
import de.fhg.iais.roberta.visitor.Txt4ValidatorAndCollectorVisitor;
import de.fhg.iais.roberta.visitor.validate.CommonNepoValidatorAndCollectorVisitor;

public class Txt4ValidatorAndCollectorWorker extends AbstractValidatorAndCollectorWorker {

    private static final List<String> NON_BLOCKING_PROPERTIES = Collections.unmodifiableList(Arrays.asList("MOTOR_FL", "MOTOR_FR", "MOTOR_RL", "MOTOR_RR", "MOTOR_L", "MOTOR_R", "BRICK_WHEEL_DIAMETER", "BRICK_TRACK_WIDTH", "WHEEL_BASE", "VCC"));

    @Override
    public void execute(Project project) {
        validateConfig(project);
        super.execute(project);
    }
    @Override
    protected CommonNepoValidatorAndCollectorVisitor getVisitor(
        Project project, ClassToInstanceMap<IProjectBean.IBuilder> beanBuilders) {
        return new Txt4ValidatorAndCollectorVisitor(project.getConfigurationAst(), beanBuilders, false);
    }

    @Override
    protected List<Class<? extends Enum<?>>> getAdditionalMethodEnums() {
        return Collections.singletonList(Txt4Methods.class);
    }

    private void validateConfig(Project project) {
        List<String> takenPins = new ArrayList<>();
        project.getConfigurationAst().getConfigurationComponents().forEach((k, v) -> checkIfPortTaken(project, v, takenPins));
        checkDiffDrive(project);
        checkOmniDrive(project);
    }

    private boolean isEncoderMissing(ConfigurationComponent motor) {
        try {
            motor.getSubComponents();
        } catch ( Exception e ) {
            return true;
        }
        return false;
    }

    private void checkDiffDrive(Project project) {
        Map<String, ConfigurationComponent> diffDrives = project.getConfigurationAst().getAllConfigurationComponentByType(SC.DIFFERENTIALDRIVE);
        boolean diffDriveCompUnique = diffDrives.size() == 1;
        diffDrives.forEach((a, diffDrive) -> {
            boolean rightMotorMissing = true;
            boolean leftMotorMissing = true;
            boolean encoderMissing = false;
            for ( ConfigurationComponent configComp : project.getConfigurationAst().getConfigurationComponents().values() ) {
                if ( configComp.componentType.equals("ENCODERMOTOR") ) {
                    if ( configComp.getProperty("PORT").equals(diffDrive.getProperty("MOTOR_R")) ) {
                        rightMotorMissing = false;
                        if ( isEncoderMissing(configComp) ) {
                            encoderMissing = true;
                        }
                    }
                    if ( configComp.getProperty("PORT").equals(diffDrive.getProperty("MOTOR_L")) ) {
                        leftMotorMissing = false;
                        if ( isEncoderMissing(configComp) ) {
                            encoderMissing = true;
                        }
                    }

                }
            }
            if ( rightMotorMissing || leftMotorMissing || !diffDriveCompUnique || encoderMissing ) {
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
                if ( encoderMissing ) {
                    project.addToConfAnnotationList(blockId, NepoInfo.error("CONFIGURATION_ERROR_SENSOR_MISSING"));
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

    private void checkOmniDrive(Project project) {
        Map<String, ConfigurationComponent> omniDrives = project.getConfigurationAst().getAllConfigurationComponentByType(FischertechnikConstants.OMNIDRIVE);
        boolean omniDriveCompUnique = omniDrives.size() == 1;
        omniDrives.forEach((a, omnidrive) -> {
            boolean flMotorMissing = true;
            boolean frMotorMissing = true;
            boolean rlMotorMissing = true;
            boolean rrMotorMissing = true;
            boolean encoderMissing = false;
            for ( ConfigurationComponent configComp : project.getConfigurationAst().getConfigurationComponents().values() ) {
                if ( configComp.componentType.equals("ENCODERMOTOR") ) {
                    if ( configComp.getProperty("PORT").equals(omnidrive.getProperty("MOTOR_FL")) ) {
                        flMotorMissing = false;
                        if ( isEncoderMissing(configComp) ) {
                            encoderMissing = true;
                        }
                    }
                    if ( configComp.getProperty("PORT").equals(omnidrive.getProperty("MOTOR_FR")) ) {
                        frMotorMissing = false;
                        if ( isEncoderMissing(configComp) ) {
                            encoderMissing = true;
                        }
                    }
                    if ( configComp.getProperty("PORT").equals(omnidrive.getProperty("MOTOR_RL")) ) {
                        rlMotorMissing = false;
                        if ( isEncoderMissing(configComp) ) {
                            encoderMissing = true;
                        }
                    }
                    if ( configComp.getProperty("PORT").equals(omnidrive.getProperty("MOTOR_RR")) ) {
                        rrMotorMissing = false;
                        if ( isEncoderMissing(configComp) ) {
                            encoderMissing = true;
                        }
                    }
                }
            }
            boolean aMotorIsMissing = flMotorMissing || frMotorMissing || rlMotorMissing || rrMotorMissing || encoderMissing;
            if ( aMotorIsMissing || !omniDriveCompUnique ) {
                BlocklyProperties blocklyProperties = omnidrive.getProperty();
                String blockId = blocklyProperties.blocklyId;
                project.setResult(Key.PROGRAM_INVALID_STATEMETNS);
                project.addToErrorCounter(1, null);
                if ( aMotorIsMissing ) {
                    project.addToConfAnnotationList(blockId, NepoInfo.error("CONFIGURATION_ERROR_MOTOR_MISSING"));
                }
                if ( !omniDriveCompUnique ) {
                    project.addToConfAnnotationList(blockId, NepoInfo.error("CONFIGURATION_ERROR_DIFFDRIVE_NOT_UNIQUE"));
                }
                if ( encoderMissing ) {
                    project.addToConfAnnotationList(blockId, NepoInfo.error("CONFIGURATION_ERROR_SENSOR_MISSING"));
                }
            }
            String[] motorProperties = {omnidrive.getProperty("MOTOR_FL"), omnidrive.getProperty("MOTOR_FR"), omnidrive.getProperty("MOTOR_RL"), omnidrive.getProperty("MOTOR_RR")};
            HashSet<String> set = new HashSet<>();
            boolean foundDuplicate = false;

            for ( String str : motorProperties ) {
                if ( !set.add(str) ) {
                    foundDuplicate = true;
                    break;
                }
            }
            if ( foundDuplicate ) {
                BlocklyProperties blocklyProperties = omnidrive.getProperty();
                String blockId = blocklyProperties.blocklyId;
                project.addToErrorCounter(1, null);
                project.setResult(Key.PROGRAM_INVALID_STATEMETNS);
                project.addToConfAnnotationList(blockId, NepoInfo.error("CONFIGURATION_ERROR_OVERLAPPING_PORTS"));
            }
        });
    }


    private void checkIfPortTaken(Project project, ConfigurationComponent configurationComponent, List<String> takenPins) {
        Map<String, String> componentProperties = configurationComponent.getComponentProperties();
        try {
            for ( Map.Entry<String, List<ConfigurationComponent>> entry : configurationComponent.getSubComponents().entrySet() ) {
                for ( ConfigurationComponent subComponent : entry.getValue() ) {
                    checkIfPortTaken(project, subComponent, takenPins);
                }
            }
        } catch ( UnsupportedOperationException ignore ) {

        }
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
            ifMotorPortAddOutputPin(property.getValue(), takenPins);
        }
    }

    private void ifMotorPortAddOutputPin(String port, List<String> takenPins) {
        if ( port.equals("M1") ) {
            takenPins.add("O1");
            takenPins.add("O2");
        } else if ( port.equals("M2") ) {
            takenPins.add("O3");
            takenPins.add("O4");
        } else if ( port.equals("M3") ) {
            takenPins.add("O5");
            takenPins.add("O6");
        } else if ( port.equals("M4") ) {
            takenPins.add("O7");
            takenPins.add("O8");
        }
    }
}