package de.fhg.iais.roberta.util.test;

import java.util.ArrayList;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.factory.AbstractRobotFactory;
import de.fhg.iais.roberta.factory.ICompilerWorkflow;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.action.IBlinkMode;
import de.fhg.iais.roberta.inter.mode.action.IBrickLedColor;
import de.fhg.iais.roberta.inter.mode.action.ILightSensorActionMode;
import de.fhg.iais.roberta.inter.mode.action.IShowPicture;
import de.fhg.iais.roberta.inter.mode.general.IWorkingState;
import de.fhg.iais.roberta.inter.mode.sensor.IBrickKey;
import de.fhg.iais.roberta.inter.mode.sensor.IColorSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IJoystickMode;
import de.fhg.iais.roberta.inter.mode.sensor.ILightSensorMode;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.program.RobotCommonCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.RobotSimulationCheckVisitor;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;

public class GenericHelperForXmlTest extends AbstractHelperForXmlTest {
    public GenericHelperForXmlTest() {
        super(new TestFactory(new RobertaProperties(Util1.loadProperties(null))), null);
    }

    private static class TestFactory extends AbstractRobotFactory {

        public TestFactory(RobertaProperties robertaProperties) {
            super(robertaProperties);
        }

        @Override
        public IBlinkMode getBlinkMode(String mode) {
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
        public ILightSensorActionMode getLightActionColor(String mode) {
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
        public IBrickKey getBrickKey(String brickKey) {
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
        public String getProgramToolboxBeginner() {
            return null;
        }

        @Override
        public String getProgramToolboxExpert() {
            return null;
        }

        @Override
        public String getProgramDefault() {
            return null;
        }

        @Override
        public String getConfigurationToolbox() {
            return null;
        }

        @Override
        public String getConfigurationDefault() {
            return null;
        }

        @Override
        public String getRealName() {
            return null;
        }

        @Override
        public Boolean hasSim() {
            return null;
        }

        @Override
        public String getInfo() {
            return null;
        }

        @Override
        public Boolean isBeta() {
            return null;
        }

        @Override
        public Boolean hasConfiguration() {
            return null;
        }

        @Override
        public RobotSimulationCheckVisitor getSimProgramCheckVisitor(Configuration brickConfiguration) {
            return null;
        }

        @Override
        public String getGroup() {
            return null;
        }

        @Override
        public String generateCode(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) {
            return null;
        }

        @Override
        public RobotCommonCheckVisitor getRobotProgramCheckVisitor(Configuration brickConfiguration) {
            return null;
        }

        @Override
        public IJoystickMode getJoystickMode(String joystickMode) {
            return null;
        }

        @Override
        public String getConnectionType() {
            return null;
        }

        @Override
        public String getVendorId() {
            return null;
        }

        @Override
        public String getCommandline() {
            return null;
        }

        @Override
        public String getSignature() {
            return null;
        }

        @Override
        public IColorSensorMode getColorSensorMode(String modeName) {
            return null;
        }
    }
}
