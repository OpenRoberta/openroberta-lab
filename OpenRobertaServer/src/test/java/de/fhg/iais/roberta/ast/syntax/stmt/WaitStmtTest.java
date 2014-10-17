package de.fhg.iais.roberta.ast.syntax.stmt;

import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.codeGeneration.Helper;

public class WaitStmtTest {

    @Test
    public void test1() throws Exception {
        String a =
            "while ( true ) {"
                + "if ( hal.isPressed(SensorPort.S1) == true ) {"
                + "hal.rotateRegulatedMotor(ActorPort.B, 30, MotorMoveMode.ROTATIONS, 1);break;"
                + "}"
                + "if ( hal.getRegulatedMotorTachoValue(ActorPort.A) == 30 ) {"
                + "   hal.driveDistance(ActorPort.A, ActorPort.B, false, DriveDirection.FOREWARD, 50, 20);break;"
                + "}"
                + "if ( hal.isPressed(BrickKey.ENTER) == true ) {"
                + "    hal.drawPicture(ShowPicture.OLDGLASSES, 0, 0);break;"
                + "}"
                + "if ( hal.getTimerValue(1) == 500 ) {"
                + "     hal.ledOff();break;"
                + "}"
                + "if ( hal.getInfraredSensorValue(SensorPort.S4) == 30 ) {"
                + "    hal.setVolume(50);break;"
                + "}}";

        Helper.assertCodeIsOk(a, "/ast/control/wait_stmt2.xml");
    }
}