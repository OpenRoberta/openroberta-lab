package de.fhg.iais.roberta.ast.action;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.syntax.Phrase;
import de.fhg.iais.roberta.syntax.action.motor.MotorOnAction;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MotorOnActionTest extends AstTest {

    @Test
    public void make() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=1, y=1], MotorOnAction [B, MotionParam [speed=NumConst [30], duration=null]], MotorOnAction [C, MotionParam [speed=NumConst [50], duration=null]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/actions/action_MotorOn.xml");
    }

    @Test
    public void getParam() throws Exception {
        List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/actions/action_MotorOnFor.xml");
        MotorOnAction<Void> mo = (MotorOnAction<Void>) forest.get(0).get(1);
        Assert.assertEquals("MotionParam [speed=NumConst [30], duration=null]", mo.getParam().toString());
    }

    @Test
    public void getPort() throws Exception {
        List<List<Phrase<Void>>> forest = UnitTestHelper.getProgramAst(testFactory, "/ast/actions/action_MotorOnFor.xml");
        MotorOnAction<Void> mo = (MotorOnAction<Void>) forest.get(0).get(1);
        Assert.assertEquals("A", mo.getUserDefinedPort());
    }

    @Test
    public void motorOnFor() throws Exception {
        String a = "BlockAST [project=[[Location [x=1, y=55], MotorOnAction [A, MotionParam [speed=NumConst [30], duration=null]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/actions/action_MotorOnFor.xml");
    }

    @Test
    public void motorOnForSim() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=2, y=46], MainTask [], MotorOnAction [C, MotionParam [speed=NumConst [30], duration=MotorDuration [type=ROTATIONS, value=NumConst [1]]]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/actions/action_MotorOnForSim.xml");
    }

    @Test
    public void motorOnMissing() throws Exception {
        String a = "BlockAST [project=[[Location [x=55, y=64], MotorOnAction [B, MotionParam [speed=EmptyExpr [defVal=NUMBER_INT], duration=null]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/actions/action_MotorOnMissing.xml");
    }

    @Test
    public void motorOnSim() throws Exception {
        String a = "BlockAST [project=[[Location [x=2, y=46], MainTask [], MotorOnAction [B, MotionParam [speed=NumConst [30], duration=null]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/actions/action_MotorOnSim.xml");
    }

    @Test
    public void motorOnForMissing() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=62, y=103], MotorOnAction [B, MotionParam [speed=EmptyExpr [defVal=NUMBER_INT], duration=MotorDuration [type=DEGREE, value=EmptyExpr [defVal=NUMBER_INT]]]], MotorOnAction [B, MotionParam [speed=NumConst [30], duration=MotorDuration [type=ROTATIONS, value=EmptyExpr [defVal=NUMBER_INT]]]], MotorOnAction [B, MotionParam [speed=EmptyExpr [defVal=NUMBER_INT], duration=MotorDuration [type=ROTATIONS, value=NumConst [30]]]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/actions/action_MotorOnForMissing.xml");
    }

    @Test
    public void reverseTransformatin() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_MotorOn.xml");
    }

    @Test
    public void reverseTransformatinSim() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_MotorOnSim.xml");
    }

    @Test
    public void reverseTransformatinOnFor() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_MotorOnFor.xml");
    }

    @Test
    public void reverseTransformatinOnForSim() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_MotorOnForSim.xml");
    }

    @Test
    public void reverseTransformatinOnMissing() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_MotorOnFor.xml");
    }

    @Test
    public void reverseTransformatinOnForMissing() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/actions/action_MotorOnForMissing.xml");
    }

}
