package de.fhg.iais.roberta.components;

import org.junit.Assert;
import org.junit.Test;

public class CompileSourceInMemoryTest {
    //EV3Factory robotModeFactory = new EV3Factory(null);
    public static String code =
        "package generated.main;"
            + "import de.fhg.iais.roberta.runtime.*;"
            + "import de.fhg.iais.roberta.runtime.ev3.*;"
            + "import de.fhg.iais.roberta.mode.general.*;import de.fhg.iais.roberta.mode.action.ev3.*;import de.fhg.iais.roberta.mode.sensor.ev3.*;import de.fhg.iais.roberta.components.*;import java.util.LinkedHashSet;import java.util.Set;import java.util.List;import java.util.ArrayList;import java.util.Arrays;import lejos.remote.nxt.NXTConnection; "
            + "public class NEPOprog {"
            + "    private static Configuration brickConfiguration;"
            + "    private Set<UsedSensor> usedSensors = new LinkedHashSet<UsedSensor>();"
            + "    private Hal hal = new Hal(brickConfiguration, usedSensors);"
            + "    public static void main(String[] args) {"
            + "        try {"
            + "             brickConfiguration = new EV3Configuration.Builder()"
            + "                .setWheelDiameter(5.6)"
            + "                .setTrackWidth(18.0)"
            + "                .build();"
            + "           new NEPOprog().run();"
            + "    } catch ( Exception e ) {"
            + "            Hal.displayExceptionWaitForKeyPress(e);"
            + "        }"
            + "    }"
            + " "
            + " "
            + "    public void run() throws Exception {"
            + "        hal.startLogging();"
            + "        hal.driveDistance(DriveDirection.FOREWARD, 30, 20);"
            + "        hal.closeResources();"
            + "    }"
            + "}";

    @Test
    public void testParsing() throws Exception {

        JavaSourceCompiler csm = new JavaSourceCompiler("generated.main.NEPOprog", code);
        CompilerFeedback t = csm.compile();
        Assert.assertEquals(true, t.isSuccess());
    }
}