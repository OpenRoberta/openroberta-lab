package de.fhg.iais.roberta.syntax.sensor.nao;

import org.junit.Test;

import de.fhg.iais.roberta.syntax.NaoAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ElectricCurrentTest extends NaoAstTest {

    @Test
    public void make_ByDefault_ReturnInstanceOfLearnFaceClass() throws Exception {
        String expectedResult =
            "BlockAST [project=[[Location [x=384, y=50], "
                + "MainTask [], "
                + "SayTextAction [SensorExpr [ElectricCurrentSensor [HEAD, VALUE, YAW]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], "
                + "SayTextAction [SensorExpr [ElectricCurrentSensor [HEAD, VALUE, PITCH]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], "
                + "SayTextAction [SensorExpr [ElectricCurrentSensor [SHOULDER, VALUE, LEFT_PITCH]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], "
                + "SayTextAction [SensorExpr [ElectricCurrentSensor [SHOULDER, VALUE, LEFT_ROLL]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], "
                + "SayTextAction [SensorExpr [ElectricCurrentSensor [SHOULDER, VALUE, RIGHT_PITCH]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], "
                + "SayTextAction [SensorExpr [ElectricCurrentSensor [SHOULDER, VALUE, RIGHT_ROLL]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], "
                + "SayTextAction [SensorExpr [ElectricCurrentSensor [ELBOW, VALUE, LEFT_YAW]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], "
                + "SayTextAction [SensorExpr [ElectricCurrentSensor [ELBOW, VALUE, LEFT_ROLL]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], "
                + "SayTextAction [SensorExpr [ElectricCurrentSensor [ELBOW, VALUE, RIGHT_YAW]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], "
                + "SayTextAction [SensorExpr [ElectricCurrentSensor [ELBOW, VALUE, RIGHT_ROLL]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], "
                + "SayTextAction [SensorExpr [ElectricCurrentSensor [WRIST, VALUE, LEFT_YAW]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], "
                + "SayTextAction [SensorExpr [ElectricCurrentSensor [WRIST, VALUE, RIGHT_YAW]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], "
                + "SayTextAction [SensorExpr [ElectricCurrentSensor [HAND, VALUE, LEFT]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], "
                + "SayTextAction [SensorExpr [ElectricCurrentSensor [HAND, VALUE, RIGHT]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], "
                + "SayTextAction [SensorExpr [ElectricCurrentSensor [HIP, VALUE, LEFT_YAW_PITCH]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], "
                + "SayTextAction [SensorExpr [ElectricCurrentSensor [HIP, VALUE, LEFT_ROLL]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], "
                + "SayTextAction [SensorExpr [ElectricCurrentSensor [HIP, VALUE, LEFT_PITCH]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], "
                + "SayTextAction [SensorExpr [ElectricCurrentSensor [HIP, VALUE, RIGHT_YAW_PITCH]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], "
                + "SayTextAction [SensorExpr [ElectricCurrentSensor [HIP, VALUE, RIGHT_ROLL]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], "
                + "SayTextAction [SensorExpr [ElectricCurrentSensor [HIP, VALUE, RIGHT_PITCH]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], "
                + "SayTextAction [SensorExpr [ElectricCurrentSensor [KNEE, VALUE, LEFT_PITCH]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], "
                + "SayTextAction [SensorExpr [ElectricCurrentSensor [KNEE, VALUE, RIGHT_PITCH]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], "
                + "SayTextAction [SensorExpr [ElectricCurrentSensor [ANKLE, VALUE, LEFT_PITCH]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], "
                + "SayTextAction [SensorExpr [ElectricCurrentSensor [ANKLE, VALUE, LEFT_ROLL]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], "
                + "SayTextAction [SensorExpr [ElectricCurrentSensor [ANKLE, VALUE, RIGHT_PITCH]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]], "
                + "SayTextAction [SensorExpr [ElectricCurrentSensor [ANKLE, VALUE, RIGHT_ROLL]], EmptyExpr [defVal=NUMBER_INT], EmptyExpr [defVal=NUMBER_INT]]"
                + "]]]";

        UnitTestHelper.checkProgramAstEquality(testFactory, expectedResult, "/sensor/electriccurrent.xml");

    }

    @Test
    public void astToBlock_XMLtoJAXBtoASTtoXML_ReturnsSameXML() throws Exception {

        UnitTestHelper.checkProgramReverseTransformation(testFactory, "/sensor/electriccurrent.xml");
    }
}