package de.fhg.iais.roberta.ast.action;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.blockly.generated.BlockSet;
import de.fhg.iais.roberta.factory.NxtFactory;
import de.fhg.iais.roberta.jaxb.JaxbHelper;
import de.fhg.iais.roberta.testutil.Helper;
import de.fhg.iais.roberta.transformer.Jaxb2BlocklyProgramTransformer;

public class ActionTest {

    @Test
    public void clearDisplay() throws Exception {
        final String a = "BlockAST [project=[[Location [x=-69, y=10], MainTask [], ClearDisplayAction []]]]";

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
        final String a = "BlockAST [project=[[Location [x=1, y=135], StopAction []]]]";

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
        BlockSet project = JaxbHelper.path2BlockSet("/ast/actions/action_Exception.xml");
        NxtFactory factory = new NxtFactory(null);
        Jaxb2BlocklyProgramTransformer<?> transformer = new Jaxb2BlocklyProgramTransformer<>(factory);
        try {
            transformer.transform(project);
            Assert.fail();
        } catch ( final Exception e ) {
            Assert.assertEquals("blockly name is not found: robactions_bricklight_on1", e.getMessage());
        }
    }

    @Test
    public void disabledComment() throws Exception {
        final Jaxb2BlocklyProgramTransformer<Void> t = Helper.generateTransformer("/ast/actions/action_DisabledComment.xml");

        Assert.assertEquals(true, t.getTree().get(0).get(2).getProperty().isDisabled());
        Assert.assertEquals("h#,,", t.getTree().get(0).get(1).getComment().getComment());
    }
}
