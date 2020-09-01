package de.fhg.iais.roberta.ast.sensor;

import org.junit.Test;

import de.fhg.iais.roberta.ast.AstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class EnvironmentalSensorTest extends AstTest {

    @Test
    public void environmentalSensorJaxbToAstTransformation() throws Exception {
        String a = "BlockAST[project=[[Location[x=38,y=238],"
                   + "EnvironmentalSensor[E,TEMPERATURE,EMPTY_SLOT],"
                   + "EnvironmentalSensor[E,HUMIDITY,EMPTY_SLOT],"
                   + "EnvironmentalSensor[E,PRESSURE,EMPTY_SLOT],"
                   + "EnvironmentalSensor[E,IAQ,EMPTY_SLOT],"
                   + "EnvironmentalSensor[E,CALIBRATION,EMPTY_SLOT],"
                   + "EnvironmentalSensor[E,CO2EQUIVALENT,EMPTY_SLOT],"
                   + "EnvironmentalSensor[E,VOCEQUIVALENT,EMPTY_SLOT]"
                   + "]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, a, "/ast/sensors/sensor_environmental.xml");
    }

    @Test
    public void reverseTransformation() throws Exception {
        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/ast/sensors/sensor_environmental.xml");
    }

}
