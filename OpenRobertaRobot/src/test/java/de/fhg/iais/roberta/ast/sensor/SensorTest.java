package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class SensorTest {

    @Test
    public void sensorReset() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-96, y=73], \n"
                + "if SensorExpr [TouchSensor [port=S1]]\n"
                + ",then\n"
                + "SensorStmt DrehSensor [mode=RESET, motor=A]\n"
                + "SensorStmt GyroSensor [mode=RESET, port=S2]\n"
                + "SensorStmt TimerSensor [mode=RESET, timer=1]\n"
                + "]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_reset.xml"));
    }

    @Test
    public void sensorGetSample() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-96, y=73], \n"
                + "if SensorExpr [TouchSensor [port=S1]]\n"
                + ",then\n"
                + "Var [item] := SensorExpr [UltraSSensor [mode=DISTANCE, port=S4]]\n\n"
                + "Var [item] := SensorExpr [ColorSensor [mode=RGB, port=S3]]\n\n"
                + "Var [item] := SensorExpr [InfraredSensor [mode=DISTANCE, port=S4]]\n\n"
                + "Var [item] := SensorExpr [DrehSensor [mode=ROTATION, motor=A]]\n\n"
                + "Var [item] := SensorExpr [GyroSensor [mode=ANGLE, port=S2]]\n\n"
                + "Var [item] := SensorExpr [TimerSensor [mode=GET_SAMPLE, timer=1]]\n\n"
                + "]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_getSample.xml"));
    }

}
