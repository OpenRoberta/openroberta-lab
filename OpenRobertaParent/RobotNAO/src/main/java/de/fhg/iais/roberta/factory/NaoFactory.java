package de.fhg.iais.roberta.factory;

import java.util.ArrayList;
import java.util.Map;

import de.fhg.iais.roberta.codegen.ICompilerWorkflow;
import de.fhg.iais.roberta.codegen.NaoCompilerWorkflow;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.nao.NAOConfiguration;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.action.IShowPicture;
import de.fhg.iais.roberta.inter.mode.general.IMode;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.mode.action.ActorPort;
import de.fhg.iais.roberta.mode.action.Language;
import de.fhg.iais.roberta.mode.sensor.SensorPort;
import de.fhg.iais.roberta.mode.sensor.nao.DetectedFaceMode;
import de.fhg.iais.roberta.mode.sensor.nao.DetectedMarkMode;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.BlocklyComment;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.sensor.GetSampleType;
import de.fhg.iais.roberta.syntax.sensor.Sensor;
import de.fhg.iais.roberta.syntax.sensor.SensorMetaDataBean;
import de.fhg.iais.roberta.syntax.sensor.nao.DetectFace;
import de.fhg.iais.roberta.syntax.sensor.nao.DetectedMark;
import de.fhg.iais.roberta.syntax.sensor.nao.ElectricCurrent;
import de.fhg.iais.roberta.syntax.sensor.nao.FsrSensor;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.visitor.codegen.NaoPythonVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractProgramValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractSimValidatorVisitor;

public class NaoFactory extends AbstractRobotFactory {
    Map<String, SensorPort> sensorToPorts = IRobotFactory.getSensorPortsFromProperties(Util1.loadProperties("classpath:NAOports.properties"));
    Map<String, ActorPort> actorToPorts = IRobotFactory.getActorPortsFromProperties(Util1.loadProperties("classpath:NAOports.properties"));

    public NaoFactory(PluginProperties pluginProperties) {
        super(pluginProperties);
    }

    @Override
    public ISensorPort getSensorPort(String port) {
        return getSensorPortValue(port, this.sensorToPorts);
    }

    @Override
    public IActorPort getActorPort(String port) {
        return getActorPortValue(port, this.actorToPorts);
    }

    public IMode getDetectMarkMode(String mode) {
        return IRobotFactory.getModeValue(mode, DetectedMarkMode.class);
    }

    public IMode getDetectFaceMode(String mode) {
        return IRobotFactory.getModeValue(mode, DetectedFaceMode.class);
    }

    @Override
    public IShowPicture getShowPicture(String picture) {
        return null;
    }

    @Override
    public String getFileExtension() {
        return "py";
    }

    @Override
    public ICompilerWorkflow getRobotCompilerWorkflow() {
        return new NaoCompilerWorkflow(pluginProperties);
    }

    @Override
    public ICompilerWorkflow getSimCompilerWorkflow() {
        return null;
    }

    @Override
    public AbstractSimValidatorVisitor getSimProgramCheckVisitor(Configuration brickConfiguration) {
        return null;
    }

    @Override
    public AbstractProgramValidatorVisitor getRobotProgramCheckVisitor(Configuration brickConfiguration) {
        return null;
    }

    @Override
    public String generateCode(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) {
        return NaoPythonVisitor.generate((NAOConfiguration) brickConfiguration, phrasesSet, withWrapping, Language.GERMAN);
    }

    @Override
    public Sensor<?> createSensor(
        GetSampleType sensorType,
        String port,
        String slot,
        boolean isPortInMutation,
        BlocklyBlockProperties properties,
        BlocklyComment comment) {
        SensorMetaDataBean sensorMetaDataBean;
        switch ( sensorType.getSensorType() ) {
            case BlocklyConstants.ELECTRIC_CURRENT:
                sensorMetaDataBean =
                    new SensorMetaDataBean(getSensorPort(port), getPlaceholderSensorMode(sensorType.getSensorMode()), getSlot(slot), isPortInMutation);
                return ElectricCurrent.make(sensorMetaDataBean, properties, comment);
            case BlocklyConstants.FSR:
                sensorMetaDataBean =
                    new SensorMetaDataBean(getSensorPort(port), getPlaceholderSensorMode(sensorType.getSensorMode()), getSlot(slot), isPortInMutation);
                return FsrSensor.make(sensorMetaDataBean, properties, comment);
            case BlocklyConstants.DETECT_MARK:
                sensorMetaDataBean =
                    new SensorMetaDataBean(getSensorPort(port), getDetectMarkMode(sensorType.getSensorMode()), getSlot(slot), isPortInMutation);
                return DetectedMark.make(sensorMetaDataBean, properties, comment);
            case BlocklyConstants.DETECT_FACE:
                sensorMetaDataBean =
                    new SensorMetaDataBean(getSensorPort(port), getDetectFaceMode(sensorType.getSensorMode()), getSlot(slot), isPortInMutation);
                return DetectFace.make(sensorMetaDataBean, properties, comment);
            default:
                return super.createSensor(sensorType, port, slot, isPortInMutation, properties, comment);
        }
    }
}
