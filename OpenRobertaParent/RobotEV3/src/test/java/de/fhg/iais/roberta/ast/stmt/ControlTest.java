package de.fhg.iais.roberta.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.Helper;

public class ControlTest {
    Helper h = new Helper();

    @Test
    public void robWait() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=100, y=50], MainTask [], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [GetSampleSensor [sensor=TimerSensor [mode=GET_SAMPLE, timer=1]]], NumConst [500]]]"
                + "\n)]]]]";
        Assert.assertEquals(a, this.h.generateTransformerString("/ast/control/wait_stmt.xml"));
    }

    @Test
    public void robWaitFor() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=100, y=50], MainTask [], WaitStmt [\n"
                + "(repeat [WAIT, Binary [EQ, SensorExpr [GetSampleSensor [sensor=TouchSensor [TOUCH, S1]]], BoolConst [true]]]\n"
                + ")]]]]";
        Assert.assertEquals(a, this.h.generateTransformerString("/ast/control/wait_stmt1.xml"));
    }

    @Test
    public void robWaitFor1() throws Exception {
        String a =
            "BlockAST[project=[[Location[x=100,y=50],MainTask[],WaitStmt[(repeat[WAIT,Binary[EQ,SensorExpr[GetSampleSensor[sensor=BrickSensor[key=ENTER,mode=IS_PRESSED]]],BoolConst[true]]])]]]]";
        Assert.assertEquals(a.replaceAll("\\s+", ""), this.h.generateTransformerString("/ast/control/wait_stmt2.xml").replaceAll("\\s+", ""));
    }

    @Test
    public void robWaitFor2() throws Exception {
        String a =
            "BlockAST[project=[[Location[x=31,y=21],WaitStmt[(repeat[WAIT,Binary[EQ,SensorExpr[GetSampleSensor[sensor=TouchSensor[TOUCH, S1]]],BoolConst[true]]]AktionStmt[MotorOnAction[B,MotionParam[speed=NumConst[30],duration=null]]])(repeat[WAIT,Binary[LT,SensorExpr[GetSampleSensor[sensor=UltrasonicSensor[PRESENCE,S4]]],NumConst[30]]]AktionStmt[MotorOnAction[B,MotionParam[speed=NumConst[30],duration=null]]])(repeat[WAIT,Binary[GT,SensorExpr[GetSampleSensor[sensor=GyroSensor[RATE,S2]]],NumConst[90]]]AktionStmt[MotorOnAction[B,MotionParam[speed=NumConst[30],duration=null]]])(repeat[WAIT,Binary[EQ,SensorExpr[GetSampleSensor[sensor=BrickSensor[key=ENTER,mode=IS_PRESSED]]],BoolConst[true]]]AktionStmt[MotorOnAction[B,MotionParam[speed=NumConst[30],duration=null]]])(repeat[WAIT,Binary[EQ,SensorExpr[GetSampleSensor[sensor=ColorSensor[COLOUR, S3]]],ColorConst[RED]]]AktionStmt[MotorOnAction[B,MotionParam[speed=NumConst[30],duration=null]]])(repeat[WAIT,Binary[GT,SensorExpr[GetSampleSensor[sensor=GyroSensor[ANGLE, S2]]],NumConst[90]]]AktionStmt[MotorOnAction[B,MotionParam[speed=NumConst[30],duration=null]]])(repeat[WAIT,Binary[LT,SensorExpr[GetSampleSensor[sensor=InfraredSensor[DISTANCE, S4]]],NumConst[30]]]AktionStmt[MotorOnAction[B,MotionParam[speed=NumConst[30],duration=null]]])(repeat[WAIT,Binary[GT,SensorExpr[GetSampleSensor[sensor=TimerSensor[mode=GET_SAMPLE,timer=1]]],NumConst[500]]]AktionStmt[MotorOnAction[B,MotionParam[speed=NumConst[30],duration=null]]])]],[Location[x=168,y=259],MotorOnAction[B,MotionParam[speed=NumConst[30],duration=null]]]]]";
        Assert.assertEquals(a.replaceAll("\\s+", ""), this.h.generateTransformerString("/ast/control/wait_stmt3.xml").replaceAll("\\s+", ""));
    }

    @Test
    public void reverseTransformation() throws Exception {
        this.h.assertTransformationIsOk("/ast/control/wait_stmt.xml");
    }

    @Test
    public void reverseTransformationFor() throws Exception {
        this.h.assertTransformationIsOk("/ast/control/wait_stmt1.xml");
    }

    @Test
    public void reverseTransformationFor1() throws Exception {
        this.h.assertTransformationIsOk("/ast/control/wait_stmt2.xml");
    }

    @Test
    public void reverseTransformationFor2() throws Exception {
        this.h.assertTransformationIsOk("/ast/control/wait_stmt3.xml");
    }
}
