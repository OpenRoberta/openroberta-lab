package de.fhg.iais.roberta.syntax.check.hardware.arduino.arduino;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import de.fhg.iais.roberta.components.ConfigurationBlock;
import de.fhg.iais.roberta.components.UsedConfigurationBlock;
import de.fhg.iais.roberta.components.UsedSensor;
import de.fhg.iais.roberta.components.arduino.ArduinoConfiguration;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.ExternalLedOffAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.ExternalLedOnAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.LedOffAction;
import de.fhg.iais.roberta.syntax.actors.arduino.mbot.LedOnAction;
import de.fhg.iais.roberta.syntax.check.hardware.RobotUsedHardwareCollectorVisitor;
import de.fhg.iais.roberta.syntax.sensor.generic.EncoderSensor;
import de.fhg.iais.roberta.util.Quadruplet;
import de.fhg.iais.roberta.visitors.arduino.ArduinoAstVisitor;

/**
 * This visitor collects information for used actors and sensors in blockly program.
 *
 * @author eovchinnikova
 */
public class UsedHardwareCollectorVisitor extends RobotUsedHardwareCollectorVisitor implements ArduinoAstVisitor<Void> {

    protected final Set<UsedConfigurationBlock> usedConfigurationBlocks = new LinkedHashSet<>();

    ArduinoConfiguration configuration;

    public UsedHardwareCollectorVisitor(ArrayList<ArrayList<Phrase<Void>>> phrasesSet, ArduinoConfiguration configuration) {
        super(configuration);
        this.configuration = configuration;
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
    public Void visitLedOnAction(LedOnAction<Void> ledOnAction) {
        return null;
    }

    @Override
    public Void visitLedOffAction(LedOffAction<Void> ledOffAction) {
        return null;
    }

    @Override
    public Void visitExternalLedOnAction(ExternalLedOnAction<Void> externalLedOnAction) {
        return null;
    }

    @Override
    public Void visitExternalLedOffAction(ExternalLedOffAction<Void> externalLedOffAction) {
        return null;
    }

    @Override
    public Void visitEncoderSensor(EncoderSensor<Void> encoderSensor) {
        return null;
    }
}
