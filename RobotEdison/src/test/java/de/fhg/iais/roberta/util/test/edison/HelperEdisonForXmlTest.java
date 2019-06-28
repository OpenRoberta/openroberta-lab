package de.fhg.iais.roberta.util.test.edison;

import com.google.common.collect.Lists;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.factory.EdisonFactory;
import de.fhg.iais.roberta.mode.action.Language;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest;
import de.fhg.iais.roberta.visitor.codegen.EdisonPythonVisitor;
import org.junit.Assert;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class is used to store helper methods for operation with JAXB objects and generation code from them.
 */
public class HelperEdisonForXmlTest extends AbstractHelperForXmlTest {

    public HelperEdisonForXmlTest() {
        super(
            new EdisonFactory(new PluginProperties("edison", "", "", Util1.loadProperties("classpath:/edison.properties"))),
            makeConfig());
    }

    public static Configuration makeConfig() {
        ConfigurationComponent leftMotor = new ConfigurationComponent("MOTOR", true, "LMOTOR", "LMOTOR", Collections.emptyMap());
        ConfigurationComponent rightMotor = new ConfigurationComponent("MOTOR", true, "RMOTOR", "RMOTOR", Collections.emptyMap());
        ConfigurationComponent leftLED = new ConfigurationComponent("LED", true, "LLED", "LLED", Collections.emptyMap());
        ConfigurationComponent rightLED = new ConfigurationComponent("LED", true, "RLED", "RLED", Collections.emptyMap());
        ConfigurationComponent irLED = new ConfigurationComponent("INFRARED", false, "IRLED", "IRLED", Collections.emptyMap());
        ConfigurationComponent obstacleDetector = new ConfigurationComponent("INFRARED", false, "OBSTACLEDETECTOR", "OBSTACLEDETECTOR", Collections.emptyMap());
        ConfigurationComponent lineTracker = new ConfigurationComponent("LIGHT", false, "LINETRACKER", "LINETRACKER", Collections.emptyMap());
        ConfigurationComponent leftLight = new ConfigurationComponent("LIGHT", false, "LLIGHT", "LLIGHT", Collections.emptyMap());
        ConfigurationComponent rightLight = new ConfigurationComponent("LIGHT", false, "RLIGHT", "RLIGHT", Collections.emptyMap());
        ConfigurationComponent sound = new ConfigurationComponent("SOUND", true, "SOUND", "SOUND", Collections.emptyMap());
        ConfigurationComponent playButton = new ConfigurationComponent("KEY", true, "PLAY", "PLAYKEY", Collections.emptyMap());
        ConfigurationComponent recordButton = new ConfigurationComponent("KEY", true, "REC", "RECKEY", Collections.emptyMap());

        ArrayList<ConfigurationComponent> components = Lists.newArrayList(
            leftMotor,
            rightMotor,
            leftLED,
            rightLED,
            irLED,
            obstacleDetector,
            lineTracker,
            leftLight,
            rightLight,
            sound,
            playButton,
            recordButton);


        return new Configuration.Builder().setTrackWidth(11f).setWheelDiameter(5.6f).addComponents(components).build();
    }


    /**
     * Assert that Python code generated from Blockly XML program is correct.<br>
     * All white space are ignored!
     *
     * @param correctJavaCode correct Python code
     * @param fileName of the program we want to generate code from
     * @throws Exception
     */
    public void assertCodeIsOk(String correctJavaCode, String fileName) throws Exception {
        Assert.assertEquals(correctJavaCode.replaceAll("\\s+", ""), generatePythonWithoutWrapping(fileName).replaceAll("\\s+", ""));
    }


    /**
     * Generate Python code as string from a given program fragment. Do not prepend and append wrappings.
     *
     * @param pathToProgramXml path to a XML file, usable for {@link Class#getResourceAsStream(String)}
     * @return the code fragment as string
     * @throws Exception
     */
    private String generatePythonWithoutWrapping(String pathToProgramXml) throws Exception {
        Jaxb2ProgramAst<Void> transformer = generateTransformer(pathToProgramXml);
        String code = EdisonPythonVisitor.generate(this.getRobotConfiguration(), transformer.getTree(), false, Language.ENGLISH);
        return code;
    }


    /**
     * Generate java code as string from a given program. Prepend and append wrappings.
     *
     * @param pathToProgramXml path to a XML file, usable for {@link Class#getResourceAsStream(String)}
     * @return the code as string
     * @throws Exception
     */
    public String generatePython(String pathToProgramXml) throws Exception {
        Jaxb2ProgramAst<Void> transformer = generateTransformer(pathToProgramXml);
        String code = EdisonPythonVisitor.generate(this.getRobotConfiguration(), transformer.getTree(), true, Language.ENGLISH);
        return code;
    }

    /**
     * Compares existing/correct Python code and generated Python code. Whitespaces will be deleted.
     *
     * @param sourceCodeFilename
     * @param xmlFilename
     * @throws Exception
     */
    public void compareExistingAndGeneratedPythonSource(String sourceCodeFilename, String xmlFilename) throws Exception {
        Assert.assertEquals(Util1.readResourceContent(sourceCodeFilename).replaceAll("\\s+", ""),
                generatePython(xmlFilename).replaceAll("\\s+", ""));
    }
}
