package de.fhg.iais.roberta.components;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.components.ev3lejos.JavaSourceCompiler;

public class CompileSourceInMemoryTest {

    public static String code =
        "package generated.main;\n"
            + "import de.fhg.iais.roberta.runtime.*;\n"
            + "import de.fhg.iais.roberta.runtime.ev3.*;\n"
            + "import de.fhg.iais.roberta.mode.general.*;\n"
            + "import de.fhg.iais.roberta.mode.action.*;\n"
            + "import de.fhg.iais.roberta.mode.sensor.*;\n"
            + "import de.fhg.iais.roberta.mode.action.ev3.*;\n"
            + "import de.fhg.iais.roberta.mode.sensor.ev3.*;\n"
            + "import de.fhg.iais.roberta.components.*;\n"
            + "import java.util.LinkedHashSet;\n"
            + "import java.util.Set;\n"
            + "import java.util.List;\n"
            + "import java.util.ArrayList;\n"
            + "import java.util.Arrays;\n"
            + "import lejos.remote.nxt.NXTConnection;\n "
            + "public class NEPOprog {\n"
            + "    private static Configuration brickConfiguration;\n"
            + "    private Set<UsedSensor> usedSensors = new LinkedHashSet<UsedSensor>();\n"
            + "    private Hal hal = new Hal(brickConfiguration, usedSensors);\n"
            + "    public static void main(String[] args) {\n"
            + "        try {\n"
            + "             brickConfiguration = new EV3Configuration.Builder()"
            + "                .setWheelDiameter(5.6)"
            + "                .setTrackWidth(18.0)"
            + "                .addActor(ActorPort.B, new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.RIGHT))"
            + "                .addActor(ActorPort.C, new Actor(ActorType.LARGE, true, DriveDirection.FOREWARD, MotorSide.LEFT))"
            + "                .build();\n"
            + "           new NEPOprog().run();\n"
            + "    } catch ( Exception e ) {\n"
            + "            Hal.displayExceptionWaitForKeyPress(e);\n"
            + "        }\n"
            + "    }\n"
            + "\n "
            + "\n "
            + "    public void run() throws Exception {\n"
            + "        hal.startLogging()\n"
            + "        hal.driveDistance(DriveDirection.FOREWARD, 30, 20);\n"
            + "        hal.closeResources();\n"
            + "    }\n"
            + "}";

    @Test
    public void testParsing() throws Exception {
        JavaSourceCompiler csm = new JavaSourceCompiler("NEPOprog", code, "");
        csm.compileAndPackage("a", "a");
        Assert.assertEquals(false, csm.isSuccess());
    }
}
