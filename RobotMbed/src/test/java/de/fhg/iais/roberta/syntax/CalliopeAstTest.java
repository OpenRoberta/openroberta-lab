package de.fhg.iais.roberta.syntax;

import org.junit.BeforeClass;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.transformer.Jaxb2ConfigurationAst;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;

public class CalliopeAstTest extends AstTest {

    protected static ConfigurationAst configuration;

    @BeforeClass
    public static void setupBefore() throws Exception {
        BlockSet blockSet = JaxbHelper.xml2BlockSet(testFactory.getConfigurationDefault());
        ConfigurationAst defaultConf = Jaxb2ConfigurationAst.blocks2NewConfig(blockSet, testFactory.getBlocklyDropdownFactory());
        ConfigurationAst.Builder builder = new ConfigurationAst.Builder();
        builder.setRobotType(defaultConf.getRobotType());
        builder.setXmlVersion(defaultConf.getXmlVersion());
        builder.setDescription(defaultConf.getDescription());
        builder.setTags(defaultConf.getTags());
        builder.addComponents(defaultConf.getConfigurationComponentsValues());
        Collection<ConfigurationComponent> additionalComps = new ArrayList<>();
        additionalComps.add(new ConfigurationComponent("DIGITAL_PIN", true, "P0", "P0", Collections.singletonMap("PIN1", "0")));
        additionalComps.add(new ConfigurationComponent("DIGITAL_PIN", true, "P1", "P1", Collections.singletonMap("PIN1", "1")));
        additionalComps.add(new ConfigurationComponent("DIGITAL_INPUT", true, "P2", "P2", Collections.singletonMap("PIN1", "2")));
        additionalComps.add(new ConfigurationComponent("DIGITAL_INPUT", true, "P2_2", "P2_2", Collections.singletonMap("PIN1", "2")));
        additionalComps.add(new ConfigurationComponent("DIGITAL_INPUT", true, "A0", "A0", Collections.singletonMap("PIN1", "4")));
        additionalComps.add(new ConfigurationComponent("SERVO", true, "C04", "C04", Collections.singletonMap("PIN1", "C04")));
        additionalComps.add(new ConfigurationComponent("MOTOR", true, "Port_A", "Port_A", Collections.singletonMap("PIN1", "A")));
        additionalComps.add(new ConfigurationComponent("MOTOR", true, "Port_B", "Port_B", Collections.singletonMap("PIN1", "B")));
        additionalComps.add(new ConfigurationComponent("HUMIDITY", false, "H", "H", Collections.singletonMap("PIN1", "5")));
        additionalComps.add(new ConfigurationComponent("LEDBAR", true, "LEDBAR", "LEDBAR", Collections.emptyMap()));
        additionalComps.add(new ConfigurationComponent("FOURDIGITDISPLAY", true, "F", "F", Collections.emptyMap()));
        builder.addComponents(additionalComps);
        configuration = builder.build();
    }
}
