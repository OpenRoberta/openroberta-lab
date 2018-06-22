package de.fhg.iais.roberta.syntax.check.hardware.wedo;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.ConfigurationBlock;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedConfigurationBlock;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.components.wedo.WeDoConfiguration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.syntax.check.hardware.RobotUsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.util.Quadruplet;

/**
 * This visitor collects information for used actors and sensors in blockly program.
 *
 * @author eovchinnikova
 */
public class UsedHardwareCollectorVisitor extends RobotUsedHardwareCollectorVisitor {

    protected final Set<UsedConfigurationBlock> usedConfigurationBlocks = new LinkedHashSet<>();

    WeDoConfiguration configuration;

    public UsedHardwareCollectorVisitor(ArrayList<ArrayList<Phrase<Void>>> phrasesSet, WeDoConfiguration brickConfiguration) {
        super(brickConfiguration);
        this.configuration = brickConfiguration;
        check(phrasesSet);
    }

    public Set<UsedSensor> getTimer() {
        return this.usedSensors;
    }

    public Set<UsedConfigurationBlock> getUsedConfigurationBlocks() {
        for ( Quadruplet<ConfigurationBlock, String, List<String>, List<String>> configurationBlock : this.configuration.getConfigurationBlocks() ) {
            this.usedConfigurationBlocks.add(
                new UsedConfigurationBlock(
                    this.configuration.getConfigurationBlockType(configurationBlock),
                    this.configuration.getBlockName(configurationBlock),
                    this.configuration.getPorts(configurationBlock),
                    this.configuration.getPins(configurationBlock)));
        }
        return this.usedConfigurationBlocks;
    }

    @Override
    public Void visitMotorOnAction(MotorOnAction<Void> motorOnAction) {
        motorOnAction.getParam().getSpeed().visit(this);
        if ( motorOnAction.getParam().getDuration() != null ) {
            motorOnAction.getDurationValue().visit(this);
        }
        if ( this.brickConfiguration != null ) {
            this.usedActors.add(new UsedActor(motorOnAction.getPort(), ActorType.MOTOR));
        }
        return null;
    }

    UsedConfigurationBlock getConfigurationBlock(String name) {
        for ( UsedConfigurationBlock usedConfigurationBlock : this.usedConfigurationBlocks ) {
            if ( usedConfigurationBlock.getBlockName().equals(name) ) {
                return usedConfigurationBlock;
            }
        }
        return null;
    }
}
