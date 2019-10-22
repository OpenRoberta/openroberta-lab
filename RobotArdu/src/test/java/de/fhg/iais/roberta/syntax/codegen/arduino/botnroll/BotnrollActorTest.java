package de.fhg.iais.roberta.syntax.codegen.arduino.botnroll;

import org.junit.Test;

import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class BotnrollActorTest extends BotnrollAstTest {

    @Test
    public void botnrollLcdTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/actions/botnroll_indication_test.ino",
                "/ast/actions/botnroll_indication_test.xml",
                makeConfiguration());
    }

    @Test
    public void botnrollMovementTest() throws Exception {
        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXml(
                testFactory,
                "/ast/actions/botnroll_movement_test.ino",
                "/ast/actions/botnroll_movement_test.xml",
                makeConfiguration());
    }
}
