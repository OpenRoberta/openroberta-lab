package de.fhg.iais.roberta.util.test.ardu;

import java.util.Arrays;
import java.util.Map;

import org.junit.Assert;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.factory.SenseboxFactory;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest;
import de.fhg.iais.roberta.visitor.codegen.SenseboxCppVisitor;

public class HelperSenseboxForXmlTest extends AbstractHelperForXmlTest {

    public HelperSenseboxForXmlTest() {
        super(new SenseboxFactory(new PluginProperties("sensebox", "", "", Util1.loadProperties("classpath:/sensebox.properties"))), makeConfiguration());
    }

    public static Configuration makeConfiguration() {
        Map<String, String> wireless = createMap("NAME", "W", "SSID", "test-ssid", "PASSWORD", "passw0rd", "BOX_ID", "L8M8EN2872oFCdzC", "Connector", "XBEE1");
        ConfigurationComponent wirelessComponent = new ConfigurationComponent("WIRELESS", true, "W", BlocklyConstants.NO_SLOT, "W", wireless);

        Map<String, String> humidity = createMap("NAME", "H", "ID", "rut9fA5f3a8ZDZAj", "I2C", "I2C");
        ConfigurationComponent humidityComponent = new ConfigurationComponent("HUMIDITY", false, "H", BlocklyConstants.NO_SLOT, "H", humidity);

        Map<String, String> temperature = createMap("NAME", "T", "ID", "MYiDfpM7QUnqQSTd", "I2C", "I2C");
        ConfigurationComponent temperatureComponent = new ConfigurationComponent("TEMPERATURE", false, "T", BlocklyConstants.NO_SLOT, "T", temperature);

        Map<String, String> lightVeml = createMap("NAME", "V", "ID", "LZQo7cUun6qB47Lf", "I2C", "I2C");
        ConfigurationComponent lightVemlComponent = new ConfigurationComponent("LIGHTVEML", false, "V", BlocklyConstants.NO_SLOT, "V", lightVeml);

        final Configuration.Builder builder = new Configuration.Builder();
        builder
            .setTrackWidth(17f)
            .setWheelDiameter(5.6f)
            .addComponents(Arrays.asList(temperatureComponent, lightVemlComponent, wirelessComponent, humidityComponent));
        return builder.build();
    }

    public String generateCpp(String pathToProgramXml, Configuration configuration) throws Exception {
        Jaxb2ProgramAst<Void> transformer = generateTransformer(pathToProgramXml);
        String code = SenseboxCppVisitor.generate(configuration, transformer.getTree(), true);
        return code + '\n';
    }

    public void compareExistingAndGeneratedSource(String sourceCodeFilename, String xmlFilename, Configuration configuration) throws Exception {
        Assert.assertEquals(Util1.readResourceContent(sourceCodeFilename), generateCpp(xmlFilename, configuration));
    }
}
