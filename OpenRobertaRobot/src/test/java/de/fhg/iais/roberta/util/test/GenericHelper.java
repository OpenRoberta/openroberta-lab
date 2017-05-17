package de.fhg.iais.roberta.util.test;

import java.util.ArrayList;
import java.util.List;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.factory.AbstractRobotFactory;
import de.fhg.iais.roberta.factory.ICompilerWorkflow;
import de.fhg.iais.roberta.inter.mode.action.IActorPort;
import de.fhg.iais.roberta.inter.mode.action.IBlinkMode;
import de.fhg.iais.roberta.inter.mode.action.IBrickLedColor;
import de.fhg.iais.roberta.inter.mode.action.ILightSensorActionMode;
import de.fhg.iais.roberta.inter.mode.action.IShowPicture;
import de.fhg.iais.roberta.inter.mode.action.IWorkingState;
import de.fhg.iais.roberta.inter.mode.sensor.IBrickKey;
import de.fhg.iais.roberta.inter.mode.sensor.IColorSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IGyroSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IInfraredSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.ILightSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IMotorTachoMode;
import de.fhg.iais.roberta.inter.mode.sensor.ISensorPort;
import de.fhg.iais.roberta.inter.mode.sensor.ISoundSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.ITouchSensorMode;
import de.fhg.iais.roberta.inter.mode.sensor.IUltrasonicSensorMode;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.check.hardware.SimulationProgramCheckVisitor;

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
        public List<IBlinkMode> getBlinkModes() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public IBrickLedColor getBrickLedColor(String mode) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<IBrickLedColor> getBrickLedColors() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public ILightSensorMode getLightColor(String mode) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<ILightSensorMode> getLightColors() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public ILightSensorActionMode getLightActionColor(String mode) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<ILightSensorActionMode> getLightActionColors() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public IWorkingState getWorkingState(String mode) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<IWorkingState> getWorkingStates() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public IShowPicture getShowPicture(String picture) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<IShowPicture> getShowPictures() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public IActorPort getActorPort(String port) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<IActorPort> getActorPorts() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public IBrickKey getBrickKey(String brickKey) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<IBrickKey> getBrickKeys() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public IColorSensorMode getColorSensorMode(String colorSensorMode) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<IColorSensorMode> getColorSensorModes() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public ILightSensorMode getLightSensorMode(String lightrSensorMode) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<ILightSensorMode> getLightSensorModes() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public ISoundSensorMode getSoundSensorMode(String soundSensorMode) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<ISoundSensorMode> getSoundSensorModes() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public IGyroSensorMode getGyroSensorMode(String gyroSensorMode) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<IGyroSensorMode> getGyroSensorModes() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public IInfraredSensorMode getInfraredSensorMode(String infraredSensorMode) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<IInfraredSensorMode> getInfraredSensorModes() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public IMotorTachoMode getMotorTachoMode(String motorTachoMode) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<IMotorTachoMode> getMotorTachoModes() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public IUltrasonicSensorMode getUltrasonicSensorMode(String ultrasonicSensorMode) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<IUltrasonicSensorMode> getUltrasonicSensorModes() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public ITouchSensorMode getTouchSensorMode(String mode) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<ITouchSensorMode> getTouchSensorModes() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public ISensorPort getSensorPort(String port) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public List<ISensorPort> getSensorPorts() {
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
        public Boolean isAutoconnected() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Boolean hasConfiguration() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public SimulationProgramCheckVisitor getProgramCheckVisitor(Configuration brickConfiguration) {
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

    }
}
