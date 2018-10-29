package de.fhg.iais.roberta.visitor.collect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import de.fhg.iais.roberta.components.ActorType;
import de.fhg.iais.roberta.components.ConfigurationBlock;
import de.fhg.iais.roberta.components.UsedActor;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.components.wedo.WeDoConfiguration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;

/**
 * This visitor collects information for used actors and sensors in blockly program.
 *
 * @author eovchinnikova
 */
public class WedoUsedHardwareCollectorVisitor extends AbstractUsedHardwareCollectorVisitor {

    protected final Map<String, ConfigurationBlock> usedConfigurationBlocks = new HashMap<String, ConfigurationBlock>();

    WeDoConfiguration configuration;

    public WedoUsedHardwareCollectorVisitor(ArrayList<ArrayList<Phrase<Void>>> phrasesSet, WeDoConfiguration brickConfiguration) {
        super(brickConfiguration);
        this.configuration = brickConfiguration;
        check(phrasesSet);
    }

    public Set<UsedSensor> getTimer() {
        return this.usedSensors;
    }

    public Map<String, ConfigurationBlock> getUsedConfigurationBlocks() {
        Map<String, ConfigurationBlock> configurationBlocks = this.configuration.getConfigurationBlocks();
        configurationBlocks.entrySet().stream().forEach(
            configurationBlock -> this.usedConfigurationBlocks.put(
                configurationBlock.getValue().getConfName(),
                new ConfigurationBlock(
                    configurationBlock.getValue().getConfType(),
                    configurationBlock.getValue().getConfName(),
                    configurationBlock.getValue().getConfPorts())));

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
}
