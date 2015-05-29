package de.fhg.iais.roberta.ast.syntax.codeGeneration;

import junit.framework.Assert;

import org.junit.Test;

import de.fhg.iais.roberta.testutil.Helper;

public class AstToTextlyVisitorTest {

    @Test
    public void test() throws Exception {
        String a = "{\n" //
            + "Display.drawText(\"Hallo\", 0, 3);\n"
            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java_code_generator.xml");
    }

    @Test
    public void test1() throws Exception {

        String a = "{\n" //
            + "   for ( int i0 = 0; i0 < 10; i0++ ) {\n"
            + "      Display.drawText(\"Hallo\", 0, 3);\n"
            + "   }\n"
            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java_code_generator1.xml");
    }

    @Test
    public void test2() throws Exception {

        String a = "{\n" //
            + "   if ( Bumper.isPressed(S1) ) {\n"
            + "      LED.on(GREEN, ON);\n"
            + "   } else if (0 == ColorSensor.getValue(S3, COLOUR) ) {\n"
            + "      while ( true ) {\n"
            + "         Display.drawPicture(EYESOPEN, 0, 0);\n"
            + "         Motor.on(B,30);\n"
            + "      }\n"
            + "   }\n"
            + "   Sound.playFile(1);\n"
            + "   Sound.setVolume(50);\n"
            + "   for ( int i = 1; i <= 10; i += 1 ) {\n\n"
            + "      Motor.on(B,30,ROTATIONS,1);"
            + "   }\n"
            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java_code_generator2.xml");
    }

    @Test
    public void test3() throws Exception {

        String a = "{\n" //
            + "        if ( Bumper.isPressed(S1) ) {\n"
            + "            LED.on(GREEN, ON);\n"
            + "        } else {\n"
            + "            if ( Bumper.isPressed(S1) ) {\n"
            + "                LED.on(GREEN, ON);\n"
            + "            } else if (0 == UltraSonicSensor.getValue(S4, DISTANCE) ) {\n"
            + "                Display.drawPicture(FLOWERS, 15, 15);\n"
            + "            } else {\n"
            + "                while ( !Button.isPressed(UP) ) {\n\n"
            + "                     Motor.on(B,30);"
            + "                }\n"
            + "            }\n"
            + "        }\n"
            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java_code_generator3.xml");
    }

    @Test
    public void test4() throws Exception {

        String a = "{\n" //
            + "        if ( 5 < Motor.getPower(B) ) {\n\n\n"
            + "            Motor.on(B,30);\n"
            + "            Motor.on(B,30,ROTATIONS,1);\n"
            + "            Motor.rotateDirection(RIGHT, 50);\n"
            + "        }\n"
            + "        if ( Motor.getTachoValue(A, ROTATION) + InfraredSensor.getValue(S4, DISTANCE) == UltraSonicSensor.getValue(S4, DISTANCE) ) {\n"
            + "            LED.off();\n"
            + "        } else {\n"
            + "            GyroSensor.reset(S2);\n"
            + "            while ( Bumper.isPressed(S1) ) {\n"
            + "                Display.drawPicture(OLDGLASSES, 0, 0);\n"
            + "                Display.clear();\n"
            + "            }\n"
            + "            LED.on(GREEN, ON);\n"
            + "        }\n"
            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java_code_generator4.xml");
    }

    public void test5() throws Exception {

        String a = "{\n" //
            + "        if ( 5 < Motor.getSpeed(B) ) {\n\n\n"
            + "            Motor.rotateDirection(A, B, RIGHT, 50);\n"
            + "        }\n"
            + "        if ( Motor.getTachoValue(A) + InfraredSensor.getValue(S4) == UltraSonicSensor.getValue(S4) ) {\n"
            + "            InfraredSensor.getMode(S4, SEEK);\n"
            + "            LED.off();\n"
            + "        } else {\n"
            + "            GyroSensor.reset(S2);\n"
            + "            while ( Bumper.isPressed(S1) ) {\n"
            + "                Display.drawPicture(\"SMILEY1\", 0, 0);\n"
            + "                Display.clearDisplay();\n"
            + "            }\n"
            + "            LED.on(GREEN, ON);\n"
            + "        }\n"
            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java_code_generator5.xml");
    }

    @Test
    public void test6() throws Exception {

        String a = "{\n" //
            + "        Display.drawText(\"Hallo\", 0, 0);\n"
            + "        Sound.playTone(300, 3000);\n"
            + "}\n";

        assertCodeIsOk(a, "/syntax/code_generator/java_code_generator6.xml");
    }

    private void assertCodeIsOk(String a, String pathToProgramXml) throws Exception {
        String code = Helper.generateString(pathToProgramXml);
        // System.out.println(code); // only needed for EXTREME debugging
        Assert.assertEquals(a.replaceAll("\\s+", ""), code.replaceAll("\\s+", ""));
    }
}