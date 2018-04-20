package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForXmlTest;

public class SensorTest {
    private final HelperBotNrollForXmlTest h = new HelperBotNrollForXmlTest();

    @Test
    public void sensorReset() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-96, y=73], \n"
                + "if SensorExpr [TouchSensor [S1, DEFAULT, NO_SLOT]]\n"
                + ",then\n"
                + "SensorStmt EncoderSensor [A, RESET, NO_SLOT]\n"
                + "SensorStmt GyroSensor [S2, RESET, NO_SLOT]\n"
                + "SensorStmt TimerSensor [S1, RESET, NO_SLOT]\n"
                + "]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/sensors/sensor_reset.xml"));
    }

    @Test
    public void sensorGetSample() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-96, y=73], \n"
                + "if SensorExpr [TouchSensor [S1, DEFAULT, NO_SLOT]]\n"
                + ",then\n"
                + "Var [item] := SensorExpr [UltrasonicSensor [S4, DISTANCE, NO_SLOT]]\n\n"
                + "Var [item] := SensorExpr [ColorSensor [S3, LIGHT, NO_SLOT]]\n\n"
                + "Var [item] := SensorExpr [InfraredSensor [S4, OBSTACLE, NO_SLOT]]\n\n"
                + "Var [item] := SensorExpr [EncoderSensor [A, ROTATION, NO_SLOT]]\n\n"
                + "Var [item] := SensorExpr [GyroSensor [S2, ANGLE, NO_SLOT]]\n\n"
                + "Var [item] := SensorExpr [TimerSensor [S1, DEFAULT, NO_SLOT]]\n\n"
                + "]]]";

        Assert.assertEquals(a, this.h.generateTransformerString("/ast/sensors/sensor_getSample.xml"));
    }

}
