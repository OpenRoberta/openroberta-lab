package de.fhg.iais.roberta.syntax.sensor;

import java.util.Collections;

import org.junit.BeforeClass;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class GyroSensorTest extends AstTest {
    @BeforeClass
    public static void setupFactory() {
        testFactory = Util.configureRobotPlugin("calliope2017", "", "", Collections.emptyList());
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfAccelerometerSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=88, y=38], "
                + "MainTask [], "
                + "DisplayTextAction [TEXT, SensorExpr [GyroSensor [X, ANGLE, NO_SLOT]]], "
                + "DisplayTextAction [TEXT, SensorExpr [GyroSensor [Y, ANGLE, NO_SLOT]]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/sensor/gyro_sensor.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/sensor/gyro_sensor.xml");
    }

}
