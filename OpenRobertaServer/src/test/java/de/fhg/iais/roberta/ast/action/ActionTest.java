package de.fhg.iais.roberta.ast.action;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.transformer.JaxbTransformer;
import de.fhg.iais.roberta.blockly.generated.BlockSet;

public class ActionTest {

    @Test
    public void clearDisplay() throws Exception {
        String a = "BlockAST [project=[[ClearDisplayAction []]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_ClearDisplay.xml"));
    }

    @Test
    public void stop() throws Exception {
        String a = "BlockAST [project=[[StopAction []]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_Stop.xml"));
    }

    @Test
    public void blockException() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(BlockSet.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/actions/action_Exception.xml"));
        BlockSet project = (BlockSet) jaxbUnmarshaller.unmarshal(src);

        JaxbTransformer<?> transformer = new JaxbTransformer<>();
        try {
            transformer.blockSetToAST(project);
            Assert.fail();
        } catch ( Exception e ) {
            Assert.assertEquals("Invalid Block: robActions_brickLight_on1", e.getMessage());
        }
    }

    @Test
    public void disabledComment() throws Exception {
        JaxbTransformer<Void> t = Helper.generateTransformer("/ast/actions/action_DisabledComment.xml");

        Assert.assertEquals(true, t.getTree().get(1).isDisabled());
        Assert.assertEquals("h#,,", t.getTree().get(0).getComment());
    }
}
