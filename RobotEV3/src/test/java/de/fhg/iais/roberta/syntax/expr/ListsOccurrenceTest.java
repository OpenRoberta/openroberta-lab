package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class ListsOccurrenceTest {
    @Test
    public void Test() throws Exception {
        String a =
            "publicvoidrun()throwsException{hal.turnOnRegulatedMotor(ActorPort.B,BlocklyMethods.findFirst(BlocklyMethods.createListWithNumber(0,0,0),30));hal.turnOnRegulatedMotor(ActorPort.B,BlocklyMethods.findLast(BlocklyMethods.createListWithNumber(0,0,0),30));}";

        Helper.assertCodeIsOk(a, "/syntax/lists/lists_occurrence.xml");
    }

}
