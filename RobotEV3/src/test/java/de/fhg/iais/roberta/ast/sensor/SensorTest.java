package de.fhg.iais.roberta.ast.sensor;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class SensorTest extends AstTest {

    @Test
    public void sensorReset() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-96, y=73], \n"
                + "if SensorExpr [TouchSensor [1, DEFAULT, NO_SLOT]]\n"
                + ",then\n"
                + "SensorStmt EncoderSensor [A, RESET, NO_SLOT]\n"
                + "SensorStmt GyroSensor [2, RESET, NO_SLOT]\n"
                + "SensorStmt TimerSensor [1, RESET, NO_SLOT]\n"
                + "]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/sensors/sensor_reset.xml");
    }

    @Test
    public void sensorGetSample() throws Exception {
        String a =
            "BlockAST [project=[[Location [x=-96, y=73], \n"
                + "if SensorExpr [TouchSensor [1, DEFAULT, NO_SLOT]]\n"
                + ",then\n"
                + "Var [item] := SensorExpr [UltrasonicSensor [4, DISTANCE, NO_SLOT]]\n\n"
                + "Var [item] := SensorExpr [ColorSensor [3, RGB, NO_SLOT]]\n\n"
                + "Var [item] := SensorExpr [InfraredSensor [4, DISTANCE, NO_SLOT]]\n\n"
                + "Var [item] := SensorExpr [EncoderSensor [A, ROTATION, NO_SLOT]]\n\n"
                + "Var [item] := SensorExpr [GyroSensor [2, ANGLE, NO_SLOT]]\n\n"
                + "Var [item] := SensorExpr [TimerSensor [1, DEFAULT, NO_SLOT]]\n\n"
                + "]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/sensors/sensor_getSample.xml");
    }

}
