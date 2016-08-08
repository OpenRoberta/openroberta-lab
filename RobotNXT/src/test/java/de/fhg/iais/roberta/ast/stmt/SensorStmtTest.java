package de.fhg.iais.roberta.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.mode.sensor.nxt.SensorPort;
import de.fhg.iais.roberta.mode.sensor.nxt.TouchSensorMode;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;
import de.fhg.iais.roberta.syntax.stmt.SensorStmt;

public class SensorStmtTest {

    @Test
    public void make() throws Exception {
        TouchSensor<Void> touchSensor =
            TouchSensor
                .make(TouchSensorMode.TOUCH, SensorPort.IN_1, BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true, false), null);
        SensorStmt<Void> sensorStmt = SensorStmt.make(touchSensor);

        String a = "\nSensorStmt TouchSensor [port=IN_1]";
        Assert.assertEquals(a, sensorStmt.toString());
    }

    @Test
    public void getSensor() throws Exception {
        TouchSensor<Void> touchSensor =
            TouchSensor
                .make(TouchSensorMode.TOUCH, SensorPort.IN_1, BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true, false), null);
        SensorStmt<Void> sensorStmt = SensorStmt.make(touchSensor);

        Assert.assertEquals("TouchSensor [port=IN_1]", sensorStmt.getSensor().toString());
    }

}
