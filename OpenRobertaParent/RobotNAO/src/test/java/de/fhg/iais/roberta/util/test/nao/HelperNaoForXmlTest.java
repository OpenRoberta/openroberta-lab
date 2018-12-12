package de.fhg.iais.roberta.util.test.nao;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;

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
        super(new NaoFactory(new PluginProperties("nao", "", "", Util1.loadProperties("classpath:nao.properties"))), new Configuration.Builder().build());
    }

    public String generateCpp(String pathToProgramXml) throws Exception {
        Jaxb2ProgramAst<Void> transformer = generateTransformer(pathToProgramXml);
        String code = NaoPythonVisitor.generate(transformer.getTree(), true);
        code += "\n";
        return code;
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

    public void compareExistingAndGeneratedSource(String sourceCodeFilename, String xmlFilename) throws Exception {
        Assert.assertEquals(readFileToString(sourceCodeFilename), generateCpp(xmlFilename));
    }
}
