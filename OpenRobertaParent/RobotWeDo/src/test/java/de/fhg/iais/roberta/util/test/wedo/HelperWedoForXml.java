package de.fhg.iais.roberta.util.test.wedo;

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
import de.fhg.iais.roberta.factory.WeDoFactory;
import de.fhg.iais.roberta.syntax.BlocklyConstants;
import de.fhg.iais.roberta.transformer.Jaxb2ProgramAst;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.AbstractHelperForXmlTest;
import de.fhg.iais.roberta.visitor.WeDoStackMachineVisitor;

public class HelperWedoForXml extends AbstractHelperForXmlTest {

    Configuration configuration = makeConfiguration();

    public HelperWedoForXml() {
        super(new WeDoFactory(new PluginProperties("wedo", "", "", Util1.loadProperties("classpath:/wedo.properties"))), makeConfiguration());
    }

    public static Configuration makeConfiguration() {
        Map<String, String> motorMproperties = createMap("NAME", "M", "VAR", "W", "CONNECTOR", "1");
        ConfigurationComponent motorM = new ConfigurationComponent("LARGE", true, "M", BlocklyConstants.NO_SLOT, "M", motorMproperties);

        Map<String, String> keySensorProperties = createMap("NAME", "T", "VAR", "W");
        ConfigurationComponent keySensor = new ConfigurationComponent("TOUCH", false, "T", BlocklyConstants.NO_SLOT, "T", keySensorProperties);

        Map<String, String> gyroSensorProperties = createMap("NAME", "G", "VAR", "W", "CONNECTOR", "1");
        ConfigurationComponent gyroSensor = new ConfigurationComponent("GYRO", false, "G", BlocklyConstants.NO_SLOT, "G", gyroSensorProperties);

        Map<String, String> infraredSensorProperties = createMap("NAME", "I", "VAR", "W", "CONNECTOR", "2");
        ConfigurationComponent infraredSensor = new ConfigurationComponent("INFRARED", false, "I", BlocklyConstants.NO_SLOT, "I", infraredSensorProperties);

        Map<String, String> buzzerProperties = createMap("NAME", "S", "VAR", "W");
        ConfigurationComponent buzzer = new ConfigurationComponent("BUZZER", false, "S", BlocklyConstants.NO_SLOT, "S", keySensorProperties);

        Map<String, String> ledProperties = createMap("NAME", "L", "VAR", "W");
        ConfigurationComponent led = new ConfigurationComponent("LIGHT", false, "L", BlocklyConstants.NO_SLOT, "L", keySensorProperties);

        final Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(11f).setWheelDiameter(5.6f).addComponents(Arrays.asList(motorM, keySensor, gyroSensor, infraredSensor, buzzer, led));
        return builder.build();
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

    public String generateVmCode(String pathToProgramXml) throws Exception {
        final Jaxb2ProgramAst<Void> transformer = generateTransformer(pathToProgramXml);
        final String code = WeDoStackMachineVisitor.generate(this.configuration, transformer.getTree());
        return code;
    }

    public void compareExistingAndGeneratedVmSource(String sourceCodeFilename, String xmlFilename) throws Exception {
        Assert.assertEquals(HelperWedoForXml.readFileToString(sourceCodeFilename), generateVmCode(xmlFilename) + '\n');
    }

    public static Map<String, String> createMap(String... args) {
        Map<String, String> m = new HashMap<>();
        for ( int i = 0; i < args.length; i += 2 ) {
            m.put(args[i], args[i + 1]);
        }
        return m;
    }
}
