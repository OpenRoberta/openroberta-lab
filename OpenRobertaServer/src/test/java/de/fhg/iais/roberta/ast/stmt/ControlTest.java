package de.fhg.iais.roberta.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class ControlTest {

    @Test
    public void robWait() throws Exception {
        String a =
            "BlockAST [project=[[WaitStmt [statements=\n"
                + "(repeat [WAIT, Binary [EQ, NumConst [0], NumConst [0]]]\n"
                + "exprStmt Funct [PRINT, [StringConst [1]]]\n"
                + ")\n(repeat [WAIT, Binary [EQ, NumConst [0], NumConst [0]]]\n"
                + "exprStmt Funct [PRINT, [StringConst [2]]]\n"
                + ")]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/control/wait_stmt.xml"));
    }

    @Test
    public void robWaitFor() throws Exception {
        String a =
            "BlockAST [project=[[WaitStmt [statements=\n" + "(repeat [WAIT, Binary [EQ, SensorExpr [TouchSensor [port=S1]], BoolConst [true]]]\n" + ")]]]]";
        Assert.assertEquals(a, Helper.generateTransformerString("/ast/control/wait_stmt1.xml"));
    }

    @Test
    public void robWaitFor1() throws Exception {
        String a =
            "BlockAST [project=[[WaitStmt [statements=(repeat [WAIT, Binary [EQ, SensorExpr[TouchSensor[port=S1]],BoolConst[true]]]AktionStmt[MotorOnAction[B,MotionParam[speed=NumConst[30],duration=MotorDuration[type=ROTATIONS,value=NumConst[1]]]]])(repeat[WAIT,Binary[EQ,SensorExpr[DrehSensor[mode=GET_SAMPLE,motor=A]],NumConst[30]]]AktionStmt[DriveAction[FOREWARD,MotionParam[speed=NumConst[50],duration=MotorDuration[type=DISTANCE,value=NumConst[20]]]]])(repeat[WAIT,Binary[EQ,SensorExpr[BrickSensor[key=ENTER,mode=IS_PRESSED]],BoolConst[true]]]AktionStmt[ShowPictureAction[SMILEY1,NumConst[0],NumConst[0]]])(repeat[WAIT,Binary[EQ,SensorExpr[TimerSensor[mode=GET_SAMPLE,timer=1]],NumConst[500]]]AktionStmt[LightStatusAction[OFF]])(repeat[WAIT,Binary[EQ,SensorExpr[InfraredSensor[mode=GET_SAMPLE,port=S4]],NumConst[30]]]AktionStmt[VolumeAction[SET,NumConst[50]]]"
                + ")]]]]";
        Assert.assertEquals(a.replaceAll("\\s+", ""), Helper.generateTransformerString("/ast/control/wait_stmt2.xml").replaceAll("\\s+", ""));
    }
}
