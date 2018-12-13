package de.fhg.iais.roberta.util.test.ev3;

import org.junit.Assert;

import de.fhg.iais.roberta.components.vorwerk.VorwerkConfiguration;
import de.fhg.iais.roberta.factory.VorwerkFactory;
import de.fhg.iais.roberta.mode.action.Language;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest;
import de.fhg.iais.roberta.visitor.codegen.VorwerkPythonVisitor;

public class HelperVorwerkForXmlTest extends AbstractHelperForXmlTest {

    public HelperVorwerkForXmlTest() {
        super(
            new VorwerkFactory(new PluginProperties("vorwerk", "", "", Util1.loadProperties("classpath:/vorwerk.properties"))),
            new VorwerkConfiguration.Builder().build());
    }

    /**
     * Generate java code as string from a given program fragment. Do not prepend and append wrappings.
     *
     * @param pathToProgramXml path to a XML file, usable for {@link Class#getResourceAsStream(String)}
     * @return the code fragment as string
     * @throws Exception
     */
    private String generateStringWithoutWrapping(String pathToProgramXml) throws Exception {
        Jaxb2ProgramAst<Void> transformer = generateTransformer(pathToProgramXml);
        String javaCode = VorwerkPythonVisitor.generate((VorwerkConfiguration) getRobotConfiguration(), transformer.getTree(), false, Language.ENGLISH);
        return javaCode;
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
        Assert.assertEquals(correctJavaCode.replaceAll("\\s+", ""), generateStringWithoutWrapping(fileName).replaceAll("\\s+", ""));
    }

    /**
     * this.robotConfiguration Generate python code as string from a given program . Prepend and append wrappings.
     *
     * @param pathToProgramXml path to a XML file, usable for {@link Class#getResourceAsStream(String)}
     * @return the code as string
     * @throws Exception
     */
    public String generatePython(String pathToProgramXml, VorwerkConfiguration brickConfiguration) throws Exception {
        Jaxb2ProgramAst<Void> transformer = generateTransformer(pathToProgramXml);
        String code = VorwerkPythonVisitor.generate(brickConfiguration, transformer.getTree(), true, Language.ENGLISH);
        // System.out.println(code); // only needed for EXTREME debugging
        return code;
    }
}
