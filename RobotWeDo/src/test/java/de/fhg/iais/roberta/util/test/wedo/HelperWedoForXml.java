package de.fhg.iais.roberta.util.test.wedo;

import java.util.Arrays;
import java.util.Map;

import org.junit.Assert;

import de.fhg.iais.roberta.components.Configuration;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.factory.WeDoFactory;
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
        ConfigurationComponent motorM = new ConfigurationComponent("LARGE", true, "M", "M", motorMproperties);

        Map<String, String> keySensorProperties = createMap("NAME", "T", "VAR", "W");
        ConfigurationComponent keySensor = new ConfigurationComponent("TOUCH", false, "T", "T", keySensorProperties);

        Map<String, String> gyroSensorProperties = createMap("NAME", "G", "VAR", "W", "CONNECTOR", "1");
        ConfigurationComponent gyroSensor = new ConfigurationComponent("GYRO", false, "G", "G", gyroSensorProperties);

        Map<String, String> infraredSensorProperties = createMap("NAME", "I", "VAR", "W", "CONNECTOR", "2");
        ConfigurationComponent infraredSensor = new ConfigurationComponent("INFRARED", false, "I", "I", infraredSensorProperties);

        Map<String, String> buzzerProperties = createMap("NAME", "S", "VAR", "W");
        ConfigurationComponent buzzer = new ConfigurationComponent("BUZZER", false, "S", "S", buzzerProperties);

        Map<String, String> ledProperties = createMap("NAME", "L", "VAR", "W");
        ConfigurationComponent led = new ConfigurationComponent("LIGHT", false, "L", "L", ledProperties);

        final Configuration.Builder builder = new Configuration.Builder();
        builder.setTrackWidth(11f).setWheelDiameter(5.6f).addComponents(Arrays.asList(motorM, keySensor, gyroSensor, infraredSensor, buzzer, led));
        return builder.build();
    }

    public String generateVmCode(String pathToProgramXml) throws Exception {
        final Jaxb2ProgramAst<Void> transformer = generateTransformer(pathToProgramXml);
        final String code = WeDoStackMachineVisitor.generate(this.configuration, transformer.getTree());
        return code;
    }

    public void compareExistingAndGeneratedVmSource(String sourceCodeResource, String xmlResource) throws Exception {
        Assert.assertEquals(Util1.readResourceContent(sourceCodeResource).replaceAll("\\s+", ""), generateVmCode(xmlResource).replaceAll("\\s+", ""));
    }
}
