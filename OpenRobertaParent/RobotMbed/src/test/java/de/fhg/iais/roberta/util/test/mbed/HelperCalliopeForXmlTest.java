package de.fhg.iais.roberta.util.test.mbed;

import org.junit.Assert;

import de.fhg.iais.roberta.components.CalliopeConfiguration;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.factory.Calliope2016Factory;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.visitor.codegen.CalliopeCppVisitor;
import de.fhg.iais.roberta.visitor.codegen.MbedSimVisitor;

/**
 * This class is used to store helper methods for operation with JAXB objects and generation code from them.
 */
public class HelperCalliopeForXmlTest extends de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest {

    public HelperCalliopeForXmlTest() {
        super(
            new Calliope2016Factory(new PluginProperties("calliope2016", "", "", Util1.loadProperties("classpath:/calliope2016.properties"))),
            new CalliopeConfiguration.Builder().build());
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

    public void compareExistingAndGeneratedSource(String sourceCodeFilename, String xmlFilename) throws Exception {
        Assert
            .assertEquals(
                Util1.readResourceContent(sourceCodeFilename).replaceAll("\\s+", ""),
                generateCpp(xmlFilename, new Configuration.Builder().build()).replaceAll("\\s+", ""));
    }

}
