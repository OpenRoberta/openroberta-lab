package de.fhg.iais.roberta.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.shared.sensor.ev3.SensorPort;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.sensor.ev3.TouchSensor;
import de.fhg.iais.roberta.syntax.stmt.SensorStmt;

public class SensorStmtTest {

    @Test
    public void make() throws Exception {
        TouchSensor<Void> touchSensor = TouchSensor.make(SensorPort.S1, BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true), null);
        SensorStmt<Void> sensorStmt = SensorStmt.make(touchSensor);

        String a = "\nSensorStmt TouchSensor [port=S1]";
        Assert.assertEquals(a, sensorStmt.toString());
    }

    @Test
    public void getSensor() throws Exception {
        TouchSensor<Void> touchSensor = TouchSensor.make(SensorPort.S1, BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true), null);
        SensorStmt<Void> sensorStmt = SensorStmt.make(touchSensor);

        Assert.assertEquals("TouchSensor [port=S1]", sensorStmt.getSensor().toString());
    }

}
