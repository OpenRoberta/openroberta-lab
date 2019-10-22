package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.Ev3LejosAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class ListsOccurrenceTest extends Ev3LejosAstTest {

    @Test
    public void Test() throws Exception {
        String a =
            "publicvoidrun()throwsException{hal.turnOnRegulatedMotor(ActorPort.B,newArrayList<>(Arrays.asList((float)0,(float)0,(float)0)).indexOf((float)30));hal.turnOnRegulatedMotor(ActorPort.B,newArrayList<>(Arrays.asList((float)0,(float)0,(float)0)).lastIndexOf((float)30));}";

        UnitTestHelper.checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/syntax/lists/lists_occurrence.xml", makeStandard(), false);
    }

}
