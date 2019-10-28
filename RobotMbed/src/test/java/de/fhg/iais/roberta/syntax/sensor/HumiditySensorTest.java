package de.fhg.iais.roberta.syntax.sensor;

import java.util.Collections;

import org.junit.BeforeClass;
import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.Util;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class HumiditySensorTest extends AstTest {
    @BeforeClass
    public static void setupFactory() {
        testFactory = Util.configureRobotPlugin("calliope2017", "", "", Collections.emptyList());
    }

    @Test
    public void make_ByDefault_ReturnInstanceOfTemperatureSensorClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=384, y=50], "
                + "MainTask [], "
                + "DisplayTextAction [TEXT, SensorExpr [HumiditySensor [5, HUMIDITY, EMPTY_SLOT]]], "
                + "DisplayTextAction [TEXT, SensorExpr [HumiditySensor [5, TEMPERATURE, EMPTY_SLOT]]]]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/sensor/humidity_sensor.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/sensor/humidity_sensor.xml");
    }
}
