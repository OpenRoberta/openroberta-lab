package de.fhg.iais.roberta.ast;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class ColorTest {

    @Test
    public void test1() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=41, y=101], \nif Binary [EQ, ColorConst [WHITE], SensorExpr [ColorSensor [mode=GET_SAMPLE, port=S3]]]\n"
                + ",then\n"
                + "AktionStmt [DriveAction [FOREWARD, MotionParam [speed=NumConst [50], duration=MotorDuration [type=DISTANCE, value=NumConst [20]]]]]\n]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/colour/colour_const.xml"));
    }
}
