package de.fhg.iais.roberta.ast.sensor;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class SensorTest {

    @Test
    public void sensorSet() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-96, y=73], \n"
                + "if SensorExpr [TouchSensor [port=S1]]\n"
                + ",then\n"
                + "SensorStmt UltraSSensor [mode=DISTANCE, port=S4]\n"
                + "SensorStmt ColorSensor [mode=COLOUR, port=S3]\n"
                + "SensorStmt InfraredSensor [mode=DISTANCE, port=S4]\n"
                + "SensorStmt DrehSensor [mode=ROTATION, motor=A]\n"
                + "SensorStmt GyroSensor [mode=ANGLE, port=S2]\n"
                + "]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_set.xml"));
    }

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
    public void sensorGetMode() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-96, y=73], \n"
                + "if SensorExpr [TouchSensor [port=S1]]\n"
                + ",then\n"
                + "Var [item] := SensorExpr [UltraSSensor [mode=GET_MODE, port=S4]]\n\n"
                + "Var [item] := SensorExpr [ColorSensor [mode=GET_MODE, port=S3]]\n\n"
                + "Var [item] := SensorExpr [InfraredSensor [mode=GET_MODE, port=S4]]\n\n"
                + "Var [item] := SensorExpr [DrehSensor [mode=GET_MODE, motor=A]]\n\n"
                + "Var [item] := SensorExpr [GyroSensor [mode=GET_MODE, port=S2]]\n\n"
                + "]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_getMode.xml"));
    }

    @Test
    public void sensorGetSample() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-96, y=73], \n"
                + "if SensorExpr [TouchSensor [port=S1]]\n"
                + ",then\n"
                + "Var [item] := SensorExpr [UltraSSensor [mode=GET_SAMPLE, port=S4]]\n\n"
                + "Var [item] := SensorExpr [ColorSensor [mode=GET_SAMPLE, port=S3]]\n\n"
                + "Var [item] := SensorExpr [InfraredSensor [mode=GET_SAMPLE, port=S4]]\n\n"
                + "Var [item] := SensorExpr [DrehSensor [mode=GET_SAMPLE, motor=A]]\n\n"
                + "Var [item] := SensorExpr [GyroSensor [mode=GET_SAMPLE, port=S2]]\n\n"
                + "Var [item] := SensorExpr [TimerSensor [mode=GET_SAMPLE, timer=1]]\n\n"
                + "]]]";

        Assert.assertEquals(a, Helper.generateTransformerString("/ast/sensors/sensor_getSample.xml"));
    }

}
