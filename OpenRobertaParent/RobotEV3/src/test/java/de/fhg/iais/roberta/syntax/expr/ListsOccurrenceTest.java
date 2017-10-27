package de.fhg.iais.roberta.syntax.expr;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.ev3.Helper;

public class ListsOccurrenceTest {
    Helper h = new Helper();

    @Test
    public void Test() throws Exception {
        String a =
            "publicvoidrun()throwsException{hal.turnOnRegulatedMotor(ActorPort.B,BlocklyMethods.findFirst(BlocklyMethods.createListWithNumber(0,0,0),30));hal.turnOnRegulatedMotor(ActorPort.B,BlocklyMethods.findLast(BlocklyMethods.createListWithNumber(0,0,0),30));}";

        this.h.assertCodeIsOk(a, "/syntax/lists/lists_occurrence.xml");
    }

}
