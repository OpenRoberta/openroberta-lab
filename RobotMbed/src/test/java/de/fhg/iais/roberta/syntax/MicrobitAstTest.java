package de.fhg.iais.roberta.syntax;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.junit.BeforeClass;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.components.ConfigurationComponent;
import de.fhg.iais.roberta.factory.RobotFactory;
import de.fhg.iais.roberta.transformer.Jaxb2ConfigurationAst;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;

public class MicrobitAstTest extends AstTest {

    protected static ConfigurationAst configuration;

    @BeforeClass
    public static void setupBefore() throws Exception {
        testFactory = new RobotFactory(new PluginProperties("microbit", "", "", Util.loadProperties("classpath:/microbit.properties")));
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
        builder.addComponents(additionalComps);
        configuration = builder.build();
    }
}
