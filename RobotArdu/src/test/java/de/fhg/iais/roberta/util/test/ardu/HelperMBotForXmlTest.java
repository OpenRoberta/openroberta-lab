package de.fhg.iais.roberta.util.test.ardu;

import org.junit.Assert;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.factory.MbotFactory;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest;
import de.fhg.iais.roberta.visitor.codegen.MbotCppVisitor;

public class HelperMBotForXmlTest extends AbstractHelperForXmlTest {

    public HelperMBotForXmlTest() {
        super(new MbotFactory(new PluginProperties("mbot", "", "", Util1.loadProperties("classpath:/mbot.properties"))), new Configuration.Builder().build());
    }

    public String generateCpp(String pathToProgramXml, Configuration configuration) throws Exception {
        Jaxb2ProgramAst<Void> transformer = generateTransformer(pathToProgramXml);
        String code = MbotCppVisitor.generate(configuration, transformer.getTree(), true);
        return code;
    }

    public void compareExistingAndGeneratedSource(String sourceCodeFilename, String xmlFilename, Configuration configuration) throws Exception {
        Assert
            .assertEquals(Util1.readResourceContent(sourceCodeFilename).replaceAll("\\s+", ""), generateCpp(xmlFilename, configuration).replaceAll("\\s+", ""));
    }
}
