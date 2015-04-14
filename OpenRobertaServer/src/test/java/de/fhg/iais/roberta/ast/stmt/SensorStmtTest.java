package de.fhg.iais.roberta.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.ast.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.ast.syntax.sensor.SensorPort;
import de.fhg.iais.roberta.ast.syntax.sensor.TouchSensor;
import de.fhg.iais.roberta.ast.syntax.stmt.SensorStmt;

public class SensorStmtTest {

    @Test
    public void make() throws Exception {
        TouchSensor<Void> touchSensor = TouchSensor.make(SensorPort.S1, BlocklyBlockProperties.make("1", "1", false, false, false, false, false), null);
        SensorStmt<Void> sensorStmt = SensorStmt.make(touchSensor);

        String a = "\nSensorStmt TouchSensor [port=S1]";
        Assert.assertEquals(a, sensorStmt.toString());
    }

    @Test
    public void getSensor() throws Exception {
        TouchSensor<Void> touchSensor = TouchSensor.make(SensorPort.S1, BlocklyBlockProperties.make("1", "1", false, false, false, false, false), null);
        SensorStmt<Void> sensorStmt = SensorStmt.make(touchSensor);

        Assert.assertEquals("TouchSensor [port=S1]", sensorStmt.getSensor().toString());
    }

}
