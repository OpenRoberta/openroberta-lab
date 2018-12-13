package de.fhg.iais.roberta.util.test.nxt;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.factory.NxtFactory;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest;
import de.fhg.iais.roberta.visitor.codegen.NxtNxcVisitor;
import de.fhg.iais.roberta.visitor.codegen.NxtSimVisitor;

/**
 * This class is used to store helper methods for operation with JAXB objects and generation code from them.
 */
public class HelperNxtForXmlTest extends AbstractHelperForXmlTest {

    public HelperNxtForXmlTest() {
        super(new NxtFactory(new PluginProperties("nxt", "", "", Util1.loadProperties("classpath:/nxt.properties"))), makeConfiguration());
    }

    public static Configuration makeConfiguration() {
        Map<String, String> motorAproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "NONE");
        ConfigurationComponent motorA = new ConfigurationComponent("LARGE", true, "A", BlocklyConstants.NO_SLOT, "A", motorAproperties);

        Map<String, String> motorBproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "LEFT");
        ConfigurationComponent motorB = new ConfigurationComponent("LARGE", true, "B", BlocklyConstants.NO_SLOT, "B", motorBproperties);

        Map<String, String> motorCproperties = createMap("MOTOR_REGULATION", "TRUE", "MOTOR_REVERSE", "OFF", "MOTOR_DRIVE", "RIGHT");
        ConfigurationComponent motorC = new ConfigurationComponent("LARGE", true, "C", BlocklyConstants.NO_SLOT, "C", motorCproperties);

        ConfigurationComponent ultrasonicSensor = new ConfigurationComponent("ULTRASONIC", false, "S4", BlocklyConstants.NO_SLOT, "4", Collections.emptyMap());
        ConfigurationComponent ultrasonicSensor2 = new ConfigurationComponent("ULTRASONIC", false, "S2", BlocklyConstants.NO_SLOT, "2", Collections.emptyMap());

        final Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(11f).setWheelDiameter(5.6f).addComponents(Arrays.asList(motorA, motorB, motorC, ultrasonicSensor, ultrasonicSensor2));
        return builder.build();
    }

    public static Map<String, String> createMap(String... args) {
        Map<String, String> m = new HashMap<>();
        for ( int i = 0; i < args.length; i += 2 ) {
            m.put(args[i], args[i + 1]);
        }
        return m;
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
        String code = NxtSimVisitor.generate(getRobotConfiguration(), transformer.getTree());
        return code;
    }

    /**
     * Generate nxc as string from a given program fragment. Do not prepend and append wrappings.
     *
     * @param pathToProgramXml path to a XML file, usable for {@link Class#getResourceAsStream(String)}
     * @return the code fragment as string
     * @throws Exception
     */
    private String generateNXCWithoutWrapping(String pathToProgramXml) throws Exception {
        Jaxb2ProgramAst<Void> transformer = generateTransformer(pathToProgramXml);
        final String code = NxtNxcVisitor.generate(getRobotConfiguration(), transformer.getTree(), false);
        return code;
    }

    /**
     * Generate nxc as string from a given program . Prepend and append wrappings.
     *
     * @param pathToProgramXml path to a XML file, usable for {@link Class#getResourceAsStream(String)}
     * @return the code as string
     * @throws Exception
     */
    public String generateNXC(String pathToProgramXml, Configuration brickConfiguration) throws Exception {
        final Jaxb2ProgramAst<Void> transformer = generateTransformer(pathToProgramXml);
        final String code = NxtNxcVisitor.generate(brickConfiguration, transformer.getTree(), true);
        return code;
    }

    /**
     * Assert that Java code generated from Blockly XML program is correct.<br>
     * All white space are ignored!
     *
     * @param correctJavaCode correct java code
     * @param fileName of the program we want to generate java code
     * @throws Exception
     */
    public void assertCodeIsOk(String correctJavaCode, String fileName) throws Exception {
        Assert.assertEquals(correctJavaCode.replaceAll("\\s+", ""), generateNXCWithoutWrapping(fileName).replaceAll("\\s+", ""));
    }

    public void assertWrappedCodeIsOk(String correctJavaCode, String fileName) throws Exception {
        Configuration brickConfiguration = makeConfiguration();
        Assert.assertEquals(correctJavaCode.replaceAll("\\s+", ""), generateNXC(fileName, brickConfiguration).replaceAll("\\s+", ""));
    }

    public static String readFileToString(String filename) {
        List<String> lines = Collections.emptyList();
        try {
            lines = Files.readAllLines(Paths.get(ClassLoader.getSystemResource(filename).toURI()));
        } catch ( IOException e ) {
            return "";
        } catch ( URISyntaxException e ) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for ( String line : lines ) {
            sb.append(line);
            sb.append("\n");
        }
        return sb.toString();
    }

    public void compareExistingAndGeneratedNxcSource(String sourceCodeFilename, String xmlFilename, Configuration configuration) throws Exception {
        Assert.assertEquals(HelperNxtForXmlTest.readFileToString(sourceCodeFilename), generateNXC(xmlFilename, configuration));
    }
}
