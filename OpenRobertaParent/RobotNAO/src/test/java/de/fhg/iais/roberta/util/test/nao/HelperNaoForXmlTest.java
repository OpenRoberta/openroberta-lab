package de.fhg.iais.roberta.util.test.nao;

import org.junit.Assert;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.factory.NaoFactory;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.visitor.codegen.NaoPythonVisitor;

/**
 * This class is used to store helper methods for operation with JAXB objects and generation code from them.
 */
public class HelperNaoForXmlTest extends de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest {

    public HelperNaoForXmlTest() {
        super(new NaoFactory(new PluginProperties("nao", "", "", Util1.loadProperties("classpath:/nao.properties"))), new Configuration.Builder().build());
    }

    public String generateCpp(String pathToProgramXml) throws Exception {
        Jaxb2ProgramAst<Void> transformer = generateTransformer(pathToProgramXml);
        return NaoPythonVisitor.generate(transformer.getTree(), true);
    }

    public void compareExistingAndGeneratedSource(String sourceCodeFilename, String xmlFilename) throws Exception {
        Assert.assertEquals(Util1.readResourceContent(sourceCodeFilename).replaceAll("\\s+", ""), generateCpp(xmlFilename).replaceAll("\\s+", ""));
    }
}
