package de.fhg.iais.roberta.util.test;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import de.fhg.iais.roberta.codegen.ICompilerWorkflow;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.factory.AbstractRobotFactory;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.action.IBrickLedColor;
import de.fhg.iais.roberta.inter.mode.action.ILightMode;
import de.fhg.iais.roberta.inter.mode.action.IShowPicture;
import de.fhg.iais.roberta.inter.mode.general.IWorkingState;
import de.fhg.iais.roberta.inter.mode.sensor.IColorSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IJoystickMode;
import de.fhg.iais.roberta.inter.mode.sensor.ILightSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.mode.sensor.SensorPort;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.visitor.validate.AbstractProgramValidatorVisitor;
import de.fhg.iais.roberta.visitor.validate.AbstractSimValidatorVisitor;

public class GenericHelperForXmlTest extends AbstractHelperForXmlTest {
    public GenericHelperForXmlTest() {
        super(new TestFactory("test", new Properties(), null), (Configuration) null);
    }

    private static class TestFactory extends AbstractRobotFactory {
        Map<String, SensorPort> sensorToPorts = IRobotFactory.getSensorPortsFromProperties(Util1.loadProperties("classpath:robotports.properties"));

        public TestFactory(String robotName, Properties robotProperties, String tempDirForUserProjects) {
            super(robotName, robotProperties);
        }

        @Override
        public ILightMode getBlinkMode(String mode) {
            return null;
        }

        @Override
        public IBrickLedColor getBrickLedColor(String mode) {
            return null;
        }

        @Override
        public ILightSensorMode getLightColor(String mode) {
            return null;
        }

        @Override
        public IWorkingState getWorkingState(String mode) {
            return null;
        }

        @Override
        public IShowPicture getShowPicture(String picture) {
            return null;
        }

        @Override
        public IActorPort getActorPort(String port) {
            return null;
        }

        @Override
        public ICompilerWorkflow getRobotCompilerWorkflow() {
            return null;
        }

        @Override
        public ICompilerWorkflow getSimCompilerWorkflow() {
            return null;
        }

        @Override
        public String getFileExtension() {
            return null;
        }

        @Override
        public AbstractSimValidatorVisitor getSimProgramCheckVisitor(Configuration brickConfiguration) {
            return null;
        }

        @Override
        public String generateCode(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) {
            return null;
        }

        @Override
        public AbstractProgramValidatorVisitor getRobotProgramCheckVisitor(Configuration brickConfiguration) {
            return null;
        }

        @Override
        public IJoystickMode getJoystickMode(String joystickMode) {
            return null;
        }

        @Override
        public IColorSensorMode getColorSensorMode(String modeName) {
            return null;
        }

        @Override
        public ISensorPort getSensorPort(String port) {
            return getSensorPortValue(port, this.sensorToPorts);
        }
    }
}
