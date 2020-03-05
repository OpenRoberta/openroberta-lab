package de.fhg.iais.roberta.ast.action;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.NxtAstTest;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ActionTest extends NxtAstTest {

    @Test
    public void clearDisplay() throws Exception {
        final String a = "BlockAST [project=[[Location [x=-69, y=10], MainTask [], ClearDisplayAction []]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/actions/action_ClearDisplay.xml");
    }

    @Test
    public void reverseTransformatinclearDisplay() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_ClearDisplay.xml");
    }

    @Test
    public void reverseTransformatinclearDisplay1() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_ClearDisplay1.xml");
    }

    @Test
    public void stop() throws Exception {
        final String a = "BlockAST [project=[[Location [x=1, y=135], StopAction []]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/actions/action_Stop.xml");
    }

    @Test
    public void reverseTransformatinStop() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_Stop.xml");
    }

    @Test
    public void reverseTransformatinStop1() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_Stop1.xml");
    }

    @Test
    public void reverseTransformatinStop2() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_Stop2.xml");
    }

    @Test(expected = DbcException.class)
    public void blockException() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_Exception.xml");
    }

    @Test
    public void disabledComment() throws Exception {
        final List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/actions/action_DisabledComment.xml");

        Assert.assertEquals(true, forest.get(0).get(2).getProperty().isDisabled());
        Assert.assertEquals("h#,,", forest.get(0).get(1).getComment().getComment());
    }
}
