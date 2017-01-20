package de.fhg.iais.roberta.ast;

import java.util.Properties;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.factory.NxtFactory;
import de.fhg.iais.roberta.jaxb.JaxbHelper;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;
import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;

public class ExceptionTest {
    @BeforeClass
    public static void loadPropertiesForTests() {
        Properties properties = Util1.loadProperties(null);
        RobertaProperties.setRobertaProperties(properties);
    }

    @Test
    public void valueException() throws Exception {
        BlockSet project = JaxbHelper.path2BlockSet("/ast/exceptions/value_exception.xml");
        NxtFactory factory = new NxtFactory(null);
        Jaxb2BlocklyProgramTransformer<?> transformer = new Jaxb2BlocklyProgramTransformer<>(factory);
        try {
            transformer.transform(project);
            Assert.fail();
        } catch ( Exception e ) {
            Assert.assertEquals("Values size is not less or equal to 2!", e.getMessage());
        }
    }

}
