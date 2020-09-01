package de.fhg.iais.roberta.ast.action;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.factory.EV3Factory;
import de.fhg.iais.roberta.factory.IRobotFactory;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.util.PluginProperties;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.dbc.DbcException;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ActionTest extends AstTest {

    private IRobotFactory robotFactory;

    @Before
    public void setupTest() {
        this.robotFactory = new EV3Factory(new PluginProperties("ev3lejosv1", "", "", Util.loadProperties("classpath:/ev3lejosv1" + ".properties")));
    }

    @Test
    public void reverseTransformatinclearDisplay() throws Exception {

        String configurationText = this.robotFactory.getConfigurationDefault();
        String programText = Util.readResourceContent("/ast/actions/action_ClearDisplay.xml");

        String projectXml =
            "<export xmlns=\"http://de.fhg.iais.roberta.blockly\"><program>" + programText + "</program><config>" + configurationText + "</config></export>";

        // if this succeeds, transformation was successful
        UnitTestHelper.setupWithExportXML(this.robotFactory, projectXml);
    }

    @Test
    public void clearDisplay() throws Exception {
        String a = "BlockAST [project=[[Location [x=-69, y=10], MainTask [], ClearDisplayAction []]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/actions/action_ClearDisplay.xml");
    }

    @Test
    public void reverseTransformatinclearDisplay1() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_ClearDisplay1.xml");
    }

    @Test
    public void stop() throws Exception {
        String a = "BlockAST [project=[[Location [x=1, y=135], StopAction []]]]";

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
        List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/actions/action_DisabledComment.xml");

        Assert.assertEquals(true, forest.get(0).get(2).getProperty().isDisabled());
        Assert.assertEquals("h#,,", forest.get(0).get(1).getComment().getComment());
    }
}
