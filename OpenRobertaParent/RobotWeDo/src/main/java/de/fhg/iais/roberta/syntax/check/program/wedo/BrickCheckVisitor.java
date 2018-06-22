package de.fhg.iais.roberta.syntax.check.program.wedo;

import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ConfigurationBlock;
import de.fhg.iais.roberta.components.SensorType;
import de.fhg.iais.roberta.components.wedo.WeDoConfiguration;
import de.fhg.iais.roberta.syntax.action.light.LightAction;
import de.fhg.iais.roberta.syntax.action.light.LightStatusAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorStopAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.sound.ToneAction;
import de.fhg.iais.roberta.syntax.check.program.RobotBrickCheckVisitor;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.syntax.sensor.generic.BrickSensor;
import de.fhg.iais.roberta.typecheck.NepoInfo;

public class BrickCheckVisitor<V> extends RobotBrickCheckVisitor {

    public BrickCheckVisitor(Configuration brickConfiguration) {
        super(brickConfiguration);
    }

    @Override
    public Void visitBrickSensor(BrickSensor<Void> brickSensor) {
        checkSensorPort(brickSensor);
        return null;
    }

    @Override
    protected void checkSensorPort(ExternalSensor<Void> sensor) {
        ConfigurationBlock usedConfigurationBlock = ((WeDoConfiguration) this.brickConfiguration).getConfigurationBlockOnPort(sensor.getPort().toString());
        if ( usedConfigurationBlock == null ) {
            sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_MISSING"));
            this.errorCount++;
        } else {
            switch ( usedConfigurationBlock.getType().toString() ) {
                case "INFRARED_SENSING":
                    if ( !usedConfigurationBlock.getType().equals(SensorType.INFRARED) ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
                        this.errorCount++;
                    }
                    break;
                case "GYRO_SENSING":
                    if ( !usedConfigurationBlock.getType().equals(SensorType.GYRO) ) {
                        sensor.addInfo(NepoInfo.error("CONFIGURATION_ERROR_SENSOR_WRONG"));
                        this.errorCount++;
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        if ( motorOnAction.getInfos().getErrorCount() == 0 ) {
            ConfigurationBlock usedConfigurationBlock =
                ((WeDoConfiguration) this.brickConfiguration).getConfigurationBlockOnPort(motorOnAction.getPort().toString());
            boolean duration = motorOnAction.getParam().getDuration() != null;
            if ( usedConfigurationBlock == null ) {
                motorOnAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
                this.errorCount++;
            } else if ( usedConfigurationBlock.getType().equals(ActorType.OTHER) && duration ) {
                motorOnAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_OTHER_NOT_SUPPORTED"));
            }
        }
        return null;
    }

    @Override
    public Void visitMotorStopAction(MotorStopAction<Void> motorStopAction) {
        if ( motorStopAction.getInfos().getErrorCount() == 0 ) {
            ConfigurationBlock usedConfigurationBlock =
                ((WeDoConfiguration) this.brickConfiguration).getConfigurationBlockOnPort(motorStopAction.getPort().toString());
            if ( usedConfigurationBlock == null ) {
                motorStopAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
                this.errorCount++;
            }
        }
        return null;
    }

    @Override
    public Void visitLightAction(LightAction<Void> lightAction) {
        if ( lightAction.getInfos().getErrorCount() == 0 ) {
            ConfigurationBlock usedConfigurationBlock =
                ((WeDoConfiguration) this.brickConfiguration).getConfigurationBlockOnPort(lightAction.getPort().toString());
            if ( usedConfigurationBlock == null ) {
                lightAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
                this.errorCount++;
            }
        }
        return null;
    }

    @Override
    public Void visitPlayNoteAction(PlayNoteAction<Void> playNoteAction) {
        if ( playNoteAction.getInfos().getErrorCount() == 0 ) {
            ConfigurationBlock usedConfigurationBlock =
                ((WeDoConfiguration) this.brickConfiguration).getConfigurationBlockOnPort(playNoteAction.getPort().toString());
            if ( usedConfigurationBlock == null ) {
                playNoteAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
                this.errorCount++;
            }
        }
        return null;
    }

    @Override
    public Void visitLightStatusAction(LightStatusAction<Void> lightStatusAction) {
        if ( lightStatusAction.getInfos().getErrorCount() == 0 ) {
            ConfigurationBlock usedConfigurationBlock =
                ((WeDoConfiguration) this.brickConfiguration).getConfigurationBlockOnPort(lightStatusAction.getPort().toString());
            if ( usedConfigurationBlock == null ) {
                lightStatusAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
                this.errorCount++;
            }
        }
        return null;
    }

    @Override
    public Void visitToneAction(ToneAction<Void> toneAction) {
        if ( toneAction.getInfos().getErrorCount() == 0 ) {
            ConfigurationBlock usedConfigurationBlock =
                ((WeDoConfiguration) this.brickConfiguration).getConfigurationBlockOnPort(toneAction.getPort().toString());
            if ( usedConfigurationBlock == null ) {
                toneAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
                this.errorCount++;
            }
        }
        return null;
    }
}
