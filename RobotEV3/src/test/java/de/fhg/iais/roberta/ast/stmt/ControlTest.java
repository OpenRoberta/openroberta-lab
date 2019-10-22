package de.fhg.iais.roberta.ast.stmt;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ControlTest extends AstTest {

    @Test
    public void robWait() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=100, y=50], MainTask [], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [GetSampleSensor [TimerSensor [1, VALUE, EMPTY_SLOT]]], NumConst [500]]]"
                + "\n)]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/control/wait_stmt.xml");
    }

    @Test
    public void robWaitFor() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=100, y=50], MainTask [], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [GetSampleSensor [TouchSensor [1, PRESSED, EMPTY_SLOT]]], BoolConst [true]]]\n"
                + ")]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/control/wait_stmt1.xml");
    }

    @Test
    public void robWaitFor1() throws Exception {
        String a =
            "BlockAST[project=[[Location[x=100,y=50],MainTask[],WaitStmt[(repeat[WAIT,Binary[EQ,SensorExpr[GetSampleSensor[KeysSensor[LEFT, PRESSED, EMPTY_SLOT]]],BoolConst[true]]])]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/control/wait_stmt2.xml");
    }

    @Test
    public void robWaitFor2() throws Exception {
        String a =
            "BlockAST[project=[[Location[x=31,y=21],WaitStmt[(repeat[WAIT,Binary[EQ,SensorExpr[GetSampleSensor[TouchSensor[1, PRESSED, EMPTY_SLOT]]],BoolConst[true]]]AktionStmt[MotorOnAction[B,MotionParam[speed=NumConst[30],duration=null]]])(repeat[WAIT,Binary[LT,SensorExpr[GetSampleSensor[UltrasonicSensor[1, PRESENCE, EMPTY_SLOT]]],NumConst[30]]]AktionStmt[MotorOnAction[B,MotionParam[speed=NumConst[30],duration=null]]])(repeat[WAIT,Binary[GT,SensorExpr[GetSampleSensor[GyroSensor[1, RATE, EMPTY_SLOT]]],NumConst[90]]]AktionStmt[MotorOnAction[B,MotionParam[speed=NumConst[30],duration=null]]])(repeat[WAIT,Binary[EQ,SensorExpr[GetSampleSensor[KeysSensor[LEFT, PRESSED, EMPTY_SLOT]]],BoolConst[true]]]AktionStmt[MotorOnAction[B,MotionParam[speed=NumConst[30],duration=null]]])(repeat[WAIT,Binary[EQ,SensorExpr[GetSampleSensor[ColorSensor[1, COLOUR, EMPTY_SLOT]]],ColorConst[#b30006]]]AktionStmt[MotorOnAction[B,MotionParam[speed=NumConst[30],duration=null]]])(repeat[WAIT,Binary[GT,SensorExpr[GetSampleSensor[GyroSensor[1, ANGLE, EMPTY_SLOT]]],NumConst[90]]]AktionStmt[MotorOnAction[B,MotionParam[speed=NumConst[30],duration=null]]])(repeat[WAIT,Binary[LT,SensorExpr[GetSampleSensor[InfraredSensor[1, DISTANCE, EMPTY_SLOT]]],NumConst[30]]]AktionStmt[MotorOnAction[B,MotionParam[speed=NumConst[30],duration=null]]])(repeat[WAIT,Binary[GT,SensorExpr[GetSampleSensor[TimerSensor[1,VALUE,EMPTY_SLOT]]],NumConst[500]]]AktionStmt[MotorOnAction[B,MotionParam[speed=NumConst[30],duration=null]]])]],[Location[x=168,y=259],MotorOnAction[B,MotionParam[speed=NumConst[30],duration=null]]]]]";
        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/control/wait_stmt3.xml");
    }

    public void reverseTransformation() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/control/wait_stmt.xml");
    }

    @Ignore
    public void reverseTransformationFor() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/control/wait_stmt1.xml");
    }

    @Ignore
    public void reverseTransformationFor1() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/control/wait_stmt2.xml");
    }

    @Ignore
    public void reverseTransformationFor2() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/control/wait_stmt3.xml");
    }
}
