package de.fhg.iais.roberta.syntax;

import org.junit.BeforeClass;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.ConfigurationAst;
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
        BlockSet blockSet = JaxbHelper.xml2BlockSet(testFactory.getConfigurationTransformer());
        configuration = Jaxb2ConfigurationAst.blocks2NewConfig(blockSet, testFactory.getBlocklyDropdownFactory());
    }
}
