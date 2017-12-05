package de.fhg.iais.roberta.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.mode.sensor.Slot;
import de.fhg.iais.roberta.mode.sensor.TouchSensorMode;
import de.fhg.iais.roberta.mode.sensor.ev3.SensorPort;
import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.lang.stmt.SensorStmt;
import de.fhg.iais.roberta.syntax.sensor.SensorMetaDataBean;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;

public class SensorStmtTest {

    @Test
    public void make() throws Exception {
        TouchSensor<Void> touchSensor =
            TouchSensor.make(
                new SensorMetaDataBean(SensorPort.S1, TouchSensorMode.TOUCH, Slot.NO_SLOT),
                BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true, false),
                null);
        SensorStmt<Void> sensorStmt = SensorStmt.make(touchSensor);

        String a = "\nSensorStmt TouchSensor [S1, TOUCH, NO_SLOT]";
        Assert.assertEquals(a, sensorStmt.toString());
    }

    @Test
    public void getSensor() throws Exception {
        TouchSensor<Void> touchSensor =
            TouchSensor.make(
                new SensorMetaDataBean(SensorPort.S1, TouchSensorMode.TOUCH, Slot.NO_SLOT),
                BlocklyBlockProperties.make("1", "1", false, false, false, false, false, true, false),
                null);
        SensorStmt<Void> sensorStmt = SensorStmt.make(touchSensor);

        Assert.assertEquals("TouchSensor [S1, TOUCH, NO_SLOT]", sensorStmt.getSensor().toString());
    }

}
