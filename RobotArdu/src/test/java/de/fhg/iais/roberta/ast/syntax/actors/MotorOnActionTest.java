package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.codegen.arduino.botnroll.BotnrollAstTest;
import de.fhg.iais.roberta.util.test.UnitTestHelper;

public class MotorOnActionTest extends BotnrollAstTest {

    @Ignore // TODO not implemented yet
    @Test
    public void motorOn() throws Exception {
        String a = "one.move1mPID(B,30);one.move1mPID(C,50);";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/actions/action_MotorOn.xml", makeConfiguration(), false);
    }

    @Test
    public void motorOnFor() throws Exception {
        String a = "one.servo1(30);";

        UnitTestHelper
            .checkGeneratedSourceEqualityWithProgramXmlAndSourceAsString(testFactory, a, "/ast/actions/action_MotorOnFor.xml", makeConfiguration(), false);
    }
}