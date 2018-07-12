package de.fhg.iais.roberta.syntax.check.program.wedo;

import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ConfigurationBlock;
import de.fhg.iais.roberta.components.SensorType;
import de.fhg.iais.roberta.components.wedo.WeDoConfiguration;
import de.fhg.iais.roberta.syntax.action.light.LedAction;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.action.sound.PlayNoteAction;
import de.fhg.iais.roberta.syntax.action.wedo.LedOnAction;
import de.fhg.iais.roberta.syntax.check.program.RobotBrickCheckVisitor;
import de.fhg.iais.roberta.syntax.sensor.ExternalSensor;
import de.fhg.iais.roberta.typecheck.NepoInfo;
import de.fhg.iais.roberta.visitor.wedo.WeDoAstVisitor;

public class BrickCheckVisitor<V> extends RobotBrickCheckVisitor implements WeDoAstVisitor<V> {

    public BrickCheckVisitor(Configuration brickConfiguration) {
        super(brickConfiguration);
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
    public V visitLedOnAction(LedOnAction<V> ledOnAction) {
        if ( ledOnAction.getInfos().getErrorCount() == 0 ) {
            ConfigurationBlock usedConfigurationBlock =
                ((WeDoConfiguration) this.brickConfiguration).getConfigurationBlockOnPort(ledOnAction.getPort().toString());
            if ( usedConfigurationBlock == null ) {
                ledOnAction.addInfo(NepoInfo.error("CONFIGURATION_ERROR_ACTOR_MISSING"));
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
    public Void visitLedAction(LedAction<Void> ledAction) {
        return null;
    }
}
