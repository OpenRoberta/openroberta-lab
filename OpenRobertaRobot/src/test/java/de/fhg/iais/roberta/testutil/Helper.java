package de.fhg.iais.roberta.testutil;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.factory.AbstractRobotFactory;
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
import de.fhg.iais.roberta.jaxb.JaxbHelper;
import de.fhg.iais.roberta.robotCommunication.ICompilerWorkflow;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.hardwarecheck.generic.SimulationProgramCheckVisitor;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;

/**
 * This class is used to store helper methods for operation with JAXB objects and generation code from them.
 */
public class Helper {
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

    }

    static TestFactory robotModeFactory;

    static {
        Properties properties = Util1.loadProperties(null);
        RobertaProperties.setRobertaProperties(properties);
        robotModeFactory = new TestFactory();
    }

    /**
     * return the first and only one phrase from a given program fragment.
     *
     * @param pathToProgramXml path to a XML file, usable for {@link Class#getResourceAsStream(String)}
     * @return the first and only one phrase
     * @throws Exception
     */
    public static <V> ArrayList<ArrayList<Phrase<V>>> generateASTs(String pathToProgramXml) throws Exception {
        BlockSet project = JaxbHelper.path2BlockSet(pathToProgramXml);
        Jaxb2BlocklyProgramTransformer<V> transformer = new Jaxb2BlocklyProgramTransformer<>(robotModeFactory);
        transformer.transform(project);
        ArrayList<ArrayList<Phrase<V>>> tree = transformer.getTree();
        return tree;
    }

    /**
     * Generate AST from XML Blockly stored program
     *
     * @param pathToProgramXml
     * @return AST of the program
     * @throws Exception
     */
    public static <V> Phrase<V> generateAST(String pathToProgramXml) throws Exception {
        ArrayList<ArrayList<Phrase<V>>> tree = generateASTs(pathToProgramXml);
        return tree.get(0).get(1);
    }

}
