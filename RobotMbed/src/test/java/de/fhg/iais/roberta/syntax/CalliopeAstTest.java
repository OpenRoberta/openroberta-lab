package de.fhg.iais.roberta.syntax;

import org.junit.BeforeClass;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.components.ConfigurationAst;
import de.fhg.iais.roberta.transformer.Jaxb2ConfigurationAst;
import de.fhg.iais.roberta.util.jaxb.JaxbHelper;

public class CalliopeAstTest extends AstTest {

    protected static ConfigurationAst configuration;

    @BeforeClass
    public static void setupBefore() throws Exception {
        BlockSet blockSet = JaxbHelper.xml2BlockSet(testFactory.getConfigurationTransformer());
        configuration = Jaxb2ConfigurationAst.blocks2NewConfig(blockSet, testFactory.getBlocklyDropdownFactory());
    }
}
