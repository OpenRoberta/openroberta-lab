package de.fhg.iais.roberta.ast.action;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.InputSource;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;
import de.fhg.iais.roberta.ast.transformer.JaxbProgramTransformer;
import de.fhg.iais.roberta.blockly.generated.BlockSet;

public class ActionTest {

    @Test
    public void clearDisplay() throws Exception {
        String a = "BlockAST [project=[[Location [x=-76, y=212], ClearDisplayAction []]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_ClearDisplay.xml"));
    }

    @Test
    public void reverseTransformatinclearDisplay() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_ClearDisplay.xml");
    }

    @Test
    public void reverseTransformatinclearDisplay1() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_ClearDisplay1.xml");
    }

    @Test
    public void stop() throws Exception {
        String a = "BlockAST [project=[[Location [x=1, y=135], StopAction []]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/actions/action_Stop.xml"));
    }

    @Test
    public void reverseTransformatinStop() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_Stop.xml");
    }

    @Test
    public void reverseTransformatinStop1() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_Stop1.xml");
    }

    @Test
    public void reverseTransformatinStop2() throws Exception {
        Helper.assertTransformationIsOk("/ast/actions/action_Stop2.xml");
    }

    @Test
    public void blockException() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(BlockSet.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

        InputSource src = new InputSource(Math.class.getResourceAsStream("/ast/actions/action_Exception.xml"));
        BlockSet project = (BlockSet) jaxbUnmarshaller.unmarshal(src);

        JaxbProgramTransformer<?> transformer = new JaxbProgramTransformer<>();
        try {
            transformer.transform(project);
            Assert.fail();
        } catch ( Exception e ) {
            Assert.assertEquals("Invalid Block: robActions_brickLight_on1", e.getMessage());
        }
    }

    @Test
    public void disabledComment() throws Exception {
        JaxbProgramTransformer<Void> t = Helper.generateTransformer("/ast/actions/action_DisabledComment.xml");

        Assert.assertEquals(true, t.getTree().get(2).getProperty().isDisabled());
        Assert.assertEquals("h#,,", t.getTree().get(1).getComment().getComment());
    }
}
