package de.fhg.iais.roberta.worker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    private static final List<String> NON_BLOCKING_PROPERTIES = Collections.unmodifiableList(Arrays.asList("MOTOR_FL", "MOTOR_FR", "MOTOR_RL", "MOTOR_RR", "MOTOR_L", "MOTOR_R", "BRICK_WHEEL_DIAMETER", "BRICK_TRACK_WIDTH", "WHEEL_BASE", "VCC", "GND", "DETECTORS", "RESOLUTION", "XSTART", "XEND", "YSTART", "YEND", "NUMBERLINES", "MINIMUM", "MAXIMUM", "COLOUR", "TOLERANCE"));
    private List<String> takenPins;
    private List<String> usedI2CSensors;

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
        this.takenPins = new ArrayList<>();
        for ( ConfigurationComponent configurationComponent : project.getConfigurationAst().getConfigurationComponents().values() ) {
            checkIfPortTaken(project, configurationComponent);
            if ( configurationComponent.componentType.equals("I2C") ) {
                checkI2CSensors(project, configurationComponent);
            }
        }
        checkDiffDrive(project);
        checkOmniDrive(project);
    }

    private void checkI2CSensors(Project project, ConfigurationComponent configurationComponent) {
        try {
            List<ConfigurationComponent> i2cSensors = configurationComponent.getSubComponents().get("BUS");
            if ( i2cSensors.size() > 2 ) {
                BlocklyProperties blocklyProperties = configurationComponent.getProperty();
                String blockId = blocklyProperties.blocklyId;
                project.addToErrorCounter(1, null);
                project.setResult(Key.PROGRAM_INVALID_STATEMETNS);
                project.addToConfAnnotationList(blockId, NepoInfo.error("CONFIGURATION_ERROR_TOO_MANY_SUB_SENSORS"));
            }
            checkBusConnections(project, i2cSensors);
        } catch ( Exception e ) {
        }
    }

    private void checkBusConnections(Project project, List<ConfigurationComponent> i2cSensors) {
        Set<String> usedI2CSensors = new HashSet<>();
        for ( ConfigurationComponent i2cSensor : i2cSensors ) {
            BlocklyProperties blocklyProperties = i2cSensor.getProperty();
            String blockId = blocklyProperties.blocklyId;
            if ( !usedI2CSensors.contains(i2cSensor.componentType) && !i2cSensor.componentType.equals(SC.ENCODER) ) {
                usedI2CSensors.add(i2cSensor.componentType);
            } else {
                project.addToErrorCounter(1, null);
                project.setResult(Key.PROGRAM_INVALID_STATEMETNS);
                if ( i2cSensor.componentType.equals(SC.ENCODER) ) {
                    project.addToConfAnnotationList(blockId, NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
                } else {
                    project.addToConfAnnotationList(blockId, NepoInfo.error("CONFIGURATION_ERROR_DIFFDRIVE_NOT_UNIQUE"));
                }
            }
        }
    }

    private boolean isEncoderMissing(ConfigurationComponent motor) {
        try {
            motor.getSubComponents();
        } catch (Exception e) {
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
            for (ConfigurationComponent configComp : project.getConfigurationAst().getConfigurationComponents().values()) {
                if (configComp.componentType.equals("ENCODERMOTOR")) {
                    if (configComp.getProperty("PORT").equals(diffDrive.getProperty("MOTOR_R"))) {
                        rightMotorMissing = false;
                        if (isEncoderMissing(configComp)) {
                            encoderMissing = true;
                        }
                    }
                    if (configComp.getProperty("PORT").equals(diffDrive.getProperty("MOTOR_L"))) {
                        leftMotorMissing = false;
                        if (isEncoderMissing(configComp)) {
                            encoderMissing = true;
                        }
                    }

                }
            }
            if (rightMotorMissing || leftMotorMissing || !diffDriveCompUnique || encoderMissing) {
                BlocklyProperties blocklyProperties = diffDrive.getProperty();
                String blockId = blocklyProperties.blocklyId;
                project.setResult(Key.PROGRAM_INVALID_STATEMETNS);
                project.addToErrorCounter(1, null);
                if (leftMotorMissing) {
                    project.addToConfAnnotationList(blockId, NepoInfo.error("CONFIGURATION_ERROR_MOTOR_LEFT_MISSING"));
                }
                if (rightMotorMissing) {
                    project.addToConfAnnotationList(blockId, NepoInfo.error("CONFIGURATION_ERROR_MOTOR_RIGHT_MISSING"));
                }
                if (!diffDriveCompUnique) {
                    project.addToConfAnnotationList(blockId, NepoInfo.error("CONFIGURATION_ERROR_DIFFDRIVE_NOT_UNIQUE"));
                }
                if (encoderMissing) {
                    project.addToConfAnnotationList(blockId, NepoInfo.error("CONFIGURATION_ERROR_SENSOR_MISSING"));
                }
            }
            if (diffDrive.getProperty("MOTOR_L").equals(diffDrive.getProperty(("MOTOR_R")))) {
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
            for (ConfigurationComponent configComp : project.getConfigurationAst().getConfigurationComponents().values()) {
                if (configComp.componentType.equals("ENCODERMOTOR")) {
                    if (configComp.getProperty("PORT").equals(omnidrive.getProperty("MOTOR_FL"))) {
                        flMotorMissing = false;
                        if (isEncoderMissing(configComp)) {
                            encoderMissing = true;
                        }
                    }
                    if (configComp.getProperty("PORT").equals(omnidrive.getProperty("MOTOR_FR"))) {
                        frMotorMissing = false;
                        if (isEncoderMissing(configComp)) {
                            encoderMissing = true;
                        }
                    }
                    if (configComp.getProperty("PORT").equals(omnidrive.getProperty("MOTOR_RL"))) {
                        rlMotorMissing = false;
                        if (isEncoderMissing(configComp)) {
                            encoderMissing = true;
                        }
                    }
                    if (configComp.getProperty("PORT").equals(omnidrive.getProperty("MOTOR_RR"))) {
                        rrMotorMissing = false;
                        if (isEncoderMissing(configComp)) {
                            encoderMissing = true;
                        }
                    }
                }
            }
            boolean aMotorIsMissing = flMotorMissing || frMotorMissing || rlMotorMissing || rrMotorMissing || encoderMissing;
            if (aMotorIsMissing || !omniDriveCompUnique) {
                BlocklyProperties blocklyProperties = omnidrive.getProperty();
                String blockId = blocklyProperties.blocklyId;
                project.setResult(Key.PROGRAM_INVALID_STATEMETNS);
                project.addToErrorCounter(1, null);
                if (aMotorIsMissing) {
                    project.addToConfAnnotationList(blockId, NepoInfo.error("CONFIGURATION_ERROR_MOTOR_MISSING"));
                }
                if (!omniDriveCompUnique) {
                    project.addToConfAnnotationList(blockId, NepoInfo.error("CONFIGURATION_ERROR_DIFFDRIVE_NOT_UNIQUE"));
                }
                if (encoderMissing) {
                    project.addToConfAnnotationList(blockId, NepoInfo.error("CONFIGURATION_ERROR_SENSOR_MISSING"));
                }
            }
            String[] motorProperties = {omnidrive.getProperty("MOTOR_FL"), omnidrive.getProperty("MOTOR_FR"), omnidrive.getProperty("MOTOR_RL"), omnidrive.getProperty("MOTOR_RR")};
            HashSet<String> set = new HashSet<>();
            boolean foundDuplicate = false;

            for (String str : motorProperties) {
                if (!set.add(str)) {
                    foundDuplicate = true;
                    break;
                }
            }
            if (foundDuplicate) {
                BlocklyProperties blocklyProperties = omnidrive.getProperty();
                String blockId = blocklyProperties.blocklyId;
                project.addToErrorCounter(1, null);
                project.setResult(Key.PROGRAM_INVALID_STATEMETNS);
                project.addToConfAnnotationList(blockId, NepoInfo.error("CONFIGURATION_ERROR_OVERLAPPING_PORTS"));
            }
        });
    }


    private void checkIfPortTaken(Project project, ConfigurationComponent configurationComponent) {
        Map<String, String> componentProperties = configurationComponent.getComponentProperties();
        try {
            for (Map.Entry<String, List<ConfigurationComponent>> entry : configurationComponent.getSubComponents().entrySet()) {
                for (ConfigurationComponent subComponent : entry.getValue()) {
                    checkIfPortTaken(project, subComponent);
                }
            }
        } catch (UnsupportedOperationException ignore) {

        }
        for (Map.Entry<String, String> property : componentProperties.entrySet()) {
            if (NON_BLOCKING_PROPERTIES.contains(property.getKey())) {
                continue;
            }
            addErrorIfTaken(configurationComponent, property.getValue(), project);
            ifMotorPortAddOutputPin(configurationComponent, property.getValue(), project);
        }
    }

    private void ifMotorPortAddOutputPin(ConfigurationComponent configurationComponent, String port, Project project) {
        if (port.equals("M1")) {
            addErrorIfTaken(configurationComponent, "O1", project);
            addErrorIfTaken(configurationComponent, "O2", project);
        } else if (port.equals("M2")) {
            addErrorIfTaken(configurationComponent, "O3", project);
            addErrorIfTaken(configurationComponent, "O4", project);
        } else if (port.equals("M3")) {
            addErrorIfTaken(configurationComponent, "O5", project);
            addErrorIfTaken(configurationComponent, "O6", project);
        } else if (port.equals("M4")) {
            addErrorIfTaken(configurationComponent, "O7", project);
            addErrorIfTaken(configurationComponent, "O8", project);
        }
    }

    private void addErrorIfTaken(ConfigurationComponent configurationComponent, String port, Project project) {
        if (this.takenPins.contains(port)) {
            BlocklyProperties blocklyProperties = configurationComponent.getProperty();
            String blockId = blocklyProperties.blocklyId;
            project.addToErrorCounter(1, null);
            project.setResult(Key.PROGRAM_INVALID_STATEMETNS);
            project.addToConfAnnotationList(blockId, NepoInfo.error("CONFIGURATION_ERROR_OVERLAPPING_PORTS"));
            return;
        }
        this.takenPins.add(port);
    }
}