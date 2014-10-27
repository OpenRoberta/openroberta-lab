package generated.main;

import de.fhg.iais.roberta.ast.syntax.BrickConfiguration;
import de.fhg.iais.roberta.ast.syntax.HardwareComponent;
import de.fhg.iais.roberta.codegen.lejos.Hal;

import de.fhg.iais.roberta.ast.syntax.action.*;
import de.fhg.iais.roberta.ast.syntax.sensor.*;
public class AnyKey {
    private BrickConfiguration brickConfiguration = new BrickConfiguration.Builder()
    .setWheelDiameter(5.6)
    .setTrackWidth(13.0)
    .addActor(ActorPort.B, new HardwareComponent(HardwareComponentType.EV3LargeRegulatedMotor, DriveDirection.FOREWARD, MotorSide.RIGHT))
    .addActor(ActorPort.C, new HardwareComponent(HardwareComponentType.EV3LargeRegulatedMotor, DriveDirection.FOREWARD, MotorSide.LEFT))
    .addSensor(SensorPort.S1, new HardwareComponent(HardwareComponentType.EV3TouchSensor))
    .build();

    public static void main(String[] args) {
        new AnyKey().run();
    }

    public void run() {
        Hal hal = new Hal(brickConfiguration);
        hal.drawText("Hallo", 0, 0);
        while ( true ) {
            if ( hal.isPressed(SensorPort.S1) == true ) {
                break;
            }
        }
        hal.driveDistance(ActorPort.C, ActorPort.B, false, DriveDirection.FOREWARD, 50, 100);
        hal.rotateDirectionAngle(ActorPort.C, ActorPort.B, false, TurnDirection.LEFT, 50, 180);
        hal.driveDistance(ActorPort.C, ActorPort.B, false, DriveDirection.BACKWARD, 50, 50);
        try {
            Thread.sleep(2000);
        } catch ( InterruptedException e ) {
            // ok
        }
    }
}
