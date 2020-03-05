package de.fhg.iais.roberta.ast.action;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.mode.action.TurnDirection;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.motor.differential.TurnAction;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class TurnActionTest extends AstTest {

    @Test
    public void make() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=1, y=243], TurnAction [direction=RIGHT, param=MotionParam [speed=NumConst [50], duration=MotorDuration [type=DEGREE, value=NumConst [20]]]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/actions/action_MotorDiffTurnFor.xml");
    }

    @Test
    public void getDirection() throws Exception {
        List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/actions/action_MotorDiffTurnFor.xml");
        TurnAction<Void> ta = (TurnAction<Void>) forest.get(0).get(1);
        Assert.assertEquals(TurnDirection.RIGHT, ta.getDirection());
    }

    @Test
    public void getParam() throws Exception {
        List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/actions/action_MotorDiffTurnFor.xml");
        TurnAction<Void> ta = (TurnAction<Void>) forest.get(0).get(1);
        Assert.assertEquals("MotionParam [speed=NumConst [50], duration=MotorDuration [type=DEGREE, value=NumConst [20]]]", ta.getParam().toString());
    }

    @Test
    public void motorDiffTurn() throws Exception {
        String a = "BlockAST [project=[[Location [x=29, y=89], TurnAction [direction=RIGHT, param=MotionParam [speed=NumConst [50], duration=null]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/actions/action_MotorDiffTurn.xml");
    }

    @Test
    public void motorDiffTurnMissing() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=35, y=84], TurnAction [direction=RIGHT, param=MotionParam [speed=EmptyExpr [defVal=NUMBER_INT], duration=null]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/actions/action_MotorDiffTurnMissing.xml");
    }

    @Test
    public void motorDiffTurnForMissing() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=27, y=3], TurnAction [direction=RIGHT, param=MotionParam [speed=EmptyExpr [defVal=NUMBER_INT], duration=MotorDuration [type=DEGREE, value=EmptyExpr [defVal=NUMBER_INT]]]], TurnAction [direction=RIGHT, param=MotionParam [speed=EmptyExpr [defVal=NUMBER_INT], duration=MotorDuration [type=DEGREE, value=NumConst [20]]]], TurnAction [direction=RIGHT, param=MotionParam [speed=NumConst [50], duration=MotorDuration [type=DEGREE, value=EmptyExpr [defVal=NUMBER_INT]]]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/actions/action_MotorDiffTurnForMissing.xml");
    }

    @Test
    public void reverseTransformatinTurnFor() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_MotorDiffTurnFor.xml");
    }

    @Test
    public void reverseTransformatinDiffTurn() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_MotorDiffTurn.xml");
    }

    @Test
    public void reverseTransformatinTurnMissing() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_MotorDiffTurnMissing.xml");
    }

    @Test
    public void reverseTransformatinForMissing() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_MotorDiffTurnForMissing.xml");
    }
}
