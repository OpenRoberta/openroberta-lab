package de.fhg.iais.roberta.util.test.mbed;

import java.util.Arrays;
import java.util.Map;

import org.junit.Assert;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.factory.Calliope2016Factory;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.visitor.codegen.CalliopeCppVisitor;
import de.fhg.iais.roberta.visitor.codegen.MbedSimVisitor;
import de.fhg.iais.roberta.visitor.codegen.MbedStackMachineVisitor;

/**
 * This class is used to store helper methods for operation with JAXB objects and generation code from them.
 */
public class HelperCalliopeForXmlTest extends de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest {

    public HelperCalliopeForXmlTest() {
        super(
            new Calliope2016Factory(new PluginProperties("calliope2016", "", "", Util1.loadProperties("classpath:/calliope2016.properties"))),
            makeConfiguration());
    }

    public static Configuration makeConfiguration() {
        Map<String, String> motorMproperties = createMap("NAME", "M", "VAR", "W", "CONNECTOR", "1");
        ConfigurationComponent motorM = new ConfigurationComponent("LARGE", true, "M", BlocklyConstants.NO_SLOT, "M", motorMproperties);

        Map<String, String> keySensorProperties = createMap("NAME", "T", "VAR", "W");
        ConfigurationComponent keySensor = new ConfigurationComponent("TOUCH", false, "T", BlocklyConstants.NO_SLOT, "T", keySensorProperties);

        Map<String, String> gyroSensorProperties = createMap("NAME", "G", "VAR", "W", "CONNECTOR", "1");
        ConfigurationComponent gyroSensor = new ConfigurationComponent("GYRO", false, "G", BlocklyConstants.NO_SLOT, "G", gyroSensorProperties);

        Map<String, String> infraredSensorProperties = createMap("NAME", "I", "VAR", "W", "CONNECTOR", "2");
        ConfigurationComponent infraredSensor = new ConfigurationComponent("INFRARED", false, "I", BlocklyConstants.NO_SLOT, "I", infraredSensorProperties);

        Map<String, String> buzzerProperties = createMap("NAME", "S", "VAR", "W");
        ConfigurationComponent buzzer = new ConfigurationComponent("BUZZER", false, "S", BlocklyConstants.NO_SLOT, "S", buzzerProperties);

        Map<String, String> ledProperties = createMap("NAME", "L", "VAR", "W");
        ConfigurationComponent led = new ConfigurationComponent("LIGHT", false, "L", BlocklyConstants.NO_SLOT, "L", ledProperties);

        final Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(11f).setWheelDiameter(5.6f).addComponents(Arrays.asList(motorM, keySensor, gyroSensor, infraredSensor, buzzer, led));
        return builder.build();
    }

    /**
     * Generate java script code as string from a given program .
     *
     * @param pathToProgramXml path to a XML file, usable for {@link Class#getResourceAsStream(String)}
     * @return the code as string
     * @throws Exception
     */
    public String generateJavaScript(String pathToProgramXml) throws Exception {
        Jaxb2ProgramAst<Void> transformer = generateTransformer(pathToProgramXml);
        String code = MbedSimVisitor.generate(getRobotConfiguration(), transformer.getTree());
        return code;
    }

    /**
     * Generate cpp code as string from a given program . Prepend and append wrappings.
     *
     * @param pathToProgramXml path to a XML file, usable for {@link Class#getResourceAsStream(String)}
     * @return the code as string
     * @throws Exception
     */
    public String generateCpp(String pathToProgramXml, Configuration brickConfiguration) throws Exception {
        final Jaxb2ProgramAst<Void> transformer = generateTransformer(pathToProgramXml);
        final String code = CalliopeCppVisitor.generate(brickConfiguration, transformer.getTree(), true);
        return code;
    }

    public String generateVmCode(String pathToProgramXml) throws Exception {
        final Jaxb2ProgramAst<Void> transformer = generateTransformer(pathToProgramXml);
        final String code = MbedStackMachineVisitor.generate(this.robotConfiguration, transformer.getTree());
        return code;
    }

    public void compareExistingAndGeneratedVmSource(String sourceCodeResource, String xmlResource) throws Exception {
        Assert.assertEquals(Util1.readResourceContent(sourceCodeResource).replaceAll("\\s+", ""), generateVmCode(xmlResource).replaceAll("\\s+", ""));
    }

    public void compareExistingAndGeneratedSource(String sourceCodeFilename, String xmlFilename) throws Exception {
        Assert
            .assertEquals(
                Util1.readResourceContent(sourceCodeFilename).replaceAll("\\s+", ""),
                generateCpp(xmlFilename, new Configuration.Builder().build()).replaceAll("\\s+", ""));
    }

}
