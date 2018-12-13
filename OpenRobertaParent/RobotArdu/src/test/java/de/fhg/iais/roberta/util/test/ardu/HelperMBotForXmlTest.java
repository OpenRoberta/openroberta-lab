package de.fhg.iais.roberta.util.test.ardu;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.factory.MbotFactory;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.visitor.codegen.MbotCppVisitor;

public class HelperMBotForXmlTest extends de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest {

    public HelperMBotForXmlTest() {
        super(new MbotFactory(new PluginProperties("mbot", "", "", Util1.loadProperties("classpath:/mbot.properties"))), new Configuration.Builder().build());
    }

    public static Map<String, String> createMap(String... args) {
        Map<String, String> m = new HashMap<>();
        for ( int i = 0; i < args.length; i += 2 ) {
            m.put(args[i], args[i + 1]);
        }
        return m;
    }

    public String generateCpp(String pathToProgramXml, Configuration configuration) throws Exception {
        Jaxb2ProgramAst<Void> transformer = generateTransformer(pathToProgramXml);
        String code = MbotCppVisitor.generate(configuration, transformer.getTree(), true);
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

    public void compareExistingAndGeneratedSource(String sourceCodeFilename, String xmlFilename, Configuration configuration) throws Exception {
        Assert.assertEquals(HelperMBotForXmlTest.readFileToString(sourceCodeFilename), generateCpp(xmlFilename, configuration));
    }
}
