package generated.main;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.HardwareComponent;
import de.fhg.iais.roberta.codegen.lejos.Hal;

import de.fhg.iais.roberta.ast.syntax.action.*;
import de.fhg.iais.roberta.ast.syntax.sensor.*;
public class test {
    private BrickConfiguration brickConfiguration = new BrickConfiguration.Builder()
    .setWheelDiameter(5.6)
    .setTrackWidth(13.0)
    .addActor(ActorPort.B, new HardwareComponent(HardwareComponentType.EV3LargeRegulatedMotor, DriveDirection.FOREWARD, MotorSide.LEFT))
    .addActor(ActorPort.C, new HardwareComponent(HardwareComponentType.EV3LargeRegulatedMotor, DriveDirection.FOREWARD, MotorSide.RIGHT))
    .addSensor(SensorPort.S2, new HardwareComponent(HardwareComponentType.EV3UltrasonicSensor))
    .addSensor(SensorPort.S3, new HardwareComponent(HardwareComponentType.EV3ColorSensor))
    .build();

    public static void main(String[] args) {
        new test().run();
    }

    public void run() {
        Hal hal = new Hal(brickConfiguration);
        hal.drawText("Hallo", 0, 0);
        try {
            Thread.sleep(2000);
        } catch ( InterruptedException e ) {
            // ok
        }
    }
}
