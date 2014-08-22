package de.fhg.iais.roberta.ast.syntax.actors;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class TurnActionTest {

    @Test
    public void turn() throws Exception {
        String a = "\nhal.rotateDirectionRegulated(A, B, RIGHT, 50);";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/actions/action_MotorDiffTurn.xml"));
    }

    @Test
    public void turnFor() throws Exception {
        String a = "\nhal.rotateDirectionDistanceRegulated(A, B, RIGHT, 50, 20);";

        Assert.assertEquals(a, Helper.generateStringWithoutWrapping("/ast/actions/action_MotorDiffTurnFor.xml"));
    }
}