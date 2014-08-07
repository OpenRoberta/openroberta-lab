package de.fhg.iais.roberta.ast.syntax;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.helper.Helper;

public class JavaGenerateCodeTest {

    @Test
    public void test() throws Exception {
        String a = "import de.fhg.iais.roberta.codegen.lejos;\n" + "import de.fhg.iais.roberta.ast.syntax;\n\n"

        + "public class Test {\n" + "    private BrickConfiguration brickConfiguration = new BrickConfiguration.Builder()\n" + "    .build();\n\n"

        + "    public static void main(String[] args) {\n" + "        Test.run();\n" + "    }\n\n"

        + "    public static void run() {\n" + "        Hal hal = new Hal(brickConfiguration);\n"

        + "        hal.drawText(\"Hallo\", 0, 3);\n"

        + "    }\n" + "}";

        Assert.assertEquals(a, Helper.generateProgram("/syntax/code_generator/java_code_generator.xml"));
    }

    @Test
    public void test1() throws Exception {

        String a =
            "import de.fhg.iais.roberta.codegen.lejos;\n"
                + "import de.fhg.iais.roberta.ast.syntax;\n\n"

                + "public class Test {\n"
                + "    private BrickConfiguration brickConfiguration = new BrickConfiguration.Builder()\n"
                + "    .build();\n\n"

                + "    public static void main(String[] args) {\n"
                + "        Test.run();\n"
                + "    }\n\n"

                + "    public static void run() {\n"
                + "        Hal hal = new Hal(brickConfiguration);\n"
                + "        for ( int i = 0; i < 10; i++ ) {\n"
                + "            hal.drawText(\"Hallo\", 0, 3);\n"
                + "        }\n"
                + "    }\n"
                + "}";

        Assert.assertEquals(a, Helper.generateProgram("/syntax/code_generator/java_code_generator1.xml"));
    }

    @Test
    public void test2() throws Exception {

        String a =
            "import de.fhg.iais.roberta.codegen.lejos;\n"
                + "import de.fhg.iais.roberta.ast.syntax;\n\n"

                + "public class Test {\n"
                + "    private BrickConfiguration brickConfiguration = new BrickConfiguration.Builder()\n"
                + "    .build();\n\n"

                + "    public static void main(String[] args) {\n"
                + "        Test.run();\n"
                + "    }\n\n"

                + "    public static void run() {\n"
                + "        Hal hal = new Hal(brickConfiguration);\n"
                + "        if ( hal.isPressed(S1) ) {\n"
                + "            hal.setUltrasonicSensorMode(S4, DISTANCE);\n"
                + "            hal.ledOn(GREEN, true);\n"
                + "        } else if ( \"#585858\" == hal.getColorSensorValue(S3) ) {\n"
                + "            while ( true ) {\n"
                + "                hal.drawPicture(\"SMILEY1\", 0, 0);\n\n"

                + "            }\n"
                + "        }\n"
                + "        hal.playFile(\"SOUNDFILE1\");\n"
                + "        hal.setVolume(50);\n"
                + "        for ( int i = 1; i <= 10; i += 1 ) {\n\n"
                + "        }\n"
                + "    }\n"
                + "}";

        Assert.assertEquals(a, Helper.generateProgram("/syntax/code_generator/java_code_generator2.xml"));
    }

    @Test
    public void test3() throws Exception {

        String a =
            "import de.fhg.iais.roberta.codegen.lejos;\n"
                + "import de.fhg.iais.roberta.ast.syntax;\n\n"

                + "public class Test {\n"
                + "    private BrickConfiguration brickConfiguration = new BrickConfiguration.Builder()\n"
                + "    .build();\n\n"

                + "    public static void main(String[] args) {\n"
                + "        Test.run();\n"
                + "    }\n\n"

                + "    public static void run() {\n"
                + "        Hal hal = new Hal(brickConfiguration);\n"
                + "        if ( hal.isPressed(S1) ) {\n"
                + "            hal.ledOn(GREEN, true);\n"
                + "        } else {\n"
                + "            if ( hal.isPressed(S1) ) {\n"
                + "                hal.ledOn(GREEN, true);\n"
                + "            } else if ( 0 == hal.getUltraSonicSensorValue(S4) ) {\n"
                + "                hal.drawPicture(\"SMILEY3\", 15, 15);\n"
                + "            } else {\n"
                + "                hal.setUltrasonicSensorMode(S4, DISTANCE);\n"
                + "                while ( !hal.isPressedAndReleased(UP) ) {\n\n"

                + "                }\n"
                + "            }\n"
                + "        }\n"
                + "    }\n"
                + "}";

        Assert.assertEquals(a, Helper.generateProgram("/syntax/code_generator/java_code_generator3.xml"));
    }
}
