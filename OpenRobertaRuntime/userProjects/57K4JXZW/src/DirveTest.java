package generated.main;

import de.fhg.iais.roberta.ast.syntax.*;
import de.fhg.iais.roberta.codegen.lejos.Hal;

import de.fhg.iais.roberta.ast.syntax.action.*;
import de.fhg.iais.roberta.ast.syntax.sensor.*;
public class DirveTest {
    private EV3BrickConfiguration brickConfiguration = new EV3BrickConfiguration.Builder()
    .setWheelDiameter(5.6)
    .setTrackWidth(13.0)
    .addActor(ActorPort.B, new EV3Actor(HardwareComponentType.EV3LargeRegulatedMotor, DriveDirection.FOREWARD, MotorSide.LEFT))
    .addActor(ActorPort.C, new EV3Actor(HardwareComponentType.EV3LargeRegulatedMotor, DriveDirection.FOREWARD, MotorSide.RIGHT))
    .addSensor(SensorPort.S1, new EV3Sensor(HardwareComponentType.EV3TouchSensor))
    .addSensor(SensorPort.S4, new EV3Sensor(HardwareComponentType.EV3UltrasonicSensor))
    .build();

    public static void main(String[] args) {
        new DirveTest().run();
    }

    public void run() {
        Hal hal = new Hal(brickConfiguration);
        hal.drawText("Hallo", 0, 0);
        while ( true ) {
            if ( hal.isPressed(SensorPort.S1) == true ) {
                break;
            }
        }
        hal.driveDistance(ActorPort.B, ActorPort.C, false, DriveDirection.FOREWARD, 50, 50);
        hal.driveDistance(ActorPort.B, ActorPort.C, false, DriveDirection.FOREWARD, 200, 50);
        try {
            Thread.sleep(2000);
        } catch ( InterruptedException e ) {
            // ok
        }
    }
}
