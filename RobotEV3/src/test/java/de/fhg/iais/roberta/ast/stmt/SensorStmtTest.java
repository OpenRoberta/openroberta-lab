package de.fhg.iais.roberta.ast.stmt;

import org.junit.Assert;
import org.junit.Test;

import de.fhg.iais.roberta.syntax.BlocklyBlockProperties;
import de.fhg.iais.roberta.syntax.SC;
import de.fhg.iais.roberta.syntax.lang.stmt.SensorStmt;
import de.fhg.iais.roberta.syntax.sensor.SensorMetaDataBean;
import de.fhg.iais.roberta.syntax.sensor.generic.TouchSensor;

public class SensorStmtTest {
    @Test
    public void make() throws Exception {
        TouchSensor<Void> touchSensor =
            TouchSensor.make(new SensorMetaDataBean("1", SC.TOUCH, "EMPTY_SLOT", false), BlocklyBlockProperties.make("1", "1"), null);
        SensorStmt<Void> sensorStmt = SensorStmt.make(touchSensor);

        String a = "\nSensorStmt TouchSensor [1, TOUCH, EMPTY_SLOT]";
        Assert.assertEquals(a, sensorStmt.toString());
    }

    @Test
    public void getSensor() throws Exception {
        TouchSensor<Void> touchSensor =
            TouchSensor.make(new SensorMetaDataBean("1", SC.TOUCH, "EMPTY_SLOT", false), BlocklyBlockProperties.make("1", "1"), null);
        SensorStmt<Void> sensorStmt = SensorStmt.make(touchSensor);

        Assert.assertEquals("TouchSensor [1, TOUCH, EMPTY_SLOT]", sensorStmt.getSensor().toString());
    }

}
