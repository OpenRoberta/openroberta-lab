package de.fhg.iais.roberta.factory;

import java.util.ArrayList;

import de.fhg.iais.roberta.codegen.ArduinoCompilerWorkflow;
import de.fhg.iais.roberta.codegen.ICompilerWorkflow;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.arduino.ArduinoConfiguration;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.action.IShowPicture;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.mode.action.ActorPort;
import de.fhg.iais.roberta.mode.sensor.SensorPort;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.visitor.codegen.ArduinoCppVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractBrickValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractSimValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.ArduinoBrickValidatorVisitor;

public abstract class AbstractArduinoFactory extends AbstractRobotFactory {
    private final ArduinoCompilerWorkflow compilerWorkflow;

    public AbstractArduinoFactory(PluginProperties pluginProperties) {
        super(pluginProperties);
        addBlockTypesFromProperties("arduino", Util1.loadProperties("classpath:arduino.properties"));
        this.compilerWorkflow = new ArduinoCompilerWorkflow(pluginProperties);
    }

    public SensorPort getSensorName(String port) {
        return new SensorPort(port, port);
    }

    public ActorPort getActorName(String port) {
        return new ActorPort(port, port);
    }

    @Override
    public ISensorPort getSensorPort(String port) {
        return getSensorName(port);
    }

    @Override
    public IActorPort getActorPort(String port) {
        return getActorName(port);
    }

    @Override
    public IShowPicture getShowPicture(String picture) {
        return null;
    }

    @Override
    public ICompilerWorkflow getRobotCompilerWorkflow() {
        return this.compilerWorkflow;
    }

    @Override
    public ICompilerWorkflow getSimCompilerWorkflow() {
        return null;
    }

    @Override
    public String getFileExtension() {
        return "ino";
    }

    @Override
    public AbstractSimValidatorVisitor getSimProgramCheckVisitor(Configuration brickConfiguration) {
        return null;
    }

    @Override
    public AbstractBrickValidatorVisitor getRobotProgramCheckVisitor(Configuration brickConfiguration) {
        return new ArduinoBrickValidatorVisitor(brickConfiguration);
    }

    @Override
    public String generateCode(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) {
        return ArduinoCppVisitor.generate((ArduinoConfiguration) brickConfiguration, phrasesSet, withWrapping);
    }
}
