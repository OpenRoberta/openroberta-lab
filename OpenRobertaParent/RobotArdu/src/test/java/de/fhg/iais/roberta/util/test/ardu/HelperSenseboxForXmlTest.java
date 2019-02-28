package de.fhg.iais.roberta.util.test.ardu;

import java.util.Arrays;
import java.util.Map;

import org.junit.Assert;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.factory.SenseboxFactory;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.transformers.arduino.Jaxb2ArduinoConfigurationTransformer;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;
import de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest;
import de.fhg.iais.roberta.visitor.codegen.SenseboxCppVisitor;

public class HelperSenseboxForXmlTest extends AbstractHelperForXmlTest {
    private SenseboxFactory senseboxFactory = null;

    public HelperSenseboxForXmlTest() {
        super(new SenseboxFactory(new PluginProperties("sensebox", "", "", Util1.loadProperties("classpath:/sensebox.properties"))), makeConfiguration());
        this.senseboxFactory = (SenseboxFactory) super.getRobotFactory();
    }

    public Configuration regenerateConfiguration(String blocklyXml) throws Exception {
        final BlockSet project = JaxbHelper.xml2BlockSet(blocklyXml);
        final Jaxb2ArduinoConfigurationTransformer transformer = new Jaxb2ArduinoConfigurationTransformer(this.senseboxFactory.getBlocklyDropdownFactory());
        return transformer.transform(project);
    }

    public static Configuration makeConfiguration() {
        Map<String, String> wireless = createMap("NAME", "W", "SSID", "mySSID", "PASSWORD", "myPassw0rd", "Connector", "XBEE1");
        ConfigurationComponent wirelessComponent = new ConfigurationComponent("WIRELESS", true, "W", BlocklyConstants.NO_SLOT, "W", wireless);

        Map<String, String> humidity = createMap("NAME", "H", "I2C", "I2C");
        ConfigurationComponent humidityComponent = new ConfigurationComponent("HUMIDITY", false, "H", BlocklyConstants.NO_SLOT, "H", humidity);

        Map<String, String> temperature = createMap("NAME", "T", "I2C", "I2C");
        ConfigurationComponent temperatureComponent = new ConfigurationComponent("TEMPERATURE", false, "T", BlocklyConstants.NO_SLOT, "T", temperature);

        Map<String, String> lightVeml = createMap("NAME", "V", "I2C", "I2C");
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
        Assert
            .assertEquals(Util1.readResourceContent(sourceCodeFilename).replaceAll("\\s+", ""), generateCpp(xmlFilename, configuration).replaceAll("\\s+", ""));
    }
}
