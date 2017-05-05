package de.fhg.iais.roberta.util.test;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.blockly.generated.Instance;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.factory.AbstractRobotFactory;
import de.fhg.iais.roberta.factory.IRobotFactory;
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
import de.fhg.iais.roberta.syntax.blocksequence.Location;
import de.fhg.iais.roberta.syntax.check.hardware.SimulationProgramCheckVisitor;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;

/**
 * This class is used to store helper methods for operation with JAXB objects and generation code from them.
 */
public class Helper {
    private IRobotFactory robotFactory;
    private Configuration robotConfiguration;

    public Helper() {
        Properties properties = Util1.loadProperties(null);
        RobertaProperties.setRobertaProperties(properties);
        this.robotFactory = new TestFactory();
    }

    public IRobotFactory getRobotFactory() {
        return this.robotFactory;
    }

    public void setRobotFactory(IRobotFactory robotFactory) {
        this.robotFactory = robotFactory;
    }

    public Configuration getRobotConfiguration() {
        return this.robotConfiguration;
    }

    public void setRobotConfiguration(Configuration robotConfiguration) {
        this.robotConfiguration = robotConfiguration;
    }

    /**
     * return the first and only one phrase from a given program fragment.
     *
     * @param pathToProgramXml path to a XML file, usable for {@link Class#getResourceAsStream(String)}
     * @return the first and only one phrase
     * @throws Exception
     */
    public <V> ArrayList<ArrayList<Phrase<V>>> generateASTs(String pathToProgramXml) throws Exception {
        BlockSet project = JaxbHelper.path2BlockSet(pathToProgramXml);
        Jaxb2BlocklyProgramTransformer<V> transformer = new Jaxb2BlocklyProgramTransformer<>(this.robotFactory);
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
    public <V> Phrase<V> generateAST(String pathToProgramXml) throws Exception {
        ArrayList<ArrayList<Phrase<V>>> tree = generateASTs(pathToProgramXml);
        return tree.get(0).get(1);
    }

    /**
     * return the jaxb transformer for a given program fragment.
     *
     * @param pathToProgramXml path to a XML file, usable for {@link Class#getResourceAsStream(String)}
     * @return jaxb transformer
     * @throws Exception
     */
    public Jaxb2BlocklyProgramTransformer<Void> generateTransformer(String pathToProgramXml) throws Exception {
        BlockSet project = JaxbHelper.path2BlockSet(pathToProgramXml);

        Jaxb2BlocklyProgramTransformer<Void> transformer = new Jaxb2BlocklyProgramTransformer<>(this.robotFactory);
        transformer.transform(project);
        return transformer;
    }

    /**
     * return the toString representation for a jaxb transformer for a given program fragment.
     *
     * @param pathToProgramXml path to a XML file, usable for {@link Class#getResourceAsStream(String)}
     * @return the toString representation for a jaxb transformer
     * @throws Exception
     */
    public String generateTransformerString(String pathToProgramXml) throws Exception {
        return generateTransformer(pathToProgramXml).toString();
    }

    /**
     * Asserts if transformation of Blockly XML saved program is correct.<br>
     * <br>
     * <b>Transformation:</b>
     * <ol>
     * <li>XML to JAXB</li>
     * <li>JAXB to AST</li>
     * <li>AST to JAXB</li>
     * <li>JAXB to XML</li>
     * </ol>
     * Return true if the first XML is equal to second XML.
     *
     * @param fileName of the program
     * @throws Exception
     */
    public void assertTransformationIsOk(String fileName) throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = generateTransformer(fileName);
        JAXBContext jaxbContext = JAXBContext.newInstance(BlockSet.class);
        Marshaller m = jaxbContext.createMarshaller();
        m.setProperty(Marshaller.JAXB_FRAGMENT, true);
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        BlockSet blockSet = astToJaxb(transformer.getTree());
        //        m.marshal(blockSet, System.out); // only needed for EXTREME debugging
        StringWriter writer = new StringWriter();
        m.marshal(blockSet, writer);
        String t = Resources.toString(Helper.class.getResource(fileName), Charsets.UTF_8);
        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = XMLUnit.compareXML(writer.toString(), t);
        //        System.out.println(diff.toString()); // only needed for EXTREME debugging
        Assert.assertTrue(diff.identical());
    }

    public BlockSet astToJaxb(ArrayList<ArrayList<Phrase<Void>>> astProgram) {
        BlockSet blockSet = new BlockSet();

        Instance instance = null;
        for ( ArrayList<Phrase<Void>> tree : astProgram ) {
            for ( Phrase<Void> phrase : tree ) {
                if ( phrase.getKind().hasName("LOCATION") ) {
                    blockSet.getInstance().add(instance);
                    instance = new Instance();
                    instance.setX(((Location<Void>) phrase).getX());
                    instance.setY(((Location<Void>) phrase).getY());
                }
                instance.getBlock().add(phrase.astToBlock());
            }
        }
        blockSet.getInstance().add(instance);
        return blockSet;
    }

    /**
     * Asserts if two XML string are identical by ignoring white space.
     *
     * @param arg1 first XML string
     * @param arg2 second XML string
     * @throws Exception
     */
    public void assertXML(String arg1, String arg2) throws Exception {
        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = XMLUnit.compareXML(arg1, arg2);
        Assert.assertTrue(diff.toString(), diff.identical());
    }

    /**
     * Asserts if two XML string are identical by ignoring white space.
     *
     * @param arg1 first XML string
     * @param arg2 second XML string
     * @throws Exception
     */
    public void assertXMLtransformation(String xml) throws Exception {
        BlockSet program = JaxbHelper.xml2BlockSet(xml);
        //TODO: change the static ev3modeFactory
        //        EV3Factory ev3ModeFactory = new EV3Factory(null);
        Jaxb2BlocklyProgramTransformer<Void> transformer = new Jaxb2BlocklyProgramTransformer<>(null);
        transformer.transform(program);

        BlockSet blockSet = astToJaxb(transformer.getTree());
        String newXml = jaxbToXml(blockSet);

        XMLUnit.setIgnoreWhitespace(true);
        Diff diff = XMLUnit.compareXML(xml, newXml);
        Assert.assertTrue(diff.toString(), diff.identical());
    }

    public String jaxbToXml(BlockSet blockSet) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(BlockSet.class);
        Marshaller m = jaxbContext.createMarshaller();
        m.setProperty(Marshaller.JAXB_FRAGMENT, true);
        StringWriter writer = new StringWriter();
        m.marshal(blockSet, writer);
        return writer.toString();
    }

    /**
     * Generate java code as string from a given program fragment. Do not prepend and append wrappings.
     *
     * @param pathToProgramXml path to a XML file, usable for {@link Class#getResourceAsStream(String)}
     * @return the code fragment as string
     * @throws Exception
     */
    private String generateString(String pathToProgramXml, boolean wrapping) throws Exception {
        Jaxb2BlocklyProgramTransformer<Void> transformer = generateTransformer(pathToProgramXml);
        return this.robotFactory.generateCode(this.robotConfiguration, transformer.getTree(), wrapping);
    }

    /**
     * Assert that Java code generated from Blockly XML program is correct.<br>
     * All white space are ignored!
     *
     * @param correctJavaCode correct java code
     * @param fileName of the program we want to generate java code
     * @throws Exception
     */
    public void assertCodeIsOk(String correctJavaCode, String fileName, boolean wrapping) throws Exception {
        Assert.assertEquals(correctJavaCode.replaceAll("\\s+", ""), generateString(fileName, wrapping).replaceAll("\\s+", ""));
    }

    private class TestFactory extends AbstractRobotFactory {

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
