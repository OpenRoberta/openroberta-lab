package de.fhg.iais.roberta.ast;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.transformer.JaxbBlocklyProgramTransformer;
import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.jaxb.JaxbHelper;

public class ExceptionTest {

    @Test
    public void valueException() throws Exception {
        BlockSet project = JaxbHelper.path2BlockSet("/ast/exceptions/value_exception.xml");
        JaxbBlocklyProgramTransformer<?> transformer = new JaxbBlocklyProgramTransformer<>();
        try {
            transformer.transform(project);
            Assert.fail();
        } catch ( Exception e ) {
            Assert.assertEquals("Values size is not less or equal to 2!", e.getMessage());
        }
    }

}
