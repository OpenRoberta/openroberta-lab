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
import de.fhg.iais.roberta.syntax.check.program.RobotBrickCheckVisitor;
import de.fhg.iais.roberta.syntax.check.program.RobotSimulationCheckVisitor;

public class GenericHelper extends Helper {
    public GenericHelper() {
        this.robotFactory = new TestFactory();
    }

    private static class TestFactory extends AbstractRobotFactory {

        @Override
        public IBlinkMode getBlinkMode(String mode) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public IBrickLedColor getBrickLedColor(String mode) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public ILightSensorMode getLightColor(String mode) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public ILightSensorActionMode getLightActionColor(String mode) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public IWorkingState getWorkingState(String mode) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public IShowPicture getShowPicture(String picture) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public IActorPort getActorPort(String port) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public IBrickKey getBrickKey(String brickKey) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public ICompilerWorkflow getRobotCompilerWorkflow() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public ICompilerWorkflow getSimCompilerWorkflow() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getFileExtension() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getProgramToolboxBeginner() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getProgramToolboxExpert() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getProgramDefault() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getConfigurationToolbox() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getConfigurationDefault() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getRealName() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Boolean hasSim() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getInfo() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Boolean isBeta() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Boolean hasConfiguration() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public RobotSimulationCheckVisitor getSimProgramCheckVisitor(Configuration brickConfiguration) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getGroup() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String generateCode(Configuration brickConfiguration, ArrayList<ArrayList<Phrase<Void>>> phrasesSet, boolean withWrapping) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public RobotBrickCheckVisitor getRobotProgramCheckVisitor(Configuration brickConfiguration) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public IJoystickMode getJoystickMode(String joystickMode) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getConnectionType() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getVendorId() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getCommandline() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public String getSignature() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public IColorSensorMode getColorSensorMode(String modeName) {
            // TODO Auto-generated method stub
            return null;
        }

    }
}
