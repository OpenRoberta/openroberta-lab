package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForXmlTest;

public class ListsOccurrenceTest {
    private final HelperEv3ForXmlTest h = new HelperEv3ForXmlTest();

    @Test
    public void Test() throws Exception {
        String a =
            "publicvoidrun()throwsException{hal.turnOnRegulatedMotor(ActorPort.B,newArrayList<>(Arrays.asList((float)0,(float)0,(float)0)).indexOf((float)30));hal.turnOnRegulatedMotor(ActorPort.B,newArrayList<>(Arrays.asList((float)0,(float)0,(float)0)).lastIndexOf((float)30));}";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_occurrence.xml");
    }

}
