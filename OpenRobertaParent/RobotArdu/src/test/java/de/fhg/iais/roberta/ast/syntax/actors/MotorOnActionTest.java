package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Ignore;
import org.junit.Test;

import de.fhg.iais.roberta.util.test.ardu.HelperBotNrollForXmlTest;

public class MotorOnActionTest {
    private final HelperBotNrollForXmlTest h = new HelperBotNrollForXmlTest();

    @Ignore // not implemented yet
    @Test
    public void motorOn() throws Exception {
        String a = "one.move1mPID(B,30);one.move1mPID(C,50);";

        this.h.assertCodeIsOk(a, "/ast/actions/action_MotorOn.xml", false);
    }

    @Test
    public void motorOnFor() throws Exception {
        String a = "one.servo1(30);";

        this.h.assertCodeIsOk(a, "/ast/actions/action_MotorOnFor.xml", false);
    }
}