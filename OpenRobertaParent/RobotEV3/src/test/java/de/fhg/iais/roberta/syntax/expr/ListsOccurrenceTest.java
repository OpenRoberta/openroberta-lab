package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.RobertaProperties;
import de.fhg.iais.roberta.util.Util1;
import de.fhg.iais.roberta.util.test.ev3.HelperEv3ForTest;

public class ListsOccurrenceTest {
    HelperEv3ForTest h = new HelperEv3ForTest(new RobertaProperties(Util1.loadProperties(null)));

    @Test
    public void Test() throws Exception {
        String a =
            "publicvoidrun()throwsException{hal.turnOnRegulatedMotor(ActorPort.B,BlocklyMethods.findFirst(BlocklyMethods.createListWithNumber(0,0,0),(float)30));hal.turnOnRegulatedMotor(ActorPort.B,BlocklyMethods.findLast(BlocklyMethods.createListWithNumber(0,0,0),(float)30));}";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_occurrence.xml");
    }

}
